package com.voyageone.web2.cms.views.setting.mapping.feed;

import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.web2.base.BaseAppService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 为属性匹配画面提供功能
 * @author Jonas, 12/24/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsFeedPropMappingService extends BaseAppService {

    @Autowired
    private CmsMtCategorySchemaDao categorySchemaDao;

    public CmsMtCategorySchemaModel getCategoryProps(String categoryPath) {

        String categoryId = getCategoryIdByPath(categoryPath);

        return categorySchemaDao.getMasterSchemaModelByCatId(categoryId);
    }

    /**
     * 通过 categoryPath 转换获取 categoryId
     * @param categoryPath 类目路径
     * @return String
     */
    public String getCategoryIdByPath(String categoryPath) {

        // 当前为 Path 的 Base64 码
        // 有可能未来更改为 MD5
        return new String(Base64.decodeBase64(categoryPath));
    }
}
