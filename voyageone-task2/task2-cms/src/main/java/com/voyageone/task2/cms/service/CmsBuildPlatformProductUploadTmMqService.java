package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbProductService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.CmsConstants;
import com.voyageone.task2.cms.service.putaway.ConditionPropValueRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 天猫平台产品上新服务
 * Product表中产品不存在就向天猫平台新增商品，否则就更新商品
 *
 * @author desmond on 2016/5/11.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformProductUploadTmJob)
public class CmsBuildPlatformProductUploadTmMqService extends BaseMQCmsService {

    // 用户名（当前类名）
    private final String UserId_ClassName = this.getClass().getSimpleName();
    @Autowired
    private ConditionPropValueRepo conditionPropValueRepo;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private TbProductService tbProductService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        doMain(taskControlList);
    }

    /**
     * 天猫平台上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    public void doMain(List<TaskControlBean> taskControlList) throws Exception {

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        conditionPropValueRepo.init();

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                // TODO 虽然workload表里不想上新的渠道，不会有数据，这里的循环稍微有点效率问题，后面再改
                // 天猫平台商品信息新增或更新(天猫)
                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TM.getId()));
                // 天猫国际商品信息新增或更新(天猫国际)
                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TG.getId()));
                // 淘宝商品信息新增或更新(淘宝)
                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TB.getId()));
                // 天猫MiniMall商品信息新增或更新(天猫MiniMall)
                doProductUpload(channelId, Integer.parseInt(CartEnums.Cart.TMM.getId()));
            }
        }

        // 正常结束
        $info("正常结束");
    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     */
    private void doProductUpload(String channelId, int cartId) throws Exception {

        // 默认线程池最大线程数
        int threadPoolCnt = 5;

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channelId, cartId);
        if (sxWorkloadModels == null || sxWorkloadModels.size() == 0) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel:sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); //并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shopProp ShopBean 店铺信息
     */
    private void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shopProp) {
        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shopProp.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shopProp.getCart_id());
        // 产品id
        String productId = "";

        // 天猫产品上新处理
        // 先看一下productGroup表和调用天猫API去平台上取看是否有platformPid,两个地方都没有才需要上传产品，
        // 只有有一个地方有就认为产品已存在，不用新增和更新产品，直接做后面的商品上新处理
        try {
            // 上新用的商品数据信息取得
            SxData sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
            if (sxData == null) {
                String errMsg = String.format("取得上新用的商品数据信息失败！[ChannelId:%s] [GroupId:%s]", channelId, groupId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            // 单个product内部的sku列表分别进行排序
            for (CmsBtProductModel cmsBtProductModel : sxData.getProductList()) {
                sxProductService.sortSkuInfo(cmsBtProductModel.getSkus());
            }
            // 主产品等列表取得
            CmsBtProductModel mainProduct = sxData.getMainProduct();
            List<CmsBtProductModel> productList = sxData.getProductList();
            List<CmsBtProductModel_Sku> skuList = sxData.getSkuList();

            // 天猫平台产品上新处理（产品上新成功之后，再做每个商品的上新处理）
            // 先看一下productGroup表和调用天猫API去平台上取看是否有platformPid,两个地方都没有才需要上传产品，
            // 只有有一个地方有就认为产品已存在，不用新增和更新产品
            if (StringUtils.isEmpty(sxData.getPlatform().getPlatformPid())) {
                // productGroup表中platformPid为空的时候，调用天猫API查找产品platformPid
                // 获取匹配产品规则
//                tbProductService.getProductMatchSchema();
            }


        } catch (Exception ex) {
            // 回写workload表   (失败2)
            // 正常结束
//            $error(String.format("京东单个商品新增或更新信息失败！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
//                    channelId, cartId, groupId, jdWareId));
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SX_WORKLOAD_PUBLISH_STATUS_ERROR, UserId_ClassName);
            throw ex;
        }

        // 天猫产品上新处理


        // 正常结束
//        $info(String.format("京东单个商品新增或更新信息成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
//                channelId, cartId, groupId, jdWareId));
    }

}
