package com.voyageone.task2.cms.dao.feed;
import com.voyageone.task2.cms.bean.SuperFeedWmfBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoWmfModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/12/5.
 */
@Repository
public interface WmfFeedDao{

    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedWmfBean record);

    int insertSelective(SuperFeedWmfBean record);

    SuperFeedWmfBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedWmfBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedWmfBean record);

    int updateByPrimaryKey(SuperFeedWmfBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoWmfModel> selectSuperFeedModel(Map column);
}
