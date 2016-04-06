package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmPromotionProductModel implements Serializable
{
   public CmsBtJmPromotionProductModel()
    {
        setChannelId(""); 
        setProductCode(""); 
        setJmHashId(""); 
        setMarketPrice(new BigDecimal(0));
        setDealPrice(new BigDecimal(0));
        setCreater(""); 
        setCreated(DateHelp.getDefaultDate());
        setModifier(""); 
        setAppId(""); 
        setPcId(""); 
        setDiscount(new BigDecimal(0));
        setErrorMsg(""); 

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
     private int cmsBtJmPromotionId;
    
    
 /**
商品id
        */
     private int cmsBtJmProductId;
    
    
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
市场价格 大于等于团购价
        */
     private BigDecimal marketPrice;
    
    
 /**
团购价格 至少大于15元
        */
     private BigDecimal dealPrice;
    
    
 /**
更新聚美平台的状态:0:未更新  1:待上传 2:上传成功 3:上传异常
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
PC端模块ID
        */
     private String appId;
    
    
 /**
APP端模块ID
        */
     private String pcId;
    
    
 /**
折扣
        */
     private BigDecimal discount;
    
    
 /**
同步更新异常信息
        */
     private String errorMsg;
    
    
 /**
sku数量
        */
     private int skuCount;
    
        
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
        public int getCmsBtJmPromotionId()
        {
         
        return this.cmsBtJmPromotionId;
        }
        public void setCmsBtJmPromotionId(int cmsBtJmPromotionId)
        {
         this.cmsBtJmPromotionId=cmsBtJmPromotionId;
        }
    
        
         /**
           商品id
        */
        public int getCmsBtJmProductId()
        {
         
        return this.cmsBtJmProductId;
        }
        public void setCmsBtJmProductId(int cmsBtJmProductId)
        {
         this.cmsBtJmProductId=cmsBtJmProductId;
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
           市场价格 大于等于团购价
        */
        public BigDecimal getMarketPrice()
        {
         
        return this.marketPrice;
        }
        public void setMarketPrice(BigDecimal marketPrice)
        {
         this.marketPrice=marketPrice;
        }
    
        
         /**
           团购价格 至少大于15元
        */
        public BigDecimal getDealPrice()
        {
         
        return this.dealPrice;
        }
        public void setDealPrice(BigDecimal dealPrice)
        {
         this.dealPrice=dealPrice;
        }
    
        
         /**
           更新聚美平台的状态:0:未更新  1:待上传 2:上传成功 3:上传异常
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
    
        
         /**
           PC端模块ID
        */
        public String getAppId()
        {
         
        return this.appId;
        }
        public void setAppId(String appId)
        {
        if(appId!=null){
this.appId=appId;
 }
else
{
this.appId="";
}

        }
    
        
         /**
           APP端模块ID
        */
        public String getPcId()
        {
         
        return this.pcId;
        }
        public void setPcId(String pcId)
        {
        if(pcId!=null){
this.pcId=pcId;
 }
else
{
this.pcId="";
}

        }
    
        
         /**
           折扣
        */
        public BigDecimal getDiscount()
        {
         
        return this.discount;
        }
        public void setDiscount(BigDecimal discount)
        {
         this.discount=discount;
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
           sku数量
        */
        public int getSkuCount()
        {
         
        return this.skuCount;
        }
        public void setSkuCount(int skuCount)
        {
         this.skuCount=skuCount;
        }
    
}