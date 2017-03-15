package com.voyageone.components.jd.bean;

import com.jd.open.api.sdk.domain.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 京东商品对象结构
 * 属性项目是从 WareWriteAddRequest 中提取
 * <p/>
 * Created on 2017/02/23
 *
 * @author charis
 * @version 2.0.0
 * @since 2.0.0
 */
@SuppressWarnings("ALL")
public class JdProductNewBean {
    private Long wareId;                     // 京东商品ID
    private String title;                    // 商品标题
    private Long categoryId;                 // 类目id
    private Long categorySecId;
    private Long brandId;                    // 品牌id
    private Long templateId;                 // 关联版式id
    private Long transportId;                // 运费模板id
    private Integer wareStatus;              // 商品状态
    private String outerId;                  // 商品外部id
    private String itemNum;                  // 商品货号
    private String barCode;                  // 条形码
    private Integer wareLocation;            // 产地
    private Date onlineTime;                 // 定时上架时间
    private Date offlineTime;                // 定时下架时间
    private Integer colType;
    private Long delivery;                   // 商品发货地
    private AdWords adWords;                 // 广告词
    private String wrap;                     // 包装规格
    private String packListing;              // 包装清单
    private Integer length;                  // 商品长度,单位mm
    private Integer width;                   // 商品宽度,单位mm
    private Integer height;                  // 商品高度,单位mm
    private Float weight;                    // 商品重量,单位mm
    private Set<Prop> props;                 // 商品基础属性
    private Set<Feature> features;           // 商品特殊属性
    private List<Image> images;              // 商品图片
    private Set<Long> shopCategorys;         // 店铺自定义分类id
    private String mobileDesc;               // 移动端详情
    private String introduction;             // pc端详情
    private String afterSales;               // 售后服务
    private String logo;
    private BigDecimal marketPrice;          // 市场价
    private BigDecimal costPrice;
    private Long stockNum;                   // 商品数量
    private Date created;                    // 商品创建时间
    private Date modified;                   // 商品修改时间
    private List<Sku> skus;                  // SKU
    private String brandName;                // 品牌名
    private Long shopId;                     // 店铺id
    private BigDecimal jdPrice;              // 京东价

    public Long getWareId() {
        return wareId;
    }

