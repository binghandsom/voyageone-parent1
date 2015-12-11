package com.voyageone.web2.cms.views;

import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel_Field;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsMtCommonPropDefDao;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
    public List<JSONObject> getPropOptions(String channel_id) {
        List<JSONObject> jsonList = cmsMtCommonPropDefDao.selectPropAllWithJson();
        List<JSONObject> resultList = new ArrayList<>();
        for (JSONObject obj : jsonList) {
            if ("optConfig".equals(obj.get("dataSource"))) {
                List<TypeChannelBean> channelList = TypeChannel.getTypeList(obj.get("id").toString(), channel_id);
                List<Map<String, Object>> optionList = new ArrayList<>();
                for (int i = 0; i < channelList.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("displayName", channelList.get(i).getName());
                    map.put("value", i);
                    optionList.add(map);
                }
                JSONObject optionObj = new JSONObject();
                optionObj.put("option", optionList);
                obj.put("options", optionObj);
            }
            resultList.add(obj);
        }

        return resultList;
    }

    /**
     * 批量修改属性.
     */
    public void savePropOptions(JSONObject params) {

        Object[] codes = ((JSONArray)params.get("codes")).toArray();
        String channel_id = (String)params.get("channelId");
        String prop_id = (String)params.get("propId");
        String prop_value = (String)params.get("propValue");

        List<String> codeList = new ArrayList<>();
        for (Object code : codes) {
            codeList.add(code.toString());
        }
        CmsBtProductModel_Field field = new CmsBtProductModel_Field();
        field.put(prop_id, prop_value);
        cmsBtProductDao.bathUpdateWithField(channel_id, codeList, field, null);

    }
}
