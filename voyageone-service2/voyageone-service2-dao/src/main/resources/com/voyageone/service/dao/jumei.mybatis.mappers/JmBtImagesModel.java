package com.voyageone.service.dao.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
public class JmBtImagesModel implements Serializable
{
   public JmBtImagesModel()
    {
        setChannelId(""); 
        setImageKey1(""); 
        setImageKey2(""); 
        setImageKey3(""); 
        setOriginUrl(""); 
        setJmUrl(""); 
        setCreater(""); 
        setModifier(""); 

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
     private String imageKey1;
    
    
 /**
图片锁定条件（1:白底方图的情况下为prduct_type；2:商品详情图的情况下为prduct_type； 3:参数图的情况下为prduct_type； 4：品牌故事图的情况下为prduct_type； 5：尺码图的情况下为商品的prduct_type 6：物流介绍的情况下为prduct_type 7：竖图的情况下为prduct_type）
        */
     private String imageKey2;
    
    
 /**
图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"； 4：品牌故事图的情况下为size_type； 5：尺码图的情况下为商品的size_type 6：物流介绍的情况下为"shop_process" 7：竖图的情况下为"mobile_image"）
        */
     private String imageKey3;
    
    
 /**
图片类型（1:白底方图 2:商品详情图 3:参数图 4：品牌故事图 5：尺码图 6：物流介绍 7：竖图）
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
        public String getImageKey1()
        {
         
        return this.imageKey1;
        }
        public void setImageKey1(String imageKey1)
        {
        if(imageKey1!=null){
this.imageKey1=imageKey1;
 }
else
{
this.imageKey1="";
}

        }
    
        
         /**
           图片锁定条件（1:白底方图的情况下为prduct_type；2:商品详情图的情况下为prduct_type； 3:参数图的情况下为prduct_type； 4：品牌故事图的情况下为prduct_type； 5：尺码图的情况下为商品的prduct_type 6：物流介绍的情况下为prduct_type 7：竖图的情况下为prduct_type）
        */
        public String getImageKey2()
        {
         
        return this.imageKey2;
        }
        public void setImageKey2(String imageKey2)
        {
        if(imageKey2!=null){
this.imageKey2=imageKey2;
 }
else
{
this.imageKey2="";
}

        }
    
        
         /**
           图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"； 4：品牌故事图的情况下为size_type； 5：尺码图的情况下为商品的size_type 6：物流介绍的情况下为"shop_process" 7：竖图的情况下为"mobile_image"）
        */
        public String getImageKey3()
        {
         
        return this.imageKey3;
        }
        public void setImageKey3(String imageKey3)
        {
        if(imageKey3!=null){
this.imageKey3=imageKey3;
 }
else
{
this.imageKey3="";
}

        }
    
        
         /**
           图片类型（1:白底方图 2:商品详情图 3:参数图 4：品牌故事图 5：尺码图 6：物流介绍 7：竖图）
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