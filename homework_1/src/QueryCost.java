public class QueryCost {
    Long seeks, scans, ops;

    public QueryCost()
    {
        seeks = 0L;
        scans = 0L;
        ops = 0L;
    }

    public void addCost(QueryCost otherCost) {
        seeks += otherCost.getSeeks();
        scans += otherCost.getScans();
        ops += otherCost.getOps();
    }

    public void setSeeks(Long seeks) { this.seeks = seeks; }
    public void setScans(Long scans) { this.scans = scans; }
    public void setOps(Long ops) { this.ops = ops; }

    public Long getSeeks() { return seeks; }
    public Long getScans() { return scans; }
    public Long getOps() { return ops; }

    public String toString() {
        return "Ops: " + ops.toString() + " | Seeks: " + seeks.toString() + " | Scans: " + scans.toString();
    }
}
