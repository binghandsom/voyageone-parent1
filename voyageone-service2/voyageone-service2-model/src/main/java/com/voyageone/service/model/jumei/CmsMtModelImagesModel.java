package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsMtModelImagesModel implements Serializable
{
   public CmsMtModelImagesModel()
    {
        setChannelId(""); 
        setKeyExtend1(""); 
        setKeyExtend2(""); 
        setModelType(""); 
        setImageModelUrl(""); 
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**

        */
     private int cartId;
    
    
 /**
店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
        */
     private String channelId;
    
    
 /**
对模板有细节要求的情况下塞品牌（brand name）
        */
     private String keyExtend1;
    
    
 /**
对模板有细节要求的情况下塞品类（product type）
        */
     private String keyExtend2;
    
    
 /**
图片模板类别（1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图））
        */
     private String modelType;
    
    
 /**
图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
        */
     private String imageModelUrl;
    
    
 /**
0:有变更需刷images表 1:已经同步images表
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
           
        */
        public int getCartId()
        {
         
        return this.cartId;
        }
        public void setCartId(int cartId)
        {
         this.cartId=cartId;
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
           对模板有细节要求的情况下塞品牌（brand name）
        */
        public String getKeyExtend1()
        {
         
        return this.keyExtend1;
        }
        public void setKeyExtend1(String keyExtend1)
        {
        if(keyExtend1!=null){
this.keyExtend1=keyExtend1;
 }
else
{
this.keyExtend1="";
}

        }
    
        
         /**
           对模板有细节要求的情况下塞品类（product type）
        */
        public String getKeyExtend2()
        {
         
        return this.keyExtend2;
        }
        public void setKeyExtend2(String keyExtend2)
        {
        if(keyExtend2!=null){
this.keyExtend2=keyExtend2;
 }
else
{
this.keyExtend2="";
}

        }
    
        
         /**
           图片模板类别（1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图））
        */
        public String getModelType()
        {
         
        return this.modelType;
        }
        public void setModelType(String modelType)
        {
        if(modelType!=null){
this.modelType=modelType;
 }
else
{
this.modelType="";
}

        }
    
        
         /**
           图片锁定条件（1:白底方图的情况下为"main_image"；2:商品详情图的情况下为"deal_image"； 3:参数图的情况下为"property_image"）
        */
        public String getImageModelUrl()
        {
         
        return this.imageModelUrl;
        }
        public void setImageModelUrl(String imageModelUrl)
        {
        if(imageModelUrl!=null){
this.imageModelUrl=imageModelUrl;
 }
else
{
this.imageModelUrl="";
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