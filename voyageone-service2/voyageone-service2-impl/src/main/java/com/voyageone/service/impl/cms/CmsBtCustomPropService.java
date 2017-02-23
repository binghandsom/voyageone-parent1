package com.voyageone.service.impl.cms;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
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
 */
@Service
public class CmsBtCustomPropService extends BaseService {

    private final CmsBtCustomPropDao cmsBtCustomPropDao;

    @Autowired
    public CmsBtCustomPropService(CmsBtCustomPropDao cmsBtCustomPropDao) {
        this.cmsBtCustomPropDao = cmsBtCustomPropDao;
    }

    public CmsBtCustomPropModel getCustomPropByCatChannel(String channelId, String orgChannelId, String cat) {
        return cmsBtCustomPropDao.getCustomPropByCatChannel(channelId, orgChannelId, cat);
    }

    public void update(CmsBtCustomPropModel cmsBtCustomPropModel) {
        if (cmsBtCustomPropModel.get_id() != null) {
            cmsBtCustomPropDao.update(cmsBtCustomPropModel);
        } else {
            CmsBtCustomPropModel old = getCustomPropByCatChannel(cmsBtCustomPropModel.getChannelId(), cmsBtCustomPropModel.getOrgChannelId(), cmsBtCustomPropModel.getCat());
            if (old == null) {
                cmsBtCustomPropDao.insert(cmsBtCustomPropModel);
            } else {
                cmsBtCustomPropModel.set_id(old.get_id());
                cmsBtCustomPropDao.update(cmsBtCustomPropModel);
            }
        }
    }

    /**
     * 根据类目和channel 获取第三放属性 带继承关系的
     *
     * @param channelId
     * @param cat
     * @return
     */
    public CmsBtCustomPropModel getCustomPropByCatChannelExtend(String channelId, String orgChannelId, String cat) {
        List<CmsBtCustomPropModel> cmsBtCustomPropModels = new ArrayList<>();
        CmsBtCustomPropModel cmsBtCustomProp = null;
        String cats[] = cat.split(">");
        String catPath = "";
        // 先找以下不带orgChannelId设置
        if (!StringUtil.isEmpty(orgChannelId)) {
            cmsBtCustomPropModels.addAll(getCustomPropByCatPath(channelId, "", cat));
        }

        cmsBtCustomPropModels.addAll(getCustomPropByCatPath(channelId, orgChannelId, cat));

        if (cmsBtCustomPropModels.size() > 0) {
            Collections.reverse(cmsBtCustomPropModels);
            if (cmsBtCustomPropModels.size() > 1) {
                cmsBtCustomProp = cmsBtCustomPropModels.get(0);
                for (int i = 1; i < cmsBtCustomPropModels.size(); i++) {
                    merge(cmsBtCustomProp, cmsBtCustomPropModels.get(i));
                }
            }
        }


        return cmsBtCustomProp;
    }

    public List<CmsBtCustomPropModel> getCustomPropByCatPath(String channelId, String orgChannelId, String cat) {
        List<CmsBtCustomPropModel> cmsBtCustomPropModels = new ArrayList<>();
        String cats[] = cat.split(">");
        String catPath = "";
        CmsBtCustomPropModel item = getCustomPropByCatChannel(channelId, orgChannelId, "all");
        if (item != null) {
            cmsBtCustomPropModels.add(item);
        }
        // 先找以下不带orgChannelId设置
        for (int i = 0; i < cats.length; i++) {
            catPath += cats[i];
            item = getCustomPropByCatChannel(channelId, orgChannelId, catPath);
            if (item != null) {
                cmsBtCustomPropModels.add(item);
            }
            catPath += ">";
        }
        return cmsBtCustomPropModels;
    }

    /**
     * 设置属性是否打勾
     * @param channelId
     * @param orgChannelId
     * @param cat
     * @param entity
     * @return
     */
    public CmsBtCustomPropModel setCustomshIsDispPlay(String channelId, String orgChannelId, String cat, CmsBtCustomPropModel.Entity entity) {
        CmsBtCustomPropModel cmsBtCustomPropExtend = getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
        CmsBtCustomPropModel cmsBtCustomPropModel = getCustomPropByCatChannel(channelId, orgChannelId, cat);
        if (cmsBtCustomPropModel == null) {
            cmsBtCustomPropModel = new CmsBtCustomPropModel();
            cmsBtCustomPropModel.setCat(cat);
            cmsBtCustomPropModel.setChannelId(channelId);
            cmsBtCustomPropModel.setOrgChannelId(orgChannelId);
        }
        CmsBtCustomPropModel.Entity item = cmsBtCustomPropModel.getEntitys().stream()
                .filter(entity1 -> entity1.getNameEn().equalsIgnoreCase(entity.getNameEn()) && entity.getType() == entity1.getType())
                .findFirst().orElse(null);
        if (item == null) {
            item = new CmsBtCustomPropModel.Entity();
            item.setNameCn(entity.getNameCn());
            item.setNameEn(entity.getNameEn());
            item.setType(entity.getType());
            item.setChecked(entity.getChecked());
            cmsBtCustomPropModel.getEntitys().add(item);
        } else {
            item.setChecked(entity.getChecked());
        }
        cmsBtCustomPropModel.setSort(cmsBtCustomPropExtend.getSort());
        if (entity.getChecked()) {
            cmsBtCustomPropModel.getSort().add(entity.getNameEn());
        } else {
            cmsBtCustomPropModel.getSort().remove(entity.getNameEn());
        }
        update(cmsBtCustomPropModel);
        return getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
    }

