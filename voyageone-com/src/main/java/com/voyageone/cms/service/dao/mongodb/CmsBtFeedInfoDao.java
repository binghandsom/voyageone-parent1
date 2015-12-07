package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by james.li on 2015/11/27.
 */

@Repository
public class CmsBtFeedInfoDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtFeedInfoModel.class;
    }

    public CmsBtFeedInfoModel selectProductByCode(String channelId, String code) {
        String query = "{\"code\":\"" + code + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 根据updFlg来获取商品列表(updFlg=0: 等待反映到主数据, updFlg=1: 已经反映到主数据)
     * @param channelId channel id
     * @param updFlg updFlg=0: 等待反映到主数据, updFlg=1: 已经反映到主数据
     * @return 商品列表
     */
    public List<CmsBtFeedInfoModel> selectProductByUpdFlg(String channelId, int updFlg) {
        String query = String.format("{ channelId: '%s', updFlg: %s}", channelId, updFlg);

        return select(query, channelId);
    }
}
