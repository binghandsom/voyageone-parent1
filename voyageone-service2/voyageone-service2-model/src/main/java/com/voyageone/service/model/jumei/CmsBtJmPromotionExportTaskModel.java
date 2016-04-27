package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;

public class CmsBtJmPromotionExportTaskModel implements Serializable
{
   public CmsBtJmPromotionExportTaskModel()
    {
        setFileName(""); 
        setFilePath(""); 
        setErrorMsg(""); 
        setBeginTime(DateTimeUtil.getCreatedDefaultDate());
        setEndTime(DateTimeUtil.getCreatedDefaultDate());
        setCreater(""); 
        setCreated(DateTimeUtil.getCreatedDefaultDate());

    }
 
    
 /**

        */
     private int id;
    
    
 /**
推广活动id
        */
     private int cmsBtJmPromotionId;
    
    
 /**
导出的文件名
        */
     private String fileName;
    
    
 /**
文件路径
        */
     private String filePath;
    
    
 /**
异常信息
        */
     private String errorMsg;
    
    
 /**
成功导出的行数
        */
     private int successRows;
    
    
 /**
1:导出失败
        */
     private int errorCode;
    
    
 /**
导入开始时间
        */
     private Date beginTime;
    
    
 /**
导入结束时间
        */
     private Date endTime;
    
    
 /**
创建人
        */
     private String creater;
    
    
 /**
创建时间
        */
     private Date created;
    
    
 /**
是否导出
        */
     private boolean isExport;
    
    
 /**
导出模板类型:0:导入模板1：价格修正模板 2:专场模板
        */
     private int templateType;
    
        
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
           推广活动id
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
           导出的文件名
        */
        public String getFileName()
        {
         
        return this.fileName;
        }
        public void setFileName(String fileName)
        {
        if(fileName!=null){
this.fileName=fileName;
 }
else
{
this.fileName="";
}

        }
    
        
         /**
           文件路径
        */
        public String getFilePath()
        {
         
        return this.filePath;
        }
        public void setFilePath(String filePath)
        {
        if(filePath!=null){
this.filePath=filePath;
 }
else
{
this.filePath="";
}

        }
    
        
         /**
           异常信息
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
           成功导出的行数
        */
        public int getSuccessRows()
        {
         
        return this.successRows;
        }
        public void setSuccessRows(int successRows)
        {
         this.successRows=successRows;
        }
    
        
         /**
           1:导出失败
        */
        public int getErrorCode()
        {
         
        return this.errorCode;
        }
        public void setErrorCode(int errorCode)
        {
         this.errorCode=errorCode;
        }
    
        
         /**
           导入开始时间
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
this.beginTime=DateTimeUtil.getCreatedDefaultDate();
}

        }
    
        
         /**
           导入结束时间
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
this.endTime=DateTimeUtil.getCreatedDefaultDate();
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
           是否导出
        */
        public boolean getIsExport()
        {
         
        return this.isExport;
        }
        public void setIsExport(boolean isExport)
        {
         this.isExport=isExport;
        }
    
        
         /**
           导出模板类型:0:导入模板1：价格修正模板 2:专场模板
        */
        public int getTemplateType()
        {
         
        return this.templateType;
        }
        public void setTemplateType(int templateType)
        {
         this.templateType=templateType;
        }
    
}