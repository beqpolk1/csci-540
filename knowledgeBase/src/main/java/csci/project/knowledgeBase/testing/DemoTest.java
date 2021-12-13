package csci.project.knowledgeBase.testing;

import csci.project.knowledgeBase.backend.KbManager;
import csci.project.knowledgeBase.client.KbClient;
import csci.project.knowledgeBase.requests.SearchRequest;

public class DemoTest {
    public static void main(String[] args) {
        KbManager knowledgeBase = new KbManager("outdoor_gear_kb");

        SimUser user1 = new SimUser(makeAgent1(), new KbClient(knowledgeBase));
        user1.runAgent();
    }

    private static Agent makeAgent1() {
        Agent agent = new Agent();

        agent.addAction(
            (client) -> {
                SearchRequest search = new SearchRequest("gear");
                search.addCriteria(
                    (checkObj) -> {
                        Number idFilter = -2147483645;
                        Number checkVal = checkObj.get("id").getAsNumber().intValue();
                        if (checkVal.equals(idFilter)) return true;
                        else return false;
                    }
                );

                client.makeRequest(search);
                System.out.println(search.getResponse().toString());
            }
        );

        return agent;
    }
}
