package com.voyageone.batch.ims.service;

import com.voyageone.batch.ims.bean.CustomMappingType;
import com.voyageone.batch.ims.bean.PlatformUploadRunState;
import com.voyageone.batch.ims.bean.tcb.TaskSignal;
import com.voyageone.batch.ims.bean.tcb.UploadProductTcb;
import com.voyageone.batch.ims.modelbean.PlatformPropBean;
import com.voyageone.batch.ims.modelbean.PropMappingBean;
import com.voyageone.batch.ims.service.rule_parser.ExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Leo on 15-6-30.
 */
public interface PlatformServiceInterface {
    List<Object> resolveCustomMappingProps(PlatformUploadRunState platformUploadRunState, Map<String, String> urlMap);
    List<Object> resolveMasterMappingProps(PlatformUploadRunState platformUploadRunState, Map<String, String> urlMap);

    void constructCustomPlatformPropsBeforeUploadImage(UploadProductTcb tcb,Map<CustomMappingType, List<PlatformPropBean>> mappingTypePropsMap, ExpressionParser expressionParser, Set<String> imageSet, List<PlatformPropBean> platformPropsWillBeFilted) throws TaskSignal;
    void constructDarwinPlatformProps(UploadProductTcb tcb, PlatformPropBean platformPropBean, String propValue) throws TaskSignal;
    void constructFieldMappingPlatformPropsBeforeUploadImage(UploadProductTcb tcb, PlatformPropBean platformProp, List<PlatformPropBean> platformProps, PropMappingBean masterPropMapping,
                                                             ExpressionParser expressionParser, Set<String> imageSet) throws TaskSignal;
    void constructMappingByTypePlatformPropsBeforeUploadImage(UploadProductTcb tcb, PlatformPropBean platformProp);
}
