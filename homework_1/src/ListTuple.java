import java.util.ArrayList;
import java.util.List;

public class ListTuple implements Tuple{
    private List<Object> values;

    public ListTuple(List<Object> newValues){
        values = newValues;
    }

    public Object getField(Integer field)
    {
        return values.get(field);
    }
}
