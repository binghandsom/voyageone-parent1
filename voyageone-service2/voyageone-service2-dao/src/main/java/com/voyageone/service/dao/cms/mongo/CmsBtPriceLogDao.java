package com.voyageone.service.dao.cms.mongo;

import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogFlatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtPriceLogModel_History;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yangjindong on 2017/6/21.
 */
@Repository
public class CmsBtPriceLogDao extends BaseMongoChannelDao<CmsBtPriceLogModel> {

    public void insert(CmsBtPriceLogModel model) {
        String channelId = model.getChannelId();
        String collectionName = getCollectionName(channelId);
        String query = String.format("{\"code\":\"%s\",\"sku\":\"%s\",\"cartId\":%s}", model.getCode(), model.getSku(), model.getCartId());
        if (!mongoTemplate.exists(query, collectionName)) {
            // 不存在的话直接追加
            mongoTemplate.insert(model, collectionName);
        } else {
            JongoUpdate jongoUpdate = new JongoUpdate();
            jongoUpdate.setQuery("{\"code\":#,\"sku\":#,\"cartId\":#}");
            jongoUpdate.setQueryParameters(model.getCode(), model.getSku(), model.getCartId());
            jongoUpdate.setUpdate("{$set: {\"modifier\": #,\"modified\":#},$addToSet:{list:{$each:#}}}");
            jongoUpdate.setUpdateParameters(model.getModifier(), model.getModified(), model.getList());

            mongoTemplate.updateMulti(jongoUpdate, collectionName);
        }
    }

    public void insert(CmsBtPriceLogFlatModel flatModel) {
        CmsBtPriceLogModel priceLogModel = convertPriceLogModel(flatModel);
        insert(priceLogModel);
    }


    public List<CmsBtPriceLogFlatModel> selectListBySkuOnCart(String sku, String code, String cartId, String channelId) {
        return selectPageBySkuOnCart(sku, code, cartId, channelId, null, null);
    }

    public List<CmsBtPriceLogFlatModel> selectPageBySkuOnCart(String sku, String code, String cartId, String channelId, Integer offset, Integer limit) {
        List<JongoAggregate> aggregateList = new ArrayList<>();
        String matchStr = getAggMatch(sku, code, cartId);
        // match
        aggregateList.add(new JongoAggregate(matchStr));
        // unwind
        aggregateList.add(new JongoAggregate("{$unwind: \"$list\"}"));
        // sort
        aggregateList.add(new JongoAggregate("{$sort:{\"list.created\":-1}}"));
        // paging
        if (offset != null && limit != null) {
            aggregateList.add(new JongoAggregate(String.format("{$skip:%d}", offset)));
            aggregateList.add(new JongoAggregate(String.format("{$limit:%d}", limit)));
        }

        List<CmsBtPriceLogFlatModel> flatModelList = new ArrayList<>();

        List<Map<String, Object>> priceLogMapList = aggregateToMap(channelId, aggregateList);
        for (Map<String, Object> priceLogMap : priceLogMapList) {
            CmsBtPriceLogFlatModel flatModel = convertPriceLogFlat(priceLogMap);
            flatModelList.add(flatModel);
        }
        return flatModelList;
    }

    public int selectCountBySkuOnCart(String sku, String code, String cartId, String channelId) {
        List<JongoAggregate> aggregateList = new ArrayList<>();
        String matchStr = getAggMatch(sku, code, cartId);
        // match
        aggregateList.add(new JongoAggregate(matchStr));
        // unwind
        aggregateList.add(new JongoAggregate("{$unwind: \"$list\"}"));
        // count
        aggregateList.add(new JongoAggregate("{$count: \"count\"}"));

        List<Map<String, Object>> countMap = aggregateToMap(channelId, aggregateList);
        int count = 0;
        if (countMap != null && countMap.size() > 0) {
            count = (Integer) countMap.get(0).get("count");
        }
        return count;
    }


    public CmsBtPriceLogFlatModel selectLastOneBySkuOnCart(String sku, Integer cartId, String channelId) {
        List<JongoAggregate> aggregateList = new ArrayList<>();
        // match
        String match = String.format("{$match:{\"sku\":\"%s\",\"cartId\":%d}}", sku, cartId);
        aggregateList.add(new JongoAggregate(match));
        // unwind
        aggregateList.add(new JongoAggregate("{$unwind: \"$list\"}"));
        // sort
        aggregateList.add(new JongoAggregate("{$sort:{\"list.created\":-1}}"));
        aggregateList.add(new JongoAggregate(String.format("{$limit:%d}", 1)));

        List<Map<String, Object>> priceLogList = aggregateToMap(channelId, aggregateList);
        if (priceLogList != null && priceLogList.size() > 0) {
            return convertPriceLogFlat(priceLogList.get(0));
        }
        return null;
    }

