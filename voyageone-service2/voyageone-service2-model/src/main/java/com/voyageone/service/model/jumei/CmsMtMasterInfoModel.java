package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;

public class CmsMtMasterInfoModel implements Serializable
{
   public CmsMtMasterInfoModel()
    {
        setChannelId(""); 
        setBrandName(""); 
        setProductType(""); 
        setSizeType(""); 
        setValue1(""); 
        setValue2(""); 
        setErrorMessage(""); 
        setCreated(DateTimeUtil.getCreatedDefaultDate());
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
平台ID
        */
     private int platformId;
    
    
 /**
店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
        */
     private String channelId;
    
    
 /**
品牌名称  data_type(3 4 5 6)  image_key
        */
     private String brandName;
    
    
 /**
商品类别  3 4 5 6
        */
     private String productType;
    
    
 /**
尺码类别    5
        */
     private String sizeType;
    
    
 /**
数据类型（3:特殊说明；4：品牌故事图 ；5：尺码图； 6：物流介绍）
        */
     private int dataType;
    
    
 /**
图片顺序 默认1
        */
     private int imageIndex;
    
    
 /**
data_type为3，value1是文字（特殊说明）,data_type为其它，value1是origin_url自己服务器的图片url
        */
     private String value1;
    
    
 /**
data_type为3，value1是NULL,data_type为其它，value2是jm_url聚美服务器的url
        */
     private String value2;
    
    
 /**
0:待上传；1:上传成功；2:上传失败
        */
     private int synFlg;
    
    
 /**

        */
     private String errorMessage;
    
    
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
platform_id  channel_id  brand_name  product_type
        */
     private boolean active;
    
        
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
           平台ID
        */
        public int getPlatformId()
        {
         
        return this.platformId;
        }
        public void setPlatformId(int platformId)
        {
         this.platformId=platformId;
        }
    
        
         /**
           店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
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
           品牌名称  data_type(3 4 5 6)  image_key
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
           商品类别  3 4 5 6
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
           尺码类别    5
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
           数据类型（3:特殊说明；4：品牌故事图 ；5：尺码图； 6：物流介绍）
        */
        public int getDataType()
        {
         
        return this.dataType;
        }
        public void setDataType(int dataType)
        {
         this.dataType=dataType;
        }
    
        
         /**
           图片顺序 默认1
        */
        public int getImageIndex()
        {
         
        return this.imageIndex;
        }
        public void setImageIndex(int imageIndex)
        {
         this.imageIndex=imageIndex;
        }
    
        
         /**
           data_type为3，value1是文字（特殊说明）,data_type为其它，value1是origin_url自己服务器的图片url
        */
        public String getValue1()
        {
         
        return this.value1;
        }
        public void setValue1(String value1)
        {
        if(value1!=null){
this.value1=value1;
 }
else
{
this.value1="";
}

        }
    
        
         /**
           data_type为3，value1是NULL,data_type为其它，value2是jm_url聚美服务器的url
        */
        public String getValue2()
        {
         
        return this.value2;
        }
        public void setValue2(String value2)
        {
        if(value2!=null){
this.value2=value2;
 }
else
{
this.value2="";
}

        }
    
        
         /**
           0:待上传；1:上传成功；2:上传失败
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
        public String getErrorMessage()
        {
         
        return this.errorMessage;
        }
        public void setErrorMessage(String errorMessage)
        {
        if(errorMessage!=null){
this.errorMessage=errorMessage;
 }
else
{
this.errorMessage="";
}

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
this.created=DateTimeUtil.getCreatedDefaultDate();
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
           platform_id  channel_id  brand_name  product_type
        */
        public boolean getActive()
        {
         
        return this.active;
        }
        public void setActive(boolean active)
        {
         this.active=active;
        }
    
}