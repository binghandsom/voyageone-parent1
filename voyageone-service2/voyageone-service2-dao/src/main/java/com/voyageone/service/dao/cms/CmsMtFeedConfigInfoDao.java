package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeedConfigInfoModel;
import org.springframework.stereotype.Repository;

/**
 * Created by gjl on 2016/12/27.
 */
@Repository
public interface CmsMtFeedConfigInfoDao {

    int deleteByPrimaryKey(Integer id);

    int insert(CmsMtFeedConfigInfoModel record);

    int insertSelective(CmsMtFeedConfigInfoModel record);

    CmsMtFeedConfigInfoModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsMtFeedConfigInfoModel record);

    int updateByPrimaryKeyWithBLOBs(CmsMtFeedConfigInfoModel record);

    int updateByPrimaryKey(CmsMtFeedConfigInfoModel record);
}
