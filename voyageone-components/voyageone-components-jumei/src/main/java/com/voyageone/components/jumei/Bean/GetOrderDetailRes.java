package com.voyageone.components.jumei.Bean;

/**
 * Created by sn3 on 2015-07-18.
 */
public class GetOrderDetailRes extends JmBaseBean {

    private String error;

    private JmOrderInfo result;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public JmOrderInfo getResult() {
        return result;
    }

    public void setResult(JmOrderInfo result) {
        this.result = result;
    }
}
