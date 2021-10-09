import java.util.HashMap;
import java.util.List;

public interface ResultSet {
    public HashMap<String, Object> getRecord(Integer idx);
    public List<String> getColumns();
    public Integer getNumTuples();
    public Tuple get(Integer idx);
}
