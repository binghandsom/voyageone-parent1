package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoCartDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryInvisibleFieldModel;
import org.springframework.stereotype.Repository;

/**
 * 平台Schema对应的不想显示在画面上的属性的表的Dao
 * catId=0为全类目共通
 *
 * @author morse.lu 2016/06/21
 * @version 2.1.0
 * @since 2.1.0
 */
@Repository
public class CmsMtPlatformCategoryInvisibleFieldDao extends BaseMongoCartDao<CmsMtPlatformCategoryInvisibleFieldModel> {

    public CmsMtPlatformCategoryInvisibleFieldModel selectOneByCatId(String catId, int cartId) {
        JomgoQuery query = new JomgoQuery();
        query.setQuery("{\"catId\":#}");
        query.setParameters(catId);
        return selectOneWithQuery(query, cartId);
    }
}
