package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsMtCustomWordDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtCustomWord;
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
    private CmsMtCustomWordDao customWordDao;

    public List<CmsMtCustomWord> getModels() {
        return customWordDao.selectWithParam();
    }

    public List<Map<String, Object>> getTransLenSet(String chnId) {
        return customWordDao.selectTransLenSet(chnId);
    }
}
