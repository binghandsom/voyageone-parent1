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
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedDfoBean;
import com.voyageone.task2.cms.dao.feed.DfoFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoDfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.DFO;
/**
 * Created by gjl on 2016/6/3.
 */
@Service
public class DfoAnalysisService extends BaseAnalysisService  {
    @Autowired
    DfoFeedDao dfoFeedDao;

    @Override
    @Transactional
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                dfoFeedDao.delFullBySku(strings);
                dfoFeedDao.insertFullBySku(strings);
                dfoFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        dfoFeedDao.delete();
    }

    /**
     * dfo产品文件读入
     */
    protected int superFeedImport() {
        $info("DFO产品文件读入开始");
        List<SuperFeedDfoBean> superfeed = new ArrayList<>();
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
                SuperFeedDfoBean superFeedDfoBean = new SuperFeedDfoBean();
                int i = 0;
                superFeedDfoBean.setSku(reader.get(i++));
                superFeedDfoBean.setBrand(reader.get(i++));
                superFeedDfoBean.setUpc(reader.get(i++));
                superFeedDfoBean.setClassification(reader.get(i++));
                superFeedDfoBean.setModel(reader.get(i++));
                superFeedDfoBean.setManufacturerColor(reader.get(i++));
                superFeedDfoBean.setGeneralColor(reader.get(i++));
                superFeedDfoBean.setSize(reader.get(i++));
                superFeedDfoBean.setLensSize(reader.get(i++));
                superFeedDfoBean.setBridgeSize(reader.get(i++));
                superFeedDfoBean.setTempleSize(reader.get(i++));
                superFeedDfoBean.setVerticalSize(reader.get(i++));
                superFeedDfoBean.setGender(reader.get(i++));
                superFeedDfoBean.setMaterial(reader.get(i++));
                superFeedDfoBean.setCountryOfOrigin(reader.get(i++));
                superFeedDfoBean.setLensColor(reader.get(i++));
                superFeedDfoBean.setLensTechnology(reader.get(i++));
                superFeedDfoBean.setFrameType(reader.get(i++));
                superFeedDfoBean.setStyle(reader.get(i++));
                superFeedDfoBean.setUrl1(reader.get(i++));
                superFeedDfoBean.setUrl2(reader.get(i++));
                superFeedDfoBean.setUrl3(reader.get(i++));
                superFeedDfoBean.setUrl4(reader.get(i++));
                superFeedDfoBean.setUrl5(reader.get(i++));
                superFeedDfoBean.setUrl6(reader.get(i++));
                superFeedDfoBean.setUrl7(reader.get(i++));
                superFeedDfoBean.setUrl8(reader.get(i++));
                superFeedDfoBean.setVoyageonePrice(reader.get(i++));
                superFeedDfoBean.setRetailPrice(reader.get(i++));
                superFeedDfoBean.setQty(reader.get(i++));
                if (StringUtil.isEmpty(superFeedDfoBean.getUpc())) {
                    continue;
                }
                superfeed.add(superFeedDfoBean);
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
            $info("DFO产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("DFO产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("DFO产品文件读入失败");
            logIssue("cms 数据导入处理", "DFO产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }
    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedDfoBean> superfeedlist) {

        for (SuperFeedDfoBean superfeed : superfeedlist) {

            if (dfoFeedDao.insertSelective(superfeed) <= 0) {
                $info("DFO产品信息插入失败 InventoryNumber = " + superfeed.getSku());
            }
        }
        return true;
    }
    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return DFO;
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

        List<CmsBtFeedInfoDfoModel> vtmModelBeans = dfoFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoDfoModel vtmModelBean : vtmModelBeans) {

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
    protected boolean backupFeedFile(String channel_id) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        String filename_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + "status" + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
//            logger.error("产品文件备份失败");
            $info("产品文件备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsDfoAnalySisJob";
    }
}
