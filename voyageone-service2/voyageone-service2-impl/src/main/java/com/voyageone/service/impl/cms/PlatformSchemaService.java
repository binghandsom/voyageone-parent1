package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsMtPlatformPropMappingCustomDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryExtendFieldDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryInvisibleFieldDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtPlatformPropMappingCustomModel;
import com.voyageone.service.model.cms.mongo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台Schema取得后，对应一些处理操作
 *
 * @author morse.lu 2016/06/22
 * @version 2.1.0
 * @since 2.1.0
 */
@Service
public class PlatformSchemaService extends BaseService {

    public static final String KEY_PRODUCT = "product";
    public static final String KEY_ITEM = "item";

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CmsMtPlatformPropMappingCustomDao cmsMtPlatformPropMappingCustomDao;
    @Autowired
    private CmsMtPlatformCategoryExtendFieldDao cmsMtPlatformCategoryExtendFieldDao;
    @Autowired
    private CmsMtPlatformCategoryInvisibleFieldDao cmsMtPlatformCategoryInvisibleFieldDao;

    /**
     * 产品画面属性list取得
     */
    public Map<String, List<Field>> getFieldForProductImage(String catId, int cartId) {
        if (CartEnums.Cart.JM.getValue() == cartId) {
            // 聚美的场合，因为只有一个catId，写死 catId = 1
            catId = "1";
        }

        CmsMtPlatformCategorySchemaModel platformCatSchemaModel = platformCategoryService.getPlatformCatSchema(catId, cartId);
        if (platformCatSchemaModel == null) {
            return null;
        }
        CmsMtPlatformCategoryInvisibleFieldModel invisibleFieldModel = cmsMtPlatformCategoryInvisibleFieldDao.selectOneByCatId(catId, cartId);
        CmsMtPlatformCategoryExtendFieldModel extendFieldModel = cmsMtPlatformCategoryExtendFieldDao.selectOneByCatId(catId, cartId);

        Map<String, List<Field>> retMap = new HashMap<>();

        // 产品
        String schemaProduct = platformCatSchemaModel.getPropsProduct();
        if (CartEnums.Cart.JG.getValue() == cartId || CartEnums.Cart.JGJ.getValue() == cartId || CartEnums.Cart.JGY.getValue() == cartId) {
            // 京东的场合，产品schema是共通，写死 catId = 1
            CmsMtPlatformCategorySchemaModel platformCatSchemaModelJD = platformCategoryService.getPlatformCatSchema("1", cartId);
            if (platformCatSchemaModelJD == null) {
                $error("JD的产品schema未设定!");
                return null;
            } else {
                schemaProduct = platformCatSchemaModelJD.getPropsProduct();
            }
        }
        if (!StringUtils.isEmpty(schemaProduct)) {
            List<Field> listProductField = SchemaReader.readXmlForList(schemaProduct);
            retMap.put(KEY_PRODUCT, getListFieldForProductImage(listProductField,
                                                                    invisibleFieldModel != null ? invisibleFieldModel.getPropsProduct() : null,
                                                                    extendFieldModel != null ? extendFieldModel.getPropsProduct() : null));
        }

        // 商品
        String schemaItem = platformCatSchemaModel.getPropsItem();
        if (!StringUtils.isEmpty(schemaItem)) {
            List<Field> listItemField = SchemaReader.readXmlForList(schemaItem);
            retMap.put(KEY_ITEM, getListFieldForProductImage(listItemField,
                                                                invisibleFieldModel != null ? invisibleFieldModel.getPropsItem() : null,
                                                                extendFieldModel != null ? extendFieldModel.getPropsItem() : null));
        }

        return retMap;
    }

