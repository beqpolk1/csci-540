public class Environment {
    private DiskHead disk;

    public Environment()
    {
        disk = new DiskHead(getBlockMap());
    }

    public DiskHead getDisk() { return disk; }

    private String getBlockMap() {
        return null; //todo: load block map as single JSON string from file
    }
}
