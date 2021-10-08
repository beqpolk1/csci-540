import java.util.HashMap;
import java.util.List;

public class Environment {
    private DiskHead disk;
    private String database, rootDir;

    public Environment(String newDatabase)
    {
        disk = new DiskHead();
        database = newDatabase;
        rootDir = "\\database\\" + database + "\\";
    }

    public DiskHead getDisk() { return disk; }

    public String getDatabase() { return database; }

    public List<String> getColumnsForTable(String theTab) {
        return null; //todo: lookup the table in metadata and return column list
    }

    public List<String> getTypesForTable(String theTab) {
        return null; //todo: lookup the table in metadata and return type list
    }

    public String getTableStart(String theTab) {
        return null; //todo: lookup the table in metadata and return first block
    }

    public ExternalMem getBlock(String logiAddr) {
        return null; //todo: lookup the logical address in metadata, construct block, and return
    }
}
