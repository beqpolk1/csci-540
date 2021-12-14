package csci.project.knowledgeBase.utils;

class IdGen {
    Integer generator;

    public IdGen () { this(Integer.MIN_VALUE); }
    public IdGen (Integer seed) { generator = seed; }
    public Integer getId() { return generator++; }
}
