package com.voyageone.web2.cms;

import com.voyageone.web2.base.BaseConstants;

/**
 * @author Edward
 * @version 2.0.0, 15/12/3
 */
public interface CmsConstants extends BaseConstants{

    /**
     * Session 内CMS信息存放的 Key
     */
    String SESSION_CMS = "voyageone.session.cms";

    /**
     * 默认的 CategoryType值 : TG
     */
    String DEFAULT_CATEGORY_TYPE = "TG";

    /**
     * 默认的 CartId值 : 23
     */
    Integer DEFAULT_CART_ID = 23;

    /**
     * Property 文件Key
     */
    interface Props {

        //  Code 文件模板文件
        String CODE_TEMPLATE = "cms.code.template.file";

        String PROMOTION_EXPORT_TEMPLATE = "cms.promotion.export.file";
    }
}
