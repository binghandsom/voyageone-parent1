package com.voyageone.service.impl.cms;

import com.jd.open.api.sdk.domain.sellercat.ShopCategory;
import com.taobao.api.domain.SellerCat;
import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.MultiCheckField;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.BeanUtils;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdShopService;
import com.voyageone.components.tmall.service.TbItemSchema;
import com.voyageone.components.tmall.service.TbItemService;
import com.voyageone.components.tmall.service.TbSellerCatService;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsBtSellerCatDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.sx.CnCategoryService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by Ethan Shi on 2016/5/23.
 *
 * @author Ethan Shi
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
    @Autowired
    MongoSequenceService commSequenceMongoService;

    @Autowired
    CnSellerCatService cnSellerCatService;
    @Autowired
    private CnCategoryService cnCategoryService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;

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
    public void addSellerCat(String channelId, int cartId, String cName, String parentCId, Map<String, Object> mapping, String creator, String urlKey) {
        /*List<CmsBtSellerCatModel> sellerCats = getSellerCatsByChannelCart(channelId, cartId, false);
        if (isDuplicateNode(sellerCats, cName, parentCId, null)) {
            throw new BusinessException("重复的店铺内分类名!");
        }*/
        List<CmsBtSellerCatModel> sellerCats = getSellerCatsByChannelCart(channelId, cartId, true);
        if (isDuplicateNodeTree(sellerCats, cName, parentCId, null)) {
            throw new BusinessException("重复的店铺内分类名!");
        }
        // 如果urlKey不为空，则校验唯一性
        if (!StringUtils.isNullOrBlank2(urlKey)) {
            if (isDuplicateUrlKey(sellerCats, urlKey)) {
                throw new BusinessException("重复的目录urlKey!");
            }
        }

        ShopBean shopBean = Shops.getShop(channelId, cartId);
        if (shopBean == null && cartId >= 20) {
            throw new BusinessException("未配置店铺的销售平台!");
        }
        String cId = "";
        String shopCartId = shopBean.getCart_id();

        if (isJDPlatform(shopBean)) {
            cId = jdShopService.addShopCategory(shopBean, cName, parentCId);
        } else if (isTMPlatform(shopCartId)) {
            cId = tbSellerCatService.addSellerCat(shopBean, cName, parentCId);
        }
        // Liking店独立官网创建店铺内分类
        else if (shopCartId.equals(CartEnums.Cart.LCN.getId())) {
            ////  2016/9/23  独立官网 店铺内分类api  下周tom提供   需返回cId
            int size = getBrotherSize(sellerCats, parentCId); // 2016-12-07 新增节点时默认设置index为当前层级节点的最后一位
            Map<String, String> resultMap = cnSellerCatService.addSellerCat(channelId, parentCId, cName, urlKey, shopBean, size + 1);
            cId = resultMap.get("catId");
            urlKey = resultMap.get("urlKey"); // 如果url传入为空，新增时向官网新增时生成urlKey，所以此处再次取值
        }
        // sneakerhead旗舰店创建店铺内分类
        else if (shopCartId.equals(CartEnums.Cart.CN.getId())) {
            cId = cnCategoryService.addSnSellerCat(channelId, parentCId, cName, shopBean);
        }else if(cartId < 20){
            cId = Long.toString(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_CnShopCategory_ID));
        }
        if (!StringUtils.isNullOrBlank2(cId)) {
            // 2016-12-08 传入参数新增urlKey
            cmsBtSellerCatDao.add(channelId, cartId, cName, urlKey, mapping, parentCId, cId, creator);
        }
    }


    public void addSellerCat(String channelId, int cartId, String cName,String cId, String parentCId, Map<String, Object> mapping, String creator, String urlKey) {
        /*List<CmsBtSellerCatModel> sellerCats = getSellerCatsByChannelCart(channelId, cartId, false);
        if (isDuplicateNode(sellerCats, cName, parentCId, null)) {
            throw new BusinessException("重复的店铺内分类名!");
        }*/
        List<CmsBtSellerCatModel> sellerCats = getSellerCatsByChannelCart(channelId, cartId, true);
        if (isDuplicateNodeTree(sellerCats, cName, parentCId, cId)) {
            throw new BusinessException("重复的店铺内分类名!-"+ cName + "-" + parentCId);
        }
        // 如果urlKey不为空，则校验唯一性
        if (!StringUtils.isNullOrBlank2(urlKey)) {
            if (isDuplicateUrlKey(sellerCats, urlKey)) {
                throw new BusinessException("重复的目录urlKey!");
            }
        }

        ShopBean shopBean = Shops.getShop(channelId, cartId);
        if (shopBean == null && cartId >= 20) {
            throw new BusinessException("未配置店铺的销售平台!");
        }
//        String cId = "";
        String shopCartId = shopBean.getCart_id();

        if (isJDPlatform(shopBean)) {
            cId = jdShopService.addShopCategory(shopBean, cName, parentCId);
        } else if (isTMPlatform(shopCartId)) {
            cId = tbSellerCatService.addSellerCat(shopBean, cName, parentCId);
        }
        // Liking店独立官网创建店铺内分类
        else if (shopCartId.equals(CartEnums.Cart.LCN.getId())) {
            ////  2016/9/23  独立官网 店铺内分类api  下周tom提供   需返回cId
            int size = getBrotherSize(sellerCats, parentCId); // 2016-12-07 新增节点时默认设置index为当前层级节点的最后一位
            Map<String, String> resultMap = cnSellerCatService.addSellerCat(channelId, parentCId, cName, urlKey, shopBean, size + 1);
            cId = resultMap.get("catId");
            urlKey = resultMap.get("urlKey"); // 如果url传入为空，新增时向官网新增时生成urlKey，所以此处再次取值
        }
        // sneakerhead旗舰店创建店铺内分类
        else if (shopCartId.equals(CartEnums.Cart.CN.getId())) {
            cId = cnCategoryService.addSnSellerCat(channelId, parentCId, cName, shopBean);
        }else if(cartId < 20){
//            cId = Long.toString(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_CnShopCategory_ID));
        }
        if (!StringUtils.isNullOrBlank2(cId)) {
            // 2016-12-08 传入参数新增urlKey
            cmsBtSellerCatDao.add(channelId, cartId, cName, urlKey, mapping, parentCId, cId, creator);
        }
    }

    /**
     * updateSellerCat
     */
    public void updateSellerCat(String channelId, int cartId, String cName, String cId, String modifier) {
        updateSellerCat(channelId, cartId, cName, cId, null, modifier);
    }
    public void updateSellerCat(String channelId, int cartId, String cName, String cId, Map<String, Object> mapping, String modifier) {


        /*List<CmsBtSellerCatModel> sellercats = getSellerCatsByChannelCart(channelId, cartId, false);
        CmsBtSellerCatModel currentNode = sellercats.stream().filter(w -> w.getCatId().equals(cId)).findFirst().get();
        if (isDuplicateNode(sellercats, cName, currentNode.getParentCatId(), cId)) {
            throw new BusinessException("重复的店铺内分类!");
        }*/

        List<CmsBtSellerCatModel> sellerCats = getSellerCatsByChannelCart(channelId, cartId, true);
        CmsBtSellerCatModel currentNode = this.getCurrentNode(sellerCats, cId);
        if (isDuplicateNodeTree(sellerCats, cName, currentNode.getParentCatId(), cId)) {
            throw new BusinessException("重复的店铺内分类!");
        }

        ShopBean shopBean = Shops.getShop(channelId, cartId);
        String shopCartId = shopBean.getCart_id();
        if (isJDPlatform(shopBean)) {
            jdShopService.updateShopCategory(shopBean, cId, cName);
        } else if (isTMPlatform(shopCartId)) {
            tbSellerCatService.updateSellerCat(shopBean, cId, cName);
        /*}else if (shopCartId.equals(CartEnums.Cart.CN.getId())) {*/
        } else if (shopCartId.equals(CartEnums.Cart.LCN.getId())) {
            ////  2016/9/23  独立官网 店铺内分类api  下周tom提供   需返回cId
            int index = this.indexOfCurrentCat(sellerCats, currentNode.getParentCatId(), cId);
            if (index == -1) {
                throw new BusinessException("从树形目录中获取序号错误！");
            }
            currentNode.setCatName(cName);
            cnSellerCatService.updateSellerCat(currentNode, shopBean, index + 1);
        } else if (shopCartId.equals(CartEnums.Cart.CN.getId())) {
            ////  2016/9/23  独立官网 店铺内分类api  下周tom提供   需返回cId
            int index = this.indexOfCurrentCat(sellerCats, currentNode.getParentCatId(), cId);
            if (index == -1) {
                throw new BusinessException("从树形目录中获取序号错误！");
            }
            currentNode.setCatName(cName);
            cnCategoryService.updateSnSellerCat(currentNode, shopBean, index + 1);
        }

        List<CmsBtSellerCatModel> changedList = cmsBtSellerCatDao.update(channelId, cartId, cName, mapping, cId, modifier);

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

        if (isJDPlatform(shopBean)) {
            jdShopService.deleteShopCategory(shopBean, cId);
        } else if (isTMPlatform(shopCartId)) {
            //去TM平台取店铺分类
            List<SellerCat> sellerCatList = tbSellerCatService.getSellerCat(shopBean);
            if (sellerCatList != null) {
                if (sellerCatList.stream().filter(w -> w.getCid() == Long.valueOf(cId).longValue()).count() > 0) {
                    throw new BusinessException(shopBean.getShop_name() + ":请先到天猫后台删除店铺内分类后再在CMS中删除。");
                }
            }
//        }else if (shopCartId.equals(CartEnums.Cart.CN.getId())) {
        } else if (shopCartId.equals(CartEnums.Cart.LCN.getId())) {
            List<CmsBtSellerCatModel> sellercats = getSellerCatsByChannelCart(channelId, cartId, false);
            CmsBtSellerCatModel currentNode = sellercats.stream().filter(w -> w.getCatId().equals(cId)).findFirst().get();
            cnSellerCatService.deleteSellerCat(currentNode, shopBean);
        } else if (shopCartId.equals(CartEnums.Cart.CN.getId())) {
            List<CmsBtSellerCatModel> sellercats = getSellerCatsByChannelCart(channelId, cartId, false);
            CmsBtSellerCatModel currentNode = sellercats.stream().filter(w -> w.getCatId().equals(cId)).findFirst().get();
            cnCategoryService.deleteSnSellerCat(currentNode, shopBean);
        }


        CmsBtSellerCatModel deleted = cmsBtSellerCatDao.delete(channelId, cartId, parentCId, cId);
        //删除product表中所有的店铺内分类
        if (deleted != null) {
            List<CmsBtProductModel> list = cmsBtProductDao.deleteSellerCat(channelId, deleted, cartId, modifier);
            //插入上新表
            insert2SxWorkload(channelId, cartId, modifier, list);
            // 商品目录删除，新增履历 add by rex.wu 2016-12-06
            insertProductStatusHistory(channelId, cartId, modifier, list);
        }
    }

    /**
     * 从TMALL，JD平台获取店铺自定义类目树
     */
    public List<CmsBtSellerCatModel> refreshSellerCat(String channelId, int cartId, String creator) {
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        String shopCartId = shopBean.getCart_id();

        List<CmsBtSellerCatModel> sellerCat = new ArrayList<>();

        if (isJDPlatform(shopBean)) {
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
                            Map<String, Object> model = new HashMap<>();
                            model.put("cId", leaf.getCatId());
                            model.put("cName", leaf.getCatPath());
                            model.put("cIds", leaf.getFullCatId().split("-"));
                            model.put("cNames", leaf.getCatPath().split(">"));

                            sellerCats.add(model);
                        }

                        Map<String, Object> updateMap = new HashMap<>();
                        updateMap.put("platform.P" + cartId + ".sellerCats", sellerCats);
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
     * 重新设置店铺内分类的顺序
     *
     * @param channelId
     * @param cartId
     * @return
     */
    public boolean doResetPlatformSellerCatIndex(String channelId, int cartId) {

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        // 获取店铺内分类的列表
        List<CmsBtSellerCatModel> sellerCatList = getSellerCatsByChannelCart(channelId, cartId);

        if (cartId == CartEnums.Cart.LCN.getValue()) {
            cnSellerCatService.resetAllCatalog(sellerCatList, shopBean);
            return true;
        }

        return doResetPlatformSellerCatIndex_sub(shopBean, sellerCatList);
    }

    /**
     * 判断是否是重复的节点，同一层的节点名不能重复
     *
     * @param sellerCats
     * @param name
     * @param parentCId
     * @return
     */
    public boolean isDuplicateNode(List<CmsBtSellerCatModel> sellerCats, String name, String parentCId, String cId) {
        if (sellerCats != null && sellerCats.size() > 0) {
            if (sellerCats.stream().filter(w -> {
                if (StringUtil.isEmpty(cId)) {
                    return w.getParentCatId().equals(parentCId) && w.getCatName().equals(name);
                } else {
                    return w.getParentCatId().equals(parentCId) && w.getCatName().equals(name) && !w.getCatId().equalsIgnoreCase(cId);
                }
            }).count() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 保存整组分类树
     *
     * @param allCats
     * @param channelId
     * @param cartId
     */
    public void saveSortableCat(List<Map> allCats, String channelId, Integer cartId) {
        //根据channelId和cartId去删除数据库对应的树
        cmsBtSellerCatDao.deleteSortableCat(cartId, channelId);

        List<CmsBtSellerCatModel> sellerCatList = new ArrayList<>();
        //保存整组分类树
        for (Map model : allCats) {
            CmsBtSellerCatModel modelCat = new CmsBtSellerCatModel();
            //将树转换成CmsBtSellerCatModel
            BeanUtils.copyProperties(model, modelCat);
            //将整组树插入数据库
            cmsBtSellerCatDao.insert(modelCat);
            sellerCatList.add(modelCat);
        }
        //重新设置店铺内分类的顺序
        // doResetPlatformSellerCatIndex(channelId, cartId);
        ShopBean shopBean = Shops.getShop(channelId, cartId);
        /*if (shopBean != null && CartEnums.Cart.LIKING.getId().equals(shopBean.getCart_id())) {*/
        if (shopBean != null && (CartEnums.Cart.LCN.getId().equals(shopBean.getCart_id()))) {
            cnSellerCatService.resetAllCatalog(sellerCatList, shopBean);
//            cnCategoryService.uploadAllCnCategory(channelId, cartId, shopBean);
        } else if (shopBean != null && (CartEnums.Cart.CN.getId().equals(shopBean.getCart_id()))) {
            cnCategoryService.resetCnAllCatalog(channelId, cartId, shopBean);
        }

    }

    public List<CmsBtSellerCatModel> findNode(String channelId, Integer cartId, String catPath) {
        List<CmsBtSellerCatModel> ret = new ArrayList<>();
        String[] cats = catPath.split(">");
        List<CmsBtSellerCatModel> cmsBtSellerCats = getSellerCatsByChannelCart(channelId, cartId);
        for (int i = 0; i < cats.length; i++) {
            CmsBtSellerCatModel item = findNode(cmsBtSellerCats, cats[i]);
            if (item != null) {
                ret.add(item);
                cmsBtSellerCats = item.getChildren();
            }
        }
        return ret;
    }

    public CmsBtSellerCatModel findNode(List<CmsBtSellerCatModel> tree, String catName) {
        if (tree == null) return null;
        return tree.stream().filter(item -> item.getCatName().equals(catName)).findFirst().orElse(null);
    }

    public CmsBtProductModel_SellerCat getSellerCat(String channelId, Integer cartId, String category) {
        CmsBtProductModel_SellerCat sellerCat = new CmsBtProductModel_SellerCat();
        List<CmsBtSellerCatModel> sellerCatModels = findNode(channelId, cartId, category);
        if (ListUtils.isNull(sellerCatModels)) return null;
        sellerCat.setcId(sellerCatModels.get(sellerCatModels.size() - 1).getCatId());
        sellerCat.setcName(sellerCatModels.get(sellerCatModels.size() - 1).getCatName());
        List<String> cIds = new ArrayList<>(sellerCatModels.size());
        List<String> cNames = new ArrayList<>(sellerCatModels.size());
        sellerCatModels.forEach(item -> {
            cIds.add(item.getCatId());
            cNames.add(item.getCatName());
        });
        sellerCat.setcIds(cIds);
        sellerCat.setcNames(cNames);
        return sellerCat;
    }

    public Map<String, List<String>>  sellerCompare(List<CmsBtProductModel_SellerCat> o, List<CmsBtProductModel_SellerCat> n){

        List<String> oCatIds = new ArrayList<>();
        List<String> delCatPath = new ArrayList<>();
        List<String> addCatPath = new ArrayList<>();
        if(ListUtils.notNull(o)) {
            o.stream().map(CmsBtProductModel_SellerCat::getcIds).collect(Collectors.toList()).forEach(oCatIds::addAll);
            oCatIds = oCatIds.stream().distinct().collect(Collectors.toList());

            for(CmsBtProductModel_SellerCat item: o){
                if(ListUtils.isNull(n) || n.stream().noneMatch(cmsBtProductModel_sellerCat -> cmsBtProductModel_sellerCat.getcId().equals(item.getcId()))){
                    delCatPath.add("-"+item.getcIds().stream().collect(Collectors.joining("-"))+"-");
                }
            }
        }


        List<String> nCatIds = new ArrayList<>();
        if(ListUtils.notNull(n)) {
            n.stream().map(CmsBtProductModel_SellerCat::getcIds).collect(Collectors.toList()).forEach(nCatIds::addAll);
            nCatIds = nCatIds.stream().distinct().collect(Collectors.toList());

            for(CmsBtProductModel_SellerCat item: n){
                if(ListUtils.isNull(o) || o.stream().noneMatch(cmsBtProductModel_sellerCat -> cmsBtProductModel_sellerCat.getcId().equals(item.getcId()))){
                    addCatPath.add("-"+item.getcIds().stream().collect(Collectors.joining("-"))+"-");
                }
            }
        }

        List<String> del = new ArrayList<>();
        List<String> add = new ArrayList<>();
        for(String s: oCatIds){
            if(!nCatIds.contains(s)){
                del.add(s);
            }
        }

        for(String s: nCatIds){
            if(!oCatIds.contains(s)){
                add.add(s);
            }
        }
        Map<String, List<String>> rep = new HashMap();
        rep.put("del", del);
        rep.put("add", add);
        rep.put("delCatPath", delCatPath);
        rep.put("addCatPath", addCatPath);

        return rep;
    }
    /**
     * 商品所属分类被删除，插入履历
     *
     * @param channelId
     * @param cartId
     * @param modifier
     * @param list
     */
    private void insertProductStatusHistory(String channelId, int cartId, String modifier, List<CmsBtProductModel> list) {
        /*insertList(String channelId, List<String> codes, int cartId, EnumProductOperationType operationType, String comment, String modifier)*/
        List<String> codes = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(productModel -> {
                codes.add(productModel.getCommonNotNull().getFieldsNotNull().getCode());
            });
            productStatusHistoryService.insertList(channelId, codes, cartId, EnumProductOperationType.CotegoryDeleted, "商品分类被删除", modifier);
        }
    }

    /**
     * clone 一个CmsBtSellerCatModel对象
     */
    private CmsBtSellerCatModel copyCmsBtSellerCatModel(CmsBtSellerCatModel node) {
        CmsBtSellerCatModel copyRoot = new CmsBtSellerCatModel();
        copyRoot.setCatId(node.getCatId());
        copyRoot.setCatName(node.getCatName());
        copyRoot.setUrlKey(node.getUrlKey());
        copyRoot.setParentCatId(node.getParentCatId());
        copyRoot.setIsParent(node.getIsParent());
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
     * 重新设置店铺内分类的顺序（指定一个列表）
     *
     * @param shopBean
     * @param sellerCatList
     * @return
     */
    private boolean doResetPlatformSellerCatIndex_sub(ShopBean shopBean, List<CmsBtSellerCatModel> sellerCatList) {

        // 检查一下是否需要做
        if (sellerCatList == null || sellerCatList.size() == 0) {
            return true;
        }

        // 先处理一下当前的列表的当前的那一级
        for (int i = 0; i < sellerCatList.size(); i++) {
            CmsBtSellerCatModel subSellerCat = sellerCatList.get(i);

            String shopCartId = shopBean.getCart_id();
            if (isJDPlatform(shopBean)) {
                // 京东API不支持， 以后京东如果支持之后再做
            } else if (isTMPlatform(shopCartId)) {
                tbSellerCatService.updateSellerCatSortOrder(shopBean, subSellerCat.getCatId(), i + 1);
            } else if (shopCartId.equals(CartEnums.Cart.LCN.getId())) {
                ////  2016/9/23  独立官网 店铺内分类api  下周tom提供   需返回cId
//                cnSellerCatService.updateSellerCat(channelId, subSellerCat.getCatId(), shopBean);
            }

        }

        // 循环遍历递归children
        for (CmsBtSellerCatModel subSellerCat : sellerCatList) {
            if (!doResetPlatformSellerCatIndex_sub(shopBean, subSellerCat.getChildren())) {
                return false;
            }
        }

        return true;
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
    private boolean isJDPlatform(ShopBean shopBean) {
        return PlatFormEnums.PlatForm.JD.getId().equals(shopBean.getPlatform_id());
    }

    /**
     * 是天猫平台
     */
    private boolean isTMPlatform(String shopCartId) {
        if (shopCartId.equals(CartEnums.Cart.TM.getId()) || shopCartId.equals(CartEnums.Cart.TB.getId()) ||
                shopCartId.equals(CartEnums.Cart.TG.getId()) || shopCartId.equals(CartEnums.Cart.TT.getId())
                || shopCartId.equals(CartEnums.Cart.LTT.getId())) {
            return true;
        }
        return false;
    }

    /**
     * 根据cId从属性目录中寻找当前节点
     *
     * @param nodes
     * @param cId
     * @return
     */
    public CmsBtSellerCatModel getCurrentNode(List<CmsBtSellerCatModel> nodes, String cId) {
        if (CollectionUtils.isNotEmpty(nodes)) {
            for (CmsBtSellerCatModel node : nodes) {
                if (node.getCatId().equals(cId)) {
                    return node;
                }
                CmsBtSellerCatModel tempNode = getCurrentNode(node.getChildren(), cId);
                if (tempNode != null) {
                    return tempNode;
                }
            }
        }
        return null;
    }

    /**
     * 遍历属性结构，判断名称是否被占用
     *
     * @param sellercats
     * @param cName
     * @param parentCatId
     * @param cId
     * @return
     */
    private boolean isDuplicateNodeTree(List<CmsBtSellerCatModel> sellercats, String cName, String parentCatId, String cId) {
        if (CollectionUtils.isNotEmpty(sellercats)) {
            if (StringUtil.isEmpty(cId)) {
                for (CmsBtSellerCatModel node : sellercats) {
                    if (node.getParentCatId().equals(parentCatId) && node.getCatName().equals(cName)) {
                        return true;
                    }
                    boolean tempResult = isDuplicateNodeTree(node.getChildren(), cName, parentCatId, cId);
                    if (tempResult) {
                        return tempResult;
                    }
                }
            } else {
                for (CmsBtSellerCatModel node : sellercats) {
                    if (node.getParentCatId().equals(parentCatId) && node.getCatName().equals(cName) && !node.getCatId().equalsIgnoreCase(cId)) {
                        return true;
                    }
                    boolean tempResult = isDuplicateNodeTree(node.getChildren(), cName, parentCatId, cId);
                    if (tempResult) {
                        return tempResult;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 树形目录中判断某个节点在同级目录的序号
     *
     * @param sellerCats
     * @param parentCid
     * @param cId
     * @return
     */
    public int indexOfCurrentCat(List<CmsBtSellerCatModel> sellerCats, String parentCid, String cId) {
        int index = -1;
        if (CollectionUtils.isNotEmpty(sellerCats)) {
            for (CmsBtSellerCatModel node : sellerCats) {
                index++;
                if (node.getParentCatId().equalsIgnoreCase(parentCid) && node.getCatId().equalsIgnoreCase(cId)) {
                    return index;
                }
                int temp = indexOfCurrentCat(node.getChildren(), node.getCatId(), cId);
                if (temp != -1) {
                    return temp;
                }
            }
        }
        return -1;
    }

    /**
     * 判断当前层级目录节点个数
     *
     * @param sellerCats
     * @param cId
     * @return
     */
    private int getBrotherSize(List<CmsBtSellerCatModel> sellerCats, String cId) {
        if (CollectionUtils.isNotEmpty(sellerCats)) {
            for (CmsBtSellerCatModel node : sellerCats) {
                if (node.getParentCatId().equalsIgnoreCase(cId)) {
                    return sellerCats.size();
                }
                int tempSize = getBrotherSize(node.getChildren(), cId);
                if (tempSize != 0) {
                    return tempSize;
                }
            }
        }
        return 0;
    }

    /**
     * 判断urlKey是否在属性目录中存在
     *
     * @param sellerCats
     * @param urlKey
     * @return
     */
    private boolean isDuplicateUrlKey(List<CmsBtSellerCatModel> sellerCats, String urlKey) {
        if (CollectionUtils.isEmpty(sellerCats) || org.apache.commons.lang.StringUtils.isBlank(urlKey)) {
            return false;
        }
        for (CmsBtSellerCatModel node : sellerCats) {
            if (urlKey.equalsIgnoreCase(node.getUrlKey())) {
                return true;
            }
            boolean temp = isDuplicateUrlKey(node.getChildren(), urlKey);
            if (temp) {
                return true;
            }
        }
        return false;
    }

    public void updateCategoryByPath(String channelId, Integer cartId, String catPath, String fullCatId, Map<String, Object> map) {
        //解析路径
        String[] totalPaths = catPath.split(">");
        String[] catIds = fullCatId.split("-");
        //通过根目录获取到完整的对象
        CmsBtSellerCatModel cmsBtSellerCatModel = cmsBtSellerCatDao.selectByRootCatId(channelId, cartId, catIds[0]);
        if (cmsBtSellerCatModel != null) {
            List<CmsBtSellerCatModel> childrens = cmsBtSellerCatModel.getChildren();
            //获取到目标节点
            CmsBtSellerCatModel targetNode = match(childrens, fullCatId);
            //修改mapping信息
            targetNode.setMapping(map);
            //备份原来的名字
            String catName = targetNode.getCatName();

            StringBuilder pathBuilder = new StringBuilder(totalPaths[0]);
            String[] currentCatId = targetNode.getFullCatId().split("-");
            for (int i = 1; i < currentCatId.length; i++) {
                pathBuilder.append(">" + totalPaths[i]);
            }
            //获取到当前层级的path
            String path = pathBuilder.toString();
            //修改catPath和catName
            if (!path.equals(targetNode.getCatPath())) {
                //path路径不匹配,需要修改
                targetNode.setCatPath(path);
                //获取路径中的最后一个,即catName
                targetNode.setCatName(totalPaths[currentCatId.length - 1]);
            }
            //因为当前节点的名字修改了之后,对应子节点的名字也要进行修改,这里要对子节点再进行递归修改
            updateChilds(targetNode.getChildren(), catName, totalPaths[currentCatId.length - 1]);
            cmsBtSellerCatDao.update(cmsBtSellerCatModel);
        }
    }

    private CmsBtSellerCatModel match(List<CmsBtSellerCatModel> childrens, String fullCatId) {
        if (ListUtils.notNull(childrens)) {
            //解析路径
            String[] catIds = fullCatId.split("-");
            for (CmsBtSellerCatModel children : childrens) {
                StringBuilder catIdBuilder = new StringBuilder(catIds[0]);
                String[] currentCatId = children.getFullCatId().split("-");
                for (int i = 1; i < currentCatId.length; i++) {
                    catIdBuilder.append("-" + catIds[i]);
                }
                //获取到当前层级的path和catId
                String catId = catIdBuilder.toString();
                if (catId.equals(children.getFullCatId())) {
                    //catId匹配成功
                    if (fullCatId.equals(children.getFullCatId())) {
                        //完全匹配,返回目标节点
                        return children;
                    } else {
                        //不完全匹配,递归
                        match(children.getChildren(), fullCatId);
                    }
                }
            }
        }
        return null;
    }

    //更新子节点的名字
    private void updateChilds(List<CmsBtSellerCatModel> childrens, String targetCatName, String replaceCatName) {
        if (ListUtils.notNull(childrens)) {
            //子节点存在,遍历修改,并递归
            for (CmsBtSellerCatModel children : childrens) {
                children.setCatPath(children.getCatPath().replace(targetCatName, replaceCatName));
                //递归
                updateChilds(children.getChildren(), targetCatName, replaceCatName);
            }
        }
    }
}
