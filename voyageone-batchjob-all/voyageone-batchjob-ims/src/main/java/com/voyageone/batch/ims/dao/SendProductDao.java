package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.SendProductBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simin on 2016/1/26.
 */
@Repository
public class SendProductDao extends BaseDao{
    public List<SendProductBean> selectSendProduct (String channel_id,int rowCount){
        Map<String,Object>dataMap=new HashMap<>();
        dataMap.put("channel_id",channel_id);
        dataMap.put("rowCount",rowCount);
        List<SendProductBean>sendProduct=selectList(Constants.DAO_NAME_SPACE_IMS+"jumei_selectSendProduct",dataMap);
        if(sendProduct.size()!=0){
            return  sendProduct;
        }
        return null;
    }

    public void updateSendProductSend_flg(String modified,String modifier,SendProductBean sendProductBean){
        Map<String,Object>mdoMap=new HashMap<>();
        mdoMap.put("channel_id",sendProductBean.getChannel_id());
        mdoMap.put("product_code",sendProductBean.getProduct_code());
        mdoMap.put("modified",modified);
        mdoMap.put("modifier",modifier);
        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS+"jumei_updateSendProductOK",mdoMap);
    }
}
