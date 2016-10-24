package com.voyageone.security.shiro;


import com.voyageone.security.dao.*;
import com.voyageone.security.daoext.ComUserDaoExt;
import com.voyageone.security.model.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义Realm,进行数据源配置
 *
 */
public class MyRealm extends AuthorizingRealm {

	@Autowired
	private ComUserDao comUserDao;


//	@Autowired
//	private ComRoleConfigDao comRoleConfigDao;
//
//	@Autowired
//	private ComResourceDao comResourceDao;
//
//
	@Autowired
	private ComUserDaoExt comUserDaoExt;

//	@Autowired
//	private ViewResUserCompanyDao viewResUserCompanyDao;

	/**
	 * 只有需要验证权限时才会调用, 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		String loginName = SecurityUtils.getSubject().getPrincipal().toString();
		if (loginName != null) {

			Session  session = SecurityUtils.getSubject().getSession();

			Integer userId  = Integer.valueOf(session.getAttribute("userId").toString());
			//CMS是需要channel的,ADMIN不需要
			String application = "admin";
			Object objApp = session.getAttribute("application");

			if(objApp != null)
			{
				application = objApp.toString();
			}

			String channelId = null;
			Object objChannelId  = session.getAttribute("channelId");

			if(objChannelId != null)
			{
				channelId = objChannelId.toString();
			}

			
			Map queryMap =new HashMap<String, Object>();
			queryMap.put("userId", userId);
			queryMap.put("channelId", channelId);
			queryMap.put("application", application);
//			queryMap.put("res_type", 2);
			List<ViewResUserCompanyModel> resources = comUserDaoExt.selectAction(queryMap);

			// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

//			// 用户的角色对应的所有权限，如果只使用角色定义访问权限
//			for (ComResourceModel res : resList) {
//				info.addStringPermission(res.getResKey());
//			}

			for (ViewResUserCompanyModel res : resources) {
				info.addStringPermission(res.getResKey());
			}


			return info;
		}
		return null;
	}

	/**
	 * 认证回调函数,登录时调用
	 * 首先根据传入的用户名获取User信息；然后如果user为空，那么抛出没找到帐号异常UnknownAccountException；
	 * 如果user找到但锁定了抛出锁定异常LockedAccountException；最后生成AuthenticationInfo信息，
	 * 交给间接父类AuthenticatingRealm使用CredentialsMatcher进行判断密码是否匹配，
	 * 如果不匹配将抛出密码错误异常IncorrectCredentialsException；
	 * 另外如果密码重试此处太多将抛出超出重试次数异常ExcessiveAttemptsException；
	 * 在组装SimpleAuthenticationInfo信息时， 需要传入：身份信息（用户名）、凭据（密文密码）、盐（username+salt），
	 * CredentialsMatcher使用盐加密传入的明文密码和此处的密文密码进行匹配。
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();

		HashMap<String, Object> userFormMap = new HashMap();
		userFormMap.put("userAccount", username);
		ComUserModel userModel = comUserDao.selectOne(userFormMap);

		if (userModel != null) {
			if (userModel.getActive() == 2) {
				throw new LockedAccountException(); // 帐号锁定
			}
            else if(userModel.getActive() == 0)
            {
                throw new UnknownAccountException();// 没找到帐号
            }
			// 从数据库查询出来的账号名和密码,与用户输入的账号和密码对比
			// 当用户执行登录时,在方法处理上要实现user.login(token);
			// 然后会自动进入这个类进行认证
			// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, // 用户名
					userModel.getPassword(), // 密码
					ByteSource.Util.bytes(username + "" + userModel.getCredentialSalt()),// salt=username+salt
					getName() // realm name
			);
			// 把用户信息放在session里
//			Session session = SecurityUtils.getSubject().getSession();
//			session.setAttribute("comUserModel",userModel);

			return authenticationInfo;
		} else {
			throw new UnknownAccountException();// 没找到帐号
		}
	}
	/**
     * 更新用户授权信息缓存.
     */
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}
	/**
     * 更新用户信息缓存.
     */
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	/**
	 * 清除用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	/**
	 * 清除用户登录信息缓存.
	 */
	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}
	
	/**
	 * 清空用户所有缓存
	 */
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}


	/**
	 * 清空所有缓存
	 */
	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}