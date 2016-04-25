package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsMtCustomWordBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * for voyageone_ims.ims_mt_custom_word and voyageone_ims.ims_mt_custom_word_param
 * <p>
 * Created by Jonas on 9/11/15.
 */
@Repository
public class CmsMtCustomWordDaoExt extends ServiceBaseDao {

    public List<CmsMtCustomWordBean> selectWithParam() {
        return selectList("cms_mt_custom_word_selectWithParam");
    }

    // 获取翻译时标题和描述的长度设置
    public List<Map<String, Object>> selectTransLenSet(String chnId) {
        return selectList("cms2_mt_channel_config_getTransLenSet", parameters("channelId", chnId));
    }
}
