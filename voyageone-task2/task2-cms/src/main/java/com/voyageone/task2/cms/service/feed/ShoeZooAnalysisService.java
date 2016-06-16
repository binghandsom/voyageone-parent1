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
import com.voyageone.task2.cms.bean.SuperFeedShoeZooBean;
import com.voyageone.task2.cms.dao.feed.ShoeZooFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoShoeZooModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.ShoeZoo;

/**
 * Created by gjl on 2016/6/15.
 */
@Service
public class ShoeZooAnalysisService extends BaseAnalysisService{
    @Autowired
    ShoeZooFeedDao shoeZooFeedDao;
    @Override
    @Transactional
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                shoeZooFeedDao.delFullBySku(strings);
                shoeZooFeedDao.insertFullBySku(strings);
                shoeZooFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        shoeZooFeedDao.delete();
    }

    /**
     * ShoeZoo产品文件读入
     */
    protected int superFeedImport() {
        $info("ShoeZoo产品文件读入开始");
        List<SuperFeedShoeZooBean> superfeed = new ArrayList<>();
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
                SuperFeedShoeZooBean superFeedShoeZooBean = new SuperFeedShoeZooBean();
                int i = 0;
                superFeedShoeZooBean.setAuctionTitle(reader.get(i++));
                superFeedShoeZooBean.setInventoryNumber(reader.get(i++));
                superFeedShoeZooBean.setUpc1(reader.get(i++));
                superFeedShoeZooBean.setMpn(reader.get(i++));
                superFeedShoeZooBean.setShortDescription(reader.get(i++));
                superFeedShoeZooBean.setDescription(reader.get(i++));
                superFeedShoeZooBean.setManufacturer(reader.get(i++));
                superFeedShoeZooBean.setBrand(reader.get(i++));
                superFeedShoeZooBean.setCondition(reader.get(i++));
                superFeedShoeZooBean.setBuyItNowPrice(reader.get(i++));
                superFeedShoeZooBean.setRetailPrice(reader.get(i++));
                superFeedShoeZooBean.setPictureUrls(reader.get(i++));
                superFeedShoeZooBean.setTaxproductcode(reader.get(i++));
                superFeedShoeZooBean.setRelationshipName(reader.get(i++));
                superFeedShoeZooBean.setVariationParentSku(reader.get(i++));
                superFeedShoeZooBean.setAmzrepricerautoprice(reader.get(i++));
                superFeedShoeZooBean.setBrandname(reader.get(i++));
                superFeedShoeZooBean.setCategories(reader.get(i++));
                superFeedShoeZooBean.setClosuretype(reader.get(i++));
                superFeedShoeZooBean.setColormap(reader.get(i++));
                superFeedShoeZooBean.setColorname(reader.get(i++));
                superFeedShoeZooBean.setDepartmentname(reader.get(i++));
                superFeedShoeZooBean.setEbaymaincolor(reader.get(i++));
                superFeedShoeZooBean.setEbaystorecategorytext(reader.get(i++));
                superFeedShoeZooBean.setExternalproductid(reader.get(i++));
                superFeedShoeZooBean.setExternalproductidtype(reader.get(i++));
                superFeedShoeZooBean.setFeedproducttype(reader.get(i++));
                superFeedShoeZooBean.setGender(reader.get(i++));
                superFeedShoeZooBean.setGenerickeywords(reader.get(i++));
                superFeedShoeZooBean.setItemclassdisplaypath(reader.get(i++));
                superFeedShoeZooBean.setItemname(reader.get(i++));
                superFeedShoeZooBean.setItemtype(reader.get(i++));
                superFeedShoeZooBean.setItemtypekeyword(reader.get(i++));
                superFeedShoeZooBean.setListprice(reader.get(i++));
                superFeedShoeZooBean.setMaterialtype(reader.get(i++));
                superFeedShoeZooBean.setModel(reader.get(i++));
                superFeedShoeZooBean.setNumberofitems(reader.get(i++));
                superFeedShoeZooBean.setProductdescription(reader.get(i++));
                superFeedShoeZooBean.setRecommendedbrowsenodes(reader.get(i++));
                superFeedShoeZooBean.setRelationshiptype(reader.get(i++));
                superFeedShoeZooBean.setShoesizedisplay(reader.get(i++));
                superFeedShoeZooBean.setShoesizeinfants(reader.get(i++));
                superFeedShoeZooBean.setShoesizekids(reader.get(i++));
                superFeedShoeZooBean.setShoesizemen(reader.get(i++));
                superFeedShoeZooBean.setShoesizewomen(reader.get(i++));
                superFeedShoeZooBean.setShoetypes(reader.get(i++));
                superFeedShoeZooBean.setSizename(reader.get(i++));
                superFeedShoeZooBean.setSolematerial(reader.get(i++));
                superFeedShoeZooBean.setStandardprice(reader.get(i++));
                superFeedShoeZooBean.setStylekeywords1(reader.get(i++));
                superFeedShoeZooBean.setStylename(reader.get(i++));
                superFeedShoeZooBean.setSwatchimageurl(reader.get(i++));
                superFeedShoeZooBean.setToestyle(reader.get(i++));
                superFeedShoeZooBean.setUpc2(reader.get(i++));
                superFeedShoeZooBean.setVariationtheme(reader.get(i++));
                superFeedShoeZooBean.setWaterresistancelevel(reader.get(i++));
                superFeedShoeZooBean.setFeatureBullets(reader.get(i++));
                superFeedShoeZooBean.setVoyageOnePurchasePrice(reader.get(i++));
                superFeedShoeZooBean.setCountry_of_Origin(reader.get(i++));
                superFeedShoeZooBean.setTmall_Weight(reader.get(i++));
                if (StringUtil.isEmpty(superFeedShoeZooBean.getUpc1())) {
                    continue;
                }
                superfeed.add(superFeedShoeZooBean);
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
            $info("ShoeZoo产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("ShoeZoo产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("ShoeZoo产品文件读入失败");
            logIssue("cms 数据导入处理", "ShoeZoo产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }
    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedShoeZooBean> superfeedlist) {

        for (SuperFeedShoeZooBean superfeed : superfeedlist) {

            if (shoeZooFeedDao.insertSelective(superfeed) <= 0) {
                $info("ShoeZoo产品信息插入失败 InventoryNumber = " + superfeed.getVariationParentSku());
            }
        }
        return true;
    }
    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return ShoeZoo;
    }

    /**
     * 生成类目数据包含model product数据
     */
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

        List<CmsBtFeedInfoShoeZooModel> vtmModelBeans = shoeZooFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoShoeZooModel vtmModelBean : vtmModelBeans) {

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
        return "CmsShoeZooAnalysisJob";
    }
}
