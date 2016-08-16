package com.voyageone.web2.cms.views.tools.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.asserts.Assert;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.value.Value;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.tools.PlatformMappingService;
import com.voyageone.service.model.cms.mongo.CmsBtPlatformMappingModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.tools.product.PlatformMappingSaveBean;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by jonas on 8/15/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
@Service
class PlatformMappingViewService extends BaseAppService {

    private final PlatformMappingService platformMappingService;

    @Autowired
    public PlatformMappingViewService(PlatformMappingService platformMappingService) {
        this.platformMappingService = platformMappingService;
    }

    public Map<String, Object> page(Integer cartId, Integer categoryType, String categoryPath, int page, int size, UserSessionBean userSessionBean) {

        Assert.notNull(cartId).elseThrowDefaultWithTitle("cartId");
        Assert.notNull(categoryType).elseThrowDefaultWithTitle("categoryType");

        ChannelConfigEnums.Channel channel = userSessionBean.getSelChannel();

        Map<String, Object> result = new HashMap<>();

        List<CmsBtPlatformMappingModel> list;

        long total;

        if (categoryType == 1) {

            list = new ArrayList<>(1);

            CmsBtPlatformMappingModel common = platformMappingService.getCommon(channel, cartId);

            if (common == null) {
                total = 0;
            } else {
                total = 1;
                list.add(common);
            }
        } else {

            list = platformMappingService.getPage(channel, cartId, categoryPath, page, size);

            total = platformMappingService.getCount(channel, cartId, categoryPath);
        }

        result.put("list", list);

        result.put("total", total);

        return result;
    }

    public boolean save(PlatformMappingSaveBean platformMappingSaveBean, UserSessionBean user) {

        String username = user.getUserName();

        Integer typeObject = platformMappingSaveBean.getCategoryType();

        String categoryPath = platformMappingSaveBean.getCategoryPath();

        int type = 0;

        if (typeObject != null)
            type = typeObject;

        if (!StringUtils.isEmpty(categoryPath))
            type = PlatformMappingService.CATEGORY_TYPE_SPECIFIC;
        else if (type != PlatformMappingService.CATEGORY_TYPE_SPECIFIC && type != PlatformMappingService.CATEGORY_TYPE_COMMON)
            throw new BusinessException("类目类型错误");

        typeObject = type;

        // 创建模型
        // 先用于查询
        CmsBtPlatformMappingModel platformMappingModel = new CmsBtPlatformMappingModel();

        platformMappingModel.setCartId(platformMappingSaveBean.getCartId());
        platformMappingModel.setCategoryType(typeObject);
        platformMappingModel.setCategoryPath(categoryPath);
        platformMappingModel.setChannelId(user.getSelChannelId());

        CmsBtPlatformMappingModel _platformMappingModel = platformMappingService.get(platformMappingModel,
                user.getSelChannelId());

        // 如果已有数据, 使用数据库数据覆盖查询模型
        // 供后续使用
        // 如果没有数据, 需要为查询模型补全数据
        // 后续插入

        if (_platformMappingModel != null)
            platformMappingModel = _platformMappingModel;
        else
            platformMappingModel.setCreater(username);

        platformMappingModel.setModifier(username);
        platformMappingModel.setModified(DateTimeUtil.getNowTimeStamp());

        // 依次循环 product 和 item 两组 schema 数据
        // 填充到模型中

        PlatformMappingSaveBean.Schema schema = platformMappingSaveBean.getSchema();

        List<CmsBtPlatformMappingModel.FieldMapping> mappingList = platformMappingModel.getMappings();

        Map<String, CmsBtPlatformMappingModel.FieldMapping> mappingMap;

        if (mappingList == null || mappingList.isEmpty())
            mappingMap = new HashMap<>();
        else
            mappingMap = mappingList.stream().collect(toMap(CmsBtPlatformMappingModel.FieldMapping::getFieldId, m -> m));

        List<Field> item = getStrongSchema(schema.getItem());

        fillMapping(mappingMap, item);

        List<Field> product = getStrongSchema(schema.getProduct());

        fillMapping(mappingMap, product);

        mappingList = mappingMap.entrySet().stream().map(Map.Entry::getValue).collect(toList());

        platformMappingModel.setMappings(mappingList);

        return platformMappingService.saveMap(platformMappingModel);
    }

    private void fillMapping(Map<String, CmsBtPlatformMappingModel.FieldMapping> mappingMap, List<Field> fieldList) {

        // 遍历传回来的每一个 Field
        // 根据类型, 获取内容, 并保存到 Map
        // 对于 Check 类的, 只有固定值
        // Input 的, 只有匹配关系

        for (Field field: fieldList) {

            String fieldId = field.getId();

            CmsBtPlatformMappingModel.FieldMapping mapping = mappingMap.get(fieldId);

            if (mapping == null) {
                mapping = new CmsBtPlatformMappingModel.FieldMapping();
                mapping.setFieldId(fieldId);
                mappingMap.put(fieldId, mapping);
            }

            switch (field.getType()) {

                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField) field;
                    Value valueObject = singleCheckField.getValue();
                    String value = valueObject.getValue();
                    mapping.setValue(value);
                    break;
                case MULTICHECK:
                    MultiCheckField multiCheckField = (MultiCheckField) field;
                    List<Value> valueObjectList = multiCheckField.getValues();
                    List<String> valueList = valueObjectList.stream().map(Value::getValue).collect(toList());
                    mapping.setValue(valueList);
                    break;
                case COMPLEX:
                    ComplexField complexField = (ComplexField) field;
                    List<Field> children = complexField.getFields();
                    fillMapping(mappingMap, children);
                    break;
                case INPUT:
                    InputField inputField = (InputField) field;
                    setExpressionList(mapping, inputField.getValue());
                    break;
            }

        }
    }

    private void setExpressionList(CmsBtPlatformMappingModel.FieldMapping mapping, String json) {
        if (StringUtils.isEmpty(json))
            return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<CmsBtPlatformMappingModel.FieldMappingExpression> expressionList = mapper.readValue(json,
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, CmsBtPlatformMappingModel.FieldMappingExpression.class));
            mapping.setExpressions(expressionList);
            mapping.setValue(null);
        } catch (IOException e) {
            throw new BusinessException("读取 ExpressionList 出错", e);
        }
    }

    private List<Field> getStrongSchema(List<Map<String, Object>> weakSchema) {
        return SchemaJsonReader.readJsonForList(weakSchema);
    }
}
