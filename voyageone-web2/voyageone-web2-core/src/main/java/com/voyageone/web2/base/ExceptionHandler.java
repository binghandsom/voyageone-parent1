package com.voyageone.web2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.Constants.LANGUAGE;
import com.voyageone.common.configs.beans.ExceptionLogBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.message.enums.DisplayType;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.base.log.LogService;
import com.voyageone.web2.base.message.MessageService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常统一处理器
 * Created on 2015-11-26 16:10:23
 *
 * @author Jonas
 * @version 2.0.0
 */
public class ExceptionHandler extends VOAbsLoggable implements HandlerExceptionResolver {

    @Autowired
    private LogService logService;

    @Autowired
    private MessageService messageService;

    private boolean debug;

    private boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception exception) {
        try {

            log(request, exception);

            Object val = request.getSession().getAttribute(BaseConstants.SESSION_LANG);

            String lang = val == null ||
                    (
                            !val.equals(LANGUAGE.EN) &&
                            !val.equals(LANGUAGE.CN) &&
                            !val.equals(LANGUAGE.JP)
                    )
                    ? LANGUAGE.EN
                    : val.toString();

            // 业务异常记错误日志及堆栈信息，迁移到共通错误页面
            if (exception instanceof BusinessException) {
                return catchBusinessException(lang, (BusinessException) exception);
            }
            // 系统异常记错误日志及堆栈信息，迁移到共通错误页面
            if (exception instanceof SystemException) {
                return catchSystemException(lang, (SystemException) exception);

                // 其他未知异常
            } else {
                // 异常信息记录至数据库
                insertLogToDB(request, exception);
                return catchDefault(exception);
            }
        } catch (Exception e) {
            $error(e);
            return catchDefault(e);
        }
    }

    /**
     * 捕获业务异常
     */
    private ModelAndView catchBusinessException(String lang, BusinessException exception) {
        // 如果携带的 code 为空, 则尝试使用异常的本来信息
        if (!StringUtils.isEmpty(exception.getCode()))
            return messageDeal(lang, exception.getCode(), exception.getInfo());

        AjaxResponse ajaxResponse = new AjaxResponse();

        ajaxResponse.setCode(exception.getCode());
        ajaxResponse.setDisplayType(DisplayType.ALERT);
        ajaxResponse.setMessage(exception.getMessage());

        return jsonView(ajaxResponse);
    }

    /**
     * 捕获系统异常处理
     */
    private ModelAndView catchSystemException(String lang, SystemException exception) {

        AjaxResponse result = catchSysCode(exception);
        if (result != null) {
            return jsonView(result);
        }

        String code = exception.getCode();
        MessageBean msgModel = messageService.getMessage(lang, code);

        if (msgModel != null) {
            return messageDeal(lang, code, null);
        }

        String msg = exception.getMessage();
        if (StringUtils.isEmpty(msg)) msg = exception.getClass().getName();
        return exceptionDeal(msg, code);
    }

    /**
     * 捕获其他异常处理
     */
    private ModelAndView catchDefault(Exception exception) {
        // 尝试根据信息获取指定的错误提示
        String msg = exception.getMessage();

        if (isDebug()) msg = "Server Exception\nException Name: " + exception.getClass().getName() +
                "\nException Message: " + exception.getMessage() +
                "\nTrace Message 1: " + exception.getStackTrace()[0].toString() +
                "\nTrace Message 2: " + CommonUtil.getExceptionSimpleContent(exception);

        if (StringUtils.isEmpty(msg))
            msg = BaseConstants.UNKNOWN_ERROR_MSG;

        return exceptionDeal(msg, BaseConstants.UNKNOWN_ERROR_CODE);
    }

    /**
     * 信息类异常处理
     *
     * @param lang     选定语言
     * @param code     信息代码
     * @param args     格式化参数
     */
    private ModelAndView messageDeal(String lang, String code, Object[] args) {

        AjaxResponse ajaxResponse = new AjaxResponse();

        MessageBean msgModel = messageService.getMessage(lang, code);

        if (msgModel == null) {
            ajaxResponse.setCode(code);
            ajaxResponse.setDisplayType(DisplayType.ALERT);
            // TODO 暂时未处理根据Code来获取message
//            ajaxResponse.setMessage(String.format("this msg_code [%s(%s)] is not exists !!", code, lang));
            ajaxResponse.setMessage(code);
        } else {
            ajaxResponse.setMessage(msgModel, args);
        }

        return jsonView(ajaxResponse);
    }

    /**
     * 业务以外异常时ajax返回处理
     */
    private ModelAndView exceptionDeal(String msg, String code) {

        AjaxResponse ajaxResponse = new AjaxResponse();
        ajaxResponse.setCode(code);
        ajaxResponse.setMessage(msg);

        return jsonView(ajaxResponse);
    }

    private void insertLogToDB(HttpServletRequest request, Exception exception) {

        // 如果是开发阶段则不需要记录
        if (isDebug()) return;

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

        // 保证不会因为数据库字段长度的问题导致报错
        if (message.length() > 200) message = message.substring(0, 200);

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

    /**
     * 尝试抓取系统代码,这类代码不在 ct_message_info 中有定义,是一组特殊的功能代码
     *
     * @param exception 携带代码的异常
     * @return 是否成功处理
     */
    private AjaxResponse catchSysCode(SystemException exception) {

        switch (exception.getCode()) {
            // 由权限拦截器返回的渠道跳转代码
            case BaseConstants.CODE_SEL_CHANNEL:
                AjaxResponse ajaxResponse = new AjaxResponse();
                ajaxResponse.setCode(BaseConstants.CODE_SYS_REDIRECT);
                ajaxResponse.setRedirectTo("/channel.html");
                return ajaxResponse;
        }

        return null;
    }

    private boolean skipLog(Exception exception) {

        if (exception instanceof BusinessException)
            return true;

        if (exception instanceof SystemException) {
            SystemException systemException = (SystemException) exception;
            if (systemException.getCode() != null) {
                switch (systemException.getCode()) {
                    case BaseConstants.CODE_SEL_CHANNEL:
                    case BaseConstants.MSG_TIMEOUT:
                    case BaseConstants.MSG_DENIED:
                        $info(String.format("Break by SystemException with [ %s ]", exception.getMessage()));
                        return true;
                }
            }
        }

        return false;
    }

    private void log(HttpServletRequest request, Exception exception) {

        if (skipLog(exception))
            return;

        String url = request.getRequestURI();
        String simpleMessage = exception.getMessage();
        if (StringUtils.isEmpty(simpleMessage)) simpleMessage = exception.toString();
        $error(String.format("%s => %s", url, simpleMessage), exception);
    }

    private ModelAndView jsonView(AjaxResponse response) {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        ModelAndView mav = new ModelAndView(jsonView);
        mav.addObject("code", response.getCode());
        mav.addObject("data", response.getData());
        mav.addObject("displayType", response.getDisplayType());
        mav.addObject("message", response.getMessage());
        mav.addObject("redirectTo", response.getRedirectTo());
        return mav;
    }
}

