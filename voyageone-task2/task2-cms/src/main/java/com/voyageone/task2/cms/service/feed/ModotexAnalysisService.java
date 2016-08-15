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
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedModotexBean;
import com.voyageone.task2.cms.dao.feed.ModotexFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoModotexModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.Modotex;

/**
 * Created by gjl on 2016/8/01.
 */
@Service
public class ModotexAnalysisService extends BaseAnalysisService {
    @Autowired
    ModotexFeedDao modotexFeedDao;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                modotexFeedDao.delFullBySku(strings);
                modotexFeedDao.insertFullBySku(strings);
                modotexFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        modotexFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("Modotex产品文件读入开始");
        List<SuperFeedModotexBean> superfeed = new ArrayList<>();
        int cnt = 0;
        //CsvReader reader;
        BufferedReader br = null;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);
            String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);
            // InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileFullName)));
            // Body读入
            String b;
            br.readLine(); // 跳过第一行
            while ((b = br.readLine()) != null) {
                SuperFeedModotexBean superFeedModotexBean = new SuperFeedModotexBean();
                List<String> reader = new ArrayList(Arrays.asList(b.split(",")));
                int i = 0;
                superFeedModotexBean.setSku(reader.get(i++));
                superFeedModotexBean.setParentid(reader.get(i++));
                superFeedModotexBean.setTitle(reader.get(i++));
                superFeedModotexBean.setProductid(reader.get(i++));
                superFeedModotexBean.setVoprice(reader.get(i++));
                superFeedModotexBean.setMsrp(reader.get(i++));
                superFeedModotexBean.setQuantity(reader.get(i++));
                superFeedModotexBean.setImages(reader.get(i++));
                superFeedModotexBean.setDescription(reader.get(i++));
                superFeedModotexBean.setShortdescription(reader.get(i++));
                superFeedModotexBean.setProductorigin(reader.get(i++));
                superFeedModotexBean.setCategory(reader.get(i++));
                superFeedModotexBean.setWeight(reader.get(i++));
                superFeedModotexBean.setAttributekey1(reader.get(i++));
                superFeedModotexBean.setAttributevalue1(reader.get(i++));
                superFeedModotexBean.setAttributekey2(reader.get(i++));
                superFeedModotexBean.setAttributevalue2(reader.get(i++));
                superFeedModotexBean.setAttributekey3(reader.get(i++));
                superFeedModotexBean.setAttributevalue3(reader.get(i++));
                superFeedModotexBean.setAttributekey4(reader.get(i++));
                superFeedModotexBean.setAttributevalue4(reader.get(i++));
                superFeedModotexBean.setAttributekey5(reader.get(i++));
                superFeedModotexBean.setAttributevalue5(reader.get(i++));
                superFeedModotexBean.setAttributekey6(reader.get(i++));
                superFeedModotexBean.setAttributevalue6(reader.get(i++));
                superFeedModotexBean.setAttributekey7(reader.get(i++));
                superFeedModotexBean.setAttributevalue7(reader.get(i++));
                superFeedModotexBean.setAttributekey8(reader.get(i++));
                superFeedModotexBean.setAttributevalue8(reader.get(i++));
                superFeedModotexBean.setAttributekey9(reader.get(i++));
                superFeedModotexBean.setAttributevalue9(reader.get(i++));
                superFeedModotexBean.setAttributekey10(reader.get(i++));
                superFeedModotexBean.setAttributevalue10(reader.get(i++));
                superFeedModotexBean.setAttributekey11(reader.get(i++));
                superFeedModotexBean.setAttributevalue11(reader.get(i++));
                superFeedModotexBean.setAttributekey12(reader.get(i++));
                superFeedModotexBean.setAttributevalue12(reader.get(i++));
                superFeedModotexBean.setAttributekey13(reader.get(i++));
                superFeedModotexBean.setAttributevalue13(reader.get(i++));
                superFeedModotexBean.setAttributekey14(reader.get(i++));
                superFeedModotexBean.setAttributevalue14(reader.get(i++));
                superFeedModotexBean.setAttributekey15(reader.get(i++));
                superFeedModotexBean.setAttributevalue15(reader.get(i++));
                superFeedModotexBean.setAttributekey16(reader.get(i++));
                superFeedModotexBean.setAttributevalue16(reader.get(i++));
                superFeedModotexBean.setRelationshiptype(reader.get(i++));
                superFeedModotexBean.setVariationtheme(reader.get(i++));
                superfeed.add(superFeedModotexBean);
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
            $info("Modotex产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("Modotex产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("Modotex产品文件读入失败");
            logIssue("cms 数据导入处理", "Modotex产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    /**
     * YogaDemocracy产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedModotexBean> superfeedlist) {

        for (SuperFeedModotexBean superfeed : superfeedlist) {

            if (modotexFeedDao.insertSelective(superfeed) <= 0) {
                $info("Modotex产品信息插入失败 sku = " + superfeed.getSku());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return Modotex;
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

        List<CmsBtFeedInfoModotexModel> vtmModelBeans = modotexFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoModotexModel vtmModelBean : vtmModelBeans) {
            if(vtmModelBean.getRelationshiptype().toUpperCase().equals("PARENT")) continue;
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
                sku.setWeightOrgUnit("lb");
            }
            cmsBtFeedInfoModel.setSkus(skus);
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
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsModotexAnalysisJob";
    }
}
