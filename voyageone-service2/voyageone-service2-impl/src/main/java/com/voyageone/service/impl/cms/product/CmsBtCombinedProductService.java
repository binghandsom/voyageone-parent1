package com.voyageone.service.impl.cms.product;

import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.response.ware.WareListResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateDelistingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateListingResponse;
import com.mongodb.WriteResult;
import com.taobao.api.ApiException;
import com.taobao.api.internal.parser.json.ObjectJsonParser;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
import com.taobao.api.response.TmallItemCombineGetResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.field.MultiComplexField;
import com.taobao.top.schema.field.SingleCheckField;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.JsonUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.jd.service.JdSaleService;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.components.jumei.reponse.HtMallStatusUpdateBatchResponse;
import com.voyageone.components.jumei.service.JumeiSaleService;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.components.tmall.service.TbSaleService;
import com.voyageone.service.bean.cms.product.CmsBtCombinedProductBean;
import com.voyageone.service.bean.cms.product.CmsBtCombinedProductPlatformStatus;
import com.voyageone.service.bean.cms.product.CmsBtCombinedProductStatus;
import com.voyageone.service.bean.cms.product.CombinedSkuInfoBean;
import com.voyageone.service.dao.cms.mongo.CmsBtCombinedProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtCombinedProductLogDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.fields.cms.CmsBtShelvesProductModelStatus;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.enums.PlatformType;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel_Sku_Item;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import org.apache.avro.data.Json;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rex.wu on 2016/11/28.
 */
@Service
public class CmsBtCombinedProductService extends BaseService {

    private static final String ACTION_TYPE_ADD = "add";
    private static final String ACTION_TYPE_EDIT = "edit";

