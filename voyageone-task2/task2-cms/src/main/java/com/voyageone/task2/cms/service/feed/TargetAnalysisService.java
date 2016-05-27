package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.base.exception.BusinessException;
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
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.cms.bean.SuperFeedTargetBean;
import com.voyageone.task2.cms.dao.feed.TargetFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoTargetModel;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
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
    private Map<String, Map<String, String>> retailPriceList;

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

        int i = 1;
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


        $info("Target产品白名单读入开始");
        Map<String, SuperFeedTargetBean> retail = getRetailPriceList();
        if(retail == null || retail.isEmpty()) return 0;
        setMadeInCountry(retail);


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
                int i = 0;
                String sku = reader.get(i++);
                if (retail.isEmpty()) break;
                if(!retail.containsKey(sku)) continue;
                SuperFeedTargetBean superFeedTargetBean = retail.get(sku);
                superFeedTargetBean.setParentSku(reader.get(i++));
                superFeedTargetBean.setManufacturerName(reader.get(i++));
                superFeedTargetBean.setMpn(reader.get(i++));
                superFeedTargetBean.setModelNumber(reader.get(i++));
                superFeedTargetBean.setName(StringEscapeUtils.unescapeHtml(reader.get(i++).replaceAll("&amp;", "&")));
                superFeedTargetBean.setDescription(StringEscapeUtils.unescapeHtml(reader.get(i++).replaceAll("&amp;", "&")));
                superFeedTargetBean.setShortDescription(reader.get(i++));
                superFeedTargetBean.setRegularPrice(reader.get(i++));
                superFeedTargetBean.setSalePrice(reader.get(i++));
                superFeedTargetBean.setMap(reader.get(i++));
                superFeedTargetBean.setBuyUrl(reader.get(i++));
                superFeedTargetBean.setCategory(reader.get(i++));
                superFeedTargetBean.setAvailability(reader.get(i++));
                superFeedTargetBean.setLargeImageUrl(reader.get(i++));
                superFeedTargetBean.setSwatchImage(reader.get(i++));
                superFeedTargetBean.setBrand(reader.get(i++));
                superFeedTargetBean.setUpc(reader.get(i++));
                superFeedTargetBean.setIsbn(reader.get(i++));
                superFeedTargetBean.setCurrency(reader.get(i++));
                superFeedTargetBean.setKeywords(reader.get(i++));
                superFeedTargetBean.setSpecifications(reader.get(i++));
                superFeedTargetBean.setShippingWeight(reader.get(i++));
                superFeedTargetBean.setVariationThemes(reader.get(i++));
                superFeedTargetBean.setAttributeNames(reader.get(i++));
                superFeedTargetBean.setAttributeValues(reader.get(i++));
                superFeedTargetBean.setMargin(reader.get(i++));
                superFeedTargetBean.setMarginPercent(reader.get(i++));
                superFeedTargetBean.setCatentryid(reader.get(i++));
                superFeedTargetBean.setWebclass(reader.get(i++));
                superFeedTargetBean.setSubclass(reader.get(i++));
                superFeedTargetBean.setMozartVendorId(reader.get(i++));
                superFeedTargetBean.setMozartVendorName(reader.get(i++));
                superFeedTargetBean.setDpci(reader.get(i++));
                superFeedTargetBean.setDivision(reader.get(i++));
                superFeedTargetBean.setSellingChannel(reader.get(i++));
                superFeedTargetBean.setItemStatus(reader.get(i++));
                superFeedTargetBean.setItemKind(reader.get(i++));
                superFeedTargetBean.setStreetdate(reader.get(i++));
                superFeedTargetBean.setLaunchdate(reader.get(i++));
                superFeedTargetBean.setAvailabilitydate(reader.get(i++));
                superFeedTargetBean.setAverageOverallRating(reader.get(i++));
                superFeedTargetBean.setTotalItemReviews(reader.get(i++));
                superFeedTargetBean.setRatableAttribute(reader.get(i++));
                superFeedTargetBean.setWebclassName(reader.get(i++));
                superFeedTargetBean.setSubclassName(reader.get(i++));
                superFeedTargetBean.setManufacturingBrand(reader.get(i++));
                superFeedTargetBean.setContributorType(reader.get(i++));
                superFeedTargetBean.setContributor(reader.get(i++));
                superFeedTargetBean.setMediaFormat(reader.get(i++));
                superFeedTargetBean.setBarcodeType(reader.get(i++));
                superFeedTargetBean.setSpecialitemsource(reader.get(i++));
                superFeedTargetBean.setEsrbagerating(reader.get(i++));
                superFeedTargetBean.setTvrating(reader.get(i++));
                superFeedTargetBean.setBopoFlag(reader.get(i++));
                superFeedTargetBean.setSoiPriceDisplay(reader.get(i++));
                superFeedTargetBean.setPricecode(reader.get(i++));
                superFeedTargetBean.setListprice(reader.get(i++));
                superFeedTargetBean.setListPriceRange(reader.get(i++));
                superFeedTargetBean.setSalePriceRange(reader.get(i++));
                superFeedTargetBean.setSalesCategory(reader.get(i++));
                superFeedTargetBean.setLargeImageAlternate(reader.get(i++));
                superFeedTargetBean.setSizingChart(reader.get(i++));
                superFeedTargetBean.setSizeChart(reader.get(i++));
                superFeedTargetBean.setShippingService(reader.get(i++));
                superFeedTargetBean.setPackageLength(reader.get(i++));
                superFeedTargetBean.setPackageWidth(reader.get(i++));
                superFeedTargetBean.setPackageHeight(reader.get(i++));
                superFeedTargetBean.setTaxCategory(reader.get(i++));
                superFeedTargetBean.setFacility(reader.get(i++));
                superFeedTargetBean.setBulky(reader.get(i++));
                superFeedTargetBean.setExpertReviewFlag(reader.get(i++));
                superFeedTargetBean.setThirdpartyhostedlink(reader.get(i++));
                superFeedTargetBean.setPickupinstore(reader.get(i++));
                superFeedTargetBean.setSubscription(reader.get(i++));
                superFeedTargetBean.setSecattributes(reader.get(i++));
                superFeedTargetBean.setSecBarcode(reader.get(i++));
                superFeedTargetBean.setAutoBullets(StringEscapeUtils.unescapeHtml(reader.get(i++).replaceAll("&amp;", "&")));
                superFeedTargetBean.setIacAttributes(reader.get(i++));
                superFeedTargetBean.setCategoryIacid(reader.get(i++));
                superFeedTargetBean.setCalloutMsg(reader.get(i++));
                superFeedTargetBean.setBuyable(reader.get(i++));
                superFeedTargetBean.setBackorderType(reader.get(i++));
                superFeedTargetBean.setMaxOrderQty(reader.get(i++));
                superFeedTargetBean.setIsHazmat(reader.get(i++));
                superFeedTargetBean.setIsFood(reader.get(i++));
                superFeedTargetBean.setWheneverShippingEligible(reader.get(i++));
                superFeedTargetBean.setShipToRestriction(reader.get(i++));
                superFeedTargetBean.setPoBoxProhibited(reader.get(i++));
                superFeedTargetBean.setNutritionFactsFlag(reader.get(i++));
                superFeedTargetBean.setDrugFactsFlag(reader.get(i++));
                superFeedTargetBean.setEnergyGuideCms(reader.get(i++));
                superFeedTargetBean.setVideoCount(reader.get(i++));
                superFeedTargetBean.setFfPickupinstoreRushdeliveryShiptostoreShipfromstore(reader.get(i++));
                superFeedTargetBean.setSaveStory(reader.get(i++));
                superFeedTargetBean.setGiftWrapable(reader.get(i++));
                superFeedTargetBean.setSignRequired(reader.get(i++));
                superFeedTargetBean.setReturnMethod(reader.get(i++));
                superFeedTargetBean.setDefaultReturnPolicy(reader.get(i++));
                superFeedTargetBean.setNormalReturnPolicy(reader.get(i++));
                superFeedTargetBean.setBestReturnPolicy(reader.get(i++));
                superFeedTargetBean.setDisplayEligibility(reader.get(i++));
                superFeedTargetBean.setAgeRestriction(reader.get(i++));
                superFeedTargetBean.setHasWarranty(reader.get(i++));

                superfeed.add(superFeedTargetBean);
                cnt++;
                retail.remove(sku);
                if (superfeed.size() > 1000) {
//                    insertSuperFeed(superfeed);
                    transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                    superfeed.clear();
                    if (cnt > 5000) break;
                }
            }

            if (superfeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                superfeed.clear();
            }
            reader.close();
            $info("Target产品文件读入完成");
            if(!retail.isEmpty()){
                String temp="";
                for(String key :retail.keySet()){
                    temp += key+"\n";
                }
                issueLog.log("target feed导入",temp+"feed中不存在",ErrorType.BatchJob,SubSystem.CMS);
            }

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
                category.replace("'", "\\\'"));

        colums.put("keyword", where);
        colums.put("tableName", table);
        if (attList.size() > 0) {
            colums.put("attr", attList.stream()
                    .map(s -> "`" + s + "`")
                    .collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoTargetModel> vtmModelBeans = TargetFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoTargetModel vtmModelBean : vtmModelBeans) {

            // 计算重量 单位 统一到LB 1磅(lb)=16盎司(oz) 向上取整
            vtmModelBean.setWeight(weightConvert(vtmModelBean.getWeight()));

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;
                if(key.equalsIgnoreCase("ratableAttribute") || key.equalsIgnoreCase("iacAttributes")){
                    attribute=parseSpecialAttr(temp.get(key).toString(),attribute);
                }else{
                    List<String> values = new ArrayList<>();
                    values.add((String) temp.get(key));
                    attribute.put(key, values);
                }

            }


            // Secattributes属性值解析
            if (!StringUtil.isEmpty(vtmModelBean.getSecattributes())) {
                List<String> keyValue = java.util.Arrays.asList(vtmModelBean.getSecattributes().split("[|]"));
                if (keyValue.size() % 2 != 0) {
                    $error("sku:" + vtmModelBean.getSkus() + "Secattributes属性值错误");
                    continue;
                }
                for (int i = 0; i < keyValue.size(); i++) {
                    if (!StringUtil.isEmpty(keyValue.get(i))) {
                        List<String> v = new ArrayList<>();
                        if (!StringUtil.isEmpty(keyValue.get(i + 1))) {
                            attribute.put(keyValue.get(i), v);
                            v.add(keyValue.get(i + 1));
                        }
                    }
                    i++;
                }
            }

            //AttributeValues属性解析
            if (!StringUtil.isEmpty(vtmModelBean.getAttributeNames())) {
                List<String> names = java.util.Arrays.asList(vtmModelBean.getAttributeNames().split("[|]"));
                List<String> values = java.util.Arrays.asList(vtmModelBean.getAttributeValues().split("[|]"));
                if (names.size() != values.size()) {
                    $error("sku:" + vtmModelBean.getSkus() + "属性值错误");
                    continue;
                }


                for (int i = 0; i < names.size(); i++) {
                    if (!StringUtil.isEmpty(values.get(i))) {
                        List<String> v = new ArrayList<>();
                        v.add(values.get(i));
                        attribute.put(names.get(i), v);
                    }
                }
            }


            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);

            if (attribute.get("gender") != null) {
                cmsBtFeedInfoModel.setSizeType(attribute.get("gender").get(0));
            } else if (attribute.get("age_group") != null) {
                cmsBtFeedInfoModel.setSizeType(attribute.get("age_group").get(0));
            } else {
                cmsBtFeedInfoModel.setSizeType("OneSize");
            }

            // size
            if (attribute.get("size") != null) {
                cmsBtFeedInfoModel.getSkus().get(0).setSize(attribute.get("size").get(0));
            } else {
                cmsBtFeedInfoModel.getSkus().get(0).setSize(("OneSize"));
            }

            // color
            if (vtmModelBean.getVariationThemes() != null && vtmModelBean.getVariationThemes().toUpperCase().indexOf("VARIATION") >= 0) {
                String secat[] = vtmModelBean.getSecattributes().split("[|]");
                if (!StringUtil.isEmpty(secat[secat.length - 1])) {
                    cmsBtFeedInfoModel.setColor(secat[secat.length - 1].replaceAll(" ", "").toUpperCase());
                    cmsBtFeedInfoModel.setCode(cmsBtFeedInfoModel.getCode() + "-" + cmsBtFeedInfoModel.getColor());
                }
            } else {
                if (attribute.get("color") != null) {
                    cmsBtFeedInfoModel.setColor(attribute.get("color").get(0).replaceAll(" ", "").toUpperCase());
                    cmsBtFeedInfoModel.setCode(cmsBtFeedInfoModel.getCode() + "-" + cmsBtFeedInfoModel.getColor());
                }
            }


            // productType
            cmsBtFeedInfoModel.setCategory(StringEscapeUtils.escapeHtml(cmsBtFeedInfoModel.getCategory()));
            List<String> categorys = Arrays.asList(cmsBtFeedInfoModel.getCategory().split(Feeds.getVal1(getChannel().getId(), FeedEnums.Name.category_split)));
            if (categorys.size() >= 2) {
                cmsBtFeedInfoModel.setProductType(categorys.get(0) + ">" + categorys.get(1));
            } else if (categorys.size() > 0) {
                cmsBtFeedInfoModel.setProductType(categorys.get(0));
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

    public Map<String, SuperFeedTargetBean> getRetailPriceList() {

        Map<String, SuperFeedTargetBean> retailPriceList = new HashMap<>();
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
                SuperFeedTargetBean superFeedTargetBean = new SuperFeedTargetBean();
                int i = 0;
                superFeedTargetBean.setSku(reader.get(i++));
                superFeedTargetBean.setMarketprice(reader.get(i++));
                retailPriceList.put(superFeedTargetBean.getSku(),superFeedTargetBean);
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

    public void setMadeInCountry(Map<String, SuperFeedTargetBean> superFeedTargetBean){

        String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id_import_made_in_country);
        String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
        String fileFullName = String.format("%s/%s", filePath, fileName);
        FileReader fr = null;
        try {
            fr=new FileReader(fileFullName);
            BufferedReader br=new BufferedReader(fr);
            String json = br.readLine();
            fr.close();
            Map<String,Object> madeInCountry = JacksonUtil.jsonToMap(json);

            for(String key: superFeedTargetBean.keySet()){
                if(madeInCountry.get(key) != null){
                    List<String> country = (List<String>) madeInCountry.get(key);
                    superFeedTargetBean.get(key).setMadeInCountry(country.stream().collect(Collectors.joining(",")));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fr != null) try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String weightConvert(String weight){
        String temp[] = weight.trim().split(" ");
        if(temp.length > 1){
            if ("oz".equalsIgnoreCase(temp[1])){

                Integer convertWeight = (int) Math.ceil(Double.parseDouble(temp[0]) / 16.0);
                return convertWeight.toString();
            }else if("lb".equalsIgnoreCase(temp[1])){
                 Integer convertWeight = (int) Math.ceil(Double.parseDouble(temp[0]));;
                return convertWeight.toString();
            }else{
                throw new BusinessException("重量转换失败：" + weight);
            }
        }
        return "";
    }

    private Map<String, List<String>> parseSpecialAttr(String attrString,Map<String, List<String>> attribute){
        if(StringUtil.isEmpty(attrString)) return attribute;
        List<String> keyValue = new ArrayList<>();
        java.util.Arrays.asList(attrString.split("[|]")).forEach(s -> keyValue.addAll(Arrays.asList(s.split("[~]"))));
        if (keyValue.size() % 2 != 0) {
            $error("属性值错误："+ attrString);
            return  attribute;
        }
        for (int i = 0; i < keyValue.size(); i++) {
            if (!StringUtil.isEmpty(keyValue.get(i))) {
                List<String> v = new ArrayList<>();
                if (!StringUtil.isEmpty(keyValue.get(i + 1))) {
                    attribute.put(keyValue.get(i), v);
                    v.add(keyValue.get(i + 1));
                }
            }
            i++;
        }
        return attribute;
    }

    @Override
    protected boolean backupFeedFile(String channelId){
        super.backupFeedFile(channelId);
        return backupFeedFile(channelId,FeedEnums.Name.file_id_import_sku);
    }
}
