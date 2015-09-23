package com.voyageone.batch.ims.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.ims.dao.BrandDao;
import com.voyageone.batch.ims.dao.DarwinBrandMappingDao;
import com.voyageone.batch.ims.dao.PlatformCategoryDao;
import com.voyageone.batch.ims.dao.PropDao;
import com.voyageone.batch.ims.modelbean.CategoryMappingBean;
import com.voyageone.batch.ims.modelbean.PlatformCategories;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbCategoryService;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.XmlUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

@Service
public class ImsCategoryInitService extends BaseTaskService{

    private final static String JOB_NAME = "imsCategoryInitJob";
    private static Log logger = LogFactory.getLog(ImsCategoryInitService.class);

    @Autowired
    BrandDao brandDao;
    @Autowired
    PlatformCategoryDao platformCategoryDao;
    @Autowired
    TbCategoryService tbCategoryService;
    @Autowired
    DarwinBrandMappingDao darwinBrandMappingDao;
    @Autowired
    ImsCategorySubService imsCategorySubService;
    @Autowired
    PropDao propDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.IMS;
    }

    @Override
    public String getTaskName() {
        return "imsCategoryInitJob";
    }

    @Transactional
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception{

        // 获取天猫系所有店铺
        List<ShopBean> shopList = ShopConfigs.getShopListByPlatform(PlatFormEnums.PlatForm.TM);
        // 获取该任务可以运行的渠道
        List<String> cartIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.cart_id);
