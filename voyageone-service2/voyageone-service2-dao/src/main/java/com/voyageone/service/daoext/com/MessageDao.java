package com.voyageone.service.daoext.com;

import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.service.dao.ServiceBaseDao;
import org.springframework.stereotype.Repository;

/**
 * 对 ct_message_info 访问
 * Created by Jonas on 11/25/15.
 * @author Jonas
 * @version 2.0
 */
@Repository
public class MessageDao extends ServiceBaseDao {

    public MessageBean selectMessage(String lang, String code) {
        return selectOne("ct_message_info_selectMessage", parameters("lang", lang, "code", code));
    }
}
