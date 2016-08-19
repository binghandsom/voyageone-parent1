package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedSwissWatchBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoSwissWatchModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/08/17.
 */
@Repository
public interface SwissWatchFeedDao {

    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedSwissWatchBean record);

    int insertSelective(SuperFeedSwissWatchBean record);

    SuperFeedSwissWatchBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedSwissWatchBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedSwissWatchBean record);

    int updateByPrimaryKey(SuperFeedSwissWatchBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoSwissWatchModel> selectSuperfeedModel(Map colums);
}
