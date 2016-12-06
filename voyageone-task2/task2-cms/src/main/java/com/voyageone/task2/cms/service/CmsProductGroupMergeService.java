package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.daoext.cms.CmsBtPlatformNumiidDaoExt;
import com.voyageone.service.impl.cms.product.CmsProductCodeChangeGroupService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * cms_bt_product_group_cXXX表整理，相同numIId合并成成一个group
 *
 * @author morse on 2016/12/06
 * @version 2.6.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CmsProductGroupMergeJob)
public class CmsProductGroupMergeService extends BaseMQCmsService {

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsProductCodeChangeGroupService cmsProductCodeChangeGroupService;

    @Autowired
    private CmsBtPlatformNumiidDao cmsBtPlatformNumiidDao;
    @Autowired
    private CmsBtPlatformNumiidDaoExt cmsBtPlatformNumiidDaoExt;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        doMain((String) messageMap.get("channelId"), (String) messageMap.get("cartId"), (String) messageMap.get("runType"));
    }

    private void doMain(String channelId, String cartId, String runType) throws Exception {
        JongoQuery queryObject = new JongoQuery();
        String query = "cartId:" + cartId;

        List<String> listAllNumiid = null;
        List<String> listSuccessNumiid = new ArrayList<>();
        List<String> listErrorNumiid = new ArrayList<>();
        if ("2".equals(runType)) {
            // 从cms_bt_platform_numiid表里抽出numIId去做
            Map<String, Object> seachParam = new HashMap<>();
            seachParam.put("channelId", channelId);
            seachParam.put("cartId", cartId);
            seachParam.put("status", "0");
            List<CmsBtPlatformNumiidModel> listModel = cmsBtPlatformNumiidDao.selectList(seachParam);
            if (ListUtils.isNull(listModel)) {
                $warn("cms_bt_platform_numiid表未找到符合的数据!");
                return;
            }
            listAllNumiid = listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.toList());
            // 表的数据都是自己临时加的，一次处理多少件自己决定，因此暂时不分批处理了，尽量别一次处理太多，不然sql可能撑不住
            query = query + "," + "numIId:{$in:[\"" + listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.joining("\",\"")) + "\"]}";
        } else {
            query = query + ",numIId:{$nin:[\"\",null]}";
        }

        queryObject.setQuery("{" + query + "}");
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId, queryObject);
        // 根据numIId分组 Map<numIId, List<CmsBtProductGroupModel>>
        Map<String, List<CmsBtProductGroupModel>> mapCmsBtProductGroupModel = cmsBtProductGroupModels.stream().collect(Collectors.groupingBy(CmsBtProductGroupModel::getNumIId));

        int index = 0;
        for(Map.Entry<String, List<CmsBtProductGroupModel>> entry: mapCmsBtProductGroupModel.entrySet()) {
            index++;
            String numIId = entry.getKey();
            if ("2".equals(runType)) {
                listAllNumiid.remove(numIId);
            }
            try {
                $info(String.format("%s-%s-%s开始merge %d/%d", channelId, cartId, numIId, index, mapCmsBtProductGroupModel.size()));
                List<CmsBtProductGroupModel> numIIdModels = entry.getValue();
                if (numIIdModels.size() == 1) {
                    $info(String.format("channelId:%s, cartId:%s, numIId:%s 不需要merge!", channelId, cartId, numIId));
                    continue;
                }
                doMerge(channelId, Integer.valueOf(cartId), numIIdModels);
                listSuccessNumiid.add(numIId);
                $info(String.format("channelId:%s, cartId:%s, numIId:%s merge成功!", channelId, cartId, numIId));
            } catch (Exception e) {
                listErrorNumiid.add(numIId);
                if (e instanceof BusinessException) {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s merge失败!" + e.getMessage(), channelId, cartId, numIId));
                } else {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s merge失败!", channelId, cartId, numIId));
                    e.printStackTrace();
                }
            }
        }

        if ("2".equals(runType)) {
            if (listSuccessNumiid.size() > 0) {
                cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "1", getTaskName(), listSuccessNumiid);
            }
            if (listErrorNumiid.size() > 0) {
                cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "2", getTaskName(), listErrorNumiid);
            }
            if (ListUtils.notNull(listAllNumiid)) {
                // 存在没有搜到的numIId
                cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "3", getTaskName(), listSuccessNumiid);
            }
        }
    }

    private void doMerge(String channelId, int cartId, List<CmsBtProductGroupModel> numIIdModels) {
        CmsBtProductGroupModel destModel = numIIdModels.get(0); // 合并到这个group下

        for (int i = 1; i < numIIdModels.size(); i++) {
            CmsBtProductGroupModel sourceGroupModel = numIIdModels.get(i);
            // 暂时循环做，不批量处理了，反正只有个别数据要做，不会很多
            for (String moveCode : sourceGroupModel.getProductCodes()) {
                cmsProductCodeChangeGroupService.moveToAnotherGroup(channelId, cartId, moveCode, sourceGroupModel, destModel, getTaskName());
            }
        }
    }
}
