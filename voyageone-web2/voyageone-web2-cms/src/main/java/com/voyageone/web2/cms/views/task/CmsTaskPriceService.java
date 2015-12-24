package com.voyageone.web2.cms.views.task;

import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.Enums.PromotionTypeEnums;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsPromotionProductPriceBean;
import com.voyageone.web2.cms.dao.*;
import com.voyageone.web2.cms.model.*;
import com.voyageone.web2.cms.views.pop.tag.promotion.CmsPromotionSelectService;
import com.voyageone.web2.sdk.api.service.ProductGetClient;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsTaskPriceService extends BaseAppService {

    @Autowired
    protected CmsPromotionTaskDao cmsPromotionTaskDao;

   public List<Map<String,Object>> getPriceList(Map<String,Object> param){

       return cmsPromotionTaskDao.getPromotionTaskPriceList(param);
   }

    public int getPriceListCnt(Map<String,Object> param){

        return cmsPromotionTaskDao.getPromotionTaskPriceListCnt(param);
    }

    public int updateTaskStatus(CmsBtPromotionTaskModel param){
        return cmsPromotionTaskDao.updatePromotionTask(param);
    }
}
