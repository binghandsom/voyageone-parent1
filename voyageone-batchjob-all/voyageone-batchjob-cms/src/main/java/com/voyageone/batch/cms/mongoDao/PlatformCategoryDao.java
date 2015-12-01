package com.voyageone.batch.cms.mongoDao;

import com.voyageone.base.dao.mongodb.BaseJomgoTemplate;
import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.batch.cms.mongoModel.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlatformCategoryDao extends BaseMongoDao {

    @Autowired
    BaseJomgoTemplate mongoTemplate;

    public PlatformCategoryDao() {
        super.entityClass = CmsMtPlatformCategoryTreeModel.class;
    }

    public CmsMtPlatformCategoryTreeModel selectByCartIdCatId(int cartId, String categoryId) {
        String queryStr = "{" +
                "cartId: '" + cartId + "'" +
                ", categoryId: '" + categoryId + "'" +
                "}";
        return selectOneWithQuery(queryStr);
    }

    public void deletePlatformCategories(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        deleteWithQuery(queryStr);
    }

    public List<CmsMtPlatformCategoryTreeModel> selectPlatformCategoriesByCartId(Integer cartId){
        String queryStr = "{cartId:"+cartId+"}";
        List<CmsMtPlatformCategoryTreeModel> categoryModels = select(queryStr);
        return categoryModels;
    }

}
