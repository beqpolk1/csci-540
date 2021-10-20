import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        processFile();
    }

    private static void processFile() {
        File file = new File("nems-g_data.csv");
        Scanner inFile = null;
        try {
            inFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        inFile.nextLine(); //get rid of header
        while (inFile.hasNextLine()) {
            String curLine = inFile.nextLine();
            String[] parseLine = parseCsvLine(curLine);

            String storeId = parseLine[1];
            String[] availableFood = extractFoods(parseLine);

            //write out store id and foods to XML
            System.out.println(storeId + ":");
            for (int i = 0; i <= availableFood.length - 1; i++) System.out.println("   " + availableFood[i]);
        }
    }

    private static String[] extractFoods(String[] fieldList) {
        ArrayList<String> foods = new ArrayList<>();

        if (fieldList[3].equals("1")) foods.add("skim milk");
        if (fieldList[4].equals("1")) foods.add("1% milk");
        if (fieldList[5].equals("1")) foods.add("2% milk");
        if (fieldList[6].equals("1")) foods.add("whole milk");

        if (fieldList[15].equals("1")) foods.add("banana");
        if (fieldList[16].equals("1")) foods.add("apple");
        if (fieldList[17].equals("1")) foods.add("orange");
        if (fieldList[18].equals("1")) foods.add("grape");
        if (fieldList[19].equals("1")) foods.add("cantaloupe");
        if (fieldList[20].equals("1")) foods.add("peach");
        if (fieldList[21].equals("1")) foods.add("strawberry");
        if (fieldList[22].equals("1")) foods.add("honey dew melon");
        if (fieldList[23].equals("1")) foods.add("watermelon");
        if (fieldList[24].equals("1")) foods.add("pear");

        if (fieldList[86].equals("1")) foods.add("carrot");
        if (fieldList[87].equals("1")) foods.add("tomato");
        if (fieldList[88].equals("1")) foods.add("sweet potato");
        if (fieldList[89].equals("1")) foods.add("broccoli");
        if (fieldList[90].equals("1")) foods.add("lettuce");
        if (fieldList[91].equals("1")) foods.add("corn");
        if (fieldList[92].equals("1")) foods.add("celery");
        if (fieldList[93].equals("1")) foods.add("cucumber");
        if (fieldList[94].equals("1")) foods.add("cabbage");
        if (fieldList[95].equals("1")) foods.add("cauliflower");

        if (fieldList[155].equals("1")) foods.add("ground beef (95% lean)");
        if (fieldList[156].equals("1")) foods.add("ground beef (90% lean)");
        if (fieldList[157].equals("1")) foods.add("ground beef (85% lean)");
        if (fieldList[158].equals("1")) foods.add("ground beef (80% lean)");
        if (fieldList[158].equals("1")) foods.add("ground beef (75% lean, regular)");

        if (fieldList[172].equals("1")) foods.add("hot dog");
        if (fieldList[173].equals("1")) foods.add("hot dog");
        if (fieldList[174].equals("1")) foods.add("low fat hot dog");
        if (fieldList[175].equals("1")) foods.add("hot dog");

        if (fieldList[205].equals("1")) foods.add("frozen meal");
        if (fieldList[206].equals("1")) foods.add("frozen meal");
        if (fieldList[207].equals("1")) foods.add("frozen meal");
        if (fieldList[208].equals("1")) foods.add("frozen meal");

        if (fieldList[271].equals("1")) foods.add("bagel");
        if (fieldList[272].equals("1")) foods.add("english muffin");
        if (fieldList[273].equals("1")) foods.add("muffin");
        if (fieldList[274].equals("1")) foods.add("muffin");
        if (fieldList[275].equals("1")) foods.add("danish");

        if (fieldList[303].equals("1")) foods.add("soft drink, diet");
        if (fieldList[304].equals("1")) foods.add("soft drink, diet");
        if (fieldList[305].equals("1")) foods.add("soft drink, regular");
        if (fieldList[306].equals("1")) foods.add("soft drink, regular");
        if (fieldList[308].equals("1")) foods.add("100% juice");
        if (fieldList[309].equals("1")) foods.add("juice");

        if (fieldList[335].equals("1")) foods.add("whole wheat bread");
        if (fieldList[336].equals("1")) foods.add("white bread");

        if (fieldList[347].equals("1")) foods.add("chips");
        if (fieldList[348].equals("1")) foods.add("chips");

        if (fieldList[359].equals("1")) foods.add("cereal");
        if (fieldList[360].equals("1")) foods.add("cereal");

        String[] a = new String[0];
        return foods.toArray(a);
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
