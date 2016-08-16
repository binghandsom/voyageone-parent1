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
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedYogaDemocracyBean;
import com.voyageone.task2.cms.dao.feed.YogaDemocracyFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoYogaDemocracyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.Yogademocracy;

/**
 * Created by gjl on 2016/7/27.
 */
@Service
public class YogaDemocracyAnalysisService extends BaseAnalysisService {
    @Autowired
    YogaDemocracyFeedDao yogaDemocracyFeedDao;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                yogaDemocracyFeedDao.delFullBySku(strings);
                yogaDemocracyFeedDao.insertFullBySku(strings);
                yogaDemocracyFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        yogaDemocracyFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("YogaDemocracy产品文件读入开始");
        List<SuperFeedYogaDemocracyBean> superfeed = new ArrayList<>();
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
                SuperFeedYogaDemocracyBean superFeedYogaDemocracyBean = new SuperFeedYogaDemocracyBean();
                int i = 0;
                superFeedYogaDemocracyBean.setSku(reader.get(i++));
                superFeedYogaDemocracyBean.setParentid(reader.get(i++));
                superFeedYogaDemocracyBean.setRelationshiptype(reader.get(i++));
                superFeedYogaDemocracyBean.setVariationtheme(reader.get(i++));
                superFeedYogaDemocracyBean.setTitle(reader.get(i++));
                superFeedYogaDemocracyBean.setProductid(reader.get(i++));
                superFeedYogaDemocracyBean.setPrice(reader.get(i++).replace("$", ""));
                superFeedYogaDemocracyBean.setMsrp(reader.get(i++).replace("$", ""));
                superFeedYogaDemocracyBean.setQuantity(reader.get(i++));
                superFeedYogaDemocracyBean.setImages(reader.get(i++));
                superFeedYogaDemocracyBean.setDescription(reader.get(i++));
                superFeedYogaDemocracyBean.setShortdiscription(reader.get(i++));
                superFeedYogaDemocracyBean.setProductorigin(reader.get(i++));
                superFeedYogaDemocracyBean.setCategory(reader.get(i++));
                superFeedYogaDemocracyBean.setWeight(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey1(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue1(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey2(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue2(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey3(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue3(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey4(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue4(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey5(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue5(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey6(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue6(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey7(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue7(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey8(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue8(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey9(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue9(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey10(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue10(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey11(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue11(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey12(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue12(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey13(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue13(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey14(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue14(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributekey15(reader.get(i++));
                superFeedYogaDemocracyBean.setAttributevalue15(reader.get(i++));
                superfeed.add(superFeedYogaDemocracyBean);
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
            $info("YogaDemocracy产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("YogaDemocracy产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("YogaDemocracy产品文件读入失败");
            logIssue("cms 数据导入处理", "YogaDemocracy产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    /**
     * YogaDemocracy产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedYogaDemocracyBean> superfeedlist) {

        for (SuperFeedYogaDemocracyBean superfeed : superfeedlist) {

            if (yogaDemocracyFeedDao.insertSelective(superfeed) <= 0) {
                $info("YogaDemocracy产品信息插入失败 sku = " + superfeed.getSku());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return Yogademocracy;
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

        List<CmsBtFeedInfoYogaDemocracyModel> vtmModelBeans = yogaDemocracyFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        HashMap<String,Object> parentIdMap = new HashMap<>();
        for (CmsBtFeedInfoYogaDemocracyModel vtmModelBean : vtmModelBeans) {
            if(StringUtil.isEmpty(vtmModelBean.getParentid())){
                parentIdMap.put(vtmModelBean.getSku(), vtmModelBean);
            };
        }
        for (CmsBtFeedInfoYogaDemocracyModel vtmModelBean : vtmModelBeans) {
            //父级数据跳过
            if(StringUtil.isEmpty(vtmModelBean.getParentid()))continue;
            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;
                if (key.contains("attribute")) continue;
                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }
            for (int i = 0; i < 15; i++) {
                String value = (String) temp.get("attributevalue" + i);
                if (!StringUtil.isEmpty(value)) {
                    List<String> values = new ArrayList<>();
                    values.add(value);
                    attribute.put(temp.get("attributekey" + i).toString(), values);
                }
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //根据父级数据取得相应的属性
            if(parentIdMap.keySet().contains(String.valueOf(vtmModelBean.getParentid()))){
                CmsBtFeedInfoYogaDemocracyModel YogaBean = (CmsBtFeedInfoYogaDemocracyModel) parentIdMap.get(vtmModelBean.getParentid());
                List<String> imagesList = new ArrayList<>();
                for(String image:YogaBean.getImage()){
                    List<String> imageList = Arrays.asList(image.split(","));
                    imagesList.addAll(imageList);
                }
                cmsBtFeedInfoModel.setImage(imagesList);
                cmsBtFeedInfoModel.setLongDescription(YogaBean.getDescription());
                cmsBtFeedInfoModel.setShortDescription(YogaBean.getShortDescription());
            }

            List<CmsBtFeedInfoModel_Sku> skus = vtmModelBean.getSkus();
            for (CmsBtFeedInfoModel_Sku sku : skus) {
                String Weight = sku.getWeightOrg().trim();
                Pattern pattern = Pattern.compile("[^0-9.]");
                Matcher matcher = pattern.matcher(Weight);
                if (matcher.find()) {
                    int index = Weight.indexOf(matcher.group());
                    if (index != -1) {
                        String weightOrg = Weight.substring(0, index);
                        String weightOrgUnit = Weight.substring(index, Weight.length());
                        sku.setWeightOrg(weightOrg);
                        sku.setWeightOrgUnit(weightOrgUnit);
                        sku.setImage(cmsBtFeedInfoModel.getImage());
                    }
                }

            }
            cmsBtFeedInfoModel.setSkus(skus);

            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
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
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsYogaDemocracyAnalysisJob";
    }
}
