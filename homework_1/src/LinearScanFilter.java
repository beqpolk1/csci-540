import com.google.gson.JsonObject;

public class LinearScanFilter implements QueryAction{
    private JsonObject tableMeta;

    public LinearScanFilter(JsonObject table) {
        tableMeta = table;
    }

    public QueryCost getCost() {
        QueryCost cost = new QueryCost();
        cost.setOps(tableMeta.get("record_count").getAsLong());
        cost.setSeeks(1L);
        cost.setScans(tableMeta.get("block_count").getAsLong());
        return cost;
    }
}
