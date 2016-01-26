package com.voyageone.cms.service.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.service.model.CmsMtPlatformSpecialFieldModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsMtPlatformSpecialFieldDao extends BaseDao {

    public List<CmsMtPlatformSpecialFieldModel> select(int cartId, String catId, String fieldId, String type) {
        CmsMtPlatformSpecialFieldModel model = new CmsMtPlatformSpecialFieldModel();
        model.setCartId(cartId);
        model.setFieldId(fieldId);
        model.setCatId(catId);
        model.setType(type);
        return selectList("select_cms_mt_platform_special_field", model);
    }

    public void insert(CmsMtPlatformSpecialFieldModel model) {
        insert("insert_cms_mt_platform_special_field", model);
    }

    public void insertWithList(List<CmsMtPlatformSpecialFieldModel> models) {
        models.forEach(this::insert);
    }

    public void delete(CmsMtPlatformSpecialFieldModel model) {
        delete("delete_cms_mt_platform_special_field", model);
    }
}
