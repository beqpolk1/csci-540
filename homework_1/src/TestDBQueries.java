import java.util.List;

public class TestDBQueries {
    public static void selectAllFromPerson(Environment env) {
        if (!(env.getDatabase().equals("test_db"))) { throw new IllegalStateException("Cannot run test_db queries in this environment!"); }

        String myTable = "person";
        List<String> tableCols = env.getColumnsForTable(myTable);
        List<String> colTypes = env.getTypesForTable(myTable);
        List<String> tableBlocks = env.getBlocksForTable(myTable); //todo: blocks need to have linked-list data structure

        for (String curBlock : tableBlocks) {
            ResultSet blockResults = DataReader.readBlock(curBlock, env.getDisk(), tableCols, colTypes);
            output(blockResults);
        }
    }

    private static void output(ResultSet results)
    {
        //todo: print results to console output
    }
}
