package com.voyageone.service.model.cms;
import com.voyageone.common.util.DateTimeUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class CmsBtJmMasterBrandModel implements Serializable
{
   public CmsBtJmMasterBrandModel()
    {
        setName(""); 
        setEnName(""); 
        setCreated(DateTimeUtil.getCreatedDefaultDate());
        setCreater(""); 
        setModifier(""); 

    }
 
    
 /**

        */
     private int id;
    
    
 /**

        */
     private String name;
    
    
 /**

        */
     private int jmMasterBrandId;
    
    
 /**

        */
     private String enName;
    
    
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
           
        */
        public int getJmMasterBrandId()
        {
         
        return this.jmMasterBrandId;
        }
        public void setJmMasterBrandId(int jmMasterBrandId)
        {
         this.jmMasterBrandId=jmMasterBrandId;
        }
    
        
         /**
           
        */
        public String getEnName()
        {
         
        return this.enName;
        }
        public void setEnName(String enName)
        {
        if(enName!=null){
this.enName=enName;
 }
else
{
this.enName="";
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