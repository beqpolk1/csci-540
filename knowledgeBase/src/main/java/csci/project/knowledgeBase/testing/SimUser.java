package csci.project.knowledgeBase.testing;

import csci.project.knowledgeBase.client.KbClient;

public class SimUser implements Runnable {
    private static Agent agent;
    private static KbClient client;
    private String agentName;

    public SimUser(Agent newAgent, KbClient newClient, String newName) {
        agent =  newAgent;
        client = newClient;
        agentName = newName;
    }

    public void run() {
        int actionCnt = 0;

        for (TestAction curAction : agent.getActions()) {
            actionCnt++;
            String actionOutput = curAction.performAction(client);
            System.out.println("-----" + agentName + " (" + actionCnt + ")-----" + System.lineSeparator() + actionOutput);
        }
    }
}
