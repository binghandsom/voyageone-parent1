package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by james on 2017/2/21.
 */
public class CmsBtCustomPropModel extends BaseMongoModel {
    private String cat;
    private String channelId = "";
    private List<Entity> entitys;

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

    public List<Entity> getEntitys() {
        if(entitys == null) entitys = new ArrayList<>();
        return entitys;
    }

    public void setEntitys(List<Entity> entitys) {
        this.entitys = entitys;
    }

    public static class Entity extends BaseMongoMap<String,Object> {


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
            setAttribute("nameEn", type);
        }

        public Boolean getChecked(){
            return getAttribute("checked");
        }

        public void setChecked (Boolean checked){
            setAttribute("checked", checked);
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
