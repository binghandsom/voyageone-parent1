package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.bean.InventoryForCmsBean;
import com.voyageone.task2.cms.dao.InventoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class CmsSynInventoryToCmsService extends BaseTaskService {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private ProductService productService;

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
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        $info("channel_id=" + TaskControlEnums.Name.order_channel_id);
        $info("orderChannelIdList=" + orderChannelIdList.size());
        // 线程
        List<Runnable> threads = new ArrayList<>();

        //删除wms_bt_inventory_aggregate TODO

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

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

                //updateGroupQty(orderChannelID,"", codeInventoryList); group下的库存数据字段qty已删除
                //usjoi的对应
                // 如果这个channel是usjoi的子channel的场合 库存也更新
                List<TypeChannelBean> typeChannelBeans = TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.SKU_CARTS_53, orderChannelID,"cn");
                typeChannelBeans.forEach(typeChannelBean -> {
                    if(Channels.isUsJoi(typeChannelBean.getValue())){
                        bulkUpdateCodeQty(typeChannelBean.getValue(), orderChannelID, codeInventoryList, getTaskName());
                    }
                });
//                if (Channels.isUsJoi(orderChannelID)) {
//                    bulkUpdateCodeQty(ChannelConfigEnums.Channel.VOYAGEONE.getId(), orderChannelID, codeInventoryList, getTaskName());
//                    //updateGroupQty(ChannelConfigEnums.Channel.VOYAGEONE.getId(), orderChannelID, codeInventoryList);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        }
    }

//    private void updateGroupQty(String channelId, String orgChannelId, List<InventoryForCmsBean> codeInventoryList){
//        //获取本渠道的cart
////            List<ShopBean> cartList = ShopConfigs.getChannelShopList(orderChannelID);
//        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, "en");
//        $info("orderChannelID:" + channelId + "    cart数:" + cartList.size());
//
//
//        List<BulkUpdateModel> bulkList = new ArrayList<>();
//        for (int j = 0; j < cartList.size(); j++) {
//
//            int cartId = Integer.parseInt(cartList.get(j).getValue());
//            $info("cartId:" + cartId + "   start" );
//
//            //获取本Cart下所有group TODO
//            Map<String, List<String>> groupList = getGroupByCartList(channelId, cartId, orgChannelId);
//
//            groupList.forEach((s, codes) -> {
//                int Inventory = getGroupInventory(codeInventoryList, codes);
//                for (String code : codes) {
//                    HashMap<String, Object> updateMap = new HashMap<>();
//                    updateMap.put("groups.platforms.$.qty", Inventory);
//                    HashMap<String, Object> queryMap = new HashMap<>();
//                    queryMap.put("fields.code", code);
//                    queryMap.put("groups.platforms.cartId", cartId);
//                    BulkUpdateModel model = new BulkUpdateModel();
//                    model.setUpdateMap(updateMap);
//                    model.setQueryMap(queryMap);
//                    bulkList.add(model);
//                    //批量插入group级记录到mysql 10000条插入一次 TODO
//                    if (bulkList.size() >= BULK_COUNT) {
//                        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");
//                        bulkList.clear();
//                    }
//                }
//            });
//        }
//
//        if (bulkList.size() > 0) {
//            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");
//            bulkList.clear();
//        }
//
//    }

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
                updateMap.put("fields.quantity", codeInventory.getQty());
                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("fields.code", codeInventory.getCode());
                if (!StringUtil.isEmpty(orgChannelId)) {
                    queryMap.put("orgChannelId", orgChannelId);
                }
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
//                logger.info("fields.code:"+ codeInventory.getCode());
            }

            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");
        }
        ret.put("result", "success");
        return ret;
    }

    /**
     * 根据cartid找出所有的groupID
     *
     * @param channelId
     * @param cartId
     * @return
     */
    private Map<String, List<String>> getGroupByCartList(String channelId, int cartId, String orgChannelId) {
        return productService.getProductGroupIdCodesMapByCart(channelId, cartId, orgChannelId);
    }

    /**
     * 计算该group下的库存
     *
     * @param codeInventory wms_bt_inventory_center_logic 里面的库存数据
     * @param codes         productCodes
     * @return
     */
    private int getGroupInventory(List<InventoryForCmsBean> codeInventory, List<String> codes) {
        int inventory = 0;
        for (String code : codes) {
            for (InventoryForCmsBean inventoryForCmsBean : codeInventory) {
                if (inventoryForCmsBean.getCode().equalsIgnoreCase(code)) {
                    inventory += inventoryForCmsBean.getQty();
                    break;
                }
            }
        }
        return inventory;
    }


}
