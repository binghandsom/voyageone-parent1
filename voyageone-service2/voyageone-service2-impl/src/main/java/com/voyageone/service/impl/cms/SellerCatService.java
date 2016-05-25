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

    /**
     *
     * @param channelId
     * @param cartId
     * @param cName
     * @param parentCId
     * @param cId
     * @param creator
     */
    public void addSellerCat(String channelId, int cartId, String cName, String parentCId, String cId, String creator) {
        cmsBtSellerCatDao.add(channelId, cartId, cName, parentCId, cId, creator);
    }

    /**
     *
     * @param channelId
     * @param cartId
     * @param cName
     * @param cId
     * @param modifier
     */
    public void  updateSellerCat(String channelId, int cartId, String cName, String cId, String modifier) {
        cmsBtSellerCatDao.update(channelId, cartId, cName,cId, modifier);
    }

    /**
     *
     * @param chnnnelId
     * @param cartId
     * @param parentCId
     * @param cId
     */
    public void deleteSellerCat(String chnnnelId, int cartId, String parentCId, String cId) {
        cmsBtSellerCatDao.delete(chnnnelId, cartId, parentCId, cId);
    }

}
