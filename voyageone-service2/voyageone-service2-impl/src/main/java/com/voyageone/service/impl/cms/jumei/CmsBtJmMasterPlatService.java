package com.voyageone.service.impl.cms.jumei;

import com.voyageone.service.dao.jumei.CmsMtJmMasterPlatDao;
import com.voyageone.service.model.jumei.CmsMtJmMasterPlatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmMasterPlatService {
    @Autowired
    CmsMtJmMasterPlatDao dao;

    public CmsMtJmMasterPlatModel select(long id) {
        return dao.select(id);
    }

    public int update(CmsMtJmMasterPlatModel entity) {
        return dao.update(entity);
    }

    public int create(CmsMtJmMasterPlatModel entity) {
        return dao.insert(entity);
    }

    public List<CmsMtJmMasterPlatModel> selectList(Map<String, Object> param) {
        return dao.selectList(param);
    }

    public List<CmsMtJmMasterPlatModel> selectListByCode(String code) {
        Map<String, Object> param = new HashMap<>();

        param.put("code", code);

        return this.selectList(param);
    }

    public List<CmsMtJmMasterPlatModel> selectAll() {
        Map<String, Object> param = new HashMap<>();
        return dao.selectList(param);
    }

}

