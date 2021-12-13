package csci.project.knowledgeBase.requests;

import com.google.gson.JsonObject;

abstract class ReadRequest implements Request {
    JsonObject response;

    public void setResponse(JsonObject newResp) {
        response = newResp;
    }

    public JsonObject getResponse() {
        return response;
    };
}
