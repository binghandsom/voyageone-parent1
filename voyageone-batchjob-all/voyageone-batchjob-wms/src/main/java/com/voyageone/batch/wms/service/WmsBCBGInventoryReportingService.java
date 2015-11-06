package com.voyageone.batch.wms.service;

import com.jcraft.jsch.ChannelSftp;
import com.taobao.api.domain.Store;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.modelbean.SimTransferBean;
import com.voyageone.batch.wms.modelbean.SumInventoryBean;
import com.voyageone.batch.wms.modelbean.ThirdReportBean;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Dell on 2015/11/3.
 */

@Service
public class WmsBCBGInventoryReportingService extends BaseTaskService {

    @Autowired
    InventoryDao inventoryDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsBCBGInventoryReportingJob";
    }


    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {
            threads.add(new Runnable() {
                @Override
                public void run() {
                    new bcbgInventoryReporting(orderChannelID).doRun();
                }
            });
        }

        runWithThreadPool(threads, taskControlList);
    }


    /**
     * 按渠道进行同步
     */
    public class bcbgInventoryReporting {
        private OrderChannelBean channel;
        // 输出文件编码
        private final String outputFileEncoding = "UTF-8";
        // 相关文件的配置区分
        private final String create_bcbg_daily_Inventory_report = "create_bcbg_daily_Inventory_report";
        // 已生成文件列表
        private List<String>createFileName = new  ArrayList<String>();

        public bcbgInventoryReporting(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            //FtpBean ftpBean = new FtpBean();
            logger.info(channel.getFull_name()+"以SKU集计物理库存生成BCBG日报开始" );
            String errmsg = "";
            boolean isSuccess = true;
            boolean isCreated = false;

            //获取取得数据时间区间
            List<String> timeRegion = getThirdPartyTime(channel.getOrder_channel_id());

            //获取相关渠道下需要处理的相关文件的配置
            List<ThirdPartyConfigBean> fileConfigs = ThirdPartyConfigs.getThirdPartyConfigList(channel.getOrder_channel_id(), create_bcbg_daily_Inventory_report);

            for (ThirdPartyConfigBean thirdPartyConfigBean :fileConfigs ) {
                //相关文件的配置没有设定
                if (thirdPartyConfigBean == null || StringUtils.isNullOrBlank2(thirdPartyConfigBean.getProp_val2())) {
                    errmsg = "在com_mt_third_party_config表中Order_channel_id=" + channel.getOrder_channel_id() + "的create_bcbg_daily_Inventory_report配置缺少";
                    logIssue(errmsg);
                } else {
                    //读取判断生成文件日期的文件
                    List<String> createdFileList = new ArrayList<String>();
                    try {
                        createdFileList = readFile(thirdPartyConfigBean.getProp_val5());
                        //文件中的日期小于取得数据时间区间，文件删除，新的一天生成全新文件
                        if (createdFileList != null && createdFileList.size() > 0) {
                            if (Long.valueOf(createdFileList.get(0)) < Long.valueOf(timeRegion.get(0))) {
                                //判断生成文件删除
                                delFile(thirdPartyConfigBean.getProp_val5());
                                //处理以SKU集计物理库存日报文件
                                isSuccess = processReportFile(timeRegion, thirdPartyConfigBean);
                            } else {
                                logger.info(timeRegion.get(0) + "以SKU集计物理库存生成BCBG日报文件已经完成，不需要再次处理！");
                                isCreated = true;
                            }
                        }else {
                            //处理以SKU集计物理库存日报文件
                            isSuccess = processReportFile(timeRegion, thirdPartyConfigBean);
                        }
                    } catch (Exception e) {
                        logger.info("读取文件内容出错");
                        logIssue(thirdPartyConfigBean.getProp_val5() + "读取文件内容出错", "readFile");
                    }
                }
            }
            // 日报文件生成后继处理
            processCreateAfter(fileConfigs.get(0),timeRegion.get(0),isSuccess,isCreated);
            logger.info(channel.getFull_name()+"以SKU集计物理库存生成BCBG日报结束" );
        }

        /**
         * @description 按照第三方的时区获取文件生成时间
         * @param order_channel_id  渠道ID
         * @return 文件生成时间（"yyyyMMdd"，"MMddyyyy"，"HHmmss"）
         */
        private List<String> getThirdPartyTime(String order_channel_id) {
            List<String> timeRegion = new ArrayList<String>();
            int timeZone = getTimeZone(order_channel_id);
            String localTime = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), timeZone);
            Date localTimeForDate = DateTimeUtil.parse(localTime);
            String reportDateYyyyMMdd = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_3);
            String reportDateMMddyyyy = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_7);
            String reportTime = DateTimeUtil.format(localTimeForDate, DateTimeUtil.DATE_TIME_FORMAT_4);
            timeRegion.add(reportDateYyyyMMdd);
            timeRegion.add(reportDateMMddyyyy);
            timeRegion.add(reportTime);

            String GMTTimeFrom = DateTimeUtil.getGMTTimeFrom(localTime, timeZone);
            logger.info("文件生成时间为：" + "第三方时间：" + reportDateYyyyMMdd + "  " + reportTime + "；格林威治时间：" + GMTTimeFrom);
            return timeRegion;
        }

        /**
         * @description 取得时区
         * @param order_channel_id  渠道ID
         *
         */
        private int getTimeZone(String order_channel_id) {
            String timeZoneStr = ChannelConfigs.getVal1(order_channel_id, ChannelConfigEnums.Name.channel_time_zone);
            if(StringUtils.isEmpty(timeZoneStr)){
                logger.info("没有配置渠道时区，按默认时区处理！");
                return 0;
            }else {
                return Integer.parseInt(timeZoneStr);
            }
        }

        /**
         * 删除第三方文件
         * @param filePath           文件名
         */
        private void delFile(String filePath){
            File file = new File(filePath);
            logger.info( filePath + "删除文件开始" );
            try {
                //file = null;
                if (file.exists()) {
                    boolean isSuccess = file.delete();
                    if (isSuccess) {
                        logger.info(filePath + "删除成功！");
                    } else {
                        logger.info(filePath + "删除失败！");
                    }
                }
            }catch (Exception e){
                logger.info("删除文件出错");
                logIssue(filePath + "删除文件出错","delFile");
            }
            logger.info( filePath + "删除文件结束" );
        }

        /**
         * @description 处理斯伯丁日报文件
         * @param timeRegion 文件生成日期
         * @param thirdPartyConfigBean  文件配置
         *
         */
        private boolean processReportFile(List<String> timeRegion,ThirdPartyConfigBean thirdPartyConfigBean) {
            //logger.info("处理以SKU集计物理库存生成BCBG日报文件开始");
            boolean isSuccess = true;
            // 以SKU集计物理库存取得
            List<SumInventoryBean> simTransferList = new ArrayList<SumInventoryBean>();
            try {
                simTransferList = inventoryDao.getSumInventoryBySKU(channel.getOrder_channel_id(), CodeConstants.Reservation_Status.Open);
                //生成日报文件开始
                isSuccess = createReportDat(thirdPartyConfigBean,simTransferList,timeRegion);
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.info("取得集计物理库存数据发生错误");
                logIssue(channel.getFull_name() + "取得集计物理库存数据发生错误：" + e);
                isSuccess = false;

            }
            //logger.info("处理以SKU集计物理库存生成BCBG日报文件结束");
            return(isSuccess);
        }


        /**
         * @description 推送文件名生成（Inv_VO_BBB_YYYYMMDD_HHMMSS.dat）
         * @param timeRegion  时区
         * @param thirdPartyConfigBean
         * @param brand 品牌名
         *
         * @return FtpBean
         */
        private FtpBean createPostFileNameForDailyInventory(List<String> timeRegion,ThirdPartyConfigBean thirdPartyConfigBean,String brand) {
            FtpBean ftpBean = new FtpBean();
            // 上传文件路径
            ftpBean.setUpload_path(thirdPartyConfigBean.getProp_val1());
            // 上传文件路径
            ftpBean.setUpload_filename(String.format(thirdPartyConfigBean.getProp_val2(), brand, timeRegion.get(0), timeRegion.get(2)));
            // 本地文件路径
            ftpBean.setUpload_localpath(thirdPartyConfigBean.getProp_val3());
            // 本地文件备份路径
            ftpBean.setUpload_local_bak_path(thirdPartyConfigBean.getProp_val4());
            return ftpBean;

        }

        /**
         * @description 上传Bean生成
         * @param thirdPartyConfigBean
         * @return FtpBean
         */
        private FtpBean formatDailyInventoryUploadFtpBean(ThirdPartyConfigBean thirdPartyConfigBean) {
            FtpBean ftpBean = new FtpBean();
            // 上传文件路径
            ftpBean.setUpload_path(thirdPartyConfigBean.getProp_val1());
            // 上传文件路径
            ftpBean.setUpload_path(thirdPartyConfigBean.getProp_val1());
            // 本地文件路径
            ftpBean.setUpload_localpath(thirdPartyConfigBean.getProp_val3());
            // 本地文件备份路径
            ftpBean.setUpload_local_bak_path(thirdPartyConfigBean.getProp_val4());
            return ftpBean;
        }


        /**
         * 读取判断今日生成文件的文件
         * @param filePath           文件名
         */
        private List<String> readFile(String filePath) throws Exception{
            List<String> returnLineTxtList = new ArrayList<String>();
            logger.info(filePath + " 读取文件开始" );

            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                String encode= CommonUtil.getCharset(filePath);
                try (InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encode);
                     BufferedReader bufferedReader = new BufferedReader(read);){
                    String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        logger.info(lineTxt);
                        returnLineTxtList.add(lineTxt);
                    }
                }
            }else{
                logger.info("找不到指定的文件");
            }
            logger.info(filePath + " 读取文件结束" );
            return returnLineTxtList;
        }

        /**
         * @description 生成DAT格式日报文件
         * @param thirdPartyConfigBean
         * @param sumInventoryBeans 集计物理库存数据
         * @param timeRegion 文件生成日期
         *
         * @return boolean
         */
        private boolean createReportDat(ThirdPartyConfigBean thirdPartyConfigBean,List<SumInventoryBean>  sumInventoryBeans,List<String> timeRegion) {
            boolean isSuccess = true;
            FtpBean ftpBean = new FtpBean();
            String brand = "";
            int rowReports = 1;
            List<SumInventoryBean> reportTransferList = new ArrayList<SumInventoryBean>();
            for (SumInventoryBean sumInventoryBean : sumInventoryBeans) {

                //根据不同的品牌生成文件
                if (!StringUtils.null2Space(sumInventoryBean.getBrand()).equals(brand) || rowReports == 1 ) {

                    if (rowReports > 1 ){
                        try {
                            //已生成文件名保存
                            createFileName.add(ftpBean.getUpload_filename());
                            // DAT文件做成
                            createInventoryReportForDat(ftpBean, timeRegion,reportTransferList);
                        } catch (Exception e) {
                            isSuccess = false;
                            logIssue(channel.getFull_name() + " " + format("生成集计物理库存日报文件 [ %s ]发生错误：", ftpBean.getUpload_filename()) + " " + e);
                            break;
                        }
                        reportTransferList = new ArrayList<SumInventoryBean>();
                        logger.info(format("生成集计物理库存日报文件 [ %s ]结束",  ftpBean.getUpload_filename()));
                        rowReports = 1;
                    }
                    //获得相应的推送文件的配置
                    ftpBean = createPostFileNameForDailyInventory(timeRegion, thirdPartyConfigBean, sumInventoryBean.getBrand());
                    brand = StringUtils.null2Space(sumInventoryBean.getBrand());
                    logger.info(format("生成集计物理库存日报文件 [ %s ]开始", ftpBean.getUpload_filename()));
                }
                reportTransferList.add(sumInventoryBean);
                rowReports = rowReports + 1;

            }
            if (sumInventoryBeans.size() > 0 && isSuccess == true) {
                //最后一个文件生成
                try {
                    //已生成文件名保存
                    createFileName.add(ftpBean.getUpload_filename());
                    // DAT文件做成
                    createInventoryReportForDat(ftpBean, timeRegion,reportTransferList);
                } catch (Exception e) {
                    isSuccess = false;
                    logIssue(channel.getFull_name() + " " + format("生成集计物理库存日报文件 [ %s ]发生错误：", ftpBean.getUpload_filename()) + " " + e);
                }
                logger.info(format("生成集计物理库存日报文件 [ %s ]结束", ftpBean.getUpload_filename()));
            }
            return isSuccess;
        }


        /**
         * 集计物理库存推送
         *
         * @param ftpBean
         * @param reportInventoryBeans
         * @param timeRegion 文件生成日期
         *
         */
        private void createInventoryReportForDat(FtpBean ftpBean,List<String> timeRegion, List<SumInventoryBean>  reportInventoryBeans)throws Exception  {

            // 本地文件生成路径
            File file = new File(ftpBean.getUpload_localpath() + "/" + ftpBean.getUpload_filename());

            try  (FileOutputStream fop = new FileOutputStream(file); ){

                FileWriteUtils fileWriter = new FileWriteUtils(fop,  Charset.forName(outputFileEncoding), "A","S");
                try{
                    // 文件内容输出
                    createUploadFileBodyForDemands(fileWriter, reportInventoryBeans,ftpBean.getUpload_filename(),timeRegion);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (Exception e) {
                    fileWriter.flush();
                    fileWriter.close();
                    logger.info(format("生成集计物理库存日报文件 [ %s ]  时createInventoryReportForDat发生错误", ftpBean.getUpload_filename()));
                    logger.error(e.getMessage());
                    //logIssue(fullName + " " + format("生成日报文件 [ %s ]发生错误：", fileName) + " " + e);
                    Exception ex = new Exception(e.getMessage() + "   createInventoryReportForDat发生错误");
                    throw ex;
                }
            }
        }


        /**
         * 生成DAT文件
         *
         * @param fileWriter
         * @param reportInventoryBeans
         * @param filename
         * @param timeRegion 文件生成日期
         *
         */
        private void createUploadFileBodyForDemands(FileWriteUtils fileWriter,List<SumInventoryBean>  reportInventoryBeans,String filename,List<String> timeRegion) throws IOException {

            logger.info(format("[ %s ]  条数据准备写入开始", reportInventoryBeans.size()));
            //site和storageLocation取得
            List<StoreBean> storeBean = StoreConfigs.getChannelStoreList(channel.getOrder_channel_id(), false, false);
            Long storeid = storeBean.get(0).getStore_id();
            String site = StoreConfigs.getVal1(storeid, StoreConfigEnums.Name.site);
            //表示 右对齐占用4个字符，不足的用0补位
            site = String.format("%020d", Integer.valueOf(site));
            String storageLocation = StoreConfigs.getVal1(storeid, StoreConfigEnums.Name.storage_location);
            storageLocation = String.format("%012d", Integer.valueOf(storageLocation));
            for (SumInventoryBean reportInventoryBean :  reportInventoryBeans) {
                int quantityAvailable = Integer.valueOf(reportInventoryBean.getQty()) - Integer.valueOf(reportInventoryBean.getRes_qty());

                fileWriter.write("", DemandsFileFormat.RecordType);
                fileWriter.write("", DemandsFileFormat.LineNumber);
                fileWriter.write(site, DemandsFileFormat.Site);
//                if (StringUtils.null2Space(reportInventoryBean.getClient_sku()).equals("CTT66F26001")) {
//                    fileWriter.write(null, DemandsFileFormat.ARTICLE);
//                }else{
                fileWriter.write(StringUtils.null2Space(reportInventoryBean.getClient_sku()), DemandsFileFormat.ARTICLE);
//                }
                fileWriter.write("", DemandsFileFormat.LongItemNumber);
                fileWriter.write(StringUtils.null2Space(reportInventoryBean.getBarcode()), DemandsFileFormat.UPC);
                fileWriter.write(reportInventoryBean.getQty(), DemandsFileFormat.QuantityOnHand);
                fileWriter.write(String.valueOf(quantityAvailable), DemandsFileFormat.QuantityAvailable);
                fileWriter.write(reportInventoryBean.getSales_price(), DemandsFileFormat.SalesPrice);
                fileWriter.write(reportInventoryBean.getMsrp(), DemandsFileFormat.MSRP);
                fileWriter.write("EA", DemandsFileFormat.UOM);
                fileWriter.write(storageLocation, DemandsFileFormat.StorageLocation);
                fileWriter.write(timeRegion.get(1), DemandsFileFormat.DATE);
                fileWriter.write(timeRegion.get(2), DemandsFileFormat.Time);

                fileWriter.write(System.getProperty("line.separator"));
            }

            logger.info(format("[ %s ]  条数据准备写入结束", reportInventoryBeans.size()));

        }


        /**
         * Inv_VO_BBB_YYYYMMDD_HHMMSS.dat 文件格式（upload：/opt/ftp-shared/clients-ftp/voyageone-bcbg-sftp/inventory/reports）
         */
        private class DemandsFileFormat {
            private static final String RecordType = "A,11";
            private static final String LineNumber = "A,10";
            private static final String Site = "A,20";
            private static final String ARTICLE = "A,25";
            private static final String LongItemNumber = "A,25";
            private static final String UPC = "A,25";
            private static final String QuantityOnHand = "S,15";
            private static final String QuantityAvailable = "S,15";
            private static final String SalesPrice = "S,15.2";
            private static final String MSRP = "S,15.2";
            private static final String UOM = "A,2";
            private static final String StorageLocation = "A,12";
            private static final String DATE = "A,8";
            private static final String Time = "A,6";
        }

        /**
         * 日报文件生成后继处理
         *
         * @param fileConfig
         */
        private void processCreateAfter(ThirdPartyConfigBean fileConfig,String createDate,boolean isSuccess,boolean isCreated ){
            // FTP目录夹Copy推送
            if (isSuccess) {
                isSuccess = uploadOrderFile(fileConfig);
            }
            //集计物理库存日报文件生成备份文件
            if (isSuccess) {
                isSuccess = moveReportFileForBack(fileConfig);
            }
            //处理出错
            if (!isSuccess) {
                //删除所有生成日报文件
                for (String fileName : createFileName) {
                    // 源文件
                    String srcFile = fileConfig.getProp_val3() + "/" + fileName;
                    delFile(srcFile);
                }
            } else {
                if (isCreated == false) {
                    //日报文件生成后，日报文件生成时间写入相应的判断文件
                    writeFileCreated(fileConfig.getProp_val5(), createDate);
                }
            }
        }

        /**
         * 集计物理库存日报文件上传
         *
         * @param fileConfig
         */
        private boolean uploadOrderFile(ThirdPartyConfigBean fileConfig) {

            boolean isSuccess = true;
            for (String fileName :createFileName) {
                logger.info(format("[ %s ] 集计物理库存日报文件上传开始", fileName));
                // 本地文件名称
                 try {
                    String localFile = fileConfig.getProp_val3() + File.separator + fileName;
                    String remoteFile = fileConfig.getProp_val1() + File.separator + fileName;
                    FileUtils.copyFileByBcbg(localFile, remoteFile);
                } catch (Exception e) {
                    isSuccess = false;
                    logger.error("集计物理库存日报文件上传出错", e);
                    logIssue(channel.getFull_name() + " " + format("集计物理库存日报文件上传 [ %s ]发生错误：", fileName) + " " + e);
                    break;
                }
                logger.info(format("[ %s ] 集计物理库存日报文件上传结束", fileName));
            }
            return isSuccess;
        }


        /**
         * 集计物理库存日报文件生成备份文件
         * @param fileConfig
         *
         */
        private boolean moveReportFileForBack(ThirdPartyConfigBean fileConfig) {
            boolean isSuccess = true;
            for (String fileName :createFileName) {
                // 源文件
                String srcFile =fileConfig.getProp_val3() + "/" + fileName;
                // 目标文件
                String destFile = fileConfig.getProp_val4() + "/" + fileName;
                logger.info("生成备份文件： " + srcFile + " " + destFile);
                try {
                    //生成备份文件
                    FileUtils.moveFileByBcbg(srcFile, destFile);
                }catch (Exception e) {
                    isSuccess = false;
                    logger.error("集计物理库存日报文件生成备份文件出错", e);
                    logIssue(channel.getFull_name() + " " + "集计物理库存日报文件生成备份文件出错：",  e);
                    break;
                }
            }
            return isSuccess;
        }

        /**
         * 日报文件生成后，日报文件生成时间写入相应的判断文件
         * @param checkFileName String 文件路径及名称 如c:/fqf.txt
         * @param fileContent String 文件内容
         * @return
         */
        private  void writeFileCreated(String checkFileName, String fileContent){

            logger.info(checkFileName + " 写入文件开始" );
            String lineSeparator = System.getProperty("line.separator");

            try {
                //checkFileName = "D:/zhangfeng_download/Spaldingaaaa/002";
                File myFilePath = new File(checkFileName);
                if (!myFilePath.exists()) {
                    myFilePath.createNewFile();
                }
                try( FileWriter writer = new FileWriter(myFilePath, true);) {
                    // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                    writer.write(fileContent + lineSeparator);
                }

            } catch (Exception e) {
                logger.info("写入文件内容出错");
                logger.error(e.getMessage());
                logIssue(channel.getFull_name() + " " + "   writeFileCreated发生错误：", e);
            }
            logger.info(checkFileName + " 写入文件结束" );
        }
    }
}


