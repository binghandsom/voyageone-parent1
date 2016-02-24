package com.voyageone.batch.wms.service.thirdFilePro;

import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.modelbean.OrderUpdateBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service public class WmsThirdOrdFilePro extends ThirdFileProBaseService {

    private List<OrderUpdateBean> bakOrderUpdtBean = new ArrayList<>();
    private String bakTempTable = "";

    public static final class ParamKey{
        private static final String TEMPTABLE = "tempTable";
        private static final String TEMPTABLEBEAN = "tempTableBean";
        private static final String TASKNAME = "task_name";
        private static final String ORDERCHANNELID = "orderChannelId";
        private static final String PREREQUISITE = "prerequisite";
        private static final String STATUS = "status";
        private static final String NOTES = "notes";
    }

    public void doRun(String channelId, List < TaskControlBean > taskControlList, List < ThirdPartyConfigBean > thirdPartyConfigBean) {
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        assert channel != null;
        log(channel.getFull_name() + ": " + getTaskName() + "开始");
        for (ThirdPartyConfigBean tcb : thirdPartyConfigBean) {
            try {
                processFile(tcb, channel.getOrder_channel_id());
            } catch(Exception e) {
                logger.error(channel.getFull_name() + "解析第三方订单文件发生错误：", e);
                logIssue(channel.getFull_name() + "解析第三方订单文件发生错误：" + e);
            }
        }
        log(channel.getFull_name() + ": " + getTaskName() + "结束");
    }

    /**
     * @description 文件数据做相关处理
     * @param tcb 第三方表的配置
     * @param orderChannelId 渠道
     */
    private void processFile(ThirdPartyConfigBean tcb, String orderChannelId) {
        //需要处理的文件名
        String fileName = tcb.getProp_val1();
        //根据文件名获取文件路径
        final String filePath = getFilePathByName(orderChannelId, fileName);
        //实际操作的文件名列表,按时间升序排序
        final Object[] fileNameList = processFileNames(fileName, filePath);
        //文件第一行内容验证
        final String firstRowContent = tcb.getProp_val2();
        //source_order_id 字段对应文件的字段
        final Map<String,String> colMap = getUsefulCol(tcb.getProp_val3(), firstRowContent);
        //文件分割方式
        final String splitStr = tcb.getProp_val4();
        //完成处理后的备份路径
        final String bakFilePath = tcb.getProp_val5();

        for(Object fileNameInfo : fileNameList) {
            String filenameInfoStr = (String) fileNameInfo;
            List<OrderUpdateBean> fileDataList = readFile(filePath, filenameInfoStr, colMap, firstRowContent, orderChannelId, splitStr);
            if(fileDataList != null && fileDataList.size() > 0) {
                updateTabels(filenameInfoStr, orderChannelId, setParam(orderChannelId, fileDataList));
                moveFile(filePath, filenameInfoStr, bakFilePath);
            }
        }
    }

    /**
     * @description 设置数据库操作需要用到的参数
     * @param orderChannelId 渠道
     * @param fileDataList 文件中的所有数据beanList
     * @return paramMap
     */
    private Map<String, Object> setParam(String orderChannelId, List<OrderUpdateBean> fileDataList) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.TEMPTABLE, createTempTableByFileDate(fileDataList));
        params.put(ParamKey.TEMPTABLEBEAN, fileDataList);
        params.put(ParamKey.TASKNAME, getTaskName());
        params.put(ParamKey.ORDERCHANNELID, orderChannelId);
        //11（Open），12（Reserved）或99（Cancelled）
        params.put(ParamKey.PREREQUISITE,"('11', '12', '99')");
        //状态修改为 24（品牌方发货）
        params.put(ParamKey.STATUS, CodeConstants.Reservation_Status.ShippedTP);
        //设置reservationLog的notes
        params.put(ParamKey.NOTES, "Status change to Package");
        return params;
    }

    /**
     * @description 拼接临时表
     * @param fileDataList 文件中的所有数据
     * @return sql语句
     */
    private String createTempTableByFileDate(List<OrderUpdateBean> fileDataList) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for(OrderUpdateBean orderUpdateBean : fileDataList){
            i++;
            if(i == fileDataList.size()){
                sb.append("select '").append(orderUpdateBean.getSource_order_id()).append("' source_order_id").append(",")
                        .append("'").append(orderUpdateBean.getTrackingNo()).append("' trankingNo").append(",")
                        .append("'").append(orderUpdateBean.getCreated()).append("' created").append(",")
                        .append("'").append(orderUpdateBean.getCreater()).append("' creater");
            }else{
                sb.append("select '").append(orderUpdateBean.getSource_order_id()).append("' source_order_id").append(",")
                        .append("'").append(orderUpdateBean.getTrackingNo()).append("' trankingNo").append(",")
                        .append("'").append(orderUpdateBean.getCreated()).append("' created").append(",")
                        .append("'").append(orderUpdateBean.getCreater()).append("' creater")
                        .append(" union all ");
            }
        }
        return sb.toString();
    }

    /**
     * @description 更新数据库
     * @param fileNameInfo 文件名称
     * @param orderChannelId 渠道Id
     * @param paramMap 参数Map
     */
    private void updateTabels(final String fileNameInfo, final String orderChannelId, final Map<String, Object> paramMap){
        log("处理文件 " + fileNameInfo + " 开始");
        transactionRunner.runWithTran(() -> {
            try {
                //tt_orders、tt_reservation 更新：当状态小于14时，状态变为14
                boolean returnCheck = updateOrdRes(paramMap);
                //插入 tt_res_tracking 按照物品级别插入，tracking_type:'SYB'
                if (returnCheck) {
                    returnCheck = insertResTracking(paramMap);
                }
                //插入tt_tracking表，按照运单级别插入，tracking_type:'SYB' 仿照EMS的插入
                if (returnCheck) {
                    returnCheck = insertTracking(paramMap);
                }
                //按照物品级别插入，插入tracking_status=050的记录
                if (returnCheck) {
                    returnCheck = insertTrackingInfo(paramMap);
                }
                //插入数据到 wms_bt_reservation_log
                if (returnCheck) {
                    returnCheck = insertReservationLog(paramMap);
                }
                if(returnCheck){
                    log("处理文件 " + fileNameInfo + " 结束");
                }
            } catch(Exception e) {
                logger.error(orderChannelId + "处理第三方订单更新文件失败！" + e);
                throw new RuntimeException(orderChannelId + "处理第三方订单更新文件失败！");
            }
        });
    }

    /**
     * @description 插入数据到wms_bt_reservation_log表
     * @param paramMap 参数Map
     * @return 是否插入成功
     */
    private boolean insertReservationLog(Map<String, Object> paramMap){
        log("插入数据到 wms_bt_reservation_log 表的开始");
        int insertCount = clientOrderDao.insertReservationLog(paramMap);
        logger.info(insertCount + "条记录成功插入 wms_bt_reservation_log");
        log("插入数据到 wms_bt_reservation_log 表结束");
        return true;
    }

    /**
     * @description 修改order表和tt_reservation表的状态
     * @param paramMap 参数Map
     * @return 是否修改成功标识
     */
    private boolean updateOrdRes(Map<String, Object> paramMap) {
        log("更新 tt_orders、 tt_reservation 表的状态为" + paramMap.get(ParamKey.STATUS) + "开始");
        int updateOrdCount = clientOrderDao.updateOrders(paramMap);
        logger.info("tt_orders 表" + updateOrdCount + "条记录成功更新");
        int updateResCount = clientOrderDao.updateReservation(paramMap);
        logger.info("tt_reservation 表" + updateResCount + "条记录成功更新");
        log("更新 tt_orders、 tt_reservation 表的状态为" + paramMap.get(ParamKey.STATUS)+ "结束");
        return true;
    }

    /**
     * @description 插入数据到trankingInfo表
     * @param paramMap 参数Map
     * @return 是否插入成功
     * @throws MessagingException
     */
    private boolean insertTrackingInfo(Map<String, Object> paramMap) throws MessagingException {
        log("插入数据到 tt_tracking_info 表的开始");
        List<OrderUpdateBean> ordExistTTTrackingInfoList = clientOrderDao.insertTTTrackingInfoCheck(paramMap);
        //备份paramMap
        bakParam(paramMap);
        if(ordExistTTTrackingInfoList.size() > 0){
            String logMsg = "文件中的对应数据在 tt_tracking_info 表中已经存在，不能重复插入；数据：";
            //过滤掉重复数据
            paramMap = filterData(ordExistTTTrackingInfoList, paramMap, logMsg);
        }
        int insertCount = 0;
        if(!StringUtils.isEmpty((String) paramMap.get(ParamKey.TEMPTABLE))) {
            insertCount = clientOrderDao.insertTTTrackingInfo(paramMap);
        }else{
            log("tt_tracking_Info表中所有记录均为重复记录。");
        }
        //重新还原参数Map
        rollBackParam(paramMap);
        logger.info(insertCount + "条记录成功插入tt_tracking_info");
        log("插入数据到 tt_tracking_info 表结束");
        return true;
    }

    /**
     * @description 插入数据到Tracking表
     * @param paramMap 参数Map
     * @return 是否插入成功标识
     */
    private boolean insertTracking(Map<String, Object> paramMap) {
        log("插入数据到 tt_tracking 表的开始");
        List<OrderUpdateBean> ordExistTTTrackingList = clientOrderDao.insertTTTrackingCheck(paramMap);
        //备份paramMap
        bakParam(paramMap);
        if(ordExistTTTrackingList.size() > 0){
            String logMsg = "文件中的对应数据在 tt_tracking 表中已经存在，不能重复插入；数据：";
            paramMap = filterData(ordExistTTTrackingList, paramMap, logMsg);
        }
        int insertCount = 0;
        if(!StringUtils.isEmpty((String) paramMap.get(ParamKey.TEMPTABLE))) {
            insertCount = clientOrderDao.insertTTTracking(paramMap);
        }else{
            log("tt_tracking表中所有数据均为重复数据。");
        }
        //重新设置Map
        rollBackParam(paramMap);
        logger.info(insertCount + "条记录成功插入tt_tracking");
        log("插入数据到 tt_tracking 表结束");
        return true;
    }

    /**
     * @description 插入数据到resTrancking表
     * @param paramMap 参数Map
     * @return 是否插入成功标识
     */
    private boolean insertResTracking(Map<String, Object> paramMap) {
        log("插入数据到 tt_res_tracking 表的开始");
        List<OrderUpdateBean> ordExistTTResTrackingList = clientOrderDao.insertTTResTrackingCheck(paramMap);
        bakParam(paramMap);
        if(ordExistTTResTrackingList.size() > 0){
            String logMsg = "文件中的对应数据在 tt_res_tracking 表中已经存在，不能重复插入；数据：";
            paramMap = filterData(ordExistTTResTrackingList, paramMap, logMsg);
        }
        int insertCount = 0;
        if(!StringUtils.isEmpty((String) paramMap.get(ParamKey.TEMPTABLE))){
            insertCount = clientOrderDao.insertTTResTracking(paramMap);
        }else{
            log("tt_res_tracking表中所有数据均为重复数据。");
        }
        rollBackParam(paramMap);
        logger.info(insertCount + "条记录成功插入tt_res_traking");
        log("插入数据到 tt_res_tracking 表结束");
        return true;
    }

    /**
     * @description 回滚PramMap
     * @param paramMap 参数Map
     */
    private void rollBackParam(Map<String, Object> paramMap) {
        paramMap.put(ParamKey.TEMPTABLEBEAN, bakOrderUpdtBean);
        paramMap.put(ParamKey.TEMPTABLE, bakTempTable);
    }

    /**
     * @description 备份参数Map
     * @param paramMap 参数Map
     */
    private void bakParam(Map<String, Object> paramMap) {
        bakOrderUpdtBean = (List<OrderUpdateBean>) ((ArrayList) paramMap.get(ParamKey.TEMPTABLEBEAN)).clone();
        bakTempTable = (String) paramMap.get(ParamKey.TEMPTABLE);
    }

    /**
     * @description 过滤重复数据
     * @param ordExistTTResTrackingList 已经存在的数据List
     * @param paramMap 参数Map
     * @param logMsg 打印日志的字符串
     * @return 过滤后的参数Map
     */
    private Map<String, Object> filterData(List<OrderUpdateBean> ordExistTTResTrackingList, Map<String, Object> paramMap, String logMsg) {
        List<OrderUpdateBean> orderUpdateBeans = (List<OrderUpdateBean>) paramMap.get(ParamKey.TEMPTABLEBEAN);
        for(int i = 0; i < orderUpdateBeans.size(); i ++) {
            for (OrderUpdateBean orderUpdateBeanExist : ordExistTTResTrackingList) {
                if(orderUpdateBeans.size() == 0) break;
                String fileDate = JsonUtil.getJsonString(orderUpdateBeans.get(i));
                String existDate = JsonUtil.getJsonString(orderUpdateBeanExist);
                if (fileDate.equals(existDate)) {
                    orderUpdateBeans.remove(i);
                    logIssue(logMsg + fileDate);
                }
            }
        }
        //重置过滤后的tempTable
        paramMap.put(ParamKey.TEMPTABLE, createTempTableByFileDate(orderUpdateBeans));
        return paramMap;
    }

    @Override public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override public String getTaskName() {
        return "ThirdOrderFilePro";
    }

    @Override protected void onStartup(List < TaskControlBean > taskControlList) throws Exception {

    }

}