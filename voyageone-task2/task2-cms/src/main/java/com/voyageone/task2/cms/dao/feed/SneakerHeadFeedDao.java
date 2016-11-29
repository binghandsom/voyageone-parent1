package com.voyageone.task2.cms.dao.feed;

import com.voyageone.task2.cms.bean.SuperFeedShoeZooBean;
import com.voyageone.task2.cms.bean.SuperFeedSneakerHeadBean;
import com.voyageone.task2.cms.model.CmsBtFeedInfoShoeZooModel;
import com.voyageone.task2.cms.model.CmsBtFeedInfoSneakerHeadModel;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/11/15.
 */
@Repository
public interface SneakerHeadFeedDao {
    int deleteByPrimaryKey(String variationParentSku);

    int insert(SuperFeedSneakerHeadBean record);

    int insertSelective(SuperFeedSneakerHeadBean record);

    SuperFeedSneakerHeadBean selectByPrimaryKey(String variationParentSku);

    int updateByPrimaryKeySelective(SuperFeedSneakerHeadBean record);

    int updateByPrimaryKeyWithBLOBs(SuperFeedSneakerHeadBean record);

    int updateByPrimaryKey(SuperFeedShoeZooBean record);

    int updateFlagBySku(List<String> skuList);

    int insertFullBySku(List<String> skuList);

    int delFullBySku(List<String> skuList);

    void delete();

    List<CmsBtFeedInfoSneakerHeadModel> selectSuperFeedModel(Map colums);

    int fullCopyTemp();

    int updateMd5();

    int updateUpdateFlag();

    Date selectSuperFeedModelDate();
}