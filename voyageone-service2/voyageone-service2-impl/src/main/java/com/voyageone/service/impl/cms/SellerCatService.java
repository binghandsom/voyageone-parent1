package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.tmall.service.TbSellerCatService;
import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


/**
 * Created by Ethan Shi on 2016/5/23.
 */
@Service
public class SellerCatService extends BaseService {

    @Autowired
    private CmsBtSellerCatDao cmsBtSellerCatDao;

    @Autowired
    private JdShopService jdShopService;

    @Autowired
    private TbSellerCatService tbSellerCatService;

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
     * @param creator
     */
    public void addSellerCat(String channelId, int cartId, String cName, String parentCId, String creator) {

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String shopCartId = shopBean.getCart_id();

        String cId = "";

        if(shopCartId.equals(CartEnums.Cart.JD.getId())  ||  shopCartId.equals(CartEnums.Cart.JG.getId()) )
        {
            cId = jdShopService.addShopCategory(shopBean, cName, parentCId);
        }
        else if (shopCartId.equals(CartEnums.Cart.TM.getId())  ||  shopCartId.equals(CartEnums.Cart.TB.getId()) ||  shopCartId.equals(CartEnums.Cart.TG.getId() ))
        {
            cId = tbSellerCatService.addSellerCat(shopBean, cName, parentCId);
        }

        //TestCode
        Random random = new Random();
        cId = String.valueOf(random.nextInt(1000) + 1000);

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

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String shopCartId = shopBean.getCart_id();


        if(shopCartId.equals(CartEnums.Cart.JD.getId())  ||  shopCartId.equals(CartEnums.Cart.JG.getId()) )
        {
            jdShopService.updateShopCategory(shopBean,cId, cName);
        }
        else if (shopCartId.equals(CartEnums.Cart.TM.getId())  ||  shopCartId.equals(CartEnums.Cart.TB.getId()) ||  shopCartId.equals(CartEnums.Cart.TG.getId() ))
        {
            tbSellerCatService.updateSellerCat(shopBean, cId, cName);
        }

        cmsBtSellerCatDao.update(channelId, cartId, cName, cId, modifier);
    }

    /**
     *
     * @param channelId
     * @param cartId
     * @param parentCId
     * @param cId
     */
    public void deleteSellerCat(String channelId, int cartId, String parentCId, String cId) {

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String shopCartId = shopBean.getCart_id();

        if(shopCartId.equals(CartEnums.Cart.JD.getId())  ||  shopCartId.equals(CartEnums.Cart.JG.getId()) )
        {
            jdShopService.deleteShopCategory(shopBean, cId);
        }
        else if (shopCartId.equals(CartEnums.Cart.TM.getId())  ||  shopCartId.equals(CartEnums.Cart.TB.getId()) ||  shopCartId.equals(CartEnums.Cart.TG.getId() ))
        {
            throw new BusinessException(shopBean.getShop_name(), "Unsupported Method.");
        }
        cmsBtSellerCatDao.delete(channelId, cartId, parentCId, cId);
    }

}
