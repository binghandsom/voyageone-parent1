package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Ethan Shi on 2016/5/23.
 */
@Service
public class SellerCatService extends BaseService {

    @Autowired
    private CmsBtSellerCatDao cmsBtSellerCatDao;

    /**
     * 取得Category Tree 根据channelId， cartId
     */
    public List<CmsBtSellerCatModel> getSellerCatsByChannelCart(String channelId, int cartId) {
        return cmsBtSellerCatDao.selectByChannelCart(channelId, cartId);
    }

    public String addSellerCat(String chnnnelId, int cartId, String cName, String parentCId) {
        return null;
    }

    public String updateSellerCat(String chnnnelId, int cartId, String cName, String parentCId) {
        return null;
    }

    public String deleteSellerCat(String chnnnelId, int cartId, String cName, String parentCId) {
        return null;
    }

}
