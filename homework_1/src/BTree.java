public class BTree<T extends Comparable> {
    BNode<T> root;
    Integer size;

    public BTree(Integer size, T dummy) {
        //this.size = size;
        this.size = 4;
        Class<T> tClass = (Class<T>) dummy.getClass();
        root = new BNode<T>(tClass, size);
    }

    public BNode<T> search(T value) {
        return root.search(value);
    }

    public void addValue(T value, Integer addr) {
        BNode<T> addResult = root.search(value).addValue(value, addr);
        if (!(addResult.isLeaf())) root = addResult;
    }

    public void checkTree() { checkTree(0, root); }

    private void checkTree(int level, BNode<T> node) {
        String prefix = (level > 0 ? String.format("%-" + (level * 2) + "s", "") : "");
        System.out.println(prefix + "Checking node");

        if (!(node.equals(root)) && node.getNumVals() < Math.ceil(size / 2.0)) System.out.println("****NUMVALS ERROR****");

        if (node.isLeaf()) {
            String output = "LEAF: ";
            for (int i = 0; i < size - 1; i++) { if (node.getVal(i) != null) output += node.getVal(i) + "[" + node.getAddr(i) + "], "; }
            System.out.println(prefix + output);
        }
        else {
            for (int i = 0; i <= size - 1; i++) {
                if (i < size - 1 && node.getVal(i) != null) {
                    String output = node.getVal(i) + " => ";
                    if (node.getPointer(i) != null)
                        output += node.getPointer(i).getFirstVal() + "-" + node.getPointer(i).getMaxVal();
                    System.out.println(prefix + output);

                    if (node.getPointer(i) != null && node.getVal(i).compareTo(node.getPointer(i).getMaxVal()) < 0) {
                        System.out.println("****ERROR****");
                    }
                    if (node.getPointer(i) != null) checkTree(level + 1, node.getPointer(i));
                } else {
                    String output = "(final) => ";
                    if (node.getPointer(i) != null)
                        output += node.getPointer(i).getFirstVal() + "-" + node.getPointer(i).getMaxVal();
                    System.out.println(prefix + output);

                    if (node.getVal(i - 1).compareTo(node.getPointer(i).getFirstVal()) > 0) {
                        System.out.println("****ERROR****");
                    }
                    if (node.getPointer(i) != null) checkTree(level + 1, node.getPointer(i));
                    break;
                }
            }
        }
    }
}
