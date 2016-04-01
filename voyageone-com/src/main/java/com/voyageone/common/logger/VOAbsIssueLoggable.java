package com.voyageone.common.logger;

import com.voyageone.common.components.issueLog.IssueLog;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * VOAbsIssueLoggable
 *
 * @author chuanyu.liang, 12/04/16.
 * @version 2.0.1
 * @since 2.0.0
 */
public abstract class VOAbsIssueLoggable extends VOAbsLoggable {
    @Autowired
    protected IssueLog issueLog;
}
