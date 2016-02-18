package com.voyageone.batch.synship.service.ems;

import com.voyageone.common.configs.Codes;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.voyageone.batch.synship.service.ems.EmsApiKeys.*;

/**
 * Ems 服务调用接口
 *
 * Created by Jonas on 9/23/15.
 */
@Component
public class EmsService {
    // EMS 请求用信息
    private static String userName;

    private static String password;

    private static String key;

    public static String getUserName() {
        if (StringUtils.isEmpty(userName))
            userName = Codes.getCodeName(EMS_API_KEY, EMS_API_USER);
        return userName;
    }

    public static String getPassword() {
        if (StringUtils.isEmpty(password))
            password = Codes.getCodeName(EMS_API_KEY, EMS_API_PASSWORD);
        return password;
    }

    public static String getKey() {
        if (StringUtils.isEmpty(key))
            key = Codes.getCodeName(EMS_API_KEY, EMS_API_VALID_KEY);
        return key;
    }

    private B2COrderServiceStub getClient() throws AxisFault {
        B2COrderServiceStub stub = new B2COrderServiceStub();
        Options option = stub._getServiceClient().getOptions();
        option.setTimeOutInMilliSeconds(EMS_TIMEOUT);
        option.setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
        return stub;
    }

    public B2COrderServiceStub.ServiceResult validCardNo(String name, String code) throws Exception {
        // 请求信息设定
        B2COrderServiceStub.B2CVaildCarNo bean = new B2COrderServiceStub.B2CVaildCarNo();
        bean.setReceiverName(name);
        bean.setReceiverCarNo(code);
        bean.setUserName(getUserName());
        bean.setPassword(getPassword());
        bean.setKey(getKey());

        B2COrderServiceStub.ValidCarNo validCarNo14 = new B2COrderServiceStub.ValidCarNo();
        validCarNo14.setCarno(bean);

        B2COrderServiceStub stub = getClient();
        B2COrderServiceStub.ValidCarNoResponse response = stub.validCarNo(validCarNo14);

        return response.getValidCarNoResult();
    }

    public B2COrderServiceStub.ServiceResult validCardNoFormat(String code) throws Exception {
        // 请求信息设定
        B2COrderServiceStub.ValidCardNoFormat validCardNoFormat2 = new B2COrderServiceStub.ValidCardNoFormat();

        validCardNoFormat2.setCardno(code);

        B2COrderServiceStub stub = getClient();
        B2COrderServiceStub.ValidCardNoFormatResponse response = stub.validCardNoFormat(validCardNoFormat2);
        return response.getValidCardNoFormatResult();
    }

    public B2COrderServiceStub.ServiceResult isCardExist(String name, String card) throws Exception {

        B2COrderServiceStub.IsCardExist isCardExist = new B2COrderServiceStub.IsCardExist();

        isCardExist.setCardNo(card);
        isCardExist.setUserName(getUserName());
        isCardExist.setPassword(getPassword());
        isCardExist.setName(name);

        B2COrderServiceStub stub = getClient();
        B2COrderServiceStub.IsCardExistResponse response = stub.isCardExist(isCardExist);
        return response.getIsCardExistResult();
    }
}
