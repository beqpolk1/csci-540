package csci.project.knowledgeBase.requests;

import com.google.gson.JsonObject;

abstract class ReadRequest implements Request {
    private String entity;
    private JsonObject response;

    public SearchRequest(String newEntity) {
        entity = newEntity;
    }

    public void setResponse(JsonObject newResp) {
        response = newResp;
    }

    public JsonObject getResponse() {
        return response;
    };
}
