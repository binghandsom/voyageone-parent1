package com.voyageone.batch.core.service;

import com.google.gson.Gson;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.CoreConstants;
import com.voyageone.batch.core.mapper.MonitorMapper;
import com.voyageone.batch.core.modelbean.MonitorBean;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckSlaveStatusService extends BaseTaskService {

	@Autowired
	MonitorMapper monitorMapper;

	@Override
	public SubSystem getSubSystem() {
		return SubSystem.CORE;
	}

	@Override
	public String getTaskName() {
		return "CheckSlaveStatusJob";
	}

	public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        MonitorBean monitorBean = null;
        String errorMail = "";

		try {

			monitorBean = monitorMapper.getSlaveStatus();

			String json = monitorBean == null ? "" : new Gson().toJson(monitorBean);

			logger.info("从库状态：" + json);

			if (monitorBean == null) {
				logger.info("从库状态取得为空" );
				errorMail = sendErrorMail(monitorBean,"");
			}
			else if (monitorBean.getSlave_IO_Running().equals(CoreConstants.SlaveStatus.YES) && monitorBean.getSlave_SQL_Running().equals(CoreConstants.SlaveStatus.YES)) {
				logger.info("从库状态同步正常" );
			} else {
				logger.info("从库状态同步正常，错误信息："+ monitorBean.getLast_Error() );
                errorMail = sendErrorMail(monitorBean,"");
			}

		} catch (Exception e) {
            logger.info("从库状态取得失败："+e );
            errorMail = sendErrorMail(monitorBean, e.toString());
		}

        if (!StringUtils.isNullOrBlank2(errorMail)) {
            logger.info("错误邮件出力");
            Mail.sendAlert(CodeConstants.EmailReceiver.VOYAGEONE_ERROR, CoreConstants.EmailCheckSlaveStatusError.SUBJECT, errorMail);
        }

	}

    /**
     * 从库状态同步信息错误邮件
     * @param monitorBean 从库状态同步信息
     * @return 错误邮件内容
     */
    private String sendErrorMail(MonitorBean monitorBean,String exception) {

        StringBuilder builderContent = new StringBuilder();

        String detail = "";

        if (StringUtils.isNullOrBlank2(exception)) {
            if (monitorBean == null) {
                detail = CoreConstants.EmailCheckSlaveStatusError.EMPTY;
            }else{

                StringBuilder builderDetail = new StringBuilder();

                builderDetail.append(String.format(CoreConstants.EmailCheckSlaveStatusError.ROW,
                        StringUtils.null2Space2(monitorBean.getSlave_IO_Running()),
                        StringUtils.null2Space2(monitorBean.getSlave_IO_State()),
                        StringUtils.null2Space2(monitorBean.getSlave_SQL_Running()),
                        StringUtils.null2Space2(monitorBean.getSlave_SQL_Running_State()),
                        StringUtils.null2Space2(monitorBean.getLast_Errno()),
                        StringUtils.null2Space2(monitorBean.getLast_Error())));

                detail = String.format(CoreConstants.EmailCheckSlaveStatusError.TABLE, builderDetail.toString());
            }
        } else {
            detail = String.format(CoreConstants.EmailCheckSlaveStatusError.ERROR, exception);
        }

        builderContent
                .append(Constants.EMAIL_STYLE_STRING)
                .append(CoreConstants.EmailCheckSlaveStatusError.HEAD)
                .append(detail);

        return  builderContent.toString();

    }

}
