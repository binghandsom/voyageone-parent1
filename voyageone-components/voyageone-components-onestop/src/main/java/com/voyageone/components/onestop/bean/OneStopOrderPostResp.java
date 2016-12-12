package com.voyageone.components.onestop.bean;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: zhen.wang
 * @date: 2016/11/23
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopOrderPostResp {

    private String Message;

    private Map<String,List<String>> ModelState;

    public boolean hasError() {
        //// TODO: 2016/11/23
        return false;
    }

    public void setMessage(String Message){
        this.Message = Message;
    }
    public String getMessage(){
        return this.Message;
    }

    public Map<String, List<String>> getModelState() {
        return ModelState;
    }

    public void setModelState(Map<String, List<String>> modelState) {
        ModelState = modelState;
    }



}
