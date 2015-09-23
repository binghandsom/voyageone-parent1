package com.voyageone.common.components.ems;

import com.voyageone.common.configs.Codes;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.commons.lang3.StringUtils;

import static com.voyageone.common.components.ems.EmsApiKeys.*;

/**
 * Ems 服务调用接口
 *
 * Created by Jonas on 9/23/15.
 */
public class EmsService {
    // EMS 请求用信息
    public static String USER_NAME;
    public static String PASSWORD;
    public static String KEY;

    // EMS 用Api信息初始化
    private static void initEMSKey() {
        USER_NAME = Codes.getCodeName(EMS_API_KEY, EMS_API_USER);
        PASSWORD = Codes.getCodeName(EMS_API_KEY, EMS_API_PASSWORD);
        KEY = Codes.getCodeName(EMS_API_KEY, EMS_API_VALID_KEY);
    }

    private static B2COrderServiceStub clientInit() throws AxisFault {
        // EMS 用Api信息初始化
        if (StringUtils.isEmpty(USER_NAME)) {
            initEMSKey();
        }

        B2COrderServiceStub stub = new B2COrderServiceStub();
        Options option = stub._getServiceClient().getOptions();
        option.setTimeOutInMilliSeconds(EMS_TIMEOUT);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);

        return stub;
    }

    public static B2COrderServiceStub.ServiceResult validCardNo(String name, String code) throws Exception {
        // 请求信息设定
        B2COrderServiceStub.B2CVaildCarNo bean = new B2COrderServiceStub.B2CVaildCarNo();

        bean.setReceiverName(name);
        bean.setReceiverCarNo(code);
        bean.setUserName(USER_NAME);
        bean.setPassword(PASSWORD);
        bean.setKey(KEY);

        B2COrderServiceStub.ValidCarNo validCarNo14 = new B2COrderServiceStub.ValidCarNo();

        validCarNo14.setCarno(bean);

        B2COrderServiceStub stub = clientInit();
        B2COrderServiceStub.ValidCarNoResponse response = stub.ValidCarNo(validCarNo14);
        return response.getValidCarNoResult();
    }

    public static B2COrderServiceStub.ServiceResult validCardNoFormat(String code) throws Exception {
        // 请求信息设定
        B2COrderServiceStub.ValidCardNoFormat validCardNoFormat2 = new B2COrderServiceStub.ValidCardNoFormat();

        validCardNoFormat2.setCardno(code);

        B2COrderServiceStub stub = clientInit();
        B2COrderServiceStub.ValidCardNoFormatResponse response = stub.ValidCardNoFormat(validCardNoFormat2);
        return response.getValidCardNoFormatResult();
    }

    public static B2COrderServiceStub.ServiceResult isCardExist(String name, String card) throws Exception {

        B2COrderServiceStub.IsCardExist isCardExist = new B2COrderServiceStub.IsCardExist();

        isCardExist.setCardNo(card);
        isCardExist.setUserName(USER_NAME);
        isCardExist.setPassword(PASSWORD);
        isCardExist.setName(name);

        B2COrderServiceStub stub = clientInit();
        B2COrderServiceStub.IsCardExistResponse response = stub.IsCardExist(isCardExist);
        return response.getIsCardExistResult();
    }
}
