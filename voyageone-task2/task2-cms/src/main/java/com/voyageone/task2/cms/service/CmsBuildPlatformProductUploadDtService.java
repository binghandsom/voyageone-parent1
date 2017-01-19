package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.dt.service.DtWareService;
import com.voyageone.service.dao.cms.CmsBtDtSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtDtSkuModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.model.ConditionPropValueModel;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 分销平台产品上新服务
 *
 * @author desmond on 2016/12/28.
 * @version 2.10.0
 * @since 2.10.0
 */
@Service
public class CmsBuildPlatformProductUploadDtService extends BaseCronTaskService {

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private DtWareService dtWareService;
    @Autowired
    private CmsBtDtSkuDao cmsBtDtSkuDao;
    @Autowired
    private ProductService productService;
    @Autowired
    private SxProductService sxProductService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadDtJob";
    }

    private Map<String, Map<String, List<ConditionPropValueModel>>> channelConditionConfig;

    /**
     * 分销平台产品上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        if (ListUtils.isNull(taskControlList)) {
            String errMsg = String.format("分销平台产品上新处理JOB没有启动! tm_task_control表中没有相应的配置信息");
            $warn(errMsg);
            return;
        }

        // 线程数(默认为5)
        int threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));
        // 抽出件数(默认为500)
        int rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

//        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
//        channelConditionConfig = new HashMap<>();
//        if (ListUtils.notNull(channelIdList)) {
//            for (final String orderChannelID : channelIdList) {
//                channelConditionConfig.put(orderChannelID, conditionPropValueRepo.getAllByChannelId(orderChannelID));
//            }
//        }

        if (ListUtils.notNull(channelIdList)) {
            for (String channelId : channelIdList) {
                // 平台商品信息新增或更新(分销)
                doProductUpload(channelId, CartEnums.Cart.DT.getValue(), threadCount, rowCount);
            }
        }

        $info("分销上新主线程正常结束");
    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     * @param threadCount String 线程池最大线程数(TaskControl表中可配置，默认为最大5个线程)
     * @param rowCount String 一次上新最大件数(TaskControl表中可配置，默认为最大500个商品)
     */
    public void doProductUpload(String channelId, int cartId, int threadCount, int rowCount) throws Exception {

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            $error("获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }
        $info("获取店铺信息成功![ChannelId:%s] [CartId:%s]", channelId, cartId);

        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> sxWorkloadModels = platformProductUploadService.getSxWorkloadWithChannelIdCartId(
                rowCount, channelId, cartId);
        if (ListUtils.isNull(sxWorkloadModels)) {
            $error("上新任务表中没有该渠道和平台对应的任务列表信息！[ChannelId:%s] [CartId:%s]", channelId, cartId);
            return;
        }

        // 保存渠道级别(channel)的共通配置项目(从cms_mt_channel_config表中取得的)
        Map<String, String> channelConfigValueMap = new ConcurrentHashMap<>();
//        // 取得cms_mt_channel_config表中配置的渠道级别的配置项目值(如：颜色别名等)
//        doChannelConfigInit(channelId, cartId, channelConfigValueMap);

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel:sxWorkloadModels) {
            // 启动多线程
            executor.execute(() -> uploadProduct(cmsBtSxWorkloadModel, shopProp, channelConfigValueMap));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * 平台产品上新处理
     *
     * @param cmsBtSxWorkloadModel CmsBtSxWorkloadModel WorkLoad信息
     * @param shop ShopBean 店铺信息
     * @param channelConfigValueMap channelConfig表的一些配置信息
     */
    public void uploadProduct(CmsBtSxWorkloadModel cmsBtSxWorkloadModel, ShopBean shop, Map<String, String> channelConfigValueMap) {

        // 当前groupid(用于取得产品信息)
        long groupId = cmsBtSxWorkloadModel.getGroupId();
        // 渠道id
        String channelId = shop.getOrder_channel_id();
        // 平台id
        int cartId = Integer.parseInt(shop.getCart_id());
        // 商品id
        long dtWareId = 0;
        // 开始时间
        long prodStartTime = System.currentTimeMillis();

        try {
            // 获取group信息
            CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
            if (grpModel == null) {
                String errMsg = String.format("取得group信息失败! 没找到对应的group数据! [groupId=%s]", groupId);
                $error(errMsg);
                return;
            }

            List<String> codes = grpModel.getProductCodes();
            for(String code : codes) {
                // 分销上新
                String result = dtWareService.onShelfProduct(shop, code);
                $info("产品(%s)调用分销上新接口成功! [result:%s]", code, result);
            }

            // 回写分销SKU信息表(cms_bt_dt_sku)
            saveProductDtSku(channelId, cartId, codes);

            // 上新成功时状态回写操作
            // 回写workload表(1:上新成功)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());

            // 正常结束时
            $info(String.format("分销单个商品上新成功！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s] [耗时:%s]",
                    channelId, cartId, groupId, dtWareId, (System.currentTimeMillis() - prodStartTime)));

        } catch (Exception ex) {
            // 异常结束时
            String errMsg = String.format("分销单个商品上新异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WareId:%s]",
                    channelId, cartId, groupId, dtWareId);
            $error(errMsg);

            ex.printStackTrace();

            // 回写workload表(2:上新失败)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());

            return;
        }
    }

    /**
     * 取得cms_mt_channel_config配置表中配置的值集合
     *
     * @param channelId String 渠道id
     * @param cartId int 平台id
     * @param channelConfigValueMap 返回cms_mt_channel_config配置表中配置的值集合用
     */
    public void doChannelConfigInit(String channelId, int cartId, Map<String, String> channelConfigValueMap) {

//        // 从配置表(cms_mt_channel_config)表中取得颜色别名(ALIAS_29.color_alias)
//        String colorAliasKey = CmsConstants.ChannelConfig.ALIAS + "_" + cartId + CmsConstants.ChannelConfig.COLOR_ALIAS;
//        String colorAliasValue1 = getChannelConfigValue(channelId, CmsConstants.ChannelConfig.ALIAS,
//                cartId + CmsConstants.ChannelConfig.COLOR_ALIAS);
//        channelConfigValueMap.put(colorAliasKey, colorAliasValue1);
    }

    /**
     * 回写分销SKU信息表(cms_bt_dt_sku)
     *
     * @param codes 产品code列表
     */
    protected void saveProductDtSku(String channelId, int cartId, List<String> codes) {

        for (String code : codes) {
            CmsBtProductModel prodObj = productService.getProductByCode(channelId, code);
            if (prodObj == null) continue;

            CmsBtProductModel_Platform_Cart platform = prodObj.getPlatformNotNull(cartId);
            if (MapUtils.isEmpty(platform)) continue;

            List<BaseMongoMap<String, Object>> skus = platform.getSkus();
            for (BaseMongoMap<String, Object> sku : skus) {
                CmsBtDtSkuModel dtSkuModel = new CmsBtDtSkuModel();
                dtSkuModel.setChannelId(channelId);
                dtSkuModel.setCartId(cartId);
                dtSkuModel.setProductCode(code);
                dtSkuModel.setSkuCode(sku.getStringAttribute("skuCode"));
                dtSkuModel.setCreater(getTaskName());
                dtSkuModel.setCreated(DateTimeUtil.getDate());
                dtSkuModel.setModifier(getTaskName());
                dtSkuModel.setModified(DateTimeUtil.getDate());

                // 查询mySql表中的sku列表(一个产品查询一次，如果每个sku更新/新增的时候都去查的话，效率太低了)
                Map<String, String> query = new HashMap<>();
                query.put("channelId", channelId);
                query.put("cartId", StringUtils.toString(cartId));
                query.put("productCode", code);
                query.put("skuCode", sku.getStringAttribute("skuCode"));
                CmsBtDtSkuModel currentCmsBtDtSku = cmsBtDtSkuDao.selectOne(query);

                if (currentCmsBtDtSku == null) {
                    // 不存在，新增
                    cmsBtDtSkuDao.insert(dtSkuModel);
                } else {
                    // 存在，更新
                    dtSkuModel.setId(currentCmsBtDtSku.getId());
                    cmsBtDtSkuDao.update(dtSkuModel);
                }
            }

        }

    }

}
