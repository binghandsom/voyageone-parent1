package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.SuperFeedJEBean;
import com.voyageone.task2.cms.bean.SuperfeedBhfoBean;
import com.voyageone.task2.cms.dao.feed.BhfoFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoBhfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.BHFO_MINIMALL;

/**
 * @author james.li on 2016/4/25.
 * @version 2.0.0
 */
@Service
public class BhfoAnalysisService extends BaseAnalysisService {

    @Autowired
    BhfoFeedDao bhfoFeedDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBhfoAnalySisJob";
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperfeedBhfoBean> superfeedlist) {
        boolean isSuccess = true;

        for (SuperfeedBhfoBean superfeed : superfeedlist) {

            if (bhfoFeedDao.insertSelective(superfeed) <= 0) {
                $info("BHFO产品信息插入失败 InventoryNumber = " + superfeed.getInventoryNumber());
            }
        }
        return isSuccess;
    }

    /**
     * Bhfo产品文件读入
     */
    public int superFeedImport() {


        $info("Bhfo产品品牌黑名单读入开始");
        Set<String> blackList = new HashSet<>();
        Feeds.getConfigs(getChannel().getId(),FeedEnums.Name.blackList).forEach(m -> blackList.add(m.getCfg_val1().toLowerCase().trim()));


        $info("Bhfo产品文件读入开始");

        List<SuperfeedBhfoBean> superfeed = new ArrayList<>();
        int cnt = 0;

        CsvReader reader;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);

            String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);

            reader = new CsvReader(new FileInputStream(fileFullName), '|', Charset.forName(encode));

            // Head读入
            reader.readHeaders();
            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                SuperfeedBhfoBean superfeedBhfobean = new SuperfeedBhfoBean();

                int i = 0;

                superfeedBhfobean.setInventoryNumber(reader.get(i++));
                superfeedBhfobean.setTitle(reader.get(i++));
                superfeedBhfobean.setMsrp(reader.get(i++));
                superfeedBhfobean.setVoyageonePrice(reader.get(i++));
                superfeedBhfobean.setShippingCost(reader.get(i++));
                superfeedBhfobean.setWeight(reader.get(i++));
                superfeedBhfobean.setDescription(reader.get(i++));
                superfeedBhfobean.setMainImageUrl(reader.get(i++));
                superfeedBhfobean.setQuantity(reader.get(i++));
                superfeedBhfobean.setAdditionalImages(reader.get(i++));
                superfeedBhfobean.setParentChildStandalone(reader.get(i++));
                superfeedBhfobean.setItemGroupId(reader.get(i++));
                superfeedBhfobean.setVariationSwapImageUrl(reader.get(i++));
                superfeedBhfobean.setVaryingAttributeNames(reader.get(i++));
                superfeedBhfobean.setVaryingAttribute1(reader.get(i++));
                superfeedBhfobean.setVaryingAttribute2(reader.get(i++));
                superfeedBhfobean.setBrand1(reader.get(i++));
                if(blackList.contains(superfeedBhfobean.getBrand1().toLowerCase().trim())) {
                    continue;
                }
                superfeedBhfobean.setCountryOfOrigin(reader.get(i++));
                superfeedBhfobean.setSeriesName(reader.get(i++));
                superfeedBhfobean.setGtin(reader.get(i++));
                superfeedBhfobean.setMpn(reader.get(i++));
                superfeedBhfobean.setStoreCategory(reader.get(i++));
                superfeedBhfobean.setMaterial(reader.get(i++));
                superfeedBhfobean.setPattern(reader.get(i++));
                superfeedBhfobean.setColor(reader.get(i++));
                superfeedBhfobean.setSize(reader.get(i++));
                superfeedBhfobean.setGender(reader.get(i++));
                superfeedBhfobean.setAgeGroup(reader.get(i++));
                superfeedBhfobean.setLength(reader.get(i++));
                superfeedBhfobean.setWidth(reader.get(i++));
                superfeedBhfobean.setHeight(reader.get(i++));
                superfeedBhfobean.setAmazonBeautyItemType(reader.get(i++));
                superfeedBhfobean.setAmazonDepartment(reader.get(i++));
                superfeedBhfobean.setAmazonHeelHeight(reader.get(i++));
                superfeedBhfobean.setAmazonHomeItemType(reader.get(i++));
                superfeedBhfobean.setAmazonInseam(reader.get(i++));
                superfeedBhfobean.setAmazonItemType(reader.get(i++));
                superfeedBhfobean.setAmazonKeywords1(reader.get(i++));
                superfeedBhfobean.setAmazonKeywords2(reader.get(i++));
                superfeedBhfobean.setAmazonKeywords3(reader.get(i++));
                superfeedBhfobean.setAmazonKeywords4(reader.get(i++));
                superfeedBhfobean.setAmazonLegStyle(reader.get(i++));
                superfeedBhfobean.setAmazonMensItemType(reader.get(i++));
                superfeedBhfobean.setAmazonPhotoReady(reader.get(i++));
                superfeedBhfobean.setAmazonProductType(reader.get(i++));
                superfeedBhfobean.setAmazonRise(reader.get(i++));
                superfeedBhfobean.setAmazonShoeItemType(reader.get(i++));
                superfeedBhfobean.setAmazonShoeSize(reader.get(i++));
                superfeedBhfobean.setAmazonShoeSizeMap(reader.get(i++));
                superfeedBhfobean.setAmazonShoeWidth(reader.get(i++));
                superfeedBhfobean.setAmazonSpecialSize(reader.get(i++));
                superfeedBhfobean.setAmazonVariantDetails(reader.get(i++));
                superfeedBhfobean.setAmazonWaistSize(reader.get(i++));
                superfeedBhfobean.setAmazonWomensItemType(reader.get(i++));
                superfeedBhfobean.setApparelStyle(reader.get(i++));
                superfeedBhfobean.setApplication(reader.get(i++));
                superfeedBhfobean.setApplicationArea(reader.get(i++));
                superfeedBhfobean.setAmazonCategory(reader.get(i++));
                superfeedBhfobean.setApplicationType(reader.get(i++));
                superfeedBhfobean.setAsin(reader.get(i++));
                superfeedBhfobean.setAssembly(reader.get(i++));
                superfeedBhfobean.setAttachment(reader.get(i++));
                superfeedBhfobean.setAuctionDescription(reader.get(i++));
                superfeedBhfobean.setAudioOutputs(reader.get(i++));
                superfeedBhfobean.setBackCoverage(reader.get(i++));
                superfeedBhfobean.setBackPockets(reader.get(i++));
                superfeedBhfobean.setBagDepthInches(reader.get(i++));
                superfeedBhfobean.setBagHeightInches(reader.get(i++));
                superfeedBhfobean.setBagLengthInches(reader.get(i++));
                superfeedBhfobean.setBagWidthInches(reader.get(i++));
                superfeedBhfobean.setBakeShape(reader.get(i++));
                superfeedBhfobean.setBeddingSize(reader.get(i++));
                superfeedBhfobean.setBhfoEbayCategory(reader.get(i++));
                superfeedBhfobean.setBhfoEbayStoreCategory(reader.get(i++));
                superfeedBhfobean.setBhfoAccessory1DescriptionC(reader.get(i++));
                superfeedBhfobean.setBhfoAccessory1TitleC(reader.get(i++));
                superfeedBhfobean.setBhfoColorC(reader.get(i++));
                superfeedBhfobean.setBhfoColorTextC(reader.get(i++));
                superfeedBhfobean.setBhfoCountryOfOriginC(reader.get(i++));
                superfeedBhfobean.setBhfoFlawDescriptionC(reader.get(i++));
                superfeedBhfobean.setBhfoSizeC(reader.get(i++));
                superfeedBhfobean.setBhfoSizeAdditionalC(reader.get(i++));
                superfeedBhfobean.setBhfoSizeTypeC(reader.get(i++));
                superfeedBhfobean.setBhfoStyleC(reader.get(i++));
                superfeedBhfobean.setBhfoStyleNameC(reader.get(i++));
                superfeedBhfobean.setBhfoStyleTypeC(reader.get(i++));
                superfeedBhfobean.setBottleMaterial(reader.get(i++));
                superfeedBhfobean.setBottleSizeOunces(reader.get(i++));
                superfeedBhfobean.setBottomClosure(reader.get(i++));
                superfeedBhfobean.setBottomRise(reader.get(i++));
                superfeedBhfobean.setBraClosure(reader.get(i++));
                superfeedBhfobean.setBraFeatures(reader.get(i++));
                superfeedBhfobean.setBraStraps(reader.get(i++));
                superfeedBhfobean.setBrand2(reader.get(i++));
                superfeedBhfobean.setCaStoreCategory(reader.get(i++));
                superfeedBhfobean.setCandleSize(reader.get(i++));
                superfeedBhfobean.setCapacityOunces(reader.get(i++));
                superfeedBhfobean.setClassification(reader.get(i++));
                superfeedBhfobean.setClosure(reader.get(i++));
                superfeedBhfobean.setCollar(reader.get(i++));
                superfeedBhfobean.setCollection(reader.get(i++));
                superfeedBhfobean.setColor2(reader.get(i++));
                superfeedBhfobean.setCompartment(reader.get(i++));
                superfeedBhfobean.setCompatibleBrands(reader.get(i++));
                superfeedBhfobean.setCompatibleProducts(reader.get(i++));
                superfeedBhfobean.setCondition(reader.get(i++));
                superfeedBhfobean.setConnectivity(reader.get(i++));
                superfeedBhfobean.setCookingFunctions(reader.get(i++));
                superfeedBhfobean.setCountryOfOrigin2(reader.get(i++));
                superfeedBhfobean.setCupSize(reader.get(i++));
                superfeedBhfobean.setDenimWash(reader.get(i++));
                superfeedBhfobean.setDepthInches(reader.get(i++));
                superfeedBhfobean.setDimensions(reader.get(i++));
                superfeedBhfobean.setDimensions2(reader.get(i++));
                superfeedBhfobean.setDirtCapture(reader.get(i++));
                superfeedBhfobean.setDisplay(reader.get(i++));
                superfeedBhfobean.setDressLength(reader.get(i++));
                superfeedBhfobean.setDressType(reader.get(i++));
                superfeedBhfobean.setDropLength(reader.get(i++));
                superfeedBhfobean.setEan(reader.get(i++));
                superfeedBhfobean.setEarpieceDesign(reader.get(i++));
                superfeedBhfobean.setEbayHeelHeight(reader.get(i++));
                superfeedBhfobean.setFabricType(reader.get(i++));
                superfeedBhfobean.setFeatures(reader.get(i++));
                superfeedBhfobean.setFeaturesAndFastening(reader.get(i++));
                superfeedBhfobean.setFeatures2(reader.get(i++));
                superfeedBhfobean.setFinish(reader.get(i++));
                superfeedBhfobean.setFit(reader.get(i++));
                superfeedBhfobean.setFitDesign(reader.get(i++));
                superfeedBhfobean.setFlagstyle(reader.get(i++));
                superfeedBhfobean.setFormulation(reader.get(i++));
                superfeedBhfobean.setFrameMaterialType(reader.get(i++));
                superfeedBhfobean.setFrameMetal(reader.get(i++));
                superfeedBhfobean.setFrontStyle(reader.get(i++));
                superfeedBhfobean.setFuelSource(reader.get(i++));
                superfeedBhfobean.setFunction(reader.get(i++));
                superfeedBhfobean.setFurType(reader.get(i++));
                superfeedBhfobean.setGender2(reader.get(i++));
                superfeedBhfobean.setGenre(reader.get(i++));
                superfeedBhfobean.setHandleType(reader.get(i++));
                superfeedBhfobean.setHarmonizedCode(reader.get(i++));
                superfeedBhfobean.setHeelHeightInches(reader.get(i++));
                superfeedBhfobean.setHeight2(reader.get(i++));
                superfeedBhfobean.setImageUrl1(reader.get(i++));
                superfeedBhfobean.setImageUrl2(reader.get(i++));
                superfeedBhfobean.setImageUrl3(reader.get(i++));
                superfeedBhfobean.setImageUrl4(reader.get(i++));
                superfeedBhfobean.setIncludes(reader.get(i++));
                superfeedBhfobean.setIncludes2(reader.get(i++));
                superfeedBhfobean.setInseamInches(reader.get(i++));
                superfeedBhfobean.setInsertMaterial(reader.get(i++));
                superfeedBhfobean.setIsAChildItem(reader.get(i++));
                superfeedBhfobean.setIsAParentItem(reader.get(i++));
                superfeedBhfobean.setIsblocked(reader.get(i++));
                superfeedBhfobean.setIsbn(reader.get(i++));
                superfeedBhfobean.setIsfba(reader.get(i++));
                superfeedBhfobean.setItemId(reader.get(i++));
                superfeedBhfobean.setItemShape(reader.get(i++));
                superfeedBhfobean.setItemSubtitle(reader.get(i++));
                superfeedBhfobean.setItemTitle(reader.get(i++));
                superfeedBhfobean.setItemauctiondescription(reader.get(i++));
                superfeedBhfobean.setItemimageurl10(reader.get(i++));
                superfeedBhfobean.setItemimageurl5(reader.get(i++));
                superfeedBhfobean.setItemimageurl6(reader.get(i++));
                superfeedBhfobean.setItemimageurl7(reader.get(i++));
                superfeedBhfobean.setJacketLegth(reader.get(i++));
                superfeedBhfobean.setJacketSleeveLengthInches(reader.get(i++));
                superfeedBhfobean.setLength2(reader.get(i++));
                superfeedBhfobean.setLensColor(reader.get(i++));
                superfeedBhfobean.setLensMaterialType(reader.get(i++));
                superfeedBhfobean.setLensWidthMillimeters(reader.get(i++));
                superfeedBhfobean.setLid(reader.get(i++));
                superfeedBhfobean.setLightbulbType(reader.get(i++));
                superfeedBhfobean.setManufacturer(reader.get(i++));
                superfeedBhfobean.setManufacturerDescription(reader.get(i++));
                superfeedBhfobean.setMaterial2(reader.get(i++));
                superfeedBhfobean.setMaterialSpecialty(reader.get(i++));
                superfeedBhfobean.setMaxMagnification(reader.get(i++));
                superfeedBhfobean.setMeliCategory(reader.get(i++));
                superfeedBhfobean.setMeliColor(reader.get(i++));
                superfeedBhfobean.setMeliSeason(reader.get(i++));
                superfeedBhfobean.setMeliSize(reader.get(i++));
                superfeedBhfobean.setMeliTitle(reader.get(i++));
                superfeedBhfobean.setModelNumber(reader.get(i++));
                superfeedBhfobean.setModelNumber2(reader.get(i++));
                superfeedBhfobean.setMovement(reader.get(i++));
                superfeedBhfobean.setMpn2(reader.get(i++));
                superfeedBhfobean.setNeckSize(reader.get(i++));
                superfeedBhfobean.setNeckline(reader.get(i++));
                superfeedBhfobean.setItemlevelshipping(reader.get(i++));
                superfeedBhfobean.setNonStick(reader.get(i++));
                superfeedBhfobean.setNumberOfPieces(reader.get(i++));
                superfeedBhfobean.setNumberOfSlices(reader.get(i++));
                superfeedBhfobean.setOccasion(reader.get(i++));
                superfeedBhfobean.setOpenQuantity(reader.get(i++));
                superfeedBhfobean.setOpenquantitypooled(reader.get(i++));
                superfeedBhfobean.setPadding(reader.get(i++));
                superfeedBhfobean.setPantType(reader.get(i++));
                superfeedBhfobean.setPantyStyle(reader.get(i++));
                superfeedBhfobean.setParentSku(reader.get(i++));
                superfeedBhfobean.setPattern2(reader.get(i++));
                superfeedBhfobean.setPhotoSize(reader.get(i++));
                superfeedBhfobean.setPillowSize(reader.get(i++));
                superfeedBhfobean.setPlatform(reader.get(i++));
                superfeedBhfobean.setPlatformHeightInches(reader.get(i++));
                superfeedBhfobean.setPocketStyle(reader.get(i++));
                superfeedBhfobean.setPolarizationType(reader.get(i++));
                superfeedBhfobean.setPower(reader.get(i++));
                superfeedBhfobean.setProductMargin(reader.get(i++));
                superfeedBhfobean.setProtection(reader.get(i++));
                superfeedBhfobean.setQuantityAvailable(reader.get(i++));
                superfeedBhfobean.setRating(reader.get(i++));
                superfeedBhfobean.setReceivedInInventory(reader.get(i++));
                superfeedBhfobean.setRecommendedAge(reader.get(i++));
                superfeedBhfobean.setRecommendedAgeRange(reader.get(i++));
                superfeedBhfobean.setRelationshipName(reader.get(i++));
                superfeedBhfobean.setRetailPrice(reader.get(i++));
                superfeedBhfobean.setRiseInches(reader.get(i++));
                superfeedBhfobean.setRopDateFirstComplete(reader.get(i++));
                superfeedBhfobean.setRugStyle(reader.get(i++));
                superfeedBhfobean.setRykaConsignment(reader.get(i++));
                superfeedBhfobean.setScentType(reader.get(i++));
                superfeedBhfobean.setSeason(reader.get(i++));
                superfeedBhfobean.setSellerLogoUrl(reader.get(i++));
                superfeedBhfobean.setSet(reader.get(i++));
                superfeedBhfobean.setShaftHeightInches(reader.get(i++));
                superfeedBhfobean.setShaftWidthInches(reader.get(i++));
                superfeedBhfobean.setShape(reader.get(i++));
                superfeedBhfobean.setShoeClosure(reader.get(i++));
                superfeedBhfobean.setShortDescription(reader.get(i++));
                superfeedBhfobean.setSilhouette(reader.get(i++));
                superfeedBhfobean.setSizeOrigin(reader.get(i++));
                superfeedBhfobean.setSkirtLength(reader.get(i++));
                superfeedBhfobean.setSkirtType(reader.get(i++));
                superfeedBhfobean.setSku(reader.get(i++));
                superfeedBhfobean.setSleeveLength(reader.get(i++));
                superfeedBhfobean.setSleeveLengthInches(reader.get(i++));
                superfeedBhfobean.setSpecialty(reader.get(i++));
                superfeedBhfobean.setSport(reader.get(i++));
                superfeedBhfobean.setStKeyword(reader.get(i++));
                superfeedBhfobean.setStone(reader.get(i++));
                superfeedBhfobean.setStoreTitle(reader.get(i++));
                superfeedBhfobean.setStrapDropInches(reader.get(i++));
                superfeedBhfobean.setStraps(reader.get(i++));
                superfeedBhfobean.setStyle(reader.get(i++));
                superfeedBhfobean.setSupplier(reader.get(i++));
                superfeedBhfobean.setSupplierPo(reader.get(i++));
                superfeedBhfobean.setSuppliercode(reader.get(i++));
                superfeedBhfobean.setSupplierid(reader.get(i++));
                superfeedBhfobean.setSwimwearBottom(reader.get(i++));
                superfeedBhfobean.setSwimwearTop(reader.get(i++));
                superfeedBhfobean.setTaxproductcode(reader.get(i++));
                superfeedBhfobean.setTheme(reader.get(i++));
                superfeedBhfobean.setThreadCount(reader.get(i++));
                superfeedBhfobean.setTieLength(reader.get(i++));
                superfeedBhfobean.setTieWidth(reader.get(i++));
                superfeedBhfobean.setTotalDressLengthInches(reader.get(i++));
                superfeedBhfobean.setTotalJacketLengthInches(reader.get(i++));
                superfeedBhfobean.setTotalLengthInches(reader.get(i++));
                superfeedBhfobean.setTotalSkirtLengthInches(reader.get(i++));
                superfeedBhfobean.setTotalquantitypooled(reader.get(i++));
                superfeedBhfobean.setUmbrellaOpener(reader.get(i++));
                superfeedBhfobean.setUpc(reader.get(i++));
                superfeedBhfobean.setUploadhaslastmodifiedgmt(reader.get(i++));
                superfeedBhfobean.setVents(reader.get(i++));
                superfeedBhfobean.setVideoInputs(reader.get(i++));
                superfeedBhfobean.setWaistAcrossInches(reader.get(i++));
                superfeedBhfobean.setWalletHeightInches(reader.get(i++));
                superfeedBhfobean.setWarehouselocation(reader.get(i++));
                superfeedBhfobean.setWarranty(reader.get(i++));
                superfeedBhfobean.setWeight2(reader.get(i++));
                superfeedBhfobean.setWidth2(reader.get(i++));
                superfeedBhfobean.setYear(reader.get(i++));
                superfeedBhfobean.setRoom(reader.get(i++));
                superfeedBhfobean.setLatestSupplier(reader.get(i++));
                superfeedBhfobean.setBandSize(reader.get(i++));
                superfeedBhfobean.setWalletWidthInches(reader.get(i++));
                superfeedBhfobean.setMd5(reader.get(i++));
                superfeed.add(superfeedBhfobean);
                cnt++;
                if (superfeed.size() > 1000) {
                    transactionRunnerCms2.runWithTran(() -> insertSuperFeed(superfeed));
                    superfeed.clear();
                }
            }

            if (superfeed.size() > 0) {
                transactionRunnerCms2.runWithTran(() -> insertSuperFeed(superfeed));
                superfeed.clear();
            }
            reader.close();
            $info("Bhfo产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("Bhfo产品文件读入不存在");
        } catch (Exception ex) {
            $info("Bhfo产品文件读入失败");
            logIssue("cms 数据导入处理", "Bhfo产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return BHFO_MINIMALL;
    }

    /**
     * 生成类目数据包含model product数据
     */
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String category) {

        Map colums = getColumns();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = new ArrayList<>();
        for (FeedBean feedConfig : feedBeans) {
            if (!StringUtil.isEmpty(feedConfig.getCfg_val1())) {
                attList.add(feedConfig.getCfg_val1());
            }
        }

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, colums.get("category").toString(),
                category.replace("'", "\\\'"));

        colums.put("keyword", where);
        colums.put("tableName", table);
        if(attList.size()>0){
            colums.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoBhfoModel> vtmModelBeans = bhfoFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoBhfoModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if(temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel();
            cmsBtFeedInfoModel.setAttribute(attribute);
            modelBeans.add(cmsBtFeedInfoModel);

        }
        $info("取得 [ %s ] 的 Product 数 %s", category, modelBeans.size());

        return modelBeans;
    }


    @Override
    @Transactional
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            bhfoFeedDao.delFullBySku(itemIds);
            bhfoFeedDao.insertFullBySku(itemIds);
            bhfoFeedDao.updateFlagBySku(itemIds);
        }
    }

    @Override
    protected void zzWorkClear() {
        bhfoFeedDao.delete();
    }
}
