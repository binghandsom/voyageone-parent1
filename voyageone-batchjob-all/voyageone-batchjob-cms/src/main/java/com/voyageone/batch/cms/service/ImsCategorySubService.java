package com.voyageone.batch.cms.service;

import com.taobao.api.domain.Brand;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.voyageone.batch.cms.dao.*;
import com.voyageone.batch.cms.CmsConstants;
import com.voyageone.batch.cms.model.*;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ImsCategorySubService {

    private static Log logger = LogFactory.getLog(ImsCategoryService.class);

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryMappingDao categoryMappingDao;
    @Autowired
    PlatformCategoryDao platformCategoryDao;
    @Autowired
    PlatformPropDao platformPropDao;
    @Autowired
    PropPropertiesDao propPropertiesDao;
    @Autowired
    PropDao propDao;
    @Autowired
    BrandDao brandDao;

    /**
     * 重建第三方平台指定店铺类目信息
     * @param subCatsAll 类目信息
     * @param shop shop
     * @param JOB_NAME job name
     */
    @Transactional
    public void doRebuidPlatformCategory(List<PlatformCategoriesModel> subCatsAll, ShopBean shop, String JOB_NAME) {
        // 删除该店的所有类目数据
        platformCategoryDao.delPlatformCatsByShop(shop);
        // 插入 父子类目
        if(subCatsAll.size() > 0){
            platformCategoryDao.insertPlatformCats(subCatsAll, shop, JOB_NAME);
        }
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



    /**
     * 获取所有主数据中的类目匹配关系
     * @return 所有主数据中的类目匹配关系
     */
    public List<CategoryMappingModel> getCategoryMappingList() {

        return categoryMappingDao.selectCategoryMapping();

    }

    /**
     * 设置主数据（类目）和主数据（类目匹配）
     * @param platformCategoryList 第三方平台类目列表
     * @param categoryMappingModelListExist 现存的类目匹配表
     * @param JOB_NAME job name
     * @return List<CategoryMappingModel> 类目匹配表
     */
    @Transactional
    public List<CategoryMappingModel> setMainDataCategory(
            List<PlatformCategoriesModel> platformCategoryList,
            List<CategoryMappingModel> categoryMappingModelListExist,
            String JOB_NAME
    ) {
        // 待处理列表
        List<PlatformCategoriesModel> platformCategoryListTodo = platformCategoryList;
        // 类目匹配表
        List<CategoryMappingModel> categoryMappingModelList = categoryMappingModelListExist;
        if (categoryMappingModelList == null) {
            categoryMappingModelList = new ArrayList<>();
        }

        // 循环第三方平台类目列表
        while (true) {
            if (platformCategoryListTodo.size() == 0) {
                break;
            }

            int intMax = platformCategoryListTodo.size() - 1;
            for (int i = intMax; i >= 0; i--) {
                PlatformCategoriesModel platformCategoriesModel = platformCategoryListTodo.get(i);

                // 主数据（类目）
                CategoryModel categoryModel = new CategoryModel();
                // 主数据（类目匹配）
                CategoryMappingModel categoryMappingModel = new CategoryMappingModel();

                // 第三方平台父类目id
                String platformParentId = platformCategoriesModel.getParentCid();

                // 获取主数据的父类目id
                boolean blnFound = false;
                if ("0".equals(platformParentId)) {
                    // 一级类目，没有父了
                    categoryModel.setParentCid(0);
                    blnFound = true;
                } else {
                    for (CategoryMappingModel mapping : categoryMappingModelList) {
                        if (platformParentId.equals(mapping.getPlatformCid())) {
                            // 找到了
                            // 父类目id
                            categoryModel.setParentCid(mapping.getCategoryId());

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

                // 主数据（类目）设定
                // 是否是父类目（是否包含子类目：1：包含； 0：不包含）
                categoryModel.setIsParent(platformCategoriesModel.getIsParent());
                // 类目名称
                categoryModel.setCategoryName(platformCategoriesModel.getCidName());
                // 类目path
                categoryModel.setCategoryPath(platformCategoriesModel.getCidPath());
                //            // 显示顺序
                //            categoryModel.setSortOrder();
                // 插入到数据库里
                int newCategoryId = categoryDao.insertCategory(categoryModel, JOB_NAME);

                // 主数据（类目匹配）设定
                categoryMappingModel.setCategoryId(newCategoryId);
                categoryMappingModel.setPlatformCartId(platformCategoriesModel.getCartId());
                categoryMappingModel.setPlatformCid(platformCategoriesModel.getPlatformCid());
                // 插入到数据库里
                categoryMappingDao.insertCategoryMapping(categoryMappingModel, JOB_NAME);

                // 类目匹配表
                categoryMappingModelList.add(categoryMappingModel);

                // 成功后移除
                platformCategoryListTodo.remove(i);

            }
        }

        return categoryMappingModelList;
    }

    /**
     * 将第三方平台属性插入第三方平台属性数据库
     *   1. 插入第三方平台属性表
     * @param platformPropModelList 第三方平台属性列表
     * @param JOB_NAME job name
     */
    @Transactional
    public void doInsertPlatformPropMain(
            List<PlatformPropModel> platformPropModelList,
            String JOB_NAME) {

        // 插入属性（第三方平台）
        platformPropDao.deletePlatformProp(platformPropModelList);
        platformPropDao.insertPlatformProp(platformPropModelList, JOB_NAME);
        // 准备属性的可选项目
        List<PlatformPropOptionModel> platformPropOptionModelList = new ArrayList<>();
        for (PlatformPropModel platformPropModel : platformPropModelList) {
            platformPropOptionModelList.addAll(platformPropModel.getPlatformPropOptionModelList());
        }
        // 插入属性可选项目（第三方平台）
        platformPropDao.deletePlatformPropOption(platformPropOptionModelList);
        platformPropDao.insertPlatformPropOption(platformPropOptionModelList, JOB_NAME);
        // 准备属性的规则
        List<PlatformPropRuleModel> platformPropRuleModelList = new ArrayList<>();
        for (PlatformPropModel platformPropModel : platformPropModelList) {
            platformPropRuleModelList.addAll(platformPropModel.getPlatformPropRuleModelList());
        }
        // 插入属性的规则（第三方平台）
        platformPropDao.deletePlatformPropRule(platformPropRuleModelList);
        platformPropDao.insertPlatformPropRule(platformPropRuleModelList, JOB_NAME);

    }

    /**
     * 将属性插入数据库
     *  插入主数据属性表 和 主数据属性关联表
     * @param platformPropModelList 第三方平台属性列表
     * @param categoryId 主数据类目id
     * @param propExist 已经存在于主数据中的属性的（第三方平台）属性名
     * @param propHashList 已经存在的属性的hash
     * @param propOptionExist 已经存在于主数据中的属性可选项
     * @param propOptionHashList 已经存在的属性可选项的hash
     * @param JOB_NAME job name
     */
    @Transactional
    public void doInsertPropMain(
            List<PlatformPropModel> platformPropModelList,
            int categoryId,
            Map<String, Integer> propExist,
            List<String> propHashList,
            Map<String, Integer> propOptionExist,
            List<String> propOptionHashList,
            String JOB_NAME) {

        // 待处理列表
        List<PlatformPropModel> platformPropModelListTodo = platformPropModelList;

        // 属性匹配
        List<PropMappingModel> propMappingModelList = new ArrayList<>();

        // 循环第三方平台类目列表
        while (true) {
            if (platformPropModelListTodo.size() == 0) {
                break;
            }

            int intMax = platformPropModelListTodo.size() - 1;
            for (int i = intMax; i >= 0; i--) {
                PlatformPropModel platformPropModel = platformPropModelListTodo.get(i);

                // 主数据（属性）
                PropertyModel propertyModel = new PropertyModel();
                // 主数据（属性匹配）
                PropMappingModel propMappingModel = new PropMappingModel();

                // 第三方平台父属性id
                String platformParentHash = platformPropModel.getParentPropHash();

                // 获取主数据的父属性id
                boolean blnFound = false;
                if (StringUtils.isEmpty(platformParentHash)) {
                    // 一级类目，没有父了
                    propertyModel.setParentPropId(0);
                    blnFound = true;
                } else {
                    for (PropMappingModel mapping : propMappingModelList) {
                        if (platformParentHash.equals(mapping.getPlatformPropHash())) {
                            // 找到了
                            // 父属性id
                            propertyModel.setParentPropId(mapping.getPropId());

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

                // 规则预检
                boolean blnNotFoundDependExpress = false;

                for (PlatformPropRuleModel platformPropRuleModel : platformPropModel.getPlatformPropRuleModelList()) {
                    // 属性的规则的关联属性
                    if (!StringUtils.isEmpty(platformPropRuleModel.getPlatformPropRuleRelationship())) {
                        List<PropRuleDependExpressModel> propRuleDependExpressModelList =
                                JsonUtil.jsonToBeanList(
                                        platformPropRuleModel.getPlatformPropRuleRelationship(),
                                        PropRuleDependExpressModel.class
                                );

                        for (int iDepend = 0; iDepend < propRuleDependExpressModelList.size(); iDepend++) {
                            if (!propExist.containsKey(propRuleDependExpressModelList.get(iDepend).getExtraPropId())) {
                                blnNotFoundDependExpress = true;
                                break;
                            }
                        }

                        if (blnNotFoundDependExpress) {
                            break;
                        }

                    }

                }
                if (blnNotFoundDependExpress) {
                    continue;
                }

                // 属性预处理 之 是否必填
                boolean isRequired = false;
                int intRequired = 0;
                boolean isDisable = false;
                {
                    List<PlatformPropRuleModel> platformPropRuleModelList = platformPropModel.getPlatformPropRuleModelList();
                    for (PlatformPropRuleModel platformPropRuleModel : platformPropRuleModelList) {

                        // 属性的规则的名称
                        String strRuleName = platformPropRuleModel.getPlatformPropRuleName();
                        // 属性的规则的值
                        String strRuleValue = platformPropRuleModel.getPlatformPropRuleValue();

                        if ("REQUIREDRULE".equals(strRuleName.toUpperCase())) {
                            if ("TRUE".equals(strRuleValue.toUpperCase())) {
                                isRequired = true;
                            }
                        }
                        if ("DISABLERULE".equals(strRuleName.toUpperCase())) {
                            if ("TRUE".equals(strRuleValue.toUpperCase())) {
                                isDisable = true;
                            }
                        }

                    }
                }

                if (isRequired && !isDisable) {
                    intRequired = 1;
                }

                // 主数据的属性的id
                int newPropId;
                boolean blnExist = propExist.containsKey(platformPropModel.getPlatformPropId());

                // 如果主数据里面已经有过了，那么就不用再插入一次了
                if (blnExist) {
                    newPropId = propExist.get(platformPropModel.getPlatformPropId());
                } else {
                    // 主数据（属性）设定
                    // 类目id（根据匹配表来找到类目id）
                    propertyModel.setCategoryId(categoryId);
                    // 属性名称
                    propertyModel.setPropName(platformPropModel.getPlatformPropName());
                    // 属性类型
                    propertyModel.setPropType(platformPropModel.getPlatformPropType());
                    // 属性默认值
                    propertyModel.setPropValueDefault(platformPropModel.getPlatformPropValueDefault());
                    // 是否是一级属性
                    propertyModel.setIsTopProp(platformPropModel.getIsTopProp());
                    // 是否是父属性（是否包含子属性：1：包含； 0：不包含）
                    propertyModel.setIsParent(platformPropModel.getIsParent());
                    // 是否必填
                    propertyModel.setIsRequired(intRequired);
                    // 内容（备用）
                    propertyModel.setContent("");

                    // 插入到数据库里
                    newPropId = propDao.insertProp(propertyModel, JOB_NAME);

                    propExist.put(platformPropModel.getPlatformPropId(), newPropId);
                }

                // 属性匹配设定
                propMappingModel.setPlatformPropHash(platformPropModel.getPlatformPropHash());
                propMappingModel.setPropId(newPropId);
                // 如果主数据里面已经有过了，那么就不用再插入一次了
                if (!propHashList.contains(platformPropModel.getPlatformPropHash())) {
                    // 插入到数据库里
                    propDao.insertPropMapping(propMappingModel, JOB_NAME);
                }

                // 属性匹配表
                propMappingModelList.add(propMappingModel);

                // 成功后移除
                platformPropModelListTodo.remove(i);

                // 如果是单选或者是多选的场合
                FieldTypeEnum propType = CmsConstants.PlatformPropType.getNameByValue(platformPropModel.getPlatformPropType());
                if (propType.compareTo(FieldTypeEnum.SINGLECHECK) == 0
                        || propType.compareTo(FieldTypeEnum.MULTICHECK) == 0) {

                    // 循环可选项
                    List<PlatformPropOptionModel> platformPropOptionModelList = platformPropModel.getPlatformPropOptionModelList();
                    for (PlatformPropOptionModel platformPropOptionModel : platformPropOptionModelList) {

                        // 属性可选项（主数据）
                        PropOptionModel propOptionModel = new PropOptionModel();

                        // 属性值单独匹配（主数据）
                        PropOptionMappingBean propOptionMappingBean = new PropOptionMappingBean();

                        int newOptionId;
                        boolean blnExistOption = propOptionExist.containsKey(newPropId + ":" + platformPropOptionModel.getPlatformPropOptionValue());
                        if (blnExistOption) {
                            newOptionId = propOptionExist.get(newPropId + ":" + platformPropOptionModel.getPlatformPropOptionValue());
                        } else {
                            // 属性可选项（主数据）设定
                            propOptionModel.setPropId(newPropId);
                            propOptionModel.setPropOptionName(platformPropOptionModel.getPlatformPropOptionName());
                            propOptionModel.setPropOptionValue(platformPropOptionModel.getPlatformPropOptionValue());

                            // 插入到数据库里
                            newOptionId = propDao.insertPropOption(propOptionModel, JOB_NAME);
                        }

                        if (!propOptionHashList.contains(platformPropOptionModel.getPlatformPropOptionHash())) {
                            // 属性值单独匹配（主数据）设定
                            propOptionMappingBean.setPlatformPropOptionHash(platformPropOptionModel.getPlatformPropOptionHash());
                            propOptionMappingBean.setPropOptionId(newOptionId);

                            propDao.insertPropOptionMapping(propOptionMappingBean, JOB_NAME);
                        }
                    }

                }

                if (!blnExist) {
                    // 循环属性的规则
                    List<PlatformPropRuleModel> platformPropRuleModelList = platformPropModel.getPlatformPropRuleModelList();
                    for (PlatformPropRuleModel platformPropRuleModel : platformPropRuleModelList) {
                        // 规则（主数据）
                        PropRuleModel propRuleModel = new PropRuleModel();

                        // 规则（主数据）设定
                        // 属性的id
                        propRuleModel.setPropId(newPropId);
                        // 属性的规则的名称
                        propRuleModel.setPropRuleName(platformPropRuleModel.getPlatformPropRuleName());
                        // 属性的规则的值
                        propRuleModel.setPropRuleValue(platformPropRuleModel.getPlatformPropRuleValue());
                        // 属性的规则的单位
                        propRuleModel.setPropRuleUnit(platformPropRuleModel.getPlatformPropRuleUnit());
                        // 属性的规则的url
                        propRuleModel.setPropRuleUrl(platformPropRuleModel.getPlatformPropRuleUrl());
                        // 属性的规则的是否包含
                        propRuleModel.setPropRuleExProperty(platformPropRuleModel.getPlatformPropRuleExProperty());

                        // 属性的规则的关联属性
                        propRuleModel.setPropRuleRelationshipOperator(platformPropRuleModel.getPlatformPropRuleRelationshipOperator());
                        if (!StringUtils.isEmpty(platformPropRuleModel.getPlatformPropRuleRelationship())) {

                            List<PropRuleDependExpressModel> propRuleDependExpressModelList =
                                    JsonUtil.jsonToBeanList(
                                            platformPropRuleModel.getPlatformPropRuleRelationship(),
                                            PropRuleDependExpressModel.class
                                    );

                            for (int iDepend = 0; iDepend < propRuleDependExpressModelList.size(); iDepend++) {
                                int intMainDataPropId = propExist.get(propRuleDependExpressModelList.get(iDepend).getExtraPropId());
                                propRuleDependExpressModelList.get(iDepend).setExtraPropId(String.valueOf(intMainDataPropId));
                            }

                            String strPlatformPropRuleRelationship = JsonUtil.getJsonString(propRuleDependExpressModelList);

                            if (strPlatformPropRuleRelationship.length() > 500) {
                                // 超出数据库承受能力，此关联规则无视之
                                propRuleModel.setPropRuleRelationshipOperator(null);
                                propRuleModel.setPropRuleRelationship(null);

                            } else {
                                propRuleModel.setPropRuleRelationship(strPlatformPropRuleRelationship);
                            }

                        }

                        // 插入到数据库里
                        propDao.insertPropRule(propRuleModel, JOB_NAME);

                    }

                }

            }

        }

    }


}
