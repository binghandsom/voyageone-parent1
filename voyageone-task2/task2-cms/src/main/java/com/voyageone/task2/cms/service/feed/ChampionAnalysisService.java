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
import com.voyageone.task2.cms.bean.SuperFeedChampionBean;
import com.voyageone.task2.cms.bean.SuperfeedBhfoBean;
import com.voyageone.task2.cms.dao.feed.ChampionFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoChampionModel;
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

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.CHAMPION;

@Service
public class ChampionAnalysisService extends BaseAnalysisService {
    @Autowired
    ChampionFeedDao championFeedDao;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                championFeedDao.delFullBySku(strings);
                championFeedDao.insertFullBySku(strings);
                championFeedDao.updateFlagBySku(strings);
            });
        }
    }

    @Override
    protected void zzWorkClear() {
        championFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("Champion产品文件读入开始");
        List<SuperFeedChampionBean> superFeed = new ArrayList<>();
        int cnt = 0;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);
            // InputStreamReader 是字节流通向字符流的桥梁,
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileFullName)));
            // Body读入
            String b;
            br.readLine(); // 跳过第一行
            // Body读入
            while ((b = br.readLine()) != null) {
                SuperFeedChampionBean championBean = new SuperFeedChampionBean();
                List<String> reader = new ArrayList(Arrays.asList(b.split("\t")));
                int i = 0;
                try {
                    championBean.setSpu(reader.get(i++));
                    championBean.setCode(reader.get(i++));
                    championBean.setSku(reader.get(i++));
                    championBean.setColorId(reader.get(i++));
                    championBean.setColorName(reader.get(i++));
                    championBean.setSize(reader.get(i++));
                    championBean.setUpc(reader.get(i++));
                    championBean.setCategory(reader.get(i++));
                    championBean.setImgurl(reader.get(i++));
                    championBean.setProductType(reader.get(i++));
                    championBean.setSizeType(reader.get(i++));
                    championBean.setPrice(reader.get(i++));
                    championBean.setBrand(reader.get(i++));
                    championBean.setName(reader.get(i++));
                    championBean.setOrigin(reader.get(i++));
                    championBean.setStyle(reader.get(i++));
                    championBean.setMaterial(reader.get(i++));
                    championBean.setDescription(reader.get(i++));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    $info(championBean.getSku());
                    continue;
                }
                superFeed.add(championBean);
                cnt++;
                if (superFeed.size() > 1000) {
                    transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                    superFeed.clear();
                }
            }
            if (superFeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superFeed));
                superFeed.clear();
            }
            $info("Champion产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("Champion产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("Champion产品文件读入失败");
            logIssue("cms 数据导入处理", "Champion产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }


    /**
     * Champion+产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedChampionBean> superFeedList) {

        superFeedList.stream()
                .filter(superFeed -> championFeedDao.insertSelective(superFeed) <= 0)
                .forEach(superFeed -> $info("LightHouse产品信息插入失败 sku = "));
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return CHAMPION;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map<String, Object> column = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = feedBeans.stream()
                .filter(feedConfig -> !StringUtil.isEmpty(feedConfig.getCfg_val1()))
                .map(FeedBean::getCfg_val1).collect(Collectors.toList());

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, column.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        column.put("keyword", where);
        column.put("tableName", table);
        if (attList.size() > 0) {
            column.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoChampionModel> vtmModelBeans = championFeedDao.selectSuperFeedModel(column);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoChampionModel vtmModelBean : vtmModelBeans) {

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
            List<CmsBtFeedInfoModel_Sku> skuList = vtmModelBean.getSkus();
            for (CmsBtFeedInfoModel_Sku sku : skuList) {
                String weight = sku.getWeightOrg().trim();
                Pattern pattern = Pattern.compile("[^0-9.]");
                Matcher matcher = pattern.matcher(weight);
                if (matcher.find()) {
                    int index = weight.indexOf(matcher.group());
                    if (index != -1) {
                        String weightOrg = weight.substring(0, index);
                        sku.setWeightOrg(weightOrg);
                    }
                }
                sku.setWeightOrgUnit(sku.getWeightOrgUnit());
            }
            cmsBtFeedInfoModel.setSkus(skuList);
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
        return "CmsChampionAnalysisJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
