package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedModotexBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoModotexModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ModotexFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedModotexBean record);

    int insertSelective(SuperFeedModotexBean record);

    SuperFeedModotexBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedModotexBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedModotexBean record);

    int updateByPrimaryKey(SuperFeedModotexBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoModotexModel> selectSuperfeedModel(Map colums);
}