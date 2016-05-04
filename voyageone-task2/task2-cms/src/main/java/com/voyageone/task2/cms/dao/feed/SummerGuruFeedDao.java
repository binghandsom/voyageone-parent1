package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperfeedSummerGuruBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoSummerGuruModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SummerGuruFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperfeedSummerGuruBean record);

    int insertSelective(SuperfeedSummerGuruBean record);

    SuperfeedSummerGuruBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperfeedSummerGuruBean record);

    int updateByPrimaryKeyWithBLOBs(SuperfeedSummerGuruBean record);

    int updateByPrimaryKey(SuperfeedSummerGuruBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoSummerGuruModel> selectSuperfeedModel(Map colums);
}