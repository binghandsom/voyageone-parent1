package com.voyageone.service.impl.jumei;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.CmsMtJmConfigDaoExt;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.jumei.businessmodel.JMDefaultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsMtJmConfigService {
    @Autowired
    CmsMtJmConfigDao dao;
    @Autowired
    CmsMtJmConfigDaoExt daoExt;

    public CmsMtJmConfigModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsMtJmConfigModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsMtJmConfigModel entity) {
        return dao.insert(entity);
    }

    public CmsMtJmConfigModel getByKey(String channelId,String key) {
        Map<String,Object> map=new HashMap<>();
        map.put("channelId",channelId);
        map.put("key",key);
       return dao.selectOne(map);
    }

    public JMDefaultSet getJMDefaultSet(String channelId) throws Exception {
        CmsMtJmConfigModel model = getByKey(channelId, "JM_IMAGE_SETTING");
        JMDefaultSet jmDefaultSet = JacksonUtil.ToObjectFromJson(model.getValue(), JMDefaultSet.class);

        return  jmDefaultSet;
    }
}

