package com.voyageone.batch.wms.service;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.modelbean.InventoryForCmsBean;
import com.voyageone.batch.wms.mongoDao.InventoryTmpDao;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
import com.voyageone.web2.sdk.api.service.ProductsGetClient;
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
public class WmsSynInventoryToCmsService extends BaseTaskService {


    @Autowired
    private InventoryTmpDao inventoryTmpDao;

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private ProductsGetClient productsGetClient;

    @Autowired
    private CmsProductService cmsProductService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "wmsSynInventoryToCmsJob";
    }

    @Override
    public boolean getLogWithThread() {
        return true;
    }

    static int i = 1;

    /**
     * 批量插入code级别的库存数据到mongdodb，以便db端的定时任务进行处理
     *
     * @param taskControlList job 配置
     * @throws Exception
     */
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

            $info("channel_id=" + orderChannelID);

            //获取本渠道所有code级别库存
            List<InventoryForCmsBean> codeInventoryList = inventoryDao.selectInventoryCode(orderChannelID, this.getTaskName());
            $info("orderChannelID:" + orderChannelID + "    库存记录数:" + codeInventoryList.size());

            //批量更新code级库存 TODO
            bulkUpdateCodeQty(orderChannelID, codeInventoryList, getTaskName());

            //获取本渠道的cart
            List<ShopBean> cartList = ShopConfigs.getChannelShopList(orderChannelID);
            $info("orderChannelID:" + orderChannelID + "    cart数:" + cartList.size());


            List<BulkUpdateModel> bulkList = new ArrayList<>();
            for (int j = 0; j < cartList.size(); j++) {

                int cartId = Integer.parseInt(cartList.get(j).getCart_id());

                //获取本Cart下所有group TODO
                Map<String, List<String>> groupList = getGroupByCartList(orderChannelID, cartId);

                groupList.forEach((s, codes) -> {
                    $info("groupId:" + s + "    code数:" + codes.size());
                    int Inventory = getGroupInventory(codeInventoryList, codes);
                    for (String code : codes) {
                        HashMap<String, Object> updateMap = new HashMap<>();
                        updateMap.put("groups.platforms.$.inventory", Inventory);
                        HashMap<String, Object> queryMap = new HashMap<>();
                        queryMap.put("fields.code", code);
                        queryMap.put("groups.platforms.cartId", cartId);
                        BulkUpdateModel model = new BulkUpdateModel();
                        model.setUpdateMap(updateMap);
                        model.setQueryMap(queryMap);
                        bulkList.add(model);
                        //批量插入group级记录到mysql 10000条插入一次 TODO
                        if (bulkList.size() >= 1000) {
                            cmsBtProductDao.bulkUpdateWithMap(orderChannelID, bulkList, getTaskName(), "$set");
                            bulkList.clear();
                        }
                    }
                });
            }

            if (bulkList.size() > 0) {
                cmsBtProductDao.bulkUpdateWithMap(orderChannelID, bulkList, getTaskName(), "$set");
                bulkList.clear();
            }

        }
    }


    /**
     * 增加商品的Tag
     */
    public Map<String, Object> bulkUpdateCodeQty(String channelId, List<InventoryForCmsBean> codeInventoryList, String modifier) {
        Map<String, Object> ret = new HashMap<>();

        //以500条更新一次数据库
        List<List<InventoryForCmsBean>> codeInventoryListSplit = CommonUtil.splitList(codeInventoryList, 5000);
        for (List<InventoryForCmsBean> codeInventoryListItem : codeInventoryListSplit) {
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            for (InventoryForCmsBean codeInventory : codeInventoryListItem) {
                Map<String, Integer> codeQty = new HashMap<>();
                codeQty.put("code_qty", codeInventory.getQty());

                HashMap<String, Object> updateMap = new HashMap<>();
                updateMap.put("batch_update", codeQty);
                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("fields.code", codeInventory.getCode());
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

    /**
     * 根据cartid找出所有的groupID
     *
     * @param channelId
     * @param cartId
     * @return
     */
    private Map<String, List<String>> getGroupByCartList(String channelId, int cartId) {
        return cmsProductService.getProductGroupIdCodesMapByCart(channelId,cartId);
    }

    /**
     * 根据groupID找出group下的所有的code
     *
     * @param channelId
     * @param groupId
     * @return
     */
    private List<CmsBtProductModel> getProductsByGroup(String channelId, Long groupId) {

        ProductsGetRequest request = new ProductsGetRequest(channelId);
        ProductsGetResponse response = productsGetClient.getProducts(request);

        return response.getProducts();
    }

    /**
     * 计算该group下的库存
     *
     * @param codeInventory wms_bt_inventory_center_logic 里面的库存数据
     * @param codes productCodes
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
