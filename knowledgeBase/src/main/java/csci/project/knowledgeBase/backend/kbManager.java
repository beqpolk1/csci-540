package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import csci.project.knowledgeBase.requests.DeleteRequest;
import csci.project.knowledgeBase.requests.GearQueryRequest;
import csci.project.knowledgeBase.requests.SearchRequest;
import csci.project.knowledgeBase.utils.IdGen;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KbManager {
    private DiskHead disk;
    private String rootDir;
    private Map<String, JsonObject> metaInfo;
    private JsonObject blockMap;
    private HashMap<Integer, Transaction> transactions;
    private IdGen transIds;

    public KbManager(String newKb) {
        transIds = new IdGen(1000);
        transactions = new HashMap<>();
        disk = new DiskHead();
        rootDir = "knowledge_base/" + newKb + "/";

        metaInfo = new HashMap<>();
        importMetaInfo();

        blockMap = new JsonParser().parse(openReader(rootDir + "meta/blockMap.meta")).getAsJsonObject();
    }

    //basic linear scan
    public JsonArray doSearch(SearchRequest search, JsonArray factFilter, Integer transactionId) {
        JsonArray ret = new JsonArray();

        JsonObject entMeta = metaInfo.get(search.getEntity());

        ExternalMem curBlock = getBlock(entMeta.get("first_block").getAsString());
        while (curBlock != null) {
            Collection<JsonObject> blockContents = DataReader.readMem(curBlock, disk);

            for (JsonObject curObj : blockContents) {
                if (factFilter != null && !factFilter.contains(curObj.get("sysId"))) continue;
                if (CriteriaChecker.matchesCriteria(curObj.getAsJsonObject("object"), search.getCriteria())) {
                    ret.add(curObj.getAsJsonObject("object"));
                    if (transactions.containsKey(transactionId)) transactions.get(transactionId).addTouchedFact(curObj.get("sysId").getAsInt());
                }
            }

            curBlock = getBlock(curBlock.getNext());
        }

        return ret;
    }

    //inference, knowledge look-up
    public JsonObject doQuery(GearQueryRequest query, boolean injectTest) {
        return QueryEvaluator.doQuery(query, this, injectTest);
    }

    public JsonObject doDelete(DeleteRequest delete) {
        JsonObject entMeta = metaInfo.get(delete.getEntity());
        int delCount = 0;
        JsonArray availFacts = getAvailFacts();

        ExternalMem curBlock = getBlock(entMeta.get("first_block").getAsString());
        while (curBlock != null) {
            Collection<JsonObject> blockContents = DataReader.readMem(curBlock, disk);

            for (JsonObject curObj : blockContents) {
                if (CriteriaChecker.matchesCriteria(curObj.getAsJsonObject("object"), delete.getCriteria())) {
                    availFacts.remove(curObj.getAsJsonPrimitive("sysId"));
                    writeAvailFacts(availFacts);

                    if (DataWriter.deleteItem(curBlock, curObj.get("sysId").getAsInt(), disk)) delCount++;
                }
            }

            curBlock = getBlock(curBlock.getNext());
        }

        JsonObject ret = new JsonObject();
        ret.addProperty("status", "success");
        ret.addProperty("count", delCount);
        return ret;
    }

    public JsonArray getAvailFacts() {
        return new JsonParser().parse(openReader(rootDir + "meta/availFacts.meta")).getAsJsonArray();
    }

    public Transaction openTransaction() {
        Transaction newTrans = new Transaction(transIds.getId());
        newTrans.resetTouchedFacts();
        transactions.put(newTrans.getId(), newTrans);
        return newTrans;
    }

    public void closeTransaction(Integer transactionId) {
        transactions.remove(transactionId);
    }

    private ExternalMem getBlock(String logiAddr) {
        if (logiAddr == null) return null;

        JsonObject theBlock = blockMap.get(logiAddr).getAsJsonObject();

        return new Block(logiAddr,
                rootDir + theBlock.get("physAddr").getAsString(),
                theBlock.get("prev").isJsonNull() ? null : theBlock.get("prev").getAsString(),
                theBlock.get("next").isJsonNull()  ? null : theBlock.get("next").getAsString()
        );
    }

    private void importMetaInfo() {
        JsonParser parser = new JsonParser();
        JsonArray allEntities = parser.parse(openReader(rootDir + "meta/allEntities.meta")).getAsJsonObject().get("entities").getAsJsonArray();

        for (int i = 0; i <= allEntities.size() - 1; i++) {
            String entityName = allEntities.get(i).getAsString();
            JsonObject curEntity = parser.parse(openReader(rootDir + "meta/" + entityName + ".meta")).getAsJsonObject();
            metaInfo.put(entityName, curEntity);
        }
    }

    private Reader openReader(String theFile) {
        Reader fileReader = null;
        try {
            fileReader = new FileReader(theFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileReader;
    }

    private void writeAvailFacts(JsonArray facts) {
        String fileName = "availFacts.meta";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rootDir+ "meta/" + fileName));
            writer.write("[" + System.lineSeparator());

            for (int i = 0; i <= facts.size() - 1; i++) {
                writer.write(facts.get(i).getAsInt() + (i < facts.size() - 1 ? "," : "") + System.lineSeparator());
            }

            writer.write("]");
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed opening/closing output file!");
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------
    //HELPER SEARCHES
    //----------------------------------------------------------------------------------
    JsonObject getEntityByType(String entity, String typeName, Integer transactionId) {
        return getEntityByType(entity, typeName, null, transactionId);
    }

    JsonObject getEntityByType(String entity, String typeName, JsonArray factFilter, Integer transactionId) {
        SearchRequest search = new SearchRequest(entity);
        search.addCriteria(
            (checkObj) -> {
                String typeFilter = typeName;
                String checkVal = checkObj.get("type").getAsString();
                if (checkVal.equals(typeFilter) && checkObj.get("name").isJsonNull()) return true;
                else return false;
            }
        );

        return doSearch(search, factFilter, transactionId).get(0).getAsJsonObject();
    }

    JsonObject getEntityById(String entity, Number id, Integer transactionId) {
        return getEntityById(entity, id, null, transactionId);
    }

    JsonObject getEntityById(String entity, Number id, JsonArray factFilter, Integer transactionId) {
        SearchRequest search = new SearchRequest(entity);
        search.addCriteria(
            (checkObj) -> {
                Number checkVal = checkObj.get("id").getAsInt();
                if (checkVal.equals(id)) return true;
                else return false;
            }
        );

        return doSearch(search, factFilter, transactionId).get(0).getAsJsonObject();
    }

    JsonObject getEntityByName(String entity, String name, Integer transactionId) {
        return getEntityByName(entity, name, null, transactionId);
    }

    JsonObject getEntityByName(String entity, String name, JsonArray factFilter, Integer transactionId) {
        SearchRequest search = new SearchRequest(entity);
        search.addCriteria(
            (checkObj) -> {
                if (checkObj.get("name").isJsonNull()) return false;
                String checkVal = checkObj.get("name").getAsString();
                if (checkVal.equals(name)) return true;
                else return false;
            }
        );

        return doSearch(search, factFilter, transactionId).get(0).getAsJsonObject();
    }
}
