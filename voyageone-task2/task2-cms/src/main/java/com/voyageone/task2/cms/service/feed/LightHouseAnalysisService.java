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
import com.voyageone.task2.cms.bean.SuperFeedLightHouseBean;
import com.voyageone.task2.cms.dao.feed.LightHouseFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoLightHouseModel;
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

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.LightHouse;

/**
 * Created by gjl on 2016/7/8.
 */
@Service
public class LightHouseAnalysisService extends BaseAnalysisService {

    @Autowired
    LightHouseFeedDao lightHouseFeedDao;
    @Override
    protected void updateFull(List<String> itemIds) {

        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                lightHouseFeedDao.delFullBySku(strings);
                lightHouseFeedDao.insertFullBySku(strings);
                lightHouseFeedDao.updateFlagBySku(strings);
            });

        }
    }
    @Override
    protected void zzWorkClear() {
        lightHouseFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("LightHouse产品文件读入开始");
        List<SuperFeedLightHouseBean> superfeed = new ArrayList<>();
        int cnt = 0;
        //CsvReader reader;
        BufferedReader br = null;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);
            // InputStreamReader 是字节流通向字符流的桥梁,
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileFullName)));
            // Body读入
            String b;
            br.readLine(); // 跳过第一行
            // Body读入
            while ((b = br.readLine()) != null) {
                SuperFeedLightHouseBean superFeedLightHouseBean = new SuperFeedLightHouseBean();
                List<String> reader = new ArrayList(Arrays.asList(b.split("\t")));
                int i = 0;
                try {
                    superFeedLightHouseBean.setSku(reader.get(i++));
                    superFeedLightHouseBean.setTitle(reader.get(i++));
                    superFeedLightHouseBean.setBrand(reader.get(i++));
                    superFeedLightHouseBean.setManufacturer(reader.get(i++));
                    superFeedLightHouseBean.setDescription(reader.get(i++));
                    superFeedLightHouseBean.setBulletpoint1(reader.get(i++));
                    superFeedLightHouseBean.setBulletpoint2(reader.get(i++));
                    superFeedLightHouseBean.setBulletpoint3(reader.get(i++));
                    superFeedLightHouseBean.setBulletpoint4(reader.get(i++));
                    superFeedLightHouseBean.setBulletpoint5(reader.get(i++));
                    superFeedLightHouseBean.setMainimage(reader.get(i++));
                    superFeedLightHouseBean.setImage2(reader.get(i++));
                    superFeedLightHouseBean.setImage3(reader.get(i++));
                    superFeedLightHouseBean.setImage4(reader.get(i++));
                    superFeedLightHouseBean.setUnitcount(reader.get(i++));
                    superFeedLightHouseBean.setColor(reader.get(i++));
                    superFeedLightHouseBean.setDirections(reader.get(i++));
                    superFeedLightHouseBean.setHairtype(reader.get(i++));
                    superFeedLightHouseBean.setIndications(reader.get(i++));
                    superFeedLightHouseBean.setIngredients(reader.get(i++));
                    superFeedLightHouseBean.setSkintone(reader.get(i++));
                    superFeedLightHouseBean.setSkintype(reader.get(i++));
                    superFeedLightHouseBean.setTargetgender(reader.get(i++));
                    superFeedLightHouseBean.setVoyageoneprice(reader.get(i++));
                    superFeedLightHouseBean.setMsrp(reader.get(i++));
                    superFeedLightHouseBean.setCountryoforigin(reader.get(i++));
                    superFeedLightHouseBean.setWeight(reader.get(i++));
                    superFeedLightHouseBean.setCategory(reader.get(i++));
                    superFeedLightHouseBean.setUpc(reader.get(i++));
                    superFeedLightHouseBean.setNjtotalinventory(reader.get(i++));
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                    $info(superFeedLightHouseBean.getSku());
                }

                superfeed.add(superFeedLightHouseBean);
                $info(superFeedLightHouseBean.getSku());
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
            $info("LightHouse产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("LightHouse产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("LightHouse产品文件读入失败");
            logIssue("cms 数据导入处理", "LightHouse产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedLightHouseBean> superfeedlist) {

        for (SuperFeedLightHouseBean superfeed : superfeedlist) {

            if (lightHouseFeedDao.insertSelective(superfeed) <= 0) {
                $info("LightHouse产品信息插入失败 sku = " + superfeed.getSku());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return LightHouse;
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

        List<CmsBtFeedInfoLightHouseModel> vtmModelBeans = lightHouseFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoLightHouseModel vtmModelBean : vtmModelBeans) {

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
        return "CmsLightHouseAnalysisJob";
    }
}
