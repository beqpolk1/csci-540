package csci.project.knowledgeBase.requests;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GearQueryRequest extends ReadRequest {
    private String activityName, activityType;
    private Number activityId;
    private JsonObject conditions;

    public GearQueryRequest(Number newActId, String conditionsString) {
        super("gear");
        activityId = newActId;
        if (conditionsString != null) conditions = new JsonParser().parse(conditionsString).getAsJsonObject();
        else conditions = new JsonObject();
    }

    public GearQueryRequest(String newAct, String typeOrName, String conditionsString) {
        super("gear");

        if (typeOrName.equals("type")) {
            activityType = newAct;
        }
        else if (typeOrName.equals("name")) {
            activityName = newAct;
        }

        if (conditionsString != null) conditions = new JsonParser().parse(conditionsString).getAsJsonObject();
        else conditions = new JsonObject();
    }

    public String getActivityName() { return activityName; }
    public String getActivityType() { return activityType; }
    public Number getActivityId() { return activityId; }
    public JsonObject getConditions() { return conditions; }
}
