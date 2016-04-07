package com.voyageone.common.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.tmall.base.TbCommon;
import com.voyageone.common.configs.beans.ShopBean;

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
