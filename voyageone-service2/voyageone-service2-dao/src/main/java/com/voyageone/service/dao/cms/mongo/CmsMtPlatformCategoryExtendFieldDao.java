package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoCartDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryExtendFieldModel;
import org.springframework.stereotype.Repository;

/**
 * 平台Schema对应的一些想要重构的属性的表的Dao
 * 重构：不想显示表(CmsMtPlatformCategoryInvisibleField)追加field_id='city',本表同步追加field_id='city'
 *
 * @author morse.lu 2016/06/21
 * @version 2.1.0
 * @since 2.1.0
 */
@Repository
public class CmsMtPlatformCategoryExtendFieldDao extends BaseMongoCartDao<CmsMtPlatformCategoryExtendFieldModel> {

    public CmsMtPlatformCategoryExtendFieldModel selectOneByCatId(String catId, int cartId) {
        JomgoQuery query = new JomgoQuery();
        query.setQuery("{\"catId\":#}");
        query.setParameters(catId);
        return selectOneWithQuery(query, cartId);
    }
}
