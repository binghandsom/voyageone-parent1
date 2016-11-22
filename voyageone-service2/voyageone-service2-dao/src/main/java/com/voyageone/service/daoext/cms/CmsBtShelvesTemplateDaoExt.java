package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.shelves.CmsBtShelvesTemplateBean;
import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by rex.wu on 2016/11/11.
 */
@Repository
public interface CmsBtShelvesTemplateDaoExt{

    List<CmsBtShelvesTemplateModel> search(CmsBtShelvesTemplateBean searchBean);

    CmsBtShelvesTemplateModel selectByChannelIdAndName(Map<String, Object> params);
}
