package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmProductModel implements Serializable
{
   public CmsBtJmProductModel()
    {
        setChannelId(""); 
        setJumeiProductId(""); 
        setProductCode(""); 
        setForeignLanguageName(""); 
        setProductNameCn(""); 
        setProductLongName(""); 
        setProductMediumName(""); 
        setProductShortName(""); 
        setVoCategoryName(""); 
        setCategoryLv1Name(""); 
        setCategoryLv2Name(""); 
        setCategoryLv3Name(""); 
        setCategoryLv4Name(""); 
        setVoBrandName(""); 
        setBrandName(""); 
        setProductType(""); 
        setSizeType(""); 
        setColorEn(""); 
        setAttribute(""); 
        setOrigin(""); 
        setAddressOfProduce(""); 
        setMaterialEn(""); 
        setMaterialCn(""); 
        setProductDesEn(""); 
        setProductDesCn(""); 
        setAvailablePeriod(""); 
        setApplicableCrowd(""); 
        setSearchMetaTextCustom(""); 
        setFunctionIds(""); 
        setMsrp(new BigDecimal(0));
        setHsCode(""); 
        setHsName(""); 
        setHsUnit(""); 
        setCreated(DateHelp.getDefaultDate());
        setCreater(""); 
        setModifier(""); 
        setSpecialNote(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
渠道id
        */
     private String channelId;
    
    
 /**
聚美的商品id
        */
     private String jumeiProductId;
    
    
 /**
商品code 唯一标识一个商品
        */
     private String productCode;
    
    
 /**
商品英文名称
        */
     private String foreignLanguageName;
    
    
 /**
商品中文名称
        */
     private String productNameCn;
    
    
 /**
商品长标题（聚美系统）
        */
     private String productLongName;
    
    
 /**
中标题（聚美系统）
        */
     private String productMediumName;
    
    
 /**
短标题（聚美系统）
        */
     private String productShortName;
    
    
 /**
vo系统自用类目（Feed类目或CMS类目）
        */
     private String voCategoryName;
    
    
 /**
聚美一级类目
        */
     private String categoryLv1Name;
    
    
 /**
聚美二级类目
        */
     private String categoryLv2Name;
    
    
 /**
聚美三级类目
        */
     private String categoryLv3Name;
    
    
 /**
聚美四级类目
        */
     private String categoryLv4Name;
    
    
 /**
聚美第四级类目id
        */
     private int categoryLv4Id;
    
    
 /**
主数据品牌
        */
     private String voBrandName;
    
    
 /**
品牌名
        */
     private String brandName;
    
    
 /**
聚美品牌id
        */
     private int brandId;
    
    
 /**
商品类别
        */
     private String productType;
    
    
 /**
尺码类别
        */
     private String sizeType;
    
    
 /**

        */
     private String colorEn;
    
    
 /**
现在仅仅设置颜色
        */
     private String attribute;
    
    
 /**
英文产地
        */
     private String origin;
    
    
 /**
中文产地
        */
     private String addressOfProduce;
    
    
 /**
英文材质（确定报关税号用）
        */
     private String materialEn;
    
    
 /**
中文材质（确定报关税号用）
        */
     private String materialCn;
    
    
 /**
商品英文描述
        */
     private String productDesEn;
    
    
 /**
商品中文描述
        */
     private String productDesCn;
    
    
 /**
保质期
        */
     private String availablePeriod;
    
    
 /**
适用人群
        */
     private String applicableCrowd;
    
    
 /**
自定义搜索词（聚美系统）
        */
     private String searchMetaTextCustom;
    
    
 /**
聚美功效id 基本上不设置
        */
     private String functionIds;
    
    
 /**
海外官网价格
        */
     private BigDecimal msrp;
    
    
 /**
税号
        */
     private String hsCode;
    
    
 /**
品名
        */
     private String hsName;
    
    
 /**
单位
        */
     private String hsUnit;
    
    
 /**
SN库存同步用
        */
     private int sendFlg;
    
    
 /**
同步状态||0:未上新 1:已新增到聚美平台 不用 计划删掉
        */
     private int state;
    
    
 /**

        */
     private Date created;
    
    
 /**

        */
     private String creater;
    
    
 /**

        */
     private Timestamp modified;
    
    
 /**

        */
     private String modifier;
    
    
 /**
0:初始化导入 1:图片上传完成 2:JM上传完成
        */
     private int synFlg;
    
    
 /**

        */
     private String specialNote;
    
        
         /**
           
        */
        public int getId()
        {
         
        return this.id;
        }
        public void setId(int id)
        {
         this.id=id;
        }
    
        
         /**
           渠道id
        */
        public String getChannelId()
        {
         
        return this.channelId;
        }
        public void setChannelId(String channelId)
        {
        if(channelId!=null){
this.channelId=channelId;
 }
else
{
this.channelId="";
}

        }
    
        
         /**
           聚美的商品id
        */
        public String getJumeiProductId()
        {
         
        return this.jumeiProductId;
        }
        public void setJumeiProductId(String jumeiProductId)
        {
        if(jumeiProductId!=null){
this.jumeiProductId=jumeiProductId;
 }
else
{
this.jumeiProductId="";
}

        }
    
        
         /**
           商品code 唯一标识一个商品
        */
        public String getProductCode()
        {
         
        return this.productCode;
        }
        public void setProductCode(String productCode)
        {
        if(productCode!=null){
this.productCode=productCode;
 }
else
{
this.productCode="";
}

        }
    
        
         /**
           商品英文名称
        */
        public String getForeignLanguageName()
        {
         
        return this.foreignLanguageName;
        }
        public void setForeignLanguageName(String foreignLanguageName)
        {
        if(foreignLanguageName!=null){
this.foreignLanguageName=foreignLanguageName;
 }
else
{
this.foreignLanguageName="";
}

        }
    
        
         /**
           商品中文名称
        */
        public String getProductNameCn()
        {
         
        return this.productNameCn;
        }
        public void setProductNameCn(String productNameCn)
        {
        if(productNameCn!=null){
this.productNameCn=productNameCn;
 }
else
{
this.productNameCn="";
}

        }
    
        
         /**
           商品长标题（聚美系统）
        */
        public String getProductLongName()
        {
         
        return this.productLongName;
        }
        public void setProductLongName(String productLongName)
        {
        if(productLongName!=null){
this.productLongName=productLongName;
 }
else
{
this.productLongName="";
}

        }
    
        
         /**
           中标题（聚美系统）
        */
        public String getProductMediumName()
        {
         
        return this.productMediumName;
        }
        public void setProductMediumName(String productMediumName)
        {
        if(productMediumName!=null){
this.productMediumName=productMediumName;
 }
else
{
this.productMediumName="";
}

        }
    
        
         /**
           短标题（聚美系统）
        */
        public String getProductShortName()
        {
         
        return this.productShortName;
        }
        public void setProductShortName(String productShortName)
        {
        if(productShortName!=null){
this.productShortName=productShortName;
 }
else
{
this.productShortName="";
}

        }
    
        
         /**
           vo系统自用类目（Feed类目或CMS类目）
        */
        public String getVoCategoryName()
        {
         
        return this.voCategoryName;
        }
        public void setVoCategoryName(String voCategoryName)
        {
        if(voCategoryName!=null){
this.voCategoryName=voCategoryName;
 }
else
{
this.voCategoryName="";
}

        }
    
        
         /**
           聚美一级类目
        */
        public String getCategoryLv1Name()
        {
         
        return this.categoryLv1Name;
        }
        public void setCategoryLv1Name(String categoryLv1Name)
        {
        if(categoryLv1Name!=null){
this.categoryLv1Name=categoryLv1Name;
 }
else
{
this.categoryLv1Name="";
}

        }
    
        
         /**
           聚美二级类目
        */
        public String getCategoryLv2Name()
        {
         
        return this.categoryLv2Name;
        }
        public void setCategoryLv2Name(String categoryLv2Name)
        {
        if(categoryLv2Name!=null){
this.categoryLv2Name=categoryLv2Name;
 }
else
{
this.categoryLv2Name="";
}

        }
    
        
         /**
           聚美三级类目
        */
        public String getCategoryLv3Name()
        {
         
        return this.categoryLv3Name;
        }
        public void setCategoryLv3Name(String categoryLv3Name)
        {
        if(categoryLv3Name!=null){
this.categoryLv3Name=categoryLv3Name;
 }
else
{
this.categoryLv3Name="";
}

        }
    
        
         /**
           聚美四级类目
        */
        public String getCategoryLv4Name()
        {
         
        return this.categoryLv4Name;
        }
        public void setCategoryLv4Name(String categoryLv4Name)
        {
        if(categoryLv4Name!=null){
this.categoryLv4Name=categoryLv4Name;
 }
else
{
this.categoryLv4Name="";
}

        }
    
        
         /**
           聚美第四级类目id
        */
        public int getCategoryLv4Id()
        {
         
        return this.categoryLv4Id;
        }
        public void setCategoryLv4Id(int categoryLv4Id)
        {
         this.categoryLv4Id=categoryLv4Id;
        }
    
        
         /**
           主数据品牌
        */
        public String getVoBrandName()
        {
         
        return this.voBrandName;
        }
        public void setVoBrandName(String voBrandName)
        {
        if(voBrandName!=null){
this.voBrandName=voBrandName;
 }
else
{
this.voBrandName="";
}

        }
    
        
         /**
           品牌名
        */
        public String getBrandName()
        {
         
        return this.brandName;
        }
        public void setBrandName(String brandName)
        {
        if(brandName!=null){
this.brandName=brandName;
 }
else
{
this.brandName="";
}

        }
    
        
         /**
           聚美品牌id
        */
        public int getBrandId()
        {
         
        return this.brandId;
        }
        public void setBrandId(int brandId)
        {
         this.brandId=brandId;
        }
    
        
         /**
           商品类别
        */
        public String getProductType()
        {
         
        return this.productType;
        }
        public void setProductType(String productType)
        {
        if(productType!=null){
this.productType=productType;
 }
else
{
this.productType="";
}

        }
    
        
         /**
           尺码类别
        */
        public String getSizeType()
        {
         
        return this.sizeType;
        }
        public void setSizeType(String sizeType)
        {
        if(sizeType!=null){
this.sizeType=sizeType;
 }
else
{
this.sizeType="";
}

        }
    
        
         /**
           
        */
        public String getColorEn()
        {
         
        return this.colorEn;
        }
        public void setColorEn(String colorEn)
        {
        if(colorEn!=null){
this.colorEn=colorEn;
 }
else
{
this.colorEn="";
}

        }
    
        
         /**
           现在仅仅设置颜色
        */
        public String getAttribute()
        {
         
        return this.attribute;
        }
        public void setAttribute(String attribute)
        {
        if(attribute!=null){
this.attribute=attribute;
 }
else
{
this.attribute="";
}

        }
    
        
         /**
           英文产地
        */
        public String getOrigin()
        {
         
        return this.origin;
        }
        public void setOrigin(String origin)
        {
        if(origin!=null){
this.origin=origin;
 }
else
{
this.origin="";
}

        }
    
        
         /**
           中文产地
        */
        public String getAddressOfProduce()
        {
         
        return this.addressOfProduce;
        }
        public void setAddressOfProduce(String addressOfProduce)
        {
        if(addressOfProduce!=null){
this.addressOfProduce=addressOfProduce;
 }
else
{
this.addressOfProduce="";
}

        }
    
        
         /**
           英文材质（确定报关税号用）
        */
        public String getMaterialEn()
        {
         
        return this.materialEn;
        }
        public void setMaterialEn(String materialEn)
        {
        if(materialEn!=null){
this.materialEn=materialEn;
 }
else
{
this.materialEn="";
}

        }
    
        
         /**
           中文材质（确定报关税号用）
        */
        public String getMaterialCn()
        {
         
        return this.materialCn;
        }
        public void setMaterialCn(String materialCn)
        {
        if(materialCn!=null){
this.materialCn=materialCn;
 }
else
{
this.materialCn="";
}

        }
    
        
         /**
           商品英文描述
        */
        public String getProductDesEn()
        {
         
        return this.productDesEn;
        }
        public void setProductDesEn(String productDesEn)
        {
        if(productDesEn!=null){
this.productDesEn=productDesEn;
 }
else
{
this.productDesEn="";
}

        }
    
        
         /**
           商品中文描述
        */
        public String getProductDesCn()
        {
         
        return this.productDesCn;
        }
        public void setProductDesCn(String productDesCn)
        {
        if(productDesCn!=null){
this.productDesCn=productDesCn;
 }
else
{
this.productDesCn="";
}

        }
    
        
         /**
           保质期
        */
        public String getAvailablePeriod()
        {
         
        return this.availablePeriod;
        }
        public void setAvailablePeriod(String availablePeriod)
        {
        if(availablePeriod!=null){
this.availablePeriod=availablePeriod;
 }
else
{
this.availablePeriod="";
}

        }
    
        
         /**
           适用人群
        */
        public String getApplicableCrowd()
        {
         
        return this.applicableCrowd;
        }
        public void setApplicableCrowd(String applicableCrowd)
        {
        if(applicableCrowd!=null){
this.applicableCrowd=applicableCrowd;
 }
else
{
this.applicableCrowd="";
}

        }
    
        
         /**
           自定义搜索词（聚美系统）
        */
        public String getSearchMetaTextCustom()
        {
         
        return this.searchMetaTextCustom;
        }
        public void setSearchMetaTextCustom(String searchMetaTextCustom)
        {
        if(searchMetaTextCustom!=null){
this.searchMetaTextCustom=searchMetaTextCustom;
 }
else
{
this.searchMetaTextCustom="";
}

        }
    
        
         /**
           聚美功效id 基本上不设置
        */
        public String getFunctionIds()
        {
         
        return this.functionIds;
        }
        public void setFunctionIds(String functionIds)
        {
        if(functionIds!=null){
this.functionIds=functionIds;
 }
else
{
this.functionIds="";
}

        }
    
        
         /**
           海外官网价格
        */
        public BigDecimal getMsrp()
        {
         
        return this.msrp;
        }
        public void setMsrp(BigDecimal msrp)
        {
         this.msrp=msrp;
        }
    
        
         /**
           税号
        */
        public String getHsCode()
        {
         
        return this.hsCode;
        }
        public void setHsCode(String hsCode)
        {
        if(hsCode!=null){
this.hsCode=hsCode;
 }
else
{
this.hsCode="";
}

        }
    
        
         /**
           品名
        */
        public String getHsName()
        {
         
        return this.hsName;
        }
        public void setHsName(String hsName)
        {
        if(hsName!=null){
this.hsName=hsName;
 }
else
{
this.hsName="";
}

        }
    
        
         /**
           单位
        */
        public String getHsUnit()
        {
         
        return this.hsUnit;
        }
        public void setHsUnit(String hsUnit)
        {
        if(hsUnit!=null){
this.hsUnit=hsUnit;
 }
else
{
this.hsUnit="";
}

        }
    
        
         /**
           SN库存同步用
        */
        public int getSendFlg()
        {
         
        return this.sendFlg;
        }
        public void setSendFlg(int sendFlg)
        {
         this.sendFlg=sendFlg;
        }
    
        
         /**
           同步状态||0:未上新 1:已新增到聚美平台 不用 计划删掉
        */
        public int getState()
        {
         
        return this.state;
        }
        public void setState(int state)
        {
         this.state=state;
        }
    
        
         /**
           
        */
        public Date getCreated()
        {
         
        return this.created;
        }
        public void setCreated(Date created)
        {
       if(created!=null){
this.created=created;
 }
else
{
this.created=DateHelp.getDefaultDate();
}

        }
    
        
         /**
           
        */
        public String getCreater()
        {
         
        return this.creater;
        }
        public void setCreater(String creater)
        {
        if(creater!=null){
this.creater=creater;
 }
else
{
this.creater="";
}

        }
    
        
         /**
           
        */
        public Timestamp getModified()
        {
         
        return this.modified;
        }
        public void setModified(Timestamp modified)
        {
         this.modified=modified;
        }
    
        
         /**
           
        */
        public String getModifier()
        {
         
        return this.modifier;
        }
        public void setModifier(String modifier)
        {
        if(modifier!=null){
this.modifier=modifier;
 }
else
{
this.modifier="";
}

        }
    
        
         /**
           0:初始化导入 1:图片上传完成 2:JM上传完成
        */
        public int getSynFlg()
        {
         
        return this.synFlg;
        }
        public void setSynFlg(int synFlg)
        {
         this.synFlg=synFlg;
        }
    
        
         /**
           
        */
        public String getSpecialNote()
        {
         
        return this.specialNote;
        }
        public void setSpecialNote(String specialNote)
        {
        if(specialNote!=null){
this.specialNote=specialNote;
 }
else
{
this.specialNote="";
}

        }
    
}