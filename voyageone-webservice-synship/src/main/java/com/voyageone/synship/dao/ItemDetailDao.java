package com.voyageone.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.synship.modelbean.ItemDetailBean;
import org.springframework.stereotype.Repository;

/**
 * Created by dell on 2015/7/23.
 */
@Repository
public class ItemDetailDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.synship.sql";
    }

    /**
     * 插入ItemDetail（没有时插入；有时更新）
     *
     * @param itemDetailBean
     */
    public int insertItemDetail(ItemDetailBean itemDetailBean) {

        return updateTemplate.insert("wms_insertItemDetail", itemDetailBean);
    }

}
