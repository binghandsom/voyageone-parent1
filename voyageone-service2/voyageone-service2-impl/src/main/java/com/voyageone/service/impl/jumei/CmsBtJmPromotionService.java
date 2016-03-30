package com.voyageone.service.impl.jumei;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionService {
    @Autowired
    CmsBtJmPromotionDao dao;

    public CmsBtJmPromotionModel select(int id) {
        return dao.select(id);
    }

    public List<CmsBtJmPromotionModel> selectList() {
        return dao.selectList();
    }

    public int update(CmsBtJmPromotionModel entity) {
        return dao.update(entity);
    }

    public int create(CmsBtJmPromotionModel entity) {
        return dao.insert(entity);
    }

    public List<CmsBtJmPromotionModel> getListByWhere(Map<String, Object> map) {
        return dao.selectList();
    }
}

