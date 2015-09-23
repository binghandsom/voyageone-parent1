package com.voyageone.batch.core.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.common.components.issueLog.beans.IssueLogBean;
import com.voyageone.common.components.issueLog.dao.IssueLogDao;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class IssueTaskErrorService extends BaseTaskService {

    @Autowired
    IssueLogDao issueLogDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CORE;
    }

    @Override
    public String getTaskName() {
        return "issueTaskErrorJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        SubSystem[] subSystems = SubSystem.values();

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList, TaskControlEnums.Name.row_count);

        int intRowCount = 1;

        if (!StringUtils.isEmpty(row_count)) {
            intRowCount = Integer.valueOf(row_count);
        }

        try {

//            //从数据库中读取最多ISSUE_LOG_MAX_ROWS条错误记录
//            List<IssueLogBean> issueLogList = issueLogDao.selectBySendFlg(0, intRowCount);
//
//            if (issueLogList == null || issueLogList.isEmpty()) {
//                logger.info("Not find any error log!");
//                return;
//            }

            // 遍历所有子系统，根据子系统将错误记录分类并加入新的list，最后通过邮件发送出去
            for (SubSystem subsystem : subSystems) {

                //从数据库中读取最多ISSUE_LOG_MAX_ROWS条错误记录
                List<IssueLogBean> issueLogList = issueLogDao.selectBySendFlg(subsystem.getId(),0, intRowCount);

                if (issueLogList == null || issueLogList.isEmpty()) {
                    logger.info("Not find any error log!");
                    continue;
                }
                HashSet<String> ids = new HashSet<>();
                List<IssueLogBean> issueLogs = new ArrayList<>();

                issueLogList.stream().filter(issueLog -> issueLog.getSub_system() == subsystem).forEach(issueLog -> {
                    issueLogs.add(issueLog);
                    ids.add("" + issueLog.getId());
                });

                if (!issueLogs.isEmpty()) {
                    if (sendIssueLogMail(issueLogs, subsystem)) {
                        // 发送成功，则将这些issueLog的send_flg置为1，表示已经发送成功
                        logger.info("" + issueLogDao.updateSendFlgByIds(ids, 1) + " error log has been issued to " + subsystem.toString());
                    }
                } else {
                    logger.info("Not find any error log for subsystem(" + subsystem.toString() + ")!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 功能： 根据子系统，返回相应的邮件组名称
     *
     * @param subSystem 子系统
     */
    public String getMailReceiverBySubSystem(SubSystem subSystem) {
        String mailReceiver;
        switch (subSystem) {
            case OMS:
                mailReceiver = "ITOMS";
                break;
            case WMS:
                mailReceiver = "ITWMS";
                break;
            case CMS:
                mailReceiver = "ITCMS";
                break;
            case IMS:
                mailReceiver = "ITIMS";
                break;
            case SYNSHIP:
                mailReceiver = "ITSYNSHIP";
                break;
            default:
                mailReceiver = "VOYAGEONE_ERROR";
                break;
        }
        return mailReceiver;
    }

    /*
     * 功能: 发送异常信息给指定子系统
     * @param issueLogBeanList  异常信息列表
     * @param subSystem     子系统
     */
    public boolean sendIssueLogMail(List<IssueLogBean> issueLogBeanList, SubSystem subSystem) {

        // 邮件正文
        StringBuilder emailConnect = new StringBuilder();
        // 邮件组名称
        String mail_receiver = getMailReceiverBySubSystem(subSystem);

        if (issueLogBeanList.size() > 0) {

            StringBuilder tableBody = new StringBuilder();

            for (IssueLogBean issueLogBean : issueLogBeanList) {
                tableBody.append(String.format(Constants.ISSUE_LOG_MAIL_FORMATE.ROW,
                        issueLogBean.getId(),
                        ErrorType.valueOf(issueLogBean.getError_type().toString()),
                        SubSystem.valueOf(issueLogBean.getSub_system().toString()),
                        issueLogBean.getError_location(),
                        issueLogBean.getDescription(),
                        issueLogBean.getDate_time(),
                        DateTimeUtil.getLocalTime(issueLogBean.getDate_time(), 8),
                        issueLogBean.getDescription_add()));
            }
            // 拼接表头字符串
            String head = String.format(Constants.ISSUE_LOG_MAIL_FORMATE.HEAD, issueLogBeanList.size());

            // 拼接表内容
            String table = String.format(Constants.ISSUE_LOG_MAIL_FORMATE.TABLE, head, tableBody.toString());

            // 拼接邮件正文
            emailConnect
                    .append(Constants.EMAIL_STYLE_STRING)
                    .append(table);
        }

        // 拼接邮件主题
        String subject = String.format(Constants.ISSUE_LOG_MAIL_FORMATE.SUBJECT, subSystem.toString());
        logger.info("发送给" + mail_receiver + "的issueLog邮件开始");
        try {
            Mail.sendAlert(mail_receiver, subject, emailConnect.toString());
            logger.info("发送给" + mail_receiver + "的issueLog邮件成功");
        } catch (MessagingException e) {
            // 如果发送失败，依然希望通过以下内容确定该邮件是否发送出去
            logger.info("发送给" + mail_receiver + "的issueLog邮件失败:" + e);
            logger.info("发送失败的邮件正文:" + emailConnect);
            return false;
        }

        return true;
    }
}
