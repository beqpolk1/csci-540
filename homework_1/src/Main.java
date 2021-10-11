import java.util.Random;

public class Main {

    public static void main(String[] args) {
        testBTree();
    }

    public static void testBTree() {
        BTree<Integer> myTree = new BTree<>(4, 0);

        Random rand = new Random();
        for (int i = 0; i <= 8; i++) {
            //myTree.addValue(rand.nextInt(20));
            myTree.addValue(i);
        }
        myTree.checkTree();

        myTree.addValue(5);
        myTree.checkTree();
        myTree.addValue(5);
        myTree.checkTree();

        System.out.println("done");
        myTree.checkTree();
    }

    public static void runTestDBQueries() {
        Environment env = new Environment("test_db");
        TestDBQueries.selectAllFromPerson(env);
        TestDBQueries.selectAllFromPersonFilterAge(env);
        TestDBQueries.selectAllFromHairColor(env);
        TestDBQueries.nestedLoopJoin(env);
    }
}
