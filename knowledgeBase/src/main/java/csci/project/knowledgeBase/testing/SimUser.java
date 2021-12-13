package csci.project.knowledgeBase.testing;

import csci.project.knowledgeBase.client.KbClient;

public class SimUser {
    private static Agent agent;
    private static KbClient client;
    private String agentName;

    public SimUser(Agent newAgent, KbClient newClient, String newName) {
        agent =  newAgent;
        client = newClient;
        agentName = newName;
    }

    public void runAgent() {
        int actionCnt = 0;

        for (TestAction curAction : agent.getActions()) {
            actionCnt++;
            System.out.println("-----" + agentName + " (" + actionCnt + ")-----");
            curAction.performAction(client);
        }
    }
}
