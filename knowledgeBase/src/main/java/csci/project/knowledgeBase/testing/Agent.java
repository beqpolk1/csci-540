package csci.project.knowledgeBase.testing;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private List<TestAction> actions;

    public Agent () {
        actions = new ArrayList<>();
    }

    public void addAction(TestAction newAction) {
        actions.add(newAction);
    }

    public List<TestAction> getActions() { return actions; }
}
