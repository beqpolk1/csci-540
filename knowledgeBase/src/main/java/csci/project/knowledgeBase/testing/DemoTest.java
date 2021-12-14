package csci.project.knowledgeBase.testing;

import com.google.gson.JsonObject;
import csci.project.knowledgeBase.backend.KbManager;
import csci.project.knowledgeBase.client.KbClient;
import csci.project.knowledgeBase.requests.SearchRequest;

public class DemoTest {
    public static void main(String[] args) {
        KbManager knowledgeBase = new KbManager("outdoor_gear_kb");

        SimUser user1 = new SimUser(makeAgent1(), new KbClient(knowledgeBase), "Bob");
        user1.runAgent();
    }

    private static Agent makeAgent1() {
        Agent agent = new Agent();

        //query to get a gear item with a specific ID
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

        //query to get gear items that are jackets that are "insulated" type or for conditions where precipitation = "true"
        agent.addAction(
            (client) -> {
                SearchRequest search = new SearchRequest("gear");

                search.addCriteria(
                    (checkObj) -> {
                        String parTypeFilter = "jacket";

                        if (!checkObj.get("par_type").isJsonNull() && !checkObj.get("par_type").getAsString().equals(parTypeFilter)) return false;
                        else return true;
                    }
                );

                search.addCriteria(
                    (checkObj) -> {
                        Boolean condPrecipFilter = true;
                        String typeFilter = "insulated";

                        if (!checkObj.get("conditions").isJsonNull()) {
                            JsonObject conds = checkObj.get("conditions").getAsJsonObject();
                            if (!conds.get("precip").isJsonNull() && conds.get("precip").getAsBoolean() == condPrecipFilter) return true;
                        }
                        if (checkObj.get("type").getAsString().equals(typeFilter)) return true;
                        return false;
                    }
                );

                client.makeRequest(search);
                System.out.println(search.getResponse().toString());
            }
        );

        return agent;
    }
}
