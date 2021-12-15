package csci.project.knowledgeBase.utils;

public class IdGen {
    Integer generator;

    public IdGen () { this(Integer.MIN_VALUE); }
    public IdGen (Integer seed) { generator = seed; }
    public Integer getId() { return generator++; }
    public Integer peek() { return generator + 1; }
}
