import com.google.gson.JsonObject;

public class SecondIndexScanFilter implements QueryAction {
    private JsonObject tableMeta;
    private int indexSize;

    public SecondIndexScanFilter(JsonObject table, int indexSize) {
        tableMeta = table;
        this.indexSize = indexSize;
    }

    public QueryCost getCost() {
        QueryCost cost = new QueryCost();
        Long recordCount = tableMeta.get("record_count").getAsLong();
        cost.setOps((long) (recordCount + Math.ceil(Math.log(recordCount) / Math.log(indexSize))));
        cost.setSeeks(1L);
        cost.setScans(tableMeta.get("block_count").getAsLong());
        return cost;
    }
}
