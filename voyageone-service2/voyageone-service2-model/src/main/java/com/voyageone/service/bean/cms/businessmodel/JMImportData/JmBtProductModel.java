package com.voyageone.service.bean.cms.businessmodel.JMImportData;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
public class JmBtProductModel implements Serializable
{
   public JmBtProductModel()
    {
        setChannelId(""); 
        setProductCode(""); 
        setDealId(""); 
        setJumeiProductId(""); 
        setProductDes(""); 
        setBrandName(""); 
        setSizeType(""); 
        setSpecialNote(""); 
        setProductName(""); 
        setForeignLanguageName(""); 
        setFunctionIds(""); 
        setAttribute(""); 
        setAddressOfProduce(""); 
        setHsCode(""); 
        setHsName(""); 
        setHsUnit(""); 
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int seq;
    
    
 /**

        */
     private String channelId;
    
    
 /**

        */
     private String productCode;
    
    
 /**

        */
     private String dealId;
    
    
 /**

        */
     private String jumeiProductId;
    
    
 /**

        */
     private String productDes;
    
    
 /**

        */
     private int categoryLv4Id;
    
    
 /**

        */
     private int brandId;
    
    
 /**

        */
     private String brandName;
    
    
 /**

        */
     private String sizeType;
    
    
 /**

        */
     private String specialNote;
    
    
 /**

        */
     private String productName;
    
    
 /**

        */
     private String foreignLanguageName;
    
    
 /**

        */
     private String functionIds;
    
    
 /**

        */
     private String attribute;
    
    
 /**

        */
     private String addressOfProduce;
    
    
 /**

        */
     private String hsCode;
    
    
 /**

        */
     private String hsName;
    
    
 /**

        */
     private String hsUnit;
    
    
 /**
SN库存同步用
        */
     private int sendFlg;
    
    
 /**

        */
     private Timestamp created;
    
    
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
           
        */
        public int getSeq()
        {
         
        return this.seq;
        }
        public void setSeq(int seq)
        {
         this.seq=seq;
        }
    
        
         /**
           
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
           
        */
        public String getDealId()
        {
         
        return this.dealId;
        }
        public void setDealId(String dealId)
        {
        if(dealId!=null){
this.dealId=dealId;
 }
else
{
this.dealId="";
}

        }
    
        
         /**
           
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
           
        */
        public String getProductDes()
        {
         
        return this.productDes;
        }
        public void setProductDes(String productDes)
        {
        if(productDes!=null){
this.productDes=productDes;
 }
else
{
this.productDes="";
}

        }
    
        
         /**
           
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
    
        
         /**
           
        */
        public String getProductName()
        {
         
        return this.productName;
        }
        public void setProductName(String productName)
        {
        if(productName!=null){
this.productName=productName;
 }
else
{
this.productName="";
}

        }
    
        
         /**
           
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
           
        */
        public Timestamp getCreated()
        {
         
        return this.created;
        }
        public void setCreated(Timestamp created)
        {
         this.created=created;
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
    
}