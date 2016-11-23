package com.voyageone.service.bean.cms;

import com.voyageone.service.model.cms.CmsBtTagJmModuleExtensionModel;

/**
 * Created by james on 2016/10/17.
 */
public class CmsBtTagJmModuleExtensionBean {
    CmsBtTagJmModuleExtensionModel cmsBtTagJmModuleExtensionModel;
    public CmsBtTagJmModuleExtensionBean(CmsBtTagJmModuleExtensionModel cmsBtTagJmModuleExtensionModel){
        if(cmsBtTagJmModuleExtensionModel == null){
            this.cmsBtTagJmModuleExtensionModel = new CmsBtTagJmModuleExtensionModel();
        }else{
            this.cmsBtTagJmModuleExtensionModel = cmsBtTagJmModuleExtensionModel;
        }

    }

    public String getHideFlag(){
        switch (cmsBtTagJmModuleExtensionModel.getHideFlag()){
            case 1:
                return "不隐藏";
            case 2:
                return "预热时隐藏";
            case 3:
                return "正式时隐藏";
            case 4:
                return "自定义显示";
            default:
                return "";
        }
    }

    public String getShelfType(){
        switch (cmsBtTagJmModuleExtensionModel.getShelfType()){
            case 1:
                return "普通货架";
            default:
                return "";
        }
    }
    public String getImageType(){
        switch (cmsBtTagJmModuleExtensionModel.getImageType()){
            case 1:
                return "方图";
            case 2:
                return "竖图";
            default:
                return "";
        }
    }
    public String getProductsSortBy(){
        switch (cmsBtTagJmModuleExtensionModel.getProductsSortBy()){
            case 1:
                return "按销量降序";
            case 2:
                return "填写顺序";
            default:
                return "";
        }
    }
    public String getNoStockToLast(){
        if(cmsBtTagJmModuleExtensionModel.getNoStockToLast()){
            return "是";
        }else if(cmsBtTagJmModuleExtensionModel.getNoStockToLast() == false){
            return "否";
        }else{
            return "";
        }
    }

    public CmsBtTagJmModuleExtensionModel getCmsBtTagJmModuleExtensionModel() {
        return cmsBtTagJmModuleExtensionModel;
    }

    public void setCmsBtTagJmModuleExtensionModel(CmsBtTagJmModuleExtensionModel cmsBtTagJmModuleExtensionModel) {
        this.cmsBtTagJmModuleExtensionModel = cmsBtTagJmModuleExtensionModel;
    }


}
