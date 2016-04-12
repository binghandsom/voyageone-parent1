package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.category.Category;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.bean.JdCategroyBean;
import com.voyageone.components.jd.service.JdCategoryImplService;
import com.voyageone.components.jd.service.JdCategoryService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by desmond on 2016/4/6.
 * 京东平台类目信息取得
 */
@Service
public class CmsBuildPlatformCategoryTreeJdMqService extends BaseMQTaskService {

    private final static String JOB_NAME = "CmsBuildPlatformCategoryTreeJdJob";

    @Autowired
    JdCategoryService jdCategoryService;
    @Autowired
    JdCategoryImplService jdCategoryImplService;
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {
        setJdCategoryInfo(taskControlList);
    }

    protected void setJdCategoryInfo(List<TaskControlBean> taskControlList) throws Exception {
        // 获取京东系所有店铺
        List<ShopBean> shopList = Shops.getShopListByPlatform(PlatFormEnums.PlatForm.JD);

        for (Iterator<ShopBean> it = shopList.iterator(); it.hasNext(); ) {
            ShopBean shop = it.next();
            if (StringUtils.isEmpty(shop.getAppKey()) || StringUtils.isEmpty(shop.getAppSecret())) {
                $info("Cart " + shop.getCart_id() + " " + shop.getCart_name() + " 对应的app key 和 app secret key 不存在，不做处理！！！");
                it.remove();
            }
        }

        // 获取该任务可以运行的销售渠道
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 循环所有店铺
        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {
            for (ShopBean shop : shopList) {
                boolean isRun = false;
                // 判断该Shop是否需要运行任务
                isRun = orderChannelIdList.contains(shop.getOrder_channel_id());

                if (isRun) {
                    // 第三方平台类目信息取得（京东系）
                    doSetPlatformCategoryJd(shop);
                }

            }
        }

        //正常结束
        $info("正常结束");
    }

    /**
     * 第三方平台类目信息取得
     *
     * @param shop 店铺信息
     */
    private void doSetPlatformCategoryJd(ShopBean shop) {

        List<Category> jdCategoryList = null;

        // 调用API 获取该店铺被授权的类目
        jdCategoryList = jdCategoryService.getCategoryInfo(shop);

        // 返回错误的场合
        if (jdCategoryList == null || jdCategoryList.size() == 0) {
            $info("未能成功获取该店铺被授权的类目或被授权类目数为0");
            return;
        }

        // 第三方平台类目信息
        ArrayList<JdCategroyBean> jdCategroyBeanList = new ArrayList<>();

        // 编辑从京东取得的类目path信息
        jdCategroyBeanList = jdCategoryImplService.getCategoryList(jdCategoryList, shop.getCart_id(), shop.getOrder_channel_id(), PlatFormEnums.PlatForm.JD.getId());

        // 获取类目树
        List<CmsMtPlatformCategoryTreeModel> savePlatformCatModels = jdCategoryImplService.getCmsMtPlatformCategoryTreeModelList(jdCategroyBeanList, shop.getCart_id(), shop.getOrder_channel_id(), this.getTaskName());

        // 更新MangoDB类目数据
        jdCategoryImplService.setMangoDBPlatformCatTrees(savePlatformCatModels, shop.getCart_id(), shop.getOrder_channel_id());
    }

}



