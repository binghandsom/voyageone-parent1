package com.voyageone.web2.cms.views.pop.prop_change;

import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel_Field;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsMtCommonPropDefDao;
import com.voyageone.web2.cms.model.CmsMtCommonPropDefModel;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void setProductFields(Map<String, Object> params, String user_name) {

        Object[] codes = ((JSONArray) params.get("codes")).toArray();
        String channel_id = (String) params.get("channelId");
        String prop_id = (String) params.get("propId");
        String prop_value = (String) params.get("propValue");

        List<String> codeList = new ArrayList<>();

        for (Object code : codes) {
            codeList.add(code.toString());
        }
        CmsBtProductModel_Field field = new CmsBtProductModel_Field();
        field.put(prop_id, prop_value);
        cmsBtProductDao.bathUpdateWithField(channel_id, codeList, field, user_name);

    }
}
