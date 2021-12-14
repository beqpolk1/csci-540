package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import csci.project.knowledgeBase.requests.SearchRequest;

import java.util.*;

class InferenceEngine {
    private static HashMap<String, Queue<String>> typeTree;

    public static JsonObject matchGearToConditions(JsonArray reqGearTypes, JsonObject conditions, KbManager knowledgeBase) {
        JsonObject reqGear = getCandidateGear(reqGearTypes.deepCopy(), knowledgeBase);
        JsonArray adjTypes = adjustGearTypes(reqGearTypes);

        reqGear = trimReqGear(adjTypes, reqGear);

        return reqGear;
    }

    //remove entries from the required gear object that aren't present in the required type array
    private static JsonObject trimReqGear(JsonArray reqGearTypes, JsonObject orig) {
        JsonObject trimmed = new JsonObject();

        for (int i = 0; i <= reqGearTypes.size() - 1; i++) {
            trimmed.add(reqGearTypes.get(i).getAsString(), orig.get(reqGearTypes.get(i).getAsString()));
        }

        return trimmed;
    }

    //remove entries from the original array of required types that have child types required
    //if keeping a required type, add all of its child types as required as well
    private static JsonArray adjustGearTypes(JsonArray reqGearTypes) {
        JsonArray trimTypes = new JsonArray();

        for (int i = 0; i <= reqGearTypes.size() - 1; i++) {
            //only keep a type if none of it's children were a required type
            if (!hasReqChild(reqGearTypes.get(i), reqGearTypes)) {
                addAllTypeAndChildren(reqGearTypes.get(i).getAsString(), trimTypes);
            }
        }

        return trimTypes;
    }

    private static Boolean hasReqChild(JsonElement checkType, JsonArray reqGearTypes) {
        Queue<String> searchList = new LinkedList<>(typeTree.get(checkType.getAsString()));

        while (!searchList.isEmpty()) {
            String curType = searchList.poll();
            searchList.addAll(typeTree.get(curType));
            if (reqGearTypes.contains(new JsonPrimitive(curType))) return true;
        }

        return false;
    }

    private static void addAllTypeAndChildren(String type, JsonArray allGearTypes) {
        allGearTypes.add(type);

        for (String curType : typeTree.get(type)) {
            addAllTypeAndChildren(curType, allGearTypes);
        }
    }

    private static JsonObject getCandidateGear(JsonArray typeArr, KbManager knowledgeBase) {
        typeTree = new HashMap<>();
        HashSet<String> scanned = new HashSet<>();
        JsonObject candidates = new JsonObject();

        while (typeArr.size() > 0) {
            JsonElement curElement = typeArr.get(0);

            if (!scanned.contains(curElement.getAsString())) {
                scanned.add(curElement.getAsString());
                if (!typeTree.containsKey(curElement.getAsString())) typeTree.put(curElement.getAsString(), new LinkedList<>());

                //search for all gear of this type
                SearchRequest search = new SearchRequest("gear");
                search.addCriteria(
                    (checkObj) -> {
                        String typeFilter = curElement.getAsString();
                        String checkVal = checkObj.get("type").getAsString();
                        if (checkVal.equals(typeFilter)) return true;
                        else return false;
                    }
                );
                candidates.add(curElement.getAsString(), knowledgeBase.doSearch(search));

                //search for all children gear types (i.e. types that have this type as a parent)
                search = new SearchRequest("gear");
                search.addCriteria(
                    (checkObj) -> {
                        if (checkObj.get("par_type").isJsonNull()) return false;
                        String parTypeFilter = curElement.getAsString();
                        String checkVal = checkObj.get("par_type").getAsString();
                        if (checkVal.equals(parTypeFilter) && checkObj.get("name").isJsonNull()) return true;
                        else return false;
                    }
                );
                JsonArray childTypes = knowledgeBase.doSearch(search);

                //add all child types the the array for processing, and build out the tree for internal use
                for (int i = 0; i <= childTypes.size() - 1; i++) {
                    typeArr.add(childTypes.get(i).getAsJsonObject().get("type").getAsString());
                    typeTree.get(curElement.getAsString()).add(childTypes.get(i).getAsJsonObject().get("type").getAsString());
                }
            }

            typeArr.remove(0);
        }

        return candidates;
    }
}
