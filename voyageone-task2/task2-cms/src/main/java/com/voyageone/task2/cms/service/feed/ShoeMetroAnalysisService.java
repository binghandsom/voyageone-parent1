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
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.SuperFeedShoeMetroBean;
import com.voyageone.task2.cms.dao.feed.ShoeMetroFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoShoeMetroModel;
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

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.ShoeMetro;

/**
 * Created by gjl on 2016/8/01.
 */
@Service
public class ShoeMetroAnalysisService extends BaseAnalysisService {
    @Autowired
    ShoeMetroFeedDao shoeMetroFeedDao;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                shoeMetroFeedDao.delFullBySku(strings);
                shoeMetroFeedDao.insertFullBySku(strings);
                shoeMetroFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        shoeMetroFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("ShoeMetro产品文件读入开始");
        List<SuperFeedShoeMetroBean> superfeed = new ArrayList<>();
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
                SuperFeedShoeMetroBean superFeedShoeMetroBean = new SuperFeedShoeMetroBean();
                int i = 0;
                superFeedShoeMetroBean.setSku(reader.get(i++));
                superFeedShoeMetroBean.setParentid(reader.get(i++));
                superFeedShoeMetroBean.setTitle(reader.get(i++));
                superFeedShoeMetroBean.setFeaturebullet1(reader.get(i++));
                superFeedShoeMetroBean.setFeaturebullet2(reader.get(i++));
                superFeedShoeMetroBean.setFeaturebullet3(reader.get(i++));
                superFeedShoeMetroBean.setFeaturebullet4(reader.get(i++));
                superFeedShoeMetroBean.setFeaturebullet5(reader.get(i++));
                superFeedShoeMetroBean.setMsrp(reader.get(i++));
                superFeedShoeMetroBean.setSize(reader.get(i++));
                superFeedShoeMetroBean.setColor(reader.get(i++));
                superFeedShoeMetroBean.setCountryoforigin(reader.get(i++));
                superFeedShoeMetroBean.setProductid(reader.get(i++));
                superFeedShoeMetroBean.setWeight(reader.get(i++));
                superFeedShoeMetroBean.setShoematerial(reader.get(i++));
                superFeedShoeMetroBean.setOutermaterial(reader.get(i++));
                superFeedShoeMetroBean.setOccassionandlifestyle(reader.get(i++));
                superFeedShoeMetroBean.setImages(reader.get(i++));
                superFeedShoeMetroBean.setBootopeningcircumference(reader.get(i++));
                superFeedShoeMetroBean.setClosuretype(reader.get(i++));
                superFeedShoeMetroBean.setHeelheight(reader.get(i++));
                superFeedShoeMetroBean.setHeeltype(reader.get(i++));
                superFeedShoeMetroBean.setShoewidth(reader.get(i++));
                superFeedShoeMetroBean.setSolematerial(reader.get(i++));
                superFeedShoeMetroBean.setStraptype(reader.get(i++));
                superFeedShoeMetroBean.setToestyle(reader.get(i++));
                superFeedShoeMetroBean.setRelationshipname(reader.get(i++));
                superFeedShoeMetroBean.setVoyageoneprice(reader.get(i++));
                superFeedShoeMetroBean.setDescription(reader.get(i++));
                superFeedShoeMetroBean.setBrand(reader.get(i++));
                superFeedShoeMetroBean.setQuantity(reader.get(i++));
                superFeedShoeMetroBean.setGender(reader.get(i++));
                superFeedShoeMetroBean.setClothingmaterial(reader.get(i++));
                superFeedShoeMetroBean.setClothingsizetype(reader.get(i++));
                superFeedShoeMetroBean.setApparelclosure(reader.get(i++));
                superFeedShoeMetroBean.setEyewearframemeasurement(reader.get(i++));
                superFeedShoeMetroBean.setEyewearbridgemeasurement(reader.get(i++));
                superFeedShoeMetroBean.setFramematerial(reader.get(i++));
                superFeedShoeMetroBean.setEyewearlenscolor(reader.get(i++));
                superFeedShoeMetroBean.setEyewearlensmaterialtype(reader.get(i++));
                superFeedShoeMetroBean.setEyewearlenswidth(reader.get(i++));
                superFeedShoeMetroBean.setCategory(reader.get(i++));
                superfeed.add(superFeedShoeMetroBean);
                cnt++;
                $info("-------------------Sku"+superFeedShoeMetroBean.getSku()+"数量"+cnt);
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
            $info("ShoeMetro产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("ShoeMetro产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("ShoeMetro产品文件读入失败");
            logIssue("cms 数据导入处理", "ShoeMetro产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }
    @Override
    public int fullCopyTemp(){
        int cnt = shoeMetroFeedDao.fullCopyTemp();
        shoeMetroFeedDao.updateMd5();
        shoeMetroFeedDao.updateUpdateFlag();
        return cnt;
    }

    /**
     * YogaDemocracy产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedShoeMetroBean> superfeedlist) {

        for (SuperFeedShoeMetroBean superfeed : superfeedlist) {

            if (shoeMetroFeedDao.insertSelective(superfeed) <= 0) {
                $info("ShoeMetro产品信息插入失败 sku = " + superfeed.getSku());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return ShoeMetro;
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

        List<CmsBtFeedInfoShoeMetroModel> vtmModelBeans = shoeMetroFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoShoeMetroModel vtmModelBean : vtmModelBeans) {
            if(StringUtil.isEmpty(vtmModelBean.getProductid())) continue;
            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }
            String[] features = vtmModelBean.getFeaturebullet1().split("/");
            if(features.length>2){
                String productMatnr = features[2].trim();
                for(int i=3;i<features.length;i++){
                    productMatnr +="-"+features[i].trim();
                }
                List<String> values = new ArrayList<>();
                values.add(productMatnr);
                attribute.put("product MATNR", values);
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
        return "CmsShoeMetroAnalysisJob";
    }
}
