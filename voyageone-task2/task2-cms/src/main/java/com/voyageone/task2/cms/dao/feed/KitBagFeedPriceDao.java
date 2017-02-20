package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedKitBagPriceBean;
import org.springframework.stereotype.Repository;

/**
 * Created by gjl on 2017/2/16.
 */
@Repository
public interface KitBagFeedPriceDao {

    int deleteByPrimaryKey(String variationid);

    int insert(SuperFeedKitBagPriceBean record);

    int insertSelective(SuperFeedKitBagPriceBean record);

    SuperFeedKitBagPriceBean selectByPrimaryKey(String variationid);

    int updateByPrimaryKeySelective(SuperFeedKitBagPriceBean record);

    int updateByPrimaryKey(SuperFeedKitBagPriceBean record);
}
