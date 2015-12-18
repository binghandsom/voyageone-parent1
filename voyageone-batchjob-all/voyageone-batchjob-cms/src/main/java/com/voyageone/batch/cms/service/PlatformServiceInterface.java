package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.bean.CustomMappingType;
import com.voyageone.batch.cms.bean.PlatformUploadRunState;
import com.voyageone.batch.cms.bean.tcb.TaskSignal;
import com.voyageone.batch.cms.bean.tcb.UploadProductTcb;
import com.voyageone.batch.cms.bean.PlatformPropBean;
import com.voyageone.batch.cms.service.rule_parser.ExpressionParser;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Leo on 15-6-30.
 */
public interface PlatformServiceInterface {
    //List<Object> resolveCustomMappingProps(PlatformUploadRunState platformUploadRunState, Map<String, String> urlMap);
    //List<Object> resolveMappingProps(PlatformUploadRunState platformUploadRunState, Map<String, String> urlMap);

    //void constructCustomPlatformPropsBeforeUploadImage(UploadProductTcb tcb, Map<CustomMappingType, List<PlatformPropBean>> mappingTypePropsMap, ExpressionParser expressionParser, Set<String> imageSet, List<PlatformPropBean> platformPropsWillBeFilted) throws TaskSignal;
    //void constructDarwinPlatformProps(UploadProductTcb tcb, PlatformPropBean platformPropBean, String propValue) throws TaskSignal;
    //void constructMappingPlatformProps(UploadProductTcb tcb, Object platformProps, CmsMtPlatformMappingModel cmsMtPlatformMappingModel, ExpressionParser expressionParser, Set<String> imageSet) throws TaskSignal;
    //void constructMappingByTypePlatformPropsBeforeUploadImage(UploadProductTcb tcb, PlatformPropBean platformProp);
}
