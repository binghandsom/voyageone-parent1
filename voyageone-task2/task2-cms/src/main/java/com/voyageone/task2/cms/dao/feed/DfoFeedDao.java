package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedDfoBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoDfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DfoFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedDfoBean record);

    int insertSelective(SuperFeedDfoBean record);

    SuperFeedDfoBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedDfoBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedDfoBean record);

    int updateByPrimaryKey(SuperFeedDfoBean record);



    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoDfoModel> selectSuperfeedModel(Map colums);
}