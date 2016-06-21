package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedShoeZooBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoShoeZooModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShoeZooFeedDao {
    int deleteByPrimaryKey(String variationParentSku);

    int insert(SuperFeedShoeZooBean record);

    int insertSelective(SuperFeedShoeZooBean record);

    SuperFeedShoeZooBean selectByPrimaryKey(String variationParentSku);

    int updateByPrimaryKeySelective(SuperFeedShoeZooBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedShoeZooBean record);

    int updateByPrimaryKey(SuperFeedShoeZooBean record);
    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoShoeZooModel> selectSuperfeedModel(Map colums);
}