package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtShelvesModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by rex.wu on 2016/11/16.
 */
@Repository
public interface CmsBtShelvesDaoExt {

    List<CmsBtShelvesModel> selectByTemplateId(Integer templateId);
}