    /**
     * 产品画面属性list取得
     * 产品画面要分产品商品，所以检索表都放在外部去做，listField,listInvisibleField,listExtendField作为参数传入
     *
     * @param listField 平台产品或商品的fields
     * @param listInvisibleField 不想显示的fields
     * @param listExtendField 增加的fields
     * @return
     */
    private List<Field> getListFieldForProductImage(List<Field> listField, List<CmsMtPlatformCategoryInvisibleFieldModel_Field> listInvisibleField, List<CmsMtPlatformCategoryExtendFieldModel_Field> listExtendField) {
        Map<String, Field> mapField = new HashMap<>();
        for (Field field : listField){
            mapField.put(field.getId(), field);
        }

        // 删除不想显示的属性
        if (listInvisibleField != null && !listInvisibleField.isEmpty()) {
            listInvisibleField.forEach(invisibleFieldModel -> getFieldById(mapField, StringUtil.replaceToDot(invisibleFieldModel.getFieldId()), CmsMtPlatformCategoryInvisibleFieldModel_Field.SEPARATOR, true));
        }

        // 增加属性
        if (listExtendField != null && !listExtendField.isEmpty()) {
            listExtendField.forEach(extendFieldModel -> {
                Field addField = extendFieldModel.getField();
                FieldUtil.convertFieldIdToDot(addField); // 把field中的【->】替换成【.】
                addExtendField(mapField, StringUtil.replaceToDot(extendFieldModel.getParentFieldId()), CmsMtPlatformCategoryExtendFieldModel_Field.SEPARATOR, addField);
            });
        }

        List<Field> retList = new ArrayList<>();
        mapField.forEach((key, value) -> retList.add(value));

        FieldUtil.replaceFieldIdDot(retList); // 把field中的【.】替换成【->】

        return retList;
    }

    /**
     * Mapping画面属性Map取得
     */
    public Map<String, Field> getMapFieldForMappingImage(String catId, int cartId) throws Exception {
        if (CartEnums.Cart.JM.getValue() == cartId) {
            // 聚美的场合，因为只有一个catId，写死 catId = 1
            catId = "1";
        }

        Map<String, Field> retMap = new HashMap<>();

        CmsMtPlatformCategoryInvisibleFieldModel invisibleFieldModel = cmsMtPlatformCategoryInvisibleFieldDao.selectOneByCatId(catId, cartId);
        CmsMtPlatformCategorySchemaModel platformCatSchemaModel = platformCategoryService.getPlatformCatSchema(catId, cartId);
        if (invisibleFieldModel == null || platformCatSchemaModel == null) {
            return retMap;
        }

        // 从cms_mt_platform_prop_mapping查找，该属性是否在范围，如果在，那么不显示在Mapping画面，直接会做特殊处理，所以不加进Map
        List<CmsMtPlatformPropMappingCustomModel> cmsMtPlatformPropMappingCustomModels = cmsMtPlatformPropMappingCustomDao.selectList(new HashMap<String, Object>(){{put("cartId", cartId);}});
        List<String> listCustomField = new ArrayList<>();
        cmsMtPlatformPropMappingCustomModels.forEach(model -> listCustomField.add(model.getPlatformPropId()));

        // 产品
        String schemaProduct = platformCatSchemaModel.getPropsProduct();
        if (CartEnums.Cart.JG.getValue() == cartId || CartEnums.Cart.JGJ.getValue() == cartId || CartEnums.Cart.JGY.getValue() == cartId) {
            // 京东的场合，产品schema是共通，写死 catId = 1
            CmsMtPlatformCategorySchemaModel platformCatSchemaModelJD = platformCategoryService.getPlatformCatSchema("1", cartId);
            if (platformCatSchemaModelJD == null) {
                $error("JD的产品schema未设定!");
                return null;
            } else {
                schemaProduct = platformCatSchemaModelJD.getPropsProduct();
            }
        }
        if (!StringUtils.isEmpty(schemaProduct)) {
            Map<String, Field> mapProductField = SchemaReader.readXmlForMap(schemaProduct);
            List<CmsMtPlatformCategoryInvisibleFieldModel_Field> listInvisibleField = invisibleFieldModel.getPropsProduct();
            if (listInvisibleField != null && !listInvisibleField.isEmpty()) {
                addMappingMap(retMap, listInvisibleField, mapProductField, listCustomField);
            }
        }

        // 商品
        String schemaItem = platformCatSchemaModel.getPropsItem();
        if (!StringUtils.isEmpty(schemaItem)) {
            Map<String, Field> mapItemField = SchemaReader.readXmlForMap(schemaItem);
            List<CmsMtPlatformCategoryInvisibleFieldModel_Field> listInvisibleField = invisibleFieldModel.getPropsItem();
            if (listInvisibleField != null && !listInvisibleField.isEmpty()) {
                addMappingMap(retMap, listInvisibleField, mapItemField, listCustomField);
            }
        }

        retMap.forEach((key, value) -> FieldUtil.replaceFieldIdDot(value)); // 把field中的【.】替换成【->】

        return retMap;
    }

