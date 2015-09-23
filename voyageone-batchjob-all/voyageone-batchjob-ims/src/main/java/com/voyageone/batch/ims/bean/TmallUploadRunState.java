package com.voyageone.batch.ims.bean;

import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.taobao.top.schema.value.ComplexValue;
import com.voyageone.batch.ims.bean.tcb.UploadProductTcb;
import com.voyageone.batch.ims.modelbean.CmsCodePropBean;
import com.voyageone.batch.ims.modelbean.CmsSkuPropBean;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.batch.ims.modelbean.PropValueBean;
import com.voyageone.batch.ims.service.AbstractSkuFieldBuilder;

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

    private List<PlatformPropBean> platformProps;
    private Map<PlatformPropBean, List<String>> platformPropValueMap;

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
        private Map<String, CmsCodePropBean> usingColorMap;
        private Map<String, CmsSkuPropBean> usingSkuMap;
        private Map<String, Map.Entry<CmsCodePropBean, CmsSkuPropBean>> usingRingSizeMap;
        private Map<String, ComplexValue> srcUrlExtendCodePropMap;

        //商品数量
        private InputField quantityField;
        //商品价格
        private InputField priceField;

        public TmallContextBuildCustomFields() {
            usingColorMap = new HashMap<>();
            usingSkuMap = new HashMap<>();
            usingRingSizeMap = new HashMap<>();
            srcUrlExtendCodePropMap = new HashMap<>();
        }

        public InputField getPriceField() {
            return priceField;
        }

        public void setPriceField(InputField priceField) {
            this.priceField = priceField;
        }

        public Map<String, ComplexValue> getSrcUrlExtendCodePropMap() {
            return srcUrlExtendCodePropMap;
        }

        public AbstractSkuFieldBuilder getSkuFieldBuilder() {
            return skuFieldBuilder;
        }

        public void setSkuFieldBuilder(AbstractSkuFieldBuilder skuFieldBuilder) {
            this.skuFieldBuilder = skuFieldBuilder;
        }

        public void setSrcUrlExtendCodePropMap(Map<String, ComplexValue> srcUrlExtendCodePropMap) {
            this.srcUrlExtendCodePropMap = srcUrlExtendCodePropMap;
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

        public Map<String, CmsCodePropBean> getUsingColorMap() {
            return usingColorMap;
        }

        public void setUsingColorMap(Map<String, CmsCodePropBean> usingColorMap) {
            this.usingColorMap = usingColorMap;
        }

        public Map<String, Map.Entry<CmsCodePropBean, CmsSkuPropBean>> getUsingRingSizeMap() {
            return usingRingSizeMap;
        }

        public void setUsingRingSizeMap(Map<String, Map.Entry<CmsCodePropBean, CmsSkuPropBean>> usingRingSizeMap) {
            this.usingRingSizeMap = usingRingSizeMap;
        }

        public Map<String, CmsSkuPropBean> getUsingSkuMap() {
            return usingSkuMap;
        }

        public void setUsingSkuMap(Map<String, CmsSkuPropBean> usingSkuMap) {
            this.usingSkuMap = usingSkuMap;
        }
    }

    public class TmallContextBuildFields extends PlatformContextBuildFields {
        //自定义属性匹配的上下文
        private TmallContextBuildCustomFields contextBuildCustomFields;
        private List<Field> customFields;

        //Darwin 上下文
        private List<Field> darwinFields;

        //主数据匹配字段处理的字段上下文
        private List<PlatformPropBean> platformProps;
        private Map<PlatformPropBean, List<PropValueBean>> platformPropBeanValueMap;

        //通过主数据的mapping_type来处理字段的上下文
        //private ;


        public TmallContextBuildFields(TmallUploadRunState tmallUploadRunState) {
            customFields = new ArrayList<>();
            darwinFields = new ArrayList<>();
            platformPropBeanValueMap = new HashMap<>();

            contextBuildCustomFields = new TmallContextBuildCustomFields();
            contextBuildCustomFields.setPlatformContextBuildFields(this);

            this.platformUploadRunState = tmallUploadRunState;
        }

        public void clearContext()
        {
            customFields.clear();
            darwinFields.clear();
            platformPropBeanValueMap.clear();
            if (platformProps != null)
                platformProps.clear();
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

        public List<PlatformPropBean> getPlatformProps() {
            return platformProps;
        }

        public void setPlatformProps(List<PlatformPropBean> platformProps) {
            this.platformProps = platformProps;
        }

        public Map<PlatformPropBean, List<PropValueBean>> getPlatformPropBeanValueMap() {
            return platformPropBeanValueMap;
        }

        public void setPlatformPropBeanValueMap(Map<PlatformPropBean, List<PropValueBean>> platformPropBeanValueMap) {
            this.platformPropBeanValueMap = platformPropBeanValueMap;
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

    public List<PlatformPropBean> getPlatformProps() {
        return platformProps;
    }

    public void setPlatformProps(List<PlatformPropBean> platformProps) {
        this.platformProps = platformProps;
    }

    public Map<PlatformPropBean, List<String>> getPlatformPropValueMap() {
        return platformPropValueMap;
    }

    public void setPlatformPropValueMap(Map<PlatformPropBean, List<String>> platformPropValueMap) {
        this.platformPropValueMap = platformPropValueMap;
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
