package com.voyageone.service.model.cms;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsMtImageCreateFileModel implements Serializable
{
   public CmsMtImageCreateFileModel()
    {
        setRequestQueryString(""); 
        setChannelId(""); 
        setFile(""); 
        setFilePath(""); 
        setVparam(""); 
        setOssFilePath(""); 
        setUsCdnFilePath(""); 
        setErrorMsg(""); 
        setCreated(DateHelp.getDefaultDate());
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
请求参数生成的hashcode
        */
     private long hashCode;
    
    
 /**
请求参数
        */
     private String requestQueryString;
    
    
 /**
渠道id
        */
     private String channelId;
    
    
 /**
模板id
        */
     private int templateId;
    
    
 /**
上传oss的的文件名
        */
     private String file;
    
    
 /**
本地文件路径
        */
     private String filePath;
    
    
 /**
模板参数
        */
     private String vparam;
    
    
 /**
阿里云oss 文件存放路径 不包含域名shenzhen-vo.oss-cn-shenzhen.aliyuncs.com
        */
     private String ossFilePath;
    
    
 /**
美国cdn 文件存放路径  仅channel:001  需要生成
        */
     private String usCdnFilePath;
    
    
 /**
1:生成成功
        */
     private int state;
    
    
 /**
oss阿里云上传状态   1:上传成功
        */
     private int ossState;
    
    
 /**
美国cdn上传状态     1:上传成功
        */
     private int uscdnState;
    
    
 /**
错误信息
        */
     private String errorMsg;
    
    
 /**
错误码
        */
     private int errorCode;
    
    
 /**
创建日期
        */
     private Date created;
    
    
 /**
创建人
        */
     private String creater;
    
    
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
           请求参数生成的hashcode
        */
        public long getHashCode()
        {
         
        return this.hashCode;
        }
        public void setHashCode(long hashCode)
        {
         this.hashCode=hashCode;
        }
    
        
         /**
           请求参数
        */
        public String getRequestQueryString()
        {
         
        return this.requestQueryString;
        }
        public void setRequestQueryString(String requestQueryString)
        {
        if(requestQueryString!=null){
this.requestQueryString=requestQueryString;
 }
else
{
this.requestQueryString="";
}

        }
    
        
         /**
           渠道id
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
           模板id
        */
        public int getTemplateId()
        {
         
        return this.templateId;
        }
        public void setTemplateId(int templateId)
        {
         this.templateId=templateId;
        }
    
        
         /**
           上传oss的的文件名
        */
        public String getFile()
        {
         
        return this.file;
        }
        public void setFile(String file)
        {
        if(file!=null){
this.file=file;
 }
else
{
this.file="";
}

        }
    
        
         /**
           本地文件路径
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
           模板参数
        */
        public String getVparam()
        {
         
        return this.vparam;
        }
        public void setVparam(String vparam)
        {
        if(vparam!=null){
this.vparam=vparam;
 }
else
{
this.vparam="";
}

        }
    
        
         /**
           阿里云oss 文件存放路径 不包含域名shenzhen-vo.oss-cn-shenzhen.aliyuncs.com
        */
        public String getOssFilePath()
        {
         
        return this.ossFilePath;
        }
        public void setOssFilePath(String ossFilePath)
        {
        if(ossFilePath!=null){
this.ossFilePath=ossFilePath;
 }
else
{
this.ossFilePath="";
}

        }
    
        
         /**
           美国cdn 文件存放路径  仅channel:001  需要生成
        */
        public String getUsCdnFilePath()
        {
         
        return this.usCdnFilePath;
        }
        public void setUsCdnFilePath(String usCdnFilePath)
        {
        if(usCdnFilePath!=null){
this.usCdnFilePath=usCdnFilePath;
 }
else
{
this.usCdnFilePath="";
}

        }
    
        
         /**
           1:生成成功
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
           oss阿里云上传状态   1:上传成功
        */
        public int getOssState()
        {
         
        return this.ossState;
        }
        public void setOssState(int ossState)
        {
         this.ossState=ossState;
        }
    
        
         /**
           美国cdn上传状态     1:上传成功
        */
        public int getUscdnState()
        {
         
        return this.uscdnState;
        }
        public void setUscdnState(int uscdnState)
        {
         this.uscdnState=uscdnState;
        }
    
        
         /**
           错误信息
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
           错误码
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