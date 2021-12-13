package csci.project.knowledgeBase.testing;

import csci.project.knowledgeBase.client.KbClient;

public class SimUser {
    private static Agent agent;
    private static KbClient client;

    public SimUser(Agent newAgent, KbClient newClient) {
        agent =  newAgent;
        client = newClient;
    }

    public void runAgent() {
        for (TestAction curAction : agent.getActions()) {
            curAction.performAction(client);
        }
    }
}
