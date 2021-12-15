package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import csci.project.knowledgeBase.requests.GearQueryRequest;

class QueryEvaluator {
    public static JsonObject doQuery(GearQueryRequest query, KbManager knowledgeBase) {
        JsonObject activityObj;

        if (query.getActivityId() != null) {
            activityObj = knowledgeBase.getEntityById("activity", query.getActivityId());
        }
        else if (query.getActivityType() != null) {
            activityObj = knowledgeBase.getEntityByType("activity", query.getActivityType());
        }
        else if (query.getActivityName() != null) {
            activityObj = knowledgeBase.getEntityByName("activity", query.getActivityName());
        }
        else {
            return new JsonObject();
        }

        JsonArray availFacts = knowledgeBase.getAvailFacts();
        JsonArray reqGearTypes = getAllGearReq(activityObj, knowledgeBase, new JsonArray(), availFacts);
        JsonObject reqGear = InferenceEngine.getGearForActivity(reqGearTypes, knowledgeBase, availFacts);

        InferenceEngine.getBestMatchGear(reqGear, query.getConditions());
        return reqGear;
    }

    //recursively gather all required gear types for the activity and any parent activities
    private static JsonArray getAllGearReq(JsonObject activity, KbManager knowledgeBase, JsonArray gearList, JsonArray availFacts) {
        gearList.addAll(activity.get("gear").getAsJsonArray());

        if (!activity.get("name").isJsonNull()) {
            String actType = activity.get("type").getAsString();
            JsonObject parActivity = knowledgeBase.getEntityByType("activity", actType, availFacts);
            return getAllGearReq(parActivity, knowledgeBase, gearList, availFacts);
        }
        else if (activity.get("par_type").isJsonNull()) {
            return gearList;
        }
        else {
            String parentType = activity.get("par_type").getAsString();
            JsonObject parActivity = knowledgeBase.getEntityByType("activity", parentType, availFacts);
            return getAllGearReq(parActivity, knowledgeBase, gearList, availFacts);
        }
    }
}