    private void addMappingMap(Map<String, Field> retMap, List<CmsMtPlatformCategoryInvisibleFieldModel_Field> listInvisibleField, Map<String, Field> mapPlatformField, List<String> listCustomField) throws Exception {
        String separator = CmsMtPlatformCategoryInvisibleFieldModel_Field.SEPARATOR;
        for (CmsMtPlatformCategoryInvisibleFieldModel_Field invisibleField : listInvisibleField) {
            String fieldId = StringUtil.replaceToDot(invisibleField.getFieldId());
            String[] fieldIds = fieldId.split(separator);
            if (listCustomField.contains(fieldIds[fieldIds.length - 1])) {
                // 不想显示的属性是特殊处理对象,不用显示在Mapping画面,不要加进Map，跳过
                continue;
            }
            if (getFieldById(mapPlatformField, fieldId, separator, false) != null) {
                // 找到不想显示的属性
                addMapMapping(retMap, fieldId, separator, mapPlatformField);
            }
        }
    }

    private void addMapMapping(Map<String, Field> retMap, String fieldId, String separator, Map<String, Field> mapPlatformField) throws Exception {
        String[] fieldIds = fieldId.split(separator);
        String newFieldId = "";
        Map<String, Field> newMapPlatformField = mapPlatformField;
        Field newPlatformField;
        Field addField;

        for(int index = 0; index < fieldIds.length; index++) {
            String field_id = fieldIds[index];
            // 一层一层的加
            newPlatformField = newMapPlatformField.get(field_id);
            addField = deepCloneField(newPlatformField); // copy出来
            if (addField.getType() == FieldTypeEnum.COMPLEX) {
                ComplexField complexField = (ComplexField) addField;
                newMapPlatformField = complexField.getFieldMap();
                if (index + 1 != fieldIds.length) {
                    // 最后一层不清
                    complexField.clear();
                }
            } else if (addField.getType() == FieldTypeEnum.MULTICOMPLEX) {
                MultiComplexField multiComplexField = (MultiComplexField) addField;
                newMapPlatformField = multiComplexField.getFieldMap();
                if (index + 1 != fieldIds.length) {
                    // 最后一层不清
                    multiComplexField.clear();
                }
            }

            // ★★★注意★★★：addExtendField方法是不会覆盖已经有了的属性，所有才可以这么做，否则代码需要修正，一层层加的时候要判断这一层是否已经加过了
            addExtendField(retMap, newFieldId, separator, addField);

            if (!StringUtils.isEmpty(newFieldId)) {
                newFieldId = newFieldId + separator;
            }
            newFieldId = newFieldId + field_id;
        }
    }

