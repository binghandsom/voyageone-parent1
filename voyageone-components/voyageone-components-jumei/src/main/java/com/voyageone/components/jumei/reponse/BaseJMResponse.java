package com.voyageone.components.jumei.reponse;

import com.voyageone.common.components.issueLog.IssueLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * BaseJMResponse
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class BaseJMResponse implements Serializable {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected IssueLog issueLog;

    public String requestUrl;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
