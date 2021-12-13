package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import csci.project.knowledgeBase.requests.SearchRequest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KbManager {
    private DiskHead disk;
    private String rootDir;
    private Map<String, JsonObject> metaInfo;
    private JsonObject blockMap;

    public KbManager(String newKb) {
        disk = new DiskHead();
        rootDir = "knowledge_base\\" + newKb + "\\";

        metaInfo = new HashMap<>();
        importMetaInfo();

        blockMap = new JsonParser().parse(openReader(rootDir + "meta\\blockMap.meta")).getAsJsonObject();
    }

    //basic linear scan
    public JsonArray doSearch(SearchRequest search) {
        JsonArray ret = new JsonArray();

        JsonObject entMeta = metaInfo.get(search.getEntity());

        ExternalMem curBlock = getBlock(entMeta.get("first_block").getAsString());
        while (curBlock != null) {
            Collection<JsonObject> blockContents = DataReader.readMem(curBlock, disk);

            for (JsonObject curObj : blockContents) {
                if (CriteriaChecker.matchesCriteria(curObj, search.getCriteria())) ret.add(curObj);
            }

            curBlock = getBlock(curBlock.getNext());
        }

        return ret;
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
        JsonArray allEntities = parser.parse(openReader(rootDir + "meta\\allEntities.meta")).getAsJsonObject().get("entities").getAsJsonArray();

        for (int i = 0; i <= allEntities.size() - 1; i++) {
            String entityName = allEntities.get(i).getAsString();
            JsonObject curEntity = parser.parse(openReader(rootDir + "meta\\" + entityName + ".meta")).getAsJsonObject();
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
}