    /**
     * 取得指定Field
     *
     * @param mapField schema转换成的Map
     * @param fieldId 一级属性>二级属性>三级属性
     * @param separator fieldId的分隔符
     * @param isNeedDelete true的话，检索到的同时在Map里删除
     * @return Field
     */
    public Field getFieldById(Map<String, Field> mapField, String fieldId, String separator, boolean isNeedDelete) {
        String[] fieldIds;
        if (separator == null) {
            // 一级属性
            fieldIds = new String[]{fieldId};
        } else {
            fieldIds = fieldId.split(separator);
        }

        Field field = mapField.get(fieldIds[0]);
        if (field == null) {
            $info("没有找到指定的field");
            return null;
        }
        if (fieldIds.length == 1) {
            // 最后一层啦
            if (isNeedDelete) {
                // map里删除
                mapField.remove(field.getId());
            }
            return field;
        } else {
            String newFieldId = fieldIds[1];
            for (int i = 2; i < fieldIds.length; i++) {
                newFieldId = newFieldId + separator + fieldIds[i];
            }

            if (field.getType() == FieldTypeEnum.COMPLEX) {
                ComplexField complexField = (ComplexField) field;

                Field findField = getFieldById(complexField.getFieldMap(), newFieldId, separator, isNeedDelete);
                if (findField != null && fieldIds.length == 2) {
                    // 找到了, fieldIds.length == 2表示之后一次递归是最后一层啦
                    if (isNeedDelete) {
                        // map和list里删除
                        complexField.getFieldMap().remove(newFieldId);
                        complexField.getFields().remove(findField);
                    }
                }
                return findField;
            } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
                MultiComplexField multiComplexField = (MultiComplexField) field;
                Field findField = getFieldById(multiComplexField.getFieldMap(), newFieldId, separator, isNeedDelete);
                if (findField != null && fieldIds.length == 2) {
                    // 找到了, fieldIds.length == 2表示之后一次递归是最后一层啦
                    if (isNeedDelete) {
                        // list里删除
                        multiComplexField.getFields().remove(findField);
                    }
                }
                return findField;
            } else {
                $warn(String.format("field_id=%s 不是复杂类型,没有下一层属性,无法取得%s", fieldIds[0], fieldId));
                return null;
            }
        }
    }

    /**
     * 增加指定Field(暂时做成相同id存在，不会增加不会覆盖)
     *
     * @param mapField schema转换成的Map
     * @param fieldId  需要增加的属性结构，例：一级属性>二级属性,Field增加到一级属性>二级属性属性下面
     * @param separator fieldId的分隔符
     * @param addField 需要增加的Field
     */
    public void addExtendField(Map<String, Field> mapField, String fieldId, String separator, Field addField) {
        if (StringUtils.isEmpty(fieldId)) {
            // 要加在根属性下
            mapField.putIfAbsent(StringUtil.replaceDot(addField.getId()), addField);
            return;
        }

        String[] fieldIds;
        if (separator == null) {
            // 一级属性
            fieldIds = new String[]{fieldId};
        } else {
            fieldIds = fieldId.split(separator);
        }

        Field field = mapField.get(fieldIds[0]);
        if (field == null) {
            $warn("没有找到指定的field");
            return;
        }
        if (fieldIds.length == 1) {
            // 最后一层啦
            if (field.getType() == FieldTypeEnum.COMPLEX) {
                ComplexField complexField = (ComplexField) field;
                if (complexField.getFieldMap().get(addField.getId()) == null) {
                    complexField.add(addField);
                } else {
                    $warn("原先相同的field_id已经存在!");
                }
                return;
            } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
                MultiComplexField multiComplexField = (MultiComplexField) field;
                if (multiComplexField.getFieldMap().get(addField.getId()) == null) {
                    multiComplexField.add(addField);
                } else {
                    $warn("原先相同的field_id已经存在!");
                }
                return;
            } else {
                $warn(String.format("field_id=%s 不是复杂类型,无法增加属性", field.getId()));
                return;
            }
        } else {
            String newFieldId = fieldIds[1];
            for (int i = 2; i < fieldIds.length; i++) {
                newFieldId = newFieldId + separator + fieldIds[i];
            }

            if (field.getType() == FieldTypeEnum.COMPLEX) {
                ComplexField complexField = (ComplexField) field;
                addExtendField(complexField.getFieldMap(), newFieldId, separator, addField);
            } else if (field.getType() == FieldTypeEnum.MULTICOMPLEX) {
                MultiComplexField multiComplexField = (MultiComplexField) field;
                addExtendField(multiComplexField.getFieldMap(), newFieldId, separator, addField);
            } else {
                $warn(String.format("field_id=%s 不是复杂类型,没有下一层属性,无法增加属性", field.getId()));
                return;
            }
        }
    }

    private Field deepCloneField(Field field) throws Exception {
        try {
            return SchemaReader.elementToField(field.toElement());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
