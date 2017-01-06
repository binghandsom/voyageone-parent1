package com.voyageone.task2.cms.service;

import com.taobao.api.domain.Item;
import com.taobao.api.domain.SellerCat;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.components.tmall.service.TbSellerCatService;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtPlatformNumiidDaoExt;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.cms.service.product.CmsProcductPriceUpdateService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author james.li on 2016/7/11.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_TMFieldsImportCms2Job)
public class CmsPlatformProductImportTmFieldsService extends BaseMQCmsService {

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private TbSellerCatService tbSellerCatService;

    @Autowired
    private CmsBtPlatformNumiidDao cmsBtPlatformNumiidDao;
    @Autowired
    private CmsBtPlatformNumiidDaoExt cmsBtPlatformNumiidDaoExt;

//    @Autowired
//    private TbItemService tbItemService;

    @Autowired
    private MqSender sender;
    @Autowired
    private CmsProcductPriceUpdateService cmsProcductPriceUpdateService;
    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

//        doMain((String) messageMap.get("channelId"));
//        doMain((String) messageMap.get("channelId"), (String) messageMap.get("cartId"), (String) messageMap.get("code"));
        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }
        String cartId = null;
        if (messageMap.containsKey("cartId")) {
            cartId = String.valueOf(messageMap.get("cartId"));
            if (!CartEnums.Cart.isTmSeries(CartEnums.Cart.getValueByID(cartId))) {
                $error("入参的平台id不是天猫系!");
                return;
            }
        }
        String numIId = null;
        if (messageMap.containsKey("numIId")) {
            numIId = String.valueOf(messageMap.get("numIId"));
        }
        String code = null;
        if (messageMap.containsKey("code")) {
            code = String.valueOf(messageMap.get("code"));
        }

        String runType = null; // runType=2 从cms_bt_platform_numiid表里抽出numIId去做
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        doMain(channelId, cartId, numIId, code, runType);
    }

    private void doMain(String channelId, String cartId, String numIId, String code, String runType) throws Exception {
        JongoQuery queryObject = new JongoQuery();
        // modified by morse.lu 2016/11/08 start
//        queryObject.setQuery("{cartId:23,numIId:{$nin:[\"\",null]}}");
//        String query = "cartId:" + cartId +",numIId:{$nin:[\"\",null]}";
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
            if (!StringUtils.isEmpty(numIId)) {
                query = query + "," + "numIId:\"" + numIId + "\"";
            } else {
                query = query + ",numIId:{$nin:[\"\",null]}";
            }
            if (!StringUtils.isEmpty(code)) {
                query = query + "," + "productCodes:\"" + code + "\"";
            }
        }
        queryObject.setQuery("{" + query + "}");
        // modified by morse.lu 2016/11/08 end
//        Long cnt = productGroupService.countByQuery(queryObject.getQuery(), channelId);
        List<CmsBtProductGroupModel> cmsBtProductGroupModels = productGroupService.getList(channelId, queryObject);
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        List<CmsBtSellerCatModel> sellerCat = new ArrayList<>();

        List<SellerCat> sellerCatList = tbSellerCatService.getSellerCat(shopBean);
        sellerCat = formatTMModel(sellerCatList, channelId, Integer.valueOf(cartId), getTaskName());
        convert2Tree(sellerCat);

        final List<CmsBtSellerCatModel> finalSellerCat = sellerCat;
        for (int i = 0; i < cmsBtProductGroupModels.size(); i++) {
            CmsBtProductGroupModel item = cmsBtProductGroupModels.get(i);
            if ("2".equals(runType)) {
                listAllNumiid.remove(item.getNumIId());
            }
            try {
//                $info(String.format("%s-%s天猫属性取得 %d/%d", channelId, item.getNumIId(), i+1, cnt));
                $info(String.format("%s-%s-%s天猫属性取得 %d/%d", channelId, cartId, item.getNumIId(), i + 1, cmsBtProductGroupModels.size()));
                doSetProduct(shopBean, item, channelId, Integer.valueOf(cartId), finalSellerCat);
                listSuccessNumiid.add(item.getNumIId());
                $info(String.format("channelId:%s, cartId:%s, numIId:%s 取得成功!", channelId, cartId, item.getNumIId()));
            } catch (Exception e) {
                listErrorNumiid.add(item.getNumIId());
                if (e instanceof BusinessException) {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s 取得失败!" + e.getMessage(), channelId, cartId, item.getNumIId()));
                } else {
                    $error(String.format("channelId:%s, cartId:%s, numIId:%s 取得失败!", channelId, cartId, item.getNumIId()));
                    e.printStackTrace();
                }
            }

            if ((i + 1) % 300 == 0) {
                // 怕中途断掉,300一更新
                if ("2".equals(runType)) {
                    updateCmsBtPlatformNumiid(channelId, cartId, listSuccessNumiid, listErrorNumiid);
                }
                $info(String.format("天猫属性取得,成功%d个,失败%d个!", listSuccessNumiid.size(), listErrorNumiid.size()));
                listSuccessNumiid.clear();
                listErrorNumiid.clear();
            }
        }

        $info(String.format("天猫属性取得,成功%d个,失败%d个!", listSuccessNumiid.size(), listErrorNumiid.size()));
        if ("2".equals(runType)) {
            updateCmsBtPlatformNumiid(channelId, cartId, listSuccessNumiid, listErrorNumiid);
            if (ListUtils.notNull(listAllNumiid)) {
                // 存在没有搜到的numIId
                cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "3", getTaskName(), listSuccessNumiid);
            }
        }
    }

    private void updateCmsBtPlatformNumiid(String channelId, String cartId, List<String> listSuccessNumiid, List<String> listErrorNumiid) {
        if (listSuccessNumiid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "1", getTaskName(), listSuccessNumiid);
        }
        if (listErrorNumiid.size() > 0) {
            cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "2", getTaskName(), listErrorNumiid);
        }
    }

    /**
     * 将TM店铺自定义分类Model转换成CmsBtSellerCatModel
     */
    private List<CmsBtSellerCatModel> formatTMModel(List<SellerCat> list, String channelId, int cartId, String creator) {
        List<CmsBtSellerCatModel> result = new ArrayList<>();

        for (SellerCat model : list) {
            CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel();
            cmsBtSellerCatModel.setCatId(String.valueOf(model.getCid()));
            cmsBtSellerCatModel.setCatName(model.getName());
            cmsBtSellerCatModel.setParentCatId(String.valueOf(model.getParentCid()));
            cmsBtSellerCatModel.setChannelId(channelId);
            cmsBtSellerCatModel.setCartId(cartId);
            String now = DateTimeUtil.getNow();
            cmsBtSellerCatModel.setCreated(now);
            cmsBtSellerCatModel.setModified(now);
            cmsBtSellerCatModel.setCreater(creator);
            cmsBtSellerCatModel.setModifier(creator);
            result.add(cmsBtSellerCatModel);
        }


        return result;
    }

    // modified by morse.lu 2016/11/25 start