// TODO:本功能暂时不提供，之后也许可能需要 START
//        // 获取该任务参照源类目ID（第三方平台的类目ID）
//        List<String> platformCategoryIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.platform_category_id);
// TODO:本功能暂时不提供，之后也许可能需要 START

        // 获取所有主数据中的类目匹配关系（用于过滤掉已经存在的类目）
        List<CategoryMappingBean> categoryMappingBeanListExist = imsCategorySubService.getCategoryMappingList();

        for (String cartId : cartIdList) {
            // 主数据类目设定 -------------------------------------------- START
            // 获取（指定渠道）所有（第三方平台）类目信息
            List<PlatformCategories> platformCategoryList = platformCategoryDao.getPlatformCatsWithoutShop(cartId);

            // 换一种匹配方式
            Map<String, String> mapCategoryMappingExist = new HashMap<>();
            for (CategoryMappingBean categoryMappingBean : categoryMappingBeanListExist) {
                mapCategoryMappingExist.put(categoryMappingBean.getPlatformCid(), categoryMappingBean.getPlatformCid());
            }

            // 已经存在了的话，那就从数组中删掉吧（意思就是不用再添加到主数据里去了）
            int intMax = platformCategoryList.size() - 1;
            for (int i = intMax; i >= 0; i--) {
                PlatformCategories platformCategories = platformCategoryList.get(i);

                // 以 渠道:第三方平台类目ID 为主键
                if (mapCategoryMappingExist.containsKey(platformCategories.getPlatformCid())) {
                    platformCategoryList.remove(i);
                }
            }

            // 设置主数据（类目）和主数据（类目匹配）
            logger.info("设置主数据（类目）和主数据（类目匹配）:START");
            categoryMappingBeanListExist = imsCategorySubService.setMainDataCategory(
                    platformCategoryList,
                    categoryMappingBeanListExist,
                    JOB_NAME
            );
            logger.info("设置主数据（类目）和主数据（类目匹配）:END");
            // 主数据类目设定 -------------------------------------------- END

            // 第三方平台属性设定 ----------------------------------------- START
            // 获取（指定渠道）所有（第三方平台）叶子类目信息
            List<PlatformCategories> platformSubCategoryList = platformCategoryDao.getPlatformSubCatsWithoutShop(cartId);

            // 换一种匹配方式（第三方平台类目ID：主数据类目ID）
            Map<String, Integer> mapCategoryMapping = new HashMap<>();
            for (CategoryMappingBean categoryMappingBean : categoryMappingBeanListExist) {
                mapCategoryMapping.put(categoryMappingBean.getPlatformCid(), categoryMappingBean.getCategoryId());
            }

            // 操作对象：属性
            List<PlatformPropBean> platformPropBeanList;
            // 循环（指定渠道）所有（第三方平台）叶子类目
            for (PlatformCategories platformCategories : platformSubCategoryList) {
                // 主数据类目id
                int categoryId = mapCategoryMapping.get(platformCategories.getPlatformCid());

                // 根据主数据类目id， 获取该类目在主数据属性表中，已经保存过的那些属性的（第三方平台的）属性id列表
                List<HashMap> propExistResult = propDao.selectPlatformPropIdListByCategoryId(categoryId);
                Map<String, Integer> propExist = new HashMap<>();
                List<String> propHashList = new ArrayList<>();
                for (HashMap<String, Object> map : propExistResult) {
                    propExist.put(map.get("platformPropId").toString(), Integer.parseInt(map.get("propId").toString()));
                    propHashList.add(map.get("platformPropHash").toString());
                }

                // 根据主数据类目id， 获取该类目在主数据属性表中，已经保存过的那些属性的（第三方平台的）属性id列表
                List<HashMap> propOptionExistResult = propDao.selectPropOptionListByCategoryId(categoryId);
                Map<String, Integer> propOptionExist = new HashMap<>();
                List<String> propOptionHashList = new ArrayList<>();
                for (HashMap<String, Object> map : propOptionExistResult) {
                    propOptionExist.put(map.get("optionKey").toString(), Integer.parseInt(map.get("optionId").toString()));
                    propOptionHashList.add(map.get("platformPropOptionHash").toString());
                }

                // 获取（指定渠道）所有（第三方平台）指定类目的属性信息（包括属性可选项， 包括属性规则）
                // 读取属性：产品
                platformPropBeanList = doReadPlatformPropFromXmlFile(cartId, platformCategories.getPlatformCid(), 1);
                if (platformPropBeanList != null && platformPropBeanList.size() > 0) {
                    // 插入到属性表中
                    imsCategorySubService.doInsertPropMain(platformPropBeanList, categoryId, propExist, propHashList, propOptionExist, propOptionHashList, JOB_NAME);
                }

                // 读取属性：商品
                platformPropBeanList = doReadPlatformPropFromXmlFile(cartId, platformCategories.getPlatformCid(), 0);
                if (platformPropBeanList != null && platformPropBeanList.size() > 0) {
                    // 插入到属性表中
                    imsCategorySubService.doInsertPropMain(platformPropBeanList, categoryId, propExist, propHashList, propOptionExist, propOptionHashList, JOB_NAME);
                }
            }

            // 第三方平台属性设定 ----------------------------------------- END

        }

        //正常结束
        logger.info("正常结束");
//        return TaskControlEnums.Status.SUCCESS.getIs();
    }

    /**
     * 从XML文件中读取第三方平台类目属性
     * @param cartId 渠道
     * @param platformCategoryId 第三方平台类目ID
     * @param isProduct 是否是产品（产品：1； 商品：0）
     */
    private List<PlatformPropBean> doReadPlatformPropFromXmlFile(String cartId, String platformCategoryId, int isProduct) {

        // xml文件的保存路径
        String path;

        if (isProduct == 1) {
            // 产品的xml保存路径
            path = Properties.readValue(Constants.PATH.PATH_PRODUCT_RULE_FILE);

        } else {
            // 商品的xml保存路径
            path = Properties.readValue(Constants.PATH.PATH_ITEM_RULE_FILE);
        }

        // XML文件名字
        String strFileNameXML = platformCategoryId + "_" + cartId + ".xml";

        // 判断是否存在
        File xmlFile = new File(path + strFileNameXML);
        if (!xmlFile.exists()) {
            return null;
        }
        String xmlContentRead = XmlUtil.readXml(strFileNameXML, path);

        // 分析XML
        List<PlatformPropBean> platformPropBeanList = TmallPropertyParser.getTmallPropertiesByXmlContent(
                Integer.parseInt(cartId),
                platformCategoryId,
                isProduct,
                xmlContentRead
        );

        return platformPropBeanList;

    }


}
