package com.voyageone.service.impl.com.user;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.mail.Mail;
import com.voyageone.security.dao.ComUserTokenDao;
import com.voyageone.security.model.ComUserTokenModel;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.security.dao.ComUserDao;
import com.voyageone.security.dao.ComUserRoleDao;
import com.voyageone.service.dao.com.CtApplicationDao;
import com.voyageone.service.daoext.core.AdminResourceDaoExt;
import com.voyageone.security.model.ComUserModel;
import com.voyageone.security.model.ComUserRoleModel;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.daoext.core.AdminUserDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.CtApplicationModel;
import com.voyageone.service.model.com.PageModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016-08-15.
 */

@Service
public class AdminUserService extends BaseService {

    @Autowired
    AdminUserDaoExt adminUserDaoExt;

    @Autowired
    ComUserDao comUserDao;

    @Autowired
    ComUserRoleDao comUserRoleDao;


    @Autowired
    AdminResourceDaoExt adminResourceDaoExt;

    @Autowired
    AdminResService adminResService;

    @Autowired
    CtApplicationDao ctApplicationDao;

    @Autowired
    ComUserTokenDao comUserTokenDao;

    /**
     * 检索用户
     *
     * @param userAccount
     * @param active
     * @param orgId
     * @param channelId
     * @param storeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageModel<AdminUserBean> searchUser(String userAccount, Integer active, Integer orgId, Integer roleId,
                                               String channelId, Integer storeId, String application, Integer pageNum, Integer pageSize) {

        PageModel<AdminUserBean> pageModel = new PageModel<>();

        // 设置查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userAccount", userAccount);
        params.put("active", active);
        params.put("channelId", "".equals(channelId) ? null : channelId);
        params.put("storeId", storeId);
        params.put("orgId", orgId);
        params.put("roleId", roleId);
        params.put("application", "".equals(application) ? null : application);

        boolean needPage = false;

        // 判断查询结果是否分页
        if (pageNum != null && pageSize != null) {
            needPage = true;
            pageModel.setCount(adminUserDaoExt.selectUserCount(params));
            params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).addSort("modified", Order.Direction.DESC).toMap();
        }
        else
        {
            params = MySqlPageHelper.build(params).addSort("modified", Order.Direction.DESC).toMap();
        }

        List<AdminUserBean> list = adminUserDaoExt.selectUserByPage(params);
        if(!needPage)
        {
            pageModel.setCount(list.size());
        }
        pageModel.setResult(list);
        return pageModel;
    }


    /**
     * 修改用户信息
     *
     * @param model
     * @param username
     */

    @VOTransactional
    public void updateUser(AdminUserBean model, String username) {

        ComUserModel user = comUserDao.select(model.getId());
        if (user == null) {
            throw new BusinessException("修改的用户信息不存在。");
        }

        Map map = new HashMap<>();

        map.put("email", model.getEmail());

        List<ComUserModel> userList = comUserDao.selectList(map);

        if (userList.size() > 0) {
            if (userList.stream().filter(w -> w.getId() != model.getId()).count() > 0) {
                throw new BusinessException("该用户名已在系统中注册。");
            }
        }

        // 保存用户信息
        model.setModifier(username);
        model.setModified(new Date());
        comUserDao.update(model);

        //查询该user的所有Role
        Map rMap = new HashMap<>();
        rMap.put("userId", model.getId());
        //如果不为空，则需要判断哪些需要删除
        List<ComUserRoleModel> relationList = comUserRoleDao.selectList(rMap);

        List<ComUserRoleModel> oldList = new ArrayList<>();
        List<String> oldIdList = new ArrayList<>();
        if (relationList.size() > 0) {


            String[] roleIds = model.getRoleId().split(",");
            List<String> roleIdList = Arrays.asList(roleIds);
            for (String roleId : roleIds) {
                ComUserRoleModel oldModel = relationList.stream().filter(w -> w.getRoleId() == Integer.valueOf(roleId)).findFirst().get();
                oldList.add(oldModel);
            }
        }

        if (oldList.size() > 0) {
            oldIdList = oldList.stream().map(w -> w.getRoleId().toString()).collect(Collectors.toList());
        }

        //需要删除的项目
        List<ComUserRoleModel> deleteList = (List<ComUserRoleModel>) CollectionUtils.subtract(relationList, oldList);
        //需要新增的项目
        String[] roleIds = model.getRoleId().split(",");
        List<String> roleIdList = Arrays.asList(roleIds);
        List<String> addList = (List<String>) CollectionUtils.subtract(roleIdList, oldIdList);

        for (ComUserRoleModel deleteModel : deleteList) {
            comUserRoleDao.delete(deleteModel.getId());
        }

        for (ComUserRoleModel oldModel : oldList) {
            oldModel.setModifier(username);
            oldModel.setModified(new Date());
            comUserRoleDao.update(oldModel);
        }

        for (String roleId : addList) {
            ComUserRoleModel rModel = new ComUserRoleModel();
            rModel.setRoleId(Integer.valueOf(roleId));
            rModel.setUserId(model.getId());
            rModel.setCreater(username);
            comUserRoleDao.insert(rModel);
        }
    }


