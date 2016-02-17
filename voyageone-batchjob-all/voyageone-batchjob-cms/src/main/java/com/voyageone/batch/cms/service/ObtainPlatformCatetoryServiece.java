package com.voyageone.batch.cms.service;

import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;

import java.util.List;

/**
 * Created by lewis on 15-11-30.
 */
public interface ObtainPlatformCatetoryServiece {

     List<CmsMtPlatformCategoryTreeModel> getPlatformCategories();

     CmsMtPlatformCategorySchemaModel getPlatformCategorySchemas();

}
