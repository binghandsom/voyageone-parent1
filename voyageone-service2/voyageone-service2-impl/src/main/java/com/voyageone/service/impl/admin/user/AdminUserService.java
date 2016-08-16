package com.voyageone.service.impl.admin.user;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.security.dao.ComUserDao;
import com.voyageone.security.dao.ComUserRoleDao;
import com.voyageone.security.model.ComUserModel;
import com.voyageone.security.model.ComUserRoleModel;
import com.voyageone.service.bean.admin.AdminUserBean;
import com.voyageone.service.daoext.admin.AdminUserDaoExt;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016-08-15.
 */

@Service
public class AdminUserService {

    @Autowired
    AdminUserDaoExt adminUserDaoExt;

    @Autowired
    ComUserDao comUserDao;

    @Autowired
    ComUserRoleDao comUserRoleDao;

    /**检索用户
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
    public PageModel<AdminUserBean> searchUserByPage(String userAccount, Integer active, Integer orgId,
                                                    String channelId,Integer storeId, Integer pageNum, Integer pageSize)
    {

        PageModel<AdminUserBean> pageModel = new PageModel<AdminUserBean>();

        // 设置查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userAccount", userAccount);
        params.put("active", active);
        params.put("channelId", channelId);
        params.put("storeId", storeId);
        params.put("orgId", orgId);
        // 判断查询结果是否分页
        if (pageNum != null && pageSize != null) {
            pageModel.setCount(adminUserDaoExt.selectUserCount(params));
            params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
        }

        List<AdminUserBean> list = adminUserDaoExt.selectUserByPage(params);
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

        List<ComUserModel> userList= comUserDao.selectList(map);

        if(userList.size() >0) {
            if(userList.stream().filter(w -> w.getId() != model.getId()).count() > 0)
            {
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
        List<ComUserRoleModel> relationList =  comUserRoleDao.selectList(rMap);

        List<ComUserRoleModel> oldList = new ArrayList<>();
        List<String> oldIdList = new ArrayList<>();
        if(relationList.size() > 0) {


            String[] roleIds = model.getRoleId().split(",");
            List<String> roleIdList = Arrays.asList(roleIds);
            for (String roleId : roleIds) {
                ComUserRoleModel oldModel = relationList.stream().filter(w -> w.getRoleId() == Integer.valueOf(roleId)).findFirst().get();
                oldList.add(oldModel);
            }
        }

        if(oldList.size() > 0) {
            oldIdList = oldList.stream().map(w -> w.getRoleId().toString()).collect(Collectors.toList());
        }

        //需要删除的项目
        List<ComUserRoleModel> deleteList = (List<ComUserRoleModel>) CollectionUtils.subtract(relationList , oldList);
        //需要新增的项目
        String[] roleIds = model.getRoleId().split(",");
        List<String> roleIdList = Arrays.asList(roleIds);
        List<String> addList =  (List<String>) CollectionUtils.subtract(roleIdList , oldIdList);

        for (ComUserRoleModel deleteModel  : deleteList)
        {
            comUserRoleDao.delete(deleteModel.getId());
        }

        for (ComUserRoleModel oldModel  : oldList)
        {
            oldModel.setModifier(username);
            oldModel.setModified(new Date());
            comUserRoleDao.update(oldModel);
        }

        for (String roleId  : addList)
        {
            ComUserRoleModel rModel =  new ComUserRoleModel();
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
        comUserDao.insert(model) ;

        Integer userId = model.getId();


        //查询该user的所有Role
        Map rMap = new HashMap<>();
        rMap.put("userId", userId);
        //如果不为空，则全部是垃圾数据
        List<ComUserRoleModel> deleteList =  comUserRoleDao.selectList(rMap);

        for (ComUserRoleModel deleteModel  : deleteList)
        {
            comUserRoleDao.delete(deleteModel.getId());
        }

        String[] addList = model.getRoleId().split(",");
        for (String roleId  : addList)
        {
            ComUserRoleModel rModel =  new ComUserRoleModel();
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






}
