package com.voyageone.web2.cms.views.pop.prop_change;

import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel_Field;
import com.voyageone.cms.service.model.CmsMtCommonPropDefModel;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsMtCommonPropDefDao;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
@Service
public class CmsPropChangeService extends BaseAppService {

    @Autowired
    private CmsMtCommonPropDefDao cmsMtCommonPropDefDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * 获取pop画面options.
     */
    public List<CmsMtCommonPropDefModel> getPopOptions(String channel_id) {

        List<CmsMtCommonPropDefModel> modelList = cmsMtCommonPropDefDao.selectAll();
        List<CmsMtCommonPropDefModel> resultList = new ArrayList<>();

        for (CmsMtCommonPropDefModel model : modelList) {
            CmsMtCommonPropDefModel resModel = new CmsMtCommonPropDefModel();
            Field field = model.getField();
            if ("optConfig".equals(field.getDataSource())) {
                OptionsField optionsField = (OptionsField) field;
                List<Option> options = TypeChannel.getOptions(optionsField.getId(), channel_id);
                optionsField.setOptions(options);
                resModel.setField(optionsField);
            } else {
                resModel.setField(field);
            }
            resultList.add(resModel);
        }
        return resultList;
    }

    /**
     * 批量修改属性.
     */
    public void setProductFields(Map<String, Object> params, UserSessionBean userInfo) {

        List<Long> productIds = (ArrayList<Long>)params.get("productIds");
        String prop_id = (String) ((Map<String, Object>) params.get("property")).get("id");
        String prop_value = (String) params.get("value");

        CmsBtProductModel_Field field = new CmsBtProductModel_Field();
        field.put(prop_id, prop_value);
        // TODO 等待liangxiong
//        cmsBtProductDao.bathUpdateWithField(userInfo.getSelChannelId(), productIds, field, userInfo.getUserName());
    }
}
