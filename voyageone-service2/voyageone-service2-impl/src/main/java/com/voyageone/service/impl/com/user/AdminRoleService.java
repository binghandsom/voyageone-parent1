package com.voyageone.service.impl.com.user;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.user.ComResRoleDao;
import com.voyageone.service.dao.user.ComResourceDao;
import com.voyageone.service.dao.user.ComRoleConfigDao;
import com.voyageone.service.dao.user.ComRoleDao;
import com.voyageone.service.model.user.ComResRoleModel;
import com.voyageone.service.model.user.ComResourceModel;
import com.voyageone.service.model.user.ComRoleConfigModel;
import com.voyageone.service.model.user.ComRoleModel;
import com.voyageone.security.service.ComUserService;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.service.bean.com.AdminRoleBean;
import com.voyageone.service.daoext.com.WmsMtStoreDaoExt;
import com.voyageone.service.daoext.core.AdminResourceDaoExt;
import com.voyageone.service.daoext.core.AdminRoleDaoExt;
import com.voyageone.service.daoext.core.AdminUserDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.bean.com.PaginationBean;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016-08-18.
 */

@Service
public class AdminRoleService extends BaseService {


    @Autowired
    AdminRoleDaoExt adminRoleDaoExt;

    @Autowired
    ComRoleDao comRoleDao;

    @Autowired
    WmsMtStoreDaoExt wmsMtStoreDaoExt;

    @Autowired
    ComRoleConfigDao comRoleConfigDao;

    @Autowired
    ComResourceDao comResourceDao;

    @Autowired
    ComResRoleDao comResRoleDao;

    @Autowired
    AdminResourceDaoExt adminResourceDaoExt;

    @Autowired
    AdminUserService adminUserService;

    @Autowired
    ComUserService comUserService;

    @Autowired
    AdminUserDaoExt adminUserDaoExt;

    public List<Map<String, Object>> getAllRole() {
        List<ComRoleModel> roleList = comRoleDao.selectList(new HashMap<String, Object>() {{
            put("active", 1);
        }});

        List<Map<String, Object>> retList = new ArrayList<>();


        roleList.forEach(w -> retList.add(new HashMap<String, Object>()  {{put("roleId", w.getId());  put("roleName", w.getRoleName()); } } ) );

        return retList;
    }

    /**
     * 检索角色
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PaginationBean<AdminRoleBean> searchRole(Integer pageNum, Integer pageSize) {
        return searchRole(null, null, null, null, null, null, pageNum, pageSize);
    }

    /**
     * 检索角色
     *
     * @param roleName
     * @param roleType
     * @param channelId
     * @param active
     * @param storeId
     * @param application
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PaginationBean<AdminRoleBean> searchRole(String roleName, Integer roleType, String channelId,
                                                    Integer active, Integer storeId, String application, Integer pageNum, Integer pageSize) {


        PaginationBean<AdminRoleBean> paginationBean = new PaginationBean<>();
        // 设置查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roleName", roleName);
        params.put("roleType", roleType);
        params.put("channelId", channelId);
        params.put("storeId", storeId);
        params.put("active", active);
        params.put("application", application);

        // 判断查询结果是否分页
        boolean needPage = false;
        if (pageNum != null && pageSize != null) {
            needPage = true;
            paginationBean.setCount(adminRoleDaoExt.selectRoleCount(params));
            params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
        }

        List<AdminRoleBean> list = adminRoleDaoExt.selectRoleByPage(params);
        if (!needPage) {
            paginationBean.setCount(list.size());
        }
        paginationBean.setResult(list);
        return paginationBean;

    }


    /**
     * 修改用户
     *
     * @param model
     * @param appList
     * @param channelIds
     * @param storeIds
     * @param allChannel
     * @param allStore
     */
    @VOTransactional
    public void updateRole(ComRoleModel model, List<String> appList, List<String> channelIds, List<Integer> storeIds, String allChannel, String allStore) {

        checkParams(allChannel, allStore, appList, channelIds, storeIds);


        Integer roleId = model.getId();

        //修改授权渠道
        if ("1".equals(allChannel)) {
            insertIgnoreConfig(roleId, "channel_id", "ALL");
        } else {
            updateConfig(channelIds, roleId, "channel_id");
        }
        //修改授权仓库
        if ("1".equals(allStore)) {
            //查找是否有ALL的记录,没有则新增
            insertIgnoreConfig(roleId, "store_id", "ALL");
        } else {
            List<String> strStores = new ArrayList<>();
            for(Integer i : storeIds)
            {
                strStores.add(i.toString());
            }
            updateConfig(strStores, roleId, "store_id");
        }
        //修改授权系统

        Map map = new HashMap<>();
        //查找resource表中的res_id
        map.clear();
        map.put("resType", 0);
        List<ComResourceModel> resList = comResourceDao.selectList(map);
        //查找老的app列表
        List<Map<String, Object>> oldApps = adminRoleDaoExt.selectAppByRole(roleId);

        for (Map old : oldApps)
        {
            String strOld = old.get("application").toString();

            if(appList.stream().filter(w->w.equals(strOld)).count() == 0)
            {
                //删除系统权限
                Integer oldResId = Integer.valueOf(old.get("res_id").toString());

                ComResRoleModel rrModel = new ComResRoleModel();
                rrModel.setRoleId(roleId);
                rrModel.setResId(oldResId);
                List<ComResRoleModel>  list = comResRoleDao.selectList(rrModel);

                for(ComResRoleModel rr : list)
                {
                    comResRoleDao.delete(rr.getId());
                }
                ComRoleConfigModel rc = new ComRoleConfigModel();
                rc.setRoleId(roleId);
                rc.setCfgName("all_permission");
                rc.setCfgVal1(strOld);
                List<ComRoleConfigModel>  configs = comRoleConfigDao.selectList(rc);

                for(ComRoleConfigModel config : configs)
                {
                    comRoleConfigDao.delete(config.getId());
                }
            }
        }


        for (String app : appList) {
            if (resList.stream().filter(w -> w.getApplication().equals(app)).count() > 0) {
                //找到resId
                ComResourceModel res = resList.stream().filter(w -> w.getApplication().equals(app)).findFirst().get();

                if(oldApps.stream().filter(w ->w.get("application").toString().equals(app)).count() == 0) {

                    ComResRoleModel rrModel = new ComResRoleModel();
                    rrModel.setRoleId(roleId);
                    rrModel.setResId(res.getId());
                    rrModel.setCreater(model.getCreater());
                    comResRoleDao.insert(rrModel);
                }
            }
        }

        //修改Role
        ComRoleModel role = comRoleDao.select(roleId);
        role.setActive(model.getActive());
        role.setRoleType(model.getRoleType());
        role.setDescription(model.getDescription());
        role.setModifier(model.getModifier());
        role.setModified(new Date());
        comRoleDao.update(role);
    }

