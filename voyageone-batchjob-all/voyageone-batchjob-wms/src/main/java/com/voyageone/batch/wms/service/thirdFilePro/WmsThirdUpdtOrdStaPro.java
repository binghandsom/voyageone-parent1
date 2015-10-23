package com.voyageone.batch.wms.service.thirdFilePro;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.modelbean.OrderUpdateBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description  解析JC 【JCTR_OrdersData_*.txt】文件, 获取订单状态，如果返回的状态是Canceled，则需要把相应的物品状态更新为Backordered。
 * @author sky
 * @create 20150825
 */
@Service
public class WmsThirdUpdtOrdStaPro extends ThirdFileProBaseService {

    public static final class ParamKey{
        private static final String TEMPTABLE = "tempTable";
        private static final String TEMPTABLEBEAN = "tempTableBean";
        private static final String TASKNAME = "task_name";
        private static final String ORDERCHANNELID = "orderChannelId";
        private static final String PREREQUISITE = "prerequisite";
        private static final String STATUS = "status";
        private static final String NOTES = "notes";
    }

    /**
     * @description 本类入口函数
     * @param channelId 渠道
     * @param taskControlList 任务相关配置
     * @param thirdPartyConfigBean 第三方配置
     */
    public void doRun(String channelId, List < TaskControlBean > taskControlList, List < ThirdPartyConfigBean > thirdPartyConfigBean) {
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        assert channel != null;
        log(channel.getFull_name() + ": " + getTaskName() + "开始");
        for (ThirdPartyConfigBean tcb : thirdPartyConfigBean) {
            try {
                processFile(tcb, channel.getOrder_channel_id());
            } catch(Exception e) {
                logger.error(channel.getFull_name() + "解析第三方订单文件【JCTR_OrdersData_*.txt】发生错误：", e);
                logIssue(channel.getFull_name() + "解析第三方订单文件【JCTR_OrdersData_*.txt】发生错误：" + e);
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
        //与文件字段对应的bean字段map获取
        final Map<String,String> colMap = getUsefulCol(tcb.getProp_val3(), firstRowContent);
        //文件分割方式
        final String splitStr = tcb.getProp_val4();
        //完成处理后的备份路径
        final String bakFilePath = tcb.getProp_val5();

        for(Object fileNameInfo : fileNameList) {
            String filenameInfoStr = (String) fileNameInfo;
            List<OrderUpdateBean> fileDataList = readFile(filePath, filenameInfoStr, colMap, firstRowContent, orderChannelId, splitStr);
            //过滤掉OrderUpdateBean中orderStatus不为Canceled的数据
            fileDataList = filterOrderStatus(fileDataList);
            if(fileDataList != null && fileDataList.size() > 0) {
                updateTabels(filenameInfoStr, orderChannelId, setParam(orderChannelId, fileDataList));
            }
            moveFile(filePath, filenameInfoStr, bakFilePath);
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
        //11（Open）
        params.put(ParamKey.PREREQUISITE, "('11')");
        //状态修改为 98（Backordered）
        params.put(ParamKey.STATUS, "98");
        //设置reservationLog的notes
        params.put(ParamKey.NOTES, "Status change to Backordered");
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
                sb.append("select '").append(orderUpdateBean.getClient_order_id()).append("' client_order_id").append(",")
                  .append("'").append(orderUpdateBean.getSource_order_id()).append("' source_order_id").append(",")
                  .append("'").append(orderUpdateBean.getUpc()).append("' barcode");
            }else{
                sb.append("select '").append(orderUpdateBean.getClient_order_id()).append("' client_order_id").append(",")
                  .append("'").append(orderUpdateBean.getSource_order_id()).append("' source_order_id").append(",")
                  .append("'").append(orderUpdateBean.getUpc()).append("' barcode")
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
                boolean returnCheck = updateReservation(paramMap);
                if(returnCheck){
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
     * @description 更新tt_reservation表的状态
     * @param paramMap 参数Map
     * @return 是否更新成功标识
     */
    private boolean updateReservation(Map<String, Object> paramMap) {
        log("更新tt_reservation数据开始");
        logger.info("tempTable:" + paramMap.get(ParamKey.TEMPTABLE));
        int updateCount = clientOrderDao.updateTTresStat2BackOrder(paramMap);
        int canceledNum = ((List<OrderUpdateBean>)paramMap.get(ParamKey.TEMPTABLEBEAN)).size();
        logger.info("文件中为orderStatus为【Canceled】的记录" + canceledNum + "条; 其中" + updateCount + "条记录被成功更新到 tt_reservation");
        log("更新tt_reservation数据结束");
        return updateCount > 0;
    }

    /**
     * @description 插入数据到wms_bt_reservation_log表
     * @param paramMap 参数Map
     * @return 是否成功标志
     */
    private boolean insertReservationLog(Map<String, Object> paramMap){
        log("插入数据到 wms_bt_reservation_log 表的开始");
        int insertCount = clientOrderDao.insertReservationLog(paramMap);
        int canceledNum = ((List<OrderUpdateBean>)paramMap.get(ParamKey.TEMPTABLEBEAN)).size();
        logger.info("文件中为orderStatus为【Canceled】的记录" + canceledNum + "条; 其中" + insertCount + "条记录成功插入 wms_bt_reservation_log");
        log("插入数据到 wms_bt_reservation_log 表结束");
        return true;
    }

    /**
     * @description 过滤orderStatus为【Cancelled】的数据
     * @param fileDataList OrderUpdateBean对象
     */
    private List<OrderUpdateBean> filterOrderStatus(List<OrderUpdateBean> fileDataList) {
        return fileDataList.stream().filter(orderUpdateBean -> "Canceled".equals(orderUpdateBean.getOrderStatus())).collect(Collectors.toList());
    }

    @Override public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override public String getTaskName() {
        return "WmsThirdUpdtOrdStaPro";
    }

    @Override protected void onStartup(List < TaskControlBean > taskControlList) throws Exception {

    }

}