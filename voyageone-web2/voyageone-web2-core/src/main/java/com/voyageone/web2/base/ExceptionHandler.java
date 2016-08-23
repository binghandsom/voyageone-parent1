package com.voyageone.web2.base;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.common.Constants.LANGUAGE;
import com.voyageone.common.configs.beans.ExceptionLogBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.message.enums.DisplayType;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.com.MessageBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.base.log.LogService;
import com.voyageone.web2.base.message.MessageService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 异常统一处理器
 * Created on 2015-11-26 16:10:23
 *
 * @author Jonas
 * @version 2.4.0
 * @since 2.0.0
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

        // 先创建默认的内容
        AjaxResponse result = new AjaxResponse();
        Object[] args = null;

        // 默认信息以 alert 弹出显示
        result.setDisplayType(DisplayType.ALERT);

        try {
            // 先尝试输出错误的信息到控制台
            log(request, exception);

            // 汇总异常携带的信息
            if (exception instanceof BusinessException) {
                BusinessException businessException = (BusinessException) exception;
                result.setCode(businessException.getCode());
                result.setMessage(businessException.getMessage());
                args = businessException.getInfo();
            } else if (exception instanceof SystemException) {
                SystemException systemException = (SystemException) exception;
                result.setCode(systemException.getCode());
                result.setMessage(systemException.getMessage());
            } else {
                // 其他未知异常
                // 异常信息记录至数据库
                insertLogToDB(request, exception);
                result.setMessage(getUnknownExceptionMessage(exception));
                result.setCode(BaseConstants.UNKNOWN_ERROR_CODE);
            }
        } catch (Exception e) {
            $error(e);
            result.setMessage(getUnknownExceptionMessage(exception));
            result.setCode(BaseConstants.UNKNOWN_ERROR_CODE);
        }

        Object val = request.getSession().getAttribute(BaseConstants.SESSION_LANG);

        String code = result.getCode(), lang = val == null ||
                (
                        !val.equals(LANGUAGE.EN) &&
                                !val.equals(LANGUAGE.CN) &&
                                !val.equals(LANGUAGE.JP)
                )
                ? LANGUAGE.EN
                : val.toString();

        // 如果 code 有并且是数字 (isNumeric 内部会判断 isEmpty), 就尝试去信息库查询
        // 如果这个 code 取不到预定义的信息, 就放弃设置, 保留异常自身的信息。通常可能就是 code 或另一段描述信息
        // 如果是预定义的系统 code, 则设置预定义的系统行为
        if (StringUtils.isNumeric(code)) {

            if (BaseConstants.CODE_SEL_CHANNEL.equals(code)) {
                result.setRedirectTo("/channel.html");
            } else {
                MessageBean msgModel = messageService.getMessage(lang, code);
                if (msgModel != null)
                    result.setMessage(msgModel, args);
                // 暂时未打开下部分代码, 因为现有的大部分情况
                // 都是没有预定义信息, 而是直接返回信息
                // else
                //    result.setMessage(String.format("this msg_code [%s(%s)] is not exists !!", code, lang));
            }
        }

        return isAjax(request) ? textView(result) : jsonView(result);
    }

    private boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("x_requested_with");
        return "xmlhttprequest".equals(header);
    }

    private String getUnknownExceptionMessage(Exception exception) {

        String msg = exception.getMessage();

        if (isDebug()) msg = "Server Exception\nException Name: " + exception.getClass().getName() +
                "\nException Message: " + exception.getMessage() +
                "\nTrace Message 1: " + exception.getStackTrace()[0].toString() +
                "\nTrace Message 2: " + CommonUtil.getExceptionSimpleContent(exception);

        if (StringUtils.isEmpty(msg))
            msg = BaseConstants.UNKNOWN_ERROR_MSG;

        return msg;
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

    private void log(HttpServletRequest request, Exception exception) {

        if (exception instanceof BusinessException)
            return;

        if (exception instanceof SystemException) {
            SystemException systemException = (SystemException) exception;
            if (systemException.getCode() != null) {
                switch (systemException.getCode()) {
                    case BaseConstants.CODE_SEL_CHANNEL:
                    case BaseConstants.MSG_TIMEOUT:
                    case BaseConstants.MSG_DENIED:
                        $info(String.format("Break by SystemException with [ %s ]", exception.getMessage()));
                        return;
                }
            }
        }

        String url = request.getRequestURI();
        String simpleMessage = exception.getMessage();
        if (StringUtils.isEmpty(simpleMessage)) simpleMessage = exception.toString();
        $error(String.format("%s => %s", url, simpleMessage), exception);
    }

    private ModelAndView jsonView(AjaxResponse ajaxResponse) {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        ModelAndView mav = new ModelAndView(jsonView);
        mav.addObject("code", ajaxResponse.getCode());
        mav.addObject("data", ajaxResponse.getData());
        mav.addObject("displayType", ajaxResponse.getDisplayType());
        mav.addObject("message", ajaxResponse.getMessage());
        mav.addObject("redirectTo", ajaxResponse.getRedirectTo());
        return mav;
    }

    private ModelAndView textView(AjaxResponse ajaxResponse) {

        String json = JacksonUtil.bean2Json(ajaxResponse);

        return new ModelAndView(new View() {
            @Override
            public String getContentType() {
                return "text/html";
            }

            @Override
            public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
                httpServletResponse.getWriter().write(json);
            }
        });
    }
}

