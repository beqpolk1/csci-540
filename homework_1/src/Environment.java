import java.util.List;

public class Environment {
    private DiskHead disk;
    private String database, rootDir;

    public Environment(String newDatabase)
    {
        disk = new DiskHead(getBlockMap());
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

    private String getBlockMap() {
        return null; //todo: load block map as single JSON string from file
    }
}
