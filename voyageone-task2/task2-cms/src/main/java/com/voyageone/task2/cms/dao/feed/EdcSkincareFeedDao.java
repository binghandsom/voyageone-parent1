package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedEdcSkincareBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoEdcSkincareModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/7/12.
 */
@Repository
public interface EdcSkincareFeedDao {

    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedEdcSkincareBean record);

    int insertSelective(SuperFeedEdcSkincareBean record);

    SuperFeedEdcSkincareBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedEdcSkincareBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedEdcSkincareBean record);

    int updateByPrimaryKey(SuperFeedEdcSkincareBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoEdcSkincareModel> selectSuperfeedModel(Map colums);
}
