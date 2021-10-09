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

    public String toString(Integer maxColSize) {
        String ret = "|";

        for (Object curVal : values) {
            String str = curVal.toString();
            if (str.length() > maxColSize) { str = str.substring(0, 26) + "..."; }
            ret = ret + (String.format("%-" + maxColSize + "s", str) + "|");
        }
        return ret;
    }

    public String toString() { return toString(20); }
}
