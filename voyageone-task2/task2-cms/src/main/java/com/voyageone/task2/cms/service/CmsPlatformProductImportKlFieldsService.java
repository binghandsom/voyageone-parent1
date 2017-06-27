package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.util.*;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.ecerp.interfaces.third.koala.beans.*;
import com.voyageone.ecerp.interfaces.third.koala.beans.request.ItemBatchStatusGetRequest;
import com.voyageone.service.dao.cms.CmsBtKlSkuDao;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPlatformNumiidDaoExt;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtKlSkuModel;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
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
public class CmsPlatformProductImportKlFieldsService extends BaseMQCmsService {

    // 考拉类目属性的输入属性类型(3:下拉列表)
    private final static String Input_Type_3_LIST = "3";
    // 考拉类目属性的输入属性类型(4:单选框)
    private final static String Input_Type_4_SIMPLECHECK = "RADIO";
    // 考拉类目属性的输入属性类型(5:多选框)
    private final static String Input_Type_5_MULTICHECK = "CHECKBOX";
    @Autowired
    private KoalaItemService koalaItemService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtPlatformNumiidDao cmsBtPlatformNumiidDao;
    @Autowired
    private CmsBtPlatformNumiidDaoExt cmsBtPlatformNumiidDaoExt;
    @Autowired
    private CmsBtKlSkuDao cmsBtKlSkuDao;

