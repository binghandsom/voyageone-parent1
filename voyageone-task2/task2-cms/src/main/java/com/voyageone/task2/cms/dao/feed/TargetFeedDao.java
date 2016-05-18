package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedTargetBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoTargetModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TargetFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedTargetBean record);

    int insertSelective(SuperFeedTargetBean record);

    SuperFeedTargetBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedTargetBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedTargetBean record);

    int updateByPrimaryKey(SuperFeedTargetBean record);

    void delFullBySku(List<String> itemIds);

    void insertFullBySku(List<String> itemIds);

    void updateFlagBySku(List<String> itemIds);

    void delete();

    List<CmsBtFeedInfoTargetModel> selectSuperfeedModel(Map colums);
}