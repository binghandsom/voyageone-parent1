package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.ecerp.interfaces.third.koala.beans.ItemEdit;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.ecerp.interfaces.third.koala.beans.PagedItemEdit;
import com.voyageone.ecerp.interfaces.third.koala.beans.request.ItemBatchStatusGetRequest;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Charis on 2017/6/22.
 */
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
        // 商品的状态(1:待提交审核, 2:审核中, 3:审核未通过, 4:待上架(审核已通过), 5:在售, 6:下架, 7:已删除, 8:强制下架)
        String status = null;
        if (messageMap.containsKey("status")) {
            status = String.valueOf(messageMap.get("status"));
            PlatformStatus platformStatus = PlatformStatus.parse(status);
            if (platformStatus == null) {
                $error("入参平台状态platformStatus输入错误!");
                return;
            }
        }

        String runType = null; // runType=2 从cms_bt_platform_numiid表里抽出numIId去做
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        doMain(channelId, cartId, productId, status, runType);

    }

    public void doMain(String channelId, String cartId, String productId, String status, String runType) {
        KoalaConfig koalaConfig = Shops.getShopKoala(channelId, cartId);
        koalaConfig.setSessionkey("GwtM96");
        koalaConfig.setUseProxy(false);


        JongoQuery queryObject = new JongoQuery();

        String query = "cartId:" + cartId;
        List<String> listAllNumiid = null;
        List<String> listSuccessNumiid = new ArrayList<>();
        List<String> listErrorNumiid = new ArrayList<>();

        if (StringUtils.isEmpty(status)) {
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


            for (int i = 0; i < cmsBtProductGroupModels.size(); i++) {
                CmsBtProductGroupModel item = cmsBtProductGroupModels.get(i);
                if ("2".equals(runType)) {
                    listAllNumiid.remove(item.getNumIId());
                }

                try {
                    $info(String.format("%s-%s-%s考拉属性取得 %d/%d", channelId, cartId, item.getPlatformPid(), i + 1, cmsBtProductGroupModels.size()));
//                    doSetProduct(koalaConfig, item, channelId, Integer.valueOf(cartId));
                    listSuccessNumiid.add(item.getNumIId());
                    $info(String.format("channelId:%s, cartId:%s, numIId:%s 取得成功!", channelId, cartId, item.getPlatformPid()));
                } catch (Exception e) {
                    listErrorNumiid.add(item.getPlatformPid());
                    if (e instanceof BusinessException) {
                        $error(String.format("channelId:%s, cartId:%s, numIId:%s 取得失败!" + e.getMessage(), channelId, cartId, item.getPlatformPid()));
                    } else {
                        $error(String.format("channelId:%s, cartId:%s, numIId:%s 取得失败!", channelId, cartId, item.getPlatformPid()));
                        e.printStackTrace();
                    }
                }

                if ((i + 1) % 300 == 0) {
                    // 怕中途断掉,300一更新
                    if ("2".equals(runType)) {
//                        updateCmsBtPlatformNumiid(channelId, cartId, listSuccessNumiid, listErrorNumiid);
                    }
                    $info(String.format("考拉属性取得,成功%d个,失败%d个!", listSuccessNumiid.size(), listErrorNumiid.size()));
                    listSuccessNumiid.clear();
                    listErrorNumiid.clear();
                }


            }
        } else {

            int pageNo = 1;
            List<ItemEdit> allItems = new ArrayList<>();
            while(true) {
                List<ItemEdit> itemEdits;
                try {

                    itemEdits = getItemListByStatus(status, koalaConfig, pageNo, 20);
                    pageNo++;
                } catch (Exception exp) {
                    throw new BusinessException(String.format("获取考拉商品时出错! channelId=%s, cartId=%s", channelId, cartId), exp);
                }
                if (itemEdits != null && itemEdits.size() > 0) {
                    allItems.addAll(itemEdits);
                }
                if (itemEdits == null || itemEdits.size() == 0) {
                    break;
                }
            }

            for (ItemEdit item : allItems) {
                String itemKey = item.getKey();

                CmsBtProductGroupModel cmsBtProductGroupModel = productGroupService.selectProductGroupByPlatformPid(channelId, Integer.parseInt(cartId), itemKey);

                List<String> codeList = cmsBtProductGroupModel.getProductCodes();

                for (String code : codeList) {

                }
            }

        }




    }

    public List<ItemEdit> getItemListByStatus(String status, KoalaConfig koalaConfig, int pageNo, int pageSize) {

        ItemBatchStatusGetRequest request = new ItemBatchStatusGetRequest();
        request.setItemEditStatus(Integer.parseInt(status));
        request.setPageSize(pageSize);
        request.setPageNo(pageNo);

        PagedItemEdit itemEdit = koalaItemService.batchStatusGet(koalaConfig, request);
        if (itemEdit == null) {
            return null;
        }

        return Arrays.asList(itemEdit.getItemEditList());
    }


}
