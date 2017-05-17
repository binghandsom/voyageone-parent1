package com.voyageone.service.impl.cms.search.product;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.components.solr.bean.CmsProductSearchModel;
import com.voyageone.components.solr.query.SimpleQueryBean;
import com.voyageone.components.solr.service.CmsProductSearchService;
import com.voyageone.service.bean.cms.search.product.CmsProductCodeListBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.search.CmsSearchInfoBean2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Service;
import org.apache.solr.client.solrj.util.ClientUtils;

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
        SimpleQueryBean queryBean = getSearchQuery(searchValue, channelId);

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
        } catch (Exception ex) {
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

    public SimpleQueryBean getSearchQuery(CmsSearchInfoBean2 searchValue, String channelId) {

        Criteria criteria = new Criteria("productChannel").is(channelId);

        // 添加platform cart
        int cartId = searchValue.getCartId();

        // 只有选中具体的某个平台的时候,和platform相关的检索才有效
        if (cartId > 1) {
            // 设置platform检索条件
            // 获取platform/lock
            if (StringUtils.isNotEmpty(searchValue.getpLockFlg())) {
                if ("1".equals(searchValue.getpLockFlg())) {
                    criteria = criteria.and("P" + cartId + "_lock").is("1");
                } else {
                    criteria = criteria.and("P" + cartId + "_lock").is("1").not();
                }
            }
            // 获取platform/cart status
            if (searchValue.getPlatformStatus() != null && searchValue.getPlatformStatus().size() > 0) {
                criteria = criteria.and("P" + cartId + "_pStatus").in(searchValue.getPlatformStatus());
            }

            // 获取product status
            if (searchValue.getProductStatus() != null && searchValue.getProductStatus().size() > 0) {
                criteria = criteria.and("P" + cartId + "_status").in(searchValue.getProductStatus());
            }

            // 获取实际平台状态
            if (searchValue.getpRealStatus() != null && searchValue.getpRealStatus().size() > 0) {
                criteria = criteria.and("P" + cartId + "_pReallyStatus").in(searchValue.getpRealStatus());
            }

            // 获取platform category
            if (searchValue.getpCatPathList() != null && searchValue.getpCatPathList().size() > 0) {
                criteria = criteria.and("P" + cartId + "_pCatPath").in(searchValue.getpCatPathList());
            }


            // 获取promotion tag查询条件
            if (searchValue.getPromotionTags() != null && searchValue.getPromotionTags().length > 0 && searchValue.getPromotionTagType() > 0) {
                criteria = criteria.and("tags").in(searchValue.getPromotionTags());
            }

            // 获取店铺内分类查询条件
            if (searchValue.getCidValue() != null && searchValue.getCidValue().size() > 0) {
                if (1 == searchValue.getShopCatType()) {
                    criteria = criteria.and("P" + cartId + "_sellerCats").in(searchValue.getCidValue());
                } else {
                    criteria = criteria.and("P" + cartId + "_sellerCats").in(searchValue.getCidValue()).not();
                }
            }

            // 查询价格变动(指导售价)
            if (StringUtils.isNotEmpty(searchValue.getPriceChgFlg())) {
                if (searchValue.getPriceChgFlg().startsWith("X")) {
                    criteria = criteria.and("P" + cartId + "_priceMsrpFlg").contains(searchValue.getPriceChgFlg());
                } else {
                    criteria = criteria.and("P" + cartId + "_priceChgFlg").contains(searchValue.getPriceChgFlg());
                }
            }

            // 查询价格比较（最终售价）
            if (StringUtils.isNotEmpty(searchValue.getPriceDiffFlg())) {
                criteria = criteria.and("P" + cartId + "_priceDiffFlg").contains(searchValue.getPriceDiffFlg());
            }


            // 查询销量范围
            if (StringUtils.isNotEmpty(searchValue.getSalesType())) {
                if (searchValue.getSalesStart() != null) {
                    // 获取销量上限
                    if (searchValue.getSalesEnd() != null) {
                        criteria = criteria.and("P" + cartId + "_sale" + searchValue.getSalesType()).between(searchValue.getSalesStart(), searchValue.getSalesEnd());
                    } else {
                        criteria = criteria.and("P" + cartId + "_sale" + searchValue.getSalesType()).greaterThanEqual(searchValue.getSalesStart());
                    }
                } else {
                    if (searchValue.getSalesEnd() != null) {
                        criteria = criteria.and("P" + cartId + "_sale" + searchValue.getSalesType()).lessThanEqual(searchValue.getSalesEnd());
                    }
                }
            }

            // NumIID多项查询
            if (StringUtils.isNoneEmpty(searchValue.getNumIIds())) {
                // 聚美平台按MallID作为查询条件
                if (CartEnums.Cart.JM.getId().equals(String.valueOf(cartId))) {
                    criteria = criteria.and("P" + cartId + "_pPlatformMallId").in(searchValue.getNumIIds());
                } else {
                    criteria = criteria.and("P" + cartId + "_pNumIId").in(searchValue.getNumIIds());
                }
            }
        }


        // 获取 feed category
        if (searchValue.getfCatPathList() != null && searchValue.getfCatPathList().size() > 0) {
            if ("001".equals(channelId)) {
                searchValue.setfCatPathList(searchValue.getfCatPathList().stream()
                        .map(String::trim)
                        .map(str -> ClientUtils.escapeQueryChars(str) + "*")
                        .collect(Collectors.toList()));
                Criteria tempCriteria = null;
                for (String str : searchValue.getfCatPathList()) {
                    if (tempCriteria == null) {
                        if (searchValue.getfCatPathType() == 1) {
                            tempCriteria = new Criteria("subCategories").expression(str);
                        } else {
                            criteria = criteria.and("subCategories").expression(str).not();
                        }
                    } else {
                        if (searchValue.getfCatPathType() == 1) {
                            tempCriteria = tempCriteria.or("subCategories").expression(str);
                        } else {
                            criteria = criteria.and("subCategories").expression(str).not();
                        }
                    }
                }
                if (searchValue.getfCatPathType() == 1) {
                    criteria = criteria.and(tempCriteria);
                }
            } else {
                searchValue.setfCatPathList(searchValue.getfCatPathList().stream()
                        .map(String::trim)
                        .map(ClientUtils::escapeQueryChars)
                        .collect(Collectors.toList()));
                if (searchValue.getfCatPathType() == 1) {
                    criteria = criteria.and("feedCat").contains(searchValue.getfCatPathList());
                } else {
                    criteria = criteria.and("feedCat").contains(searchValue.getfCatPathList()).not();
                }
            }

        }

        // 获取 master category
        if (ListUtils.notNull(searchValue.getmCatPath())) {
            if (searchValue.getmCatPathType() == 1) {
                criteria = criteria.and("catPath").contains(searchValue.getmCatPath());
            } else {
                criteria = criteria.and("catPath").contains(searchValue.getmCatPath()).not();
            }
        }

        if (StringUtils.isNotEmpty(searchValue.getCreateTimeStart())) {
            // 获取createdTime End
            if (StringUtils.isNotEmpty(searchValue.getCreateTimeTo())) {
                criteria = criteria.and("created").between(searchValue.getCreateTimeStart() + " 00.00.00", searchValue.getCreateTimeTo() + " 23.59.59");
            } else {
                criteria = criteria.and("created").greaterThanEqual(searchValue.getCreateTimeStart() + " 00.00.00");
            }
        } else {
            if (StringUtils.isNotEmpty(searchValue.getCreateTimeTo())) {
                criteria = criteria.and("created").lessThanEqual(searchValue.getCreateTimeTo() + " 00.00.00");
            }
        }

        // 获取inventory
        if (StringUtils.isNotEmpty(searchValue.getCompareType()) && searchValue.getInventory() != null) {
            if (searchValue.getCompareType().equals("$eq")) {
                criteria = criteria.and("quantity").is(searchValue.getInventory());
            } else if (searchValue.getCompareType().equals("$lt")) {
                criteria = criteria.and("quantity").lessThan(searchValue.getInventory());
            } else {
                criteria = criteria.and("quantity").greaterThan(searchValue.getInventory());
            }
        }

        // 获取brand
        if (searchValue.getBrandList() != null && searchValue.getBrandList().size() > 0 && searchValue.getBrandSelType() > 0) {
            searchValue.setBrandList(searchValue.getBrandList().stream().map(String::trim).collect(Collectors.toList()));
            if (searchValue.getBrandSelType() == 1) {
                criteria = criteria.and("brand").in(searchValue.getBrandList());
            } else if (searchValue.getBrandSelType() == 2) {
                criteria = criteria.and("brand").in(searchValue.getBrandList()).not();
            }
        }

        // 获取free tag查询条件
        if (searchValue.getFreeTags() != null && searchValue.getFreeTags().size() > 0 && searchValue.getFreeTagType() > 0) {
            if (searchValue.getFreeTagType() == 1) {
                criteria = criteria.and("freeTags").in(searchValue.getFreeTags());
            } else if (searchValue.getFreeTagType() == 2) {
                // 不在指定范围
                criteria = criteria.and("freeTags").in(searchValue.getFreeTags()).not();
            }
        }

        // 获取翻译状态
        if (StringUtils.isNotEmpty(searchValue.getTransStsFlg())) {
            if ("1".equals(searchValue.getTransStsFlg()) || "2".equals(searchValue.getTransStsFlg())) {
                criteria = criteria.and("translateStatus").is(searchValue.getTransStsFlg());
            } else {
                criteria = criteria.and("translateStatus").is(Arrays.asList("1", "2")).not();
            }
        }
        // 获取主类目完成状态
        if (StringUtils.isNotEmpty(searchValue.getmCatStatus())) {
            if ("1".equals(searchValue.getmCatStatus())) {
                criteria = criteria.and("categoryStatus").is(searchValue.getmCatStatus());
            } else {
                criteria = criteria.and("categoryStatus").is("1").not();
            }
        }
        // 获取税号设置完成状态
        if (StringUtils.isNotEmpty(searchValue.getTaxNoStatus())) {
            if ("1".equals(searchValue.getTaxNoStatus())) {
                criteria = criteria.and("hsCodeStatus").is(searchValue.getTaxNoStatus());
            } else {
                criteria = criteria.and("hsCodeStatus").is("1").not();
            }
        }
        // 获取商品锁定状态
        if (StringUtils.isNotEmpty(searchValue.getLockFlg())) {
            if ("1".equals(searchValue.getLockFlg())) {
                criteria = criteria.and("lock").is("1");
            } else {
                criteria = criteria.and("lock").is("1").not();
            }
        }
        // 获取产品类型设置状态
        if (StringUtils.isNotEmpty(searchValue.getProductSelType())
                && CollectionUtils.isNotEmpty(searchValue.getProductTypeList())) {
            if ("1".equals(searchValue.getProductSelType())) {
                criteria = criteria.and("productType").in(searchValue.getProductTypeList());
            } else if ("2".equals(searchValue.getProductSelType())) {
                criteria = criteria.and("productType").in(searchValue.getProductTypeList()).not();
            }
        }

        // 获取尺寸类型设置状态
        if (StringUtils.isNotEmpty(searchValue.getSizeSelType())
                && CollectionUtils.isNotEmpty(searchValue.getSizeTypeList())) {
            if ("1".equals(searchValue.getSizeSelType())) {
                criteria = criteria.and("sizeType").in(searchValue.getSizeTypeList());
            } else if ("2".equals(searchValue.getProductSelType())) {
                criteria = criteria.and("sizeType").in(searchValue.getSizeTypeList()).not();
            }
        }

        // MINI MALL 店铺时查询原始CHANNEL(供应商)
        if (searchValue.getSupplierList() != null && searchValue.getSupplierList().size() > 0 && searchValue.getSupplierType() > 0) {
            if (searchValue.getSupplierType() == 1) {
                criteria = criteria.and("orgChannelId").in(searchValue.getSupplierList());
            } else if (searchValue.getSupplierType() == 2) {
                // 不在指定范围
                criteria = criteria.and("orgChannelId").in(searchValue.getSupplierList()).not();
            }
        }

        // 获取code list用于检索code,model,sku
        if (searchValue.getCodeList() != null
                && searchValue.getCodeList().length > 0) {
            List<String> inputCodeList = Arrays.asList(searchValue.getCodeList());
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                criteria = criteria.and("keywords").in(inputCodeList);
            }
        }

        // 获取模糊查询条件，用于检索产品名，描述
        if (StringUtils.isNotEmpty(searchValue.getFuzzyStr())) {
            List<String> orSearch = new ArrayList<>();
            // 英文查询内容
            String fuzzyStr = searchValue.getFuzzyStr();
            fuzzyStr = "*" + fuzzyStr.replaceAll(" ", "\\\\ ") + "*";
            Criteria criteria1 = new Criteria("nameEn").expression(fuzzyStr).or("nameCn").expression(fuzzyStr);
            criteria = criteria.and(criteria1);
        }

        if (searchValue.getNoSale() != null && searchValue.getNoSale()) {
            criteria = criteria
                    .and("P20.pNumIId").in(Arrays.asList("", null))
                    .and("P20.pNumIId").in(Arrays.asList("", null))
                    .and("P21.pNumIId").in(Arrays.asList("", null))
                    .and("P22.pNumIId").in(Arrays.asList("", null))
                    .and("P23.pNumIId").in(Arrays.asList("", null))
                    .and("P24.pNumIId").in(Arrays.asList("", null))
                    .and("P25.pNumIId").in(Arrays.asList("", null))
                    .and("P26.pNumIId").in(Arrays.asList("", null))
                    .and("P27.pNumIId").in(Arrays.asList("", null));
        }


        SimpleQueryBean query = new SimpleQueryBean(criteria);

        // Fields
        query.addProjectionOnField("productCode");

        // Sort
        getSortValue(searchValue, query);

        // limit & page
        int offset = (searchValue.getProductPageNum() - 1) * searchValue.getProductPageSize();
        int limit = searchValue.getProductPageSize();
        query.setRows(limit);
        query.setOffset(offset);

        return query;
    }

    public void getSortValue(CmsSearchInfoBean2 searchValue, SimpleQueryBean query) {
        StringBuilder result = new StringBuilder();

        // 获取排序字段1
        if (StringUtils.isNotEmpty(searchValue.getSortOneName()) && StringUtils.isNotEmpty(searchValue.getSortOneType())) {
            Sort.Direction sortType;
            if ("1".equalsIgnoreCase(searchValue.getSortOneType())) {
                sortType = Sort.Direction.ASC;
            } else {
                sortType = Sort.Direction.DESC;
            }
            query.addSort(new Sort(sortType, searchValue.getSortOneName()));
        }

        // 获取排序字段2
        if (StringUtils.isNotEmpty(searchValue.getSortTwoName()) && StringUtils.isNotEmpty(searchValue.getSortTwoType())) {
            Sort.Direction sortType;
            if ("1".equalsIgnoreCase(searchValue.getSortTwoType())) {
                sortType = Sort.Direction.ASC;
            } else {
                sortType = Sort.Direction.DESC;
            }
            query.addSort(new Sort(sortType, searchValue.getSortTwoName()));
        }

        // 获取排序字段3
        if (StringUtils.isNotEmpty(searchValue.getSortThreeName()) && StringUtils.isNotEmpty(searchValue.getSortThreeType())) {
            Sort.Direction sortType;
            if ("1".equalsIgnoreCase(searchValue.getSortThreeType())) {
                sortType = Sort.Direction.ASC;
            } else {
                sortType = Sort.Direction.DESC;
            }
            query.addSort(new Sort(sortType, searchValue.getSortThreeName()));
        }

    }
}
