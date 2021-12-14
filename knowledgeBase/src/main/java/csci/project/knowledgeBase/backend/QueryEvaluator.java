package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import csci.project.knowledgeBase.requests.GearQueryRequest;

class QueryEvaluator {
    public static JsonObject doQuery(GearQueryRequest query, KbManager knowledgeBase) {
        JsonObject activityObj = knowledgeBase.getEntityById("activity", query.getActivityId());
        JsonArray reqGearTypes = getAllGearReq(activityObj, knowledgeBase, new JsonArray());
        return InferenceEngine.matchGearToConditions(reqGearTypes, query.getConditions(), knowledgeBase);
    }

    //recursively gather all required gear types for the activity and any parent activities
    private static JsonArray getAllGearReq(JsonObject activity, KbManager knowledgeBase, JsonArray gearList) {
        gearList.addAll(activity.get("gear").getAsJsonArray());

        if (activity.get("par_type").isJsonNull()) return gearList;
        else {
            String parentType = activity.get("par_type").getAsString();
            JsonObject parActivity = knowledgeBase.getEntityByType("activity", parentType);
            return getAllGearReq(parActivity, knowledgeBase, gearList);
        }
    }
}
