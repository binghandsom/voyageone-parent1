package com.voyageone.service.model.cms;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsMtImageCreateTemplateModel implements Serializable
{
   public CmsMtImageCreateTemplateModel()
    {
        setChannelId(""); 
        setName(""); 
        setContent(""); 
        setCreated(DateHelp.getDefaultDate());
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
渠道id
        */
     private String channelId;
    
    
 /**
模板名字
        */
     private String name;
    
    
 /**
模板内容
        */
     private String content;
    
    
 /**
高
        */
     private int height;
    
    
 /**
宽
        */
     private int width;
    
    
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
        public int getId()
        {
         
        return this.id;
        }
        public void setId(int id)
        {
         this.id=id;
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
           模板名字
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
           模板内容
        */
        public String getContent()
        {
         
        return this.content;
        }
        public void setContent(String content)
        {
        if(content!=null){
this.content=content;
 }
else
{
this.content="";
}

        }
    
        
         /**
           高
        */
        public int getHeight()
        {
         
        return this.height;
        }
        public void setHeight(int height)
        {
         this.height=height;
        }
    
        
         /**
           宽
        */
        public int getWidth()
        {
         
        return this.width;
        }
        public void setWidth(int width)
        {
         this.width=width;
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
    
}