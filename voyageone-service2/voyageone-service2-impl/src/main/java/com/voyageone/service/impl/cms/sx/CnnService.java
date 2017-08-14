package com.voyageone.service.impl.cms.sx;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.cnn.request.*;
import com.voyageone.components.cnn.request.bean.CategoryInfoBean;
import com.voyageone.components.cnn.response.*;
import com.voyageone.components.cnn.response.data.CategoryGetResDataBean;
import com.voyageone.components.cnn.service.CnnCategoryNewService;
import com.voyageone.components.cnn.service.CnnWareNewService;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * sn app 辅助类
 * Created by morse.lu on 2017/08/14
 */
@Service
public class CnnService {

    @Autowired
    private CnnWareNewService cnnWareNewService;
    @Autowired
    private CnnCategoryNewService cnnCategoryNewService;

    @Autowired
    private SellerCatService sellerCatService;

    /**
     * sn app新增商品
     * @param shop
     * @param request
     */
    public String addProduct(ShopBean shop, ProductAddRequest request) throws Exception {
        ProductAddResponse response = cnnWareNewService.addProduct(shop, request);
        if (response == null) throw new BusinessException("新增商品请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("新增商品请求失败!" + response.getErrMsgStr());
        if (response.getData() == null || response.getData().getNumIId() == null) throw new BusinessException("新增商品请求返回numIId为空!");
        return response.getData().getNumIId().toString();
    }

    /**
     * sn app更新商品
     * @param shop
     * @param request
     */
    public boolean updateProduct(ShopBean shop, ProductUpdateRequest request) throws Exception {
        ProductUpdateResponse response = cnnWareNewService.updateProduct(shop, request);
        if (response == null) throw new BusinessException("更新商品请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("更新商品请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app批量更新商品价格，每次请求最多处理100个商品
     * @param shop
     * @param request
     */
    public boolean updateMultiProductPrice(ShopBean shop, ProductBatchUpdatePriceRequest request) throws Exception {
        ProductBatchUpdatePriceResponse response = cnnWareNewService.updateMultiProductPrice(shop, request);
        if (response == null) throw new BusinessException("批量更新商品价格请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("批量更新商品价格请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app删除商品 group下线
     * @param shop
     * @param numIId
     */
    public boolean deleteProduct(ShopBean shop, String numIId) throws Exception {
        ProductDeleteRequest request = new ProductDeleteRequest();
        request.setNumIId(numIId);
        ProductDeleteResponse response = cnnWareNewService.deleteProduct(shop, request);
        if (response == null) throw new BusinessException("group下线请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("group下线请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app删除CODE 单CODE下线
     * @param shop
     * @param numIId
     * @param code
     */
    public boolean deleteProductCode(ShopBean shop, String numIId, String code) throws Exception {
        ProductDeleteCodeRequest request = new ProductDeleteCodeRequest();
        request.setNumIId(numIId);
        request.setProdCode(code);
        ProductDeleteCodeResponse response = cnnWareNewService.deleteProductCode(shop, request);
        if (response == null) throw new BusinessException("单CODE下线请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("单CODE下线请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app删除sku,逻辑删除
     * @param shop
     * @param numIId
     * @param sku
     */
    public boolean deleteProductSku(ShopBean shop, String numIId, String sku) throws Exception {
        ProductDeleteSkuRequest request = new ProductDeleteSkuRequest();
        request.setNumIId(numIId);
        request.setSkuCode(sku);
        ProductDeleteSkuResponse response = cnnWareNewService.deleteProductSku(shop, request);
        if (response == null) throw new BusinessException("删除sku请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("删除sku请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app 上架
     * @param shop
     * @param numIId
     */
    public boolean doProductListing(ShopBean shop, String numIId) throws Exception {
        ProductListingRequest request = new ProductListingRequest();
        request.setNumIId(numIId);
        ProductListingResponse response = cnnWareNewService.doProductListing(shop, request);
        if (response == null) throw new BusinessException("上架请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("上架请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app 下架
     * @param shop
     * @param numIId
     */
    public boolean doProductDeListing(ShopBean shop, String numIId) throws Exception {
        ProductDeListingRequest request = new ProductDeListingRequest();
        request.setNumIId(numIId);
        ProductDeListingResponse response = cnnWareNewService.doProductDeListing(shop, request);
        if (response == null) throw new BusinessException("下架请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("下架请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app 获取商品上下架状态
     * @param shop
     * @param numIId
     */
    public CmsConstants.PlatformStatus getProductPlatformStatus(ShopBean shop, String numIId) throws Exception {
        ProductGetStatusRequest request = new ProductGetStatusRequest();
        request.setNumIId(numIId);
        ProductGetStatusResponse response = cnnWareNewService.getProductPlatformStatus(shop, request);
        if (response == null) throw new BusinessException("获取商品上下架状态请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("获取商品上下架状态请求失败!" + response.getErrMsgStr());
        if (response.getData() == null || response.getData().getPlatformStatus() == null) throw new BusinessException("获取商品上下架状态请求返回状态为空!");
        return response.getData().getPlatformStatus();
    }

    /**
     * sn app添加店铺内分类
     * @param shop
     * @param catId 店铺内分类ID
     * @param name 店铺分类名
     * @param parentCatId 分类的父节点ID(传空的话，默认为'0'表示是顶级节点)
     */
    public boolean addCategory(ShopBean shop, String catId, String name, String parentCatId) throws Exception {
        CategoryAddRequest request = new CategoryAddRequest();
        request.setId(catId);
        request.setName(name);
        request.setParentCatalogId(StringUtils.isEmpty(parentCatId) ? "0" : parentCatId);
        CategoryAddResponse response = cnnCategoryNewService.addCategory(shop, request);
        if (response == null) throw new BusinessException("添加店铺内分类请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("添加店铺内分类请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app修改店铺内分类名称
     * @param shop
     * @param catId 店铺内分类ID
     * @param name 分类名
     */
    public boolean updateCategoryName(ShopBean shop, String catId, String name) throws Exception {
        CategoryUpdateNameRequest request = new CategoryUpdateNameRequest();
        request.setId(catId);
        request.setName(name);
        CategoryUpdateNameResponse response = cnnCategoryNewService.updateCategoryName(shop, request);
        if (response == null) throw new BusinessException("修改店铺内分类名称请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("修改店铺内分类名称请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app删除店铺内分类
     * @param shop
     * @param catId 店铺内分类ID
     */
    public boolean deleteCategory(ShopBean shop, String catId) throws Exception {
        CategoryDeleteRequest request = new CategoryDeleteRequest();
        request.setId(catId);
        CategoryDeleteResponse response = cnnCategoryNewService.deleteCategory(shop, request);
        if (response == null) throw new BusinessException("删除店铺内分类请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("删除店铺内分类请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app批量设置商品的店铺内分类
     * numIId设置为idList中的店铺内分类，直接完全覆盖
     * @param shop
     * @param numIId
     * @param catIdList 店铺内分类ID列表，最多10个，目前必须是叶子节点
     */
    public boolean setCategoryProduct(ShopBean shop, String numIId, List<String> catIdList) throws Exception {
        CategoryProductSetRequest request = new CategoryProductSetRequest();
        request.setNumIId(Long.valueOf(numIId));
        request.setIdList(catIdList);
        CategoryProductSetResponse response = cnnCategoryNewService.setCategoryProduct(shop, request);
        if (response == null) throw new BusinessException("批量设置商品的店铺内分类请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("批量设置商品的店铺内分类请求失败!" + response.getErrMsgStr());
        return true;
    }

    /**
     * sn app查询店铺内分类信息
     * 只返回指定的分类信息及其下级分类信息，（不包括再下一级的分类，若需要请重新指定父节点查询）
     * @param shop
     * @param catId 店铺内分类ID
     */
    public CmsMtPlatformCategoryTreeModel searchCategory(ShopBean shop, String catId) throws Exception {
        CategoryGetRequest request = new CategoryGetRequest();
        request.setCatalogId(catId);
        CategoryGetResponse response = cnnCategoryNewService.searchCategory(shop, request);
        if (response == null) throw new BusinessException("查询店铺内分类信息请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("查询店铺内分类信息请求失败!" + response.getErrMsgStr());
        if (response.getData() == null) throw new BusinessException("查询店铺内分类信息请求返回内容为空!");

        CmsMtPlatformCategoryTreeModel category = new CmsMtPlatformCategoryTreeModel();
        category.setCatName(response.getData().getName());
        category.setCatId(catId);
        List<CmsMtPlatformCategoryTreeModel> child = new ArrayList<>();
        category.setChildren(child);
        if (ListUtils.notNull(response.getData().getSubList())) {
            for (CategoryGetResDataBean.SubCategory subCategory : response.getData().getSubList()) {
                CmsMtPlatformCategoryTreeModel childCategory = new CmsMtPlatformCategoryTreeModel();
                childCategory.setCatName(subCategory.getSubName());
                childCategory.setCatId(subCategory.getSubId());
                child.add(childCategory);
            }
        }

        return category;
    }

    /**
     * sn app重置所有店铺内分类
     * @param shop
     * @param channelId
     * @param cartId
     */
    public boolean resetCategory(ShopBean shop, String channelId, int cartId) throws Exception {
        CategoryResetRequest request = new CategoryResetRequest();
        List<CategoryInfoBean> catalogList = new ArrayList<>();
        request.setCatalogList(catalogList);

        List<CmsBtSellerCatModel> sellerCatModels = sellerCatService.getSellerCatsByChannelCart(channelId, cartId, true);
        setChildCategoryInfo(catalogList, sellerCatModels);

        CategoryResetResponse response = cnnCategoryNewService.resetCategory(shop, request);
        if (response == null) throw new BusinessException("重置所有店铺内分类请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("重置所有店铺内分类请求失败!" + response.getErrMsgStr());
        return true;
    }

    private void setChildCategoryInfo(List<CategoryInfoBean> catalogList, List<CmsBtSellerCatModel> childSellerCatModels) {
        for (CmsBtSellerCatModel sellerCatModel : childSellerCatModels) {
            CategoryInfoBean categoryInfoBean = new CategoryInfoBean();
            categoryInfoBean.setCatId(sellerCatModel.getCatId());
            categoryInfoBean.setCatName(sellerCatModel.getCatName());
            categoryInfoBean.setChildren(new ArrayList<>());
            catalogList.add(categoryInfoBean);
            setChildCategoryInfo(categoryInfoBean.getChildren(), sellerCatModel.getChildren());
        }
    }

    /**
     * sn app重置指定店铺内分类下的所有商品
     * @param shop
     * @param numIIdList 商品numIId列表，最多500个
     * @param catId 店铺内分类ID
     */
    public boolean resetCategoryProduct(ShopBean shop, List<Long> numIIdList, String catId) throws Exception {
        CategoryProductResetRequest request = new CategoryProductResetRequest();
        request.setCatId(catId);
        request.setNumIIdList(numIIdList);
        CategoryProductResetResponse response = cnnCategoryNewService.resetCategoryProduct(shop, request);
        if (response == null) throw new BusinessException("重置指定店铺内分类下的所有商品请求response为空!");
        if (!response.isSuccess()) throw new BusinessException("重置指定店铺内分类下的所有商品请求失败!" + response.getErrMsgStr());
        return true;
    }

}
