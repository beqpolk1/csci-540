public class Block implements ExternalMem {
    private String physAddr;
    private Block prev, next;

    public Block(String newAddr, Block newPrev, Block newNext) {
        physAddr = newAddr;
        prev = newPrev;
        next = newNext;
    }

    public Block getNext() { return next; }
    public Block getPrev() { return prev; }
    public String getPhysAddr() { return physAddr; }
}
