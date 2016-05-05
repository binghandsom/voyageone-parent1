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
import com.voyageone.task2.cms.bean.SuperfeedSummerGuruBean;
import com.voyageone.task2.cms.dao.feed.SummerGuruFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoSummerGuruModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SUMMERGURU;

/**
 * @author james.li on 2016/4/25.
 * @version 2.0.0
 */
@Service
public class SummerGuruAnalysisService extends BaseAnalysisService {

    @Autowired
    SummerGuruFeedDao summerGuruFeedDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSummerGuruAnalySisJob";
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperfeedSummerGuruBean> superfeedlist) {

        for (SuperfeedSummerGuruBean superfeed : superfeedlist) {

            if (summerGuruFeedDao.insertSelective(superfeed) <= 0) {
                $info("SummerGuru产品信息插入失败 InventoryNumber = " + superfeed.getSku());
            }
        }
        return true;
    }

    /**
     * SummerGuru产品文件读入
     */
    public int superFeedImport() {


//        $info("SummerGuru产品品牌黑名单读入开始");
//        Set<String> blackList = new HashSet<>();
//        Feeds.getConfigs(getChannel().getId(),FeedEnums.Name.blackList).forEach(m -> blackList.add(m.getCfg_val1().toLowerCase().trim()));


        $info("SummerGuru产品文件读入开始");

        List<SuperfeedSummerGuruBean> superfeed = new ArrayList<>();
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
                SuperfeedSummerGuruBean SuperfeedSummerGuruBean = new SuperfeedSummerGuruBean();

                int i = 0;

                SuperfeedSummerGuruBean.setSku(reader.get(i++));
                SuperfeedSummerGuruBean.setTitle(reader.get(i++));
                SuperfeedSummerGuruBean.setDescription(reader.get(i++));
                SuperfeedSummerGuruBean.setFeatureBullets(reader.get(i++));
                SuperfeedSummerGuruBean.setPrice(reader.get(i++));
                SuperfeedSummerGuruBean.setMsrp(reader.get(i++));
                SuperfeedSummerGuruBean.setSize(reader.get(i++));
                SuperfeedSummerGuruBean.setColor(reader.get(i++));
                SuperfeedSummerGuruBean.setCountryOfOrigin(reader.get(i++));
                SuperfeedSummerGuruBean.setProductId(reader.get(i++));
                SuperfeedSummerGuruBean.setWeight(reader.get(i++));
                SuperfeedSummerGuruBean.setMaterial(reader.get(i++));
                SuperfeedSummerGuruBean.setImages(reader.get(i++));
                SuperfeedSummerGuruBean.setBodyMeasurements(reader.get(i++));
                SuperfeedSummerGuruBean.setRelationshipName(reader.get(i++));
                SuperfeedSummerGuruBean.setParentId(reader.get(i++));
                SuperfeedSummerGuruBean.setVoyageonePrice(reader.get(i++));
                SuperfeedSummerGuruBean.setShortDescription(reader.get(i++));
                SuperfeedSummerGuruBean.setCategory(reader.get(i++));
                SuperfeedSummerGuruBean.setSizeType(reader.get(i++));
                SuperfeedSummerGuruBean.setCondition(reader.get(i++));
                SuperfeedSummerGuruBean.setConditionNotes(reader.get(i++));
                SuperfeedSummerGuruBean.setBrand(reader.get(i++));

                if (StringUtil.isEmpty(SuperfeedSummerGuruBean.getCategory()) || StringUtil.isEmpty(SuperfeedSummerGuruBean.getTitle())) {
                    continue;
                }
                superfeed.add(SuperfeedSummerGuruBean);
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
            $info("SummerGuru产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("SummerGuru产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("SummerGuru产品文件读入失败");
            logIssue("cms 数据导入处理", "SummerGuru产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return SUMMERGURU;
    }

    /**
     * 生成类目数据包含model product数据
     */
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String category) {

        Map<String, Object> colums = getColumns();

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
            colums.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoSummerGuruModel> vtmModelBeans = summerGuruFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoSummerGuruModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }
            vtmModelBean.setProductType(getProducType(vtmModelBean));
            vtmModelBean.setSizeType(getSizeType(vtmModelBean));

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel();
            cmsBtFeedInfoModel.setAttribute(attribute);
            modelBeans.add(cmsBtFeedInfoModel);

        }
        $info("取得 [ %s ] 的 Product 数 %s", category, modelBeans.size());

        return modelBeans;
    }

    private String getSizeType(CmsBtFeedInfoSummerGuruModel cmsBtFeedInfoSummerGuru) {
        String title = cmsBtFeedInfoSummerGuru.getName();
        String keys[] = {"Women's", "Baby Boy's", "Baby Girl's", "Men's",};
        for (String key : keys) {
            if (title.contains(key)) return key;
        }
        return "One Size";
    }

    // productType 是类目的第三级
    private String getProducType(CmsBtFeedInfoSummerGuruModel cmsBtFeedInfoSummerGuru) {
        String categorySplit = Feeds.getVal1(channel, FeedEnums.Name.category_split);

        String split[] = cmsBtFeedInfoSummerGuru.getCategory().split(categorySplit);
        if (split.length >= 3) {
            return split[2];
        } else {
            return split[split.length - 1];
        }
    }

    @Override
    @Transactional
    protected void updateFull(List<String> itemIds) {

        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                summerGuruFeedDao.delFullBySku(strings);
                summerGuruFeedDao.insertFullBySku(strings);
                summerGuruFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        summerGuruFeedDao.delete();
    }
}
