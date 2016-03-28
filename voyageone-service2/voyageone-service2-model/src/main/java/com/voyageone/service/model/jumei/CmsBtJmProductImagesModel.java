package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmProductImagesModel implements Serializable
{
   public CmsBtJmProductImagesModel()
    {
        setChannelId(""); 
        setProductCode(""); 
        setImageTypeName(""); 
        setOriginUrl(""); 
        setJmUrl(""); 
        setCreated(DateHelp.getDefaultDate());
        setCreater(""); 
        setModifier(""); 
        setProductImageUrlKey(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
        */
     private String channelId;
    
    
 /**
图片锁定条件（1:白底方图的情况下为Product Code；2:商品详情图的情况下为Product Code； 3:参数图的情况下为Product Code； 4：品牌故事图的情况下为品牌名称； 5：尺码图的情况下为品牌名称 6：物流介绍的情况下为品牌名称 7：竖图的情况下为Product Code）
        */
     private String productCode;
    
    
 /**
图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
        */
     private String imageTypeName;
    
    
 /**
图片类型（1:白底方图 ；2:商品详情图 ；3:参数图 ；7：竖图）
        */
     private int imageType;
    
    
 /**
图片顺序
        */
     private int imageIndex;
    
    
 /**
自己服务器的图片url
        */
     private String originUrl;
    
    
 /**
聚美服务器的url
        */
     private String jmUrl;
    
    
 /**
0:没上传 1:已经上传
        */
     private int synFlg;
    
    
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
商品图片urlKey
        */
     private String productImageUrlKey;
    
        
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
           图片锁定条件（1:白底方图的情况下为Product Code；2:商品详情图的情况下为Product Code； 3:参数图的情况下为Product Code； 4：品牌故事图的情况下为品牌名称； 5：尺码图的情况下为品牌名称 6：物流介绍的情况下为品牌名称 7：竖图的情况下为Product Code）
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
           图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
        */
        public String getImageTypeName()
        {
         
        return this.imageTypeName;
        }
        public void setImageTypeName(String imageTypeName)
        {
        if(imageTypeName!=null){
this.imageTypeName=imageTypeName;
 }
else
{
this.imageTypeName="";
}

        }
    
        
         /**
           图片类型（1:白底方图 ；2:商品详情图 ；3:参数图 ；7：竖图）
        */
        public int getImageType()
        {
         
        return this.imageType;
        }
        public void setImageType(int imageType)
        {
         this.imageType=imageType;
        }
    
        
         /**
           图片顺序
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
           自己服务器的图片url
        */
        public String getOriginUrl()
        {
         
        return this.originUrl;
        }
        public void setOriginUrl(String originUrl)
        {
        if(originUrl!=null){
this.originUrl=originUrl;
 }
else
{
this.originUrl="";
}

        }
    
        
         /**
           聚美服务器的url
        */
        public String getJmUrl()
        {
         
        return this.jmUrl;
        }
        public void setJmUrl(String jmUrl)
        {
        if(jmUrl!=null){
this.jmUrl=jmUrl;
 }
else
{
this.jmUrl="";
}

        }
    
        
         /**
           0:没上传 1:已经上传
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
           商品图片urlKey
        */
        public String getProductImageUrlKey()
        {
         
        return this.productImageUrlKey;
        }
        public void setProductImageUrlKey(String productImageUrlKey)
        {
        if(productImageUrlKey!=null){
this.productImageUrlKey=productImageUrlKey;
 }
else
{
this.productImageUrlKey="";
}

        }
    
}