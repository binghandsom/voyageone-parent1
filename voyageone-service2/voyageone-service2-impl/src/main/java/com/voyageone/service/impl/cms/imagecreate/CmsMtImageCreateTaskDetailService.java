package com.voyageone.service.impl.cms.imagecreate;

import com.voyageone.service.dao.cms.CmsMtImageCreateTaskDetailDao;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CmsMtImageCreateTaskDetailService {
    @Autowired
    CmsMtImageCreateTaskDetailDao dao;

    public List<CmsMtImageCreateTaskDetailModel> getListByCmsMtImageCreateTaskId(int CmsMtImageCreateTaskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("cmsMtImageCreateTaskId", CmsMtImageCreateTaskId);
        return dao.selectList(map);
    }
}
