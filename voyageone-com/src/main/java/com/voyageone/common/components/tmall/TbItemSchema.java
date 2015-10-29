package com.voyageone.common.components.tmall;

import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.*;
import com.taobao.top.schema.value.ComplexValue;

import java.util.List;
import java.util.Map;

/**
 * 淘宝商品信息操作辅助类
 * Created by Jonas on 7/29/15.
 */
public class TbItemSchema {
    private List<Field> fields;

    private long num_iid;

    protected TbItemSchema(long num_iid, List<Field> fields) {
        this.num_iid = num_iid;
        this.fields = fields;
    }

    public long getNum_iid() {
        return num_iid;
    }

    public List<Field> getFields() {
        return fields;
    }

    /**
     * 将所有默认值转换为属性值
     */
    private void setFieldValue() {
        // 将所有 Field 的默认值，设置到其值上。等待后续更新提交
        fields.stream().forEach(this::setFieldValue);
    }

    /**
     * 辅助方法：在更新淘宝商品时，全量更新需要将不更改的值，从 Default Value 中设置到 Valued
     */
    private void setFieldValue(Field field) {
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
        }
    }

    /**
     * 获取商品的默认价格
     */
    public String getDefaultPrice() {
        Field field = fields.stream().filter(f -> f.getId().equals("sku_price")).findFirst().orElse(null);

        if (field == null || !field.getType().equals(FieldTypeEnum.INPUT)) return null;

        InputField inputField = (InputField) field;

        return inputField.getDefaultValue();
    }

    /**
     * 设置商品的主图，返回商品的原主图地址
     *
     * @param imageUrls 主图地址
     */
    public void setMainImage(Map<Integer, String> imageUrls) {

        // 如果更新默认值到值上，则覆盖后续的设置。所以要在这之前设置
        setFieldValue();

        // 找到第一个节点。否则为空
        Field field = fields.stream().filter(f -> f.getId().equals("item_images")).findFirst().orElse(null);

        ComplexField complexField = (ComplexField) field;

        ComplexValue complexValue = complexField.getComplexValue();

        for (Map.Entry<Integer, String > imageUrl: imageUrls.entrySet()) {

            int index = imageUrl.getKey();

            // 不在 1-5 范围内说明数据本身有问题,直接无视
            if (index < 1 || index > 5) return;

            complexValue.setInputFieldValue("item_image_" + String.valueOf(index - 1), imageUrl.getValue());
        }
    }

}
