import java.util.ArrayList;
import java.util.List;

public class DiskHead
{
    private Integer seeks, scans;
    private String curBlock;

    public DiskHead() {
        seeks = 0;
        scans = 0;
        curBlock = null;
    }

    public List<Tuple> fetchBlock(String logicalAddr)
    {
        List<Tuple> recordSet = new ArrayList<>();

        recordSet.add(new Record("foobar"));

        return recordSet;
    }
}
