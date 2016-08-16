package com.voyageone.service.dao.cms.mongo;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoCartDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingDeprecatedModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author james.li, 2015/12/7.
 * @author Jonas, 2016/01/11.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtPlatformMappingDao extends BaseMongoCartDao<CmsMtPlatformMappingDeprecatedModel> {

    public CmsMtPlatformMappingDeprecatedModel selectMapping(String channelId, int cartId, String platformCategoryId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",platformCartId:%s" +
                ",platformCategoryId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, platformCategoryId);
        return selectOneWithQuery(queryStr, cartId);
    }

    public long isExist(String channelId, int cartId, String platformCategoryId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",platformCartId:%s" +
                ",platformCategoryId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, platformCategoryId);
        return countByQuery(queryStr, cartId);
    }

    public CmsMtPlatformMappingDeprecatedModel selectMappingByMainCatId(String channelId, int cartId, String mainCatId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",platformCartId:%s" +
                ",mainCategoryId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, mainCatId);
        return selectOneWithQuery(queryStr, cartId);
    }

    public List<CmsMtPlatformMappingDeprecatedModel> selectMappings(String channelId, int catId) {

        return select(String.format("{ channelId: '%s', platformCartId: %s }", channelId, catId), catId);
    }

    public WriteResult insertPlatformMapping(CmsMtPlatformMappingDeprecatedModel cmsMtPlatformMappingModel) {
        return insert(cmsMtPlatformMappingModel);
    }

    // 20160506 tom 这个功能不需要, 删掉 START
//    public List<CmsMtPlatformMappingDeprecatedModel> selectMappingByMainCatId(String channelId, String mainCatId) {
//
//        return select(String.format("{ channelId: '%s', mainCategoryId: '%s' }", channelId, mainCatId));
//    }
    // 20160506 tom 这个功能不需要, 删掉 END

    /**
     * 最精确查找平台类目的匹配关系
     *
     * @param mainCategoryId     主数据类目 ID
     * @param platformCategoryId 平台类目 ID
     * @param cartId             平台 ID
     * @param channelId         渠道
     * @return 模型
     */
    public CmsMtPlatformMappingDeprecatedModel selectMapping(String mainCategoryId, String platformCategoryId, int cartId, String channelId) {
        return selectOneWithQuery(String.format(
                "{ channelId: '%s', mainCategoryId: '%s', platformCartId: %s, platformCategoryId: '%s' }",
                channelId, mainCategoryId, cartId, platformCategoryId
        ), cartId);
    }
}
