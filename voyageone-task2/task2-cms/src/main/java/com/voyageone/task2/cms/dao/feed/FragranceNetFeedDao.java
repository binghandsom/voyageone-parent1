package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedFragranceNetBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoFragranceNetModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/7/5.
 */
@Repository
public interface FragranceNetFeedDao {

    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedFragranceNetBean record);

    int insertSelective(SuperFeedFragranceNetBean record);

    SuperFeedFragranceNetBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedFragranceNetBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedFragranceNetBean record);

    int updateByPrimaryKey(SuperFeedFragranceNetBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoFragranceNetModel> selectSuperfeedModel(Map colums);
}
