package com.voyageone.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.cms.service.dao.FeedToCmsDao;
import com.voyageone.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/11/26.
 */
@Service
public class FeedToCmsService {
    @Autowired
    FeedToCmsDao feedToCmsDao;

    /**
     * 获取feed类目
     *
     * @param channelId
     * @return
     */
    public List<Map> getFeedCategory(String channelId) {
        return (List<Map>) feedToCmsDao.getFeedCategory(channelId).getCategoryTree();
    }

    /**
     * 设定feed类目
     *
     * @param channelId
     * @param tree
     */
    public void setFeedCategory(String channelId, List<Map> tree) {
        feedToCmsDao.setFeedCategory(channelId, tree);
    }

    /**
     * 根据category从tree中找到节点
     *
     * @param tree
     * @param cat
     * @return
     */
    public Map findCategory(List<Map> tree, String cat) {
        Object jsonObj = JsonPath.parse(tree).json();
        System.out.println(jsonObj.toString());
        List<Map> child = JsonPath.read(jsonObj, "$..child[?(@.category == '" + cat + "')]");
        if(child.size()  == 0){
            child = JsonPath.read(jsonObj, "$..*[?(@.category == '" + cat + "')]");
        }
        return child == null || child.size() == 0 ? null : child.get(0);
    }

    /**
     * 追加一个类目
     * @param tree
     * @param cat
     */
    public void addCategory(List<Map> tree, String cat) {
        String[] c = cat.split("-");
        String temp = "";
        Map befNode = null;
        for (int i = 0; i < c.length; i++) {
            temp += c[i];
            Map node = findCategory(tree, temp);
            if(node == null){
                Map newNode = new HashMap<>();
                newNode.put("category",temp);
                newNode.put("child",new ArrayList<>());
                if(befNode == null){
                    tree.add(newNode);
                }else{
                    ((List<Map>) befNode.get("child")).add(newNode);
                }
                befNode = newNode;
            }else{
                befNode = node;
            }
            temp += "-";
        }
    }
}
