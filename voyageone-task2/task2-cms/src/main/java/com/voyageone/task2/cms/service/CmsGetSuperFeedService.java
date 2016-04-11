package com.voyageone.task2.cms.service;

import com.csvreader.CsvReader;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.CmsConstants;
import com.voyageone.task2.cms.bean.*;
import com.voyageone.task2.cms.dao.SuperFeedDao;
import com.voyageone.task2.cms.utils.WebServiceUtil;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CmsGetSuperFeedService extends BaseTaskService {

    @Autowired
    private SuperFeedDao superfeeddao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsGetSuperFeedJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        $info("channel_id=" + TaskControlEnums.Name.order_channel_id);
        $info("orderChannelIdList=" + orderChannelIdList.size());
        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            $info("channel_id=" + orderChannelID);
            threads.add(() -> new getSuperFeed(orderChannelID).doRun());
        }

        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道进行产品导入
     */
    public class getSuperFeed {
        private OrderChannelBean channel;

        public getSuperFeed(String orderChannelId) {
            this.channel = Channels.getChannel(orderChannelId);
        }

        public void doRun() {
            $info(channel.getFull_name() + "产品导入开始");

            boolean isSuccess = true;
//			// 下载文件
//			try {
//				isSuccess = downloadFileForFtp(channel.getOrder_channel_id());
//			} catch (Exception e) {
//				e.printStackTrace();
//				$error("文件下载失败");
//				logIssue("cms 数据导入处理", "文件下载失败" +  e.getMessage());
//			}

            // JEWELRY数据导入
            if (channel.getOrder_channel_id().equals(ChannelConfigEnums.Channel.JEWELRY.getId())) {
                // JE产品文件读入
                List<SuperFeedJEBean> superfeedjebean = jeSuperFeedImport();

                // JE产品信息插入
                isSuccess = superfeedjebean.size() > 0 && insertSuperFeedJE(superfeedjebean);
            }

//			// LOCONDO数据导入
//			if(channel.getOrder_channel_id().equals(ChannelConfigEnums.Channel.LOCONDO.getId())){
//				// LC产品文件读入
//				List<SuperFeedLCBean> superfeedlcbean = lcSuperFeedImport();
//
//				if (superfeedlcbean.size() > 0) {
//					// JE产品信息插入
//					isSuccess = insertSuperFeedLC(superfeedlcbean);
//				}else{
//					isSuccess = false;
//				}
//			}

            // 所有Attribute
            AttributeListInsert(channel.getOrder_channel_id());

            // 新旧数据判定
            if (isSuccess) {
                isSuccess = CheckSuperFeed(channel.getOrder_channel_id());
            }

            // 新旧数据处理
            if (isSuccess) {
                // 插入数据
                isSuccess = NewProductFeed(channel.getOrder_channel_id());
                // 更新数据
                isSuccess = OldProductFeed(channel.getOrder_channel_id());
            }

            if (isSuccess) {
                // 清除本地文件
                isSuccess = backupFeedFile(channel.getOrder_channel_id());
            }

            $info(channel.getFull_name() + "产品导入结束");
        }
    }

    /**
     * JE产品文件读入
     */
    public List<SuperFeedJEBean> jeSuperFeedImport() {
        $info("JE产品文件读入开始");

        List<SuperFeedJEBean> superfeed = new ArrayList<>();

        CsvReader reader;
        try {
            String fileName = Feeds.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);

            String encode = Feeds.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.feed_ftp_file_coding);

            reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));

            // Head读入
            reader.readHeaders();
            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                SuperFeedJEBean superfeedjebean = new SuperFeedJEBean();

                int i = 0;
                superfeedjebean.setAuctionTitle(reader.get(i++));
                superfeedjebean.setInventoryNumber(reader.get(i++));
                superfeedjebean.setWeight(reader.get(i++));
                superfeedjebean.setISBN(reader.get(i++));
                superfeedjebean.setUPC(reader.get(i++));
                superfeedjebean.setEAN(reader.get(i++));
                superfeedjebean.setASIN(reader.get(i++));
                superfeedjebean.setMPN(reader.get(i++));
                superfeedjebean.setShortDescription(reader.get(i++));
                superfeedjebean.setDescription(reader.get(i++));
                superfeedjebean.setFlag(reader.get(i++));
                superfeedjebean.setFlagDescription(reader.get(i++));
                superfeedjebean.setBlocked(reader.get(i++));
                superfeedjebean.setBlockedComment(reader.get(i++));
                superfeedjebean.setManufacturer(reader.get(i++));
                superfeedjebean.setBrand(reader.get(i++));
                superfeedjebean.setCondition(reader.get(i++));
                superfeedjebean.setWarranty(reader.get(i++));
                superfeedjebean.setSellerCost(reader.get(i++));
                superfeedjebean.setProductMargin(reader.get(i++));
                superfeedjebean.setBuyItNowPrice(reader.get(i++));
                superfeedjebean.setRetailPrice(reader.get(i++));
                superfeedjebean.setPictureURLs(reader.get(i++));
                superfeedjebean.setTaxProductCode(reader.get(i++));
                superfeedjebean.setSupplierCode(reader.get(i++));
                superfeedjebean.setSupplierPO(reader.get(i++));
                superfeedjebean.setWarehouseLocation(reader.get(i++));
                superfeedjebean.setInventorySubtitle(reader.get(i++));
                superfeedjebean.setRelationshipName(reader.get(i++));
                superfeedjebean.setVariationParentSKU(reader.get(i++));
                superfeedjebean.setLabels(reader.get(i++));
                superfeedjebean.setAttribute1Name(reader.get(i++));
                superfeedjebean.setAttribute1Value(reader.get(i++));
                superfeedjebean.setAttribute2Name(reader.get(i++));
                superfeedjebean.setAttribute2Value(reader.get(i++));
                superfeedjebean.setAttribute3Name(reader.get(i++));
                superfeedjebean.setAttribute3Value(reader.get(i++));
                superfeedjebean.setAttribute4Name(reader.get(i++));
                superfeedjebean.setAttribute4Value(reader.get(i++));
                superfeedjebean.setAttribute5Name(reader.get(i++));
                superfeedjebean.setAttribute5Value(reader.get(i++));
                superfeedjebean.setAttribute6Name(reader.get(i++));
                superfeedjebean.setAttribute6Value(reader.get(i++));
                superfeedjebean.setAttribute7Name(reader.get(i++));
                superfeedjebean.setAttribute7Value(reader.get(i++));
                superfeedjebean.setAttribute8Name(reader.get(i++));
                superfeedjebean.setAttribute8Value(reader.get(i++));
                superfeedjebean.setAttribute9Name(reader.get(i++));
                superfeedjebean.setAttribute9Value(reader.get(i++));
                superfeedjebean.setAttribute10Name(reader.get(i++));
                superfeedjebean.setAttribute10Value(reader.get(i++));
                superfeedjebean.setAttribute11Name(reader.get(i++));
                superfeedjebean.setAttribute11Value(reader.get(i++));
                superfeedjebean.setAttribute12Name(reader.get(i++));
                superfeedjebean.setAttribute12Value(reader.get(i++));
                superfeedjebean.setAttribute13Name(reader.get(i++));
                superfeedjebean.setAttribute13Value(reader.get(i++));
                superfeedjebean.setAttribute14Name(reader.get(i++));
                superfeedjebean.setAttribute14Value(reader.get(i++));
                superfeedjebean.setAttribute15Name(reader.get(i++));
                superfeedjebean.setAttribute15Value(reader.get(i++));
                superfeedjebean.setAttribute16Name(reader.get(i++));
                superfeedjebean.setAttribute16Value(reader.get(i++));
                superfeedjebean.setAttribute17Name(reader.get(i++));
                superfeedjebean.setAttribute17Value(reader.get(i++));
                superfeedjebean.setAttribute18Name(reader.get(i++));
                superfeedjebean.setAttribute18Value(reader.get(i++));
                superfeedjebean.setAttribute19Name(reader.get(i++));
                superfeedjebean.setAttribute19Value(reader.get(i++));
                superfeedjebean.setAttribute20Name(reader.get(i++));
                superfeedjebean.setAttribute20Value(reader.get(i++));
                superfeedjebean.setAttribute21Name(reader.get(i++));
                superfeedjebean.setAttribute21Value(reader.get(i++));
                superfeedjebean.setAttribute22Name(reader.get(i++));
                superfeedjebean.setAttribute22Value(reader.get(i++));
                superfeedjebean.setAttribute23Name(reader.get(i++));
                superfeedjebean.setAttribute23Value(reader.get(i++));
                superfeedjebean.setAttribute24Name(reader.get(i++));
                superfeedjebean.setAttribute24Value(reader.get(i++));
                superfeedjebean.setAttribute25Name(reader.get(i++));
                superfeedjebean.setAttribute25Value(reader.get(i++));
                superfeedjebean.setAttribute26Name(reader.get(i++));
                superfeedjebean.setAttribute26Value(reader.get(i++));
                superfeedjebean.setAttribute27Name(reader.get(i++));
                superfeedjebean.setAttribute27Value(reader.get(i++));
                superfeedjebean.setAttribute28Name(reader.get(i++));
                superfeedjebean.setAttribute28Value(reader.get(i++));
                superfeedjebean.setAttribute29Name(reader.get(i++));
                superfeedjebean.setAttribute29Value(reader.get(i++));
                superfeedjebean.setAttribute30Name(reader.get(i++));
                superfeedjebean.setAttribute30Value(reader.get(i++));
                superfeedjebean.setAttribute31Name(reader.get(i++));
                superfeedjebean.setAttribute31Value(reader.get(i++));
                superfeedjebean.setAttribute32Name(reader.get(i++));
                superfeedjebean.setAttribute32Value(reader.get(i++));
                superfeedjebean.setAttribute33Name(reader.get(i++));
                superfeedjebean.setAttribute33Value(reader.get(i++));
                superfeedjebean.setAttribute34Name(reader.get(i++));
                superfeedjebean.setAttribute34Value(reader.get(i++));
                superfeedjebean.setAttribute35Name(reader.get(i++));
                superfeedjebean.setAttribute35Value(reader.get(i++));
                superfeedjebean.setAttribute36Name(reader.get(i++));
                superfeedjebean.setAttribute36Value(reader.get(i++));
                superfeedjebean.setAttribute37Name(reader.get(i++));
                superfeedjebean.setAttribute37Value(reader.get(i++));
                superfeedjebean.setAttribute38Name(reader.get(i++));
                superfeedjebean.setAttribute38Value(reader.get(i++));
                superfeedjebean.setAttribute39Name(reader.get(i++));
                superfeedjebean.setAttribute39Value(reader.get(i++));
                superfeedjebean.setAttribute40Name(reader.get(i++));
                superfeedjebean.setAttribute40Value(reader.get(i++));
                superfeedjebean.setAttribute41Name(reader.get(i++));
                superfeedjebean.setAttribute41Value(reader.get(i++));
                superfeedjebean.setAttribute42Name(reader.get(i++));
                superfeedjebean.setAttribute42Value(reader.get(i++));
                superfeedjebean.setAttribute43Name(reader.get(i++));
                superfeedjebean.setAttribute43Value(reader.get(i++));
                superfeedjebean.setAttribute44Name(reader.get(i++));
                superfeedjebean.setAttribute44Value(reader.get(i++));
                superfeedjebean.setAttribute47Name(reader.get(i++));
                superfeedjebean.setAttribute47Value(reader.get(i++));
                superfeedjebean.setAttribute48Name(reader.get(i++));
                superfeedjebean.setAttribute48Value(reader.get(i++));
                superfeedjebean.setAttribute49Name(reader.get(i++));
                superfeedjebean.setAttribute49Value(reader.get(i++));
                superfeedjebean.setAttribute50Name(reader.get(i++));
                superfeedjebean.setAttribute50Value(reader.get(i++));
                superfeedjebean.setAttribute51Name(reader.get(i++));
                superfeedjebean.setAttribute51Value(reader.get(i++));
                superfeedjebean.setAttribute52Name(reader.get(i++));
                superfeedjebean.setAttribute52Value(reader.get(i++));
                superfeedjebean.setAttribute53Name(reader.get(i++));
                superfeedjebean.setAttribute53Value(reader.get(i++));
                superfeedjebean.setAttribute54Name(reader.get(i++));
                superfeedjebean.setAttribute54Value(reader.get(i++));
                superfeedjebean.setAttribute55Name(reader.get(i++));
                superfeedjebean.setAttribute55Value(reader.get(i++));
                superfeedjebean.setAttribute56Name(reader.get(i++));
                superfeedjebean.setAttribute56Value(reader.get(i++));
                superfeedjebean.setAttribute57Name(reader.get(i++));
                superfeedjebean.setAttribute57Value(reader.get(i++));
                superfeedjebean.setAttribute58Name(reader.get(i++));
                superfeedjebean.setAttribute58Value(reader.get(i++));
                superfeedjebean.setAttribute59Name(reader.get(i++));
                superfeedjebean.setAttribute59Value(reader.get(i++));
                superfeedjebean.setAttribute60Name(reader.get(i++));
                superfeedjebean.setAttribute60Value(reader.get(i++));
                superfeedjebean.setAttribute61Name(reader.get(i++));
                superfeedjebean.setAttribute61Value(reader.get(i++));
                superfeedjebean.setAttribute62Name(reader.get(i++));
                superfeedjebean.setAttribute62Value(reader.get(i++));
                superfeedjebean.setAttribute63Name(reader.get(i++));
                superfeedjebean.setAttribute63Value(reader.get(i++));
                superfeedjebean.setAttribute64Name(reader.get(i++));
                superfeedjebean.setAttribute64Value(reader.get(i++));
                superfeedjebean.setAttribute65Name(reader.get(i++));
                superfeedjebean.setAttribute65Value(reader.get(i++));
                superfeedjebean.setAttribute66Name(reader.get(i++));
                superfeedjebean.setAttribute66Value(reader.get(i++));
                superfeedjebean.setAttribute67Name(reader.get(i++));
                superfeedjebean.setAttribute67Value(reader.get(i++));
                superfeedjebean.setAttribute68Name(reader.get(i++));
                superfeedjebean.setAttribute68Value(reader.get(i++));
                superfeedjebean.setAttribute69Name(reader.get(i++));
                superfeedjebean.setAttribute69Value(reader.get(i++));
                superfeedjebean.setAttribute70Name(reader.get(i++));
                superfeedjebean.setAttribute70Value(reader.get(i++));
                superfeedjebean.setAttribute71Name(reader.get(i++));
                superfeedjebean.setAttribute71Value(reader.get(i++));
                superfeedjebean.setAttribute72Name(reader.get(i++));
                superfeedjebean.setAttribute72Value(reader.get(i++));
                superfeedjebean.setAttribute73Name(reader.get(i++));
                superfeedjebean.setAttribute73Value(reader.get(i++));
                superfeedjebean.setAttribute74Name(reader.get(i++));
                superfeedjebean.setAttribute74Value(reader.get(i++));
                superfeedjebean.setAttribute75Name(reader.get(i++));
                superfeedjebean.setAttribute75Value(reader.get(i++));
                superfeedjebean.setAttribute76Name(reader.get(i++));
                superfeedjebean.setAttribute76Value(reader.get(i++));
                superfeedjebean.setAttribute77Name(reader.get(i++));
                superfeedjebean.setAttribute77Value(reader.get(i++));
                superfeedjebean.setAttribute78Name(reader.get(i++));
                superfeedjebean.setAttribute78Value(reader.get(i++));
                superfeedjebean.setAttribute79Name(reader.get(i++));
                superfeedjebean.setAttribute79Value(reader.get(i++));
                superfeedjebean.setAttribute80Name(reader.get(i++));
                superfeedjebean.setAttribute80Value(reader.get(i++));
                superfeedjebean.setAttribute81Name(reader.get(i++));
                superfeedjebean.setAttribute81Value(reader.get(i++));
                superfeedjebean.setAttribute82Name(reader.get(i++));
                superfeedjebean.setAttribute82Value(reader.get(i++));
                superfeedjebean.setAttribute83Name(reader.get(i++));
                superfeedjebean.setAttribute83Value(reader.get(i++));
                superfeedjebean.setAttribute84Name(reader.get(i++));
                superfeedjebean.setAttribute84Value(reader.get(i++));
                superfeedjebean.setAttribute85Name(reader.get(i++));
                superfeedjebean.setAttribute85Value(reader.get(i++));
                superfeedjebean.setAttribute86Name(reader.get(i++));
                superfeedjebean.setAttribute86Value(reader.get(i++));
                superfeedjebean.setAttribute87Name(reader.get(i++));
                superfeedjebean.setAttribute87Value(reader.get(i++));
                superfeedjebean.setAttribute88Name(reader.get(i++));
                superfeedjebean.setAttribute88Value(reader.get(i++));
                superfeedjebean.setAttribute89Name(reader.get(i++));
                superfeedjebean.setAttribute89Value(reader.get(i++));
                superfeedjebean.setAttribute90Name(reader.get(i++));
                superfeedjebean.setAttribute90Value(reader.get(i++));
                superfeedjebean.setAttribute91Name(reader.get(i++));
                superfeedjebean.setAttribute91Value(reader.get(i++));
                superfeedjebean.setAttribute92Name(reader.get(i++));
                superfeedjebean.setAttribute92Value(reader.get(i++));
                superfeedjebean.setAttribute93Name(reader.get(i++));
                superfeedjebean.setAttribute93Value(reader.get(i++));
                superfeedjebean.setAttribute94Name(reader.get(i++));
                superfeedjebean.setAttribute94Value(reader.get(i++));
                superfeedjebean.setHarmonizedCode(reader.get(i++));
                superfeedjebean.setHeight(reader.get(i++));
                superfeedjebean.setLength(reader.get(i++));
                superfeedjebean.setWidth(reader.get(i++));
                superfeedjebean.setDCCode(reader.get(i++));
                superfeedjebean.setClassification(reader.get(i++));
                superfeedjebean.setAttribute95Name(reader.get(i++));
                superfeedjebean.setAttribute95Value(reader.get(i++));
                superfeedjebean.setAttribute96Name(reader.get(i++));
                superfeedjebean.setAttribute96Value(reader.get(i++));

                // 97 / 98 (2015-12-03 16:22:59 - jonas add)
                superfeedjebean.setAttribute97Name(reader.get(i++));
                superfeedjebean.setAttribute97Value(reader.get(i++));
                superfeedjebean.setAttribute98Name(reader.get(i++));
                superfeedjebean.setAttribute98Value(reader.get(i));

                superfeed.add(superfeedjebean);
            }

            reader.close();
            $info("JE产品文件读入完成");
        } catch (Exception ex) {
            $info("JE产品文件读入失败");
            logIssue("cms 数据导入处理", "JE产品文件读入失败 " + ex.getMessage());
        }
        return superfeed;
    }

    /**
     * LC产品文件读入
     */
    public List<SuperFeedLCBean> lcSuperFeedImport() {
        $info("LC产品文件读入开始");

        List<SuperFeedLCBean> superfeed = new ArrayList<>();

        CsvReader reader;
        try {
            reader = new CsvReader(new FileInputStream(Feeds.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(), FeedEnums.Name.feed_ftp_localpath) + "/"
                    + Feeds.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(), FeedEnums.Name.file_id)), ',', Charset.forName(Feeds.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(), FeedEnums.Name.feed_ftp_file_coding)));
            // Head读入
            reader.readHeaders();
            reader.getHeaders();

            // Body读入
            while (reader.readRecord()) {
                SuperFeedLCBean superfeedlcbean = new SuperFeedLCBean();

                int i = 0;
                superfeedlcbean.setPath(reader.get(i++));
                superfeedlcbean.setName(reader.get(i++));
                superfeedlcbean.setCode(reader.get(i++));
                superfeedlcbean.setSub_code(reader.get(i++));
                superfeedlcbean.setOriginal_price(reader.get(i++));
                superfeedlcbean.setPrice(reader.get(i++));
                superfeedlcbean.setSale_price(reader.get(i++));
                superfeedlcbean.setOptions(reader.get(i++));
                superfeedlcbean.setHeadline(reader.get(i++));
                superfeedlcbean.setCaption(reader.get(i++));
                superfeedlcbean.setAbstract(reader.get(i++));
                superfeedlcbean.setExplanation(reader.get(i++));
                superfeedlcbean.setAdditional1(reader.get(i++));
                superfeedlcbean.setAdditional2(reader.get(i++));
                superfeedlcbean.setAdditional3(reader.get(i++));
                superfeedlcbean.setRelevant_links(reader.get(i++));
                superfeedlcbean.setShip_weight(reader.get(i++));
                superfeedlcbean.setTaxable(reader.get(i++));
                superfeedlcbean.setRelease_date(reader.get(i++));
                superfeedlcbean.setTemporary_point_term(reader.get(i++));
                superfeedlcbean.setPoint_code(reader.get(i++));
                superfeedlcbean.setMeta_key(reader.get(i++));
                superfeedlcbean.setMeta_desc(reader.get(i++));
                superfeedlcbean.setDisplay(reader.get(i++));
                superfeedlcbean.setTemplate(reader.get(i++));
                superfeedlcbean.setSale_period_start(reader.get(i++));
                superfeedlcbean.setSale_period_end(reader.get(i++));
                superfeedlcbean.setSale_limit(reader.get(i++));
                superfeedlcbean.setSp_code(reader.get(i++));
                superfeedlcbean.setBrand_code(reader.get(i++));
                superfeedlcbean.setYahoo_product_code(reader.get(i++));
                superfeedlcbean.setProduct_code(reader.get(i++));
                superfeedlcbean.setJan(reader.get(i++));
                superfeedlcbean.setDelivery(reader.get(i++));
                superfeedlcbean.setAstk_code(reader.get(i++));
                superfeedlcbean.setCondition(reader.get(i++));
                superfeedlcbean.setProduct_category(reader.get(i++));
                superfeedlcbean.setSpec1(reader.get(i++));
                superfeedlcbean.setSpec2(reader.get(i++));
                superfeedlcbean.setSpec3(reader.get(i++));
                superfeedlcbean.setSpec4(reader.get(i++));
                superfeedlcbean.setSpec5(reader.get(i++));
                superfeedlcbean.setSort(reader.get(i++));
                superfeedlcbean.setSp_additional(reader.get(i));

                superfeed.add(superfeedlcbean);
            }

            reader.close();
            $info("LC产品文件读入完成");
        } catch (Exception ex) {
            $info("LC产品文件读入失败");
            logIssue("cms 数据导入处理", "LC产品文件读入失败 " + ex.getMessage());
        }
        return superfeed;
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeedJE(List<SuperFeedJEBean> superfeedlist) {
        boolean isSuccess = true;
        $info("JE产品信息插入开始");

        // 清表
        if (superfeeddao.deleteTableInfo(Feeds.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(), FeedEnums.Name.table_id)) >= 0) {
            for (SuperFeedJEBean superfeed : superfeedlist) {

                if (superfeeddao.insertSuperfeedJEInfo(superfeed) <= 0) {
                    $info("JE产品信息插入失败 InventoryNumber = " + superfeed.getInventoryNumber());
                }
            }
        } else {
            isSuccess = false;
            $info("JE产品信息插入失败 清TMP表失败");
        }
        $info("JE产品信息插入完成");
        return isSuccess;
    }

    /**
     * LC产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeedLC(List<SuperFeedLCBean> superfeedlist) {
        boolean isSuccess = true;
        $info("LC产品信息插入开始");

        // 清表
        if (superfeeddao.deleteTableInfo(Feeds.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(), FeedEnums.Name.table_id)) >= 0) {
            for (SuperFeedLCBean superfeed : superfeedlist) {

                if (superfeeddao.insertSuperfeedLCInfo(superfeed) <= 0) {
                    $info("LC产品信息插入失败 code = " + superfeed.getCode());
                }
            }
        } else {
            isSuccess = false;
            $info("LC产品信息插入失败 清TMP表失败");
        }
        $info("LC产品信息插入完成");
        return isSuccess;
    }

    /**
     * CheckSuperFeed 产品信息
     * 异常数据清除 新数据，更新数据划分。
     *
     * @return isSuccessS
     */
    public boolean CheckSuperFeed(String channel_id) {
        boolean isSuccess = true;
        $info("异常数据清除开始");


        // 取得category异常数据
        List<String> err_categorylist = superfeeddao.selectErrData(Feeds.getVal1(channel_id, FeedEnums.Name.feed_delete_category_err_sql));

        // 异常数据-邮件提示
        String err_data_maill = "category异常:";
        // 删除异常数据
        String err_data = "";
        for (String anErr_categorylist : err_categorylist) {
            err_data = err_data + " '" + anErr_categorylist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feeds.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得model异常数据
        List<String> err_modellist = superfeeddao.selectErrData(Feeds.getVal1(channel_id, FeedEnums.Name.feed_delete_model_sql));
        err_data_maill = "model异常:";
        err_data = "";
        for (String anErr_modellist : err_modellist) {
            err_data = err_data + " '" + anErr_modellist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feeds.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得product异常数据
        List<String> err_productlist = superfeeddao.selectErrData(Feeds.getVal1(channel_id, FeedEnums.Name.feed_delete_product_sql));
        err_data = "";
        err_data_maill = "product异常:";
        for (String anErr_productlist : err_productlist) {
            err_data = err_data + " '" + anErr_productlist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feeds.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得有model无product异常数据
        List<String> err_model_noproductlist = superfeeddao.selectErrData(Feeds.getVal1(channel_id, FeedEnums.Name.feed_delete_model_no_product_sql));
        err_data = "";
        err_data_maill = "有model无product异常:";
        for (String anErr_model_noproductlist : err_model_noproductlist) {
            err_data = err_data + " '" + anErr_model_noproductlist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feeds.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }

        // 取得有prouduct无model异常数据
        List<String> err_product_nomodellist = superfeeddao.selectErrData(Feeds.getVal1(channel_id, FeedEnums.Name.feed_delete_product_no_model_sql));
        err_data = "";
        err_data_maill = "有prouduct无model异常:";
        for (String anErr_product_nomodellist : err_product_nomodellist) {
            err_data = err_data + " '" + anErr_product_nomodellist + "',";
        }
        err_data_maill = err_data_maill + err_data;
        // 存在异常数据
        if (err_data.length() > 0) {
            logIssue("cms 数据导入处理", "异常数据清除对象=>" + err_data_maill);

            // 去掉最后一个“，”
            err_data = Feeds.getVal1(channel_id, FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0, err_data.lastIndexOf(",")) + ")";

            if (superfeeddao.deleteErrData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id), err_data) <= 0) {
                //异常数据清除失败
                $info("异常数据清除失败");
                logIssue("cms 数据导入处理", "异常数据清除失败");
                isSuccess = false;
            }
        }
        $info("异常数据清除结束");

        // UpdateFlag : 1:插入 2:更新 3:两者都有 0:不处理
        $info("数据判定开始");
        if (isSuccess) {
            // insert data
            List<String> insert_data = superfeeddao.inertSuperfeedInsertData(Feeds.getVal1(channel_id, FeedEnums.Name.category_column),
                    Feeds.getVal1(channel_id, FeedEnums.Name.feed_model_key),
                    Feeds.getVal1(channel_id, FeedEnums.Name.feed_code_key),
                    Feeds.getVal1(channel_id, FeedEnums.Name.table_id),
                    Feeds.getVal1(channel_id, FeedEnums.Name.table_id) + "_full");

            // update data
            List<String> update_data = superfeeddao.inertSuperfeedUpdateData(Feeds.getVal1(channel_id, FeedEnums.Name.feed_code_key),
                    Feeds.getVal1(channel_id, FeedEnums.Name.table_id),
                    Feeds.getVal1(channel_id, FeedEnums.Name.table_id) + "_full");

            // 新数据_code
            String str_code_insert = "";
            // 更新数据_code
            String str_code_update = "";
            // 插入数据_code 存在
            if (insert_data.size() > 0) {
                for (String anInsert_data : insert_data) {
                    str_code_insert = str_code_insert + "'" + anInsert_data + "',";
                }
                // 去掉最后一个“，”
                str_code_insert = str_code_insert.substring(0, str_code_insert.lastIndexOf(","));

                // 插入数据 更新UpdateFlag :1
                int reslut_insert = superfeeddao.updateInsertData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id),
                        Feeds.getVal1(channel_id, FeedEnums.Name.product_keyword),
                        Feeds.getVal1(channel_id, FeedEnums.Name.feed_code_key),
                        str_code_insert);

                if (reslut_insert <= 0) {
                    logIssue("cms 数据导入处理", "更新UpdateFlag :1 ( for product ), 无受影响行数");
                }

                // 插入数据 补足没有model的数据
                int reslut_insertmodel = superfeeddao.updateInsertModelData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id), Feeds.getVal1(channel_id, FeedEnums.Name.feed_model_key),
                        Feeds.getVal1(channel_id, FeedEnums.Name.feed_code_key), str_code_insert, Feeds.getVal1(channel_id, FeedEnums.Name.feed_model_keyword));
                if (reslut_insertmodel < 0) {
                    logIssue("cms 数据导入处理", "更新UpdateFlag :1 ( for model ), 无受影响行数");
                }
            }

            // 更新数据_code 存在
            if (update_data.size() > 0) {
                for (String anUpdate_data : update_data) {
                    str_code_update = str_code_update + "'" + anUpdate_data + "',";
                }
                // 去掉最后一个“，”
                str_code_update = str_code_update.substring(0, str_code_update.lastIndexOf(","));

                // 更新数据 更新UpdateFlag :2，3
                int reslut_update = superfeeddao.updateUpdateData(Feeds.getVal1(channel_id, FeedEnums.Name.table_id), Feeds.getVal1(channel_id, FeedEnums.Name.feed_code_key), str_code_update);
                if (reslut_update <= 0) {
                    logIssue("cms 数据导入处理", "更新UpdateFlag :2，3 ( for all ), 无受影响行数");
                }
            }
        }
        $info("数据判定结束");

        return isSuccess;
    }

    /**
     * NewProductFeed 新产品处理
     *
     * @return isSuccess
     */
    public boolean NewProductFeed(String channel_id) {
        $info("新产品处理开始");

        // 只处理新数据
        String keyword = " and UpdateFlag in ('1','3')";

        // 取得所有Superfeed category划分
        List<String> superfeedjebeanlist = superfeeddao.selectSuperfeedCategory(Feeds.getVal1(channel_id, FeedEnums.Name.category_column), Feeds.getVal1(channel_id, FeedEnums.Name.table_id), keyword);

        for (String aSuperfeedjebeanlist : superfeedjebeanlist) {

            boolean isPostSuccess = true;

            // CategoryModel ModelBean ProductBean 以Category单位
            List<CategoryBean> categoryBeans = new ArrayList<>();
            List<ModelBean> modelBeans = new ArrayList<>();

            $info("新产品category= " + aSuperfeedjebeanlist);

            // category
            String[] strarray = aSuperfeedjebeanlist.split(Feeds.getVal1(channel_id, FeedEnums.Name.category_split));
            for (int j = 0; j < strarray.length; j++) {
                CategoryBean categoryBean = new CategoryBean();

                // 父层
                if (j == 0) {
                    categoryBean.setC_name(strarray[j]);
                    categoryBean.setC_header_title(strarray[j]);
                    // 全部小写,且空格用-替代
                    categoryBean.setUrl_key(formaturl(channel_id, strarray[j], "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));

                } else {
                    // 子层
                    categoryBean.setC_name(strarray[j]);
                    categoryBean.setC_header_title(strarray[j]);
                    // 全部小写,且空格用-替代
                    categoryBean.setUrl_key(categoryBeans.get(categoryBeans.size() - 1).getUrl_key() + '-' + formaturl(channel_id, strarray[j], "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));
                    categoryBean.setParent_url_key(categoryBeans.get(categoryBeans.size() - 1).getUrl_key());
                }

                categoryBean.setC_is_enable_filter("1");
                categoryBean.setC_is_visible_on_menu("0");
                categoryBean.setC_is_published("0");
                categoryBean.setC_is_effective("1");

                // 最终category
                if (j >= strarray.length - 1) {
                    // model 取得
                    modelBeans = createModel(channel_id, aSuperfeedjebeanlist, keyword);
                    for (int k = 0; k < modelBeans.size(); k++) {
                        ModelBean modelbean = modelBeans.get(k);

                        List<ProductBean> productBeans;
                        // product 取得
                        productBeans = createProduct(channel_id, aSuperfeedjebeanlist, modelbean.getM_model(), "", keyword, Feeds.getVal1(channel_id, FeedEnums.Name.table_id));

                        $info("新产品product= " + productBeans.get(0).getP_code());

                        // product 设定
                        modelbean.setProductbeans(productBeans);
                        modelBeans.set(k, modelbean);
                    }
                }
                categoryBeans.add(categoryBean);
            }

            //新产品Attribute处理
            if (!AttributeInsert(channel_id, keyword, aSuperfeedjebeanlist, "")) {
                $info("新产品Attribute处理异常");

                // 更新update数据flag
                superfeeddao.changeUpdateDateFlag(Feeds.getVal1(channel_id, FeedEnums.Name.table_id),
                        keyword,
                        Feeds.getVal1(channel_id, FeedEnums.Name.category_column),
                        "'" + aSuperfeedjebeanlist + "'");
                continue;
            }

            // model 数量
            int model_size = 0;
            // category 数量
            int category_size = categoryBeans.size();
            if (category_size > 0) {
                model_size = modelBeans.size();
            }

            // 100 model 推送一次
            int post_max_model = Integer.parseInt(Feeds.getVal1(channel_id, FeedEnums.Name.post_max_model));
            int post_count = model_size / post_max_model;
            if (model_size % post_max_model > 0) {
                post_count = post_count + 1;
            }

            // 异常数据
            List<String> modelFailList = new ArrayList<>();
            List<String> productFailList = new ArrayList<>();

            for (int model_i = 0; model_i < post_count; model_i++) {
                // post productsFeed
                ProductsFeedInsert productsFeed = new ProductsFeedInsert();

                // model设定
                int post_i = 0;
                List<ModelBean> post_modelBeans = new ArrayList<>();
                for (int model_j = model_i * post_max_model; model_j < model_i * post_max_model + post_max_model; model_j++) {
                    if (model_j > model_size - 1) {
                        break;
                    }
                    post_modelBeans.add(post_i, modelBeans.get(model_j));
                    post_i++;
                }

                categoryBeans.get(category_size - 1).setModelbeans(post_modelBeans);
                productsFeed.setCategorybeans(categoryBeans);
                productsFeed.setChannel_id(channel_id);


                /************* 2015-12-31 16:29:55 By Jonas *************/
                // 在输出之前输出所有将处理的 Model 标识类字段
                $info("准备调用 WS API");
                for (CategoryBean categoryBean : categoryBeans) {
                    $info("\t处理类目是: %s", categoryBean.getUrl_key());
                    for (ModelBean modelBean : notNull(categoryBean.getModelbeans())) {
                        $info("\t\t处理 Model 是: %s", modelBean.getUrl_key());
                        for (ProductBean productBean : notNull(modelBean.getProductbeans())) {
                            $info("\t\t\t处理 Product Code: %s", productBean.getP_code());
                        }
                    }
                }
                /************* 2015-12-31 16:29:55 By Jonas *************/

                // post web servies
                WsdlResponseBean wsdlResponseBean = jsonBeanOutInsert(channel_id, productsFeed);

                // 处理失败
                if (wsdlResponseBean == null) {
                    $info("cms 数据导入处理，新产品推送失败！");
                    isPostSuccess = false;
                    break;
                }

                ProductFeedResponseBean productFeedResponseBean = JsonUtil.jsonToBean(JsonUtil.getJsonString(wsdlResponseBean.getResultInfo()), ProductFeedResponseBean.class);

                /************* 2015-12-31 16:29:55 By Jonas *************/
                // 输出处理结果
                $info("调用 WS API 完成");
                $info("\t最终结果: %s", wsdlResponseBean.getResult());
                $info("\t携带信息及代码: %s / %s", wsdlResponseBean.getMessageCode(), wsdlResponseBean.getMessage());
                $info("\t以下是成功的");
                for (ProductFeedDetailBean productFeedDetailBean : notNull(productFeedResponseBean.getSuccess())) {
                    $info("\t\t(%s)%s", productFeedDetailBean.getBeanType(), productFeedDetailBean.getDealObject().getUrl_key());
                }
                $info("\t以下是失败的");
                for (ProductFeedDetailBean productFeedDetailBean : notNull(productFeedResponseBean.getFailure())) {
                    $info("\t\t(%s)%s", productFeedDetailBean.getResultMessage(), productFeedDetailBean.getDealObject().getUrl_key());
                }
                $info("调用日志输出完毕");
                /************* 2015-12-31 16:29:55 By Jonas *************/

                if (wsdlResponseBean.getResult().equals("OK") && productFeedResponseBean.getSuccess().size() > 0) {
                    // 出错统计
                    List<ProductFeedDetailBean> productFeedDetailBeans = productFeedResponseBean.getFailure();
                    for (ProductFeedDetailBean productFeedDetailBean : productFeedDetailBeans) {
                        //  处理类型 1:category 无; 2:model
                        if (productFeedDetailBean.getBeanType() == 2) {
                            modelFailList.add(productFeedDetailBean.getDealObject().getModel());
                        }
                        //  处理类型 3:product; 4:item
                        if (productFeedDetailBean.getBeanType() == 3 || productFeedDetailBean.getBeanType() == 4) {
                            productFailList.add(productFeedDetailBean.getDealObject().getCode());
                        }
                    }
                }

                if (wsdlResponseBean.getResult().equals("NG")) {
                    logIssue("cms 数据导入处理", "新产品推送失败：Message=" + wsdlResponseBean.getMessage());
                    isPostSuccess = false;
                    break;
                }
            }

            if (isPostSuccess) {
                // 插入ZZ_Work_Superfeed_Full
                if (!SuperfeedFullInsert(aSuperfeedjebeanlist, channel_id, modelFailList, productFailList)) {
                    $error("插入ZZ_Work_Superfeed_Full 失败");
                    $info("插入ZZ_Work_Superfeed_Full 失败");
                }
            } else {
//				// 更新update数据flag
                // 更新update数据flag
                superfeeddao.changeUpdateDateFlag(Feeds.getVal1(channel_id, FeedEnums.Name.table_id),
                        keyword,
                        Feeds.getVal1(channel_id, FeedEnums.Name.category_column),
                        "'" + aSuperfeedjebeanlist + "'");
            }
        }

        $info("新产品处理完成");
        return true;
    }

    /**
     * Attribute 属性插入、更新处理
     *
     * @param channel_id keyword
     * @return isSuccess
     */
    public boolean AttributeInsert(String channel_id, String keyword, String category, String product) {
        boolean isSuccess = true;

        AttributeBean attributebean_param = new AttributeBean();

        attributebean_param.setCategory_url_key(Feeds.getVal1(channel_id, FeedEnums.Name.product_category_url_key));
        attributebean_param.setModel_url_key(Feeds.getVal1(channel_id, FeedEnums.Name.product_model_url_key));
        attributebean_param.setProduct_url_key(Feeds.getVal1(channel_id, FeedEnums.Name.product_url_key));
        attributebean_param.setAttribute1(Feeds.getVal1(channel_id, FeedEnums.Name.attribute1));
        attributebean_param.setAttribute2(Feeds.getVal1(channel_id, FeedEnums.Name.attribute2));
        attributebean_param.setAttribute3(Feeds.getVal1(channel_id, FeedEnums.Name.attribute3));
        attributebean_param.setAttribute4(Feeds.getVal1(channel_id, FeedEnums.Name.attribute4));
        attributebean_param.setAttribute5(Feeds.getVal1(channel_id, FeedEnums.Name.attribute5));
        attributebean_param.setAttribute6(Feeds.getVal1(channel_id, FeedEnums.Name.attribute6));
        attributebean_param.setAttribute7(Feeds.getVal1(channel_id, FeedEnums.Name.attribute7));
        attributebean_param.setAttribute8(Feeds.getVal1(channel_id, FeedEnums.Name.attribute8));
        attributebean_param.setAttribute9(Feeds.getVal1(channel_id, FeedEnums.Name.attribute9));
        attributebean_param.setAttribute10(Feeds.getVal1(channel_id, FeedEnums.Name.attribute10));
        attributebean_param.setAttribute11(Feeds.getVal1(channel_id, FeedEnums.Name.attribute11));
        attributebean_param.setAttribute12(Feeds.getVal1(channel_id, FeedEnums.Name.attribute12));
        attributebean_param.setAttribute13(Feeds.getVal1(channel_id, FeedEnums.Name.attribute13));
        attributebean_param.setAttribute14(Feeds.getVal1(channel_id, FeedEnums.Name.attribute14));
        attributebean_param.setAttribute15(Feeds.getVal1(channel_id, FeedEnums.Name.attribute15));
        attributebean_param.setAttribute16(Feeds.getVal1(channel_id, FeedEnums.Name.attribute16));
        attributebean_param.setAttribute17(Feeds.getVal1(channel_id, FeedEnums.Name.attribute17));
        attributebean_param.setAttribute18(Feeds.getVal1(channel_id, FeedEnums.Name.attribute18));
        attributebean_param.setAttribute19(Feeds.getVal1(channel_id, FeedEnums.Name.attribute19));
        attributebean_param.setAttribute20(Feeds.getVal1(channel_id, FeedEnums.Name.attribute20));
        attributebean_param.setAttribute21(Feeds.getVal1(channel_id, FeedEnums.Name.attribute21));
        attributebean_param.setAttribute22(Feeds.getVal1(channel_id, FeedEnums.Name.attribute22));
        attributebean_param.setAttribute23(Feeds.getVal1(channel_id, FeedEnums.Name.attribute23));
        attributebean_param.setAttribute24(Feeds.getVal1(channel_id, FeedEnums.Name.attribute24));
        attributebean_param.setAttribute25(Feeds.getVal1(channel_id, FeedEnums.Name.attribute25));
        attributebean_param.setAttribute26(Feeds.getVal1(channel_id, FeedEnums.Name.attribute26));
        attributebean_param.setAttribute27(Feeds.getVal1(channel_id, FeedEnums.Name.attribute27));
        attributebean_param.setAttribute28(Feeds.getVal1(channel_id, FeedEnums.Name.attribute28));
        attributebean_param.setAttribute29(Feeds.getVal1(channel_id, FeedEnums.Name.attribute29));
        attributebean_param.setAttribute30(Feeds.getVal1(channel_id, FeedEnums.Name.attribute30));
        attributebean_param.setAttribute31(Feeds.getVal1(channel_id, FeedEnums.Name.attribute31));
        attributebean_param.setAttribute32(Feeds.getVal1(channel_id, FeedEnums.Name.attribute32));
        attributebean_param.setAttribute33(Feeds.getVal1(channel_id, FeedEnums.Name.attribute33));
        attributebean_param.setAttribute34(Feeds.getVal1(channel_id, FeedEnums.Name.attribute34));
        attributebean_param.setAttribute35(Feeds.getVal1(channel_id, FeedEnums.Name.attribute35));
        attributebean_param.setAttribute36(Feeds.getVal1(channel_id, FeedEnums.Name.attribute36));
        attributebean_param.setAttribute37(Feeds.getVal1(channel_id, FeedEnums.Name.attribute37));
        attributebean_param.setAttribute37(Feeds.getVal1(channel_id, FeedEnums.Name.attribute37));
        attributebean_param.setAttribute38(Feeds.getVal1(channel_id, FeedEnums.Name.attribute38));
        attributebean_param.setAttribute39(Feeds.getVal1(channel_id, FeedEnums.Name.attribute39));
        attributebean_param.setAttribute40(Feeds.getVal1(channel_id, FeedEnums.Name.attribute40));
        attributebean_param.setAttribute41(Feeds.getVal1(channel_id, FeedEnums.Name.attribute41));
        attributebean_param.setAttribute42(Feeds.getVal1(channel_id, FeedEnums.Name.attribute42));
        attributebean_param.setAttribute43(Feeds.getVal1(channel_id, FeedEnums.Name.attribute43));
        attributebean_param.setAttribute44(Feeds.getVal1(channel_id, FeedEnums.Name.attribute44));
        attributebean_param.setAttribute45(Feeds.getVal1(channel_id, FeedEnums.Name.attribute45));
        attributebean_param.setAttribute46(Feeds.getVal1(channel_id, FeedEnums.Name.attribute46));
        attributebean_param.setAttribute47(Feeds.getVal1(channel_id, FeedEnums.Name.attribute47));
        attributebean_param.setAttribute48(Feeds.getVal1(channel_id, FeedEnums.Name.attribute48));
        attributebean_param.setAttribute49(Feeds.getVal1(channel_id, FeedEnums.Name.attribute49));
        attributebean_param.setAttribute50(Feeds.getVal1(channel_id, FeedEnums.Name.attribute50));

        String keyword_attribute = Feeds.getVal1(channel_id, FeedEnums.Name.product_keyword) + keyword;

        if (!Objects.equals(category, "")) {
            keyword_attribute = keyword_attribute + " and " + Feeds.getVal1(channel_id, FeedEnums.Name.product_category_url_key) + " ='" + transferStr(category) + "'";
        }

        if (!Objects.equals(product, "")) {
            keyword_attribute = keyword_attribute + " and " + Feeds.getVal1(channel_id, FeedEnums.Name.product_p_code) + " ='" + transferStr(product) + "'";
        }

        // 新产品Attribute处理
        ProductsFeedAttribute productsFeedAttribute = new ProductsFeedAttribute();
        List<AttributeBean> attributebeans = superfeeddao.selectAttribute(attributebean_param, Feeds.getVal1(channel_id, FeedEnums.Name.table_id), keyword_attribute);

        for (int k = 0; k < attributebeans.size(); k++) {
            AttributeBean attributebean = attributebeans.get(k);
            attributebean.setCategory_url_key(formaturl(channel_id, attributebean.getCategory_url_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));
            attributebean.setModel_url_key(formaturl(channel_id, attributebean.getModel_url_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));
            attributebean.setProduct_url_key(formaturl(channel_id, attributebean.getProduct_url_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));

            attributebeans.set(k, attributebean);
        }

        // 产品属性存在
        if (attributebeans.size() > 0) {
            productsFeedAttribute.setChannel_id(channel_id);
            productsFeedAttribute.setAttributebeans(attributebeans);

            // post web servies
            WsdlResponseBean wsdlResponseBean = jsonBeanOutAttribute(channel_id, productsFeedAttribute);

            // 处理失败
            if (wsdlResponseBean == null) {
//                $error("产品Attribute处理失败，处理失败！");
                $info("产品Attribute处理失败，处理失败！");
                isSuccess = false;
            } else if (wsdlResponseBean.getResult().equals("NG")) {
                isSuccess = false;
//                $error("产品Attribute处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
                $info("产品Attribute处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
                logIssue("cms 数据导入处理", "产品Attribute处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
            }
        } else {
            isSuccess = false;
        }

        return isSuccess;
    }

    /**
     * OldProductFeed 更新产品处理
     *
     * @return isSuccess
     */
    public boolean OldProductFeed(String channel_id) {
        $info("更新产品处理开始");

        // 只处理更新数据
        String keyword = " and UpdateFlag in ('2','3')";

        List<ProductBean> productBeans;

        // product 取得
        productBeans = createProduct(channel_id, "", "", "", keyword, Feeds.getVal1(channel_id, FeedEnums.Name.table_id));

        // 自动生成 Name Urlkey Parentcategory Parenturlkey
        for (ProductBean productBean : productBeans) {

            ProductsFeedUpdate productsFeed = new ProductsFeedUpdate();

            List<ProductBean> productBeans_full = createProduct(channel_id, "", "", transferStr(productBean.getP_code()), "", Feeds.getVal1(channel_id, FeedEnums.Name.table_id) + "_full");

            if (productBeans_full.size() != 1) {
//                $error("更新产品处理异常 code=" + productBean.getP_code() + ",抽出件数=" + productBeans_full.size());
                $info("更新产品处理异常 code=" + productBean.getP_code() + ",抽出件数=" + productBeans_full.size());
                logIssue("cms 数据导入处理", "更新产品处理异常 code=" + productBean.getP_code() + ",抽出件数=" + productBeans_full.size());
                continue;
            }

            Map<String, String> updatefields = new HashMap<>();

            // msrp、price、cn_price、long_description、image_url 变化
            if (!productBean.getP_msrp().equals(productBeans_full.get(0).getP_msrp())) {
                updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_MSRP, productBean.getP_msrp());
            }

            if (!productBean.getPs_price().equals(productBeans_full.get(0).getPs_price())) {
                updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_PRICE, productBean.getPs_price());
            }

            if (!productBean.getCps_cn_price().equals(productBeans_full.get(0).getCps_cn_price())) {
                updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE, productBean.getCps_cn_price());
            }

            if (!productBean.getCps_cn_price_rmb().equals(productBeans_full.get(0).getCps_cn_price_rmb())) {
                updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE_RMB, productBean.getCps_cn_price_rmb());
            }

            if (!productBean.getPe_long_description().equals(productBeans_full.get(0).getPe_long_description())) {
                updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION, productBean.getPe_long_description());
            }

            // image
            String image_url = "";
            for (int j = 0; j < productBean.getImages().size(); j++) {
                image_url = image_url + productBean.getImages().get(j).getImage_url() + CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;
            }

            // image_full
            String image_url_full = "";
            for (int j = 0; j < productBeans_full.get(0).getImages().size(); j++) {
                image_url_full = image_url_full + productBeans_full.get(0).getImages().get(j).getImage_url() + CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;
            }

            // image update
            if (!image_url.equals(image_url_full)) {
                updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_URL, image_url);
            }

            // 没有变化跳过。
            if (updatefields.size() <= 0) {
//				// 更新ZZ_Work_Superfeed_Full
//				if (!SuperfeedFullUpdate(channel_id, productBeans.get(i).getP_code())){
//					$error("更新ZZ_Work_Superfeed_Full 失败");
//					$info("更新ZZ_Work_Superfeed_Full 失败");
//					logIssue("cms 数据导入处理", "更新ZZ_Work_Superfeed_Full 失败，code=" + productBeans.get(i).getP_code(),
//							ErrorType.BatchJob, SubSystem.CMS);
//				}
                // 如果失败下面跳过
                continue;
            }

            // key :channel_id,code,product_url_key,barcode
            productsFeed.setChannel_id(channel_id);
            productsFeed.setCode(productBean.getP_code());
            productsFeed.setProduct_url_key(productBean.getUrl_key());
//			productsFeed.setBarcode();

            // update fields: msrp,price,cn_price,long_description,image_url
            productsFeed.setUpdatefields(updatefields);

            //新产品Attribute处理
            if (!AttributeInsert(channel_id, keyword, "", productBean.getP_code())) {
                $info("更新产品Attribute处理异常");
                // 如果失败下面跳过
                continue;
            }

            // post web servies
            WsdlResponseBean wsdlResponseBean = jsonBeanOutUpdate(channel_id, productsFeed);

            // 处理失败 跳过
            if (wsdlResponseBean == null) {
//                $error("更新产品Attribute处理失败，处理失败！");
                $info("更新产品Attribute处理失败，处理失败！");
                continue;
            }

            ProductUpdateResponseBean productUpdateResponseBean = JsonUtil.jsonToBean(JsonUtil.getJsonString(wsdlResponseBean.getResultInfo()), ProductUpdateResponseBean.class);
            // web servies 返回失败
            if (wsdlResponseBean.getResult().equals("NG") || productUpdateResponseBean.getFailure().size() > 0) {

                // web servies 返回系统失败
                if (wsdlResponseBean.getResult().equals("NG")) {
//                    $error("更新产品处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
                    logIssue("cms 数据导入处理", "更新产品处理异常 code=" + productBean.getP_code() + ",抽出件数=" + productBeans_full.size());
                    $info("更新产品处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
                    logIssue("cms 数据导入处理", String.format("更新产品处理异常 code= %s ,抽出件数= %s ,Message = %s ,MessageCode = %s",
                            productBean.getP_code(),
                            productBeans_full.size(),
                            wsdlResponseBean.getMessageCode(),
                            wsdlResponseBean.getMessage()));
                }
                // web servies 返回数据失败
                else {
                    String failureMessage = "";
                    // 出错统计
                    List<ProductUpdateDetailBean> productUpdateDetailBeans = productUpdateResponseBean.getFailure();
                    for (int b = 0; b < productUpdateDetailBeans.size(); b++) {
                        failureMessage = failureMessage + "Messag(" + b + ")=" + productUpdateDetailBeans.get(b).getResultMessage() + ";";
                    }
//                    $error("更新产品处理失败，" + failureMessage);
                    $info("更新产品处理失败，" + failureMessage);
                    logIssue("cms 数据导入处理", "更新产品处理失败，" + failureMessage);
                }
            } else {
                // 更新ZZ_Work_Superfeed_Full
                if (!SuperfeedFullUpdate(channel_id, productBean.getP_code())) {
//                    $error("更新ZZ_Work_Superfeed_Full 失败");
                    $info("更新ZZ_Work_Superfeed_Full 失败");
                    logIssue("cms 数据导入处理", "更新ZZ_Work_Superfeed_Full 失败，code=" + productBean.getP_code());
                }
            }
        }
        $info("更新产品处理完成");
        return true;
    }

    /**
     * 创建 Model
     *
     * @param channel_id category keyword
     * @return modelBeans
     */
    public List<ModelBean> createModel(String channel_id, String category, String keyword) {
        $info("新产品Model处理开始");

        List<ModelBean> modelBeans;

        ModelBean modelbean_params = new ModelBean();
        String model_keyword = Feeds.getVal1(channel_id, FeedEnums.Name.model_keyword) + " and "
                + Feeds.getVal1(channel_id, FeedEnums.Name.model_category_url_key) + "='" + transferStr(category) + "' " + keyword;

        // Model 字段 以category为单位
        modelbean_params.setUrl_key(Feeds.getVal1(channel_id, FeedEnums.Name.model_url_key));
        modelbean_params.setCategory_url_key(Feeds.getVal1(channel_id, FeedEnums.Name.model_category_url_key));
        modelbean_params.setM_product_type(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_product_type));

        modelbean_params.setM_brand(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_brand));

        modelbean_params.setM_model(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_model));
        modelbean_params.setM_name(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_name));

        modelbean_params.setM_short_description(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_short_description));
        modelbean_params.setM_long_description(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_long_description));

        modelbean_params.setM_size_type(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_size_type));

        modelbean_params.setM_is_unisex(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_is_unisex));

        modelbean_params.setM_weight(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_weight));

        modelbean_params.setM_is_taxable(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_is_taxable));

        modelbean_params.setM_is_effective(Feeds.getVal1(channel_id, FeedEnums.Name.model_m_is_effective));

        // 取得所有Superfeed Model
        modelBeans = superfeeddao.selectSuperfeedModel(model_keyword, modelbean_params, Feeds.getVal1(channel_id, FeedEnums.Name.table_id));

        // urlkey  category_url_key 转换
        for (int i = 0; i < modelBeans.size(); i++) {
            ModelBean modelbean = modelBeans.get(i);

            // urlkey 转换
            modelbean.setCategory_url_key(formaturl(channel_id, modelbean.getCategory_url_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));
            modelbean.setUrl_key(formaturl(channel_id, modelbean.getUrl_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));

            modelBeans.set(i, modelbean);
        }

        $info("新产品Model处理完成");
        return modelBeans;
    }

    /**
     * 创建 Product
     *
     * @param category keyword tablename
     * @return productBeans
     */
    public List<ProductBean> createProduct(String channel_id, String category, String models, String codes, String keyword, String tablename) {

        List<ProductBean> productBeans;
        String category_keyword = "";
        String model_keyword = "";
        String code_keyword = "";
        String updateflag_keyword = "";

        // category
        if (!Objects.equals(category, "")) {
            category_keyword = " and " + Feeds.getVal1(channel_id, FeedEnums.Name.product_category_url_key) + "='" + transferStr(category) + "'";
        }
        // product models
        if (!Objects.equals(models, "")) {
            model_keyword = " and (" + Feeds.getVal1(channel_id, FeedEnums.Name.model_m_model) + " in ('" + transferStr(models) + "')"
                    + " or " + Feeds.getVal1(channel_id, FeedEnums.Name.model_m_model_other_feild) + " in ('" + transferStr(models) + "'))";
        }
        // product codes
        if (!Objects.equals(codes, "")) {
            code_keyword = " and " + Feeds.getVal1(channel_id, FeedEnums.Name.product_p_code) + " in ('" + transferStr(codes) + "')";
        }
        // updateflag
        if (!Objects.equals(keyword, "")) {
            updateflag_keyword = keyword;
        }

        ProductBean productbean_params = new ProductBean();

        String product_keyword = Feeds.getVal1(channel_id, FeedEnums.Name.product_keyword) + category_keyword + model_keyword + code_keyword + updateflag_keyword;
        productbean_params.setUrl_key(Feeds.getVal1(channel_id, FeedEnums.Name.product_url_key));
        productbean_params.setModel_url_key(Feeds.getVal1(channel_id, FeedEnums.Name.product_model_url_key));
        productbean_params.setCategory_url_key(Feeds.getVal1(channel_id, FeedEnums.Name.product_category_url_key));
        productbean_params.setP_code(Feeds.getVal1(channel_id, FeedEnums.Name.product_p_code));
        productbean_params.setP_name(Feeds.getVal1(channel_id, FeedEnums.Name.product_p_name));
        productbean_params.setP_color(Feeds.getVal1(channel_id, FeedEnums.Name.product_p_color));
        productbean_params.setP_msrp(Feeds.getVal1(channel_id, FeedEnums.Name.product_p_msrp));
        productbean_params.setP_made_in_country(Feeds.getVal1(channel_id, FeedEnums.Name.product_p_made_in_country));
        productbean_params.setPe_short_description(Feeds.getVal1(channel_id, FeedEnums.Name.product_pe_short_description));
        productbean_params.setPe_long_description(Feeds.getVal1(channel_id, FeedEnums.Name.product_pe_long_description));
        productbean_params.setPs_price(Feeds.getVal1(channel_id, FeedEnums.Name.product_ps_price));
        productbean_params.setCps_cn_price_rmb(Feeds.getVal1(channel_id, FeedEnums.Name.product_cps_cn_price_rmb));
        productbean_params.setCps_cn_price(Feeds.getVal1(channel_id, FeedEnums.Name.product_cps_cn_price));
        productbean_params.setCps_cn_price_final_rmb(Feeds.getVal1(channel_id, FeedEnums.Name.product_cps_cn_price_final_rmb));

        // 取得所有Superfeed  product
        productBeans = superfeeddao.selectSuperfeedProduct(product_keyword,
                productbean_params, tablename);

        // urlkey  category_url_key 转换
        for (int i = 0; i < productBeans.size(); i++) {
            ProductBean productbean = productBeans.get(i);

            // urlkey 转换
            productbean.setCategory_url_key(formaturl(channel_id, productbean.getCategory_url_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));
            productbean.setModel_url_key(formaturl(channel_id, productbean.getModel_url_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));
            productbean.setUrl_key(formaturl(channel_id, productbean.getUrl_key(), "1", Feeds.getVal1(channel_id, FeedEnums.Name.category_split)));
            // 初期值 0
            productbean.setP_image_item_count("0");

            // 抽出条件 code
            String item_keyword = product_keyword + " and" + Feeds.getVal1(channel_id, FeedEnums.Name.product_p_code) + " = '" + transferStr(productbean.getP_code()) + "'";

            // 图片取得
            List<String> image = superfeeddao.selectSuperfeedImage(item_keyword, Feeds.getVal1(channel_id, FeedEnums.Name.images), tablename);
            List<ImageBean> imagebeans = new ArrayList<>();

            // 多条只取第一条;
            for (String anImage : image) {
                String[] images = anImage.split(Feeds.getVal1(channel_id, FeedEnums.Name.image_split));

                for (String image1 : images) {

                    ImageBean imagebean = new ImageBean();
                    // 有图
                    if (image1.lastIndexOf("/") > 0 && image1.lastIndexOf(".") > 0) {
                        // Image
                        imagebean.setImage_type("1");
                        imagebean.setImage(String.valueOf(imagebeans.size() + 1));
                        imagebean.setImage_url(image1);
                        imagebean.setImage_name(image1.substring(image1.lastIndexOf("/") + 1, image1.lastIndexOf(".")));
                        imagebean.setDisplay_order("0");

                        imagebeans.add(imagebean);
                    }
                }

                // 图片更新
                if (imagebeans.size() > 0) {
                    productbean.setImages(imagebeans);
                    productbean.setP_image_item_count(String.valueOf(imagebeans.size()));
                    break;
                }
            }

            ItemBean itembean_params = new ItemBean();
            itembean_params.setCode(Feeds.getVal1(channel_id, FeedEnums.Name.item_code));
            itembean_params.setI_sku(Feeds.getVal1(channel_id, FeedEnums.Name.item_i_sku));
            itembean_params.setI_client_sku(Feeds.getVal1(channel_id, FeedEnums.Name.item_i_sku));
            itembean_params.setI_itemcode(Feeds.getVal1(channel_id, FeedEnums.Name.item_i_itemcode));
            itembean_params.setI_size(Feeds.getVal1(channel_id, FeedEnums.Name.item_i_size));
            itembean_params.setI_barcode(Feeds.getVal1(channel_id, FeedEnums.Name.item_i_barcode));

            // item size取得
            List<ItemBean> items = superfeeddao.selectSuperfeedItem(item_keyword, itembean_params, tablename);

            // item更新
            if (items.size() > 0) {
                productbean.setItembeans(items);
            }

            productBeans.set(i, productbean);
        }

        return productBeans;
    }

    /**
     * formaturl 转换分隔符为-，转换空格为-，去除特殊字符，转小写
     *
     * @param url tolower split
     * @return formaturl
     */
    public String formaturl(String channel_id, String url, String tolower, String split) {

        String strformaturl = url.replace(split, "-");

        for (int i = 0; i < Feeds.getVal1(channel_id, FeedEnums.Name.url_special_symbol).length(); i++) {
            strformaturl = strformaturl.replace(Feeds.getVal1(channel_id, FeedEnums.Name.url_special_symbol).substring(i, i + 1), "");
        }

        if ("1".equals(tolower)) {
            // 除去空格 去除双连续空格
            strformaturl = strformaturl.replace("  ", "-");
            // 除去单空格
            strformaturl = strformaturl.replace(" ", "-");
            strformaturl = strformaturl.toLowerCase();
        }

//		$info("url = " +  url + ", strformaturl = " +  strformaturl);
        return strformaturl;
    }
