package com.voyageone.service.impl.com.user;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.mail.Mail;
import com.voyageone.security.dao.ComResourceDao;
import com.voyageone.security.dao.ComUserDao;
import com.voyageone.security.dao.ComUserRoleDao;
import com.voyageone.security.dao.ComUserTokenDao;
import com.voyageone.security.model.ComResourceModel;
import com.voyageone.security.model.ComUserModel;
import com.voyageone.security.model.ComUserRoleModel;
import com.voyageone.security.model.ComUserTokenModel;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.service.bean.com.AdminUserBean;
import com.voyageone.service.dao.com.*;
import com.voyageone.service.daoext.core.AdminResourceDaoExt;
import com.voyageone.service.daoext.core.AdminUserDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.*;
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

    @Autowired
    CtUserDao ctUserDao;

    @Autowired
    CtModuleDao ctModuleDao;

    @Autowired
    ComResourceDao comResourceDao;

    @Autowired
    CtActionDao ctActionDao;

    @Autowired
    CtControllerDao ctControllerDao;
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
        user.setModifier(model.getUserAccount());
        user.setModified(new Date());
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

    public List<AdminResourceBean>  showUserAuth(String userAccount, String application) {
        Map<String, Object> map = new HashMap<>();
        map.put("userAccount", userAccount);

        ComUserModel user = comUserDao.selectOne(map);
        if (user == null) {
            throw new BusinessException("用户不存在。");
        }

        map.put("application", application);

        List<AdminResourceBean> resList = adminResourceDaoExt.selectResByUser(map);

        return convert2Tree(resList);
    }



    public List<Map<String, Object>>  getAllApp()
    {
        List<Map<String, Object>> list = adminUserDaoExt.selectAllApp();
        return list;
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

    /**
     * 将资源列转成一组树
     */
    private List<AdminResourceBean> convert2Tree(List<AdminResourceBean> resList) {
        List<AdminResourceBean> roots = findRoots(resList);
        List<AdminResourceBean> notRoots = (List<AdminResourceBean>) CollectionUtils.subtract(resList, roots);
        for (AdminResourceBean root : roots) {
            List<AdminResourceBean> children = findChildren(root, notRoots);
            root.setChildren(children);
        }
        return roots;
    }

    /**
     * 查找所有根节点
     */
    private List<AdminResourceBean> findRoots(List<AdminResourceBean> allNodes) {
        List<AdminResourceBean> results = new ArrayList<>();
        for (AdminResourceBean node : allNodes) {
            if (node.getParentId() == 0) {
                results.add(node);
            }
        }
        return results;
    }


    /**
     * 查找所有子节点
     */
    private List<AdminResourceBean> findChildren(AdminResourceBean root, List<AdminResourceBean> allNodes) {
        List<AdminResourceBean> children = new ArrayList<>();

        for (AdminResourceBean node : allNodes) {
            if (node.getParentId() == root.getId()) {
                children.add(node);
            }
        }
        root.setChildren(children);

        List<AdminResourceBean> notChildren = (List<AdminResourceBean>) CollectionUtils.subtract(allNodes, children);

        for (AdminResourceBean child : children) {
            List<AdminResourceBean> tmpChildren = findChildren(child, notChildren);
            child.setChildren(tmpChildren);
        }
        return children;
    }


    /**
     * 迁移旧系统的User
     *
     */
    @VOTransactional
    public void moveUser()
    {
        CtUserModel query = new CtUserModel();
//        query.setActive(true);

        List<CtUserModel> allOldUser = ctUserDao.selectList(query);

        for(CtUserModel ct : allOldUser)
        {
            ComUserModel model = new ComUserModel();
            model.setId(ct.getId());
            model.setUserAccount(ct.getUsername());
            model.setUserName(ct.getFirstName() + ct.getLastName());
            model.setPassword(ct.getPassword());
            model.setEmail(ct.getEmail());
            model.setActive(ct.getActive() ? 1 : 0);
            model.setCredentialSalt("");
            model.setOrgId(1);
            model.setIsSuperuser(ct.getIsSuperuser());
            model.setCreater(ct.getUsername());
            model.setCompanyId(ct.getCompanyId());
            adminUserDaoExt.insert(model);
        }
    }

    @VOTransactional
    public void moveApplication()
    {
        CtApplicationModel query = new CtApplicationModel();
        query.setActive(true);
        query.setShowInMenu(true);

        //添加顶级资源
        List<CtApplicationModel> allApps = ctApplicationDao.selectList(query);
        for(CtApplicationModel app : allApps)
        {
            ComResourceModel  res = new ComResourceModel();
            res.setOriginId(app.getId());
            res.setResType(0);
            res.setActive(1);
            res.setResName(app.getMenuTitle());
            res.setResKey(app.getApplication());
            res.setParentId(0);
            res.setApplication(app.getApplication());
            res.setWeight(app.getOrderBy());
            res.setResUrl(app.getDefaultUrl());
            res.setShowInMenu(app.getShowInMenu());
            res.setDescription(app.getDescription());
            res.setOriginTable("ct_application");
            res.setOriginName(app.getApplication());
            res.setMenuTitle(app.getMenuTitle());
            adminResService.addRes(res);
        }

        //添加菜单资源
        CtModuleModel module = new CtModuleModel();
//        module.setActive(true);

        List<CtModuleModel> allModule = ctModuleDao.selectList(module);

        for(CtModuleModel model : allModule)
        {
            ComResourceModel  res = new ComResourceModel();
            res.setOriginId(model.getId());
            res.setResType(1);
            res.setActive(model.getActive() ? 1 : 0);

            res.setWeight(model.getOrderBy());
            res.setResUrl(model.getDefaultUrl());
            res.setShowInMenu(model.getShowInMenu());
            res.setDescription(model.getDescription());
            res.setOriginName(model.getModule());

            ComResourceModel app = new ComResourceModel();
            app.setOriginId(model.getApplicationId());
            app.setResType(0);
            app.setActive(1);
            app = comResourceDao.selectOne(app);

            res.setResKey(app.getResKey() + "_" + model.getModule());
//            res.setResName(StringUtils.isEmpty(model.getMenuTitle()) ? res.getResKey().toUpperCase() : model.getMenuTitle().toUpperCase());
            res.setResName(res.getResKey().toUpperCase());
            res.setApplication(app.getApplication());
            res.setParentId(app.getId());
            res.setOriginTable("ct_module");
            res.setMenuTitle(model.getMenuTitle());
            adminResService.addRes(res);
        }

        //添加菜单资源
        CtControllerModel controller = new CtControllerModel();
//        controller.setActive(true);

        List<CtControllerModel> allControllers = ctControllerDao.selectList(controller);

        for(CtControllerModel model : allControllers)
        {
            ComResourceModel  res = new ComResourceModel();
            res.setOriginId(model.getId());
            res.setResType(1);
            res.setActive(model.getActive() ? 1 : 0);
            res.setWeight(model.getOrderBy());
            res.setResUrl(model.getDefaultUrl());
            res.setShowInMenu(model.getShowInMenu());
            res.setDescription(model.getDescription());
            res.setOriginName(model.getController());

            ComResourceModel module1 = new ComResourceModel();
            module1.setOriginId(model.getModuleId());
            module1.setResType(1);
//            module1.setActive(1);
            module1.setOriginTable("ct_module");
            module1 = comResourceDao.selectOne(module1);

            ComResourceModel app = comResourceDao.select(module1.getParentId());

            if(app != null) {
                res.setResKey(module1.getResKey() + "_" + model.getController());
//            res.setResName(StringUtils.isEmpty(model.getMenuTitle()) ? res.getResKey().toUpperCase() : module1.getResName() + "_" + model.getMenuTitle().toUpperCase());
                res.setResName(res.getResKey().toUpperCase());
                res.setApplication(app.getApplication());
                res.setParentId(module1.getId());
                res.setOriginTable("ct_controller");
                res.setMenuTitle(model.getMenuTitle());
                adminResService.addRes(res);
            }
        }

        //添加Action资源
        CtActionModel action = new CtActionModel();
//        controller.setActive(true);

        List<CtActionModel> allActions = ctActionDao.selectList(action);

        for(CtActionModel model : allActions)
        {
            ComResourceModel  res = new ComResourceModel();
            res.setOriginId(model.getId());
            res.setResType(2);
            res.setActive(model.getActive() ? 1 : 0);
            res.setWeight(model.getOrderBy());
            res.setDescription(model.getDescription());
            res.setOriginName(model.getName());

            ComResourceModel controller1 = new ComResourceModel();
            controller1.setOriginId(model.getControllerId());
            controller1.setResType(1);
//            controller1.setActive(1);
            controller1.setOriginTable("ct_controller");
            controller1 = comResourceDao.selectOne(controller1);

            ComResourceModel module1 = comResourceDao.select(controller1.getParentId());

            if(module1 != null) {
                ComResourceModel app = comResourceDao.select(module1.getParentId());


                res.setResUrl(controller1.getResUrl() + "/" + model.getName());
                res.setShowInMenu(false);

                res.setResKey(controller1.getResKey() + "_" + model.getName());
                res.setResName(controller1.getResName() + "_" + model.getName().toUpperCase());
                res.setApplication(app.getApplication());
                res.setParentId(controller1.getId());
                res.setOriginTable("ct_action");
                res.setMenuTitle("");
                adminResService.addRes(res);
            }
        }
    }







}
