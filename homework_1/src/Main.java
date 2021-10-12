import java.util.Random;

public class Main {

    public static void main(String[] args) {
        testBTree();
    }

    public static void testBTree() {
        BTree<Integer> myTree;

        System.out.println("=========SEQUENTIAL TEST=========");
        //sequential test
        myTree = new BTree<>(4, 0);
        for (int i = 0; i <= 40; i++) {
            myTree.addValue(i, 1);
            if (i % 2 == 0) myTree.addValue(i, 1);
            if (i % 3 == 0) myTree.addValue(i, 1);
        }
        myTree.checkTree();
        myTree.checkBPlus();

        System.out.println(System.lineSeparator() + "=========REVERSE SEQUENTIAL TEST=========");
        //reverse sequential test
        myTree = new BTree<>(4, 0);
        for (int i = 40; i >= 0; i--) {
            myTree.addValue(i, 1);
            if (i % 2 == 0) myTree.addValue(i, 1);
            if (i % 3 == 0) myTree.addValue(i, 1);
        }
        myTree.checkTree();
        myTree.checkBPlus();

        System.out.println(System.lineSeparator() + "=========RANDOM TEST=========");
        //random test
        myTree = new BTree<>(4, 0);
        Random rand = new Random();
        for (int i =0; i <= 50; i++) {
            Integer newVal = rand.nextInt(100);
            while (myTree.search(newVal).getIndexOf(newVal) >= 0) newVal = rand.nextInt(100);
            myTree.addValue(newVal, 1);
            if (newVal % 2 == 0) myTree.addValue(newVal, 1);
            if (newVal % 3 == 0) myTree.addValue(newVal, 1);
        }
        myTree.checkTree();
        myTree.checkBPlus();

        System.out.println(System.lineSeparator() + "=========BIG RANDOM TEST=========");
        //big random test
        Integer[] tally = new Integer[200];
        for (int i = 0; i <= tally.length - 1; i++) tally[i] = 0;
        myTree = new BTree<>(4, 0);
        for (int i =0; i <= 1000; i++) {
            Integer newVal = rand.nextInt(200);
            tally[newVal]++;
            myTree.addValue(newVal, 1);
        }
        myTree.checkTree(tally);
        myTree.checkBPlus();

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
