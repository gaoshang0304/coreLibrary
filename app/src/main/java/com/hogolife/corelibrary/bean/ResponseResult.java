package com.hogolife.corelibrary.bean;

import java.util.List;

/**
 * 干货集中营 相应结果
 *
 * @author gjc
 * @since 2018-04-08
 */

public class ResponseResult {

    private boolean error;
    private List<GankListBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GankListBean> getResults() {
        return results;
    }

    public void setResults(List<GankListBean> results) {
        this.results = results;
    }
}
