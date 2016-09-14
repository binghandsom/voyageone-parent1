package com.voyageone.service.impl.cms;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.taobao.api.domain.SellerCat;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiCheckField;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.components.tmall.service.TbSellerCatService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Ethan Shi on 2016/5/23.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @version 2.1.0
 */
@Service
public class SellerCatService extends BaseService {

    private static final String DEFAULT_SELLER_CAT_DEPTH = "2";
    private static final String DEFAULT_SELLER_CAT_CNT = "10";

    @Autowired
    private CmsBtSellerCatDao cmsBtSellerCatDao;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private JdShopService jdShopService;

    @Autowired
    private TbSellerCatService tbSellerCatService;

    @Autowired
    private TbItemService tbItemService;

    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;


    @Autowired
    private SxProductService sxProductService;


    /**
     * 获取店铺自定义分类的相关配置参数
     */
    public Map<String, Object> getSellerCatConfig(int cartId) {
        String cartIdStr = String.valueOf(cartId);
        Codes.reload();
        String depth = Codes.getCodeName("SELLER_CAT_MAX_DEPTH", cartIdStr);
        String cnt = Codes.getCodeName("SELLER_CAT_MAX_CNT", cartIdStr);

        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.isNullOrBlank2(depth)) {
            result.put("MAX_SELLER_CAT_DEPTH", depth);
        } else {
            result.put("MAX_SELLER_CAT_DEPTH", DEFAULT_SELLER_CAT_DEPTH);
        }

        if (!StringUtils.isNullOrBlank2(cnt)) {
            result.put("MAX_SELLER_CAT_CNT", cnt);
        } else {
            result.put("MAX_SELLER_CAT_CNT", DEFAULT_SELLER_CAT_CNT);
        }

