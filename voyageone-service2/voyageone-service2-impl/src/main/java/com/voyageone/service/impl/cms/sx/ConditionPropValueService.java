package com.voyageone.service.impl.cms.sx;

import com.voyageone.service.dao.cms.CmsMtChannelConditionConfigDao;
import com.voyageone.service.model.cms.CmsMtChannelConditionConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by morse.lu on 16-5-12. (copy and modified from task2 / ConditionPropValueRepo)
 */
@Repository
public class ConditionPropValueService {
    @Autowired
    private CmsMtChannelConditionConfigDao cmsMtChannelConditionConfigDao;
    private static Map<String, List<CmsMtChannelConditionConfigModel>> propValueRepo;

    public ConditionPropValueService() {
        propValueRepo = new HashMap<>();
    }

    public void init() {
        List<CmsMtChannelConditionConfigModel> conditionPropValueModelList = cmsMtChannelConditionConfigDao.selectList(null);

        if (conditionPropValueModelList != null)
        {
            for (CmsMtChannelConditionConfigModel conditionPropValueModel : conditionPropValueModelList) {
                List<CmsMtChannelConditionConfigModel> conditionPropValueModels = propValueRepo.get(encodeKey(conditionPropValueModel.getChannelId(), conditionPropValueModel.getPlatformPropId()));
                if (conditionPropValueModels == null)
                {
                    conditionPropValueModels = new ArrayList<>();
                    put(conditionPropValueModel.getChannelId(), conditionPropValueModel.getPlatformPropId(), conditionPropValueModels);
                }
                conditionPropValueModels.add(conditionPropValueModel);
            }
        }
    }

    private String encodeKey(String channelId, String platformPropId) {
        return channelId + "_" + platformPropId;
    }

    public void put(String channelId, String platformPropId, List<CmsMtChannelConditionConfigModel> value) {
        propValueRepo.put(encodeKey(channelId, platformPropId), value);
    }

    public List<CmsMtChannelConditionConfigModel> get(String channelId, String platformPropId) {
        if (propValueRepo.isEmpty()) {
            init();
        }
        return propValueRepo.get(encodeKey(channelId, platformPropId));
    }
}
