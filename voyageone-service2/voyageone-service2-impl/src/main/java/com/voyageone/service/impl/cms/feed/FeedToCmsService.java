package com.voyageone.service.impl.cms.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.category.match.*;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.CmsBtBrandBlockService;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedSkuPqModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

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
    private FeedInfoService feedInfoService;

    @Autowired
    private CmsMtChannelValuesService cmsMtChannelValuesService;

    @Autowired
    private FeedCategoryAttributeService feedCategoryAttributeService;

    @Autowired
    private CmsBtBrandBlockService cmsBtBrandBlockService;

    @Autowired
    private Searcher searcher;

    @Autowired
    private PriceService priceService;


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
    public Map<String, List<CmsBtFeedInfoModel>> updateProduct(String channelId, List<CmsBtFeedInfoModel> products,
                                                               String modifier) {


        Boolean isSetCategory = false;
        List<String> existCategory = new ArrayList<>();
        List<CmsBtFeedInfoModel> failProduct = new ArrayList<>();
        List<CmsBtFeedInfoModel> succeedProduct = new ArrayList<>();

        //0:brand 1:sizeType 2:productType
        Set<String> brandList = new HashSet<>();
        Set<String> sizeTypeList = new HashSet<>();
        Set<String> productTypeList = new HashSet<>();
        Boolean isUsJoi = isUsJoi(channelId);
        Map<String, Map<String, List<String>>> attributeMtDatas = new HashMap<>();
        // 读入是否计算主类目
        CmsChannelConfigBean cmsChannelConfigs = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.FEED_IS_SET_MAIN_CATEGORY);
        if (cmsChannelConfigs != null) {
            isSetCategory = "1".equalsIgnoreCase(cmsChannelConfigs.getConfigValue1());
        }
        for (CmsBtFeedInfoModel product : products) {
            product.setCode(product.getCode().replaceAll("\"", "-"));
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
                    //把之前的sku（新的product中没有的sku）保存到新的product的sku中
                    for (CmsBtFeedInfoModel_Sku skuModel : befproduct.getSkus()) {
                        if (!product.getSkus().contains(skuModel)) {
                            // Vms系统以新的sku为准
                            product.getSkus().add(skuModel);
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
                    product.setCreated(befproduct.getCreated());
                    product.setCreater(befproduct.getCreater());
//                    product.setAttribute(attributeMerge(product.getAttribute(), befproduct.getAttribute()));
                    //feed增加状态属性(New(9), Waiting For Import(0),Finish Import(1),Error(2), Not Import(3))，9,3 ,0->不变, 2, 1->0
                    if (befproduct.getUpdFlg() == 2 || befproduct.getUpdFlg() == 1 || befproduct.getUpdFlg() == 0) {
                        if (insertLog || compare(befproduct, product)) {
                            product.setUpdFlg(0);
                        }
                    } else {
                        product.setUpdFlg(9);
                    }
                } else {
                    //如果是新的产品,如config已配置直接导入
                    //flag 1导入
                    CmsChannelConfigBean isImportFeedTypeConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SET_FEED_IMPORT_FLG);
                    if (isImportFeedTypeConfig != null && "1".equals(isImportFeedTypeConfig.getConfigValue1())) {
                        product.setCreater(modifier);
                        product.setCreated(DateTimeUtil.getNow());
                        product.setModifier(modifier);
                        product.setModified(DateTimeUtil.getNowTimeStamp());
                        product.setUpdFlg(0);
                    } else {
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
//                    if (isUsJoi) priceConvert(sku);
                }
                product.setQty(qty);

                product.setCatId(MD5.getMD5(product.getCategory()));

                //计算主类目
                if (isSetCategory && StringUtil.isEmpty(product.getMainCategoryEn())) {
                    setMainCategory(product);
                }

                // 产品数据合法性检查
                checkProduct(product);

                // 计算人民币价格
                try {
                    priceService.setFeedPrice(product);
                } catch (Exception e) {
                    $error(e);
                }

                feedInfoService.updateFeedInfo(product);

                brandList.add(product.getBrand());
                sizeTypeList.add(product.getSizeType());
                productTypeList.add(product.getProductType());

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
                e.printStackTrace();
                issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
                $error(e.getMessage(), e);
                product.setUpdMessage(e.getMessage());
                failProduct.add(product);
            }
        }

        // 更新类目中属性
        for (Map.Entry<String, Map<String, List<String>>> entry : attributeMtDatas.entrySet()) {
            try {
                updateFeedCategoryAttribute(channelId, entry.getValue(), entry.getKey());
            } catch (Exception e) {

            }
        }

        //0:brand 1:sizeType 2:productType
        insertCmsMtChannelValues(channelId, brandList, 0, modifier);
        insertCmsMtChannelValues(channelId, sizeTypeList, 1, modifier);
        insertCmsMtChannelValues(channelId, productTypeList, 2, modifier);

        Map<String, List<CmsBtFeedInfoModel>> response = new HashMap<>();
        response.put("succeed", succeedProduct);
        response.put("fail", failProduct);
        return response;
    }

    /**
     * VMS的sku的库存和价格改变时，更新cms系统中的信息
     * @param channelId
     * @param products
     * @param modifier
     * @return
     */
    public Map<String, List<CmsBtOperationLogModel_Msg>> updateFeedSkuPrice(String channelId, List<CmsBtFeedSkuPqModel> products,
                                                                            String modifier) {

        List<CmsBtOperationLogModel_Msg> success = new ArrayList<>(),
                failed = new ArrayList<>();

        for (CmsBtFeedSkuPqModel skuEntity : products) {

            CmsBtFeedInfoModel orgFeedInfo = feedInfoService.getProductByCode(channelId, skuEntity.getCode());
            /**标识是否要触发价格公式，
             * 当判断中的3个价格都没有值时，不会触发价格公式
             * */
            boolean triggerPrice = false;

            for (CmsBtFeedInfoModel_Sku skuInfo : skuEntity.getSkus()) {
                CmsBtFeedInfoModel_Sku targetSku = filterSku(orgFeedInfo.getSkus(), skuInfo);

                /**
                 * 比较一下客户价格
                 * priceNet:美金成本价
                 * priceClientRetail:美金指导价
                 * priceClientMsrp:美金专柜价
                 */
                triggerPrice = skuInfo.getPriceNet() == 0 && skuInfo.getPriceClientRetail() == 0 && skuInfo.getPriceClientMsrp() == 0 ? false : true;

                if (skuInfo.getPriceNet() != 0)
                    targetSku.setPriceNet(skuInfo.getPriceNet());
                if (skuInfo.getPriceClientRetail() != 0)
                    targetSku.setPriceClientRetail(skuInfo.getPriceClientRetail());
                if (skuInfo.getPriceClientMsrp() != 0)
                    targetSku.setPriceClientMsrp(skuInfo.getPriceClientMsrp());
                if (skuInfo.getQty() != 0)
                    targetSku.setQty(skuInfo.getQty());
            }

            try {
                orgFeedInfo.setCreated(DateTimeUtil.getNowTimeStamp());
                orgFeedInfo.setCreater(modifier);

                /**code 库存计算*/
                Integer qty = 0;
                for (CmsBtFeedInfoModel_Sku sku : orgFeedInfo.getSkus()) {
                    if (sku.getQty() != null)
                        qty += sku.getQty();
                }
                orgFeedInfo.setQty(qty);


                // 计算人民币价格
                if (triggerPrice) {
                    try {
                        priceService.setFeedPrice(orgFeedInfo);
                    } catch (Exception e) {
                        $error("触发价格公式报错：\n" + e.getMessage());
                    }
                }

                feedInfoService.updateFeedInfo(orgFeedInfo);

                CmsBtOperationLogModel_Msg _successMsg = new CmsBtOperationLogModel_Msg();
                _successMsg.setSkuCode(orgFeedInfo.getCode());
                _successMsg.setMsg("修改feed信息完毕");
                success.add(_successMsg);

            } catch (Exception e) {
                e.printStackTrace();
                CmsBtOperationLogModel_Msg _failedMsg = new CmsBtOperationLogModel_Msg();
                _failedMsg.setSkuCode(orgFeedInfo.getCode());
                _failedMsg.setMsg(e.getMessage());
                failed.add(_failedMsg);
            }
        }

        Map<String, List<CmsBtOperationLogModel_Msg>> response = new HashMap<>();
        response.put("success", success);
        response.put("failed", failed);
        return response;

    }

    /**
     * 找出符合条件的sku
     *
     * @param skus     feed当中的sku
     * @param matchSku 要匹配的sku模型
     */
    private CmsBtFeedInfoModel_Sku filterSku(List<CmsBtFeedInfoModel_Sku> skus, CmsBtFeedInfoModel_Sku matchSku) {

        CmsBtFeedInfoModel_Sku targetSku = null;

        for (CmsBtFeedInfoModel_Sku entity : skus) {
            if (entity.getSku().equals(matchSku.getSku())) {
                targetSku = entity;
                break;
            }
        }

        return targetSku;
    }

    public Boolean checkProduct(CmsBtFeedInfoModel product) {
        if (!product.getChannelId().equalsIgnoreCase(ChannelConfigEnums.Channel.CHAMPION.getId())) {
            if (product.getImage() == null || product.getImage().size() == 0) {
                product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
                product.setUpdMessage("没有图片");
                for (CmsBtFeedInfoModel_Sku sku : product.getSkus()) {
                    sku.setErrInfo("没有图片");
                }
                $info(product.getCode() + "----" + product.getUpdMessage());
                return false;
            } else if (product.getImage().stream().filter(str -> !StringUtil.isEmpty(str.trim())).collect(Collectors.toList()).size() == 0) {
                product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
                product.setUpdMessage("没有图片");
                for (CmsBtFeedInfoModel_Sku sku : product.getSkus()) {
                    sku.setErrInfo("没有图片");
                }
                $info(product.getCode() + "----" + product.getUpdMessage());
                return false;
            }
        }
        if (product.getBrand() == null || StringUtil.isEmpty(product.getBrand().trim())) {
            product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
            product.setUpdMessage("没有品牌");
            for (CmsBtFeedInfoModel_Sku sku : product.getSkus()) {
                sku.setErrInfo("没有品牌");
            }
            $info(product.getCode() + "----" + product.getUpdMessage());
            return false;
        }
        //String channelId, int cartId, String feedBrand, String masterBrand, String platformBrandId
        if (cmsBtBrandBlockService.isBlocked(product.getChannelId(), CmsBtBrandBlockService.BRAND_TYPE_FEED, product.getBrand(), null, null)) {
            product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedBlackList);
            product.setUpdMessage("已经加入黑名单商品");
            for (CmsBtFeedInfoModel_Sku sku : product.getSkus()) {
                sku.setErrInfo("已经加入黑名单商品");
            }
            $info(product.getCode() + "----" + product.getUpdMessage());
            return false;
        }

