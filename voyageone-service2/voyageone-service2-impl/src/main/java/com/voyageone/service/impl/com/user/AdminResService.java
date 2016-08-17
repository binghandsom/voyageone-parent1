package com.voyageone.service.impl.com.user;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.security.bean.ComResourceBean;
import com.voyageone.security.dao.ComResourceDao;
import com.voyageone.security.model.ComResourceModel;
import com.voyageone.service.impl.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Ethan Shi on 2016-08-17.
 */
@Service
public class AdminResService extends BaseService {


    @Autowired
    ComResourceDao comResourceDao;

    public List<ComResourceBean> searchRes(String app)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("application", app);

        map = MySqlPageHelper.build(map).addSort("res_type", Order.Direction.ASC).addSort("weight", Order.Direction.ASC).toMap();

        List<ComResourceModel> list  = comResourceDao.selectList(map);
        List<ComResourceBean> beanList = new ArrayList<>();

        for(ComResourceModel model : list)
        {
            ComResourceBean bean = new ComResourceBean() ;
            BeanUtils.copyProperties(model, bean);
            beanList.add(bean);
        }

        return convert2Tree(beanList);
    }


    /**
     * 将资源列转成一组树
     */
    private List<ComResourceBean> convert2Tree(List<ComResourceBean> resList) {
        List<ComResourceBean> roots = findRoots(resList);
        List<ComResourceBean> notRoots = (List<ComResourceBean>) CollectionUtils.subtract(resList, roots);
        for (ComResourceBean root : roots) {
            List<ComResourceBean> children = findChildren(root, notRoots);
            root.setChildren(children);
        }
        return roots;
    }

    /**
     * 查找所有根节点
     */
    private List<ComResourceBean> findRoots(List<ComResourceBean> allNodes) {
        List<ComResourceBean> results = new ArrayList<>();
        for (ComResourceBean node : allNodes) {
            if (node.getParentId() == 0) {
                results.add(node);
            }
        }
        return results;
    }


    /**
     * 查找所有子节点
     */
    private List<ComResourceBean> findChildren(ComResourceBean root, List<ComResourceBean> allNodes) {
        List<ComResourceBean> children = new ArrayList<>();

        for (ComResourceBean node : allNodes) {
            if (node.getParentId()  == root.getId() ) {
                children.add(node);
            }
        }
        root.setChildren(children);

        List<ComResourceBean> notChildren = (List<ComResourceBean>) CollectionUtils.subtract(allNodes, children);

        for (ComResourceBean child : children) {
            List<ComResourceBean> tmpChildren = findChildren(child, notChildren );
            child.setChildren(tmpChildren);
        }
        return children;
    }
}
