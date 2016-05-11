package com.voyageone.web2.cms.views.mapping.feed;

import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.configs.beans.TypeBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.util.MD5;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.CommonSchemaService;
import com.voyageone.service.impl.cms.feed.FeedCategoryAttributeService;
import com.voyageone.service.impl.cms.feed.FeedMappingService;
import com.voyageone.service.model.cms.enums.MappingPropType;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedMappingModel;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import com.voyageone.service.model.cms.mongo.feed.mapping.Prop;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.feed.FieldBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.GetFieldMappingBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SaveFieldMappingBean;
import com.voyageone.web2.cms.bean.setting.mapping.feed.SetMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * 为属性匹配画面提供功能
 *
 * @author Jonas, 12/24/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
class CmsFeedPropMappingService extends BaseAppService {

    @Autowired
    private FeedCategoryAttributeService feedCategoryAttributeService;

    @Autowired
    private CategorySchemaService categorySchemaService;

    @Autowired
    private FeedMappingService feedMappingService;

    @Autowired
    private CommonSchemaService commonSchemaService;

    /**
     * 返回主类目和相应的属性
     */
    Map<String, Object> getMainCategoryInfo(String mainCategoryPath, String channelId, String lang) {

        String categoryId = convertPathToId(mainCategoryPath);

        // 查询主类目信息
        CmsMtCategorySchemaModel categorySchemaModel = categorySchemaService.getCmsMtCategorySchema(categoryId);

        // 拍平主类目的字段信息
        // 并构造画面特供模型
        Map<String, FieldBean> mainFieldBeanMap = wrapFields(categorySchemaModel.getFields().stream(),
                MappingPropType.FIELD);

        Map<String, FieldBean> skuFieldBeanMap = wrapFields(
                ((MultiComplexField) categorySchemaModel.getSku())
                        .getFields().stream(),
                MappingPropType.SKU);

        // 构造一个纯字段名的集合,用于共通属性的检查
        List<String> names = Stream.concat(
                mainFieldBeanMap.entrySet().stream(),
                skuFieldBeanMap.entrySet().stream()
        )
                .map(e -> e.getValue().getField().getName())
                .distinct()
                .collect(toList());

        List<Field> commonFields = getCommonSchema();

        // 编辑SingleCheckField的Options项目
        editOptions(commonFields, channelId, lang);

        // 对共通属性同样处理
        // 但如果普通属性里已经有的则需要忽略掉
        Stream<Field> commonFieldStream = commonFields.stream()
                .filter(f -> {
                    // 过滤 ID 相同的
                    if (mainFieldBeanMap.containsKey(f.getId()))
                        return false;
                    if (skuFieldBeanMap.containsKey(f.getId()))
                        return false;
                    // 过滤名称相同的
                    return !names.contains(f.getName());
                });
        Map<String, FieldBean> commonFieldBeanMap = wrapFields(commonFieldStream,
                MappingPropType.COMMON);

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
     * @return 属性匹配设定
     */
    Prop getFieldMapping(GetFieldMappingBean getFieldMappingBean, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel mappingModel = feedMappingService.getMapping(userSessionBean.getSelChannel(),new ObjectId(getFieldMappingBean.getMappingId()));

        if (mappingModel == null)
            return null;

        List<Prop> props = mappingModel.getProps();
        if (props == null)
            return null;

        String fieldId = getFieldMappingBean.getFieldId();

        MappingPropType fieldType = getFieldMappingBean.getFieldType();

        return flattenFinalProp(props)
                .filter(p -> p.getProp().equals(fieldId) && fieldType.equals(p.getType()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取类目匹配中已匹配的主类目属性
     */
    Map<MappingPropType, List<String>> getMatched(SetMappingBean setMappingBean, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel feedMappingModel = feedMappingService.getMapping(userSessionBean.getSelChannel(),new ObjectId(setMappingBean.getMappingId()));

        List<Prop> props = feedMappingModel.getProps();

        return props == null
                ? new HashMap<>(0)
                : flattenFinalProp(props)
                .filter(p -> p.getMappings() != null && p.getMappings().size() > 0 && p.getType() != null)
                .collect(groupingBy(Prop::getType, mapping(Prop::getProp, toList())));
    }

    /**
     * 按 feedCategoryPath 查找类目并返回类目的属性
     *
     * @param feedCategoryPath 类目路径
     * @param userSessionBean  当前用户
     * @return 类目的属性
     */
    Map<String, List<String>> getFeedAttributes(String feedCategoryPath, String lang, UserSessionBean userSessionBean) {

        CmsMtFeedAttributesModel cmsMtFeedAttributesModel = feedCategoryAttributeService.getCategoryAttributeByCategory(userSessionBean.getSelChannelId(),feedCategoryPath);

        // 从 type/value 中取得 Feed 通用的属性
        List<TypeBean> typeList = Types.getTypeList(49, lang);

        Assert.notNull(typeList);

        Map<String, List<String>> attributes = typeList
                .stream()
                .collect(toMap(TypeBean::getValue, t -> new ArrayList<>(0)));

        if (cmsMtFeedAttributesModel == null)
            return attributes;

        attributes.putAll(cmsMtFeedAttributesModel.getAttribute());

        return attributes;
    }

    /**
     * 通过 categoryPath 转换获取 categoryId
     *
     * @param categoryPath 类目路径
     * @return String
     */
    private String convertPathToId(String categoryPath) {
        return MD5.getMD5(categoryPath);
    }

    /**
     * 保存一个字段/属性的关联关系
     *
     * @param saveFieldMappingBean 请求参数
     */
    boolean saveFeedMapping(SaveFieldMappingBean saveFieldMappingBean, UserSessionBean userSessionBean) {

        CmsBtFeedMappingModel feedMappingModel = feedMappingService.getMapping(userSessionBean.getSelChannel(),new ObjectId(saveFieldMappingBean.getMappingId()));

        if (feedMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        Prop submitted = saveFieldMappingBean.getPropMapping();
        List<String> fieldPath = saveFieldMappingBean.getFieldPath();

        if (submitted == null || fieldPath == null || fieldPath.size() < 1 || submitted.getType() == null)
            throw new BusinessException("没有参数");

        List<Prop> props = feedMappingModel.getProps();

        if (props == null)
            feedMappingModel.setProps(props = new ArrayList<>());

        Prop prop = findProp(fieldPath, submitted.getType(), props);

        prop.setMappings(submitted.getMappings());

        WriteResult result = feedMappingService.updateMapping(feedMappingModel);

        return result.getN() > 0;
    }

    /**
     * 按属性路径查找或创建树形的属性结构
     *
     * @param propPath 属性路径
     * @param type     Mapping 的属性类型
     * @param props    顶层属性集合
     * @return 返回最叶子级
     */
    private Prop findProp(List<String> propPath, MappingPropType type, List<Prop> props) {

        Prop prop = null;
        String last = propPath.get(propPath.size() - 1);

        for (String name : propPath) {
            // 先找找
            prop = findProp(props, type, name);
            // 没找到就造个新的,并同时扔到父亲的子集合中
            if (prop == null) {
                props.add(prop = new Prop());
                prop.setProp(name);
                prop.setType(type);
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

    private Prop findProp(List<Prop> props, MappingPropType type, String name) {
        if (props.size() < 1) return null;
        if (props.size() < 2) return props.get(0).getProp().equals(name) ? props.get(0) : null;
        return props.stream()
                .filter(p -> p.getProp().equals(name) && type.equals(p.getType()))
                .findFirst()
                .orElse(null);
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

    private Map<String, FieldBean> wrapFields(Stream<Field> fieldStream, MappingPropType type) {

        final int[] seq = {0};

        return fieldStream
                .flatMap(f -> flattenField(0, null, type, f))
                .map(f -> {
                    f.setSeq(seq[0]);
                    seq[0]++;
                    return f;
                })
                .collect(toMap(mfb -> mfb.getField().getId(), mfb -> mfb));
    }

    private Stream<FieldBean> flattenField(int level, String parentId, MappingPropType type, Field field) {

        FieldBean fieldBean = new FieldBean();
        fieldBean.setField(field);
        fieldBean.setParentId(parentId);
        fieldBean.setLevel(level);
        fieldBean.setType(type);

        Stream<FieldBean> stream = Stream.of(fieldBean);

        Stream<FieldBean> children = null;

        if (field.getType() == FieldTypeEnum.COMPLEX) {
            children = ((ComplexField) field)
                    .getFields()
                    .stream()
                    .flatMap(f -> flattenField(level + 1, field.getId(), type, f));
        } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
            children = ((MultiComplexField) field)
                    .getFields()
                    .stream()
                    .flatMap(f -> flattenField(level + 1, field.getId(), type, f));
        }

        return children == null ? stream : Stream.concat(stream, children);
    }

    private List<Field> getCommonSchema() {
        List<Map<String, Object>> list = commonSchemaService.getAll();
        return SchemaJsonReader.readJsonForList(list);
    }

    // jeff 2016/04 add start
    /**
     * 编辑SingleCheckField的Options项目
     */
    private void editOptions (List<Field> fields, String channelId, String language) {

        for (Field field : fields) {
            if (field instanceof SingleCheckField) {
                if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE.equals(field.getDataSource())) {
                    List<TypeBean> typeBeanList = Types.getTypeList(field.getId(), language);

                    // 替换成field需要的样式
                    List<Option> options = new ArrayList<>();
                    if (typeBeanList != null) {
                        for (TypeBean typeBean : typeBeanList) {
                            Option opt = new Option();
                            opt.setDisplayName(typeBean.getName());
                            opt.setValue(typeBean.getValue());
                            options.add(opt);
                        }
                        ((SingleCheckField) field).setOptions(options);
                    }
                } else if (CmsConstants.OptionConfigType.OPTION_DATA_SOURCE_CHANNEL.equals(field.getDataSource())) {
                    // 获取type channel bean
                    List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeWithLang(field.getId(), channelId, language);

                    // 替换成field需要的样式
                    List<Option> options = new ArrayList<>();
                    if (typeChannelBeanList != null) {
                        for (TypeChannelBean typeChannelBean : typeChannelBeanList) {
                            Option opt = new Option();
                            opt.setDisplayName(typeChannelBean.getName());
                            opt.setValue(typeChannelBean.getValue());
                            options.add(opt);
                        }
                        ((SingleCheckField) field).setOptions(options);
                    }
                }
            }
        }
    }
    // jeff 2016/04 add end
}
