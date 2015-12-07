package com.voyageone.web2.base.message;

import com.voyageone.web2.base.dao.WebBaseDao;
import com.voyageone.web2.base.dao.WebDaoNs;
import org.springframework.stereotype.Repository;

/**
 * 对 ct_message_info 访问
 * Created by Jonas on 11/25/15.
 * @author Jonas
 * @version 2.0
 */
@Repository
class MessageDao extends WebBaseDao {
    @Override
    protected WebDaoNs webNs() {
        return WebDaoNs.CORE;
    }

    public MessageModel selectMessage(String lang, String code) {
        return selectOne("ct_message_info_selectMessage", parameters("lang", lang, "code", code));
    }
}
