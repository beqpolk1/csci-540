package csci.project.knowledgeBase.requests;

import java.util.ArrayList;
import java.util.List;

public class DeleteRequest extends DmlRequest{
    private String entity;
    private List<Criteria> criteria;

    public DeleteRequest(String newEntity) {
        entity = newEntity;
        criteria = new ArrayList<>();
    }

    public String getEntity () { return entity; }

    public void addCriteria (Criteria newCriteria) { criteria.add(newCriteria); }
    public List<Criteria> getCriteria() { return criteria; }
}
