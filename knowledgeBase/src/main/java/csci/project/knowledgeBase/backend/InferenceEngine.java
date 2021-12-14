package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import csci.project.knowledgeBase.requests.SearchRequest;

import java.util.*;

class InferenceEngine {
    private static HashMap<String, Queue<String>> typeTree;

    public static JsonObject getGearForActivity(JsonArray reqGearTypes, KbManager knowledgeBase) {
        JsonObject reqGear = getCandidateGear(reqGearTypes.deepCopy(), knowledgeBase);
        JsonArray adjTypes = adjustGearTypes(reqGearTypes);

        reqGear = trimReqGear(adjTypes, reqGear);

        return reqGear;
    }

    public static void getBestMatchGear(JsonObject reqGear, JsonObject conditions) {
        List<String> removes = new ArrayList<>();

        for (String curField : reqGear.keySet()) {
            JsonArray newArr = new JsonArray(), curArr = reqGear.getAsJsonArray(curField);
            int bestScore = 0;
            boolean onlyGeneric = true;

            for (int i = 0; i <= curArr.size() - 1; i++) {
                int curScore = getMatchScore(curArr.get(i).getAsJsonObject(), conditions);

                //only add a generic gear entry if no specific options have been found yet
                if (curArr.get(i).getAsJsonObject().get("name").isJsonNull() && onlyGeneric && curScore >= 0) {
                    newArr.add(curArr.get(i).getAsJsonObject());
                }
                //processing for a non-generic gear entry
                else if (!curArr.get(i).getAsJsonObject().get("name").isJsonNull()) {
                    //clear array and add gear entry if:
                    //  first non-generic gear entry found that matches conditions
                    //  OR it's a new best match score
                    if ((curScore >= 0 && onlyGeneric) || (curScore >= 0 && curScore > bestScore)) {
                        bestScore = curScore;
                        newArr = new JsonArray();
                        newArr.add(curArr.get(i).getAsJsonObject());
                        onlyGeneric = false;
                    }
                    //gear entry that matches conditions as well as current best - add it to array
                    else if (curScore >= 0 && curScore == bestScore) {
                        newArr.add(curArr.get(i).getAsJsonObject());
                    }
                }
            }

            if (newArr.size() == 0 && curArr.size() > 0) removes.add(curField);
            else reqGear.add(curField, newArr);
        }

        for (String curField : removes) reqGear.remove(curField);
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

    private static int getMatchScore(JsonObject gearObj, JsonObject conditions) {
        if (conditions.isJsonNull() || gearObj.get("conditions").isJsonNull()) return 0;

        int score = 0;
        JsonObject gearConds = gearObj.get("conditions").getAsJsonObject();

        for (String curField : conditions.keySet()) {
            if (gearConds.has(curField) && !gearConds.get(curField).isJsonNull()) {
                //integer range comparisons
                if (curField.equals("avg_temp") || curField.equals("hi_temp") || curField.equals("lo_temp")
                        || curField.equals("wind_avg") || curField.equals("wind_gust") || curField.equals("cloud_cover")) {
                    JsonArray range = gearConds.getAsJsonArray(curField);
                    int range1 = range.get(0).getAsInt(), range2 = range.get(1).getAsInt(), checkVal = conditions.get(curField).getAsInt();

                    if (checkVal < Integer.min(range1, range2) || checkVal > Integer.max(range1, range2)) {
                        score = -1;
                        break;
                    }
                    else {
                        score++;
                    }
                }
                else if (curField.equals("precip")) {
                    boolean gearVal = gearConds.get(curField).getAsBoolean(), checkVal = conditions.get(curField).getAsBoolean();
                    if (gearVal != checkVal) {
                        score = -1;
                        break;
                    }
                    else {
                        score++;
                    }
                }
                else if (curField.equals("precip_type")) {
                    JsonArray eligibleVals = gearConds.getAsJsonArray(curField);
                    JsonElement checkVal = conditions.get(curField);
                    if (!eligibleVals.contains(checkVal)) {
                        score = -1;
                        break;
                    }
                    else {
                        score++;
                    }
                }
            }
        }

        return score;
    }
}
