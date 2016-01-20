package com.voyageone.batch.wms.service.clientInventory;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.WmsConstants.updateClientInventoryConstants;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.batch.wms.modelbean.ItemDetailsBean;
import com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean;
import com.voyageone.common.components.channelAdvisor.soap.InventoryItemResSoapenv;
import com.voyageone.common.components.channelAdvisor.soap.InventoryItemSoapenv;
import com.voyageone.common.components.channelAdvisor.soap.InventoryResSoapenv;
import com.voyageone.common.components.channelAdvisor.webservices.ArrayOfDistributionCenterInfoResponse;
import com.voyageone.common.components.channelAdvisor.webservices.ArrayOfInventoryItemResponse;
import com.voyageone.common.components.channelAdvisor.webservices.DistributionCenterInfoResponse;
import com.voyageone.common.components.channelAdvisor.webservices.InventoryItemResponse;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ThirdPartyConfigBean;
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
public class WmsGetJewelryClientInvService extends WmsGetClientInvBaseService {

    //允许webSericce请求超时的连续最大次数
    private static int ALLOWLOSEPAGECOUNT = 10;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) {
        // 确保其他位置错误调用该 Service 后，不会处理任何操作
        // 可以更改任务启动内容为：只处理 Cart 为 JD 的内容。但暂时搁置
    }

    /**
     * @description 同步库存
     * @param channelId 渠道
     * @param threads 线程集合
     */
    public void sysCAInventoryByClient(String channelId,  List<Runnable> threads){
        threads.add(() -> {
            GetInventoryParamBean getInventoryParamBean = setParamBean(channelId);
            String updateType = getUpdateType(channelId);
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);

            List<String> processSkuList = new ArrayList<String>();

            if (updateClientInventoryConstants.FULL.equals(updateType)) {
                log(channel.getFull_name()+"全量库存取得");

                // 取得该渠道下的所有SKU（没有设定ClientSku）
                getItemDetaiInfoNoClient(channelId);

                // 取得该渠道下的所有SKU（有设定ClientSku）
                List<ItemDetailsBean> itemDetailBeans = itemDetailsDao.getItemDetaiInfo(channelId);

                for (ItemDetailsBean itemDetailsBean : itemDetailBeans) {
                    processSkuList.add(itemDetailsBean.getClient_sku());
                }

            } else {
                log(channel.getFull_name()+"时间段：【" + getInventoryParamBean.getDateTimeStart() + " - " + getInventoryParamBean.getDateTimeEnd() + "】库存变化取得");
                List<InventoryResSoapenv> inventoryResSoapenvs = getClientInvIncreace(getInventoryParamBean, channelId, updateType);

                //有库存变化的SKU取得
                for (InventoryResSoapenv responseBean : inventoryResSoapenvs) {
                    List<InventoryItemResponse> inventoryItemResponseList = responseBean.getBody().getFilteredInventoryItemListResponse().getGetFilteredInventoryItemListResult().getResultData().getInventoryItemResponse();

                    for (InventoryItemResponse inventoryItemResponse : inventoryItemResponseList) {
                        processSkuList.add(inventoryItemResponse.getSku());
                    }
                }
            }

            log(channel.getFull_name()+"需要取得库存的SKU件数："+processSkuList.size());


            List<InventoryItemResSoapenv> responseBeans = getClientInv(getInventoryParamBean,processSkuList, channelId);

            if(responseBeans.size() > 0) {
//                logger.info(channel.getFull_name() + "删除第三方库存表中的记录开始");
//                //删除临时库存表数据
//                clientInventoryDao.delClientInventory(channelId);
//                logger.info(channel.getFull_name() + "删除第三方库存表中的记录结束");
                transactionRunner.runWithTran(() -> {
                    assert channel != null;
                    Long store_id = getStore(channelId, getInventoryParamBean.getStoreArea());
                    if (store_id == 0) {
                        log(channel.getFull_name() + "所属区域仓库取得失败：" + getInventoryParamBean.getStoreArea());
                        logIssue(channel.getFull_name() + "所属区域仓库取得失败：" + getInventoryParamBean.getStoreArea());
                    } else {
                        try {
                            log(channel.getFull_name() + "库存插入wms_bt_client_inventory开始");
                            int totalRow = 0, insertRow = 0;
                            for (InventoryItemResSoapenv responseBean : responseBeans) {
                                log(responseBean.getBody().getInventoryItemListResponse().getGetInventoryItemListResult().getStatus().value());
                                if (responseBean.getBody().getInventoryItemListResponse().getGetInventoryItemListResult().getStatus().value().equals("Failure")) {
                                    log(channel.getFull_name() + "取得库存失败：" + responseBean.getBody().getInventoryItemListResponse().getGetInventoryItemListResult().getMessage());
                                    logIssue(channel.getFull_name() + "取得库存失败：" + responseBean.getBody().getInventoryItemListResponse().getGetInventoryItemListResult().getMessage());
                                    continue;
                                }
                                List<InventoryItemResponse> inventoryItemResponseList = responseBean.getBody().getInventoryItemListResponse().getGetInventoryItemListResult().getResultData().getInventoryItemResponse();
                                totalRow = totalRow + inventoryItemResponseList.size();
                                insertRow = insertRow + insertClientInventory(channelId, inventoryItemResponseList, store_id, getInventoryParamBean.getCenterCode());
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
                    }
                });
            }else{
                log("没有从渠道：【" + channel.getFull_name() + "】获取到任何库存数据！");
            }
        });
    }

    private List<InventoryResSoapenv> getClientInvIncreace(GetInventoryParamBean getInventoryParamBean, String channelId, String updateType){
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        List<InventoryResSoapenv> inventoryResSoapenvList = new ArrayList<>();
        assert channel != null;
        int pageIndex = getInventoryParamBean.getnPageIndex();
        int losePageCount = 1;
        try {
            log(channel.getFull_name() + "库存变化SKU取得开始");
            while (true){
                logger.info("----------"+channel.getFull_name()+"当前执行到第【" + pageIndex + "】页----------");
                InventoryResSoapenv responseBean ;
                try {
                    responseBean = inventoryService.getCaInventory(getInventoryParamBean, updateType);
                }catch (Exception e){
                    logger.info("----------"+channel.getFull_name()+"第【" + pageIndex + "】页数据请求失败！----------");
                    if(losePageCount == ALLOWLOSEPAGECOUNT){
                        String msg = channel.getFull_name()+"已经连续【" + ALLOWLOSEPAGECOUNT + "】次请求webService库存数据失败！" + e;
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
                        log(channel.getFull_name()+"全量库存取得时无库存变化！");
                    } else {
                        log(channel.getFull_name()+"时间段：【" + getInventoryParamBean.getDateTimeStart() + " - " + getInventoryParamBean.getDateTimeEnd() + "】无库存变化！");
                    }
                }
                if (inventoryItemResponses.size() < Integer.parseInt(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.PAGESIZE).get(0).getProp_val1())){
                    break;
                }else{
                    pageIndex++;
                    getInventoryParamBean.setnPageIndex(pageIndex);
                }
            }
            log(channel.getFull_name() + "库存变化SKU取得结束");
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

    private List<InventoryItemResSoapenv> getClientInv(GetInventoryParamBean getInventoryParamBean, List<String> processSkuList, String channelId){
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        List<InventoryItemResSoapenv> inventoryResSoapenvList = new ArrayList<>();

        int losePageCount = 1;
        try {
            log(channel.getFull_name() + "库存取得开始");

            int intCount = 0;
            int totalCount = 0;
            int i =0;
            List<String> skuList = new ArrayList<>();

            getInventoryParamBean.setPostAction(getInventoryParamBean.getPostItemAction());

            for (String sku : processSkuList) {

                skuList.add(sku);
                intCount = intCount +1;
                totalCount = totalCount +1;

                if (intCount == 80 || totalCount == processSkuList.size()) {

                    getInventoryParamBean.setSkuList(skuList);

                    while (true) {
                        logger.info("----------" + channel.getFull_name() + "当前处理到第【" + totalCount + "】件----------");

                        InventoryItemResSoapenv responseBean ;
                        try {
                            responseBean = inventoryService.GetInventoryItemList(getInventoryParamBean);
                        } catch (Exception e) {
                            if(losePageCount == ALLOWLOSEPAGECOUNT){
                                logger.info("----------" + channel.getFull_name() + "第【" + totalCount + "】件数据请求失败！----------");
                                String msg = channel.getFull_name()+"已经连续【" + ALLOWLOSEPAGECOUNT + "】次请求webService库存数据失败！" + e;
                                logger.info("----------" + msg + "----------");
                                throw new RuntimeException(msg);
                            }
                            losePageCount ++;
                            continue;
                        }

                        losePageCount = 1;
                        if(null != responseBean) {
                            inventoryResSoapenvList.add(responseBean);
                        }

                        break;
                    }

                    intCount = 0;
                    skuList = new ArrayList<>();
                }
            }

            log(channel.getFull_name() + "库存取得结束");
            return inventoryResSoapenvList;
        } catch (Exception e) {
            //库存取得失败后记录pageIndex（pageIndex - ALLOWLOSEPAGECOUNT + 1）,设置认识失败的开始时间
            setFaildConfig(channel.getOrder_channel_id(), 0, getInventoryParamBean);
            String msg = channel.getFull_name() + "取得库存失败" + e;
            log(msg);
            logIssue(msg);
            throw new RuntimeException(msg);
        }
    }


    /**
     * @description 设置webService请求的参数
     * @param channelId PRE： 取上一次任务运行的开始时间； CUR： 取本次任务运行的开始时间
     * @return GetInventoryParamBean 参数bean
     */
    private GetInventoryParamBean setParamBean(String channelId) {
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        //获取上一次任务执行失败的配置项（pv4:上一次任务失败的起始时间（正常结束时，清空为0）;pv5、下一次更新的起始pageIndex（pv4非0时使用））
        ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(channelId, updateClientInventoryConstants.FULLUPDATECONFIG);
        GetInventoryParamBean getInventoryParamBean = new GetInventoryParamBean();
        try {
            getInventoryParamBean.setPostAction(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.POSTACTION).get(0).getProp_val1());
            getInventoryParamBean.setPostItemAction(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.POSTITEMACTION).get(0).getProp_val1());
            getInventoryParamBean.setNameSpace(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.NAMESPACE).get(0).getProp_val1());
            getInventoryParamBean.setPostUrl(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.URL).get(0).getProp_val1());
            getInventoryParamBean.setAccountId(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.ACCOUNTID).get(0).getProp_val1());
            getInventoryParamBean.setDeveloperKey(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.DEVELOPERKEY).get(0).getProp_val1());
            getInventoryParamBean.setPassword(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.PASSWORD).get(0).getProp_val1());
            getInventoryParamBean.setLabelName(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.LABELNAME).get(0).getProp_val1());
            getInventoryParamBean.setDateRangeField(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.DATERANGEFIELD).get(0).getProp_val1());
            getInventoryParamBean.setDateTimeEnd(getUpdateTime(channel.getFull_name(), "CUR"));
            getInventoryParamBean.setCenterCode(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.CENTERCODE).get(0).getProp_val1());
            String perFaildTime =  thirdPartyConfigBean.getProp_val4();
            if(!"0".equals(perFaildTime)){
                getInventoryParamBean.setDateTimeStart(DateTimeUtil.strDateTimeTOXMLGregorianCalendar(perFaildTime));
                getInventoryParamBean.setnPageIndex(Integer.parseInt(thirdPartyConfigBean.getProp_val5()));
            }else{
                getInventoryParamBean.setDateTimeStart(getUpdateTime(channel.getFull_name(),"PRE"));
                getInventoryParamBean.setnPageIndex(1);
            }
            getInventoryParamBean.setStoreArea(thirdPartyConfigBean.getProp_val6());
            log(channel.getFull_name() + "变量更新库存时间为：" + getInventoryParamBean.getDateTimeStart() + "-" + getInventoryParamBean.getDateTimeEnd() + "; 全量更新忽略此时间！");
            getInventoryParamBean.setnPageSize(Integer.parseInt(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.PAGESIZE).get(0).getProp_val1()));
        }catch (Exception e){
            String msg = channel.getFull_name()+"设置库存取得请求参数错误：" + e;
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        return getInventoryParamBean;
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
     * @description 从 com_bt_task_history 里面获取任务运行时间
     * @param dateTimeFlg PRE： 取上一次任务运行的开始时间； CUR： 取本次任务运行的开始时间
     * @return XMLGregorianCalendar 任务运行时间
     */
    private XMLGregorianCalendar getUpdateTime(String channelName, String dateTimeFlg) {
        try {
            log(channelName + "获取 " + getTaskName() + "的" + dateTimeFlg + " 开始");
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", getTaskName());
            paramMap.put("dateTimeFlg", dateTimeFlg);
            String dateTimeStr = clientInventoryDao.getTaskRunTime(paramMap);
            if(StringUtils.isEmpty(dateTimeStr)){
                dateTimeStr = "2015-01-01 00:00:00";
            }
            XMLGregorianCalendar xmlGregorianCalendar = DateTimeUtil.strDateTimeTOXMLGregorianCalendar(dateTimeStr);
            //logger.info(dateTimeFlg + "时间为：" + xmlGregorianCalendar);
            log(channelName + "获取 " + getTaskName() + "的" + dateTimeFlg + " 结束");
            return xmlGregorianCalendar;
        }catch (Exception e){
            throw new RuntimeException(channelName + "获取" + getTaskName() + "的" + dateTimeFlg + "运行时间失败。");
        }
    }



    /**
     * @description 获取库存失败时候记录失败的时间，失败时候的pageIndex
     * @param order_channel_id 渠道
     * @param pageIndex 失败时候pageIndex
     * @param getInventoryParamBean 发送请求webService的参数
     */
    private void setFaildConfig(String order_channel_id, int pageIndex, GetInventoryParamBean getInventoryParamBean) {
        OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
        try {
            log(channel.getFull_name()+"库存获取失败，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置开始");
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
            log(channel.getFull_name()+"库存获取失败，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置结束");
        }catch (Exception e){
            String msg = channel.getFull_name()+"库存获取失败，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置失败";
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
    private int insertClientInventory(String order_channel_id, List<InventoryItemResponse> inventoryItemResponses, Long store_id, String center_code) {
        OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
        try {
            List<ClientInventoryBean> clientInventoryBeans = getInsertData(order_channel_id, inventoryItemResponses, center_code);
            if(clientInventoryBeans.size() > 0) {
                String tempTable = preparetClientInventoryData(clientInventoryBeans, order_channel_id, store_id);
                return clientInventoryDao.insertClientInventoryCommonly(tempTable);
            }else{
                return 0;
            }
        }catch (Exception e){
            String msg = channel.getFull_name()+"将库存结果插入wms_bt_client_inventory表失败";
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
        OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
        try {
            log(channel.getFull_name()+"库存获取成功，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置开始");
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
            log(channel.getFull_name()+"库存获取成功，更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置结束");
        }catch (Exception e){
            String msg = channel.getFull_name()+"库存获取，成功更新【" + updateClientInventoryConstants.FULLUPDATECONFIG + "】配置失败";
            throw new RuntimeException(msg + "：" + e);
        }
    }

    /**
     * @description 用webService获取的库存数据关联detail表获取产品相关的属性数据
     * @param order_channel_id 渠道
     * @param inventoryItemResponses 从webService返回的库存数据
     * @return 有效的可以插入wms_bt_client_inventory 的数据集合
     */
    private List<ClientInventoryBean> getInsertData(String order_channel_id, List<InventoryItemResponse> inventoryItemResponses, String center_code) {
        OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
        try {
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (InventoryItemResponse inventoryItemResponse : inventoryItemResponses) {
                ArrayOfDistributionCenterInfoResponse arrayOfDistributionCenterInfoResponse = inventoryItemResponse.getDistributionCenterList();
                int qty = 0;
                if (arrayOfDistributionCenterInfoResponse != null) {
                    for (DistributionCenterInfoResponse distributionCenterInfoResponse : arrayOfDistributionCenterInfoResponse.getDistributionCenterInfoResponse()) {
                        // 取得所属中心的库存
                        if (distributionCenterInfoResponse.getDistributionCenterCode().equals(center_code)) {
                            qty = distributionCenterInfoResponse.getAvailableQuantity();
                            break;
                        }
                    }
                }
                sb.append("select '").append(order_channel_id).append("' order_channel_id, '").append(inventoryItemResponse.getSku()).append("' client_sku,").append(qty).append(" qty");
                if (i != inventoryItemResponses.size()) sb.append(" union all ");
                i++;
            }
            return clientInventoryDao.getInsertClientInvData(sb.toString());
        }catch (Exception e){
            throw new RuntimeException(channel.getFull_name()+"用webService获取的库存数据关联detail表获取产品相关的属性数据失败：" + e);
        }
    }


}
