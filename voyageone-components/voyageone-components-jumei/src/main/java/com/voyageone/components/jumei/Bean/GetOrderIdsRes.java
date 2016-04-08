package com.voyageone.components.jumei.Bean;

import java.util.List;

/**
 * Created by sn3 on 2015-07-20.
 */
public class GetOrderIdsRes extends JmBaseBean {

    private String error;
    private List<String> result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
