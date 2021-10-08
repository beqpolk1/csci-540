import java.util.ArrayList;

public class Record implements Tuple{
    private ArrayList<Object> values;

    public Record(String rawData){
        //todo: parse rawData CSV string to hash map
        values = new ArrayList<>();
    }

    public Object getField(Integer field)
    {
        return values.get(field);
    }
}
