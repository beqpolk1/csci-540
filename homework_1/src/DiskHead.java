import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiskHead
{
    private Integer seeks, scans;
    private String curAddr;

    public DiskHead(String newBlockMap) {
        seeks = 0;
        scans = 0;
        curAddr = null;
    }

    public List<String> fetchMemContents(ExternalMem block)
    {
        List<String> recordsRaw = new ArrayList<>();
        String physAddr = block.getPhysAddr();

        if (!(curAddr.equals(physAddr))) {
            seeks++;
            curAddr = physAddr;
        }

        File file = new File(physAddr);
        Scanner inFile = null;
        try {
            inFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (inFile.hasNextLine()) {
            recordsRaw.add(inFile.nextLine());
        }

        curAddr = block.getNext().getPhysAddr();
        return recordsRaw;
    }

    public void reset() {
        seeks = 0;
        scans = 0;
    }
}
