package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCommonSchemaModel;
import org.springframework.stereotype.Repository;

/**
 * cms_mt_platform_common_schema 表的操作类
 * <p>
 * Created by jonas on 8/17/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Repository
public class CmsMtPlatformCommonSchemaDao extends BaseMongoDao<CmsMtPlatformCommonSchemaModel> {

    public CmsMtPlatformCommonSchemaModel selectOne(int cartId) {
        return selectOneWithQuery(JongoQuery.simple("cartId", cartId));
    }
}
