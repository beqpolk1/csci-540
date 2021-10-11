import java.lang.reflect.Array;

public class BNode<T extends Comparable> {
    T[] values;
    Class<T> tClass;
    String[] addresses;
    BNode[] pointers;
    Boolean isLeaf;
    BNode<T> parent, resetLeft, resetRight;
    int numVals;

    public BNode(Class<T> tClass, int size, BNode<T> parent){
        this.tClass = tClass;
        pointers = new BNode[size];
        values = (T[]) Array.newInstance(tClass, size - 1);
        addresses = new String[size - 1];
        isLeaf = true;
        numVals = 0;
        this.parent = parent;
    }

    public BNode(Class<T> tClass, int size){
        this(tClass, size, null);
    }

    public BNode<T> addValue(T value) {
        int insertIndex = getLessThanIdx(value);

        if (values[insertIndex] == null) {
            values[insertIndex] = value;
            numVals++;
            return this;
        }
        else if (numVals < values.length) {
            int nullIndex = 0;
            while (values[nullIndex] != null) nullIndex++;

            while (nullIndex > insertIndex) {
                values[nullIndex] = values[nullIndex - 1];
                nullIndex--;
            }

            values[insertIndex] = value;
            numVals++;
            return this;
        }
        else { //hardcoded implementation for size = 4
            BNode<T> addPar;

            if (parent == null) { addPar = new BNode(tClass, pointers.length); }
            else { addPar = parent; }

            BNode<T> newLeft = new BNode(tClass, pointers.length, addPar), newRight = new BNode(tClass, pointers.length, addPar);
            int median = (values.length + 1) / 2;

            for (int i = 0; i <= values.length - 1; i++) {
                if (insertIndex == i) {
                    if (newLeft.getNumVals() < median) { newLeft.addValue(value); }
                    else { newRight.addValue(value); }
                }

                if (newLeft.getNumVals() < median) { newLeft.addValue(values[i]); }
                else { newRight.addValue(values[i]); }
            }

            addPar.pushUp(newRight.getFirstVal(), newLeft, newRight);
            resetLeft = newLeft;
            resetRight = newRight;

            BNode<T> newRoot = newLeft;
            while (newRoot.getParent() != null) { newRoot = newRoot.getParent(); }
            return newRoot;
        }
    }

    public void pushUp(T value, BNode<T> newLeft, BNode<T> newRight) {
        if (isLeaf) {
            isLeaf = false;
            numVals++;
            values[0] = value;
            pointers[0] = newLeft;
            pointers[1] = newRight;
        }
        else {
            int insertIndex = getLessThanIdx(value);

            if (values[insertIndex] == null) {
                pointers[insertIndex] = newLeft;
                pointers[insertIndex + 1] = newRight;
                addValue(value);
            }
            else if (numVals < values.length) {
                int nullIndex = 0;
                while (values[nullIndex] != null) nullIndex++;

                while (nullIndex > insertIndex) {
                    pointers[nullIndex + 1] = pointers[nullIndex];
                    nullIndex--;
                }

                addValue(value);
                pointers[insertIndex] = newLeft;
                pointers[insertIndex + 1] = newRight;
            }
            else {
                BNode<T> newPar = addValue(value);

                if (resetLeft != null && resetRight != null) {
                    for (int i = 0; i < pointers.length - 1; i++) {
                        resetLeft.setPointer(i, pointers[i]);
                    }

                    resetRight.setPointer(1, newLeft);
                    resetRight.setPointer(2, newRight);
                }
                else {
                    int newIndex = newPar.getIndexOf(value);
                    newPar.setPointer(newIndex, newLeft);
                    newPar.setPointer(newIndex + 1, newRight);
                }
            }

        }
    }

    public BNode<T> search(T value) {
        if (isLeaf) return this;

        int pointerIndex = getLessThanIdx(value);
        if (values[pointerIndex] != null && values[pointerIndex].compareTo(value) <= 0) pointerIndex++;

        return pointers[pointerIndex].search(value);
    }

    public BNode<T> getPointer(int index) { return pointers[index]; }
    public void setPointer(int index, BNode<T> newVal) {
        pointers[index] = newVal;
        newVal.setParent(this);
        isLeaf = false;
    }

    public int getIndexOf(T value) {
        for (int i = 0; i <= values.length - 1; i++) {
            if (values[i].compareTo(value) == 0) return i;
        }
        return -1;
    }

    public T getFirstVal() { return values[0]; }

    //public void makeNextLink(BNode<T> nextNode)

    public int getNumVals() { return numVals; }
    public Boolean isLeaf() { return isLeaf; }
    public void setParent(BNode<T> newPar) { parent = newPar; }
    public BNode<T> getParent() { return parent; }

    private int getLessThanIdx(T value) {
        int i = 0;
        for (i = 0; i < values.length - 1; i++) {
            if (values[i] == null) break;
            if (values[i].compareTo(value) > 0) break;
        }
        return i;
    }
}
