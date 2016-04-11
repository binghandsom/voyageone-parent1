package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmPromotionImportTaskModel implements Serializable
{
   public CmsBtJmPromotionImportTaskModel()
    {
        setFileName(""); 
        setErrorMsg(""); 
        setFailuresFileName(""); 
        setBeginTime(DateHelp.getDefaultDate());
        setEndTime(DateHelp.getDefaultDate());
        setCreater(""); 
        setCreated(DateHelp.getDefaultDate());

    }
 
    
 /**

        */
     private int id;
    
    
 /**
推广活动id
        */
     private int cmsBtJmPromotionId;
    
    
 /**
是否导入
        */
     private boolean isImport;
    
    
 /**
导入的文件名
        */
     private String fileName;
    
    
 /**
异常信息
        */
     private String errorMsg;
    
    
 /**
功导入成功的行数
        */
     private int successRows;
    
    
 /**
导入失败的行数
        */
     private int failuresRows;
    
    
 /**
导入失败的行记录  存储的文件
        */
     private String failuresFileName;
    
    
 /**
0:导入成功  1:导入异常    2：(生成失败记录文件)
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
           是否导入
        */
        public boolean getIsImport()
        {
         
        return this.isImport;
        }
        public void setIsImport(boolean isImport)
        {
         this.isImport=isImport;
        }
    
        
         /**
           导入的文件名
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
           功导入成功的行数
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
           导入失败的行数
        */
        public int getFailuresRows()
        {
         
        return this.failuresRows;
        }
        public void setFailuresRows(int failuresRows)
        {
         this.failuresRows=failuresRows;
        }
    
        
         /**
           导入失败的行记录  存储的文件
        */
        public String getFailuresFileName()
        {
         
        return this.failuresFileName;
        }
        public void setFailuresFileName(String failuresFileName)
        {
        if(failuresFileName!=null){
this.failuresFileName=failuresFileName;
 }
else
{
this.failuresFileName="";
}

        }
    
        
         /**
           0:导入成功  1:导入异常    2：(生成失败记录文件)
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
this.beginTime=DateHelp.getDefaultDate();
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
this.endTime=DateHelp.getDefaultDate();
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
this.created=DateHelp.getDefaultDate();
}

        }
    
}