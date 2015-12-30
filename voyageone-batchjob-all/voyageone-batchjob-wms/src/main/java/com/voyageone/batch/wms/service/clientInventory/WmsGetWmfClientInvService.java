package com.voyageone.batch.wms.service.clientInventory;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.batch.wms.modelbean.ItemDetailsBean;
import com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean;
import com.voyageone.common.components.channelAdvisor.soap.InventoryItemResSoapenv;
import com.voyageone.common.components.channelAdvisor.webservices.ArrayOfDistributionCenterInfoResponse;
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
 * Created by fred on 2015/12/29.
 */
@Service
@Transactional
public class WmsGetWmfClientInvService extends WmsGetClientInvBaseService  {

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
    public void sysSearsInventoryByClient(String channelId,  List<Runnable> threads){
        threads.add(() -> {
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
            GetInventoryParamBean getInventoryParamBean = setParamBean(channelId);
            List<String> processSkuList = new ArrayList<String>();

            log(channel.getFull_name()+"全量库存取得");
            // 取得该渠道下的所有SKU（没有设定ClientSku）
            getItemDetaiInfoNoClient(channelId);
            // 取得该渠道下的所有SKU（有设定ClientSku）
            List<ItemDetailsBean> itemDetailBeans = itemDetailsDao.getItemDetaiInfo(channelId);
            for (ItemDetailsBean itemDetailsBean : itemDetailBeans) {
                processSkuList.add(itemDetailsBean.getClient_sku());
            }
            log(channel.getFull_name()+"需要取得库存的SKU件数："+processSkuList.size());
            List<InventoryItemResSoapenv> responseBeans = getClientInv(getInventoryParamBean,processSkuList, channelId);

            if(responseBeans.size() > 0) {
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

    /**
     * @description 设置webService请求的参数
     * @param channelId PRE： 取上一次任务运行的开始时间； CUR： 取本次任务运行的开始时间
     * @return GetInventoryParamBean 参数bean
     */
    private com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean setParamBean(String channelId) {
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        //获取上一次任务执行失败的配置项（pv4:上一次任务失败的起始时间（正常结束时，清空为0）;pv5、下一次更新的起始pageIndex（pv4非0时使用））
        ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(channelId, WmsConstants.updateClientInventoryConstants.FULLUPDATECONFIG);
        com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean getInventoryParamBean = new com.voyageone.common.components.channelAdvisor.bean.inventory.GetInventoryParamBean();
        try {
            getInventoryParamBean.setPostAction(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.POSTACTION).get(0).getProp_val1());
            getInventoryParamBean.setPostItemAction(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.POSTITEMACTION).get(0).getProp_val1());
            getInventoryParamBean.setNameSpace(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.NAMESPACE).get(0).getProp_val1());
            getInventoryParamBean.setPostUrl(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.URL).get(0).getProp_val1());
            getInventoryParamBean.setAccountId(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.ACCOUNTID).get(0).getProp_val1());
            getInventoryParamBean.setDeveloperKey(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.DEVELOPERKEY).get(0).getProp_val1());
            getInventoryParamBean.setPassword(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.PASSWORD).get(0).getProp_val1());
            getInventoryParamBean.setLabelName(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.LABELNAME).get(0).getProp_val1());
            getInventoryParamBean.setDateRangeField(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.DATERANGEFIELD).get(0).getProp_val1());
            //CUR： 取本次任务运行的开始时间
            getInventoryParamBean.setDateTimeEnd(getUpdateTime(channel.getFull_name(), "CUR"));
            getInventoryParamBean.setCenterCode(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.CENTERCODE).get(0).getProp_val1());
            String perFaildTime =  thirdPartyConfigBean.getProp_val4();
            if(!"0".equals(perFaildTime)){
                getInventoryParamBean.setDateTimeStart(DateTimeUtil.strDateTimeTOXMLGregorianCalendar(perFaildTime));
                getInventoryParamBean.setnPageIndex(Integer.parseInt(thirdPartyConfigBean.getProp_val5()));
            }else{
                //PRE： 取上一次任务运行的开始时间
                getInventoryParamBean.setDateTimeStart(getUpdateTime(channel.getFull_name(),"PRE"));
                getInventoryParamBean.setnPageIndex(1);
            }
            getInventoryParamBean.setStoreArea(thirdPartyConfigBean.getProp_val6());
            log(channel.getFull_name() + "变量更新库存时间为：" + getInventoryParamBean.getDateTimeStart() + "-" + getInventoryParamBean.getDateTimeEnd() + "; 全量更新忽略此时间！");
            getInventoryParamBean.setnPageSize(Integer.parseInt(ThirdPartyConfigs.getThirdPartyConfigList(channelId, WmsConstants.updateClientInventoryConstants.PAGESIZE).get(0).getProp_val1()));
        }catch (Exception e){
            String msg = channel.getFull_name()+"设置库存取得请求参数错误：" + e;
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
            //setFaildConfig(channel.getOrder_channel_id(), 0, getInventoryParamBean);
            String msg = channel.getFull_name() + "取得库存失败" + e;
            log(msg);
            logIssue(msg);
            throw new RuntimeException(msg);
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
