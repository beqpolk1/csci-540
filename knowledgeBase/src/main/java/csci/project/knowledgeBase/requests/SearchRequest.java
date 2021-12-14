package csci.project.knowledgeBase.requests;

import java.util.ArrayList;
import java.util.List;

public class SearchRequest extends ReadRequest {
    private List<Criteria> criteria;

    public SearchRequest(String newEntity) {
        super(newEntity);
        criteria = new ArrayList<>();
    }

    public void addCriteria (Criteria newCriteria) { criteria.add(newCriteria); }
    public List<Criteria> getCriteria() { return criteria; }
}
