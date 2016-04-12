package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.category.Category;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.JdConstants;
import com.voyageone.components.jd.bean.JdCategroyBean;
import com.voyageone.components.jd.service.JdCategoryService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
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
    private PlatformCategoryService platformCategoryService;

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

    /**
     * 京东平台类目信息取得
     *
     * @param taskControlList taskcontrol信息
     */
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
        jdCategroyBeanList = this.getCategoryList(jdCategoryList, shop.getCart_id(), shop.getOrder_channel_id(), PlatFormEnums.PlatForm.JD.getId());

        // 获取类目树
        List<CmsMtPlatformCategoryTreeModel> savePlatformCatModels = this.getCmsMtPlatformCategoryTreeModelList(jdCategroyBeanList, shop.getCart_id(), shop.getOrder_channel_id(), this.getTaskName());

        // 更新MangoDB类目数据
        platformCategoryService.setMangoDBPlatformCatTrees(savePlatformCatModels, shop.getCart_id(), shop.getOrder_channel_id());
    }

    /**
     * 取得平台类目模型信息
     *
     * @param jdCategoryList List<Category>    京东类目列表
     *        cartId         String            CartId
     *        channelId      String            渠道id
     *        platformId     String            平台id
     * @return ArrayList<Category>    京东类目列表
     * @throws BusinessException 业务异常
     */
    public ArrayList<JdCategroyBean> getCategoryList(List<Category> jdCategoryList, String cartId, String channelId, String platformId) {
        ArrayList<JdCategroyBean> categoryList = new ArrayList<>();

        // 待处理列表
        ArrayList<Category> jdCategoryAllTodo = new ArrayList<>(jdCategoryList);
        // 第三方平台path信息编辑
        // 循环列表
        while (true) {
            if (jdCategoryAllTodo.size() == 0) {
                break;
            }

            int intMax = jdCategoryAllTodo.size() - 1;
            for (int i = intMax; i >= 0; i--) {
                Category itemCat = jdCategoryAllTodo.get(i);

                JdCategroyBean jdCategroyBean = new JdCategroyBean();
                // 父类目id
                String platformParentId = String.valueOf(itemCat.getFid());

                // 获取主数据的父类目id
                boolean blnFound = false;
                if ("0".equals(platformParentId)) {
                    // 一级类目，没有父了
                    jdCategroyBean.setCidPath(itemCat.getName());
                    blnFound = true;
                } else {
                    for (JdCategroyBean cat : categoryList) {

                        if (platformParentId.equals(cat.getPlatformCid())) {
                            // 找到了父类目id
                            // path信息
                            jdCategroyBean.setCidPath(cat.getCidPath() + JdConstants.C_PROP_PATH_SPLIT_CHAR + itemCat.getName());

                            blnFound = true;
                            break;
                        }
                    }
                }

                // 没找到父类目的场合
                if (!blnFound) {
                    // 跳过
                    continue;
                }

                // 编辑
                // 渠道id
                jdCategroyBean.setChannelId(channelId);
                // Cart_id
                jdCategroyBean.setCartId(Integer.parseInt(cartId));
                // 平台id
                jdCategroyBean.setPlatformId(Integer.parseInt(platformId)); // 第三方平台Id
                // 类目id
                jdCategroyBean.setPlatformCid(String.valueOf(itemCat.getId()));
                // 父类目id
                jdCategroyBean.setParentCid(String.valueOf(itemCat.getFid()));
                // 父类目区分
                if (itemCat.isParent()) {
                    jdCategroyBean.setIsParent(1);
                } else {
                    jdCategroyBean.setIsParent(0);
                }
                // 类目名
                jdCategroyBean.setCidName(itemCat.getName());
                // 排序
                jdCategroyBean.setSortOrder(0);

                // 追加到列表
                categoryList.add(jdCategroyBean);

                // 成功后移除
                jdCategoryAllTodo.remove(i);
            }
        }

        return categoryList;
    }

    /**
	 * 获得各渠道的平台类目树
     *
     * @param jdCategroyBeanList ArrayList<JdCategroyBean>    京东类目列表
     *        cartId         String            CartId
     *        channelId      String            渠道id
     *        taskName       String            Task名
     * @return List<CmsMtPlatformCategoryTreeModel>    京东类目树列表
	 */
    public List<CmsMtPlatformCategoryTreeModel> getCmsMtPlatformCategoryTreeModelList(ArrayList<JdCategroyBean> jdCategroyBeanList, String cartId, String channelId, String taskName) {

        List<CmsMtPlatformCategoryTreeModel> platformCategoryMongoBeanList = new ArrayList<>();
        for(JdCategroyBean category:jdCategroyBeanList)
        {
            CmsMtPlatformCategoryTreeModel mongoModel = new CmsMtPlatformCategoryTreeModel();
            mongoModel.setCartId(Integer.parseInt(cartId));
            mongoModel.setChannelId(channelId);
            mongoModel.setCatId(category.getPlatformCid());
            mongoModel.setCatName(category.getCidName());
            mongoModel.setParentCatId(category.getParentCid());
            mongoModel.setIsParent(category.getIsParent());
            mongoModel.setCatPath(category.getCidPath());
            mongoModel.setCreater(null);
            mongoModel.setCreated(null);
            mongoModel.setModifier(null);
            mongoModel.setModified(null);

            platformCategoryMongoBeanList.add(mongoModel);
        }

        // 创建各渠道的平台类目层次关系
        List<CmsMtPlatformCategoryTreeModel> savePlatformCatModels = this.buildPlatformCatTrees(platformCategoryMongoBeanList, cartId, channelId, taskName);

        return savePlatformCatModels;
    }

    /**
     * 创建各渠道的平台类目层次关系.
     *
     * @param platformCatModelList List<CmsMtPlatformCategoryTreeModel>    京东类目树列表
     *        cartId         String            CartId
     *        channelId      String            渠道id
     *        taskName       String            Task名
     * @return List<CmsMtPlatformCategoryTreeModel>    京东类目层次树列表
     */
    public List<CmsMtPlatformCategoryTreeModel> buildPlatformCatTrees(List<CmsMtPlatformCategoryTreeModel> platformCatModelList, String cartId, String channelId, String taskName) {
        // 设置类目层次关系.
        List<CmsMtPlatformCategoryTreeModel> assistPlatformCatList = new ArrayList<>(platformCatModelList);

        List<CmsMtPlatformCategoryTreeModel> removePlatformCatList = new ArrayList<>();

        for (CmsMtPlatformCategoryTreeModel platformCat : platformCatModelList) {
            List<CmsMtPlatformCategoryTreeModel> subPlatformCatgories = new ArrayList<>();
            for (Iterator assIterator = assistPlatformCatList.iterator(); assIterator.hasNext(); ) {

                CmsMtPlatformCategoryTreeModel subPlatformCatItem = (CmsMtPlatformCategoryTreeModel) assIterator.next();
                if (subPlatformCatItem.getParentCatId().equals(platformCat.getCatId())) {
                    subPlatformCatgories.add(subPlatformCatItem);
                    assIterator.remove();
                }
            }
            platformCat.setChildren(subPlatformCatgories);
            if (!"0".equals(platformCat.getParentCatId())) {
                //将所有非顶层类目的引用添加到待删除列表
                removePlatformCatList.add(platformCat);
            } else {
                //设置顶层类目的信息
                platformCat.setChannelId(channelId);
                platformCat.setCartId(Integer.parseInt(cartId));
                platformCat.setCreater(taskName);
                platformCat.setCreated(DateTimeUtil.getNow());
                platformCat.setModifier(taskName);
                platformCat.setModified(DateTimeUtil.getNow());
            }
            platformCat.setChannelId(channelId);
        }

        // 删除掉所有非顶层类目引用,只留下最顶层类目
        platformCatModelList.removeAll(removePlatformCatList);

        return platformCatModelList;
    }

}



