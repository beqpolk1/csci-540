package csci.project.knowledgeBase.requests;

import com.google.gson.JsonObject;

abstract class DmlRequest implements Request{
    private JsonObject response;

    public void setResponse(JsonObject newResp) {
        response = newResp;
    }
    public JsonObject getResponse() {
        return response;
    };
}
