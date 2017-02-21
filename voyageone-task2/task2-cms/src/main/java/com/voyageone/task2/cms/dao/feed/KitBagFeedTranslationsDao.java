package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedKitBagTranslationsBean;
import org.springframework.stereotype.Repository;

/**
 * Created by gjl on 2017/2/16.
 */
@Repository
public interface KitBagFeedTranslationsDao {

    int deleteByPrimaryKey(String productId);

    int insert(SuperFeedKitBagTranslationsBean record);

    int insertSelective(SuperFeedKitBagTranslationsBean record);

    SuperFeedKitBagTranslationsBean selectByPrimaryKey(String productId);

    int updateByPrimaryKeySelective(SuperFeedKitBagTranslationsBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedKitBagTranslationsBean record);

    int updateByPrimaryKey(SuperFeedKitBagTranslationsBean record);

}
