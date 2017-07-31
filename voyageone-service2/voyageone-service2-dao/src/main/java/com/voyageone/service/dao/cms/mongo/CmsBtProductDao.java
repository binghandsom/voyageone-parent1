package com.voyageone.service.dao.cms.mongo;

import com.google.common.base.Joiner;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.*;
import org.springframework.stereotype.Repository;

import java.util.*;


/**
 * @author Ethan Shi
 * @version 2.1.0
 */
@Repository
public class CmsBtProductDao extends BaseMongoChannelDao<CmsBtProductModel> {

    /**
     * 更新SKU
     */
    public WriteResult updateWithSku(String channelId, String code, CmsBtProductModel_Sku skuModel) {
        if (channelId == null || code == null || skuModel == null || skuModel.getSkuCode() == null) {
            throw new RuntimeException("channelId, code, skuModel, sku not null");
        }
        String query = String.format("{\"skus.sku\":\"%s\"}", skuModel.getSkuCode());
        String update = String.format("{$set: %s }", skuModel.toUpdateString("skus.$."));
        String collectionName = getCollectionName(channelId);
        return mongoTemplate.updateFirst(query, update, collectionName);
    }

    /**
     * 根据Id返回多条产品数据
     */
    public List<CmsBtProductModel> selectProductByIds(List<Long> ids, String channelId) {
        if (ids == null || ids.size() == 0) {  // 对于list千万不要返回null
            return Collections.emptyList();
        }
        String idsStr = Joiner.on(",").join(ids);

        String query = "{prodId:{$in:[" + idsStr + "]}}";
        return select(query, channelId);
    }

    /**
     * 根据codes返回多条产品数据
     */
    public List<CmsBtProductModel> selectProductByCodes(List<String> codes, String channelId) {
        if (codes == null || codes.size() == 0) {
            return Collections.emptyList();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codes.size(); i++) {
            if (i == 0) {
                sb.append("\"").append(codes.get(i)).append("\"");
            } else {
                sb.append(", \"").append(codes.get(i)).append("\"");
            }
        }

        String query = "{\"common.fields.code\":{\"$in\":[" + sb.toString() + "]}}";
        return select(query, channelId);
    }

