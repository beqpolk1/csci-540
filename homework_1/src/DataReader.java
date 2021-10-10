import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataReader {
    public static Collection<Tuple> readMem(ExternalMem block, DiskHead disk, List<String> colTypes)
    {
        List<String> rawData = disk.fetchMemContents(block);

        String[] parseLine;
        List<Tuple> tuples = new ArrayList<>();
        for (String curLine : rawData) {
            parseLine = parseCsvLine(curLine.substring(curLine.indexOf(':') + 1));
            Tuple newTuple = buildTuple(parseLine, colTypes);
            tuples.add(newTuple);
        }

        return tuples;
    }

    private static Tuple buildTuple(String[] fieldVals, List<String> types){
        List<Object> objVals = new ArrayList<>();

        for (int i = 0; i <= fieldVals.length - 1; i++) {
            if (types.get(i).equals("string")) {
                String newStr = fieldVals[i];
                objVals.add(newStr);
            }
            else if (types.get(i).equals("int")) {
                Integer newInt = Integer.parseInt(fieldVals[i]);
                objVals.add(newInt);
            }
        }

        return new ListTuple(objVals);
    }

    //helper function to parse a csv line that contains literals enclosed in quotes ("")
    private static String[] parseCsvLine(String line)
    {
        String[] splitString = line.split(",");
        String[] parsedList = new String[splitString.length];
        String literal;
        int elementCnt = 0;

        for (int i = 0; i < splitString.length; i++)
        {
            if (!splitString[i].startsWith("\""))
            {
                parsedList[elementCnt] = splitString[i];
            }
            else
            {
                literal = splitString[i].substring(1);

                while (i < splitString.length - 1 && !(splitString[i].endsWith("\"")))
                {
                    i++;
                    literal += "," + splitString[i];
                }

                literal = literal.substring(0,literal.length() - 1);
                parsedList[elementCnt] = literal;
            }
            elementCnt++;
        }

        String[] retVal = new String[elementCnt];
        for (int i = 0; i < elementCnt; i++) retVal[i] = parsedList[i];
        return retVal;
    }
}
