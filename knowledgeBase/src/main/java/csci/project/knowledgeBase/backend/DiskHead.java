package csci.project.knowledgeBase.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiskHead
{
    private Long seeks, scans;
    private String curAddr;

    public DiskHead() {
        seeks = 0L;
        scans = 0L;
        curAddr = "";
    }

    public List<String> fetchMemContents(ExternalMem block)
    {
        List<String> recordsRaw = new ArrayList<>();

        if (!(curAddr.equals(block.getLogiAddr()))) {
            seeks++;
            curAddr = block.getLogiAddr();
        }

        String physAddr = block.getPhysAddr();
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

        scans++;
        curAddr = (block.getNext() != null ? block.getNext() : "");
        return recordsRaw;
    }

    public void reset() {
        seeks = 0L;
        scans = 0L;
    }

    public Long getSeeks() { return seeks; }
    public Long getScans() { return scans; }
}
