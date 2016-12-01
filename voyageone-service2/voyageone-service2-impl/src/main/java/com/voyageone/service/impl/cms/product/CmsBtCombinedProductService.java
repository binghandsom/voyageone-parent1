package com.voyageone.service.impl.cms.product;

import com.jd.open.api.sdk.response.ware.WareUpdateDelistingResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateListingResponse;
import com.mongodb.WriteResult;
import com.taobao.api.ApiException;
import com.taobao.api.response.ItemUpdateDelistingResponse;
import com.taobao.api.response.ItemUpdateListingResponse;
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
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jd.service.JdSaleService;
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
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.enums.PlatformType;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtCombinedProductModel_Sku_Item;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private JumeiSaleService jmSaleService;
    @Autowired
    private CmsBtCombinedProductLogDao cmsBtCombinedProductLogDao;

    public CmsBtCombinedProductModel getCombinedProductPlatformDetail(String numId, String channelId, Integer cartId) {
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        shopBean.setAppKey("21008948");
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
        shopBean.setSessionKey("620230429acceg4103a72932e22e4d53856b145a192140b2854639042");
        shopBean.setShop_name("Target海外旗舰店");
        if (shopBean != null) {
            CmsBtCombinedProductModel productBean = new CmsBtCombinedProductModel();
            productBean.setCartId(cartId);
            productBean.setNumID(numId);
            if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.TMALL.getPlatformId().toString())) {
                try{
                    TmallItemUpdateSchemaGetResponse itemUpdateSchemaGetResponse = tbProductService.doGetWareInfoItem(numId, shopBean);
                    long threadNo = Thread.currentThread().getId();
                    $info("threadNo:" + threadNo + " numiid:" + numId );

                    if (null != itemUpdateSchemaGetResponse && itemUpdateSchemaGetResponse.isSuccess()) {
                        Map<String, Field> fieldMap = SchemaReader.readXmlForMap(itemUpdateSchemaGetResponse.getUpdateItemResult());
                        InputField titleField = (InputField) fieldMap.get("title");
                        productBean.setProductName(titleField.getDefaultValue()); // 组合商品名称
                        MultiComplexField skuField = (MultiComplexField) fieldMap.get("sku");
                        // TODO
                        SingleCheckField itemStatus = (SingleCheckField) fieldMap.get("item_status");
                        Integer platformStatus = "0".equalsIgnoreCase(itemStatus.getDefaultValue())? 0 : 1;
                        if (null == skuField || null == skuField.getDefaultComplexValues() || skuField.getDefaultComplexValues().size() == 0) {
                            InputField outerIdField = (InputField) fieldMap.get("outer_id");
                            InputField priceField = (InputField) fieldMap.get("price");
                            String outerId = outerIdField.getDefaultValue();
                            String price = priceField.getDefaultValue();
                            CmsBtCombinedProductModel_Sku skuBean = new CmsBtCombinedProductModel_Sku();
                            skuBean.setSuitSkuCode(outerId);
                            skuBean.setSuitPreferentialPrice(Double.valueOf(price));
                            skuBean.getSkuItems().add(new CmsBtCombinedProductModel_Sku_Item()); // 在页面初始化一个空的SKU ITEM
                            productBean.getSkus().add(skuBean);
                        } else {
                            skuField.getDefaultComplexValues().forEach(skuValue -> {
                                CmsBtCombinedProductModel_Sku skuBean = new CmsBtCombinedProductModel_Sku();
                                skuBean.setSuitSkuCode(skuValue.getInputFieldValue("sku_outerId"));
                                skuBean.setSuitPreferentialPrice(Double.parseDouble(skuValue.getInputFieldValue("sku_price")));
                                skuBean.getSkuItems().add(new CmsBtCombinedProductModel_Sku_Item()); // 在页面初始化一个空的SKU ITEM
                                productBean.getSkus().add(skuBean);
                            });
                        }
                        return productBean;
                    }else{
                        $info("threadNo:" + threadNo + " numiid:" + numId +" 取得异常");
                        throw new BusinessException("获取天猫商品数据出错了！");
                    }
                }catch (ApiException | TopSchemaException e) {
                    e.printStackTrace();
                    throw new BusinessException("获取天猫商品数据出错了！");
                }
            } else if (shopBean.getPlatform_id().equalsIgnoreCase(PlatformType.JD.getPlatformId().toString())) {
                // nothing.......
            }

        }
        return null;
    }

    public CmsBtCombinedProductModel_Sku_Item getSkuDetail (String skuCode, String channelId, Integer cartId) {
        CmsBtCombinedProductModel_Sku_Item skuItem = null;
        CmsBtProductModel product = productService.getProductBySku(channelId, skuCode);
        CmsBtProductModel_Platform_Cart cart = null;
        List<BaseMongoMap<String, Object>> skus = null;
        if (product != null && (cart = product.getPlatform(cartId)) != null &&
                CollectionUtils.isNotEmpty(skus = cart.getSkus())) {
            for (BaseMongoMap<String, Object> sku:skus) {
                if (skuCode.equals(sku.getStringAttribute("skuCode"))) {
                    skuItem = new CmsBtCombinedProductModel_Sku_Item();
                    skuItem.setProductName(product.getCommonNotNull().getFieldsNotNull().getOriginalTitleCn());
                    skuItem.setSkuCode(skuCode);
                    skuItem.setSellingPriceCn(sku.getDoubleAttribute("priceSale"));
                    break;
                }
            }
        }
        if (skuItem == null) {
            throw new BusinessException("查询不到SKU信息！");
        }
        return skuItem;
    }

    public void addCombinedProduct (CmsBtCombinedProductModel product, String channelId, String user) {
        // 校验输入值是否合法
        checkCombinedProducrtModel(product, channelId, ACTION_TYPE_ADD);
        product.setCreater(user);
        product.setCreated(DateTimeUtil.getNow());
        if (product.getStatus() == null || !CmsBtCombinedProductStatus.KV.containsKey(product.getStatus())) {
            product.setStatus(CmsBtCombinedProductStatus.TEMPORAL); // 默认暂存状态
        }
        product.setPlatformStatus(CmsBtCombinedProductPlatformStatus.OFF_SHELVES);
        product.setChannelId(channelId);
        WriteResult rs = cmsBtCombinedProductDao.insert(product);
        $debug("新增 组合套装商品 结果 " + rs.toString());

        // 添加操作日志
        CmsBtCombinedProductLogModel logModel = new CmsBtCombinedProductLogModel();
        logModel.setNumId(product.getNumID());
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
        if (pageSize < 0) {
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
        if (modelBean == null || StringUtils.isBlank(modelBean.getNumID())) {
            throw new BusinessException("请先选择要删除的组合套装商品！");
        }
        String query = String.format("{'numID':'%s', 'channelId':'%s'}", modelBean.getNumID(), channelId);
        CmsBtCombinedProductModel target = cmsBtCombinedProductDao.selectOneWithQuery(query);
        if (target == null) {
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
        /*JongoUpdate updateObj = new JongoUpdate();
        updateObj.setQuery("{'numID':#, 'channelId':#}");
        updateObj.setQueryParameters(modelBean.getNumID(), channelId);
        updateObj.setUpdate("{$set:{'active':0, 'modifier':#, 'modified':#}}");
        updateObj.setUpdateParameters(user, DateTimeUtil.getNow());
        WriteResult rs = cmsBtCombinedProductDao.updateFirst(updateObj.getQuery(), updateObj.getUpdate());*/

        // 添加操作日志
        CmsBtCombinedProductLogModel logModel = new CmsBtCombinedProductLogModel();
        logModel.setNumId(modelBean.getNumID());
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
        if (model == null || StringUtils.isBlank(model.getNumID()) || StringUtils.isBlank(channelId)) {
            throw new BusinessException("参数错误！");
        }
        String query = String.format("{'active':1, 'numID':'%s', 'channelId':'%s'}", model.getNumID(), channelId);
        CmsBtCombinedProductModel target = cmsBtCombinedProductDao.selectOneWithQuery(query);
        /*if (target == null) {
            throw new BusinessException("查询不到要编辑的组合套装商品！");
        }*/
        return target;
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
        logModel.setNumId(model.getNumID());
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
        if (model == null || StringUtils.isBlank(model.getNumID()) || model.getCartId() == null) {
            throw new BusinessException("参数错误！");
        }
        List<CmsBtCombinedProductModel_Sku> skus = model.getSkus();
        if (CollectionUtils.isEmpty(skus)) {
            throw new BusinessException("组合套装SKU为空！");
        }

        CmsBtCombinedProductModel targetModel = null;
        if (ACTION_TYPE_EDIT.equals(actionType)) {
            String _id = model.get_id();
            if (StringUtils.isBlank(_id) || (targetModel = cmsBtCombinedProductDao.selectById(_id)) == null) {
                $error("要编辑的组合套装(id=" + _id + ")不存在！");
                throw new BusinessException("要编辑的组合套装商品不存在！");
            }
            if (!targetModel.getNumID().equals(model.getNumID())) {
                // 如果numID发生了变化，则需要校验新的numID是否已被占用
                CmsBtCombinedProductModel existModel = this.getCombinedProduct(model, channelId);
                if (existModel != null) {
                    throw new BusinessException("组合套装商品(numID=" + model.getNumID() + ")已经存在了！");
                }
            }
        }else if (ACTION_TYPE_ADD.equals(actionType)) {
            CmsBtCombinedProductModel existModel = this.getCombinedProduct(model, channelId);
            if (existModel != null) {
                throw new BusinessException("组合套装商品(numID=" + model.getNumID() + ")已经存在了！");
            }
        }else {
            throw new BusinessException("不明操作！");
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

        CmsBtCombinedProductModel platformModel = this.getCombinedProductPlatformDetail(model.getNumID(), channelId, model.getCartId());
        if (platformModel == null) {
            throw new BusinessException("组合套装(numID=" + model.getNumID() + ")在平台上不存在！");
        }
        model.setProductName(platformModel.getProductName());
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
            if (platformSuitSkuMap.get(suitSkuCode) == null || suitSkuMap.get(suitSkuCode).doubleValue() != platformSuitSkuMap.get(suitSkuCode).doubleValue()) {
                throw new BusinessException("组合套装SKU(" + suitSkuCode + ")优惠售价和平台实际销售价格不一致！");
            }
        });
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
            logModel.setNumId(targetModel.getNumID());
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
        query.setQuery("{'active':#}");
        query.setParameters(1);
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
                        suitSkuInfo.setReal_sku_name(product.getProductName());
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
     * @param modelBean
     * @return
     */
    public List<CmsBtCombinedProductLogModel> getOperateLogs (CmsBtCombinedProductBean modelBean) {
        if (modelBean != null && StringUtils.isNotBlank(modelBean.getNumID()) && StringUtils.isNotBlank(modelBean.getChannelId()) && modelBean.getCartId() != null) {
            JongoQuery query = new JongoQuery();
            query.setQuery("{'numID':#, 'channelId':#, 'cartId':#}");
            query.setParameters(modelBean.getNumID(), modelBean.getChannelId(), modelBean.getCartId());
            query.setSort("{'operateTime':-1}");
            return cmsBtCombinedProductLogDao.select(query);
        }
        return null;
    }
}
