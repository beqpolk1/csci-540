package csci.project.knowledgeBase.requests;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import csci.project.knowledgeBase.backend.KbManager;

public class KbRequester {
    private volatile KbManager knowledgeBase;
    private boolean injectTest;

    public KbRequester(KbManager newKb, boolean newInjectTest) {
        injectTest = newInjectTest;
        knowledgeBase = newKb;
    }

    public void makeRequest(Request request) {
        if (request instanceof ReadRequest) {
            makeReadRequest((ReadRequest) request);
        }
        else if (request instanceof DmlRequest) {
            makeDmlRequest((DmlRequest) request);
        }
        else {
            JsonObject ret = new JsonObject();
            ret.addProperty("err", request.getClass().getSimpleName() + " is not a valid request type");
            request.setResponse(ret);
        }
    }

    public void setInjectTest(boolean newVal) { injectTest = newVal; }

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
        JsonArray results = knowledgeBase.doSearch(search, null, -1);
        JsonObject ret = new JsonObject();

        ret.addProperty("count", results.size());
        ret.add("results", results);
        search.setResponse(ret);
    }

    private void makeQueryRequest(GearQueryRequest query) {
        JsonObject ret = knowledgeBase.doQuery(query, injectTest);
        query.setResponse(ret);
    }

    private void makeDmlRequest(DmlRequest dml) {
        if (dml instanceof DeleteRequest) {
            makeDeleteRequest((DeleteRequest) dml);
        }
        else {
            JsonObject ret = new JsonObject();
            ret.addProperty("err", dml.getClass().getSimpleName() + " is not a valid request type");
            dml.setResponse(ret);
        }
    }

    private void makeDeleteRequest(DeleteRequest delete) {
        JsonObject ret = knowledgeBase.doDelete((DeleteRequest) delete);
        delete.setResponse(ret);
    }
}
