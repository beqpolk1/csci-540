package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import csci.project.knowledgeBase.requests.SearchRequest;

import java.util.HashSet;

class InferenceEngine {
    public static JsonObject matchGearToConditions(JsonArray reqGearTypes, JsonObject conditions, KbManager knowledgeBase) {
        JsonObject reqGear = getCandidateGear(reqGearTypes.deepCopy(), knowledgeBase);
        return reqGear;
    }

    private static JsonObject getCandidateGear(JsonArray typeArr, KbManager knowledgeBase) {
        HashSet<String> scanned = new HashSet<>();
        JsonObject candidates = new JsonObject();

        while (typeArr.size() > 0) {
            JsonElement curElement = typeArr.get(0);

            if (!scanned.contains(curElement.getAsString())) {
                scanned.add(curElement.getAsString());

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
                for (int i = 0; i <= childTypes.size() - 1; i++) {
                    typeArr.add(childTypes.get(i).getAsJsonObject().get("type").getAsString());
                }
            }

            typeArr.remove(0);
        }

        return candidates;
    }
}
