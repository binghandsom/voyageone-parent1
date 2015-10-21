package com.voyageone.batch.wms.service.thirdFilePro;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.dao.ClientInventoryDao;
import com.voyageone.batch.wms.dao.ClientOrderDao;
import com.voyageone.batch.wms.dao.ClientShipmentDao;
import com.voyageone.batch.wms.modelbean.OrderUpdateBean;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public abstract class ThirdFileProBaseService extends BaseTaskService{
    @Autowired
    protected ClientInventoryDao clientInventoryDao;

    @Autowired
    protected ClientOrderDao clientOrderDao;

    @Autowired
    protected ClientShipmentDao clientShipmentDao;

    @Autowired
    protected TransactionRunner transactionRunner;

    //和配置表中的字段对应，根据这个数组的值，将对应的值赋值到bean对应的属性里面
    private String[] ORDUPDATEBENCOL = {"source_order_id", "creater", "created", "trackingNo", "client_order_id", "orderStatus", "upc"};

    //ftp下载第三方文件属性配置通过他找到对应的文件在本地的路径
    private static final String DOWNLOAD_FILE_PATH = "download_file_path";

    /**
     * @description 获取对应文件的本地路径
     * @param fileName 要处理的文件名
     * @return string 需要处理的文件的本地路径
     */
    protected String getFilePathByName(String orderChannelId, String fileName) {
        List< ThirdPartyConfigBean > ftpFilePaths = ThirdPartyConfigs.getThirdPartyConfigList(orderChannelId, DOWNLOAD_FILE_PATH);
        String filePath = "";
        for (ThirdPartyConfigBean tcb: ftpFilePaths) {
            if (tcb.getProp_val5().equals(fileName)) {
                filePath = tcb.getProp_val2();
            }
        }
        return filePath;
    }

    /**
     * @description 检查每行数据是否合法
     * @param lineTxt 数据行
     */
    protected void checkData(int lineNo, String lineTxt, String firstRowContent, String splitStr) {
        if (lineNo == 0) {
            if (StringUtils.isEmpty(firstRowContent)) {
                throw new RuntimeException("请在【com_mt_third_party_config】表设置【prop_val2】设置文件第一行内容");
            } else if (!firstRowContent.equals(lineTxt.trim())) {
                throw new RuntimeException("读入文件格式不对，请检查！");
            }
        } else {
            int lent = lineTxt.split(splitStr).length;
            if (lineTxt.split(splitStr).length != firstRowContent.split(splitStr).length) {
                throw new RuntimeException("文件第【" + lineNo + "】行数据格式不对：content【" + lineTxt + "】！");
            }
        }
    }

    /**
     * @description 抽象方法由子类实现
     * @param channelId 渠道
     * @param taskControlList 任务相关配置
     * @param thirdPartyConfigBean 第三方配置
     */
    public abstract void doRun(String channelId, List<TaskControlBean> taskControlList, List<ThirdPartyConfigBean> thirdPartyConfigBean);

    /**
     * @description 读取文件，将数据设置到OrderUpdateBean的集合里面
     * @param filePath 文件所在路径
     * @param filename 文件名称
     * @param colMap 文件字段和orderUpdateBean属性对应关系
     * @param firstRowContent 第一行内容（文件第一行标题）
     * @param orderChannelId 渠道
     * @return OrderUpdateBean集合
     */
    protected List<OrderUpdateBean> readFile(String filePath, String filename, Map<String, String> colMap, String firstRowContent, String orderChannelId, String splitStr) {
        log("读取 "+ filename + " 文件信息开始");
        List<OrderUpdateBean> ordDataList = new ArrayList<>();
        if(StringUtils.isEmpty(splitStr)){
            splitStr = ",";
        }
        String[] firstRowArr = firstRowContent.split(splitStr);
        try {
            filePath = filePath + "/" + filename;
            File file = new File(filePath);
            //判断文件是否下载完毕
            while (FileUtils.fileIsInUse(file)){
                Thread.sleep(1000);
            }
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), CommonUtil.getCharset(filePath));
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                int countRow = 0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    OrderUpdateBean orderUpdateBean = new OrderUpdateBean();
                    //数据合法性验证
                    checkData(countRow, lineTxt, firstRowContent, splitStr);
                    for (String colKey : colMap.keySet()) {
                        if(countRow != 0) {
                            String colData = getLineCol(colKey, firstRowArr, lineTxt, colMap, getTimeZone(orderChannelId), splitStr);
                            setBeanData(colKey, colData, orderUpdateBean);
                        }
                    }
                    if(!lineTxt.equals(firstRowContent)) {
                        ordDataList.add(orderUpdateBean);
                    }
                    countRow++;
                }
                bufferedReader.close();
                read.close();

                //文件中可处理数据总记录数为0
                if (countRow == 0) {
                    logger.info(filePath + "文件的可处理数据数为0");
                    throw new RuntimeException(filePath + "文件的可处理数据数为0");
                }
            } else {
                logger.info("找不到指定的文件 filePath:" + filePath);
                //throw new RuntimeException("找不到指定的文件 filePath:" + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("读取 "+ filePath + " 文件出错!" + e);
        }
        log("读取 " + filename + " 文件信息结束");
        return ordDataList;
    }

    /**
     * @description 日志处理
     * @param msg 写日志信息
     */
    protected void log(String msg){
        logger.info("===============" + msg + "===============");
    }

    /**
     * @description 建立文件中的字段和OrderUpdateBean的Map
     * @param usefulColStr 配置表中的文件中字段与OrderUpdateBean属性字段的对应关系
     * @param firstRowContent 文件第一行数据内容
     */
    protected Map<String,String> getUsefulCol(String usefulColStr, String firstRowContent) {
        Map<String, String> usefulColMap = new HashMap<>();
        String[] usefulColArr = usefulColStr.split(";");
        for(String usefulCol : usefulColArr){
            String[] tempColStr = usefulCol.split(":");
            if(tempColStr.length == 2 && firstRowContent.contains(tempColStr[1])) {
                usefulColMap.put(tempColStr[0], tempColStr[1]);
            }else if(!firstRowContent.contains(tempColStr[1])){
                throw new RuntimeException("对应文件中找不到配置中的字段：" + tempColStr[1]);
            }else {
                throw new RuntimeException("com_mt_third_party_config 的 Prop_val3 配置表字段与文件字段对应关系错误");
            }
        }
        return usefulColMap;
    }

    /**
     * @description 实际操作的文件名列表,按时间升序排序
     * @param oldFileName 文件名样例
     * @param filePath 文件路径
     * @return 文件夹下累死 oldFileName的文件数组
     */
    protected Object[] processFileNames(String oldFileName, String filePath) {
        Object[] fileNameArr = FileUtils.getFileGroup(oldFileName, filePath).toArray();
        if(fileNameArr.length == 0 ){
            log("指定的目录下下无任何文件！");
        } else {
            Arrays.sort(fileNameArr);
        }
        return fileNameArr;
    }

    /**
     * @description 移动文件到指定目录
     * @param srcPath 源文件位置
     * @param srcFileName 源文件名称
     * @param bakFilePath 目标文件路径
     */
    protected void moveFile(String srcPath, String srcFileName, String bakFilePath) {
        log("移动文件 " + srcFileName + " 到备份文件夹开始！");
        String bakFileName = srcFileName.substring(0, srcFileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + srcFileName.substring(srcFileName.lastIndexOf("."));
        FileUtils.copyFile(srcPath + "/" + srcFileName, bakFilePath + "/" + bakFileName);
        FileUtils.delFile(srcPath + "/" + srcFileName);
        log("移动文件 " + srcFileName + " 到备份文件夹结束！");
    }

    /**
     * @description 移动文件到指定目录
     * @param srcPath 源文件位置
     * @param srcFileName 源文件名称
     * @param bakFilePath 目标文件路径
     */
    protected void copyFile(String srcPath, String srcFileName, String bakFilePath) {
        log("复制文件 " + srcFileName + " 到备份文件夹开始！");
        String bakFileName = srcFileName.substring(0, srcFileName.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + srcFileName.substring(srcFileName.lastIndexOf("."));
        FileUtils.copyFile(srcPath + "/" + srcFileName, bakFilePath + "/" + bakFileName);
        log("复制文件 " + srcFileName + " 到备份文件夹结束！");
    }

    /**
     * @description 获取文件中的行中的某一列数据
     * @param key 文件与orderUpdateBean对应关系的Map中的Key（文件中的字段名）
     * @param firstRowArr 文件中的第一行数组（列名）
     * @param lineTxt 文件中的一行数据内容
     * @param colMap 文件与orderUpdateBean对应关系的Map
     * @param timeZone 配置中的时区（若是有时间相关的字段，将其转化成个GMT）
     * @return
     */
    private String getLineCol(String key , String[] firstRowArr, String lineTxt, Map<String, String> colMap, int timeZone, String splitStr) {
        for (int j = 0; j < firstRowArr.length; j++) {
            if (colMap.get(key).equals(firstRowArr[j])) {
                if(key.equals("created")) {
                    try {
                        return DateTimeUtil.getGMTTime(DateTimeUtil.changeENDateTimeToDefault(lineTxt.split(splitStr)[j]), timeZone);
                    }catch (Exception e){
                        throw new RuntimeException("将" + colMap.get(key) + "字段转化成格林威治时间失败！");
                    }
                }else{
                    return lineTxt.split(splitStr)[j];
                }
            }
        }
        throw new RuntimeException("数据文件中找不到配置的字段：" + key);
    }

    /**
     * @description 将文件中的数据对应到OrderUpdateBean，一行数据对应一个bean实例
     * @param colKey 文件中数据和bean对应关系
     * @param colData 字段值
     * @param orderUpdateBean bean对象
     */
    private void setBeanData(String colKey, String colData, OrderUpdateBean orderUpdateBean) {
        //source_order_id 赋值
        if (colKey.equals(ORDUPDATEBENCOL[0])) {
            orderUpdateBean.setSource_order_id(colData);
        }//creater 赋值
        else if (colKey.equals(ORDUPDATEBENCOL[1])) {
            orderUpdateBean.setCreater(colData);
        }//created 赋值
        else if(colKey.equals(ORDUPDATEBENCOL[2])){
            orderUpdateBean.setCreated(colData);
        }//trankingNo 赋值
        else if(colKey.equals(ORDUPDATEBENCOL[3])){
            orderUpdateBean.setTrackingNo(colData);
        }//client_order_id
        else if(colKey.equals(ORDUPDATEBENCOL[4])){
            orderUpdateBean.setClient_order_id(colData);
        }//order_status
        else if(colKey.equals(ORDUPDATEBENCOL[5])){
            orderUpdateBean.setOrderStatus(colData);
        }//upc
        else if(colKey.equals(ORDUPDATEBENCOL[6])){
            orderUpdateBean.setUpc(colData);
        }
    }

    /**
     * @description 获取配置时间
     * @param orderChannelId 渠道
     * @return 时区
     */
    private int getTimeZone(String orderChannelId) {
        String timeZoneStr = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.channel_time_zone);
        if(StringUtils.isEmpty(timeZoneStr)){
            return 0;
        }else {
            return Integer.parseInt(timeZoneStr);
        }
    }

}
