package csci.project.knowledgeBase.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataWriter {
    public static boolean deleteItem(ExternalMem block, Integer sysId, DiskHead disk) {
        List<String> rawData = disk.fetchMemContents(block), keepData = new ArrayList<>();

        for (String curLine : rawData) {
            String thisId = curLine.substring(0, curLine.indexOf(':'));
            if (!sysId.equals(Integer.parseInt(thisId))) keepData.add(curLine);
        }

        try {
            disk.flushToBlock(block, keepData);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
