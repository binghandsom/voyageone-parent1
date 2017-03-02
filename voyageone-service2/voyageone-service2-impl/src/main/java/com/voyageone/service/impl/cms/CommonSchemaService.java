package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.service.dao.cms.mongo.CmsMtCommonSchemaDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CommonSchemaService extends BaseService {

    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;

    public List getAll() {
        return cmsMtCommonSchemaDao.selectAllProps();
    }

    /**
     * 获取common schema.
     */
    public CmsMtCommonSchemaModel getComSchemaModel() {

        CmsMtCommonSchemaModel comSchemaModel = cmsMtCommonSchemaDao.selectComSchema();

        if (comSchemaModel == null) {

            //common schema 不存在时异常处理.
            String errMsg = "共通schema（cms_mt_common_schema）的信息不存在！";

            $error(errMsg);

            throw new BusinessException(errMsg);
        }

        return comSchemaModel;
    }

    public List<Map<String,Object>> getCommonFields(){
        List<Field> cmsMtCommonFields = getComSchemaModel().getFields();
        List<Map<String,Object>> commonFieldList = new ArrayList<>();
        cmsMtCommonFields.forEach(field -> {
            Map<String,Object> item = new HashMap();
            item.put("id",field.getId());
            item.put("name",field.getName());
            commonFieldList.add(item);
        });
        return commonFieldList;
    };
}
