package com.voyageone.service.daoext.cms;

import com.voyageone.service.model.cms.CmsBtFeedImportSizeModel;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/12/15.
 */
@Repository
public interface CmsBtFeedImportSizeDaoExt {
    List<CmsBtFeedImportSizeModel> selectList(Map<String, Object> map);
}
