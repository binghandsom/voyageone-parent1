package com.voyageone.security.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.security.bean.ComChannelPermissionBean;
import com.voyageone.security.dao.ComLoginLogDao;
import com.voyageone.security.dao.ComUserConfigDao;
import com.voyageone.security.dao.ComUserDao;
import com.voyageone.security.daoext.ComUserDaoExt;
import com.voyageone.security.model.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by Ethan Shi on 2016-08-12. */


@Service
public class ComUserService {

    // 密码加密固定盐值
    public  static  final String MD5_FIX_SALT = "crypto.voyageone.la";
    // 密码加密散列加密次数
    public  static  final int MD5_HASHITERATIONS = 4;

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
    public void login(String account, String password)
    {
        Subject user = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(account, password);

        try {
            user.login(token);
        }catch (LockedAccountException lae) {
            token.clear();
            throw new BusinessException("A003","user locked!", lae);
        } catch (ExcessiveAttemptsException e) {
            token.clear();
            throw new BusinessException("A004", "too many fails, user will be locked for 10 minutes.", e);

        } catch (AuthenticationException e) {
            token.clear();
            //尝试用老密码登录
            ComUserModel userModel = new ComUserModel();
            userModel.setUserAccount(account);
            userModel =comUserDao.selectOne(userModel);
            if(userModel!= null) {
                String cryptoPassword = new Md5Hash(password, account + MD5_FIX_SALT, MD5_HASHITERATIONS).toHex();
                if (!userModel.getPassword().equals(cryptoPassword)) {
                    throw new BusinessException("A005", "authentication failed.", e);
                }
            }
            else
            {
                throw new BusinessException("A005", "authentication failed.", e);
            }
        }

        //如果user的密码不是自己设的，则强制要求修改密码
        ComUserModel userModel = new ComUserModel();
        userModel.setUserAccount(account);
        userModel =comUserDao.selectOne(userModel);

        if(!userModel.getModifier().equals(account))
        {
            throw new BusinessException("A006", "need change password.", null);
        }

        ComLoginLogModel model = new ComLoginLogModel();
        model.setApplication("admin");
        model.setCreater(account);
        model.setIp(user.getSession().getHost());

        comLoginLogDao.insert(model);
    }


    /**
     * CMS的channel页面ViewModle
     *
     * @param userId
     * @return
     */
    public List<ComChannelPermissionBean> getPermissionCompany(Integer  userId) {
        List<ComChannelPermissionBean>  list =  comUserDaoExt.selectPermissionChannel(userId);
        return  list;
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
        List<ComUserConfigModel>  list =  comUserConfigDao.selectList(map);
        return list;
    }



    public List<String> getPermissionUrls(Integer userId, String channelId, String application) {
        return comUserDaoExt.getPermissionUrls(userId, channelId, application);
    }


    public List<ComRoleModel> selectRolesByUserId(Integer userId)
    {
        return comUserDaoExt.selectRolesByUserId(userId);
    }

}
