package com.voyageone.task2.cms.service.product;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class CmsFindProdOrdersInfoService extends BaseTaskService {

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

    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Set<String>> prodCodeChannelMap) throws Exception {
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
        JomgoQuery qryObj = new JomgoQuery();
        qryObj.setProjection("{'_id':0,'fields.code':1,'skus.skuCode':1,'skus.skuCarts':1}");
        qryObj.setLimit(PAGE_LIMIT);
        int prodIdx;

        // 统计cart数据的查询条件
        JomgoQuery grpqryObj = new JomgoQuery();
        grpqryObj.setQuery("{'cartId':{$nin:[0,1]}}");
        grpqryObj.setProjection("{'groupId':1,'cartId':1,'productCodes':1,'_id':1}");
        grpqryObj.setLimit(PAGE_LIMIT);
        int grpIdx;
        JomgoQuery grpqryObj2 = new JomgoQuery();
        grpqryObj2.setQuery("{'cartId':0}");
        grpqryObj2.setProjection("{'groupId':1,'productCodes':1,'_id':1}");
        grpqryObj2.setLimit(PAGE_LIMIT);

        for (OrderChannelBean chnObj : list) {
            $info(String.format("onStartup excute msg channel_id:%s", chnObj.getOrder_channel_id()));
            //get prodCodeSet
            Set<String> prodCodeSet = prodCodeChannelMap.get(chnObj.getOrder_channel_id());
            $debug("prodCodeSet:%s", JacksonUtil.bean2Json(prodCodeSet));
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
                prodList = cmsBtProductDao.select(qryObj, chnObj.getOrder_channel_id());
                if (prodList == null || prodList.isEmpty()) {
                    $warn("CmsFindProdOrdersInfoService 该店铺无产品数据！ + channel_id=" + chnObj.getOrder_channel_id());
                    break;
                }

                // check product sales exist
                List<CmsBtProductModel> prodListThead = new ArrayList<>();
                for (CmsBtProductModel prodObj : prodList) {
                    if (prodCodeSet.contains(prodObj.getFields().getCode())) {
                        prodListThead.add(prodObj);
                    }
                }
                // add thread
                runnableList.add(() -> cmsSumProdOrdersService.sumProdOrders(prodListThead, chnObj.getOrder_channel_id(), begDate1, begDate2, endDate, getTaskName()));
            } while (prodList.size() == PAGE_LIMIT);
            // 运行线程
            runWithThreadPool(runnableList, taskControlList);
            $info(String.format("sumProdOrders end msg channel_id:%s", chnObj.getOrder_channel_id()));

            // 统计group级的销售数据
            // 先统计各个cart的数据
            List<CmsBtProductGroupModel> getList;
            grpIdx = 0;
            List<Runnable> runnableList2 = new ArrayList<>();
            do {
                grpqryObj.setSkip(grpIdx * PAGE_LIMIT);
                grpIdx++;
                getList = cmsBtProductGroupDao.select(grpqryObj, chnObj.getOrder_channel_id());
                if (getList == null || getList.isEmpty()) {
                    $warn(String.format("CmsFindProdOrdersInfoService(统计各个cart) 该店铺无group数据！ channel_id=%s", chnObj.getOrder_channel_id()));
                    break;
                }

                final List<CmsBtProductGroupModel> finalGrpList = getList;
                runnableList.add(() -> cmsSumGroupOrdersService.sumPerCartGroupOrders(finalGrpList, chnObj.getOrder_channel_id(), begDate1, begDate2, endDate, getTaskName()));
            } while (getList.size() == PAGE_LIMIT);
            // 运行线程
            runWithThreadPool(runnableList2, taskControlList);
            $info(String.format("sumPerCartGroupOrders end msg channel_id:%s", chnObj.getOrder_channel_id()));

            // 再统计所有cart的数据
            grpIdx = 0;
            List<Runnable> runnableList3 = new ArrayList<>();
            do {
                grpqryObj2.setSkip(grpIdx * PAGE_LIMIT);
                grpIdx++;
                getList = cmsBtProductGroupDao.select(grpqryObj2, chnObj.getOrder_channel_id());
                if (getList == null || getList.isEmpty()) {
                    $warn(String.format("CmsFindProdOrdersInfoService(统计所有cart) 该店铺无group数据！ channel_id=%s", chnObj.getOrder_channel_id()));
                    break;
                }

                final List<CmsBtProductGroupModel> finalGrpList = getList;
                runnableList.add(() -> cmsSumGroupOrdersService.sumAllCartGroupOrders(finalGrpList, chnObj.getOrder_channel_id(), begDate1, begDate2, endDate, getTaskName()));
            } while (getList.size() == PAGE_LIMIT);
            // 运行线程
            runWithThreadPool(runnableList3, taskControlList);
            $info(String.format("sumAllCartGroupOrders end msg channel_id:%s", chnObj.getOrder_channel_id()));

        } // end for channel list

        $info("onStartup end");
    }

}
