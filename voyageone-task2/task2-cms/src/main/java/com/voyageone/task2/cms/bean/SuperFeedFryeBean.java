package com.voyageone.task2.cms.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gjl on 2016/11/29.
 */
public class SuperFeedFryeBean extends SuperFeedBean {
    private String variantsUpc;

    private Long productId;

    private Boolean isactive;

    private Boolean istaxable;

    private Double categoryid;

    private String metadataCategoryname;

    private Double metadataUnitcost;

    private Double metadataPreviouscost;

    private Double metadataMaxcost;

    private Double metadataMincost;

    private Double metadataMinregularprice;

    private Double metadataMaxregularprice;

    private Double metadataMinsaleprice;

    private Double metadataMaxsaleprice;

    private Boolean metadataSamemaxmin;

    private Double metadataShippingsurcharge;

    private Double metadataWeight;

    private Boolean metadataDisplayoospopup;

    private Boolean metadataShowwhenoos;

    private Boolean metadataNonremovablefromcart;

    private String md5;

    private Integer updateflag;

    private String attributes;

    private String fabric;

    private String variantsName;

    private String variantsMediasMetaExtraimagethumb;

    private String variantsMediasMetaExtraimagethumburl;

    private String variantsMediasMetaExtraimagelargethumb;

    private String variantsMediasMetaExtraimagelargethumburl;

    private String variantsOptionsColor;

    private String variantsOptionsSize;

    private String mfgsku;

    private String taxclassification;

    private String name;

    private String firstlivedate;

    private String metatags;

    private String options;

    private String metadataCategoryextratext1;

    private String metadataCategoryextratext2;

    private String metadataCategoryextratext3;

    private String metadataExtratext1;

    private String metadataDescription2;

    private String metadataDescription3;

    private String metadataDescription4;

    private String metadataDescription2withformatting;

    private String metadataDescription3withformatting;

    private String metadataDescription4withformatting;

    private String metadataDetail;

    private String metadataSizecharturl;

    private String metadataPreorderavailabledate;

    private String metadataProducturl;

    private String origin;

    private String variantsMediasFilepath;

    private String sex;

    public String getVariantsUpc() {
        return variantsUpc;
    }

    public void setVariantsUpc(String variantsUpc) {
        this.variantsUpc = variantsUpc == null ? null : variantsUpc.trim();
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public Boolean getIstaxable() {
        return istaxable;
    }

    public void setIstaxable(Boolean istaxable) {
        this.istaxable = istaxable;
    }

    public Double getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Double categoryid) {
        this.categoryid = categoryid;
    }

    public String getMetadataCategoryname() {
        return metadataCategoryname;
    }

    public void setMetadataCategoryname(String metadataCategoryname) {
        this.metadataCategoryname = metadataCategoryname == null ? null : metadataCategoryname.trim();
    }

    public Double getMetadataUnitcost() {
        return metadataUnitcost;
    }

    public void setMetadataUnitcost(Double metadataUnitcost) {
        this.metadataUnitcost = metadataUnitcost;
    }

    public Double getMetadataPreviouscost() {
        return metadataPreviouscost;
    }

    public void setMetadataPreviouscost(Double metadataPreviouscost) {
        this.metadataPreviouscost = metadataPreviouscost;
    }

    public Double getMetadataMaxcost() {
        return metadataMaxcost;
    }

    public void setMetadataMaxcost(Double metadataMaxcost) {
        this.metadataMaxcost = metadataMaxcost;
    }

    public Double getMetadataMincost() {
        return metadataMincost;
    }

    public void setMetadataMincost(Double metadataMincost) {
        this.metadataMincost = metadataMincost;
    }

    public Double getMetadataMinregularprice() {
        return metadataMinregularprice;
    }

    public void setMetadataMinregularprice(Double metadataMinregularprice) {
        this.metadataMinregularprice = metadataMinregularprice;
    }

    public Double getMetadataMaxregularprice() {
        return metadataMaxregularprice;
    }

    public void setMetadataMaxregularprice(Double metadataMaxregularprice) {
        this.metadataMaxregularprice = metadataMaxregularprice;
    }

    public Double getMetadataMinsaleprice() {
        return metadataMinsaleprice;
    }

    public void setMetadataMinsaleprice(Double metadataMinsaleprice) {
        this.metadataMinsaleprice = metadataMinsaleprice;
    }

    public Double getMetadataMaxsaleprice() {
        return metadataMaxsaleprice;
    }

    public void setMetadataMaxsaleprice(Double metadataMaxsaleprice) {
        this.metadataMaxsaleprice = metadataMaxsaleprice;
    }

