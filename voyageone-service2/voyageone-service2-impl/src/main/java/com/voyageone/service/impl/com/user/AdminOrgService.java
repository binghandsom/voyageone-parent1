package com.voyageone.service.impl.com.user;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.security.dao.ComOrganizationDao;
import com.voyageone.security.model.ComOrganizationModel;
import com.voyageone.security.model.ComResourceModel;
import com.voyageone.service.bean.com.AdminOrgBean;
import com.voyageone.service.daoext.core.AdminOrganizationDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ethan Shi on 2016-08-17.
 */
@Service
public class AdminOrgService extends BaseService {


    @Autowired
    ComOrganizationDao comOrganizationDao;

    @Autowired
    AdminOrganizationDaoExt adminOrganizationDaoExt;


    /**
     * 取所有组织的列表
     *
     * @return
     */
    public List<Map<String, Object>> getAllOrg()
    {
        List<ComOrganizationModel> orgList = comOrganizationDao.selectList(new HashMap<String, Object>(){{put("active", 1);}});

        List<ComOrganizationModel> result = new ArrayList<>();

        result = getChildTreeObjects(orgList, 0 , "-", result);

//        Map resultMap = result.stream().collect(Collectors.toMap(ComOrganizationModel:: getId ,ComOrganizationModel:: getOrgName ));

        List<Map<String, Object>> retList = new ArrayList<>();


        result.forEach(w -> retList.add(new HashMap<String, Object>()  {{put("id", w.getId());  put("orgName", w.getOrgName()); } } ) );

        return  retList;
    }

    public PageModel<AdminOrgBean>  searchOrg(Integer pageNum, Integer pageSize)
    {
        return searchOrg(null, null, pageNum, pageSize);
    }

    public PageModel<AdminOrgBean>  searchOrg(String orgName ,Integer active, Integer pageNum, Integer pageSize)
    {
        PageModel<AdminOrgBean> pageModel = new PageModel<>();

        // 判断查询结果是否分页
        boolean needPage = false;
        Map<String,Object> newMap = new HashMap<>();

        newMap.put("orgName", orgName);
        newMap.put("active", active);

        if (pageNum != null && pageSize != null) {
            needPage = true;
            pageModel.setCount(adminOrganizationDaoExt.selectCount(newMap));
            newMap = MySqlPageHelper.build(newMap).page(pageNum).limit(pageSize).addSort("created", Order.Direction.DESC).toMap();
        }
        else
        {
            newMap = MySqlPageHelper.build(newMap).addSort("created", Order.Direction.DESC).toMap();
        }

        List<AdminOrgBean> list = adminOrganizationDaoExt.selectList(newMap);
        if (!needPage) {
            pageModel.setCount(list.size());
        }

        pageModel.setResult(list);
        return pageModel;
    }

    public void addOrg(ComOrganizationModel model)
    {
        //检查orgName唯一性
        Map map = new HashMap<>();
        map.put("orgName" , model.getOrgName());
        map.put("parentId" , model.getParentId());

        if(comOrganizationDao.selectCount(map) > 0)
        {
            throw new BusinessException("组织名称在系统中已存在。");
        }

        ComOrganizationModel parent = null;
        if(model.getParentId() != null) {
            parent = comOrganizationDao.select(model.getParentId());
        }
        else
        {
            model.setParentId(0);
        }

        if(model.getWeight() == null) {
            map.clear();
            map.put("parentId" , model.getParentId());
            List<ComOrganizationModel> siblings = comOrganizationDao.selectList(map);
            int weight = siblings.stream().mapToInt(ComOrganizationModel::getWeight).max().getAsInt();
            model.setWeight(++weight);
        }
        if(parent != null) {
            model.setParentIds(parent.getParentIds() + "," + parent.getId());
        }else
        {
            model.setParentIds("0");
        }
        comOrganizationDao.insert(model);
    }


    public void updateOrg(ComOrganizationModel model)
    {
        //检查orgName唯一性
        Map map = new HashMap<>();
        map.put("orgName" , model.getOrgName());
        map.put("parentId" , model.getParentId());

        if(comOrganizationDao.selectCount(map) > 1)
        {
            throw new BusinessException("组织名称在系统中已存在。");
        }

        ComOrganizationModel parent = comOrganizationDao.select(model.getParentId());

        if(model.getWeight() == null) {
            map.clear();
            map.put("parentId" , model.getParentId());
            List<ComOrganizationModel> siblings = comOrganizationDao.selectList(map);
            int weight = siblings.stream().mapToInt(ComOrganizationModel::getWeight).max().getAsInt();
            model.setWeight(++weight);
        }

        if(parent != null) {
            model.setParentIds(parent.getParentIds() + "," + parent.getId());
        }else
        {
            model.setParentIds("0");
        }
        comOrganizationDao.update(model);
    }

    public void deleteOrg(List<Integer> orgIds, String username) {
        for (Integer id : orgIds) {
            ComOrganizationModel model = new ComOrganizationModel();
            model.setId(id);
            model.setActive(0);
            model.setModifier(username);
            if (!(comOrganizationDao.update(model) > 0)) {
                throw new BusinessException("禁用组织信息失败");
            }
        }
    }


    /**
     * 生成树状列表
     *
     * @param list
     * @param typeId
     * @param prefix
     * @param returnList
     * @return
     */
    private List<ComOrganizationModel> getChildTreeObjects(List<ComOrganizationModel> list, int typeId, String prefix , List<ComOrganizationModel> returnList){
        if(list == null) return null;
        for (Iterator<ComOrganizationModel> iterator = list.iterator(); iterator.hasNext();) {
            ComOrganizationModel node = (ComOrganizationModel) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.getParentId().equals(typeId)) {
                recursionFn(list, node,prefix , returnList);
            }
        }
        return returnList;
    }

    private void recursionFn(List<ComOrganizationModel> list, ComOrganizationModel node,String p , List<ComOrganizationModel> returnList) {
        List<ComOrganizationModel> childList = getChildList(list, node);// 得到子节点列表
        if (hasChild(list, node)) {// 判断是否有子节点
            returnList.add(node);
            Iterator<ComOrganizationModel> it = childList.iterator();
            while (it.hasNext()) {
                ComOrganizationModel n = (ComOrganizationModel) it.next();
                n.setOrgName(p+n.getOrgName());
                recursionFn(list, n,p+p, returnList);
            }
        } else {
            returnList.add(node);
        }
    }

    // 判断是否有子节点
    private boolean hasChild(List<ComOrganizationModel> list, ComOrganizationModel t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    // 得到子节点列表
    private List<ComOrganizationModel> getChildList(List<ComOrganizationModel> list, ComOrganizationModel t) {

        List<ComOrganizationModel> tlist = new ArrayList<ComOrganizationModel>();
        Iterator<ComOrganizationModel> it = list.iterator();
        while (it.hasNext()) {
            ComOrganizationModel n = (ComOrganizationModel) it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

}
