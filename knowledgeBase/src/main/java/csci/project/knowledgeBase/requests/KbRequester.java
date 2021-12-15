package csci.project.knowledgeBase.requests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import csci.project.knowledgeBase.backend.KbManager;

public class KbRequester {
    private volatile KbManager knowledgeBase;

    public KbRequester(KbManager newKb) {
        knowledgeBase = newKb;
    }

    public void makeRequest(Request request) {
        if (request instanceof ReadRequest) {
            makeReadRequest((ReadRequest) request);
        }
        else {
            JsonObject ret = new JsonObject();
            ret.addProperty("err", request.getClass().getSimpleName() + " is not a valid request type");
            request.setResponse(ret);
        }
    }

    private void makeReadRequest(ReadRequest read) {
        if (read instanceof SearchRequest) {
            makeSearchRequest((SearchRequest) read);
        }
        else if (read instanceof GearQueryRequest) {
            makeQueryRequest((GearQueryRequest) read);
        }
        else {
            JsonObject ret = new JsonObject();
            ret.addProperty("err", read.getClass().getSimpleName() + " is not a valid request type");
            read.setResponse(ret);
        }
    }

    private void makeSearchRequest(SearchRequest search) {
        JsonArray results = knowledgeBase.doSearch(search, null);
        JsonObject ret = new JsonObject();

        ret.addProperty("count", results.size());
        ret.add("results", results);
        search.setResponse(ret);
    }

    private void makeQueryRequest(GearQueryRequest query) {
        JsonObject ret = knowledgeBase.doQuery(query);
        query.setResponse(ret);
    }
}
