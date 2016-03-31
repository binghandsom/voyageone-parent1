package com.voyageone.service.impl.jumei;
import com.voyageone.common.util.MapCamel;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionDaoExt;
import com.voyageone.service.model.jumei.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionService {
    @Autowired
    CmsBtJmPromotionDao dao;
    @Autowired
    CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand;
    @Autowired
    CmsBtJmPromotionDaoExt daoExt;

    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>();
        List<CmsBtJmMasterBrandModel> jmMasterBrandList = daoCmsBtJmMasterBrand.selectList();
        map.put("jmMasterBrandList", jmMasterBrandList);
        return map;
    }

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

    public List<MapCamel> getListByWhere(Map<String, Object> map) {
        if (map.containsKey("state1") && map.get("state1") == "false")//待进行
        {
            map.remove("state1");  //小于开始时间
        }
        if (map.containsKey("state2") && map.get("state1") == "false")//进行中
        {
            map.remove("state2"); // 当前时间大于开始时间 小于结束时间
        }
        if (map.containsKey("state3") && map.get("state1") == "false")//完成
        {
            map.remove("state3"); //当前时间大于结束时间
        }
        return daoExt.getListByWhere(map);
    }
}

