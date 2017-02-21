package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsBtCustomPropDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by james on 2017/2/21.
 *
 */
@Service
public class CmsBtCustomPropService extends BaseService {

    private final CmsBtCustomPropDao cmsBtCustomPropDao;

    @Autowired
    public CmsBtCustomPropService(CmsBtCustomPropDao cmsBtCustomPropDao) {
        this.cmsBtCustomPropDao = cmsBtCustomPropDao;
    }

    public CmsBtCustomPropModel getCustomPropByCatChannel(String channelId, String cat){
        return cmsBtCustomPropDao.getCustomPropByCatChannel(channelId, cat);
    }

    public void update(CmsBtCustomPropModel cmsBtCustomPropModel){
        cmsBtCustomPropDao.update(cmsBtCustomPropModel);
    }

    /**
     * 根据类目和channel 获取第三放属性 带继承关系的
     * @param channelId
     * @param cat
     * @return
     */
    public CmsBtCustomPropModel getCustomPropByCatChannelExtend(String channelId, String cat){
        List<CmsBtCustomPropModel> cmsBtCustomPropModels = new ArrayList<>();
        CmsBtCustomPropModel cmsBtCustomProp = null;
        String cats[] = cat.split(">");
        String catPath ="";
        for(int i = 0; i<cats.length;i++){
            catPath += cats[i];
            CmsBtCustomPropModel item = getCustomPropByCatChannel(channelId, catPath);
            if(item != null){
                cmsBtCustomPropModels.add(item);
            }
            catPath +=">";
        }
        if(cmsBtCustomPropModels.size() > 0){
            Collections.reverse(cmsBtCustomPropModels);
            if(cmsBtCustomPropModels.size() > 1){
                cmsBtCustomProp = cmsBtCustomPropModels.get(0);
                for(int i=1;i<cmsBtCustomPropModels.size();i++){
                    merge(cmsBtCustomProp, cmsBtCustomPropModels.get(i));
                }
            }
        }
        return cmsBtCustomProp;
    }

    private CmsBtCustomPropModel merge(CmsBtCustomPropModel prop1, CmsBtCustomPropModel prop2){
        for(CmsBtCustomPropModel.Entity entity: prop2.getEntitys()){
            CmsBtCustomPropModel.Entity temp = prop1.getEntitys().stream()
                    .filter(entity1 -> entity.getNameEn().equalsIgnoreCase(entity1.getNameEn()))
                    .findFirst().orElse(null);
            if(temp == null){
                prop1.getEntitys().add(entity);
            }else{
                entity.forEach(temp::putIfAbsent);
            }
        }
        return prop1;
    }
}
