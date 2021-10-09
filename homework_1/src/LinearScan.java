import com.google.gson.JsonObject;

public class LinearScan implements QueryAction{
    private JsonObject tableMeta;

    public LinearScan(JsonObject table) {
        tableMeta = table;
    }

    public QueryCost getCost() {
        QueryCost cost = new QueryCost();
        cost.setOps(1L);
        cost.setSeeks(1L);
        cost.setScans(tableMeta.get("block_count").getAsLong());
        return cost;
    }
}
