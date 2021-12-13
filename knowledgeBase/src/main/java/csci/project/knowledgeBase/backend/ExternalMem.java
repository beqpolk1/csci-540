package csci.project.knowledgeBase.backend;

interface ExternalMem {
    public String getNext();
    public String getPrev();
    public String getLogiAddr();
    public String getPhysAddr();
}
