package com.voyageone.service.impl.cms.jumei;

import com.voyageone.service.dao.jumei.CmsMtJmCategoryDao;
import com.voyageone.service.model.jumei.CmsMtJmCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmCategoryService {
    @Autowired
    CmsMtJmCategoryDao dao;

    public CmsMtJmCategoryModel select(long id) {
        return dao.select(id);
    }

    public int update(CmsMtJmCategoryModel entity) {
        return dao.update(entity);
    }

    public int create(CmsMtJmCategoryModel entity) {
        return dao.insert(entity);
    }

    public List<CmsMtJmCategoryModel> selectList(Map<String, Object> param) {
        return dao.selectList(param);
    }

    public List<CmsMtJmCategoryModel> selectListByCode(String code) {
        Map<String, Object> param = new HashMap<>();

        param.put("code", code);

        return this.selectList(param);
    }

    public List<CmsMtJmCategoryModel> selectAll() {
        Map<String, Object> param = new HashMap<>();
        return dao.selectList(param);
    }

}

