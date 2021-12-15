package csci.project.knowledgeBase.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

class Reconciler {
    public static Boolean reconcile(JsonArray usedFacts, JsonArray availFacts) {
        for (int i = 0; i <= usedFacts.size() - 1; i++) {
            if (!availFacts.contains(new JsonPrimitive(usedFacts.get(i).getAsInt()))) {
                System.out.println(">>>>>SYSTEM WARNING - RECONCILE FAILED");
                return false;
            }
        }
        return true;
    }
}
