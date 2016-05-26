package com.voyageone.service.impl.cms;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.jd.open.api.sdk.internal.util.StringUtil;
import com.taobao.api.domain.SellerCat;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.tmall.service.TbSellerCatService;
import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * 保存整组分类树
     * @param allCats
     */
    public void save(List<CmsBtSellerCatModel> allCats)
    {
        for (CmsBtSellerCatModel model: allCats) {
            cmsBtSellerCatDao.insert(model);
        }

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

        String cId = "";

//        String shopCartId = shopBean.getCart_id();
//
//
//        if(isJDPlatform(shopCartId))
//        {
//            cId = jdShopService.addShopCategory(shopBean, cName, parentCId);
//        }
//        else if (isTMPlatform(shopCartId))
//        {
//            cId = tbSellerCatService.addSellerCat(shopBean, cName, parentCId);
//        }

        //TestCode
        Random random = new Random();
        cId = String.valueOf(random.nextInt(1000) + 1000);

        if(!StringUtils.isNullOrBlank2(cId)) {
            cmsBtSellerCatDao.add(channelId, cartId, cName, parentCId, cId, creator);
        }
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

//        String shopCartId = shopBean.getCart_id();
//        if(isJDPlatform(shopCartId))
//        {
//            jdShopService.updateShopCategory(shopBean,cId, cName);
//        }
//        else if (isTMPlatform(shopCartId))
//        {
//            tbSellerCatService.updateSellerCat(shopBean, cId, cName);
//        }

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

//        String shopCartId = shopBean.getCart_id();
//
//        if(isJDPlatform(shopCartId))
//        {
//            jdShopService.deleteShopCategory(shopBean, cId);
//        }
//        else if (isTMPlatform(shopCartId))
//        {
//            throw new BusinessException(shopBean.getShop_name(), "Unsupported Method.");
//        }
        cmsBtSellerCatDao.delete(channelId, cartId, parentCId, cId);
    }

    /**
     * 从TMALL，JD平台获取店铺自定义类目树
     *
     * @param channelId
     * @param cartId
     * @param creator
     * @return
     */
    public List<CmsBtSellerCatModel> refreshSellerCat(String channelId, int cartId, String creator)
    {
        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String shopCartId = shopBean.getCart_id();

        //JD TEST CODE
//        shopCartId = "24";
//
//        shopBean.setAppKey("BFA3102EFD4B981E9EEC2BE32DF1E44E");
//        shopBean.setAppSecret("90742900899f49a5acfaf3ec1040a35c");
//        shopBean.setSessionKey("8bac1a4d-3853-446b-832d-060ed9d8bb8c");
//        shopBean.setApp_url("https://api.jd.com/routerjson");


        List<CmsBtSellerCatModel> sellerCat = new ArrayList<>();

        if(isJDPlatform(shopCartId))
        {
            List<ShopCategory> shopCategory = jdShopService.getShopCategoryList(shopBean);
            sellerCat = formatJDModel(shopCategory, channelId, cartId, creator);

        }
        else if (isJDPlatform(shopCartId) )
        {
            List<SellerCat> sellerCatList= tbSellerCatService.getSellerCat(shopBean);
            sellerCat = formatTMModel(sellerCatList, channelId, cartId, creator);
        }
        return  convert2Tree(sellerCat);

    }


    private List<CmsBtSellerCatModel> formatTMModel(List<SellerCat> list, String channelId, int cartId, String creator)
    {
        List<CmsBtSellerCatModel> result = new ArrayList<>() ;

        for (SellerCat model: list) {
            CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel();
            cmsBtSellerCatModel.setCatId(String.valueOf(model.getCid()));
            cmsBtSellerCatModel.setCatName(model.getName());
            cmsBtSellerCatModel.setParentCatId(String.valueOf(model.getParentCid()));
            cmsBtSellerCatModel.setChannelId(channelId);
            cmsBtSellerCatModel.setCartId(cartId);
            String now = DateTimeUtil.getNow();
            cmsBtSellerCatModel.setCreated(now);
            cmsBtSellerCatModel.setModified(now);
            cmsBtSellerCatModel.setCreater(creator);
            cmsBtSellerCatModel.setModifier(creator);

            result.add(cmsBtSellerCatModel);
        }


        return result;
    }


    /**
     * 将JD店铺自定义分类Model转换成CmsBtSellerCatModel
     *
     * @param list
     * @param channelId
     * @param cartId
     * @param creator
     * @return
     */
    private List<CmsBtSellerCatModel> formatJDModel(List<ShopCategory> list, String channelId, int cartId, String creator)
    {

        List<CmsBtSellerCatModel> result = new ArrayList<>() ;

        for (ShopCategory model: list) {
            CmsBtSellerCatModel cmsBtSellerCatModel = new CmsBtSellerCatModel();
            cmsBtSellerCatModel.setCatId(String.valueOf(model.getCid()));
            cmsBtSellerCatModel.setCatName(model.getName());
            cmsBtSellerCatModel.setParentCatId(String.valueOf(model.getParentId()));
            cmsBtSellerCatModel.setChannelId(channelId);
            cmsBtSellerCatModel.setCartId(cartId);
            String now = DateTimeUtil.getNow();
            cmsBtSellerCatModel.setCreated(now);
            cmsBtSellerCatModel.setModified(now);
            cmsBtSellerCatModel.setCreater(creator);
            cmsBtSellerCatModel.setModifier(creator);

            result.add(cmsBtSellerCatModel);
        }


        return result;
    }


    /**
     * 将店铺自定义分类列转成一组树
     *
     * @param sellCatList
     * @return
     */
    private List<CmsBtSellerCatModel> convert2Tree(List<CmsBtSellerCatModel> sellCatList) {
        List<CmsBtSellerCatModel> roots = findRoots(sellCatList);
        List<CmsBtSellerCatModel> notRoots = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(sellCatList, roots);
        for (CmsBtSellerCatModel root : roots) {
            List<CmsBtSellerCatModel> children = findChildren(root, notRoots);
            root.setChildren(children);
        }
        return roots;

    }

    /**
     * 查找所有子节点
     * @param root
     * @param allNodes
     * @return
     */
    private List<CmsBtSellerCatModel> findChildren(CmsBtSellerCatModel root, List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> children = new ArrayList<>();

        for (CmsBtSellerCatModel comparedOne : allNodes) {
            if (comparedOne.getParentCatId().equals(root.getCatId())) {
                children.add(comparedOne);
                comparedOne.setCatPath(root.getCatPath() + ">" + comparedOne.getCatName());
                comparedOne.setFullCatCId(root.getFullCatCId() + "-" + comparedOne.getCatId());
            }
        }
        root.setChildren(children);
        if(children.size() > 0)
        {
            root.setIsParent(1);
        }
        else
        {
            root.setIsParent(0);
        }

        List<CmsBtSellerCatModel> notChildren = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(allNodes, children);

        for (CmsBtSellerCatModel child : children) {
            List<CmsBtSellerCatModel> tmpChildren = findChildren(child, notChildren);

            child.setChildren(tmpChildren);
        }

        return  children;
    }

    /**
     * 查找所有根节点
     * @param allNodes
     * @return
     */
    private List<CmsBtSellerCatModel> findRoots(List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> results = new ArrayList<>();
        for (CmsBtSellerCatModel node : allNodes) {
            if(node.getParentCatId().equals("0"))
            {
                results.add(node);
                node.setCatPath(node.getCatName());
                node.setFullCatCId(node.getCatId());
            }
        }
        return results;
    }


    private boolean isJDPlatform(String shopCartId)
    {
        if(shopCartId.equals(CartEnums.Cart.JD.getId())  ||  shopCartId.equals(CartEnums.Cart.JG.getId()) ||
                shopCartId.equals(CartEnums.Cart.JGJ.getId())||  shopCartId.equals(CartEnums.Cart.JGY.getId()) )
        {
            return true;
        }
        return  false;
    }

    private boolean isTMPlatform(String shopCartId)
    {
        if(shopCartId.equals(CartEnums.Cart.TM.getId())  ||  shopCartId.equals(CartEnums.Cart.TB.getId()) ||
                shopCartId.equals(CartEnums.Cart.TG.getId() ) ||  shopCartId.equals(CartEnums.Cart.TMM.getId()) )
        {
            return true;
        }
        return  false;
    }


}
