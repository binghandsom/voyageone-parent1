package com.voyageone.batch.synship.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.synship.modelbean.ShortUrlBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.stereotype.Repository;

/**
 * Created by Jonas on 9/22/15.
 */
@Repository
public class ShortUrlDao extends BaseDao {
    /**
     * 获取 mapper 的 namespace，只在初始化时调用
     */
    @Override
    protected String namespace() {
        return Constants.getDaoNameSpace(SubSystem.SYNSHIP);
    }

    public ShortUrlBean getInfosBySourceOrderId(String source_order_id) {
        return selectOne("tt_short_url_selectBySourceOrderIdFromTtShorturl", parameters("source_order_id", source_order_id));
    }
}
