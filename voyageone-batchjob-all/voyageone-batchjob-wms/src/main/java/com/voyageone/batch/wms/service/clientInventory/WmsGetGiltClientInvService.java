package com.voyageone.batch.wms.service.clientInventory;

import com.google.gson.Gson;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.WmsConstants.updateClientInventoryConstants;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.batch.wms.modelbean.ItemDetailsBean;
import com.voyageone.common.components.gilt.bean.*;
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

import java.util.*;

/**
 *
 * Created by jack on 2016/02/05.
 */
@Service
@Transactional
public class WmsGetGiltClientInvService extends WmsGetClientInvBaseService {

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
    public void sysGiltInventoryByClient(String channelId,  List<Runnable> threads){
        threads.add(() -> {
            OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
            List<ClientInventoryBean> inventoryBeans = new ArrayList<>();
            List<ClientInventoryBean> saleInventoryBeans = new ArrayList<>();
            List<String> saleSkuList = new ArrayList<>();

            String updateType = getUpdateType(channelId);

            GetInventoryParamBean getInventoryParamBean = setParamBean(channelId);

            //更新全量更新配置
            if (!updateClientInventoryConstants.FULL.equals(updateType)) {
                setLastFullUpdateTime(channelId, getInventoryParamBean);
                updateType = getUpdateType(channelId);
            }

            // 判断是否需要取得Sales相关的库存
            if (getInventoryParamBean.getSalesAllow().equals(WmsConstants.SALE.ALLOW_YES)) {
                if (StringUtils.isNullOrBlank2(getInventoryParamBean.getSalesInterval())) {
                    log(channel.getFull_name()+" Sales取得间隔时间未设定，无法取得库存，退出处理");
                    logIssue(channel.getFull_name()+" Sales取得间隔时间未设定，无法取得库存，退出处理");
                }else {
                    int timeInterval = Integer.parseInt(getInventoryParamBean.getSalesInterval());
                    String UpdateTime = "";
                    if (!StringUtils.isNullOrBlank2(getInventoryParamBean.getSalesUpdateSince())) {
                        UpdateTime = DateTimeUtil.format(DateTimeUtil.addMinutes(DateTimeUtil.parse(getInventoryParamBean.getSalesUpdateSince()), timeInterval), DateTimeUtil.DEFAULT_DATETIME_FORMAT);
                    }

                    log(channel.getFull_name()+" Sales取得前次处理时间："+getInventoryParamBean.getSalesUpdateSince());
                    // 初回更新或现在时间大于上传更新时间时，需要去取得相关Sales的库存
                    if (StringUtils.isNullOrBlank2(getInventoryParamBean.getSalesUpdateSince()) || DateTimeUtil.getNow().compareTo(UpdateTime) > 0) {
                        saleInventoryBeans = getClientInvSales(getInventoryParamBean, channelId, saleSkuList);
                    }else {
                        log(channel.getFull_name()+" Sales取得间隔时间未到，还无需取得库存，退出处理");
                        return;
                    }
                }
            }

            // Sales取得后，判断全量是否有必要取得
            if (getInventoryParamBean.getFullAllow().equals(WmsConstants.FULL.YES)) {

                if (updateClientInventoryConstants.FULL.equals(updateType)) {
                    log(channel.getFull_name() + "全量库存取得");
                    inventoryBeans = getClientInvFull(getInventoryParamBean, channelId, saleSkuList);
                }
            }

            inventoryBeans.addAll(saleInventoryBeans);

            if(inventoryBeans.size() > 0) {

                final List<ClientInventoryBean> finalInventoryBeans = inventoryBeans;

                transactionRunner.runWithTran(() -> {
                    assert channel != null;
                    Long store_id = getStore(channelId, getInventoryParamBean.getStoreArea());
                    if (store_id == 0) {
                        log(channel.getFull_name() + "所属区域仓库取得失败：" + getInventoryParamBean.getStoreArea());
                        logIssue(channel.getFull_name() + "所属区域仓库取得失败：" + getInventoryParamBean.getStoreArea());
                    } else {
                        try {
                            log(channel.getFull_name() + "库存插入wms_bt_client_inventory开始：" + finalInventoryBeans.size());
                            int intCount = 0, totalRow = 0, insertRow = 0;
                            List<ClientInventoryBean> inventoryItems = new ArrayList<>();
                            for (ClientInventoryBean clientInventoryBean : finalInventoryBeans) {

                                inventoryItems.add(clientInventoryBean);
                                intCount = intCount + 1;
                                totalRow = totalRow + 1;

                                if (intCount == getInventoryParamBean.getUpdateCount() || totalRow == finalInventoryBeans.size()) {
                                    logger.info("----------" + channel.getFull_name() + "当前更新到第【" + totalRow + "】件----------");
                                    insertRow = insertRow + insertClientInventory(channelId, inventoryItems, store_id);
                                    intCount = 0;
                                    inventoryItems = new ArrayList<>();
                                }

                            }
                            logger.info("----------从渠道【" + channel.getFull_name() + "】获取到的库存sku记录数为:" + totalRow + "----------");
                            logger.info("----------" + channel.getFull_name() + "与【wms_bt_item_detail】中【client_sku】匹配且成功插入【wms_bt_client_inventory】记录数为:" + insertRow + "----------");
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
                //全部插入成功后更新标志
                setLastFullUpdateTime(channelId, getInventoryParamBean);
                log(channel.getFull_name() + "库存插入wms_bt_client_inventory结束");
            }
        });
    }

    private List<ClientInventoryBean> getClientInvSales(GetInventoryParamBean getInventoryParamBean, String channelId, List<String> saleSkuList){
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        List<ClientInventoryBean> clientInventoryList = new ArrayList<>();

        try {
            log(channel.getFull_name() + " Sales库存取得开始");

            // 取得所有Sales(Returns data for all active and upcoming sales for the next seven days.)
            List<GiltSale> giltSales = giltSalesService.getAllSales();

            logger.info("----------"+channel.getFull_name() + " all active and upcoming sales size:" + giltSales.size());

            for (GiltSale giltSale : giltSales) {
                int offset = 0;
                int pageIndex = 1;
                int losePageCount = 1;
                GiltPageGetSaleAttrRequest giltPageGetSaleAttrRequest = new GiltPageGetSaleAttrRequest();
                giltPageGetSaleAttrRequest.setId(giltSale.getId().toString());
                giltPageGetSaleAttrRequest.setLimit(StringUtils.isNullOrBlank2(getInventoryParamBean.getSalesLimit())? 1 : Integer.parseInt(getInventoryParamBean.getSalesLimit()));
                giltPageGetSaleAttrRequest.setOffset(offset);

                while (true) {

                    try {
                        logger.info("----------" + channel.getFull_name() + "当前处理到Sales【" + giltSale.getId() + "】的第【" + pageIndex + "】页----------");

                        // 根据类型决定取得线下库存还是线上库存
                        if (getInventoryParamBean.getSalesUpdateType().equals(WmsConstants.SALE.UPDATE_INVENTORY)) {
                            List<GiltInventory> giltInventories =  giltSalesService.getInventorysBySaleId(giltPageGetSaleAttrRequest);

                            log(channel.getFull_name() + " Sales线下库存取得结束："+  new Gson().toJson(giltInventories));

                            if (giltInventories == null || giltInventories.size() == 0) {
                                break;
                            }

                            for (GiltInventory giltInventory : giltInventories) {
                                ClientInventoryBean clientInventoryBean = new ClientInventoryBean();

                                clientInventoryBean.setClient_sku(String.valueOf(giltInventory.getSku_id()));
                                clientInventoryBean.setQty(String.valueOf(giltInventory.getQuantity()));

                                if (!saleSkuList.contains(clientInventoryBean.getClient_sku())) {
                                    saleSkuList.add(clientInventoryBean.getClient_sku());
                                    clientInventoryList.add(clientInventoryBean);
                                }
                            }

                            if (giltInventories.size() < giltPageGetSaleAttrRequest.getLimit()) {
                                break;
                            }
                        }
                        else if (getInventoryParamBean.getSalesUpdateType().equals(WmsConstants.SALE.UPDATE_REALTIME_INVENTORY)) {
                            List<GiltRealTimeInventory> giltRealTimeInventories =  giltSalesService.getRealTimeInventoriesBySaleId(giltPageGetSaleAttrRequest);

                            log(channel.getFull_name() + " Sales实时库存取得结束："+  new Gson().toJson(giltRealTimeInventories));

                            if (giltRealTimeInventories == null || giltRealTimeInventories.size() == 0) {
                                break;
                            }

                            for (GiltRealTimeInventory giltRealTimeInventory : giltRealTimeInventories) {
                                ClientInventoryBean clientInventoryBean = new ClientInventoryBean();

                                clientInventoryBean.setClient_sku(String.valueOf(giltRealTimeInventory.getSku_id()));
                                clientInventoryBean.setQty(String.valueOf(giltRealTimeInventory.getQuantity()));

                                if (!saleSkuList.contains(clientInventoryBean.getClient_sku())) {
                                    saleSkuList.add(clientInventoryBean.getClient_sku());
                                    clientInventoryList.add(clientInventoryBean);
                                }
                            }

                            if (giltRealTimeInventories.size() < giltPageGetSaleAttrRequest.getLimit()) {
                                break;
                            }
                        }

                        pageIndex ++;
                        offset  ++;
                        giltPageGetSaleAttrRequest.setOffset(offset * giltPageGetSaleAttrRequest.getLimit());
                    } catch (Exception e) {
                        logger.info("----------" + channel.getFull_name() + "当前处理到Sales【" + giltSale.getId() + "】的第【" + pageIndex + "】页数据请求失败----------");
                        if(losePageCount == ALLOWLOSEPAGECOUNT){
                            String msg = channel.getFull_name()+"已经连续【" + ALLOWLOSEPAGECOUNT + "】次请求webService库存数据失败！" + e;
                            logger.info("----------" + msg + "----------");
                            throw new RuntimeException(msg);
                        }
                        losePageCount ++;
                        continue;
                    }
                }

            }

            //设置参数
            Map<String, String> map = new HashMap<>();
            map.put("order_channel_id", channel.getOrder_channel_id());
            map.put("propName", updateClientInventoryConstants.INVENTORYSALECONFIG);
            map.put("modifier", getTaskName());
            map.put("currDate",DateTimeUtil.getNow());
            clientInventoryDao.setLastSalesUpdateTime(map);

            log(channel.getFull_name() + " Sales库存取得结束："+ clientInventoryList.size());
            return clientInventoryList;
        } catch (Exception e) {
            String msg = channel.getFull_name() + " Sales取得库存失败" + e;
            log(msg);
            logIssue(msg);
            throw new RuntimeException(msg);
        }

    }

    private List<ClientInventoryBean> getClientInvFull(GetInventoryParamBean getInventoryParamBean, String channelId,List<String> saleSkuList){
        OrderChannelBean channel = ChannelConfigs.getChannel(channelId);
        List<ClientInventoryBean> clientInventoryList = new ArrayList<>();

        try {
            log(channel.getFull_name() + "全量库存取得开始");

            // 取得该渠道下的所有SKU（没有设定ClientSku）
            getItemDetaiInfoNoClient(channelId);

            // 取得该渠道下的所有SKU（有设定ClientSku）
            List<ItemDetailsBean> itemDetailBeans = itemDetailsDao.getItemDetaiInfo(channelId);

            logger.info("----------"+channel.getFull_name() + " ItemDetail total size:" + itemDetailBeans.size());

            logger.info("----------"+channel.getFull_name() + " Sales ItemDetail total size:" + saleSkuList.size());

            int totalCount = 0;
            int intCount = 0;
            for (ItemDetailsBean itemDetailBean : itemDetailBeans) {

                if (!StringUtils.isNullOrBlank2(itemDetailBean.getClient_sku())) {
                    // 如果SKU已经在Sales中取得，则不再取得线上库存
                    if (saleSkuList.contains(itemDetailBean.getClient_sku())) {
                        continue;
                    }
                }

                totalCount = totalCount + 1 ;
                intCount = intCount + 1;

                if (intCount == getInventoryParamBean.getnPageSize() || totalCount == itemDetailBeans.size() -  saleSkuList.size()) {
                    logger.info("----------" + channel.getFull_name() + "当前处理到第【" + totalCount + "】件----------");
                }

                GiltRealTimeInventory giltRealTimeInventory =  giltRealTimeInventoryService.getRealTimeInventoryBySkuId(itemDetailBean.getClient_sku());

                ClientInventoryBean clientInventoryBean = new ClientInventoryBean();

                clientInventoryBean.setClient_sku(String.valueOf(giltRealTimeInventory.getSku_id()));
                clientInventoryBean.setQty(String.valueOf(giltRealTimeInventory.getQuantity()));

                clientInventoryList.add(clientInventoryBean);

            }

            log(channel.getFull_name() + "全量库存取得结束");
            return clientInventoryList;
        } catch (Exception e) {
            //库存取得失败后记录pageIndex（pageIndex - ALLOWLOSEPAGECOUNT + 1）,设置认识失败的开始时间
            setFaildConfig(channel.getOrder_channel_id(), 0, getInventoryParamBean);
            String msg = channel.getFull_name() + "全量取得库存失败" + e;
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
        ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(channelId, updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG);
        GetInventoryParamBean getInventoryParamBean = new GetInventoryParamBean();
        try {
            String perFaildTime =  thirdPartyConfigBean.getProp_val4();
            if(!"0".equals(perFaildTime)){
                getInventoryParamBean.setDateTimeSince(perFaildTime);
                getInventoryParamBean.setnPageIndex(Integer.parseInt(thirdPartyConfigBean.getProp_val5()));
            }else{
                getInventoryParamBean.setDateTimeSince(getUpdateTime(channel.getFull_name(),"PRE"));
                getInventoryParamBean.setnPageIndex(1);
            }
            getInventoryParamBean.setStoreArea(thirdPartyConfigBean.getProp_val6());
            log(channel.getFull_name()+"变量更新库存时间为：" + getInventoryParamBean.getDateTimeSince() + "; 全量更新忽略此时间！");
            getInventoryParamBean.setnPageSize(Integer.parseInt(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.INVENTORYPAGESIZE).get(0).getProp_val1()));
            getInventoryParamBean.setUpdateCount(Integer.parseInt(ThirdPartyConfigs.getThirdPartyConfigList(channelId, updateClientInventoryConstants.INVENTORYPAGESIZE).get(0).getProp_val2()));

            // Sale级别的参数设定
            thirdPartyConfigBean = getFullUpdtConfig(channelId, updateClientInventoryConstants.INVENTORYSALECONFIG);

            getInventoryParamBean.setSalesAllow(thirdPartyConfigBean.getProp_val1());
            getInventoryParamBean.setSalesInterval(thirdPartyConfigBean.getProp_val2());
            getInventoryParamBean.setSalesUpdateSince(thirdPartyConfigBean.getProp_val3());
            getInventoryParamBean.setSalesUpdateType(thirdPartyConfigBean.getProp_val4());
            getInventoryParamBean.setFullAllow(thirdPartyConfigBean.getProp_val5());
            getInventoryParamBean.setSalesLimit(thirdPartyConfigBean.getProp_val6());

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
        ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(order_channel_id, updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG);
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
    private String getUpdateTime(String channelName, String dateTimeFlg) {
        try {
            log(channelName + "获取 " + getTaskName() + "的" + dateTimeFlg + " 开始");
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("taskName", getTaskName());
            paramMap.put("dateTimeFlg", dateTimeFlg);
            String dateTimeStr = clientInventoryDao.getTaskRunTime(paramMap);
            if(StringUtils.isEmpty(dateTimeStr)){
                dateTimeStr = "2015-01-01 00:00:00";
            }
            log(channelName + "获取 " + getTaskName() + "的" + dateTimeFlg + " 结束");
            return dateTimeStr;
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
            log(channel.getFull_name()+"库存获取失败，更新【" + updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG + "】配置开始");
            //设置参数
            Map<String, String> map = new HashMap<>();
            map.put("order_channel_id", order_channel_id);
            map.put("propName", updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG);
            map.put("nextTimeStartPageIndex", String.valueOf(pageIndex));
            map.put("perFaildStartTime", getInventoryParamBean.getDateTimeSince());
            map.put("modifier", getTaskName());
            clientInventoryDao.setFaildConfig(map);
            log(channel.getFull_name()+"库存获取失败，更新【" + updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG + "】配置结束");
        }catch (Exception e){
            String msg = channel.getFull_name()+"库存获取失败，更新【" + updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG + "】配置失败";
            log(msg);
            throw new RuntimeException(msg + ":" + e);
        }
    }

    /**
     * @description 将请求webService后返回的sku库存详情插入到中间库存表
     * @param order_channel_id 渠道
     * @param inventoryItems 库存详情数据
     * @param store_id 仓库Id
     */
    private int insertClientInventory(String order_channel_id, List<ClientInventoryBean> inventoryItems, Long store_id) {
        OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
        try {
            List<ClientInventoryBean> clientInventoryBeans = getInsertData(order_channel_id, inventoryItems);
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
            log(channel.getFull_name()+"库存获取，更新【" + updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG + "】配置开始");
            ThirdPartyConfigBean thirdPartyConfigBean = getFullUpdtConfig(order_channel_id, updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG);
            //全量更新标志 0：全量更新；1：增量更新
            String fullUpdateFlg = thirdPartyConfigBean.getProp_val1();
            //设置的时间间隔
            String timeInterval = thirdPartyConfigBean.getProp_val2();
            //最后一次更新时间
            String lastFullUpdtTimeStr = thirdPartyConfigBean.getProp_val3();
            //设置参数
            Map<String, String> map = new HashMap<>();
            map.put("order_channel_id", order_channel_id);
            map.put("propName", updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG);
            map.put("fullUpdateFlg", "0");
            map.put("currDate",getInventoryParamBean.getDateTimeSince());
            //设置为正常结束的pageIndex
            map.put("perFaildStartTime", "0" );     // pv4:设置为 【0】表示正常更新，没有失败
            map.put("nextTimeStartPageIndex", "1"); //pv5: 设置下一次任务运行的起始pageIndex为 【1】
            map.put("modifier", getTaskName());
            if (!StringUtils.isEmpty(lastFullUpdtTimeStr)) {
                Date lastFullUpdtTimeDate = DateTimeUtil.parse(lastFullUpdtTimeStr);
                String nextFullUpdtTime = DateTimeUtil.format(DateTimeUtil.addHours(lastFullUpdtTimeDate, Integer.parseInt(timeInterval)), DateTimeUtil.DEFAULT_DATETIME_FORMAT);
                if (getInventoryParamBean.getDateTimeSince().compareTo(nextFullUpdtTime) > 0) {
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
            log(channel.getFull_name()+"库存获取，更新【" + updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG + "】配置结束");
        }catch (Exception e){
            String msg = channel.getFull_name()+"库存获取，成功更新【" + updateClientInventoryConstants.INVENTORYFULLUPDATECONFIG + "】配置失败";
            throw new RuntimeException(msg + "：" + e);
        }
    }

    /**
     * @description 用webService获取的库存数据关联detail表获取产品相关的属性数据
     * @param order_channel_id 渠道
     * @param inventoryItems 从webService返回的库存数据
     * @return 有效的可以插入wms_bt_client_inventory 的数据集合
     */
    private List<ClientInventoryBean> getInsertData(String order_channel_id, List<ClientInventoryBean> inventoryItems) {
        OrderChannelBean channel = ChannelConfigs.getChannel(order_channel_id);
        try {
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (ClientInventoryBean clientInventoryBean : inventoryItems) {
                sb.append("select '").append(order_channel_id).append("' order_channel_id, '").append(clientInventoryBean.getClient_sku()).append("' client_sku,").append(clientInventoryBean.getQty()).append(" qty");
                if (i != inventoryItems.size()) sb.append(" union all ");
                i++;
            }
            return clientInventoryDao.getInsertClientInvData(sb.toString());
        }catch (Exception e){
            throw new RuntimeException(channel.getFull_name()+"用webService获取的库存数据关联detail表获取产品相关的属性数据失败：" + e);
        }
    }


}
