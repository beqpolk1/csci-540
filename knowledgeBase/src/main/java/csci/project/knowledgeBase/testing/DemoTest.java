package csci.project.knowledgeBase.testing;

import com.google.gson.JsonObject;
import csci.project.knowledgeBase.backend.KbManager;
import csci.project.knowledgeBase.client.KbClient;
import csci.project.knowledgeBase.requests.DeleteRequest;
import csci.project.knowledgeBase.requests.GearQueryRequest;
import csci.project.knowledgeBase.requests.SearchRequest;

public class DemoTest {
    public static void main(String[] args) {
        String runMode, defaultRunMode = "read_only";
        KbManager knowledgeBase = new KbManager("outdoor_gear_kb");

        if (args.length > 0) runMode = args[0];
        else runMode = defaultRunMode;

        if (runMode.equals("read_only")) {
            SimUser user1 = new SimUser(makeAgent1(), new KbClient(knowledgeBase), "Bob");
            new Thread(user1).start();
        }
        else if (runMode.equals("delete_only")) {
            SimUser user2 = new SimUser(makeAgent2(), new KbClient(knowledgeBase), "Rafael");
            new Thread(user2).start();
        }
        else if (runMode.equals("read_delete")) {
            SimUser user1 = new SimUser(makeAgent1(), new KbClient(knowledgeBase), "Bob");
            new Thread(user1).start();

            SimUser user2 = new SimUser(makeAgent2(), new KbClient(knowledgeBase), "Rafael");
            new Thread(user2).start();
        }
        else if (runMode.equals("force_err")) {
            KbClient user3Client = new KbClient(knowledgeBase);
            user3Client.setInjectTest(true);
            SimUser user3 = new SimUser(makeAgent3(), user3Client, "Miriam");
            new Thread(user3).start();

            SimUser user4 = new SimUser(makeAgent4(), new KbClient(knowledgeBase), "Nadia");
            new Thread(user4).start();
        }
    }

