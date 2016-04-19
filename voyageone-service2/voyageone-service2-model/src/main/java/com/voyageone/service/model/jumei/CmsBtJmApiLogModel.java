package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmApiLogModel implements Serializable
{
   public CmsBtJmApiLogModel()
    {
        setApiType(""); 
        setErrorMsg(""); 
        setRemark(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
数据源id
        */
     private int sourceId;
    
    
 /**
操作类型
        */
     private String apiType;
    
    
 /**
错误消息
        */
     private String errorMsg;
    
    
 /**
0:成功
        */
     private int errorCode;
    
    
 /**
备注
        */
     private String remark;
    
        
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
           数据源id
        */
        public int getSourceId()
        {
         
        return this.sourceId;
        }
        public void setSourceId(int sourceId)
        {
         this.sourceId=sourceId;
        }
    
        
         /**
           操作类型
        */
        public String getApiType()
        {
         
        return this.apiType;
        }
        public void setApiType(String apiType)
        {
        if(apiType!=null){
this.apiType=apiType;
 }
else
{
this.apiType="";
}

        }
    
        
         /**
           错误消息
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
           0:成功
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
           备注
        */
        public String getRemark()
        {
         
        return this.remark;
        }
        public void setRemark(String remark)
        {
        if(remark!=null){
this.remark=remark;
 }
else
{
this.remark="";
}

        }
    
}