//
//	public void backupfile(ProductsFeedInsert productsFeed)   {
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		String date_ymd  = sdf.format(date);
//		File file = new File(CmsConstants.BACKUP_FEED_FILE + date_ymd +".txt");
//		try {
//			FileOutputStream outFile = new FileOutputStream(file);
//			// 准备要输出的内容
//			StringBuffer sb = new StringBuffer();
//			sb.append( JsonUtil.getJsonString(productsFeed));
//			sb.append("\n");
//
//			// 关闭文件
//			try {
//				outFile.write(sb.toString().getBytes());
//				outFile.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				$error(e.getMessage());
//				$info(e.getMessage());
//			}
//		} catch (FileNotFoundException e) {
//			$error(e);
//			$info(e.getMessage());
//		}
//	}

    /**
     * json bean 新数据  post
     */
    public WsdlResponseBean jsonBeanOutInsert(String channel_id, ProductsFeedInsert productsFeed) {

        Map<String, Object> param = new HashMap<>();
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppKey));
        authMap.put("appSecret", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppSecret));
        authMap.put("sessionKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesSessionKey));

        param.put("authentication", authMap);
        param.put("dataBody", productsFeed);

        String jsonParam = JsonUtil.getJsonString(param);

        String response;
        WsdlResponseBean wsdlresponsebean = null;
        try {
//			$info("Url= " + CmsConstants.WEB_SERVIES_URI_INSERT);
            response = WebServiceUtil.postByJsonStr(Codes.getCodeName("WEB_SERVIES_URL_FEED", "01"), jsonParam);
            wsdlresponsebean = JsonUtil.jsonToBean(response, WsdlResponseBean.class);
        } catch (Exception e) {
//            $error("json bean 新数据 post 失败: web servies =" + Codes.getCodeName("WEB_SERVIES_URL_FEED", "01"));
            $info("json bean 新数据 post 失败: web servies =" + Codes.getCodeName("WEB_SERVIES_URL_FEED", "01"));
            logIssue(e);
        }

        return wsdlresponsebean;
    }

    /**
     * json bean 更新数据  post
     */
    private WsdlResponseBean jsonBeanOutUpdate(String channel_id, ProductsFeedUpdate productsFeed) {

        Map<String, Object> param = new HashMap<>();
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppKey));
        authMap.put("appSecret", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppSecret));
        authMap.put("sessionKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesSessionKey));

        param.put("authentication", authMap);
        param.put("dataBody", productsFeed);
        String jsonParam = JsonUtil.getJsonString(param);
        String response;
        WsdlResponseBean wsdlresponsebean = null;
        try {
//			$info("Url= " + CmsConstants.WEB_SERVIES_URI_UPDATE);
            response = WebServiceUtil.postByJsonStr(Codes.getCodeName("WEB_SERVIES_URL_FEED", "02"), jsonParam);
            wsdlresponsebean = JsonUtil.jsonToBean(response, WsdlResponseBean.class);
        } catch (Exception e) {
//            $error("json bean 更新数据 post 失败: web servies =" + Codes.getCodeName("WEB_SERVIES_URL_FEED", "02"));
            $info("json bean 更新数据 post 失败: web servies =" + Codes.getCodeName("WEB_SERVIES_URL_FEED", "02"));
            logIssue(e);
        }
        return wsdlresponsebean;
    }

    /**
     * json bean 属性数据  post
     */
    private WsdlResponseBean jsonBeanOutAttribute(String channel_id, ProductsFeedAttribute attributebeans) {

        Map<String, Object> param = new HashMap<>();
        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppKey));
        authMap.put("appSecret", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppSecret));
        authMap.put("sessionKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesSessionKey));

        param.put("authentication", authMap);
        param.put("dataBody", attributebeans);
        String jsonParam = JsonUtil.getJsonString(param);
        String response;
        WsdlResponseBean wsdlresponsebean = null;
        try {
//			$info("Url= " + CmsConstants.WEB_SERVIES_URI_ATTRIBUTE);
            response = WebServiceUtil.postByJsonStr(Codes.getCodeName("WEB_SERVIES_URL_FEED", "03"), jsonParam);
            wsdlresponsebean = JsonUtil.jsonToBean(response, WsdlResponseBean.class);
        } catch (Exception e) {
//            $error("json bean 属性数据 post 失败: web servies =" + Codes.getCodeName("WEB_SERVIES_URL_FEED", "03"));
            $info("json bean 属性数据 post 失败: web servies =" + Codes.getCodeName("WEB_SERVIES_URL_FEED", "03"));
            logIssue(e);
        }
        return wsdlresponsebean;
    }

    /**
     * 下载ftp文件
     */
    private boolean downloadFileForFtp(String channel_id) throws Exception {
        $info("产品文件下载开始 ");
        boolean isSuccess = true;

        // FtpBean初期化
        FtpBean ftpBean = new FtpBean();

        ftpBean.setPort(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_port));
        ftpBean.setUrl(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_url));
        ftpBean.setUsername(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_username));
        ftpBean.setPassword(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_password));
        ftpBean.setFile_coding(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_file_coding));

        FtpUtil ftpUtil = new FtpUtil();
        FTPClient ftpClient = new FTPClient();
        try {
            //建立连接
            ftpClient = ftpUtil.linkFtp(ftpBean);
            if (ftpClient != null) {
                //本地文件路径设定
                ftpBean.setDown_localpath(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath));
                //Ftp源文件路径设定
                ftpBean.setDown_remotepath(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_remotepath));
                //Ftp源文件名设定
                ftpBean.setDown_filename(StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_filename)));

                String filePathName = ftpBean.getDown_localpath() + "/" + ftpBean.getDown_filename();
                int result = ftpUtil.downFile(ftpBean, ftpClient);

                //下载文件 失败
                if (result != 2) {
                    if (result == 0) {
                        File file = new File(filePathName);
                        file.delete();
                        isSuccess = false;
//                        $error(filePathName + "下载异常！");
                        $info(filePathName + "下载异常！");
                        logIssue("cms 数据导入处理", filePathName + "下载异常！");
                    } else {
                        $info(filePathName + "文件不存在.");
                    }
                } else {
                    //下载文件 成功
//					ftpUtil.delOneFile(ftpBean,ftpClient, StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_filename)));
                }
            }
        } finally {
            //断开连接
            ftpUtil.disconnectFtp(ftpClient);
        }
        $info("产品文件下载结束");
        return isSuccess;
    }

    /**
     * 备份处理文件
     */
    private boolean backupFeedFile(String channel_id) {
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
//            $error("产品文件备份失败");
            $info("产品文件备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }

    /**
     * 备份处理文件
     */
    private boolean AttributeListInsert(String channel_id) {
        $info("AttributeList处理开始");

        List<String> attributelist = superfeeddao.selectSuperfeedAttributeList(channel_id, "1", "1");
        for (String attribute : attributelist) {
            // 单事务处理
            transactionRunner.runWithTran(() -> {
//					String sql = "delete from voyageone_cms.cms_mt_feed_attribute where attribute_name = '" + attribute + "'";
//					if (superfeeddao.deleteData(sql) >= 0) {
// 					}

                List<String> allattributes = superfeeddao.selectAllAttribute(attribute, Feeds.getVal1(channel_id, FeedEnums.Name.table_id));
                for (String allattribute : allattributes) {
                    String result = superfeeddao.selectFeedAttribute(channel_id, attribute, allattribute);
                    if (Integer.parseInt(result) == 0) {
                        if (superfeeddao.insertFeedAttributeNew(channel_id, attribute, allattribute) < 0) {
//                            $error("AttributeList 插入失败 attribute = " + attribute + "," + allattribute);
                            $info("AttributeList 插入失败 attribute = " + attribute + "," + allattribute);
                            logIssue("cms 数据导入处理", "AttributeList 插入失败 attribute= " + attribute);
                        }
                    }
                }

//					if (superfeeddao.deleteData(sql) >= 0) {
//						if (superfeeddao.insertFeedAttribute(channel_id, attribute, Feed.getVal1(channel_id, FeedEnums.Name.table_id)) < 0) {
//							$error("AttributeList 插入失败 attribute = " + attribute);
//							$info("AttributeList 插入失败 attribute = " + attribute);
//							logIssue("cms 数据导入处理", "AttributeList 插入失败 attribute= " + attribute);
//						}
//					}
            });
        }

        $info("AttributeList处理结束");
        return true;
    }

    /**
     * 转换数据中的特殊字符
     */
    public static String transferStr(String data) {
        return data.replace("'", "''").replace("\\", "\\\\").replace("\r\n", " ").replace("\n", " ").replace("\r", " ");
    }

    /**
     * 插入ZZ_Work_Superfeed_Full产品信息
     */
    private boolean SuperfeedFullInsert(String category, String channel_id, List<String> modelList, List<String> productList) {
        boolean isSuccess = true;
        $info("插入ZZ_Work_Superfeed_Full产品处理开始");

        // 成功的数据保存至full表
        String keyWrod;
        // model date
        String keyWrod_model = "";
        for (int f = 0; f < modelList.size(); f++) {
            if (f == 0) {
                keyWrod_model = Feeds.getVal1(channel_id, FeedEnums.Name.model_m_model) + " not in ('" + modelList.get(f);
            } else {
                keyWrod_model = keyWrod_model + "', '" + modelList.get(f);
            }
        }
        if (!Objects.equals(keyWrod_model, "")) {
            keyWrod_model = keyWrod_model + "')";
        }

        // product date
        String keyWrod_product = "";
        for (int f = 0; f < productList.size(); f++) {
            if (f == 0) {
                keyWrod_product = Feeds.getVal1(channel_id, FeedEnums.Name.product_p_code) + "not in ('" + productList.get(f);
            } else {
                keyWrod_product = keyWrod_product + "', '" + productList.get(f);
            }
        }
        if (!Objects.equals(keyWrod_product, "")) {
            keyWrod_product = keyWrod_product + "')";
        }

        keyWrod = " where UpdateFlag in ('1','3') and "
                + Feeds.getVal1(channel_id, FeedEnums.Name.category_column) + " ='" + transferStr(category) + "'";

        // model + product date isexist'
        if (!Objects.equals(keyWrod_model, "") && !Objects.equals(keyWrod_product, "")) {
            keyWrod = keyWrod + " and (" + keyWrod_model + " and " + keyWrod_product + ")";
        } else {
            // model or product date isexist
            if (!Objects.equals(keyWrod_model, "") || !Objects.equals(keyWrod_product, "")) {
                keyWrod = keyWrod + " and (" + keyWrod_model + keyWrod_product + ")";
            }
        }

        if (StringUtils.isEmpty(keyWrod)) {
            int count = superfeeddao.inertSuperfeedFull(keyWrod, Feeds.getVal1(channel_id, FeedEnums.Name.table_id), Feeds.getVal1(channel_id, FeedEnums.Name.table_id) + "_full");
            if (count < 0) {
                isSuccess = false;
//                $error("插入ZZ_Work_Superfeed_Full产品处理失败");
                $info("插入ZZ_Work_Superfeed_Full产品处理失败");
            }
        }

        $info("插入ZZ_Work_Superfeed_Full产品处理结束");
        return isSuccess;
    }

    /**
     * 更新ZZ_Work_Superfeed_Full产品信息
     */
    private boolean SuperfeedFullUpdate(String channel_id, String product_code) {
        // 单事务处理
        transactionRunner.runWithTran(() -> {

            String deletefeed = "delete from " + Feeds.getVal1(channel_id, FeedEnums.Name.table_id) + "_full" + " where "
                    + Feeds.getVal1(channel_id, FeedEnums.Name.product_p_code) + "='" + product_code + "'";

            // 删除full表即存数据
            if (superfeeddao.deleteData(deletefeed) > 0) {
                String keyword = " where " + Feeds.getVal1(channel_id, FeedEnums.Name.product_p_code) + "='" + product_code + "'";

                // 新的增加
                int result = superfeeddao.inertSuperfeedFull(keyword, Feeds.getVal1(channel_id, FeedEnums.Name.table_id), Feeds.getVal1(channel_id, FeedEnums.Name.table_id) + "_full");

                if (result <= 0) {
//                    $error("更新 ZZ_Work_Superfeed_Full表 delete失败，code= " + product_code);
                    $info("更新 ZZ_Work_Superfeed_Full表 delete失败，code= " + product_code);
                }
            } else {
//                $error("更新 ZZ_Work_Superfeed_Full表 insert失败，code= " + product_code);
                $info("更新 ZZ_Work_Superfeed_Full表 insert失败，code= " + product_code);
            }
        });

        $info("更新ZZ_Work_Superfeed_Full产品处理结束 [ %s ]", product_code);
        return true;
    }

    private <T> List<T> notNull(List<T> list) {
        return list == null ? new ArrayList<>(0) : list;
    }
}