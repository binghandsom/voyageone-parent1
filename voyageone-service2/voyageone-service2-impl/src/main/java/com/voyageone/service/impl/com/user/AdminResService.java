package com.voyageone.service.impl.com.user;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.StringUtils;
import com.voyageone.security.dao.ComResourceDao;
import com.voyageone.security.dao.ComUserDao;
import com.voyageone.security.model.ComResourceModel;
import com.voyageone.security.model.ComUserModel;
import com.voyageone.service.bean.com.AdminOrgBean;
import com.voyageone.service.bean.com.AdminResourceBean;
import com.voyageone.service.daoext.core.AdminResourceDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
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
     * 查找菜单资源
     *
     * @param app
     * @return
     */
    public PageModel<AdminResourceBean> searchRes(String app, Integer pageNum, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isNullOrBlank2(app)) {
            map.put("application", app);
        }

        map = MySqlPageHelper.build(map).addSort("res_type", Order.Direction.ASC).addSort("weight", Order.Direction.ASC).toMap();

        List<ComResourceModel> list = comResourceDao.selectList(map);

        Map<Integer, String> mameMap = list.stream().collect(Collectors.toMap(ComResourceModel::getId, ComResourceModel::getResName));
        List<AdminResourceBean> beanList = new ArrayList<>();

        for (ComResourceModel model : list) {
            AdminResourceBean bean = new AdminResourceBean();
            BeanUtils.copyProperties(model, bean);
            if (bean.getParentId() != 0) {
                bean.setParentName(mameMap.get(bean.getParentId()));
            } else {
                bean.setParentName("");
            }
            beanList.add(bean);
        }
        List<AdminResourceBean> all = convert2Tree(beanList);


        List<AdminResourceBean> result =  new ArrayList<>();

        result = convert2List(all, result);

        PageModel<AdminResourceBean> pageModel = new PageModel<>();

        pageModel.setResult(result.stream().skip((pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList()));
        pageModel.setCount(result.size());
        return  pageModel;

    }


    /**
     * @param model
     */
    public void addRes(ComResourceModel model) {
        //检查resKey唯一性，resName唯一性
        Map map = new HashMap<>();
        map.put("resKey", model.getResKey());

        if (comResourceDao.selectCount(map) > 0) {
            throw new BusinessException("菜单Key在系统中已存在。");
        }
        map.clear();
        map.put("resName", model.getResName());

        if (comResourceDao.selectCount(map) > 0) {
            throw new BusinessException("菜单名称在系统中已存在。");
        }

        ComResourceModel parent = comResourceDao.select(model.getParentId());


        if (model.getWeight() == null) {
            map.clear();
            map.put("parentId", model.getParentId());
            List<ComResourceModel> siblings = comResourceDao.selectList(map);
            int weight = siblings.stream().mapToInt(ComResourceModel::getWeight).max().getAsInt();
            model.setWeight(++weight);
        }

        if (parent != null) {
            model.setParentIds(parent.getParentIds() + "," + parent.getId());
        } else {
            model.setParentIds("0");
        }

        comResourceDao.insert(model);
    }


    public void updateRes(ComResourceModel model) {
        //检查resName唯一性
        Map map = new HashMap<>();
        map.put("resName", model.getResName());

        if (comResourceDao.selectCount(map) > 0) {
            throw new BusinessException("菜单名称在系统中已存在。");
        }

        ComResourceModel parent = comResourceDao.select(model.getParentId());


        if (model.getWeight() == null) {
            map.clear();
            map.put("parentId", model.getParentId());
            List<ComResourceModel> siblings = comResourceDao.selectList(map);
            int weight = siblings.stream().mapToInt(ComResourceModel::getWeight).max().getAsInt();
            model.setWeight(++weight);
        }
        if (parent != null) {
            model.setParentIds(parent.getParentIds() + "," + parent.getId());
        } else {
            model.setParentIds("0");
        }

        comResourceDao.update(model);
    }

    public List<AdminResourceBean> getMenu(String app, String user) {
        Map map = new HashMap<>();
        map.put("application", app);
        map.put("userAccount", user);
        List<AdminResourceBean> list = convert2Tree(adminResourceDaoExt.selectMenu(map));

        if (list != null && list.size() > 0 && list.get(0) != null && list.get(0).getChildren() != null && list.get(0).getChildren().size() != 0) {
            return list.get(0).getChildren();
        } else {
            return Collections.EMPTY_LIST;
        }
    }


    /**
     * 把树拆成列表
     * 为了列表页翻页展示用
     */
    private List<AdminResourceBean> convert2List(List<AdminResourceBean> resList,List<AdminResourceBean> result) {
        for (AdminResourceBean bean : resList) {

            if(bean.getResType() == 1)
            {
                result.add(bean);
            }

            if (bean.getChildren() != null && bean.getChildren().size() > 0) {
                convert2List(bean.getChildren(), result);
                if(bean.getChildren().get(0).getResType() == 1)
                {
                    bean.setChildren(Collections.EMPTY_LIST);
                }
            }
        }
        return result;
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


}
