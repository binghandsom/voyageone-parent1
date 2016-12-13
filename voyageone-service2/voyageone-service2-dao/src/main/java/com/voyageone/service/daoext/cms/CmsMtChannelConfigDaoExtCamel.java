package com.voyageone.service.daoext.cms;
import com.voyageone.service.bean.cms.mt.channel.config.CmsMtChannelConfigInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CmsMtChannelConfigDaoExtCamel {

    List<CmsMtChannelConfigInfo> selectConfigInfoList(Map<String,Object> map);

}
