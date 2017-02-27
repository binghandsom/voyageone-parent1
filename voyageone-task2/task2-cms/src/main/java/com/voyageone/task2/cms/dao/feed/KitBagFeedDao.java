package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedKitBagBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoKitBagModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2017/2/15.
 */
@Repository
public interface KitBagFeedDao {

    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedKitBagBean record);

    int insertSelective(SuperFeedKitBagBean record);

    SuperFeedKitBagBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedKitBagBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedKitBagBean record);

    int updateByPrimaryKey(SuperFeedKitBagBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoKitBagModel> selectSuperFeedModel(Map column);
}
