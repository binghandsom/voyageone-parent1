package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedYogaDemocracyBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoYogaDemocracyModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface YogaDemocracyFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedYogaDemocracyBean record);

    int insertSelective(SuperFeedYogaDemocracyBean record);

    SuperFeedYogaDemocracyBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedYogaDemocracyBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedYogaDemocracyBean record);

    int updateByPrimaryKey(SuperFeedYogaDemocracyBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoYogaDemocracyModel> selectSuperfeedModel(Map colums);
}