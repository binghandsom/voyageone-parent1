package com.voyageone.task2.cms.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.dt.enums.DtConstants;
import com.voyageone.components.dt.service.DtWareService;
import com.voyageone.service.dao.cms.CmsBtDtSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
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

import java.util.*;
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
    private CmsBtProductDao cmsBtProductDao;
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
        String dtWareId = null;
        // 产品code
        String prodCode = null;
        // 开始时间
        long prodStartTime = System.currentTimeMillis();

        try {
            // 获取group信息
            CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
            if (grpModel == null) {
                String errMsg = String.format("取得group信息失败! 没找到对应的group数据! [groupId=%s]", groupId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            // 分销numIId
            if (!StringUtils.isEmpty(grpModel.getNumIId())) {
                dtWareId = grpModel.getNumIId();
            }
            List<String> codes = grpModel.getProductCodes();
            if (ListUtils.isNull(codes)) {
                String errMsg = String.format("分销上新对象group信息里面没有找到对应的产品code信息! [groupId=%s]", groupId);
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            // 分销numIId
            if (StringUtils.isEmpty(dtWareId)) {
                dtWareId = Joiner.on(",").join(codes);
            }
            prodCode = codes.get(0);
            // 每个group应该只有一个code
            for(String code : codes) {
                // 分销上新
                String result = dtWareService.onShelfProduct(shop, code);
                $info("产品(%s)调用分销上新接口结束! [result:%s]", code, result);

                // 判断调用API分销上新成功与否
                if (StringUtils.isEmpty(result)) {
                    throw new BusinessException(String.format("当前产品分销上新失败！调用分销上新接口返回值为空! [groupId:%s] [code:%s]", groupId, code));
                } else {
                    // 分销返回结果基类里面一定要设置data,所以分销比别的平台(如:cnn)返回的结果里面多了一层data
                    // 例如：{"data":{"result":"OK"}}  {"data":{"result":"NG","reason":"没有可以上架的SKU。(code:022-EA3060538652)"}}
                    Map<String, Object> responseMap = JacksonUtil.jsonToMap(result);
                    if (responseMap != null && responseMap.containsKey("data") && responseMap.get("data") != null) {
                        Map<String, Object> resultMap = (LinkedHashMap)responseMap.get("data");
                        if (resultMap != null && resultMap.containsKey("result") && resultMap.get("result") != null
                                && DtConstants.C_DT_RETURN_SUCCESS_OK.equals(resultMap.get("result"))) {
                            // 新增/更新商品成功,回写product表numIId(code)和上下架状态(OnSale)
                            saveProductStatus(channelId, StringUtils.toString(cartId), code, CmsConstants.PlatformActive.ToOnSale, true, null, getTaskName());

                            // 回写分销SKU信息表(cms_bt_dt_sku)
                            saveProductDtSku(channelId, cartId, code);
                        } else {
                            // 新增/更新商品失败
                            String errMsg = String.format("当前产品调用分销接口上新失败! [groupId:%s] [code:%s] [errMsg=%s]", groupId, code, resultMap.get("reason"));
                            throw new BusinessException(errMsg);
                        }
                    }
                }
            }

            // 回写numIid(code)到productGroup表中
            saveProductGroupStatus(channelId, StringUtils.toString(cartId), groupId, prodCode, CmsConstants.PlatformActive.ToOnSale, getTaskName());

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

            String resultMsg = !StringUtils.isEmpty(ex.getMessage()) ? ex.getMessage() : errMsg;

            // 新增/更新商品失败,回写错误信息到product表
            saveProductStatus(channelId, StringUtils.toString(cartId), prodCode, CmsConstants.PlatformActive.ToOnSale, false, resultMsg, getTaskName());

            // 回写workload表(2:上新失败)
            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
        }
    }

    /**
     * 回写product表的numIId和platfromStatus
     *
     * @param channelId 渠道Id
     * @param cartId    平台Id
     * @param code  产品code
     * @param platformActive 上下架动作(ToOnSale/ToInStock)
     * @param resultFlg  分销上新是否成功(true：成功，false:失败)
     * @param errMsg    分销上新失败时的错误信息
     * @param modifier  更新者(可以不填)
     */
    public void saveProductStatus(String channelId, String cartId, String code, CmsConstants.PlatformActive platformActive, boolean resultFlg, String errMsg, String modifier) {
        if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(cartId) || StringUtils.isEmpty(code) || platformActive == null) return;

        String platformStatus = null;
        if (CmsConstants.PlatformActive.ToOnSale.name().equals(platformActive.name())) {
            // 上架
            platformStatus = CmsConstants.PlatformStatus.OnSale.name();
        } else {
            // 下架
            platformStatus = CmsConstants.PlatformStatus.InStock.name();
        }

        // 更新产品的numIId(更新成code)和平台状态
        JongoUpdate updProductObj = new JongoUpdate();
        updProductObj.setQuery("{'common.fields.code':#}");
        updProductObj.setQueryParameters(code);
        if (resultFlg) {
            // 上新成功
            updProductObj.setUpdate("{$set:{'platforms.P#.pNumIId':#,'platforms.P#.pStatus':#,'platforms.P#.pReallyStatus':#,'platforms.P#.pPublishError':'','platforms.P#.pPublishMessage':'','platforms.P#.modified':#,'platforms.P#.modifier':#}}");
            updProductObj.setUpdateParameters(cartId, code, cartId, platformStatus, cartId, platformStatus, cartId, cartId, cartId, DateTimeUtil.getNow(), cartId, modifier);
        } else {
            // 上新失败
            updProductObj.setUpdate("{$set:{'platforms.P#.pPublishError':'Error','platforms.P#.pPublishMessage':#,'platforms.P#.modified':#,'platforms.P#.modifier':#}}");
            updProductObj.setUpdateParameters(cartId, cartId, errMsg, cartId, DateTimeUtil.getNow(), cartId, modifier);
        }
        cmsBtProductDao.updateFirst(updProductObj, channelId);
    }

    /**
     * 回写productGroup表的numIId和platfromStatus,并记录每个产品的上下架历史到cms_bt_platform_active_log_cXXX表
     * (分销的数据，应该是一个code一个group)
     *
     * @param channelId 渠道Id
     * @param cartId    平台Id
     * @param code  产品code
     * @param platformActive 上下架动作(ToOnSale/ToInStock)
     * @param modifier  更新者(可以不填)
     */
    public void saveProductGroupStatus(String channelId, String cartId, Long groupId, String code, CmsConstants.PlatformActive platformActive, String modifier) {
        if (StringUtils.isEmpty(channelId) || groupId == null || platformActive == null) return;

        String platformStatus = null;
        // 更新产品Group的numIId(更新成其中的一个code)和平台状态
        // 回写上下架状态productGroup表
        JongoUpdate updateGroupObj = new JongoUpdate();
        updateGroupObj.setQuery("{'groupId':#}");
        updateGroupObj.setQueryParameters(groupId);
        if (CmsConstants.PlatformActive.ToOnSale.name().equals(platformActive.name())) {
            // 上架
            platformStatus = CmsConstants.PlatformStatus.OnSale.name();
            updateGroupObj.setUpdate("{$set:{'numIId':#,'platformStatus':#,'onSaleTime':#,'modified':#,'modifier':#}}");
            updateGroupObj.setUpdateParameters(code, platformStatus, DateTimeUtil.getNow(), DateTimeUtil.getNow(), modifier);
        } else {
            // 下架
            platformStatus = CmsConstants.PlatformStatus.InStock.name();
            updateGroupObj.setUpdate("{$set:{'numIId':#,'platformStatus':#,'inStockTime':#,'modified':#,'modifier':#}}");
            updateGroupObj.setUpdateParameters(code, platformStatus, DateTimeUtil.getNow(), DateTimeUtil.getNow(), modifier);
        }
        cmsBtProductGroupDao.updateFirst(updateGroupObj, channelId);

        // 记录每个产品的上下架历史到mongoDB
        List<String> codeList = new ArrayList<>();
        codeList.add(code);
        sxProductService.addPlatformActiveLog(channelId, cartId, groupId, codeList, true, modifier, modifier);
    }

    /**
     * 回写分销SKU信息表(cms_bt_dt_sku)
     *
     * @param channelId 渠道id
     * @param cartId 平台id
     * @param code 单个产品code
     */
    protected void saveProductDtSku(String channelId, int cartId, String code) {
        List<String> codes = new ArrayList<>();
        codes.add(code);

        saveProductDtSku(channelId, cartId, codes);
    }

    /**
     * 回写分销SKU信息表(cms_bt_dt_sku)
     *
     * @param channelId 渠道id
     * @param cartId 平台id
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
                dtSkuModel.setOrgChannelId(prodObj.getOrgChannelId());
                dtSkuModel.setChannelId(prodObj.getChannelId()); // ims表同步库存需要用OrgChannelId
                dtSkuModel.setProductCode(code);
                dtSkuModel.setSkuCode(sku.getStringAttribute("skuCode"));
                dtSkuModel.setCreater(getTaskName());
                dtSkuModel.setCreated(DateTimeUtil.getDate());
                dtSkuModel.setModifier(getTaskName());
                dtSkuModel.setModified(DateTimeUtil.getDate());

                // 查询mySql表中的sku列表(一个产品查询一次，如果每个sku更新/新增的时候都去查的话，效率太低了)
                Map<String, String> query = new HashMap<>();
                query.put("channelId", prodObj.getOrgChannelId());  // ims表同步库存需要用OrgChannelId
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
                    dtSkuModel.setCreater(currentCmsBtDtSku.getCreater());
                    dtSkuModel.setCreated(currentCmsBtDtSku.getCreated());
                    cmsBtDtSkuDao.update(dtSkuModel);
                }
            }

        }

    }

}