    public Boolean getMetadataSamemaxmin() {
        return metadataSamemaxmin;
    }

    public void setMetadataSamemaxmin(Boolean metadataSamemaxmin) {
        this.metadataSamemaxmin = metadataSamemaxmin;
    }

    public Double getMetadataShippingsurcharge() {
        return metadataShippingsurcharge;
    }

    public void setMetadataShippingsurcharge(Double metadataShippingsurcharge) {
        this.metadataShippingsurcharge = metadataShippingsurcharge;
    }

    public Double getMetadataWeight() {
        return metadataWeight;
    }

    public void setMetadataWeight(Double metadataWeight) {
        this.metadataWeight = metadataWeight;
    }

    public Boolean getMetadataDisplayoospopup() {
        return metadataDisplayoospopup;
    }

    public void setMetadataDisplayoospopup(Boolean metadataDisplayoospopup) {
        this.metadataDisplayoospopup = metadataDisplayoospopup;
    }

    public Boolean getMetadataShowwhenoos() {
        return metadataShowwhenoos;
    }

    public void setMetadataShowwhenoos(Boolean metadataShowwhenoos) {
        this.metadataShowwhenoos = metadataShowwhenoos;
    }

    public Boolean getMetadataNonremovablefromcart() {
        return metadataNonremovablefromcart;
    }

    public void setMetadataNonremovablefromcart(Boolean metadataNonremovablefromcart) {
        this.metadataNonremovablefromcart = metadataNonremovablefromcart;
    }

    public String getMd5() {
        StringBuffer temp = new StringBuffer();
        Set<String> noMd5Fields = new HashSet<>();
        noMd5Fields.add("md5");
        noMd5Fields.add("updateflag");
        return  beanToMd5(this,noMd5Fields);
    }

    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    public Integer getUpdateflag() {
        return updateflag;
    }

