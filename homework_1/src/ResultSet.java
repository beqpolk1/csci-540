import java.util.HashMap;

public interface ResultSet {
    public HashMap<String, Object> getRecord(Integer idx);
    public Tuple get(Integer idx);
}
