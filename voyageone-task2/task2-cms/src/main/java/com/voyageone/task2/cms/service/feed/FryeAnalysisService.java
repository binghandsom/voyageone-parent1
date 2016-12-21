package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.onestop.bean.OneStopProduct;
import com.voyageone.components.onestop.service.OneStopClient;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedFryeBean;
import com.voyageone.task2.cms.dao.feed.FryeFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoFryeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.Frye;

@Service
public class FryeAnalysisService extends BaseAnalysisService {

    @Autowired
    FryeFeedDao fryeFeedDao;
    OneStopClient oneStopClient = new OneStopClient(OneStopClient.getConfigFromDb("032"));

    @PreDestroy
    public void shutdown() {
        try {
            oneStopClient.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                fryeFeedDao.delFullBySku(strings);
                fryeFeedDao.insertFullBySku(strings);
                fryeFeedDao.updateFlagBySku(strings);
            });
        }
    }

    @Override
    protected void zzWorkClear() {
        fryeFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("Frye产品文件读入开始");
        List<SuperFeedFryeBean> superFeed = new ArrayList<>();
        int cnt = 0;
        Date getFeedDate = new Date(0);
        //取得产品productId
        List<Long> productIdList = oneStopClient.catalogProductId(getFeedDate);
        if (productIdList.size() > 0) {
            for (Long productId : productIdList) {
                //根据产品productId取得对应的产品信息
                OneStopProduct oneStopProduct = oneStopClient.catalog(productId);
                //Variants处理开始
                if (oneStopProduct.getVariants().size() > 0) {
                    for (OneStopProduct.VariantsBean variant : oneStopProduct.getVariants()) {
                        SuperFeedFryeBean fryeBean = new SuperFeedFryeBean();
                        //ProductId
                        fryeBean.setProductId(oneStopProduct.getProductId());
                        //IsActive
                        fryeBean.setIsactive(oneStopProduct.isIsActive());
                        //Attributes处理开始
                        List<OneStopProduct.AttributesBean> Attributes = oneStopProduct.getAttributes();
                        StringBuilder sb = new StringBuilder();
                        if (Attributes != null) {
                            Attributes.stream().filter(Attribute -> !StringUtil.isEmpty(Attribute.getName())).forEach(Attribute -> {
                                StringBuilder sbKey = new StringBuilder();
                                for (Map.Entry<String, String> entry : Attribute.getValues().entrySet()) {
                                    sbKey.append(entry.getValue());
                                }
                                sb.append(Attribute.getName()).append(":").append(sbKey).append(",");
                                //Fabric
                                if ("Fabric".equals(Attribute.getName())) {
                                    StringBuilder sbFabric = new StringBuilder();
                                    for (Map.Entry<String, String> entry : Attribute.getValues().entrySet()) {
                                        sbFabric.append(entry.getValue());
                                    }
                                    fryeBean.setFabric(String.valueOf(sbFabric));
                                }
                                if ("Tax Code".equals(Attribute.getName())) {
                                    StringBuilder sbTaxCode = new StringBuilder();
                                    for (Map.Entry<String, String> entry : Attribute.getValues().entrySet()) {
                                        sbTaxCode.append(entry.getValue());
                                    }
                                    fryeBean.setTaxCode(String.valueOf(sbTaxCode));
                                }
                                if ("Gender".equals(Attribute.getName())) {
                                    StringBuilder sbGender = new StringBuilder();
                                    for (Map.Entry<String, String> entry : Attribute.getValues().entrySet()) {
                                        sbGender.append(entry.getValue());
                                    }
                                    fryeBean.setSex(String.valueOf(sbGender));
                                }
                            });
                        }
                        if (sb.length() > 0) {
                            fryeBean.setAttributes(sb.deleteCharAt(sb.length() - 1).toString());
                        }
                        //Attributes处理结束
                        //IsTaxable
                        fryeBean.setIstaxable(oneStopProduct.isIsTaxable());
                        //MfgSku
                        fryeBean.setMfgsku(oneStopProduct.getMfgSku());
                        //TaxClassification
                        fryeBean.setTaxclassification(oneStopProduct.getTaxClassification());
                        //Name
                        fryeBean.setName(oneStopProduct.getName());
                        //FirstLiveDate
                        fryeBean.setFirstlivedate(oneStopProduct.getFirstLiveString());
                        //MetaTags
                        fryeBean.setMetatags(String.valueOf(oneStopProduct.getMetaTags()));
                        //CategoryId
                        fryeBean.setCategoryid(oneStopProduct.getCategoryId());
                        OneStopProduct.MetaDataBean metaDataBean = oneStopProduct.getMetaData();
                        fryeBean.setMetadataCategoryextratext1(metaDataBean.getCategoryExtraText1());
                        fryeBean.setMetadataCategoryextratext2(metaDataBean.getCategoryExtraText2());
                        fryeBean.setMetadataCategoryextratext3(metaDataBean.getCategoryExtraText3());
                        fryeBean.setMetadataExtratext1(metaDataBean.getExtraText1());
                        fryeBean.setMetadataDescription2(metaDataBean.getDescription2());
                        fryeBean.setMetadataDescription3(metaDataBean.getDescription3());
                        fryeBean.setMetadataDescription4(metaDataBean.getDescription4());
                        if (!StringUtil.isEmpty(metaDataBean.getDescription2WithFormatting())) {
                            fryeBean.setMetadataDescription2withformatting(metaDataBean.getDescription2WithFormatting());
                        } else {
                            fryeBean.setMetadataDescription2withformatting("");
                        }
                        if (!StringUtil.isEmpty(metaDataBean.getDescription3WithFormatting())) {
                            fryeBean.setMetadataDescription3withformatting(metaDataBean.getDescription3WithFormatting());
                        } else {
                            fryeBean.setMetadataDescription3withformatting("");
                        }
                        if (!StringUtil.isEmpty(metaDataBean.getDescription4WithFormatting())) {
                            fryeBean.setMetadataDescription4withformatting(metaDataBean.getDescription4WithFormatting());
                        } else {
                            fryeBean.setMetadataDescription4withformatting("");
                        }
                        if (!StringUtil.isEmpty(metaDataBean.getDescription4WithFormatting())) {
                            String origin = metaDataBean.getDescription4WithFormatting().replaceAll("<ul>", "").replaceAll("</ul>", ",").replaceAll("<li>", "").replaceAll("</li>", ",");
                            String[] originList = origin.split(",");
                            String countyValue = "";
                            for (String county : originList) {
                                if (county.contains("Made in the")) {
                                    countyValue = county.replace("Made in the ", "");
                                }
                            }
                            fryeBean.setOrigin(countyValue);
                        }
                        fryeBean.setMetadataCategoryname(metaDataBean.getCategoryName());

                        if (!StringUtil.isEmpty(metaDataBean.getCategoryName()) && StringUtil.isEmpty(fryeBean.getSex())) {
                            String[] categoryNameList = metaDataBean.getCategoryName().replace(" ", "").trim().split("->");
                            for (String categoryName : categoryNameList) {
                                if ("women".equals(categoryName.toLowerCase()) || "womens".equals(categoryName.toLowerCase())) {
                                    fryeBean.setSex(categoryName);
                                }
                                if ("baby".equals(categoryName.toLowerCase())) {
                                    fryeBean.setSex(categoryName);
                                }
                                if ("girl".equals(categoryName.toLowerCase())) {
                                    fryeBean.setSex(categoryName);
                                }
                                if ("boy".equals(categoryName.toLowerCase())) {
                                    fryeBean.setSex(categoryName);
                                }
                                if ("mens".equals(categoryName.toLowerCase()) || "men".equals(categoryName.toLowerCase())) {
                                    fryeBean.setSex(categoryName);
                                }
                                if ("kids".equals(categoryName.toLowerCase())) {
                                    fryeBean.setSex(categoryName);
                                }
                            }
                        }
                        if (StringUtil.isEmpty(fryeBean.getSex())) {
                            fryeBean.setSex("unisex");
                        }
                        fryeBean.setMetadataDetail(metaDataBean.getDetail());
                        //如果【Fabric】为空，取MetaData.Detail
                        if (StringUtil.isEmpty(fryeBean.getFabric())) {
                            fryeBean.setFabric(metaDataBean.getDetail());
                        }
                        fryeBean.setMetadataUnitcost(metaDataBean.getUnitCost());
                        fryeBean.setMetadataPreviouscost(metaDataBean.getPreviousCost());
                        fryeBean.setMetadataMaxcost(metaDataBean.getMaxCost());
                        fryeBean.setMetadataMincost(metaDataBean.getMinCost());
                        fryeBean.setMetadataMinregularprice(metaDataBean.getMinRegularPrice());
                        fryeBean.setMetadataMaxregularprice(metaDataBean.getMaxRegularPrice());
                        fryeBean.setMetadataMinsaleprice(metaDataBean.getMinSalePrice());
                        fryeBean.setMetadataMaxsaleprice(metaDataBean.getMaxSalePrice());
                        fryeBean.setMetadataSamemaxmin(metaDataBean.isSameMaxMin());
                        fryeBean.setMetadataShippingsurcharge(metaDataBean.getShippingSurcharge());
                        fryeBean.setMetadataSizecharturl(metaDataBean.getSizeChartUrl());
                        fryeBean.setMetadataWeight(metaDataBean.getWeight());
                        fryeBean.setMetadataPreorderavailabledate(metaDataBean.getPreOrderAvailableString());
                        fryeBean.setMetadataDisplayoospopup(metaDataBean.isDisplayOosPopup());
                        fryeBean.setMetadataShowwhenoos(metaDataBean.isShowWhenOos());
                        fryeBean.setMetadataNonremovablefromcart(metaDataBean.isNonremovableFromCart());
                        fryeBean.setMetadataProducturl(metaDataBean.getProductUrl());
                        if (!StringUtil.isEmpty(fryeBean.getMetadataProducturl())) {
                            fryeBean.getMetadataProducturl().replace("//frye.dev.onestop.com", "http://www.thefryecompany.com ");
                        }
                        //Variants_UPC
                        fryeBean.setVariantsUpc(variant.getUPC());
                        //Variants_Name
                        fryeBean.setVariantsName(variant.getName());
                        StringBuilder sbExtraImageThumb = new StringBuilder();
                        StringBuilder sbExtraImageThumbUrl = new StringBuilder();
                        StringBuilder sbExtraImageLargeThumb = new StringBuilder();
                        StringBuilder sbExtraImageLargeThumbUrl = new StringBuilder();
                        StringBuilder sbFilePath = new StringBuilder();
                        //Variants.Medias.Meta取得开始
                        for (OneStopProduct.VariantsBean.MediasBean medias : variant.getMedias()) {
                            OneStopProduct.VariantsBean.MediasBean.MediaMetaBean metaBean = medias.getMeta();
                            if (!StringUtil.isEmpty(metaBean.getExtraImageThumb())) {
                                sbExtraImageThumb.append(metaBean.getExtraImageThumb()).append(",");
                            }
                            if (!StringUtil.isEmpty(metaBean.getExtraImageThumbUrl())) {
                                sbExtraImageThumbUrl.append(metaBean.getExtraImageThumbUrl()).append(",");
                            }
                            if (!StringUtil.isEmpty(metaBean.getExtraImageLargeThumb())) {
                                sbExtraImageLargeThumb.append(metaBean.getExtraImageLargeThumb()).append(",");
                            }
                            if (!StringUtil.isEmpty(metaBean.getExtraImageLargeThumbUrl())) {
                                sbExtraImageLargeThumbUrl.append(metaBean.getExtraImageLargeThumbUrl()).append(",");
                            }
                            if (!StringUtil.isEmpty(medias.getFilePath())) {
                                sbFilePath.append(medias.getFilePath()).append(",");
                            }
                        }
                        //Variants_Medias_Meta_ExtraImageThumb
                        if (sbExtraImageThumb.length() > 0) {
                            fryeBean.setVariantsMediasMetaExtraimagethumb(sbExtraImageThumb.deleteCharAt(sbExtraImageThumb.length() - 1).toString());
                        }
                        //Variants_Medias_Meta_ExtraImageThumbUrl
                        if (sbExtraImageThumbUrl.length() > 0) {
                            fryeBean.setVariantsMediasMetaExtraimagethumburl(sbExtraImageThumbUrl.deleteCharAt(sbExtraImageThumbUrl.length() - 1).toString());
                        }
                        //Variants_Medias_Meta_ExtraImageLargeThumb
                        if (sbExtraImageLargeThumb.length() > 0) {
                            fryeBean.setVariantsMediasMetaExtraimagelargethumb(sbExtraImageLargeThumb.deleteCharAt(sbExtraImageLargeThumb.length() - 1).toString());
                        }
                        //Variants_Medias_Meta_ExtraImageLargeThumbUrl
                        if (sbExtraImageLargeThumbUrl.length() > 0) {
                            fryeBean.setVariantsMediasMetaExtraimagelargethumburl(sbExtraImageLargeThumbUrl.deleteCharAt(sbExtraImageLargeThumbUrl.length() - 1).toString());
                        }
                        //
                        if (sbFilePath.length() > 0) {
                            fryeBean.setVariantsMediasFilepath(sbFilePath.deleteCharAt(sbFilePath.length() - 1).toString());
                        }
                        //Variants.Medias.Meta取得结束

                        //Variants.Medias.Options取得开始
                        for (OneStopProduct.OptionsBean option : variant.getOptions()) {
                            if ("Color".equals(option.getName())) {
                                //Variants_Options_Color
                                StringBuilder sbValue = new StringBuilder();
                                StringBuilder sbName = new StringBuilder();
                                for (Map.Entry<String, String> entry : option.getValues().entrySet()) {
                                    sbValue.append(entry.getValue());
                                    sbName.append(entry.getValue());
                                }
                                if (sbValue.length() > 0) {
                                    fryeBean.setVariantsOptionsColor(sbValue.toString());
                                    fryeBean.setVariantsOptionsColorValues(String.valueOf(sbName.toString()));
                                }
                            }
                            //Variants_Options_size
                            if ("Size".equals(option.getName())) {
                                //Variants_Options_Color
                                StringBuilder sbValue = new StringBuilder();
                                StringBuilder sbName = new StringBuilder();
                                for (Map.Entry<String, String> entry : option.getValues().entrySet()) {
                                    sbValue.append(entry.getValue());
                                    sbName.append(entry.getValue());
                                }
                                if (sbValue.length() > 0) {
                                    fryeBean.setVariantsOptionsSize(String.valueOf(sbValue.toString()));
                                    fryeBean.setVariantsOptionsSizeValues(String.valueOf(sbName.toString()));
                                }
                            }
                        }
                        //Variants.Medias.Options取得结束
                        superFeed.add(fryeBean);
                        cnt++;
                        if (superFeed.size() > 1000) {
                            transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                            superFeed.clear();
                        }
                    }
                }
            }
            if (superFeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                superFeed.clear();
            }
            $info("Frye产品文件读入完成");
        }
        return cnt;
    }

    /**
     * Frye产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedFryeBean> superFeedList) {

        superFeedList.stream()
                .filter(superFeed -> fryeFeedDao.insertSelective(superFeed) <= 0)
                .forEach(superFeed -> $info("Frye产品信息插入失败 Sku = " + superFeed.getVariantsUpc()));
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return Frye;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map<String, Object> column = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = feedBeans.stream()
                .filter(feedConfig -> !StringUtil.isEmpty(feedConfig.getCfg_val1()))
                .map(FeedBean::getCfg_val1)
                .collect(Collectors.toList());

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, column.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        column.put("keyword", where);
        column.put("tableName", table);
        if (attList.size() > 0) {
            column.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }
        List<CmsBtFeedInfoFryeModel> vtmModelBeans = fryeFeedDao.selectSuperFeedModel(column);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoFryeModel vtmModelBean : vtmModelBeans) {
            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add(String.valueOf(temp.get(key)));
                attribute.put(key, values);
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //设置重量开始
            List<CmsBtFeedInfoModel_Sku> skus = vtmModelBean.getSkus();
            for (CmsBtFeedInfoModel_Sku sku : skus) {
                String Weight = sku.getWeightOrg().trim();
                Pattern pattern = Pattern.compile("[^0-9.]");
                Matcher matcher = pattern.matcher(Weight);
                if (matcher.find()) {
                    int index = Weight.indexOf(matcher.group());
                    if (index != -1) {
                        String weightOrg = Weight.substring(0, index);
                        sku.setWeightOrg(weightOrg);
                    }
                }
                sku.setWeightOrgUnit(sku.getWeightOrgUnit());
            }
            cmsBtFeedInfoModel.setSkus(skus);
            //设置重量结束

            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setImage(beforeFeed.getImage().stream().distinct().collect(Collectors.toList()));
                beforeFeed.setAttribute(attributeMerge(beforeFeed.getAttribute(), cmsBtFeedInfoModel.getAttribute()));
            } else {
                modelBeans.add(cmsBtFeedInfoModel);
                codeMap.put(cmsBtFeedInfoModel.getCode(), cmsBtFeedInfoModel);
            }

        }
        $info("取得 [ %s ] 的 Product 数 %s", categorPath, modelBeans.size());

        return modelBeans;
    }

    @Override
    protected String getTaskName() {
        return "CmsFryeAnalysisJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
