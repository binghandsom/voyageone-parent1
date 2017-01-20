package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.CmsBtOperationLogService;
import com.voyageone.task2.base.TBaseMQAnnoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TBaseMQCmsService
 *
 * @author aooer 2016/12/27.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class TBaseMQCmsService<TMQMessageBody extends IMQMessageBody> extends TBaseMQAnnoService<TMQMessageBody> {
    @Autowired
    CmsBtOperationLogService cmsBtOperationLogService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return getClass().getSimpleName();
    }

    /**
     * 获取job描述信息
     *
     * @return job描述信息
     */
    private String getTaskComment() {
        if (ListUtils.isNull(this.taskControlList)) {
            return "";
        }
        return this.taskControlList.get(0).getTask_comment();
    }

    /**
     * 发送消息
     *
     * @param messageBody messageBody
     * @throws Exception Exception
     */
    @Override
    public void startup(TMQMessageBody messageBody) throws Exception {
        try {
            $debug(this.getTaskName(), ":start->");
            onStartup(messageBody);
        } catch (BusinessException ex) {
            cmsBusinessExLog(messageBody, ex.getMessage());
        } catch (Exception ex) {
            //记异常日志
            cmsLog(messageBody, OperationLog_Type.unknownException, ex.getMessage());
            cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, ex);
            throw ex;
        } finally {
            $debug(this.getTaskName(), ":end");
        }
    }

    /**
     * 写日志
     *
     * @param messageBody       messageBody
     * @param operationLog_type operationLog_type
     * @param msg               msg
     */
    public void cmsLog(TMQMessageBody messageBody, OperationLog_Type operationLog_type, String msg) {
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, operationLog_type, msg);
    }

    /**
     * 写配置异常的日志
     *
     * @param messageBody messageBody
     * @param msg         msg
     */
    public void cmsConfigExLog(TMQMessageBody messageBody, String msg) {
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.configException, msg);
    }

    /**
     * 写业务异常的日志
     *
     * @param messageBody messageBody
     * @param msg         msg
     */
    public void cmsBusinessExLog(TMQMessageBody messageBody, String msg) {
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.businessException, msg);
    }

    /**
     * 成功结束
     *
     * @param messageBody messageBody
     * @param msg         msg
     */
    public void cmsSuccessLog(TMQMessageBody messageBody, String msg) {
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.success, msg);
    }
    /**
     * 成功结束
     *
     * @param messageBody messageBody
     * @param msg         msg
     */
    public void cmsSuccessIncludeFailLog(TMQMessageBody messageBody, String msg) {
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.successIncludeFail, msg);
    }
}
