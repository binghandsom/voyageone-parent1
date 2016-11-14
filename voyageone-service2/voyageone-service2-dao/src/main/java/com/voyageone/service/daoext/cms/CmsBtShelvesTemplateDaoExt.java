package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/11.
 */
@Repository
public class CmsBtShelvesTemplateDaoExt extends ServiceBaseDao {

    public List<CmsBtShelvesTemplateModel> search(CmsBtShelvesTemplateBean searchBean) {
        return  selectList("select_cms_bt_shelves_template", searchBean);
    }
}
