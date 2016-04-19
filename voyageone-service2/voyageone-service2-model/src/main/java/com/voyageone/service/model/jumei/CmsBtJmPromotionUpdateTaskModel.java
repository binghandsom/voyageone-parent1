package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmPromotionUpdateTaskModel implements Serializable
{
   public CmsBtJmPromotionUpdateTaskModel()
    {
        setTaskParameter(""); 
        setBeginTime(DateHelp.getDefaultDate());
        setEndTime(DateHelp.getDefaultDate());
        setErrorMsg(""); 
        setCreated(DateHelp.getDefaultDate());
        setCreater(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
聚美推广活动id
        */
     private int jmBtPromotionId;
    
    
 /**
任务类型
        */
     private int TaskType;
    
    
 /**
任务参数
        */
     private String TaskParameter;
    
    
 /**
开始执行时间
        */
     private Date beginTime;
    
    
 /**
结束执行时间
        */
     private Date endTime;
    
    
 /**
异常错误信息
        */
     private String errorMsg;
    
    
 /**
1:更新完成 2:更新出错
        */
     private int state;
    
    
 /**
创建时间
        */
     private Date created;
    
    
 /**
创建人
        */
     private String creater;
    
        
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
           聚美推广活动id
        */
        public int getJmBtPromotionId()
        {
         
        return this.jmBtPromotionId;
        }
        public void setJmBtPromotionId(int jmBtPromotionId)
        {
         this.jmBtPromotionId=jmBtPromotionId;
        }
    
        
         /**
           任务类型
        */
        public int getTaskType()
        {
         
        return this.TaskType;
        }
        public void setTaskType(int TaskType)
        {
         this.TaskType=TaskType;
        }
    
        
         /**
           任务参数
        */
        public String getTaskParameter()
        {
         
        return this.TaskParameter;
        }
        public void setTaskParameter(String TaskParameter)
        {
        if(TaskParameter!=null){
this.TaskParameter=TaskParameter;
 }
else
{
this.TaskParameter="";
}

        }
    
        
         /**
           开始执行时间
        */
        public Date getBeginTime()
        {
         
        return this.beginTime;
        }
        public void setBeginTime(Date beginTime)
        {
       if(beginTime!=null){
this.beginTime=beginTime;
 }
else
{
this.beginTime=DateHelp.getDefaultDate();
}

        }
    
        
         /**
           结束执行时间
        */
        public Date getEndTime()
        {
         
        return this.endTime;
        }
        public void setEndTime(Date endTime)
        {
       if(endTime!=null){
this.endTime=endTime;
 }
else
{
this.endTime=DateHelp.getDefaultDate();
}

        }
    
        
         /**
           异常错误信息
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
           1:更新完成 2:更新出错
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
this.created=DateHelp.getDefaultDate();
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
    
}