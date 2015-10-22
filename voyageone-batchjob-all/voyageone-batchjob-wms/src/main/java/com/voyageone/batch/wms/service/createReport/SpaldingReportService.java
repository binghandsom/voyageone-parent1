package com.voyageone.batch.wms.service.createReport;

import com.csvreader.CsvWriter;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.modelbean.SetPriceBean;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.SetPriceUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.CreateReportDao;
import com.voyageone.batch.wms.modelbean.ThirdReportBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

/**
 * Created by Dell on 2015/8/6.
 */
@Service
public class SpaldingReportService extends CreateReportBaseService {
    // 相关文件的配置区分
    private final String create_report_file_path = "create_report_file_path";

    // 日报文件的扩展名
    private final String report_file_extension = ".csv";

    // 输出文件编码
    private final String outputFileEncoding = "UTF-8";
    // bom文件头
    private final String bom = "BOM";
    // OneSize判断用
    private final String ONE_SIZE = "OneSize";
    // Date判断用
    private final String DATE_0921 = "20150921";

    protected final Log logger = LogFactory.getLog(getClass());

    //private HashMap<String, List<SmsConfigBean>> mapSmsConfig = new HashMap<String, List<SmsConfigBean>>();
    private List<String> reportFileName = new ArrayList<String>();

    @Autowired
    private CreateReportDao createReportDao;

    @Override public String getTaskName() {
        return "SpaldingReportService";
    }

    @Override public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override protected void onStartup(List <TaskControlBean> taskControlList) throws Exception {

    }

    public void doRun(String channelId,String taskName) {
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        assert channel != null;
        String errmsg = "";


        logger.info(channel.getFull_name() + "----------" + getTaskName() + "----------开始");
        //根据订单渠道,取出相关渠道下的所有店铺
        List<ShopBean> shopBeans = ShopConfigs.getChannelShopList(channel.getOrder_channel_id());

        //获取相关渠道下需要处理的相关文件的配置
        List<ThirdPartyConfigBean> fileConfigs = ThirdPartyConfigs.getThirdPartyConfigList(channel.getOrder_channel_id(), create_report_file_path);


        //获取取得数据时间区间
        List<String> timeRegion = getCalcGMTTime(channel.getOrder_channel_id());


        //


        for (final ShopBean shopBean : shopBeans) {
            //获得相应的Cartid的相关文件的配置
            ThirdPartyConfigBean thirdPartyConfigBean = setThirdPartyConfigByCartid(fileConfigs,shopBean.getCart_id());
            //相关文件的配置没有设定
            if (thirdPartyConfigBean == null || StringUtils.isNullOrBlank2(thirdPartyConfigBean.getProp_val2())){
                errmsg = "在com_mt_third_party_config表中cartid=" + shopBean.getCart_id() + "的create_report_file_path配置缺少";
                logIssue(errmsg);
            } else {
                //获得相应的Cartid的斯伯丁日报文件名
                List<String> fileNameList = new ArrayList<String>();
                fileNameList = setFileNameByCartid(thirdPartyConfigBean.getProp_val4());
                //读取判断生成文件日期的文件
                List<String> createdFileList = new  ArrayList<String>();
                try {
                    createdFileList = readFile(thirdPartyConfigBean.getProp_val3());
                }catch (Exception e) {
                    logger.info("读取文件内容出错");
                    logIssue(thirdPartyConfigBean.getProp_val3() + "读取文件内容出错","readFile");
                    continue;
                }
                //文件中的日期小于取得数据时间区间，文件删除，新的一天生成全新文件
                if (createdFileList != null && createdFileList.size() > 0){
                    if (Long.valueOf(createdFileList.get(0).substring(1,9)) < Long.valueOf(fileNameList.get(0).substring(1,9))){
                        //判断生成文件删除
                        delFile(thirdPartyConfigBean.getProp_val3());
                        //判断已生成文件用数组初期化
                        createdFileList = new ArrayList<String>();
                    }
                }
                //处理斯伯丁日报文件
                processReportFile(fileNameList,shopBean.getCart_id(),channel.getOrder_channel_id(),taskName,timeRegion,channel.getFull_name(),thirdPartyConfigBean,createdFileList);
            }
            //for (ThirdPartyConfigBean tcb: thirdPartyConfigBean) {
            //处理文件
            try {
                logger.info("ok");
                //processFile(tcb, channel.getOrder_channel_id(), taskControlList);
            } catch (Exception e) {
                String msg = channel.getFull_name() + "解析第三方库存文件发生错误" + e;
                logger.error(msg);
                logIssue(msg);
            }
            //}
        }
        logger.info(channel.getFull_name() + "----------" + getTaskName() + "----------结束");
    }


