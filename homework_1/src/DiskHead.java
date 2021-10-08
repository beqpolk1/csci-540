import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiskHead
{
    private Integer seeks, scans;
    private String blockMap, curBlock;

    public DiskHead(String newBlockMap) {
        seeks = 0;
        scans = 0;
        curBlock = null;
        blockMap = newBlockMap;
    }

    public List<String> fetchBlock(String logicalAddr)
    {
        List<String> recordsRaw = new ArrayList<>();

        if (!(curBlock.equals(logicalAddr))) {
            seeks++;
            curBlock = logicalAddr;
        }

        String physAddr = translateAddr(logicalAddr);
        File file = new File(physAddr);
        Scanner inFile = null;
        try {
            inFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (inFile.hasNextLine())
        {
            recordsRaw.add(inFile.nextLine());
        }

        return recordsRaw;
    }

    public void reset() {
        seeks = 0;
        scans = 0;
    }

    private String translateAddr(String logicalAddr)
    {
        //todo: need something like blockMap.get(logicalAddr), but blockMap is a JSON string
        return null;
    }
}
