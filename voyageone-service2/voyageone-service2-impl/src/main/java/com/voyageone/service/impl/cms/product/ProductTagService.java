package com.voyageone.service.impl.cms.product;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtTagDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Product Tag Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class ProductTagService extends BaseService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtTagDaoExt cmsBtTagDaoExt;
    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    /**
     * 向产品添加tag，同时添加该tag的所有上级tag
     *
     * @param channelId
     * @param tagPath
     * @param prodIdList
     * @param modifier
     */
    public void addProdTag(String channelId, String tagPath, List<Long> prodIdList, String tagsKey, String modifier) {
        if (tagPath == null || prodIdList == null || prodIdList.isEmpty()) {
            $warn("ProductTagService：addProdTag 缺少参数");
            throw new BusinessException("缺少参数!");
        }

        // 先删除同级的tag
        Map params = new HashMap<>(1);
        params.put("tagPath", tagPath);
        CmsBtTagModel tagBean = cmsBtTagDao.selectOne(params);
        if (tagBean != null) {
            List<CmsBtTagModel> modelList = cmsBtTagDaoExt.selectListBySameLevel(channelId, tagBean.getParentTagId(), tagBean.getId());
            if (modelList.size() > 0) {
                List<String> pathList = new ArrayList<>(modelList.size());
                modelList.forEach(model -> pathList.add(model.getTagPath()));
                deleteTags(channelId, prodIdList, tagsKey, pathList, modifier);
            }
        }

        // 更新条件
        HashMap<String, Object> queryMap = new HashMap<>();
        HashMap<String, Object> inMap = new HashMap<>();
        inMap.put("$in", prodIdList);
        queryMap.put("prodId", inMap);

        // 生成级联tag path列表
        String[] pathArr = org.apache.commons.lang3.StringUtils.split(tagPath, '-');
        int arrSize = pathArr.length;
        List<String> pathList = new ArrayList<>(arrSize);
        for (int j = 0; j < arrSize; j ++) {
            StringBuilder curTagPath = new StringBuilder("-");
            for (int i = 0; i <= j ; i ++) {
                curTagPath.append(pathArr[i]);
                curTagPath.append("-");
            }
            pathList.add(curTagPath.toString());
        }

        // 更新操作
        HashMap<String, Object> eachMap = new HashMap<>();
        eachMap.put("$each", pathList);

        HashMap<String, Object> updateMap = new HashMap<>();
        HashMap<String, Object> tagsMap = new HashMap<>();
        tagsMap.put(tagsKey, eachMap);
        updateMap.put("$addToSet", tagsMap);

        // 批量更新product表
        WriteResult result = cmsBtProductDao.update(channelId, queryMap, updateMap);
        $debug(String.format("ProductTagService：addProdTag 操作结果-> %s", result.toString()));
    }

    /**
     * 批量删除商品的tag(根据指定的tag path)，同时删除关联的下级tag
     * 当不存在同级的tag时，也删除关联的上级tag
     */
    public void delete(String channelId, String tagPath, List<Long> prodIdList, String tagsKey, String modifier) {
        if (tagPath == null || prodIdList == null || prodIdList.isEmpty()) {
            $warn("ProductTagService：delete 缺少参数");
            throw new RuntimeException("缺少参数!");
        }

        // 先找出该tag的所有子节点tag
        List<String> pathList = new ArrayList<>();
        pathList.add(tagPath);

        String[] pathArr = org.apache.commons.lang3.StringUtils.split(tagPath, '-');
        if (pathArr.length == 3) {
            // 先确认是否存在同级别的tag
            int tagId = Integer.parseInt(pathArr[2]);
            CmsBtTagModel tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagId);
            List<CmsBtTagModel> modelList = cmsBtTagDaoExt.selectListBySameLevel(channelId, tagBean.getParentTagId(), tagId);
            List<String> curPathList = new ArrayList<>();
            if (modelList.size() > 0) {
                modelList.forEach(model -> { curPathList.add(model.getTagPath()); });
            }
            if (curPathList.isEmpty()) {
                // 若不存在，则删除关联的上级tag
                tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagBean.getParentTagId());
                pathList.add(tagBean.getTagPath());
            } else {
                JomgoQuery queryObject = new JomgoQuery();
                StringBuilder queryStr = new StringBuilder();
                queryStr.append("{");
                queryStr.append(MongoUtils.splicingValue("prodId", prodIdList.toArray(), "$in"));
                queryStr.append(",");
                queryStr.append(MongoUtils.splicingValue(tagsKey, (String[]) curPathList.toArray(new String[curPathList.size()]), "$in"));
                queryStr.append("}");
                queryObject.setQuery(queryStr.toString());
                queryObject.setProjection("{'fields.code':1,'_id':0}");
                List<CmsBtProductModel> prodInfoList = cmsBtProductDao.select(queryObject, channelId);
                if (prodInfoList.isEmpty()) {
                    // 若不存在，则删除关联的上级tag
                    tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagBean.getParentTagId());
                    pathList.add(tagBean.getTagPath());

                    // 再向上查上级tag
                    modelList = cmsBtTagDaoExt.selectListBySameLevel(channelId, tagBean.getParentTagId(), tagBean.getId());
                    List<String> upPathList = new ArrayList<>();
                    if (modelList.size() > 0) {
                        modelList.forEach(model -> {
                            upPathList.add(model.getTagPath());
                        });
                    }
                    if (upPathList.isEmpty()) {
                        // 若不存在，则删除关联的上级tag
                        tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagBean.getParentTagId());
                        pathList.add(tagBean.getTagPath());
                    } else {
                        queryObject = new JomgoQuery();
                        queryStr = new StringBuilder();
                        queryStr.append("{");
                        queryStr.append(MongoUtils.splicingValue("prodId", prodIdList.toArray(), "$in"));
                        queryStr.append(",");
                        queryStr.append(MongoUtils.splicingValue(tagsKey, (String[]) curPathList.toArray(new String[curPathList.size()]), "$in"));
                        queryStr.append("}");
                        queryObject.setQuery(queryStr.toString());
                        queryObject.setProjection("{'fields.code':1,'_id':0}");
                        prodInfoList = cmsBtProductDao.select(queryObject, channelId);
                        if (prodInfoList.isEmpty()) {
                            // 若不存在，则删除关联的上级tag
                            tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagBean.getParentTagId());
                            pathList.add(tagBean.getTagPath());
                        }
                    }
                }
            }

        } else if (pathArr.length == 2) {
            int tagId = Integer.parseInt(pathArr[1]);
            List<CmsBtTagModel> tagList = cmsBtTagDaoExt.selectListByParentTagId(tagId);
            tagList.forEach(model -> pathList.add(model.getTagPath()));

            // 先确认是否存在同级别的tag
            CmsBtTagModel tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagId);
            List<CmsBtTagModel> modelList = cmsBtTagDaoExt.selectListBySameLevel(channelId, tagBean.getParentTagId(), tagId);
            List<String> curPathList = new ArrayList<>();
            if (modelList.size() > 0) {
                modelList.forEach(model -> { curPathList.add(model.getTagPath()); });
            }
            if (curPathList.isEmpty()) {
                // 若不存在，则删除关联的上级tag
                tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagBean.getParentTagId());
                pathList.add(tagBean.getTagPath());
            } else {
                JomgoQuery queryObject = new JomgoQuery();
                StringBuilder queryStr = new StringBuilder();
                queryStr.append("{");
                queryStr.append(MongoUtils.splicingValue("prodId", prodIdList.toArray(), "$in"));
                queryStr.append(",");
                queryStr.append(MongoUtils.splicingValue(tagsKey, (String[]) curPathList.toArray(new String[curPathList.size()]), "$in"));
                queryStr.append("}");
                queryObject.setQuery(queryStr.toString());
                queryObject.setProjection("{'fields.code':1,'_id':0}");
                List<CmsBtProductModel> prodInfoList = cmsBtProductDao.select(queryObject, channelId);
                if (prodInfoList.isEmpty()) {
                    // 若不存在，则删除关联的上级tag
                    tagBean = cmsBtTagDaoExt.selectCmsBtTagByTagId(tagBean.getParentTagId());
                    pathList.add(tagBean.getTagPath());
                }
            }
        } else if (pathArr.length == 1) {
            List<CmsBtTagModel> tagList = cmsBtTagDaoExt.selectListByParentTagId(Integer.parseInt(pathArr[0]));
            tagList.forEach(model -> {
                pathList.add(model.getTagPath());
                List<CmsBtTagModel> nexttagList = cmsBtTagDaoExt.selectListByParentTagId(model.getId());
                nexttagList.forEach(nextmodel -> pathList.add(nextmodel.getTagPath()));
            });
        }

        // 更新条件
        HashMap<String, Object> queryMap = new HashMap<>();
        HashMap<String, Object> inMap = new HashMap<>();
        inMap.put("$in", prodIdList);
        queryMap.put("prodId", inMap);

        // 更新操作
        HashMap<String, Object> updateMap = new HashMap<>();
        HashMap<String, Object> tagsMap = new HashMap<>();
        tagsMap.put(tagsKey, pathList);
        updateMap.put("$pullAll", tagsMap);

        // 批量更新product表
        WriteResult result = cmsBtProductDao.update(channelId, queryMap, updateMap);
        $debug(String.format("ProductTagService：delete 操作结果-> %s", result.toString()));
    }

    /**
     * 批量删除商品的tag(只删除指定的tag path)，不删除关联的上级以及下级tag
     */
    public void deleteTags(String channelId, List<Long> prodIdList, String tagsKey, List<String> tagPathList, String modifier) {
        if (tagPathList == null || tagPathList.isEmpty() || prodIdList == null || prodIdList.isEmpty()) {
            $warn("ProductTagService：deleteTags 缺少参数");
            throw new RuntimeException("缺少参数!");
        }

        // 更新条件
        HashMap<String, Object> queryMap = new HashMap<>();
        HashMap<String, Object> inMap = new HashMap<>();
        inMap.put("$in", prodIdList);
        queryMap.put("prodId", inMap);

        // 更新操作
        HashMap<String, Object> updateMap = new HashMap<>();
        HashMap<String, Object> tagsMap = new HashMap<>();
        tagsMap.put(tagsKey, tagPathList);
        updateMap.put("$pullAll", tagsMap);

        // 批量更新product表
        WriteResult result = cmsBtProductDao.update(channelId, queryMap, updateMap);
        $debug(String.format("ProductTagService：deleteTags 操作结果-> %s", result.toString()));
    }
}
