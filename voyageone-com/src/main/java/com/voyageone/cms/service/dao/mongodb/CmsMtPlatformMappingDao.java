package com.voyageone.cms.service.dao.mongodb;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author james.li, 2015/12/7.
 * @author Jonas, 2016/01/11.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public class CmsMtPlatformMappingDao extends BaseMongoDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    @Override
    public Class getEntityClass() {
        return CmsMtPlatformMappingModel.class;
    }

    public CmsMtPlatformMappingModel getMapping(String channelId, int cartId, String platformCategoryId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",platformCartId:%s" +
                ",platformCategoryId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, platformCategoryId);
        return selectOneWithQuery(queryStr);
    }

    public long isExist(String channelId, int cartId, String platformCategoryId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",platformCartId:%s" +
                ",platformCategoryId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, platformCategoryId);
        return mongoTemplate.count(queryStr, collectionName);
    }

    public CmsMtPlatformMappingModel getMappingByMainCatId(String channelId, int cartId, String mainCatId) {
        String queryStrTemp = "{" +
                "channelId:'%s'" +
                ",platformCartId:%s" +
                ",mainCategoryId:'%s'" +
                "}";
        String queryStr = String.format(queryStrTemp, channelId, cartId, mainCatId);
        return selectOneWithQuery(queryStr);
    }

    public List<CmsMtPlatformMappingModel> selectMappings(ChannelConfigEnums.Channel channel, Integer catId) {

        return select(String.format("{ channelId: '%s', platformCartId: %s }", channel.getId(), catId));
    }

    public WriteResult insertPlatformMapping(CmsMtPlatformMappingModel cmsMtPlatformMappingModel) {
        return insert(cmsMtPlatformMappingModel);
    }

    public List<CmsMtPlatformMappingModel> getMappingByMainCatId(ChannelConfigEnums.Channel selChannel, String mainCategoryId) {

        return select(String.format("{ channelId: '%s', mainCategoryId: '%s' }", selChannel.getId(), mainCategoryId));
    }

    /**
     * 最精确查找平台类目的匹配关系
     *
     * @param mainCategoryId     主数据类目 ID
     * @param platformCategoryId 平台类目 ID
     * @param cartId             平台 ID
     * @param selChannel         渠道
     * @return 模型
     */
    public CmsMtPlatformMappingModel selectMapping(String mainCategoryId, String platformCategoryId, Integer cartId, ChannelConfigEnums.Channel selChannel) {
        return selectOneWithQuery(String.format(
                "{ channelId: '%s', mainCategoryId: '%s', platformCartId: %s, platformCategoryId: '%s' }",
                selChannel.getId(), mainCategoryId, cartId, platformCategoryId
        ));
    }
}
