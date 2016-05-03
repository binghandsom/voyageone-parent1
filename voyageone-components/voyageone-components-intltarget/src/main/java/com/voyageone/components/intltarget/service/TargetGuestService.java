package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.guest.TargetGuestV3AuthResponse;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetGuestService extends TargetBase{

    private static final String Url="/guests";

    /**
     * 执行V3 Auth，获取验证响应
     * @return 验证信息
     * @throws Exception
     */
    public TargetGuestV3AuthResponse v3Auth() throws Exception {
        return postApiResponseWithKey(Url+"/v3/auth",null,TargetGuestV3AuthResponse.class,false);
    }

}
