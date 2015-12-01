package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.base.dao.mongodb.BaseMongoDao;
import com.voyageone.cms.service.model.CmsBtFeedInfoModel;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class CmsBtProductDao extends BaseMongoDao {

    @Override
    public Class getEntityClass() {
        return CmsBtFeedInfoModel.class;
    }

    /**
     * 获取商品 根据ID获
     * @param channelId
     * @param prodId
     * @return
     */
    public CmsBtProductModel selectProductById(String channelId, int prodId) {
        String query = "{\"prodId\":" + prodId + "}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品 根据Code
     * @param channelId
     * @param code
     * @return
     */
    public CmsBtProductModel selectProductByCode(String channelId, String code) {
        String query = "{\"field.code\":\"" + code + "\"}";
        return selectOneWithQuery(query, channelId);
    }

    /**
     * 获取商品List 根据GroupId
     * @param channelId
     * @param groupId
     * @return
     */
    public List<CmsBtProductModel> selectProductByGroupId(String channelId, int groupId) {
        String query = "{\"group.platforms.groupId\":" + groupId + "}";
        return select(query, channelId);
    }

    /**
     * 获取SKUList 根据prodId
     * @param channelId
     * @param prodId
     * @return
     */
    public List<CmsBtProductModel_Sku>  selectSKUById(String channelId, int prodId) throws IOException {
        String query = "{\"prodId\":" + prodId + "}";
        CmsBtProductModel product = selectOneWithQuery(query, channelId);
        if (product != null) {
            return product.getSkus();
        }
        //Object jsonObj = JsonPath.parse(JacksonUtil.bean2Json(product)).json();
        //List<Map> authors = JsonPath.read(jsonObj, "$.skus.*");
        return null;
    }

}
