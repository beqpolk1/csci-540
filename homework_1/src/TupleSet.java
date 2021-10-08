import java.util.HashMap;

public interface TupleSet {
    public HashMap<String, Object> get(Integer idx);
    public Tuple get(Integer idx);
}