    public void setUpdateflag(Integer updateflag) {
        this.updateflag = updateflag;
    }
    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes == null ? null : attributes.trim();
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric == null ? null : fabric.trim();
    }

    public String getVariantsName() {
        return variantsName;
    }

    public void setVariantsName(String variantsName) {
        this.variantsName = variantsName == null ? null : variantsName.trim();
    }

    public String getVariantsMediasMetaExtraimagethumb() {
        return variantsMediasMetaExtraimagethumb;
    }

    public void setVariantsMediasMetaExtraimagethumb(String variantsMediasMetaExtraimagethumb) {
        this.variantsMediasMetaExtraimagethumb = variantsMediasMetaExtraimagethumb == null ? null : variantsMediasMetaExtraimagethumb.trim();
    }

    public String getVariantsMediasMetaExtraimagethumburl() {
        return variantsMediasMetaExtraimagethumburl;
    }

    public void setVariantsMediasMetaExtraimagethumburl(String variantsMediasMetaExtraimagethumburl) {
        this.variantsMediasMetaExtraimagethumburl = variantsMediasMetaExtraimagethumburl == null ? null : variantsMediasMetaExtraimagethumburl.trim();
    }

    public String getVariantsMediasMetaExtraimagelargethumb() {
        return variantsMediasMetaExtraimagelargethumb;
    }

    public void setVariantsMediasMetaExtraimagelargethumb(String variantsMediasMetaExtraimagelargethumb) {
        this.variantsMediasMetaExtraimagelargethumb = variantsMediasMetaExtraimagelargethumb == null ? null : variantsMediasMetaExtraimagelargethumb.trim();
    }

    public String getVariantsMediasMetaExtraimagelargethumburl() {
        return variantsMediasMetaExtraimagelargethumburl;
    }

    public void setVariantsMediasMetaExtraimagelargethumburl(String variantsMediasMetaExtraimagelargethumburl) {
        this.variantsMediasMetaExtraimagelargethumburl = variantsMediasMetaExtraimagelargethumburl == null ? null : variantsMediasMetaExtraimagelargethumburl.trim();
    }

    public String getVariantsOptionsColor() {
        return variantsOptionsColor;
    }

    public void setVariantsOptionsColor(String variantsOptionsColor) {
        this.variantsOptionsColor = variantsOptionsColor == null ? null : variantsOptionsColor.trim();
    }

    public String getVariantsOptionsSize() {
        return variantsOptionsSize;
    }

    public void setVariantsOptionsSize(String variantsOptionsSize) {
        this.variantsOptionsSize = variantsOptionsSize == null ? null : variantsOptionsSize.trim();
    }

    public String getMfgsku() {
        return mfgsku;
    }

    public void setMfgsku(String mfgsku) {
        this.mfgsku = mfgsku == null ? null : mfgsku.trim();
    }

    public String getTaxclassification() {
        return taxclassification;
    }

    public void setTaxclassification(String taxclassification) {
        this.taxclassification = taxclassification == null ? null : taxclassification.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getFirstlivedate() {
        return firstlivedate;
    }

    public void setFirstlivedate(String firstlivedate) {
        this.firstlivedate = firstlivedate == null ? null : firstlivedate.trim();
    }

    public String getMetatags() {
        return metatags;
    }

    public void setMetatags(String metatags) {
        this.metatags = metatags == null ? null : metatags.trim();
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options == null ? null : options.trim();
    }

    public String getMetadataCategoryextratext1() {
        return metadataCategoryextratext1;
    }

    public void setMetadataCategoryextratext1(String metadataCategoryextratext1) {
        this.metadataCategoryextratext1 = metadataCategoryextratext1 == null ? null : metadataCategoryextratext1.trim();
    }

    public String getMetadataCategoryextratext2() {
        return metadataCategoryextratext2;
    }

    public void setMetadataCategoryextratext2(String metadataCategoryextratext2) {
        this.metadataCategoryextratext2 = metadataCategoryextratext2 == null ? null : metadataCategoryextratext2.trim();
    }

    public String getMetadataCategoryextratext3() {
        return metadataCategoryextratext3;
    }

    public void setMetadataCategoryextratext3(String metadataCategoryextratext3) {
        this.metadataCategoryextratext3 = metadataCategoryextratext3 == null ? null : metadataCategoryextratext3.trim();
    }

    public String getMetadataExtratext1() {
        return metadataExtratext1;
    }

    public void setMetadataExtratext1(String metadataExtratext1) {
        this.metadataExtratext1 = metadataExtratext1 == null ? null : metadataExtratext1.trim();
    }

    public String getMetadataDescription2() {
        return metadataDescription2;
    }

    public void setMetadataDescription2(String metadataDescription2) {
        this.metadataDescription2 = metadataDescription2 == null ? null : metadataDescription2.trim();
    }

    public String getMetadataDescription3() {
        return metadataDescription3;
    }

    public void setMetadataDescription3(String metadataDescription3) {
        this.metadataDescription3 = metadataDescription3 == null ? null : metadataDescription3.trim();
    }

    public String getMetadataDescription4() {
        return metadataDescription4;
    }

    public void setMetadataDescription4(String metadataDescription4) {
        this.metadataDescription4 = metadataDescription4 == null ? null : metadataDescription4.trim();
    }

    public String getMetadataDescription2withformatting() {
        return metadataDescription2withformatting;
    }

    public void setMetadataDescription2withformatting(String metadataDescription2withformatting) {
        this.metadataDescription2withformatting = metadataDescription2withformatting == null ? null : metadataDescription2withformatting.trim();
    }

    public String getMetadataDescription3withformatting() {
        return metadataDescription3withformatting;
    }

    public void setMetadataDescription3withformatting(String metadataDescription3withformatting) {
        this.metadataDescription3withformatting = metadataDescription3withformatting == null ? null : metadataDescription3withformatting.trim();
    }

    public String getMetadataDescription4withformatting() {
        return metadataDescription4withformatting;
    }

    public void setMetadataDescription4withformatting(String metadataDescription4withformatting) {
        this.metadataDescription4withformatting = metadataDescription4withformatting == null ? null : metadataDescription4withformatting.trim();
    }

    public String getMetadataDetail() {
        return metadataDetail;
    }

    public void setMetadataDetail(String metadataDetail) {
        this.metadataDetail = metadataDetail == null ? null : metadataDetail.trim();
    }

    public String getMetadataSizecharturl() {
        return metadataSizecharturl;
    }

    public void setMetadataSizecharturl(String metadataSizecharturl) {
        this.metadataSizecharturl = metadataSizecharturl == null ? null : metadataSizecharturl.trim();
    }

    public String getMetadataPreorderavailabledate() {
        return metadataPreorderavailabledate;
    }

    public void setMetadataPreorderavailabledate(String metadataPreorderavailabledate) {
        this.metadataPreorderavailabledate = metadataPreorderavailabledate == null ? null : metadataPreorderavailabledate.trim();
    }

    public String getMetadataProducturl() {
        return metadataProducturl;
    }

    public void setMetadataProducturl(String metadataProducturl) {
        this.metadataProducturl = metadataProducturl == null ? null : metadataProducturl.trim();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getVariantsMediasFilepath() {
        return variantsMediasFilepath;
    }

    public void setVariantsMediasFilepath(String variantsMediasFilepath) {
        this.variantsMediasFilepath = variantsMediasFilepath;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
