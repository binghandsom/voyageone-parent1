package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.CmsBtOperationLogService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.base.TBaseMQAnnoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * TBaseMQCmsService
 *
 * @author aooer 2016/12/27.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class TBaseMQCmsService<TMQMessageBody extends IMQMessageBody> extends TBaseMQAnnoService<TMQMessageBody> {
    public long count = 0;
    public boolean isFailed = false;
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
            String begin = DateTimeUtil.format(new Date(), DateTimeUtil.DEFAULT_DATETIME_FORMAT);
            $debug(this.getTaskName(), ":start->");
            onStartup(messageBody);
            String end = DateTimeUtil.format(new Date(), DateTimeUtil.DEFAULT_DATETIME_FORMAT);

            $debug(String.format("处理总件数(%s), 开始时间: %s, 结束时间: %s", count, begin, end));
            if (!isFailed)
                cmsSuccessLog(messageBody, String.format("处理总件数(%s), 开始时间: %s, 结束时间: %s", count == 0 ? "无法统计件数" : count, begin, end));
        } catch (BusinessException ex) {
            cmsBusinessExLog(messageBody, ex.getMessage());
        } catch (Exception ex) {
            //记异常日志
            $error(CommonUtil.getMessages(ex));
            cmsLog(messageBody, OperationLog_Type.unknownException, CommonUtil.getMessages(ex));
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
     * @param comment           comment
     */
    public void cmsLog(TMQMessageBody messageBody, OperationLog_Type operationLog_type, String comment) {
        switch (operationLog_type.getId()) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                isFailed = true;
                break;
            default:
                isFailed = false;
                break;
        }
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, operationLog_type, comment);
    }

    /**
     * 写配置异常的日志
     *
     * @param messageBody messageBody
     * @param comment     comment
     */
    public void cmsConfigExLog(TMQMessageBody messageBody, String comment) {
        isFailed = true;
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.configException, comment);
    }

    /**
     * 写业务异常的日志
     *
     * @param messageBody messageBody
     * @param comment     comment
     */
    public void cmsBusinessExLog(TMQMessageBody messageBody, String comment) {
        isFailed = true;
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.businessException, comment);
    }

    /**
     * 成功结束
     *
     * @param messageBody messageBody
     * @param comment     comment
     */
    public void cmsSuccessLog(TMQMessageBody messageBody, String comment) {
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.success, comment);
    }

    /**
     * 成功结束
     *
     * @param messageBody messageBody
     * @param comment     comment
     * @param msg         msg
     */
    public void cmsSuccessIncludeFailLog(TMQMessageBody messageBody, String comment, List<CmsBtOperationLogModel_Msg> msg) {
        isFailed = true;
        cmsBtOperationLogService.log(getTaskName(), getTaskComment(), messageBody, OperationLog_Type.successIncludeFail, comment, msg);
    }
}
