package com.voyageone.web2.core.dao;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/3
 */
@Repository
public class ChannelShopDao extends WebBaseDao {

    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CORE;
    }

    /**
     * 根据channelId获取CategoryType列表.
     * @param channelId
     * @return
     */
    public List<Map<String, Object>> selectChannelShop(String channelId) {
        return selectList("tm_channel_shop_selectCartListByChannelId", channelId);
    }
}
