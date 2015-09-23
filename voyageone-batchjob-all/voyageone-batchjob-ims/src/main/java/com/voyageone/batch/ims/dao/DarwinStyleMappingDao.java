package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Leo on 15-9-15.
 */
@Repository
public class DarwinStyleMappingDao extends BaseDao{
    public String selectStyleCode(int cartId, String code) {
        List<String> styleCodeList = selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectDarwinStyleCode", parameters("cart_id", cartId, "code", code));
        if (styleCodeList == null || styleCodeList.isEmpty()) {
            return null;
        }
        else
            return styleCodeList.get(0);
    }
}