    /**
     * @description 获得相应的Cartid的相关文件的配置
     * @param fileConfigs 相关渠道下需要处理的相关文件的配置
     * @param cart_id  店铺编码
     *
     */
    private ThirdPartyConfigBean setThirdPartyConfigByCartid(List<ThirdPartyConfigBean> fileConfigs,String cart_id) {
        ThirdPartyConfigBean thirdPartyConfigBean = new ThirdPartyConfigBean();
        for (final ThirdPartyConfigBean configBean : fileConfigs) {
            //找到相应店铺的文件配置
            if (configBean.getProp_val1().equals(cart_id)){
                thirdPartyConfigBean = configBean;
                break;
            }
        }
        return(thirdPartyConfigBean);
    }

    /**
     * @description 获得相应的Cartid的斯伯丁日报文件名
     * @param lastName 斯伯丁日报文件名末尾三位（001,002）
     *
     */
    private List<String> setFileNameByCartid(String lastName) {
        List<String> fileNameList = new ArrayList<String>();
        String fileName = "";
        for (String fileNameA : WmsConstants.spaldingReportFileName.FILE_NAME_A){
            fileName = fileNameA + DateTimeUtil.format(DateTimeUtil.addDays(DateTimeUtil.getDate(), -1),DateTimeUtil.DATE_TIME_FORMAT_3) + lastName;
            fileNameList.add(fileName);
        }
        return(fileNameList);
    }



    /**
     * @description 处理斯伯丁日报文件
     * @param fileNameList 斯伯丁日报文件名
     * @param cart_id  店铺编码
     * @param order_channel_id  渠道ID
     * @param taskName
     * @param taskName 取得数据时间区间
     * @param fullName
     * @param thirdPartyConfigBean  文件配置
     *
     */
    private void processReportFile(List<String> fileNameList,String cart_id,String order_channel_id ,String taskName,List<String> timeRegion,String fullName,ThirdPartyConfigBean thirdPartyConfigBean,List<String> createdFileList) {
        logger.info("处理斯伯丁日报文件开始");
        String fileName = "";
        int fileFlg = 0;
        boolean isExistsFile = false;
        for (String fileNameA : fileNameList){
            //判断日报文件今天是否已生成
            if (checkFileIsCreate(fileNameA ,createdFileList)== false) {
                //取得日报表基本数据
                List<ThirdReportBean> ReportDatas = new ArrayList<ThirdReportBean>();
                try {
                    ReportDatas = setReportBasicData(fileFlg, cart_id, order_channel_id, taskName, timeRegion, fullName);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    logger.info("取得日报表基本数据发生错误");
                    logIssue(fullName + "取得日报表基本数据发生错误：" + e);
                    fileFlg = fileFlg + 1;
                    continue;
                }

                //生成日报文件开始
                createReportCsv(fullName, fileFlg, fileNameA, thirdPartyConfigBean.getProp_val2(),
                        thirdPartyConfigBean.getProp_val5(), ReportDatas, order_channel_id,thirdPartyConfigBean.getProp_val3(),cart_id);
//                createReportExcel(fullName, fileFlg, fileNameA, thirdPartyConfigBean.getProp_val2(),
//                        thirdPartyConfigBean.getProp_val5(), ReportDatas, order_channel_id,thirdPartyConfigBean.getProp_val3());
            }

            fileFlg = fileFlg + 1;
        }
        logger.info("处理斯伯丁日报文件结束");
    }


