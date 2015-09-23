package com.voyageone.batch.ims.service;

import com.taobao.api.domain.Brand;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.dao.*;
import com.voyageone.batch.ims.modelbean.*;
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
    public void doRebuidPlatformCategory(List<PlatformCategories> subCatsAll, ShopBean shop, String JOB_NAME) {
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
    public List<CategoryMappingBean> getCategoryMappingList() {

        return categoryMappingDao.selectCategoryMapping();

    }

    /**
     * 设置主数据（类目）和主数据（类目匹配）
     * @param platformCategoryList 第三方平台类目列表
     * @param categoryMappingBeanListExist 现存的类目匹配表
     * @param JOB_NAME job name
     * @return List<CategoryMappingBean> 类目匹配表
     */
    @Transactional
    public List<CategoryMappingBean> setMainDataCategory(
            List<PlatformCategories> platformCategoryList,
            List<CategoryMappingBean> categoryMappingBeanListExist,
            String JOB_NAME
    ) {
        // 待处理列表
        List<PlatformCategories> platformCategoryListTodo = platformCategoryList;
        // 类目匹配表
        List<CategoryMappingBean> categoryMappingBeanList = categoryMappingBeanListExist;
        if (categoryMappingBeanList == null) {
            categoryMappingBeanList = new ArrayList<>();
        }

        // 循环第三方平台类目列表
        while (true) {
            if (platformCategoryListTodo.size() == 0) {
                break;
            }

            int intMax = platformCategoryListTodo.size() - 1;
            for (int i = intMax; i >= 0; i--) {
                PlatformCategories platformCategories = platformCategoryListTodo.get(i);

                // 主数据（类目）
                CategoryBean categoryBean = new CategoryBean();
                // 主数据（类目匹配）
                CategoryMappingBean categoryMappingBean = new CategoryMappingBean();

                // 第三方平台父类目id
                String platformParentId = platformCategories.getParentCid();

                // 获取主数据的父类目id
                boolean blnFound = false;
                if ("0".equals(platformParentId)) {
                    // 一级类目，没有父了
                    categoryBean.setParentCid(0);
                    blnFound = true;
                } else {
                    for (CategoryMappingBean mapping : categoryMappingBeanList) {
                        if (platformParentId.equals(mapping.getPlatformCid())) {
                            // 找到了
                            // 父类目id
                            categoryBean.setParentCid(mapping.getCategoryId());

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
                categoryBean.setIsParent(platformCategories.getIsParent());
                // 类目名称
                categoryBean.setCategoryName(platformCategories.getCidName());
                // 类目path
                categoryBean.setCategoryPath(platformCategories.getCidPath());
                //            // 显示顺序
                //            categoryBean.setSortOrder();
                // 插入到数据库里
                int newCategoryId = categoryDao.insertCategory(categoryBean, JOB_NAME);

                // 主数据（类目匹配）设定
                categoryMappingBean.setCategoryId(newCategoryId);
                categoryMappingBean.setPlatformCartId(platformCategories.getCartId());
                categoryMappingBean.setPlatformCid(platformCategories.getPlatformCid());
                // 插入到数据库里
                categoryMappingDao.insertCategoryMapping(categoryMappingBean, JOB_NAME);

                // 类目匹配表
                categoryMappingBeanList.add(categoryMappingBean);

                // 成功后移除
                platformCategoryListTodo.remove(i);

            }
        }

        return categoryMappingBeanList;
    }

    /**
     * 将第三方平台属性插入第三方平台属性数据库
     *   1. 插入第三方平台属性表
     * @param platformPropBeanList 第三方平台属性列表
     * @param JOB_NAME job name
     */
    @Transactional
    public void doInsertPlatformPropMain(
            List<PlatformPropBean> platformPropBeanList,
            String JOB_NAME) {

        // 插入属性（第三方平台）
        platformPropDao.deletePlatformProp(platformPropBeanList);
        platformPropDao.insertPlatformProp(platformPropBeanList, JOB_NAME);
        // 准备属性的可选项目
        List<PlatformPropOptionBean> platformPropOptionBeanList = new ArrayList<>();
        for (PlatformPropBean platformPropBean : platformPropBeanList) {
            platformPropOptionBeanList.addAll(platformPropBean.getPlatformPropOptionBeanList());
        }
        // 插入属性可选项目（第三方平台）
        platformPropDao.deletePlatformPropOption(platformPropOptionBeanList);
        platformPropDao.insertPlatformPropOption(platformPropOptionBeanList, JOB_NAME);
        // 准备属性的规则
        List<PlatformPropRuleBean> platformPropRuleBeanList = new ArrayList<>();
        for (PlatformPropBean platformPropBean : platformPropBeanList) {
            platformPropRuleBeanList.addAll(platformPropBean.getPlatformPropRuleBeanList());
        }
        // 插入属性的规则（第三方平台）
        platformPropDao.deletePlatformPropRule(platformPropRuleBeanList);
        platformPropDao.insertPlatformPropRule(platformPropRuleBeanList, JOB_NAME);

    }

    /**
     * 将属性插入数据库
     *  插入主数据属性表 和 主数据属性关联表
     * @param platformPropBeanList 第三方平台属性列表
     * @param categoryId 主数据类目id
     * @param propExist 已经存在于主数据中的属性的（第三方平台）属性名
     * @param propHashList 已经存在的属性的hash
     * @param propOptionExist 已经存在于主数据中的属性可选项
     * @param propOptionHashList 已经存在的属性可选项的hash
     * @param JOB_NAME job name
     */
    @Transactional
    public void doInsertPropMain(
            List<PlatformPropBean> platformPropBeanList,
            int categoryId,
            Map<String, Integer> propExist,
            List<String> propHashList,
            Map<String, Integer> propOptionExist,
            List<String> propOptionHashList,
            String JOB_NAME) {

        // 待处理列表
        List<PlatformPropBean> platformPropBeanListTodo = platformPropBeanList;

        // 属性匹配
        List<PropMappingBean> propMappingBeanList = new ArrayList<>();

        // 循环第三方平台类目列表
        while (true) {
            if (platformPropBeanListTodo.size() == 0) {
                break;
            }

            int intMax = platformPropBeanListTodo.size() - 1;
            for (int i = intMax; i >= 0; i--) {
                PlatformPropBean platformPropBean = platformPropBeanListTodo.get(i);

                // 主数据（属性）
                PropBean propBean = new PropBean();
                // 主数据（属性匹配）
                PropMappingBean propMappingBean = new PropMappingBean();

                // 第三方平台父属性id
                String platformParentHash = platformPropBean.getParentPropHash();

                // 获取主数据的父属性id
                boolean blnFound = false;
                if (StringUtils.isEmpty(platformParentHash)) {
                    // 一级类目，没有父了
                    propBean.setParentPropId(0);
                    blnFound = true;
                } else {
                    for (PropMappingBean mapping : propMappingBeanList) {
                        if (platformParentHash.equals(mapping.getPlatformPropHash())) {
                            // 找到了
                            // 父属性id
                            propBean.setParentPropId(mapping.getPropId());

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

                for (PlatformPropRuleBean platformPropRuleBean : platformPropBean.getPlatformPropRuleBeanList()) {
                    // 属性的规则的关联属性
                    if (!StringUtils.isEmpty(platformPropRuleBean.getPlatformPropRuleRelationship())) {
                        List<PropRuleDependExpressBean> propRuleDependExpressBeanList =
                                JsonUtil.jsonToBeanList(
                                        platformPropRuleBean.getPlatformPropRuleRelationship(),
                                        PropRuleDependExpressBean.class
                                );

                        for (int iDepend = 0; iDepend < propRuleDependExpressBeanList.size(); iDepend++) {
                            if (!propExist.containsKey(propRuleDependExpressBeanList.get(iDepend).getExtraPropId())) {
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
                    List<PlatformPropRuleBean> platformPropRuleBeanList = platformPropBean.getPlatformPropRuleBeanList();
                    for (PlatformPropRuleBean platformPropRuleBean : platformPropRuleBeanList) {

                        // 属性的规则的名称
                        String strRuleName = platformPropRuleBean.getPlatformPropRuleName();
                        // 属性的规则的值
                        String strRuleValue = platformPropRuleBean.getPlatformPropRuleValue();

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
                boolean blnExist = propExist.containsKey(platformPropBean.getPlatformPropId());

                // 如果主数据里面已经有过了，那么就不用再插入一次了
                if (blnExist) {
                    newPropId = propExist.get(platformPropBean.getPlatformPropId());
                } else {
                    // 主数据（属性）设定
                    // 类目id（根据匹配表来找到类目id）
                    propBean.setCategoryId(categoryId);
                    // 属性名称
                    propBean.setPropName(platformPropBean.getPlatformPropName());
                    // 属性类型
                    propBean.setPropType(platformPropBean.getPlatformPropType());
                    // 属性默认值
                    propBean.setPropValueDefault(platformPropBean.getPlatformPropValueDefault());
                    // 是否是一级属性
                    propBean.setIsTopProp(platformPropBean.getIsTopProp());
                    // 是否是父属性（是否包含子属性：1：包含； 0：不包含）
                    propBean.setIsParent(platformPropBean.getIsParent());
                    // 是否必填
                    propBean.setIsRequired(intRequired);
                    // 内容（备用）
                    propBean.setContent("");

                    // 插入到数据库里
                    newPropId = propDao.insertProp(propBean, JOB_NAME);

                    propExist.put(platformPropBean.getPlatformPropId(), newPropId);
                }

                // 属性匹配设定
                propMappingBean.setPlatformPropHash(platformPropBean.getPlatformPropHash());
                propMappingBean.setPropId(newPropId);
                // 如果主数据里面已经有过了，那么就不用再插入一次了
                if (!propHashList.contains(platformPropBean.getPlatformPropHash())) {
                    // 插入到数据库里
                    propDao.insertPropMapping(propMappingBean, JOB_NAME);
                }

                // 属性匹配表
                propMappingBeanList.add(propMappingBean);

                // 成功后移除
                platformPropBeanListTodo.remove(i);

                // 如果是单选或者是多选的场合
                FieldTypeEnum propType = ImsConstants.PlatformPropType.getNameByValue(platformPropBean.getPlatformPropType());
                if (propType.compareTo(FieldTypeEnum.SINGLECHECK) == 0
                        || propType.compareTo(FieldTypeEnum.MULTICHECK) == 0) {

                    // 循环可选项
                    List<PlatformPropOptionBean> platformPropOptionBeanList = platformPropBean.getPlatformPropOptionBeanList();
                    for (PlatformPropOptionBean platformPropOptionBean : platformPropOptionBeanList) {

                        // 属性可选项（主数据）
                        PropOptionBean propOptionBean = new PropOptionBean();

                        // 属性值单独匹配（主数据）
                        PropOptionMappingBean propOptionMappingBean = new PropOptionMappingBean();

                        int newOptionId;
                        boolean blnExistOption = propOptionExist.containsKey(newPropId + ":" + platformPropOptionBean.getPlatformPropOptionValue());
                        if (blnExistOption) {
                            newOptionId = propOptionExist.get(newPropId + ":" + platformPropOptionBean.getPlatformPropOptionValue());
                        } else {
                            // 属性可选项（主数据）设定
                            propOptionBean.setPropId(newPropId);
                            propOptionBean.setPropOptionName(platformPropOptionBean.getPlatformPropOptionName());
                            propOptionBean.setPropOptionValue(platformPropOptionBean.getPlatformPropOptionValue());

                            // 插入到数据库里
                            newOptionId = propDao.insertPropOption(propOptionBean, JOB_NAME);
                        }

                        if (!propOptionHashList.contains(platformPropOptionBean.getPlatformPropOptionHash())) {
                            // 属性值单独匹配（主数据）设定
                            propOptionMappingBean.setPlatformPropOptionHash(platformPropOptionBean.getPlatformPropOptionHash());
                            propOptionMappingBean.setPropOptionId(newOptionId);

                            propDao.insertPropOptionMapping(propOptionMappingBean, JOB_NAME);
                        }
                    }

                }

                if (!blnExist) {
                    // 循环属性的规则
                    List<PlatformPropRuleBean> platformPropRuleBeanList = platformPropBean.getPlatformPropRuleBeanList();
                    for (PlatformPropRuleBean platformPropRuleBean : platformPropRuleBeanList) {
                        // 规则（主数据）
                        PropRuleBean propRuleBean = new PropRuleBean();

                        // 规则（主数据）设定
                        // 属性的id
                        propRuleBean.setPropId(newPropId);
                        // 属性的规则的名称
                        propRuleBean.setPropRuleName(platformPropRuleBean.getPlatformPropRuleName());
                        // 属性的规则的值
                        propRuleBean.setPropRuleValue(platformPropRuleBean.getPlatformPropRuleValue());
                        // 属性的规则的单位
                        propRuleBean.setPropRuleUnit(platformPropRuleBean.getPlatformPropRuleUnit());
                        // 属性的规则的url
                        propRuleBean.setPropRuleUrl(platformPropRuleBean.getPlatformPropRuleUrl());
                        // 属性的规则的是否包含
                        propRuleBean.setPropRuleExProperty(platformPropRuleBean.getPlatformPropRuleExProperty());

                        // 属性的规则的关联属性
                        propRuleBean.setPropRuleRelationshipOperator(platformPropRuleBean.getPlatformPropRuleRelationshipOperator());
                        if (!StringUtils.isEmpty(platformPropRuleBean.getPlatformPropRuleRelationship())) {

                            List<PropRuleDependExpressBean> propRuleDependExpressBeanList =
                                    JsonUtil.jsonToBeanList(
                                            platformPropRuleBean.getPlatformPropRuleRelationship(),
                                            PropRuleDependExpressBean.class
                                    );

                            for (int iDepend = 0; iDepend < propRuleDependExpressBeanList.size(); iDepend++) {
                                int intMainDataPropId = propExist.get(propRuleDependExpressBeanList.get(iDepend).getExtraPropId());
                                propRuleDependExpressBeanList.get(iDepend).setExtraPropId(String.valueOf(intMainDataPropId));
                            }

                            String strPlatformPropRuleRelationship = JsonUtil.getJsonString(propRuleDependExpressBeanList);

                            if (strPlatformPropRuleRelationship.length() > 500) {
                                // 超出数据库承受能力，此关联规则无视之
                                propRuleBean.setPropRuleRelationshipOperator(null);
                                propRuleBean.setPropRuleRelationship(null);

                            } else {
                                propRuleBean.setPropRuleRelationship(strPlatformPropRuleRelationship);
                            }

                        }

                        // 插入到数据库里
                        propDao.insertPropRule(propRuleBean, JOB_NAME);

                    }

                }

            }

        }

    }


}
