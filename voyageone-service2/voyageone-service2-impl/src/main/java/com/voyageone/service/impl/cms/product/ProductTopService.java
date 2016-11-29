package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.producttop.GetTopListParameter;
import com.voyageone.service.bean.cms.producttop.ProductInfo;
import com.voyageone.service.bean.cms.producttop.ProductPageParameter;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductTopDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductTopModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/11/28.
 */
public class ProductTopService extends BaseService {
    @Autowired
    CmsBtProductTopDao dao;
    @Autowired
    CmsBtProductDao cmsBtProductDao;
    public List<ProductInfo> getPage(ProductPageParameter param, String channelId) {
        CmsBtProductTopModel topModel = dao.selectByCatId(param.getCartId(), channelId);
        int pageIndex = param.getPageIndex();
        int pageSize = param.getPageSize();
        JongoQuery queryObject = getJongoQuery(param,topModel);
        queryObject.setProjection("");
        queryObject.setSort("{prodId:-1}");
        queryObject.setLimit(pageSize);
        queryObject.setSkip((pageIndex - 1) * pageSize);
        List<CmsBtProductModel> list = cmsBtProductDao.select(queryObject, channelId);
        List<ProductInfo> listResult = list.stream().map(f -> mapProductInfo(f, param.getCartId())).collect(Collectors.toList());
        return listResult;
    }
    public Object getCount(ProductPageParameter param, String channelId) {
        CmsBtProductTopModel topModel = dao.selectByCatId(param.getCartId(), channelId);

        JongoQuery queryObject = getJongoQuery(param, topModel);

        return cmsBtProductDao.countByQuery(queryObject.getQuery(), channelId);
    }
    public List<ProductInfo> getTopList(GetTopListParameter parameter) {
        CmsBtProductTopModel topModel = dao.selectByCatId(parameter.getCartId(), parameter.getChannelId());
        if (topModel == null || topModel.getProductCodeList() == null || topModel.getProductCodeList().size() == 0)
            return new ArrayList<>();

        JongoQuery jongoQuery = getTopListJongoQuery(parameter, topModel);
        List<CmsBtProductModel> list = cmsBtProductDao.select(jongoQuery, parameter.getChannelId());
        List<ProductInfo> listResult = list.stream().map(f -> mapProductInfo(f, parameter.getCartId())).collect(Collectors.toList());
        return listResult;
    }

    JongoQuery getTopListJongoQuery(GetTopListParameter param,CmsBtProductTopModel topModel) {
        JongoQuery queryObject = new JongoQuery();

        //平台cartId    商品分类
        queryObject.addQuery("{'platforms.P#.pCatId':#}");
        queryObject.addParameters(param.getCartId(), param.getpCatId());

        // 获取code list用于检索code
        if (topModel.getProductCodeList() != null
                && topModel.getProductCodeList().size() > 0) {
            List<String> inputCodeList = topModel.getProductCodeList();
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                Object inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);
                queryObject.addQuery("{'common.fields.code':{$in:#}}");
                queryObject.addParameters(inputCodeArr);
            }
        }
        return queryObject;
    }


    ProductInfo mapProductInfo(CmsBtProductModel f,int cartId)
    {
        ProductInfo info = new ProductInfo();
        info.setBrand(f.getCommon().getFields().getBrand());
        info.setCode(f.getCommon().getFields().getCode());
        info.setModel(f.getCommon().getFields().getModel());
        info.setProductName(f.getCommon().getFields().getProductNameEn());
        info.setQuantity(f.getCommon().getFields().getQuantity());
        //图片
        List<CmsBtProductModel_Field_Image> imgList = f.getCommonNotNull().getFieldsNotNull().getImages6();
        if (!imgList.isEmpty()) {
            info.setImage1(imgList.get(0).getName());
        }
        if(StringUtil.isEmpty(info.getImage1())){
            imgList = f.getCommonNotNull().getFieldsNotNull().getImages1();
            if (!imgList.isEmpty()) {
                info.setImage1(imgList.get(0).getName());
            }
        }
        CmsBtProductModel_Platform_Cart platform_Cart= f.getPlatform(cartId);
        if(platform_Cart!=null) {
            info.setSkuCount(platform_Cart.getSkus().size());
        }
        return info;
    }

     JongoQuery getJongoQuery(ProductPageParameter param, CmsBtProductTopModel topModel) {
        JongoQuery queryObject = new JongoQuery();

        //平台cartId    商品分类
        queryObject.addQuery("{'platforms.P#.pCatId':#}");
        queryObject.addParameters(param.getCartId(), param.getpCatId());

        // 获取code list用于检索code  not in
         if (topModel!=null&&topModel.getProductCodeList() != null
                 && topModel.getProductCodeList().size() > 0) {
             List<String> inputCodeList = topModel.getProductCodeList();
             inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
             if (inputCodeList.size() > 0) {
                 Object inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);
                 queryObject.addQuery("{'common.fields.code':{$nin:#}}");
                 queryObject.addParameters(inputCodeArr);
             }
         }
        //品牌
         if (param.getBrandList() !=  null && param.getBrandList().size() > 0) {
             if (param.isInclude()) {
                 queryObject.addQuery("{'common.fields.brand':{$in:#}}");
                 queryObject.addParameters(param.getBrandList());
             } else {
                 // 不在指定范围
                 queryObject.addQuery("{'common.fields.brand':{$nin:#}}");
                 queryObject.addParameters(param.getBrandList());
             }
         }


        //库存 quantity
        if (StringUtils.isNotEmpty(param.getCompareType()) && param.getQuantity() != null) {
            queryObject.addQuery("{'common.fields.quantity':{#:#}}");
            queryObject.addParameters(param.getCompareType(), param.getQuantity());
        }

        // 获取code list用于检索code,model,sku
        if (param.getCodeList() != null
                && param.getCodeList().size() > 0) {
            List<String> inputCodeList = param.getCodeList();
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                Object inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);
                queryObject.addQuery("{$or:[{'common.fields.code':{$in:#}},{'common.fields.model':{$in:#}},{'common.skus.skuCode':{$in:#}}]}");
                queryObject.addParameters(inputCodeArr, inputCodeArr, inputCodeArr);
            }
        }
        return queryObject;
    }
}