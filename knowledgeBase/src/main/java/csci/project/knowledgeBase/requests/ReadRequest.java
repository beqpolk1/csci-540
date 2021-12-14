package csci.project.knowledgeBase.requests;

import com.google.gson.JsonObject;

abstract class ReadRequest implements Request {
    private String entity;
    private JsonObject response;

    public ReadRequest(String newEntity) {
        entity = newEntity;
    }

    public String getEntity () { return entity; }

    public void setResponse(JsonObject newResp) {
        response = newResp;
    }
    public JsonObject getResponse() {
        return response;
    };
}
