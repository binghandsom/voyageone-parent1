package com.voyageone.service.dao.cms;

import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/4/19.
 * @version 2.0.0
 */
@Repository
public interface CmsMtChannelValuesDao {

    public List<CmsMtChannelValuesModel> selectList(Map<String, Object> map);
    public int insert(CmsMtChannelValuesModel entity);
    public int update(CmsMtChannelValuesModel entity);

}
