package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.CreateReportDao;
import com.voyageone.batch.wms.modelbean.SPThirdWarehouseReportBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Dell on 2016/1/4.
 */
@Service
public class WmsSPThirdWarehouseReportService extends BaseTaskService {

    @Autowired
    CreateReportDao createReportDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsSPThirdWarehouseReportJob";
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
                    new spThirdWarehouseReporting(orderChannelID).doRun();
                }
            });
        }

        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道进行同步
     */
    public class spThirdWarehouseReporting {
        private OrderChannelBean channel;
        // 输出文件编码
        private final String outputFileEncoding = "UTF-8";
        // 相关文件的配置区分
        private final String create_sp_third_warehouse_report = "create_sp_third_warehouse_report";
        // 已生成文件列表
        private List<String> createFileName = new ArrayList<String>();

        private final String report_file_extension = ".xls";

        public spThirdWarehouseReporting(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
        }

        public void doRun() {
            //FtpBean ftpBean = new FtpBean();
            logger.info(channel.getFull_name() + "以SKU集计生成斯伯丁第三方仓库发货日报开始");
            String errmsg = "";
            boolean isSuccess = true;
            boolean isCreated = false;

            //获取相关渠道下需要处理的相关文件的配置
            List<ThirdPartyConfigBean> fileConfigs = ThirdPartyConfigs.getThirdPartyConfigList(channel.getOrder_channel_id(), create_sp_third_warehouse_report);

            for (ThirdPartyConfigBean thirdPartyConfigBean : fileConfigs) {
                createFileName = new ArrayList<String>();
                isCreated = false;
                //相关文件的配置没有设定
                if (thirdPartyConfigBean == null || StringUtils.isNullOrBlank2(thirdPartyConfigBean.getProp_val2())) {
                    errmsg = "在com_mt_third_party_config表中Order_channel_id=" + channel.getOrder_channel_id() + "的create_sp_third_warehouse_report配置缺少";
                    logIssue(errmsg);
                } else {
                    //读取判断生成文件日期的文件
                    List<String> createdFileList = new ArrayList<String>();
                    // 判断时间是否可以运行
                    int timeZone = getTimeZone(channel.getOrder_channel_id());
                    String localTime = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), timeZone);
                    if (DateTimeUtil.getDateHour(DateTimeUtil.parse(localTime)) >= Integer.valueOf(thirdPartyConfigBean.getProp_val1())) {
                        //获取取得数据时间区间
                        List<String> timeRegion = getThirdPartyTime(channel.getOrder_channel_id(), thirdPartyConfigBean.getProp_val1(), thirdPartyConfigBean.getProp_val2(), timeZone);
                        try {
                            createdFileList = readFile(thirdPartyConfigBean.getProp_val5());
                            //文件中的日期小于取得数据时间区间，文件删除，新的一天生成全新文件
                            if (createdFileList != null && createdFileList.size() > 0) {
                                //文件指定生成日期为空，或者文件文件指定生成日期=当前日期
                                if (Long.valueOf(createdFileList.get(0)) < Long.valueOf(timeRegion.get(2))) {
                                    //判断生成文件删除
                                    delFile(thirdPartyConfigBean.getProp_val5());
                                    //处理以SKU集计生成斯伯丁第三方仓库发货日报文件
                                    isSuccess = processReportFile(timeRegion, thirdPartyConfigBean);
                                } else {
                                    logger.info(timeRegion.get(0) + "以SKU集计生成斯伯丁第三方仓库发货日报 From:" + timeRegion.get(0) + "  To:" + timeRegion.get(1) + "已经完成，不需要再次处理！");
                                    isCreated = true;
                                }
                            } else {
                                //处理以SKU集计生成斯伯丁第三方仓库发货日报
                                isSuccess = processReportFile(timeRegion, thirdPartyConfigBean);
                            }
                        } catch (Exception e) {
                            logger.info("读取文件内容出错");
                            logIssue(thirdPartyConfigBean.getProp_val5() + "读取文件内容出错", "readFile");
                        }
                        // 日报文件生成后继处理
                        processCreateAfter(thirdPartyConfigBean, timeRegion.get(2), isSuccess, isCreated);
                    } else {
                        int hours = Integer.valueOf(thirdPartyConfigBean.getProp_val2()) + 1;
                        logger.info("没有到达时间点" + hours + ":00:00,以SKU集计生成斯伯丁第三方仓库发货日报，不需要处理！");
                    }
                }
            }

            logger.info(channel.getFull_name() + "以SKU集计生成斯伯丁第三方仓库发货日报结束");
        }


        /**
         * @param order_channel_id 渠道ID
         * @return 查询时间段，文件生成时间
         * @description 按照第三方的时区获取文件生成时间
         */
        private List<String> getThirdPartyTime(String order_channel_id, String hoursFrom, String hoursTo, int timeZone) {

            List<String> timeRegion = new ArrayList<String>();
            String GMTTimeFrom = "";
            String GMTTimeTo = "";
            String localTime = "";
            String beforeTime = "";

            //设定当前工作日
            beforeTime = DateTimeUtil.format(DateTimeUtil.addDays(DateTimeUtil.getDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT);
            localTime = DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DEFAULT_DATE_FORMAT);
            //取得不同时间段
            if (hoursFrom.equals("16")) {
                GMTTimeFrom = DateTimeUtil.getGMTTimeSPFrom(beforeTime, timeZone, hoursFrom);
                GMTTimeTo = DateTimeUtil.getGMTTimeSPTo(localTime, timeZone, hoursTo);
            } else {
                GMTTimeFrom = DateTimeUtil.getGMTTimeSPFrom(localTime, timeZone, hoursFrom);
                GMTTimeTo = DateTimeUtil.getGMTTimeSPTo(localTime, timeZone, hoursTo);
            }
            String reportDateYyyyMMdd = DateTimeUtil.format(DateTimeUtil.getDate(), DateTimeUtil.DATE_TIME_FORMAT_3) + hoursTo;
            timeRegion.add(GMTTimeFrom);
            timeRegion.add(GMTTimeTo);
            timeRegion.add(reportDateYyyyMMdd);
            logger.info("计算截止时间为：" + "本地时间：" + localTime + "；格林威治时间：" + GMTTimeFrom);
            logger.info("计算开始时间为：" + "本地时间：" + localTime + "；格林威治时间：" + GMTTimeTo);
            return timeRegion;
        }

        /**
         * @param order_channel_id 渠道ID
         * @description 取得时区
         */
        private int getTimeZone(String order_channel_id) {
            String timeZoneStr = ChannelConfigs.getVal1(order_channel_id, ChannelConfigEnums.Name.channel_time_zone);
            if (StringUtils.isEmpty(timeZoneStr)) {
                logger.info("没有配置渠道时区，按默认时区处理！");
                return 0;
            } else {
                return Integer.parseInt(timeZoneStr);
            }
        }

        /**
         * 读取判断今日生成文件的文件
         *
         * @param filePath 文件名
         */
        private List<String> readFile(String filePath) throws Exception {
            List<String> returnLineTxtList = new ArrayList<String>();
            logger.info(filePath + " 读取文件开始");

            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                String encode = CommonUtil.getCharset(filePath);
                try (InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encode);
                     BufferedReader bufferedReader = new BufferedReader(read);) {
                    String lineTxt = null;
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        logger.info(lineTxt);
                        returnLineTxtList.add(lineTxt);
                    }
                }
            } else {
                logger.info("找不到指定的文件");
            }
            logger.info(filePath + " 读取文件结束");
            return returnLineTxtList;
        }

        /**
         * 删除第三方文件
         *
         * @param filePath 文件名
         */
        private void delFile(String filePath) {
            File file = new File(filePath);
            logger.info(filePath + "删除文件开始");
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
            } catch (Exception e) {
                logger.info("删除文件出错");
                logIssue(filePath + "删除文件出错", "delFile");
            }
            logger.info(filePath + "删除文件结束");
        }

        /**
         * 日报文件生成后继处理
         *
         * @param fileConfig
         */
        private void processCreateAfter(ThirdPartyConfigBean fileConfig, String createDate, boolean isSuccess, boolean isCreated) {

            //处理出错
            if (!isSuccess) {
                //删除所有生成日报文件
                //删除所有生成日报文件
                for (String fileName : createFileName) {
                    // 源文件
                    String srcFile = fileConfig.getProp_val4() + "/" + fileName;
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
         * 日报文件生成后，日报文件生成时间写入相应的判断文件
         *
         * @param checkFileName String 文件路径及名称 如c:/fqf.txt
         * @param fileContent   String 文件内容
         * @return
         */
        private void writeFileCreated(String checkFileName, String fileContent) {

            logger.info(checkFileName + " 写入文件开始");
            String lineSeparator = System.getProperty("line.separator");

            try {
                //checkFileName = "D:/zhangfeng_download/Spaldingaaaa/002";
                File myFilePath = new File(checkFileName);
                if (!myFilePath.exists()) {
                    myFilePath.createNewFile();
                }
                try (FileWriter writer = new FileWriter(myFilePath, true);) {
                    // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                    writer.write(fileContent + lineSeparator);
                }
            } catch (Exception e) {
                logger.info("写入文件内容出错");
                logger.error(e.getMessage());
                logIssue(channel.getFull_name() + " " + "   writeFileCreated发生错误：", e);
            }
            logger.info(checkFileName + " 写入文件结束");
        }

        /**
         * @param timeRegion           文件生成日期
         * @param thirdPartyConfigBean 文件配置
         * @description 处理斯伯丁日报文件
         */
        private boolean processReportFile(List<String> timeRegion, ThirdPartyConfigBean thirdPartyConfigBean) {

            boolean isSuccess = true;
            // 以SKU集计物理库存取得
            List<SPThirdWarehouseReportBean> spThirdWarehouseReports = new ArrayList<SPThirdWarehouseReportBean>();
            try {
                spThirdWarehouseReports = createReportDao.getSPThirdWarehouseReportBySKU(channel.getOrder_channel_id(),timeRegion.get(0),timeRegion.get(1), CodeConstants.Reservation_Status.Open,getTaskName());
                logger.info(format("取得 [ %s ] 条数据", spThirdWarehouseReports.size()));
                //生成日报文件开始
                isSuccess = createReportExcel(thirdPartyConfigBean, spThirdWarehouseReports, timeRegion);
                //生成的日报文件发给第三发
                if (isSuccess == true){
                    isSuccess = sendThirdMail(timeRegion,String.valueOf(spThirdWarehouseReports.size()),thirdPartyConfigBean.getProp_val4());
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                logger.info("取得斯伯丁第三方仓库发货日报数据发生错误");
                logIssue(channel.getFull_name() + "取得斯伯丁第三方仓库发货日报数据发生错误：" + e);
                isSuccess = false;
            }

            return (isSuccess);
        }

        /**
         * @description 生成日报文件
         * @param thirdPartyConfigBean
         * @param ReportDatas 日报表基本数据
         * @param timeRegion  判断今日生成的文件
         *
         */
        private boolean createReportExcel(ThirdPartyConfigBean thirdPartyConfigBean,List<SPThirdWarehouseReportBean>  ReportDatas,List<String> timeRegion) {
            String fileName = String.format(thirdPartyConfigBean.getProp_val3(), timeRegion.get(2)) + report_file_extension;
            logger.info(format("生成斯伯丁第三方仓库发货日报文件 [ %s ]开始", fileName));
            String templateFile = Properties.readValue(WmsConstants.spaldingReportTemplateFile.SPALDING_REPORT_TEMPLATE);
            HashMap<String, String>  fileTile = new HashMap<String, String>();
            logger.info(format("读取斯伯丁第三方仓库发货日报文档模板文件 [ %s ]", templateFile));


            try (InputStream inputStream = new FileInputStream(templateFile);
                 // FileOutputStream fileOut = new FileOutputStream(filePath + "e:/workbook.xls");
                 FileOutputStream fileOut = new FileOutputStream(thirdPartyConfigBean.getProp_val4() + "/" + fileName);
                 Workbook book = WorkbookFactory.create(inputStream)) {
                    writeReportExcel(book,fileName,ReportDatas);
                    book.write(fileOut);
                    //已生成文件名保存
                    createFileName.add(fileName);

            } catch (Exception e) {
                logger.info(format("生成斯伯丁第三方仓库发货日报文件 [ %s ]发生错误", fileName));
                logger.error(e.getMessage());
                logIssue(channel.getFull_name() + " " + format("生成日报文件 [ %s ]发生错误：", fileName)+ " " + e);
                return false;

            }
            logger.info(format("生成斯伯丁第三方仓库发货日报文件 [ %s ]结束", fileName));
            return true;
        }

        /**
         * @description 日报文件内容输出
         * @param book
         * @param fileName  文件名
         * @param ReportDatas 日报表基本数据
         *
         */
        private void writeReportExcel(Workbook book, String fileName,
                                      List<SPThirdWarehouseReportBean>  ReportDatas)  throws Exception {
            String sourceOrderIdBasic = "";
            String orderNumBasic = "";
            String sizeValue = "";
            logger.info(format(" [ %s ]斯伯丁第三方仓库发货日报文件内容输出开始", fileName));

            // 从模版中克隆第一个
            Sheet sheet = book.cloneSheet(WmsConstants.SPThirdWarehouseReportItems.TEMPLATE_SHEET);

            // 设置SheetName
            book.setSheetName(book.getSheetIndex(sheet), fileName);

            // 设置报表内容
            int rowStart = 1;
            int rowLine = 1;
            logger.info(format("[ %s ]  条数据准备写入开始", ReportDatas.size()));
            for (SPThirdWarehouseReportBean reportBean : ReportDatas) {
                //日报文件，Size变换
                sizeValue =  changeSize(StringUtils.null2Space(reportBean.getSize()));

                if(rowLine >= WmsConstants.SPThirdWarehouseReportItems.ADD_START_ROWS){
                    // 仓库没改变时，增加新行
                    Row newRow = sheet.createRow(rowStart);
                    Row oldRow = sheet.getRow(WmsConstants.SPThirdWarehouseReportItems.OLD_ROWS);
                    for (int col = WmsConstants.SPThirdWarehouseReportItems.Column_Start; col < WmsConstants.SPThirdWarehouseReportItems.Column_End; col++) {
                        Cell newCell = newRow.createCell(col);
                        Cell oldCell = oldRow.getCell(col);
                        newCell.setCellStyle(oldCell.getCellStyle());
                    }
                }
                Row row = sheet.getRow(rowStart);
                //Order_number不一样
                if (!sourceOrderIdBasic.equals(reportBean.getSource_order_id()) && !orderNumBasic.equals(reportBean.getOrder_number())){

                    //序号
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Index).setCellValue(rowLine);
                    //付款时间
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Order_date_time).setCellValue(reportBean.getOrder_date_time());
                    //官网订单号
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Source_order_id).setCellValue(reportBean.getSource_order_id());
                    // 	内部订单号
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Order_number).setCellValue(reportBean.getOrder_number());
                    //购买物品
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Sku).setCellValue(reportBean.getItemcode() + "-" + sizeValue);
                    // 	数量
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Qty).setCellValue(reportBean.getQty());
                    // 	物品类型
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Product).setCellValue(reportBean.getProduct());
                    // 	收货人姓名
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_name).setCellValue(reportBean.getShip_name());
                    // 	手机
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_phone).setCellValue(reportBean.getShip_phone());
                    // 	省
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_state).setCellValue(reportBean.getShip_state());
                    // 	市
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_city).setCellValue(reportBean.getShip_city());
                    // 	详细地址
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_address).setCellValue(reportBean.getShip_address());
                    // 	备注
                    row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Customer_comments).setCellValue(reportBean.getCustomer_comments());
                    // 	承运方
                    // 	运单号
                    sourceOrderIdBasic = reportBean.getSource_order_id();
                    orderNumBasic = reportBean.getOrder_number();
                }else{
                    //序号
                    row.getCell(0).setCellValue(rowLine);
                    //购买物品
                    row.getCell(4).setCellValue(reportBean.getItemcode() + "-" + sizeValue);
                    // 数量
                    row.getCell(5).setCellValue(reportBean.getQty());
                }
                rowStart = rowStart + 1;
                rowLine = rowLine + 1;
            }
            logger.info(format("[ %s ]  条数据准备写入结束", ReportDatas.size()));
            book.removeSheetAt(0);
            logger.info(format(" [ %s ]斯伯丁第三方仓库发货日报文件内容输出结束", fileName));
        }


        /**
         * 日报文件，Size变换
         * @param sizeValue String
         * @return String
         */
        private  String changeSize(String sizeValue) throws Exception{
            if (WmsConstants.spaldingReportSizeChange.SIZE_LIST.containsKey(sizeValue)){
                sizeValue = sizeValue.replace(sizeValue,WmsConstants.spaldingReportSizeChange.SIZE_LIST.get(sizeValue));
            }else if (sizeValue.indexOf("*") >= 0 ){
                sizeValue = sizeValue.replace("*","-");
            }
            return sizeValue;
        }


        /**
         * 斯伯丁第三方仓库发货日报邮件发送
         * @param timeRegion
         * @return 邮件内容
         */
        private boolean sendThirdMail(List<String> timeRegion,String listSize,String filePath) {
            // 存放邮件附件
            List<String> fileAffix = new ArrayList<String>();
            logger.info("斯伯丁第三方仓库发货日报邮件发送");
            try{
                int timeZone = getTimeZone(channel.getOrder_channel_id());
                StringBuilder builderContent = new StringBuilder();
                String detail = String.format(WmsConstants.EmailSPThirdWarehouseReport.HEAD,DateTimeUtil.getLocalTime(timeRegion.get(0),timeZone),DateTimeUtil.getLocalTime(timeRegion.get(1),timeZone), listSize);
                builderContent
                        .append(Constants.EMAIL_STYLE_STRING)
                        .append(detail);


                String subject = String.format(WmsConstants.EmailSPThirdWarehouseReport.SUBJECT, timeRegion.get(2));
                //Mail.sendAlert(CodeConstants.EmailReceiver.NEED_SOLVE, subject, builderContent.toString(), true);


                // 源文件
                String srcFile = filePath + "/" + createFileName.get(0);
                fileAffix.add(srcFile);

                Mail.send(CodeConstants.EmailReceiver.NEED_SOLVE, subject, builderContent.toString(), fileAffix, false);
                return true;
            } catch (Exception e) {
                logger.info("斯伯丁第三方仓库发货日报邮件发送发生错误");
                logger.error(e.getMessage());
                logIssue(channel.getFull_name() + " 斯伯丁第三方仓库发货日报邮件发送发生错误：" + e);
                return false;

            }
        }
    }
}
