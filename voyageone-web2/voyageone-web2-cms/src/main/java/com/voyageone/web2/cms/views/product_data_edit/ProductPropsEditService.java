package com.voyageone.web2.cms.views.product_data_edit;

import com.voyageone.cms.service.CmsMasterBeanConvertService;
import com.voyageone.cms.service.model.CmsMtCategorySchemaWithValueModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lewis on 15-12-16.
 */
@Service
public class ProductPropsEditService {

    @Autowired
    CmsMasterBeanConvertService cmsMasterBeanConvertService;

    public CmsMtCategorySchemaWithValueModel handleInit(String channelId, int productId){

        return cmsMasterBeanConvertService.getViewModels(channelId,productId);

    }


}
