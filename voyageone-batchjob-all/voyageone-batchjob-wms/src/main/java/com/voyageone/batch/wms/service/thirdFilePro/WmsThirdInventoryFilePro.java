package com.voyageone.batch.wms.service.thirdFilePro;

import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service public class WmsThirdInventoryFilePro extends ThirdFileProBaseService {

    public void doRun(String channelId, List < TaskControlBean > taskControlList, List < ThirdPartyConfigBean > thirdPartyConfigBean) {
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        assert channel != null;
        logger.info(channel.getFull_name() + "----------" + getTaskName() + "----------开始");
        //获取相关渠道下需要处理的相关文件的配置
        for (ThirdPartyConfigBean tcb: thirdPartyConfigBean) {
            //处理文件
            try {
                processFile(tcb, channel.getOrder_channel_id(), taskControlList);
            } catch(Exception e) {
                String msg = channel.getFull_name() + "解析第三方库存文件发生错误" + e;
                logger.error(msg);
                logIssue(msg);
            }
        }
        logger.info(channel.getFull_name() + "----------" + getTaskName() + "----------结束");
    }

    /**
     *
     * @param tcb 对应渠道下的配置集合
     * @param orderChannelId 渠道
     * @param taskControlList  任务集合
     */
    private void processFile(ThirdPartyConfigBean tcb, final String orderChannelId, final List < TaskControlBean > taskControlList) {
        //需要处理的文件名
        String fileName = tcb.getProp_val1();
        //根据文件名获取文件路径
        final String filePath = getFilePathByName(orderChannelId, fileName);
        //实际操作的文件名
        fileName = processFileName(fileName, filePath);
        //文件第一行内容验证
        final String firstRowContent = tcb.getProp_val2();
        //拼接规则配置获取
        final String concatRule = tcb.getProp_val3();
        //是否需要更新 wms_bt_item_detal 表
        final String updateItemDetail = tcb.getProp_val4();
        //完成处理后的备份路径，为null为不需要备份
        final String bakFilePath = tcb.getProp_val5();
        //文件分割字符串
        final String splitStr = ",";
        //storeID取得
        long storeID = 0;
        List < StoreBean > storeBeans = StoreConfigs.getChannelStoreList(orderChannelId);
        assert storeBeans != null;
        for (StoreBean storeBean: storeBeans) {
            if (storeBean.getStore_location().equals(StoreConfigEnums.Location.CB.getId()) && storeBean.getStore_kind().equals("0")) {
                storeID = storeBean.getStore_id();
                break;
            }
        }

        final String finalFileName = fileName;
        final long finalStoreID = storeID;
        //开启事务处理
        transactionRunner.runWithTran(new Runnable() {@Override public void run() {
            try {
                //临时库存表中在SizeMapping表中不存在的记录集
                List < ClientInventoryBean > notExistsSizeMappingList;
                //临时库存表中在wms_bt_item_details表中不存在的记录集
                List < ClientInventoryBean > notExistsItemDetailsList;

                final String taskName = TaskControlUtils.getTaskName(taskControlList);
                //将库存文件信息全部导入临时库存表
                boolean returnCheck = insertClientInventoryByFileData(updateItemDetail, firstRowContent, concatRule, filePath, finalFileName, orderChannelId, finalStoreID, taskName, splitStr);
                //更新临时库存表的SKU,
                if (returnCheck) {
                    returnCheck = updateClientInventorySKU(orderChannelId, taskName);
                }
                //更新wms_bt_item_details
                if (returnCheck && "Y".equals(updateItemDetail)) {
                    returnCheck = updateItemDetailsSizeBarcode(orderChannelId, taskName);
                    //对wms_bt_item_details表中更新成功的记录，更新临时库存表的syn_flg（更新成功）
                    if (returnCheck) {
                        returnCheck = updateClientInventorySynflgSucc(orderChannelId, taskName);
                    }
                }
                //临时库存表中在SizeMapping表中不存在的记录，更新临时库存表的syn_flg
                if (returnCheck) {
                    returnCheck = updateClientInventorySynflgIgnore(orderChannelId, taskName);
                }

                if (returnCheck) {
                    //获取临时库存表中在SizeMapping表中不存在的记录
                    //notExistsSizeMappingList = clientInventoryDao.getNotExistsSizeMapping(orderChannelId);
                    //发送警告邮件
//                    createErrorMail(notExistsSizeMappingList, "1");
                    if ("Y".equals(updateItemDetail)) {
                        //获取临时库存表中在wms_bt_item_details表中不存在的记录
                        notExistsItemDetailsList = clientInventoryDao.getNotExistsItemDetails(orderChannelId);
                        //发送警告邮件
                        createErrorMail(notExistsItemDetailsList, "2");
                    }
                    //移除文件到指定目录
                    moveFiles(filePath, tcb.getProp_val1(), bakFilePath);
                }
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
        });
    }

    /**
     * @description 获取一批文件中时间搓最大的文件名 源文件格式必须是 xxx_*.xxx
     * @param oldFileName 原始文件名
     * @param filePath 文件所在的文件夹路径
     * @return string 文件名排序后时间撮最大的文件名
     */
    private String processFileName(String oldFileName, String filePath) {
        if (oldFileName.contains("*")) {
            Object[] sortFileNameArr = FileUtils.getFileGroup(oldFileName, filePath).toArray();
            Arrays.sort(sortFileNameArr, Collections.reverseOrder());
            if (sortFileNameArr.length > 0) {
                return (String) sortFileNameArr[0];
            }else {
                return "";
            }
        } else {
            return oldFileName;
        }
    }

    /**
     * @description 将库存文件信息全部导入临时库存表
     * @return 是否插入成功
     */
    private boolean insertClientInventoryByFileData(String updateItemDetail, String firstLineData, String concatRule, String filePath, String filename, String orderchannelId, long storeID, String taskName, String splitStr) throws IOException {
        OrderChannelBean channel = ChannelConfigs.getChannel(orderchannelId);
        logger.info(channel.getFull_name() + "将库存文件信息全部导入表开始");
        try {
            filePath = filePath + "/" + filename;
            File file = new File(filePath);
            if(!file.exists()){
                log("文件不存在：" + filePath);
                return false;
            }
            //判断文件是否下载完毕
            while (FileUtils.fileIsInUse(file)) {
                Thread.sleep(1000);
            }

            if (file.isFile() && file.exists()) {
//                logger.info(channel.getFull_name() + "删除第三方库存表中的记录开始");
//                //删除临时库存表数据
//                clientInventoryDao.delClientInventory(orderchannelId);
//                logger.info(channel.getFull_name() + "删除第三方库存表中的记录结束");
                //获得文件编码
                String encodeing = CommonUtil.getCharset(filePath);
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encodeing);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                int i = 0, countRow = 0;
                StringBuilder sqlBuffer = new StringBuilder();
                String ordersBatchStr = "";
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    //数据合法性验证
                    checkData(i, lineTxt, firstLineData, splitStr);
                    if (i > 0) {
                        //根据不同的渠道将文件数据处理成固定格式数组
                        String[] stringArr = proLineData(concatRule, lineTxt, firstLineData);
                        //生成SQL文
                        ordersBatchStr = getClientInventorySqlData(updateItemDetail, sqlBuffer, stringArr, orderchannelId, storeID, taskName);
                        //500条数据执行一次插入处理
                        if (i == 500) {
                            clientInventoryDao.insertClientInventory(ordersBatchStr.substring(0, ordersBatchStr.length() - 1));
                            ordersBatchStr = "";
                            sqlBuffer.setLength(0);
                            i = 1;
                        }
                    }
                    //文件中可处理数据总记录数 + 1
                    countRow = countRow + 1;
                    i = i + 1;
                }
                bufferedReader.close();
                read.close();
                //文件中可处理数据总记录数为0
                if (countRow == 0) {
                    logger.info(filePath + "文件的可处理数据数为0");
                    throw new RuntimeException(filePath + "文件的可处理数据数为0");
                }
                //最后一次不够500条的进行兜底插入
                if (i > 0 && !StringUtils.isNullOrBlank2(ordersBatchStr)) {
                    clientInventoryDao.insertClientInventory(ordersBatchStr.substring(0, ordersBatchStr.length() - 1));
                    logger.info("至第" + countRow + "条记录插入数据");
                }
            } else {
                logger.info(channel.getFull_name() + "找不到指定的文件 filePath:" + filePath);
                return false;
                //throw new RuntimeException("找不到指定的文件 filePath:" + filePath);
            }
        }catch (Exception e){
            throw new RuntimeException(channel.getFull_name() + "将库存文件信息全部导入表失败：" + e);
        }
        logger.info(channel.getFull_name() + "将库存文件信息全部导入表结束");
        return true;
    }

    /**
     * @description 处理数组得到想要格式的数组，【clientSku,qty,barCode】
     * @param concatRule 连接规则字符串
     */
    private String[] proLineData(String concatRule, String lineData, String firstLineData) {
        String[] resArr = new String[3]; //返回结果数组
        String[] colArr = concatRule.split(";"); //插入client_sku,qty,barCode对应的字段
        String[] concatRuleArr = colArr[0].split(","); //第一个字段(client_sku)的拼接规则
        String[] lineDataArr = lineData.split(","); //每行数据
        String[] firstLineDataArr = firstLineData.split(","); //第一行数据
        String concatRes = ""; //client_sku 拼接结果
        boolean flag = false;

        //目前只需要库存同步文件中间的三个字段，配置大于3个字段则报错
        if (colArr.length > 3) {
            String logMsg = "配置拼装规则错误！请检查【com_mt_third_party_config】表设置";
            logger.error(logMsg);
            throw new RuntimeException(logMsg);
        }

        //按照配置规则拼装clientSku
        for (String ruleElm: concatRuleArr) {
            for (int j = 0; j < firstLineDataArr.length; j++) {
                flag = false;
                if (ruleElm.equals(firstLineDataArr[j])) {
                    concatRes = concatRes + lineDataArr[j];
                    flag = true;
                    break;
                }
            }
            if (!flag && "-".equals(ruleElm)) {
                concatRes = concatRes + ruleElm;
            } else if (!flag) {
                String logMsg = "数据文件中找不到拼接规则所需要的字段！请检查【com_mt_third_party_config】表设置";
                logger.error(logMsg);
                throw new RuntimeException(logMsg);
            }
        }
        resArr[0] = concatRes;

        //根据配置获取对应的字段组成数组
        for (int i = 1; i < colArr.length; i++) {
            for (int j = 0; j < firstLineDataArr.length; j++) {
                if (colArr[i].equals(firstLineDataArr[j])) {
                    resArr[i] = lineDataArr[j];
                    break;
                }
            }
        }
        return resArr;
    }

    /**
     * 更新临时库存表的SKU,itemCode,size
     * @param orderchannelId 运行渠道
     * @param taskName 任务名
     * @return 是否成功
     */
    private boolean updateClientInventorySKU(String orderchannelId, String taskName) {
        logger.info("更新临时库存表的SKU开始");
        int returnValue = clientInventoryDao.updateSKUBySizeMapping(orderchannelId, taskName);
        logger.info(returnValue + "条记录更新结束");
        logger.info("更新临时库存表的SKU结束");
        return true;
    }

    /**
     * 更新wms_bt_item_details的Size,Barcode
     * @param orderchannelId 运行渠道
     * @param taskName 任务名
     * @return 是否更新成功
     */
    private boolean updateItemDetailsSizeBarcode(String orderchannelId, String taskName) {
        logger.info("更新ItemDetails表的开始");
        int returnValue = clientInventoryDao.updateItemDetailsSizeBarcode(orderchannelId, taskName);
        logger.info(returnValue + "条记录更新结束");
        logger.info("更新ItemDetails的结束");
        return true;
    }

    /**
     * 临时库存表中在SizeMapping表中不存在的记录，更新临时库存表的syn_flg（更新忽略）
     * @param orderchannelId 运行渠道
     * @param taskName 任务名
     * @return 更新成功标识
     */
    private boolean updateClientInventorySynflgIgnore(String orderchannelId, String taskName) {
        logger.info("更新临时库存表的syn_flg（更新忽略）开始");
        int returnValue = clientInventoryDao.updateClientInventorySynflgIgnore(orderchannelId, taskName);
        logger.info(returnValue + "条记录更新结束");
        logger.info("更新临时库存表的syn_flg（更新忽略）结束");
        return true;
    }

    /**
     * 对wms_bt_item_details表中更新成功的记录，更新临时库存表的syn_flg（更新成功）
     * @param orderchannelId 运行渠道
     * @param taskName 任务名
     * @return 更新成功标识
     */
    private boolean updateClientInventorySynflgSucc(String orderchannelId, String taskName) {
        logger.info("更新临时库存表的syn_flg（更新成功）开始");
        int returnValue = clientInventoryDao.updateClientInventorySynflgSucc(orderchannelId, taskName);
        logger.info(returnValue + "条记录更新结束");
        logger.info("更新临时库存表的syn_flg（更新成功）结束");
        return true;
    }

    /**
     * 批处理新库存临时表信息所需数据拼装
     *
     * @param stringArr 数据数组
     * @param orderchannelId 渠道ID
     * @param storeID 仓库Id
     * @param taskName 任务名
     * @return String sql字符串
     */
    private String getClientInventorySqlData(String updateItemDetail, StringBuilder sqlBuffer, String[] stringArr, String orderchannelId, long storeID, String taskName) {
        // 拼装SQL values 部分
        sqlBuffer.append(preparetClientInventoryData(updateItemDetail, stringArr, orderchannelId, String.valueOf(storeID), taskName));
        sqlBuffer.append(Constants.COMMA_CHAR);
        return sqlBuffer.toString();
    }

    /**
     * 一条库存临时表的插入语句values部分
     * @param stringArr 处理后的从文件中取出来的数据数组
     * @param orderChannelId 渠道
     * @param storeId 仓库ID
     * @param taskName 任务名称
     * @return 更新成功标识
     */
    private String preparetClientInventoryData(String updateItemDetail, String[] stringArr, String orderChannelId, String storeId, String taskName) {

        StringBuilder sqlValueBuffer = new StringBuilder();
        sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

        // order_channel_id
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(orderChannelId);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        //client_sku
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(stringArr[0]);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        //sku
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(0);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // qty
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(stringArr[1]);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // store_id
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(storeId);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // barcode
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        String barCode = "";
        if (!StringUtils.isEmpty(stringArr[2])) barCode = stringArr[2];
        sqlValueBuffer.append(barCode);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // syn_flg
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        if ("N".equals(updateItemDetail)) {
            sqlValueBuffer.append(WmsConstants.SYN_FLG.SUCCESS);
        } else {
            sqlValueBuffer.append(WmsConstants.SYN_FLG.INITAL);
        }
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        //sim_flg
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append("0");
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        //active
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append("1");
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // created
        sqlValueBuffer.append(Constants.NOW_MYSQL);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // creater
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(taskName);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // modified
        sqlValueBuffer.append(Constants.NOW_MYSQL);
        sqlValueBuffer.append(Constants.COMMA_CHAR);

        // modifier
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(taskName);
        sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
        sqlValueBuffer.append(Constants.RIGHT_BRACKET_CHAR);
        return sqlValueBuffer.toString();
    }

    /**
     * 错误邮件生成
     * @param errorList 错误一览
     * @return 错误邮件内容
     */
    private void createErrorMail(List < ClientInventoryBean > errorList, String flagValue) throws MessagingException {
        String subject, head, logInfo;
        //临时库存表中在SizeMapping表中不存在的记录错误邮件出力
        if (flagValue.equals("1")) {
            subject = WmsConstants.EmailClientInventoryError.SUBJECT_NOT_EXISTS_SIZEMAPPING;
            head = WmsConstants.EmailClientInventoryError.HEAD_NOT_EXISTS_SIZEMAPPING;
            logInfo = "临时库存表中在SizeMapping表中不存在的记录错误邮件输出";
        } else {
            //临时库存表中在ItemDetails表中不存在的记录错误邮件输出
            subject = WmsConstants.EmailClientInventoryError.SUBJECT_NOT_EXISTS_ITEMDEAILS;
            head = WmsConstants.EmailClientInventoryError.HEAD_NOT_EXISTS_ITEMDEAILS;
            logInfo = "临时库存表中在ItemDetails表中不存在的记录错误邮件输出";
        }
        if (errorList != null && errorList.size() > 0) {
            //发送警告邮件
            String errorNotExistsSizeMappingMail = sendErrorMail(errorList, head);
            if (!StringUtils.isNullOrBlank2(errorNotExistsSizeMappingMail)) {
                logger.info(logInfo);
                Mail.sendAlert(CodeConstants.EmailReceiver.ITWMS, subject, errorNotExistsSizeMappingMail);
            }
        }
    }

    /**
     * 错误邮件出力
     * @param errorSkuList 错误一览
     * @return 错误邮件内容
     */
    private String sendErrorMail(List < ClientInventoryBean > errorSkuList, String head) {
        StringBuilder builderContent = new StringBuilder();
        if (errorSkuList.size() > 0) {
            StringBuilder builderDetail = new StringBuilder();
            int index = 0;
            for (ClientInventoryBean errorSku: errorSkuList) {
                index = index + 1;
                builderDetail.append(String.format(WmsConstants.EmailClientInventoryError.ROW, index, errorSku.getOrder_channel_id(), errorSku.getClient_sku(), errorSku.getQty(), errorSku.getStore_id(), errorSku.getItemCode(), StringUtils.null2Space2(errorSku.getSku()).equals("0") ? "": errorSku.getSku(), StringUtils.null2Space2(errorSku.getSize()).equals("") ? "": errorSku.getSize()));
            }
            String count = String.format(WmsConstants.EmailClientInventoryError.COUNT, errorSkuList.size());
            String detail = String.format(WmsConstants.EmailClientInventoryError.TABLE, count, builderDetail.toString());
            builderContent.append(Constants.EMAIL_STYLE_STRING).append(head).append(detail);
        }
        return builderContent.toString();
    }

    /**
     * 移动文件到指定目录
     * @param srcPath 源文件位置
     * @param srcFileName 源文件名称
     * @param bakFilePath 目标文件路径
     */
    private void moveFiles(String srcPath, String srcFileName, String bakFilePath) {
        //移动库存文件进入备份文件夹
        Object[] srcFileGroup = FileUtils.getFileGroup(srcFileName, srcPath).toArray();
        for (Object aFileName: srcFileGroup) {
            String fileNameStr = (String) aFileName;
            String bakFileName = fileNameStr.substring(0, fileNameStr.lastIndexOf(".")) + "_" + DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_2) + fileNameStr.substring(fileNameStr.lastIndexOf("."));
            FileUtils.copyFile(srcPath + "/" + fileNameStr, bakFilePath + "/" + bakFileName);
            FileUtils.delFile(srcPath + "/" + fileNameStr);
        }
    }

    @Override public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override public String getTaskName() {
        return "ThirdInventoryFilePro";
    }

    @Override protected void onStartup(List < TaskControlBean > taskControlList) throws Exception {

    }
}