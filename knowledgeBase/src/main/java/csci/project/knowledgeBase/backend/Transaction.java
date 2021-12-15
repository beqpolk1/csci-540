package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

public class Transaction {
    private Integer id;
    private JsonArray touchedFacts;

    public Transaction(Integer newId) {
        id = newId;
    }

    public Integer getId() { return id; }

    public void resetTouchedFacts() { touchedFacts = new JsonArray(); }
    public void addTouchedFact(Integer factId) {
        if (!touchedFacts.contains(new JsonPrimitive(factId))) touchedFacts.add(factId);
    }
    public JsonArray getTouchedFacts() { return touchedFacts; }
}
