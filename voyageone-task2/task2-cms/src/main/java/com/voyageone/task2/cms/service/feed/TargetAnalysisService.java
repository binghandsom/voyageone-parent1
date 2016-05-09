package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.cms.bean.SuperFeedTargetBean;
import com.voyageone.task2.cms.dao.feed.TargetFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoTargetModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.TARGET;

/**
 * @author james.li on 2016/4/25.
 * @version 2.0.0
 */
@Service
public class TargetAnalysisService extends BaseAnalysisService {

    @Autowired
    TargetFeedDao TargetFeedDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsTargetAnalySisJob";
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedTargetBean> superfeedlist) {
        boolean isSuccess = true;

        int i=1;
        for (SuperFeedTargetBean superfeed : superfeedlist) {
            if (TargetFeedDao.insertSelective(superfeed) <= 0) {
                $info("Target产品信息插入失败 InventoryNumber = " + superfeed.getSku());
            }
        }
        return isSuccess;
    }

    /**
     * Target产品文件读入
     */
    public int superFeedImport() {


        $info("Target产品品牌黑名单读入开始");
        Set<String> blackList = new HashSet<>();
        Feeds.getConfigs(getChannel().getId(),FeedEnums.Name.blackList).forEach(m -> blackList.add(m.getCfg_val1().toLowerCase().trim()));


        $info("Target产品文件读入开始");

        List<SuperFeedTargetBean> superfeed = new ArrayList<>();
        int cnt = 0;

        CsvReader reader;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);

            String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);

            reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));

            // Head读入
            reader.readHeaders();
            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                SuperFeedTargetBean SuperFeedTargetBean = new SuperFeedTargetBean();

                int i = 0;

                SuperFeedTargetBean.setSku(reader.get(i++));
                SuperFeedTargetBean.setParentSku(reader.get(i++));
                SuperFeedTargetBean.setManufacturerName(reader.get(i++));
                SuperFeedTargetBean.setMpn(reader.get(i++));
                SuperFeedTargetBean.setModelNumber(reader.get(i++));
                SuperFeedTargetBean.setName(reader.get(i++));
                SuperFeedTargetBean.setDescription(reader.get(i++));
                SuperFeedTargetBean.setShortDescription(reader.get(i++));
                SuperFeedTargetBean.setRegularPrice(reader.get(i++));
                SuperFeedTargetBean.setSalePrice(reader.get(i++));
                SuperFeedTargetBean.setMap(reader.get(i++));
                SuperFeedTargetBean.setBuyUrl(reader.get(i++));
                SuperFeedTargetBean.setCategory(reader.get(i++));
                SuperFeedTargetBean.setAvailability(reader.get(i++));
                SuperFeedTargetBean.setLargeImageUrl(reader.get(i++));
                SuperFeedTargetBean.setSwatchImage(reader.get(i++));
                SuperFeedTargetBean.setBrand(reader.get(i++));
                SuperFeedTargetBean.setUpc(reader.get(i++));
                SuperFeedTargetBean.setIsbn(reader.get(i++));
                SuperFeedTargetBean.setCurrency(reader.get(i++));
                SuperFeedTargetBean.setKeywords(reader.get(i++));
                SuperFeedTargetBean.setSpecifications(reader.get(i++));
                SuperFeedTargetBean.setShippingWeight(reader.get(i++));
                SuperFeedTargetBean.setVariationThemes(reader.get(i++));
                SuperFeedTargetBean.setAttributeNames(reader.get(i++));
                SuperFeedTargetBean.setAttributeValues(reader.get(i++));
                SuperFeedTargetBean.setMargin(reader.get(i++));
                SuperFeedTargetBean.setMarginPercent(reader.get(i++));
                SuperFeedTargetBean.setCatentryid(reader.get(i++));
                SuperFeedTargetBean.setWebclass(reader.get(i++));
                SuperFeedTargetBean.setSubclass(reader.get(i++));
                SuperFeedTargetBean.setMozartVendorId(reader.get(i++));
                SuperFeedTargetBean.setMozartVendorName(reader.get(i++));
                SuperFeedTargetBean.setDpci(reader.get(i++));
                SuperFeedTargetBean.setDivision(reader.get(i++));
                SuperFeedTargetBean.setSellingChannel(reader.get(i++));
                SuperFeedTargetBean.setItemStatus(reader.get(i++));
                SuperFeedTargetBean.setItemKind(reader.get(i++));
                SuperFeedTargetBean.setStreetdate(reader.get(i++));
                SuperFeedTargetBean.setLaunchdate(reader.get(i++));
                SuperFeedTargetBean.setAvailabilitydate(reader.get(i++));
                SuperFeedTargetBean.setAverageOverallRating(reader.get(i++));
                SuperFeedTargetBean.setTotalItemReviews(reader.get(i++));
                SuperFeedTargetBean.setRatableAttribute(reader.get(i++));
                SuperFeedTargetBean.setWebclassName(reader.get(i++));
                SuperFeedTargetBean.setSubclassName(reader.get(i++));
                SuperFeedTargetBean.setManufacturingBrand(reader.get(i++));
                SuperFeedTargetBean.setContributorType(reader.get(i++));
                SuperFeedTargetBean.setContributor(reader.get(i++));
                SuperFeedTargetBean.setMediaFormat(reader.get(i++));
                SuperFeedTargetBean.setBarcodeType(reader.get(i++));
                SuperFeedTargetBean.setSpecialitemsource(reader.get(i++));
                SuperFeedTargetBean.setEsrbagerating(reader.get(i++));
                SuperFeedTargetBean.setTvrating(reader.get(i++));
                SuperFeedTargetBean.setBopoFlag(reader.get(i++));
                SuperFeedTargetBean.setSoiPriceDisplay(reader.get(i++));
                SuperFeedTargetBean.setPricecode(reader.get(i++));
                SuperFeedTargetBean.setListprice(reader.get(i++));
                SuperFeedTargetBean.setListPriceRange(reader.get(i++));
                SuperFeedTargetBean.setSalePriceRange(reader.get(i++));
                SuperFeedTargetBean.setSalesCategory(reader.get(i++));
                SuperFeedTargetBean.setLargeImageAlternate(reader.get(i++));
                SuperFeedTargetBean.setSizingChart(reader.get(i++));
                SuperFeedTargetBean.setSizeChart(reader.get(i++));
                SuperFeedTargetBean.setShippingService(reader.get(i++));
                SuperFeedTargetBean.setPackageLength(reader.get(i++));
                SuperFeedTargetBean.setPackageWidth(reader.get(i++));
                SuperFeedTargetBean.setPackageHeight(reader.get(i++));
                SuperFeedTargetBean.setTaxCategory(reader.get(i++));
                SuperFeedTargetBean.setFacility(reader.get(i++));
                SuperFeedTargetBean.setBulky(reader.get(i++));
                SuperFeedTargetBean.setExpertReviewFlag(reader.get(i++));
                SuperFeedTargetBean.setThirdpartyhostedlink(reader.get(i++));
                SuperFeedTargetBean.setPickupinstore(reader.get(i++));
                SuperFeedTargetBean.setSubscription(reader.get(i++));
                SuperFeedTargetBean.setSecattributes(reader.get(i++));
                SuperFeedTargetBean.setSecBarcode(reader.get(i++));
                SuperFeedTargetBean.setAutoBullets(reader.get(i++));
                SuperFeedTargetBean.setIacAttributes(reader.get(i++));
                SuperFeedTargetBean.setCategoryIacid(reader.get(i++));
                SuperFeedTargetBean.setCalloutMsg(reader.get(i++));
                SuperFeedTargetBean.setBuyable(reader.get(i++));
                SuperFeedTargetBean.setBackorderType(reader.get(i++));
                SuperFeedTargetBean.setMaxOrderQty(reader.get(i++));
                SuperFeedTargetBean.setIsHazmat(reader.get(i++));
                SuperFeedTargetBean.setIsFood(reader.get(i++));
                SuperFeedTargetBean.setWheneverShippingEligible(reader.get(i++));
                SuperFeedTargetBean.setShipToRestriction(reader.get(i++));
                SuperFeedTargetBean.setPoBoxProhibited(reader.get(i++));
                SuperFeedTargetBean.setNutritionFactsFlag(reader.get(i++));
                SuperFeedTargetBean.setDrugFactsFlag(reader.get(i++));
                SuperFeedTargetBean.setEnergyGuideCms(reader.get(i++));
                SuperFeedTargetBean.setVideoCount(reader.get(i++));
                SuperFeedTargetBean.setFfPickupinstoreRushdeliveryShiptostoreShipfromstore(reader.get(i++));
                SuperFeedTargetBean.setSaveStory(reader.get(i++));
                SuperFeedTargetBean.setGiftWrapable(reader.get(i++));
                SuperFeedTargetBean.setSignRequired(reader.get(i++));
                SuperFeedTargetBean.setReturnMethod(reader.get(i++));
                SuperFeedTargetBean.setDefaultReturnPolicy(reader.get(i++));
                SuperFeedTargetBean.setNormalReturnPolicy(reader.get(i++));
                SuperFeedTargetBean.setBestReturnPolicy(reader.get(i++));
                SuperFeedTargetBean.setDisplayEligibility(reader.get(i++));
                SuperFeedTargetBean.setAgeRestriction(reader.get(i++));
                SuperFeedTargetBean.setHasWarranty(reader.get(i++));

                superfeed.add(SuperFeedTargetBean);
                cnt++;
                if (superfeed.size() > 1000) {
//                    insertSuperFeed(superfeed);
                    transactionRunnerCms2.runWithTran(() -> insertSuperFeed(superfeed));
                    superfeed.clear();
                    break;
                }
            }

            if (superfeed.size() > 0) {
                transactionRunnerCms2.runWithTran(() -> insertSuperFeed(superfeed));
                superfeed.clear();
            }
            reader.close();
            $info("Target产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("Target产品文件读入不存在");
        } catch (Exception ex) {
            $info("Target产品文件读入失败");
            ex.printStackTrace();
            logIssue("cms 数据导入处理", "Target产品文件读入失败 " + CommonUtil.getMessages(ex));
        }
        return cnt;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return TARGET;
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

        List<CmsBtFeedInfoTargetModel> vtmModelBeans = TargetFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoTargetModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if(temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
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
            TargetFeedDao.delFullBySku(itemIds);
            TargetFeedDao.insertFullBySku(itemIds);
            TargetFeedDao.updateFlagBySku(itemIds);
        }
    }

    @Override
    protected void zzWorkClear() {
        TargetFeedDao.delete();
    }
}
