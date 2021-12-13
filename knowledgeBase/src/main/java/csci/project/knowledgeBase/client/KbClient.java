package csci.project.knowledgeBase.client;

import com.google.gson.JsonObject;
import csci.project.knowledgeBase.backend.KbManager;
import csci.project.knowledgeBase.requests.KbRequester;
import csci.project.knowledgeBase.requests.Request;

public class KbClient {
    private KbRequester requester;

    public KbClient(String knowledgeBaseName) {
        requester = new KbRequester(new KbManager(knowledgeBaseName));
    }

    public void makeRequest(Request request) {
        requester.makeRequest(request);
    }
}
