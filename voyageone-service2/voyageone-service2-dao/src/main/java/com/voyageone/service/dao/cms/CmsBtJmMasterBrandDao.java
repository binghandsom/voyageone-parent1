package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmMasterBrandDao {
     public List<CmsBtJmMasterBrandModel> selectList(Map<String, Object> map);
    public CmsBtJmMasterBrandModel selectOne(Map<String, Object> map);
    public CmsBtJmMasterBrandModel select(long id);
    public int insert(CmsBtJmMasterBrandModel entity);
    public int update(CmsBtJmMasterBrandModel entity);
    public int delete(long id);
    }
