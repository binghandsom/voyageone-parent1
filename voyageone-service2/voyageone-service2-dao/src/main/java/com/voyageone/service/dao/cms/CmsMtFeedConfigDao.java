package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtFeedConfigModel;
import org.springframework.stereotype.Repository;

/**
 * Created by gjl on 2016/12/21.
 */
@Repository
public interface CmsMtFeedConfigDao {

    int deleteByPrimaryKey(Integer id);

    int insert(CmsMtFeedConfigModel record);

    int insertSelective(CmsMtFeedConfigModel record);

    CmsMtFeedConfigModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CmsMtFeedConfigModel record);

    int updateByPrimaryKeyWithBLOBs(CmsMtFeedConfigModel record);

    int updateByPrimaryKey(CmsMtFeedConfigModel record);
}
