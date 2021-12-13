package csci.project.knowledgeBase.utils;

class IdGen {
    Integer generator;

    public IdGen () { generator = Integer.MIN_VALUE; }
    public Integer getId() { return generator++; }
}
