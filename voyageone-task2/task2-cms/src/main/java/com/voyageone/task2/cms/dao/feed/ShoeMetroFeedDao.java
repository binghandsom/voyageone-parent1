package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedShoeMetroBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoShoeMetroModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShoeMetroFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedShoeMetroBean record);

    int insertSelective(SuperFeedShoeMetroBean record);

    SuperFeedShoeMetroBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedShoeMetroBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedShoeMetroBean record);

    int updateByPrimaryKey(SuperFeedShoeMetroBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoShoeMetroModel> selectSuperfeedModel(Map colums);
}