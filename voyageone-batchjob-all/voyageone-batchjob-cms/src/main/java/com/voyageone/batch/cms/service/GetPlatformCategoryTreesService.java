package com.voyageone.batch.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.WriteResult;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Brand;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerAuthorize;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.CmsConstants;
import com.voyageone.batch.cms.dao.BrandDao;
import com.voyageone.batch.cms.model.PlatformCategoriesModel;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategorySchemaDao;
import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbCategoryService;
import com.voyageone.common.components.tmall.bean.ItemSchema;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class GetPlatformCategoryTreesService extends BaseTaskService{

    private final static String JOB_NAME = "getPlatformCategoryTreesTask";
    private static Log logger = LogFactory.getLog(GetPlatformCategoryTreesService.class);

    @Autowired
    BrandDao brandDao;
    @Autowired
    TbCategoryService tbCategoryService;
    @Autowired
    CmsMtPlatformCategoryDao platformCategoryDao;
    @Autowired
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Resource(name = "availableChannelList")
    List availableChannelList;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception{

        // 获取天猫系所有店铺
        List<ShopBean> shopList = ShopConfigs.getShopListByPlatform(PlatFormEnums.PlatForm.TM);

        for (Iterator<ShopBean> it = shopList.iterator();it.hasNext();){
            ShopBean shop = it.next();
            if (StringUtils.isEmpty(shop.getAppKey())||StringUtils.isEmpty(shop.getAppSecret())){
                logger.info("Cart "+shop.getCart_id()+" "+shop.getCart_name()+" 对应的app key 和 app secret key 不存在，不做处理！！！");
                it.remove();
            }

        }
        // 获取该任务可以运行的销售渠道
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 循环所有店铺
        for (ShopBean shop : shopList) {
            if (availableChannelList != null && availableChannelList.size()>0){
                if (availableChannelList.contains(shop.getOrder_channel_id())){
                    // 判断该Shop是否需要运行任务
                    boolean isRun = orderChannelIdList.contains(shop.getOrder_channel_id());

                    if (isRun) {
                        // 第三方平台类目信息取得（天猫系）
                        doSetPlatformCategoryTm(shop);
                    }
                }
            }else {
                // 判断该Shop是否需要运行任务
                boolean isRun = orderChannelIdList.contains(shop.getOrder_channel_id());

                if (isRun) {
                    // 第三方平台类目信息取得（天猫系）
                    doSetPlatformCategoryTm(shop);
                }
            }

        }


        //正常结束
        logger.info("正常结束");
    }

    /**
     * 第三方平台类目信息取得（天猫系）
     * @param shop 店铺信息
     */
    private void doSetPlatformCategoryTm(ShopBean shop) throws ApiException {

        // 调用API 获取该店铺被授权的类目和品牌
        SellerAuthorize sellerAuthorize = tbCategoryService.getSellerCategoriesAuthorize(shop);

        // 返回错误的场合
        if (sellerAuthorize == null) {
            return;
        }

        // 第三方平台类目信息
        List<PlatformCategoriesModel> platformCategoriesModelList = new ArrayList<>();
        // 循环所有父类目，获取所有子类目
        List<ItemCat> subCatsAll = new ArrayList<>();
        if (sellerAuthorize.getItemCats() != null && sellerAuthorize.getItemCats().size() > 0 ) {
            // 插入该店的所有一级类目数据
            subCatsAll.addAll(sellerAuthorize.getItemCats());

            // 循环一级类目，获取所有子类目
            for(ItemCat itemcat : sellerAuthorize.getItemCats()) {
                List<ItemCat> subCats = tbCategoryService.getCategory(shop, itemcat.getCid());

                // 返回错误的场合
                if (subCats == null) {
                    return;
                }

                subCatsAll.addAll(subCats);
            }

            // 待处理列表
            List<ItemCat> subCatsAllTodo = subCatsAll;
            // 第三方平台path信息编辑
            // 循环列表
            while (true) {
                if (subCatsAllTodo.size() == 0) {
                    break;
                }

                int intMax = subCatsAllTodo.size() - 1;
                for (int i = intMax; i >= 0; i--) {
                    ItemCat itemCat = subCatsAllTodo.get(i);

                    PlatformCategoriesModel platformCategoriesModel = new PlatformCategoriesModel();
                    // 第三方平台父类目id
                    String platformParentId = String.valueOf(itemCat.getParentCid());

                    // 获取主数据的父类目id
                    boolean blnFound = false;
                    if ("0".equals(platformParentId)) {
                        // 一级类目，没有父了
                        platformCategoriesModel.setCidPath(itemCat.getName());
                        blnFound = true;
                    } else {
                        for (PlatformCategoriesModel cat : platformCategoriesModelList) {

                            if (platformParentId.equals(cat.getPlatformCid())) {
                                // 找到了
                                // 父类目id
                                platformCategoriesModel.setCidPath(cat.getCidPath() + CmsConstants.C_PROP_PATH_SPLIT_CHAR + itemCat.getName());

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
                    platformCategoriesModel.setChannelId(shop.getOrder_channel_id());
                    platformCategoriesModel.setCartId(Integer.parseInt(shop.getCart_id()));
                    platformCategoriesModel.setPlatformId(Integer.parseInt(PlatFormEnums.PlatForm.TM.getId())); // 第三方平台Id
                    platformCategoriesModel.setPlatformCid(String.valueOf(itemCat.getCid()));
                    platformCategoriesModel.setParentCid(String.valueOf(itemCat.getParentCid()));
                    if (itemCat.getIsParent()) {
                        platformCategoriesModel.setIsParent(1);
                    } else {
                        platformCategoriesModel.setIsParent(0);
                    }
                    platformCategoriesModel.setCidName(itemCat.getName());
                    platformCategoriesModel.setSortOrder(0);

                    platformCategoriesModelList.add(platformCategoriesModel);

                    // 成功后移除
                    subCatsAllTodo.remove(i);

                }
            }

        }

        /** 保存类目信息 */
        List<CmsMtPlatformCategoryTreeModel> platformCategoryMongoBeanList = new ArrayList<>();
        for (PlatformCategoriesModel category: platformCategoriesModelList){
            CmsMtPlatformCategoryTreeModel mongoModel = new CmsMtPlatformCategoryTreeModel();
            mongoModel.setCartId(null);
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

        //获取类目树
        List<CmsMtPlatformCategoryTreeModel> savePlatformCatModels = this.buildPlatformCatTrees(platformCategoryMongoBeanList,Integer.valueOf(shop.getCart_id()),shop.getOrder_channel_id());

        //删除原有类目信息
        WriteResult delCatRes = platformCategoryDao.deletePlatformCategories(Integer.valueOf(shop.getCart_id()),shop.getOrder_channel_id());

        logger.info("批量删除类目 CART_ID 为："+shop.getCart_id()+"  channel id: "+shop.getOrder_channel_id() + " 的数据为: "+delCatRes.getN() + "条...");

        if(savePlatformCatModels.size()>0){
            logger.info("保存最新的类目信息: "+ savePlatformCatModels.size() +"条记录。");
            //保存最新的类目信息
            platformCategoryDao.insertWithList(savePlatformCatModels);
        }

        /** add by lewis end 2015/11/27 **/


        // 重建第三方平台指定店铺品牌信息
        List<Brand> brands = removeListDuplicate(sellerAuthorize.getBrands());
        this.doRebuidPlatformBrand(brands, shop, JOB_NAME);

    }

    /** add by lewis start 2015/11/27 */
    /**
     * 创建各渠道的平台类目层次关系.
     *
     * @param platformCatModelList
     */
    private List<CmsMtPlatformCategoryTreeModel> buildPlatformCatTrees(List<CmsMtPlatformCategoryTreeModel> platformCatModelList,int cartId,String channelId) {
        // 设置类目层次关系.
        List<CmsMtPlatformCategoryTreeModel> assistPlatformCatList = new ArrayList<>(platformCatModelList);

        List<CmsMtPlatformCategoryTreeModel> removePlatformCatList = new ArrayList<>();

        for (int i = 0; i < platformCatModelList.size(); i++) {
            CmsMtPlatformCategoryTreeModel platformCat = platformCatModelList.get(i);
            List<CmsMtPlatformCategoryTreeModel> subPlatformCatgories = new ArrayList<>();
            for (Iterator assIterator = assistPlatformCatList.iterator(); assIterator.hasNext();) {

                CmsMtPlatformCategoryTreeModel subPlatformCatItem = (CmsMtPlatformCategoryTreeModel) assIterator.next();
                if (subPlatformCatItem.getParentCatId().equals(platformCat.getCatId()) ) {
                    subPlatformCatgories.add(subPlatformCatItem);
                    assIterator.remove();
                }

            }
            platformCat.setChildren(subPlatformCatgories);
            if (!"0".equals(platformCat.getParentCatId())) {
                //将所有非顶层类目的引用添加到待删除列表
                removePlatformCatList.add(platformCat);
            }else {
                //设置顶层类目的信息
                platformCat.setChannelId(channelId);
                platformCat.setCartId(cartId);
                platformCat.setCreater(this.getTaskName());
                platformCat.setCreated(DateTimeUtil.getNow());
                platformCat.setModifier(this.getTaskName());
                platformCat.setModified(DateTimeUtil.getNow());
            }

            //为取得平台类目Schema方便，为每个叶子叶子结点添加channelId
            if(platformCat.getIsParent().intValue()==0){

                platformCat.setChannelId(channelId);

            }

        }

        //删除掉所有非顶层类目引用,只留下最顶层类目
        platformCatModelList.removeAll(removePlatformCatList);

        return platformCatModelList;
    }
    /** add by lewis end 2015/11/27 */

    /**
     * 去除重复的品牌
     * @param brands
     * @return
     */
    private List<Brand> removeListDuplicate(List<Brand> brands) {
        List<Brand> ret = new ArrayList<>();

        if (brands != null) {
            for (int i = 0; i < brands.size(); i++){
                Boolean dup = false;
                Brand brand = brands.get(i);
                Long vid = brand.getVid();
                for (int j = i + 1; j < brands.size(); j++){
                    //查看是否有重复
                    if (vid.longValue() == brands.get(j).getVid().longValue()){
                        dup = true;
                    }
                }
                //没有重复才添加
                if (!dup){
                    ret.add(brand);
                }
            }
        }

        return ret;
    }

    /**
     * 重建第三方平台指定店铺品牌信息
     * @param brands 品牌信息
     * @param shop shop
     * @param JOB_NAME job name
     */
    @Transactional
    public void doRebuidPlatformBrand(List<Brand> brands, ShopBean shop, String JOB_NAME) {
        //删除该店的所有品牌数据
        int delCount = brandDao.delBrandsByShop(shop);
        logger.info("删除该店的所有品牌数据: " + delCount + "条。");
        //插入该店的所有品牌数据
        if(brands != null && brands.size() > 0){
            logger.info("插入该店的所有品牌数据,CartId: "+shop.getCart_id());
            brandDao.insertBrands(brands, shop, JOB_NAME);
        }
    }

}
