package com.voyageone.core.controller;

import com.voyageone.common.Constants;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.UrlConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.formbean.InFormLoginUser;
import com.voyageone.core.init.MessageHelp;
import com.voyageone.core.modelbean.CompanyBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = UrlConstants.URL_CORE_ACCOUNT_LOGIN)
public class AccessController {
    /**
     * 日志
     */
    private static Log logger = LogFactory.getLog(AccessController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 登陆
     */
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public void doLogin(HttpServletRequest request, HttpServletResponse response,
                        @Valid @RequestBody InFormLoginUser bean, BindingResult bindingResult) {

        // 输入参数出力
        logger.info(bean.getUserName());

        // 输入参数校验失败
        if (bean.hasInputError(bean, bindingResult)) {

            logger.info(MessageHelp.getMessage(MessageConstants.MESSAGE_TYPE_VALIDATE, MessageConstants.MESSAGE_CODE_100001));

            bean.writeTo(request, response);

            logger.info(bean.getResponseBean().toString());

            return;
        }
        // 取得用户密码
        List<Map<String, Object>> userResult = loginService.getUserInfoByName(bean.getUserName());
        // 用户输入密码加密
        String cryptoPassword = new Md5Hash(bean.getPassword(),
                bean.getUserName().toLowerCase() + CoreConstants.MD5_FIX_SALT,
                CoreConstants.MD5_HASHITERATIONS).toHex();
        // 登陆验证成功
        if (userResult.size() > 0 && cryptoPassword.equals(userResult.get(0).get("password").toString())) {

            UserSessionBean user = new UserSessionBean();
            // 登陆标志设置
            user.setIsAuthenticated();
            // 设置用户名
            user.setUserName(bean.getUserName());
            // 设置用户ID
            user.setUserId((int) userResult.get(0).get("userId"));
            // 取得有权限的公司列表
            List<Map<String, Object>> companyList;
            // 是否是超级用户
            if ((boolean) userResult.get(0).get("is_superuser")) {
                user.setIsSuperUser();
                companyList = loginService.getCompanyInfoForSuperUser();
            } else {
                companyList = loginService.getCompanyInfoByName(bean.getUserName());
            }

            // 设置所属公司
            CompanyBean selfCompany = new CompanyBean();
            selfCompany.setCompanyId(Integer.parseInt(companyList.get(0).get("self_company_id").toString()));
            selfCompany.setCompanyName(companyList.get(0).get("self_company").toString());
            user.setSelfCompany(selfCompany);

            // 设置权限公司列表
            List<CompanyBean> permissionCompanies = new ArrayList<>();
            for (Map<String, Object> aCompanyList : companyList) {
                CompanyBean company = new CompanyBean();
                company.setCompanyId(Integer.parseInt(aCompanyList.get("company_id").toString()));
                company.setCompanyName(aCompanyList.get("company").toString());
                permissionCompanies.add(company);
            }

            // 设置默认选中公司
            user.setSelectCompany(permissionCompanies.get(0));
            user.setPermissionCompanies(permissionCompanies);

            //TODO 要修改时区
            // 用户登陆时间设定
            user.setLoginTime(DateTimeUtil.getNow());

            // 取得用户信息
            user = loginService.getUserInfo(user);

            // 设置时区
            user.setTimeZone(bean.getTimezone());

            // 设置返回结果
            bean.getResponseBean().setResult(true);
            
            Map<String, Object> resultInfoMap = new HashMap<>();
            
            // 当只有一个公司权限时，跳转到Menu页面；如果有多个公司权限，跳转到公司选择页面
            if (permissionCompanies.size() > 1) {
                bean.getResponseBean().setForward(UrlConstants.URL_CORE_ACCOUNT_COMPANY);
                resultInfoMap.put("companyId", "");
            } else if(permissionCompanies.size() == 1) {
                bean.getResponseBean().setForward(UrlConstants.URL_CORE_MENU_HOME);
                resultInfoMap.put("companyId", permissionCompanies.get(0).getCompanyId());
            } else {
            	bean.getResponseBean().setForward(UrlConstants.URL_CORE_MENU_HOME);
            }

            // 对应当只有一个公司权限时，跳转到Menu页面报错问题
            bean.getResponseBean().setResultInfo(resultInfoMap);
            // 用户信息放入session
            HttpSession session = request.getSession(true);
            session.setAttribute(CoreConstants.VOYAGEONE_USER_INFO, user);

            // 生成token
            String token = CommonUtil.generateToken();
            session.setAttribute(CoreConstants.VOYAGEONE_USER_TOKEN, token);

            // 登陆验证失败
        } else {
            bean.getResponseBean().setResult(false, MessageConstants.MESSAGE_CODE_200001,
                    MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
        }

        // 结果返回输出流
        bean.getResponseBean().writeTo(request, response);

        // 输出结果出力
        logger.debug(bean.getResponseBean().toString());
    }

    /**
     * logout
     */
    @RequestMapping(value = "/doLogout", method = RequestMethod.POST)
    public void doLogout(HttpServletRequest request, HttpServletResponse response) {
        // 返回登陆页面
        AjaxResponseBean responseBean = new AjaxResponseBean();
        responseBean.setResult(true);
        responseBean.setForward(UrlConstants.URL_CORE_ACCOUNT_LOGIN);
        responseBean.writeTo(request, response);

        // 让session失效
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * 页面刷新时获得当前用户信息
     *
     * @param request  请求
     * @param response 响应
     */
    @RequestMapping(value = "/doGetUserInfo", method = RequestMethod.POST)
    public void doGetUserInfo(HttpServletRequest request, HttpServletResponse response) {

        // 获得session中用户信息
        UserSessionBean user = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);

        // 选择语言
        String selLang = (String) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);

        // 获得该用户有权限公司列表
        List<CompanyBean> companyList = user.getPermissionCompanies();

        // 当前用户信息设定
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put("userName", user.getUserName());

        userInfoMap.put("isLogin", true);

        userInfoMap.put("selCompanyId", user.getSelectCompany().getCompanyId());

        userInfoMap.put("selLang", ("cn".equals(selLang) ? "zh" : selLang));

        userInfoMap.put("menuList", user.getUserMenuList());
        // 用户权限
        userInfoMap.put("pagePermissionList", user.getPagePermission());
        // 导航设置
        userInfoMap.put("navigationList", user.getPageNavi());

        userInfoMap.put("companyList", companyList);
        // 用户所属的所有仓库
        userInfoMap.put("stores", user.getStoreList());
        // 用户所属的所有店铺
        userInfoMap.put("shops", user.getShopList());
        // 用户所属的所有渠道
        userInfoMap.put("channels", getChannels(user));
        // 用户的硬件配置
        userInfoMap.put("hardware", user.getHardwareConfig());
        // 用户选择的当前系统
        userInfoMap.put("selMenu", user.getSelMenu());
        // 用户选择的当前Channel
        userInfoMap.put("selChannel", user.getSelChannel());
        // 可选语言列表
        userInfoMap.put("languageList", getLanguages());
        
        // CMS Product信息显示的用户自定义字段列表
        userInfoMap.put("cmsProductFields", user.getCmsProductfields());
        
        Map<String, Object> resultInfoMap = new HashMap<>();
        resultInfoMap.put("userInfo", userInfoMap);
        
        // 返回值设定
        AjaxResponseBean responseBean = new AjaxResponseBean();
        responseBean.setResult(true);
        responseBean.setResultInfo(resultInfoMap);
        responseBean.writeTo(request, response);

        // 输出结果出力
        logger.debug(responseBean.toString());
    }

    /**
     * 将 User 中完整的属性、权限配置，转换为简单的渠道信息
     * @param user UserSessionBean
     */
    private List<Map<String, String>> getChannels(UserSessionBean user) {
        HashMap<String, PermissionBean> ppMap = user.getPropertyPermissions();

        List<Map<String, String>> beanList = new ArrayList<>();

        for (String key: ppMap.keySet()) {
            // 取出 Bean，并获取有限的信息到 Map 中。
            // 放在 List 中，传回给前台使用

            PermissionBean bean = ppMap.get(key);

            Map<String, String> mapBean = new HashMap<>();

            mapBean.put("id", bean.getPropertyId());

            mapBean.put("name", bean.getPropertyName());

            beanList.add(mapBean);
        }

        return beanList;
    }
    


    /**
     * 将 User 中完整的属性、权限配置，转换为简单的渠道信息
     */
    private List<Map<String, String>> getLanguages() {

        List<Map<String, String>> beanList = new ArrayList<>();

        Map<String, String> mapBeanZh = new HashMap<>();

        mapBeanZh.put("id", "zh");

        mapBeanZh.put("name", "中文");

        beanList.add(mapBeanZh);

        Map<String, String> mapBeanEn = new HashMap<>();

        mapBeanEn.put("id", "en");

        mapBeanEn.put("name", "English");

        beanList.add(mapBeanEn);

        return beanList;
    }
}
