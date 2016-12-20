package com.voyageone.service.impl.cms;

import com.voyageone.service.bean.cms.mt.channel.config.CmsMtChannelConfigInfo;
import com.voyageone.service.bean.cms.mt.channel.config.SaveListInfo;
import com.voyageone.service.impl.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/12/20.
 */
@Service
public class CmsFeedConfigService  extends BaseService {
    /**
     * 数据初始化
     * @param map
     * @param channelId
     * @param lang
     * @return
     */
    public List<CmsMtChannelConfigInfo> search(Map<String, Object> map, String channelId, String lang){
        return null;
    }

    /**
     * 数据保存
     * @param info
     * @param channelId
     * @param userName
     */
    public void saveList(SaveListInfo info, String channelId, String userName) {

    }
}
