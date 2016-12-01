package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedFragranceNetBean;
import com.voyageone.task2.cms.bean.SuperFeedFryeBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoFragranceNetModel;
import com.voyageone.task2.cms.model.CmsBtFeedInfoFryeModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/7/5.
 */
@Repository
public interface FryeFeedDao {

    int deleteByPrimaryKey(String sku);

    int insert(SuperFeedFryeBean record);

    int insertSelective(SuperFeedFryeBean record);

    SuperFeedFryeBean selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(SuperFeedFryeBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedFryeBean record);

    int updateByPrimaryKey(SuperFeedFryeBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

}