//        StringBuffer sbUpdMessage = new StringBuffer();
//        for (CmsBtFeedInfoModel_Sku sku : product.getSkus()) {
//            if (StringUtil.isEmpty(sku.getBarcode())) {
//                product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
//                sku.setErrInfo("没有UPC");
//                sbUpdMessage.append(sku.getClientSku() +":没有UPC,");
//            }
//
//            if (sku.getPriceNet() == null || sku.getPriceNet().compareTo(0D) == 0) {
//                product.setUpdFlg(CmsConstants.FeedUpdFlgStatus.FeedErr);
//                sku.setErrInfo("成本价为0");
//                sbUpdMessage.append(sku.getClientSku() +":成本价为0,");
//            }
//        }
        if (CmsConstants.FeedUpdFlgStatus.FeedErr == product.getUpdFlg()) {
//            product.setUpdMessage(sbUpdMessage.toString());
            $info(product.getCode() + "----" + product.getUpdMessage());
            return false;
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

    private Double round(Double value) {
        BigDecimal b = new BigDecimal(value);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private void priceConvert(CmsBtFeedInfoModel_Sku skuModel) {
        Double weightCalc = StringUtil.isEmpty(skuModel.getWeightCalc()) ? 4.0 : Double.parseDouble(skuModel.getWeightCalc());
        Double current = (skuModel.getPriceNet() + weightCalc * 3.5) * 6.7 / (1 - 0.1 - 0.05 - 0.119 - 0.05);
        skuModel.setPriceCurrent(Math.ceil(current));
        Double msrp = (skuModel.getPriceClientMsrp() + weightCalc * 3.5) * 6.7 / (1 - 0.1 - 0.05 - 0.119 - 0.05);
        skuModel.setPriceMsrp(Math.ceil(msrp));
    }

    private Boolean isUsJoi(String channelId) {
        return "1".equalsIgnoreCase(Feeds.getVal1(channelId, FeedEnums.Name.is_usjoi));
    }

    private void setMainCategory(CmsBtFeedInfoModel feedProduct) {

        StopWordCleaner cleaner = new StopWordCleaner(StopWordCleaner.STOPWORD_LIST);
        // 子店feed类目path分隔符(由于导入feedInfo表时全部替换成用"-"来分隔了，所以这里写固定值就可以了)
        List<String> categoryPathSplit = new ArrayList<>();
        categoryPathSplit.add("-");
        Tokenizer tokenizer = new Tokenizer(categoryPathSplit);

        FeedQuery query = new FeedQuery(feedProduct.getCategory(), cleaner, tokenizer);
        query.setProductType(feedProduct.getProductType());
        query.setSizeType(feedProduct.getSizeType());
        query.setProductName(feedProduct.getName(), feedProduct.getBrand());

        MatchResult searchResult = searcher.search(query, false);
        if (searchResult == null) {
            String errMsg = String.format("调用Feed到主数据的匹配程序匹配主类目失败！[feedCategoryPath:%s] [productType:%s] " +
                    "[sizeType:%s] [productNameEn:%s] [brand:%s]", feedProduct.getCategory(), feedProduct.getProductType(), feedProduct.getSizeType(), feedProduct.getName(), feedProduct.getBrand());
            $error(errMsg);
        } else {
            feedProduct.setMainCategoryEn(searchResult.getEnName());
            feedProduct.setMainCategoryCn(searchResult.getCnName());
        }
    }

    private boolean compare(CmsBtFeedInfoModel model1, CmsBtFeedInfoModel model2) {
        Map<String, Object> d1 = JacksonUtil.bean2Map(model1);
        Map<String, Object> d2 = JacksonUtil.bean2Map(model2);
        CmsChannelConfigBean isImportFeedTypeConfig = CmsChannelConfigs.getConfigBeanNoCode(model1.getChannelId(), CmsConstants.ChannelConfig.AUTO_SET_FEED_IMPORT_FLG);
        if (isImportFeedTypeConfig == null) return false;
        if ("2".equalsIgnoreCase(isImportFeedTypeConfig.getConfigValue2())) {
            return true;
        } else if ("1".equalsIgnoreCase(isImportFeedTypeConfig.getConfigValue2()) && StringUtil.isEmpty(isImportFeedTypeConfig.getConfigValue3())) {
            List<String> keys = Arrays.asList(isImportFeedTypeConfig.getConfigValue3().split(","));

            for (String key : keys) {
                String v1 = d1.get(key) == null ? "" : d1.get(key).toString();
                String v2 = d2.get(key) == null ? "" : d2.get(key).toString();
                if (!v1.equalsIgnoreCase(v2)) return true;
            }
        }
        return false;


    }
}