//    private List<Map<String, Object>> doSetSeller(ShopBean shopBean, Long nummIId, List<CmsBtSellerCatModel> sellerCat) throws Exception {
//        TbItemSchema schema = tbItemService.getUpdateSchema(shopBean, nummIId);
//
//        List<com.taobao.top.schema.field.Field> fields = schema.getFields();
//
//        List<String> cIds = new ArrayList<>();
//
//        for (com.taobao.top.schema.field.Field field : fields) {
//            if ("seller_cids".equals(field.getId())) {
//                List<String> values = ((com.taobao.top.schema.field.MultiCheckField) field).getDefaultValues();
//
//                for (String value : values) {
//                    cIds.add(value);
//                }
//            }
//        }
    private List<Map<String, Object>> doSetSeller(List<String> cIds, List<CmsBtSellerCatModel> sellerCat) throws Exception {
    // modified by morse.lu 2016/11/25 end
        List<Map<String, Object>> sellerCats = new ArrayList<>();

        if (ListUtils.notNull(cIds)) {
            for (String pCId : cIds) {
                CmsBtSellerCatModel leaf = sellerCat.stream().filter(w -> pCId.equals(w.getCatId())).findFirst().get();
                Map<String, Object> model = new HashMap<>();
                model.put("cId", leaf.getCatId());
                model.put("cName", leaf.getCatPath());
                model.put("cIds", leaf.getFullCatId().split("-"));
                model.put("cNames", leaf.getCatPath().split(">"));

                sellerCats.add(model);
            }
        }
        return sellerCats;
    }

    private void doSetProduct(ShopBean shopBean, CmsBtProductGroupModel cmsBtProductGroup, String channelId, int cartId, List<CmsBtSellerCatModel> sellerCat) throws Exception {
        Map<String, Object> fieldMap = new HashMap<>();
        // added by morse.lu 2016/11/25 start
        // 获取产品id和商品类目id并回填
        Item item = tbProductService.doGetItemInfo(cmsBtProductGroup.getNumIId(), "cid,product_id", shopBean).getItem();
        if (item == null) {
            throw new BusinessException(String.format("numIId:%s 天猫商品取得失败!", cmsBtProductGroup.getNumIId()));
        }
        Long itemProductId = item.getProductId();
        String platformPid = null;
        if (itemProductId != null && itemProductId.intValue() != 0) {
            platformPid = Long.toString(itemProductId);
        }
        cmsBtProductGroup.setPlatformPid(platformPid);
        fieldMap.put("cid", Long.toString(item.getCid()));
        // added by morse.lu 2016/11/25 end
        if (PlatFormEnums.PlatForm.TM.getId().equals(shopBean.getPlatform_id())) {
            // 只有天猫系才会更新fields字段
//            fieldMap.putAll(getPlatformProduct(cmsBtProductGroup.getPlatformPid(), shopBean));
            fieldMap.putAll(getPlatformProduct(platformPid, shopBean));
            fieldMap.putAll(getPlatformWareInfoItem(cmsBtProductGroup.getNumIId(), shopBean));
        }
        // modified by morse.lu 2016/11/25 start
//        List<Map<String, Object>> sellerCats = doSetSeller(shopBean, Long.parseLong(cmsBtProductGroup.getNumIId()), sellerCat);
        List<Map<String, Object>> sellerCats = doSetSeller((List<String>) fieldMap.get("seller_cids"), sellerCat);
        // modified by morse.lu 2016/11/25 end
        upProductPlatform(fieldMap, cmsBtProductGroup, channelId, cartId, sellerCats);
    }

    private void upProductPlatform(Map<String, Object> fieldMap, CmsBtProductGroupModel cmsBtProductGroup, String channelId, int cartId, List<Map<String, Object>> sellerCats) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        String pricePropName = getPricePropName(channelId, cartId);
        Map<String, Double> mapSkuSalePrice = getSalePriceBySku(channelId);
        Map<String, Double> mapNumIIdSalePrice = getSalePriceByNumIId(channelId);
        // added by morse.lu 2017/01/05 start
        // 用于之后价格同步
        List<Map<String, Object>> listProducts = new ArrayList<>();
        // added by morse.lu 2017/01/05 end
        final boolean[] hasErr = {false};
        cmsBtProductGroup.getProductCodes().forEach(s -> {
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", s);
            // added by morse.lu 2016/07/18 start
            CmsBtProductModel product = cmsBtProductDao.selectByCode(s, channelId);
            // modified by morse.lu 2016/11/18 start
            // 全小写比较skuCode
//            List<String> listSkuCode = product.getCommon().getSkus().stream().map(CmsBtProductModel_Sku::getSkuCode).collect(Collectors.toList());
//            List<String> listSkuCode = product.getCommon().getSkus().stream().map(sku -> sku.getSkuCode().toLowerCase()).collect(Collectors.toList());
            Map<String, BaseMongoMap<String, Object>> mapSkus = product.getPlatform(cartId).getSkus().stream()
                    .collect(Collectors.toMap(
                            sku -> sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name()).toLowerCase(),
                            sku -> sku)
                    );
            // modified by morse.lu 2016/11/18 end
            // added by morse.lu 2016/07/18 end
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("platforms.P" + cartId + ".modified", DateTimeUtil.getNowTimeStamp());
            updateMap.put("platforms.P" + cartId + ".sellerCats", sellerCats);
            final boolean[] hasPublishSku = {false, false}; // 第一个表示是否是sku级的，第二个表示本code是否有sku上新过
            fieldMap.forEach((s1, o) -> {
                // added by morse.lu 2016/07/18 start
                if ("sku".equals(s1) || "darwin_sku".equals(s1)) {
                    hasPublishSku[0] = true;
                    List<Map<String, Object>> upValSku = new ArrayList<>();
                    List<Map<String, Object>> listVal = (List) o;
                    listVal.forEach(skuVal -> {
                        // modified by morse.lu 2016/11/18 start
                        // 全小写比较skuCode
//                        if (listSkuCode.contains(skuVal.get("sku_outerId"))) {
                        if (skuVal.get("sku_outerId") == null || "".equals(skuVal.get("sku_outerId").toString())) {
                            hasErr[0] = true;
                        } else {
//                            if (listSkuCode.contains(skuVal.get("sku_outerId").toString().toLowerCase())) {
                            BaseMongoMap<String, Object> skuInfo = mapSkus.get(skuVal.get("sku_outerId").toString().toLowerCase());
                            if (skuInfo != null) {
                                upValSku.add(skuVal);
                                hasPublishSku[1] = true;
                                if (needWritePrice(channelId, pricePropName)) {
                                    // 回写price
                                    // added by morse.lu 2017/01/05 start
                                    // 用于之后价格同步
                                    Map<String, Object> products = new HashMap<>();
                                    products.put("channelId", channelId);
                                    products.put("productId", product.getProdId());
                                    products.put("cartId", cartId);
                                    listProducts.add(products);
                                    // added by morse.lu 2017/01/05 end
                                    String price = (String) skuVal.get("sku_price");
                                    skuInfo.setAttribute(pricePropName, Double.valueOf(price));
                                    skuInfo.setAttribute("priceRetail", Double.valueOf(price));
                                    if (mapSkuSalePrice != null) {
                                        // 需要回写 写死的priceSale
                                        double salePrice = mapSkuSalePrice.getOrDefault(skuVal.get("sku_outerId").toString().toLowerCase(), 9999999d);
                                        if (salePrice == 0d) {
                                            // 原价 - 1
                                            salePrice = Double.valueOf(price) - 1d;
                                        }
                                        skuInfo.setAttribute("priceSale", salePrice);
                                        skuInfo.setAttribute("priceRetail", salePrice);
                                    }
                                    if (mapNumIIdSalePrice != null) {
                                        // 需要回写 写死的priceSale
                                        double salePrice = mapNumIIdSalePrice.getOrDefault(cmsBtProductGroup.getNumIId(), 9999999d);
                                        if (salePrice == 0d) {
                                            // 原价 - 1
                                            salePrice = Double.valueOf(price) - 1d;
                                        }
                                        skuInfo.setAttribute("priceSale", salePrice);
                                        skuInfo.setAttribute("priceRetail", salePrice);
                                    }
                                }
                            }
                        }
                    });

                    if (needWritePrice(channelId, pricePropName)) {
                        // 回写price
                        updateMap.put("platforms.P" + cartId + ".skus", product.getPlatform(cartId).getSkus());
                    }
                    updateMap.put("platforms.P" + cartId + ".fields." + s1, upValSku);
//                } else if ("prop_13021751".equals(s1)) {
                    // 不要回写,model是主字段,会影响别的逻辑,改上新逻辑,货号优先去platform.P23.prop_13021751里取，取不到，再用model
//                    // 货号，回写进主商品common.fields.model
//                    updateMap.put("common.fields.model", o);
                } else {
                    // added by morse.lu 2016/07/18 end
                    updateMap.put("platforms.P" + cartId + ".fields." + s1, o);
                }
            });
            // added by morse.lu 2016/07/18 start
            // modified by morse.lu 2016/11/25 start
//            String catId = (String) fieldMap.get("cat_id"); // 类目ID
            String catId = (String) fieldMap.get("cid"); // 类目ID
            // modified by morse.lu 2016/11/25 end
            if (!StringUtils.isEmpty(catId)) {
                // 取到了再回写
                updateMap.put("platforms.P" + cartId + ".pCatId", catId);
//                CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(catId, 23);
                CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = platformCategoryService.getPlatformCatSchemaTm(catId, channelId, cartId);
                if (cmsMtPlatformCategorySchemaModel != null) {
                    updateMap.put("platforms.P" + cartId + ".pCatPath", cmsMtPlatformCategorySchemaModel.getCatFullPath());
                } else {
                    updateMap.put("platforms.P" + cartId + ".pCatPath", "");
                }
            } else{
                // 产品id错了，取不到产品信息
                $warn(String.format("PlatformPid[%s] numIid=[%s] 天猫上不存在!group表PlatformPid已经清除,product表的平台类目pCatId需要重新选择填写!", cmsBtProductGroup.getPlatformPid(), cmsBtProductGroup.getNumIId()));
                cmsBtProductGroup.setPlatformPid("");
            }

            if (!hasPublishSku[0]) {
                // product级
                if (needWritePrice(channelId, pricePropName)) {
                    // 回写price
                    // added by morse.lu 2017/01/05 start
                    // 用于之后价格同步
                    Map<String, Object> products = new HashMap<>();
                    products.put("channelId", channelId);
                    products.put("productId", product.getProdId());
                    products.put("cartId", cartId);
                    listProducts.add(products);
                    // added by morse.lu 2017/01/05 end
                    BaseMongoMap<String, Object> skuInfo = mapSkus.get(StringUtils.null2Space((String)fieldMap.get("outer_id")).toLowerCase());
                    if (skuInfo != null) {
                        String price = (String) fieldMap.get("price");
                        skuInfo.setAttribute(pricePropName, Double.valueOf(price));
                        skuInfo.setAttribute("priceRetail", Double.valueOf(price));
                        if (mapSkuSalePrice != null) {
                            // 需要回写 写死的priceSale
                            double salePrice = mapSkuSalePrice.getOrDefault(StringUtils.null2Space((String)fieldMap.get("outer_id")).toLowerCase(), 9999999d);
                            if (salePrice == 0d) {
                                // 原价 - 1
                                salePrice = Double.valueOf(price) - 1d;
                            }
                            skuInfo.setAttribute("priceSale", salePrice);
                            skuInfo.setAttribute("priceRetail", salePrice);
                        }
                        if (mapNumIIdSalePrice != null) {
                            // 需要回写 写死的priceSale
                            double salePrice = mapNumIIdSalePrice.getOrDefault(cmsBtProductGroup.getNumIId(), 9999999d);
                            if (salePrice == 0d) {
                                // 原价 - 1
                                salePrice = Double.valueOf(price) - 1d;
                            }
                            skuInfo.setAttribute("priceSale", salePrice);
                            skuInfo.setAttribute("priceRetail", salePrice);
                        }

                        updateMap.put("platforms.P" + cartId + ".skus", product.getPlatform(cartId).getSkus());
                    }
                }
            }

            if (!hasPublishSku[0] || hasPublishSku[1]) {
                // product级 或者 本code有sku上新过
                updateMap.put("platforms.P" + cartId + ".pProductId", cmsBtProductGroup.getPlatformPid());
                updateMap.put("platforms.P" + cartId + ".pNumIId", cmsBtProductGroup.getNumIId());
                String item_status = (String) fieldMap.get("item_status"); // 商品状态
                if ("0".equals(item_status)) {
                    // 出售中
                    updateMap.put("platforms.P" + cartId + ".pStatus", CmsConstants.PlatformStatus.OnSale.name());
                    updateMap.put("platforms.P" + cartId + ".pReallyStatus", CmsConstants.PlatformStatus.OnSale.name());
                } else {
                    // 仓库中
                    updateMap.put("platforms.P" + cartId + ".pStatus", CmsConstants.PlatformStatus.InStock.name());
                    updateMap.put("platforms.P" + cartId + ".pReallyStatus", CmsConstants.PlatformStatus.InStock.name());
                }
            }
            // added by morse.lu 2016/07/18 end
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        });

        cmsBtProductDao.bulkUpdateWithMap(cmsBtProductGroup.getChannelId(), bulkList, getTaskName(), "$set");

        if (hasErr[0]) {
            $warn(String.format("channelId:%s, cartId:%s, numIId:%s 存在outer_id为空的sku!", channelId, cartId, cmsBtProductGroup.getNumIId()));
        }

