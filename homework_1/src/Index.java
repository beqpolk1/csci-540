import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.List;

public class Index<T extends Comparable> {
    BTree<T> theTree;

    public Index(String theTab, String col, Environment env, T dummy) {
        JsonArray columns = env.getTableMeta(theTab).getAsJsonArray("columns");
        String colType = "";

        for (int i = 0; i <= columns.size() - 1; i++) {
            if (columns.get(i).getAsJsonObject().get("name").getAsString().equals(col)) {
                colType = columns.get(i).getAsJsonObject().get("type").getAsString();
                break;
            }
        }

        if (colType.equals("int")) {
            theTree = new BTree<T>(4, dummy);
        }
        else if (colType.equals("string")) {
            theTree = new BTree<T>(4, dummy);
        }

        List<String> tableCols = env.getColumnsForTable(theTab);
        List<String> colTypes = env.getTypesForTable(theTab);
        ExternalMem curBlock = env.getBlock(env.getTableStart(theTab));

        while (curBlock != null) {
            Collection<Tuple> blockContents = DataReader.readMem(curBlock, env.getDisk(), colTypes);
            ListResultSet blockResults = new ListResultSet(tableCols, colTypes, blockContents);

            for (int i = 0; i <= blockResults.getNumTuples() - 1; i++) {
                theTree.addValue((T) blockResults.getRecord(i).get(col), curBlock.getLogiAddr() + "." + (i + 1));
            }
            curBlock = env.getBlock(curBlock.getNext());
        }

        //theTree.checkTree();
        //theTree.checkBPlus();
    }

    public BNode<T> getStartNode(T searchVal) {
        return theTree.search(searchVal);
    }
}
