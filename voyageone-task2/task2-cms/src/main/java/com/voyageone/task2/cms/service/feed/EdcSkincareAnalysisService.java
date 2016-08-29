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
import com.voyageone.task2.cms.bean.SuperFeedEdcSkincareBean;
import com.voyageone.task2.cms.dao.feed.EdcSkincareFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoEdcSkincareModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.EDCSKINCARE;

/**
 * Created by gjl on 2016/7/12.
 */
@Service
public class EdcSkincareAnalysisService extends BaseAnalysisService {
    @Autowired
    EdcSkincareFeedDao edcSkincareFeedDao;
    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                edcSkincareFeedDao.delFullBySku(strings);
                edcSkincareFeedDao.insertFullBySku(strings);
                edcSkincareFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        edcSkincareFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("EdcSkincare产品文件读入开始");
        List<SuperFeedEdcSkincareBean> superfeed = new ArrayList<>();
        int cnt = 0;
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
                SuperFeedEdcSkincareBean edcSkincareBean = new SuperFeedEdcSkincareBean();
                int i = 0;
                edcSkincareBean.setSku(reader.get(i++));
                edcSkincareBean.setParentid(reader.get(i++));
                edcSkincareBean.setTitle(reader.get(i++));
                edcSkincareBean.setProductid(reader.get(i++));
                edcSkincareBean.setPrice(reader.get(i++));
                edcSkincareBean.setMsrp(reader.get(i++));
                edcSkincareBean.setQuantity(reader.get(i++));
                edcSkincareBean.setImages(reader.get(i++));
                edcSkincareBean.setDescription(reader.get(i++));
                edcSkincareBean.setShortdescription(reader.get(i++));
                edcSkincareBean.setProductorigin(reader.get(i++));
                edcSkincareBean.setCategory(reader.get(i++));
                edcSkincareBean.setWeight(reader.get(i++));
                edcSkincareBean.setBrand(reader.get(i++));
                edcSkincareBean.setAttributekey1(reader.get(i++));
                edcSkincareBean.setAttributevalue1(reader.get(i++));
                edcSkincareBean.setAttributekey2(reader.get(i++));
                edcSkincareBean.setAttributevalue2(reader.get(i++));
                edcSkincareBean.setAttributekey3(reader.get(i++));
                edcSkincareBean.setAttributevalue3(reader.get(i++));
                edcSkincareBean.setAttributekey4(reader.get(i++));
                edcSkincareBean.setAttributevalue4(reader.get(i++));
                edcSkincareBean.setAttributekey5(reader.get(i++));
                edcSkincareBean.setAttributevalue5(reader.get(i++));
                edcSkincareBean.setAttributekey6(reader.get(i++));
                edcSkincareBean.setAttributevalue6(reader.get(i++));
                edcSkincareBean.setAttributekey7(reader.get(i++));
                edcSkincareBean.setAttributevalue7(reader.get(i++));
                edcSkincareBean.setAttributekey8(reader.get(i++));
                edcSkincareBean.setAttributevalue8(reader.get(i++));
                edcSkincareBean.setAttributekey9(reader.get(i++));
                edcSkincareBean.setAttributevalue9(reader.get(i++));
                edcSkincareBean.setAttributekey10(reader.get(i++));
                edcSkincareBean.setAttributevalue10(reader.get(i++));
                edcSkincareBean.setAttributekey11(reader.get(i++));
                edcSkincareBean.setAttributevalue11(reader.get(i++));
                edcSkincareBean.setAttributekey12(reader.get(i++));
                edcSkincareBean.setAttributevalue12(reader.get(i++));
                edcSkincareBean.setAttributekey13(reader.get(i++));
                edcSkincareBean.setAttributevalue13(reader.get(i++));
                edcSkincareBean.setAttributekey14(reader.get(i++));
                edcSkincareBean.setAttributevalue14(reader.get(i++));
                edcSkincareBean.setAttributekey15(reader.get(i++));
                edcSkincareBean.setAttributevalue15(reader.get(i++));
                edcSkincareBean.setAttributekey16(reader.get(i++));
                edcSkincareBean.setAttributevalue16(reader.get(i++));
                edcSkincareBean.setAttributekey17(reader.get(i++));
                edcSkincareBean.setAttributevalue17(reader.get(i++));
                edcSkincareBean.setAttributekey18(reader.get(i++));
                edcSkincareBean.setAttributevalue18(reader.get(i++));
                edcSkincareBean.setAttributekey19(reader.get(i++));
                edcSkincareBean.setAttributevalue19(reader.get(i++));
                edcSkincareBean.setAttributekey20(reader.get(i++));
                edcSkincareBean.setAttributevalue20(reader.get(i++));
                superfeed.add(edcSkincareBean);
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
            $info("EdcSkincare产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("EdcSkincare产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("EdcSkincare产品文件读入失败");
            logIssue("cms 数据导入处理", "EdcSkincare产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedEdcSkincareBean> superfeedlist) {

        for (SuperFeedEdcSkincareBean superfeed : superfeedlist) {

            if (edcSkincareFeedDao.insertSelective(superfeed) <= 0) {
                $info("EdcSkincare产品信息插入失败 Sku = " + superfeed.getSku());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return EDCSKINCARE;
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

        List<CmsBtFeedInfoEdcSkincareModel> vtmModelBeans = edcSkincareFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoEdcSkincareModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //设置重量
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
            if(codeMap.containsKey(cmsBtFeedInfoModel.getCode())){
                CmsBtFeedInfoModel beforeFeed =  codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setImage(beforeFeed.getImage().stream().distinct().collect(Collectors.toList()));
                beforeFeed.setAttribute(attributeMerge(beforeFeed.getAttribute(),cmsBtFeedInfoModel.getAttribute()));
            }else{
                modelBeans.add(cmsBtFeedInfoModel);
                codeMap.put(cmsBtFeedInfoModel.getCode(),cmsBtFeedInfoModel);
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
        return "CmsEdcSkincareAnalysisJob";
    }
}