    /**
     * runType=1或者空的话，根据入参的pid或者status批量处理  runType=2 从cms_bt_platform_numiid表里抽出numIId(商品key)去做
     */
    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        $info("start MqTask[CmsPlatformProductImportKlFieldsService考拉商品信息回写]!" + messageMap.toString());

        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }
        String cartId = CartEnums.Cart.KL.getId();

        String pid = null; // 产品id，对应考拉的商品key
        if (messageMap.containsKey("pid")) {
            pid = String.valueOf(messageMap.get("pid"));
        }

        String runType = null; // runType=2 从cms_bt_platform_numiid表里抽出numIId去做
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        PlatformStatus status = null;
        String platformStatus = null;
        if (messageMap.containsKey("platformStatus")) {
            platformStatus = String.valueOf(messageMap.get("platformStatus"));
            status = PlatformStatus.parse(platformStatus);
            if (status == null) {
                $error("入参平台状态platformStatus输入错误!");
                return;
            }
        }

        if (!"2".equals(runType)) {
            if (StringUtils.isEmpty(pid) && StringUtils.isEmpty(platformStatus)) {
                $error("入参未指定pid,必须指定平台状态platformStatus!");
                return;
            }
        }

        KoalaConfig shopBean = Shops.getShopKoala(channelId, cartId);

        try {
            if ("2".equals(runType)) {
                // 从cms_bt_platform_numiid表里抽出numIId(商品key)去做
                executeFromTable(shopBean, channelId, Integer.parseInt(cartId));
            } else if (StringUtils.isEmpty(pid)) {
                // 未指定某个商品，全店处理
                executeAll(shopBean, channelId, cartId, status);
            } else {
                // 只处理指定的商品
                executeSingle(shopBean, channelId, cartId, pid);
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                $error(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }

        $info("finish MqTask[CmsPlatformProductImportKlFieldsService考拉商品信息回写]");

    }

    private boolean executeFromTable(KoalaConfig shopBean, String channelId, int cartId) {
        Map<String, Object> seachParam = new HashMap<>();
        seachParam.put("channelId", channelId);
        seachParam.put("cartId", cartId);
        seachParam.put("status", "0");
        List<CmsBtPlatformNumiidModel> listModel = cmsBtPlatformNumiidDao.selectList(seachParam);
        if (ListUtils.isNull(listModel)) {
            $warn("cms_bt_platform_numiid表未找到符合的数据!");
            return false;
        }
        List<String> listSuccessPid = new ArrayList<>();
        List<String> listErrorPid = new ArrayList<>();
        boolean hasErrorData = false;
        int index = 1;
        int pageSize = 300;
        List<List<String>> listAllPid = CommonUtil.splitList(listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.toList()), pageSize);
        for (List<String> listPid : listAllPid) {
            ItemEdit[] itemEdits;
            try {
                itemEdits = koalaItemService.batchGet(shopBean, listPid.toArray(new String[listPid.size()]));
            } catch (Exception e) {
                $error(String.format("调用获取商品信息API失败!channelId:%s, cartId:%s, pid:%s!" + e.getMessage(), channelId, cartId, listPid));
                index = index + pageSize;
                continue;
            }

            for (ItemEdit itemEdit : itemEdits) {
                String platformPid = itemEdit.getKey();
                $info(String.format("%s-%s-%s考拉属性取得 %d/%d", channelId, cartId, platformPid, index, listModel.size()));
                try {
                    doSetProduct(channelId, cartId, itemEdit);
                    listSuccessPid.add(platformPid);
                } catch (Exception e) {
                    hasErrorData = true;
                    listErrorPid.add(platformPid);
                    if (e instanceof BusinessException) {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 属性取得失败!" + e.getMessage(), channelId, cartId, platformPid));
                    } else {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 属性取得失败!", channelId, cartId, platformPid));
                        e.printStackTrace();
                    }
                }
                index++;
            }
            // 300一更新
            updateCmsBtPlatformNumiid(channelId, cartId, listSuccessPid, listErrorPid);
            listSuccessPid.clear();
            listErrorPid.clear();
        }

        return hasErrorData;
    }

    void updateCmsBtPlatformNumiid(String channelId, int cartId, List<String> listSuccessPid, List<String> listErrorPid) {
        if (listSuccessPid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, cartId, "1", getTaskName(), listSuccessPid);
        }
        if (listErrorPid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, cartId, "2", getTaskName(), listErrorPid);
        }
        $info(String.format("cms_bt_platform_numiid表里,成功%d个,失败%d个!", listSuccessPid.size(), listErrorPid.size()));
    }

    private void executeSingle(KoalaConfig shopBean, String channelId, String cartId, String platformPid) throws Exception {
        ItemEdit itemEdit = koalaItemService.itemGet(shopBean, platformPid);
        if (itemEdit == null) {
            throw new BusinessException(String.format("考拉平台未找到此商品key[%s]", platformPid));
        }
        doSetProduct(channelId, Integer.parseInt(cartId), itemEdit);
    }

    private boolean executeAll(KoalaConfig shopBean, String channelId, String cartId, CmsPlatformProductImportKlFieldsService.PlatformStatus status) throws Exception {
        List<String> errorList = new ArrayList<>();
        ItemBatchStatusGetRequest req = new ItemBatchStatusGetRequest();
        req.setItemEditStatus(Integer.parseInt(status.value()));
        int pageNo = 1;
        int pageSize = 100;
        int index = 1;
        while (true) {
            req.setPageNo(pageNo++);
            req.setPageSize(pageSize);
            PagedItemEdit edit = koalaItemService.batchStatusGet(shopBean, req);
            ItemEdit[] itemEdits = edit.getItemEditList();
            if (itemEdits == null || itemEdits.length == 0) {
                break;
            }

            for (ItemEdit itemEdit : itemEdits) {
                $info(String.format("%s-%s-%s考拉[%s]属性取得 %d/%d", channelId, cartId, itemEdit.getKey(), status.name(), index, edit.getTotalCount()));
                try {
                    doSetProduct(channelId, Integer.valueOf(cartId), itemEdit);
                } catch (Exception e) {
                    errorList.add(itemEdit.getKey());
                    if (e instanceof BusinessException) {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 属性取得失败!" + e.getMessage(), channelId, cartId, itemEdit.getKey()));
                    } else {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 属性取得失败!", channelId, cartId, itemEdit.getKey()));
                        e.printStackTrace();
                    }
                }
                index++;
            }

            if (itemEdits.length < pageSize) {
                break;
            }
        }

        return errorList.size() == 0;
    }

    /**
     * 回写主逻辑
     * 暂时不回写价格
     */
    private void doSetProduct(String channelId, int cartId, ItemEdit itemEdit) throws Exception {
        String platformPid = itemEdit.getKey();
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByPlatformPid(channelId, cartId, platformPid);
        if (cmsBtProductGroup == null) {
            throw new BusinessException(String.format("cms里没有这个PlatformPid[%s]!", platformPid));
        }
        cmsBtProductGroup.setPlatformPid(platformPid);

        // status
        CmsPlatformProductImportKlFieldsService.PlatformStatus klPlatformStatus = CmsPlatformProductImportKlFieldsService.PlatformStatus.parse(itemEdit.getRawItemEdit().getItemStatus());
        CmsConstants.PlatformStatus status;
        if (klPlatformStatus == CmsPlatformProductImportKlFieldsService.PlatformStatus.ON_SALE) {
            status = CmsConstants.PlatformStatus.OnSale;
        } else {
            status = CmsConstants.PlatformStatus.InStock;
        }
        cmsBtProductGroup.setPlatformStatus(status);

        // 共通属性
        RawItemEdit rawItemEdit = itemEdit.getRawItemEdit();
        Map<String, Object> mapComm = BeanUtils.toMap(rawItemEdit);

        // 类目
        ItemCategory[] itemCategories = itemEdit.getItemCategoryList();
        String errMsgCid = String.format("类目取得失败!PlatformPid[%s]", platformPid);
        if (itemCategories != null && itemCategories.length > 0) {
            long cid = itemCategories[0].getCategoryId();
            if (cid > 0) {
                mapComm.put("cid", String.valueOf(cid));
            } else {
                $warn(errMsgCid);
            }
        } else {
            $warn(errMsgCid);
        }

        // sku属性
        Map<String, Sku> mapKlSku = new HashMap<>(); // Map<skuCode, Sku>
        Sku[] klSkus = itemEdit.getSkuList();
        if (klSkus != null && klSkus.length > 0) {
            mapKlSku.putAll(Arrays.stream(klSkus)
                    .filter(sku -> !StringUtils.isEmpty(sku.getRawSku().getBarCode()))
                    .collect(Collectors.toMap(sku -> sku.getRawSku().getBarCode().toLowerCase(), sku -> sku))); // 考拉barcode保存的是skuCode
        }

        // 各类目预定义属性，下拉框，单选框，多选框
        ItemProperty[] itemProperties = itemEdit.getItemPropertyList();
        Map<String, Object> mapItemProperties = new HashMap<>(); // 下拉框，单选框: String，多选框: List
        if (itemProperties != null && itemProperties.length > 0) {
            for (ItemProperty itemProperty : itemProperties) {
                String id = itemProperty.getPropertyValue().getPropertyNameId();
                String value = itemProperty.getPropertyValue().getPropertyValueId();
                String inputType = itemProperty.getPropertyName().getPropertyEditPolicy().getInputType();
                if (Input_Type_3_LIST.equals(inputType) || Input_Type_4_SIMPLECHECK.equals(inputType)) {
                    mapItemProperties.put(id, value);
                } else if (Input_Type_5_MULTICHECK.equals(inputType)) {
                    List<String> list = (List) mapItemProperties.get(id);
                    if (list == null) {
                        list = new ArrayList<>();
                        mapItemProperties.put(id, list);
                    }
                    list.add(value);
                }
            }
        }

        // 各类目自定义属性，输入框
        ItemTextProperty[] itemTextProperties = itemEdit.getItemTextPropertyList();
        Map<String, Object> mapItemTextProperties = new HashMap<>();
        if (itemTextProperties != null && itemTextProperties.length > 0) {
            for (ItemTextProperty itemTextProperty : itemTextProperties) {
                mapItemTextProperties.put(itemTextProperty.getPropNameId(), itemTextProperty.getTextValue());
            }
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        List<CmsBtKlSkuModel> cmsBtKlSkuModels = new ArrayList<>();
        cmsBtProductGroup.getProductCodes().forEach(s -> {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", s);

            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("platforms.P" + cartId + ".modified", DateTimeUtil.getNowTimeStamp());
            updateMap.put("platforms.P" + cartId + ".modifier", getTaskName());
            mapComm.forEach((fieldName, value) -> {
                if ("cid".equals(fieldName)) {
                    String catId = (String) value;
                    updateMap.put("platforms.P" + cartId + ".pCatId", catId);
                    CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchema(catId, cartId);
                    if (cmsMtPlatformCategorySchemaModel != null) {
                        updateMap.put("platforms.P" + cartId + ".pCatPath", cmsMtPlatformCategorySchemaModel.getCatFullPath());
                    }
                } else {
                    updateMap.put("platforms.P" + cartId + ".fields." + fieldName, value);
                }
            });

            mapItemProperties.forEach((fieldName, value) -> {
                updateMap.put("platforms.P" + cartId + ".fields." + fieldName, value);
            });

            mapItemTextProperties.forEach((fieldName, value) -> {
                updateMap.put("platforms.P" + cartId + ".fields." + fieldName, value);
            });

            if (!mapKlSku.isEmpty()) {
                CmsBtProductModel product = cmsBtProductDao.selectByCode(s, channelId);
                // 全小写比较skuCode
                Map<String, BaseMongoMap<String, Object>> mapSkus = product.getPlatform(cartId).getSkus().stream()
                        .collect(Collectors.toMap(
                                sku -> sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).toLowerCase(),
                                sku -> sku)
                        );

                mapKlSku.forEach((skuCodeLower, klSku) -> {
                    BaseMongoMap<String, Object> skuInfo = mapSkus.get(skuCodeLower);
                    if (skuInfo != null) {
                        // 这一版只回写skuKey
                        skuInfo.setAttribute("skuKey", klSku.getKey());
                        String skuCode = skuInfo.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
                        String barCode = Optional.ofNullable(product.getCommon().getSku(skuCode)).map(CmsBtProductModel_Sku::getBarcode).orElse("");
                        cmsBtKlSkuModels.add(fillCmsBtKlSkuModel(channelId, s, skuCode, barCode, klSku.getKey(), "", product.getOrgChannelId()));
                    }
                });

                updateMap.put("platforms.P" + cartId + ".skus", product.getPlatform(cartId).getSkus());
            }

            updateMap.put("platforms.P" + cartId + ".pProductId", platformPid);
            if (klPlatformStatus == CmsPlatformProductImportKlFieldsService.PlatformStatus.ON_SALE) {
                // 出售中
                updateMap.put("platforms.P" + cartId + ".pStatus", CmsConstants.PlatformStatus.OnSale.name());
                updateMap.put("platforms.P" + cartId + ".pReallyStatus", CmsConstants.PlatformStatus.OnSale.name());
            } else {
                // 仓库中
                updateMap.put("platforms.P" + cartId + ".pStatus", CmsConstants.PlatformStatus.InStock.name());
                updateMap.put("platforms.P" + cartId + ".pReallyStatus", CmsConstants.PlatformStatus.InStock.name());
            }

            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        });

        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, getTaskName(), "$set");
        productGroupService.update(cmsBtProductGroup);
        insertOrUpdateCmsBtKlSku(cmsBtKlSkuModels);
    }

    private CmsBtKlSkuModel fillCmsBtKlSkuModel(String channelId, String productCode, String skuCode, String barCode, String skuKey, String numIId, String orgChannelId) {
        CmsBtKlSkuModel cmsBtKlSkuModel = new CmsBtKlSkuModel();

        cmsBtKlSkuModel.setChannelId(channelId);
        cmsBtKlSkuModel.setOrgChannelId(orgChannelId);
        cmsBtKlSkuModel.setKlNumiid(numIId);
        cmsBtKlSkuModel.setProductCode(productCode);
        cmsBtKlSkuModel.setSku(skuCode);
        cmsBtKlSkuModel.setKlSkuKey(skuKey);
        cmsBtKlSkuModel.setUpc(barCode);

        cmsBtKlSkuModel.setCreated(DateTimeUtil.getDate());
        cmsBtKlSkuModel.setCreater(getTaskName());
        cmsBtKlSkuModel.setModified(DateTimeUtil.getDate());
        cmsBtKlSkuModel.setModifier(getTaskName());

        return cmsBtKlSkuModel;
    }

    private void insertOrUpdateCmsBtKlSku(List<CmsBtKlSkuModel> cmsBtKlSkuModels) {
        cmsBtKlSkuModels.forEach(skuModel -> {
            String skuKey = skuModel.getKlSkuKey();
            Map<String, Object> map = new HashMap<>();
            map.put("channelId", skuModel.getChannelId());
            map.put("orgChannelId", skuModel.getOrgChannelId());
            map.put("productCode", skuModel.getProductCode());
            map.put("sku", skuModel.getSku());
            CmsBtKlSkuModel cmsBtKlSkuModel = cmsBtKlSkuDao.selectOne(map);

            if (cmsBtKlSkuModel == null) {
                cmsBtKlSkuDao.insert(skuModel);
            } else {
                if (!skuKey.equals(cmsBtKlSkuModel.getKlSkuKey())) {
                    cmsBtKlSkuModel.setKlSkuKey(skuKey);
                    cmsBtKlSkuModel.setModifier(getTaskName());
                    cmsBtKlSkuModel.setModified(DateTimeUtil.getDate());
                    cmsBtKlSkuDao.update(cmsBtKlSkuModel);
                }
            }
        });

    }

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

        String value() {
            return value;
        }
    }

}
