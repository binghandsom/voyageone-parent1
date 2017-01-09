package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedChampionBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoChampionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/12/9.
 */
@Repository
public interface ChampionFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedChampionBean record);

    int insertSelective(SuperFeedChampionBean record);

    SuperFeedChampionBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedChampionBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedChampionBean record);

    int updateByPrimaryKey(SuperFeedChampionBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoChampionModel> selectSuperFeedModel(Map column);
}
