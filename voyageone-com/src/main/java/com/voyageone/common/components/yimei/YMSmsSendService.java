package com.voyageone.common.components.yimei;

import com.voyageone.common.Constants;
import com.voyageone.common.components.yimei.bean.YMSMSSendBean;
import com.voyageone.common.components.yimei.client.Client;
import com.voyageone.common.configs.ChannelConfigs;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.CommonUtil;
import org.springframework.stereotype.Component;

/**
 * 亿美短信调用
 * Created by Fred on 2015/8/28.
 */
@Component
public class YMSmsSendService {


    /**
     * 调用亿美WSDL，发送短信
     *
     * @param ymsmsSendBean 发送短信BEAN
     * @throws Exception
     */
    public Integer sendSMS(YMSMSSendBean ymsmsSendBean) throws Exception {

        // 发送者扩展号码
        //String SMS_ADD_SERIAL = "";
        //Map<String, String> mapTemp = null;

        // 手机号码有效性检查
        if (CommonUtil.isPhoneNum(ymsmsSendBean.getPhone()) == false) {
            return Constants.PHONE_NUM_ERR;
        }

        // 短信配置
        //SMS_ADD_SERIAL = Codes.getCodeName(Constants.smsInfo.SMS_INFO, Constants.smsInfo.ADD_SERIAL);
        //SMS_ADD_SERIAL = mapTemp.get(Constants.smsInfo.ADD_SERIAL);
        //addSerial
        // 短信发送客户端
        Client sendIdCardSMSclient = null;
        //sendIdCardSMSclient = new Client(SMS_USER, SMS_PASSWORD);
        sendIdCardSMSclient = createClient(ymsmsSendBean.getOrder_channel_id(),ymsmsSendBean.getSms_type());
        // 发送短消息
        String sms_add_serial = ChannelConfigs.getVal1(ymsmsSendBean.getOrder_channel_id(), ChannelConfigEnums.Name.sms_add_serial);
        //需要特殊码
        String addSerial = "";
        //物流短信
        if (Constants.smsInfo.SMS_TYPE_LOGISTICS.equals(ymsmsSendBean.getSms_type())) {
            if (ChannelConfigs.getVal2(ymsmsSendBean.getOrder_channel_id(), ChannelConfigEnums.Name.sms_add_serial, sms_add_serial).equals(Constants.smsInfo.NEED_ADD_SERIAL)) {
                addSerial = ymsmsSendBean.getOrder_channel_id();
            }
        }

        //int ret=sendIdCardSMSclient.sendSMS(new String[] {shortUrlBean.getShip_phone()}, smsContent,"",5);
        int ret = sendIdCardSMSclient.sendSMS(new String[]{ymsmsSendBean.getPhone()}, ymsmsSendBean.getContent(), addSerial, 5);

        return ret;

    }

    /**
     * 调用亿美WSDL，发送短信
     * @param orderChannelId
     * @param sms_type
     * @throws Exception
     */
    public double getBalance(String orderChannelId,String sms_type) throws Exception {
        // 短信发送客户端
        Client sendIdCardSMSclient = null;
        //sendIdCardSMSclient = new Client(SMS_USER, SMS_PASSWORD);
        sendIdCardSMSclient = createClient(orderChannelId,sms_type);
        return sendIdCardSMSclient.getBalance();
    }

    /**
     * 调用亿美WSDL，发送短信
     * @param orderChannelId
     * @param sms_type       String 短信类型
     *
     * @throws Exception
     */
    private Client createClient(String orderChannelId,String sms_type) {
        // 短信软件序列号
        String SMS_USER = "";
        // 短信软件密码
        String SMS_PASSWORD = "";
        //物流短信
        if (Constants.smsInfo.SMS_TYPE_LOGISTICS.equals(sms_type)) {
            //Map<String, String> mapTemp = null;
            // 短信配置
            //mapTemp = Codes.getCodeMap(Constants.smsInfo.SMS_INFO);
            //SMS_USER = mapTemp.get(Constants.smsInfo.USER);
            //SMS_PASSWORD = mapTemp.get(Constants.smsInfo.PASSWORD);
            SMS_USER = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sms_user);
            SMS_PASSWORD = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sms_password);
        }else{
            SMS_USER = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sms_user_marketing);
            SMS_PASSWORD = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sms_password_marketing);

        }
        //addSerial
        // 短信发送客户端
        //Client sendIdCardSMSclient = null;
        return  new Client(SMS_USER, SMS_PASSWORD);
    }
}
