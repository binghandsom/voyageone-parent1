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
import com.voyageone.task2.cms.bean.SuperFeedKitBagBean;
import com.voyageone.task2.cms.bean.SuperFeedKitBagPriceBean;
import com.voyageone.task2.cms.bean.SuperFeedKitBagStockBean;
import com.voyageone.task2.cms.bean.SuperFeedKitBagTranslationsBean;
import com.voyageone.task2.cms.dao.feed.KitBagFeedDao;
import com.voyageone.task2.cms.dao.feed.KitBagFeedPriceDao;
import com.voyageone.task2.cms.dao.feed.KitBagFeedStockDao;
import com.voyageone.task2.cms.dao.feed.KitBagFeedTranslationsDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoKitBagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.KitBag;

/**
 * Created by gjl on 2017/2/15.
 */
@Service
public class KitBagAnalysisService extends BaseAnalysisService {

    @Autowired
    KitBagFeedDao kitBagFeedDao;
    @Autowired
    KitBagFeedPriceDao kitBagFeedPriceDao;
    @Autowired
    KitBagFeedStockDao kitBagFeedStockDao;
    @Autowired
    KitBagFeedTranslationsDao kitBagFeedTranslationsDao;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                kitBagFeedDao.delFullBySku(strings);
                kitBagFeedDao.insertFullBySku(strings);
                kitBagFeedDao.updateFlagBySku(strings);
            });
        }
    }

    @Override
    protected void zzWorkClear() {
        kitBagFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
        String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);
        //CsvReader reader;
        CsvReader reader;
        int cnt = 0;
        //取得价格文件数据
        List<SuperFeedKitBagPriceBean> superFeedPriceList = getPriceList(filePath, encode);
        //取得库存文件数据
        List<SuperFeedKitBagStockBean> superFeedStockList = getStockList(filePath, encode);
        //取得翻译文件数据
        getTranslationsList(filePath, encode);
        try {
            List<SuperFeedKitBagBean> superFeed = new ArrayList<>();
            //产品文件
            String fileSuperFeedName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String fileSuperFeedFullName = String.format("%s/%s", filePath, fileSuperFeedName);
            // InputStreamReader 是字节流通向字符流的桥梁,
            reader = new CsvReader(new FileInputStream(fileSuperFeedFullName), ',', Charset.forName(encode));
            // Body读入
            while (reader.readRecord()) {
                SuperFeedKitBagBean superFeedKitBagBean = new SuperFeedKitBagBean();
                int i = 0;
                try {
                    superFeedKitBagBean.setProductid(reader.get(i++));
                    superFeedKitBagBean.setVariationid(reader.get(i++));
                    superFeedKitBagBean.setMainvid(reader.get(i++));
                    superFeedKitBagBean.setSitename(reader.get(i++));
                    superFeedKitBagBean.setSiteid(reader.get(i++));
                    superFeedKitBagBean.setTerritories(reader.get(i++));
                    superFeedKitBagBean.setEan(reader.get(i++));
                    superFeedKitBagBean.setTitle(reader.get(i++));
                    superFeedKitBagBean.setDetaileddescription(reader.get(i++));
                    superFeedKitBagBean.setProductsku(reader.get(i++));
                    superFeedKitBagBean.setVariationsku(reader.get(i++));
                    superFeedKitBagBean.setGender(reader.get(i++));
                    superFeedKitBagBean.setSize(reader.get(i++));
                    superFeedKitBagBean.setMultivariationgroupid(reader.get(i++));
                    superFeedKitBagBean.setWeight(reader.get(i++));
                    superFeedKitBagBean.setWidth(reader.get(i++));
                    superFeedKitBagBean.setLength(reader.get(i++));
                    superFeedKitBagBean.setDepth(reader.get(i++));
                    superFeedKitBagBean.setItembrand(reader.get(i++));
                    superFeedKitBagBean.setColour(reader.get(i++));
                    superFeedKitBagBean.setCategories(reader.get(i++));
                    superFeedKitBagBean.setImages(reader.get(i++));
                    superFeedKitBagBean.setCommoditycode(reader.get(i++));
                    superFeedKitBagBean.setCountryoforigin(reader.get(i++));
                    superFeedKitBagBean.setComposition(reader.get(i++));
                    //取价格
                    if (superFeedPriceList.size() > 0) {
                        superFeedPriceList.stream()
                                .filter(superFeedKitBagPriceBean -> superFeedKitBagPriceBean.getVariationid().equals(superFeedKitBagBean.getVariationid()))
                                .forEach(superFeedKitBagPriceBean -> {
                                    superFeedKitBagBean.setPrice(superFeedKitBagPriceBean.getPrice());
                                    superFeedKitBagBean.setPricewas(superFeedKitBagPriceBean.getPricewas());
                                    superFeedKitBagBean.setCurrency(superFeedKitBagPriceBean.getCurrency());
                                });
                    }

                    //取库存
                    if (superFeedStockList.size() > 0) {
                        superFeedStockList.stream()
                                .filter(superFeedKitBagStockBean -> superFeedKitBagStockBean.getVariationid().equals(superFeedKitBagBean.getVariationid()))
                                .forEach(superFeedKitBagStockBean -> {
                                    superFeedKitBagBean.setQuantity(superFeedKitBagStockBean.getQuantity());
                                });
                    }

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    $info(superFeedKitBagBean.getVariationsku());
                }
                if (!StringUtil.isEmpty(superFeedKitBagBean.getPrice()) && "155".equals(superFeedKitBagBean.getSiteid())) {
                    superFeed.add(superFeedKitBagBean);
                    $info(superFeedKitBagBean.getVariationsku());
                }
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
            $info("KitBag产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("KitBag产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("KitBag产品文件读入失败");
            logIssue("cms数据导入处理", "KitBag产品文件读入失败" + ex.getMessage());
        }
        return cnt;
    }

    public List<SuperFeedKitBagPriceBean> getPriceList(String filePath, String encode) {
        $info("KitBag价格文件读入开始");
        List<SuperFeedKitBagPriceBean> superFeedPriceList = new ArrayList<>();
        try {
            //价格文件
            String filePriceName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_price_id);
            String filePriceFullName = String.format("%s/%s", filePath, filePriceName);
            CsvReader brPriceReader = new CsvReader(new FileInputStream(filePriceFullName), ',', Charset.forName(encode));
            // Head读入
            brPriceReader.readHeaders();
            //读取价格文件
            while (brPriceReader.readRecord()) {
                SuperFeedKitBagPriceBean superFeedKitBagPriceBean = new SuperFeedKitBagPriceBean();
                int i = 0;
                try {
                    superFeedKitBagPriceBean.setProductid(brPriceReader.get(i++));
                    superFeedKitBagPriceBean.setVariationid(brPriceReader.get(i++));
                    superFeedKitBagPriceBean.setSitename(brPriceReader.get(i++));
                    superFeedKitBagPriceBean.setSiteid(brPriceReader.get(i++));
                    superFeedKitBagPriceBean.setTerritory(brPriceReader.get(i++));
                    superFeedKitBagPriceBean.setPrice(brPriceReader.get(i++));
                    superFeedKitBagPriceBean.setPricewas(brPriceReader.get(i++));
                    superFeedKitBagPriceBean.setCurrency(brPriceReader.get(i++));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    $info(superFeedKitBagPriceBean.getVariationid());
                }
                superFeedPriceList.add(superFeedKitBagPriceBean);
            }
        } catch (FileNotFoundException e) {
            $info("KitBag价格文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("KitBag价格文件读入失败");
            logIssue("cms数据导入处理", "KitBag产品文件读入失败" + ex.getMessage());
        }

        if (superFeedPriceList.size() > 0) {
            for (SuperFeedKitBagPriceBean superFeedKitBagPriceBean : superFeedPriceList) {

                SuperFeedKitBagPriceBean bean = kitBagFeedPriceDao.selectByPrimaryKey(superFeedKitBagPriceBean.getVariationid());

                if (bean != null) {
                    kitBagFeedPriceDao.updateByPrimaryKey(superFeedKitBagPriceBean);
                } else {
                    kitBagFeedPriceDao.insert(superFeedKitBagPriceBean);
                }
            }
        }
        return superFeedPriceList;
    }

    public List<SuperFeedKitBagStockBean> getStockList(String filePath, String encode) {
        $info("KitBag库存文件读入开始");
        List<SuperFeedKitBagStockBean> superFeedStockList = new ArrayList<>();
        CsvReader brStockReader;
        try {
            //库存文件
            String fileStockName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_stock_id);
            String fileStockFullName = String.format("%s/%s", filePath, fileStockName);
            brStockReader = new CsvReader(new FileInputStream(fileStockFullName), ',', Charset.forName(encode));
            // Head读入
            brStockReader.readHeaders();
            //库存文件读取
            while (brStockReader.readRecord()) {
                SuperFeedKitBagStockBean superFeedKitBagStockBean = new SuperFeedKitBagStockBean();
                int i = 0;
                try {
                    superFeedKitBagStockBean.setProductid(brStockReader.get(i++));
                    superFeedKitBagStockBean.setVariationid(brStockReader.get(i++));
                    superFeedKitBagStockBean.setQuantity(brStockReader.get(i++));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    $info(superFeedKitBagStockBean.getVariationid());
                }
                superFeedStockList.add(superFeedKitBagStockBean);
            }
        } catch (FileNotFoundException e) {
            $info("KitBag库存文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("KitBag库存文件读入失败");
            logIssue("cms数据导入处理", "KitBag库存文件读入失败" + ex.getMessage());
        }

        if (superFeedStockList.size() > 0) {
            for (SuperFeedKitBagStockBean superFeedKitBagStockBean : superFeedStockList) {

                SuperFeedKitBagStockBean bean = kitBagFeedStockDao.selectByPrimaryKey(superFeedKitBagStockBean.getVariationid());

                if (bean != null) {
                    kitBagFeedStockDao.updateByPrimaryKey(superFeedKitBagStockBean);
                } else {
                    kitBagFeedStockDao.insert(superFeedKitBagStockBean);
                }
            }
        }
        return superFeedStockList;
    }

    public List<SuperFeedKitBagTranslationsBean> getTranslationsList(String filePath, String encode) {
        $info("KitBag翻译文件读入开始");
        List<SuperFeedKitBagTranslationsBean> superFeedTranslationsList = new ArrayList<>();
        try {
            //翻译文件
            String fileTranslationsName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_translations_id);
            String fileTranslationsFullName = String.format("%s/%s", filePath, fileTranslationsName);
            CsvReader brTranslationsReader = new CsvReader(new FileInputStream(fileTranslationsFullName), ',', Charset.forName(encode));
            // Head读入
            brTranslationsReader.readHeaders();
            //翻译文件读取
            while (brTranslationsReader.readRecord()) {
                SuperFeedKitBagTranslationsBean superFeedKitBagTranslationsBean = new SuperFeedKitBagTranslationsBean();
                int i = 0;
                try {
                    superFeedKitBagTranslationsBean.setProductid(brTranslationsReader.get(i++));
                    superFeedKitBagTranslationsBean.setSitename(brTranslationsReader.get(i++));
                    superFeedKitBagTranslationsBean.setSiteid(brTranslationsReader.get(i++));
                    superFeedKitBagTranslationsBean.setTerritory(brTranslationsReader.get(i++));
                    superFeedKitBagTranslationsBean.setTitle(brTranslationsReader.get(i++));
                    superFeedKitBagTranslationsBean.setDetaileddescription(brTranslationsReader.get(i++));
                    superFeedKitBagTranslationsBean.setGender(brTranslationsReader.get(i++));
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    $info(superFeedKitBagTranslationsBean.getProductid());
                }
                superFeedTranslationsList.add(superFeedKitBagTranslationsBean);
            }
        } catch (FileNotFoundException e) {
            $info("KitBag翻译文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("KitBag翻译文件读入失败");
            logIssue("cms数据导入处理", "KitBag翻译文件读入失败" + ex.getMessage());
        }

        if (superFeedTranslationsList.size() > 0) {
            for (SuperFeedKitBagTranslationsBean superFeedKitBagTranslationsBean : superFeedTranslationsList) {

                SuperFeedKitBagTranslationsBean bean = kitBagFeedTranslationsDao.selectByPrimaryKey(superFeedKitBagTranslationsBean.getProductid());

                if (bean != null) {
                    kitBagFeedTranslationsDao.updateByPrimaryKey(superFeedKitBagTranslationsBean);
                } else {
                    kitBagFeedTranslationsDao.insert(superFeedKitBagTranslationsBean);
                }
            }
        }
        return superFeedTranslationsList;
    }

    /**
     * KitBag产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedKitBagBean> superFeedlist) {

        superFeedlist.stream()
                .filter(superFeed -> kitBagFeedDao.insertSelective(superFeed) <= 0)
                .forEach(superFeed -> $info("KitBag产品信息插入失败 sku = " + superFeed.getVariationsku()));
        return true;
    }

    @Override
    protected boolean backupFeedFile(String channel_id) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        String filename_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
            $info("产品文件备份失败");
        }

        String filePriceName = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_price_id));
        String filePriceName_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_price_id));
        File filePrice = new File(filePriceName);
        File filePrice_backup = new File(filePriceName_backup);

        if (!filePrice.renameTo(filePrice_backup)) {
            $info("价格文件备份失败");
        }


        String fileStockName = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_stock_id));
        String fileStockName_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_stock_id));
        File fileStock = new File(fileStockName);
        File fileStock_backup = new File(fileStockName_backup);

        if (!fileStock.renameTo(fileStock_backup)) {
            $info("库存文件备份失败");
        }


        String fileTranslationsName = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_translations_id));
        String fileTranslationsName_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_translations_id));
        File fileTranslations = new File(fileTranslationsName);
        File fileTranslations_backup = new File(fileTranslationsName_backup);

        if (!fileTranslations.renameTo(fileTranslations_backup)) {
            $info("翻译文件备份失败");
        }
        $info("备份处理文件结束");
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return KitBag;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map<String, Object> column = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = feedBeans.stream()
                .filter(feedConfig -> !StringUtil.isEmpty(feedConfig.getCfg_val1()))
                .map(FeedBean::getCfg_val1)
                .collect(Collectors.toList());

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, column.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        column.put("keyword", where);
        column.put("tableName", table);
        if (attList.size() > 0) {
            column.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoKitBagModel> kitBagModelBeans = kitBagFeedDao.selectSuperFeedModel(column);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoKitBagModel kitBagModelBean : kitBagModelBeans) {
            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(kitBagModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();
            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;

                List<String> values = new ArrayList<>();
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }
            CmsBtFeedInfoModel cmsBtFeedInfoModel = kitBagModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            //设置重量开始
            List<CmsBtFeedInfoModel_Sku> skus = kitBagModelBean.getSkus();
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
                if (!StringUtils.isEmpty(kitBagModelBean.getMainvid())) {
                    if (!"0".equals(kitBagModelBean.getMainvid())) {
                        sku.setMainVid(kitBagModelBean.getMainvid());
                    }
                }
            }
            cmsBtFeedInfoModel.setSkus(skus);
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
        return "CmsKitBagAnalysisJob";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
}
