package csci.project.knowledgeBase.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataReader {
    public static Collection<JsonObject> readMem(ExternalMem block, DiskHead disk)
    {
        List<String> rawData = disk.fetchMemContents(block);

        JsonParser parser = new JsonParser;
        List<JsonObject> results = new ArrayList<>();
        for (String curLine : rawData) {
            results.add(parser.parse(curLine).getAsJsonObject);
        }

        return results;
    }
}
