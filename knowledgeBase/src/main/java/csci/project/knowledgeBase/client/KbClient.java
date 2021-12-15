package csci.project.knowledgeBase.client;

import csci.project.knowledgeBase.backend.KbManager;
import csci.project.knowledgeBase.requests.KbRequester;
import csci.project.knowledgeBase.requests.Request;

public class KbClient {
    private KbRequester requester;
    private boolean injectTest;

    public KbClient(KbManager knowledgeBase) {
        injectTest = false;
        requester = new KbRequester(knowledgeBase, injectTest);
    }

    public void makeRequest(Request request) {
        requester.makeRequest(request);
    }

    public void setInjectTest(boolean newVal) {
        injectTest = newVal;
        requester.setInjectTest(newVal);
    }
}
