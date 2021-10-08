import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListResultSet implements ResultSet{
    private List<String> columns, types;
    private List<Tuple> tuples;

    public ListResultSet(List<String> newColumns, List<String> newTypes, List<Tuple> newTuples){
        tuples = newTuples;
        columns = newColumns;
        types = newTypes;
    }

    public Tuple get(Integer idx){
        return tuples.get(idx);
    }

    public HashMap<String, Object> getRecord(Integer idx) {
        HashMap<String, Object> allData = new HashMap<>();

        for (int i = 0; i < columns.size(); i++) {
            allData.put(columns.get(i), tuples.get(idx).getField(i));
        }
        return allData;
    }
}
