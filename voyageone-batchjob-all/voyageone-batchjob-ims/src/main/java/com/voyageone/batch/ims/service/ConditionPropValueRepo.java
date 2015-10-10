package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.dao.ConditionPropValueDao;
import com.voyageone.batch.ims.modelbean.ConditionPropValue;
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
    private static Map<String, List<ConditionPropValue>> propValueRepo;

    public ConditionPropValueRepo() {
        propValueRepo = new HashMap<>();
    }

    public void init() {
        List<ConditionPropValue> conditionPropValueList = conditionPropValueDao.selectAllConditionPropValue();

        if (conditionPropValueList != null)
        {
            for (ConditionPropValue conditionPropValue : conditionPropValueList) {
                List<ConditionPropValue> conditionPropValues = propValueRepo.get(encodeKey(conditionPropValue.getChannel_id(), conditionPropValue.getPlatform_prop_id()));
                if (conditionPropValues == null)
                {
                    conditionPropValues = new ArrayList<>();
                    put(conditionPropValue.getChannel_id(), conditionPropValue.getPlatform_prop_id(), conditionPropValues);
                }
                conditionPropValues.add(conditionPropValue);
            }
        }
    }

    private String encodeKey(String channelId, String platformPropId) {
        return channelId + "_" + platformPropId;
    }

    public void put(String channelId, String platformPropId, List<ConditionPropValue> value) {
        propValueRepo.put(encodeKey(channelId, platformPropId), value);
    }

    public List<ConditionPropValue> get(String channelId, String platformPropId) {
        if (propValueRepo.isEmpty()) {
            init();
        }
        return propValueRepo.get(encodeKey(channelId, platformPropId));
    }
}
