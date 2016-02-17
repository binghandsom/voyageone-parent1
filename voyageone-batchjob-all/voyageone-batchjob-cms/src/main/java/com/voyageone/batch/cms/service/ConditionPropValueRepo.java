package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.dao.ConditionPropValueDao;
import com.voyageone.batch.cms.model.ConditionPropValueModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-9-24.
 */
@Repository
public class ConditionPropValueRepo {
    @Autowired
    private ConditionPropValueDao conditionPropValueDao;
    private static Map<String, List<ConditionPropValueModel>> propValueRepo;

    public ConditionPropValueRepo() {
        propValueRepo = new HashMap<>();
    }

    public void init() {
        List<ConditionPropValueModel> conditionPropValueModelList = conditionPropValueDao.selectAllConditionPropValue();

        if (conditionPropValueModelList != null)
        {
            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModelList) {
                List<ConditionPropValueModel> conditionPropValueModels = propValueRepo.get(encodeKey(conditionPropValueModel.getChannel_id(), conditionPropValueModel.getPlatform_prop_id()));
                if (conditionPropValueModels == null)
                {
                    conditionPropValueModels = new ArrayList<>();
                    put(conditionPropValueModel.getChannel_id(), conditionPropValueModel.getPlatform_prop_id(), conditionPropValueModels);
                }
                conditionPropValueModels.add(conditionPropValueModel);
            }
        }
    }

    private String encodeKey(String channelId, String platformPropId) {
        return channelId + "_" + platformPropId;
    }

    public void put(String channelId, String platformPropId, List<ConditionPropValueModel> value) {
        propValueRepo.put(encodeKey(channelId, platformPropId), value);
    }

    public List<ConditionPropValueModel> get(String channelId, String platformPropId) {
        if (propValueRepo.isEmpty()) {
            init();
        }
        return propValueRepo.get(encodeKey(channelId, platformPropId));
    }
}
