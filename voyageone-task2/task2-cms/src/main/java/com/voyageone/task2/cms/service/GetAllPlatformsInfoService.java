package com.voyageone.task2.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.model.cms.CmsMtPlatformCategoryExtendInfoModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 取所有平台tree和schema
 *
 * @author morse on 2016/9/12
 * @version 2.6.0
 */
@Service
public class GetAllPlatformsInfoService extends BaseCronTaskService {

    private final static String JOB_NAME = "CmsGetAllPlatformsInfoJob";

    @Autowired
    private GetPlatformCategoryTreesService tmTreeService;
    @Autowired
    private GetPlatformCategorySchemaService tmSchemaService;

    @Autowired
    private CmsBuildPlatformCategoryTreeJdMqService jdTreeService;
    @Autowired
    private CmsBuildPlatformCategorySchemaJdMqService jdSchemaService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;
    @Autowired
    private CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        Set<Integer> jdCarts = new HashSet<>();
        List<TaskControlBean> listTaskControlBeen = TaskControlUtils.getVal1s(taskControlList, TaskControlEnums.Name.order_channel_id);
        for (TaskControlBean taskControlBean : listTaskControlBeen) {
            // cfg_val1 放 channelId，cfg_val2 放 cartId(逗号分隔)
            String channelId = taskControlBean.getCfg_val1();
            String[] carts = taskControlBean.getCfg_val2().split(",");
            for (String cart : carts) {
                Integer cartId = Integer.valueOf(cart);
                ShopBean shopBean = Shops.getShop(channelId, cartId);
                if (shopBean == null ||
                        StringUtils.isEmpty(shopBean.getApp_url()) ||
                        StringUtils.isEmpty(shopBean.getAppKey()) ||
                        StringUtils.isEmpty(shopBean.getAppSecret()) ||
                        StringUtils.isEmpty(shopBean.getSessionKey())
                        ) {
                    $info(String.format("店铺信息不存在, 无需获取平台tree, channelId:[%s], cartId:[%s]", channelId, cartId));
                    continue;
                }

                if (CartEnums.Cart.TM.getValue() == cartId || CartEnums.Cart.TG.getValue() == cartId
                        || CartEnums.Cart.TT.getValue() == cartId
                        || CartEnums.Cart.USTT.getValue() == cartId) {
                    // 天猫(包括天猫国际，天猫官网同购，Usjoi天猫国际官网同购)
                    doTmPlatformCategory(channelId, cartId, shopBean);
                }

                if (CartEnums.Cart.JD.getValue() == cartId
                        || CartEnums.Cart.JG.getValue() == cartId
                        || CartEnums.Cart.JGJ.getValue() == cartId
                        || CartEnums.Cart.JGY.getValue() == cartId) {
                    // 京东
                    // modified by morse.lu 2016/09/14 start
                    // 京东按平台取，不按channel取
//                    doJdPlatformCategory(channelId, cartId, shopBean);
                    jdCarts.add(cartId);
                    // 京东tree取得
                    doJdPlatformCategoryTree(shopBean);
                    // modified by morse.lu 2016/09/14 end
                }
            }
        }

