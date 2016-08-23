package com.voyageone.service.impl.com.user;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.StringUtils;
import com.voyageone.security.dao.ComUserDao;
import com.voyageone.security.model.ComUserModel;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.security.dao.ComResourceDao;
import com.voyageone.security.model.ComResourceModel;
import com.voyageone.service.daoext.core.AdminResourceDaoExt;
import com.voyageone.service.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016-08-17.
 */
@Service
public class AdminResService extends BaseService {


    @Autowired
    ComResourceDao comResourceDao;

    @Autowired
    ComUserDao comUserDao;

    @Autowired
    AdminResourceDaoExt adminResourceDaoExt;


    /**
     *查找菜单资源
     *
     * @param app
     * @return
     */
    public List<AdminResourceBean> searchRes(String app)
    {
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isNullOrBlank2(app))
        {
            map.put("application", app);
        }

        map = MySqlPageHelper.build(map).addSort("res_type", Order.Direction.ASC).addSort("weight", Order.Direction.ASC).toMap();

        List<ComResourceModel> list  = comResourceDao.selectList(map);
        List<AdminResourceBean> beanList = new ArrayList<>();

        for(ComResourceModel model : list)
        {
            AdminResourceBean bean = new AdminResourceBean() ;
            BeanUtils.copyProperties(model, bean);
            beanList.add(bean);
        }
        return convert2Tree(beanList);
    }


    /**
     *
     * @param model
     */
    public void addRes(ComResourceModel model)
    {
        //检查resKey唯一性，resName唯一性
        Map map = new HashMap<>();
        map.put("resKey" , model.getResKey());

        if(comResourceDao.selectCount(map) > 0)
        {
            throw new BusinessException("菜单Key在系统中已存在。");
        }
        map.clear();
        map.put("resName" , model.getResName());

        if(comResourceDao.selectCount(map) > 0)
        {
            throw new BusinessException("菜单名称在系统中已存在。");
        }

        ComResourceModel parent = comResourceDao.select(model.getParentId());

        map.put("parentId" , model.getParentId());

        if(model.getWeight() == null) {
            List<ComResourceModel> siblings = comResourceDao.selectList(map);
            int weight = siblings.stream().mapToInt(ComResourceModel::getWeight).max().getAsInt();
            model.setWeight(++weight);
        }
        model.setParentIds(parent.getParentIds() + "," + parent.getId());

        comResourceDao.insert(model);
    }



    public void updateRes(ComResourceModel model)
    {
        //检查resName唯一性
        Map map = new HashMap<>();
        map.put("resName" , model.getResName());

        if(comResourceDao.selectCount(map) > 0)
        {
            throw new BusinessException("菜单名称在系统中已存在。");
        }

        ComResourceModel parent = comResourceDao.select(model.getParentId());

        map.put("parentId" , model.getParentId());
        if(model.getWeight() == null) {
            List<ComResourceModel> siblings = comResourceDao.selectList(map);
            int weight = siblings.stream().mapToInt(ComResourceModel::getWeight).max().getAsInt();
            model.setWeight(++weight);
        }
        model.setParentIds(parent.getParentIds() + "," + parent.getId());

        comResourceDao.update(model);
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
            if (node.getParentId()  == root.getId() ) {
                children.add(node);
            }
        }
        root.setChildren(children);

        List<AdminResourceBean> notChildren = (List<AdminResourceBean>) CollectionUtils.subtract(allNodes, children);

        for (AdminResourceBean child : children) {
            List<AdminResourceBean> tmpChildren = findChildren(child, notChildren );
            child.setChildren(tmpChildren);
        }
        return children;
    }

    public void deleteRes(List<Integer> resIds, String username) {
        for (Integer id : resIds) {
            ComResourceModel model = new ComResourceModel();
            model.setId(id);
            model.setActive(0);
            model.setModifier(username);
            if (!(comResourceDao.update(model) > 0)) {
                throw new BusinessException("删除菜单资源信息失败");
            }
        }
    }

    public Map<String, Object> showUserAuth(Integer userId) {
        ComUserModel user = comUserDao.select(userId);
        if (user == null) {
            throw new BusinessException("用户不存在。");
        }

        Map<String, Object> result = new HashMap<>();


        List<AdminResourceBean> resList = adminResourceDaoExt.selectResByUser(userId);
        Set<Integer> resIds = resList.stream().map(AdminResourceBean::getId).collect(Collectors.toSet());

        List<AdminResourceBean> allRes = searchRes(null);

        allRes = markSelected(allRes, resIds);

        Map<String, AdminResourceBean> treeMap = allRes.stream().collect(Collectors.toMap(AdminResourceBean::getResKey, (p) -> p));

        result.put("treeMap", treeMap);
        return result;
    }

    private List<AdminResourceBean>  markSelected(List<AdminResourceBean> allRes, Set<Integer> resIds) {
        if (resIds == null || resIds.size() == 0)
            return  allRes;

        for (AdminResourceBean bean : allRes) {
            if (resIds.contains(bean.getId())) {
                bean.setSelected(1);
            } else {
                bean.setSelected(0);
            }

            if (bean.getChildren() != null && bean.getChildren().size() > 0) {
                markSelected(bean.getChildren(), resIds);
            }
        }
        return  allRes;
    }




}
