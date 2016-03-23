package com.voyageone.task2.cms.model;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author morse.lu
 * @version 0.0.1, 16/3/9
 */
public class CmsBtFeedInfoGiltModel extends CmsBtFeedInfoModel {
    private String attributes_style_name;
    private String attributes_material_value;
    private String attributes_size_size_chart_id;

    public String getAttributes_style_name() {
        return attributes_style_name;
    }

    public void setAttributes_style_name(String attributes_style_name) {
        this.attributes_style_name = attributes_style_name;
    }

    public String getAttributes_material_value() {
        return attributes_material_value;
    }

    public void setAttributes_material_value(String attributes_material_value) {
        this.attributes_material_value = attributes_material_value;
    }

    public String getAttributes_size_size_chart_id() {
        return attributes_size_size_chart_id;
    }

    public void setAttributes_size_size_chart_id(String attributes_size_size_chart_id) {
        this.attributes_size_size_chart_id = attributes_size_size_chart_id;
    }

    public CmsBtFeedInfoModel getCmsBtFeedInfoModel(){
        CmsBtFeedInfoModel cmsBtFeedInfoModel =new CmsBtFeedInfoModel(this.channelId);
        cmsBtFeedInfoModel.setCategory(this.getCategory());
        cmsBtFeedInfoModel.setCode(this.getCode());
        cmsBtFeedInfoModel.setName(this.getName());
        cmsBtFeedInfoModel.setModel(this.getModel());
        cmsBtFeedInfoModel.setColor(this.getColor());
        cmsBtFeedInfoModel.setOrigin(this.getOrigin());
        cmsBtFeedInfoModel.setSizeType(this.getSizeType());
        if(this.getImage().size()>0){
            cmsBtFeedInfoModel.setImage(Arrays.asList(this.getImage().get(0).split(",")));
        }else{
            cmsBtFeedInfoModel.setImage(new ArrayList<>());
        }
        cmsBtFeedInfoModel.setBrand(this.getBrand());
        cmsBtFeedInfoModel.setWeight(this.getWeight());
        cmsBtFeedInfoModel.setShort_description(this.getShort_description());
        cmsBtFeedInfoModel.setLong_description(this.getLong_description());
        cmsBtFeedInfoModel.setSkus(this.getSkus());
        cmsBtFeedInfoModel.setUpdFlg(0);
        return  cmsBtFeedInfoModel;
    }
}
