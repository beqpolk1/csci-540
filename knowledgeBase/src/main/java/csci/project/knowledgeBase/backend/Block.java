package csci.project.knowledgeBase.backend;

class Block implements ExternalMem {
    private String logiAddr, physAddr, prev, next;

    public Block(String newLogi, String newPhys, String newPrev, String newNext) {
        logiAddr = newLogi;
        physAddr = newPhys;
        prev = newPrev;
        next = newNext;
    }

    public String getNext() { return next; }
    public String getPrev() { return prev; }
    public String getLogiAddr() { return logiAddr; }
    public String getPhysAddr() { return physAddr; }
}