    public void setWareId(Long wareId) {
        this.wareId = wareId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategorySecId() {
        return categorySecId;
    }

    public void setCategorySecId(Long categorySecId) {
        this.categorySecId = categorySecId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getTransportId() {
        return transportId;
    }

    public void setTransportId(Long transportId) {
        this.transportId = transportId;
    }

    public Integer getWareStatus() {
        return wareStatus;
    }

    public void setWareStatus(Integer wareStatus) {
        this.wareStatus = wareStatus;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getWareLocation() {
        return wareLocation;
    }

    public void setWareLocation(Integer wareLocation) {
        this.wareLocation = wareLocation;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

    public Integer getColType() {
        return colType;
    }

    public void setColType(Integer colType) {
        this.colType = colType;
    }

    public Long getDelivery() {
        return delivery;
    }

    public void setDelivery(Long delivery) {
        this.delivery = delivery;
    }

    public AdWords getAdWords() {
        return adWords;
    }

    public void setAdWords(AdWords adWords) {
        this.adWords = adWords;
    }

    public String getWrap() {
        return wrap;
    }

    public void setWrap(String wrap) {
        this.wrap = wrap;
    }

    public String getPackListing() {
        return packListing;
    }

    public void setPackListing(String packListing) {
        this.packListing = packListing;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Set<Prop> getProps() {
        return props;
    }

    public void setProps(Set<Prop> props) {
        this.props = props;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Set<Long> getShopCategorys() {
        return shopCategorys;
    }

    public void setShopCategorys(Set<Long> shopCategorys) {
        this.shopCategorys = shopCategorys;
    }

    public String getMobileDesc() {
        return mobileDesc;
    }

    public void setMobileDesc(String mobileDesc) {
        this.mobileDesc = mobileDesc;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAfterSales() {
        return afterSales;
    }

    public void setAfterSales(String afterSales) {
        this.afterSales = afterSales;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public BigDecimal getJdPrice() {
        return jdPrice;
    }

    public void setJdPrice(BigDecimal jdPrice) {
        this.jdPrice = jdPrice;
    }

    //    private String wareId;              // 京东商品ID
//    private String skuUpcCodes;         //
//    private String tradeNo;             // 流水号
//    private String wareLocation;        // 产地
//    private String brandId;             //
//    private String cid;                 // 类目id
//    private String shopCategory;        // 自定义店内分类(通过360buy.sellercats.get获取店铺分类的parent_id及cid，按“parent_id-cid"格式传入，同时设置多个以分号（;）分隔即可。店内分类，格式:206-208;207-208 206(一级)-208(二级);207(一级)-207(一级))
//    private String title;               // 商品标题
//    private String is7ToReturn;         // 7天无理由退货, 1为支持，0为不支持
//    private String upcCode;             // UPC编码
//    private String optionType;          // 操作类型 现只支持：offsale 或onsale,默认为下架状态
//    private String itemNum;             // 外部商品编号，对应商家后台货号
//    private String stockNum;            // 库存
//    private String producter;           // 生产厂商
//    private String wrap;                // 包装规格
//    private String length;              // 长(单位:mm)
//    private String wide;                // 宽(单位:mm)
//    private String high;                // 高(单位:mm)
//    private String weight;              // 重量(单位:kg)
//    private String costPrice;           // 进货价,精确到2位小数，单位:元
//    private String marketPrice;         // 市场价, 精确到2位小数，单位:元
//    private String jdPrice;             // 京东价,精确到2位小数，单位:元
//    private String notes;               // 描述（最多支持3万个英文字符）
//    private byte[] wareImage;           // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）
//    private String packListing;         // 包装清单
//    private String service;             // 售后服务
//    private String skuProperties;       // sku 属性,一组sku 属性之间用^分隔，多组用|分隔格式:aid:vid^aid1:vid2|aid3:vid3^aid4:vid4 （需要从类目服务接口获取）
//    private String attributes;          // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1（需要从类目服务接口获取） 如输入类型input_type为1或2，则attributes为必填属性；如输入类型input_type为3，则用字段input_str填入属性的值
//    private String skuPrices;           // sku 价格,多组之间用‘|’分隔，格式:p1|p2
//    private String skuStocks;           // sku 库存,多组之间用‘|’分隔， 格式:s1|s2
//    private String propertyAlias;       // 自定义属性值别名： 属性ID:属性值ID:别名 ，多组之间用^分开，如aid:vid:别名^aid1:vid1:别名1
//    private String outerId;             // SKU外部ID，对个之间用‘|’分隔格，比如：sdf|sds（支持没有sku的情况下，可以输入外部id，并将外部id绑定在默认生成的sku上），对应商家后台‘商家skuid’
//    private String isPayFirst;          // 是否先款后货 , false为否，true为是
//    private String isCanVAT;            // 发票限制：非必须输入，true为限制，false为不限制开增值税发票，FBP、LBP、SOPL、SOP类型商品均可输入
//    private String isImported;          // 是否进口商品：非必须输入，false为否，true为是，FBP类型商品可输入
//    private String isHealthProduct;     // 是否保健品：非必须输入，false为否，true为是，FBP类型商品可输入
//    private String isShelfLife;         // 是否保质期管理商品, false为否，true为是
//    private String shelfLifeDays;       // 保质期：非必须输入，0-99999范围区间，FBP类型商品可输入
//    private String isSerialNo;          // 是否序列号管理：非必须输入，false为否，true为是，FBP类型商品可输入
//    private String isAppliancesCard;    // 大家电购物卡：非必须输入，false为否，true为是，FBP类型商品可输入
//    private String isSpecialWet;        // 是否特殊液体：非必须输入，false为否，true为是，FBP、LBP、SOPL类型商品可输入
//    private String wareBigSmallModel;   // 商品件型：FBP类型商品必须输入，非FBP类型商品可输入非必填，0免费、1超大件、2超大件半件、3大件、4大件半件、5中件、6中件半件、7小件、8超小件
//    private String warePackType;        // 商品包装：FBP类型商品必须输入，非FBP类型商品可输入非必填，1普通商品、2易碎品、3裸瓶液体、4带包装液体、5按原包装出库
//    private String inputPids;           // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入
//    private String inputStrs;           // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’图书品类输入值规则： ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd” 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式
//    private String hasCheckCode;        // 是否输入验证码 true:是;false:否
//    private String adContent;           // 广告词内容最大支持45个字符
//    private String listTime;            // 定时上架时间 时间格式：yyyy-MM-dd HH:mm:ss;规则是大于当前时间，10天内。
//
//    public String getWareId() {
//        return this.wareId;
//    }
//
//    public void setWareId(String wareId) {
//        this.wareId = wareId;
//    }
//
//    public String getSkuUpcCodes() {
//        return this.skuUpcCodes;
//    }
//
//    public void setSkuUpcCodes(String skuUpcCodes) {
//        this.skuUpcCodes = skuUpcCodes;
//    }
//
//    public String getBrandId() {
//        return this.brandId;
//    }
//
//    public void setBrandId(String brandId) {
//        this.brandId = brandId;
//    }
//
//    public String getWareLocation() {
//        return this.wareLocation;
//    }
//
//    public void setWareLocation(String wareLocation) {
//        this.wareLocation = wareLocation;
//    }
//
//    public String getHasCheckCode() {
//        return this.hasCheckCode;
//    }
//
//    public void setHasCheckCode(String hasCheckCode) {
//        this.hasCheckCode = hasCheckCode;
//    }
//
//    public String getAdContent() {
//        return this.adContent;
//    }
//
//    public void setAdContent(String adContent) {
//        this.adContent = adContent;
//    }
//
//    public String getListTime() {
//        return this.listTime;
//    }
//
//    public void setListTime(String listTime) {
//        this.listTime = listTime;
//    }
//
//    public String getInputPids() {
//        return this.inputPids;
//    }
//
//    public void setInputPids(String inputPids) {
//        this.inputPids = inputPids;
//    }
//
//    public String getInputStrs() {
//        return this.inputStrs;
//    }
//
//    public void setInputStrs(String inputStrs) {
//        this.inputStrs = inputStrs;
//    }
//
//    public String getShelfLife() {
//        return this.isShelfLife;
//    }
//
//    public void setShelfLife(String shelfLife) {
//        this.isShelfLife = shelfLife;
//    }
//
//    public String getPayFirst() {
//        return this.isPayFirst;
//    }
//
//    public void setPayFirst(String payFirst) {
//        this.isPayFirst = payFirst;
//    }
//
//    public String getCanVAT() {
//        return this.isCanVAT;
//    }
//
//    public void setCanVAT(String canVAT) {
//        this.isCanVAT = canVAT;
//    }
//
//    public String getImported() {
//        return this.isImported;
//    }
//
//    public void setImported(String imported) {
//        this.isImported = imported;
//    }
//
//    public String getHealthProduct() {
//        return this.isHealthProduct;
//    }
//
//    public void setHealthProduct(String healthProduct) {
//        this.isHealthProduct = healthProduct;
//    }
//
//    public String getShelfLifeDays() {
//        return this.shelfLifeDays;
//    }
//
//    public void setShelfLifeDays(String shelfLifeDays) {
//        this.shelfLifeDays = shelfLifeDays;
//    }
//
//    public String getSerialNo() {
//        return this.isSerialNo;
//    }
//
//    public void setSerialNo(String serialNo) {
//        this.isSerialNo = serialNo;
//    }
//
//    public String getAppliancesCard() {
//        return this.isAppliancesCard;
//    }
//
//    public void setAppliancesCard(String appliancesCard) {
//        this.isAppliancesCard = appliancesCard;
//    }
//
//    public String getSpecialWet() {
//        return this.isSpecialWet;
//    }
//
//    public void setSpecialWet(String specialWet) {
//        this.isSpecialWet = specialWet;
//    }
//
//    public String getWareBigSmallModel() {
//        return this.wareBigSmallModel;
//    }
//
//    public void setWareBigSmallModel(String wareBigSmallModel) {
//        this.wareBigSmallModel = wareBigSmallModel;
//    }
//
//    public String getWarePackType() {
//        return this.warePackType;
//    }
//
//    public void setWarePackType(String warePackType) {
//        this.warePackType = warePackType;
//    }
//
//    public String getOuterId() {
//        return this.outerId;
//    }
//
//    public void setOuterId(String outerId) {
//        this.outerId = outerId;
//    }
//
//    public String getPropertyAlias() {
//        return this.propertyAlias;
//    }
//
//    public void setPropertyAlias(String propertyAlias) {
//        this.propertyAlias = propertyAlias;
//    }
//
//    public String getStockNum() {
//        return this.stockNum;
//    }
//
//    public void setStockNum(String stockNum) {
//        this.stockNum = stockNum;
//    }
//
//    public String getOptionType() {
//        return this.optionType;
//    }
//
//    public void setOptionType(String optionType) {
//        this.optionType = optionType;
//    }
//
//    public String getCid() {
//        return this.cid;
//    }
//
//    public void setCid(String cid) {
//        this.cid = cid;
//    }
//
//    public String getTitle() {
//        return this.title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getIs7ToReturn() {
//        return this.is7ToReturn;
//    }
//
//    public void setIs7ToReturn(String is7ToReturn) {
//        this.is7ToReturn = is7ToReturn;
//    }
//
//    public String getUpcCode() {
//        return this.upcCode;
//    }
//
//    public void setUpcCode(String upcCode) {
//        this.upcCode = upcCode;
//    }
//
//    public String getItemNum() {
//        return this.itemNum;
//    }
//
//    public void setItemNum(String itemNum) {
//        this.itemNum = itemNum;
//    }
//
//    public String getProducter() {
//        return this.producter;
//    }
//
//    public void setProducter(String producter) {
//        this.producter = producter;
//    }
//
//    public String getWrap() {
//        return this.wrap;
//    }
//
//    public void setWrap(String wrap) {
//        this.wrap = wrap;
//    }
//
//    public String getLength() {
//        return this.length;
//    }
//
//    public void setLength(String length) {
//        this.length = length;
//    }
//
//    public String getWide() {
//        return this.wide;
//    }
//
//    public void setWide(String wide) {
//        this.wide = wide;
//    }
//
//    public String getHigh() {
//        return this.high;
//    }
//
//    public void setHigh(String high) {
//        this.high = high;
//    }
//
//    public String getWeight() {
//        return this.weight;
//    }
//
//    public void setWeight(String weight) {
//        this.weight = weight;
//    }
//
//    public String getCostPrice() {
//        return this.costPrice;
//    }
//
//    public void setCostPrice(String costPrice) {
//        this.costPrice = costPrice;
//    }
//
//    public String getMarketPrice() {
//        return this.marketPrice;
//    }
//
//    public void setMarketPrice(String marketPrice) {
//        this.marketPrice = marketPrice;
//    }
//
//    public String getJdPrice() {
//        return this.jdPrice;
//    }
//
//    public void setJdPrice(String jdPrice) {
//        this.jdPrice = jdPrice;
//    }
//
//    public String getNotes() {
//        return this.notes;
//    }
//
//    public void setNotes(String notes) {
//        this.notes = notes;
//    }
//
//    public byte[] getWareImage() {
//        return this.wareImage;
//    }
//
//    public void setWareImage(byte[] wareImage) {
//        this.wareImage = wareImage;
//    }
//
//    public String getPackListing() {
//        return this.packListing;
//    }
//
//    public void setPackListing(String packListing) {
//        this.packListing = packListing;
//    }
//
//    public String getService() {
//        return this.service;
//    }
//
//    public void setService(String service) {
//        this.service = service;
//    }
//
//    public String getSkuProperties() {
//        return this.skuProperties;
//    }
//
//    public void setSkuProperties(String skuProperties) {
//        this.skuProperties = skuProperties;
//    }
//
//    public String getAttributes() {
//        return this.attributes;
//    }
//
//    public void setAttributes(String attributes) {
//        this.attributes = attributes;
//    }
//
//    public String getShopCategory() {
//        return this.shopCategory;
//    }
//
//    public void setShopCategory(String shopCategory) {
//        this.shopCategory = shopCategory;
//    }
//
//    public String getSkuPrices() {
//        return this.skuPrices;
//    }
//
//    public void setSkuPrices(String skuPrices) {
//        this.skuPrices = skuPrices;
//    }
//
//    public String getSkuStocks() {
//        return this.skuStocks;
//    }
//
//    public void setSkuStocks(String skuStocks) {
//        this.skuStocks = skuStocks;
//    }
//
//    public String getTradeNo() {
//        return this.tradeNo;
//    }
//
//    public void setTradeNo(String tradeNo) {
//        this.tradeNo = tradeNo;
//    }

}