    /**
     * @description 取得日报表基本数据
     * @param fileFlg 区分
     * @param cart_id  店铺编码
     * @param order_channel_id  渠道ID
     * @param taskName 取得数据时间区间
     * @param fullName
     *
     */
    private List<ThirdReportBean> setReportBasicData(int fileFlg,String cart_id,String order_channel_id, String taskName,List<String> timeRegion,String fullName) throws Exception {
        logger.info("取得日报表基本数据开始");
        List<ThirdReportBean>  ReportDatas  = new ArrayList<ThirdReportBean>();

        //取得销售订单的日报基本数据记录
        if (fileFlg == 0){
            //============================================================================================
            //============================================================================================
            timeRegion.set(0,"2015-08-29 16:00:00"); timeRegion.set(1,"2015-09-29 15:59:59");
            ReportDatas  = createReportDao.getCreateReportData(cart_id,order_channel_id, CodeConstants.TransferStatus.ClOSE,CodeConstants.TransferType.OUT,
                    CodeConstants.TransferOrigin.RESERVED,timeRegion.get(0),timeRegion.get(1),taskName);
        //取得退货订单（退回TM仓库）的日报基本数据记录
        }else if (fileFlg == 1){
            //============================================================================================
            //============================================================================================
            timeRegion.set(0,"2015-08-29 16:00:00"); timeRegion.set(1,"2015-09-29 15:59:59");
            ReportDatas  = createReportDao.getCreateReportDataByTM(cart_id, order_channel_id, CodeConstants.TransferStatus.ClOSE, CodeConstants.TransferType.IN,
                    CodeConstants.TransferOrigin.RETURNED, timeRegion.get(0), timeRegion.get(1), taskName);
        //取得退货订单（退回福建仓库）的日报基本数据记录
        }else if (fileFlg == 2){
            //============================================================================================
            //============================================================================================
            //timeRegion.set(0,"2015-07-14 16:00:00"); timeRegion.set(1,"2015-07-15 15:59:59");
            timeRegion.set(0,"2015-08-29 16:00:00"); timeRegion.set(1,"2015-09-29 15:59:59");
            ReportDatas  = createReportDao.getCreateReportData(cart_id, order_channel_id, CodeConstants.TransferStatus.ClOSE, CodeConstants.TransferType.WITHDRAWAL,
                    CodeConstants.TransferOrigin.WITHDRAWAL, timeRegion.get(0), timeRegion.get(1), taskName);
        //取得特殊物品(定制球)销售订单的日报基本数据记录
        }else if (fileFlg == 3){
            //============================================================================================
            //============================================================================================
            timeRegion.set(0,"2015-08-29 16:00:00"); timeRegion.set(1,"2015-09-29 15:59:59");
            ReportDatas  = createReportDao.getCreateReportSpecialData(cart_id, order_channel_id, WmsConstants.specialType.BALL, timeRegion.get(0), timeRegion.get(1), taskName);
        }

        logger.info("取得日报表基本数据结束");
        return ReportDatas;

    }


