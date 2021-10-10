import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TestDBQueries {
    public static void selectAllFromPerson(Environment env) {
        checkEnv(env);
        env.getDisk().reset();
        Long ops = 0L;

        String myTable = "person";
        Queue<QueryAction> queryActions = new LinkedList<>();
        queryActions.add(new LinearScan(env.getTableMeta(myTable)));
        QueryCost estCost = CostEstimator.estimateCost(queryActions);

        List<String> tableCols = env.getColumnsForTable(myTable);
        List<String> colTypes = env.getTypesForTable(myTable);
        ExternalMem curBlock = env.getBlock(env.getTableStart(myTable));
        ops++;

        while (curBlock != null) {
            Collection<Tuple> blockContents = DataReader.readMem(curBlock, env.getDisk(), colTypes);
            ListResultSet blockResults = new ListResultSet(tableCols, colTypes, blockContents);

            output(blockResults);
            curBlock = env.getBlock(curBlock.getNext());
        }

        System.out.println("Done reading " + myTable);

        QueryCost trueCost = new QueryCost();
        trueCost.setOps(ops);
        trueCost.setSeeks(env.getDisk().getSeeks());
        trueCost.setScans(env.getDisk().getScans());

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }

    /*public static void selectAllFromPersonFilterAge(Environment env) {
        checkEnv(env);
        env.getDisk().reset();
        Long ops = 0L;

        String myTable = "person";
        Integer ageFilter = 25;

        Queue<QueryAction> queryActions = new LinkedList<>();
        queryActions.add(new LinearScanFilter(env.getTableMeta(myTable)));
        QueryCost estCost = CostEstimator.estimateCost(queryActions);

        List<String> tableCols = env.getColumnsForTable(myTable);
        List<String> colTypes = env.getTypesForTable(myTable);
        ExternalMem curBlock = env.getBlock(env.getTableStart(myTable));
        ops++;

        while (curBlock != null) {
            ResultSet blockResults = DataReader.readMem(curBlock, env.getDisk(), tableCols, colTypes);
            output(blockResults);
            curBlock = env.getBlock(curBlock.getNext());
        }

        System.out.println("Done reading " + myTable);

        QueryCost trueCost = new QueryCost();
        trueCost.setOps(ops);
        trueCost.setSeeks(env.getDisk().getSeeks());
        trueCost.setScans(env.getDisk().getScans());

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }*/

    private static void output(ResultSet results)
    {
        int maxColSize = 20;
        //System.out.println(results.toString());
        List<String> columns = results.getColumns();
        System.out.print("|");
        for (String curCol : columns) {
            System.out.print(rPad(curCol, maxColSize) + "|");
        }

        System.out.print(System.lineSeparator());
        for (int i = 1; i <= (20 * columns.size()) + columns.size(); i++) System.out.print('-');
        System.out.print(System.lineSeparator());

        for (Integer i = 0; i < results.getNumTuples(); i++) {
            Tuple curTuple = results.get(i);
            System.out.println(curTuple.toString());
        }
        System.out.println();
    }

    private static String rPad(String str, int length) {
        if (str.length() > length) { str = str.substring(0, 26) + "..."; }
        return String.format("%-" + length + "s", str);
    }

    private static void checkEnv(Environment env) {
        if (!(env.getDatabase().equals("test_db"))) { throw new IllegalStateException("Cannot run test_db queries in this environment!"); }
    }
}
