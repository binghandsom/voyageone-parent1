package com.voyageone.web2.cms.views.system.setting;

import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.com.ComMtValueChannelDao;
import com.voyageone.service.model.com.ComMtValueChannelModel;
import com.voyageone.web2.base.BaseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/8/15.
 * @version 2.0.0
 */
@Service
public class ValueChannelService extends BaseViewService {

    private final ComMtValueChannelDao comMtValueChannelDao;

    @Autowired
    public ValueChannelService(ComMtValueChannelDao comMtValueChannelDao) {
        this.comMtValueChannelDao = comMtValueChannelDao;
    }

    boolean addHsCodes(String channelId, List<String> hsCodeList, Integer typeId, String modifier) {

        hsCodeList.forEach(s -> {
            if (!StringUtil.isEmpty(s.trim())) {
                Map<String, Object> map = new HashMap<>();
                map.put("channelId", channelId);
                map.put("typeId", typeId);
                map.put("name", s);
                map.put("value", s);
                int cnt = comMtValueChannelDao.selectCount(map);
                if (cnt == 0) {
                    ComMtValueChannelModel comMtValueChannelModel = new ComMtValueChannelModel();
                    comMtValueChannelModel.setChannelId(channelId);
                    comMtValueChannelModel.setLangId("cn");
                    comMtValueChannelModel.setTypeId(typeId);
                    comMtValueChannelModel.setValue(s);
                    comMtValueChannelModel.setName(s);
                    comMtValueChannelModel.setAddName1(s);
                    comMtValueChannelModel.setDisplayOrder(0);
                    comMtValueChannelModel.setCreated(DateTimeUtil.getDate());
                    comMtValueChannelModel.setModified(DateTimeUtil.getDate());
                    comMtValueChannelModel.setCreater(modifier);
                    comMtValueChannelModel.setModifier(modifier);
                    comMtValueChannelDao.insert(comMtValueChannelModel);
                    comMtValueChannelModel.setLangId("en");
                    comMtValueChannelModel.setId(null);
                    comMtValueChannelDao.insert(comMtValueChannelModel);
                }
            }
        });

        TypeChannels.reload();
        return true;
    }
}
