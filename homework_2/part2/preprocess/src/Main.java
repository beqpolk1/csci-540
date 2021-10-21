import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        processFile();
    }

    private static void processFile() throws IOException {
        BufferedWriter outFile = new BufferedWriter(new FileWriter("store_food_data.xml", true));
        outFile.write("<?xml version=\"1.0\"?>" + System.lineSeparator());
        outFile.append("<store_food_data>" + System.lineSeparator());

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
            System.out.println(storeId + ": " + availableFood.length);
            outputXML(storeId, availableFood, outFile);
        }

        outFile.append("</store_food_data>");
        outFile.close();
    }

    private static void outputXML(String storeId, String[] foods, BufferedWriter outFile) throws IOException {
        outFile.append("    <store>" + System.lineSeparator());
        outFile.append("        <store_id>" + storeId + "</store_id>" + System.lineSeparator());

        for (String curFood : foods) {
            outFile.append("        <food>" + curFood + "</food>" + System.lineSeparator());
        }

        outFile.append("    </store>" + System.lineSeparator());
    }

    private static String[] extractFoods(String[] fieldList) {
        HashMap<String, Integer> foods = new HashMap<>();

        if (fieldList[3].equals("1")) foods.put("skim milk", 1);
        if (fieldList[4].equals("1")) foods.put("1% milk", 1);
        if (fieldList[5].equals("1")) foods.put("2% milk", 1);
        if (fieldList[6].equals("1")) foods.put("whole milk", 1);

        if (fieldList[15].equals("1")) foods.put("banana", 1);
        if (fieldList[16].equals("1")) foods.put("apple", 1);
        if (fieldList[17].equals("1")) foods.put("orange", 1);
        if (fieldList[18].equals("1")) foods.put("grape", 1);
        if (fieldList[19].equals("1")) foods.put("cantaloupe", 1);
        if (fieldList[20].equals("1")) foods.put("peach", 1);
        if (fieldList[21].equals("1")) foods.put("strawberry", 1);
        if (fieldList[22].equals("1")) foods.put("honey dew melon", 1);
        if (fieldList[23].equals("1")) foods.put("watermelon", 1);
        if (fieldList[24].equals("1")) foods.put("pear", 1);

        if (fieldList[86].equals("1")) foods.put("carrot", 1);
        if (fieldList[87].equals("1")) foods.put("tomato", 1);
        if (fieldList[88].equals("1")) foods.put("sweet potato", 1);
        if (fieldList[89].equals("1")) foods.put("broccoli", 1);
        if (fieldList[90].equals("1")) foods.put("lettuce", 1);
        if (fieldList[91].equals("1")) foods.put("corn", 1);
        if (fieldList[92].equals("1")) foods.put("celery", 1);
        if (fieldList[93].equals("1")) foods.put("cucumber", 1);
        if (fieldList[94].equals("1")) foods.put("cabbage", 1);
        if (fieldList[95].equals("1")) foods.put("cauliflower", 1);

        if (fieldList[155].equals("1")) foods.put("ground beef (95% lean)", 1);
        if (fieldList[156].equals("1")) foods.put("ground beef (90% lean)", 1);
        if (fieldList[157].equals("1")) foods.put("ground beef (85% lean)", 1);
        if (fieldList[158].equals("1")) foods.put("ground beef (80% lean)", 1);
        if (fieldList[158].equals("1")) foods.put("ground beef (75% lean, regular)", 1);

        if (fieldList[172].equals("1")) foods.put("hot dog", 1);
        if (fieldList[173].equals("1")) foods.put("hot dog", 1);
        if (fieldList[174].equals("1")) foods.put("low fat hot dog", 1);
        if (fieldList[175].equals("1")) foods.put("hot dog", 1);

        if (fieldList[205].equals("1")) foods.put("frozen meal", 1);
        if (fieldList[206].equals("1")) foods.put("frozen meal", 1);
        if (fieldList[207].equals("1")) foods.put("frozen meal", 1);
        if (fieldList[208].equals("1")) foods.put("frozen meal", 1);

        if (fieldList[271].equals("1")) foods.put("bagel", 1);
        if (fieldList[272].equals("1")) foods.put("english muffin", 1);
        if (fieldList[273].equals("1")) foods.put("muffin", 1);
        if (fieldList[274].equals("1")) foods.put("muffin", 1);
        if (fieldList[275].equals("1")) foods.put("danish", 1);

        if (fieldList[303].equals("1")) foods.put("soft drink, diet", 1);
        if (fieldList[304].equals("1")) foods.put("soft drink, diet", 1);
        if (fieldList[305].equals("1")) foods.put("soft drink, regular", 1);
        if (fieldList[306].equals("1")) foods.put("soft drink, regular", 1);
        if (fieldList[308].equals("1")) foods.put("100% juice", 1);
        if (fieldList[309].equals("1")) foods.put("juice", 1);

        if (fieldList[335].equals("1")) foods.put("whole wheat bread", 1);
        if (fieldList[336].equals("1")) foods.put("white bread", 1);

        if (fieldList[347].equals("1")) foods.put("chips", 1);
        if (fieldList[348].equals("1")) foods.put("chips", 1);

        if (fieldList[359].equals("1")) foods.put("cereal", 1);
        if (fieldList[360].equals("1")) foods.put("cereal", 1);

        String[] a = new String[0];
        return foods.keySet().toArray(a);
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