    /**
     * @description 获取取得数据时间区间
     * @param order_channel_id  渠道ID
     * @return 计算开始时间,结束时间
     */
    private List<String> getCalcGMTTime(String order_channel_id) {
        List<String> timeRegion = new ArrayList<String>();
        int timeZone = getTimeZone(order_channel_id);
        String GMTTimeFrom = "";
        String GMTTimeTo = "";
        String localTime = "";
        if (DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_3).equals(DATE_0921)){
            GMTTimeFrom = "2015-09-14 16:00:00";
            GMTTimeTo =  "2015-09-20 15:59:59";
        }else {
            //设定前一工作日
            localTime = DateTimeUtil.format(DateTimeUtil.addDays(DateTimeUtil.getDate(), -1), DateTimeUtil.DEFAULT_DATE_FORMAT);
            GMTTimeFrom = DateTimeUtil.getGMTTimeFrom(localTime, timeZone);
            GMTTimeTo  = DateTimeUtil.getGMTTimeTo(localTime, timeZone);
        }
        timeRegion.add(GMTTimeFrom);
        timeRegion.add(GMTTimeTo);
        logger.info("计算截止时间为：" + "本地时间：" + localTime + "；格林威治时间：" + GMTTimeFrom);
        logger.info("计算开始时间为：" + "本地时间：" + localTime + "；格林威治时间：" + GMTTimeTo);
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
     * @description 生成CSV格式日报文件
     * @param fullName
     * @param fileFlg 区分
     * @param fileNameA  文件名
     * @param filePath  文件路径,
     * @param Customer
     * @param ReportDatas 日报表基本数据
     * @param order_channel_id  渠道ID
     * @param checkFileName  判断今日生成的文件
     *
     */
    private boolean createReportCsv(String fullName,int fileFlg,String fileNameA, String filePath,
                                      String Customer,List<ThirdReportBean>  ReportDatas,String order_channel_id, String checkFileName,String cart_id) {

        String fileName = fileNameA + report_file_extension;
        logger.info(format("生成日报文件 [ %s ]开始", fileName));

        HashMap<String, String>  fileTile = new HashMap<String, String>();

        //取得销售订单的日报标题
        String[] fileTitleHeads ;
        if (fileFlg == 0){
            fileTitleHeads = WmsConstants.spaldingReportTitleHead.SALES_ORDER_TITLE_HEAD;
            fileTile = WmsConstants.spaldingReportTitle.SALES_ORDER_TITLE;
            //退货订单(退回到上海仓库)标题
        }else if (fileFlg == 1){
            fileTitleHeads = WmsConstants.spaldingReportTitleHead.RETURN_TP_TITLE_HEAD;
            fileTile = WmsConstants.spaldingReportTitle.RETURN_TP_TITLE;
            //退货订单(退回到福建仓库)标题
        }else  if (fileFlg == 2) {
            fileTitleHeads = WmsConstants.spaldingReportTitleHead.RETURN_TP_TITLE_HEAD;
            fileTile = WmsConstants.spaldingReportTitle.RETURN_SPALDING_TITLE;
            //特殊物品销售订单（定制球）的日报标题
        } else {
            fileTitleHeads = WmsConstants.spaldingReportTitleHead.SALES_ORDER_TITLE_HEAD;
            fileTile = WmsConstants.spaldingReportTitle.SPECIAL_SALES_ORDER_TITLE;
        }

        try {
            // CSV文件做成
            createCSVReportFile(fullName,fileFlg,fileNameA,filePath,Customer,ReportDatas,order_channel_id,fileTitleHeads,fileTile,cart_id);
            // bom 追加
            addBom(filePath, fullName, fileName);
            writeFileCreated(checkFileName,fileNameA);
        } catch (Exception e) {
            //String msg = fullName + "解析第三方库存文件发生错误" + e;
//            logger.info(format("生成日报文件 [ %s ]发生错误", fileName));
//            logger.error(e.getMessage());
            logIssue(fullName + " " + format("生成日报文件 [ %s ]发生错误：", fileName)+ " " + e);
            return false;
        }
        logger.info(format("生成日报文件 [ %s ]结束", fileName));
        return true;
    }


    /**
     * 上传文件做成
     * @param fileFlg 区分
     * @param fileNameA  文件名
     * @param filePath  文件路径,
     * @param Customer
     * @param fullName
     * @param ReportDatas 日报表基本数据
     * @param order_channel_id  渠道ID
     * @param fileTitleHeads  文件标题栏
     * @param fileTile  文件标题栏基本内容
     */
    private void createCSVReportFile(String fullName,int fileFlg,String fileNameA, String filePath,String Customer,List<ThirdReportBean> ReportDatas,
                                     String order_channel_id,String[] fileTitleHeads,HashMap<String, String>  fileTile,String cart_id)throws Exception {
        //boolean isSuccess = true;
        String fileName = fileNameA + report_file_extension;
        //logger.info(format("生成日报文件 [ %s ]开始", fileName));
        File file = new File(filePath + "/" + fileName);
        try (FileOutputStream  fop = new FileOutputStream(file);) {


            //

//            File file = new File(filePath + "/" + fileName);
//            FileOutputStream  fop = new FileOutputStream(file);
            CsvWriter csvWriter = new CsvWriter(fop, ',', Charset.forName(outputFileEncoding));
            try {
                // Head输出
                createUploadFileHead(csvWriter, fileTitleHeads);
                // Body输出
                createUploadFileBody(csvWriter, fileTile, fileNameA, Customer, ReportDatas, order_channel_id, fileFlg,cart_id);

                csvWriter.flush();
                csvWriter.close();

            } catch (Exception e) {
                //isSuccess = false;
                csvWriter.flush();
                csvWriter.close();
                logger.info(format("生成日报文件 [ %s ]  时createCSVReportFile发生错误", fileName));
                logger.error(e.getMessage());
                //logIssue(fullName + " " + format("生成日报文件 [ %s ]发生错误：", fileName) + " " + e);
                Exception ex = new Exception(e.getMessage() + "   createCSVReportFile发生错误");
                throw ex;
            }
        }
        //logger.info(format("生成日报文件 [ %s ]结束", fileName));
        //return isSuccess;
    }

