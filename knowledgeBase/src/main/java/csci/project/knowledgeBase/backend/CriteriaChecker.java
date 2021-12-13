package csci.project.knowledgeBase.backend;

import com.google.gson.JsonObject;
import csci.project.knowledgeBase.requests.Criteria;

import java.util.List;

class CriteriaChecker {
    public static Boolean matchesCriteria(JsonObject testObj, List<Criteria> criteria) {
        for (Criteria curCond : criteria) {
            if (!curCond.check(testObj)) return false;
        }
        return true;
    }
}
