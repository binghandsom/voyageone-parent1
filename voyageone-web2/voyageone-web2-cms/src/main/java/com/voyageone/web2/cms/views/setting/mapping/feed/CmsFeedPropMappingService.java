package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtFeedCategoryTreeDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.cms.service.model.feed.mapping.Prop;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.util.MD5;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.FieldBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.GetFieldMappingBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SaveFieldMappingBean;
import com.voyageone.web2.cms.dao.CmsMtCommonPropDefDao;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 为属性匹配画面提供功能
 *
 * @author Jonas, 12/24/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsFeedPropMappingService extends BaseAppService {

    @Autowired
    private CmsMtFeedCategoryTreeDao cmsMtFeedCategoryTreeDao;

    @Autowired
    private CmsMtCategorySchemaDao categorySchemaDao;

    @Autowired
    private CmsFeedMappingService feedMappingService;

    @Autowired
    private CmsMtCommonPropDefDao commonPropDefDao;

    @Autowired
    private com.voyageone.cms.service.CmsFeedMappingService com$feedMappingService;

    /**
     * 通过 Feed 类目,获取其默认匹配的主类目
     *
     * @param feedCategoryPath Feed 类目路径
     * @param userSessionBean  当前用户及配置
     * @return 主类目
     */
    public Map<String, Object> getCategoryPropsByFeed(String feedCategoryPath, UserSessionBean userSessionBean) {
        // 获取完整的 Feed 类目树
        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectFeedCategoryx(userSessionBean.getSelChannelId());
        // 从全部 Feed 类目中查询具体的类目
        CmsFeedCategoryModel feedCategoryModel = feedMappingService.findByPath(feedCategoryPath, treeModelx);

        if (feedCategoryModel == null)
            throw new BusinessException("根据路径没找到类目");
        // 从查询到的 Feed 类目查询默认的 Mapping 关系
        CmsFeedMappingModel feedMappingModel = feedMappingService.findMapping(feedCategoryModel, m -> m.getDefaultMapping() == 1);

        if (feedMappingModel == null)
            throw new BusinessException("类目没有默认的类目匹配");

        // 通过 Mapping 获取主类目的 Path
        // 通过 Path 转换获取到 ID (这部分是规定的 MD5 转换)
        String categoryId = convertPathToId(feedMappingModel.getMainCategoryPath());
        // 查询主类目信息
        CmsMtCategorySchemaModel categorySchemaModel = categorySchemaDao.getMasterSchemaModelByCatId(categoryId);

        // 拍平主类目的字段信息
        // 并构造画面特供模型
        Map<String, FieldBean> mainFieldBeanMap = wrapFields(categorySchemaModel.getFields().stream());

        Map<String, FieldBean> skuFieldBeanMap = wrapFields(
                ((MultiComplexField) categorySchemaModel.getSku())
                        .getFields().stream());

        // 构造一个纯字段名的集合,用于共通属性的检查
        List<String> names = Stream.concat(
                mainFieldBeanMap.entrySet().stream(),
                skuFieldBeanMap.entrySet().stream()
        )
                .map(e -> e.getValue().getField().getName())
                .distinct()
                .collect(toList());

        // 对共通属性同样处理
        // 但如果普通属性里已经有的则需要忽略掉
        Stream<Field> commonFieldStream = commonPropDefDao.selectAll()
                .stream()
                .map(CmsMtCommonPropDefModel::getField)
                .filter(f -> {
                    // 过滤 ID 相同的
                    if (mainFieldBeanMap.containsKey(f.getId()))
                        return false;
                    if (skuFieldBeanMap.containsKey(f.getId()))
                        return false;
                    // 过滤名称相同的
                    return !names.contains(f.getName());
                });
        Map<String, FieldBean> commonFieldBeanMap = wrapFields(commonFieldStream);

        // 最后清除主类目模型的字段信息,因为不需要再重复提供
        categorySchemaModel.setFields(null);
        categorySchemaModel.setSku(null);

        return new HashMap<String, Object>() {{
            put("category", categorySchemaModel);
            put("fields", mainFieldBeanMap);
            put("sku", skuFieldBeanMap);
            put("common", commonFieldBeanMap);
        }};
    }

    /**
     * 获取具体到字段的 Mapping 设定
     *
     * @param getFieldMappingBean 查询参数
     * @param user                当前用户
     * @return 属性匹配设定
     */
    public Prop getFieldMapping(GetFieldMappingBean getFieldMappingBean, UserSessionBean user) {

        CmsBtFeedMappingModel mappingModel = com$feedMappingService.getMapping(user.getSelChannel(),
                getFieldMappingBean.getFeedCategoryPath(), getFieldMappingBean.getMainCategoryPath());

        List<Prop> props = mappingModel.getProps();

        if (props == null)
            return null;

        return flattenFinalProp(props)
                .filter(p -> p.getProp().equals(getFieldMappingBean.getField()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取类目匹配中已匹配的主类目属性
     *
     * @param feedCategoryPath Feed 类目路径
     * @param mainCategoryPath Main 类目路径
     * @param userSessionBean  当前用户
     * @return 属性名集合
     */
    public List<String> getMatched(String feedCategoryPath, String mainCategoryPath, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel btFeedMappingModel = com$feedMappingService.getMapping(userSessionBean.getSelChannel(),
                feedCategoryPath, mainCategoryPath);

        if (btFeedMappingModel == null)
            throw new BusinessException("没有找到匹配");

        List<Prop> props = btFeedMappingModel.getProps();

        if (props == null)
            return new ArrayList<>(0);

        return flattenFinalProp(props)
                .filter(p -> p.getMappings() != null && p.getMappings().size() > 0)
                .map(Prop::getProp)
                .collect(toList());
    }

    /**
     * 通过 categoryPath 转换获取 categoryId
     *
     * @param categoryPath 类目路径
     * @return String
     */
    public String convertPathToId(String categoryPath) {
        return MD5.getMD5(categoryPath);
    }

    /**
     * 按 feedCategoryPath 查找类目并返回类目的属性
     *
     * @param feedCategoryPath 类目路径
     * @param userSessionBean  当前用户
     * @return 类目的属性
     */
    public Map<String, List<String>> getFeedAttributes(String feedCategoryPath, UserSessionBean userSessionBean) {

        CmsMtFeedCategoryTreeModelx treeModelx = cmsMtFeedCategoryTreeDao.selectFeedCategoryx(userSessionBean.getSelChannelId());

        CmsFeedCategoryModel feedCategoryModel = feedMappingService.findByPath(feedCategoryPath, treeModelx);

        // 从 type/value 中取得 Feed 通用的属性
        Map<String, List<String>> attributes = Type.getTypeList(49, "en")
                .stream()
                .collect(toMap(TypeBean::getValue, t -> new ArrayList<>(0)));

        if (feedCategoryModel == null)
            return attributes;

        attributes.putAll(feedCategoryModel.getAttribute());

        return attributes;
    }

    /**
     * 保存一个字段/属性的关联关系
     *
     * @param saveFieldMappingBean 请求参数
     * @param userSessionBean      当前用户
     */
    public void saveFeedMapping(SaveFieldMappingBean saveFieldMappingBean, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel feedMappingModel = com$feedMappingService.getMapping(userSessionBean.getSelChannel(),
                saveFieldMappingBean.getFeedCategoryPath(), saveFieldMappingBean.getMainCategoryPath());

        if (feedMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        Prop Submitted = saveFieldMappingBean.getPropMapping();
        List<String> fieldPath = saveFieldMappingBean.getFieldPath();

        if (Submitted == null || fieldPath == null || fieldPath.size() < 1)
            throw new BusinessException("没有参数");

        List<Prop> props = feedMappingModel.getProps();

        if (props == null)
            feedMappingModel.setProps(props = new ArrayList<>());

        Prop prop = findProp(fieldPath, props);

        prop.setMappings(Submitted.getMappings());

        feedMappingModel.setMatchOver(hasComplete(feedMappingModel) ? 1 : 0);

        com$feedMappingService.setMapping(feedMappingModel);
    }

    private boolean hasComplete(CmsBtFeedMappingModel feedMappingModel) {

        String categoryId = convertPathToId(feedMappingModel.getScope().getMainCategoryPath());

        CmsMtCategorySchemaModel categorySchemaModel = categorySchemaDao.getMasterSchemaModelByCatId(categoryId);

        long fieldCount = categorySchemaModel
                .getFields()
                .stream()
                .flatMap(this::flattenFinalField)
                .count();

        long propCount = flattenFinalProp(feedMappingModel.getProps()).count();

        return fieldCount == propCount;
    }

    /**
     * 按属性路径查找或创建树形的属性结构
     *
     * @param propPath 属性路径
     * @param props    顶层属性集合
     * @return 返回最叶子级
     */
    private Prop findProp(List<String> propPath, List<Prop> props) {

        Prop prop = null;
        String last = propPath.get(propPath.size() - 1);

        for (String name : propPath) {
            // 先找找
            prop = findProp(props, name);
            // 没找到就造个新的,并同时扔到父亲的子集合中
            if (prop == null) {
                props.add(prop = new Prop());
                prop.setProp(name);
            }
            // 看看还会不会在进行下一次查询
            if (last.equals(name)) break;
            // 为遍历的下一次做准备
            props = prop.getChildren();
            // 如果没取到,就更新
            if (props == null)
                prop.setChildren(props = new ArrayList<>());
        }

        return prop;
    }

    private Prop findProp(List<Prop> props, String name) {
        if (props.size() < 1) return null;
        if (props.size() < 2) return props.get(0).getProp().equals(name) ? props.get(0) : null;
        return props.stream().filter(p -> p.getProp().equals(name)).findFirst().orElse(null);
    }

    private Stream<Prop> flattenFinalProp(List<Prop> props) {

        return props.stream().flatMap(this::flattenFinalProp);
    }

    private Stream<Prop> flattenFinalProp(Prop prop) {

        List<Prop> children = prop.getChildren();

        if (children == null || children.size() < 1)
            return Stream.of(prop);

        return flattenFinalProp(children);
    }

    private Map<String, FieldBean> wrapFields(Stream<Field> fieldStream) {

        final int[] seq = {0};

        return fieldStream
                .flatMap(f -> flattenField(0, null, f))
                .map(f -> {
                    f.setSeq(seq[0]);
                    seq[0]++;
                    return f;
                })
                .collect(toMap(mfb -> mfb.getField().getId(), mfb -> mfb));
    }

    private Stream<FieldBean> flattenField(int level, String parentId, Field field) {

        FieldBean fieldBean = new FieldBean();
        fieldBean.setField(field);
        fieldBean.setParentId(parentId);
        fieldBean.setLevel(level);

        Stream<FieldBean> stream = Stream.of(fieldBean);

        Stream<FieldBean> children = null;

        if (field.getType() == FieldTypeEnum.COMPLEX) {
            children = ((ComplexField) field)
                    .getFields()
                    .stream()
                    .flatMap(f -> flattenField(level + 1, field.getId(), f));
        } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
            children = ((MultiComplexField) field)
                    .getFields()
                    .stream()
                    .flatMap(f -> flattenField(level + 1, field.getId(), f));
        }

        return children == null ? stream : Stream.concat(stream, children);
    }

    private Stream<Field> flattenFinalField(Field field) {

        if (field.getType() == FieldTypeEnum.COMPLEX) {
            return ((ComplexField) field).getFields().stream().flatMap(this::flattenFinalField);
        } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
            return ((MultiComplexField) field).getFields().stream().flatMap(this::flattenFinalField);
        }

        return Stream.of(field);
    }
}
