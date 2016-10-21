package com.voyageone.service.impl.cms.search.product;

import com.voyageone.components.solr.bean.CmsProductSearchModel;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CmsProductSearchQueryService
 *
 * @author chuanyu.liang 2016/10/9.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsProductSearchQueryService extends BaseService {

    private static final String SIMPLE_QUERY_TEMPLATE_NO_KEYWORDS = "productChannel:\"%s\"";

    private static final String SIMPLE_QUERY_TEMPLATE_HAVE_KEYWORDS = "productChannel:\"%s\" && keywords:%s";

    @Autowired
    private CmsProductSearchService cmsProductSearchService;

    /**
     * 获取当前查询的product列表（查询条件从画面而来）
     */
    public CmsProductCodeListBean getProductCodeList(CmsSearchInfoBean2 searchValue, String channelId) {
        CmsProductCodeListBean result = new CmsProductCodeListBean();
        SimpleQueryBean queryBean = createQuery(searchValue, channelId);

        if ($isDebugEnabled()) {
            $debug(String.format("获取当前查询的product列表 ChannelId=%s, %s", channelId, queryBean.toString()));
        }

        Page<CmsProductSearchModel> page = cmsProductSearchService.queryForPage(queryBean, CmsProductSearchModel.class);
        List<String> productCodeList = page.getContent().stream().filter(model -> model.getProductCode() != null).map(CmsProductSearchModel::getProductCode).collect(Collectors.toList());
        result.setProductCodeList(productCodeList);
        result.setTotalCount(page.getTotalElements());

        return result;
    }

    public List<String> getTop10ProductModelCodeSkuList(String searchValue, String channelId) {
        List<String> result = new ArrayList<>();
        // condition
        String queryStr;
        if (searchValue != null && searchValue.length() > 0) {
            queryStr = String.format(SIMPLE_QUERY_TEMPLATE_NO_KEYWORDS, channelId) + " && " + String.format("(productModel:%s* || productCode:%s* || skuCode: %s*)", searchValue, searchValue, searchValue);
        } else {
            return result;
        }

        try {

            SimpleQueryBean queryBean = new SimpleQueryBean(queryStr);

            // Fields
            queryBean.addProjectionOnField("productModel, productCode, skuCode");

            // Sort
            queryBean.addSort(new Sort(Sort.Direction.ASC, "productCode"));

            // limit & page
            int offset = 0;
            int limit = 10;
            queryBean.setRows(limit);
            queryBean.setOffset(offset);

            if ($isDebugEnabled()) {
                $debug(String.format("获取当前查询的product列表 ChannelId=%s, %s", channelId, queryBean.toString()));
            }

            Page<CmsProductSearchModel> page = cmsProductSearchService.queryForPage(queryBean, CmsProductSearchModel.class);

            Set<String> resultSet = new HashSet<>();
            for (CmsProductSearchModel model : page.getContent()) {
                if (model.getProductCode() != null && model.getProductCode().toLowerCase().contains(searchValue.toLowerCase())) {
                    resultSet.add(model.getProductCode());
                }

                if (model.getProductModel() != null && model.getProductModel().toLowerCase().contains(searchValue.toLowerCase())) {
                    resultSet.add(model.getProductModel());
                }

                if (model.getSkuCode() != null && !model.getSkuCode().isEmpty()) {
                    resultSet.addAll(model.getSkuCode().stream().filter(skuCode -> skuCode != null && skuCode.toLowerCase().contains(searchValue.toLowerCase())).collect(Collectors.toList()));
                }
            }

            result.addAll(resultSet);
            Collections.sort(result);
            if (result.size() > 10) {
                result = result.subList(0, 10);
            }
        }catch (Exception ex) {
            $error("CmsProductSearchQueryService.getTop10ProductModelCodeSkuList", ex);
        }

        return result;
    }

    private SimpleQueryBean createQuery(CmsSearchInfoBean2 searchValue, String channelId) {
        // condition
        String queryStr = null;
        if (searchValue.getCodeList() != null && searchValue.getCodeList().length > 0) {
            List<String> inputCodeList = Arrays.asList(searchValue.getCodeList());
            inputCodeList = inputCodeList.stream().map(StringUtils::trimToEmpty).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                String inputCodeStr = "(\"" + String.join("\" \"", inputCodeList) + "\")";
                queryStr = String.format(SIMPLE_QUERY_TEMPLATE_HAVE_KEYWORDS, channelId, inputCodeStr);
            }
        }
        if (queryStr == null) {
            queryStr = String.format(SIMPLE_QUERY_TEMPLATE_NO_KEYWORDS, channelId);
        }
        SimpleQueryBean query = new SimpleQueryBean(queryStr);

        // Fields
        query.addProjectionOnField("productCode");

        // Sort
        query.addSort(new Sort(Sort.Direction.DESC, "id"));

        // limit & page
        int offset = (searchValue.getProductPageNum() - 1) * searchValue.getProductPageSize();
        int limit = searchValue.getProductPageSize();
        query.setRows(limit);
        query.setOffset(offset);

        return query;
    }
}
