package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedOverStockBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoOverStockModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OverStockFeedDao {
    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedOverStockBean record);

    int insertSelective(SuperFeedOverStockBean record);

    SuperFeedOverStockBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedOverStockBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedOverStockBean record);

    int updateByPrimaryKey(SuperFeedOverStockBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    int deleteBySku(SuperFeedOverStockBean record);

    List<CmsBtFeedInfoOverStockModel> selectSuperfeedModel(Map colums);

    int fullCopyTemp();

    int updateMd5();

    int updateUpdateFlag();
}