        // added by morse.lu 2016/09/14 start
        // 京东按平台取，不按channel取
        for (Integer cartId : jdCarts) {
            doJdPlatformCategory(cartId);
        }
        // added by morse.lu 2016/09/14 end
    }

    /**
     * 天猫类目取得
     */
    protected void doTmPlatformCategory(String channelId, int cartId, ShopBean shopBean) {
        try {
            // 天猫tree取得
            tmTreeService.doSetPlatformCategoryTm(shopBean);

            // 天猫schema取得
            // 取得根据品牌，或者产品id才能取得类目schema的一览表
            Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap = tmSchemaService.doInit();

            List<Map> allCategoryLeavesMap = new ArrayList<>();

            // 根据channel和cart, 获取platform category tree (一条记录就是一级类目含以下的一整棵树)
            List<CmsMtPlatformCategoryTreeModel> categoryTrees = platformCategoryDao.selectByChannel_CartId(channelId, cartId);

            // 遍历一级类目, 获取下面所有的叶子类目
            for (CmsMtPlatformCategoryTreeModel root : categoryTrees) {
                Object jsonObj = JsonPath.parse(root.toString()).json();
                List<Map> leavesMap = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
                allCategoryLeavesMap.addAll(leavesMap);
            }

            // 遍历所有的叶子类目
            int idxCategory = 0;
            for (Map leafMap : allCategoryLeavesMap) {
                idxCategory++;
                String catId = (String) leafMap.get("catId");
                String catPath = (String) leafMap.get("catPath");

                String logInfo = String.format("获取天猫类目schema -> 完成度:[%s], channel:[%s], cart:[%s]", idxCategory + "/" + allCategoryLeavesMap.size(), channelId, cartId);
                try {
                    tmSchemaService.doLogicSimple(shopBean, platformCategoryExtendInfoMap, catId, catPath, logInfo);
                } catch (Exception ex) {
                    $error(String.format("天猫类目取得失败!category:[%s] + ", catId));
                }
            }
        } catch (Exception ex) {
            $error(String.format("天猫类目取得失败, channel:[%s]!" + ex.getMessage(), channelId));
        }
    }

    /**
     * 京东类目树取得
     */
    protected void doJdPlatformCategoryTree(ShopBean shopBean) {
        try {
            // 京东tree取得
            jdTreeService.doSetPlatformCategoryJd(shopBean);
        } catch (Exception ex) {
            $error("京东tree取得失败!" + ex.getMessage());
        }
    }

    /**
     * 京东类目取得
     */
    // modified by morse.lu 2016/09/14 start
    // 京东按平台取，不按channel取
//    private void doJdPlatformCategory(String channelId, int cartId, ShopBean shopBean) {
    protected void doJdPlatformCategory(int cartId) {
        // modified by morse.lu 2016/09/14 end
        try {
            // deleted by morse.lu 2016/09/14 start
            // 京东按平台取，不按channel取
//            // 京东tree取得
//            jdTreeService.doSetPlatformCategoryJd(shopBean);
            // deleted by morse.lu 2016/09/14 end

            // 京东schema取得
            // 取得类目属性叶子数据并去掉重复叶子类目
            List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(cartId);

//            // 京东共通类目
//            // 删除
//            cmsMtPlatformCategorySchemaDao.deletePlatformCategorySchemaByCategory(cartId, "1");
//            // add
//            jdSchemaService.doSetPlatformJdSchemaCommon(shopBean, cartId);

            int idxCategory = 0;
            for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allCategoryTreeLeaves) {
                idxCategory++;
                // added by morse.lu 2016/10/11 start
                ShopBean shopBean = Shops.getShop(platformCategoriesModel.getChannelId(), platformCategoriesModel.getCartId());
                // added by morse.lu 2016/10/11 end
                try {
                    doJdPlatformCategorySub(cartId, platformCategoriesModel, shopBean);

                    $info(String.format("获取京东类目schema -> 完成度:[%s], cart:[%s], catId:[%s]", idxCategory + "/" + allCategoryTreeLeaves.size(), cartId, platformCategoriesModel.getCatId()));
                } catch (Exception ex) {
                    $error(String.format("京东类目取得失败!category:[%s] + ", platformCategoriesModel.getCatId()));
                }
            }
        } catch (Exception ex) {
            $error(String.format("京东类目取得失败, cart:[%s]!" + ex.getMessage(), cartId));
        }
    }

    /**
     * 京东共通类目
     */
    protected void doJdPlatformCategoryCommon(int cartId) {
        // 京东共通类目
        // 删除
        cmsMtPlatformCategorySchemaDao.deletePlatformCategorySchemaByCategory(cartId, "1");
        // add
        jdSchemaService.doSetPlatformJdSchemaCommon(cartId);
    }

    /**
     * 京东单个类目
     */
    protected void doJdPlatformCategorySub(int cartId, CmsMtPlatformCategoryTreeModel platformCategoriesModel, ShopBean shopBean) {
        // 删除数据库现有数据(单类目)
        cmsMtPlatformCategorySchemaDao.deletePlatformCategorySchemaByCategory(cartId, platformCategoriesModel.getCatId());
        // add一下
        jdSchemaService.doSetPlatformPropJdSub(shopBean, platformCategoriesModel);
    }
}
