package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.BrandCatControlInfo;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.SellerAuthorize;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import com.voyageone.components.tmall.bean.ItemSchema;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sn3 on 2015-05-22.
 */
@Component
public class TbCategoryService extends TbBase {

    /**
     * 获得授权类目和品牌
     */
    public SellerAuthorize getSellerCategoriesAuthorize(ShopBean shop) throws ApiException {

//        int intApiErrorCount = 0;

        // 设置 返回字段
        StringBuilder sbField = getRequestFields();

        ItemcatsAuthorizeGetRequest request = new ItemcatsAuthorizeGetRequest();
        request.setFields(sbField.toString());
        ItemcatsAuthorizeGetResponse response = reqTaobaoApi(shop,request);

        if (response.getErrorCode() == null) {
            // 设置返回值
            return response.getSellerAuthorize();
        }

        logger.error("调用天猫API:获取该店铺被授权的类目和品牌:" + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
        logger.error("ERROR CODE:" + response.getErrorCode() +",ERROR MSG:" + response.getMsg());
        logger.error("RESPONSE BODY:" + response.getBody());

        return null;
    }

    /**
     * 根据父类目获取子类目
     * @param shop 指定店铺
     * @param parentCid 指定的父类目id
     * @return ItemCat
     */
    public List<ItemCat> getCategory(ShopBean shop,Long parentCid) throws ApiException {

        List<ItemCat> itemCatListResult = new ArrayList<>();

        // 调用taobao.itemcats.get 获取后台供卖家发布商品的标准商品类目
        ItemcatsGetRequest request = new ItemcatsGetRequest();
        request.setParentCid(parentCid);
        ItemcatsGetResponse response = reqTaobaoApi(shop, request);

        if (response.getErrorCode() == null) {
            List<ItemCat> itemCatList = response.getItemCats();
            // 20160724 tom bug修正 START
            if (itemCatList == null) {
                // 如果返回为空, 那么久说明没有子类目 ( 下面的递归之前已经有过判断, 但是一级类目下面没有子类目的场合, 会有null的可能性 )
                return itemCatListResult;
            }
            // 20160724 tom bug修正 END
            // 设置返回值
            for (ItemCat itemCat : itemCatList) {

                // 把自己加入到返回值
                itemCatListResult.add(itemCat);

                // 如果包含子类目，则递归子类目
                if (itemCat.getIsParent()) {
                    itemCatListResult.addAll(getCategory(shop, itemCat.getCid()));
                }
            }
        } else {
            logger.error("调用天猫API:获取后台供卖家发布商品的标准商品类目:" + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",cid:" + parentCid);
            logger.error("ERROR CODE:" + response.getErrorCode() +",ERROR MSG:" + response.getMsg());
            logger.error("RESPONSE BODY:" + response.getBody());

            return null;
        }

        return itemCatListResult;
    }

    private StringBuilder getRequestFields(){
        StringBuilder sbField = new StringBuilder();

        sbField.append("brand.pid");                    // （已授权的品牌）pid
        sbField.append(",brand.vid");                   // （已授权的品牌）vid
        sbField.append(",brand.name");                  // （已授权的品牌）
        sbField.append(",brand.prop_name");             // （已授权的品牌）

        sbField.append(",item_cat.cid");                // （允许发布的类目）id值
        sbField.append(",item_cat.parent_cid");         // （允许发布的类目）父id值
        sbField.append(",item_cat.name");               // （允许发布的类目）名称
        sbField.append(",item_cat.is_parent");          // （允许发布的类目）是否还有子目录
        sbField.append(",item_cat.status");             // （允许发布的类目）状态：normal(正常),deleted(删除)

//        //C店相关属性
//        sbField.append(",xinpin_item_cat.cid");
//        sbField.append(",xinpin_item_cat.name");
//        sbField.append(",xinpin_item_cat.sort_order");
//        sbField.append(",xinpin_item_cat.parent_cid");
//        sbField.append(",xinpin_item_cat.is_parent");
        return sbField;
    }


    /**
     * 获取天猫产品属性
     */
    public String getTbProductAddSchema(ShopBean shop,Long cid, Long brandId) throws ApiException{


//        ItemcatsAuthorizeGetResponse ret = null;
        //获取淘宝API连接
//        TaobaoClient client = getDefaultTaobaoClient(shop,"xml");

        //调用tmall.product.add.schema.get 产品发布规则获取接
        TmallProductAddSchemaGetRequest request = new TmallProductAddSchemaGetRequest();
        request.setCategoryId(cid);
        if (brandId != null) {
            request.setBrandId(brandId);
        }
        TmallProductAddSchemaGetResponse response = reqTaobaoApi(shop, request);
        if (response!=null && response.getErrorCode() == null) {
            // 设置返回值
            return response.getAddProductRule();
        } else if (response != null){



            String subMsg = response.getSubMsg();
            if (subMsg == null) {
                subMsg = "";
            }
            switch(subMsg)
            {
                case "参数无效;指定类目不需要发布产品;":
                    // 正常现象, 无需打log
                    break;
                case "参数无效;此类目需要品牌;":
                    logger.error("channel_id:" + shop.getOrder_channel_id() + "  cart_id:" + shop.getCart_id() + "  cid:" + cid + " err:" + subMsg);
                    break;
                default:
                    logger.error("channel_id:" + shop.getOrder_channel_id() + "  cart_id:" + shop.getCart_id() + "  cid:" + cid + " 未知错误err:" + response.getBody());
                    break;
            }
            return "ERROR:" + subMsg;
        }

        return  "ERROR:API返回NULL";
    }

    /**
     * tmall.item.add.schema.get 天猫发布商品规则获取
     */
    public ItemSchema getTbItemAddSchema(ShopBean shop,Long cid, Long productId) throws ApiException{

        ItemSchema result = new ItemSchema();
        result.setCid(cid);

        //tmall.item.add.schema.get 天猫发布商品规则获取
        TmallItemAddSchemaGetRequest request = new TmallItemAddSchemaGetRequest();
        request.setCategoryId(cid);
        if (productId == null) {
            request.setProductId(0L);
            request.setIsvInit(true);
        } else {
            request.setProductId(productId);
            request.setIsvInit(false);
        }
        request.setType("b");
        TmallItemAddSchemaGetResponse response = reqTaobaoApi(shop, request);

        if (response != null && response.getErrorCode() == null) {
            // 设置返回值
            result.setResult(0);
            result.setItemResult(response.getAddItemResult());
            return result;
        } else if (response != null){
            String subMsg = response.getSubMsg();
            if (subMsg == null) {
                logger.error("channel_id:" + shop.getOrder_channel_id() + "  cart_id:" + shop.getCart_id() + "  cid:" + cid + " 未知错误err:" + response.getBody());
                return new ItemSchema();
            }
            switch(subMsg)
            {
                case "商品类目已被冻结, 本类目已经不能发布商品，请重新选择类目":
                case "商品类目未授权，请重新选择类目;商品类目天猫已经废弃, 本类目已经不能发布商品，请重新选择类目":
                case "商品类目天猫已经废弃, 本类目已经不能发布商品，请重新选择类目":
                    // 此处虽然是异常, 但是log还是不用了, 直接在外面生成一条schema提示, 避免log太多
                    // logger.error("channel_id:" + shop.getOrder_channel_id() + "  cart_id:" + shop.getCart_id() + "  cid:" + cid + " err:" + subMsg);
                    result.setResult(1); //异常
                    result.setItemResult(subMsg);
                    return result;
                default:
                    logger.error("channel_id:" + shop.getOrder_channel_id() + "  cart_id:" + shop.getCart_id() + "  cid:" + cid + " 未知错误err:" + response.getBody());
                    return new ItemSchema();
            }
        }

        return new ItemSchema();

    }

    /**
     * tmall.brandcat.control.get 获取品牌类目的控制信息
     */
    public BrandCatControlInfo getTbBrandCat(ShopBean shop) throws ApiException{

//        int intApiErrorCount = 0;

        //获取淘宝API连接
//        TaobaoClient client = getDefaultTaobaoClient(shop);

        //tmall.item.add.schema.get 天猫发布商品规则获取
        TmallBrandcatControlGetRequest request = new TmallBrandcatControlGetRequest();
        TmallBrandcatControlGetResponse response = reqTaobaoApi(shop, request);

        if (response != null && response.getErrorCode() == null) {
            // 设置返回值
            return response.getBrandCatControlInfo();
        }

        return new BrandCatControlInfo();
    }
}
