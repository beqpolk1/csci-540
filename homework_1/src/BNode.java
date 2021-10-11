import java.lang.reflect.Array;
import java.util.ArrayList;

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

        if (values[insertIndex] == null) { //base case: adding a value to a node and the spot it belongs is already empty
            values[insertIndex] = value;
            numVals++;
            return this;
        }
        else if (numVals < values.length) { //next case: adding a value to a node where there's space, but we need to shift thigns
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
        else { //complex case: node is full; need to split it and send the divider up to the parent
            //hardcoded implementation for size = 4
            BNode<T> addPar;

            //if we're at the root, send the divider up to a brand new node
            //otherwise, send it to the parent
            if (parent == null) { addPar = new BNode(tClass, pointers.length); }
            else { addPar = parent; }

            //split this node up into equal sizes around the divider
            //new nodes are linked to the parent node that the divider is getting pushed to
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

            //push the divider up to the parent node
            addPar.pushUp(newRight.getFirstVal(), newLeft, newRight);
            //track the new nodes that this got split into (for later use in pushUp within the same node scope)
            resetLeft = newLeft;
            resetRight = newRight;

            //always return the root
            BNode<T> newRoot = newLeft;
            while (newRoot.getParent() != null) { newRoot = newRoot.getParent(); }
            return newRoot;
        }
    }

    public void pushUp(T value, BNode<T> newLeft, BNode<T> newRight) {
        if (isLeaf) { //base case: value was pushed to a brand new ("empty") node, i.e. we had to make a new root
            isLeaf = false;
            numVals++;
            values[0] = value;
            pointers[0] = newLeft;
            pointers[1] = newRight;
        }
        else { //next case: value was pushed to a node that is already a parent, has some pointers set
            int insertIndex = getLessThanIdx(value); //get the spot where this value would go

            if (values[insertIndex] == null) { //base case: value will go into an empty spot in this node
                //set pointers accordingly
                pointers[insertIndex] = newLeft;
                pointers[insertIndex + 1] = newRight;
                addValue(value); //add the value to this node
            }
            else if (numVals < values.length) { //next case: value will go into an occupied space in this node, but there is still room
                //when we add the new value, the existing values will get shuffled around
                //move the pointers accordingly, in advance of adding the value, to keep things lined up
                int nullIndex = 0;
                while (values[nullIndex] != null) nullIndex++;

                while (nullIndex > insertIndex) {
                    pointers[nullIndex + 1] = pointers[nullIndex];
                    nullIndex--;
                }

                addValue(value);//add the value
                //set the pointers accordingly
                pointers[insertIndex] = newLeft;
                pointers[insertIndex + 1] = newRight;
            }
            else { //complex case: this node will also end up being split because we pushed to a full one
                //start by adding the value; track the new root for future reference
                BNode<T> newPar = addValue(value);

                //resetLeft and resetRight will hold the values that this node got split into after adding the new value
                if (resetLeft != null && resetRight != null) {
                    //re-assign new left, new right, and existing pointers to pointers of resetLeft and resetRight appropriately
                    //(resetLeft and resetRight are already appropriately linked to a parent)
                    //skip the pointer that got split though (i.e. the one that pushed the value that got us here)

                    ArrayList<BNode<T>> assignList = new ArrayList<>();
                    assignList.add(newLeft);
                    assignList.add(newRight);
                    for (int i = 0; i <= pointers.length - 1; i++) {
                        //skip the pointer that pushed this value
                        if (pointers[i] != null && pointers[i].getIndexOf(value) < 0) assignList.add(pointers[i]);
                    }

                    //assign pointers
                    while (assignList.size() > 0) {
                        BNode<T> toAssign = getSmallestNode(assignList);
                        if (resetLeft.getPointer(0) == null) resetLeft.setPointer(0, toAssign);
                        else if (resetLeft.getPointer(1) == null) resetLeft.setPointer(1, toAssign);
                        else if (resetLeft.getPointer(2) == null) resetLeft.setPointer(2, toAssign);
                        else if (resetRight.getPointer(1) == null) resetRight.setPointer(1, toAssign);
                        else if (resetRight.getPointer(2) == null) resetRight.setPointer(2, toAssign);
                        else throw new IllegalStateException("This shouldn't happen");
                        assignList.remove(toAssign);
                    }
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
            if (values[i] != null && values[i].compareTo(value) == 0) return i;
        }
        return -1;
    }

    public T getFirstVal() { return values[0]; }
    public T getMaxVal() { return values[numVals - 1]; }
    public T getVal(int i) { return values[i]; }
    public int getNumVals() { return numVals; }
    public Boolean isLeaf() { return isLeaf; }
    public void setParent(BNode<T> newPar) { parent = newPar; }
    public BNode<T> getParent() { return parent; }

    private BNode<T> getSmallestNode(ArrayList<BNode<T>> searchList) {
        BNode<T> smallest = searchList.get(0);
        for (int i = 1; i <= searchList.size() - 1; i++) {
            if (searchList.get(i).getFirstVal().compareTo(smallest.getFirstVal()) < 0) smallest = searchList.get(i);
        }
        return smallest;
    }

    private int getLessThanIdx(T value) {
        int i = 0;
        for (i = 0; i < values.length - 1; i++) {
            if (values[i] == null) break;
            if (values[i].compareTo(value) > 0) break;
        }
        return i;
    }
}
