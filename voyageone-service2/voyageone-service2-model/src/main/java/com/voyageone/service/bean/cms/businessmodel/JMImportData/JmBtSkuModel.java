package com.voyageone.service.bean.cms.businessmodel.JMImportData;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
public class JmBtSkuModel implements Serializable
{
   public JmBtSkuModel()
    {
        setChannelId(""); 
        setProductCode(""); 
        setSku(""); 
        setUpcCode(""); 
        setSize(""); 
        setHscode(""); 
        setHsName(""); 
        setHsUnit(""); 
        setJumeiSpuNo(""); 
        setJumeiSkuNo(""); 
        setSendFlg(""); 
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int seq;
    
    
 /**

        */
     private String channelId;
    
    
 /**

        */
     private String productCode;
    
    
 /**

        */
     private String sku;
    
    
 /**

        */
     private String upcCode;
    
    
 /**

        */
     private double abroadPrice;
    
    
 /**

        */
     private double dealPrice;
    
    
 /**

        */
     private double marketPrice;
    
    
 /**

        */
     private String size;
    
    
 /**

        */
     private String hscode;
    
    
 /**

        */
     private String hsName;
    
    
 /**

        */
     private String hsUnit;
    
    
 /**

        */
     private String jumeiSpuNo;
    
    
 /**

        */
     private String jumeiSkuNo;
    
    
 /**

        */
     private String sendFlg;
    
    
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
        public int getSeq()
        {
         
        return this.seq;
        }
        public void setSeq(int seq)
        {
         this.seq=seq;
        }
    
        
         /**
           
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
        public String getUpcCode()
        {
         
        return this.upcCode;
        }
        public void setUpcCode(String upcCode)
        {
        if(upcCode!=null){
this.upcCode=upcCode;
 }
else
{
this.upcCode="";
}

        }
    
        
         /**
           
        */
        public double getAbroadPrice()
        {
         
        return this.abroadPrice;
        }
        public void setAbroadPrice(double abroadPrice)
        {
         this.abroadPrice=abroadPrice;
        }
    
        
         /**
           
        */
        public double getDealPrice()
        {
         
        return this.dealPrice;
        }
        public void setDealPrice(double dealPrice)
        {
         this.dealPrice=dealPrice;
        }
    
        
         /**
           
        */
        public double getMarketPrice()
        {
         
        return this.marketPrice;
        }
        public void setMarketPrice(double marketPrice)
        {
         this.marketPrice=marketPrice;
        }
    
        
         /**
           
        */
        public String getSize()
        {
         
        return this.size;
        }
        public void setSize(String size)
        {
        if(size!=null){
this.size=size;
 }
else
{
this.size="";
}

        }
    
        
         /**
           
        */
        public String getHscode()
        {
         
        return this.hscode;
        }
        public void setHscode(String hscode)
        {
        if(hscode!=null){
this.hscode=hscode;
 }
else
{
this.hscode="";
}

        }
    
        
         /**
           
        */
        public String getHsName()
        {
         
        return this.hsName;
        }
        public void setHsName(String hsName)
        {
        if(hsName!=null){
this.hsName=hsName;
 }
else
{
this.hsName="";
}

        }
    
        
         /**
           
        */
        public String getHsUnit()
        {
         
        return this.hsUnit;
        }
        public void setHsUnit(String hsUnit)
        {
        if(hsUnit!=null){
this.hsUnit=hsUnit;
 }
else
{
this.hsUnit="";
}

        }
    
        
         /**
           
        */
        public String getJumeiSpuNo()
        {
         
        return this.jumeiSpuNo;
        }
        public void setJumeiSpuNo(String jumeiSpuNo)
        {
        if(jumeiSpuNo!=null){
this.jumeiSpuNo=jumeiSpuNo;
 }
else
{
this.jumeiSpuNo="";
}

        }
    
        
         /**
           
        */
        public String getJumeiSkuNo()
        {
         
        return this.jumeiSkuNo;
        }
        public void setJumeiSkuNo(String jumeiSkuNo)
        {
        if(jumeiSkuNo!=null){
this.jumeiSkuNo=jumeiSkuNo;
 }
else
{
this.jumeiSkuNo="";
}

        }
    
        
         /**
           
        */
        public String getSendFlg()
        {
         
        return this.sendFlg;
        }
        public void setSendFlg(String sendFlg)
        {
        if(sendFlg!=null){
this.sendFlg=sendFlg;
 }
else
{
this.sendFlg="";
}

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