    /**
     * 上传文件做成
     *
     * @param csvWriter 上传文件Handler
     * @param titleHeads CSV文件标题行
     */
    private void createUploadFileHead(CsvWriter csvWriter,String[] titleHeads) throws Exception {
        int indexCell = 0;
        for (String titleHead : titleHeads){
            if (indexCell == 0){
                csvWriter.write(bom + titleHead);
                //csvWriter.write(titleHead);
            }else {
                csvWriter.write(titleHead);
            }
            indexCell = indexCell + 1;
        }
        csvWriter.endRecord();
    }

    /**
     * @description CSV日报文件内容输出
     * @param fileTile  文件标题
     * @param fileNameA  文件名,
     * @param Customer
     * @param ReportDatas 日报表基本数据
     * @param order_channel_id  渠道ID
     *
     */
    private void createUploadFileBody(CsvWriter csvWriter,HashMap<String, String> fileTile,String fileNameA,
                                      String Customer,List<ThirdReportBean>  ReportDatas,String order_channel_id,int fileFlg,String cart_id)  throws Exception  {

        String orderNumBasic = "";
        boolean priceFlg = false;
        String strUnit = "";
        String sizeValue = "";
        String specialSku = "";
        //List<SpaldingPriceBean> spaldingPriceDatas = new ArrayList<SpaldingPriceBean>();
        List<SetPriceBean> spaldingPriceDatas = new ArrayList<SetPriceBean>();
        logger.info(format(" [ %s ]日报文件内容输出开始", fileNameA));
        logger.info(format("[ %s ]  条数据准备写入开始", ReportDatas.size()));
        for (ThirdReportBean reportBean : ReportDatas) {
            //特殊商品SKU装换
            if (fileFlg == 3 ) {
                reportBean = changeSpecialSku(reportBean,fileFlg,order_channel_id);
            }

            //Order_number不一样
            if (!orderNumBasic.equals(reportBean.getOrder_number())){
                //spaldingPriceDatas = new ArrayList<SpaldingPriceBean>();
                //根据order_number，以SKU汇总取得价格
                //spaldingPriceDatas  = setPrice(reportBean.getOrder_number(),order_channel_id);
                spaldingPriceDatas = new ArrayList<SetPriceBean>();
                spaldingPriceDatas  = SetPriceUtils.setPrice(reportBean.getOrder_number(),order_channel_id,cart_id,fileFlg);
                orderNumBasic = reportBean.getOrder_number();
            }
            //reportBean.setSize("22*24");
            //日报文件，Size变换
            sizeValue =  changeSize(StringUtils.null2Space(reportBean.getSize()));
            //Ordertype
            csvWriter.write(fileTile.get("Ordertype"));
            //DocumentID
            csvWriter.write(fileNameA);
            //CustAccount
            csvWriter.write(Customer);
            //ReasonCode
            if (fileFlg == 1 || fileFlg == 2) {
                csvWriter.write("残次品");
            }
            //InventSiteId
            csvWriter.write(fileTile.get("Site"));
            //InventLocationId
            csvWriter.write(fileTile.get("Warehouse"));
            //ShippingDateRequested
            csvWriter.write(DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(reportBean.getCreated(),8), DateTimeUtil.DATE_TIME_FORMAT_3));
            //E-ComSalesId
            csvWriter.write(reportBean.getSource_order_id());
            //ItemId
            csvWriter.write(StringUtils.null2Space(reportBean.getItemcode()));
            //Size
            //OneSize时显示为空
            csvWriter.write(sizeValue);
            if (StringUtils.null2Space(reportBean.getSize()).equals(ONE_SIZE)){
                strUnit = "个";
            }else{
                //csvWriter.write(StringUtils.null2Space(reportBean.getSize()));
                strUnit = "件";
            }

            //SalesQty
            csvWriter.write(reportBean.getSales_unit());


            //for (SpaldingPriceBean spaldingPriceData : spaldingPriceDatas){
            priceFlg = false;
            for (SetPriceBean spaldingPriceData : spaldingPriceDatas){
                if (spaldingPriceData.getSku().equals(reportBean.getTransfer_sku())){
                    // 	SalesPrices(按sku合计金额  /  个数)
                    double salesPrice = Double.valueOf(spaldingPriceData.getPrice()) / Double.valueOf(reportBean.getSales_unit());
                    BigDecimal bigDecimal =  new BigDecimal(salesPrice).setScale(3, BigDecimal.ROUND_HALF_UP);
                    // 	SalesPrices
                    csvWriter.write(bigDecimal.toString());
                    //Sales Unit
                    csvWriter.write(strUnit);
                    // 	LineAmount
                    csvWriter.write(spaldingPriceData.getPrice());
                    priceFlg = true;
                    break;
                }
            }
            if (priceFlg == false){
                // 	SalesPrices
                csvWriter.write("");
                //Sales Unit
                csvWriter.write(strUnit);
                // 	LineAmount
                csvWriter.write("");
            }
            csvWriter.endRecord();
        }
        logger.info(format("[ %s ]  条数据准备写入结束", ReportDatas.size()));
        logger.info(format(" [ %s ]日报文件内容输出结束", fileNameA));
    }

    /**
     * @description CSV日报文件Bom内容输出
     * @param filePath  文件路径
     * @param fullName  文件标题
     * @param fileName  文件名
     *
     */
    private void addBom(String filePath, String  fullName, String fileName) throws Exception  {

        logger.info(format(" [ %s ]日报文件addBom开始", fileName));
        // bom 头部
        byte[] bs={(byte)0xef,(byte)0xbb,(byte)0xbf};
        try(RandomAccessFile raf = new RandomAccessFile(filePath + "/" + fileName, "rw");) {
            // 将写文件指针移到文件头
            raf.seek(0);
            raf.write(bs);
            //raf.close();
        } catch (Exception e) {
            logger.info(format("生成日报文件 [ %s ]  时addBom发生错误", fileName));
            logger.error(e.getMessage());
            Exception ex = new Exception(e.getMessage() + "   addBom发生错误");
            throw ex;
        }
        logger.info(format(" [ %s ]日报文件addBom结束", fileName));
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
     * 判断日报文件今天是否已生成
     * @param fileNameA           文件名
     * @param createdFileList        已经生成文件列表
     *
     */
    private boolean checkFileIsCreate(String fileNameA , List<String> createdFileList) {
        boolean isExistsFile = false;

        //判断文件是否已经生成
        for (String createdFile : createdFileList) {
            if (fileNameA.equals(createdFile)) {
                isExistsFile = true;
                logger.info(fileNameA + report_file_extension + "文件已经生成，不需要再次处理！");
                break;
            }
        }
        return isExistsFile;
    }

    /**
     * 日报文件生成后，日报文件名写入相应的判断文件
     * @param checkFileName String 文件路径及名称 如c:/fqf.txt
     * @param fileContent String 文件内容
     * @return
     */
    private  void writeFileCreated(String checkFileName, String fileContent) throws Exception{

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
            Exception ex = new Exception(e.getMessage() + "   writeFileCreated发生错误");
            throw ex;
        }
        logger.info(checkFileName + " 写入文件结束" );
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
     * 特殊商品SKU装换
     * @param  reportBean
     * @param  fileFlg
     * @return ThirdReportBean
     */
    private  ThirdReportBean changeSpecialSku(ThirdReportBean reportBean,int fileFlg,String order_channel_id) throws Exception{
        String specialSku = "";
        //specialSku = ChannelConfigs.getVal2(order_channel_id, ChannelConfigEnums.Name.special_goods_ball, reportBean.getTransfer_sku());
        if (fileFlg == 3 ) {
            //对方定制球SKU取得
            specialSku = ChannelConfigs.getVal2(order_channel_id, ChannelConfigEnums.Name.special_goods_ball, reportBean.getTransfer_sku());
        } else {
            //对方篮板SKU取得
            specialSku = ChannelConfigs.getVal2(order_channel_id, ChannelConfigEnums.Name.special_goods_backboard, reportBean.getTransfer_sku());
        }
        //当前SKU和对方SKU不一致
        if (!specialSku.equals(reportBean.getTransfer_sku())) {
            //reportBean.setTransfer_sku(specialSku);
            int value = specialSku.lastIndexOf("-");
            reportBean.setItemcode(specialSku.substring(0,value));
            reportBean.setSize(specialSku.substring(value+1));
        }
        return reportBean;
    }
}
