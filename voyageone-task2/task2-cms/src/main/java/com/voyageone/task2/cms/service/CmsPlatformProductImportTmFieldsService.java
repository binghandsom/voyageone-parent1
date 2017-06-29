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
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
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
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_TMFieldsImportCms2Job)
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

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

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
                cmsBtPlatformNumiidDaoExt.updateStatusByNumiids(channelId, Integer.valueOf(cartId), "3", getTaskName(), listAllNumiid);
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
                } else {
                    // added by morse.lu 2016/07/18 end
                    updateMap.put("platforms.P" + cartId + ".fields." + s1, o);
                }
            });
            // added by morse.lu 2016/07/18 start
            // modified by morse.lu 2016/11/25 start
            String catId = (String) fieldMap.get("cid"); // 类目ID
            // modified by morse.lu 2016/11/25 end
            if (!StringUtils.isEmpty(catId)) {
                // 取到了再回写
                updateMap.put("platforms.P" + cartId + ".pCatId", catId);
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
        // edward 2017-02-16 价格范围范围计算交由各自的业务处理,不再通过mq发送方式处理
        listProducts.forEach(product -> {
            ProductPriceUpdateMQMessageBody productPriceUpdateMQMessageBody = new ProductPriceUpdateMQMessageBody();
            productPriceUpdateMQMessageBody.setChannelId(channelId);
            productPriceUpdateMQMessageBody.setProdId((Long) product.get("productId"));
            productPriceUpdateMQMessageBody.setCartId((Integer) product.get("cartId"));
            productPriceUpdateMQMessageBody.setSender(CmsMqRoutingKey.CMS_BATCH_TMFieldsImportCms2Job);
            cmsMqSenderService.sendMessage(productPriceUpdateMQMessageBody);
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
            // 如果以后要临时写死就扔这里， 用完后要删掉
            if (channelId.equals(ChannelConfigEnums.Channel.REAL_MADRID.getId())) {
                // WMF 回写price
                needWritePrice = true;
            }
        }

        return needWritePrice;
    }

    public Map<String, Object> getPlatformProduct(String productId, ShopBean shopBean) throws Exception {
        fieldHashMap fieldMap = new fieldHashMap();
        if(StringUtils.isEmpty(productId)) return fieldMap;
//        String schema = tbProductService.getProductSchema(Long.parseLong(productId), shopBean);
		StringBuffer failCause = null;
		String schema = tbProductService.getProductUpdateSchema(Long.parseLong(productId), shopBean, failCause);
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

//                if ("cspu_list".equals(field.getId())) {
//                    if (multiComplexField.getFields() != null) {
//                        for (Field f : multiComplexField.getFields()) {
//                            ComplexField item = (ComplexField)f;
//                            fieldHashMap obj = new fieldHashMap();
//                            for (Field ff : item.getFields()) {
//                                obj.put(ff.getId(), getFieldDefaultValue(ff));
//                            }
//                            multiComplexValues.add(obj);
//                        }
//                    }
//                } else {
                    if (multiComplexField.getDefaultComplexValues() != null) {
                        for (ComplexValue item : multiComplexField.getDefaultComplexValues()) {
                            fieldHashMap obj = new fieldHashMap();
                            for (String fieldId : item.getFieldKeySet()) {
                                obj.put(fieldId, getFieldValue(item.getValueField(fieldId)));
                            }
                            multiComplexValues.add(obj);
                        }
                    }
//                }
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

//    private Object getFieldDefaultValue(Field field) {
//        List<String> values;
//        switch (field.getType()) {
//            case INPUT:
//                InputField inputField = (InputField) field;
//                return inputField.getDefaultValue();
//
//            case MULTIINPUT:
//                MultiInputField multiInputField = (MultiInputField) field;
//                values = new ArrayList<>();
//                multiInputField.getDefaultValues().forEach(value -> values.add(value));
//                return values;
//
//            case SINGLECHECK:
//                SingleCheckField singleCheckField = (SingleCheckField) field;
//                return singleCheckField.getDefaultValue();
//
//            case MULTICHECK:
//                MultiCheckField multiCheckField = (MultiCheckField) field;
//                values = new ArrayList<>();
//                multiCheckField.getDefaultValues().forEach(value -> values.add(value));
//                return values;
//
//            case COMPLEX:
//                ComplexField complexField = (ComplexField) field;
//                Map<String, Field> fieldMap = complexField.getFieldMap();
//                fieldHashMap complexValues = new fieldHashMap();
//                for (String key : fieldMap.keySet()) {
//                    complexValues.put(key, getFieldDefaultValue(fieldMap.get(key)));
//                }
//                return complexValues;
//
//            case MULTICOMPLEX:
//                MultiComplexField multiComplexField = (MultiComplexField) field;
//                List<Object> multiComplexValues = new ArrayList<>();
//                if (multiComplexField.getFieldMap() != null) {
//                    for (ComplexValue item : multiComplexField.getComplexValues()) {
//                        for (String fieldId : item.getFieldKeySet()) {
//                            multiComplexValues.add(getFieldDefaultValue(item.getValueField(fieldId)));
//                        }
//                    }
//                }
//                return multiComplexValues;
//        }
//
//        return null;
//    }

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
        // 如果以后要临时写死就扔这里， 用完后要删掉
//        if (channelId.equals(ChannelConfigEnums.Channel.WMF.getId())) {
//            ret = new HashMap<>();
////            ret.put("537755258629", 130d);
//        }

        return ret;
    }
}
