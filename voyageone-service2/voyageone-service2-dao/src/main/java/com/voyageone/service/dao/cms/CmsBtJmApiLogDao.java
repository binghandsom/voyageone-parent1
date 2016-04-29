package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmApiLogModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmApiLogDao {
     public List<CmsBtJmApiLogModel> selectList(Map<String, Object> map);
    public CmsBtJmApiLogModel selectOne(Map<String, Object> map);
    public CmsBtJmApiLogModel select(long id);
    public int insert(CmsBtJmApiLogModel entity);
    public int update(CmsBtJmApiLogModel entity);
    public int delete(long id);
    }
