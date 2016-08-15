package com.voyageone.web2.cms.views.mapping.platform;

import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.field.ComplexField;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiComplexField;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleWord;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.bean.cms.system.dictionary.CmsDictionaryIndexBean;
import com.voyageone.service.impl.cms.CategorySchemaService;
import com.voyageone.service.impl.cms.DictService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.PlatformMappingDeprecatedService;
import com.voyageone.service.model.cms.CmsMtPlatformDictModel;
import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.setting.mapping.platform.PlatformMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 当前的功能,仅限支持天猫...完全不支持其他平台
 *
 * @author Jonas, 1/13/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("web2.cms.CmsPlatformPropMappingService")
class CmsPlatformPropMappingService extends BaseAppService {

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CategorySchemaService categorySchemaService;

    @Autowired
    private DictService dictService;

    @Autowired
    private PlatformMappingDeprecatedService platformMappingDeprecatedService;

    /**
     * 获取平台类目和 Mapping 的所有信息
     *
     * @param categoryId 主数据类目 ID
     * @param cartId     平台 ID
     * @param user       用户配置
     * @return 携带所有信息的 Map 包含: categorySchema / properties / mapping
     * @throws TopSchemaException
     */
    Map<String, Object> getPlatformCategory(String categoryId, int cartId, UserSessionBean user) throws TopSchemaException {

        CmsMtPlatformMappingModel platformMappingModel = platformMappingDeprecatedService.getMappingByMainCatId(user.getSelChannelId(), cartId, categoryId);

        if (platformMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        CmsMtPlatformCategorySchemaModel platformCatSchemaModel = platformCategoryService.getPlatformCatSchema(platformMappingModel.getPlatformCategoryId(), cartId);

        // 转换类目属性
        Map<String, Field> fieldMap = SchemaReader.readXmlForMap(platformCatSchemaModel.getPropsItem());
        // 20160525 tom 同时显示product的信息 START
        if (platformCatSchemaModel.getPropsProduct() != null) {
            fieldMap.putAll(SchemaReader.readXmlForMap(platformCatSchemaModel.getPropsProduct()));
        }
        // 20160525 tom 同时显示product的信息 END

        // 转换简化的 mapping 信息
        List<MappingBean> mappingBeen = platformMappingModel.getProps();

        Map<String, Object> mappingMap = mappingBeen.stream()
                .collect(toMap(MappingBean::getPlatformPropId, this::getMatched));

        // 清除信息
        platformCatSchemaModel.setPropsItem(null);

        return new HashMap<String, Object>() {{
            put("categorySchema", platformCatSchemaModel);
            put("properties", fieldMap);
            put("mapping", mappingMap);
            put("matchOver", platformMappingModel.getMatchOver());
        }};
    }

    /**
     * 直接获取平台类目和 Mapping 的所有信息
     *
     * @param categoryId 平台类目 ID
     * @param cartId     平台 ID
     * @param user       用户配置
     * @return CmsMtCategorySchemaModel
     */
    CmsMtCategorySchemaModel getPlatformCategorySchema(String categoryId, int cartId, UserSessionBean user) {
        CmsMtPlatformCategorySchemaModel platformCatSchemaModel = platformCategoryService.getPlatformCatSchema(categoryId, cartId);
        if (platformCatSchemaModel == null) {
            $warn("getPlatformCategorySchema 没有该类目的数据 categoryId=" + categoryId + " cartId=" + cartId);
            return null;
        }

        // 转换类目属性
        String itemSchema = platformCatSchemaModel.getPropsItem();
        String productSchema = platformCatSchemaModel.getPropsProduct();
        List<com.voyageone.common.masterdate.schema.field.Field> masterFields = new ArrayList<>();
        List<com.voyageone.common.masterdate.schema.field.Field> itemFields = new ArrayList<>();
        List<com.voyageone.common.masterdate.schema.field.Field> prodFields = new ArrayList<>();
        //取得商品fields from item schema
        if (!com.voyageone.common.util.StringUtils.isEmpty(itemSchema)) {
            itemFields = com.voyageone.common.masterdate.schema.factory.SchemaReader.readXmlForList(itemSchema);
        }
        //取得产品fields from item schema
        if (!com.voyageone.common.util.StringUtils.isEmpty(productSchema)) {
            prodFields = com.voyageone.common.masterdate.schema.factory.SchemaReader.readXmlForList(productSchema);
        }

        for (com.voyageone.common.masterdate.schema.field.Field proField : prodFields) {
            proField.setInputLevel(1);
            masterFields.add(proField);
        }
        for (com.voyageone.common.masterdate.schema.field.Field itemField : itemFields) {
            itemField.setInputLevel(2);
            //判断产品和商品中是否有相同的属性id，有则修改id名字加以区分
            if (masterFields.contains(itemField)) {
                itemField.setInputOrgId(itemField.getId());
                com.voyageone.common.masterdate.schema.field.Field upField = FieldUtil.getFieldById(masterFields, itemField.getId());
                if (upField != null) {
                    String newId = itemField.getId() + "_productLevel";
                    upField.setId(newId);
                    FieldUtil.renameDependFieldId(upField, itemField.getId(), newId, masterFields);
                }
            }
            masterFields.add(itemField);
        }

        CmsMtCategorySchemaModel masterSchemaModel = new CmsMtCategorySchemaModel(platformCatSchemaModel.getCatId(), platformCatSchemaModel.getCatFullPath(), masterFields);
        masterSchemaModel.setModifier(user.getUserName());
        masterSchemaModel.setCreater(user.getUserName());
        masterSchemaModel.setSku(null);

        return masterSchemaModel;
    }

    /**
     * 获取主数据类目 Schema
     *
     * @param categoryId 主数据类目
     * @return CmsMtCategorySchemaModel
     */
    CmsMtCategorySchemaModel getMainCategorySchema(String categoryId) {
        return categorySchemaService.getCmsMtCategorySchema(categoryId);
    }

    /**
     * 获取当前渠道的所有可用字典
     */
    List<CmsMtPlatformDictModel> getDictList(String cartId, String lang, UserSessionBean user) {
        CmsDictionaryIndexBean params = new CmsDictionaryIndexBean();
        params.setOrder_channel_id(user.getSelChannelId());
        params.setCartId(cartId);
        params.setLang(lang);
        return dictService.getModesByChannel(params);
    }

    /**
     * 精确获取平台匹配信息
     *
     * @param mainCategoryId     主数据类目 ID
     * @param platformCategoryId 平台类目 ID
     * @param cartId             平台 ID
     * @param user               用户
     * @return 信息模型
     */
    CmsMtPlatformMappingModel getPlatformMapping(String mainCategoryId, String platformCategoryId, Integer cartId, UserSessionBean user) {
        return platformMappingDeprecatedService.getMapping(mainCategoryId, platformCategoryId, cartId, user.getSelChannel().getId());
    }

    /**
     * 计算 MappingBean 是否匹配的结果
     *
     * @param mappingBean 任意类型的 Mapping
     * @return 不同类型返回不同, 分别为 Boolean / Map / List[Map]
     */
    private Object getMatched(MappingBean mappingBean) {

        if (mappingBean instanceof SimpleMappingBean) {

            SimpleMappingBean simpleMappingBean = (SimpleMappingBean) mappingBean;

            boolean result;

            RuleExpression ruleExpression = simpleMappingBean.getExpression();

            if (ruleExpression == null) result = false;
            else {
                List<RuleWord> ruleWords = ruleExpression.getRuleWordList();
                result = ruleWords != null && !ruleWords.isEmpty();
            }

            // 简单类型, 返回是否是有效映射
            return result;

        } else if (mappingBean instanceof ComplexMappingBean) {

            ComplexMappingBean complexMappingBean = (ComplexMappingBean) mappingBean;

            List<MappingBean> mappings = complexMappingBean.getSubMappings();

            if (mappings == null || mappings.isEmpty())
                return false;

            // 复杂类型, json 角度考虑, 返回其内部字段的结果对象
            return complexMappingBean.getSubMappings().stream()
                    .collect(toMap(MappingBean::getPlatformPropId, this::getMatched));

        } else if (mappingBean instanceof MultiComplexCustomMappingBean) {

            MultiComplexCustomMappingBean multiComplexCustomMappingBean =
                    (MultiComplexCustomMappingBean) mappingBean;

            List<MultiComplexCustomMappingValue> values = multiComplexCustomMappingBean.getValues();

            if (values == null || values.isEmpty())
                return false;

            return values.stream()
                    .map(value -> value.getSubMappings().stream()
                            .collect(toMap(MappingBean::getPlatformPropId, this::getMatched)))
                    .collect(toList());

        } else {
            throw new BusinessException("不支持的类型");
        }
    }

    /**
     * 查询 cartId 平台的 platformCategoryId 类目的属性 MappingType
     *
     * @param cartId             平台 ID
     * @param platformCategoryId 平台类目 ID
     * @return Mapping Map 属性 ID -> Type
     */
    Map<String, String> getMultiComplexFieldMappingType(Integer cartId, String platformCategoryId) {

        return platformMappingDeprecatedService.getPlatformSpecialField(cartId, platformCategoryId)
                .stream()
                .collect(toMap(CmsMtPlatformSpecialFieldModel::getFieldId, CmsMtPlatformSpecialFieldModel::getType));
    }

    /**
     * 保存一个 Mapping
     *
     * @param paramBean 参数模型
     * @param user      用户
     * @return 返回顶层 Mapping 模型
     * @throws TopSchemaException
     */
    MappingBean saveMapping(PlatformMappingBean paramBean, UserSessionBean user) throws TopSchemaException {

        MappingBean orgMappingBean = paramBean.getMappingBean();

        if (orgMappingBean == null)
            throw new BusinessException("没有传递任何 MappingBean");

        List<String> mappingPath = paramBean.getMappingPath();

        if (mappingPath == null || mappingPath.isEmpty())
            throw new BusinessException("没有 Mapping Path, 不知道该如何更新数据");

        CmsMtPlatformMappingModel platformMappingModel = getPlatformMapping(
                paramBean.getMainCategoryId(), paramBean.getPlatformCategoryId(), paramBean.getCartId(), user
        );

        MappingBean mappingBean = searchPropertyMappingByPath(platformMappingModel, paramBean.getMappingPath());

        if (mappingBean == null) {

            // 这里找不到的话, 要根据路径完整构建
            // 构建的话需要完整的属性树

            CmsMtPlatformCategorySchemaModel platformCatSchemaModel =
                    platformCategoryService.getPlatformCatSchema(platformMappingModel.getPlatformCategoryId(), paramBean.getCartId());

            // 转换类目属性
            Map<String, Field> fieldMap = SchemaReader.readXmlForMap(platformCatSchemaModel.getPropsItem());
            // 20160525 tom 同时保存product的信息 START
            if (platformCatSchemaModel.getPropsProduct() != null) {
                fieldMap.putAll(SchemaReader.readXmlForMap(platformCatSchemaModel.getPropsProduct()));
            }
            // 20160525 tom 同时保存product的信息 END

            // 查找并补全
            mappingBean = fixMappingStruct(platformMappingModel, fieldMap, mappingPath);
        } else if (!mappingBean.getMappingType().equals(orgMappingBean.getMappingType())) {
            throw new BusinessException("将要更新的和既存的类型不同");
        }

        updateFinalMapping(mappingBean, orgMappingBean);

        platformMappingDeprecatedService.savePlatformMapping(platformMappingModel);

        List<String> top = new ArrayList<>();
        top.add(mappingPath.get(0));
        return searchPropertyMappingByPath(platformMappingModel, top);
    }

    private void updateFinalMapping(MappingBean old, MappingBean $new) {

        switch (old.getMappingType()) {
            case MappingBean.MAPPING_SIMPLE:
                SimpleMappingBean oldSimple = (SimpleMappingBean) old;
                SimpleMappingBean newSimple = (SimpleMappingBean) $new;
                oldSimple.setExpression(newSimple.getExpression());
                break;
            case MappingBean.MAPPING_COMPLEX:
                ComplexMappingBean oldComplex = (ComplexMappingBean) old;
                ComplexMappingBean newComplex = (ComplexMappingBean) $new;
                oldComplex.setMasterPropId(newComplex.getMasterPropId());
                break;
        }
    }

    private MappingBean fixMappingStruct(CmsMtPlatformMappingModel platformMappingModel, Map<String, Field> fieldMap,
                                         List<String> mappingPath) {

        List<MappingBean> mappingBeanList = platformMappingModel.getProps();

        List<MultiComplexCustomMappingValue> values = null;

        Field field;

        MappingBean mappingBean = null;

        String mappingType = null;

        for (String propertyId : mappingPath) {

            if (mappingBeanList == null && values != null) {

                int index = Integer.valueOf(propertyId);

                if (values.size() < index) {
                    mappingBeanList = values.get(index).getSubMappings();
                } else {
                    MultiComplexCustomMappingValue value = new MultiComplexCustomMappingValue();
                    values.add(value);
                    mappingBeanList = value.getSubMappings();
                }

                continue;
            }

            if (mappingBeanList == null || fieldMap == null)
                return mappingBean;

            field = fieldMap.get(propertyId);

            mappingBean = mappingBeanList.stream()
                    .filter(m -> m.getPlatformPropId().equals(propertyId))
                    .findFirst()
                    .orElse(null);

            // 计算构建类型
            switch (field.getType()) {
                case INPUT:
                case MULTIINPUT:
                case MULTICHECK:
                case SINGLECHECK:
                case LABEL:
                    mappingType = MappingBean.MAPPING_SIMPLE;
                    fieldMap = null;
                    break;
                case COMPLEX:
                    mappingType = MappingBean.MAPPING_COMPLEX;
                    fieldMap = ((ComplexField) field).getFieldMap();
                    break;
                case MULTICOMPLEX:
                    mappingType = getMultiComplexFieldMappingType(
                            platformMappingModel.getPlatformCartId(),
                            platformMappingModel.getPlatformCategoryId(),
                            field.getId()
                    );
                    fieldMap = ((MultiComplexField) field).getFieldMap();
                    break;
            }

            // 为下一步准备数据
            switch (mappingType) {
                case MappingBean.MAPPING_SIMPLE:
                    if (mappingBean == null) {
                        mappingBean = new SimpleMappingBean();
                        mappingBean.setPlatformPropId(field.getId());
                    }
                    mappingBeanList.add(mappingBean);
                    mappingBeanList = null;
                    values = null;
                    break;
                case MappingBean.MAPPING_COMPLEX:
                    ComplexMappingBean complexMappingBean;
                    if (mappingBean == null) {
                        mappingBean = complexMappingBean = new ComplexMappingBean();
                        complexMappingBean.setPlatformPropId(field.getId());
                        complexMappingBean.setSubMappings(new ArrayList<>());
                        mappingBeanList.add(complexMappingBean);
                    } else {
                        complexMappingBean = (ComplexMappingBean) mappingBean;
                    }
                    mappingBeanList = complexMappingBean.getSubMappings();
                    values = null;
                    break;
                case MappingBean.MAPPING_MULTICOMPLEX_CUSTOM:
                    MultiComplexCustomMappingBean multiComplexCustomMappingBean;
                    if (mappingBean == null) {
                        mappingBean = multiComplexCustomMappingBean = new MultiComplexCustomMappingBean();
                        multiComplexCustomMappingBean.setPlatformPropId(field.getId());
                        multiComplexCustomMappingBean.setValues(new ArrayList<>());
                        mappingBeanList.add(multiComplexCustomMappingBean);
                    } else {
                        multiComplexCustomMappingBean = (MultiComplexCustomMappingBean) mappingBean;
                    }
                    mappingBeanList = null;
                    values = multiComplexCustomMappingBean.getValues();
                    break;
            }
        }

        return mappingBean;
    }

    /**
     * 使用 mappingPath 搜索一个 MappingBean
     *
     * @param platformMappingModel 完整的 Mapping 模型
     * @param mappingPath          属性 ID 的集合, 如果是 MultiComplexMapping 下的话, 则包含 Values 的下标
     * @return mappingPath 指向的 MappingBean
     */
    private MappingBean searchPropertyMappingByPath(CmsMtPlatformMappingModel platformMappingModel, List<String> mappingPath) {

        List<MappingBean> mappingBeen = platformMappingModel.getProps();

        List<MultiComplexCustomMappingValue> values = null;

        MappingBean mappingBean = null;

        for (String propertyId : mappingPath) {

            // 检查是不是批量复杂
            if (mappingBeen == null && values != null) {
                // 如果是, 则默认这次 propertyId 是 values 的 index
                int i = Integer.valueOf(propertyId);
                if (values.size() <= i) return null;
                mappingBeen = values.get(i).getSubMappings();
                continue;
            }

            if (mappingBeen == null)
                return null;

            // 搜索目标
            mappingBean = mappingBeen.stream()
                    .filter(m -> m.getPlatformPropId().equals(propertyId))
                    .findFirst()
                    .orElse(null);

            // 搜出来是空, 那就不用再继续了
            if (mappingBean == null)
                break;

            // 根据目标尝试为下一次准备
            switch (mappingBean.getMappingType()) {
                case MappingBean.MAPPING_SIMPLE:
                    mappingBeen = null;
                    values = null;
                    break;
                case MappingBean.MAPPING_COMPLEX:
                    mappingBeen = ((ComplexMappingBean) mappingBean).getSubMappings();
                    values = null;
                    break;
                case MappingBean.MAPPING_MULTICOMPLEX_CUSTOM:
                    mappingBeen = null;
                    values = ((MultiComplexCustomMappingBean) mappingBean).getValues();
                    break;
            }
        }

        return mappingBean;
    }

    private String getMultiComplexFieldMappingType(Integer cartId, String platformCategoryId, String propertyId) {

        String type = platformMappingDeprecatedService.getSpecialMappingType(cartId, platformCategoryId, propertyId);

        return StringUtils.isEmpty(type) ? "1" : type;
    }

    public int setMatchOver(String mainCategoryId, Integer matchOver, Integer cartId, UserSessionBean user) {

        CmsMtPlatformMappingModel platformMappingModel = platformMappingDeprecatedService.getMappingByMainCatId(user.getSelChannelId(), cartId, mainCategoryId);

        if (platformMappingModel == null)
            throw new BusinessException("没找到 Mapping");

        platformMappingModel.setMatchOver(matchOver);

        int res = platformMappingDeprecatedService.savePlatformMapping(platformMappingModel);

        return res > 0 ? matchOver : (matchOver == 1 ? 0 : 1);
    }
}
