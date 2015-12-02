package com.voyageone.core.exception;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.init.MessageHelp;
import com.voyageone.core.modelbean.ExceptionLogBean;
import com.voyageone.core.modelbean.MessageInfoBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.LogService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ExceptionHandler implements HandlerExceptionResolver {

    private static Log logger = LogFactory.getLog(ExceptionHandler.class);

    @Autowired
    private LogService logService;

    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception exception) {
        String token = null;

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                token = (String) session.getAttribute(CoreConstants.VOYAGEONE_USER_TOKEN);
            }

            // log4j打印出详细信息，包括堆栈信息
            logger.error(exception.getMessage(), exception);
            // 异常信息记录至数据库
            insertLogToDB(request, exception);
            // 业务异常记错误日志及堆栈信息，迁移到共通错误页面
            if (exception instanceof BusinessException) {
                String lang = (String) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);
                return catchBusinessException(lang, token, (BusinessException) exception, request, response);
            }
            // 系统异常记错误日志及堆栈信息，迁移到共通错误页面
            if (exception instanceof SystemException) {
                return catchSystemException((SystemException) exception, token, request, response);

                // 其他未知异常
            } else {
                return catchDefault(exception, token, request, response);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return catchDefault(e, token, request, response);
        }
    }

    /**
     * 捕获业务异常
     */
    private ModelAndView catchBusinessException(String lang, String token, BusinessException exception, HttpServletRequest request,
                                                HttpServletResponse response) {
        // 尝试根据信息获取指定的错误提示
        String key = exception.getCode() + "-" + lang;
        MessageInfoBean msg = MessageHelp.getMsgByLang(key);
        Object[] info = exception.getInfo();
        return businessExceptionDeal(msg, token, request, response, info);
    }

    /**
     * 捕获系统异常处理
     */
    private ModelAndView catchSystemException(SystemException exception, String token, HttpServletRequest request,
                                              HttpServletResponse response) {
        // 尝试根据信息获取指定的错误提示
        String msg = exception.getMessage().split(CoreConstants.EXCEPTION_MESSAGE_PREFIX)[0];

        return exceptionDeal(msg, token, request, response);
    }

    /**
     * 捕获其他异常处理
     */
    private ModelAndView catchDefault(Exception exception, String token, HttpServletRequest request,
                                      HttpServletResponse response) {
        // 尝试根据信息获取指定的错误提示
        String msg = exception.getMessage().split(CoreConstants.EXCEPTION_MESSAGE_PREFIX)[0];

        return exceptionDeal(msg, token, request, response);
    }

    /**
     * 业务异常时ajax返回处理
     */
    private ModelAndView businessExceptionDeal(MessageInfoBean msg, String token, HttpServletRequest request,
                                               HttpServletResponse response, Object... info) {
        AjaxResponseBean bean = new AjaxResponseBean();

        String msgInfo;
        int type = MessageConstants.MESSAGE_TYPE_EXCEPTION;

        if (msg == null) {

            msgInfo = "this msg_code is not exists";
        } else if (StringUtils.isEmpty(msg.getMessage())) {

            msgInfo = "this msg_code is not exists";
        } else {
            msgInfo = msg.getMessage();
            if (info != null && info.length > 0) {
                msgInfo = String.format(msgInfo, info);
            }
            type = msg.getMessageType();
        }

        bean.setResult(false, type, msgInfo);
        bean.setToken(token);

        bean.writeTo(request, response);

        return null;
    }

    /**
     * 业务以外异常时ajax返回处理
     */
    private ModelAndView exceptionDeal(String msg, String token, HttpServletRequest request,
                                       HttpServletResponse response) {
        AjaxResponseBean bean = new AjaxResponseBean();
        bean.setResult(false, MessageConstants.MESSAGE_TYPE_EXCEPTION, msg);
        bean.setToken(token);

        bean.writeTo(request, response);

        return null;
    }

    private void insertLogToDB(HttpServletRequest request, Exception exception) {
        // 异常发生时间
        String dateTime = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_1);
        // 取得用户信息
        UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
        // 操作人
        String userName = "";
        if (user != null) {
            userName = user.getUserName();
        }

        // 请求URL
        String url = request.getServletPath();
        // 异常类型
        String type = exception.getClass().getName();
        // 堆栈信息
        // String stackInfo = getExceptionStack(exception);
        // 异常描述
        String message = exception.getMessage() == null ?
                "" : exception.getMessage().split(CoreConstants.EXCEPTION_MESSAGE_PREFIX)[0];
        if (message != null && message.length() > CoreConstants.EXCEPTION_MESSAGE_LENGTH) {
            message = message.substring(0, CoreConstants.EXCEPTION_MESSAGE_LENGTH);
        }

        ExceptionLogBean exceptionBean = new ExceptionLogBean();
        exceptionBean.setDateTime(dateTime);
        exceptionBean.setExceptionType(type);
        exceptionBean.setDescription(message);
        exceptionBean.setUrl(url);
        exceptionBean.setUserName(userName);
        exceptionBean.setCreated(dateTime.split("\\.")[0]);

        // 异常信息记录至数据库
        logService.insertExceptionLog(exceptionBean);
    }

}

