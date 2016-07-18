package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedLightHouseBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoLightHouseModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/7/5.
 */
@Repository
public interface LightHouseFeedDao {

    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedLightHouseBean record);

    int insertSelective(SuperFeedLightHouseBean record);

    SuperFeedLightHouseBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedLightHouseBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedLightHouseBean record);

    int updateByPrimaryKey(SuperFeedLightHouseBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoLightHouseModel> selectSuperfeedModel(Map colums);
}
