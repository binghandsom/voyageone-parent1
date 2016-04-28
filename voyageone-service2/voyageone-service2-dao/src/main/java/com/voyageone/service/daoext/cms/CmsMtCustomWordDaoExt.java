package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsMtCustomWordBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
