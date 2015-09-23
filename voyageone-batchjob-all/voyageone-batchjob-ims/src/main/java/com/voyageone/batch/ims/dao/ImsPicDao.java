package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.ImsPic;
import org.springframework.stereotype.Repository;

/**
 * 对表 ims_bt_pic 操作
 *
 * Created by Jonas on 8/10/15.
 */
@Repository
public class ImsPicDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.ims.sql";
    }

    public ImsPic selectByTitle(String title, String category_tid) {
        return selectOne("ims_bt_pic_selectByTitle", parameters("title", title, "category_tid", category_tid));
    }


    public int insert(ImsPic pic) {
        return insert("ims_bt_pic_insert", pic);
    }
}