    private void insertIgnoreConfig(Integer roleId, String configName, String configValue) {
        //查找是否有ALL的记录,没有则新增
        ComRoleConfigModel comRoleConfigModel = new ComRoleConfigModel();
        comRoleConfigModel.setRoleId(roleId);
        comRoleConfigModel.setCfgName(configName);
        comRoleConfigModel.setCfgVal1(configValue);
        if (comRoleConfigDao.selectCount(comRoleConfigModel) == 0) {
            comRoleConfigDao.insert(comRoleConfigModel);
        }
    }

    private void insertConfig(Integer roleId, String configName, String configValue) {
        ComRoleConfigModel record = new ComRoleConfigModel();
        record.setRoleId(roleId);
        record.setCfgName(configName);
        record.setCfgVal1(configValue);
        comRoleConfigDao.insert(record);
    }

    private void updateConfig(List<String> values, Integer roleId, String configName) {
        //查找所有授权渠道的记录
        ComRoleConfigModel comRoleConfigModel = new ComRoleConfigModel();
        comRoleConfigModel.setRoleId(roleId);
        comRoleConfigModel.setCfgName(configName);
        List<ComRoleConfigModel> confList = comRoleConfigDao.selectList(comRoleConfigModel);

        List<String> addList = new ArrayList<>();
        List<String> deleteList = new ArrayList<>();
        List<String> remainList = new ArrayList<>();
        if (confList == null && confList.size() == 0) {
            addList = values;
        } else {
            List<String> conf = confList.stream().map(ComRoleConfigModel::getCfgVal1).collect(Collectors.toList());
            remainList = (List<String>) CollectionUtils.intersection(values, conf);
            addList = (List<String>) CollectionUtils.subtract(values, remainList);
            deleteList = (List<String>) CollectionUtils.subtract(conf, remainList);
        }


        //增加新项目
        for (String chnId : addList) {
            insertConfig(roleId, configName, chnId);
        }

        //删除老项目
        for (String chnId : deleteList) {
            Integer id = confList.stream().filter(w -> w.getCfgVal1().equals(chnId)).findFirst().get().getId();
            comRoleConfigDao.delete(id);
        }
    }

