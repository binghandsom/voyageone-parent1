package com.voyageone.batch.wms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.WmsConstants.updateClientInventoryConstants;
import com.voyageone.batch.wms.dao.ClientInventoryDao;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.common.components.channelAdvisor.service.InventoryService;
import com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean;
import com.voyageone.common.components.channelAdvisor.soap.InventoryResSoapenv;
import com.voyageone.common.components.channelAdvisor.webservices.ArrayOfInventoryItemResponse;
import com.voyageone.common.components.channelAdvisor.webservices.InventoryItemResponse;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
import com.voyageone.common.configs.dao.ThirdPartConfigDao;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

/**
 *
 * Created by sky on 2015/7/29.
 */
@Service
@Transactional
public class WmsGetClientInvService extends BaseTaskService {
    @Autowired
    ClientInventoryDao clientInventoryDao;

    @Autowired
    ThirdPartConfigDao thirdPartConfigDao;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    private TransactionRunner transactionRunner;

    //允许webSericce请求超时的连续最大次数
    private static int ALLOWLOSEPAGECOUNT = 10;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
            return "wmsGetClientInvJob";
    }

    protected void onStartup(final List<TaskControlBean> taskControlList) throws Exception {
        log(getTaskName() + "----------开始");
        //允许运行的渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        //线程
        List<Runnable> threads = new ArrayList<>();
        for(String channelId : orderChannelIdList){
            sysInventoryByClient(setParamBean(channelId), channelId, threads);
        }
        runWithThreadPool(threads, taskControlList);
        log(getTaskName() + "----------结束");
    }

    private List<InventoryResSoapenv> getClientInv(GetInventoryParamBean getInventoryParamBean, String channelId, String updateType){
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        List<InventoryResSoapenv> inventoryResSoapenvList = new ArrayList<>();
        assert channel != null;
        int pageIndex = getInventoryParamBean.getnPageIndex();
        int losePageCount = 1;
        try {
            log(channel.getFull_name() + "库存取得开始");
            while (true){
                logger.info("----------当前执行到第【" + pageIndex + "】页----------");
                InventoryResSoapenv responseBean ;
                try {
                    responseBean = inventoryService.getCaInventory(getInventoryParamBean, updateType);
                }catch (Exception e){
                    logger.info("----------第【" + pageIndex + "】页数据请求失败！----------");
                    if(losePageCount == ALLOWLOSEPAGECOUNT){
                        String msg = "已经连续【" + ALLOWLOSEPAGECOUNT + "】次请求webService库存数据失败！" + e;
                        logger.info("----------" + msg + "----------");
                        throw new RuntimeException(msg);
                    }
                    losePageCount ++;
                    pageIndex++;
                    continue;
                }
                losePageCount = 1;
                ArrayOfInventoryItemResponse arrayOfInventoryItemResponse = responseBean.getBody().getFilteredInventoryItemListResponse().getGetFilteredInventoryItemListResult().getResultData();
                List<InventoryItemResponse> inventoryItemResponses = new ArrayList<>();
                if(null != arrayOfInventoryItemResponse) {
                    inventoryItemResponses = arrayOfInventoryItemResponse.getInventoryItemResponse();
                    inventoryResSoapenvList.add(responseBean);
                }else {
                    if (updateClientInventoryConstants.FULL.equals(updateType)) {
                        log("全量库存取得时无库存变化！");
                    } else {
                        log("时间段：【" + getInventoryParamBean.getDateTimeStart() + " - " + getInventoryParamBean.getDateTimeEnd() + "】无库存变化！");
                    }
                }
                if (inventoryItemResponses.size() < Integer.parseInt(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.PAGESIZE).get(0).getProp_val1())){
                    break;
                }else{
                    pageIndex++;
                    getInventoryParamBean.setnPageIndex(pageIndex);
                }
            }
            log(channel.getFull_name() + "库存取得结束");
            return inventoryResSoapenvList;
        } catch (Exception e) {
            //库存取得失败后记录pageIndex（pageIndex - ALLOWLOSEPAGECOUNT + 1）,设置认识失败的开始时间
            setFaildConfig(channel.getOrder_channel_id(), pageIndex - ALLOWLOSEPAGECOUNT + 1, getInventoryParamBean);
            String msg = channel.getFull_name() + "取得库存失败" + e;
            log(msg);
            logIssue(msg);
            throw new RuntimeException(msg);
        }
    }

    /**
     * @description 获取更新类型
     * @param order_channel_id 渠道
     * @return String FULL、全量； INCREACE、增量
     */
    private String getUpdateType(String order_channel_id) {
        ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(order_channel_id, updateClientInventoryConstants.FULLUPDATECONFIG);
        if(thirdPartyConfigBean.getProp_val1().equals("0") || StringUtils.isEmpty(thirdPartyConfigBean.getProp_val3())){
            return updateClientInventoryConstants.FULL;
        }else {
            return updateClientInventoryConstants.INCREACE;
        }
    }

    /**
     * @description 设置webService请求的参数
     * @param channelId PRE： 取上一次任务运行的开始时间； CUR： 取本次任务运行的开始时间
     * @return GetInventoryParamBean 参数bean
     */
    private GetInventoryParamBean setParamBean(String channelId) {
        //获取上一次任务执行失败的配置项（pv4:上一次任务失败的起始时间（正常结束时，清空为0）;pv5、下一次更新的起始pageIndex（pv4非0时使用））
        ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(channelId, updateClientInventoryConstants.FULLUPDATECONFIG);
        GetInventoryParamBean getInventoryParamBean = new GetInventoryParamBean();
        try {
            getInventoryParamBean.setPostAction(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.POSTACTION).get(0).getProp_val1());
            getInventoryParamBean.setNameSpace(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.NAMESPACE).get(0).getProp_val1());
            getInventoryParamBean.setPostUrl(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.URL).get(0).getProp_val1());
            getInventoryParamBean.setAccountId(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.ACCOUNTID).get(0).getProp_val1());
            getInventoryParamBean.setDeveloperKey(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.DEVELOPERKEY).get(0).getProp_val1());
            getInventoryParamBean.setPassword(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.PASSWORD).get(0).getProp_val1());
            getInventoryParamBean.setLabelName(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.LABELNAME).get(0).getProp_val1());
            getInventoryParamBean.setDateRangeField(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.DATERANGEFIELD).get(0).getProp_val1());
            getInventoryParamBean.setDateTimeEnd(getUpdateTime("CUR"));
            String perFaildTime =  thirdPartyConfigBean.getProp_val4();
            if(!"0".equals(perFaildTime)){
                getInventoryParamBean.setDateTimeStart(DateTimeUtil.strDateTimeTOXMLGregorianCalendar(perFaildTime));
                getInventoryParamBean.setnPageIndex(Integer.parseInt(thirdPartyConfigBean.getProp_val5()));
            }else{
                getInventoryParamBean.setDateTimeStart(getUpdateTime("PRE"));
                getInventoryParamBean.setnPageIndex(1);
            }
            log("变量更新库存时间为：" + getInventoryParamBean.getDateTimeStart() + "-" + getInventoryParamBean.getDateTimeEnd() + "; 全量更新忽略此时间！");
            getInventoryParamBean.setnPageSize(Integer.parseInt(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.PAGESIZE).get(0).getProp_val1()));
        }catch (Exception e){
            String msg = "设置库存取得请求参数错误：" + e;
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        return getInventoryParamBean;
    }

    /**
     * @description 从 com_bt_task_history 里面获取任务运行时间
     * @param dateTimeFlg PRE： 取上一次任务运行的开始时间； CUR： 取本次任务运行的开始时间
     * @return XMLGregorianCalendar 任务运行时间
     */
    private XMLGregorianCalendar getUpdateTime(String dateTimeFlg) {
        try {
            log("获取 " + getTaskName() + "的" + dateTimeFlg + " 开始");
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", getTaskName());
            paramMap.put("dateTimeFlg", dateTimeFlg);
            String dateTimeStr = clientInventoryDao.getTaskRunTime(paramMap);
            if(StringUtils.isEmpty(dateTimeStr)){
                dateTimeStr = "2015-01-01 00:00:00";
            }
            XMLGregorianCalendar xmlGregorianCalendar = DateTimeUtil.strDateTimeTOXMLGregorianCalendar(dateTimeStr);
            //logger.info(dateTimeFlg + "时间为：" + xmlGregorianCalendar);
            log("获取 " + getTaskName() + "的" + dateTimeFlg + " 结束");
            return xmlGregorianCalendar;
        }catch (Exception e){
            throw new RuntimeException("获取" + getTaskName() + "的" + dateTimeFlg + "运行时间失败。");
        }
    }

    /**
     * @description 同步库存
     * @param getInventoryParamBean 获取第三方库存xml请求文件bean
     * @param channelId 渠道
     * @param threads 线程集合
     */
    private void sysInventoryByClient(GetInventoryParamBean getInventoryParamBean, String channelId,  List<Runnable> threads){
        threads.add(() -> {
            List<InventoryResSoapenv> responseBeans = getClientInv(getInventoryParamBean, channelId, getUpdateType(channelId));
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
            if(responseBeans.size() > 0) {
//                logger.info(channel.getFull_name() + "删除第三方库存表中的记录开始");
//                //删除临时库存表数据
//                clientInventoryDao.delClientInventory(channelId);
//                logger.info(channel.getFull_name() + "删除第三方库存表中的记录结束");
                transactionRunner.runWithTran(() -> {
                    assert channel != null;
                    Long store_id = getStore(channelId);
                    try {
                        log(channel.getFull_name() + "库存插入wms_bt_client_inventory开始");
                        int totalRow = 0, insertRow = 0;
                        for (InventoryResSoapenv responseBean : responseBeans) {
                            List<InventoryItemResponse> inventoryItemResponseList = responseBean.getBody().getFilteredInventoryItemListResponse().getGetFilteredInventoryItemListResult().getResultData().getInventoryItemResponse();
                            totalRow = totalRow + inventoryItemResponseList.size();
                            insertRow = insertRow + insertClientInventory(channelId, inventoryItemResponseList, store_id);
                        }
                        logger.info("----------从渠道【" + channel.getFull_name() + "】LabelName【" + getInventoryParamBean.getLabelName() + "】获取到的库存sku记录数为:" + totalRow + "----------");
                        logger.info("----------与【wms_bt_item_detail】中【client_sku】匹配且成功插入【wms_bt_client_inventory】记录数为:" + insertRow + "----------");
                        //全部插入成功后更新标志
                        setLastFullUpdateTime(channelId, getInventoryParamBean);
                        log(channel.getFull_name() + "库存插入wms_bt_client_inventory结束");
                    } catch (Exception e) {
                        String msg = channel.getFull_name() + "库存插入wms_bt_client_inventory失败" + e;
                        log(msg);
                        logIssue(msg);
                        throw new RuntimeException(msg);
                    }
                });
            }else{
                log("没有从渠道：【" + channel.getFull_name() + "】获取到任何库存数据！");
            }
        });
    }

    /**
     * @description 获取库存失败时候记录失败的时间，失败时候的pageIndex
     * @param order_channel_id 渠道
     * @param pageIndex 失败时候pageIndex
     * @param getInventoryParamBean 发送请求webService的参数
     */
    private void setFaildConfig(String order_channel_id, int pageIndex, GetInventoryParamBean getInventoryParamBean) {
        try {
            log("库存获取失败，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置开始");
            //取得库存失败的起始时间
            Date faildDateT = getInventoryParamBean.getDateTimeStart().toGregorianCalendar().getTime();
            //设置参数
            Map<String, String> map = new HashMap<>();
            map.put("order_channel_id", order_channel_id);
            map.put("propName", updateClientInventoryConstants.FULLUPDATECONFIG);
            map.put("nextTimeStartPageIndex", String.valueOf(pageIndex));
            map.put("perFaildStartTime", DateTimeUtil.format(faildDateT, DateTimeUtil.DEFAULT_DATETIME_FORMAT));
            map.put("modifier", getTaskName());
            clientInventoryDao.setFaildConfig(map);
            log("库存获取失败，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置结束");
        }catch (Exception e){
            String msg = "库存获取失败，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置失败";
            log(msg);
            throw new RuntimeException(msg + ":" + e);
        }
    }

    /**
     * @description 将请求webService后返回的sku库存详情插入到中间库存表
     * @param order_channel_id 渠道
     * @param inventoryItemResponses 库存详情数据
     * @param store_id 仓库Id
     */
    private int insertClientInventory(String order_channel_id, List<InventoryItemResponse> inventoryItemResponses, Long store_id) {
        try {
            List<ClientInventoryBean> clientInventoryBeans = getInsertData(order_channel_id, inventoryItemResponses);
            if(clientInventoryBeans.size() > 0) {
                String tempTable = preparetClientInventoryData(clientInventoryBeans, order_channel_id, store_id);
                return clientInventoryDao.insertClientInventoryCommonly(tempTable);
            }else{
                return 0;
            }
        }catch (Exception e){
            String msg = "将库存结果插入wms_bt_client_inventory表失败";
            log(msg);
            throw new RuntimeException(msg + ":" + e);
        }
    }

    /**
     * @description 设置最后全量更新的时间，以及控制什么时候该进行全量更新了
     * @param order_channel_id 渠道
     * @param getInventoryParamBean 发送请求webService的参数
     */
    private void setLastFullUpdateTime(String order_channel_id, GetInventoryParamBean getInventoryParamBean) {
        try {
            log("库存获取成功，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置开始");
            ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(order_channel_id, updateClientInventoryConstants.FULLUPDATECONFIG);
            //全量更新标志 0：全量更新；1：增量更新
            String fullUpdateFlg = thirdPartyConfigBean.getProp_val1();
            //设置的时间间隔
            String timeInterval = thirdPartyConfigBean.getProp_val2();
            //最后一次更新时间
            String lastFullUpdtTimeStr = thirdPartyConfigBean.getProp_val3();
            //转换时间
            Date currDate = getInventoryParamBean.getDateTimeEnd().toGregorianCalendar().getTime();
            //设置参数
            Map<String, String> map = new HashMap<>();
            map.put("order_channel_id", order_channel_id);
            map.put("propName", updateClientInventoryConstants.FULLUPDATECONFIG);
            map.put("fullUpdateFlg", "0");
            map.put("currDate", DateTimeUtil.format(currDate,DateTimeUtil.DEFAULT_DATETIME_FORMAT));
            //设置为正常结束的pageIndex
            map.put("perFaildStartTime", "0" );     // pv4:设置为 【0】表示正常更新，没有失败
            map.put("nextTimeStartPageIndex", "1"); //pv5: 设置下一次任务运行的起始pageIndex为 【1】
            map.put("modifier", getTaskName());
            if (!StringUtils.isEmpty(lastFullUpdtTimeStr)) {
                Date lastFullUpdtTimeDate = DateTimeUtil.parse(lastFullUpdtTimeStr);
                Date nextFullUpdtTime = DateTimeUtil.addHours(lastFullUpdtTimeDate, Integer.parseInt(timeInterval));
                if (currDate.after(nextFullUpdtTime)) {
                    clientInventoryDao.setLastFullUpdateTime(map);
                }else if("0".equals(fullUpdateFlg)){
                    map.put("fullUpdateFlg", "1");
                    map.put("currDate", lastFullUpdtTimeStr);
                    clientInventoryDao.setLastFullUpdateTime(map);
                }
            } else {
                map.put("fullUpdateFlg", "1");
                clientInventoryDao.setLastFullUpdateTime(map);
            }
            log("库存获取成功，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置结束");
        }catch (Exception e){
            String msg = "库存获取，成功更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置失败";
            throw new RuntimeException(msg + "：" + e);
        }
    }

    /**
     * @description 获取渠道对应的仓库
     * @param order_channel_id 渠道
     * @return sotreId
     */
    private Long getStore(String order_channel_id) {
        log("获取渠道:" + order_channel_id + "的store_id开始");
        try {
            long storeID = 0;
            List<StoreBean> storeBeans = StoreConfigs.getChannelStoreList(order_channel_id);
            assert storeBeans != null;
            for (StoreBean storeBean : storeBeans) {
                if (storeBean.getStore_location().equals(StoreConfigEnums.Location.CB.getId()) && storeBean.getStore_kind().equals("0")) {
                    storeID = storeBean.getStore_id();
                    break;
                }
            }
            logger.info("渠道：" + order_channel_id + "对应的store_id为" + storeID);
            log("获取渠道:" + order_channel_id + "的store_id结束");
            return storeID;
        }catch (Exception e){
            throw new RuntimeException("获取storeId失败：" + e);
        }
    }

    /**
     * @description 用webService获取的库存数据关联detail表获取产品相关的属性数据
     * @param order_channel_id 渠道
     * @param inventoryItemResponses 从webService返回的库存数据
     * @return 有效的可以插入wms_bt_client_inventory 的数据集合
     */
    private List<ClientInventoryBean> getInsertData(String order_channel_id, List<InventoryItemResponse> inventoryItemResponses) {
        try {
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (InventoryItemResponse inventoryItemResponse : inventoryItemResponses) {
                sb.append("select '").append(order_channel_id).append("' order_channel_id, '").append(inventoryItemResponse.getSku()).append("' client_sku,").append(inventoryItemResponse.getQuantity().getAvailable()).append(" qty");
                if (i != inventoryItemResponses.size()) sb.append(" union all ");
                i++;
            }
            return clientInventoryDao.getInsertClientInvData(sb.toString());
        }catch (Exception e){
            throw new RuntimeException("用webService获取的库存数据关联detail表获取产品相关的属性数据失败：" + e);
        }
    }

    /**
     * 一条库存临时表的插入语句values部分
     * @param clientInventoryBeans 要插入的数据集合
     * @param orderChannelId 渠道
     * @param storeId 仓库ID
     * @return 拼接好的Sql
     */
    private String preparetClientInventoryData(List<ClientInventoryBean> clientInventoryBeans , String orderChannelId, long storeId) {
        StringBuilder sqlValueBuffer = new StringBuilder();
        int i = 1;
        for(ClientInventoryBean clientInventoryBean: clientInventoryBeans) {
            sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

            // order_channel_id
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(orderChannelId);
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            //client_sku
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getClient_sku());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // qty
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getQty());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // store_id
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(storeId);
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // itemCode
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getItemCode());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            //sku
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getSku());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // size
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getSize());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // barcode
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(clientInventoryBean.getBarcode());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // syn_flg
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(WmsConstants.SYN_FLG.SUCCESS);
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
            sqlValueBuffer.append(getTaskName());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // modified
            sqlValueBuffer.append(Constants.NOW_MYSQL);
            sqlValueBuffer.append(Constants.COMMA_CHAR);

            // modifier
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(getTaskName());
            sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
            sqlValueBuffer.append(Constants.RIGHT_BRACKET_CHAR);
            if(i != clientInventoryBeans.size())
            sqlValueBuffer.append(Constants.COMMA_CHAR);
            i++;
        }
        return sqlValueBuffer.toString();
    }

    /**
     * @description 临时获取第三方配置表的配置数据
     * @param orderChannelId 渠道
     * @param propName 属性名称
     * @return 配置对象
     */
    public ThirdPartyConfigBean getFullUpdtConfig(String orderChannelId, String propName) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("order_channel_id", orderChannelId);
        paramMap.put("propName", propName);
        return  clientInventoryDao.getFullUpdtConfig(paramMap);
    }

    private void log (String logMsg){
        logger.info("===============" + logMsg + "===============");
    }

}