    /**
     * 根据codes返回一条产品数据
     */
    public CmsBtProductModel selectByCode(String code, String channelId) {
        String query = "{\"common.fields.code\":\"" + code + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 根据skuCode返回一条产品数据(忽略大小写)
     */
    public CmsBtProductModel selectBySkuIgnoreCase(String skuCode, String channelId) {
        String query = "{\"common.skus.skuCode\":{$regex:\"^" + skuCode + "$\",$options:'i'}}";
        return selectOneWithQuery(query, channelId);
    }

    public List<CmsBtProductBean> selectBean(JongoQuery queryObject, String channelId) {
        return mongoTemplate.find(queryObject, CmsBtProductBean.class, getCollectionName(channelId));
    }

    /**
     * 批量更新记录
     *
     * @param channelId 渠道ID
     * @param bulkList  更新条件
     * @param modifier  更新者
     * @param key       MongoKey $pop,$set,$push,$addToSet
     * @return 运行结果
     */
    public BulkWriteResult bulkUpdateWithMap(String channelId, List<BulkUpdateModel> bulkList, String modifier, String key) {
        return bulkUpdateWithMap(channelId, bulkList, modifier, key, false);
}

    @Override
    public WriteResult update(BaseMongoModel model) {
        throw new BusinessException("not suppert");
//         return super.update(model);
    }

    @Deprecated
    public WriteResult updateByModel(BaseMongoModel model) {
        return super.update(model);
    }

    /**
     * 检查该产品的数据是否已经准备完成.
     *
     * @param channelId String
     * @param productId Long
     * @return boolean
     */
    public boolean checkProductDataIsReady(String channelId, Long productId) {

        JongoQuery jongoQuery = new JongoQuery();
        jongoQuery.setQuery(String.format("{prodId: %s, batchField.switchCategory: 1}", productId));
        jongoQuery.setProjectionExt("prodId");

        List<CmsBtProductModel> result = select(jongoQuery, channelId);

        return result.size() <= 0;
    }

    //执行大小写不敏感的匹配
    public static final String FieldStatusEqArrpoved = "{'fields.status':{ $regex: '^approved$', $options: 'i' }}";

    @Deprecated
    public long countByFieldStatusEqualApproved(String channelId) {
        throw new BusinessException("not used");
//        return countByQuery(FieldStatusEqArrpoved, channelId);
    }
//
//    private static final String PriceNotEqualQuery =
//            "{$where: 'function() {return (this.skus||[]).some(function(obj){return obj.priceRetail != obj.priceSale; }) }'}";
//
//    /**
//     * 查询priceSale和priceRetail不相等的products
//     */
//    public List<CmsBtProductModel> selectByRetailSalePriceNonEqual(String channelId) {
//        return select(PriceNotEqualQuery, channelId);
//    }


    /**
     * 删除Product对应的店铺内自定义分类
     */
    public List<CmsBtProductModel> deleteSellerCat(String channelId, CmsBtSellerCatModel catModel, int cartId, String modifier) {
        String queryStr = "{'channelId':'" + channelId + "','platforms.P" + cartId + ".sellerCats.cId':'" + catModel.getCatId() + "'}";

        List<CmsBtProductModel> allProduct = select(queryStr, channelId);


        for (CmsBtProductModel product : allProduct) {

            List<CmsBtProductModel_SellerCat> sellerCatList = product.getPlatform(cartId).getSellerCats();

            for (int i = sellerCatList.size() - 1; i >= 0; i--) {
                if (sellerCatList.get(i).getcId().equals(catModel.getCatId())) {
                    sellerCatList.remove(i);
                    break;
                }
            }


            HashMap<String, Object> rsMap = new HashMap<>();
            rsMap.put("platforms.P" + cartId + ".sellerCats", sellerCatList);
            rsMap.put("modifier", modifier);
            rsMap.put("modified", DateTimeUtil.getNow());

            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("prodId", product.getProdId());

            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("$set", rsMap);

            update(product.getChannelId(), queryMap, updateMap);
        }

        return allProduct;
    }

    /**
     * 更新产品的店铺内分类数据
     *
     * @param product
     * @param catModel
     */
    private void updateSellerCat(CmsBtProductModel product, CmsBtSellerCatModel catModel, int cartId, String modifier) {

        List<CmsBtProductModel_SellerCat> sellerCatList = product.getPlatform(cartId).getSellerCats();

        for (CmsBtProductModel_SellerCat cat : sellerCatList) {
            List<String> cIds = cat.getcIds();
            List<String> cNames = cat.getcNames();
            String cId = catModel.getCatId();
            //如果包含修改过的cId,则需要修改cName和cNames
            if (cIds.stream().filter(w -> w.equals(cId)).count() > 0) {
                for (int i = 0; i < cIds.size(); i++) {
                    if (cIds.get(i).equals(cId)) {
                        cNames.set(i, catModel.getCatName());
                        break;
                    }
                }
                cat.setcName(Joiner.on(">").join(cNames));
            }
        }

        HashMap<String, Object> rsMap = new HashMap<>();
        rsMap.put("platforms.P" + cartId + ".sellerCats", sellerCatList);
        rsMap.put("modifier", modifier);
        rsMap.put("modified", DateTimeUtil.getNow());

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("prodId", product.getProdId());

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("$set", rsMap);

        update(product.getChannelId(), queryMap, updateMap);
    }


    /**
     * 更新所有产品的店铺内分类数据
     *
     * @param channelId
     * @param catList
     * @return
     */
    public List<CmsBtProductModel> updateSellerCat(String channelId, List<CmsBtSellerCatModel> catList, int cartId, String userName) {
        if (catList != null) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < catList.size(); i++) {
                if (i == 0) {
                    sb.append("'").append(catList.get(i).getCatId()).append("'");
                } else {
                    sb.append(", '").append(catList.get(i).getCatId()).append("'");
                }

            }

            String queryStr = "{'channelId':'" + channelId + "','platforms.P" + cartId + ".sellerCats.cIds':" + "{ '$in': [" + sb.toString() + "]}}";

            List<CmsBtProductModel> allProduct = select(queryStr, channelId);

            for (CmsBtProductModel model : allProduct) {
                for (CmsBtSellerCatModel sellerCat : catList) {
                    updateSellerCat(model, sellerCat, cartId, userName);
                }
            }
            return allProduct;
        }
        return null;
    }

    /**
     * 移行数据使用,取得老的product数据
     *
     * @return
     */
    public List<OldCmsBtProductModel> selectOldProduct(String channelId, List<String> codes) {

        JongoQuery jongoQuery = new JongoQuery();
        if (codes.size() > 0) {
            jongoQuery.setQuery("{\"fields.code\" : { $in : #}}");
            jongoQuery.setParameters(codes);
        }
        return mongoTemplate.find(jongoQuery, OldCmsBtProductModel.class, "cms_bt_product_c" + channelId);
    }

    /**
     * 根据类目id找出所有code(检索时带上排序条件)
     *
     * @param sortKey 排序项目
     * @param sortType 排序类型  1：升序  -1：降序
     * @param expCodes 不要检索出来的code列表
     */
    public List<String> selectListCodeBySellerCat(String channelId, int cartId, String catId, String sortKey, Integer sortType, List<String> expCodes) {
        JongoQuery jongoQuery = new JongoQuery();
        // modified by morse.lu 2016/11/30 start
//        jongoQuery.setQuery(String.format("{\"channelId\":#, \"platforms.P%s.sellerCats.cIds\":#, \"platforms.P%s.pNumIId\":{$nin: ['', null]}, \"platforms.P%s.pStatus\":'%s'}", cartId, cartId, cartId, CmsConstants.PlatformStatus.OnSale.name()));
//        jongoQuery.setParameters(channelId, catId);
        jongoQuery.addQuery(String.format("{\"channelId\":#}, {\"platforms.P%s.sellerCats.cIds\":#}, {\"platforms.P%s.pNumIId\":{$nin: ['', null]}}, {\"platforms.P%s.pStatus\":'%s'}", cartId, cartId, cartId, CmsConstants.PlatformStatus.OnSale.name()));
        jongoQuery.addParameters(channelId, catId);
        if (ListUtils.notNull(expCodes)) {
            jongoQuery.addQuery("{'common.fields.code':{$nin:#}}");
            jongoQuery.addParameters(expCodes);
        }
        // modified by morse.lu 2016/11/30 end
        jongoQuery.setProjection("{\"common.fields.code\": 1}");
        // modified by morse.lu 2016/11/30 start
//        jongoQuery.setSort(String.format("{\"platforms.P%s.pPublishTime\":-1}", cartId)); // 暂定pPublishTime
        if (!StringUtils.isEmpty(sortKey)) {
            jongoQuery.setSort(String.format("{%s:%s}", sortKey, sortType));
        } else {
            jongoQuery.setSort("{prodId:-1}");
        }
        // modified by morse.lu 2016/11/30 start

        List<CmsBtProductModel> products = select(jongoQuery, channelId);

        List<String> codes = new ArrayList<>();
        if (ListUtils.notNull(products)) {
            products.forEach(p -> codes.add(p.getCommon().getFields().getCode()));
        }

        return codes;
    }

    public void removeTagByCodes(String channelId, List<String> codes, int tagId) {
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{\"common.fields.code\":{$in:#}}");
        updObj.setQueryParameters(codes);
        updObj.setUpdate("{$pull:{\"tags\":{$regex:\"-" + tagId + "-\"}}}");
        String collectionName = getCollectionName(channelId);
        mongoTemplate.updateMulti(updObj, collectionName);
    }

    public void updateUsPlatforms(String channelId,
                                  String code,
                                  Map<String, CmsBtProductModel_UsPlatform_Cart> usPlatforms,
                                  String modifier) {
        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"common.fields.code\": #}, {\"modifier\": #}");
        jongoUpdate.setQueryParameters(code, modifier);
        jongoUpdate.setUpdate("{$set: {\"usPlatforms\": #}}");
        jongoUpdate.setUpdateParameters(usPlatforms);
        String collectionName = getCollectionName(channelId);
        mongoTemplate.updateMulti(jongoUpdate, collectionName);
    }
}
