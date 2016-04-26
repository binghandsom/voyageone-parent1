package com.voyageone.task2.cms.dao.feed;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.bean.SuperfeedBhfoBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoBhfoModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface BhfoFeedDao{
    int deleteByPrimaryKey(String inventoryNumber);

    int delete();

    int insertSelective(SuperfeedBhfoBean record);

    int insert(SuperfeedBhfoBean record);

    SuperfeedBhfoBean selectByPrimaryKey(String inventoryNumber);

    int updateByPrimaryKeySelective(SuperfeedBhfoBean record);

    int updateByPrimaryKeyWithBLOBs(SuperfeedBhfoBean record);

    int updateByPrimaryKey(SuperfeedBhfoBean record);

    List<CmsBtFeedInfoBhfoModel> selectSuperfeedModel(Map parmat);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

}