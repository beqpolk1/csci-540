package csci.project.knowledgeBase.requests;

import com.google.gson.JsonObject;

public interface Request {
    void setResponse(JsonObject response);
    JsonObject getResponse();
}