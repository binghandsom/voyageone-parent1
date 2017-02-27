package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/2/21.
 */
public class CmsBtCustomPropModel extends BaseMongoModel {
    private String cat;
    private String channelId;
    private String orgChannelId="";
    private List<Entity> entitys;
    private List<String> sort;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getOrgChannelId() {
        return orgChannelId;
    }

    public void setOrgChannelId(String orgChannelId) {
        this.orgChannelId = orgChannelId;
    }

    public List<Entity> getEntitys() {
        if(entitys == null) entitys = new ArrayList<>();
        return entitys;
    }

    public void setEntitys(List<Entity> entitys) {
        this.entitys = entitys;
    }

    public List<String> getSort() {
        if(sort == null) sort = new ArrayList<>();
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public static class Entity extends BaseMongoMap<String,Object> {

        public Entity(Map map){
            putAll(map);
        }

        public Entity(){

        }


        public String getNameEn(){
            return getStringAttribute("nameEn");
        }
        public void setNameEn(String nameEn){
            setStringAttribute("nameEn", nameEn);
        }

        public String getNameCn(){
            return getStringAttribute("nameCn");
        }
        public void setNameCn(String nameCn){
            setStringAttribute("nameCn", nameCn);
        }

        public Integer getType(){
            return getAttribute("type");
        }
        public void setType(Integer type){
            setAttribute("type", type);
        }

        public Boolean getChecked(){
            return getAttribute("checked");
        }

        public void setChecked (Boolean checked){
            setAttribute("checked", checked);
        }

        public Integer getAttributeType(){
            return getIntAttribute("attributeType");
        }
        public void setAttributeType(Integer attributeType){
            setAttribute("attributeType", attributeType);
        }

        public String getValue(){
            return getStringAttribute("value");
        }
        public void setValue(String value){
            setStringAttribute("value", value);
        }
    }

    public enum CustomPropType{
        Common(1),
        Main(2),
        Feed(3),
        Custom(4);
        private Integer id;
        CustomPropType(Integer id){
            this.id = id;
        }

        public Integer getValue(){
            return this.id;
        }
    }
}
