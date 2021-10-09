import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Environment {
    private DiskHead disk;
    private String database, rootDir;
    JsonObject blockMap;

    public Environment(String newDatabase)
    {
        disk = new DiskHead();
        database = newDatabase;
        rootDir = "database\\" + database + "\\";

        blockMap = new JsonParser().parse(openReader(rootDir + "meta\\blockMap.meta")).getAsJsonObject();
    }

    public DiskHead getDisk() { return disk; }

    public String getDatabase() { return database; }

    public JsonObject getTableMeta(String theTab) {
        return new JsonParser().parse(openReader(rootDir + "meta\\" + theTab + ".meta")).getAsJsonObject();
    }

    public List<String> getColumnsForTable(String theTab) {
        JsonObject tableMeta = getTableMeta(theTab);
        JsonArray colList = tableMeta.get("columns").getAsJsonArray();

        List<String> types = new ArrayList<>();
        for (JsonElement curItem : colList) {
            types.add(curItem.getAsJsonObject().get("name").getAsString());
        }
        return types;
    }

    public List<String> getTypesForTable(String theTab) {
        JsonObject tableMeta = getTableMeta(theTab);
        JsonArray colList = tableMeta.get("columns").getAsJsonArray();

        List<String> types = new ArrayList<>();
        for (JsonElement curItem : colList) {
            types.add(curItem.getAsJsonObject().get("type").getAsString());
        }
        return types;
    }

    public String getTableStart(String theTab) {
        JsonObject tableMeta = getTableMeta(theTab);
        return tableMeta.get("first_block").getAsString();
    }

    public ExternalMem getBlock(String logiAddr) {
        if (logiAddr == null) return null;

        JsonObject theBlock = blockMap.get(logiAddr).getAsJsonObject();

        return new Block(logiAddr,
                rootDir + theBlock.get("physAddr").getAsString(),
                theBlock.get("prev").isJsonNull() ? null : theBlock.get("prev").getAsString(),
                theBlock.get("next").isJsonNull()  ? null : theBlock.get("next").getAsString()
        );
    }

    private Reader openReader(String theFile) {
        Reader fileReader = null;
        try {
            fileReader = new FileReader(theFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileReader;
    }
}
