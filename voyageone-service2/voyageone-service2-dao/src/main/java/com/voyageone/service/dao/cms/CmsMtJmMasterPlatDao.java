package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsMtJmMasterPlatModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsMtJmMasterPlatDao {
     public List<CmsMtJmMasterPlatModel> selectList(Map<String, Object> map);
    public CmsMtJmMasterPlatModel selectOne(Map<String, Object> map);
    public CmsMtJmMasterPlatModel select(long id);
    public int insert(CmsMtJmMasterPlatModel entity);
    public int update(CmsMtJmMasterPlatModel entity);
    public int delete(long id);
    }
