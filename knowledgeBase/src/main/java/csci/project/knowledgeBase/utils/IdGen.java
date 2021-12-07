package csci.project.knowledgeBase.utils;

public class IdGen {
    Integer generator;

    public IdGen () { generator = Integer.MIN_VALUE; }
    public Integer getId() { return generator++; }
}
