package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
public class JmBtDealOldModel implements Serializable
{
   public JmBtDealOldModel()
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
        setDeferFailMsg(""); 
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
店铺渠道编号（如：Sneakerhead为001,Jewelry为010）
        */
     private String channelId;
    
    
 /**
商品编号（来自品牌方）
        */
     private String productCode;
    
    
 /**
上传批次
        */
     private String dealId;
    
    
 /**
聚美活动销售开始时间
        */
     private String startTime;
    
    
 /**
聚美活动销售结束时间
        */
     private String endTime;
    
    
 /**
单个用户商品限购数量
        */
     private int userPurchaseLimit;
    
    
 /**
发货仓库ID
        */
     private int shippingSystemId;
    
    
 /**
商品长标题
        */
     private String productLongName;
    
    
 /**
商品中标题
        */
     private String productMediumName;
    
    
 /**
商品短标题
        */
     private String productShortName;
    
    
 /**
自定义搜索词
        */
     private String searchMetaTextCustom;
    
    
 /**
上传完成状态标签
        */
     private int synFlg;
    
    
 /**
聚美HashID
        */
     private String jumeiHashId;
    
    
 /**
专场ID（1）
        */
     private String specialActivityId1;
    
    
 /**
专场内模块ID（1）
        */
     private String shelfId1;
    
    
 /**
专场ID（2）
        */
     private String specialActivityId2;
    
    
 /**
专场内模块ID（2）
        */
     private String shelfId2;
    
    
 /**
专场ID（3）
        */
     private String specialActivityId3;
    
    
 /**
专场内模块ID（3）
        */
     private String shelfId3;
    
    
 /**
变更标签
        */
     private int deferDateFlg;
    
    
 /**
变更失败信息
        */
     private String deferFailMsg;
    
    
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
商品id
        */
     private int jmBtProductId;
    
        
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
           商品编号（来自品牌方）
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
           上传批次
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
           聚美活动销售开始时间
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
           聚美活动销售结束时间
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
           单个用户商品限购数量
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
           商品长标题
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
           商品中标题
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
           商品短标题
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
           自定义搜索词
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
           上传完成状态标签
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
           聚美HashID
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
           专场ID（1）
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
           专场内模块ID（1）
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
           专场ID（2）
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
           专场内模块ID（2）
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
           专场ID（3）
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
           专场内模块ID（3）
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
           变更标签
        */
        public int getDeferDateFlg()
        {
         
        return this.deferDateFlg;
        }
        public void setDeferDateFlg(int deferDateFlg)
        {
         this.deferDateFlg=deferDateFlg;
        }
    
        
         /**
           变更失败信息
        */
        public String getDeferFailMsg()
        {
         
        return this.deferFailMsg;
        }
        public void setDeferFailMsg(String deferFailMsg)
        {
        if(deferFailMsg!=null){
this.deferFailMsg=deferFailMsg;
 }
else
{
this.deferFailMsg="";
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
    
        
         /**
           商品id
        */
        public int getJmBtProductId()
        {
         
        return this.jmBtProductId;
        }
        public void setJmBtProductId(int jmBtProductId)
        {
         this.jmBtProductId=jmBtProductId;
        }
    
}