    private void checkParams(String allChannel, String allStore, List<String> appList, List<String> channelIds, List<Integer> storeIds) {
        //只有选wms的才需要选仓库
        Set<String> appSet = appList.stream().collect(Collectors.toSet());
        if (appSet.contains("wms")) {
            //检查channel和store的关联关系
            if ("0".equals(allChannel) && "0".equals(allStore)) {

                List<String> sIds = wmsMtStoreDaoExt.selectIdsByChannel(channelIds);

                for (Integer store : storeIds) {
                    if (sIds.stream().filter(w -> w.equals(store.toString())).count() == 0) {
                        throw new BusinessException("仓库和渠道不匹配。");
                    }
                }
            }
        } else if (storeIds.size() > 0 || "1".equals(allStore)) {
            throw new BusinessException("只有选WMS系统的角色才能选授权仓库。");
        }
    }

    public void addRole(ComRoleModel model, List<String> appList, List<String> channelIds, List<Integer> storeIds, String allChannel, String allStore) {
    	addRole(model, appList, channelIds, storeIds, allChannel, allStore, null);
    }

    @VOTransactional
    public void addRole(ComRoleModel model, List<String> appList, List<String> channelIds, List<Integer> storeIds, String allChannel, String allStore, String action) {
    	Integer srcRoleId = model.getId();
    	model.setId(null);
        //检查roleName唯一性
        Map map = new HashMap<>();
        map.put("roleName", model.getRoleName());

        if (comRoleDao.selectCount(map) > 0) {
            throw new BusinessException("角色名在系统中已存在。");
        }
        checkParams(allChannel, allStore, appList, channelIds, storeIds);

        //添加角色
        comRoleDao.insert(model);
        Integer roleId = model.getId();

        //添加授权渠道
        if ("1".equals(allChannel)) {
            insertConfig(roleId, "channel_id", "ALL");
        } else {
            for (String chnId : channelIds) {
                insertConfig(roleId, "channel_id", chnId);
            }
        }

        //添加授权仓库
        if ("1".equals(allStore)) {
            insertConfig(roleId, "store_id", "ALL");
        } else {
            for (Integer stId : storeIds) {
                insertConfig(roleId, "store_id", stId.toString());
            }
        }

        //添加授权系统
        //查找resource表中的res_id
        map.clear();
        map.put("resType", 0);
        List<ComResourceModel> resList = comResourceDao.selectList(map);
        for (String app : appList) {
            if (resList.stream().filter(w -> w.getApplication().equals(app)).count() > 0) {
                ComResourceModel res = resList.stream().filter(w -> w.getApplication().equals(app)).findFirst().get();
                ComResRoleModel rrModel = new ComResRoleModel();
                rrModel.setRoleId(roleId);
                rrModel.setResId(res.getId());
                rrModel.setCreater(model.getCreater());
                comResRoleDao.insert(rrModel);
            }
        }
        
        // 复制角色
        if ("copy".equals(action)) {
        	copyRoleAuth(srcRoleId, roleId);
        }
    }

    @VOTransactional
    public void deleteRole(List<Integer> ids, String username) {
        for (Integer id : ids) {
            ComRoleModel model = new ComRoleModel();
            model.setId(id);
            model.setActive(0);
            model.setModifier(username);
            if (!(comRoleDao.update(model) > 0)) {
                throw new BusinessException("禁用角色信息失败");
            }
        }

        List<String> users = adminUserDaoExt.selectUserByRoles(ids);

        for (String account: users) {
            comUserService.clearCachedAuthorizationInfo(account);
        }

    }

    @VOTransactional
    public void addAuth(List<Integer> roleIds, List<Integer> resIds, String username) {
        for (Integer roleId : roleIds) {
            //添加新项目
            for(Integer resId : resIds)
            {
                ComResRoleModel model = new ComResRoleModel();
                model.setRoleId(roleId);
                model.setResId(resId);

                if(comResRoleDao.selectCount(model) == 0)
                {
                    model.setCreater(username);
                    comResRoleDao.insert(model);
                }
            }
        }

        List<String> users = adminUserDaoExt.selectUserByRoles(roleIds);

        for (String account: users) {
            comUserService.clearCachedAuthorizationInfo(account);
        }
    }

    @VOTransactional
    public void removeAuth(List<Integer> roleIds, List<Integer> resIds, String username) {
        for (Integer roleId : roleIds) {
            //删除项目
            for(Integer resId : resIds)
            {
                ComResRoleModel model = new ComResRoleModel();
                model.setRoleId(roleId);
                model.setResId(resId);

                ComResRoleModel old = comResRoleDao.selectOne(model);

                if(old != null)
                {
                    comResRoleDao.delete(old.getId());
                }
            }
        }

        List<String> users =  adminUserDaoExt.selectUserByRoles(roleIds);

        for (String account: users) {
            comUserService.clearCachedAuthorizationInfo(account);
        }
    }



