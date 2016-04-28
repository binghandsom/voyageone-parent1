package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsMtMasterInfoModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsMtMasterInfoDao {
     public List<CmsMtMasterInfoModel> selectList(Map<String, Object> map);
    public CmsMtMasterInfoModel selectOne(Map<String, Object> map);
    public CmsMtMasterInfoModel select(long id);
    public int insert(CmsMtMasterInfoModel entity);
    public int update(CmsMtMasterInfoModel entity);
    public int delete(long id);
    }
