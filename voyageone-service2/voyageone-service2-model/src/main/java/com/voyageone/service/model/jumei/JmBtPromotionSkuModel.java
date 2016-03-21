package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
public class JmBtPromotionSkuModel implements Serializable
{
   public JmBtPromotionSkuModel()
    {
        setProductCode(""); 
        setJmSkuNo(""); 
        setSku(""); 
        setUpc(""); 
        setJmSize(""); 
        setErrorMsg(""); 
        setCreater(""); 
        setCreated(DateHelp.getDefaultDate());
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
聚美活动ID
        */
     private int promotionId;
    
    
 /**
商品Code
        */
     private String productCode;
    
    
 /**
聚美SKU
        */
     private String jmSkuNo;
    
    
 /**
品牌方SKU(聚美商家商品编码)
        */
     private String sku;
    
    
 /**

        */
     private String upc;
    
    
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
        public int getPromotionId()
        {
         
        return this.promotionId;
        }
        public void setPromotionId(int promotionId)
        {
         this.promotionId=promotionId;
        }
    
        
         /**
           商品Code
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
           聚美SKU
        */
        public String getJmSkuNo()
        {
         
        return this.jmSkuNo;
        }
        public void setJmSkuNo(String jmSkuNo)
        {
        if(jmSkuNo!=null){
this.jmSkuNo=jmSkuNo;
 }
else
{
this.jmSkuNo="";
}

        }
    
        
         /**
           品牌方SKU(聚美商家商品编码)
        */
        public String getSku()
        {
         
        return this.sku;
        }
        public void setSku(String sku)
        {
        if(sku!=null){
this.sku=sku;
 }
else
{
this.sku="";
}

        }
    
        
         /**
           
        */
        public String getUpc()
        {
         
        return this.upc;
        }
        public void setUpc(String upc)
        {
        if(upc!=null){
this.upc=upc;
 }
else
{
this.upc="";
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
    
}