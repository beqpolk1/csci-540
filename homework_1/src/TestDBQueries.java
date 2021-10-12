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

        System.out.println("Done reading person table with filter on age");

        QueryCost trueCost = new QueryCost(env.getDisk().getSeeks(), env.getDisk().getScans(), ops);

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }

    //select all tuples/records from the hair color relation
    public static void selectAllFromHairColor(Environment env) {
        checkEnv(env);
        env.getDisk().reset();
        Long ops = 0L;

        String myTable = "hair_color";
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

    //select all tuples/records from the person relation and join to include hair_color data
    public static void nestedLoopJoin(Environment env) {
        checkEnv(env);
        env.getDisk().reset();
        Long ops = 0L;

        String table1 = "person", table2 = "hair_color";

        Queue<QueryAction> queryActions = new LinkedList<>();
        queryActions.add(new FullJoinNestedLoop(env.getTableMeta(table1), env.getTableMeta(table2)));
        QueryCost estCost = CostEstimator.estimateCost(queryActions);

        List<String> table1Cols = env.getColumnsForTable(table1), table2Cols = env.getColumnsForTable(table2);
        List<String> col1Types = env.getTypesForTable(table1), col2Types = env.getTypesForTable(table1);

        String joinField1 = "hair_color", joinField2 = "id";
        Integer joinField1Index = table1Cols.indexOf(joinField1), joinField2Index =table2Cols.indexOf(joinField2);

        List<String> resultCols = env.getColumnsForTable(table1), resultTypes = env.getTypesForTable(table1);
        for (int i = 0; i <= table2Cols.size() - 1; i++) {
            if (!(table2Cols.get(i).equals("id"))) {
                resultCols.add(table2Cols.get(i));
                resultTypes.add(col2Types.get(i));
            }
        }

        ExternalMem curBlock1 = env.getBlock(env.getTableStart(table1));
        while (curBlock1 != null) {
            Collection<Tuple> tab1Block = DataReader.readMem(curBlock1, env.getDisk(), col1Types);
            Collection<Tuple> joinedContents = new ArrayList<>();

            for (Tuple curTuple1 : tab1Block) {
                ExternalMem curBlock2 = env.getBlock(env.getTableStart(table2));

                while (curBlock2 != null) {
                    Collection<Tuple> tab2Block = DataReader.readMem(curBlock2, env.getDisk(), col2Types);

                    for (Tuple curTuple2 : tab2Block) {
                        if (curTuple1.getField(joinField1Index).equals(curTuple2.getField(joinField2Index))) {
                            List<Object> joined = (List) curTuple1.getAllFields();

                            for (int i = 0; i <= table2Cols.size() - 1; i++) {
                                ops++;
                                if (!(table2Cols.get(i).equals("id"))) joined.add(curTuple2.getField(i));
                            }
                            joinedContents.add(new ListTuple(joined));
                        }
                    }

                    curBlock2 = env.getBlock(curBlock2.getNext());
                }
            }

            ListResultSet joinedResults = new ListResultSet(resultCols, resultTypes, joinedContents);
            output(joinedResults);

            curBlock1 = env.getBlock(curBlock1.getNext());
        }

        System.out.println("Done joining " + table1 + " and " + table2);

        QueryCost trueCost = new QueryCost(env.getDisk().getSeeks(), env.getDisk().getScans(), ops);

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }

    //select all tuples/records from the person relation where the id is >= 7, using the index
    public static void selectAllFromPersonUsePrimaryIndex(Environment env) {
        checkEnv(env);
        env.getDisk().reset();
        Long ops = 0L;

        String myTable = "person";
        String filterCol = "id";
        Integer idFilter = 7;

        Queue<QueryAction> queryActions = new LinkedList<>();
        queryActions.add(new IndexScanFilter(env.getTableMeta(myTable), 4));
        QueryCost estCost = CostEstimator.estimateCost(queryActions);

        List<String> tableCols = env.getColumnsForTable(myTable);
        List<String> colTypes = env.getTypesForTable(myTable);

        BNode curNode = env.getIndex(myTable, filterCol).getStartNode(idFilter);
        ops = (long) curNode.getSearchOps();

        String lastBlock = null;
        List<Tuple> blockContents = null;
        while (curNode != null) {
            for (int i = 0; i < curNode.getSize() - 1; i++) {
                Collection<Tuple> filteredResults = new ArrayList<>();
                if (curNode.getVal(i) != null) {
                    if (curNode.getVal(i).compareTo(idFilter) >= 0) {
                        ops++;
                        ArrayList<String> addr = curNode.getAddr(i);

                        for (String curAddr : addr) {
                            String blockAddr = curAddr.substring(0, curAddr.indexOf("."));
                            String recAddr = curAddr.substring(curAddr.indexOf(".") + 1);

                            if (lastBlock == null) {
                                ExternalMem curBlock = env.getBlock(blockAddr);
                                blockContents = (List<Tuple>) DataReader.readMem(curBlock, env.getDisk(), colTypes);
                                lastBlock = blockAddr;
                                filteredResults.add(blockContents.get(Integer.parseInt(recAddr) - 1));
                            }
                            else if (lastBlock.equals(blockAddr)) {
                                filteredResults.add(blockContents.get(Integer.parseInt(recAddr) - 1));
                            }
                            else {
                                if (filteredResults.size() > 0) {
                                    ListResultSet results = new ListResultSet(tableCols, colTypes, filteredResults);
                                    output(results);
                                }
                                ExternalMem curBlock = env.getBlock(blockAddr);
                                blockContents = (List<Tuple>) DataReader.readMem(curBlock, env.getDisk(), colTypes);
                                lastBlock = blockAddr;
                                filteredResults.add(blockContents.get(Integer.parseInt(recAddr) - 1));
                            }
                        }
                    }
                }

                if (filteredResults.size() > 0) {
                    ListResultSet results = new ListResultSet(tableCols, colTypes, filteredResults);
                    output(results);
                }
            }
            curNode = curNode.getNextLeaf();
        }

        System.out.println("Done reading person table with filter on ID using index");

        QueryCost trueCost = new QueryCost(env.getDisk().getSeeks(), env.getDisk().getScans(), ops);

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }

    //select all tuples/records from the person relation where the id is >= 7, using the index
    public static void selectAllFromPersonUseSecondIndex(Environment env) {
        checkEnv(env);
        env.getDisk().reset();
        Long ops = 0L;

        String myTable = "person";
        String filterCol = "age";
        Integer ageFilter = 25;

        Queue<QueryAction> queryActions = new LinkedList<>();
        queryActions.add(new IndexScanFilter(env.getTableMeta(myTable), 4));
        QueryCost estCost = CostEstimator.estimateCost(queryActions);

        List<String> tableCols = env.getColumnsForTable(myTable);
        List<String> colTypes = env.getTypesForTable(myTable);

        BNode curNode = env.getIndex(myTable, filterCol).getStartNode(ageFilter);
        ops = (long) curNode.getSearchOps();

        String lastBlock = null;
        List<Tuple> blockContents = null;
        while (curNode != null) {
            for (int i = 0; i < curNode.getSize() - 1; i++) {
                Collection<Tuple> filteredResults = new ArrayList<>();
                if (curNode.getVal(i) != null) {
                    if (curNode.getVal(i).compareTo(ageFilter) >= 0) {
                        ops++;
                        ArrayList<String> addr = curNode.getAddr(i);

                        for (String curAddr : addr) {
                            String blockAddr = curAddr.substring(0, curAddr.indexOf("."));
                            String recAddr = curAddr.substring(curAddr.indexOf(".") + 1);

                            if (lastBlock == null) {
                                ExternalMem curBlock = env.getBlock(blockAddr);
                                blockContents = (List<Tuple>) DataReader.readMem(curBlock, env.getDisk(), colTypes);
                                lastBlock = blockAddr;
                                filteredResults.add(blockContents.get(Integer.parseInt(recAddr) - 1));
                            }
                            else if (lastBlock.equals(blockAddr)) {
                                filteredResults.add(blockContents.get(Integer.parseInt(recAddr) - 1));
                            }
                            else {
                                if (filteredResults.size() > 0) {
                                    ListResultSet results = new ListResultSet(tableCols, colTypes, filteredResults);
                                    output(results);
                                }
                                ExternalMem curBlock = env.getBlock(blockAddr);
                                blockContents = (List<Tuple>) DataReader.readMem(curBlock, env.getDisk(), colTypes);
                                lastBlock = blockAddr;
                                filteredResults.add(blockContents.get(Integer.parseInt(recAddr) - 1));
                            }
                        }
                    }
                }

                if (filteredResults.size() > 0) {
                    ListResultSet results = new ListResultSet(tableCols, colTypes, filteredResults);
                    output(results);
                }
            }
            curNode = curNode.getNextLeaf();
        }

        System.out.println("Done reading person table with filter on Age using index");

        QueryCost trueCost = new QueryCost(env.getDisk().getSeeks(), env.getDisk().getScans(), ops);

        System.out.println("Est. cost: " + estCost.toString());
        System.out.println("True cost: " + trueCost.toString());
    }

    private static void output(ResultSet results) {
        int maxColSize = ListTuple.maxColSize;
        //System.out.println(results.toString());
        List<String> columns = results.getColumns();
        System.out.print("|");
        for (String curCol : columns) {
            System.out.print(rPad(curCol, maxColSize) + "|");
        }

        System.out.print(System.lineSeparator());
        for (int i = 1; i <= (maxColSize * columns.size()) + columns.size() + 1; i++) System.out.print('-');
        System.out.print(System.lineSeparator());

        for (Integer i = 0; i < results.getNumTuples(); i++) {
            Tuple curTuple = results.get(i);
            System.out.println(curTuple.toString());
        }
        System.out.println();
    }

    private static String rPad(String str, int length) {
        if (str.length() > length) { str = str.substring(0, length - 3) + "..."; }
        return String.format("%-" + length + "s", str);
    }

    private static void checkEnv(Environment env) {
        if (!(env.getDatabase().equals("test_db"))) { throw new IllegalStateException("Cannot run test_db queries in this environment!"); }
    }
}
