package com.voyageone.service.impl.cms;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CmsBtTasksBean;
import com.voyageone.service.bean.cms.task.stock.StockExcelBean;
import com.voyageone.service.bean.cms.task.stock.StockIncrementExcelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImageGroup Service
 *
 * @author jeff.duan 16/6/6
 * @version 2.0.0
 */
@Service
public class ImageGroupService extends BaseService {
    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;

    public void save(CmsBtImageGroupModel model) {
        cmsBtImageGroupDao.insert(model);
    }

    public void update(CmsBtImageGroupModel model) {
        cmsBtImageGroupDao.update(model);
    }

    public List<CmsBtImageGroupModel> getList(JomgoQuery queryObject) {
        return cmsBtImageGroupDao.select(queryObject);
    }

    public CmsBtImageGroupModel getOne(JomgoQuery queryObject) {
        return cmsBtImageGroupDao.selectOneWithQuery(queryObject);
    }
}
