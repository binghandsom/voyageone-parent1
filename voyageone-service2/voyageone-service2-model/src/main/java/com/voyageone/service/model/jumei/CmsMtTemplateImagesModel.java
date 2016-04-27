package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;

public class CmsMtTemplateImagesModel implements Serializable
{
   public CmsMtTemplateImagesModel()
    {
        setChannelId(""); 
        setImageTemplateUrl(""); 
        setCreated(DateTimeUtil.getCreatedDefaultDate());
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
平台id   聚美27
        */
     private int platformId;
    
    
 /**
店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
        */
     private String channelId;
    
    
 /**
图片模板类别（1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图））
        */
     private int templateType;
    
    
 /**
图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
        */
     private String imageTemplateUrl;
    
    
 /**
0:有变更需刷images表 1:已经同步images表
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
           平台id   聚美27
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
           图片模板类别（1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图））
        */
        public int getTemplateType()
        {
         
        return this.templateType;
        }
        public void setTemplateType(int templateType)
        {
         this.templateType=templateType;
        }
    
        
         /**
           图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
        */
        public String getImageTemplateUrl()
        {
         
        return this.imageTemplateUrl;
        }
        public void setImageTemplateUrl(String imageTemplateUrl)
        {
        if(imageTemplateUrl!=null){
this.imageTemplateUrl=imageTemplateUrl;
 }
else
{
this.imageTemplateUrl="";
}

        }
    
        
         /**
           0:有变更需刷images表 1:已经同步images表
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
    
}