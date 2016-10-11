package com.voyageone.service.impl.cms.product.search;

import com.voyageone.base.dao.mongodb.JongoAggregate;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsAdvSearchQueryService extends BaseService {

    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    private static final int EXPORT_PAGE_SIZE = 4000;

    // 查询产品信息时的缺省输出列
    public final static String searchItems = "channelId;prodId;created;creater;modified;orgChannelId;modifier;freeTags;sales;bi;platforms;lock;" +
            "common.skus.skuCode;common.fields.originalTitleCn;common.catPath;common.fields.productNameEn;common.fields.brand;common.fields.code;" +
            "common.fields.images1;common.fields.images2;common.fields.images3;common.fields.images4;common.fields.images5;common.fields.images6;common.fields.images7;common.fields.images8;common.fields.images9;" +
            "common.fields.quantity;common.fields.productType;common.fields.sizeType;common.fields.isMasterMain;" +
            "common.fields.priceRetailSt;common.fields.priceRetailEd;common.fields.priceMsrpSt;common.fields.priceMsrpEd;common.fields.hsCodeCrop;common.fields.hsCodePrivate;";

    /**
     * 获取当前查询的product列表（查询条件从画面而来）<br>
     */
    public List<String> getProductCodeList(CmsSearchInfoBean2 searchValue, String channelId) {
        JongoQuery queryObject = getSearchQuery(searchValue);
        queryObject.setProjection("{'common.fields.code':1,'_id':0}");
        queryObject.setSort(getSortValue(searchValue));
        if (searchValue.getProductPageNum() > 0) {
            queryObject.setSkip((searchValue.getProductPageNum() - 1) * searchValue.getProductPageSize());
            queryObject.setLimit(searchValue.getProductPageSize());
        }

        if ($isDebugEnabled()) {
            $debug(String.format("高级检索 获取当前查询的product列表 ChannelId=%s, %s", channelId, queryObject.toString()));
        }

        queryObject.setSkip(0);
        queryObject.setLimit(EXPORT_PAGE_SIZE);
        $info("高级检索 skip:" + queryObject.getSkip());
        $info("高级检索 limit:" + queryObject.getLimit());
        $info("高级检索 query joson:" + JacksonUtil.bean2Json(queryObject));
        $info("高级检索 query str:" + queryObject.toString());

        List<CmsBtProductModel> prodObjList = productService.getList(channelId, queryObject);
        if (prodObjList == null || prodObjList.isEmpty()) {
            $warn("高级检索 getProductCodeList prodObjList为空 查询条件=：" + queryObject.toString());
            return new ArrayList<>(0);
        }
        if (prodObjList.size() == EXPORT_PAGE_SIZE) {
            int page = 1;
            List<CmsBtProductModel> prodObjList2;
            while (true) {
                queryObject.setSkip(page * EXPORT_PAGE_SIZE);
                queryObject.setLimit(EXPORT_PAGE_SIZE);
                prodObjList2 = productService.getList(channelId, queryObject);
                if (prodObjList2 == null || prodObjList2.isEmpty()) {
                    break;
                }
                prodObjList.addAll(prodObjList2);
                page++;
            }
        }

        List<String> codeList = prodObjList.stream().map(prodObj -> prodObj.getCommonNotNull().getFieldsNotNull().getCode()).filter(prodCode -> (prodCode != null && !prodCode.isEmpty())).collect(Collectors.toList());
        return codeList;
    }

    /**
     * 返回当前页的group列表，这里是分页查询<br>
     * 这里不是直接去检索group表，而是根据CmsBtProductModel中的mainProductCode过滤而来
     * 注意要过滤重复code，另外由于$group不会排序，必须在$group中输出排序项后再使用$sort排序
     */
    public List<String> getGroupCodeList(CmsSearchInfoBean2 searchValue, String channelId) {
        List<JongoAggregate> aggrList = new ArrayList<>();
        // 查询条件
        String qry1 = cmsBtProductDao.getQueryStr(getSearchQuery(searchValue));
        if (qry1 != null && qry1.length() > 0) {
            aggrList.add(new JongoAggregate("{ $match : " + qry1 + " }"));
        }

        Map<String, List<String>> sortColList = getSortColumn(searchValue);
        List<String> groupOutList = sortColList.get("groupOutList");
        List<String> sortOutList = sortColList.get("sortOutList");
        if (groupOutList.isEmpty()) {
            // 使用默认排序
            // 分组
            String gp1 = "{ $group : { _id : '$platforms.P" + searchValue.getCartId() + ".mainProductCode', '_pprodId':{$first:'$prodId'} } }";
            aggrList.add(new JongoAggregate(gp1));
            // 排序
            aggrList.add(new JongoAggregate("{ $sort : {'_pprodId':1} }"));
        } else {
            // 分组
            String gp1 = "{ $group : { _id : '$platforms.P" + searchValue.getCartId() + ".mainProductCode'," + org.apache.commons.lang3.StringUtils.join(groupOutList, ',') + "} }";
            aggrList.add(new JongoAggregate(gp1));
            // 排序
            aggrList.add(new JongoAggregate("{ $sort : {" + org.apache.commons.lang3.StringUtils.join(sortOutList, ',') + "} }"));
        }

        aggrList.add(new JongoAggregate("{ $skip:" + (searchValue.getGroupPageNum() - 1) * searchValue.getGroupPageSize() + "}"));
        if (searchValue.getGroupPageSize() > 0) {
            aggrList.add(new JongoAggregate("{ $limit:" + searchValue.getGroupPageSize() + "}"));
        }
        if ($isDebugEnabled()) {
            $debug(String.format("高级检索 获取当前查询的group id列表 ChannelId=%s, %s", channelId, aggrList.toString()));
        }

        List<Map<String, Object>> rs = productService.aggregateToMap(channelId, aggrList);
        if (rs == null || rs.isEmpty()) {
            $warn("高级检索 getGroupCodeList 统计无结果 param=" + searchValue.toString());
            return null;
        }

        return rs.stream().map(rsObj -> (String) rsObj.get("_id")).filter(prodCode -> (prodCode != null && !prodCode.isEmpty())).collect(Collectors.toList());
    }

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    public JongoQuery getSearchQuery(CmsSearchInfoBean2 searchValue) {
        JongoQuery queryObject = new JongoQuery();

        // 添加platform cart
        int cartId = searchValue.getCartId();

        // 只有选中具体的某个平台的时候,和platform相关的检索才有效
        if (cartId > 1) {
            // 设置platform检索条件
            queryObject.addQuery("{'platforms.P#.cartId':#}");
            queryObject.addParameters(cartId, cartId);

            // 获取platform/cart status
            if (searchValue.getPlatformStatus() != null && searchValue.getPlatformStatus().size() > 0) {
                // 获取platform status
                queryObject.addQuery("{'platforms.P#.pStatus':{$in:#}}");
                queryObject.addParameters(cartId, searchValue.getPlatformStatus());
            }

            // 获取product status
            if (searchValue.getProductStatus() != null && searchValue.getProductStatus().size() > 0) {
                queryObject.addQuery("{'platforms.P#.status':{$in:#}}");
                queryObject.addParameters(cartId, searchValue.getProductStatus());
            }

            // 获取实际平台状态
            if (searchValue.getpRealStatus() != null && searchValue.getpRealStatus().size() > 0) {
                queryObject.addQuery("{'platforms.P#.pReallyStatus':{$in:#}}");
                queryObject.addParameters(cartId, searchValue.getpRealStatus());
            }

            // 获取publishTime End
            if (StringUtils.isNotEmpty(searchValue.getPublishTimeStart())) {
                if (StringUtils.isNotEmpty(searchValue.getPublishTimeTo())) {
                    queryObject.addQuery("{'platforms.P#.pPublishTime':{$gte:#,$lte:#}}");
                    queryObject.addParameters(cartId, searchValue.getPublishTimeStart() + " 00.00.00", searchValue.getPublishTimeTo() + " 23.59.59");
                } else {
                    queryObject.addQuery("{'platforms.P#.pPublishTime':{$gte:#}}");
                    queryObject.addParameters(cartId, searchValue.getPublishTimeStart() + " 00.00.00");
                }
            } else {
                // 获取publishTime End
                if (StringUtils.isNotEmpty(searchValue.getPublishTimeTo())) {
                    queryObject.addQuery("{'platforms.P#.pPublishTime':{$lte:#}}");
                    queryObject.addParameters(cartId, searchValue.getPublishTimeStart() + " 00.00.00", searchValue.getPublishTimeTo() + " 23.59.59");
                }
            }

            // 获取price start/end
            if (StringUtils.isNotEmpty(searchValue.getPriceType())) {
                if (searchValue.getPriceStart() != null) {
                    if (searchValue.getPriceEnd() != null) {
                        queryObject.addQuery("{'platforms.P#.skus.#':{$gte:#,$lte:#}}");
                        queryObject.addParameters(cartId, searchValue.getPriceType(), searchValue.getPriceStart(), searchValue.getPriceEnd());
                    } else {
                        queryObject.addQuery("{'platforms.P#.skus.#':{$gte:#}}");
                        queryObject.addParameters(cartId, searchValue.getPriceType(), searchValue.getPriceStart());
                    }
                } else {
                    queryObject.addQuery("{'platforms.P#.skus.#':{$lte:#}}");
                    queryObject.addParameters(cartId, searchValue.getPriceType(), searchValue.getPriceEnd());
                }
            }

            // 获取platform category
            if (searchValue.getpCatPathList() != null && searchValue.getpCatPathList().size() > 0) {
                StringBuilder pCatPathStr = new StringBuilder("{$or:[");
                int idx = 0;
                for (String pCatPath : searchValue.getpCatPathList()) {
                    if (idx == 0) {
                        pCatPathStr.append("{'platforms.P" + cartId + ".pCatPath':{'$regex':'^" + pCatPath + "'}}");
                        idx ++;
                    } else {
                        pCatPathStr.append(",{'platforms.P" + cartId + ".pCatPath':{'$regex':'^" + pCatPath + "'}}");
                    }
                }
                pCatPathStr.append("]}");
                queryObject.addQuery(pCatPathStr.toString());
            }
            // 平台类目是否未设置
            if (searchValue.getPCatStatus() == 1) {
                queryObject.addQuery("{'platforms.P#.pCatStatus':{$in:[null,'','0']}}");
                queryObject.addParameters(cartId);
            }

            // 获取promotion tag查询条件
            if (searchValue.getPromotionTags() != null && searchValue.getPromotionTags().length > 0 && searchValue.getPromotionTagType() > 0) {
                Object para = searchValue.getPromotionTags();
                if (searchValue.getPromotionTagType() == 1) {
                    queryObject.addQuery("{'tags':{$in:#}}");
                    queryObject.addParameters(para);
                } else if (searchValue.getPromotionTagType() == 2) {
                    // 不在指定范围
                    queryObject.addQuery("{'tags':{$nin:#}}");
                    queryObject.addParameters(para);
                }
            }
            // 获取店铺内分类查询条件
            if (searchValue.getCidValue() !=  null && searchValue.getCidValue().size() > 0) {
                queryObject.addQuery("{'platforms.P#.sellerCats.cId':{$in:#}}");
                queryObject.addParameters(cartId, searchValue.getCidValue());
            }
            // 店铺内分类未设置
            if (searchValue.getShopCatStatus() == 1) {
                queryObject.addQuery("{$or:[{'platforms.P#.sellerCats':{$size:0}},{'platforms.P#.sellerCats':{$in:[null,'']}}]}");
                queryObject.addParameters(cartId, cartId);
            }

            // 查询产品上新错误
            if (searchValue.getHasErrorFlg() > 0) {
                if (searchValue.getHasErrorFlg() == 1) {
                    queryObject.addQuery("{'platforms.P#.pPublishError':{$in:[null,'']}}");
                    queryObject.addParameters(cartId);
                } else if (searchValue.getHasErrorFlg() == 2) {
                    queryObject.addQuery("{'platforms.P#.pPublishError':'Error'}");
                    queryObject.addParameters(cartId);
                } else if (searchValue.getHasErrorFlg() == 3) {
                    queryObject.addQuery("{$or:[{'platforms.P#.pReallyStatus':'OnSale','platforms.P#.pStatus':{$ne:'OnSale'}},{'platforms.P#.pReallyStatus':'InStock','platforms.P#.pStatus':{$ne:'InStock'}}]}");
                    queryObject.addParameters(cartId, cartId, cartId, cartId);
                }
            }

            // 查询价格变动(指导售价)
            if (StringUtils.isNotEmpty(searchValue.getPriceChgFlg())) {
                if (searchValue.getPriceChgFlg().startsWith("X")) {
                    // 比较指导售价和建议售价的大小
                    queryObject.addQuery("{'platforms.P#.skus.priceMsrpFlg':{'$regex':'^" + searchValue.getPriceChgFlg() + "'}}");
                    queryObject.addParameters(cartId);
                } else {
                    queryObject.addQuery("{'platforms.P#.skus.priceChgFlg':{'$regex':'^" + searchValue.getPriceChgFlg() + "'}}");
                    queryObject.addParameters(cartId);
                }
            }

            // 查询价格比较（最终售价）
            if (StringUtils.isNotEmpty(searchValue.getPriceDiffFlg())) {
                queryObject.addQuery("{'platforms.P#.skus.priceDiffFlg':#}");
                queryObject.addParameters(cartId, searchValue.getPriceDiffFlg());
            }

            // 获取平台属性设置状态(是否完成)
            if (StringUtils.isNotEmpty(searchValue.getPropertyStatus())) {
                if ("1".equals(searchValue.getPropertyStatus())) {
                    queryObject.addQuery("{'platforms.P#.pAttributeStatus':#}");
                    queryObject.addParameters(cartId, searchValue.getPropertyStatus());
                } else {
                    queryObject.addQuery("{'platforms.P#.pAttributeStatus':{$in:[null,'','0']}}");
                    queryObject.addParameters(cartId);
                }
            }

            // 查询销量范围
            if (StringUtils.isNotEmpty(searchValue.getSalesType())) {
                if (searchValue.getSalesStart() != null) {
                    // 获取销量上限
                    if (searchValue.getSalesEnd() != null) {
                        queryObject.addQuery("{'sales.codeSum#.cartId#':{$gte:#,$lte:#}}");
                        queryObject.addParameters(searchValue.getSalesType(), cartId, searchValue.getSalesStart(), searchValue.getSalesEnd());
                    } else {
                        queryObject.addQuery("{'sales.codeSum#.cartId#':{$gte:#}}");
                        queryObject.addParameters(searchValue.getSalesType(), cartId, searchValue.getSalesStart());
                    }
                } else {
                    if (searchValue.getSalesEnd() != null) {
                        queryObject.addQuery("{'sales.codeSum#.cartId#':{$lte:#}}");
                        queryObject.addParameters(searchValue.getSalesType(), cartId, searchValue.getSalesEnd());
                    }
                }
            }
        }

        // 获取其他检索条件
        getSearchValueForMongo(searchValue, queryObject);
        return queryObject;
    }

    /**
     * 获取其他检索条件
     */
    private void getSearchValueForMongo(CmsSearchInfoBean2 searchValue, JongoQuery queryObject) {
        // 获取 feed category
        if (searchValue.getfCatPathList() != null && searchValue.getfCatPathList().size() > 0) {
            StringBuilder fCatPathStr = new StringBuilder("{$or:[");
            int idx = 0;
            for (String fCatPath : searchValue.getfCatPathList()) {
                if (idx == 0) {
                    fCatPathStr.append("{'feed.catPath':{'$regex':'^" + fCatPath + "'}}");
                    idx ++;
                } else {
                    fCatPathStr.append(",{'feed.catPath':{'$regex':'^" + fCatPath + "'}}");
                }
            }
            fCatPathStr.append("]}");
            queryObject.addQuery(fCatPathStr.toString());
        }

        // 获取 master category
        if (StringUtils.isNotEmpty(searchValue.getmCatPath())) {
            queryObject.addQuery("{'common.catPath':{'$regex':'^" + searchValue.getmCatPath() + "'}}");
        }

        if (StringUtils.isNotEmpty(searchValue.getCreateTimeStart())) {
            // 获取createdTime End
            if (StringUtils.isNotEmpty(searchValue.getCreateTimeTo())) {
                queryObject.addQuery("{'created':{$gte:#,$lte:#}}");
                queryObject.addParameters(searchValue.getCreateTimeStart() + " 00.00.00", searchValue.getCreateTimeTo() + " 23.59.59");
            } else {
                queryObject.addQuery("{'created':{$gte:#}}");
                queryObject.addParameters(searchValue.getCreateTimeStart() + " 00.00.00");
            }
        } else {
            if (StringUtils.isNotEmpty(searchValue.getCreateTimeTo())) {
                queryObject.addQuery("{'created':{$lte:#}}");
                queryObject.addParameters(searchValue.getCreateTimeTo() + " 23.59.59");
            }
        }

        // 获取inventory
        if (StringUtils.isNotEmpty(searchValue.getCompareType()) && searchValue.getInventory() != null) {
            queryObject.addQuery("{'common.fields.quantity':{#:#}}");
            queryObject.addParameters(searchValue.getCompareType(), searchValue.getInventory());
        }

        // 获取brand
        if (searchValue.getBrandList() !=  null && searchValue.getBrandList().size() > 0 && searchValue.getBrandSelType() > 0) {
            if (searchValue.getBrandSelType() == 1) {
                queryObject.addQuery("{'common.fields.brand':{$in:#}}");
                queryObject.addParameters(searchValue.getBrandList());
            } else if (searchValue.getBrandSelType() == 2) {
                // 不在指定范围
                queryObject.addQuery("{'common.fields.brand':{$nin:#}}");
                queryObject.addParameters(searchValue.getBrandList());
            }
        }

        // 获取free tag查询条件
        if (searchValue.getFreeTags() != null && searchValue.getFreeTags().size() > 0 && searchValue.getFreeTagType() > 0) {
            if (searchValue.getFreeTagType() == 1) {
                queryObject.addQuery("{'freeTags':{$in:#}}");
                queryObject.addParameters(searchValue.getFreeTags());
            } else if (searchValue.getFreeTagType() == 2) {
                // 不在指定范围
                queryObject.addQuery("{'freeTags':{$nin:#}}");
                queryObject.addParameters(searchValue.getFreeTags());
            }
        }

        // 获取翻译状态
        if (StringUtils.isNotEmpty(searchValue.getTransStsFlg())) {
            if ("1".equals(searchValue.getTransStsFlg()) || "2".equals(searchValue.getTransStsFlg())) {
                queryObject.addQuery("{'common.fields.translateStatus':#}");
                queryObject.addParameters(searchValue.getTransStsFlg());
            } else {
                queryObject.addQuery("{'common.fields.translateStatus':{$in:[null,'','0']}}");
            }
        }
        // 获取主类目完成状态
        if (StringUtils.isNotEmpty(searchValue.getmCatStatus())) {
            if ("1".equals(searchValue.getmCatStatus())) {
                queryObject.addQuery("{'common.fields.categoryStatus':#}");
                queryObject.addParameters(searchValue.getmCatStatus());
            } else {
                queryObject.addQuery("{'common.fields.categoryStatus':{$in:[null,'','0']}}");
            }
        }
        // 获取税号设置完成状态
        if (StringUtils.isNotEmpty(searchValue.getTaxNoStatus())) {
            if ("1".equals(searchValue.getTaxNoStatus())) {
                queryObject.addQuery("{'common.fields.hsCodeStatus':#}");
                queryObject.addParameters(searchValue.getTaxNoStatus());
            } else {
                queryObject.addQuery("{'common.fields.hsCodeStatus':{$in:[null,'','0']}}");
            }
        }

        // 获取商品锁定状态
        if (StringUtils.isNotEmpty(searchValue.getLockFlg())) {
            queryObject.addQuery("{'lock':#}");
            queryObject.addParameters(searchValue.getLockFlg());
        }

        // MINI MALL 店铺时查询原始CHANNEL
        if (StringUtils.isNotEmpty(searchValue.getOrgChaId()) && !ChannelConfigEnums.Channel.NONE.getId().equals(searchValue.getOrgChaId())) {
            queryObject.addQuery("{'orgChannelId':#}");
            queryObject.addParameters(searchValue.getOrgChaId());
        }

        // 获取code list用于检索code,model,sku
        if (searchValue.getCodeList() != null
                && searchValue.getCodeList().length > 0) {
            List<String> inputCodeList = Arrays.asList(searchValue.getCodeList());
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            if (inputCodeList.size() > 0) {
                Object inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);

                queryObject.addQuery("{$or:[{'common.fields.code':{$in:#}},{'common.fields.model':{$in:#}},{'common.skus.skuCode':{$in:#}}]}");
                queryObject.addParameters(inputCodeArr, inputCodeArr, inputCodeArr);
            }
        }

        // 获取模糊查询条件，用于检索产品名，描述
        if (StringUtils.isNotEmpty(searchValue.getFuzzyStr())) {
            List<String> orSearch = new ArrayList<>();
            // 英文查询内容
            String fuzzyStr = searchValue.getFuzzyStr();
            queryObject.addQuery("{$or:[{'common.fields.productNameEn':{$regex:#}},{'common.fields.longDesEn':{$regex:#}},{'common.fields.shortDesEn':{$regex:#}},{'common.fields.originalTitleCn':{$regex:#}},{'common.fields.shortDesCn':{$regex:#}},{'common.fields.longDesCn':{$regex:#}}]}");
            queryObject.addParameters(fuzzyStr, fuzzyStr, fuzzyStr, fuzzyStr, fuzzyStr, fuzzyStr);
        }

        // 获取自定义查询条件
        // 1.  >  有输入框  eg {"a": {$gt: 123123}}
        // 2.  =  有输入框  eg {"a": 123123}}
        // 3.  <  有输入框  eg {"a": {$lt: 123123}}
        // 4.  =null(未设值)  无输入框  eg {"a": {$in:[null,'']}}
        // 5.  !=null(已设值) 无输入框  eg {"a": {$nin:[null,''], $exists:true}}
        // 6.  包含   有输入框  eg {"a": {$regex: "oops"}}
        // 7.  不包含 有输入框  eg {"a":{$not: {$regex: "oops"}}}
        List<Map<String, Object>> custList = searchValue.getCustAttrMap();
        if (custList != null && custList.size() > 0) {
            List<String> inputList = new ArrayList<>();
            for (Map<String, Object> item : custList) {
                String inputOptsKey = StringUtils.trimToNull((String) item.get("inputOptsKey"));//条件字段
                if (inputOptsKey == null) {
                    continue;
                }
                Object inputOptsObj = item.get("inputOpts");//操作符
                String inputVal = StringUtils.trimToNull((String) item.get("inputVal"));//值
                String inputType = StringUtils.trimToNull((String) item.get("inputType"));//输入类型 list/string/number
                String optsWhere = getCustAttrOptsWhere(inputOptsKey, inputOptsObj, inputVal, inputType);
                if (!StringUtil.isEmpty(optsWhere)) {
                    inputList.add(optsWhere);
                }
            }
            if (inputList.size() > 0) {
                queryObject.addQuery("{'$and':[" + inputList.stream().collect(Collectors.joining(",")) + "]}");
            }
        }
    }

    private String getCustAttrOptsWhere(String inputOptsKey, Object inputOpts, String inputVal, String inputType) {
        if (inputOptsKey == null) {
            return null;
        }
        String result = null;
        if (inputType != null && inputType.indexOf("list") == 0) {
            if (inputOpts == null) {
                return null;
            }
            if ("list-1".equals(inputType)) {
                // 数字类型
                if (inputOpts instanceof String && StringUtils.trimToNull((String) inputOpts) == null) {
                    // 未设值
                    result = "{'" + inputOptsKey + "':{$in:[null,'']}}";
                } else {
                    result = "{'" + inputOptsKey + "':" + inputOpts + "}";
                }
            } else {
                // 字符串
                if (inputOpts instanceof String && StringUtils.trimToNull((String) inputOpts) == null) {
                    // 未设值
                    result = "{'" + inputOptsKey + "':{$in:[null,'']}}";
                } else {
                    result = "{'" + inputOptsKey + "':'" + inputOpts + "'}";
                }
            }
            return  result;
        }
        if (inputOpts == null || (inputOpts instanceof String && StringUtils.isEmpty((String) inputOpts))) {
            return null;
        }
        switch ((Integer) inputOpts) {
            case 1:
                if (inputVal == null) {
                    break;
                }
                result = "{'" + inputOptsKey + "':{$gt:" + inputVal + "}}";
                break;
            case 2:
                if (inputVal == null) {
                    break;
                }
                result = "{'" + inputOptsKey + "':" + inputVal + "}";
                break;
            case 3:
                if (inputVal == null) {
                    break;
                }
                result = "{'" + inputOptsKey + "':{$lt:" + inputVal + "}}";
                break;
            case 4:
                result = "{'" + inputOptsKey + "':{$in:[null,'']}}";
                break;
            case 5:
                result = "{'" + inputOptsKey + "':{$nin:[null,''],$exists:true}}";
                break;
            case 6:
                if (inputVal == null) {
                    break;
                }
                result = "{'" + inputOptsKey + "':{$regex:'" + inputVal + "'}}";
                break;
            case 7:
                if (inputVal == null) {
                    break;
                }
                result = "{'" + inputOptsKey + "':{$not:{$regex:'" + inputVal + "'}}}";
                break;
            default:
                break;
        }
        return  result;
    }

    /**
     * 获取排序规则
     */
    public String getSortValue(CmsSearchInfoBean2 searchValue) {
        StringBuilder result = new StringBuilder();

        // 获取排序字段1
        if (StringUtils.isNotEmpty(searchValue.getSortOneName()) && StringUtils.isNotEmpty(searchValue.getSortOneType())) {
            if ("comment".equals(searchValue.getSortOneName())) {
                result.append(MongoUtils.splicingValue("common.comment", Integer.valueOf(searchValue.getSortOneType())));
            } else if (searchValue.getSortOneName().startsWith("bi.sum")) {
                // 按指定bi数据排序
                result.append(MongoUtils.splicingValue(searchValue.getSortOneName(), Integer.valueOf(searchValue.getSortOneType())));
            } else {
                result.append(MongoUtils.splicingValue("common.fields." + searchValue.getSortOneName(), Integer.valueOf(searchValue.getSortOneType())));
            }
            result.append(",");
        }

        // 获取排序字段2
        if (StringUtils.isNotEmpty(searchValue.getSortTwoName()) && StringUtils.isNotEmpty(searchValue.getSortTwoType())) {
            if ("comment".equals(searchValue.getSortTwoName())) {
                result.append(MongoUtils.splicingValue("common.comment", Integer.valueOf(searchValue.getSortTwoType())));
            } else if (searchValue.getSortTwoName().startsWith("bi.sum")) {
                // 按指定bi数据排序
                result.append(MongoUtils.splicingValue(searchValue.getSortTwoName(), Integer.valueOf(searchValue.getSortTwoType())));
            } else {
                result.append(MongoUtils.splicingValue("common.fields." + searchValue.getSortTwoName(), Integer.valueOf(searchValue.getSortTwoType())));
            }
            result.append(",");
        }

        // 获取排序字段3
        if (StringUtils.isNotEmpty(searchValue.getSortThreeName()) && StringUtils.isNotEmpty(searchValue.getSortThreeType())) {
            if ("comment".equals(searchValue.getSortThreeName())) {
                result.append(MongoUtils.splicingValue("common.comment", Integer.valueOf(searchValue.getSortThreeType())));
            } else if (searchValue.getSortThreeName().startsWith("bi.sum")) {
                // 按指定bi数据排序
                result.append(MongoUtils.splicingValue(searchValue.getSortThreeName(), Integer.valueOf(searchValue.getSortThreeType())));
            } else {
                result.append(MongoUtils.splicingValue("common.fields." + searchValue.getSortThreeName(), Integer.valueOf(searchValue.getSortThreeType())));
            }
            result.append(",");
        }

        // 添加platform cart
        int cartId = searchValue.getCartId();
        if (cartId > 1) {
            // 获取按销量排序字段
            if (StringUtils.isNotEmpty(searchValue.getSalesType()) && StringUtils.isNotEmpty(searchValue.getSalesSortType())) {
                result.append(MongoUtils.splicingValue("sales.codeSum" + searchValue.getSalesType(), Integer.valueOf(searchValue.getSalesSortType())));
                result.append(",");
            }
        }

        return result.toString().length() > 0 ? "{" + result.toString().substring(0, result.toString().length() - 1) + "}" : "{'prodId':1}";
    }

    /**
     * 获取排序列一览（group查询时专用）
     */
    public Map<String, List<String>> getSortColumn(CmsSearchInfoBean2 searchValue) {
        List<String> groupOutList = new ArrayList<>();
        List<String> sortOutList = new ArrayList<>();
        int listIdx = 0;

        // 获取排序字段1
        if (StringUtils.isNotEmpty(searchValue.getSortOneName()) && StringUtils.isNotEmpty(searchValue.getSortOneType())) {
            listIdx = groupOutList.size() + 1;
            if ("comment".equals(searchValue.getSortOneName())) {
                groupOutList.add("'col" + listIdx + "':{$first:'$common.comment'}");
                sortOutList.add("'col" + listIdx + "':" + searchValue.getSortOneType());
            } else {
                groupOutList.add("'col" + listIdx + "':{$first:'$common.fields." + searchValue.getSortOneName() + "'}");
                sortOutList.add("'col" + listIdx + "':" + searchValue.getSortOneType());
            }
        }

        // 获取排序字段2
        if (StringUtils.isNotEmpty(searchValue.getSortTwoName()) && StringUtils.isNotEmpty(searchValue.getSortTwoType())) {
            listIdx = groupOutList.size() + 1;
            if ("comment".equals(searchValue.getSortTwoName())) {
                groupOutList.add("'col" + listIdx + "':{$first:'$common.comment'}");
                sortOutList.add("'col" + listIdx + "':" + searchValue.getSortTwoType());
            } else {
                groupOutList.add("'col" + listIdx + "':{$first:'$common.fields." + searchValue.getSortTwoName() + "'}");
                sortOutList.add("'col" + listIdx + "':" + searchValue.getSortTwoType());
            }
        }

        // 获取排序字段3
        if (StringUtils.isNotEmpty(searchValue.getSortThreeName()) && StringUtils.isNotEmpty(searchValue.getSortThreeType())) {
            listIdx = groupOutList.size() + 1;
            if ("comment".equals(searchValue.getSortThreeName())) {
                groupOutList.add("'col" + listIdx + "':{$first:'$common.comment'}");
                sortOutList.add("'col" + listIdx + "':" + searchValue.getSortThreeType());
            } else {
                groupOutList.add("'col" + listIdx + "':{$first:'$common.fields." + searchValue.getSortThreeName() + "'}");
                sortOutList.add("'col" + listIdx + "':" + searchValue.getSortThreeType());
            }
        }

        // 添加platform cart
        int cartId = searchValue.getCartId();
        if (cartId > 1) {
            // 获取按销量排序字段
            if (StringUtils.isNotEmpty(searchValue.getSalesType()) && StringUtils.isNotEmpty(searchValue.getSalesSortType())) {
                listIdx = groupOutList.size() + 1;
                groupOutList.add("'col" + listIdx + "':{$first:'$sales.codeSum" + searchValue.getSalesType() + "'}");
                sortOutList.add("'col" + listIdx + "':" + searchValue.getSalesSortType());
            }
        }

        Map<String, List<String>> result = new HashMap<>();
        result.put("groupOutList", groupOutList);
        result.put("sortOutList", sortOutList);
        return result;
    }

}
