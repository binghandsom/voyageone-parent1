package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedNewTargetBean;
import com.voyageone.task2.cms.bean.SuperFeedTargetBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfNewTargetModel;
import com.voyageone.task2.cms.model.CmsBtFeedInfoTargetModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface NewTargetFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedNewTargetBean record);

    int insertSelective(SuperFeedNewTargetBean record);

    SuperFeedNewTargetBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedNewTargetBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedNewTargetBean record);

    int updateByPrimaryKey(SuperFeedNewTargetBean record);

    void delFullBySku(List<String> itemIds);

    void insertFullBySku(List<String> itemIds);

    void updateFlagBySku(List<String> itemIds);

    void delete();

    List<CmsBtFeedInfNewTargetModel> selectSuperfeedModel(Map colums);
}