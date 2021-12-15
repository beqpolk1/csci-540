package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import csci.project.knowledgeBase.requests.GearQueryRequest;

class QueryEvaluator {
    private static Transaction queryTrans;

    public static JsonObject doQuery(GearQueryRequest query, KbManager knowledgeBase, boolean injectTest) {
        JsonObject activityObj, reqGear = new JsonObject();
        queryTrans = knowledgeBase.openTransaction();
        boolean reconciled = false;

        while (!reconciled) {
            queryTrans.resetTouchedFacts();
            if (query.getActivityId() != null) {
                activityObj = knowledgeBase.getEntityById("activity", query.getActivityId(), queryTrans.getId());
            } else if (query.getActivityType() != null) {
                activityObj = knowledgeBase.getEntityByType("activity", query.getActivityType(), queryTrans.getId());
            } else if (query.getActivityName() != null) {
                activityObj = knowledgeBase.getEntityByName("activity", query.getActivityName(), queryTrans.getId());
            } else {
                return new JsonObject();
            }

            JsonArray availFacts = knowledgeBase.getAvailFacts();
            JsonArray reqGearTypes = getAllGearReq(activityObj, knowledgeBase, new JsonArray(), availFacts);
            reqGear = InferenceEngine.getGearForActivity(reqGearTypes, knowledgeBase, availFacts, queryTrans);

            InferenceEngine.getBestMatchGear(reqGear, query.getConditions());
            if (injectTest) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            reconciled = Reconciler.reconcile(queryTrans.getTouchedFacts(), knowledgeBase.getAvailFacts());
            if (!reconciled) System.out.println(">>>>>FAILED TO RECONCILE QUERY RESULT: " + reqGear.toString());
        }

        knowledgeBase.closeTransaction(queryTrans.getId());
        return reqGear;
    }

    //recursively gather all required gear types for the activity and any parent activities
    private static JsonArray getAllGearReq(JsonObject activity, KbManager knowledgeBase, JsonArray gearList, JsonArray availFacts) {
        gearList.addAll(activity.get("gear").getAsJsonArray());

        if (!activity.get("name").isJsonNull()) {
            String actType = activity.get("type").getAsString();
            JsonObject parActivity = knowledgeBase.getEntityByType("activity", actType, availFacts, queryTrans.getId());
            return getAllGearReq(parActivity, knowledgeBase, gearList, availFacts);
        }
        else if (activity.get("par_type").isJsonNull()) {
            return gearList;
        }
        else {
            String parentType = activity.get("par_type").getAsString();
            JsonObject parActivity = knowledgeBase.getEntityByType("activity", parentType, availFacts, queryTrans.getId());
            return getAllGearReq(parActivity, knowledgeBase, gearList, availFacts);
        }
    }
}
