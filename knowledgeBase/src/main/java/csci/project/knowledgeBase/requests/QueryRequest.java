package csci.project.knowledgeBase.requests;

public class GearQueryRequest extends ReadRequest {
    private String activityName;
    private Number activityId;
    private JsonObject conditions;

    public SearchRequest(String newActName, ) {
        super("gear");
    }


}
