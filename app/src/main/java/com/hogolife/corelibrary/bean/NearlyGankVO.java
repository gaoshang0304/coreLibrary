package com.hogolife.corelibrary.bean;

import java.util.List;
import java.util.Map;

/**
 * 最新一天干货
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-01-11
 */

public class NearlyGankVO {

    private List<String> category;

    private boolean error;

    private Map<String, List<GankListBean>> results;

    public Map<String, List<GankListBean>> getResults() {
        return results;
    }

    public void setResults(Map<String, List<GankListBean>> results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
