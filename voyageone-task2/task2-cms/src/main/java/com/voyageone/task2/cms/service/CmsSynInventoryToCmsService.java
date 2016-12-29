package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.InventoryForCmsBean;
import com.voyageone.task2.cms.dao.InventoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class CmsSynInventoryToCmsService extends BaseCronTaskService {

    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private MqSender sender;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSynInventoryToCmsJob";
    }

    @Override
    public boolean getLogWithThread() {
        return true;
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    // mongoDB 每次批量执行的数
    public static final int BULK_COUNT = 500;

    /**
     * 批量插入code级别的库存数据到mongdodb，以便db端的定时任务进行处理
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 获取允许运行的渠道
        Set<String> colList = mongoTemplate.getCollectionNames();
        List<String> orderChannelIdList = colList.stream().filter(s -> s.indexOf("cms_bt_product_c") != -1 && s.length() == 19).map(s1 -> s1.substring(16)).collect(Collectors.toList());

        $info("orderChannelIdList=" + orderChannelIdList.size());

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {
            if (Integer.parseInt(orderChannelID) > 900) continue;
            try {
                $info("channel_id=" + orderChannelID);

                //获取本渠道所有code级别库存
                List<InventoryForCmsBean> codeInventoryList = inventoryDao.selectInventoryCode(orderChannelID, this.getTaskName());
                $info("orderChannelID:" + orderChannelID + "    库存记录数:" + codeInventoryList.size());

                if (codeInventoryList.size() == 0) {
                    continue;
                }
                //批量更新code级库存 TODO
                bulkUpdateCodeQty(orderChannelID, "", codeInventoryList, getTaskName());

                //usjoi的对应
                // 如果这个channel是usjoi的子channel的场合 库存也更新
                List<TypeChannelBean> typeChannelBeans = TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SKU_CARTS_53, orderChannelID,"cn");
                typeChannelBeans.forEach(typeChannelBean -> {
                    if(Channels.isUsJoi(typeChannelBean.getValue())){
                        bulkUpdateCodeQty(typeChannelBean.getValue(), orderChannelID, codeInventoryList, getTaskName());
                    }
                });
            } catch (Exception e) {
                $error("导入库存数据时出错 " + e.getMessage(), e);
                throw e;
            }
        }

        // 同步库存数据到聚美活动表 cms_bt_jm_promotion_product
        sender.sendMessage(CmsMqRoutingKey.CMS_BATCH_JmPromotionProductStockSyncServiceJob, null);
    }

    /**
     * 增加商品的Tag
     */
    public Map<String, Object> bulkUpdateCodeQty(String channelId, String orgChannelId, List<InventoryForCmsBean> codeInventoryList, String modifier) {
        Map<String, Object> ret = new HashMap<>();

        //以500条更新一次数据库
        List<List<InventoryForCmsBean>> codeInventoryListSplit = CommonUtil.splitList(codeInventoryList, BULK_COUNT);
        for (List<InventoryForCmsBean> codeInventoryListItem : codeInventoryListSplit) {
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            for (InventoryForCmsBean codeInventory : codeInventoryListItem) {

                HashMap<String, Object> updateMap = new HashMap<>();
                updateMap.put("common.fields.quantity", codeInventory.getQty());
                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("common.fields.code", codeInventory.getCode());
                if (!StringUtil.isEmpty(orgChannelId)) {
                    queryMap.put("orgChannelId", orgChannelId);
                }
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
            }

            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");
        }
        ret.put("result", "success");
        return ret;
    }

}
