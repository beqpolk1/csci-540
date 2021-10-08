public class Main {

    public static void main(String[] args) {
        String curLine = "5:\"10\",\"Greg\",\"Sand\",\"47\"";
        Integer foo = Integer.parseInt(curLine.substring(0, curLine.indexOf(':')));
        System.out.println(foo);
        curLine = curLine.substring(curLine.indexOf(':') + 1);
        System.out.println(curLine);
    }
}
