package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.TargetGuestContact;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetGuestService extends TargetBase{

    private static final String Url="/guests/v3/addresses";

    /**
     * 获取guest address
     * @return 客户联系地址
     * @throws Exception
     */
    public TargetGuestContact getGuestContactAddress() throws Exception {
        String result=reqGiltApi(Url,new HashMap<>());
        return StringUtils.isEmpty(result)?null: JacksonUtil.json2Bean(result,TargetGuestContact.class);
    }


}
