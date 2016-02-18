package com.voyageone.batch.wms.service.thirdFilePro;

import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.SpThirdReportDao;
import com.voyageone.batch.wms.modelbean.ClientTrackingBean;
import com.voyageone.batch.wms.modelbean.SPThirdWarehouseReportBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.CarrierConfigs;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.CarrierBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.mail.Mail;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Fred on 2016/1/7.
 */
@Service
public class WmsSPThirdWarehouseResolveDataPro extends ThirdFileProBaseService {
    private OrderChannelBean channel;

    @Autowired
    protected SpThirdReportDao spThirdReportDao;

    // 已生成文件列表
    //private List<String> disposedFileName = new ArrayList<String>();

    //错误邮件初期化
    private List<SPThirdWarehouseReportBean> errorSPReportList = new ArrayList<SPThirdWarehouseReportBean>();

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsSPThirdWarehousePro";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

    }

    public void doRun(String channelId, List<TaskControlBean> taskControlList, List<ThirdPartyConfigBean> thirdPartyConfigBean) {
        channel = ChannelConfigs.getChannel(channelId);
        assert channel != null;
        log(channel.getFull_name() + ": " + getTaskName() + "解析SP第三方仓库发货文件开始");
        for (ThirdPartyConfigBean tcb : thirdPartyConfigBean) {
            try {
                processFile(tcb, channel.getOrder_channel_id());
            } catch (Exception e) {
                logger.error(channel.getFull_name() + "解析SP第三方仓库发货文件发生错误：", e);
                logIssue(channel.getFull_name() + "解析SP第三方仓库发货文件发生错误：" + e);
            }
        }
        log(channel.getFull_name() + ": " + getTaskName() + "解析SP第三方仓库发货文件结束");
    }

    /**
     * @param bean           第三方表的配置
     * @param orderChannelId 渠道
     * @description 文件数据做相关处理
     */
    private void processFile(ThirdPartyConfigBean bean, String orderChannelId) throws Exception {
        //需要处理的文件名
        String fileName = bean.getProp_val1();
        //获取文件路径
        final String filePath = bean.getProp_val2();
        //获取Sheet名
        final String sheetName = bean.getProp_val3();
        //目录夹中文件集合
        List<String> ftpFileNames = new ArrayList<String>();

        boolean isSuccess = false;

        //没有上传文件名
        if (fileName == null || StringUtils.isNullOrBlank2(fileName)) {
            //直接报错
            logger.info(channel.getFull_name() + "解析文件发生错误：没有设置解析文件名");
            logIssue(channel.getFull_name() + "解析文件发生错误：没有设置解析文件名");
            //路径下全部文件取得
        } else {
            //取得上传路径下所有符合条件的文件
            ftpFileNames = FileUtils.getFileGroup(fileName, filePath);
            if (ftpFileNames.size() == 0){
                logger.info("没有需要解析的文件");
            }
            for (String ftpFileName : ftpFileNames) {
                isSuccess = false;
                errorSPReportList = new ArrayList<SPThirdWarehouseReportBean>();
                //读取SP第三方仓库发货文件的文件
                List<SPThirdWarehouseReportBean> spThirdWarehouseReports = readExcelFile(filePath + "/" + ftpFileName, sheetName);
                if (spThirdWarehouseReports != null && spThirdWarehouseReports.size() > 0) {
                    // 数据处理
                    isSuccess = processData(spThirdWarehouseReports, ftpFileName);
                    if (isSuccess == true) {
                        //disposedFileName.add(ftpFileName);
                        //文件生成备份文件
                        moveReportFileForBack(bean, ftpFileName);
                    }
                    //检查错误的记录发邮件
                    String errorMail = sendErrorMail(errorSPReportList, ftpFileName);
                    if (!StringUtils.isNullOrBlank2(errorMail)) {
                        logger.info("SP第三方仓库发货文件错误记录邮件发出");
                        String subject = String.format(WmsConstants.EmailSpThirdReportErrShipping.SUBJECT, channel.getFull_name());
                        Mail.sendAlert(CodeConstants.EmailReceiver.ITWMS, subject, errorMail, true);
                    }
                }
            }
        }
    }


    /**
     * 读取SP第三方仓库发货文件的文件
     *
     * @param filePathName 文件名
     * @param sheetName    Sheet名
     */
    private List<SPThirdWarehouseReportBean> readExcelFile(String filePathName, String sheetName) throws Exception {
        List<SPThirdWarehouseReportBean> spThirdWarehouseReports = new ArrayList<SPThirdWarehouseReportBean>();
        logger.info(filePathName + " 读取文件开始");
        int rowSum = 0;
        try (InputStream inputStream = new FileInputStream(filePathName);
             Workbook book = WorkbookFactory.create(inputStream)) {
            //获取每个Sheet表
            for (int i = 0; i < book.getNumberOfSheets(); i++) {
                Sheet sheet = book.getSheetAt(i);
                //找到需要的Sheet
                if (sheet.getSheetName().contains(sheetName)) {
                    logger.info(format("SHEET [ %s ]开始解读", sheet.getSheetName()));
                    for (Row row : sheet) {
                        //空行不再做处理
                        if (row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Index)==null){
                            break;
                        }
                        row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Index).setCellType(Cell.CELL_TYPE_STRING);
                        if (StringUtils.isNullOrBlank2(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Index).getStringCellValue())){
                            break;
                        }
                        if (row.getRowNum() == 0) {
                            continue;
                        } else {

                            SPThirdWarehouseReportBean spBean = new SPThirdWarehouseReportBean();
                            //下单时间
                            spBean.setOrder_date_time(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Order_date_time).getStringCellValue());
                            //官网订单号
                            spBean.setSource_order_id(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Source_order_id).getStringCellValue());
                            // 	内部订单号
                            spBean.setOrder_number(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Order_number).getStringCellValue());
                            //购买物品
                            spBean.setSku(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Sku).getStringCellValue());
                            // 	数量
                            spBean.setQty(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Qty).getStringCellValue());
                            // 	物品类型
                            spBean.setStyle(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Product).getStringCellValue());
                            // 	收货人姓名
                            spBean.setShip_name(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_name).getStringCellValue());
                            // 	手机
                            spBean.setShip_phone(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_phone).getStringCellValue());
                            // 	省
                            spBean.setShip_state(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_state).getStringCellValue());
                            // 	市
                            spBean.setShip_city(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_city).getStringCellValue());
                            // 	详细地址
                            spBean.setShip_address(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Ship_address).getStringCellValue());
                            // 	备注
                            spBean.setCustomer_comments(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Column_Customer_comments).getStringCellValue());
                            // 	承运方
                            spBean.setTracking_type(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Tracking_type).getStringCellValue());
                            // 	运单号
                            spBean.setTracking_no(row.getCell(WmsConstants.SPThirdWarehouseReportItems.Tracking_no).getStringCellValue());
                            spThirdWarehouseReports.add(spBean);
                            rowSum = rowSum + 1;
                        }
                    }
                    logger.info(format("SHEET [ %s ]有[ %s ]条记录", sheet.getSheetName(), rowSum));
                    logger.info(format("SHEET [ %s ]结束解读", sheet.getSheetName()));
                }
            }
            logger.info(filePathName + " 读取文件结束");
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(format("文件[ %s ]解读发生错误", filePathName));
            logIssue(channel.getFull_name() + format("文件[ %s ]解读发生错误:", filePathName) + e);
            spThirdWarehouseReports = null;
        }
        return spThirdWarehouseReports;
    }

    /**
     * 数据处理
     *
     * @param spThirdWarehouseReports
     * @param ftpFileName
     */
    private boolean processData(List<SPThirdWarehouseReportBean> spThirdWarehouseReports, String ftpFileName) {
        boolean isSuccess = true;
        String sourceOrderIDBasic = "";
        String orderNumBasic = "";
        SPThirdWarehouseReportBean checkReport = new SPThirdWarehouseReportBean();
        List<ClientTrackingBean> clientTrackingBeans = new ArrayList<ClientTrackingBean>();
        try {
            logger.info(format("文件[ %s ]数据处理开始", ftpFileName));
            for (SPThirdWarehouseReportBean spReport : spThirdWarehouseReports) {
                //第一条记录读入
                if (StringUtils.isNullOrBlank2(sourceOrderIDBasic) && StringUtils.isNullOrBlank2(orderNumBasic)){
                    sourceOrderIDBasic = spReport.getSource_order_id();
                    orderNumBasic = spReport.getOrder_number();
                    //检查用的Report设定
                    checkReport = spReport;
                }else{
                    //多件不同物品同一订单
                    if (StringUtils.isNullOrBlank2(spReport.getOrder_number()) && StringUtils.isNullOrBlank2(spReport.getSource_order_id())) {
                        //检查用的Report的Tracking_type为空，本记录Tracking_type不为空,设定Tracking_type
                        if (StringUtils.isNullOrBlank2(checkReport.getTracking_type()) && !StringUtils.isNullOrBlank2(spReport.getTracking_type())){
                            checkReport.setTracking_type(spReport.getTracking_type());
                        }
                        //检查用的Report的Tracking_no为空，本记录Tracking_no不为空,设定Tracking_no
                        if (StringUtils.isNullOrBlank2(checkReport.getTracking_no()) && !StringUtils.isNullOrBlank2(spReport.getTracking_no())){
                            checkReport.setTracking_no(spReport.getTracking_no());
                        }
                    //不同订单
                    }else if(!spReport.getOrder_number().equals(orderNumBasic) || !spReport.getSource_order_id().equals(sourceOrderIDBasic)){
                        // 对上一个订单进行数据检查通过
                        if (checkData(checkReport) == true) {
                            ClientTrackingBean clientTrackingBean = setClientTracking(checkReport);
                            clientTrackingBeans.add(clientTrackingBean);
                        }
                        // 本条订单作为检查用的Report设定
                        sourceOrderIDBasic = spReport.getSource_order_id();
                        orderNumBasic = spReport.getOrder_number();
                        //检查用的Report设定
                        checkReport = spReport;
                    }
                }

//                if (!StringUtils.isNullOrBlank2(spReport.getOrder_number()) && !StringUtils.isNullOrBlank2(spReport.getSource_order_id())) {
//
//                    // 数据检查通过
//                    if (checkData(spReport) == true) {
//                        ClientTrackingBean clientTrackingBean = setClientTracking(spReport);
//                        clientTrackingBeans.add(clientTrackingBean);
//                    }
//                }
            }
            // 对最后订单进行数据检查通过
            if (checkData(checkReport) == true) {
                ClientTrackingBean clientTrackingBean = setClientTracking(checkReport);
                clientTrackingBeans.add(clientTrackingBean);
            }

            logger.info(format("有[ %s ]条数据符合插入条件", clientTrackingBeans.size()));
            if (clientTrackingBeans.size() > 0){
                int sumResult = spThirdReportDao.insertClientTrackingInfo(clientTrackingBeans);
                logger.info(format("有[ %s ]条数据插入表tt_client_tracking", sumResult));
            }
            logger.info(format("文件[ %s ]数据处理结束", ftpFileName));
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.info(format("文件[ %s ]数据处理发生错误", ftpFileName));
            logIssue(channel.getFull_name() + format("文件[ %s ]数据处理发生错误:", ftpFileName) + e);
            isSuccess = false;
        }
        return isSuccess;

    }


    /**
     * 对数据进行检查
     *
     * @param spReport
     */
    private boolean checkData(SPThirdWarehouseReportBean spReport) {
        boolean isSuccess = true;
        SPThirdWarehouseReportBean errorSPReport = new SPThirdWarehouseReportBean();
        //List<CarrierBean> carrierConfigs = CarrierConfigs.getCarrier(channel.getOrder_channel_id());
        String errReason = "";

        // 	承运方为空，运单号为空
        if (StringUtils.isNullOrBlank2(spReport.getTracking_type()) && StringUtils.isNullOrBlank2(spReport.getTracking_no())) {
            return false;
        }

        // 	承运方为空
        if (StringUtils.isNullOrBlank2(spReport.getTracking_type()) && !StringUtils.isNullOrBlank2(spReport.getTracking_no())) {
            errReason = errReason + WmsConstants.SpThirdReportErrReason.TRACHING_TYPE_NULL + "<br>";
        }

        // 	运单号为空
        if (StringUtils.isNullOrBlank2(spReport.getTracking_no()) && !StringUtils.isNullOrBlank2(spReport.getTracking_type())) {
            errReason = errReason + WmsConstants.SpThirdReportErrReason.TRACHING_NO_NULL + "<br>";
            //将所有的全角空格和半角空格都替换掉，然后判断长度
        }

        //承运方不是规定的物流公司
        if (!StringUtils.isNullOrBlank2(spReport.getTracking_type()) && checkTracking_type(spReport.getTracking_type()) == false) {
            errReason = errReason + WmsConstants.SpThirdReportErrReason.TRACHING_TYPE_ERR + "<br>";
        }

        //将所有的全角空格和半角空格都替换掉，然后判断长度
        if (spReport.getTracking_no().replaceAll(" ", "").replaceAll("　", "").length() != spReport.getTracking_no().length()) {
            errReason = errReason + WmsConstants.SpThirdReportErrReason.TRACHING_NO_ERR + "<br>";
        }

        // 根据order_number获取订单状态
        int countExists = spThirdReportDao.selectCountByOrderNumber(spReport.getSource_order_id(), spReport.getOrder_number(), channel.getOrder_channel_id());
        // 订单不存在
        if (countExists == 0) {
            errReason = errReason + WmsConstants.SpThirdReportErrReason.ORDER_NO_ERR + "<br>";
        }

        // 根据Source_order_id获取订单的物流单号
        int countTrackingExists = spThirdReportDao.selectTrackingBySourcOrderId(spReport.getSource_order_id(), spReport.getOrder_number(), channel.getOrder_channel_id());
        // 订单的物流单号存在
        if (countTrackingExists > 0) {
            errReason = errReason + WmsConstants.SpThirdReportErrReason.ClIENT_TRACKING_ERR + "<br>";
        }
        // 数据不对的情况
        if (!StringUtils.isNullOrBlank2(errReason)) {
            errorSPReport = spReport;
            errorSPReport.setErr_reason(errReason.substring(0, errReason.length() - 4));
            errorSPReportList.add(errorSPReport);
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 承运方是不是规定的物流公司进行检查
     *
     * @param trackingType
     */
    private boolean checkTracking_type(String trackingType) {
        boolean isSuccess = false;
        List<CarrierBean> carrierConfigs = CarrierConfigs.getCarrier(channel.getOrder_channel_id());
        if (carrierConfigs != null && carrierConfigs.size() > 0) {
            for (CarrierBean carrierBean : carrierConfigs) {
                if (carrierBean.getCarrier().equals(trackingType)) {
                    isSuccess = true;
                    break;
                }
            }
        }
        return isSuccess;
    }

    /**
     * 准备插入tt_client_tracking表的数据
     *
     * @param spReports
     */
    private ClientTrackingBean setClientTracking(SPThirdWarehouseReportBean spReports) {
        ClientTrackingBean clientTrackingBean = new ClientTrackingBean();

        clientTrackingBean.setOrder_channel_id(channel.getOrder_channel_id());
        clientTrackingBean.setClient_order_id(spReports.getOrder_number());
        clientTrackingBean.setSource_order_id(spReports.getSource_order_id());
        clientTrackingBean.setTracking_type(spReports.getTracking_type());
        clientTrackingBean.setTracking_no(spReports.getTracking_no());
        clientTrackingBean.setCreater(getTaskName());
        clientTrackingBean.setModifier(getTaskName());
        return clientTrackingBean;
    }

    /**
     * 错误邮件出力
     *
     * @param errorList 错误SKU一览
     * @param ftpFileName
     * @return 错误邮件内容
     */
    private String sendErrorMail(List<SPThirdWarehouseReportBean> errorList,String ftpFileName) {

        StringBuilder builderContent = new StringBuilder();

        if (errorList.size() > 0) {

            StringBuilder builderDetail = new StringBuilder();

            int index = 0;
            for (SPThirdWarehouseReportBean error : errorList) {
                index = index + 1;
                builderDetail.append(String.format(WmsConstants.EmailSpThirdReportErrShipping.ROW,
                        index,
                        StringUtils.null2Space(error.getSource_order_id()),
                        StringUtils.null2Space(error.getOrder_number()),
                        StringUtils.null2Space(error.getSku()),
                        StringUtils.null2Space(error.getTracking_type()),
                        StringUtils.null2Space(error.getTracking_no()),
                        StringUtils.null2Space(error.getOrder_date_time()),
                        StringUtils.null2Space(error.getErr_reason())));
            }

            String count = String.format(WmsConstants.EmailSpThirdReportErrShipping.COUNT, errorList.size());

            String detail = String.format(WmsConstants.EmailSpThirdReportErrShipping.TABLE, count, builderDetail.toString());

            builderContent
                    .append(Constants.EMAIL_STYLE_STRING)
                    .append(String.format(WmsConstants.EmailSpThirdReportErrShipping.HEAD, ftpFileName))
                    .append(detail);


        }

        return builderContent.toString();
    }


    /**
     * 文件生成备份文件
     *
     * @param fileConfig
     */
    private boolean moveReportFileForBack(ThirdPartyConfigBean fileConfig,String fileName) {
        boolean isSuccess = true;
        //for (String fileName : disposedFileName) {
        // 源文件
        String srcFile = fileConfig.getProp_val2() + "/" + fileName;
        // 目标文件
        String bakFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + fileName.substring(fileName.lastIndexOf("."));
        String destFile = fileConfig.getProp_val4() + "/" + bakFileName;

        logger.info("生成备份文件： " + srcFile + "  TO  " + destFile);
        try {
            //生成备份文件
            FileUtils.moveFileByBcbg(srcFile, destFile);
        } catch (Exception e) {
            isSuccess = false;
            logger.error("SP第三方仓库发货文件生成备份文件出错", e);
            logIssue(channel.getFull_name() + " " + "SP第三方仓库发货文件生成备份文件出错：", e);
            //break;
        }
        //}
        return isSuccess;
    }
}
