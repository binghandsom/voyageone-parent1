package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
public class JmBtPromotionProductModel implements Serializable
{
   public JmBtPromotionProductModel()
    {
        setChannelId(""); 
        setProductCode(""); 
        setJmHashId(""); 
        setErrorMsg(""); 
        setCreater(""); 
        setCreated(DateHelp.getDefaultDate());
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
渠道id
        */
     private String channelId;
    
    
 /**
推广活动Id
        */
     private int promotionId;
    
    
 /**
聚美产品ID（聚美系统）
        */
     private int jmBtProductId;
    
    
 /**
商品code 唯一标识一个商品（CMS系统）
        */
     private String productCode;
    
    
 /**
聚美HashID（聚美系统）
        */
     private String jmHashId;
    
    
 /**
每人限购（聚美系统）
        */
     private int limit;
    
    
 /**
同步更新异常信息
        */
     private String errorMsg;
    
    
 /**
更新聚美平台的状态:1:待更新2:同步更新完成 3:同步更新失败
        */
     private int synchState;
    
    
 /**
创建人
        */
     private String creater;
    
    
 /**
创建日期
        */
     private Date created;
    
    
 /**
修改日期
        */
     private Timestamp modified;
    
    
 /**
修改人
        */
     private String modifier;
    
    
 /**
0:未上新商品 1：已上新商品  (上新：新增到聚美平台)
        */
     private int state;
    
        
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
           推广活动Id
        */
        public int getPromotionId()
        {
         
        return this.promotionId;
        }
        public void setPromotionId(int promotionId)
        {
         this.promotionId=promotionId;
        }
    
        
         /**
           聚美产品ID（聚美系统）
        */
        public int getJmBtProductId()
        {
         
        return this.jmBtProductId;
        }
        public void setJmBtProductId(int jmBtProductId)
        {
         this.jmBtProductId=jmBtProductId;
        }
    
        
         /**
           商品code 唯一标识一个商品（CMS系统）
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
           聚美HashID（聚美系统）
        */
        public String getJmHashId()
        {
         
        return this.jmHashId;
        }
        public void setJmHashId(String jmHashId)
        {
        if(jmHashId!=null){
this.jmHashId=jmHashId;
 }
else
{
this.jmHashId="";
}

        }
    
        
         /**
           每人限购（聚美系统）
        */
        public int getLimit()
        {
         
        return this.limit;
        }
        public void setLimit(int limit)
        {
         this.limit=limit;
        }
    
        
         /**
           同步更新异常信息
        */
        public String getErrorMsg()
        {
         
        return this.errorMsg;
        }
        public void setErrorMsg(String errorMsg)
        {
        if(errorMsg!=null){
this.errorMsg=errorMsg;
 }
else
{
this.errorMsg="";
}

        }
    
        
         /**
           更新聚美平台的状态:1:待更新2:同步更新完成 3:同步更新失败
        */
        public int getSynchState()
        {
         
        return this.synchState;
        }
        public void setSynchState(int synchState)
        {
         this.synchState=synchState;
        }
    
        
         /**
           创建人
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
           创建日期
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
           修改日期
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
           修改人
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
           0:未上新商品 1：已上新商品  (上新：新增到聚美平台)
        */
        public int getState()
        {
         
        return this.state;
        }
        public void setState(int state)
        {
         this.state=state;
        }
    
}