//        {
//            // price 回写进common.skus.size和platforms.P23.skus下的priceMsrp或priceSale
//            // size 先不回写
//            // 该group下的所有code
//            List<String> productCodeList = cmsBtProductGroup.getProductCodes();
//            String[] codeArr = new String[productCodeList.size()];
//            codeArr = productCodeList.toArray(codeArr);
//            List<CmsBtProductModel> productModelList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "}", channelId);
//
//            String pricePropName = getPricePropName(channelId);
//
//            List<Map<String, Object>> listSkus = (List) fieldMap.get("sku");
//            // Map<skuCode, Map<String, Object>>
//            Map<String, Map<String, Object>> mapSkus = listSkus.stream().collect(Collectors.toMap((p) -> (String) p.get("sku_outerId"), (p) -> p));
//
//            productModelList.forEach(model-> {
//                model.getPlatform(23).getSkus().forEach(sku -> {
//                    String skuCode = sku.getStringAttribute(CmsBtProductConstants.Platform_SKU_COM.skuCode.name());
//                    if (mapSkus.get(skuCode) != null) {
//                        String price = (String) mapSkus.get(skuCode).get("sku_price");
//                        sku.setStringAttribute(pricePropName, price);
//                    }
//                });
//
//                try {
//                    // ★★★★★此更新方法已经被干掉了，需要的话，本地打开★★★★★
//                    cmsBtProductDao.updateByModel(model);
//                } catch (BusinessException ex) {
//                    $warn("product表更新关闭!");
//                }
//            });
//        }

        // added by morse.lu 2016/07/18 start
        String item_status = (String) fieldMap.get("item_status"); // 商品状态
        if ("0".equals(item_status)) {
            // 出售中
            cmsBtProductGroup.setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
            cmsBtProductGroup.setPlatformActive(CmsConstants.PlatformActive.ToOnSale);
        } else {
            // 仓库中
            cmsBtProductGroup.setPlatformStatus(CmsConstants.PlatformStatus.InStock);
            cmsBtProductGroup.setPlatformActive(CmsConstants.PlatformActive.ToInStock);
        }
        productGroupService.update(cmsBtProductGroup);
        // added by morse.lu 2016/07/18 end

        // added by morse.lu 2017/01/05 start
        // 向Mq发送消息同步sku,code,group价格范围
