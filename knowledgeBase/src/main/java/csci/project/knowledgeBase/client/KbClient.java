package csci.project.knowledgeBase.client;

import csci.project.knowledgeBase.backend.KbManager;
import csci.project.knowledgeBase.requests.KbRequester;
import csci.project.knowledgeBase.requests.Request;

public class KbClient {
    private KbRequester requester;

    public KbClient(KbManager knowledgeBase) {
        requester = new KbRequester(knowledgeBase);
    }

    public void makeRequest(Request request) {
        requester.makeRequest(request);
    }
}
