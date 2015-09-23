package com.voyageone.batch.ims.service;

import com.taobao.top.schema.depend.DependExpress;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.*;
import com.taobao.top.schema.label.Label;
import com.taobao.top.schema.label.LabelGroup;
import com.taobao.top.schema.option.Option;
import com.taobao.top.schema.rule.*;
import com.voyageone.batch.ims.ImsConstants;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.batch.ims.modelbean.PlatformPropOptionBean;
import com.voyageone.batch.ims.modelbean.PlatformPropRuleBean;
import com.voyageone.batch.ims.modelbean.PropRuleDependExpressBean;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.MD5;
import com.voyageone.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TmallPropertyParser {

    /**
     * 将输入的XML内容，转化为我们自己定义的（便于插入到数据库的）结构
     * @param cartId 第三方平台cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品（产品：1； 商品：0）
     * @param strXml 输入的XML内容
     * @return List<PlatformPropBean>
     */
    public static List<PlatformPropBean> getTmallPropertiesByXmlContent(
            int cartId,
            String cid,
            int isProduct,
            String strXml
    ) {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        // 读入的属性列表
        List<Field> fieldList = null;
        try {
            fieldList = SchemaReader.readXmlForList(strXml);
        } catch (TopSchemaException e) {
            e.printStackTrace();
            return result;
        }

        // 分析每个属性的具体内容
        if (fieldList != null) {
            for (Field field : fieldList) {
                result.addAll(doGetField(cartId, cid, isProduct, field, 1, "", ImsConstants.C_PROP_PATH_SPLIT_CHAR));
            }
        }

        return result;
    }

    /**
     * 取得属性的基本入口
     * @param cartId 第三方平台cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品
     * @param field 传进来的属性
     * @param isTopProp 是否是顶层属性
     * @param parentHash 父属性hash
     * @param path path
     * @return List<PlatformPropBean>
     */
    private static List<PlatformPropBean> doGetField(
            int cartId,
            String cid,
            int isProduct,
            Field field,
            int isTopProp,
            String parentHash,
            String path)
    {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        if (field.getType().compareTo(FieldTypeEnum.LABEL) == 0) {
            result.addAll(doGetField_Label(cartId, cid, isProduct, (LabelField) field, isTopProp, parentHash, path));
        } else if (field.getType().compareTo(FieldTypeEnum.INPUT) == 0) {
            result.addAll(doGetField_Input(cartId, cid, isProduct, (InputField) field, isTopProp, parentHash, path));
        } else if (field.getType().compareTo(FieldTypeEnum.COMPLEX) == 0) {
            result.addAll(doGetField_Complex(cartId, cid, isProduct, (ComplexField) field, isTopProp, parentHash, path));
        } else if (field.getType().compareTo(FieldTypeEnum.SINGLECHECK) == 0) {
            result.addAll(doGetField_SingleCheck(cartId, cid, isProduct, (SingleCheckField) field, isTopProp, parentHash, path));
        } else if (field.getType().compareTo(FieldTypeEnum.MULTICHECK) == 0) {
            result.addAll(doGetField_MultiCheck(cartId, cid, isProduct, (MultiCheckField) field, isTopProp, parentHash, path));
        } else if (field.getType().compareTo(FieldTypeEnum.MULTICOMPLEX) == 0) {
            result.addAll(doGetField_MultiComplex(cartId, cid, isProduct, (MultiComplexField) field, isTopProp, parentHash, path));
        } else {
            // 应该是没有这种情况的，如果有，那就说明有问题了。
        }

        return result;
    }

    /**
     * 取得属性 - 显示用的文字列
     * @param cartId 第三方平台 cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品
     * @param field 传进来的属性
     * @param isTopProp 是否是顶层属性
     * @param parentHash 父属性hash
     * @param path path
     * @return List<PlatformPropBean>
     */
    private static List<PlatformPropBean> doGetField_Label(
            int cartId,
            String cid,
            int isProduct,
            LabelField field,
            int isTopProp,
            String parentHash,
            String path)
    {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        // 属性信息
        for (LabelGroup labelGroup : field.getLabelGroup().getLabelGroupList()) {
            String propName = labelGroup.getName();

            for (Label label : labelGroup.getLabelList()) {
                PlatformPropBean platformPropBean = new PlatformPropBean();

                // 第三方平台cart id
                platformPropBean.setPlatformCartId(cartId);
                // 第三方平台的类目id
                platformPropBean.setPlatformCid(cid);
                // 第三方平台的属性id
                platformPropBean.setPlatformPropId(propName);
                // 第三方平台的属性路径
                platformPropBean.setPlatformPropPath(path);
                // 第三方平台的属性名称
                platformPropBean.setPlatformPropName(label.getName());
                // 第三方平台的属性类型
                platformPropBean.setPlatformPropType(ImsConstants.PlatformPropType.getValueByName(FieldTypeEnum.LABEL));
                // 第三方平台的属性默认值
                platformPropBean.setPlatformPropValueDefault("");
                // 第三方平台的属性的父属性id
                platformPropBean.setParentPropHash(parentHash);
                // 是否是顶层属性
                platformPropBean.setIsTopProp(isTopProp);
                // 是否是父亲（是否包含子属性）
                platformPropBean.setIsParent(0);
                // 是否是产品（产品：1； 商品：0）
                platformPropBean.setIsProduct(isProduct);
                // 预留
                platformPropBean.setContent("");
                // 第三方平台类目的属性的hash
                platformPropBean.setPlatformPropHash(getPropHash(platformPropBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

                result.add(platformPropBean);
            }

        }


        return result;
    }

    /**
     * 取得属性 - 输入框
     * @param cartId 第三方平台cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品
     * @param field 传进来的属性
     * @param isTopProp 是否是顶层属性
     * @param parentHash 父属性hash
     * @param path path
     * @return List<PlatformPropBean>
     */
    private static List<PlatformPropBean> doGetField_Input(
            int cartId,
            String cid,
            int isProduct,
            InputField field,
            int isTopProp,
            String parentHash,
            String path)
    {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        PlatformPropBean platformPropBean = new PlatformPropBean();

        // 第三方平台id
        platformPropBean.setPlatformCartId(cartId);
        // 第三方平台的类目id
        platformPropBean.setPlatformCid(cid);
        // 第三方平台的属性id
        platformPropBean.setPlatformPropId(field.getId());
        // 第三方平台的属性路径
        platformPropBean.setPlatformPropPath(path);
        // 第三方平台的属性名称
        platformPropBean.setPlatformPropName(field.getName());
        // 第三方平台的属性类型
        platformPropBean.setPlatformPropType(ImsConstants.PlatformPropType.getValueByName(FieldTypeEnum.INPUT));
        // 第三方平台的属性默认值
        platformPropBean.setPlatformPropValueDefault(field.getDefaultValue());
        // 第三方平台的属性的父属性id
        platformPropBean.setParentPropHash(parentHash);
        // 是否是顶层属性
        platformPropBean.setIsTopProp(isTopProp);
        // 是否是父亲（是否包含子属性）
        platformPropBean.setIsParent(0);
        // 是否是产品（产品：1； 商品：0）
        platformPropBean.setIsProduct(isProduct);
        // 预留
        platformPropBean.setContent("");
        // 第三方平台类目的属性的hash
        platformPropBean.setPlatformPropHash(getPropHash(platformPropBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

        // 规则
        platformPropBean.setPlatformPropRuleBeanList(getRule(field, platformPropBean.getPlatformPropHash()));

        result.add(platformPropBean);

        return result;
    }

    /**
     * 取得属性 - 带有子属性的属性
     * @param cartId 第三方平台cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品
     * @param field 传进来的属性
     * @param isTopProp 是否是顶层属性
     * @param parentHash 父属性Hash
     * @param path path
     * @return List<PlatformPropBean>
     */
    private static List<PlatformPropBean> doGetField_Complex(
            int cartId,
            String cid,
            int isProduct,
            ComplexField field,
            int isTopProp,
            String parentHash,
            String path)
    {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        PlatformPropBean platformPropBean = new PlatformPropBean();

        // 第三方平台id
        platformPropBean.setPlatformCartId(cartId);
        // 第三方平台的类目id
        platformPropBean.setPlatformCid(cid);
        // 第三方平台的属性id
        platformPropBean.setPlatformPropId(field.getId());
        // 第三方平台的属性路径
        platformPropBean.setPlatformPropPath(path);
        // 第三方平台的属性名称
        platformPropBean.setPlatformPropName(field.getName());
        // 第三方平台的属性类型
        platformPropBean.setPlatformPropType(ImsConstants.PlatformPropType.getValueByName(FieldTypeEnum.COMPLEX));
        // 第三方平台的属性默认值
        platformPropBean.setPlatformPropValueDefault("");
        // 第三方平台的属性的父属性id
        platformPropBean.setParentPropHash(parentHash);
        // 是否是顶层属性
        platformPropBean.setIsTopProp(isTopProp);
        // 是否是父亲（是否包含子属性）
        platformPropBean.setIsParent(0);
        // 是否是产品（产品：1； 商品：0）
        platformPropBean.setIsProduct(isProduct);
        // 预留
        platformPropBean.setContent("");
        // 第三方平台类目的属性的hash
        platformPropBean.setPlatformPropHash(getPropHash(platformPropBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

        // 规则
        platformPropBean.setPlatformPropRuleBeanList(getRule(field, platformPropBean.getPlatformPropHash()));

        result.add(platformPropBean);

        // 设置子属性
        String newPath = path + field.getId() + ImsConstants.C_PROP_PATH_SPLIT_CHAR;
        for (Field fieldSub : field.getFieldList()) {
            result.addAll(doGetField(cartId, cid, isProduct, fieldSub, 0, platformPropBean.getPlatformPropHash(), newPath));
        }

        return result;
    }

    /**
     * 取得属性 - 单选
     * @param cartId 第三方平台cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品
     * @param field 传进来的属性
     * @param isTopProp 是否是顶层属性
     * @param parentHash 父属性hash
     * @param path path
     * @return List<PlatformPropBean>
     */
    private static List<PlatformPropBean> doGetField_SingleCheck(
            int cartId,
            String cid,
            int isProduct,
            SingleCheckField field,
            int isTopProp,
            String parentHash,
            String path)
    {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        PlatformPropBean platformPropBean = new PlatformPropBean();

        // 第三方平台id
        platformPropBean.setPlatformCartId(cartId);
        // 第三方平台的类目id
        platformPropBean.setPlatformCid(cid);
        // 第三方平台的属性id
        platformPropBean.setPlatformPropId(field.getId());
        // 第三方平台的属性路径
        platformPropBean.setPlatformPropPath(path);
        // 第三方平台的属性名称
        platformPropBean.setPlatformPropName(field.getName());
        // 第三方平台的属性类型
        platformPropBean.setPlatformPropType(ImsConstants.PlatformPropType.getValueByName(FieldTypeEnum.SINGLECHECK));
        // 第三方平台的属性默认值
        platformPropBean.setPlatformPropValueDefault(field.getDefaultValue());
        // 第三方平台的属性的父属性id
        platformPropBean.setParentPropHash(parentHash);
        // 是否是顶层属性
        platformPropBean.setIsTopProp(isTopProp);
        // 是否是父亲（是否包含子属性）
        platformPropBean.setIsParent(0);
        // 是否是产品（产品：1； 商品：0）
        platformPropBean.setIsProduct(isProduct);
        // 预留
        platformPropBean.setContent("");
        // 第三方平台类目的属性的hash
        platformPropBean.setPlatformPropHash(getPropHash(platformPropBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

        // 规则
        platformPropBean.setPlatformPropRuleBeanList(getRule(field, platformPropBean.getPlatformPropHash()));
        // 选项
        List<PlatformPropOptionBean> platformPropOptionBeanList = new ArrayList<>();
        // 选项值重复防止（天猫的可选项的值，有时候会重复（找天猫反馈，被认为是正常的，无法解决）由于值是一样的，所以对我们来说是一样的，跳过即可）
        List<String> lstValueExist = new ArrayList<>();
        for (Option option : field.getOptions()) {
            // 检查是否重复
            if (lstValueExist.contains(option.getValue())) {
                // 跳过
                continue;
            }

            // 添加
            PlatformPropOptionBean platformPropOptionBean = new PlatformPropOptionBean();

            // 第三方平台类目的属性的id
            platformPropOptionBean.setPlatformPropHash(platformPropBean.getPlatformPropHash());
            // 第三方平台的选项名称
            platformPropOptionBean.setPlatformPropOptionName(option.getDisplayName());
            // 第三方平台的选项值
            platformPropOptionBean.setPlatformPropOptionValue(option.getValue());
            // 第三方平台类目的属性的选项的hash
            platformPropOptionBean.setPlatformPropOptionHash(getPropOptionHash(platformPropOptionBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

            platformPropOptionBeanList.add(platformPropOptionBean);

            // 给检查是否重复用的集合
            lstValueExist.add(option.getValue());
        }
        platformPropBean.setPlatformPropOptionBeanList(platformPropOptionBeanList);

        result.add(platformPropBean);

        return result;
    }

    /**
     * 取得属性 - 多选
     * @param cartId 第三方平台cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品
     * @param field 传进来的属性
     * @param isTopProp 是否是顶层属性
     * @param parentHash 父属性hash
     * @param path path
     * @return List<PlatformPropBean>
     */
    private static List<PlatformPropBean> doGetField_MultiCheck(
            int cartId,
            String cid,
            int isProduct,
            MultiCheckField field,
            int isTopProp,
            String parentHash,
            String path)
    {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        PlatformPropBean platformPropBean = new PlatformPropBean();

        // 第三方平台id
        platformPropBean.setPlatformCartId(cartId);
        // 第三方平台的类目id
        platformPropBean.setPlatformCid(cid);
        // 第三方平台的属性id
        platformPropBean.setPlatformPropId(field.getId());
        // 第三方平台的属性路径
        platformPropBean.setPlatformPropPath(path);
        // 第三方平台的属性名称
        platformPropBean.setPlatformPropName(field.getName());
        // 第三方平台的属性类型
        platformPropBean.setPlatformPropType(ImsConstants.PlatformPropType.getValueByName(FieldTypeEnum.MULTICHECK));
        // 第三方平台的属性默认值
        platformPropBean.setPlatformPropValueDefault("");
        // 第三方平台的属性的父属性id
        platformPropBean.setParentPropHash(parentHash);
        // 是否是顶层属性
        platformPropBean.setIsTopProp(isTopProp);
        // 是否是父亲（是否包含子属性）
        platformPropBean.setIsParent(0);
        // 是否是产品（产品：1； 商品：0）
        platformPropBean.setIsProduct(isProduct);
        // 预留
        platformPropBean.setContent("");
        // 第三方平台类目的属性的hash
        platformPropBean.setPlatformPropHash(getPropHash(platformPropBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

        // 规则
        platformPropBean.setPlatformPropRuleBeanList(getRule(field, platformPropBean.getPlatformPropHash()));
        // 选项
        List<PlatformPropOptionBean> platformPropOptionBeanList = new ArrayList<>();
        for (Option option : field.getOptions()) {

            PlatformPropOptionBean platformPropOptionBean = new PlatformPropOptionBean();

            // 第三方平台类目的属性的id
            platformPropOptionBean.setPlatformPropHash(platformPropBean.getPlatformPropHash());
            // 第三方平台的选项名称
            platformPropOptionBean.setPlatformPropOptionName(option.getDisplayName());
            // 第三方平台的选项值
            platformPropOptionBean.setPlatformPropOptionValue(option.getValue());
            // 第三方平台类目的属性的选项的hash
            platformPropOptionBean.setPlatformPropOptionHash(getPropOptionHash(platformPropOptionBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

            platformPropOptionBeanList.add(platformPropOptionBean);
        }
        platformPropBean.setPlatformPropOptionBeanList(platformPropOptionBeanList);

        result.add(platformPropBean);

        return result;
    }

    /**
     * 取得属性 - 包含重复的子属性项目
     * @param cartId 第三方平台cart id
     * @param cid 第三方平台类目id
     * @param isProduct 是否是产品
     * @param field 传进来的属性
     * @param isTopProp 是否是顶层属性
     * @param parentHash 父属性hash
     * @param path path
     * @return List<PlatformPropBean>
     */
    private static List<PlatformPropBean> doGetField_MultiComplex(
            int cartId,
            String cid,
            int isProduct,
            MultiComplexField field,
            int isTopProp,
            String parentHash,
            String path)
    {
        // 返回值
        List<PlatformPropBean> result = new ArrayList<>();

        PlatformPropBean platformPropBean = new PlatformPropBean();

        // 第三方平台id
        platformPropBean.setPlatformCartId(cartId);
        // 第三方平台的类目id
        platformPropBean.setPlatformCid(cid);
        // 第三方平台的属性id
        platformPropBean.setPlatformPropId(field.getId());
        // 第三方平台的属性路径
        platformPropBean.setPlatformPropPath(path);
        // 第三方平台的属性名称
        platformPropBean.setPlatformPropName(field.getName());
        // 第三方平台的属性类型
        platformPropBean.setPlatformPropType(ImsConstants.PlatformPropType.getValueByName(FieldTypeEnum.MULTICOMPLEX));
        // 第三方平台的属性默认值
        platformPropBean.setPlatformPropValueDefault("");
        // 第三方平台的属性的父属性id
        platformPropBean.setParentPropHash(parentHash);
        // 是否是顶层属性
        platformPropBean.setIsTopProp(isTopProp);
        // 是否是父亲（是否包含子属性）
        platformPropBean.setIsParent(0);
        // 是否是产品（产品：1； 商品：0）
        platformPropBean.setIsProduct(isProduct);
        // 预留
        platformPropBean.setContent("");
        // 第三方平台类目的属性的hash
        platformPropBean.setPlatformPropHash(getPropHash(platformPropBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

        // 规则
        platformPropBean.setPlatformPropRuleBeanList(getRule(field, platformPropBean.getPlatformPropHash()));

        result.add(platformPropBean);

        // 设置子属性
        String newPath = path + field.getId() + ImsConstants.C_PROP_PATH_SPLIT_CHAR;
        for (Field fieldSub : field.getFieldList()) {
            result.addAll(doGetField(cartId, cid, isProduct, fieldSub, 0, platformPropBean.getPlatformPropHash(), newPath));
        }

        return result;
    }

    /**
     * 取得属性的规则
     * @param field 传进来的属性
     * @param platformPropHash 属性hash
     * @return List<PlatformPropRuleBean>
     */
    private static List<PlatformPropRuleBean> getRule(
            Field field,
            String platformPropHash)
    {
        // 返回值
        List<PlatformPropRuleBean> result = new ArrayList<>();

        // 属性id（单个属性为单位的自增序号）
        int intId = 1;

        // 关联属性的规则
        List<PropRuleDependExpressBean> propRuleDependExpressBeanList = new ArrayList<>();

        // 循环，有可能有多条规则
        for (Rule rule : field.getRules()) {

            PlatformPropRuleBean platformPropRuleBean = new PlatformPropRuleBean();

            if (rule.getDependGroup() == null) {
                // 没有关联属性的规则的场合
                platformPropRuleBean.setPlatformPropRuleRelationshipOperator(null);
                platformPropRuleBean.setPlatformPropRuleRelationship(null);
            } else {
                if (rule.getDependGroup().getDependExpressList().size() > 0) {
                    // 多个关联规则之间的连接符
                    platformPropRuleBean.setPlatformPropRuleRelationshipOperator(rule.getDependGroup().getOperator());

                    // 对于每条规则，可能会有多个关联规则
                    for (DependExpress dependExpress : rule.getDependGroup().getDependExpressList()) {
                        PropRuleDependExpressBean propRuleDependExpressBean = new PropRuleDependExpressBean();

                        propRuleDependExpressBean.setExtraPropId(dependExpress.getFieldId());
                        propRuleDependExpressBean.setSymbol(dependExpress.getSymbol());
                        propRuleDependExpressBean.setExtraValue(dependExpress.getValue());

                        propRuleDependExpressBeanList.add(propRuleDependExpressBean);

                    }
                    String strPlatformPropRuleRelationship = JsonUtil.getJsonString(propRuleDependExpressBeanList);

                    if (strPlatformPropRuleRelationship.length() > 500) {
                        // 超出数据库承受能力，此关联规则无视之
                        platformPropRuleBean.setPlatformPropRuleRelationshipOperator(null);
                        platformPropRuleBean.setPlatformPropRuleRelationship(null);

                    } else {
                        platformPropRuleBean.setPlatformPropRuleRelationship(strPlatformPropRuleRelationship);
                    }
                }

            }

            // 第三方平台类目的属性的hash
            platformPropRuleBean.setPlatformPropHash(platformPropHash);
            // 第三方平台类目的属性的规则的id
            platformPropRuleBean.setPlatformPropRuleId(String.valueOf(intId)); intId++;
            // 第三方平台类目的属性的规则的名称
            platformPropRuleBean.setPlatformPropRuleName(rule.getName());
            // 第三方平台类目的属性的规则的值
            platformPropRuleBean.setPlatformPropRuleValue(rule.getValue());
            // 第三方平台类目的属性的规则的扩展属性
            platformPropRuleBean.setPlatformPropRuleExProperty(rule.getExProperty());

            // 第三方平台类目的属性的规则的单位
            platformPropRuleBean.setPlatformPropRuleUnit(null);
            // 第三方平台类目的属性的规则的url
            platformPropRuleBean.setPlatformPropRuleUrl(null);

            if (rule.getClass().equals(MaxLengthRule.class)) {
                MaxLengthRule maxLengthRule = (MaxLengthRule) rule;
                if (!StringUtils.isEmpty(maxLengthRule.getUnit())) {
                    // 第三方平台类目的属性的规则的单位
                    platformPropRuleBean.setPlatformPropRuleUnit(maxLengthRule.getUnit());
                }
            } else if (rule.getClass().equals(MinLengthRule.class)) {
                MinLengthRule minLengthRule = (MinLengthRule) rule;
                if (!StringUtils.isEmpty(minLengthRule.getUnit())) {
                    // 第三方平台类目的属性的规则的单位
                    platformPropRuleBean.setPlatformPropRuleUnit(minLengthRule.getUnit());
                }
            } else if (rule.getClass().equals(MinTargetSizeRule.class)) {
                MinTargetSizeRule minTargetSizeRule = (MinTargetSizeRule) rule;
                if (!StringUtils.isEmpty(minTargetSizeRule.getUnit())) {
                    // 第三方平台类目的属性的规则的单位
                    platformPropRuleBean.setPlatformPropRuleUnit(minTargetSizeRule.getUnit());
                }
            } else if (rule.getClass().equals(MaxTargetSizeRule.class)) {
                MaxTargetSizeRule maxTargetSizeRule = (MaxTargetSizeRule) rule;
                if (!StringUtils.isEmpty(maxTargetSizeRule.getUnit())) {
                    // 第三方平台类目的属性的规则的单位
                    platformPropRuleBean.setPlatformPropRuleUnit(maxTargetSizeRule.getUnit());
                }
            } else if (rule.getClass().equals(TipRule.class)) {
                TipRule tipRule = (TipRule) rule;
                if (!StringUtils.isEmpty(tipRule.getUrl())) {
                    // 第三方平台类目的属性的规则的url
                    platformPropRuleBean.setPlatformPropRuleUrl(tipRule.getUrl());
                }
            } else if (rule.getClass().equals(DevTipRule.class)) {
                DevTipRule devTipRule = (DevTipRule) rule;
                if (!StringUtils.isEmpty(devTipRule.getUrl())) {
                    // 第三方平台类目的属性的规则的url
                    platformPropRuleBean.setPlatformPropRuleUrl(devTipRule.getUrl());
                }
            }

            // 第三方平台类目的属性的规则的hash
            platformPropRuleBean.setPlatformPropRuleHash(getPropRuleHash(platformPropRuleBean)); // 这个属性要放到最后才设定，因为会需要引用自己对象的其他值

            result.add(platformPropRuleBean);

        }

        return result;
    }

    /**
     * 生成hash值（属性级）
     * @param platformPropBean 第三方平台属性信息
     * @return String
     */
    private static String getPropHash(PlatformPropBean platformPropBean) {

        // 数据准备
        int platformCartId = platformPropBean.getPlatformCartId();
        String platformCid = platformPropBean.getPlatformCid();
        String platformPropId = platformPropBean.getPlatformPropId();
        String platformPropPath = platformPropBean.getPlatformPropPath();

        String strValue = String.valueOf(platformCartId) + platformCid + platformPropId + platformPropPath;

        return getMd5FromString(strValue);
    }

    /**
     * 生成hash值（属性选项级）
     * @param platformPropOptionBean 第三方平台属性选项信息
     * @return String
     */
    private static String getPropOptionHash(PlatformPropOptionBean platformPropOptionBean) {

        // 数据准备
        String platformPropHash = platformPropOptionBean.getPlatformPropHash();
        String platformPropOptionValue = platformPropOptionBean.getPlatformPropOptionValue();

        String strValue = platformPropHash + platformPropOptionValue;

        return getMd5FromString(strValue);
    }

    /**
     * 生成hash值（属性规则级）
     * @param platformPropRuleBean 第三方平台属性规则信息
     * @return String
     */
    private static String getPropRuleHash(PlatformPropRuleBean platformPropRuleBean) {

        // 数据准备
        String platformPropHash = platformPropRuleBean.getPlatformPropHash();
        String platformPropRuleId = platformPropRuleBean.getPlatformPropRuleId();

        String strValue = platformPropHash + platformPropRuleId;

        return getMd5FromString(strValue);
    }

    /**
     * 将字符串MD5化
     * @param value 输入的字符串
     * @return String
     */
    private static String getMd5FromString(String value) {
        return MD5.getMD5(value);
    }

}
