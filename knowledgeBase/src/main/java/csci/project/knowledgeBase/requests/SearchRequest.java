package csci.project.knowledgeBase.requests;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest extends ReadRequest {
    private String entity;
    private List<Criteria> criteria;

    public SearchRequest(String newEntity) {
        entity = newEntity;
        criteria = new ArrayList<>();
    }

    public String getEntity () { return entity; }
    public void addCriteria (Criteria newCriteria) { criteria.add(newCriteria); }
    public List<Criteria> getCriteria() { return criteria; }
}
