package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedKitBagStockBean;
import org.springframework.stereotype.Repository;

/**
 * Created by gjl on 2017/2/16.
 */
@Repository
public interface KitBagFeedStockDao {

    int deleteByPrimaryKey(String variationId);

    int insert(SuperFeedKitBagStockBean record);

    int insertSelective(SuperFeedKitBagStockBean record);

    SuperFeedKitBagStockBean selectByPrimaryKey(String variationId);

    int updateByPrimaryKeySelective(SuperFeedKitBagStockBean record);

    int updateByPrimaryKey(SuperFeedKitBagStockBean record);
}