    public int insertCmsBtPriceLogList(List<CmsBtPriceLogFlatModel> paramList) {
        for (CmsBtPriceLogFlatModel flatModel : paramList) {
            CmsBtPriceLogModel priceLogModel = convertPriceLogModel(flatModel);
            insert(priceLogModel);
        }
        return paramList.size();
    }

    public int updateCmsBtPriceLogForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        String collectionName = getCollectionName(channelId);
        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"code\":#,\"sku\":{$in:#}}");
        jongoUpdate.setQueryParameters(itemCodeOld, skuList);
        jongoUpdate.setUpdate("{$set: {\"code\":#,\"modifier\": #,\"modified\":#}}");
        jongoUpdate.setUpdateParameters(itemCodeNew, modifier, new Date());

        return mongoTemplate.updateMulti(jongoUpdate, collectionName).getN();
    }

    private String getAggMatch(String sku, String code, String cartId) {
        StringBuilder matchSb = new StringBuilder(100);
        matchSb.append("{$match:{");
        if (!StringUtils.isEmpty(code)) {
            matchSb.append(String.format("\"code\":\"%s\",", code));
        }
        if (!StringUtils.isEmpty(sku)) {
            matchSb.append(String.format("\"sku\":\"%s\",", sku));
        }
        if (!StringUtils.isEmpty(cartId)) {
            matchSb.append(String.format("\"cartId\":%d", Integer.parseInt(cartId)));
        }
        if (matchSb.charAt(matchSb.length() - 1) == ',') {
            matchSb.deleteCharAt(matchSb.length() - 1);
        }
        matchSb.append("}}");
        return matchSb.toString();
    }

    private CmsBtPriceLogFlatModel convertPriceLogFlat(Map<String, Object> priceLogMap) {
        CmsBtPriceLogFlatModel flatModel = new CmsBtPriceLogFlatModel();
        flatModel.setChannelId((String) priceLogMap.get("channelId"));
        flatModel.setCartId((Integer) priceLogMap.get("cartId"));
        flatModel.setProductId((Long) priceLogMap.get("productId"));
        flatModel.setCode((String) priceLogMap.get("code"));
        flatModel.setSku((String) priceLogMap.get("sku"));

        Map<String, Object> historyMap = (Map<String, Object>) priceLogMap.get("list");
        flatModel.setMsrpPrice((Double) historyMap.get("msrpPrice"));
        flatModel.setRetailPrice((Double) historyMap.get("retailPrice"));
        flatModel.setSalePrice((Double) historyMap.get("salePrice"));
        flatModel.setClientMsrpPrice((Double) historyMap.get("clientMsrpPrice"));
        flatModel.setClientRetailPrice((Double) historyMap.get("clientRetailPrice"));
        flatModel.setClientNetPrice((Double) historyMap.get("clientNetPrice"));
        flatModel.setComment((String) historyMap.get("comment"));
        flatModel.setCreater((String) historyMap.get("creater"));
        flatModel.setModifier((String) historyMap.get("modifier"));
        flatModel.setCreated((Date) historyMap.get("created"));
        flatModel.setModified((Date) historyMap.get("modified"));

        return flatModel;
    }

    private CmsBtPriceLogModel convertPriceLogModel(CmsBtPriceLogFlatModel priceLogFlatModel) {
        CmsBtPriceLogModel priceLogModel = new CmsBtPriceLogModel();
        priceLogModel.setChannelId(priceLogFlatModel.getChannelId());
        priceLogModel.setCartId(priceLogFlatModel.getCartId());
        priceLogModel.setProductId(priceLogFlatModel.getProductId());
        priceLogModel.setCode(priceLogFlatModel.getCode());
        priceLogModel.setSku(priceLogFlatModel.getSku());
        priceLogModel.setCreater(priceLogFlatModel.getCreater());
        priceLogModel.setModifier(priceLogFlatModel.getModifier());

        CmsBtPriceLogModel_History history = new CmsBtPriceLogModel_History();
        history.setMsrpPrice(priceLogFlatModel.getMsrpPrice());
        history.setRetailPrice(priceLogFlatModel.getRetailPrice());
        history.setSalePrice(priceLogFlatModel.getSalePrice());
        history.setClientMsrpPrice(priceLogFlatModel.getClientMsrpPrice());
        history.setClientRetailPrice(priceLogFlatModel.getClientRetailPrice());
        history.setClientNetPrice(priceLogFlatModel.getClientNetPrice());
        history.setComment(priceLogFlatModel.getComment());
        history.setCreater(priceLogFlatModel.getCreater());
        history.setModifier(priceLogFlatModel.getModifier());
        history.setCreated(priceLogFlatModel.getCreated());
        history.setModified(priceLogFlatModel.getModified());
        priceLogModel.getList().add(history);

        return priceLogModel;
    }
}
