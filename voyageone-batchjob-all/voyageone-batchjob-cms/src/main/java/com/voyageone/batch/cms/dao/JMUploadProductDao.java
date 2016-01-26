package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.JmBtProductImportModel;
import com.voyageone.batch.cms.model.JmBtSkuImportModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@Repository
public class JMUploadProductDao extends BaseDao {

    public List<JmBtProductImportModel> getNotUploadProduct(Integer count) {
        return selectList("get_jm_upload_product", count);
    }

    public int updateJMProduct(JmBtProductImportModel jmBtProductImportModel) {
        return updateTemplate.update("update_jm_bt_product", jmBtProductImportModel);
    }

    public int insertJMProduct(JmBtProductImportModel jmBtProductImportModel) {
        return updateTemplate.update("insert_jm_bt_product", jmBtProductImportModel);
    }

    public int insertJMProductSkuList(List<JmBtSkuImportModel> skuList, String modifier) {
        skuList.forEach(jmBtSkuImportModel -> {
            jmBtSkuImportModel.setCreater(modifier);
            jmBtSkuImportModel.setModifier(modifier);
        });
        return updateTemplate.update("insert_skus", skuList);
    }
    public int delJMProductSkuByCode(String channelId, String productCode) {
        Map<String,String> param = new HashMap<>();
        param.put("channelId",channelId);
        param.put("productCode",productCode);
        return updateTemplate.delete("delete_jm_bt_sku_by_code", param);
    }

}
