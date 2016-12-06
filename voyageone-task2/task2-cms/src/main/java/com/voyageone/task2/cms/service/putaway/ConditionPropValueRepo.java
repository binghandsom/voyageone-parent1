package com.voyageone.task2.cms.service.putaway;

import com.voyageone.task2.cms.dao.ConditionPropValueDao;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
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
//    private static Map<String, List<ConditionPropValueModel>> propValueRepo;
//
//    public ConditionPropValueRepo() {
//        propValueRepo = new HashMap<>();
//    }
//
//    public void init() {
//        List<ConditionPropValueModel> conditionPropValueModelList = conditionPropValueDao.selectAllConditionPropValue();
//
//        if (conditionPropValueModelList != null)
//        {
//            for (ConditionPropValueModel conditionPropValueModel : conditionPropValueModelList) {
//                List<ConditionPropValueModel> conditionPropValueModels = propValueRepo.get(encodeKey(conditionPropValueModel.getChannel_id(), conditionPropValueModel.getPlatform_prop_id()));
//                if (conditionPropValueModels == null)
//                {
//                    conditionPropValueModels = new ArrayList<>();
//                    put(conditionPropValueModel.getChannel_id(), conditionPropValueModel.getPlatform_prop_id(), conditionPropValueModels);
//                }
//                conditionPropValueModels.add(conditionPropValueModel);
//            }
//        }
//    }
//
//    private String encodeKey(String channelId, String platformPropId) {
//        return channelId + "_" + platformPropId;
//    }
//
//    public void put(String channelId, String platformPropId, List<ConditionPropValueModel> value) {
//        propValueRepo.put(encodeKey(channelId, platformPropId), value);
//    }
//
//    public List<ConditionPropValueModel> get(String channelId, String platformPropId) {
//        if (propValueRepo.isEmpty()) {
//            init();
//        }
//        return propValueRepo.get(encodeKey(channelId, platformPropId));
//    }

    /**
     * 获取指定channel的condition表数据
     * @param channelId 指定channel
     * @return 按照属性名称整理好之后的map
     */
    public Map<String, List<ConditionPropValueModel>> getAllByChannelId(String channelId) {
        // 抽取数据
        List<ConditionPropValueModel> conditionPropValueModelList = conditionPropValueDao.selectConditionPropValueByChannelId(channelId);

        // 整理一下再返回
        Map<String, List<ConditionPropValueModel>> result = new HashMap<>();
        if (conditionPropValueModelList != null) {
            for (ConditionPropValueModel model : conditionPropValueModelList) {
                if (!result.containsKey(model.getPlatform_prop_id())) {
                    List<ConditionPropValueModel> modelList = new ArrayList<>();
                    modelList.add(model);
                    result.put(model.getPlatform_prop_id(), modelList);
                } else {
                    result.get(model.getPlatform_prop_id()).add(model);
                }
            }
        }

        return result;
    }
}
