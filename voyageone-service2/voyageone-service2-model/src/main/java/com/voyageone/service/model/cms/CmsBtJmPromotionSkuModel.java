package com.voyageone.service.model.cms;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;

public class CmsBtJmPromotionSkuModel implements Serializable
{
   public CmsBtJmPromotionSkuModel()
    {
        setSkuCode(""); 
        setJmSize(""); 
        setMarketPrice(new BigDecimal(0));
        setDiscount(new BigDecimal(0));
        setDealPrice(new BigDecimal(0));
        setErrorMsg(""); 
        setCreater(""); 
        setCreated(DateTimeUtil.getCreatedDefaultDate());
        setModifier(""); 
        setChannelId(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
聚美活动ID
        */
     private int cmsBtJmPromotionId;
    
    
 /**
商品id
        */
     private int cmsBtJmProductId;
    
    
 /**
sku id
        */
     private int cmsBtJmSkuId;
    
    
 /**
规格代码
        */
     private String skuCode;
    
    
 /**
尺码（聚美系统）
        */
     private String jmSize;
    
    
 /**
市场价格 大于等于团购价
        */
     private BigDecimal marketPrice;
    
    
 /**
折扣
        */
     private BigDecimal discount;
    
    
 /**
团购价格 至少大于15元
        */
     private BigDecimal dealPrice;
    
    
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
0:未上新 1：已上新  (上新：新增到聚美平台)
        */
     private int state;
    
    
 /**
渠道商id
        */
     private String channelId;
    
    
 /**
更新状态
        */
     private int updateState;
    
        
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
           聚美活动ID
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
           sku id
        */
        public int getCmsBtJmSkuId()
        {
         
        return this.cmsBtJmSkuId;
        }
        public void setCmsBtJmSkuId(int cmsBtJmSkuId)
        {
         this.cmsBtJmSkuId=cmsBtJmSkuId;
        }
    
        
         /**
           规格代码
        */
        public String getSkuCode()
        {
         
        return this.skuCode;
        }
        public void setSkuCode(String skuCode)
        {
        if(skuCode!=null){
this.skuCode=skuCode;
 }
else
{
this.skuCode="";
}

        }
    
        
         /**
           尺码（聚美系统）
        */
        public String getJmSize()
        {
         
        return this.jmSize;
        }
        public void setJmSize(String jmSize)
        {
        if(jmSize!=null){
this.jmSize=jmSize;
 }
else
{
this.jmSize="";
}

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
this.created=DateTimeUtil.getCreatedDefaultDate();
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
           0:未上新 1：已上新  (上新：新增到聚美平台)
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
           渠道商id
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
           更新状态
        */
        public int getUpdateState()
        {
         
        return this.updateState;
        }
        public void setUpdateState(int updateState)
        {
         this.updateState=updateState;
        }
    
}