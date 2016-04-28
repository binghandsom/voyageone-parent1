package com.voyageone.service.dao.cms;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.List;

@Repository
public interface CmsBtJmSkuDao {
     public List<CmsBtJmSkuModel> selectList(Map<String, Object> map);
    public CmsBtJmSkuModel selectOne(Map<String, Object> map);
    public CmsBtJmSkuModel select(long id);
    public int insert(CmsBtJmSkuModel entity);
    public int update(CmsBtJmSkuModel entity);
    public int delete(long id);
    }
