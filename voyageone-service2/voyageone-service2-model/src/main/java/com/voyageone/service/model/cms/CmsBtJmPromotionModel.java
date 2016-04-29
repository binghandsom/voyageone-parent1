package com.voyageone.service.model.cms;

import com.voyageone.common.util.DateTimeUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
public class CmsBtJmPromotionModel implements Serializable
{
   public CmsBtJmPromotionModel()
    {
        setChannelId(""); 
        setName(""); 
        setActivityPcId(""); 
        setActivityAppId(""); 
        setCmsBtJmMasterBrandName(""); 
        setBrand(""); 
        setCategory(""); 
        setActivityStart(DateTimeUtil.getCreatedDefaultDate());
        setActivityEnd(DateTimeUtil.getCreatedDefaultDate());
        setPrePeriodStart(DateTimeUtil.getCreatedDefaultDate());
        setPrePeriodEnd(DateTimeUtil.getCreatedDefaultDate());
        setCreated(DateTimeUtil.getCreatedDefaultDate());
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**
聚美活动编号（CMS系统）
        */
     private int id;
    
    
 /**
渠道商
        */
     private String channelId;
    
    
 /**
名称
        */
     private String name;
    
    
 /**
聚美专场PC端ID
        */
     private String activityPcId;
    
    
 /**
聚美专场APP端ID
        */
     private String activityAppId;
    
    
 /**

        */
     private int cmsBtJmMasterBrandId;
    
    
 /**

        */
     private String cmsBtJmMasterBrandName;
    
    
 /**
专场涉及品牌 明细汇总 顿号、分隔
        */
     private String brand;
    
    
 /**
专场涉及品类
        */
     private String category;
    
    
 /**
活动开始时间
        */
     private Date activityStart;
    
    
 /**
活动结束时间
        */
     private Date activityEnd;
    
    
 /**
预热开始时间
        */
     private Date prePeriodStart;
    
    
 /**
预热结束时间
        */
     private Date prePeriodEnd;
    
    
 /**
发货仓库ID
        */
     private int shippingSystemId;
    
    
 /**
创建时间
        */
     private Date created;
    
    
 /**
创建人
        */
     private String creater;
    
    
 /**
修改时间
        */
     private Timestamp modified;
    
    
 /**
修改人
        */
     private String modifier;
    
    
 /**

        */
     private boolean isDelete;
    
        
         /**
           聚美活动编号（CMS系统）
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
           渠道商
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
           名称
        */
        public String getName()
        {
         
        return this.name;
        }
        public void setName(String name)
        {
        if(name!=null){
this.name=name;
 }
else
{
this.name="";
}

        }
    
        
         /**
           聚美专场PC端ID
        */
        public String getActivityPcId()
        {
         
        return this.activityPcId;
        }
        public void setActivityPcId(String activityPcId)
        {
        if(activityPcId!=null){
this.activityPcId=activityPcId;
 }
else
{
this.activityPcId="";
}

        }
    
        
         /**
           聚美专场APP端ID
        */
        public String getActivityAppId()
        {
         
        return this.activityAppId;
        }
        public void setActivityAppId(String activityAppId)
        {
        if(activityAppId!=null){
this.activityAppId=activityAppId;
 }
else
{
this.activityAppId="";
}

        }
    
        
         /**
           
        */
        public int getCmsBtJmMasterBrandId()
        {
         
        return this.cmsBtJmMasterBrandId;
        }
        public void setCmsBtJmMasterBrandId(int cmsBtJmMasterBrandId)
        {
         this.cmsBtJmMasterBrandId=cmsBtJmMasterBrandId;
        }
    
        
         /**
           
        */
        public String getCmsBtJmMasterBrandName()
        {
         
        return this.cmsBtJmMasterBrandName;
        }
        public void setCmsBtJmMasterBrandName(String cmsBtJmMasterBrandName)
        {
        if(cmsBtJmMasterBrandName!=null){
this.cmsBtJmMasterBrandName=cmsBtJmMasterBrandName;
 }
else
{
this.cmsBtJmMasterBrandName="";
}

        }
    
        
         /**
           专场涉及品牌 明细汇总 顿号、分隔
        */
        public String getBrand()
        {
         
        return this.brand;
        }
        public void setBrand(String brand)
        {
        if(brand!=null){
this.brand=brand;
 }
else
{
this.brand="";
}

        }
    
        
         /**
           专场涉及品类
        */
        public String getCategory()
        {
         
        return this.category;
        }
        public void setCategory(String category)
        {
        if(category!=null){
this.category=category;
 }
else
{
this.category="";
}

        }
    
        
         /**
           活动开始时间
        */
        public Date getActivityStart()
        {
         
        return this.activityStart;
        }
        public void setActivityStart(Date activityStart)
        {
       if(activityStart!=null){
this.activityStart=activityStart;
 }
else
{
this.activityStart=DateTimeUtil.getCreatedDefaultDate();
}

        }
    
        
         /**
           活动结束时间
        */
        public Date getActivityEnd()
        {
         
        return this.activityEnd;
        }
        public void setActivityEnd(Date activityEnd)
        {
       if(activityEnd!=null){
this.activityEnd=activityEnd;
 }
else
{
this.activityEnd=DateTimeUtil.getCreatedDefaultDate();
}

        }
    
        
         /**
           预热开始时间
        */
        public Date getPrePeriodStart()
        {
         
        return this.prePeriodStart;
        }
        public void setPrePeriodStart(Date prePeriodStart)
        {
       if(prePeriodStart!=null){
this.prePeriodStart=prePeriodStart;
 }
else
{
this.prePeriodStart=DateTimeUtil.getCreatedDefaultDate();
}

        }
    
        
         /**
           预热结束时间
        */
        public Date getPrePeriodEnd()
        {
         
        return this.prePeriodEnd;
        }
        public void setPrePeriodEnd(Date prePeriodEnd)
        {
       if(prePeriodEnd!=null){
this.prePeriodEnd=prePeriodEnd;
 }
else
{
this.prePeriodEnd=DateTimeUtil.getCreatedDefaultDate();
}

        }
    
        
         /**
           发货仓库ID
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
           创建时间
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
           修改时间
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
           
        */
        public boolean getIsDelete()
        {
         
        return this.isDelete;
        }
        public void setIsDelete(boolean isDelete)
        {
         this.isDelete=isDelete;
        }
    
}