package com.voyageone.components.tmall.service;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.*;
import com.taobao.top.schema.value.ComplexValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voyageone.components.tmall.service.TbConstants.sizeSortMap;

/**
 * 淘宝商品信息操作辅助类
 * Created by Jonas on 7/29/15.
 */
public class TbItemSchema {
    private List<Field> fields;

    private Map<String, Field> fieldMap;

    private long num_iid;

    TbItemSchema(long num_iid, List<Field> fields) {
        this.num_iid = num_iid;
        this.fields = fields;
    }

    public long getNum_iid() {
        return num_iid;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Map<String, Field> getFieldMap() {

        if (fieldMap != null)
            return fieldMap;

        fieldMap = new HashMap<>();

        for (Field field : fields)
            fieldMap.put(field.getId(), field);

        return fieldMap;
    }

    /**
     * 将所有默认值转换为属性值
     */
    public void setFieldValue() {
        // 将所有 Field 的默认值，设置到其值上。等待后续更新提交
        fields.forEach(this::setFieldValue);
    }

    /**
     * 辅助方法：在更新淘宝商品时，全量更新需要将不更改的值，从 Default Value 中设置到 Valued
     */
    private void setFieldValue(Field field) {
        // 对特定字段进行处理
        if (setSpecialFieldValue(field))
            return;

        switch (field.getType()) {
            case INPUT:
                InputField inputField = (InputField) field;
                inputField.setValue(inputField.getDefaultValue());
                break;
            case MULTIINPUT:
                MultiInputField multiInputField = (MultiInputField) field;
                multiInputField.setValues(multiInputField.getDefaultValues());
                break;
            case MULTICHECK:
                MultiCheckField multiCheckField = (MultiCheckField) field;
                multiCheckField.setValues(multiCheckField.getDefaultValuesDO());
                break;
            case SINGLECHECK:
                SingleCheckField singleCheckField = (SingleCheckField) field;
                singleCheckField.setValue(singleCheckField.getDefaultValue());
                break;
            case COMPLEX:
                ComplexField complexField = (ComplexField) field;
                complexField.setComplexValue(complexField.getDefaultComplexValue());
                break;
            case MULTICOMPLEX:
                MultiComplexField multiComplexField = (MultiComplexField) field;
                multiComplexField.setComplexValues(multiComplexField.getDefaultComplexValues());
                break;
            default:
                break;
        }
    }

    private boolean setSpecialFieldValue(Field field) {
        // 暂时只有 sku 排序, 所以这里是简写写法
        // 后续请在此修改, 对更多字段提供支持
        return "darwin_sku".equals(field.getId()) && setSortedSku(field);
    }

    private boolean setSortedSku(Field field) {
        // 如果不是这种类型, 那么返回 false 按默认方式操作
        if (!field.getType().equals(FieldTypeEnum.MULTICOMPLEX))
            return false;

        MultiComplexField multiComplexField = (MultiComplexField) field;

        List<ComplexValue> complexValues = multiComplexField.getDefaultComplexValues();

        // 依据尺码进行 SKU 排序
        if (!complexValues.isEmpty()) complexValues.sort((v1, v2) -> sizeIndex(v1).compareTo(sizeIndex(v2)));

        multiComplexField.setComplexValues(complexValues);

        return true;
    }

    private Integer sizeIndex(ComplexValue complexValue) {
        String outerId = complexValue.getInputFieldValue("sku_outerId");
        if (StringUtils.isEmpty(outerId)) return 999;
        if (!outerId.contains("-") || outerId.endsWith("-")) return 999;
        String sizeName = outerId.substring(outerId.lastIndexOf("-") + 1);
        Integer index = sizeSortMap.get(sizeName);
        return index == null ? 999 : index;
    }

    /**
     * 设置商品的主图，返回商品的原主图地址
     *
     * @param imageUrls 主图地址
     */
    public void setMainImage(Map<Integer, String> imageUrls) {

        // 找到第一个节点。否则为空
        Field field = getFieldMap().get("item_images");

        ComplexField complexField = (ComplexField) field;

        ComplexValue complexValue = complexField.getComplexValue();

        for (Map.Entry<Integer, String> imageUrl : imageUrls.entrySet()) {

            int index = imageUrl.getKey();

            // 不在 1-5 范围内说明数据本身有问题,直接无视
            if (index < 1 || index > 5) return;

            complexValue.setInputFieldValue("item_image_" + String.valueOf(index - 1), imageUrl.getValue());
        }
    }

    private Field getVerticalImage() {
        return getFieldMap().get("vertical_image");
    }

    public boolean hasVerticalImage() {
        return getFieldMap().containsKey("vertical_image");
    }

    public void setVerticalImage(String taobaoVerticalImageUrl) {
        InputField inputField = (InputField) getVerticalImage();
        inputField.setValue(taobaoVerticalImageUrl);
    }
}
