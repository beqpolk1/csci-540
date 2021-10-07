import java.util.HashMap;

public class Record implements Tuple{
    HashMap<String, Object> values;

    public Record(String rawData){
        //todo: parse rawData JSON string to hash map
        values = new HashMap<>();
    }

    public Object getField(String field)
    {
        return values.get(field);
    }
}
