package com.voyageone.components.cnn.request;

import com.voyageone.components.cnn.enums.CnnConstants;
import com.voyageone.components.cnn.response.ProductAddResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by morse on 2017/7/31.
 */
public class ProductAddRequest extends AbstractCnnRequest<ProductAddResponse> {

    public ProductAddRequest() {
        commonFields = new CommonFields();
        skuList = new ArrayList<>();
        optionsList = new ArrayList<>();
    }

    @Override
    public String getUrl() {
        return CnnConstants.CnnApiAction.PRODUCT_ADD;
    }

    private CommonFields commonFields; // 共通级属性
    private Map<String, Object> customFields; // 自定义属性
    private List<SkuIem> skuList; // sku级属性
    private List<OptionItem> optionsList; // sku级option属性

    public CommonFields getCommonFields() {
        return commonFields;
    }

    public void setCommonFields(CommonFields commonFields) {
        this.commonFields = commonFields;
    }

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, Object> customFields) {
        this.customFields = customFields;
    }

    public List<SkuIem> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuIem> skuList) {
        this.skuList = skuList;
    }

    public List<OptionItem> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<OptionItem> optionsList) {
        this.optionsList = optionsList;
    }

    /**
     * 共通级属性
     */
    public class CommonFields {

        private String categoryId; // 店铺内分类ID(注意是cms端的分类ID),该商品属于多个分类时，分类ID之间用","分隔,同时要上传该节点的所有父节点
        private String channelId; // 商品渠道ID
        private String model; // 款号
        private String brand; // 品牌名/制造商名
        private String title; // 产品名称(中文)
        private String shortDesc; // 简短描述(中文)
        private String longDesc; // 详情描述(中文)
        private String material; // 材质(中文)
        private String feature; // 商品特质(颜色/口味/香型等)(中文)
        private String origin; // 产地
        private String productType; // 产品分类
        private String sizeType; // 适用人群
        private String usage; // 使用说明(中文)
        private String mainImage; // 商品主图
        private List<String> images; // 产品图列表
        private String pageDetailPC; // PC端产品页详情内容（直接是html脚本）
        private String pageDetailM; // 移动端产品页详情内容（直接是html脚本）
        private String searchKey; // 搜索用关键词

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShortDesc() {
            return shortDesc;
        }

        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        public String getLongDesc() {
            return longDesc;
        }

        public void setLongDesc(String longDesc) {
            this.longDesc = longDesc;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getSizeType() {
            return sizeType;
        }

        public void setSizeType(String sizeType) {
            this.sizeType = sizeType;
        }

        public String getUsage() {
            return usage;
        }

        public void setUsage(String usage) {
            this.usage = usage;
        }

        public String getMainImage() {
            return mainImage;
        }

        public void setMainImage(String mainImage) {
            this.mainImage = mainImage;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getPageDetailPC() {
            return pageDetailPC;
        }

        public void setPageDetailPC(String pageDetailPC) {
            this.pageDetailPC = pageDetailPC;
        }

        public String getPageDetailM() {
            return pageDetailM;
        }

        public void setPageDetailM(String pageDetailM) {
            this.pageDetailM = pageDetailM;
        }

        public String getSearchKey() {
            return searchKey;
        }

        public void setSearchKey(String searchKey) {
            this.searchKey = searchKey;
        }
    }

    /**
     * sku级属性
     */
    public class SkuIem {
        private String skuCode; // skuCode
        private String prodCode; // code
        private String name; // sku名称，可以不设值，页面显示时直接使用'产品名称'
        private int inventory; // 库存数
        private double msrpPrice; // 市场价
        private double salePrice; // 最终销售价
        private Double tax; // 税额
        /*
            sku区分选项(键值对形式)，根据其值可以唯一确定商品里的sku
            例如：{ 'color':'黑色', 'size':'L' }
            其中的键名和值的设置请参照"应用级参数-optionItem"
            只有单个sku的情形时，不需要此项，其属性设置在"skuFields"中
        */
        private Map<String, Object> skuOptions;
        private Map<String, Object> skuFields; // sku个别扩展属性(键值对形式),可以不设值

        public String getSkuCode() {
            return skuCode;
        }

        public void setSkuCode(String skuCode) {
            this.skuCode = skuCode;
        }

        public String getProdCode() {
            return prodCode;
        }

        public void setProdCode(String prodCode) {
            this.prodCode = prodCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public double getMsrpPrice() {
            return msrpPrice;
        }

        public void setMsrpPrice(double msrpPrice) {
            this.msrpPrice = msrpPrice;
        }

        public double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(double salePrice) {
            this.salePrice = salePrice;
        }

        public Double getTax() {
            return tax;
        }

        public void setTax(Double tax) {
            this.tax = tax;
        }

        public Map<String, Object> getSkuOptions() {
            return skuOptions;
        }

        public void setSkuOptions(Map<String, Object> skuOptions) {
            this.skuOptions = skuOptions;
        }

        public Map<String, Object> getSkuFields() {
            return skuFields;
        }

        public void setSkuFields(Map<String, Object> skuFields) {
            this.skuFields = skuFields;
        }
    }

    /**
     * sku级option属性
     */
    public class OptionItem {
        private String key; // 键名(保存到数据库时统一转为小写字符) 例如：键名是 'color', 'size' 等等
        private String name; // 显示用选项名称,例如：显示名称是 '颜色', '尺寸' 等等
        private Integer order; // 该选项在商品画面上的显示顺序
        /*
            图片名称(图片保存在单独的图片服务器上)
            该值现在只有在键名是'color'的情况下有效，商品颜色用一个图片（小图）表示
            该值与"valueList"中的项目、按顺序一一对应
            若键名是'color'，而该值没有设置，则直接显示该颜色名称，而不是显示色块
         */
        private List<String> urlList;
        private List<String> valueList; // 该选项下所有值的列表（直接按数组中的顺序显示）例如：列表是 ['红色','黑色','白色','黄色']，['S','M','L','XL'] 等等

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public List<String> getUrlList() {
            return urlList;
        }

        public void setUrlList(List<String> urlList) {
            this.urlList = urlList;
        }

        public List<String> getValueList() {
            return valueList;
        }

        public void setValueList(List<String> valueList) {
            this.valueList = valueList;
        }
    }
}