    @Autowired
    private TbProductService tbProductService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtCombinedProductDao cmsBtCombinedProductDao;
    @Autowired
    private TbSaleService tbSaleService;
    @Autowired
    private JdSaleService jdSaleService;
    @Autowired
    private JdWareService jdWareService;
    @Autowired
    private JumeiSaleService jmSaleService;
    @Autowired
    private CmsBtCombinedProductLogDao cmsBtCombinedProductLogDao;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * 获取平台组合套装商品数据
     * @param numId
     * @param channelId
     * @param cartId
     * @param local true表示和本地mongo进行虚拟SKU取交集，false表示纯取平台数据
     * @return
     */
    public CmsBtCombinedProductModel getCombinedProductPlatformDetail(String numId, String channelId, Integer cartId, boolean local) {
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        /*shopBean.setAppKey("21008948");
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("620230429acceg4103a72932e22e4d53856b145a192140b2854639042");
        shopBean.setShop_name("Target海外旗舰店");*/
        if (shopBean != null) {
            long threadNo = Thread.currentThread().getId();
            $info("threadNo:" + threadNo + " numiid:" + numId );
            CmsBtCombinedProductModel productBean = new CmsBtCombinedProductModel();
            productBean.setCartId(cartId);
            productBean.setChannelId(channelId);
            productBean.setNumID(numId);
            if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.TMALL.getPlatformId().toString())) {
                try {
                    TmallItemUpdateSchemaGetResponse itemUpdateSchemaGetResponse = tbProductService.doGetWareInfoItem(numId, shopBean);
                    if (itemUpdateSchemaGetResponse == null || !itemUpdateSchemaGetResponse.isSuccess()) {
                        $info("threadNo:" + threadNo + " numiid:" + numId +" 取得异常 response:" + itemUpdateSchemaGetResponse == null ? "null" : itemUpdateSchemaGetResponse.getBody());
                        throw new BusinessException("获取天猫商品数据出错了，请先确认商品在平台上是否存在或已删除，然后确认平台接口是否畅通！");
                    } else {
                        Map<String, Field> fieldMap = SchemaReader.readXmlForMap(itemUpdateSchemaGetResponse.getUpdateItemResult());
                        InputField titleField = (InputField) fieldMap.get("title");
                        productBean.setProductName(titleField.getDefaultValue()); // 组合套装商品名称
                        MultiComplexField skuField = (MultiComplexField) fieldMap.get("sku");
                        // 平台状态
                        SingleCheckField itemStatus = (SingleCheckField) fieldMap.get("item_status");
                        Integer platformStatus = "0".equalsIgnoreCase(itemStatus.getDefaultValue())? 1 : 0;
                        productBean.setPlatformStatus(platformStatus);
                        boolean oneSku = false;
                        if (null == skuField || null == skuField.getDefaultComplexValues() || skuField.getDefaultComplexValues().size() == 0) {
                            oneSku = true;
                            InputField outerIdField = (InputField) fieldMap.get("outer_id");
                            InputField priceField = (InputField) fieldMap.get("price");
                            String outerId = outerIdField.getDefaultValue();
                            String price = priceField.getDefaultValue();
                            CmsBtCombinedProductModel_Sku skuBean = new CmsBtCombinedProductModel_Sku();
                            skuBean.setSuitSkuCode(outerId);
                            skuBean.setSuitPreferentialPrice(Double.valueOf(price));
                            productBean.getSkus().add(skuBean);
                        } else {
                            oneSku = false;
                            skuField.getDefaultComplexValues().forEach(skuValue -> {
                                CmsBtCombinedProductModel_Sku skuBean = new CmsBtCombinedProductModel_Sku();
                                skuBean.setSkuId(skuValue.getInputFieldValue("sku_id"));
                                skuBean.setSuitSkuCode(skuValue.getInputFieldValue("sku_outerId"));
                                skuBean.setSuitPreferentialPrice(Double.parseDouble(skuValue.getInputFieldValue("sku_price")));
                                productBean.getSkus().add(skuBean);
                            });
                        }

                        // 再去调tmall.item.combine.get接口
                        TmallItemCombineGetResponse resp = tbProductService.getTmallTtemCombine(numId, shopBean);
                        if (resp == null || !resp.isSuccess()) {
                            $info("threadNo:" + threadNo + " numiid:" + numId +" 取得异常 response:" + resp == null ? "null" : resp.getBody());
                            throw new BusinessException("获取天猫组合商品数据出错了！");
                        } else {
                            if (CollectionUtils.isEmpty(resp.getResults()) || StringUtils.isBlank(resp.getResults().get(0))) {
                                if (local) {
                                    productBean.getSkus().forEach(skuBean -> {
                                        if (skuBean.getSkuItems().size() < 1) {
                                            // 初始化一个空的真实SKU对象，以便在页面显示一个真实SKU输入框组
                                            skuBean.getSkuItems().add(new CmsBtCombinedProductModel_Sku_Item());
                                        }
                                    });
                                }
                            } else {
                                String json = resp.getResults().get(0);
                                if (json.startsWith("\"")) {
                                    json = json.substring(1);
                                }
                                if (json.endsWith("\"")) {
                                    json = json.substring(0, json.length() -1);
                                }
                                json = json.replaceAll("\\\\\"", "\"");
                                TmallItemCombine tmallItemCombine = JsonUtil.jsonToBean(json, TmallItemCombine.class);
                                this.combineTmallProductSku(productBean.getSkus(), tmallItemCombine, oneSku);
                            }
                            return productBean;
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof BusinessException) {
                        throw new BusinessException(e.getMessage());
                    }
                }
            } else if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.JD.getPlatformId().toString())) {
                try {
                    WareListResponse wareListResponse = jdWareService.getJdProduct(shopBean, numId, "ware_id,skus");
                    if (wareListResponse != null) {
                        Ware ware = wareListResponse.getWareList().get(0);
                        productBean.setProductName(ware.getTitle());
                        Integer platformStatus = "ON_SALE".equalsIgnoreCase(ware.getWareStatus()) ? CmsBtCombinedProductPlatformStatus.ON_SHELVES : CmsBtCombinedProductPlatformStatus.OFF_SHELVES;
                        productBean.setPlatformStatus(platformStatus);
                        for (Sku item:wareListResponse.getWareList().get(0).getSkus()){
                            CmsBtCombinedProductModel_Sku skuBean = new CmsBtCombinedProductModel_Sku();
                            skuBean.setSuitSkuCode(item.getOuterId());
                            skuBean.setSuitPreferentialPrice(Double.parseDouble(item.getJdPrice()));
                            productBean.getSkus().add(skuBean);
                        };
                        this.compareSuitSkuWithLocal(productBean, local);
                        return productBean;
                    } else {
                        $info("threadNo:" + threadNo + " numiid:" + numId +" 取得异常");
                        throw new BusinessException("获取天猫商品数据出错了！");
                    }
                } catch (Exception e) {
                    $info("threadNo:" + threadNo + " numiid:" + numId +" 取得异常");
                    e.printStackTrace();
                    $error(e);
                    throw new BusinessException("获取京东商品数据出错了！");
                }
            }
        }
        return null;
    }

    /**
     * 将天猫商品数据接口和天猫组合商品数据接口返回数据合起来
     * @param skus
     * @param tmallItemCombine
     * @param oneSku 该组合商品数据是否只有一个虚拟SKU
     */
    private void combineTmallProductSku(List<CmsBtCombinedProductModel_Sku> skus, TmallItemCombine tmallItemCombine, boolean oneSku) throws CloneNotSupportedException {
        if (CollectionUtils.isNotEmpty(skus) && tmallItemCombine != null && CollectionUtils.isNotEmpty(tmallItemCombine.getSkuList())) {
            if (!oneSku) {
                for (CmsBtCombinedProductModel_Sku skuBean:skus) {
                    for (TmallItemCombineSku tmallItemCombineSku:tmallItemCombine.getSkuList()) {
                        if (skuBean.getSkuId().equals(tmallItemCombineSku.getSkuId())) {
                            for (TmallItemCombineSubSku subSku:tmallItemCombineSku.getSubSkuList()) {
                                CmsBtCombinedProductModel_Sku_Item skuItem = new CmsBtCombinedProductModel_Sku_Item();
                                skuItem.setSkuCode(subSku.getOuterId());
                                skuItem.setPreferentialPrice(Double.valueOf(subSku.getPrice()));
                                // 循环subItemList
                                String ratioVal = "";
                                for (TmallItemCombinedSubItem subItem:tmallItemCombine.getSubItemList()) {
                                    if (subSku.getOuterId().equals(subItem.getOuterId()) && subSku.getSubItemId().equalsIgnoreCase(subItem.getSubItemId())) {
                                        //skuItem.setProductName(subItem.getTitle()); // 真实SKU商品名称
                                        ratioVal = subItem.getRatio();
                                        break;
                                    }
                                }
                                int ratio = 0;
                                if (StringUtils.isNumeric(ratioVal)) {
                                    ratio = Integer.parseInt(ratioVal);
                                }
                                for (int i = 0; i < ratio; i++) {
                                    skuBean.getSkuItems().add((CmsBtCombinedProductModel_Sku_Item)skuItem.clone());
                                }
                            }
                            break;
                        }
                    }
                }
            } else {
                CmsBtCombinedProductModel_Sku skuBean = skus.get(0);
                TmallItemCombineSku tmallItemCombineSku = tmallItemCombine.getSkuList().get(0);
                for (TmallItemCombineSubSku subSku:tmallItemCombineSku.getSubSkuList()) {
                    CmsBtCombinedProductModel_Sku_Item skuItem = new CmsBtCombinedProductModel_Sku_Item();
                    skuItem.setSkuCode(subSku.getOuterId()); // 真实skuCode
                    skuItem.setPreferentialPrice(Double.valueOf(subSku.getPrice()));
                    // 循环subItemList
                    String ratioVal = "";
                    for (TmallItemCombinedSubItem subItem:tmallItemCombine.getSubItemList()) {
                        if (subSku.getOuterId().equals(subItem.getOuterId()) && subSku.getSubItemId().equalsIgnoreCase(subItem.getSubItemId())) {
                            //skuItem.setProductName(subItem.getTitle());
                            ratioVal = subItem.getRatio();
                            break;
                        }
                    }
                    int ratio = 0;
                    if (StringUtils.isNumeric(ratioVal)) {
                        ratio = Integer.parseInt(ratioVal);
                    }
                    for (int i = 0; i < ratio; i++) {
                        skuBean.getSkuItems().add((CmsBtCombinedProductModel_Sku_Item)skuItem.clone());
                    }
                }
            }
        }
    }

    /**
     * 组合套装商品->批量查询真实SKU详情
     * @param skuCodes
     * @param channelId
     * @param cartId
     * @return
     */
    public List<CmsBtCombinedProductModel_Sku_Item> batchGetSkuDetail(Set<String> skuCodes, String channelId, Integer cartId) {
        List<CmsBtCombinedProductModel_Sku_Item> skuDetails = null;
        if (CollectionUtils.isNotEmpty(skuCodes) && StringUtils.isNotBlank(channelId) && cartId != null) {
            JongoQuery queryObj = new JongoQuery();
            queryObj.addQuery("{'platforms.P#.skus.skuCode':{$in:#}}");
            queryObj.setParameters(cartId, skuCodes.toArray());
            List<CmsBtProductModel> products = cmsBtProductDao.select(queryObj, channelId);
            if (CollectionUtils.isNotEmpty(products)) {
                skuDetails = new ArrayList<>();
                for (CmsBtProductModel product:products) {
                    CmsBtProductModel_Platform_Cart platFormCart = product.getPlatform(cartId);
                    List<BaseMongoMap<String, Object>> skus = null;
                    if (platFormCart != null && CollectionUtils.isNotEmpty(skus = platFormCart.getSkus())) {
                        for (BaseMongoMap<String, Object> skuMap:skus) {
                            String skuCode = skuMap.getStringAttribute("skuCode");
                            if (skuCodes.contains(skuCode)) {
                                CmsBtCombinedProductModel_Sku_Item skuItem = new CmsBtCombinedProductModel_Sku_Item();
                                skuItem.setCode(product.getCommonNotNull().getFieldsNotNull().getCode());
                                skuItem.setSkuCode(skuCode);
                                skuItem.setProductName(product.getCommonNotNull().getFieldsNotNull().getOriginalTitleCn());
                                skuItem.setSkuCode(skuCode);
                                skuItem.setSellingPriceCn(skuMap.getDoubleAttribute("priceSale"));
                                skuDetails.add(skuItem);
                            }
                        }
                    }
                }
            }
        }
        return skuDetails;
    }


    private void compareSuitSkuWithLocal(CmsBtCombinedProductModel product, boolean local) {
        if (product != null && StringUtils.isNotBlank(product.getNumID()) && StringUtils.isNotBlank(product.getChannelId()) && product.getCartId() != null) {
            JongoQuery queryObj = new JongoQuery();
            queryObj.addQuery("{'active':1,'channelId':#,'cartId':#,'numID':#}");
            queryObj.addParameters(product.getChannelId(), product.getCartId(), product.getNumID());
            CmsBtCombinedProductModel localProduct = cmsBtCombinedProductDao.selectOneWithQuery(queryObj);
            if (localProduct != null && CollectionUtils.isNotEmpty(localProduct.getSkus())) {
                Map<String, CmsBtCombinedProductModel_Sku> localSkus = new HashMap<String, CmsBtCombinedProductModel_Sku>();
                for (CmsBtCombinedProductModel_Sku sku:localProduct.getSkus()) {
                    localSkus.put(sku.getSuitSkuCode(), sku);
                }
                for (CmsBtCombinedProductModel_Sku sku:product.getSkus()) {
                    if (localSkus.containsKey(sku.getSuitSkuCode())) {
                        sku.setSkuItems(localSkus.get(sku.getSuitSkuCode()).getSkuItems());
                        if (local && CollectionUtils.isEmpty(sku.getSkuItems())) {
                            sku.getSkuItems().add(new CmsBtCombinedProductModel_Sku_Item());
                        }
                    }else {
                        if (local) {
                            sku.getSkuItems().add(new CmsBtCombinedProductModel_Sku_Item());
                        }
                    }
                }
            }
        }

    }

    /**
     * 根据单个skuCode获取SKU详情
     * @param skuCode
     * @param channelId
     * @param cartId
     * @return
     */
    public CmsBtCombinedProductModel_Sku_Item getSkuDetail (String skuCode, String channelId, Integer cartId) {
        CmsBtCombinedProductModel_Sku_Item skuItem = null;
        CmsBtProductModel product = productService.getProductBySku(channelId, skuCode);
        CmsBtProductModel_Platform_Cart cart = null;
        List<BaseMongoMap<String, Object>> skus = null;
        if (product != null && (cart = product.getPlatform(cartId)) != null && CollectionUtils.isNotEmpty(skus = cart.getSkus())) {
            for (BaseMongoMap<String, Object> sku:skus) {
                if (skuCode.equals(sku.getStringAttribute("skuCode"))) {
                    skuItem = new CmsBtCombinedProductModel_Sku_Item();
                    skuItem.setProductName(product.getCommonNotNull().getFieldsNotNull().getOriginalTitleCn());
                    skuItem.setCode(product.getCommonNotNull().getFieldsNotNull().getCode());
                    skuItem.setSkuCode(skuCode);
                    skuItem.setSellingPriceCn(sku.getDoubleAttribute("priceSale"));
                    break;
                }
            }
        }
        return skuItem;
    }

    /**
     * 新增组合商品
     * @param product
     * @param channelId
     * @param user
     */
    public void addCombinedProduct (CmsBtCombinedProductModel product, String channelId, String user) {
        // 校验输入值是否合法
        checkCombinedProducrtModel(product, channelId, ACTION_TYPE_ADD);
        product.setCreater(user);
        product.setCreated(DateTimeUtil.getNow());
        if (product.getStatus() == null || !CmsBtCombinedProductStatus.KV.containsKey(product.getStatus())) {
            product.setStatus(CmsBtCombinedProductStatus.TEMPORAL); // 默认暂存状态
        }
        /*product.setPlatformStatus(CmsBtCombinedProductPlatformStatus.OFF_SHELVES);*/
        product.setChannelId(channelId);
        WriteResult rs = cmsBtCombinedProductDao.insert(product);
        $debug("新增 组合套装商品 结果 " + rs.toString());

        // 添加操作日志
        CmsBtCombinedProductLogModel logModel = new CmsBtCombinedProductLogModel();
        logModel.setProductId(product.get_id());
        logModel.setNumID(product.getNumID());
        logModel.setChannelId(product.getChannelId());
        logModel.setCartId(product.getCartId());
        logModel.setOperater(user);
        logModel.setOperateTime(product.getCreated());
        logModel.setOperateType("新建");
        logModel.setOperateContent("新建成功");
        logModel.setStatus(product.getStatus());
        logModel.setPlatformStatus(product.getPlatformStatus());
        WriteResult rs_log = cmsBtCombinedProductLogDao.insert(logModel);
        $debug("新增 组合套装商品操作日志 结果 " + rs_log.toString());
    }

    public Map<String, Object> search (int page, int pageSize, CmsBtCombinedProductBean searchBean) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        JongoQuery queryObj = new JongoQuery();
        List<Object> params = new ArrayList<Object>();
        queryObj.addQuery("{'active' : 1}");
        if (StringUtils.isNotBlank(searchBean.getChannelId())) {
            queryObj.addQuery("{'channelId' : #}");
            params.add(searchBean.getChannelId());
        }
        if (StringUtils.isNotBlank(searchBean.getProductName())) {
            queryObj.addQuery("{'productName' : {'$regex' : #}}");
            params.add(searchBean.getProductName());
        }
        if (StringUtils.isNotBlank(searchBean.getNumIDs())) {
            queryObj.addQuery("{'numID' : {$in : #}}");
            params.add(searchBean.getNumIDs().split("\n"));
        }
        if (CollectionUtils.isNotEmpty(searchBean.getStatuses())) {
            queryObj.addQuery("{'status' : {$in : #}}");
            params.add(searchBean.getStatuses());
        }
        if (CollectionUtils.isNotEmpty(searchBean.getPlatformStatuses())) {
            queryObj.addQuery("{'platformStatus' : {$in : #}}");
            params.add(searchBean.getPlatformStatuses());
        }
        if (StringUtils.isNotBlank(searchBean.getSkuCodes())) {
            String[] skuCodes = searchBean.getSkuCodes().split("\n");
            queryObj.addQuery("{$or : [{'skus.suitSkuCode' : {$in : #}}, {'skus.skuItems.skuCode' : {$in : #}}]}");
            params.add(skuCodes);
            params.add(skuCodes);
        }
        queryObj.setParameters(params.toArray());
        long total = cmsBtCombinedProductDao.countByQuery(queryObj.getQuery(), queryObj.getParameters());
        resultMap.put("total", total);

        queryObj.setSort("{'modified' : -1, 'created' : -1}");
        if (page <= 0) {
            page = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        queryObj.setSkip(pageSize * (page - 1)).setLimit(pageSize);
        List<CmsBtCombinedProductModel> products = cmsBtCombinedProductDao.select(queryObj);
        resultMap.put("products", products);
        return resultMap;
    }

    /**
     * 逻辑删除组合套装商品
     * @param modelBean
     * @param user
     * @param channelId
     */
    public void deleteCombinedProduct(CmsBtCombinedProductBean modelBean, String user, String channelId) {
        /*String query = String.format("{'numID':'%s', 'channelId':'%s'}", modelBean.getNumID(), channelId);
        CmsBtCombinedProductModel target = cmsBtCombinedProductDao.selectOneWithQuery(query);*/
        CmsBtCombinedProductModel target = null;
        if (modelBean == null || StringUtils.isBlank(modelBean.get_id()) || (target = cmsBtCombinedProductDao.selectById(modelBean.get_id())) == null) {
            throw new BusinessException("要删除的组合套装商品不存在！");
        }
        if (target.getActive() != null && target.getActive().intValue() != 1) {
            throw new BusinessException("要删除的组合套装商品已被删除，请勿重复操作！");
        }
        target.setModifier(user);
        target.setModified(DateTimeUtil.getNow());
        target.setActive(0);
        WriteResult rs = cmsBtCombinedProductDao.update(target);
        $debug("删除 组合套装商品 结果 " + rs.toString());

        // 添加操作日志
        CmsBtCombinedProductLogModel logModel = new CmsBtCombinedProductLogModel();
        logModel.setProductId(modelBean.get_id());
        logModel.setNumID(modelBean.getNumID());
        logModel.setChannelId(modelBean.getChannelId());
        logModel.setCartId(modelBean.getCartId());
        logModel.setOperater(user);
        logModel.setOperateTime(modelBean.getModified());
        logModel.setOperateType("删除");
        logModel.setOperateContent("删除成功");
        logModel.setStatus(modelBean.getStatus());
        logModel.setPlatformStatus(modelBean.getPlatformStatus());
        WriteResult rs_log = cmsBtCombinedProductLogDao.insert(logModel);
        $debug("删除 组合套装商品操作日志 结果 " + rs_log.toString());
    }

    /**
     * 从MongoDB中查询组合套装商品
     * @param model
     * @param channelId
     * @return
     */
    public CmsBtCombinedProductModel getCombinedProduct (CmsBtCombinedProductModel model, String channelId) {
        if (model == null || StringUtils.isBlank(model.getNumID()) || StringUtils.isBlank(channelId) || model.getCartId() == null) {
            throw new BusinessException("参数错误！");
        }
        JongoQuery queryObj = new JongoQuery();
        queryObj.addQuery("{'active':1, 'channelId':#,'cartId':#,'numID':#}");
        queryObj.addParameters(channelId, model.getCartId(), model.getNumID());
        return cmsBtCombinedProductDao.selectOneWithQuery(queryObj);
    }

    /**
     * 根据ID查找组合套装商品
     * @param _id
     * @return
     */
    public CmsBtCombinedProductModel selectById(String _id) {
        if (StringUtils.isNotBlank(_id))
            return cmsBtCombinedProductDao.selectById(_id);
        return null;
    }

    /**
     * 编辑组合套装商品
     * @param model
     * @param user
     * @param channelId
     */
    public void editCombinedProduct (CmsBtCombinedProductModel model, String channelId, String user) {
        checkCombinedProducrtModel(model, channelId, ACTION_TYPE_EDIT);
        model.setModifier(user);
        model.setModified(DateTimeUtil.getNow());
        WriteResult rs = cmsBtCombinedProductDao.update(model);
        $debug("编辑 组合套装商品 结果 " + rs.toString());

        // 添加操作日志
        CmsBtCombinedProductLogModel logModel = new CmsBtCombinedProductLogModel();
        logModel.setProductId(model.get_id());
        logModel.setNumID(model.getNumID());
        logModel.setChannelId(model.getChannelId());
        logModel.setCartId(model.getCartId());
        logModel.setOperater(user);
        logModel.setOperateTime(model.getModified());
        logModel.setOperateType("编辑");
        logModel.setOperateContent("编辑成功");
        logModel.setStatus(model.getStatus());
        logModel.setPlatformStatus(model.getPlatformStatus());
        WriteResult rs_log = cmsBtCombinedProductLogDao.insert(logModel);
        $debug("编辑 组合套装商品操作日志 结果 " + rs_log.toString());
    }

    /**
     * 新增或者编辑时校验组合套装商品
     * @param model
     * @param channelId
     * @param actionType
     */
    public void checkCombinedProducrtModel (CmsBtCombinedProductModel model, String channelId, String actionType) {
        // 如果是暂存，不进行check，只需输入numId和选择cartId就行了
        if (model != null && model.getStatus() != null && model.getStatus().intValue() == CmsBtCombinedProductStatus.TEMPORAL) {
            if (model.getCartId() == null || StringUtils.isBlank(model.getNumID())) {
                throw new BusinessException("组合套装商品暂存，请至少输入平台和商品编码！");
            }
            return;
        }
        List<CmsBtCombinedProductModel_Sku> skus = model.getSkus();
        if (model == null || StringUtils.isBlank(model.getNumID()) || model.getCartId() == null ) {
            throw new BusinessException("参数错误！");
        }
        if (CollectionUtils.isEmpty(skus = model.getSkus())) {
            throw new BusinessException("组合套装商品SKU为空！");
        }
        int startSupplyChain = 0; // 店铺是否启动了供应链管理
        CmsChannelConfigBean startSupplyChainConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.START_SUPPLY_CHAIN);
        if (startSupplyChainConfig != null && "1".equals(startSupplyChainConfig.getConfigValue1()) && StringUtils.isBlank(model.getWuliubaoCode())) {
            throw new BusinessException("店铺启用了供应链管理，请填写物流宝商品后台编码！");
        }
        CmsBtCombinedProductModel targetModel = null;
        if (ACTION_TYPE_EDIT.equals(actionType)) {
            String _id = model.get_id();
            if (StringUtils.isBlank(_id) || (targetModel = cmsBtCombinedProductDao.selectById(_id)) == null) {
                $error("要编辑的组合套装(id=" + _id + ")不存在！");
                throw new BusinessException("要编辑的组合套装商品不存在！");
            }
            /* 编辑组合套装商品时，不允许修改numID*/
            if (!targetModel.getNumID().equals(model.getNumID())) {
                throw new BusinessException("组合套装商品numID不允许修改！");
            }
        }else if (ACTION_TYPE_ADD.equals(actionType)) {
            CmsBtCombinedProductModel existModel = this.getCombinedProduct(model, channelId);
            if (existModel != null) {
                throw new BusinessException("组合套装商品(numID=" + model.getNumID() + ")已经存在了！");
            }
        }else {
            throw new BusinessException("异常操作！");
        }

        Map<String, Double> suitSkuMap = new HashMap<String, Double>();
        for (CmsBtCombinedProductModel_Sku skuBean:skus) {
            suitSkuMap.put(skuBean.getSuitSkuCode(), skuBean.getSuitPreferentialPrice());
            List<CmsBtCombinedProductModel_Sku_Item> skuItems = skuBean.getSkuItems();
            if (CollectionUtils.isEmpty(skuItems)) {
                throw new BusinessException("组合套装SKU(" + skuBean.getSuitSkuCode() + ")的实际sku为空！");
            }
            double tempSuitSellingPriceCn = 0; // 每个组合套装SKU的最终中国售价计算
            double tempSuitPreferentialPrice = 0; // 每个组合套装SKU的最终优惠售价，需要和平台上售价保持一致
            for (CmsBtCombinedProductModel_Sku_Item skuItem:skuItems) {
                if (StringUtils.isBlank(skuItem.getSkuCode()) || skuItem.getPreferentialPrice() == null) {
                    throw new BusinessException("组合套装SKU(" + skuBean.getSuitSkuCode() + ")下真实SKU必填项为空！");
                }
                CmsBtCombinedProductModel_Sku_Item tempSkuItem = this.getSkuDetail(skuItem.getSkuCode(), channelId, model.getCartId());
                if (tempSkuItem == null) {
                    throw new BusinessException("真实SKU(" + skuItem.getSkuCode() + ")不存在！");
                }
                skuItem.setProductName(tempSkuItem.getProductName());
                tempSuitSellingPriceCn += tempSkuItem.getSellingPriceCn() == null ? 0 : tempSkuItem.getSellingPriceCn().doubleValue();
                tempSuitPreferentialPrice += skuItem.getPreferentialPrice().doubleValue();
            }
            skuBean.setSuitSellingPriceCn(tempSuitSellingPriceCn);
            if (tempSuitPreferentialPrice != skuBean.getSuitPreferentialPrice()) {
                throw new BusinessException("组合套装SKU(" + skuBean.getSuitSkuCode() + ")和真实SKU优惠售价之和不一致！");
            }
        }

        CmsBtCombinedProductModel platformModel = this.getCombinedProductPlatformDetail(model.getNumID(), channelId, model.getCartId(), false);
        if (platformModel == null) {
            throw new BusinessException("组合套装(numID=" + model.getNumID() + ")在平台上不存在！");
        }
        model.setProductName(platformModel.getProductName()); // 同步商品名称
        model.setPlatformStatus(platformModel.getPlatformStatus()); // 同步平台状态
        Map<String, Double> platformSuitSkuMap = new HashMap<String, Double>();
        platformModel.getSkus().forEach(skuBean -> {
            if (StringUtils.isNotBlank(skuBean.getSuitSkuCode())) {
                platformSuitSkuMap.put(skuBean.getSuitSkuCode(), skuBean.getSuitPreferentialPrice());
            }
        });
        if (suitSkuMap.keySet().size() != platformSuitSkuMap.keySet().size()) {
            throw new BusinessException("组合套装SKU数量和平台真实SKU数量不一致！");
        }
        suitSkuMap.keySet().forEach(suitSkuCode -> {
            if (platformSuitSkuMap.get(suitSkuCode) == null) {
                throw new BusinessException("组合套装SKU(" + suitSkuCode + ")在平台组合商品numId=" + model.getNumID() + "下不存在！");
            }
            if (suitSkuMap.get(suitSkuCode).doubleValue() != platformSuitSkuMap.get(suitSkuCode).doubleValue()) {
                throw new BusinessException("组合套装SKU(" + suitSkuCode + ")优惠售价和平台实际销售价格不一致！");
            }
            // 新增校验，组合套装SKU不能挂在多个组合套装商品
            CmsBtCombinedProductModel target = this.getCombinedProductBySuitSkuCode(suitSkuCode);
            if (target != null && !model.getNumID().equals(target.getNumID())) {
                throw new BusinessException("组合套装SKU(" + suitSkuCode + ")已属于组合套装商品numId=" + target.getNumID());
            }
        });
    }

    /**
     * 根据组合套装SKU查询组合套装商品
     * @param suitSkuCode
     * @return
     */
    private CmsBtCombinedProductModel getCombinedProductBySuitSkuCode (String suitSkuCode) {
        CmsBtCombinedProductModel target = null;
        if (StringUtils.isNotBlank(suitSkuCode)) {
            JongoQuery queryObj = new JongoQuery();
            queryObj.addQuery("{'active':1, 'skus.suitSkuCode':#}");
            queryObj.addParameters(suitSkuCode);
            target = cmsBtCombinedProductDao.selectOneWithQuery(queryObj);
        }
        return target;
    }

    public void onOffShelves(CmsBtCombinedProductModel modelBean, String user){
        if (modelBean == null || StringUtils.isBlank(modelBean.get_id())) {
            throw new BusinessException("请先选择要上/下架的组合套装商品!");
        }
        CmsBtCombinedProductModel targetModel = cmsBtCombinedProductDao.selectById(modelBean.get_id());
        if (targetModel == null) {
            throw new BusinessException("要上/下架的组合套装商品不存在!");
        }
        Integer platformStatus = modelBean.getPlatformStatus();
        if (platformStatus == null || !CmsBtCombinedProductPlatformStatus.KV.containsKey(platformStatus)) {
            throw new BusinessException("请先选择操作(上架/下架)!");
        }
        // 先操作平台状态，根据反馈结果再更新CMS平台状态
        ShopBean shopBean = Shops.getShop(modelBean.getChannelId(), modelBean.getCartId());
        if (shopBean != null) {
            CmsBtCombinedProductModel productBean = new CmsBtCombinedProductModel();
            boolean updRsFlg = false; // 各平台上下架执行结果，true表示执行成功，false表示失败或错误
            String errMsg = "";
            if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.TMALL.getPlatformId().toString())) {
                if (modelBean.getPlatformStatus().intValue() == CmsBtCombinedProductPlatformStatus.OFF_SHELVES) {
                    // 下架
                    ItemUpdateDelistingResponse response = tbSaleService.doWareUpdateDelisting(shopBean, modelBean.getNumID());
                    if (response == null) {
                        errMsg = "调用淘宝商品下架API失败";
                    } else {
                        if (org.apache.commons.lang3.StringUtils.isEmpty(response.getErrorCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getBody();
                        }
                    }
                }else {
                    // 上架
                    ItemUpdateListingResponse response = tbSaleService.doWareUpdateListing(shopBean, modelBean.getNumID());
                    if (response == null) {
                        errMsg = "调用淘宝商品上架API失败";
                    } else {
                        if (org.apache.commons.lang3.StringUtils.isEmpty(response.getErrorCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getBody();
                        }
                    }
                }
            }else if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.JD.getPlatformId().toString())) {
                if (modelBean.getPlatformStatus().intValue() == CmsBtCombinedProductPlatformStatus.OFF_SHELVES) {
                    // 下架
                    WareUpdateDelistingResponse response = jdSaleService.doWareUpdateDelisting(shopBean, modelBean.getNumID());
                    if (response == null) {
                        errMsg = "调用京东商品下架API失败";
                    } else {
                        if ("0".equals(response.getCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getMsg();
                        }
                    }
                } else {
                    // 上架
                    WareUpdateListingResponse response = jdSaleService.doWareUpdateListing(shopBean, modelBean.getNumID());
                    if (response == null) {
                        errMsg = "调用京东商品上架API失败";
                    } else {
                        if ("0".equals(response.getCode())) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getMsg();
                        }
                    }
                }
            } else if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.JM.getPlatformId().toString())) {
                if (modelBean.getPlatformStatus().intValue() == CmsBtCombinedProductPlatformStatus.OFF_SHELVES) {
                    // 下架
                    HtMallStatusUpdateBatchResponse response = jmSaleService.doWareUpdateDelisting(shopBean, modelBean.getNumID());
                    if (response == null) {
                        errMsg = "调用聚美商品下架API失败";
                    } else {
                        if (response.isSuccess()) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getErrorMsg();
                        }
                    }
                } else {
                    // 上架
                    HtMallStatusUpdateBatchResponse response = jmSaleService.doWareUpdateListing(shopBean, modelBean.getNumID());
                    if (response == null) {
                        errMsg = "调用聚美商品上架API失败";
                    } else {
                        if (response.isSuccess()) {
                            updRsFlg = true;
                        } else {
                            errMsg = response.getErrorMsg();
                        }
                    }
                }
            } else {
                throw new BusinessException("平台参数错误！");
            }
            if (!updRsFlg) {
                $error("组合套装商品上下架->API调用返回错误结果，错误信息：" + errMsg);
            }
            // 平台操作OK，更新CMS数据
            targetModel.setPlatformStatus(platformStatus);
            targetModel.setModified(DateTimeUtil.getNow());
            targetModel.setModifier(user);
            WriteResult rs = cmsBtCombinedProductDao.update(targetModel);
            $debug("上下架 组合套装商品 结果 " + rs.toString());

            // 添加操作日志
            CmsBtCombinedProductLogModel logModel = new CmsBtCombinedProductLogModel();
            logModel.setProductId(targetModel.get_id());
            logModel.setNumID(targetModel.getNumID());
            logModel.setChannelId(targetModel.getChannelId());
            logModel.setCartId(targetModel.getCartId());
            logModel.setOperater(user);
            logModel.setOperateTime(targetModel.getModified());
            logModel.setOperateType(targetModel.getPlatformStatus().intValue() == CmsBtCombinedProductPlatformStatus.ON_SHELVES.intValue() ? "上架" : "下架");
            logModel.setOperateContent(targetModel.getPlatformStatus().intValue() == CmsBtCombinedProductPlatformStatus.ON_SHELVES.intValue() ? "上架成功" : "下架成功");
            logModel.setStatus(targetModel.getStatus());
            logModel.setPlatformStatus(targetModel.getPlatformStatus());
            WriteResult rs_log = cmsBtCombinedProductLogDao.insert(logModel);
            $debug("上下架 组合套装商品操作日志 结果 " + rs_log.toString());
        }
    }


    /**
     * 获取组合套装商品SKU信息，供OMS调用
     * @return
     */
    public List<CombinedSkuInfoBean> getSuitSkuInfo() {
        /*String query = String.format("{'active':1}");*/
        JongoQuery query = new JongoQuery();
        query.setQuery("{'active':#, 'status':#}");
        query.addParameters(1);
        query.addParameters(CmsBtCombinedProductStatus.SUBMITTED);
        query.setSort("{'modified':-1, 'created':-1}");
        List<CmsBtCombinedProductModel> productModels = cmsBtCombinedProductDao.select(query);
        List<CombinedSkuInfoBean> suitSkuInfos = new ArrayList<CombinedSkuInfoBean>();
        if (CollectionUtils.isNotEmpty(productModels)) {
            productModels.forEach(product -> {
                product.getSkus().forEach(skuBean -> {
                    skuBean.getSkuItems().forEach(skuItem -> {
                        CombinedSkuInfoBean suitSkuInfo = new CombinedSkuInfoBean();
                        suitSkuInfo.setOrder_channel_id(product.getChannelId());
                        suitSkuInfo.setCart_id(String.valueOf(product.getCartId()));
                        suitSkuInfo.setSku(skuBean.getSuitSkuCode());
                        suitSkuInfo.setReal_sku(skuItem.getSkuCode());
                        suitSkuInfo.setReal_sku_price(String.valueOf(skuItem.getPreferentialPrice()));
                        suitSkuInfo.setReal_sku_name(skuItem.getProductName());
                        suitSkuInfo.setNum_iid(product.getNumID());
                        suitSkuInfos.add(suitSkuInfo);
                    });
                });
            });
        }
        return suitSkuInfos;
    }

    /**
     * 查询组合套装商品操作日志
     * @param searchBean
     * @return
     */
    public Map<String, Object> getOperateLogs (int page, int pageSize, CmsBtCombinedProductBean searchBean) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (searchBean != null && StringUtils.isNotBlank(searchBean.get_id())) {
            JongoQuery query = new JongoQuery();
            query.setQuery("{'productId':#}");
            query.setParameters(searchBean.get_id());
            long total = cmsBtCombinedProductLogDao.countByQuery(query.getQuery(), query.getParameters());
            resultMap.put("total", total);
            query.setSort("{'operateTime':-1}");
            if (page <= 0) {
                page = 1;
            }
            if (pageSize <= 0) {
                pageSize = 10;
            }
            query.setSkip(pageSize * (page - 1)).setLimit(pageSize);

            List<CmsBtCombinedProductLogModel> logs = cmsBtCombinedProductLogDao.select(query);
            resultMap.put("logs", logs);
        }else {
            resultMap.put("total", 0);
        }
        return resultMap;
    }


    /**
     * 天猫组合商品信息
     */
    class TmallItemCombine {
        private String categoryId;
        private String itemId;
        private Double price;
        private Integer quantity;
        private String title;
        private List<TmallItemCombineSku> skuList = new ArrayList<>();
        private List<TmallItemCombinedSubItem> subItemList = new ArrayList<>();

        public TmallItemCombine() {
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<TmallItemCombineSku> getSkuList() {
            return skuList;
        }

        public void setSkuList(List<TmallItemCombineSku> skuList) {
            this.skuList = skuList;
        }

        public List<TmallItemCombinedSubItem> getSubItemList() {
            return subItemList;
        }

        public void setSubItemList(List<TmallItemCombinedSubItem> subItemList) {
            this.subItemList = subItemList;
        }
    }

    class TmallItemCombineSku {
        private String skuId;
        private String skuTitle;
        private String price;
        private List<TmallItemCombineSubSku> subSkuList = new ArrayList<>();

        public TmallItemCombineSku() {
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuTitle() {
            return skuTitle;
        }

        public void setSkuTitle(String skuTitle) {
            this.skuTitle = skuTitle;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<TmallItemCombineSubSku> getSubSkuList() {
            return subSkuList;
        }

        public void setSubSkuList(List<TmallItemCombineSubSku> subSkuList) {
            this.subSkuList = subSkuList;
        }
    }

    class TmallItemCombineSubSku {
        private String subItemId;
        private String outerId;
        private String price;

        public TmallItemCombineSubSku() {
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSubItemId() {
            return subItemId;
        }

        public void setSubItemId(String subItemId) {
            this.subItemId = subItemId;
        }

        public String getOuterId() {
            return outerId;
        }

        public void setOuterId(String outerId) {
            this.outerId = outerId;
        }
    }

    class TmallItemCombinedSubItem {
        private String title;
        private String subItemId;
        private String outerId;
        private String ratio;

        public TmallItemCombinedSubItem() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubItemId() {
            return subItemId;
        }

        public void setSubItemId(String subItemId) {
            this.subItemId = subItemId;
        }

        public String getOuterId() {
            return outerId;
        }

        public void setOuterId(String outerId) {
            this.outerId = outerId;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
    }
}
