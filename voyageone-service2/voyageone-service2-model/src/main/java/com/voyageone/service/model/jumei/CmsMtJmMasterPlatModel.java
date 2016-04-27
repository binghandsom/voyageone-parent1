package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;

public class CmsMtJmMasterPlatModel implements Serializable
{
   public CmsMtJmMasterPlatModel()
    {
        setCode(""); 
        setKey(""); 
        setName1(""); 
        setName2(""); 
        setName3(""); 
        setName4(""); 
        setCreated(DateTimeUtil.getCreatedDefaultDate());
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**
0：brand 1:货币  2:仓库
        */
     private String code;
    
    
 /**

        */
     private String key;
    
    
 /**

        */
     private String name1;
    
    
 /**

        */
     private String name2;
    
    
 /**

        */
     private String name3;
    
    
 /**

        */
     private String name4;
    
    
 /**

        */
     private Date created;
    
    
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
        public int getId()
        {
         
        return this.id;
        }
        public void setId(int id)
        {
         this.id=id;
        }
    
        
         /**
           0：brand 1:货币  2:仓库
        */
        public String getCode()
        {
         
        return this.code;
        }
        public void setCode(String code)
        {
        if(code!=null){
this.code=code;
 }
else
{
this.code="";
}

        }
    
        
         /**
           
        */
        public String getKey()
        {
         
        return this.key;
        }
        public void setKey(String key)
        {
        if(key!=null){
this.key=key;
 }
else
{
this.key="";
}

        }
    
        
         /**
           
        */
        public String getName1()
        {
         
        return this.name1;
        }
        public void setName1(String name1)
        {
        if(name1!=null){
this.name1=name1;
 }
else
{
this.name1="";
}

        }
    
        
         /**
           
        */
        public String getName2()
        {
         
        return this.name2;
        }
        public void setName2(String name2)
        {
        if(name2!=null){
this.name2=name2;
 }
else
{
this.name2="";
}

        }
    
        
         /**
           
        */
        public String getName3()
        {
         
        return this.name3;
        }
        public void setName3(String name3)
        {
        if(name3!=null){
this.name3=name3;
 }
else
{
this.name3="";
}

        }
    
        
         /**
           
        */
        public String getName4()
        {
         
        return this.name4;
        }
        public void setName4(String name4)
        {
        if(name4!=null){
this.name4=name4;
 }
else
{
this.name4="";
}

        }
    
        
         /**
           
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