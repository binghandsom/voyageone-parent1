package com.voyageone.service.daoext.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsMtPlatformSpecialFieldDaoExt extends ServiceBaseDao {

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

    public String selectSpecialMappingType(Integer cartId, String platformCategoryId, String propertyId) {
        CmsMtPlatformSpecialFieldModel model = new CmsMtPlatformSpecialFieldModel();
        model.setCartId(cartId);
        model.setFieldId(propertyId);
        model.setCatId(platformCategoryId);
        return selectOne("select_cms_mt_platform_special_field_type", model);
    }
}
