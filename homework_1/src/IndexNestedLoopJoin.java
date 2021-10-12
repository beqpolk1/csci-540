import com.google.gson.JsonObject;

public class IndexNestedLoopJoin implements QueryAction{
    private JsonObject table1Meta, table2Meta;
    private int indexSize;

    public IndexNestedLoopJoin(JsonObject table1, JsonObject table2, int indexSize) {
        table1Meta = table1;
        table2Meta = table2;
        this.indexSize = indexSize;
    }

    public QueryCost getCost() {
        QueryCost cost = new QueryCost();
        Long recordCount = table2Meta.get("record_count").getAsLong();
        //                     one linear scan of a BTree node per record in table1                                 one BTree search per record in table1
        cost.setOps((long) (((indexSize - 1) * table1Meta.get("record_count").getAsLong()) + (table1Meta.get("record_count").getAsLong() * (Math.ceil(Math.log(recordCount) / Math.log(indexSize))))));
        //                       one seek to each block of table1                   for each record in table1, seek to a block of table2
        cost.setSeeks(table1Meta.getAsJsonArray("blocks").size() + table1Meta.get("record_count").getAsLong());
        //                       one scan for each block of table1                  for each record in table1, scan a block of table2
        cost.setScans(table1Meta.getAsJsonArray("blocks").size() + table1Meta.get("record_count").getAsLong());
        return cost;
    }
}
