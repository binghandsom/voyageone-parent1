package com.voyageone.web2.cms.views.jm;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.impl.cms.jumei.CmsMtJmConfigService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSpecialExtensionModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsMtJmConfigModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/10/17.
 */
public class CmsBtJmPromotionExportContext {

    private CmsBtJmPromotionModel model;
    private CmsBtJmPromotionSpecialExtensionModel extModel;

    private final CmsMtJmConfigService cmsMtJmConfigService;

    @Autowired
    public CmsBtJmPromotionExportContext(CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean, CmsMtJmConfigService cmsMtJmConfigService) {
        this.model = cmsBtJmPromotionSaveBean.getModel();
        this.extModel = cmsBtJmPromotionSaveBean.getExtModel();
        this.cmsMtJmConfigService = cmsMtJmConfigService;
        if(this.extModel == null) this.extModel= new CmsBtJmPromotionSpecialExtensionModel();
    }

    public CmsBtJmPromotionModel getModel() {
        return model;
    }

    public void setModel(CmsBtJmPromotionModel model) {
        this.model = model;
    }

    public CmsBtJmPromotionSpecialExtensionModel getExtModel() {
        return extModel;
    }

    public void setExtModel(CmsBtJmPromotionSpecialExtensionModel extModel) {
        this.extModel = extModel;
    }

    public String getDisplayPlatform() {
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtil.isEmpty(this.extModel.getDisplayPlatform())) {
            List<Integer> displayPllatforms = JacksonUtil.jsonToBeanList(this.extModel.getDisplayPlatform(), Integer.class);
            CmsMtJmConfigModel cmsMtJmConfigModel = cmsMtJmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.displayPlatform);
            displayPllatforms.forEach(displayPllatform -> {
                Map<String, Object> value = cmsMtJmConfigModel.getValues().stream().filter(objectObjectMap -> objectObjectMap.get("value") == displayPllatform).findFirst().orElse(null);
                if(value != null){
                    stringBuffer.append(value.get("name"));
                    stringBuffer.append(" ");
                }
            });
        }
        return stringBuffer.toString();
    }

    public String getPreDisplayChannel(){
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtil.isEmpty(this.extModel.getDisplayPlatform())) {
            List<Integer> displayPllatforms = JacksonUtil.jsonToBeanList(this.extModel.getPreDisplayChannel(), Integer.class);
            CmsMtJmConfigModel cmsMtJmConfigModel = cmsMtJmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.preDisplayChannel);
            displayPllatforms.forEach(displayPllatform -> {
                Map<String, Object> value = cmsMtJmConfigModel.getValues().stream().filter(objectObjectMap -> objectObjectMap.get("value") == displayPllatform).findFirst().orElse(null);
                if(value != null){
                    stringBuffer.append(value.get("name"));
                    stringBuffer.append(" ");
                }
            });
        }
        return stringBuffer.toString();
    }

    public String getSessionType(){
        String sessionCategory = "";
        if (!StringUtil.isEmpty(this.extModel.getSessionType())) {
            CmsMtJmConfigModel cmsMtJmConfigModel = cmsMtJmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.sessionType);
            Map<String, Object> value = cmsMtJmConfigModel.getValues().stream().filter(objectObjectMap -> this.extModel.getSessionType().equalsIgnoreCase(objectObjectMap.get("value").toString()) ).findFirst().orElse(null);
            if(value != null){
                sessionCategory = value.get("name").toString();
            }
        }
        return sessionCategory;
    }


    public String getSessionCategory(){
        String sessionCategory = "";
        if (!StringUtil.isEmpty(this.extModel.getSessionCategory())) {
            CmsMtJmConfigModel cmsMtJmConfigModel = cmsMtJmConfigService.getCmsMtJmConfigById(CmsMtJmConfigService.JmCofigTypeEnum.sessionCategory);
            Map<String, Object> value = cmsMtJmConfigModel.getValues().stream().filter(objectObjectMap -> this.extModel.getSessionCategory().equalsIgnoreCase(objectObjectMap.get("value").toString()) ).findFirst().orElse(null);
            if(value != null){
                sessionCategory = value.get("name").toString();
            }
        }
        return sessionCategory;
    }

    public String getBrandString() {
        return String.format("%s (%s)", this.model.getCmsBtJmMasterBrandId(), this.model.getBrand());
    }

    public String getSyncMobile() {
        return getBooleanString(this.extModel.getSyncMobile());
    }

    public String getShowHiddenDeal() {
        return getBooleanString(this.extModel.getShowHiddenDeal());
    }

    public String getShowSoldOutDeal() {
        return getBooleanString(this.extModel.getShowSoldOutDeal());
    }

    private String getBooleanString(Integer value) {
        if (value == null || value == 0) {
            return "否";
        } else {
            return "是";
        }
    }
}
