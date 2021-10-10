import java.util.*;

public class TestDBQueries {
    //select all tuples/records from the person relation
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

        while (curBlock != null) {
            Collection<Tuple> blockContents = DataReader.readMem(curBlock, env.getDisk(), colTypes);
            ListResultSet blockResults = new ListResultSet(tableCols, colTypes, blockContents);

            output(blockResults);
            curBlock = env.getBlock(curBlock.getNext());
        }

        System.out.println("Done reading " + myTable);

        QueryCost trueCost = new QueryCost(env.getDisk().getSeeks(), env.getDisk().getScans(), ops);

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }

    //select all tuples/records from the person relation where age >= 25
    public static void selectAllFromPersonFilterAge(Environment env) {
        checkEnv(env);
        env.getDisk().reset();
        Long ops = 0L;

        String myTable = "person";
        String filterCol = "age";
        Integer ageFilter = 25;

        Queue<QueryAction> queryActions = new LinkedList<>();
        queryActions.add(new LinearScanFilter(env.getTableMeta(myTable)));
        QueryCost estCost = CostEstimator.estimateCost(queryActions);

        List<String> tableCols = env.getColumnsForTable(myTable);
        Integer filterIndex = tableCols.indexOf(filterCol);
        List<String> colTypes = env.getTypesForTable(myTable);
        ExternalMem curBlock = env.getBlock(env.getTableStart(myTable));

        while (curBlock != null) {
            Collection<Tuple> blockContents = DataReader.readMem(curBlock, env.getDisk(), colTypes);
            Collection<Tuple> filteredResults = new ArrayList<>();

            for (Tuple curTuple : blockContents) {
                ops++;
                if (!(curTuple.getField(filterIndex) instanceof Integer)) throw new IllegalArgumentException();
                Integer comp = (Integer) curTuple.getField(filterIndex);
                if (comp >= ageFilter) filteredResults.add(curTuple);
            }

            ListResultSet blockResults = new ListResultSet(tableCols, colTypes, filteredResults);
            output(blockResults);
            curBlock = env.getBlock(curBlock.getNext());
        }

        System.out.println("Done reading " + myTable);

        QueryCost trueCost = new QueryCost(env.getDisk().getSeeks(), env.getDisk().getScans(), ops);

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }

    private static void output(ResultSet results)
    {
        int maxColSize = ListTuple.maxColSize;
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
