import java.util.List;

public class TestDBQueries {
    public static void selectAllFromPerson(Environment env) {
        checkEnv(env);

        String myTable = "person";
        List<String> tableCols = env.getColumnsForTable(myTable);
        List<String> colTypes = env.getTypesForTable(myTable);
        ExternalMem curBlock = env.getBlock(env.getTableStart(myTable));

        while (curBlock != null) {
            ResultSet blockResults = DataReader.readMem(curBlock, env.getDisk(), tableCols, colTypes);
            output(blockResults);
            curBlock = env.getBlock(curBlock.getNext());
        }

        System.out.println("Done reading " + myTable);
    }

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
            System.out.print("|");

            for (Integer j = 0; j < columns.size(); j++) {
                System.out.print(rPad(curTuple.getField(j).toString(), maxColSize) + "|");
            }
            System.out.print(System.lineSeparator());
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
