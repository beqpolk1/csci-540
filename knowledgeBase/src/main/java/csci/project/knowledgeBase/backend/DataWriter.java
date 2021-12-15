package csci.project.knowledgeBase.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataWriter {
    public static Collection<JsonObject> readMem(ExternalMem block, DiskHead disk)
    {
        List<String> rawData = disk.fetchMemContents(block);

        JsonParser parser = new JsonParser();
        List<JsonObject> results = new ArrayList<>();
        for (String curLine : rawData) {
            String jsonString = curLine.substring(curLine.indexOf(':') + 1);
            String sysId = curLine.substring(0, curLine.indexOf(':'));
            JsonObject ret = new JsonObject();
            ret.addProperty("sysId", Integer.parseInt(sysId));
            ret.add("object", parser.parse(jsonString).getAsJsonObject());
            results.add(ret);
        }

        return results;
    }

    public static boolean deleteItem(ExternalMem block, Integer sysId, DiskHead disk) {
        List<String> rawData = disk.fetchMemContents(block), keepData = new ArrayList<>();

        for (String curLine : rawData) {
            String thisId = curLine.substring(0, curLine.indexOf(':'));
            if (!sysId.equals(Integer.parseInt(thisId))) keepData.add(curLine);
        }

        try {
            disk.flushToBlock(block, keepData);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
