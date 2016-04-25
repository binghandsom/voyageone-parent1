package com.voyageone.service.impl.cms;

import com.voyageone.service.bean.cms.CmsMtCustomWordBean;
import com.voyageone.service.daoext.cms.CmsMtCustomWordDaoExt;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liang 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CustomWordService extends BaseService {

    @Autowired
    private CmsMtCustomWordDaoExt customWordDaoExt;

    public List<CmsMtCustomWordBean> getModels() {
        return customWordDaoExt.selectWithParam();
    }

    public List<Map<String, Object>> getTransLenSet(String chnId) {
        return customWordDaoExt.selectTransLenSet(chnId);
    }
}