    @VOTransactional
    public void setAuth(List<String> apps, List<Integer> roleIds, List<Integer> resIds, Boolean hasAllAuth, String username) {

        //新的逻辑只能对单个application授权
        String app = apps.get(0);

        for (Integer roleId : roleIds) {
//            updateConfig(apps, roleId, "all_permission");

            ComRoleConfigModel comRoleConfigModel = new ComRoleConfigModel();
            comRoleConfigModel.setRoleId(roleId);
            comRoleConfigModel.setCfgName("all_permission");
            comRoleConfigModel.setCfgVal1(app);
            List<ComRoleConfigModel> confList = comRoleConfigDao.selectList(comRoleConfigModel);

            if(hasAllAuth) {
                if (confList == null || confList.size() == 0) {
                    comRoleConfigDao.insert(comRoleConfigModel);
                }
            }
            else
            {
                if (confList != null && confList.size() >0)
                {
                   for(ComRoleConfigModel conf : confList)
                   {
                       comRoleConfigDao.delete(conf.getId());
                   }
                }
            }


            //查询该角色在app下的所有权限
            List<ComResRoleModel> olds = adminResourceDaoExt.selectResRoleList(roleId, app);

            //删除不需要的项目
            for(ComResRoleModel old : olds )
            {
                if(resIds.stream().filter(w -> w.equals(old.getResId())).count() == 0)
                {
                    comResRoleDao.delete(old.getId());
                }
            }

            //添加新项目
            for(Integer resId : resIds)
            {
                ComResRoleModel model = new ComResRoleModel();
                model.setRoleId(roleId);
                model.setResId(resId);

                if(comResRoleDao.selectCount(model) == 0)
                {
                    model.setCreater(username);
                    comResRoleDao.insert(model);
                }
            }


            List<String> users =  adminUserDaoExt.selectUserByRoles(roleIds);

            for (String account: users) {
                comUserService.clearCachedAuthorizationInfo(account);
            }
        }
    }


    public List<Map<String, Object>> getAllPermConfig( List<Integer> roleIds) {

        List<Map> list = adminResourceDaoExt.selectAllPermConfig(roleIds);

        Integer roleCnt = roleIds.size();


        List<Map<String, Object>> all = adminUserService.getAllApp();

        for(Map map : all)
        {
            if(list.stream().filter(w -> w.get("application").equals(map.get("application"))).count() > 0)
            {
                if(list.stream().filter(w -> w.get("application").equals(map.get("application"))).count()  == roleCnt)
                {
                    map.put("selected", 1);
                }
                else
                {
                    map.put("selected", 2);
                }
            }
            else
            {
                map.put("selected", 0);
            }
        }

        return all;


    }


    public List<AdminResourceBean> getAuthByRoles( List<Integer> roleIds, String  app) {
        List<AdminResourceBean>  list = adminResourceDaoExt.selectResByRoles(roleIds, app);

        Integer roleCnt = roleIds.size();

        for(AdminResourceBean bean : list)
        {
            if(list.stream().filter(w -> w.getId().equals(bean.getId())).count() == roleCnt)
            {
                bean.setSelected(1);
            }
            else
            {
                bean.setSelected(2);
            }
        }

        //取所有的资源
        List<AdminResourceBean>  all = adminResourceDaoExt.selectRes(app);

        for(AdminResourceBean bean : all)
        {
            if(list.stream().filter(w -> w.getId().equals(bean.getId())).count() > 0)
            {
                AdminResourceBean one =list.stream().filter(w -> w.getId().equals(bean.getId())).findFirst().get();
                bean.setSelected(one.getSelected());
            }
        }
        return convert2Tree(all);
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
            if (node.getParentId().equals(root.getId())) {
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
     * 复制角色
     * @param srcRoldId 源角色ID
     * @param dstName 目标角色名
     */
    private void copyRoleAuth(Integer srcRoldId, Integer desRoldId)
    {
        ComResRoleModel rr = new ComResRoleModel();
        rr.setRoleId(srcRoldId);
        List<ComResRoleModel> srcList = comResRoleDao.selectList(rr);

        for(ComResRoleModel src : srcList)
        {
            ComResRoleModel dst = new ComResRoleModel();
            dst.setRoleId(desRoldId);
            dst.setResId(src.getResId());

            if(comResRoleDao.selectCount(dst) == 0)
            {
                comResRoleDao.insert(dst);
            }
        }
    }



}
