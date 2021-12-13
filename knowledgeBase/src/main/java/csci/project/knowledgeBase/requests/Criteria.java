package csci.project.knowledgeBase.requests;

import com.google.gson.JsonObject;

public interface Criteria {
    Boolean check(JsonObject obj);
}
