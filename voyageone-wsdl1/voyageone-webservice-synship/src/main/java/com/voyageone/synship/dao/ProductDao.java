package com.voyageone.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.synship.modelbean.ProductBean;
import org.springframework.stereotype.Repository;

/**
 * Created by dell on 2015/7/23.
 */
@Repository
public class ProductDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.synship.sql";
    }

    /**
     * 插入Product（没有时插入；有时更新）
     *
     * @param productBean
     */
    public int insertProduct(ProductBean productBean) {

        return updateTemplate.insert("wms_insertProduct", productBean);
    }

}
