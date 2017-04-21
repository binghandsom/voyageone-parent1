package com.voyageone.task2.cms.service;

import com.google.common.collect.Lists;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  获取京东商品的skuId 回写mongo
 * Created by Charis on 2017/4/21.
 */
@Service
public class CmsBuildPlatformGetJdSkuIdService extends BaseCronTaskService{

    private final static List<String> CARTIDLIST = Lists.newArrayList("24","26","28","29");
    
    private final static String WORKLOADNAME = PlatformWorkloadAttribute.JD_SKUID.getValue();

    @Autowired
    private PlatformProductUploadService platformProductUploadService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private JdSkuService jdSkuService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private SxProductService sxProductService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformGetJdSkuIdJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 获取该任务可以运行的销售渠道
        List<String> channels = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        List<CmsBtSxWorkloadModel> workloadList =  platformProductUploadService.getSxWorkloadByChannelIdCartIdWorkloadName(
                CmsConstants.PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE, channels, CARTIDLIST, WORKLOADNAME);
        if (workloadList.size() == 0) {
            $error("更新任务表中没有该平台对应的任务列表信息！[ChannelIdList:%s] [workloadName:%s]", channels, WORKLOADNAME);
            return;
        }
        $info("从更新任务表中共读取共读取[%d]条更新任务！[ChannelIdList:%s]", workloadList.size(), channels);

        for(CmsBtSxWorkloadModel workloadModel : workloadList) {
            getJdSkuId(workloadModel);
        }
    }

    public void getJdSkuId(CmsBtSxWorkloadModel workloadModel) {
        String channelId = workloadModel.getChannelId();
        int cartId = workloadModel.getCartId();
        long groupId = workloadModel.getGroupId();
        StringBuilder failCause = new StringBuilder("");
        ShopBean shop;
        try {
            //读店铺信息
            shop = Shops.getShop(channelId, cartId);

            shop.setApp_url("https://api.jd.com/routerjson");
            shop.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
            shop.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
            shop.setSessionKey("b8a15129-971d-4428-a9bb-f806d9817724");

            if (shop == null) {
                $error("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                throw new Exception(String.format("获取到店铺信息失败! [ChannelId:%s] [CartId:%s]", channelId, cartId));
            }

            // 获取group信息
            CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + groupId + "}", channelId);
            if (grpModel == null) {
                String errMsg = "没找到对应的group数据(groupId=" + groupId + ")";
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
            // 商品对应的numIId
            String numIId = grpModel.getNumIId();

            // 根据京东商品id取得京东平台上的sku信息列表
            List<Sku> skuList = jdSkuService.getSkusByWareId(shop, numIId, failCause);
            if (ListUtils.isNull(skuList)) {
                // 忽略这条数据  等待下次继续执行
                return;
            } else {
                // 回写workload表(成功1)
                sxProductService.updatePlatformWorkload(workloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());

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

        } catch (Exception e) {
            String errMsg = String.format("获取jd_skuId任务失败！[channelId:%s] [cartId:%s] [groupId:%s] [message:%S]", channelId, cartId, groupId, e.getMessage());
            $error(errMsg);
            e.printStackTrace();
        }
    }



}