//        listProducts.forEach(product -> sender.sendMessage(MqRoutingKey.CMS_TASK_ProdcutPriceUpdateJob, product));
        listProducts.forEach(product -> {
            try {
                cmsProcductPriceUpdateService.onStartup(product);
            } catch (Exception e) {
                $error(String.format("prodId[%s]价格同步失败!" + e.getMessage(), product.get("productId")));
            }
        });
        // added by morse.lu 2017/01/05 end
    }

    private String getPricePropName(String channelId, int cartId) {
        CmsChannelConfigBean sxPriceConfig = CmsChannelConfigs.getConfigBean(channelId
                                    , CmsConstants.ChannelConfig.PRICE_SX_KEY
                                    , String.valueOf(cartId) + CmsConstants.ChannelConfig.PRICE_SX_PRICE_CODE);

        if (sxPriceConfig == null) {
            return null;
        } else {
            return sxPriceConfig.getConfigValue1();
        }
    }

    private boolean needWritePrice(String channelId, String pricePropName) {
        boolean needWritePrice = false;
        if (!StringUtils.isEmpty(pricePropName)){
            if (channelId.equals(ChannelConfigEnums.Channel.WMF.getId())) {
                // WMF 回写price
                needWritePrice = true;
            }
        }

        return needWritePrice;
    }

    public Map<String, Object> getPlatformProduct(String productId, ShopBean shopBean) throws Exception {
        fieldHashMap fieldMap = new fieldHashMap();
        if(StringUtils.isEmpty(productId)) return fieldMap;
        String schema = tbProductService.getProductSchema(Long.parseLong(productId), shopBean);
        if (schema != null) {
            List<Field> fields = SchemaReader.readXmlForList(schema);


            fields.forEach(field -> {
                fields2Map(field, fieldMap);
            });
        }
        return fieldMap;

    }

    public Map<String, Object> getPlatformWareInfoItem(String numIid, ShopBean shopBean) throws Exception {
        fieldHashMap fieldMap = new fieldHashMap();
        String schema = tbProductService.doGetWareInfoItem(numIid, shopBean).getUpdateItemResult();
        $debug("取得天猫商品信息schema:" + schema);
        if (schema != null) {
            List<Field> fields = SchemaReader.readXmlForList(schema);
            fields.forEach(field -> {
                fields2Map(field, fieldMap);
            });
        }
        return fieldMap;

    }

    private void fields2Map(Field field, fieldHashMap fieldMap) {

        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                fieldMap.put(inputField.getId(), inputField.getDefaultValue());
                break;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                fieldMap.put(multiInputField.getId(), multiInputField.getDefaultValues());
                break;
            case LABEL:
                return;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                fieldMap.put(singleCheckField.getId(), singleCheckField.getDefaultValue());
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                fieldMap.put(multiCheckField.getId(), multiCheckField.getDefaultValues());
                break;
            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                fieldHashMap values = new fieldHashMap();
                if (complexField.getDefaultComplexValue() != null) {
                    for (String fieldId : complexField.getDefaultComplexValue().getFieldKeySet()) {
                        values.put(fieldId, getFieldValue(complexField.getDefaultComplexValue().getValueField(fieldId)));
                    }
                }
                fieldMap.put(field.getId(), values);
                break;
            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Map<String, Object>> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getDefaultComplexValues() != null) {
                    for (ComplexValue item : multiComplexField.getDefaultComplexValues()) {
                        fieldHashMap obj = new fieldHashMap();
                        for (String fieldId : item.getFieldKeySet()) {
                            obj.put(fieldId, getFieldValue(item.getValueField(fieldId)));
                        }
                        multiComplexValues.add(obj);
                    }
                }
                fieldMap.put(multiComplexField.getId(), multiComplexValues);
                break;
        }
    }

    private Object getFieldValue(Field field) {
        List<String> values;
        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                return inputField.getValue();

            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                values = new ArrayList<>();
                multiInputField.getValues().forEach(value -> values.add(value.getValue()));
                return values;

            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                return singleCheckField.getValue().getValue();

            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                values = new ArrayList<>();
                multiCheckField.getValues().forEach(value -> values.add(value.getValue()));
                return values;

            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                Map<String, Field> fieldMap = complexField.getFieldMap();
                fieldHashMap complexValues = new fieldHashMap();
                for (String key : fieldMap.keySet()) {
                    complexValues.put(key, getFieldValue(fieldMap.get(key)));
                }
                return complexValues;

            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                List<Object> multiComplexValues = new ArrayList<>();
                if (multiComplexField.getFieldMap() != null) {
                    for (ComplexValue item : multiComplexField.getComplexValues()) {
                        for (String fieldId : item.getFieldKeySet()) {
                            multiComplexValues.add(getFieldValue(item.getValueField(fieldId)));
                        }
                    }
                }
                return multiComplexValues;
        }

        return null;
    }

    class fieldHashMap extends HashMap<String, Object> {
        @Override
        public Object put(String key, Object value) {
            if (value == null) {
                return value;
            }
            return super.put(StringUtils.replaceDot(key), value);
        }
    }

    /**
     * 将店铺自定义分类列转成一组树
     */
    private List<CmsBtSellerCatModel> convert2Tree(List<CmsBtSellerCatModel> sellCatList) {
        List<CmsBtSellerCatModel> roots = findRoots(sellCatList);
        List<CmsBtSellerCatModel> notRoots = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(sellCatList, roots);
        for (CmsBtSellerCatModel root : roots) {
            List<CmsBtSellerCatModel> children = findChildren(root, notRoots);
            root.setChildren(children);
        }
        return roots;

    }

    /**
     * 查找所有子节点
     */
    private List<CmsBtSellerCatModel> findChildren(CmsBtSellerCatModel root, List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> children = new ArrayList<>();

        for (CmsBtSellerCatModel comparedOne : allNodes) {
            if (comparedOne.getParentCatId().equals(root.getCatId())) {
                children.add(comparedOne);
                comparedOne.setCatPath(root.getCatPath() + ">" + comparedOne.getCatName());
                comparedOne.setFullCatId(root.getFullCatId() + "-" + comparedOne.getCatId());
            }
        }
        root.setChildren(children);
        if (!children.isEmpty()) {
            root.setIsParent(1);
        } else {
            root.setIsParent(0);
        }

        List<CmsBtSellerCatModel> notChildren = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(allNodes, children);

        for (CmsBtSellerCatModel child : children) {
            List<CmsBtSellerCatModel> tmpChildren = findChildren(child, notChildren);

            child.setChildren(tmpChildren);
        }

        return children;
    }

    /**
     * 查找所有根节点
     */
    private List<CmsBtSellerCatModel> findRoots(List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> results = new ArrayList<>();
        for (CmsBtSellerCatModel node : allNodes) {
            if ("0".equals(node.getParentCatId())) {
                results.add(node);
                node.setCatPath(node.getCatName());
                node.setFullCatId(node.getCatId());
            }
        }
        return results;
    }


    // 销售价格，写死，用于回写platforms.PXX.skus.priceSale
    private Map<String, Double> getSalePriceBySku(String channelId) {
        Map<String, Double> ret = null; // Map<skuCode, price>
//        if (channelId.equals(ChannelConfigEnums.Channel.WMF.getId())) {
//            ret = new HashMap<>();
//            ret.put("".toLowerCase(), 0d);
//        }

        return ret;
    }

    // 销售价格，写死，用于回写platforms.PXX.skus.priceSale
    private Map<String, Double> getSalePriceByNumIId(String channelId) {
        Map<String, Double> ret = null; // Map<numIId, price>
        if (channelId.equals(ChannelConfigEnums.Channel.WMF.getId())) {
            ret = new HashMap<>();
            ret.put("526567438118", 593.00d);
            ret.put("526590128569", 599.00d);
            ret.put("526589892926", 729.00d);
            ret.put("526567686052", 633.00d);
            ret.put("526575757168", 641.00d);
            ret.put("526575545427", 751.00d);
            ret.put("526590040756", 794.00d);
            ret.put("526986032359", 752.00d);
            ret.put("538225741105", 967.00d);
            ret.put("538181754518", 1153.00d);
            ret.put("538264708057", 1462.00d);
            ret.put("538225373889", 930.00d);
            ret.put("538225385856", 1065.00d);
            ret.put("538225621431", 1285.00d);
            ret.put("538105851651", 1588.00d);
            ret.put("537511320640", 717.00d);
            ret.put("537511532291", 273.00d);
            ret.put("537464366156", 268.00d);
            ret.put("537843630295", 1187.00d);
            ret.put("537925568357", 1017.00d);
            ret.put("526581074283", 577.00d);
            ret.put("537765923520", 220.00d);
            ret.put("537925480537", 278.00d);
            ret.put("526589565601", 583.00d);
            ret.put("526580586997", 649.00d);
            ret.put("538239520109", 958.00d);
            ret.put("538239360361", 958.00d);
            ret.put("540659700187", 614.00d);
            ret.put("540669690289", 497.00d);
            ret.put("538239240676", 395.00d);
            ret.put("538198945132", 160.00d);
            ret.put("538155051324", 160.00d);
            ret.put("538155382232", 159.00d);
            ret.put("538275461710", 221.00d);
            ret.put("526919642686", 998.00d);
            ret.put("537432874269", 899.00d);
            ret.put("526569874133", 413.00d);
            ret.put("526943136467", 545.00d);
            ret.put("538198877945", 330.00d);
            ret.put("538154995501", 531.00d);
            ret.put("526578273585", 269.00d);
            ret.put("537511208966", 697.00d);
            ret.put("538199125648", 370.00d);
            ret.put("538199305290", 291.00d);
            ret.put("537432830386", 338.00d);
            ret.put("538105543999", 815.00d);
            ret.put("538205661606", 1416.00d);
            ret.put("538264248859", 1030.00d);
            ret.put("537546425195", 244.00d);
            ret.put("537431055285", 371.00d);
            ret.put("537925256927", 372.00d);
            ret.put("538039275859", 220.00d);
            ret.put("538239024405", 160.00d);
            ret.put("538205661546", 229.00d);
            ret.put("538264284837", 1089.00d);
            ret.put("538225229895", 243.00d);
            ret.put("526544603508", 255.00d);
            ret.put("526580742756", 382.00d);
            ret.put("526544415756", 400.00d);
            ret.put("538245612679", 457.00d);
            ret.put("538105635745", 331.00d);
            ret.put("538080075865", 334.00d);
            ret.put("540670463742", 277.00d);
            ret.put("537871361026", 288.00d);
            ret.put("538114746604", 161.00d);
            ret.put("537585664899", 158.00d);
            ret.put("538114822597", 161.00d);
            ret.put("537431071330", 155.00d);
            ret.put("537511348761", 335.00d);
            ret.put("537506978196", 355.00d);
            ret.put("537802223719", 279.00d);
            ret.put("537883733986", 401.00d);
            ret.put("537430811811", 336.00d);
            ret.put("537506890263", 449.00d);
            ret.put("537464410499", 223.00d);
            ret.put("537585648909", 358.00d);
            ret.put("526580910551", 240.00d);
            ret.put("526604652235", 240.00d);
            ret.put("526580934543", 262.00d);
            ret.put("526544759331", 239.00d);
            ret.put("538239052753", 321.00d);
            ret.put("526604540499", 980.00d);
            ret.put("526544627603", 448.00d);
            ret.put("526589553728", 374.00d);
            ret.put("540670961133", 215.00d);
            ret.put("538264296646", 697.00d);
            ret.put("537430931580", 207.00d);
            ret.put("537464502354", 391.00d);
            ret.put("537389031009", 221.00d);
            ret.put("537546529087", 158.00d);
            ret.put("537357771306", 225.00d);
            ret.put("537507006146", 568.00d);
            ret.put("538105915088", 214.00d);
            ret.put("526544715453", 561.00d);
            ret.put("526589949157", 322.00d);
            ret.put("526544943118", 498.00d);
            ret.put("538105559924", 636.00d);
            ret.put("538231206706", 514.00d);
            ret.put("526580914656", 431.00d);
            ret.put("526544967101", 327.00d);
            ret.put("537430935632", 162.00d);
            ret.put("540665256500", 796.00d);
            ret.put("540669646106", 613.00d);
            ret.put("538199141306", 162.00d);
            ret.put("537912048992", 220.00d);
            ret.put("537508753893", 1033.00d);
            ret.put("538157993020", 276.00d);
            ret.put("538157469944", 374.00d);
            ret.put("538114930963", 584.00d);
            ret.put("538114666864", 215.00d);
            ret.put("538245308253", 334.00d);
            ret.put("538225233862", 160.00d);
            ret.put("538155003563", 188.00d);
            ret.put("526997560321", 385.00d);
            ret.put("540668726763", 1133.00d);
            ret.put("538225745269", 1373.00d);
            ret.put("538225545706", 1554.00d);
            ret.put("537585940376", 1313.00d);
            ret.put("537586100066", 1589.00d);
            ret.put("537506478953", 1973.00d);
            ret.put("538231506148", 369.00d);
            ret.put("538264380709", 1283.00d);
            ret.put("537431123185", 1013.00d);
            ret.put("538225853130", 1433.00d);
            ret.put("526567022996", 609.00d);
            ret.put("526590304250", 712.00d);
            ret.put("526575721254", 532.00d);
            ret.put("526590384152", 664.00d);
            ret.put("526567158761", 460.00d);
            ret.put("526575589433", 344.00d);
            ret.put("526575837129", 994.00d);
            ret.put("526577005396", 1091.00d);
            ret.put("538155091414", 376.00d);
            ret.put("538275909045", 486.00d);
            ret.put("538314048354", 656.00d);
            ret.put("538225485453", 207.00d);
            ret.put("538225365652", 212.00d);
            ret.put("538225413668", 264.00d);
            ret.put("538909713360", 550.00d);
            ret.put("538085459788", 536.00d);
            ret.put("526918047733", 1148.00d);
            ret.put("526918155482", 1358.00d);
            ret.put("537546029989", 1999.00d);
            ret.put("526530483693", 1105.00d);
            ret.put("526918019779", 1506.00d);
            ret.put("526567386333", 1657.00d);
            ret.put("526917887979", 1221.00d);
            ret.put("526575805205", 1673.00d);
            ret.put("526590056819", 1953.00d);
            ret.put("526567194672", 1133.00d);
            ret.put("526530987010", 1502.00d);
            ret.put("538314208008", 314.00d);
            ret.put("538155307095", 371.00d);
            ret.put("537506754588", 1999.00d);
            ret.put("537656892367", 1999.00d);
            ret.put("538314064286", 270.00d);
            ret.put("537617149013", 1866.00d);
            ret.put("538154975791", 159.00d);
            ret.put("538314040463", 159.00d);
            ret.put("538314108294", 160.00d);
            ret.put("538275733525", 214.00d);
            ret.put("538155035664", 236.00d);
            ret.put("538039715471", 746.00d);
            ret.put("526530535972", 1385.00d);
            ret.put("526931110461", 1673.00d);
            ret.put("526930938604", 1808.00d);
            ret.put("526935379131", 1978.00d);
            ret.put("537890853514", 1999.00d);
            ret.put("538275877304", 159.00d);
            ret.put("538314040557", 160.00d);
            ret.put("538275993079", 1095.00d);
            ret.put("526935375145", 1935.00d);
            ret.put("526894307364", 1999.00d);
            ret.put("538080387662", 352.00d);
            ret.put("538231382662", 1585.00d);
            ret.put("537932412276", 1999.00d);
            ret.put("538275737639", 1243.00d);
            ret.put("537932072779", 1999.00d);
            ret.put("537802167698", 536.00d);
            ret.put("526931074475", 385.00d);
            ret.put("538155479005", 353.00d);
            ret.put("538275777552", 250.00d);
            ret.put("537616981372", 134.00d);
            ret.put("537920437896", 218.00d);
            ret.put("537920749308", 211.00d);
            ret.put("526604628495", 351.00d);
            ret.put("537577150754", 247.00d);
            ret.put("526593108273", 1373.00d);
            ret.put("538238952937", 188.00d);
            ret.put("538198801935", 186.00d);
            ret.put("540659060566", 207.00d);
            ret.put("538199057436", 182.00d);
            ret.put("540658852843", 206.00d);
            ret.put("538239332301", 167.00d);
            ret.put("538155362798", 165.00d);
            ret.put("540668646457", 401.00d);
            ret.put("540664541869", 659.00d);
            ret.put("540669891030", 326.00d);
            ret.put("540659168420", 414.00d);
            ret.put("538199169260", 171.00d);
            ret.put("540669727546", 278.00d);
            ret.put("538080407552", 174.00d);
            ret.put("538238960948", 173.00d);
            ret.put("540669631587", 472.00d);
            ret.put("540668526893", 839.00d);
            ret.put("540664861368", 424.00d);
            ret.put("537831054108", 1797.00d);
            ret.put("540668458794", 453.00d);
            ret.put("538239344235", 189.00d);
            ret.put("537920801235", 550.00d);
            ret.put("540659208595", 346.00d);
            ret.put("537577162899", 1999.00d);
            ret.put("540664845291", 839.00d);
            ret.put("538198825896", 184.00d);
            ret.put("540668786287", 264.00d);
            ret.put("538198957681", 183.00d);
            ret.put("538155758199", 182.00d);
            ret.put("538079727915", 154.00d);
            ret.put("538080035511", 153.00d);
            ret.put("540659192492", 441.00d);
            ret.put("540664829388", 767.00d);
            ret.put("540659224436", 284.00d);
            ret.put("540664769551", 394.00d);
            ret.put("540664701697", 1641.00d);
            ret.put("540668638805", 1702.00d);
            ret.put("540668870331", 421.00d);
            ret.put("538080347751", 190.00d);
            ret.put("537920897111", 554.00d);
            ret.put("538420530022", 155.00d);
            ret.put("537656768865", 1788.00d);
            ret.put("538080859854", 182.00d);
            ret.put("537656784794", 1999.00d);
            ret.put("540659448144", 411.00d);
            ret.put("540669807639", 381.00d);
            ret.put("537500475561", 1193.00d);
            ret.put("538465813714", 155.00d);
            ret.put("537657268020", 324.00d);
            ret.put("537802335948", 518.00d);
            ret.put("537835984486", 275.00d);
            ret.put("526593104907", 396.00d);
            ret.put("537802667014", 624.00d);
            ret.put("537961464432", 646.00d);
            ret.put("526593200690", 663.00d);
            ret.put("537879402096", 646.00d);
            ret.put("538231422576", 394.00d);
            ret.put("537961324830", 647.00d);
            ret.put("537836044686", 401.00d);
            ret.put("537920993134", 647.00d);
            ret.put("526882847715", 398.00d);
            ret.put("537879202545", 649.00d);
            ret.put("526570006566", 339.00d);
            ret.put("538114778724", 213.00d);
            ret.put("526544975082", 162.00d);
            ret.put("537961568352", 643.00d);
            ret.put("537677171660", 377.00d);
            ret.put("537755370090", 278.00d);
            ret.put("538158165501", 291.00d);
            ret.put("537794613322", 909.00d);
            ret.put("537802823225", 403.00d);
            ret.put("538039567182", 217.00d);
            ret.put("537676955879", 218.00d);
            ret.put("538157865217", 266.00d);
            ret.put("538039555158", 211.00d);
            ret.put("537677083664", 154.00d);
            ret.put("538155311283", 218.00d);
            ret.put("537836096647", 274.00d);
            ret.put("538231530456", 160.00d);
            ret.put("538231394696", 419.00d);
            ret.put("537677339305", 392.00d);
            ret.put("526578253629", 179.00d);
            ret.put("537802451351", 648.00d);
            ret.put("537802823015", 644.00d);
            ret.put("540665537901", 660.00d);
            ret.put("537802655284", 651.00d);
            ret.put("537794257998", 391.00d);
            ret.put("537879470212", 643.00d);
            ret.put("537802747205", 637.00d);
            ret.put("526567506220", 1087.00d);
            ret.put("526575789222", 1368.00d);
            ret.put("526590572035", 1563.00d);
            ret.put("526575741319", 990.00d);
            ret.put("526590524138", 1673.00d);
            ret.put("526590536116", 1349.00d);
            ret.put("541417967468", 1494.00d);
            ret.put("538155666473", 498.00d);
            ret.put("538080279847", 644.00d);
            ret.put("526575873261", 953.00d);
            ret.put("526575905163", 1053.00d);
            ret.put("526530443179", 1112.00d);
            ret.put("526590580063", 1493.00d);
            ret.put("526575469888", 1634.00d);
            ret.put("526567170870", 1219.00d);
            ret.put("526567646056", 1673.00d);
            ret.put("526530939178", 1804.00d);
            ret.put("526575725556", 1738.00d);
            ret.put("526567310538", 1942.00d);
            ret.put("526530511924", 1193.00d);
            ret.put("526590316457", 1133.00d);
            ret.put("526450179964", 1073.00d);
            ret.put("540670447587", 156.00d);
            ret.put("537794605253", 158.00d);
            ret.put("526577337045", 236.00d);
            ret.put("538231454700", 235.00d);
            ret.put("526532171621", 238.00d);
            ret.put("526532087686", 179.00d);
            ret.put("526568922062", 208.00d);
            ret.put("526531871889", 198.00d);
            ret.put("526591824384", 238.00d);
            ret.put("526576905863", 151.00d);
            ret.put("526568550916", 224.00d);
            ret.put("526568886206", 157.00d);
            ret.put("526591780943", 238.00d);
            ret.put("526593064439", 238.00d);
            ret.put("526577317215", 209.00d);
            ret.put("526568874672", 222.00d);
            ret.put("526569042004", 209.00d);
            ret.put("526591912383", 240.00d);
            ret.put("526532459441", 157.00d);
            ret.put("537836000813", 215.00d);
            ret.put("526591596890", 1146.00d);
            ret.put("526591772609", 243.00d);
            ret.put("537920793621", 346.00d);
            ret.put("526531959969", 336.00d);
            ret.put("526577257422", 236.00d);
            ret.put("526604664509", 490.00d);
            ret.put("537921009261", 231.00d);
            ret.put("526532499041", 566.00d);
            ret.put("526532167717", 498.00d);
            ret.put("526577429119", 220.00d);
            ret.put("526544779495", 519.00d);
            ret.put("526532247559", 486.00d);
            ret.put("526568778614", 244.00d);
            ret.put("526532447142", 209.00d);
            ret.put("526532495082", 245.00d);
            ret.put("526577509040", 248.00d);
            ret.put("526531879906", 222.00d);
            ret.put("526568558895", 250.00d);
            ret.put("526576989563", 509.00d);
            ret.put("526532487107", 354.00d);
            ret.put("526568846756", 246.00d);
            ret.put("526568638924", 272.00d);
            ret.put("526591556896", 257.00d);
            ret.put("526532091822", 510.00d);
            ret.put("537677343364", 222.00d);
            ret.put("526532139620", 221.00d);
            ret.put("526592024489", 505.00d);
            ret.put("538039599893", 1024.00d);
            ret.put("526592172605", 361.00d);
            ret.put("526577461119", 220.00d);
            ret.put("537830666626", 158.00d);
            ret.put("537836196479", 158.00d);
            ret.put("537794553537", 210.00d);
            ret.put("526532527295", 206.00d);
            ret.put("526577737663", 209.00d);
            ret.put("526592272191", 263.00d);
            ret.put("526568670787", 299.00d);
            ret.put("538252292680", 1526.00d);
            ret.put("526592196066", 248.00d);
            ret.put("537676939975", 243.00d);
            ret.put("537794461737", 158.00d);
            ret.put("526591956630", 212.00d);
            ret.put("537923932502", 130.00d);
            ret.put("537755498066", 159.00d);
            ret.put("537836352343", 159.00d);
            ret.put("538093363704", 217.00d);
            ret.put("538169014965", 217.00d);
            ret.put("537755298467", 156.00d);
            ret.put("537677415383", 156.00d);
            ret.put("537836180714", 156.00d);
            ret.put("538231730112", 216.00d);
            ret.put("538275925407", 216.00d);
            ret.put("538203932203", 216.00d);
            ret.put("538155231554", 415.00d);
            ret.put("538039603517", 556.00d);
            ret.put("538155015996", 431.00d);
            ret.put("538155379352", 523.00d);
            ret.put("538039903018", 639.00d);
            ret.put("526592372350", 533.00d);
            ret.put("537881337628", 159.00d);
            ret.put("526532423512", 609.00d);
            ret.put("526577721063", 1133.00d);
            ret.put("526533119574", 1193.00d);
            ret.put("538039951232", 615.00d);
            ret.put("538115278623", 604.00d);
            ret.put("538115118598", 658.00d);
            ret.put("538199256279", 1143.00d);
            ret.put("538115018685", 377.00d);
            ret.put("538039875176", 1966.00d);
            ret.put("526592084418", 5573.00d);
            ret.put("526592040495", 3971.00d);
            ret.put("538115066261", 268.00d);
            ret.put("526577605797", 987.00d);
            ret.put("526577337559", 860.00d);
            ret.put("526569194168", 1999.00d);
            ret.put("526533375554", 248.00d);
            ret.put("526533127906", 154.00d);
            ret.put("537753307201", 547.00d);
            ret.put("537871273214", 432.00d);
            ret.put("540669533130", 1493.00d);
            ret.put("538114834752", 267.00d);
            ret.put("526532559649", 285.00d);
            ret.put("526592492229", 361.00d);
            ret.put("537830666807", 652.00d);
            ret.put("526532919056", 213.00d);
            ret.put("526577933047", 239.00d);
            ret.put("526592208638", 299.00d);
            ret.put("537830922257", 547.00d);
            ret.put("538157649542", 266.00d);
            ret.put("526569022239", 555.00d);
            ret.put("537912188922", 713.00d);
            ret.put("526577409433", 833.00d);
            ret.put("526532467408", 1013.00d);
            ret.put("526532627187", 893.00d);
            ret.put("537840958616", 1157.00d);
            ret.put("526577457414", 1999.00d);
            ret.put("537871061714", 713.00d);
            ret.put("535936530139", 1216.00d);
            ret.put("537890549713", 1612.00d);
            ret.put("537871241397", 1193.00d);
            ret.put("537912436566", 1073.00d);
            ret.put("538275777723", 1999.00d);
            ret.put("537932064394", 1999.00d);
            ret.put("537912512362", 833.00d);
            ret.put("538199041886", 192.00d);
            ret.put("537849658537", 1973.00d);
            ret.put("526532447287", 1999.00d);
            ret.put("526532407339", 539.00d);
            ret.put("526532675149", 526.00d);
            ret.put("526569230140", 833.00d);
            ret.put("526578405193", 893.00d);
            ret.put("526577501397", 833.00d);
            ret.put("526577289662", 833.00d);
            ret.put("526592036505", 833.00d);
            ret.put("526533511389", 713.00d);
            ret.put("526533799305", 1999.00d);
            ret.put("538275913554", 439.00d);
            ret.put("538314192514", 439.00d);
            ret.put("526570022447", 713.00d);
            ret.put("537763467165", 445.00d);
            ret.put("538155131897", 1133.00d);
            ret.put("538114842439", 132.00d);
            ret.put("538039311646", 151.00d);
            ret.put("538198868770", 331.00d);
            ret.put("538039427628", 225.00d);
            ret.put("538115162115", 297.00d);
            ret.put("538157853093", 216.00d);
            ret.put("538157641499", 232.00d);
            ret.put("538157737562", 378.00d);
            ret.put("538039419895", 518.00d);
            ret.put("538114954747", 471.00d);
            ret.put("538199152182", 393.00d);
            ret.put("538115234309", 603.00d);
            ret.put("538198816995", 517.00d);
            ret.put("538114734797", 255.00d);
            ret.put("538115238085", 331.00d);
            ret.put("538198752727", 203.00d);
            ret.put("538115046168", 163.00d);
            ret.put("538198668817", 189.00d);
            ret.put("538199405180", 188.00d);
            ret.put("538080555438", 181.00d);
            ret.put("538080679254", 166.00d);
            ret.put("538155118892", 119.00d);
            ret.put("538155790409", 237.00d);
            ret.put("538156398090", 226.00d);
            ret.put("538199505131", 341.00d);
            ret.put("538239752981", 298.00d);
            ret.put("537759771499", 130.00d);
            ret.put("538200017131", 280.00d);
            ret.put("538080615493", 176.00d);
            ret.put("538199421353", 188.00d);
            ret.put("538198589775", 149.00d);
            ret.put("538080267227", 140.00d);
            ret.put("537922412458", 399.00d);
            ret.put("537763071434", 631.00d);
            ret.put("542481614806", 1999d);
            ret.put("538525812879", 1999d);
            ret.put("540342258971", 1999d);
            ret.put("539108338827", 1999d);
            ret.put("542455408193", 1999d);
            ret.put("538504096534", 1960d);
            ret.put("542452720502", 1936d);
            ret.put("542471757533", 1366d);
            ret.put("538924022577", 1366d);
            ret.put("537765943529", 0d);
            ret.put("526580998405", 0d);
            ret.put("526592916272", 0d);
            ret.put("526591684632", 0d);
            ret.put("526580870683", 0d);
            ret.put("526580810638", 0d);
            ret.put("526580730786", 0d);
            ret.put("526604376804", 0d);
            ret.put("526567670122", 0d);
            ret.put("526567182827", 0d);
            ret.put("526530347987", 0d);
            ret.put("526591488981", 0d);
            ret.put("526568942121", 0d);
            ret.put("526935095543", 0d);
            ret.put("526566990887", 0d);
            ret.put("526532211337", 0d);
            ret.put("526954044583", 0d);
            ret.put("526604816113", 0d);
            ret.put("526973030874", 1999d);
            ret.put("538106087179", 1750d);
            ret.put("538275777730", 1493d);
            ret.put("526532915325", 1142d);
            ret.put("526575813254", 856d);
            ret.put("526575777156", 745d);
            ret.put("537921093346", 666d);
            ret.put("526589469938", 637d);
            ret.put("526604424738", 576d);
            ret.put("526569910086", 539d);
            ret.put("526567094874", 456d);
            ret.put("526589577769", 398d);
            ret.put("526589345994", 364d);
            ret.put("526576961749", 355d);
            ret.put("526592944501", 347d);
            ret.put("537925420580", 267d);
            ret.put("538313972538", 266d);
            ret.put("540659360230", 262d);
            ret.put("526604428778", 257d);
            ret.put("526589593681", 247d);
            ret.put("538420478191", 155d);
            ret.put("526589825516", 0d);
            ret.put("526589709448", 0d);
            ret.put("526974286594", 0d);
            ret.put("526604620316", 0d);
            ret.put("526569942061", 0d);
            ret.put("526569146194", 0d);
            ret.put("526577137489", 0d);
            ret.put("526577069630", 0d);
            ret.put("526577337157", 0d);
            ret.put("526575905146", 0d);
            ret.put("526592060743", 0d);
            ret.put("537925784040", 0d);
            ret.put("537925592412", 0d);
            ret.put("537925512497", 0d);
            ret.put("526997604222", 0d);
            ret.put("537357583762", 0d);
            ret.put("537657144091", 0d);
            ret.put("537577282592", 0d);
            ret.put("537500507467", 0d);
            ret.put("537500483433", 0d);
            ret.put("537500267749", 0d);
            ret.put("537473121183", 0d);
            ret.put("537676979929", 0d);
            ret.put("537617165111", 0d);
            ret.put("537577594087", 0d);
            ret.put("537835976871", 0d);
            ret.put("537794541460", 0d);
            ret.put("526532807551", 0d);
            ret.put("526581158167", 0d);
            ret.put("537577450296", 0d);
            ret.put("526533443334", 0d);
            ret.put("526533471111", 0d);
            ret.put("535938286373", 0d);
            ret.put("535937810885", 0d);
            ret.put("535967985687", 0d);
            ret.put("536009236256", 0d);
            ret.put("537657164148", 0d);
            ret.put("537617229047", 0d);
            ret.put("537546421176", 0d);
            ret.put("526964577028", 0d);
            ret.put("526593104289", 0d);
            ret.put("526592992270", 0d);
            ret.put("526589493792", 0d);
            ret.put("526578541163", 0d);
            ret.put("526577277041", 0d);
            ret.put("526569530904", 0d);
            ret.put("526545019015", 0d);
            ret.put("526544767293", 0d);
            ret.put("526544731536", 0d);
            ret.put("526533135889", 0d);
            ret.put("526577301366", 0d);
            ret.put("526939117499", 0d);
            ret.put("526578237839", 0d);
            ret.put("526578097692", 0d);
            ret.put("526930974535", 0d);
            ret.put("526580918522", 0d);
            ret.put("526928029631", 0d);
            ret.put("526575533615", 0d);
            ret.put("526544683537", 0d);
            ret.put("526568646695", 0d);
            ret.put("526568606816", 0d);
            ret.put("526577305399", 0d);
            ret.put("526577369295", 0d);
            ret.put("526590149241", 0d);
            ret.put("526592756491", 0d);
            ret.put("526532387046", 0d);
            ret.put("526936351102", 0d);
            ret.put("526928273325", 0d);
            ret.put("526590240476", 0d);
            ret.put("526580738821", 0d);
            ret.put("526577149502", 0d);
            ret.put("526575505486", 0d);
            ret.put("526575389863", 0d);
            ret.put("526569846924", 0d);
            ret.put("526567058878", 0d);
            ret.put("537546505017", 1999d);
            ret.put("537772387601", 1999d);
            ret.put("526568826857", 1999d);
            ret.put("537772479490", 1999d);
            ret.put("537577198825", 1999d);
            ret.put("526569014420", 1999d);
            ret.put("526996404675", 1999d);
            ret.put("526953956572", 1999d);
            ret.put("538231726192", 1999d);
            ret.put("526978832874", 1999d);
            ret.put("526590144658", 1999d);
            ret.put("540668522909", 1835d);
            ret.put("526575985057", 1583d);
            ret.put("538811209110", 1582d);
            ret.put("526590612062", 1493d);
            ret.put("526590336382", 1251d);
            ret.put("537357523380", 1216d);
            ret.put("537755470065", 1199d);
            ret.put("526997556283", 1196d);
            ret.put("538231742053", 1193d);
            ret.put("526532779448", 893d);
            ret.put("526530635506", 771d);
            ret.put("543941618582", 621d);
            ret.put("538501824611", 620d);
            ret.put("540660360251", 618d);
            ret.put("540668942198", 614d);
            ret.put("540659284929", 609d);
            ret.put("540670175157", 594d);
            ret.put("526931158421", 581d);
            ret.put("526954156442", 577d);
            ret.put("538093495049", 567d);
            ret.put("526544587773", 565d);
            ret.put("538157917441", 554d);
            ret.put("526938985782", 551d);
            ret.put("537884257248", 458d);
            ret.put("537766043449", 449d);
            ret.put("538275357977", 431d);
            ret.put("526589465968", 429d);
            ret.put("538199236084", 401d);
            ret.put("526894319447", 395d);
            ret.put("526580886656", 388d);
            ret.put("538157869409", 379d);
            ret.put("540659844116", 377d);
            ret.put("537925524643", 377d);
            ret.put("526591768624", 370d);
            ret.put("526592012304", 360d);
            ret.put("538231246785", 359d);
            ret.put("537766035385", 357d);
            ret.put("526581182361", 353d);
            ret.put("537843334800", 350d);
            ret.put("537883937701", 321d);
            ret.put("538231582163", 307d);
            ret.put("538157653675", 303d);
            ret.put("526580826561", 300d);
            ret.put("538231566111", 276d);
            ret.put("537925724367", 275d);
            ret.put("537849834530", 275d);
            ret.put("540665421411", 268d);
            ret.put("538157521781", 268d);
            ret.put("537766151325", 267d);
            ret.put("526532163734", 253d);
            ret.put("537432902318", 252d);
            ret.put("526589557824", 251d);
            ret.put("526591784669", 249d);
            ret.put("526592232032", 247d);
            ret.put("526544611563", 240d);
            ret.put("526591552981", 238d);
            ret.put("537617001350", 231d);
            ret.put("537883885906", 230d);
            ret.put("537920785299", 225d);
            ret.put("538231486313", 218d);
            ret.put("538168882928", 217d);
            ret.put("538093315407", 210d);
            ret.put("526544887297", 192d);
            ret.put("526544875277", 187d);
            ret.put("538155706780", 186d);
            ret.put("538238964899", 183d);
            ret.put("540659560492", 162d);
            ret.put("537884385032", 161d);
            ret.put("537677415029", 161d);
            ret.put("537881061607", 159d);
            ret.put("537766019541", 155d);
            ret.put("538157309945", 149d);
            ret.put("537755258629", 130d);
        }

        return ret;
    }
}
