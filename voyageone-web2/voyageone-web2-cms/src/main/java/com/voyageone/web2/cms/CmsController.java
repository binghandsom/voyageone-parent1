package com.voyageone.web2.cms;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.cms.bean.CmsSessionBean;

/**
 * @author Edward
 * @version 2.0.0, 15/12/3
 */
public abstract class CmsController extends BaseController{

    /**
     * 获取CMS相关session信息.
     */
    public CmsSessionBean getCmsSession() {
        CmsSessionBean cmsSessionBean = (CmsSessionBean)getSession().getAttribute(CmsConstants.SESSION_CMS);
        if(cmsSessionBean == null) {
            cmsSessionBean = new CmsSessionBean();
            setCmsSession(cmsSessionBean);
        }
        return cmsSessionBean;
    }

    /**
     * 保存CMS相关的session信息.
     * @param cmsSession
     */
    public void setCmsSession(CmsSessionBean cmsSession) {
        getSession().setAttribute(CmsConstants.SESSION_CMS, cmsSession);
    }
}
