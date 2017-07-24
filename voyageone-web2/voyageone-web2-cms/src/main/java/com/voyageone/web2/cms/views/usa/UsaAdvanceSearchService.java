package com.voyageone.web2.cms.views.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import com.voyageone.service.impl.cms.search.product.CmsProductSearchQueryService;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2017/7/24.
 */
@Service
public class UsaAdvanceSearchService extends BaseViewService {

    @Autowired
    CmsProductSearchQueryService cmsProductSearchQueryService;

    @Autowired
    private ProductService productService;


    public List<CmsBtProductBean> saleDataSort(CmsSearchInfoBean2 params, UserSessionBean userInfo) {
        int pageNum = params.getProductPageNum();
        int pageSize = params.getProductPageSize();
        String cartId = params.getSortOneName();
        params.setSortOneName(null);
        CmsProductCodeListBean cmsProductCodeListBean = cmsProductSearchQueryService.getProductCodeList(params, userInfo.getSelChannelId(), userInfo.getUserId(), userInfo.getUserName());
        List<String> productCodeList = new ArrayList<>();
        long productListTotal = cmsProductCodeListBean.getTotalCount();
        //要根据查询出来的总页数设置分页
        long pageNumber = 0;
        if (productListTotal % 10000 == 0) {
            //整除
            pageNumber = productListTotal / 10000;
        } else {
            //不整除
            pageNumber = (productListTotal / 10000) + 1;
        }
        for (int i = 0; i < pageNumber; i++) {
            params.setProductPageSize(10000);
            params.setProductPageNum(i + 1);
            CmsProductCodeListBean cmsProductCodeListBean1 = cmsProductSearchQueryService.getProductCodeList(params, userInfo.getSelChannelId());
            if (cmsProductCodeListBean1.getProductCodeList() != null) {
                productCodeList.addAll(cmsProductCodeListBean1.getProductCodeList());
            }
        }

        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery(new Criteria("common.fields.code").in(productCodeList));
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);

        queryObject.setSort(String.format("{'sales.P%s':%s}", cartId, params.getSortOneType()));
        return productService.getBeanList(userInfo.getSelChannelId(), queryObject);
    }
}
