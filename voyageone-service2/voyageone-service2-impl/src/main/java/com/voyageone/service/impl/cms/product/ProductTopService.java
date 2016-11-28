package com.voyageone.service.impl.cms.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.bean.cms.producttop.ProductInfo;
import com.voyageone.service.bean.cms.producttop.ProductTopPageParameter;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductTopDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
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

    public List<ProductInfo> getPage(ProductTopPageParameter param, String channelId) {


        int pageIndex = param.getPageIndex();
        int pageSize = param.getPageSize();
        JongoQuery queryObject = getJongoQuery(param);
        queryObject.setProjection("");
        queryObject.setSort("{prodId:-1}");
        queryObject.setLimit(pageSize);
        queryObject.setSkip((pageIndex - 1) * pageSize);
        List<CmsBtProductModel> list = cmsBtProductDao.select(queryObject, channelId);
//        return changeToBeanList(list, channelId, lang);
        return null;
    }

    public Object getCount(ProductTopPageParameter param, String channelId) {

        JongoQuery queryObject = getJongoQuery(param);

        return cmsBtProductDao.countByQuery(queryObject.getQuery(), channelId);
    }

     JongoQuery getJongoQuery(ProductTopPageParameter param) {
        JongoQuery queryObject = new JongoQuery();

        //平台cartId    商品分类
        queryObject.addQuery("{'platforms.P#.pCatId':#}");
        queryObject.addParameters(param.getCartId(), param.getpCatId());

        //品牌
        if (!StringUtils.isEmpty(param.getBrand())) {
            if (param.isInclude()) {
                //包含
                queryObject.addQuery("{'common.fields.brand':#}");
                queryObject.addParameters(param.getBrand());
            } else {
                //不包含
                queryObject.addQuery("{'common.fields.brand':{$ne:#}}");
                queryObject.addParameters(param.getBrand());
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