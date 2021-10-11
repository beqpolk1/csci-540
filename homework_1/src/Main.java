import java.util.Random;

public class Main {

    public static void main(String[] args) {
        testBTree();
    }

    public static void testBTree() {
        BTree<Integer> myTree = new BTree<>(3, 0);

        Random rand = new Random();
        for (int i = 12; i >= 0; i--) {
            //myTree.addValue(rand.nextInt(20));
            myTree.addValue(i);
        }

        System.out.println("done");
    }

    public static void runTestDBQueries() {
        Environment env = new Environment("test_db");
        TestDBQueries.selectAllFromPerson(env);
        TestDBQueries.selectAllFromPersonFilterAge(env);
        TestDBQueries.selectAllFromHairColor(env);
        TestDBQueries.nestedLoopJoin(env);
    }
}
