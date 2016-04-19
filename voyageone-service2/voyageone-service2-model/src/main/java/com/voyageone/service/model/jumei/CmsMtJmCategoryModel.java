package com.voyageone.service.model.jumei;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;
import java.io.Serializable;
import com.voyageone.common.util.*;
import com.voyageone.common.help.DateHelp;
public class CmsMtJmCategoryModel implements Serializable
{
   public CmsMtJmCategoryModel()
    {
        setName(""); 
        setLevel(""); 
        setSeoKeywords(""); 
        setSeoDescription(""); 
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
     private String level;
    
    
 /**

        */
     private int parentCategoryId;
    
    
 /**

        */
     private String seoKeywords;
    
    
 /**

        */
     private String seoDescription;
    
    
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
        public String getLevel()
        {
         
        return this.level;
        }
        public void setLevel(String level)
        {
        if(level!=null){
this.level=level;
 }
else
{
this.level="";
}

        }
    
        
         /**
           
        */
        public int getParentCategoryId()
        {
         
        return this.parentCategoryId;
        }
        public void setParentCategoryId(int parentCategoryId)
        {
         this.parentCategoryId=parentCategoryId;
        }
    
        
         /**
           
        */
        public String getSeoKeywords()
        {
         
        return this.seoKeywords;
        }
        public void setSeoKeywords(String seoKeywords)
        {
        if(seoKeywords!=null){
this.seoKeywords=seoKeywords;
 }
else
{
this.seoKeywords="";
}

        }
    
        
         /**
           
        */
        public String getSeoDescription()
        {
         
        return this.seoDescription;
        }
        public void setSeoDescription(String seoDescription)
        {
        if(seoDescription!=null){
this.seoDescription=seoDescription;
 }
else
{
this.seoDescription="";
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