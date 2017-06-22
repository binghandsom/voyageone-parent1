package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
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
 * Created by Charis on 2017/6/22.
 */
//@SuppressWarnings("ALL")
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_KLFieldsImportCms2Job)
public class CmsPlatformProductImportKlFieldsService extends BaseMQCmsService{

    private static String cartId = CartEnums.Cart.KL.getId();

    @Autowired
    private KoalaItemService koalaItemService;
    @Autowired
    private CmsBtPlatformNumiidDao cmsBtPlatformNumiidDao;
    @Autowired
    private ProductGroupService productGroupService;

    /**
     * 平台状态
     */
    enum PlatformStatus {
        /**
         * 待提交审核
         */
        WAITING("1"),
        /**
         * 审核中
         */
        CHECKING("2"),
        /**
         * 审核未通过
         */
        ERROR("3"),
        /**
         * 待上架(审核已通过)
         */
        CHECKED("4"),
        /**
         * 在售
         */
        ON_SALE("5"),
        /**
         * 下架
         */
        IN_STOCK("6"),
        /**
         * 已删除
         */
        DELETED("7"),
        /**
         * 强制下架
         */
        FORCE_STOCK("8"),
        ;

        private String value;

        PlatformStatus(String value) {
            this.value = value;
        }

        String value() {
            return value;
        }

        static PlatformStatus parse(String value) {
            switch (value) {
                case "1":
                case "WAITING":
                    return WAITING;
                case "2":
                case "CHECKING":
                    return CHECKING;
                case "3":
                case "ERROR":
                    return ERROR;
                case "4":
                case "CHECKED":
                    return CHECKED;
                case "5":
                case "ON_SALE":
                    return ON_SALE;
                case "6":
                case "IN_STOCK":
                    return IN_STOCK;
                case "7":
                case "DELETED":
                    return DELETED;
                case "8":
                case "FORCE_STOCK":
                    return FORCE_STOCK;
                default:
                    return null;
            }
        }
    }

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {

        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }

        String productId = null;
        if (messageMap.containsKey("productId")) {
            productId = String.valueOf(messageMap.get("productId"));
        }
        // 商品的状态(1:待提交审核, 2:审核中, 3:审核未通过, 4:待上架(审核已通过), 5:在售, 6:下架, 8:强制下架)
        String status = null;
        if (messageMap.containsKey("status")) {
            status = String.valueOf(messageMap.get("status"));
        }

        String runType = null; // runType=2 从cms_bt_platform_numiid表里抽出numIId去做
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        doMain(channelId, cartId, productId, status, runType);

    }

    private void doMain(String channelId, String cartId, String productId, String status, String runType) {
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
            query = query + "," + "platformPid:{$in:[\"" + listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.joining("\",\"")) + "\"]}";
        } else {
            if (!StringUtils.isEmpty(productId)) {
                query = query + "," + "platformPid:\"" + productId + "\"";
            } else {
                query = query + ",platformPid:{$nin:[\"\",null]}";
            }

        }
        queryObject.setQuery("{" + query + "}");
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId, queryObject);
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        for (int i = 0; i < cmsBtProductGroupModels.size(); i++) {
            CmsBtProductGroupModel item = cmsBtProductGroupModels.get(i);
            if ("2".equals(runType)) {
                listAllNumiid.remove(item.getNumIId());
            }


        }


    }


}
