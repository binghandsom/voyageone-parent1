package com.voyageone.service.model.jumei.businessmodel;

import com.voyageone.service.model.jumei.CmsMtMasterInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/12.
 */
public class CmsBtJmImportSaveInfo {
    List<CmsBtJmProductImportSaveInfo> listProductSaveInfo;
   Map<String,CmsMtMasterInfoModel>  mapSaveCmsMtMasterInfoModel;
    public CmsBtJmImportSaveInfo() {
        listProductSaveInfo = new ArrayList<>();
        mapSaveCmsMtMasterInfoModel = new HashMap<String,CmsMtMasterInfoModel>();
    }
    public List<CmsBtJmProductImportSaveInfo> getListProductSaveInfo() {
        return listProductSaveInfo;
    }
    public void setListProductSaveInfo(List<CmsBtJmProductImportSaveInfo> listProductSaveInfo) {
        this.listProductSaveInfo = listProductSaveInfo;
    }

    public Map<String, CmsMtMasterInfoModel> getMapSaveCmsMtMasterInfoModel() {
        return mapSaveCmsMtMasterInfoModel;
    }

    public void setMapSaveCmsMtMasterInfoModel(Map<String, CmsMtMasterInfoModel> mapSaveCmsMtMasterInfoModel) {
        this.mapSaveCmsMtMasterInfoModel = mapSaveCmsMtMasterInfoModel;
    }

    public  void  add(CmsMtMasterInfoModel model) {
        //`platform_id`,`channel_id`,`brand_name`,`product_type`,`size_type`,`data_type`,`image_index`
        String key = model.getPlatformId() + model.getChannelId() + model.getBrandName() + model.getProductType() + model.getSizeType() + model.getDataType() + model.getImageIndex();
        mapSaveCmsMtMasterInfoModel.put(key, model);
    }
}