        return result;
    }


    /**
     * 取得Category 根据channelId， cartId
     */
    public List<CmsBtSellerCatModel> getSellerCatsByChannelCart(String channelId, int cartId) {
        return getSellerCatsByChannelCart(channelId, cartId, true);
    }


    /**
     * 取得Category Tree 根据channelId， cartId
     */
    public List<CmsBtSellerCatModel> getSellerCatsByChannelCart(String channelId, int cartId, boolean isTree) {

        if (isTree) {
            return cmsBtSellerCatDao.selectByChannelCart(channelId, cartId);
        } else {
            List<CmsBtSellerCatModel> result = new ArrayList<>();

            List<CmsBtSellerCatModel> treeList = cmsBtSellerCatDao.selectByChannelCart(channelId, cartId);

            for (CmsBtSellerCatModel node : treeList) {
                CmsBtSellerCatModel copyRoot = copyCmsBtSellerCatModel(node);
                result.add(copyRoot);

                result.addAll(findAllChildren(node));
            }

            return result;
        }
    }

    /**
     * clone 一个CmsBtSellerCatModel对象
     */
    private CmsBtSellerCatModel copyCmsBtSellerCatModel(CmsBtSellerCatModel node) {
        CmsBtSellerCatModel copyRoot = new CmsBtSellerCatModel();
        copyRoot.setCatId(node.getCatId());
        copyRoot.setCatName(node.getCatName());
        copyRoot.setParentCatId(node.getParentCatId());
        copyRoot.setChannelId(node.getChannelId());
        copyRoot.setCartId(node.getCartId());
        copyRoot.setCreated(node.getCreated());
        copyRoot.setModified(node.getModified());
        copyRoot.setCreater(node.getCreater());
        copyRoot.setModifier(node.getModifier());
        copyRoot.setFullCatId(node.getFullCatId());
        copyRoot.setCatPath(node.getCatPath());
        copyRoot.setChildren(new ArrayList<>());
        return copyRoot;
    }

    /**
     * 找出所有孩子节点
     */
    private List<CmsBtSellerCatModel> findAllChildren(CmsBtSellerCatModel root) {
        List<CmsBtSellerCatModel> result = new ArrayList<>();

        for (CmsBtSellerCatModel model : root.getChildren()) {

            CmsBtSellerCatModel cmsBtSellerCatModel = copyCmsBtSellerCatModel(model);
            result.add(cmsBtSellerCatModel);
            result.addAll(findAllChildren(model));
        }
        return result;
    }

    /**
     * 保存整组分类树
     */
    public void save(List<CmsBtSellerCatModel> allCats) {
        for (CmsBtSellerCatModel model : allCats) {
            cmsBtSellerCatDao.insert(model);
        }
    }

    /**
     * addSellerCat
     */
    public void addSellerCat(String channelId, int cartId, String cName, String parentCId, String creator) {


        List<CmsBtSellerCatModel>  sellerCats = getSellerCatsByChannelCart(channelId, cartId, false);
        if(isDuplicateNode(sellerCats,cName,parentCId))
        {
            throw  new BusinessException("重复的店铺内分类名!");
        }

        ShopBean shopBean = Shops.getShop(channelId, cartId);
        String cId = "";
        String shopCartId = shopBean.getCart_id();

        if (isJDPlatform(shopCartId)) {
            cId = jdShopService.addShopCategory(shopBean, cName, parentCId);
        } else if (isTMPlatform(shopCartId)) {
            cId = tbSellerCatService.addSellerCat(shopBean, cName, parentCId);
        }

        //TestCode
//        Random random = new Random();
//        cId = String.valueOf(random.nextInt(1000) + 1000);

        if (!StringUtils.isNullOrBlank2(cId)) {
            cmsBtSellerCatDao.add(channelId, cartId, cName, parentCId, cId, creator);
        }
    }

    /**
     * updateSellerCat
     */
    public void updateSellerCat(String channelId, int cartId, String cName, String cId, String modifier) {


        List<CmsBtSellerCatModel>  sellercats = getSellerCatsByChannelCart(channelId, cartId, false);
        CmsBtSellerCatModel currentNode = sellercats.stream().filter(w ->w.getCatId().equals(cId)).findFirst().get();
        currentNode.getParentCatId();

        if(isDuplicateNode(sellercats,cName,currentNode.getParentCatId()))
        {
            throw  new BusinessException("重复的店铺内分类!");
        }

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String shopCartId = shopBean.getCart_id();
        if (isJDPlatform(shopCartId)) {
            jdShopService.updateShopCategory(shopBean, cId, cName);
        } else if (isTMPlatform(shopCartId)) {
            tbSellerCatService.updateSellerCat(shopBean, cId, cName);
        }

        List<CmsBtSellerCatModel> changedList = cmsBtSellerCatDao.update(channelId, cartId, cName, cId, modifier);

        //更新product表中所有的店铺内分类
        if (changedList != null) {
            List<CmsBtProductModel> list = cmsBtProductDao.updateSellerCat(channelId, changedList, cartId, modifier);
            //插入上新表
            insert2SxWorkload(channelId, cartId, modifier, list);
        }
    }

    /**
     * deleteSellerCat
     */
    public void deleteSellerCat(String channelId, int cartId, String parentCId, String cId, String modifier) {

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        String shopCartId = shopBean.getCart_id();

        if (isJDPlatform(shopCartId)) {
            jdShopService.deleteShopCategory(shopBean, cId);
        } else if (isTMPlatform(shopCartId)) {
            //去TM平台取店铺分类
            List<SellerCat> sellerCatList = tbSellerCatService.getSellerCat(shopBean);
            if(sellerCatList != null) {
                if (sellerCatList.stream().filter(w -> w.getCid() == Long.valueOf(cId).longValue()).count() > 0) {
                    throw new BusinessException(shopBean.getShop_name() + ":请先到天猫后台删除店铺内分类后再在CMS中删除。");
                }
            }
        }


        CmsBtSellerCatModel deleted = cmsBtSellerCatDao.delete(channelId, cartId, parentCId, cId);
        //删除product表中所有的店铺内分类
        if (deleted != null) {
            List<CmsBtProductModel> list = cmsBtProductDao.deleteSellerCat(channelId, deleted, cartId, modifier);

            //插入上新表
            insert2SxWorkload(channelId, cartId, modifier, list);
        }
    }


    /**
     * 插入上新表
     */
    private void insert2SxWorkload(String channelId, int cartId, String modifier, List<CmsBtProductModel> list) {
        if (list != null) {
            for (CmsBtProductModel product : list) {
                sxProductService.insertSxWorkLoad(channelId, product.getCommon().getFields().getCode(), cartId, modifier);
            }
        }
    }

    /**
     * 从TMALL，JD平台获取店铺自定义类目树
     */
    public List<CmsBtSellerCatModel> refreshSellerCat(String channelId, int cartId, String creator) {
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

        if (isJDPlatform(shopCartId)) {
            List<ShopCategory> shopCategory = jdShopService.getShopCategoryList(shopBean);
            sellerCat = formatJDModel(shopCategory, channelId, cartId, creator);

        } else if (isTMPlatform(shopCartId)) {
            List<SellerCat> sellerCatList = tbSellerCatService.getSellerCat(shopBean);
            sellerCat = formatTMModel(sellerCatList, channelId, cartId, creator);
        }
        return convert2Tree(sellerCat);

    }

    public void refeshAllProduct(String channelId, int cartId, String creator) {

        ShopBean shopBean = Shops.getShop(channelId, 23);
//        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
//        shopBean.setAppKey("21008948");
//        shopBean.setAppSecret("0a16bd08019790b269322e000e52a19f");
//        shopBean.setSessionKey("6201d2770dbfa1a88af5acfd330fd334fb4ZZa8ff26a40b2641101981");
//        shopBean.setShop_name("Jewelry海外旗舰店");

        String shopCartId = shopBean.getCart_id();

        List<CmsBtSellerCatModel> sellerCat = new ArrayList<>();

        if (isTMPlatform(shopCartId)) {
            List<SellerCat> sellerCatList = tbSellerCatService.getSellerCat(shopBean);
            sellerCat = formatTMModel(sellerCatList, channelId, cartId, creator);
            convert2Tree(sellerCat);
        }


        //得到所有的product_group
        String query = "{\"numIId\": {\"$ne\": \"\"} }";
        List<CmsBtProductGroupModel> groupList = cmsBtProductGroupDao.select(query, channelId);
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        for (CmsBtProductGroupModel group : groupList) {


            List<CmsBtProductModel> productList = cmsBtProductDao.selectProductByCodes(group.getProductCodes(), channelId);
            String numIId = group.getNumIId();


            try {
                if (numIId != null) {
                    TbItemSchema schema = tbItemService.getUpdateSchema(shopBean, Long.valueOf(numIId));

                    List<Field> fields = schema.getFields();

                    List<String> cIds = new ArrayList<>();

                    for (Field field : fields) {
                        if ("seller_cids".equals(field.getId())) {
                            List<String> values = ((MultiCheckField) field).getDefaultValues();

                            for (String value : values) {
                                cIds.add(value);
                            }
                        }
                    }

                    for (CmsBtProductModel product : productList) {

                        List<Map<String, Object>> sellerCats = new ArrayList<>();

                        for (String pCId : cIds) {
                            CmsBtSellerCatModel leaf = sellerCat.stream().filter(w -> pCId.equals(w.getCatId())).findFirst().get();
                            Map<String, Object> model =  new HashMap<>();
                            model.put("cId", leaf.getCatId());
                            model.put("cName", leaf.getCatPath());
                            model.put("cIds", leaf.getFullCatId().split("-"));
                            model.put("cNames", leaf.getCatPath().split(">"));

                            sellerCats.add(model);
                        }

                        Map<String, Object> updateMap = new HashMap<>();
                        updateMap.put("platform.P"+cartId+".sellerCats" , sellerCats);
                        Map<String, Object> queryMap = new HashMap<>();
                        queryMap.put("prodId", product.getProdId());

                        BulkUpdateModel bulk = new BulkUpdateModel();
                        bulk.setUpdateMap(updateMap);
                        bulk.setQueryMap(queryMap);
                        bulkList.add(bulk);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, "", "$set");

    }


    /**
     * 将TM店铺自定义分类Model转换成CmsBtSellerCatModel
     */
    private List<CmsBtSellerCatModel> formatTMModel(List<SellerCat> list, String channelId, int cartId, String creator) {
        List<CmsBtSellerCatModel> result = new ArrayList<>();

        for (SellerCat model : list) {
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
     */
    private List<CmsBtSellerCatModel> formatJDModel(List<ShopCategory> list, String channelId, int cartId, String creator) {

        List<CmsBtSellerCatModel> result = new ArrayList<>();

        for (ShopCategory model : list) {
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
     */
    private List<CmsBtSellerCatModel> findChildren(CmsBtSellerCatModel root, List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> children = new ArrayList<>();

        for (CmsBtSellerCatModel comparedOne : allNodes) {
            if (comparedOne.getParentCatId().equals(root.getCatId())) {
                children.add(comparedOne);
                comparedOne.setCatPath(root.getCatPath() + ">" + comparedOne.getCatName());
                comparedOne.setFullCatId(root.getFullCatId() + "-" + comparedOne.getCatId());
            }
        }
        root.setChildren(children);
        if (!children.isEmpty()) {
            root.setIsParent(1);
        } else {
            root.setIsParent(0);
        }

        List<CmsBtSellerCatModel> notChildren = (List<CmsBtSellerCatModel>) CollectionUtils.subtract(allNodes, children);

        for (CmsBtSellerCatModel child : children) {
            List<CmsBtSellerCatModel> tmpChildren = findChildren(child, notChildren);

            child.setChildren(tmpChildren);
        }

        return children;
    }

    /**
     * 查找所有根节点
     */
    private List<CmsBtSellerCatModel> findRoots(List<CmsBtSellerCatModel> allNodes) {
        List<CmsBtSellerCatModel> results = new ArrayList<>();
        for (CmsBtSellerCatModel node : allNodes) {
            if ("0".equals(node.getParentCatId())) {
                results.add(node);
                node.setCatPath(node.getCatName());
                node.setFullCatId(node.getCatId());
            }
        }
        return results;
    }


    /**
     * 是京东平台
     */
    private boolean isJDPlatform(String shopCartId) {
        if (shopCartId.equals(CartEnums.Cart.JD.getId()) || shopCartId.equals(CartEnums.Cart.JG.getId()) ||
                shopCartId.equals(CartEnums.Cart.JGJ.getId()) || shopCartId.equals(CartEnums.Cart.JGY.getId())) {
            return true;
        }
        return false;
    }


    /**
     * 是天猫平台
     */
    private boolean isTMPlatform(String shopCartId) {
        if (shopCartId.equals(CartEnums.Cart.TM.getId()) || shopCartId.equals(CartEnums.Cart.TB.getId()) ||
                shopCartId.equals(CartEnums.Cart.TG.getId()) || shopCartId.equals(CartEnums.Cart.TT.getId())
                || shopCartId.equals(CartEnums.Cart.USTT.getId())) {
            return true;
        }
        return false;
    }


    /**
     * 判断是否是重复的节点，同一层的节点名不能重复
     * @param sellerCats
     * @param name
     * @param parentCId
     * @return
     */
    public boolean isDuplicateNode(List<CmsBtSellerCatModel>  sellerCats, String name , String parentCId)
    {
        if(sellerCats != null && sellerCats.size() > 0) {
            if (sellerCats.stream().filter(w -> w.getParentCatId().equals(parentCId) && w.getCatName().equals(name)).count() > 0) {
                return true;
            }
        }
        return false;
    }


}
