public class BTree<T extends Comparable> {
    BNode<T> root;
    Integer size;

    public BTree(Integer size, T dummy) {
        //this.size = size;
        size = 4;
        Class<T> tClass = (Class<T>) dummy.getClass();
        root = new BNode<T>(tClass, size);
    }

    public BNode<T> search(T value) {
        return root.search(value);
    }

    public void addValue(T value) {
        BNode<T> addResult = root.search(value).addValue(value);
        if (!(addResult.isLeaf())) root = addResult;
    }
}
