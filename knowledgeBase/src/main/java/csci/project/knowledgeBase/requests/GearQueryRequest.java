package csci.project.knowledgeBase.requests;

import com.google.gson.JsonNull;
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

    public String getActivityName() { return activityName; }
    public String getActivityType() { return activityType; }
    public Number getActivityId() { return activityId; }
    public JsonObject getConditions() { return conditions; }
}
