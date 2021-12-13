package csci.project.knowledgeBase.utils;

import com.google.gson.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class TestJsonMaker {
    private static final String baseDir = "gen_test_data\\";
    private static IdGen idGen;

    public static void main(String[] args) {
        idGen = new IdGen();
        writeConditions();
        writeGear(idGen);
        writeActivities(idGen);
    }

    private static void writeConditions() {
        String fileName = "conditions.json";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(baseDir + fileName));

            int cnt = 0;
            for (HashMap<String, Object> newVal : TestDataMaker.getCondData()) {
                cnt++;
                JsonObject condObj = makeConditionsObj(newVal);
                System.out.println(condObj.toString());
                writer.write(cnt + ":" + condObj.toString() + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Failed opening/closing output file!");
            e.printStackTrace();
        }

        System.out.println("Wrote conditions");
    }

    private static void writeGear(IdGen idGen) {
        String fileName = "gear.json";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(baseDir + fileName));

            int cnt = 0;
            for (HashMap<String, Object> newVal : TestDataMaker.getGearData(idGen)) {
                cnt++;
                JsonObject gearObj = makeGearObj(newVal);
                System.out.println(gearObj.toString());
                writer.write(cnt + ":" + gearObj.toString() + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Failed opening/closing output file!");
            e.printStackTrace();
        }

        System.out.println("Wrote gear");
    }

    private static void writeActivities(IdGen idGen) {
        String fileName = "activities.json";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(baseDir + fileName));

            int cnt = 0;
            for (HashMap<String, Object> newVal : TestDataMaker.getActivityData(idGen)) {
                cnt++;
                JsonObject activityObj = makeActivityObj(newVal);
                System.out.println(activityObj.toString());
                writer.write(cnt + ":" + activityObj.toString() + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Failed opening/closing output file!");
            e.printStackTrace();
        }

        System.out.println("Wrote activities");
    }

    private static JsonObject makeConditionsObj(HashMap<String, Object> condVals) {
        JsonObject condObj = new JsonObject();

        for (String curKey : condVals.keySet()) {
            if (condVals.get(curKey) instanceof String) condObj.addProperty(curKey, (String) condVals.get(curKey));
            if (condVals.get(curKey) instanceof Boolean) condObj.addProperty(curKey, (Boolean) condVals.get(curKey));
            if (condVals.get(curKey) instanceof Number) condObj.addProperty(curKey, (Number) condVals.get(curKey));
            if (condVals.get(curKey) instanceof Character) condObj.addProperty(curKey, (Character) condVals.get(curKey));
        }

        return condObj;
    }

    private static JsonObject makeGearObj(HashMap<String, Object> gearVals) {
        JsonObject gearObj = new JsonObject();

        for (String curKey : gearVals.keySet()) {
            if (gearVals.get(curKey) == null) {
                gearObj.add(curKey, null);
            }
            else if (curKey.equals("conditions")) {
                JsonParser parser = new JsonParser();
                JsonObject conds = parser.parse((String) gearVals.get(curKey)).getAsJsonObject();
                gearObj.add("conditions", conds);
            }
            else {
                if (gearVals.get(curKey).getClass().isArray()) {
                    JsonArray newArr = new JsonArray();
                    Object[] rawArr = (Object[]) gearVals.get(curKey);

                    for (Object item : rawArr) newArr.add((String) item);
                    gearObj.add(curKey, newArr);
                }
                else if (gearVals.get(curKey) instanceof String) gearObj.addProperty(curKey, (String) gearVals.get(curKey));
                else if (gearVals.get(curKey) instanceof Boolean) gearObj.addProperty(curKey, (Boolean) gearVals.get(curKey));
                else if (gearVals.get(curKey) instanceof Number) gearObj.addProperty(curKey, (Number) gearVals.get(curKey));
                else if (gearVals.get(curKey) instanceof Character) gearObj.addProperty(curKey, (Character) gearVals.get(curKey));
            }
        }

        return gearObj;
    }

    private static JsonObject makeActivityObj(HashMap<String, Object> actVals) {
        JsonObject actObj = new JsonObject();

        for (String curKey : actVals.keySet()) {
            if (actVals.get(curKey) == null) {
                actObj.add(curKey, null);
            }
            else {
                if (actVals.get(curKey).getClass().isArray()) {
                    JsonArray newArr = new JsonArray();
                    Object[] rawArr = (Object[]) actVals.get(curKey);

                    for (Object item : rawArr) newArr.add((String) item);
                    actObj.add(curKey, newArr);
                }
                else if (actVals.get(curKey) instanceof String) actObj.addProperty(curKey, (String) actVals.get(curKey));
                else if (actVals.get(curKey) instanceof Boolean) actObj.addProperty(curKey, (Boolean) actVals.get(curKey));
                else if (actVals.get(curKey) instanceof Number) actObj.addProperty(curKey, (Number) actVals.get(curKey));
                else if (actVals.get(curKey) instanceof Character) actObj.addProperty(curKey, (Character) actVals.get(curKey));
            }
        }

        return actObj;
    }
}
