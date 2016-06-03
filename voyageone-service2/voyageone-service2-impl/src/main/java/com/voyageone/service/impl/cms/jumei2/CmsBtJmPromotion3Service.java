package com.voyageone.service.impl.cms.jumei2;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmImportProduct;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmImportSku;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmImportSpecialImage;
import com.voyageone.service.bean.cms.businessmodel.JmProductImportAllInfo;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.CmsBtJmMasterBrandDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtPromotionDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionDaoExt;
import com.voyageone.service.daoext.synship.SynshipComMtValueChannelDao;
import com.voyageone.service.impl.cms.CmsMtChannelValuesService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionImportTaskService;
import com.voyageone.service.model.cms.CmsBtJmMasterBrandModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotion3Service {
    private static final Logger log = LoggerFactory.getLogger(CmsBtJmPromotion3Service.class);
    @Autowired
    CmsBtJmPromotionDao dao;
    @Autowired
    CmsBtJmMasterBrandDao daoCmsBtJmMasterBrand;
    @Autowired
    CmsBtJmPromotionDaoExt daoExt;
    @Autowired
    CmsBtTagDao daoCmsBtTag;
    @Autowired
    CmsBtPromotionDao daoCmsBtPromotion;
    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>();
        List<CmsBtJmMasterBrandModel> jmMasterBrandList = daoCmsBtJmMasterBrand.selectList(new HashMap<String, Object>());
        map.put("jmMasterBrandList", jmMasterBrandList);
        return map;
    }

    public List<CmsBtTagModel> getTagListByPromotionId(int promotionId) {
//        CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(promotionId);
//        if (model.getRefTagId() != 0) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("parentTagId", model.getRefTagId());
//            map.put("active", 1);
//            List<CmsBtTagModel> tagList = daoCmsBtTag.selectList(map);
//            return tagList;
//        }
        return new ArrayList<>();
    }
}

