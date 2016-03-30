package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsBtJmMasterBrandModel implements Serializable
{
   public CmsBtJmMasterBrandModel()
    {
        setName(""); 
        setEnName(""); 
        setCreated(DateHelp.getDefaultDate());
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
this.created=DateHelp.getDefaultDate();
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