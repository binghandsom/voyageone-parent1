package com.voyageone.batch.synship.service;

import com.voyageone.batch.SynshipConstants;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.synship.dao.SmsHistoryDao;
import com.voyageone.batch.synship.dao.TrackingDao;
import com.voyageone.batch.synship.modelbean.ClientTrackingSimBean;
import com.voyageone.batch.synship.modelbean.SmsHistoryBean;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.yimei.YMSmsSendService;
import com.voyageone.common.components.yimei.bean.YMSMSSendBean;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fred on 2015/8/28.
 */
@Service
public class SendSMSService  extends BaseTaskService {

    @Autowired
    SmsHistoryDao smsHistoryDao;

    @Autowired
    private YMSmsSendService ymSmsSendService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.SYNSHIP;
    }

    @Override
    public String getTaskName() {
        return "SendSMSJob";
    }

    /**
     * 获取打印的日志是否需要包含线程
     */
    @Override
    public boolean getLogWithThread() {
        return true;
    }

    private final int SMS_TIME_OUT = -9025;

    private final int PHONE_NUM_ERR = -3;


    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.row_count);
        int intRowCount = 1;
        if (!StringUtils.isNullOrBlank2(row_count)) {
            intRowCount = Integer.valueOf(row_count);
        }
        // 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {
            final int finalIntRowCount = intRowCount;
            threads.add(new Runnable() {
                @Override
                public void run() {
                    new sendSMS(orderChannelID, finalIntRowCount).doRun();
                }
            });
        }
        runWithThreadPool(threads, taskControlList);
    }

    /**
     * 按渠道进行模拟Shipment
     */
    public class sendSMS {
        private OrderChannelBean channel;
        private int rowCount;
        private String charCheck = "【";

        // 手机号码发送次数一览
        HashMap<String, Integer> mapPhone = new HashMap<String, Integer>();

        public sendSMS(String orderChannelId, int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.rowCount = rowCount;
        }

        public void doRun() {
            $info(channel.getFull_name() + " 定时发送短信开始");
            YMSMSSendBean ymsmsSendBean;
            // 短信内容
            String content = "";
            // 每条短信字数
            String SMS_WORDS = "";
            // 每条短信费用
            String SMS_COST = "";
            double eachFee = 0d;
            Map<String, String> mapTemp = null;

            try {
                // 需要模拟 Shipment的记录取得
                final List<SmsHistoryBean> sendSMSLstLst = smsHistoryDao.getSendSMSLst(channel.getOrder_channel_id(), rowCount);
                //final List<SmsHistoryBean> sendSMSLstLst =  trackingDao.getSendSMSLst(channel.getOrder_channel_id(), rowCount);
                mapTemp = Codes.getCodeMap(Constants.smsInfo.SMS_INFO);
                SMS_WORDS = mapTemp.get(Constants.smsInfo.WORDS);
                SMS_COST = mapTemp.get(Constants.smsInfo.COST);
                // 每条短信的费用
                eachFee = Double.valueOf(SMS_COST);

                $info(channel.getFull_name() + "----------定时发送短信件数：" + sendSMSLstLst.size());
                // 签名内容设定
                String signName = String.format(SynshipConstants.SMS_CHECK.SIGN_NAME, channel.getFull_name());
                $info("短信签名：" + signName);
                // 发送短信
                for (SmsHistoryBean sendSMSBean : sendSMSLstLst) {
                    // 判断同一号码发送次数
                    if (checkSendCount(sendSMSBean.getShip_phone()) == false) {
                        continue;
                    }

                    //聚美订单判定
                    String jumeiChannelFullName = "";
                    OrderChannelBean jumeiChannel = new OrderChannelBean();
                    List<ShopConfigBean> shopConfig = ShopConfigs.getConfigs(channel.getOrder_channel_id(), sendSMSBean.getCart_id(), ShopConfigEnums.Name.main_channel_id);
                    if (shopConfig != null && shopConfig.size() > 0 && shopConfig.get(0).getCfg_val2().equals(Constants.smsChange.CHANGE_ON)){
                        jumeiChannel = ChannelConfigs.getChannel(shopConfig.get(0).getCfg_val1());
                        jumeiChannelFullName = String.format(SynshipConstants.SMS_CHECK.SIGN_NAME, jumeiChannel.getFull_name());
                        $info("聚美变化后短信签名：" + jumeiChannelFullName);
                    }

                    //ymsmsSendBean = formatYMSMSSendBean("001","13661895431","【Sneakerhead】林冰洁，您购买的宝贝美国洛杉矶发货，请在23点前点击 synship.net.cn/a/UBVrQ3 上传身份证信息用于清关。");
                    // 判断签名是否设定
                    if (sendSMSBean.getSent_conent().startsWith(signName) || (!StringUtils.isNullOrBlank2(jumeiChannelFullName) && sendSMSBean.getSent_conent().startsWith(jumeiChannelFullName))) {
                        //聚美变化后短信签名有
                        if (!StringUtils.isNullOrBlank2(jumeiChannelFullName)){
                            // 签名已设定
                            content = sendSMSBean.getSent_conent().replaceFirst(signName,jumeiChannelFullName);
                        }else {
                            // 签名已设定
                            content = sendSMSBean.getSent_conent();
                        }
                    } else {
                        // 判断现有签名不正确
                        if (sendSMSBean.getSent_conent().startsWith(charCheck)){
                            $info(channel.getFull_name() + "定时发送短信时短信内容中签名不正确：" + sendSMSBean.getSent_conent());
                            logIssue(channel.getFull_name() + "定时发送短信时短信内容中签名不正确:" + " order_channel_id = " + sendSMSBean.getOrder_channel_id() +
                                    ",  phone = " + sendSMSBean.getShip_phone()+ ",  conent = " + sendSMSBean.getSent_conent());
                            continue;
                        } else {
                            //聚美变化后短信签名有
                            if (!StringUtils.isNullOrBlank2(jumeiChannelFullName)){
                                // 签名未设定
                                content = jumeiChannelFullName + sendSMSBean.getSent_conent();
                            }else {
                                // 签名未设定
                                content = signName + sendSMSBean.getSent_conent();
                            }
                        }
                    }
                    $info("order_channel_id = " + sendSMSBean.getOrder_channel_id() + ",  phone = " + sendSMSBean.getShip_phone() + ",  conent = " + content);
                    //聚美变化后短信签名有
                    if (!StringUtils.isNullOrBlank2(jumeiChannelFullName)){
                        ymsmsSendBean = formatYMSMSSendBean(jumeiChannel.getOrder_channel_id(), sendSMSBean.getShip_phone(), content, sendSMSBean.getSms_type());
                    }else {
                        ymsmsSendBean = formatYMSMSSendBean(sendSMSBean.getOrder_channel_id(), sendSMSBean.getShip_phone(), content, sendSMSBean.getSms_type());
                    }
                    try {
                        Integer sendFlg = ymSmsSendService.sendSMS(ymsmsSendBean);
                        $info("发送短信返回值 = " + sendFlg.toString());
                        // 发送成功
                        if (sendFlg == 0) {
                            updateSmsHistory(sendSMSBean,content,eachFee,SMS_WORDS);
                        //timeOut的情况
                        } else if(sendFlg == SMS_TIME_OUT) {
                            // 记录错误至IssueLog表
                            sendErrToIssue(sendFlg,sendSMSBean.getShip_phone());
                        //手机号码错误的情况
                        } else if(sendFlg == PHONE_NUM_ERR) {
                            updateSmsHistoryStatus(sendSMSBean, SynshipConstants.SMS_STATUS.CHECK_ERROR);
                            // 记录错误至IssueLog表
                            sendErrToIssue(sendFlg,sendSMSBean.getShip_phone());
                        //其他错误的情况
                        } else {
                            updateSmsHistoryStatus(sendSMSBean, SynshipConstants.SMS_STATUS.SENT_FAILED);
                            // 记录错误至IssueLog表
                            sendErrToIssue(sendFlg,sendSMSBean.getShip_phone());
                        }
                    } catch (Exception e) {
                        $info(channel.getFull_name() + "定时发送短信发生错误：", e);
                        logIssue(e.getMessage(), channel.getFull_name() + "定时发送短信发生错误:" +  " order_channel_id = " + sendSMSBean.getOrder_channel_id() +
                                ",  phone = " + sendSMSBean.getShip_phone()+ ",  conent = " + sendSMSBean.getSent_conent());
                    }
                }
                // 物流短信余额判断
                checkBalance(sendSMSLstLst.size(),channel.getOrder_channel_id(),Constants.smsInfo.SMS_TYPE_LOGISTICS, SynshipConstants.SMS_CHECK.ACCOUNT_BALANCE_LOGISTICS);
                // 营销短信余额判断
                checkBalance(sendSMSLstLst.size(), channel.getOrder_channel_id(), Constants.smsInfo.SMS_TYPE_MARKETING, SynshipConstants.SMS_CHECK.ACCOUNT_BALANCE_MARKETING);

            } catch (Exception e) {
                $info(channel.getFull_name() + "定时发送短信发生错误：", e);
                logIssue(e.getMessage(), channel.getFull_name() + "定时发送短信发生错误");
            }
            $info(channel.getFull_name() + " 定时发送短信结束");
        }

        /**
         * 发送短信数据准备
         *
         * @param orderChannelID String  销售渠道
         * @param phone          String   手机号码
         * @param content        String 短信内容
         * @param sms_type       String 短信类型
         */
        private YMSMSSendBean formatYMSMSSendBean(String orderChannelID, String phone, String content,String sms_type) {
            YMSMSSendBean ymsmsSendBean = new YMSMSSendBean();
            //    电话号码
            ymsmsSendBean.setPhone(phone);
            //    销售渠道
            ymsmsSendBean.setOrder_channel_id(orderChannelID);
            //    短信内容
            ymsmsSendBean.setContent(content);
            //    短信类型
            ymsmsSendBean.setSms_type(sms_type);
            return ymsmsSendBean;
        }

        /**
         * 判断同一号码发送次数
         *
         * @param phone String   手机号码
         * @return true(可以发送) or false（不可以发送）
         */
        private boolean checkSendCount(String phone) {
            // 同一号码发送次数的取得
            int sentCount = 0;
            if (mapPhone.containsKey(phone)) {
                sentCount = mapPhone.get(phone);
            } else {
                mapPhone.put(phone, sentCount);
            }
            // 同一号码已发送满限制次数的场合，不再发送
            if (sentCount < SynshipConstants.SMS_CHECK.SMS_SENT_lIMIT) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 短信发送错误处理
         * @param sendSMSBean    sms_history表用Bean
         * @param conent          短信内容
         * @param smsWords        每条短信字数
         * @param eachFee         每条短信费用
         *
         */
        private void updateSmsHistory(SmsHistoryBean sendSMSBean,String conent,double eachFee,String smsWords ){

            $info("更新sms_history开始");
            // 更新sms_history表
            double count = Math.ceil((double) conent.length() / Double.valueOf(smsWords));
            double smsFee = count * eachFee;

            sendSMSBean.setSent_conent(conent);
            sendSMSBean.setSent_cost(String.valueOf(smsFee));
            sendSMSBean.setSms_flg(SynshipConstants.SMS_STATUS.SENT_SUCCESS);
            sendSMSBean.setSent_time(DateTimeUtil.getNow());
            sendSMSBean.setUpdate_time(DateTimeUtil.getNow());
            sendSMSBean.setUpdate_person(getTaskName());
            try {
                smsHistoryDao.UpdateSmsHistory(sendSMSBean);
            } catch (Exception e) {
                $info(channel.getFull_name() + "更新sms_history发生错误：", e);
                logIssue(e.getMessage(), channel.getFull_name() + "更新sms_history发生错误");
            }
            // 同一号码发送次数的统计
            Integer sentCount = mapPhone.get(sendSMSBean.getShip_phone()) + 1;
            mapPhone.put(sendSMSBean.getShip_phone(), sentCount);
            $info("更新sms_history结束");

        }


        /**
         * 短信发送错误处理
         * @param sendSMSBean    sms_history表用Bean
         * @param status          发送状态
         *
         */
        private void updateSmsHistoryStatus(SmsHistoryBean sendSMSBean,String status ){

            $info("更新sms_history  status开始");
            // 更新sms_history表
            sendSMSBean.setSms_flg(status);
            sendSMSBean.setUpdate_time(DateTimeUtil.getNow());
            sendSMSBean.setUpdate_person(getTaskName());
            try {
                smsHistoryDao.UpdateSmsHistoryStatus(sendSMSBean);
            } catch (Exception e) {
                $info(channel.getFull_name() + "更新sms_history status发生错误：", e);
                logIssue(e.getMessage(), channel.getFull_name() + "更新sms_history status发生错误");
            }
            $info("更新sms_history  status结束");
        }



        /**
         * 短信发送错误处理
         * @param sendFlg    短信发送返回值
         * @param phone      手机号码
         *
         */
        private void sendErrToIssue(int sendFlg,String phone){
            // 记录错误至IssueLog表
            if (SynshipConstants.SMS_ERR_INFO.CONTENTS.containsKey(sendFlg)) {
                $info(channel.getFull_name() + " 返回值 = " + String.valueOf(sendFlg) + " Phone = " + phone +
                        "  定时发送短信返回错误：" + SynshipConstants.SMS_ERR_INFO.CONTENTS.get(sendFlg));
                // 根据返回错误KEY，可以找到错误内容
                logIssue(String.valueOf(sendFlg), channel.getFull_name() + " Phone = " + phone +
                        "  定时发送短信返回错误：" + SynshipConstants.SMS_ERR_INFO.CONTENTS.get(sendFlg));
            } else {
                $info(channel.getFull_name() + " 返回值 = " + String.valueOf(sendFlg) + " Phone = " + phone +
                        "  定时发送短信返回错误：错误信息未知");
                // 根据返回错误KEY，可以找到错误内容
                logIssue(String.valueOf(sendFlg), channel.getFull_name() + " Phone = " + phone +
                        "  定时发送短信返回错误：错误信息未知");
            }
        }

        /**
         * 短信余额判断
         *
         * @param sendSMSLstLstSize 需要发送短信件数
         * @param orderChannelId
         * @param
         *
         */
        private void checkBalance(int sendSMSLstLstSize,String orderChannelId,String sms_type, Double accountBalance) {
            // 余额不足的场合
            if (sendSMSLstLstSize > 0) {
                double balance = 0d;
                double balanceMarketing = 0d;
                String strValue= "";
                try {
                    //物流短信
                    if (Constants.smsInfo.SMS_TYPE_LOGISTICS.equals(sms_type)) {
                        strValue = "物流";
                    }else{
                        strValue = "营销";
                    }
                    //短信余额
                    balance = ymSmsSendService.getBalance(orderChannelId,sms_type);

                    $info(strValue + "短信账户的剩余金额(实际金额)： " + (balance / 2));
                    // 余额不足200的场合，发送要求充值邮件(之所以用大于零而不是大于等于零的原因是防止误报)
                    if (balance > 0d && (balance / 2) < accountBalance) {
                        logIssue(strValue + "短信账户的剩余金额不足，请尽快充值", "短信账户的剩余金额:" + (balance / 2));
                    }
                } catch (Exception e) {
                    $info(channel.getFull_name() + "  " + strValue + "短信账户的剩余金额取得发生错误：", e);
                    logIssue(e.getMessage(), channel.getFull_name() + "  " + strValue + "短信账户的剩余金额取得发生错误");
                }
            }
        }
    }
}
