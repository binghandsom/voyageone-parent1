package com.voyageone.service.bean.cms.businessmodel.JMImportData;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
public class JmBtDealImportModel implements Serializable
{
   public JmBtDealImportModel()
    {
        setChannelId(""); 
        setProductCode(""); 
        setDealId(""); 
        setStartTime(""); 
        setEndTime(""); 
        setProductLongName(""); 
        setProductMediumName(""); 
        setProductShortName(""); 
        setSearchMetaTextCustom(""); 
        setJumeiHashId(""); 
        setSpecialActivityId1(""); 
        setShelfId1(""); 
        setSpecialActivityId2(""); 
        setShelfId2(""); 
        setSpecialActivityId3(""); 
        setShelfId3(""); 
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
     private String dealId;
    
    
 /**

        */
     private String startTime;
    
    
 /**

        */
     private String endTime;
    
    
 /**

        */
     private int userPurchaseLimit;
    
    
 /**

        */
     private int shippingSystemId;
    
    
 /**

        */
     private String productLongName;
    
    
 /**

        */
     private String productMediumName;
    
    
 /**

        */
     private String productShortName;
    
    
 /**

        */
     private String searchMetaTextCustom;
    
    
 /**

        */
     private int synFlg;
    
    
 /**

        */
     private String jumeiHashId;
    
    
 /**

        */
     private String specialActivityId1;
    
    
 /**

        */
     private String shelfId1;
    
    
 /**

        */
     private String specialActivityId2;
    
    
 /**

        */
     private String shelfId2;
    
    
 /**

        */
     private String specialActivityId3;
    
    
 /**

        */
     private String shelfId3;
    
    
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
           
        */
        public String getStartTime()
        {
         
        return this.startTime;
        }
        public void setStartTime(String startTime)
        {
        if(startTime!=null){
this.startTime=startTime;
 }
else
{
this.startTime="";
}

        }
    
        
         /**
           
        */
        public String getEndTime()
        {
         
        return this.endTime;
        }
        public void setEndTime(String endTime)
        {
        if(endTime!=null){
this.endTime=endTime;
 }
else
{
this.endTime="";
}

        }
    
        
         /**
           
        */
        public int getUserPurchaseLimit()
        {
         
        return this.userPurchaseLimit;
        }
        public void setUserPurchaseLimit(int userPurchaseLimit)
        {
         this.userPurchaseLimit=userPurchaseLimit;
        }
    
        
         /**
           
        */
        public int getShippingSystemId()
        {
         
        return this.shippingSystemId;
        }
        public void setShippingSystemId(int shippingSystemId)
        {
         this.shippingSystemId=shippingSystemId;
        }
    
        
         /**
           
        */
        public String getProductLongName()
        {
         
        return this.productLongName;
        }
        public void setProductLongName(String productLongName)
        {
        if(productLongName!=null){
this.productLongName=productLongName;
 }
else
{
this.productLongName="";
}

        }
    
        
         /**
           
        */
        public String getProductMediumName()
        {
         
        return this.productMediumName;
        }
        public void setProductMediumName(String productMediumName)
        {
        if(productMediumName!=null){
this.productMediumName=productMediumName;
 }
else
{
this.productMediumName="";
}

        }
    
        
         /**
           
        */
        public String getProductShortName()
        {
         
        return this.productShortName;
        }
        public void setProductShortName(String productShortName)
        {
        if(productShortName!=null){
this.productShortName=productShortName;
 }
else
{
this.productShortName="";
}

        }
    
        
         /**
           
        */
        public String getSearchMetaTextCustom()
        {
         
        return this.searchMetaTextCustom;
        }
        public void setSearchMetaTextCustom(String searchMetaTextCustom)
        {
        if(searchMetaTextCustom!=null){
this.searchMetaTextCustom=searchMetaTextCustom;
 }
else
{
this.searchMetaTextCustom="";
}

        }
    
        
         /**
           
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
        public String getJumeiHashId()
        {
         
        return this.jumeiHashId;
        }
        public void setJumeiHashId(String jumeiHashId)
        {
        if(jumeiHashId!=null){
this.jumeiHashId=jumeiHashId;
 }
else
{
this.jumeiHashId="";
}

        }
    
        
         /**
           
        */
        public String getSpecialActivityId1()
        {
         
        return this.specialActivityId1;
        }
        public void setSpecialActivityId1(String specialActivityId1)
        {
        if(specialActivityId1!=null){
this.specialActivityId1=specialActivityId1;
 }
else
{
this.specialActivityId1="";
}

        }
    
        
         /**
           
        */
        public String getShelfId1()
        {
         
        return this.shelfId1;
        }
        public void setShelfId1(String shelfId1)
        {
        if(shelfId1!=null){
this.shelfId1=shelfId1;
 }
else
{
this.shelfId1="";
}

        }
    
        
         /**
           
        */
        public String getSpecialActivityId2()
        {
         
        return this.specialActivityId2;
        }
        public void setSpecialActivityId2(String specialActivityId2)
        {
        if(specialActivityId2!=null){
this.specialActivityId2=specialActivityId2;
 }
else
{
this.specialActivityId2="";
}

        }
    
        
         /**
           
        */
        public String getShelfId2()
        {
         
        return this.shelfId2;
        }
        public void setShelfId2(String shelfId2)
        {
        if(shelfId2!=null){
this.shelfId2=shelfId2;
 }
else
{
this.shelfId2="";
}

        }
    
        
         /**
           
        */
        public String getSpecialActivityId3()
        {
         
        return this.specialActivityId3;
        }
        public void setSpecialActivityId3(String specialActivityId3)
        {
        if(specialActivityId3!=null){
this.specialActivityId3=specialActivityId3;
 }
else
{
this.specialActivityId3="";
}

        }
    
        
         /**
           
        */
        public String getShelfId3()
        {
         
        return this.shelfId3;
        }
        public void setShelfId3(String shelfId3)
        {
        if(shelfId3!=null){
this.shelfId3=shelfId3;
 }
else
{
this.shelfId3="";
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