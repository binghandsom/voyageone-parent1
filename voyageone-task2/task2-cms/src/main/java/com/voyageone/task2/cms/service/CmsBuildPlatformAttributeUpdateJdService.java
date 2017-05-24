package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.components.jd.service.JdWareService;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Charis on 2017/3/17.
 */
@Service
public class CmsBuildPlatformAttributeUpdateJdService extends BaseCronTaskService {

    // 抱团一起更新回写的属性
    private final static List<String> attributeList = Lists.newArrayList(PlatformWorkloadAttribute.DESCRIPTION.getValue(),
            PlatformWorkloadAttribute.TITLE.getValue(), PlatformWorkloadAttribute.SELLER_CIDS.getValue());

    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private JdWareService jdWareService;
    @Autowired
    private JdSkuService jdSkuService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformAttributeUpdateJdJob";
    }


    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        doUpdateMain(taskControlList);

    }

    public void doUpdateMain(List<TaskControlBean> taskControlList) {
        // 由于这个方法可能会自己调用自己循环很多很多次， 不一定会跳出循环， 但又希望能获取到最新的TaskControl的信息， 所以不使用基类里的这个方法了
        // 为了调试方便， 允许作为参数传入， 但是理想中实际运行中， 基本上还是自主获取的场合比较多
        if (taskControlList == null) {
            taskControlList = taskDao.getTaskControlList(getTaskName());

            if (taskControlList.isEmpty()) {
//                $info("没有找到任何配置。");
                logIssue("没有找到任何配置！！！", getTaskName());
                return;
            }

            // 是否可以运行的判断
            if (!TaskControlUtils.isRunnable(taskControlList)) {
                $info("Runnable is false");
                return;
            }

        }
        // 抽出件数(默认为500)
        int rowCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.row_count, "500"));

        // 每个小组， 最多允许的线程数量
        int threadCount = NumberUtils.toInt(TaskControlUtils.getVal1WithDefVal(taskControlList, TaskControlEnums.Name.thread_count, "5"));

        // 获取该任务可以运行的销售渠道
        List<TaskControlBean> taskControlBeanList = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 准备按组分配线程（相同的组， 会共用相同的一组线程通道， 不同的组， 线程通道互不干涉）
        Map<String, List<String>> mapTaskControl = new HashMap<>();
        taskControlBeanList.forEach((l)->{
            String key = l.getCfg_val2();
            if (StringUtils.isEmpty(key)) {
                key = "0";
            }
            if (mapTaskControl.containsKey(key)) {
                mapTaskControl.get(key).add(l.getCfg_val1());
            } else {
                List<String> channelList = new ArrayList<>();
                channelList.add(l.getCfg_val1());
                mapTaskControl.put(key, channelList);
            }
        });

        Map<String, ExecutorService> mapThread = new HashMap<>();

        while (true) {

            mapTaskControl.forEach((k, v)->{
                boolean blnCreateThread = false;

                if (mapThread.containsKey(k)) {
                    ExecutorService t = mapThread.get(k);
                    if (t.isTerminated()) {
                        // 可以新做一个线程
                        blnCreateThread = true;
                    }
                } else {
                    // 可以新做一个线程
                    blnCreateThread = true;
                }

                if (blnCreateThread) {
                    ExecutorService t = Executors.newSingleThreadExecutor();

                    List<String> channelIdList = v;
                    if (channelIdList != null) {
                        for (String channelId : channelIdList) {
                            t.execute(() -> {
                                try {
                                    doProductUpdate(channelId, CartEnums.Cart.JD.getValue(), threadCount, rowCount);
                                    doProductUpdate(channelId, CartEnums.Cart.JG.getValue(), threadCount, rowCount);
                                    doProductUpdate(channelId, CartEnums.Cart.JGJ.getValue(), threadCount, rowCount);
                                    doProductUpdate(channelId, CartEnums.Cart.JGY.getValue(), threadCount, rowCount);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                    }
                    t.shutdown();

                    mapThread.put(k, t);

                }
            });

            boolean blnAllOver = true;
            for (Map.Entry<String, ExecutorService> entry : mapThread.entrySet()) {
                if (!entry.getValue().isTerminated()) {
                    blnAllOver = false;
                    break;
                }
            }
            if (blnAllOver) {
                break;
            }
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // TODO: 所有渠道处理总件数为0的场合， 就跳出不继续做了。 以外的场合， 说明可能还有别的未完成的数据， 继续自己调用自己一下
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        doUpdateMain(null);

    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId String 平台ID
     */
    public void doProductUpdate(String channelId, int cartId, int threadPoolCnt, int rowCount) throws Exception {

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(channelId, cartId);
        if (shopProp == null) {
            return;
        }
        // 从上新的任务表中获取该平台及渠道需要上新的任务列表(group by channel_id, cart_id, group_id)
        List<CmsBtSxWorkloadModel> workloadList = platformProductUploadService.getSxWorkloadWithChannelIdListCartIdList(
                rowCount, channelId, cartId);
        if (ListUtils.isNull(workloadList)) {
            return;
        }

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for(CmsBtSxWorkloadModel cmsBtSxWorkloadModel : workloadList) {
            // 启动多线程
            executor.execute(() -> doJdAttributeUpdate(cmsBtSxWorkloadModel, shopProp));
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
     * 平台产品 部分属性更新处理
     * @param work 需要更新的数据
     */
    public void doJdAttributeUpdate(CmsBtSxWorkloadModel work, ShopBean shop){
        SxData sxData = null;
        String channelId = work.getChannelId();
        int cartId = work.getCartId();
        Long groupId = work.getGroupId();
        String workloadName = work.getWorkloadName();
        // 开始时间
        long prodStartTime = System.currentTimeMillis();
        work.setModified(new Date(prodStartTime));
        try {
            if (PlatformWorkloadAttribute.JD_SKUID.getValue().equals(workloadName)) {
                // 获取group信息
                CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
                if (grpModel == null) {
                    String errMsg = "没找到对应的group数据(groupId=" + groupId + ")";
                    $error(errMsg);
                    throw new BusinessException(errMsg);
                }
                // 商品对应的numIId
                String numIId = grpModel.getNumIId();
                StringBuilder failCause = new StringBuilder("");
                // 根据京东商品id取得京东平台上的sku信息列表
                List<Sku> skuList = jdSkuService.getSkusByWareId(shop, numIId, failCause);
                if (ListUtils.isNull(skuList)) {
                    String errorMsg = String.format("取得页面skuList为空！请检查该商品！[numIId:%s]:", numIId);
                    $error(errorMsg);
                    throw new BusinessException(errorMsg);
                } else {
                    // 回写workload表(成功1)
                    sxProductService.updatePlatformWorkload(work, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());

                    // 循环取得的sku信息列表，把jdSkuId批量更新到product中去
                    BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
                    BulkWriteResult rs;
                    for (Sku sku : skuList) {
                        JongoUpdate jongoUpdate = new JongoUpdate();
                        jongoUpdate.setQuery("{'platforms.P"+ cartId +".skus.skuCode':#}");
                        jongoUpdate.setQueryParameters(sku.getOuterId());
                        jongoUpdate.setUpdate("{$set:{'platforms.P"+ cartId +".skus.$.jdSkuId':#,'modified':#,'modifier':#}}");
                        jongoUpdate.setUpdateParameters(StringUtils.toString(sku.getSkuId()), DateTimeUtil.getNowTimeStamp(), getTaskName());
                        rs = bulkList.addBulkJongo(jongoUpdate);
                        if (rs != null) {
                            $debug("成功回写了一条sku数据！！ channelId=%s, cartId=%s, wareId=%s, skuCode=%s, skuId=%s, jdSkuId更新结果=%s",
                                    channelId, cartId, numIId, sku.getOuterId(), StringUtils.toString(sku.getSkuId()), rs.toString());
                        }
                    }
                    rs = bulkList.execute();
                    if (rs != null) {
                        $debug("成功从待回写jd_skuId任务列表中回写了所有sku数据！！ channelId=%s, cartId=%s, wareId=%s, jdSkuId更新结果=%s", channelId, cartId, numIId, rs.toString());
                    }
                }
            } else {
                sxData = sxProductService.getSxProductDataByGroupId(channelId, groupId);
                if (sxData == null) {
                    String errorMsg = String.format("取得上新用的商品数据(SxData)信息失败！请向管理员确认 [sxData=null][workloadId:%s][groupId:%s]:", work.getId(), work.getGroupId());
                    $error(errorMsg);
                    throw new BusinessException(errorMsg);
                }
                // 如果取得上新对象商品信息出错时，报错
                if (!StringUtils.isEmpty(sxData.getErrorMessage())) {
                    String errorMsg = sxData.getErrorMessage();
                    // 有错误的时候，直接报错
                    throw new BusinessException(errorMsg);
                }

                // 表达式解析子
                ExpressionParser expressionParser = new ExpressionParser(sxProductService, sxData);
                CmsBtProductModel_Platform_Cart cartData = sxData.getMainProduct().getPlatform(Integer.parseInt(shop.getCart_id()));
                String wareId = cartData.getpNumIId();
                if (StringUtils.isEmpty(wareId)) {
                    String errorMsg = String.format("取得该平台wareId失败！[ChannelId:%s] [CartId；%s] [GroupId:%s]", channelId, cartId, groupId);
                    logger.error(errorMsg);
                    throw new BusinessException(errorMsg);
                }
                com.jd.open.api.sdk.domain.Ware ware = new com.jd.open.api.sdk.domain.Ware();
                ware.setWareId(Long.parseLong(wareId));
                // 店内分类
                ware.setShopCategorys(getShopCat(cartData));
                // 商品标题
                ware.setTitle(getTitle(sxData, cartData));
                // 商品描述
                ware.setIntroduction(getNote(expressionParser, shop, sxData));

                boolean result = jdWareService.updateJdAttribute(shop, ware, workloadName);
                if (result) {
                    // 回写workload表(成功1)
                    work.setAttributeList(attributeList);
                    sxProductService.updatePlatformWorkload(work, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());
                }
            }
        }
        catch (Exception e) {

            String errMsg;
            if (PlatformWorkloadAttribute.JD_SKUID.getValue().equals(workloadName)) {
                // 回写workload表(失败2)
                sxProductService.updatePlatformWorkload(work, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
                errMsg = String.format("获取页面上skuId异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WorkloadName:%s] [%s]",
                        channelId, cartId, groupId, workloadName, e.getMessage());
            } else {
                // 回写workload表(失败2)
                work.setAttributeList(attributeList);
                sxProductService.updatePlatformWorkload(work, CmsConstants.SxWorkloadPublishStatusNum.errorNum, getTaskName());
                if (sxData == null) {
                    sxData = new SxData();
                    sxData.setChannelId(channelId);
                    sxData.setCartId(cartId);
                    sxData.setGroupId(groupId);
                    sxData.setErrorMessage(String.format("京东取得商品数据为空！[ChannelId:%s] [GroupId:%s]", channelId, groupId));
                }
                errMsg = String.format("京东平台更新商品异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [WorkloadName:%s] [%s]",
                        channelId, cartId, groupId, workloadName, e.getMessage());
            }

            $error(errMsg);
            e.printStackTrace();
            // 如果上新数据中的errorMessage为空
            if (StringUtils.isEmpty(sxData.getErrorMessage())) {
                sxData.setErrorMessage(errMsg);
            }
            // 回写详细错误信息表(cms_bt_business_log)
            sxProductService.insertBusinessLog(sxData, getTaskName());
            $error(String.format("京东平台更新商品信息异常结束！[ChannelId:%s] [CartId:%s] [GroupId:%s] [耗时:%s]",
                    channelId, cartId, groupId, (System.currentTimeMillis() - prodStartTime)));
            return;
        }
    }

    public String getTitle(SxData sxData, CmsBtProductModel_Platform_Cart cartData){
        String title = cartData.getFields().getStringAttribute("productTitle");
        if (StringUtils.isEmpty(title) || title.length() > 45) {
            String tmpBrand = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getBrand();
            String tmpSizeType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getSizeTypeCn();
            if (StringUtils.isEmpty(tmpSizeType)) {
                tmpSizeType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getSizeType();
            }
            String tmpProductType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getProductTypeCn();
            if (StringUtils.isEmpty(tmpProductType)) {
                tmpProductType = sxData.getMainProduct().getCommonNotNull().getFieldsNotNull().getProductType();
            }
            title = tmpBrand + " " + tmpSizeType + " " + tmpProductType;
            if (StringUtils.isEmpty(title) || title.length() > 45) {
                title = tmpBrand + " " + tmpProductType;
            }
        }
        return title;
    }

    public String getNote(ExpressionParser expressionParser, ShopBean shopBean, SxData sxData){
        String strNotes;
        try {
            MasterWord masterWord = new MasterWord("details");
            RuleExpression ruleDetails = new RuleExpression();
            ruleDetails.addRuleWord(masterWord);
            String details = expressionParser.parse(ruleDetails, shopBean, getTaskName(), null);
            if (!StringUtils.isEmpty(details)) {
                strNotes = sxProductService.resolveDict(details, expressionParser, shopBean, getTaskName(), null);
                if (StringUtils.isEmpty(strNotes)) {
                    String errorMsg = String.format("详情页描述[%s]在dict表里未设置!", details);
                    sxData.setErrorMessage(errorMsg);
                    throw new BusinessException(errorMsg);
                }
            } else {
                strNotes = sxProductService.resolveDict("京东详情页描述", expressionParser, shopBean, getTaskName(), null);
            }
        } catch (Exception ex) {
            String errMsg = String.format("京东取得详情页描述信息失败！[errMsg:%s]", ex.getMessage());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }
        return strNotes;
    }

    public Set<Long> getShopCat(CmsBtProductModel_Platform_Cart cartData) {
        Set<Long> shopCatSet = new HashSet<>();

        if (ListUtils.notNull(cartData.getSellerCats())) {
            cartData.getSellerCats().stream()
                    .forEach(sellerCat -> {
                        if (sellerCat != null && !StringUtils.isEmpty(sellerCat.getcId())) {
                            shopCatSet.add(Long.parseLong(sellerCat.getcId()));
                        }
                    });
        }
        return shopCatSet;
    }
}
