import com.google.gson.JsonObject;

public class BlockNestedLoopJoin implements QueryAction{
    private JsonObject table1Meta, table2Meta;

    public BlockNestedLoopJoin(JsonObject table1, JsonObject table2) {
        table1Meta = table1;
        table2Meta = table2;
    }

    public QueryCost getCost() {
        QueryCost cost = new QueryCost();
        cost.setOps(table1Meta.get("record_count").getAsLong() * table2Meta.get("record_count").getAsLong());
        cost.setSeeks((long) table1Meta.getAsJsonArray("blocks").size() * table2Meta.getAsJsonArray("blocks").size() + table1Meta.getAsJsonArray("blocks").size());
        cost.setScans((long) 2 * table1Meta.getAsJsonArray("blocks").size());
        return cost;
    }
}