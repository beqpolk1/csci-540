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
        System.out.println(results.toString());
        //todo: print results to console output
    }

    private static void checkEnv(Environment env) {
        if (!(env.getDatabase().equals("test_db"))) { throw new IllegalStateException("Cannot run test_db queries in this environment!"); }
    }
}
