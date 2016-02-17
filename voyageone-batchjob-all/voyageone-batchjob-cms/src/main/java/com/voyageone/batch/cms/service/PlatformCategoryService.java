package com.voyageone.batch.cms.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Brand;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerAuthorize;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.model.PlatformCategoriesModel;
import com.voyageone.batch.cms.model.PlatformPropModel;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.cms.CmsConstants;
import com.voyageone.batch.cms.dao.BrandDao;
import com.voyageone.batch.cms.mongoDao.PlatformCategoryDao;
import com.voyageone.batch.cms.mongoModel.CmsMtPlatformCategoryTreeModel;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbCategoryService;
import com.voyageone.common.components.tmall.bean.ItemSchema;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PlatformCategoryService extends BaseTaskService{

    private final static String JOB_NAME = "platformCategoryTask";
    private static Log logger = LogFactory.getLog(PlatformCategoryService.class);

    @Autowired
    BrandDao brandDao;
    @Autowired
    TbCategoryService tbCategoryService;
    @Autowired
    PlatformCategoryDao platformCategoryDao;
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.IMS;
    }
    @Override
    public String getTaskName() {
        return JOB_NAME;
    }


    @Transactional
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception{

        // 任务说明 ------------------------------------------------- START
        // 要做的事情：
        // 1. 第三方平台类目信息取得（天猫与 TODO:京东暂时不做）
        // 2. 第三方平台品牌信息取得（天猫与 TODO:京东暂时不做）
        // 3. 天猫系达尔文信息取得（TODO:暂时先不做）
        // 4. 第三方平台属性信息取得（天猫与 TODO:京东暂时不做）

        // 执行频率：
        // 按理说是每天都需要执行一次的
        // 店铺搞活动的时候，为了不占用过多资源，可以暂停执行
        // 任务说明 ------------------------------------------------- END

        // 获取天猫系所有店铺
        List<ShopBean> shopList = ShopConfigs.getShopListByPlatform(PlatFormEnums.PlatForm.TM);
        // 获取该任务可以运行的销售渠道
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 是否有类目重新获取过
        boolean blnReGetCategory = false;

        // 循环所有店铺
        for (ShopBean shop : shopList) {
            // 判断该Shop是否需要运行任务
            boolean isRun = orderChannelIdList.contains(shop.getOrder_channel_id());

            if (isRun) {
                blnReGetCategory = true;

// TODO:临时注释掉不执行，正式环境需要放开 START
                // 第三方平台类目信息取得（天猫系）
                doSetPlatformCategoryTm(shop);
// TODO:临时注释掉不执行，正式环境需要放开 END
            }
        }

        // 如果有任何一家店铺获取过类目信息，那么就需要继续做属性信息取得
        if (blnReGetCategory) {

            // 获取天猫国际数据 TODO
            doSetPlatformPropTm(Integer.valueOf(CartEnums.Cart.TG.getId()));
            // 获取天猫数据 TODO
            doSetPlatformPropTm(Integer.valueOf(CartEnums.Cart.TM.getId()));


        }

        //正常结束
        logger.info("正常结束");
//        return TaskControlEnums.Status.SUCCESS.getIs();
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
        /** add by lewis start 2015/11/27 */
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
        platformCategoryDao.deletePlatformCategories(Integer.valueOf(shop.getCart_id()));

        if(savePlatformCatModels.size()>0){
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
     * 创建平台层次关系.
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
                removePlatformCatList.add(platformCat);
            }else {  //设置顶层类目的信息
                platformCat.setChannelId(channelId);
                platformCat.setCartId(cartId);
                platformCat.setCreater(this.getTaskName());
                platformCat.setCreated(DateTimeUtil.getNow());
                platformCat.setModifier(this.getTaskName());
                platformCat.setModified(DateTimeUtil.getNow());
            }

        }
        platformCatModelList.removeAll(removePlatformCatList);

        return platformCatModelList;
    }
    /** add by lewis end 2015/11/27 */


    /**
     * 第三方平台属性信息取得（天猫系）
     * @param cartId 渠道信息
     */
    private void doSetPlatformPropTm(int cartId) throws ApiException {
        // TODO 调用梁兄借口 获取天猫国际叶子类目信息
        List<PlatformCategoriesModel> platformSubCategoryList = new ArrayList<>();
        List<CmsMtPlatformCategoryTreeModel> categoryLeaves = new ArrayList<>();
        List<CmsMtPlatformCategoryTreeModel> categoryTrees = this.platformCategoryDao.selectPlatformCategoriesByCartId(cartId);

//                platformCategoryDao.getPlatformSubCatsWithoutShop(cartId);

        // 需要等待删除的第三方平台类目信息
        List<ItemSchema> delCats = new ArrayList<>();

        int i = 1;
        int cnt = platformSubCategoryList.size();
        for (PlatformCategoriesModel platformCategoriesModel : platformSubCategoryList) {

            // 获取产品属性
            logger.info("获取产品属性:" + i + "/" + cnt + ":CHANNEL_ID:" + platformCategoriesModel.getChannelId() + ":CART_ID:" + platformCategoriesModel.getCartId() + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getPlatformCid());
            doSetPlatformPropTmSub(platformCategoriesModel, 1);

            // 获取商品属性
            logger.info("获取商品属性:" + i + "/" + cnt + ":CHANNEL_ID:" + platformCategoriesModel.getChannelId() + ":CART_ID:" + platformCategoriesModel.getCartId() + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getPlatformCid());
            delCats.add(doSetPlatformPropTmSub(platformCategoriesModel, 0));

            i++;
        }

//        // 第三方平台属性设定 ----------------------------------------- START
//        // 删除无效的（第三方平台）类目 // TODO:同时要删除主数据类目和Mapping（仅第一次初始化数据的场合）
//        platformCategoryDao.delPlatformCatsByCid(delCats);
//        // 第三方平台属性设定 ----------------------------------------- END


    }

    /**
     * 第三方平台属性信息取得（天猫系）（子函数）
     * @param platformCategoriesModel 店铺信息
     * @param isProduct 是否是产品（产品：1； 商品：0）
     */
    private ItemSchema doSetPlatformPropTmSub(PlatformCategoriesModel platformCategoriesModel, int isProduct) throws ApiException {

        // xml文件的保存路径
        String path;

        // 属性规则信息
        String xmlContent = null;

        // 获取店铺信息
        ShopBean shopProp = ShopConfigs.getShop(platformCategoriesModel.getChannelId(), String.valueOf(platformCategoriesModel.getCartId()));
        if (shopProp == null) {
            logger.error("没有获取到店铺信息 " + "channel_id:" + shopProp.getOrder_channel_id() + "  cart_id:" + shopProp.getCart_id());
        }

        if (isProduct == 1) {
            // 产品的xml保存路径
            path = Properties.readValue(Constants.PATH.PATH_PRODUCT_RULE_FILE);

            // 调用API获取产品属性规则
            xmlContent = tbCategoryService.getTbProductAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getPlatformCid()));

            if (StringUtils.isEmpty(xmlContent)) {
                return new ItemSchema();
            }
        } else {
            // 商品的xml保存路径
            path = Properties.readValue(Constants.PATH.PATH_ITEM_RULE_FILE);

            // 调用API获取产品属性规则
            ItemSchema result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getPlatformCid()), null);
            //保存为XML文件
            if (result.getResult() == 0 ){
                xmlContent = result.getItemResult();
            }else if (result.getResult() == 1 ){
//              "商品类目已被冻结, 本类目已经不能发布商品，请重新选择类目":
//              "商品类目未授权，请重新选择类目;商品类目天猫已经废弃, 本类目已经不能发布商品，请重新选择类目":
//              "商品类目天猫已经废弃, 本类目已经不能发布商品，请重新选择类目":
//                以上三种类型则表明类目已经作废，需要删除
                return result;
            }
        }

        // XML文件名字
        String strFileNameXML = platformCategoriesModel.getPlatformCid().toString() + "_" + shopProp.getCart_id() + ".xml";
        // 保存为XML文件
        if (!StringUtils.isEmpty(xmlContent)){
            XmlUtil.writeXml2File(xmlContent, strFileNameXML, path);
        }

        // 读入XML文件（TODO:暂时是读取文件, 以后调用完API后就直接可以用了，不用再保存为XML，再读取什么的了）
        // 判断是否存在
        File xmlFile = new File(path + strFileNameXML);
        if (!xmlFile.exists()) {
            return new ItemSchema();
        }
        String xmlContentRead = XmlUtil.readXml(strFileNameXML, path);

        // 分析XML
        List<PlatformPropModel> platformPropModelList = TmallPropertyParser.getTmallPropertiesByXmlContent(
                Integer.parseInt(shopProp.getCart_id()),
                platformCategoriesModel.getPlatformCid(),
                isProduct,
                xmlContentRead
        );

        // 插入到属性表中
