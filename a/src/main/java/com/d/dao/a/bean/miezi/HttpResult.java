package com.d.dao.a.bean.miezi;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JokAr on 16/9/17.
 */
public class HttpResult {

    private boolean error;
    private List<DataEntities> results;

    public boolean isError() {

        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<DataEntities> getResults() {
        return results;
    }

    public void setResults(List<DataEntities> results) {
        this.results = results;
    }

    public HttpResult(boolean error, List<DataEntities> results) {

        this.error = error;
        this.results = results;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }

    public HttpResult() {
    }
}
