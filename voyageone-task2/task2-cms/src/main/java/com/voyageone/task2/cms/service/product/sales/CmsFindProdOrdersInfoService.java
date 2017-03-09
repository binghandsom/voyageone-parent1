package com.voyageone.task2.cms.service.product.sales;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 从订单历史记录表中统计出指定销量数据
 * 关联表:
 *   mongo: cms_bt_product_cxxx
 *   mongo: cms_mt_prod_sales_his
 *
 * @author jason.jiang on 2016/05/24
 * @version 2.0.0
 */
@Service
public class CmsFindProdOrdersInfoService extends BaseCronTaskService {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CmsSumProdOrdersService cmsSumProdOrdersService;
    @Autowired
    private CmsSumGroupOrdersService cmsSumGroupOrdersService;

    private final static int PAGE_LIMIT = 500;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsImportOrdersHisInfoJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
    }

    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Set<String>> prodCodeChannelMap, Map<String, Object> rsMap) throws Exception {
        $info("onStartup start");
        List<OrderChannelBean> list = channelService.getChannelListBy(null, null, -1, "1");
        if (list == null || list.isEmpty()) {
            $warn("CmsFindProdOrdersInfoService 无店铺数据！");
            return;
        }

        String endDate = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATE_FORMAT);
        String begDate1 = DateTimeUtil.getDateBeforeDays(7);
        String begDate2 = DateTimeUtil.getDateBeforeDays(30);

        // 统计code销售数据的查询条件
        JongoQuery qryObj = new JongoQuery();
        qryObj.setProjection("{'common.fields.code':1,'common.skus.skuCode':1,'platforms':1}");
        qryObj.setLimit(PAGE_LIMIT);
        int prodIdx;

        // 统计cart数据的查询条件
        JongoQuery grpqryObj = new JongoQuery();
        grpqryObj.setQuery("{'cartId':{$nin:[0,1]}}");
        grpqryObj.setProjection("{'groupId':1,'cartId':1,'productCodes':1,'_id':1}");
        grpqryObj.setLimit(PAGE_LIMIT);
        int grpIdx;
        JongoQuery grpqryObj2 = new JongoQuery();
        grpqryObj2.setQuery("{'cartId':0}");
        grpqryObj2.setProjection("{'groupId':1,'productCodes':1,'_id':1}");
        grpqryObj2.setLimit(PAGE_LIMIT);
        long staTime = 0;
        List<OrderChannelBean> list2 = new ArrayList<>();

        for (OrderChannelBean chnObj : list) {
            staTime = System.currentTimeMillis();
            String channelId = chnObj.getOrder_channel_id();
            $info(String.format("onStartup excute msg channel_id:%s", channelId));
            // 先判断该店铺的cms_bt_product_cxxx表是否存在
            boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(channelId));
            if (!exists) {
                $warn("CmsCopyOrdersInfoService 本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
                continue;
            }
            // 判断该店铺的cms_bt_product_group_cxxx表是否存在
            exists = cmsBtProductGroupDao.collectionExists(cmsBtProductGroupDao.getCollectionName(channelId));
            if (!exists) {
                $warn("CmsCopyOrdersInfoService 本店铺对应的cms_bt_product_group_cxxx表不存在！ channelId=" + channelId);
                continue;
            }

            //get prodCodeSet
            Set<String> prodCodeSet = prodCodeChannelMap.get(channelId);
            if ($isDebugEnabled()) {
                $debug("prodCodeSet:%s", JacksonUtil.bean2Json(prodCodeSet));
            }
            if (prodCodeSet == null || prodCodeSet.isEmpty()) {
                continue;
            }
            // 针对各店铺产品进行统计
            prodIdx = 0;
            // 统计code级销售数据
            List<CmsBtProductModel> prodList;
            List<Runnable> runnableList = new ArrayList<>();
            do {
                qryObj.setSkip(prodIdx * PAGE_LIMIT);
                prodIdx++;
                prodList = cmsBtProductDao.select(qryObj, channelId);
                if (prodList == null || prodList.isEmpty()) {
                    $warn("CmsFindProdOrdersInfoService 该店铺无产品数据！ + channel_id=" + channelId);
                    break;
                }

                // check product sales exist
                List<CmsBtProductModel> prodListThead = new ArrayList<>();
                for (CmsBtProductModel prodObj : prodList) {
                    if (prodObj.getCommon() == null || prodObj.getCommon().getFields() == null) {
                        $warn("CmsFindProdOrdersInfoService 产品数据不正确 channelId=%s, ObjId=%s", channelId, prodObj.get_id());
                        continue;
                    }
                    String productCode = StringUtils.trimToNull(prodObj.getCommon().getFields().getCode());
                    if (productCode == null) {
                        $warn("CmsFindProdOrdersInfoService 产品数据不正确 没有code channelId=%s, ObjId=%s", channelId, prodObj.get_id());
                        continue;
                    }
                    if (prodCodeSet.contains(productCode)) {
                        prodListThead.add(prodObj);
                    }
                }
                // add thread
                runnableList.add(() -> cmsSumProdOrdersService.sumProdOrders(prodListThead, channelId, begDate1, begDate2, endDate, getTaskName()));
            } while (prodList.size() == PAGE_LIMIT);
            // 运行线程
            runWithThreadPool(runnableList, taskControlList);
            $info(String.format("sumProdOrders end msg channel_id:%s", channelId));

//            // 统计group级的销售数据
//            // 先统计各个cart的数据
//            List<CmsBtProductGroupModel> getList;
//            grpIdx = 0;
//            List<Runnable> runnableList2 = new ArrayList<>();
//            do {
//                grpqryObj.setSkip(grpIdx * PAGE_LIMIT);
//                grpIdx++;
//                getList = cmsBtProductGroupDao.select(grpqryObj, channelId);
//                if (getList == null || getList.isEmpty()) {
//                    $warn(String.format("CmsFindProdOrdersInfoService(统计各个cart) 该店铺无group数据！ channel_id=%s", channelId));
//                    break;
//                }
//
//                final List<CmsBtProductGroupModel> finalGrpList = getList;
//                runnableList2.add(() -> cmsSumGroupOrdersService.sumPerCartGroupOrders(finalGrpList, channelId, begDate1, begDate2, endDate, getTaskName()));
//            } while (getList.size() == PAGE_LIMIT);
//            // 运行线程
//            runWithThreadPool(runnableList2, taskControlList);
//            $info(String.format("sumPerCartGroupOrders end msg channel_id:%s", channelId));
//
//            // 再统计所有cart的数据
//            grpIdx = 0;
//            List<Runnable> runnableList3 = new ArrayList<>();
//            do {
//                grpqryObj2.setSkip(grpIdx * PAGE_LIMIT);
//                grpIdx++;
//                getList = cmsBtProductGroupDao.select(grpqryObj2, channelId);
//                if (getList == null || getList.isEmpty()) {
//                    $warn(String.format("CmsFindProdOrdersInfoService(统计所有cart) 该店铺无group数据！ channel_id=%s", channelId));
//                    break;
//                }
//
//                final List<CmsBtProductGroupModel> finalGrpList = getList;
//                runnableList3.add(() -> cmsSumGroupOrdersService.sumAllCartGroupOrders(finalGrpList, channelId, begDate1, begDate2, endDate, getTaskName()));
//            } while (getList.size() == PAGE_LIMIT);
//            // 运行线程
//            runWithThreadPool(runnableList3, taskControlList);
//            $info(String.format("sumAllCartGroupOrders end msg channel_id:%s", channelId));

            chnObj.setModifier(Long.toString((System.currentTimeMillis() - staTime) / 1000));
            list2.add(chnObj);
        } // end for channel list

        rsMap.put("secPhase", list2);
        $info("onStartup end");
    }

}
