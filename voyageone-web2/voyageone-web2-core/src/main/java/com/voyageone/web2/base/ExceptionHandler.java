package com.voyageone.web2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.Constants.LANGUAGE;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.base.log.ExceptionLogBean;
import com.voyageone.web2.base.log.LogService;
import com.voyageone.web2.base.message.MessageService;
import com.voyageone.web2.core.model.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常统一处理器
 * Created on 2015-11-26 16:10:23
 * @author Jonas
 * @version 2.0.0
 */
public class ExceptionHandler implements HandlerExceptionResolver {

    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    private LogService logService;

    @Autowired
    private MessageService messageService;

    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception exception) {
        try {
            // log4j打印出详细信息，包括堆栈信息
            logger.error(exception.getMessage(), exception);
            // 异常信息记录至数据库
            insertLogToDB(request, exception);
            // 业务异常记错误日志及堆栈信息，迁移到共通错误页面
            if (exception instanceof BusinessException) {
                Object val = request.getSession().getAttribute(BaseConstants.SESSION_LANG);

                String lang = val == null ||
                        !val.equals(LANGUAGE.EN) ||
                        !val.equals(LANGUAGE.CN) ||
                        !val.equals(LANGUAGE.JP)
                        ? LANGUAGE.EN
                        : val.toString();

                return catchBusinessException(lang, (BusinessException) exception, response);
            }
            // 系统异常记错误日志及堆栈信息，迁移到共通错误页面
            if (exception instanceof SystemException) {
                return catchSystemException((SystemException) exception, response);

                // 其他未知异常
            } else {
                return catchDefault(exception, response);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return catchDefault(e, response);
        }
    }

    /**
     * 捕获业务异常
     */
    private ModelAndView catchBusinessException(String lang, BusinessException exception,
                                                HttpServletResponse response) {
        return businessExceptionDeal(lang, exception, response);
    }

    /**
     * 捕获系统异常处理
     */
    private ModelAndView catchSystemException(SystemException exception,
                                              HttpServletResponse response) {
        // 尝试根据信息获取指定的错误提示
        String msg = exception.getMessage();
        msg = StringUtils.isEmpty(msg) ? exception.getClass().getName(): msg;
        return exceptionDeal(msg, response);
    }

    /**
     * 捕获其他异常处理
     */
    private ModelAndView catchDefault(Exception exception,
                                      HttpServletResponse response) {
        // 尝试根据信息获取指定的错误提示
        String msg = exception.getMessage();
        msg = StringUtils.isEmpty(msg) ? exception.getClass().getName(): msg;
        return exceptionDeal(msg, response);
    }

    /**
     * 业务异常时ajax返回处理
     */
    private ModelAndView businessExceptionDeal(String lang, BusinessException exception, HttpServletResponse response) {
        AjaxResponse ajaxResponse = new AjaxResponse();

        String code = exception.getCode();
        String msg = messageService.getMessage(lang, code);

        if (StringUtils.isEmpty(msg)) {
            msg = String.format("this msg_code [%s(%s)] is not exists !!", code, lang);
        } else {
            msg = String.format(msg, exception.getInfo());
        }

        ajaxResponse.setCode(code);
        ajaxResponse.setMessage(msg);

        ajaxResponse.writeTo(response);

        return null;
    }

    /**
     * 业务以外异常时ajax返回处理
     */
    private ModelAndView exceptionDeal(String msg, HttpServletResponse response) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setCode("5");
        ajaxResponse.setMessage(msg);

        ajaxResponse.writeTo(response);
        return null;
    }

    private void insertLogToDB(HttpServletRequest request, Exception exception) {
        // 异常发生时间
        String dateTime = DateTimeUtil.getNow(DateTimeUtil.DATE_TIME_FORMAT_1);
        // 取得用户信息
        UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(BaseConstants.SESSION_USER);
        // 操作人
        String userName = user != null ? user.getUserName() : "ghost user";
        // 请求URL
        String url = request.getServletPath();
        // 异常类型
        String type = exception.getClass().getName();
        // 堆栈信息
        // String stackInfo = getExceptionStack(exception);
        // 异常描述
        String message = CommonUtil.getExceptionSimpleContent(exception);

        ExceptionLogBean exceptionBean = new ExceptionLogBean();
        exceptionBean.setDateTime(dateTime);
        exceptionBean.setExceptionType(type);
        exceptionBean.setDescription(message);
        exceptionBean.setUrl(url);
        exceptionBean.setUserName(userName);
        exceptionBean.setCreated(dateTime.split("\\.")[0]);

        // 异常信息记录至数据库
        logService.addExceptionLog(exceptionBean);
    }

}

