package com.voyageone.service.impl.cms.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.wms.ClientInventoryService;
import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedCategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static com.voyageone.common.CmsConstants.FeedProductUpdateType;

/**
 * feed数据导入CMS中 给各个店铺feed解析程序调用插入mongoDB的接口
 *
 * @author james.li, 2015/11/26.
 * @author Jonas, 2015-12-12.
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class FeedToCmsService extends BaseService {

    @Autowired
    private FeedCategoryTreeService feedCategoryTreeService;

    @Autowired
    private FeedInfoLogService feedInfoLogService;

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private CmsMtChannelValuesService cmsMtChannelValuesService;

    @Autowired
    private FeedCategoryAttributeService feedCategoryAttributeService;

    @Autowired
    private ClientInventoryService clientInventoryService;

    @Autowired
    private CmsBtBrandBlockService cmsBtBrandBlockService;

//    public static final String URL_FORMAT = "[~@.' '#$%&*_''/‘’^\\()]";
//    private final Pattern special_symbol = Pattern.compile(URL_FORMAT);


    /**
     * 获取该channel下所有的叶子类目
     *
     * @param channelId 渠道ID
     * @return 所有的叶子类目
     */
    public List<CmsMtFeedCategoryModel> getFinallyCategories(String channelId) {

//        CmsMtFeedCategoryTreeModel category = cmsMtFeedCategoryTreeDao.selectFeedCategory(channelId);
//
//        return JsonPath.parse(category.getCategoryTree()).read("$..child[?(@.isChild == 1)]", new TypeRef<List<CmsMtFeedCategoryModel>>() {
//        });
        return null;
    }

    public boolean chkCategoryPathValid(String categoryPath) {
        if (categoryPath.length() == categoryPath.lastIndexOf("-") + 1) {
            return false;
        }
        String category[] = categoryPath.split("-");
        return !StringUtil.isEmpty(category[category.length - 1]);
    }

    /**
     * 更新code信息如果不code不存在会新建
     *
     * @param products 产品列表
     */
    @VOTransactional
    public Map<String, List<CmsBtFeedInfoModel>> updateProduct(String channelId, List<CmsBtFeedInfoModel> products, String modifier) {
        return updateProduct(channelId, products, modifier, FeedProductUpdateType.CMS_FEED);
    }

    /**
     * 更新code信息如果不code不存在会新建
     *
     * @param products 产品列表
     */
    @VOTransactional
    public Map<String, List<CmsBtFeedInfoModel>> updateProduct(String channelId, List<CmsBtFeedInfoModel> products,
                                                               String modifier, int updateType) {
        List<String> existCategory = new ArrayList<>();
        List<CmsBtFeedInfoModel> failProduct = new ArrayList<>();
        List<CmsBtFeedInfoModel> succeedProduct = new ArrayList<>();

        //0:brand 1:sizeType 2:productType
        Set<String> brandList = new HashSet<>();
        Set<String> sizeTypeList = new HashSet<>();
        Set<String> productTypeList = new HashSet<>();
        Boolean isUsJoi = isUsJoi(channelId);
        Map<String, Map<String, List<String>>> attributeMtDatas = new HashMap<>();
        for (CmsBtFeedInfoModel product : products) {
            boolean insertLog = false;
            try {

                product.setModified(DateTimeUtil.getNow());
                product.setModifier(modifier);
                product.setUpdFlg(0);

                String category = product.getCategory();
                if (!chkCategoryPathValid(category)) {
                    throw new BusinessException("category 不合法：" + category);
                }

                // 判断是否追加一个新的类目
                if (!existCategory.contains(category)) {
                    feedCategoryTreeService.addCategory(channelId, category, modifier);
                    existCategory.add(category);
                }

                CmsBtFeedInfoModel befproduct = feedInfoService.getProductByCode(channelId, product.getCode());
                if (befproduct != null) {
                    product.set_id(befproduct.get_id());
                    // Vms客户导入的情况下，
                    if (FeedProductUpdateType.VMS_FEED == updateType) {
                        VmsChannelConfigBean vmsUpdateInventory = VmsChannelConfigs.getConfigBean(channelId,"UPDATE_INVENTORY", "0");
                        if (vmsUpdateInventory == null || "1".equals(vmsUpdateInventory.getConfigValue1())) {
                            // 库存同步
                            for (CmsBtFeedInfoModel_Sku skuModelNew : product.getSkus()) {
                                clientInventoryService.insertClientInventory(channelId, skuModelNew.getClientSku(), skuModelNew.getQty());
                            }
                        }
                        // sku以新的为准（老的舍弃）
                        if (product.getSkus().size() != befproduct.getSkus().size()) {
                            insertLog = true;
                        }
                    }
                    if (!insertLog) {
                        //把之前的sku（新的product中没有的sku）保存到新的product的sku中
                        for (CmsBtFeedInfoModel_Sku skuModel : befproduct.getSkus()) {
                            if (!product.getSkus().contains(skuModel)) {
                                if (FeedProductUpdateType.CMS_FEED == updateType) {
                                    // Vms系统以新的sku为准
                                    product.getSkus().add(skuModel);
                                }
                                insertLog = true;
                            } else {
                                // 改条数据已经需要跟新主数据了 后面价格也不需要比了
                                if (!insertLog) {
                                    CmsBtFeedInfoModel_Sku item = product.getSkus().get(product.getSkus().indexOf(skuModel));
                                    if (item.getPriceClientMsrp().compareTo(skuModel.getPriceClientMsrp()) != 0
                                            || item.getPriceClientRetail().compareTo(skuModel.getPriceClientRetail()) != 0
                                            || item.getPriceMsrp().compareTo(skuModel.getPriceMsrp()) != 0
                                            || item.getPriceNet().compareTo(skuModel.getPriceNet()) != 0
                                            || item.getPriceCurrent().compareTo(skuModel.getPriceCurrent()) != 0) {
                                        insertLog = true;
                                    }
                                }
                                // 重量变化的情况下，重新导入
                                if (!insertLog) {
                                    CmsBtFeedInfoModel_Sku item = product.getSkus().get(product.getSkus().indexOf(skuModel));
                                    String newWeight = item.getWeightOrg();
                                    String oldWeight = skuModel.getWeightOrg();
                                    if (StringUtils.isEmpty(newWeight) && !StringUtils.isEmpty(oldWeight)) {
                                        insertLog = true;
                                    } else if (!StringUtils.isEmpty(newWeight) && !newWeight.equals(oldWeight)) {
                                        insertLog = true;
                                    }
                                }
                            }
                        }
                    }
                    product.setCreated(befproduct.getCreated());
                    product.setCreater(befproduct.getCreater());
                    // Vms的场合不MergeAttribute
                    if (FeedProductUpdateType.CMS_FEED == updateType) {
                        product.setAttribute(attributeMerge(product.getAttribute(), befproduct.getAttribute()));
                    }
                    //feed增加状态属性(New(9), Waiting For Import(0),Finish Import(1),Error(2), Not Import(3))，9,3 ,0->不变, 2, 1->0
                    if (befproduct.getUpdFlg() == 2 || befproduct.getUpdFlg() == 1 || befproduct.getUpdFlg() == 0) {
                        if (insertLog) {
                            product.setUpdFlg(0);
                        }
                    } else {
                        product.setUpdFlg(9);
                    }
                } else {
                    //如果是新的产品,如config已配置直接导入
                    //flag 1导入
                    CmsChannelConfigBean isImportFeedTypeConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SET_FEED_IMPORT_FLG);
                    if(isImportFeedTypeConfig != null &&"1".equals(isImportFeedTypeConfig.getConfigValue1())){
                        insertLog = true;
                        product.setCreater(modifier);
                        product.setCreated(DateTimeUtil.getNow());
                        product.setModifier(modifier);
                        product.setModified(DateTimeUtil.getNowTimeStamp());
                        product.setUpdFlg(0);
                    }else{
                        insertLog = true;
                        product.setCreater(modifier);
                        product.setCreated(DateTimeUtil.getNow());
                        product.setModifier(modifier);
                        product.setModified(DateTimeUtil.getNowTimeStamp());
                        product.setUpdFlg(9);
                    }
                }
                // code 库存计算
                Integer qty = 0;
                for (CmsBtFeedInfoModel_Sku sku : product.getSkus()) {
                    if (sku.getQty() != null) qty += sku.getQty();
                    weightConvert(sku);
                    if (isUsJoi) priceConvert(sku);
                }
                product.setQty(qty);

                product.setCatId(MD5.getMD5(product.getCategory()));
                // 产品数据合法性检查
                checkProduct(product);

                feedInfoService.updateFeedInfo(product);

                brandList.add(product.getBrand());
                sizeTypeList.add(product.getSizeType());
                productTypeList.add(product.getProductType());

                if (insertLog) {
                    // 写log表
                    product.set_id(null);
                    feedInfoLogService.insertCmsBtFeedInfoLog(product);
                }

                Map<String, List<String>> attributeMtData;
                if (attributeMtDatas.get(category) == null) {
                    attributeMtData = new HashMap<>();
                    attributeMtDatas.put(category, attributeMtData);
                } else {
                    attributeMtData = attributeMtDatas.get(category);
                }
                attributeMtDataMake(attributeMtData, product);
                succeedProduct.add(product);
            } catch (Exception e) {
                if (FeedProductUpdateType.VMS_FEED == updateType || FeedProductUpdateType.VMS_PRICE_INVENTORY == updateType) {
                    throw e;
                }
                e.printStackTrace();
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                $error(e.getMessage(), e);
                failProduct.add(product);
            }
        }

        if (FeedProductUpdateType.VMS_PRICE_INVENTORY != updateType) {
            // 更新类目中属性
            for (Map.Entry<String, Map<String, List<String>>> entry : attributeMtDatas.entrySet()) {
                updateFeedCategoryAttribute(channelId, entry.getValue(), entry.getKey());
            }

            //0:brand 1:sizeType 2:productType
            insertCmsMtChannelValues(channelId, brandList, 0, modifier);
            insertCmsMtChannelValues(channelId, sizeTypeList, 1, modifier);
            insertCmsMtChannelValues(channelId, productTypeList, 2, modifier);
        }

        Map<String, List<CmsBtFeedInfoModel>> response = new HashMap<>();
        response.put("succeed", succeedProduct);
        response.put("fail", failProduct);
        return response;
    }

    public Boolean checkProduct(CmsBtFeedInfoModel product) {
        if (product.getImage() == null || product.getImage().size() == 0) {
            product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
            product.setUpdMessage("没有图片");
            $debug(product.getCode()+"----" +product.getUpdMessage());
            return false;
        } else if (product.getImage().stream().filter(str -> !StringUtil.isEmpty(str.trim())).collect(Collectors.toList()).size() == 0) {
            product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
            product.setUpdMessage("没有图片");
            $debug(product.getCode()+"----" +product.getUpdMessage());
            return false;
        }

        if (product.getBrand() == null || StringUtil.isEmpty(product.getBrand().trim())) {
            product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
            product.setUpdMessage("没有品牌");
            $debug(product.getCode()+"----" +product.getUpdMessage());
            return false;
        }
        //String channelId, int cartId, String feedBrand, String masterBrand, String platformBrandId
        if(cmsBtBrandBlockService.isBlocked(product.getChannelId(), CmsBtBrandBlockService.BRAND_TYPE_FEED,product.getBrand(),null,null)){
            product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedBlackList);
            product.setUpdMessage("已经加入黑名单商品");
            $debug(product.getCode()+"----" +product.getUpdMessage());
            return false;
        }

        for (CmsBtFeedInfoModel_Sku sku : product.getSkus()) {
            if (StringUtil.isEmpty(sku.getBarcode())) {
                product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
                product.setUpdMessage("没有UPC");
                $debug(product.getCode() + "----" + product.getUpdMessage());
                return false;
            }

            if (sku.getPriceNet() == null || sku.getPriceNet().compareTo(0D) == 0) {
                product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
                product.setUpdMessage("成本价为0");
                $debug(product.getCode() + "----" + product.getUpdMessage());
                return false;
            }
        }
        return true;
    }

    private Map<String, List<String>> attributeMerge(Map<String, List<String>> attribute1, Map<String, List<String>> attribute2) {

        for (Map.Entry<String, List<String>> entry1 : attribute1.entrySet()) {
            String key = entry1.getKey();
            List<String> value = entry1.getValue();
            if (attribute2.containsKey(key)) {
                attribute2.put(key, Stream.concat(value.stream(), attribute2.get(key).stream())
                        .map(String::trim)
                        .distinct()
                        .collect(toList()));
            } else {
                attribute2.put(key, value);
            }
        }
        return attribute2;
    }

    /**
     * 把一个code下的属性抽出存放到类目中
     *
     * @param attributeMtData 属性
     * @param product         产品
     */
    private void attributeMtDataMake(Map<String, List<String>> attributeMtData, CmsBtFeedInfoModel product) {
        Map<String, List<String>> map = product.getAttribute();

        if (map == null) return;

        for (Map.Entry<String, List<String>> entry1 : map.entrySet()) {
            String key = entry1.getKey();
            if (attributeMtData.containsKey(key)) {
                List<String> value = attributeMtData.get(key);
                value.addAll(entry1.getValue());
                attributeMtData.put(key, value.stream().distinct().collect(toList()));
            } else {
                List<String> value = new ArrayList<>();
                value.addAll(entry1.getValue());
                attributeMtData.put(key, value);
            }
        }
    }

    /**
     * 属性的基本数据保存到类目中
     *
     * @param channelId 渠道
     * @param attribute 属性
     * @param category  类目
     */
    private void updateFeedCategoryAttribute(String channelId, Map<String, List<String>> attribute, String category) {

        String catId = MD5.getMD5(category);
        CmsMtFeedAttributesModel cmsBtFeedCategoryAttribute = feedCategoryAttributeService.getCategoryAttributeByCatId(channelId, catId);
        if (cmsBtFeedCategoryAttribute == null) {
            cmsBtFeedCategoryAttribute = new CmsMtFeedAttributesModel();
            cmsBtFeedCategoryAttribute.setChannelId(channelId);
            cmsBtFeedCategoryAttribute.setCatId(catId);
            cmsBtFeedCategoryAttribute.setCatPath(category);
            cmsBtFeedCategoryAttribute.setAttribute(new HashMap<>());
        }


        Map<String, List<String>> oldAtt = cmsBtFeedCategoryAttribute.getAttribute();

        for (Map.Entry<String, List<String>> entry1 : attribute.entrySet()) {
            String key = entry1.getKey();
            if (oldAtt.containsKey(key)) {
                oldAtt.put(key, Stream.concat(entry1.getValue().stream(), oldAtt.get(key).stream())
                        .map(String::trim)
                        .distinct()
                        .collect(toList()));
            } else {
                oldAtt.put(key, entry1.getValue());
            }
        }
        feedCategoryAttributeService.updateAttributes(cmsBtFeedCategoryAttribute);
    }

    private void insertCmsMtChannelValues(String channelId, Set<String> values, int type, String modifier) {
        CmsMtChannelValuesModel cmsMtChannelValuesModel = new CmsMtChannelValuesModel();
        cmsMtChannelValuesModel.setChannelId(channelId);
        cmsMtChannelValuesModel.setType(type);
        cmsMtChannelValuesModel.setModifier(modifier);
        cmsMtChannelValuesModel.setCreater(modifier);
//        cmsMtChannelValuesModel.setCreatedStr(DateTimeUtil.getNow());
        cmsMtChannelValuesModel.setCreated(new Date());
        values.forEach(s -> {
            if (!StringUtil.isEmpty(s)) {
                cmsMtChannelValuesModel.setKey(s);
                cmsMtChannelValuesModel.setValue(s);
                cmsMtChannelValuesService.insertCmsMtChannelValues(cmsMtChannelValuesModel);
            }
        });

    }

    private void weightConvert(CmsBtFeedInfoModel_Sku skuModel) {
        try {
            if (!StringUtil.isEmpty(skuModel.getWeightOrg()) && !StringUtil.isEmpty(skuModel.getWeightOrgUnit())) {
                String unit = skuModel.getWeightOrgUnit().trim();
                String weightOrg = skuModel.getWeightOrg().trim();
                if (unit.toLowerCase().indexOf("oz") > -1) {
                    Double convertWeight = round(Double.parseDouble(weightOrg) / 16.0);
                    skuModel.setWeightCalc(convertWeight.toString());
                } else if (unit.toLowerCase().indexOf("lb") > -1) {
                    Double convertWeight = round(Double.parseDouble(weightOrg));
                    skuModel.setWeightCalc(convertWeight.toString());
                } else if (unit.toLowerCase().equals("g")) {
                    Double convertWeight = round(Double.parseDouble(weightOrg) / 453.59237);
                    skuModel.setWeightCalc(convertWeight.toString());
                } else if (unit.toLowerCase().equals("kg")) {
                    Double convertWeight = round(Double.parseDouble(weightOrg) / 0.4535924);
                    skuModel.setWeightCalc(convertWeight.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double round(Double value){
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    private void priceConvert(CmsBtFeedInfoModel_Sku skuModel) {
        Integer weightCalc = StringUtil.isEmpty(skuModel.getWeightCalc()) ? 4 : Integer.parseInt(skuModel.getWeightCalc());
        Double current = (skuModel.getPriceNet() + weightCalc * 3.5) * 6.7 / (1 - 0.1 - 0.05 - 0.119 - 0.05);
        skuModel.setPriceCurrent(Math.ceil(current));
        Double msrp = (skuModel.getPriceClientMsrp() + weightCalc * 3.5) * 6.7 / (1 - 0.1 - 0.05 - 0.119 - 0.05);
        skuModel.setPriceMsrp(Math.ceil(msrp));
    }

    private Boolean isUsJoi(String channelId) {
        return "1".equalsIgnoreCase(Feeds.getVal1(channelId, FeedEnums.Name.is_usjoi));
    }
}