    private static Agent makeAgent1() {
        Agent agent = new Agent();

        //search to get an activity item with a specific ID
        agent.addAction(
            (client) -> {
                String output;
                SearchRequest search = new SearchRequest("activity");
                search.addCriteria(
                    (checkObj) -> {
                        Number idFilter = -2147483628;
                        Number checkVal = checkObj.get("id").getAsNumber().intValue();
                        return checkVal.equals(idFilter);
                    }
                );

                client.makeRequest(search);
                output = search.getResponse().toString();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        //search to get gear items that are jackets that are "insulated" type or for conditions where precipitation = "true"
        agent.addAction(
            (client) -> {
                SearchRequest search = new SearchRequest("gear");

                search.addCriteria(
                    (checkObj) -> {
                        String parTypeFilter = "jacket";
                        return checkObj.get("par_type").isJsonNull() || checkObj.get("par_type").getAsString().equals(parTypeFilter);
                    }
                );

                search.addCriteria(
                    (checkObj) -> {
                        boolean condPrecipFilter = true;
                        String typeFilter = "insulated_jacket";

                        if (!checkObj.get("conditions").isJsonNull()) {
                            JsonObject conds = checkObj.get("conditions").getAsJsonObject();
                            if (conds.has("precip") && !conds.get("precip").isJsonNull() && conds.get("precip").getAsBoolean() == condPrecipFilter) return true;
                        }
                        return checkObj.get("type").getAsString().equals(typeFilter);
                    }
                );

                client.makeRequest(search);
                String output = search.getResponse().toString();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        //query to get gear recommendations for hike_xlong activity
        agent.addAction(
            (client) -> {
                String output = "Getting gear for hike_xlong activity" + System.lineSeparator();
                GearQueryRequest query = new GearQueryRequest(-2147483625, null);
                client.makeRequest(query);
                output += query.getResponse().toString();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        //query to get gear recommendations for hike_xlong activity, condition set 1
        agent.addAction(
            (client) -> {
                String conditions = "{\"precip\":false,\"avg_temp\":55,\"wind_avg\":7,\"hi_temp\":69,\"precip_type\":\"none\",\"cloud_cover\":40.2,\"lo_temp\":42,\"wind_gust\":13}";
                String output = "Getting gear for hike_xlong activity" + System.lineSeparator();
                output += "    Conditions: " + conditions + System.lineSeparator();
                GearQueryRequest query = new GearQueryRequest("hike_xlong", "type", conditions);
                client.makeRequest(query);
                output += query.getResponse().toString();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        //query to get gear recommendations for hike_xlong activity, condition set 2
        agent.addAction(
            (client) -> {
                String conditions = "{\"precip\":true,\"avg_temp\":22,\"wind_avg\":10,\"hi_temp\":37,\"precip_type\":\"snow\",\"cloud_cover\":89.0,\"lo_temp\":15,\"wind_gust\":20}";
                String output = "Getting gear for hike_xlong activity" + System.lineSeparator();
                output += "    Conditions: " + conditions + System.lineSeparator();
                GearQueryRequest query = new GearQueryRequest("hike_xlong", "type", conditions);
                client.makeRequest(query);
                output += query.getResponse().toString();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        //query to get gear recommendations for Bridger Ridge activity, condition set 3
        agent.addAction(
            (client) -> {
                String conditions = "{\"precip\":false,\"avg_temp\":73,\"wind_avg\":5,\"hi_temp\":87,\"precip_type\":\"none\",\"cloud_cover\":2.7,\"lo_temp\":58,\"wind_gust\":8}";
                String output = "Getting gear for Bridger Ridge activity" + System.lineSeparator();
                output += "    Conditions: " + conditions + System.lineSeparator();
                GearQueryRequest query = new GearQueryRequest("Bridger Ridge", "name", conditions);
                client.makeRequest(query);
                output += query.getResponse().toString();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        return agent;
    }

    private static Agent makeAgent2() {
        Agent agent = new Agent();

        //delete a gear item with a specific ID
        agent.addAction(
            (client) -> {
                String output = "Deleting specific gear item" + System.lineSeparator();

                DeleteRequest delete = new DeleteRequest("gear");
                delete.addCriteria(
                    (checkObj) -> {
                        Number idFilter = -2147483641;
                        Number checkVal = checkObj.get("id").getAsNumber().intValue();
                        return checkVal.equals(idFilter);
                    }
                );

                client.makeRequest(delete);
                output += delete.getResponse().toString();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        //delete gear items that are jackets that are "rain_jacket" type
        agent.addAction(
            (client) -> {
                String output = "Deleting gear items of type 'rain_jacket'" + System.lineSeparator();
                DeleteRequest delete = new DeleteRequest("gear");

                delete.addCriteria(
                    (checkObj) -> {
                        String typeFilter = "rain_jacket";
                        return checkObj.get("type").getAsString().equals(typeFilter);
                    }
                );

                client.makeRequest(delete);
                output += delete.getResponse().toString();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return output;
            }
        );

        return agent;
    }

    private static Agent makeAgent3() {
        Agent agent = new Agent();

        //query to get gear recommendations for College M activity, condition set 1
        agent.addAction(
            (client) -> {
                String conditions = "{\"precip\":false,\"avg_temp\":55,\"wind_avg\":7,\"hi_temp\":69,\"precip_type\":\"none\",\"cloud_cover\":40.2,\"lo_temp\":42,\"wind_gust\":13}";
                String output = "Getting gear for College M activity" + System.lineSeparator();
                output += "    Conditions: " + conditions + System.lineSeparator();
                GearQueryRequest query = new GearQueryRequest("College M", "name", conditions);
                client.makeRequest(query);
                output += query.getResponse().toString();

                return output;
            }
        );

        return agent;
    }

    private static Agent makeAgent4() {
        Agent agent = new Agent();

        //delete NF Thermoball gear item
        agent.addAction(
            (client) -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String output = "Deleting NF Thermoball gear item" + System.lineSeparator();

                DeleteRequest delete = new DeleteRequest("gear");
                delete.addCriteria(
                    (checkObj) -> {
                        String nameFilter = "NF Thermoball";
                        return !checkObj.get("name").isJsonNull() && checkObj.get("name").getAsString().equals(nameFilter);
                    }
                );

                client.makeRequest(delete);
                output += delete.getResponse().toString();

                return output;
            }
        );

        return agent;
    }
}
