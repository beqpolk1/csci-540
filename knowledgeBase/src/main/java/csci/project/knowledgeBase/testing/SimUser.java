package csci.project.knowledgeBase.testing;

import csci.project.knowledgeBase.client.KbClient;
import csci.project.knowledgeBase.requests.SearchRequest;

public class SimUser {
    private static Agent agent;
    private static KbClient client;

    public static void main(String[] args) {
        agent = new Agent();
        client = new KbClient("outdoor_gear_kb");

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
}
