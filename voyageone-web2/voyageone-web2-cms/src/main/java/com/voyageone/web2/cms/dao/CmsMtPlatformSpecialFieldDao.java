package com.voyageone.web2.cms.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import org.springframework.stereotype.Repository;

/**
 * @author Jonas, 1/26/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtPlatformSpecialFieldDao extends WebBaseDao {

    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CMS;
    }

    public String selectSpecialMappingType(Integer cartId, String platformCategoryId, String propertyId) {
        return selectOne("cms_mt_platform_special_field_selectSpecialMappingType",
                parameters("cartId", cartId, "platformCategoryId", platformCategoryId, "propertyId", propertyId));
    }
}
