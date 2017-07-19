package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductStatusHistoryModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductStatusHistoryModel_History;
import com.voyageone.service.model.util.MapModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangjindong on 2017/6/16.
 */
@Repository
public class CmsBtProductStatusHistoryDao extends BaseMongoChannelDao<CmsBtProductStatusHistoryModel> {

    public void insert(CmsBtProductStatusHistoryModel model) {
        String channelId = model.getChannelId();
        String collectionName = getCollectionName(channelId);
        String query = String.format("{\"code\":\"%s\",\"cartId\":%d}", model.getCode(), model.getCartId());
        if (!mongoTemplate.exists(query, collectionName)) {
            // 不存在的话直接追加
            mongoTemplate.insert(model, collectionName);
        } else {
            JongoUpdate jongoUpdate = new JongoUpdate();
            jongoUpdate.setQuery("{\"code\":#,\"cartId\":#}");
            jongoUpdate.setQueryParameters(model.getCode(), model.getCartId());
            jongoUpdate.setUpdate("{$set: {\"modifier\": #,\"modified\":#},$addToSet:{list:{$each:#}}}");
            jongoUpdate.setUpdateParameters(model.getModifier(), model.getModified(), model.getList());

            mongoTemplate.updateMulti(jongoUpdate, collectionName);
        }
    }

    public List<MapModel> selectPage(Map<String, Object> param) {
        String channelId = (String) param.get("channelId");
        List<JongoAggregate> aggregateList = new ArrayList<>();
        String matchStr = getAggMatch(param);
        // match
        aggregateList.add(new JongoAggregate(matchStr));
        // unwind
        aggregateList.add(new JongoAggregate("{$unwind: \"$list\"}"));
        // match array conditions
        String arrayMatch = getAggArrayMatch(param);
        if (arrayMatch != null) {
            aggregateList.add(new JongoAggregate(arrayMatch));
        }
        // sort 只有一个排序
        if (param.containsKey("orderBy")) {
            Map<String, String> orderByMap = (Map<String, String>) param.get("orderBy");
            aggregateList.add(new JongoAggregate(String.format("{$sort:{%s}}", getAggSort(orderByMap))));
        }
        // paging
        int pageSize = (Integer) param.get("pageRowCount");
        int start = (Integer) param.get("skip");
        aggregateList.add(new JongoAggregate(String.format("{$skip:%d}", start)));
        aggregateList.add(new JongoAggregate(String.format("{$limit:%d}", pageSize)));
        StringBuilder project = new StringBuilder(200);
        project.append("{$project: {\"_id\":0,");
        project.append("\"channel_id\":\"$channelId\",\"cart_id\":\"$cartId\",\"code\":\"$code\",");
        project.append("\"operation_type\":\"$list.operationType\",\"status\":\"$list.status\",\"comment\":\"$list.comment\",");
        project.append("\"created\":\"$list.created\",\"creater\":\"$list.creater\",\"modified\":\"$list.modified\",\"modifier\":\"$list.modifier\"}}");
        aggregateList.add(new JongoAggregate(project.toString()));

        List<MapModel> modelList = new ArrayList<>();
        List<Map<String, Object>> statusHistoryMaplList = aggregateToMap(channelId, aggregateList);
        for (Map<String, Object> statusMap : statusHistoryMaplList) {
            MapModel mapModel = new MapModel();
            mapModel.put("channel_id", statusMap.get("channel_id"));
            mapModel.put("cart_id", statusMap.get("cart_id"));
            mapModel.put("code", statusMap.get("code"));
            mapModel.put("operation_type", statusMap.get("operation_type"));
            mapModel.put("status", statusMap.get("status"));
            mapModel.put("comment", statusMap.get("comment"));
            mapModel.put("creater", statusMap.get("creater"));
            mapModel.put("modifier", statusMap.get("modifier"));
            mapModel.put("created", statusMap.get("created"));
            mapModel.put("modified", statusMap.get("modified"));

            modelList.add(mapModel);
        }
        return modelList;
    }

    public int selectCount(Map<String, Object> param) {
        List<JongoAggregate> aggregateList = new ArrayList<>();
        String matchStr = getAggMatch(param);
        // match
        aggregateList.add(new JongoAggregate(matchStr));
        // unwind
        aggregateList.add(new JongoAggregate("{$unwind: \"$list\"}"));
        // match array conditions
        String arrayMatch = getAggArrayMatch(param);
        if (arrayMatch != null) {
            aggregateList.add(new JongoAggregate(arrayMatch));
        }
        // count
        aggregateList.add(new JongoAggregate("{$count: \"statusCount\"}"));

        String channelId = (String) param.get("channelId");

        List<Map<String, Object>> countMap = aggregateToMap(channelId, aggregateList);
        int count = 0;
        if (countMap != null && countMap.size() > 0) {
            count = (Integer) countMap.get(0).get("statusCount");
        }
        return count;
    }

    private String getAggMatch(Map<String, Object> param) {
        String code = (String) param.get("code");
        StringBuilder matchSb = new StringBuilder(50);
        matchSb.append("{$match:{\"code\":\"");
        matchSb.append(code);
        matchSb.append('\"');
        if (param.containsKey("cartId")) {
            String cartId = (String) param.get("cartId");
            if (!StringUtil.isEmpty(cartId)) {
                matchSb.append(",\"cartId\":" + cartId);
            }
        }
        matchSb.append("}}");
        return matchSb.toString();
    }

    private String getAggArrayMatch(Map<String, Object> param) {
        boolean hasArrayMatch = false;
        StringBuilder matchSb = new StringBuilder("{$match:{");
        if (param.containsKey("operationType")) {
            hasArrayMatch = true;
            matchSb.append(String.format("\"list.operationType\":%d,", param.get("operationType")));
        }
        if (param.containsKey("status")) {
            hasArrayMatch = true;
            matchSb.append(String.format("\"list.status\":\"%s\"", param.get("status")));
        }
        matchSb.append("}}");
        if (hasArrayMatch) {
            return matchSb.toString();
        } else {
            return null;
        }
    }


    private String getAggSort(Map<String, String> orderMap) {
        StringBuilder sortSb = new StringBuilder();
        for (Map.Entry<String, String> entry : orderMap.entrySet()) {
            sortSb.append("\"list." + entry.getKey() + "\":");
            if ("desc".equals(entry.getValue().trim())) {
                sortSb.append("-1,");
            } else {
                sortSb.append("1,");
            }
        }
        sortSb.deleteCharAt(sortSb.length() - 1);
        return sortSb.toString();
    }


}
