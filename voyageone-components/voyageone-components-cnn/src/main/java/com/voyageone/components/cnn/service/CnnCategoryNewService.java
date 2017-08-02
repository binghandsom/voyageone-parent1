package com.voyageone.components.cnn.service;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.cnn.CnnBase;
import com.voyageone.components.cnn.request.*;
import com.voyageone.components.cnn.response.*;
import com.voyageone.components.cnn.util.CnnUtil;
import org.springframework.stereotype.Component;

/**
 * sn app
 * Created by morse.lu on 2017/08/01
 */
@Component
public class CnnCategoryNewService extends CnnBase {

    /**
     * sn app添加店铺内分类
     */
    public CategoryAddResponse addCategory(ShopBean shop, CategoryAddRequest request) throws Exception {
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app修改店铺内分类名称
     */
    public CategoryUpdateNameResponse updateCategoryName(ShopBean shop, CategoryUpdateNameRequest request) throws Exception {
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app删除店铺内分类
     */
    public CategoryDeleteResponse deleteCategory(ShopBean shop, CategoryDeleteRequest request) throws Exception {
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app批量设置商品的店铺内分类
     * numIIdList中的每一个商品都设置为idList中的店铺内分类，直接完全覆盖
     */
    public CategoryProductSetResponse setCategoryProduct(ShopBean shop, CategoryProductSetRequest request) throws Exception {
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

    /**
     * sn app查询店铺内分类信息
     * 只返回指定的分类信息及其下级分类信息，（不包括再下一级的分类，若需要请重新指定父节点查询）
     */
    public CategoryGetResponse searchCategory(ShopBean shop, CategoryGetRequest request) throws Exception {
        request.resetParams();
        request.addParam(request.getCatalogId());
        return reqApi(shop, request, CnnUtil.getHeadersForSnApp());
    }

}