    /**
     * 更新属性的设置
     * @param channelId
     * @param orgChannelId
     * @param cat
     * @param entity
     * @return
     */
    public CmsBtCustomPropModel updateEntity(String channelId, String orgChannelId, String cat, CmsBtCustomPropModel.Entity entity) {
        CmsBtCustomPropModel cmsBtCustomPropExtend = getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
        CmsBtCustomPropModel cmsBtCustomPropModel = getCustomPropByCatChannel(channelId, orgChannelId, cat);
        if (cmsBtCustomPropModel == null) {
            cmsBtCustomPropModel = new CmsBtCustomPropModel();
            cmsBtCustomPropModel.setCat(cat);
            cmsBtCustomPropModel.setChannelId(channelId);
            cmsBtCustomPropModel.setOrgChannelId(orgChannelId);
        }
        CmsBtCustomPropModel.Entity item = cmsBtCustomPropModel.getEntitys().stream()
                .filter(entity1 -> entity1.getNameEn().equalsIgnoreCase(entity.getNameEn()) && entity.getType() == entity1.getType())
                .findFirst().orElse(null);
        if (item == null) {
            item = new CmsBtCustomPropModel.Entity();
            item.setNameCn(entity.getNameCn());
            item.setNameEn(entity.getNameEn());
            item.setType(entity.getType());
            item.setValue(entity.getValue());
            cmsBtCustomPropModel.getEntitys().add(item);
        } else {
            item.setNameCn(entity.getNameCn());
            item.setType(entity.getType());
            item.setValue(entity.getValue());
        }
        update(cmsBtCustomPropModel);
        return getCustomPropByCatChannelExtend(channelId, orgChannelId, cat);
    }

    public List<String> getFeedAttributeName(String orgChannelId){

    }
    private CmsBtCustomPropModel merge(CmsBtCustomPropModel prop1, CmsBtCustomPropModel prop2) {
        for (CmsBtCustomPropModel.Entity entity : prop2.getEntitys()) {
            CmsBtCustomPropModel.Entity temp = prop1.getEntitys().stream()
                    .filter(entity1 -> entity.getNameEn().equalsIgnoreCase(entity1.getNameEn()))
                    .findFirst().orElse(null);
            if (temp == null) {
                prop1.getEntitys().add(entity);
            } else {
                entity.forEach(temp::putIfAbsent);
            }
        }
        if (ListUtils.isNull(prop1.getSort()) && !ListUtils.isNull(prop2.getSort())) {
            prop1.setSort(prop2.getSort());
        }
        return prop1;
    }

    public void rearrange(CmsBtCustomPropModel cmsBtCustomPropModel) {
        cmsBtCustomPropModel.getEntitys().sort((o1, o2) -> {
            Boolean b1 = o1.getChecked() == null?false:o1.getChecked();
            Boolean b2 = o2.getChecked() == null?false:o2.getChecked();
            return b1.compareTo(b2)*-1;
        });

        if (!ListUtils.isNull(cmsBtCustomPropModel.getSort())) {
            List<CmsBtCustomPropModel.Entity> newEntity = new ArrayList<>();
            for(Integer i=0;i<cmsBtCustomPropModel.getSort().size();i++){
                String key = cmsBtCustomPropModel.getSort().get(i);
                CmsBtCustomPropModel.Entity item = cmsBtCustomPropModel.getEntitys().stream().filter(entity -> entity.getNameEn().equalsIgnoreCase(key)).findFirst().orElse(null);
                if(item != null && true == item.getChecked()){
                    item.put("dispOrder",i);
                }
            }
            cmsBtCustomPropModel.getEntitys().sort((o1, o2) -> {
                Integer b1 = o1.getAttribute("dispOrder") == null?9999:o1.getAttribute("dispOrder");
                Integer b2 = o2.getAttribute("dispOrder") == null?9999:o2.getAttribute("dispOrder");
                return b1.compareTo(b2);
            });
        }
    }
}
