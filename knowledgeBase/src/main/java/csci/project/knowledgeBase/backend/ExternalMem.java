package csci.project.knowledgeBase.backend;

public interface ExternalMem {
    public String getNext();
    public String getPrev();
    public String getLogiAddr();
    public String getPhysAddr();
}