    /**
     * 新增用户信息
     *
     * @param model
     * @param username
     */
    @VOTransactional
    public void addUser(AdminUserBean model, String username) {
        Map map = new HashMap<>();
        map.put("userAccount", model.getUserAccount());
        ComUserModel user = comUserDao.selectOne(map);

        if (user != null) {
            throw new BusinessException("添加的userAccount已存在。");
        }
        map.clear();

        map.put("email", model.getEmail());

        user = comUserDao.selectOne(map);

        if (user != null) {
            throw new BusinessException("添加的Email已存在。");
        }

        // 保存用户信息
        model.setCreater(username);
        encryptPassword(model);
        comUserDao.insert(model);

        Integer userId = model.getId();


        //查询该user的所有Role
        Map rMap = new HashMap<>();
        rMap.put("userId", userId);
        //如果不为空，则全部是垃圾数据
        List<ComUserRoleModel> deleteList = comUserRoleDao.selectList(rMap);

        for (ComUserRoleModel deleteModel : deleteList) {
            comUserRoleDao.delete(deleteModel.getId());
        }

        String[] addList = model.getRoleId().split(",");
        for (String roleId : addList) {
            ComUserRoleModel rModel = new ComUserRoleModel();
            rModel.setRoleId(Integer.valueOf(roleId));
            rModel.setUserId(userId);
            rModel.setCreater(username);
            comUserRoleDao.insert(rModel);
        }
    }

    /**
     * 软删除用户信息
     *
     * @param userIds
     * @param username
     */
    @VOTransactional
    public void deleteUser(List<Integer> userIds, String username) {
        for (Integer id : userIds) {
            ComUserModel model = new ComUserModel();
            model.setId(id);
            model.setActive(0);
            model.setModifier(username);
            if (!(comUserDao.update(model) > 0)) {
                throw new BusinessException("删除用户信息失败");
            }
        }
    }


    @VOTransactional
    public void restPass (String token, String pass) {
        ComUserTokenModel model = getComUserTokenModel(token);
        ComUserModel user = new ComUserModel();
        user.setUserAccount(model.getUserAccount());
        user = comUserDao.selectOne(user);

        user.setActive(1);
        user.setPassword(pass);
        encryptPassword(user);
        comUserDao.update(user);
        comUserTokenDao.delete(model.getId());
    }

    public Map getUserByToken(String token)
    {
        ComUserTokenModel model = getComUserTokenModel(token);

        Map<String, Object> result = new HashMap<>();
        result.put("userAccount",  model.getUserAccount());
        return  result;
    }

    private ComUserTokenModel getComUserTokenModel(String token) {
        if(StringUtils.isEmpty(token))
            throw new BusinessException("A007", "Bad Request Token.");

        ComUserTokenModel model = new ComUserTokenModel();
        model.setToken(token);
        model = comUserTokenDao.selectOne(model);

        if(model == null)
        {
            throw new BusinessException("A007", "Bad Request Token.");
        }

        Date modified = model.getModified();
        Date now = new Date();

        if (now.getTime() - modified.getTime() > 1000*3600*24)
        {
            comUserTokenDao.delete(model.getId());
            throw new BusinessException("A008", "Token Expired");
        }
        return model;
    }


    @VOTransactional
    public void restPass (List<Integer> userIds, String pass, String username) {
        for (Integer id : userIds) {
            restPass(id, pass, username);
        }
    }

    private void restPass(Integer userId, String pass, String username) {
        ComUserModel model = new ComUserModel();
        model.setId(userId);
        model.setPassword(pass);
        model.setModifier(username);
        encryptPassword(model);
        if (!(comUserDao.update(model) > 0)) {
            throw new BusinessException("重设密码失败");
        }
    }

    public void forgetPass(String account)  {
        ComUserModel model = new ComUserModel();
        model.setUserAccount(account);
        ComUserModel user = comUserDao.selectOne(model);
        if(user == null)
        {
            throw new BusinessException("该用户在系统中不存在");
        }

        String email = user.getEmail();
        String token = UUID.randomUUID().toString().replaceAll("-","");
        ComUserTokenModel tokenModel = new  ComUserTokenModel();
        tokenModel.setToken(token);
        tokenModel.setUserAccount(user.getUserAccount());

        comUserTokenDao.insert(tokenModel);

        try {
            Mail.send(email, "重置密码", getMailContent(model, token));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public Map<Integer, Object>  getAllApp()
    {
       List<CtApplicationModel> list = ctApplicationDao.selectList(new HashMap<String, Object>(){{put("active", 1);}});

        Map<Integer, Object> result = list.stream().collect(Collectors.toMap(CtApplicationModel::getId, CtApplicationModel::getApplication));

        return result;
    }


    private void encryptPassword(ComUserModel model)
    {
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        String salt = randomNumberGenerator.nextBytes().toHex();
        model.setCredentialSalt(salt);
        String newPassword = new SimpleHash("md5", model.getPassword(), ByteSource.Util.bytes(model.getUserAccount() + salt), 2).toHex();
        model.setPassword(newPassword);
    }

    private String getMailContent(ComUserModel model, String token)
    {
        StringBuffer sb = new StringBuffer();

        sb.append("<p>").append("亲爱的").append(model.getUserAccount()).append(":</p>");
        sb.append("<p>").append("我们收到您的密码重置请求，点击下面链接重置你的密码:").append("</p>");
        sb.append("<p>").append("<a href=").append("www.baidu.com/").append(token).append(">").append("www.baidu.com/").append(token).append("</a>").append("</p>");
        sb.append("<p>").append("</p>");
        sb.append("<p>").append("此邮件24小时内有效，如果你忽略这条信息，密码将不进行更改").append("</p>");

        return sb.toString();
    }







}
