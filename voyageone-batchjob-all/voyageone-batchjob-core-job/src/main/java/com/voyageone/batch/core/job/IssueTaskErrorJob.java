package com.voyageone.batch.core.job;

import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.service.IssueTaskErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 邮件通知任务出错信息
 * 根据子系统类别选择邮件通知人员
 *
 * @author Leo
 */
@Component("CoreIssueTaskErrorTask")
public class IssueTaskErrorJob extends BaseTaskJob {

    @Autowired
    IssueTaskErrorService issueTaskErrorService;

    @Override
    protected BaseTaskService getTaskService() {
        return issueTaskErrorService;
    }

}
