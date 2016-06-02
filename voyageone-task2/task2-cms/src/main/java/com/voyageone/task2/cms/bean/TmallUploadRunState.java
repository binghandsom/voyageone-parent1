package com.voyageone.task2.cms.bean;

import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.value.ComplexValue;
import com.voyageone.task2.cms.bean.tcb.UploadProductTcb;
import com.voyageone.task2.cms.service.putaway.AbstractSkuFieldBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-7-10.
 */
public class TmallUploadRunState extends PlatformUploadRunState{
    private String brand_code;
    private long category_code;
    private boolean is_darwin;
    private List<String> product_code_list;
    // 产品编号
    private String style_code;
    private String product_code;

    private TmallContextBuildFields contextBuildFields;

    public TmallUploadRunState(UploadProductTcb uploadProductTcb) {
        this.uploadProductTcb = uploadProductTcb;
        contextBuildFields = new TmallContextBuildFields(this);
    }

    public class TmallContextBuildCustomFields extends PlatformContextBuildCustomFields{
        //自定义属性匹配的上下文
        private List<Field> customFields;
        private Map<String, Map.Entry<ComplexValue, String>> srcUrlColorExtendValueMap;

        //sku
        private AbstractSkuFieldBuilder skuFieldBuilder;
        private Object buildSkuResult;

        //商品数量
        private InputField quantityField;
        //商品价格
        private InputField priceField;

        public TmallContextBuildCustomFields() {
            customFields = new ArrayList<>();
        }

        public InputField getPriceField() {
            return priceField;
        }

        public void setPriceField(InputField priceField) {
            this.priceField = priceField;
        }

        public AbstractSkuFieldBuilder getSkuFieldBuilder() {
            return skuFieldBuilder;
        }

        public void setSkuFieldBuilder(AbstractSkuFieldBuilder skuFieldBuilder) {
            this.skuFieldBuilder = skuFieldBuilder;
        }

        public List<Field> getCustomFields() {
            return customFields;
        }

        public void setCustomFields(List<Field> customFields) {
            this.customFields = customFields;
        }

        public Map<String, Map.Entry<ComplexValue, String>> getSrcUrlColorExtendValueMap() {
            return srcUrlColorExtendValueMap;
        }

        public void setSrcUrlColorExtendValueMap(Map<String, Map.Entry<ComplexValue, String>> srcUrlColorExtendValueMap) {
            this.srcUrlColorExtendValueMap = srcUrlColorExtendValueMap;
        }

        public InputField getQuantityField() {
            return quantityField;
        }

        public void setQuantityField(InputField quantityField) {
            this.quantityField = quantityField;
        }

        public Object getBuildSkuResult() {
            return buildSkuResult;
        }

        public void setBuildSkuResult(Object buildSkuResult) {
            this.buildSkuResult = buildSkuResult;
        }
    }

    public static class UrlStashEntity {
        //可以是ComplexValue或InputField两种
        private Object stashObj;
        private String fieldId;

        public UrlStashEntity(String fieldId, Object stashObj) {
            this.fieldId = fieldId;
            this.stashObj = stashObj;
        }

        public String getFieldId() {
            return fieldId;
        }

        public Object getStashObj() {
            return stashObj;
        }
    }

    public class TmallContextBuildFields extends PlatformContextBuildFields {
        //自定义属性匹配的上下文
        private TmallContextBuildCustomFields contextBuildCustomFields;

        private Map<String, Field> fieldMap;
        private List<Field> customFields;
        //通过mapping关系匹配的Fields
        private List<Field> mappingFields;
        //Darwin 上下文
        private List<Field> darwinFields;

        // added by morse.lu 2016/05/17 start
        // 由于sku属性设值修正，临时添加，保存结果用
        private String xmlSkuData;
        public String getXmlSkuData() {
            return xmlSkuData;
        }
        public void setXmlSkuData(String xmlSkuData) {
            this.xmlSkuData = xmlSkuData;
        }
        // added by morse.lu 2016/05/17 end

        //key->srcUrl
        private Map<String, List<UrlStashEntity>> srcUrlStashEntityMap;


        public TmallContextBuildFields(TmallUploadRunState tmallUploadRunState) {
            fieldMap = new HashMap<>();
            customFields = new ArrayList<>();
            darwinFields = new ArrayList<>();
            mappingFields = new ArrayList<>();
            srcUrlStashEntityMap = new HashMap<>();

            contextBuildCustomFields = new TmallContextBuildCustomFields();
            contextBuildCustomFields.setPlatformContextBuildFields(this);

            this.platformUploadRunState = tmallUploadRunState;
        }

        public void clearContext()
        {
            fieldMap.clear();
            customFields.clear();
            darwinFields.clear();
            mappingFields.clear();
            srcUrlStashEntityMap.clear();
        }

        public Map<String, Field> getFieldMap() {
            return fieldMap;
        }

        public void setFieldMap(Map<String, Field> fieldMap) {
            this.fieldMap = fieldMap;
        }

        public List<Field> getMappingFields() {
            return mappingFields;
        }

        public Map<String, List<UrlStashEntity>> getSrcUrlStashEntityMap() {
            return srcUrlStashEntityMap;
        }

        public void addCustomField(Field customField)
        {
            customFields.add(customField);
        }

        public TmallContextBuildCustomFields getContextBuildCustomFields() {
            return contextBuildCustomFields;
        }

        public void setContextBuildCustomFields(TmallContextBuildCustomFields contextBuildCustomFields) {
            this.contextBuildCustomFields = contextBuildCustomFields;
        }

        public List<Field> getCustomFields() {
            return customFields;
        }

        public void setCustomFields(List<Field> customFields) {
            this.customFields = customFields;
        }

        public List<Field> getDarwinFields() {
            return darwinFields;
        }

        public void setDarwinFields(List<Field> darwinFields) {
            this.darwinFields = darwinFields;
        }
    }

    public String getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(String brand_code) {
        this.brand_code = brand_code;
    }

    public long getCategory_code() {
        return category_code;
    }

    public void setCategory_code(long category_code) {
        this.category_code = category_code;
    }

    public void setIs_darwin(boolean is_darwin) {
        this.is_darwin = is_darwin;
    }

    public TmallContextBuildFields getContextBuildFields() {
        return contextBuildFields;
    }

    public void setContextBuildFields(TmallContextBuildFields contextBuildFields) {
        this.contextBuildFields = contextBuildFields;
    }

    public String getStyle_code() {
        return style_code;
    }

    public void setStyle_code(String style_code) {
        this.style_code = style_code;
    }

    public List<String> getProduct_code_list() {
        return product_code_list;
    }

    public void setProduct_code_list(List<String> product_code_list) {
        this.product_code_list = product_code_list;
    }

    public boolean is_darwin() {
        return is_darwin;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
