package com.voyageone.components;

import com.voyageone.common.components.issueLog.IssueLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础类
 * <p/>
 * Created by jonas on 15/6/5.
 */
public abstract class ComponentBase {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected IssueLog issueLog;


}
