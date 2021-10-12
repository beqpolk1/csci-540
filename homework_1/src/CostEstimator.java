import java.util.Queue;

public class CostEstimator {
    public static QueryCost estimateCost(Queue<QueryAction> actions) {
        QueryCost totalCost = new QueryCost();

        for (QueryAction curAction : actions) {
            totalCost.addCost(curAction.getCost());
        }

        return totalCost;
    }
}
