package com.voyageone.core.interceptor;

import com.voyageone.base.exception.SystemException;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.init.MessageHelp;
import com.voyageone.core.modelbean.UserSessionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 共通处理拦截器
 * token
 * 语言lang
 * companyId
 *
 * @author jacky
 */
public class CommonHandleInterceptor implements HandlerInterceptor {
    protected final Log logger = LogFactory.getLog(getClass());

    // 客户端选择companyId和服务端companyId不一致时判断处理的例外URL
    public static List<String> EXCLUDE_URL_LIST_COMPANYID = new ArrayList<>();
    // token不校验不生成的例外URL
    public static List<String> EXCLUDE_URL_LIST_TOKEN = new ArrayList<>();

    static {
        // 登陆
        EXCLUDE_URL_LIST_COMPANYID.add(UrlConstants.URL_CORE_ACCOUNT_LOGIN + "/doLogin");
        // 注销
        EXCLUDE_URL_LIST_COMPANYID.add(UrlConstants.URL_CORE_ACCOUNT_LOGIN + "/doLogout");
        // 页面刷新时获得当前用户信息
        EXCLUDE_URL_LIST_COMPANYID.add(UrlConstants.URL_CORE_ACCOUNT_LOGIN + "/doGetUserInfo");
        // 初始化（取得公司列表）
        EXCLUDE_URL_LIST_COMPANYID.add(UrlConstants.URL_CORE_ACCOUNT_COMPANY + "/doGetCompany");
        // 公司选择
        EXCLUDE_URL_LIST_COMPANYID.add(UrlConstants.URL_CORE_ACCOUNT_COMPANY + "/doSelectCompany");

        // 页面刷新时获得当前用户信息
        EXCLUDE_URL_LIST_TOKEN.add(UrlConstants.URL_CORE_ACCOUNT_LOGIN + "/doGetUserInfo");

        EXCLUDE_URL_LIST_TOKEN.add("/oms/common/service/doGetCode");

        EXCLUDE_URL_LIST_TOKEN.add(UrlConstants.URL_CORE_COMMON + "doGetCode");

        EXCLUDE_URL_LIST_TOKEN.add(UrlConstants.URL_CORE_ACCOUNT_LOGIN + "/doLogout");
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object arg, Exception e)
            throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 拦截mvc.xml配置的/*路径的请求
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 客户端请求url
        String url = request.getServletPath();
        logger.info(url);

        // 取得用户信息
        UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

        // session过期
        if (user == null || StringUtils.isEmpty(user.getUserName())) {
            AjaxResponseBean bean = new AjaxResponseBean();
            bean.setResult(false, MessageConstants.MESSAGE_CODE_300001,
                    MessageConstants.MESSAGE_TYPE_SESSION_TIMEOUT);
            bean.writeTo(request, response);

            logger.info(bean.toString());

            return false;

        } else {
            // 需校验token的url
            if (!EXCLUDE_URL_LIST_TOKEN.contains(url)) {

                // token验证
                String tokenClient = request.getHeader(CoreConstants.VOYAGEONE_USER_TOKEN);
                if(StringUtils.isEmpty(tokenClient)){
                    tokenClient = request.getParameter("token");
                }
                if (StringUtils.isEmpty(tokenClient)) {
                    logger.info("tokenClient getHeader is " + tokenClient);
                    tokenClient = (String) request.getAttribute(CoreConstants.VOYAGEONE_USER_TOKEN);

                    if (StringUtils.isEmpty(tokenClient)) {
                        logger.info("tokenClient getAttribute is " + tokenClient);

                        tokenClient = request.getParameter(CoreConstants.VOYAGEONE_USER_TOKEN);
                        logger.info("tokenClient getParameter is " + tokenClient);
                    }
                }
                // 客户端token为空
                if (StringUtils.isEmpty(tokenClient)) {
                    String msg = MessageHelp.getMessage(MessageConstants.MESSAGE_TYPE_EXCEPTION,
                            MessageConstants.MESSAGE_CODE_500002);
                    throw new SystemException(msg);
                }
                Object tokenServer = request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_TOKEN);
                // 服务端token为空
                if (tokenServer == null || StringUtils.isEmpty((String) tokenServer)) {
                    String msg = MessageHelp.getMessage(MessageConstants.MESSAGE_TYPE_EXCEPTION,
                            MessageConstants.MESSAGE_CODE_500003);
                    throw new SystemException(msg);
                }
                // 前后端token一致
                if (tokenClient.equals(tokenServer)) {
                    // 重新生成token
//                    String token = CommonUtil.generateToken();
//                    request.getSession(false).setAttribute(CoreConstants.VOYAGEONE_USER_TOKEN, token);

                } else {
                    AjaxResponseBean bean = new AjaxResponseBean();
                    bean.setResult(false, MessageConstants.MESSAGE_CODE_400002, MessageConstants.MESSAGE_TYPE_DIALOG);
                    bean.writeTo(request, response);

                    logger.info(bean.toString());

                    return false;
                }
            }
        }

        // 比较客户端和服务端companyId是否一致
        if (!EXCLUDE_URL_LIST_COMPANYID.contains(url)) {
            // 获得客户端选择companyId
            String companyIdClient = request.getHeader(CoreConstants.VOYAGEONE_USER_COMPANY);
            if (StringUtils.isEmpty(companyIdClient)) {
                companyIdClient = request.getParameter("companyId");
            }
            // 客户端companyId为空
            if (StringUtils.isEmpty(companyIdClient)) {
                String msg = MessageHelp.getMessage(MessageConstants.MESSAGE_TYPE_EXCEPTION,
                        MessageConstants.MESSAGE_CODE_500004);
                throw new SystemException(msg);
            }
            // 获得session中已选择companyId
            String companyIdServer = String.valueOf(user.getSelectCompany().getCompanyId());
            // 服务端companyId为空
            if (StringUtils.isEmpty(companyIdServer)) {
                String msg = MessageHelp.getMessage(MessageConstants.MESSAGE_TYPE_EXCEPTION,
                        MessageConstants.MESSAGE_CODE_500005);
                throw new SystemException(msg);
            }
            // 客户端和服务端companyId不一致
            if (!companyIdServer.equals(companyIdClient)) {
                AjaxResponseBean bean = new AjaxResponseBean();
                bean.setResult(false, MessageConstants.MESSAGE_CODE_400003, MessageConstants.MESSAGE_TYPE_DIALOG);
                bean.writeTo(request, response);

                logger.info(bean.toString());

                return false;
            }
        }

        // 获得客户端传递语言
        String lang = request.getHeader(CoreConstants.VOYAGEONE_USER_LANG);

        if (!StringUtils.isNullOrBlank2(lang)) {
            if ("en".equals(lang)) {
                lang = com.voyageone.common.Constants.LANGUAGE.EN;
            } else if ("cn".equals(lang) || "zh".equals(lang)) {
                lang = com.voyageone.common.Constants.LANGUAGE.CN;
            } else if ("jp".equals(lang)) {
                lang = com.voyageone.common.Constants.LANGUAGE.JP;
            } else {
                lang = com.voyageone.common.Constants.LANGUAGE.EN;
            }
        }

        if (StringUtils.isEmpty(lang))
            lang = com.voyageone.common.Constants.LANGUAGE.EN;

        request.getSession(false).setAttribute(CoreConstants.VOYAGEONE_USER_LANG, lang);

        return true;

    }

}
