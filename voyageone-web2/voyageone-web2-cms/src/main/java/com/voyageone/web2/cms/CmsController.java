package com.voyageone.web2.cms;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.cms.bean.CmsSessionBean;

/**
 * @author Edward, 15/12/3
 * @version 2.0.0
 */
public abstract class CmsController extends BaseController {

    /**
     * Session 内CMS信息存放的 Key
     */
    public final String SESSION_CMS = "voyageone.session.cms";

    /**
     * 获取CMS相关session信息.
     */
    public CmsSessionBean getCmsSession() {
        CmsSessionBean cmsSessionBean = (CmsSessionBean) getSession().getAttribute(SESSION_CMS);
        if (cmsSessionBean == null) {
            cmsSessionBean = new CmsSessionBean();
            setCmsSession(cmsSessionBean);
        }
        return cmsSessionBean;
    }

    /**
     * 保存CMS相关的session信息.
     */
    public void setCmsSession(CmsSessionBean cmsSession) {
        getSession().setAttribute(SESSION_CMS, cmsSession);
    }
}