//        imsCategorySubService.doInsertPlatformPropMain(platformPropModelList, JOB_NAME);
        CmsMtPlatformCategoryTreeModel platformCategoryMongoBean = platformCategoryDao.selectByCartIdCatId(
                platformCategoriesModel.getCartId(),
                platformCategoriesModel.getPlatformCid()
        );

        if (platformCategoryMongoBean == null) {
            platformCategoryMongoBean = new CmsMtPlatformCategoryTreeModel();

//            platformCategoryMongoBean.setChannel_id(platformCategoriesModel.getChannelId());
            platformCategoryMongoBean.setCartId(platformCategoriesModel.getCartId());
            platformCategoryMongoBean.setCatId(platformCategoriesModel.getPlatformCid());
            platformCategoryMongoBean.setCatName(platformCategoriesModel.getCidName());
            platformCategoryMongoBean.setCatPath(platformCategoriesModel.getCidPath());
            platformCategoryMongoBean.setParentCatId(platformCategoriesModel.getParentCid());
        }

        if (isProduct == 1) {
//            platformCategoryMongoBean.setPropsProduct(xmlContent);
        } else {
//            platformCategoryMongoBean.setPropsItem(xmlContent);
        }
//        platformCategoryDao.saveWithProduct(platformCategoryMongoBean);


        return new ItemSchema();
    }

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
        brandDao.delBrandsByShop(shop);
        //插入该店的所有品牌数据
        if(brands != null && brands.size() > 0){
            brandDao.insertBrands(brands, shop, JOB_NAME);
        }
    }


}
