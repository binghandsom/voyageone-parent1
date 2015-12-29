package com.voyageone.batch.wms.service.clientInventory;

import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.modelbean.ClientInventoryBean;
import com.voyageone.common.components.sears.bean.GetInventoryParamBean;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
            String updateType = getUpdateType(channelId);
            List<ClientInventoryBean> inventoryBeans = new ArrayList<>();

            if (WmsConstants.updateClientInventoryConstants.FULL.equals(updateType)) {
                log(channel.getFull_name()+"全量库存取得");
                inventoryBeans = getClientInvFull(getInventoryParamBean, channelId);
            } else {
                log(channel.getFull_name()+"从" + getInventoryParamBean.getDateTimeSince() + "开始库存变化取得");
                inventoryBeans = getClientInvIncreace(getInventoryParamBean, channelId);
            }

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
}
