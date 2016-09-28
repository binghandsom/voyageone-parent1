package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.mongodb.WriteResult;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedNewTargetBean;
import com.voyageone.task2.cms.dao.feed.NewTargetFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoNewTargetModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.TARGET;

/**
 * @author gump on 2016/09/26
 * @version 2.0.0
 */
@Service
public class NewTargetAnalysisService extends BaseAnalysisService {
    @Autowired
    NewTargetFeedDao targetFeedDao;

    @Autowired
    FeedInfoService feedInfoService;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                targetFeedDao.delFullBySku(strings);
                targetFeedDao.insertFullBySku(strings);
                targetFeedDao.updateFlagBySku(strings);
            });

        }
    }
    @Override
    protected void zzWorkClear() {
        targetFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("Target产品价格文件读入开始");
        Map<String, String> retail = getRetailPriceList();
        if(retail.size() == 0) return 0;

        $info("Target产品文件读入开始");
        List<SuperFeedNewTargetBean> superfeed = new ArrayList<>();
        int cnt = 0;
        //CsvReader reader;
        CsvReader reader;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);
            String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);
            reader = new CsvReader(new FileInputStream(fileFullName), ',', Charset.forName(encode));
            // Head读入
            reader.readHeaders();
            // Body读入
            while (reader.readRecord()) {
                SuperFeedNewTargetBean targetBean = new SuperFeedNewTargetBean();
                int i = 0;
                targetBean.setTcin(reader.get(i++));
                if(!retail.containsKey(targetBean.getTcin())) continue;
                targetBean.setParentTcins(reader.get(i++));
                targetBean.setManufacturerId(reader.get(i++));
                targetBean.setManufacturerPartNumber(reader.get(i++));
                targetBean.setManufacturerModelNumber(reader.get(i++));
                targetBean.setProductDescriptionTitle(reader.get(i++));
                targetBean.setProductDescriptionDownstreamDescription(reader.get(i++));
                targetBean.setProductDescriptionLongDescription(reader.get(i++));
                targetBean.setRegprice(retail.get(targetBean.getTcin()));
                reader.get(i++);
                targetBean.setEnrichmentBuyUrl(reader.get(i++));
                targetBean.setProductClassificationItemTypeCategoryType(reader.get(i++));
                targetBean.setProductClassificationItemTypeName(reader.get(i++));
                targetBean.setProductClassificationItemTypeType(reader.get(i++));
                targetBean.setProductClassificationMerchandiseType(reader.get(i++));
                targetBean.setProductClassificationMerchandiseTypeName(reader.get(i++));
                targetBean.setProductClassificationProductSubtype(reader.get(i++));
                targetBean.setProductClassificationProductSubtypeName(reader.get(i++));
                targetBean.setProductClassificationProductType(reader.get(i++));
                targetBean.setProductClassificationProductTypeName(reader.get(i++));
                targetBean.setAvailabilityStatus(reader.get(i++));
                targetBean.setEnrichmentImagesBaseUrl(reader.get(i++));
                targetBean.setEnrichmentImagesPrimaryImage(reader.get(i++));
                targetBean.setProductBrandManufacturerBrand(reader.get(i++));
                targetBean.setUpc(reader.get(i++));
                targetBean.setIdentifiers(reader.get(i++));
                targetBean.setDisplayOptionKeywords(reader.get(i++));
                targetBean.setProductDescriptionSellingFeature(reader.get(i++));
                targetBean.setPackageDimensionsWeight(reader.get(i++));
                targetBean.setPackageDimensionsWeightUnitOfMeasure(reader.get(i++));
                targetBean.setVariationTheme(reader.get(i++));
                targetBean.setVariationSize(reader.get(i++));
                targetBean.setVariationColor(reader.get(i++));
                targetBean.setVariationMaterialType(reader.get(i++));
                targetBean.setMerchandiseTypeAttributes(reader.get(i++));
                targetBean.setRelationshipTypeCode(reader.get(i++));
                targetBean.setDpci(reader.get(i++));
                targetBean.setEcomDivisional(reader.get(i++));
                targetBean.setSoldonweb(reader.get(i++));
                targetBean.setSoldinstores(reader.get(i++));
                targetBean.setEstoreItemStatusCode(reader.get(i++));
                targetBean.setItemKind(reader.get(i++));
                targetBean.setMmbvContentStreetDate(reader.get(i++));
                targetBean.setLaunchDateTime(reader.get(i++));
                targetBean.setAverageoverallrating(reader.get(i++));
                targetBean.setTotalreviewcount(reader.get(i++));
                targetBean.setProductBrandBrand(reader.get(i++));
                targetBean.setHandlingIsBackOrderEligible(reader.get(i++));
                targetBean.setListprice(reader.get(i++));
                targetBean.setSalesClassificationNodes(reader.get(i++));
                targetBean.setEnrichmentImagesAlternateImages(reader.get(i++));
                targetBean.setDisplayOptionHasSizeChart(reader.get(i++));
                targetBean.setEnrichmentSizeChart(reader.get(i++));
                targetBean.setTaxCategoryTaxCodeId(reader.get(i++));
                targetBean.setPackageDimensionsDimensionUnitOfMeasure(reader.get(i++));
                targetBean.setPackageDimensionsHeight(reader.get(i++));
                targetBean.setPackageDimensionsDepth(reader.get(i++));
                targetBean.setPackageDimensionsWidth(reader.get(i++));
                targetBean.setHandlingReceivingFacilityType(reader.get(i++));
                targetBean.setProductDescriptionBulletDescription(reader.get(i++));
                targetBean.setHandlingBackOrderType(reader.get(i++));
                targetBean.setFulfillmentPurchaseLimit(reader.get(i++));
                targetBean.setEnvironmentalSegmentationIsHazardousMaterial(reader.get(i++));
                targetBean.setEnvironmentalSegmentationIsIngestible(reader.get(i++));
                targetBean.setFulfillmentIsWheneverShippingEligible(reader.get(i++));
                targetBean.setFulfillmentShippingRestriction(reader.get(i++));
                targetBean.setFulfillmentIsPoBoxProhibited(reader.get(i++));
                targetBean.setEnrichmentNutritionFactsIngredients(reader.get(i++));
                targetBean.setEnrichmentNutritionFactsWarning(reader.get(i++));
                targetBean.setEnrichmentNutritionFactsPreparedCount(reader.get(i++));
                targetBean.setEnrichmentNutritionFactsValuePreparedList(reader.get(i++));
                targetBean.setEnrichmentDrugFactsProductDetails(reader.get(i++));
                targetBean.setEnrichmentDrugFactsProductIngredients(reader.get(i++));
                targetBean.setEnrichmentDrugFactsLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsPurposeLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsActiveIngredientLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsActiveIngredients(reader.get(i++));
                targetBean.setEnrichmentDrugFactsUsesLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsUsesTreatedSymptoms(reader.get(i++));
                targetBean.setEnrichmentDrugFactsWarningText(reader.get(i++));
                targetBean.setEnrichmentDrugFactsWarningLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsWarningUsage(reader.get(i++));
                targetBean.setEnrichmentDrugFactsWarningOtherWarnings(reader.get(i++));
                targetBean.setEnrichmentDrugFactsDirectionLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsDirectionGeneralDirections(reader.get(i++));
                targetBean.setEnrichmentDrugFactsDirectionAgeGroups(reader.get(i++));
                targetBean.setEnrichmentDrugFactsOtherInformationLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsOtherInformationBullets(reader.get(i++));
                targetBean.setEnrichmentDrugFactsInactiveIngredients(reader.get(i++));
                targetBean.setEnrichmentDrugFactsQuestionsCommentsSectionLabel(reader.get(i++));
                targetBean.setEnrichmentDrugFactsQuestionsCommentsSectionPhoneNumber(reader.get(i++));
                targetBean.setEnergyGuideCapacityMeasurement(reader.get(i++));
                targetBean.setEnergyGuideEstimatedOperatingCost(reader.get(i++));
                targetBean.setEnergyGuideSliderLowRange(reader.get(i++));
                targetBean.setEnergyGuideSliderHighRange(reader.get(i++));
                targetBean.setEnergyGuideSliderLabel(reader.get(i++));
                targetBean.setEnergyGuideEstimatedEnergyUseTitle(reader.get(i++));
                targetBean.setEnergyGuideEstimatedEnergyUseValue(reader.get(i++));
                targetBean.setEnergyGuideEstimatedEnergyUseUnitOfMeasurement(reader.get(i++));
                targetBean.setEnergyGuideLegalDisclaimerText(reader.get(i++));
                targetBean.setEnergyGuideLegalDisclaimerInformation(reader.get(i++));
                targetBean.setEnergyGuideHasEnergyStar(reader.get(i++));
                targetBean.setReturnMethod(reader.get(i++));
                targetBean.setCountryOfOriginCode(reader.get(i++));
                targetBean.setPerordercharges(reader.get(i++));
                targetBean.setPerunitcharges(reader.get(i++));
                targetBean.setWeight(reader.get(i++));
                targetBean.setItembulky(reader.get(i++));
                targetBean.setFixedprice(reader.get(i++));
                targetBean.setHandlingfee(reader.get(i++));
                targetBean.setHandlingfeemessage(reader.get(i++));
                targetBean.setHandlingfeelegaldisclaimer(reader.get(i++));
                targetBean.setIsbn(reader.get(i++));
                targetBean.setPattern(reader.get(i++));
                targetBean.setAgeGroup(reader.get(i++));
                targetBean.setGender(reader.get(i++));
                targetBean.setBarcodeType(reader.get(i++));
                targetBean.setIacAttributes(reader.get(i++));
                targetBean.setSecBarcode(reader.get(i++));
                targetBean.setMozartVendorId(reader.get(i++));
                targetBean.setSpecialitemsource(reader.get(i++));
                targetBean.setEnrichmentReturnPolicies(reader.get(i++));
                superfeed.add(targetBean);
                cnt++;
                if (superfeed.size() > 1000) {
                    transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                    superfeed.clear();
                }
            }
            if (superfeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                superfeed.clear();
            }
            reader.close();
            $info("Target产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("Target产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("Target产品文件读入失败");
            logIssue("cms 数据导入处理", "Target产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedNewTargetBean> superFeedList) {

        for (SuperFeedNewTargetBean superFeed : superFeedList) {

            if (targetFeedDao.insertSelective(superFeed) <= 0) {
                $info("Target产品信息插入失败 Sku = " + superFeed.getTcin());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return TARGET ;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map colums = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = new ArrayList<>();
        for (FeedBean feedConfig : feedBeans) {
            if (!StringUtil.isEmpty(feedConfig.getCfg_val1())) {
                attList.add(feedConfig.getCfg_val1());
            }
        }

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, colums.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        colums.put("keyword", where);
        colums.put("tableName", table);
        if (attList.size() > 0) {
            colums.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoNewTargetModel> ModelBeans = targetFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoNewTargetModel modelBean : ModelBeans) {
            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(modelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;
                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }
            CmsBtFeedInfoModel cmsBtFeedInfoModel = modelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //取得图片开始
            List<String> imagesList = new ArrayList<>();
            //主图
            if(!StringUtil.isEmpty(modelBean.getEnrichmentImagesAlternateImages())){
                if(!StringUtil.isEmpty(modelBean.getEnrichmentImagesBaseUrl())){
                    String images[] =modelBean.getEnrichmentImagesAlternateImages().split(",");
                    for(String image:images){
                        imagesList.add(modelBean.getEnrichmentImagesBaseUrl()+image+"?wid=1200&hei=1200");
                    }
                }
            }
            //附图
            if(!StringUtil.isEmpty(modelBean.getEnrichmentImagesPrimaryImage())){
                if(!StringUtil.isEmpty(modelBean.getEnrichmentImagesBaseUrl())){
                    String images[] =modelBean.getEnrichmentImagesPrimaryImage().split(",");
                    for(String image:images){
                        imagesList.add(modelBean.getEnrichmentImagesBaseUrl()+image+"?wid=1200&hei=1200");
                    }
                }
            }
            cmsBtFeedInfoModel.setImage(imagesList);
            //取得图片结束
            List<CmsBtFeedInfoModel_Sku> skus = modelBean.getSkus();
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
                sku.setWeightOrgUnit("lb");
                sku.setImage(imagesList);
            }
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

    public Map<String, String> getRetailPriceList() {

        Map<String, String> retailPriceList = new HashMap<>();
        CsvReader reader;
        String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id_import_sku);
        String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
        String fileFullName = String.format("%s/%s", filePath, fileName);

        String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);

        try {
            reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));
            // Head读入
//            reader.readHeaders();
//            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                int i = 0;
                String sku = reader.get(i++);
                String marketprice = reader.get(i++);
                WriteResult writeResult = feedInfoService.updateFeedInfoSkuPrice("018", sku, Double.parseDouble(marketprice));
                if(!writeResult.isUpdateOfExisting()){
                    retailPriceList.put(sku,marketprice);
                }else{
                    CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductBySku("018",sku);
                    if(cmsBtFeedInfoModel.getUpdFlg() == CmsConstants.FeedUpdFlgStatus.Succeed || cmsBtFeedInfoModel.getUpdFlg() == CmsConstants.FeedUpdFlgStatus.Fail){
                        feedInfoService.updateAllUpdFlg("018","{\"code\":\""+ cmsBtFeedInfoModel.getCode()+"\"}",CmsConstants.FeedUpdFlgStatus.Pending,getTaskName());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            $info("Target价格列表不存在");
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
            return null;
        }
        return retailPriceList;
    }
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsTargetAnalySisJob";
    }
}
