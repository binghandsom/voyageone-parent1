package com.voyageone.security.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.spring.SpringContext;
import com.voyageone.service.bean.user.ComChannelPermissionBean;
import com.voyageone.security.shiro.MyRealm;
import com.voyageone.service.dao.user.ComLoginLogDao;
import com.voyageone.service.dao.user.ComUserConfigDao;
import com.voyageone.service.dao.user.ComUserDao;
import com.voyageone.service.daoext.user.ComUserDaoExt;
import com.voyageone.service.model.user.ComLoginLogModel;
import com.voyageone.service.model.user.ComRoleModel;
import com.voyageone.service.model.user.ComUserConfigModel;
import com.voyageone.service.model.user.ComUserModel;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016-08-12.
 */


@Service
public class ComUserService {

    @Autowired
    ComUserDaoExt comUserDaoExt;


    @Autowired
    ComUserConfigDao comUserConfigDao;

    @Autowired
    ComLoginLogDao comLoginLogDao;

    @Autowired
    ComUserDao comUserDao;


    /**
     * 登录，实际的验证逻辑在MyRealm中
     *
     * @param account
     * @param password
     */
    public ComUserModel login(String account, String password, String app) {
         logout();


        //放在这里稍有不妥，待测试
        try {
            clearCache(account);
        }
        catch (Exception e)
        {
            //do nothing
        }

        Subject user = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(account, password);

        try {
            user.login(token);
        } catch (LockedAccountException lae) {
            token.clear();
            throw new BusinessException("A003", "user locked!", lae);
        } catch (ExcessiveAttemptsException e) {
            token.clear();
            throw new BusinessException("A004", "too many fails, user will be locked for 10 minutes.", e);

        } catch (AuthenticationException e) {
            token.clear();
            throw new BusinessException("A005", "authentication failed.", e);
        }

        ComUserModel userModel = new ComUserModel();
        userModel.setUserAccount(account);
        userModel = comUserDao.selectOne(userModel);

        //如果用户没有该系统的权限，则拒绝登录
        List<String> apps = getAppsByUser(userModel.getId());

        if (!apps.contains(app)) {
            throw new BusinessException("A007", "access denied: " + app, null);
        }


        //如果user的密码不是自己设的，则强制要求修改密码-- 有问题,暂时注释掉
//        if(!userModel.getModifier().equals(account))
//        {
//            throw new BusinessException("A006", "need change password.", null);
//        }


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ComLoginLogModel model = new ComLoginLogModel();
        model.setApplication(app);
        model.setCreater(account);
        String clientIP = request.getHeader("x-real-ip");
        if (StringUtils.isEmpty(clientIP)) {
            clientIP = request.getRemoteAddr();
        }
        model.setIp(clientIP);

        comLoginLogDao.insert(model);

        return userModel;
    }


    public void logout() {
        try {
            SecurityUtils.getSubject().logout();
        }
        catch (Exception e)
        {
            //do nothing
            e.printStackTrace();
        }
    }


    /**
     * CMS的channel页面ViewModle
     *
     * @param userId
     * @return
     */
    public List<ComChannelPermissionBean> getPermissionCompany(Integer userId) {
        List<ComChannelPermissionBean> list = comUserDaoExt.selectPermissionChannel(userId);
        return list;
    }


    /**
     * 查找授权系统
     *
     * @param userId
     * @return
     */
    public List<String> getAppsByUser(Integer userId) {
        return comUserDaoExt.selectAppsByUser(userId);
    }


    /**
     * 读取UserConfig
     *
     * @param userId
     * @return
     */
    public List<ComUserConfigModel> getUserConfig(int userId) {

        Map map = new HashMap<>();
        map.put("userId", userId);
        map.put("active", 1);
        List<ComUserConfigModel> list = comUserConfigDao.selectList(map);
        return list;
    }


    @Deprecated
    public List<String> getPermissionUrls(Integer userId, String channelId, String application) {
        return comUserDaoExt.getPermissionUrls(userId, channelId, application);
    }


    /**
     * 获取用户的角色列表
     *
     * @param userId
     * @return
     */
    public List<ComRoleModel> selectRolesByUserId(Integer userId, String channelId) {
        return comUserDaoExt.selectRolesByUserId(userId, channelId);
    }


    /**
     * 清除授权缓存
     */
    public void clearCachedAuthorizationInfo() {
        Subject user = SecurityUtils.getSubject();
        if (user != null  || user.getPrincipals() != null){
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            myRealm.clearCachedAuthorizationInfo(user.getPrincipals());
        }
    }

    public void clearCachedAuthorizationInfo(String account) {

        if (account != null ){
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            SimplePrincipalCollection principals = new SimplePrincipalCollection(account, myRealm.getName());
            myRealm.clearCachedAuthorizationInfo(principals);
        }
    }

    public void clearAllCachedAuthorizationInfo() {
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            myRealm.clearAllCachedAuthorizationInfo();
    }


    /**
     * 清除登录缓存
     */
    public void clearCachedAuthenticationInfo() {
        Subject user = SecurityUtils.getSubject();
        if (user != null  || user.getPrincipals() != null){
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            myRealm.clearCachedAuthenticationInfo(user.getPrincipals());
        }
    }

    public void clearCachedAuthenticationInfo(String account) {

        if (account != null ){
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            SimplePrincipalCollection principals = new SimplePrincipalCollection(account, myRealm.getName());
            myRealm.clearCachedAuthenticationInfo(principals);
        }
    }

    public void clearAllCachedAuthenticationInfo() {
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            myRealm.clearAllCachedAuthenticationInfo();
    }

    /**
     * 清除所有缓存
     */
    public void clearCache() {
        Subject user = SecurityUtils.getSubject();
        if (user != null  || user.getPrincipals() != null){
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            myRealm.clearCache(user.getPrincipals());
        }
    }

    public void clearCache(String account) {

        if (account != null ){
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            MyRealm myRealm = (MyRealm) securityManager.getRealms().iterator().next();
            SimplePrincipalCollection principals = new SimplePrincipalCollection(account, myRealm.getName());
            myRealm.clearCache(principals);
        }
    }


    public List<String> selectChannels(Integer userId) {
        Map<String, Object> query = new HashMap<>();
        query.put("userId", userId);

        return comUserDaoExt.selectChannels(query);
    }


    public void reloadFilterChainDefinitionMap()
    {
        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringContext.getBean(ShiroFilterFactoryBean.class);

        Ini.Section chainDefinitionSectionMetaSource = ( Ini.Section)SpringContext.getBean("chainDefinitionSectionMetaSource");

        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;

            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 获取过滤管理器
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空初始权限配置
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

            // 重新构建生成
            try {
                shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinitionSectionMetaSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();

            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                manager.createChain(url, chainDefinition);
            }

        }

    }

}
