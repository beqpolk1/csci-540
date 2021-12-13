package csci.project.knowledgeBase.backend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class DataReader {
    public static Collection<JsonObject> readMem(ExternalMem block, DiskHead disk)
    {
        List<String> rawData = disk.fetchMemContents(block);

        JsonParser parser = new JsonParser();
        List<JsonObject> results = new ArrayList<>();
        for (String curLine : rawData) {
            String jsonString = curLine.substring(curLine.indexOf(':') + 1);
            results.add(parser.parse(jsonString).getAsJsonObject());
        }

        return results;
    }
}
