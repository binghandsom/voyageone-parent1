package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmSkuModel implements Serializable
{
   public CmsBtJmSkuModel()
    {
        setDealId(""); 
        setProductCode(""); 
        setSku(""); 
        setJmSpuNo(""); 
        setJmSkuNo(""); 
        setUpc(""); 
        setCmsSize(""); 
        setJmSize(""); 
        setCreater(""); 
        setCreated(DateHelp.getDefaultDate());
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**

        */
     private String dealId;
    
    
 /**
商品Code
        */
     private String productCode;
    
    
 /**
产品ID
        */
     private int cmsBtJmProductId;
    
    
 /**
品牌方SKU(聚美商家商品编码)
        */
     private String sku;
    
    
 /**
聚美子产品ID
        */
     private String jmSpuNo;
    
    
 /**
聚美SKU
        */
     private String jmSkuNo;
    
    
 /**
商品条形码
        */
     private String upc;
    
    
 /**
尺码(VO系统)
        */
     private String cmsSize;
    
    
 /**
尺码（聚美系统）
        */
     private String jmSize;
    
    
 /**
同步状态||0:未上新 1:已新增到聚美平台
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
           产品ID
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
           聚美子产品ID
        */
        public String getJmSpuNo()
        {
         
        return this.jmSpuNo;
        }
        public void setJmSpuNo(String jmSpuNo)
        {
        if(jmSpuNo!=null){
this.jmSpuNo=jmSpuNo;
 }
else
{
this.jmSpuNo="";
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
           商品条形码
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
           尺码(VO系统)
        */
        public String getCmsSize()
        {
         
        return this.cmsSize;
        }
        public void setCmsSize(String cmsSize)
        {
        if(cmsSize!=null){
this.cmsSize=cmsSize;
 }
else
{
this.cmsSize="";
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
           同步状态||0:未上新 1:已新增到聚美平台
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