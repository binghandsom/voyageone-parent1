package com.voyageone.service.dao.cms.mongo;

import com.google.common.base.Joiner;
import com.mongodb.*;
import com.voyageone.base.dao.mongodb.BaseMongoChannelDao;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

//import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Group_Platform;

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

    public List<CmsBtProductModel> selectProductByIds( List<Long> ids,String channelId) {
        if (ids == null || ids.size() == 0) {  // 对于list千万不要返回null
            return Collections.emptyList();
        }
        String idsStr = Joiner.on(",").join(ids);

        String query = "{prodId:{$in:[" + idsStr + "]}}";
        return select(query, channelId);
    }

    /**
     * 根据codes返回多条产品数据
     * @param codes
     * @param channelId
     * @return
     */
    public List<CmsBtProductModel> selectProductByCodes( List<String> codes,String channelId) {
        if (codes == null || codes.size() == 0) {  // 对于list千万不要返回null
            return Collections.emptyList();
        }
        String idsStr = Joiner.on(",").join(codes);

        String query = "{prodId:{$in:[" + idsStr + "]}}";
        return select(query, channelId);
    }

    public List<CmsBtProductBean> selectBean(JomgoQuery queryObject, String channelId) {
        return mongoTemplate.find(queryObject, CmsBtProductBean.class, getCollectionName(channelId));
    }

    /**
     * 批量更新记录
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
        // return update(model);
    }

    /**
     * 检查该产品的数据是否已经准备完成.
     * @param channelId String
     * @param productId Long
     * @return boolean
     */
    public boolean checkProductDataIsReady(String channelId, Long productId){

        JomgoQuery jomgoQuery = new JomgoQuery();
        jomgoQuery.setQuery(String.format("{prodId: %s, batchField.switchCategory: 1}",productId));
        jomgoQuery.setProjectionExt("prodId");

        List<CmsBtProductModel> result = select(jomgoQuery, channelId);

        return result.size() <= 0;
    }
     //执行大小写不敏感的匹配
    public static final String FieldStatusEqArrpoved = "{'fields.status':{ $regex: '^approved$', $options: 'i' }}";

    public long countByFieldStatusEqualApproved(String channelId) {
        return countByQuery(FieldStatusEqArrpoved,channelId);
    }

    public List<CmsBtProductModel> selectByFieldStatusEqualApproved(String channelId) {

        return select(FieldStatusEqArrpoved, channelId);
    }

    private static final String PriceNotEqualQuery=
            "{$where: 'function() {return (this.skus||[]).some(function(obj){return obj.priceRetail != obj.priceSale; }) }'}";

    /**
     * 查询priceSale和priceRetail不相等的products
     * @return
     */
    public List<CmsBtProductModel> selectByRetailSalePriceNonEqual(String channelId) {
        return select(PriceNotEqualQuery, channelId);
    }

    public void updateSellerCat(String channelId, List<BulkUpdateModel> bulkList, String modifier, String $set){

    };


    /**
     * 删除Product对应的店铺内自定义分类
     * @param channelId
     * @param catModel
     */
    public void deleteSellerCat(String channelId, CmsBtSellerCatModel catModel)
    {
        String queryStr = "{'channelId':'" + channelId + "','sellerCats.cIds':" + catModel.getCatId() + "}";

        List<CmsBtProductModel> allProduct = select(queryStr, channelId);

        for (CmsBtProductModel model:allProduct) {

            List<String> cIds = model.getSellerCats().getCid();
            List<String> cNames = model.getSellerCats().getCNames();
            List<String> fullCNames = model.getSellerCats().getFullCNames();
            List<String> fullCIds = model.getSellerCats().getFullCIds();

            //删除cId
            for(int i = cIds.size()-1; i >= 0; i--)
            {
                if(cIds.get(i).equals(catModel.getCatId()))
                {
                    cIds.remove(i);
                    break;
                }
            }

            //删除cName
            for(int i = cNames.size()-1; i >= 0; i--)
            {
                if(cNames.get(i).equals(catModel.getCatName()))
                {
                    cNames.remove(i);
                    break;
                }
            }

            //删除fullCNames
            for(int i = fullCNames.size()-1; i >= 0; i--)
            {
                if(fullCNames.get(i).equals(catModel.getCatPath()))
                {
                    fullCNames.remove(i);
                    break;
                }
            }

            //删除fullCIds
            for(int i = fullCIds.size()-1; i >= 0; i--)
            {
                if(fullCIds.get(i).equals(catModel.getCatId()) || fullCIds.get(i).equals(catModel.getFullCatCId()))
                {
                    fullCIds.remove(i);
                    break;
                }
            }
        }
    }


    private void updateSellerCat(CmsBtProductModel product, CmsBtSellerCatModel catModel)
    {
        List<String> cIds = product.getSellerCats().getCid();
        List<String> cNames = product.getSellerCats().getCNames();
        List<String> fullCNames = product.getSellerCats().getFullCNames();
        List<String> fullCIds = product.getSellerCats().getFullCIds();
        //找到cId
        for(int i = cIds.size()-1; i >= 0; i--)
        {
            if(cIds.get(i).equals(catModel.getCatId()))
            {
                cNames.set(i , catModel.getCatName());
                break;
            }
        }
        for(int i = fullCIds.size()-1; i >= 0; i--)
        {
            if(fullCIds.get(i).equals(catModel.getCatId()) || fullCIds.get(i).equals(catModel.getFullCatCId()))
            {
                fullCNames.set(i , catModel.getCatPath());
                break;
            }
        }
    }

    public void updateSellerCat(String channelId, List<CmsBtSellerCatModel> catList) {
        if (catList != null) {
            StringBuffer sb = new StringBuffer();
            int i = 0;

            for (; i < catList.size(); i++) ;
            {
                if (i == 0) {
                    sb.append("'").append(catList.get(i).getCatId()).append("'");
                } else {
                    sb.append(", '").append(catList.get(i).getCatId()).append("'");
                }

            }

            String queryStr = "{'channelId':'" + channelId + "','sellerCats.cIds':" + "'$elemMatch': { '$in': [" + sb.toString() + "]}";

            List<CmsBtProductModel> allProduct = select(queryStr, channelId);

            for (CmsBtProductModel model : allProduct) {
                for (CmsBtSellerCatModel sellerCat : catList) {
                    updateSellerCat(model, sellerCat);
                }
            }
        }
    }
}
