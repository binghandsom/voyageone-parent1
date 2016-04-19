package com.voyageone.components.jumei.reponse;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by dell on 2016/3/29.
 */

public abstract class  JMResponse implements Serializable {
    private static final long serialVersionUID = 5014379068811962022L;
    String body;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) throws IOException {
        this.body = body;
    }
}
