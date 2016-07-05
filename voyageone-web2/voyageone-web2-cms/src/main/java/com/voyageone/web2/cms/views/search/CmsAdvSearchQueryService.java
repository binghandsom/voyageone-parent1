package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.bean.search.index.CmsSearchInfoBean2;
import com.voyageone.web2.cms.views.channel.CmsChannelTagService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsAdvSearchQueryService extends BaseAppService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsChannelTagService cmsChannelTagService;
    @Autowired
    private TagService tagService;

    private final static String _KeyPrefix = "platforms.P";

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    public String getSearchQuery(CmsSearchInfoBean2 searchValue, CmsSessionBean cmsSessionBean, boolean isMain) {
        StringBuilder result = new StringBuilder();

        // 添加platform cart
        int cartId = searchValue.getCartId();

        // 只有选中具体的某个平台的时候,和platform相关的检索才有效
        if (cartId > 1) {
            // 设置platform检索条件
            result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".cartId", cartId));
            result.append(",");

            // 获取platform/cart status
            if (searchValue.getPlatformStatus() != null && searchValue.getPlatformStatus().length > 0) {
                // 获取platform status
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".pStatus", searchValue.getPlatformStatus()));
                result.append(",");
            }

            // 获取product status
            if (searchValue.getProductStatus() != null && searchValue.getProductStatus().length > 0) {
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".status", searchValue.getProductStatus()));
                result.append(",");
            }

            if (StringUtils.isNotEmpty(searchValue.getPublishTimeStart()) || StringUtils.isNotEmpty(searchValue.getPublishTimeTo())) {
                result.append("'" + _KeyPrefix + cartId + ".pPublishTime':{" );
                // 获取publishTime start
                if (StringUtils.isNotEmpty(searchValue.getPublishTimeStart())) {
                    result.append(MongoUtils.splicingValue("$gte", searchValue.getPublishTimeStart() + " 00.00.00"));
                }
                // 获取publishTime End
                if (StringUtils.isNotEmpty(searchValue.getPublishTimeTo())) {
                    if (StringUtils.isNotEmpty(searchValue.getPublishTimeStart())) {
                        result.append(",");
                    }
                    result.append(MongoUtils.splicingValue("$lte", searchValue.getPublishTimeTo() + " 23.59.59"));
                }
                result.append("},");
            }

            // 获取price start
            if (StringUtils.isNotEmpty(searchValue.getPriceType()) && searchValue.getPriceStart() != null) {
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".skus." + searchValue.getPriceType(), searchValue.getPriceStart(), "$gte"));
                result.append(",");
            }
            // 获取price end
            if (StringUtils.isNotEmpty(searchValue.getPriceType()) && searchValue.getPriceEnd() != null) {
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".skus." + searchValue.getPriceType(), searchValue.getPriceEnd(), "$lte"));
                result.append(",");
            }

            // 获取platform category
            if (StringUtils.isNotEmpty(searchValue.getpCatId())) {
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".pCatId", searchValue.getpCatId()));
                result.append(",");
            }
            // 平台类目是否未设置
            if (searchValue.getPCatStatus() == 1) {
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".pCatStatus", new String[]{null, "", "0"}, "$in"));
                result.append(",");
            }

            // 获取promotion tag查询条件
            if (searchValue.getPromotionTags() != null && searchValue.getPromotionTags().length > 0 && searchValue.getPromotionTagType() > 0) {
                if (searchValue.getPromotionTagType() == 1) {
                    result.append(MongoUtils.splicingValue("tags", searchValue.getPromotionTags()));
                    result.append(",");
                } else if (searchValue.getPromotionTagType() == 2) {
                    // 不在指定范围
                    result.append(MongoUtils.splicingValue("tags", searchValue.getPromotionTags(), "$nin"));
                    result.append(",");
                }
            }
            // 获取店铺内分类查询条件
            if (searchValue.getCidValue() !=  null && searchValue.getCidValue().size() > 0) {
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".sellerCats.cId", searchValue.getCidValue().toArray(new String[searchValue.getCidValue().size()])));
                result.append(",");
            }
            // 店铺内分类未设置
            if (searchValue.getShopCatStatus() == 1) {
                result.append("$or:[{'"+ _KeyPrefix + cartId + ".sellerCats':{$size:0}},{'" + _KeyPrefix + cartId + ".sellerCats':{$in:[null,'']}}],");
            }

            // 查询产品上新错误
            if (searchValue.getHasErrorFlg() == 1) {
                result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".pPublisError", "Error"));
                result.append(",");
            }

            // 查询价格变动
            if (StringUtils.isNotEmpty(searchValue.getPriceChgFlg())) {
                result.append("'" + _KeyPrefix + cartId + ".skus':{'$elemMatch':{'priceChgFlg':{'$regex':'^" + searchValue.getPriceChgFlg() + "'}}},");
            }

            // 查询价格比较（建议销售价和实际销售价）
            if (StringUtils.isNotEmpty(searchValue.getPriceDiffFlg())) {
                // 建议销售价等于实际销售价
                result.append("'" + _KeyPrefix + cartId + ".skus':{'$elemMatch':{'priceDiffFlg':'" + searchValue.getPriceChgFlg() + "'}},");
            }

            // 获取平台属性设置状态(是否完成)
            if (StringUtils.isNotEmpty(searchValue.getPropertyStatus())) {
                if ("1".equals(searchValue.getPropertyStatus())) {
                    result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".pAttributeStatus", searchValue.getPropertyStatus()));
                    result.append(",");
                } else {
                    result.append(MongoUtils.splicingValue(_KeyPrefix + cartId + ".pAttributeStatus", new String[]{null, "", "0"}, "$in"));
                    result.append(",");
                }
            }

            // 查询销量范围
            if (StringUtils.isNotEmpty(searchValue.getSalesType())) {
                if (searchValue.getSalesEnd() != null || searchValue.getSalesStart() != null) {
                    result.append("'sales.code_sum_" + searchValue.getSalesType() + ".cartId_" + cartId + "':{");
                    // 获取销量下限
                    if (searchValue.getSalesStart() != null) {
                        result.append(MongoUtils.splicingValue("$gte", searchValue.getSalesStart()));
                    }
                    // 获取销量上限
                    if (searchValue.getSalesEnd() != null) {
                        if (searchValue.getSalesStart() != null) {
                            result.append(",");
                        }
                        result.append(MongoUtils.splicingValue("$lte", searchValue.getSalesEnd()));
                    }
                    result.append("},");
                }
            }
        }

        // 获取其他检索条件
        result.append(getSearchValueForMongo(searchValue));

        if (StringUtils.isNotEmpty(result.toString())) {
            return "{" + result.toString().substring(0, result.toString().length() - 1) + "}";
        } else {
            return "";
        }
    }

    /**
     * 获取其他检索条件
     */
    private String getSearchValueForMongo(CmsSearchInfoBean2 searchValue) {
        StringBuilder result = new StringBuilder();

        // 获取 feed category
        if (StringUtils.isNotEmpty(searchValue.getfCatId())) {
            result.append(MongoUtils.splicingValue("feed.catId", searchValue.getfCatId()));
            result.append(",");
        }

        // 获取 master category
        if (StringUtils.isNotEmpty(searchValue.getmCatId())) {
            result.append(MongoUtils.splicingValue("common.catId", searchValue.getmCatId()));
            result.append(",");
        }

        if (StringUtils.isNotEmpty(searchValue.getCreateTimeStart()) || StringUtils.isNotEmpty(searchValue.getCreateTimeTo())) {
            result.append("'created':{");
            // 获取createdTime start
            if (StringUtils.isNotEmpty(searchValue.getCreateTimeStart())) {
                result.append(MongoUtils.splicingValue("$gte", searchValue.getCreateTimeStart() + " 00.00.00"));
            }
            // 获取createdTime End
            if (StringUtils.isNotEmpty(searchValue.getCreateTimeTo())) {
                if (searchValue.getCreateTimeStart() != null) {
                    result.append(",");
                }
                result.append(MongoUtils.splicingValue("$lte", searchValue.getCreateTimeTo() + " 23.59.59"));
            }
            result.append("},");
        }

        // 获取inventory
        if (StringUtils.isNotEmpty(searchValue.getCompareType()) && searchValue.getInventory() != null) {
            result.append(MongoUtils.splicingValue("common.fields.quantity", searchValue.getInventory(), searchValue.getCompareType()));
            result.append(",");
        }

        // 获取brand
        if (StringUtils.isNotEmpty(searchValue.getBrand())) {
            result.append(MongoUtils.splicingValue("common.fields.brand", searchValue.getBrand()));
            result.append(",");
        }

        // 获取free tag查询条件
        if (searchValue.getFreeTags() != null && searchValue.getFreeTags().length > 0 && searchValue.getFreeTagType() > 0) {
            if (searchValue.getFreeTagType() == 1) {
                result.append(MongoUtils.splicingValue("freeTags", searchValue.getFreeTags()));
                result.append(",");
            } else if (searchValue.getFreeTagType() == 2) {
                // 不在指定范围
                result.append(MongoUtils.splicingValue("freeTags", searchValue.getFreeTags(), "$nin"));
                result.append(",");
            }
        }

        // 获取翻译状态
        if (StringUtils.isNotEmpty(searchValue.getTransStsFlg())) {
            if ("1".equals(searchValue.getTransStsFlg())) {
                result.append(MongoUtils.splicingValue("common.fields.translateStatus", searchValue.getTransStsFlg()));
                result.append(",");
            } else {
                result.append(MongoUtils.splicingValue("common.fields.translateStatus", new String[]{null, "", "0"}, "$in"));
                result.append(",");
            }
        }
        // 获取主类目完成状态
        if (StringUtils.isNotEmpty(searchValue.getmCatStatus())) {
            if ("1".equals(searchValue.getmCatStatus())) {
                result.append(MongoUtils.splicingValue("common.fields.categoryStatus", searchValue.getmCatStatus()));
                result.append(",");
            } else {
                result.append(MongoUtils.splicingValue("common.fields.categoryStatus", new String[]{null, "", "0"}, "$in"));
                result.append(",");
            }
        }
        // 获取税号设置完成状态
        if (StringUtils.isNotEmpty(searchValue.getTaxNoStatus())) {
            if ("1".equals(searchValue.getTaxNoStatus())) {
                result.append(MongoUtils.splicingValue("common.fields.hsCodeStatus", searchValue.getTaxNoStatus()));
                result.append(",");
            } else {
                result.append(MongoUtils.splicingValue("common.fields.hsCodeStatus", new String[]{null, "", "0"}, "$in"));
                result.append(",");
            }
        }

        // MINI MALL 店铺时查询原始CHANNEL
        if (StringUtils.isNotEmpty(searchValue.getOrgChaId()) && !ChannelConfigEnums.Channel.NONE.getId().equals(searchValue.getOrgChaId())) {
            result.append(MongoUtils.splicingValue("orgChannelId", searchValue.getOrgChaId()));
            result.append(",");
        }

        // 获取code list用于检索code,model,productName,longTitle

        if (searchValue.getCodeList() != null
                && searchValue.getCodeList().length > 0) {
            List<String> inputCodeList = Arrays.asList(searchValue.getCodeList());
            inputCodeList = inputCodeList.stream().map(inputCode -> StringUtils.trimToEmpty(inputCode)).filter(inputCode -> !inputCode.isEmpty()).collect(Collectors.toList());
            String[] inputCodeArr = inputCodeList.toArray(new String[inputCodeList.size()]);

            List<String> orSearch = new ArrayList<>();
            orSearch.add(MongoUtils.splicingValue("common.fields.code", inputCodeArr));
            orSearch.add(MongoUtils.splicingValue("common.fields.model", inputCodeArr));
            orSearch.add(MongoUtils.splicingValue("common.skus.skuCode", inputCodeArr));

            if (inputCodeArr.length == 1) {
                // 原文查询内容
                String fstCode = inputCodeArr[0];
                orSearch.add(MongoUtils.splicingValue("common.fields.productNameEn", fstCode, "$regex"));
                orSearch.add(MongoUtils.splicingValue("common.fields.longDesEn", fstCode, "$regex"));
                orSearch.add(MongoUtils.splicingValue("common.fields.shortDesEn", fstCode, "$regex"));
                // 中文查询内容
                orSearch.add(MongoUtils.splicingValue("common.fields.originalTitleCn", fstCode, "$regex"));
                orSearch.add(MongoUtils.splicingValue("common.fields.shortDesCn", fstCode, "$regex"));
                orSearch.add(MongoUtils.splicingValue("common.fields.longDesCn", fstCode, "$regex"));
            }
            result.append(MongoUtils.splicingValue("", orSearch.toArray(), "$or"));
            result.append(",");
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
                result.append("'$and':[");
                result.append(inputList.stream().collect(Collectors.joining(",")));
                result.append("],");
            }
        }

        return result.toString();
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
    public String getSortValue(CmsSearchInfoBean2 searchValue, CmsSessionBean cmsSessionBean) {
        StringBuilder result = new StringBuilder();

        // 获取排序字段1
        if (StringUtils.isNotEmpty(searchValue.getSortOneName()) && StringUtils.isNotEmpty(searchValue.getSortOneType())) {
            result.append(MongoUtils.splicingValue("common.fields." + searchValue.getSortOneName(), Integer.valueOf(searchValue.getSortOneType())));
            result.append(",");
        }

        // 获取排序字段2
        if (StringUtils.isNotEmpty(searchValue.getSortTwoName()) && StringUtils.isNotEmpty(searchValue.getSortTwoType())) {
            result.append(MongoUtils.splicingValue("common.fields." + searchValue.getSortTwoName(), Integer.valueOf(searchValue.getSortTwoType())));
            result.append(",");
        }

        // 获取排序字段3
        if (StringUtils.isNotEmpty(searchValue.getSortThreeName()) && StringUtils.isNotEmpty(searchValue.getSortThreeType())) {
            result.append(MongoUtils.splicingValue("common.fields." + searchValue.getSortThreeName(), Integer.valueOf(searchValue.getSortThreeType())));
            result.append(",");
        }

        // 添加platform cart
        int cartId = searchValue.getCartId();
        if (cartId > 1) {
            // 获取按销量排序字段
            if (StringUtils.isNotEmpty(searchValue.getSalesType()) && StringUtils.isNotEmpty(searchValue.getSalesSortType())) {
                result.append(MongoUtils.splicingValue("sales.code_sum_" + searchValue.getSalesType(), Integer.valueOf(searchValue.getSalesSortType())));
                result.append(",");
            }
        }

        return result.toString().length() > 0 ? "{" + result.toString().substring(0, result.toString().length() - 1) + "}" : null;
    }

    /**
     * 取得当前主商品所在组的其他信息：所有商品的价格变动信息，子商品图片
     */
    public List[] getGroupExtraInfo(List<CmsBtProductBean> groupsList, String channelId, int cartId, boolean hasImgFlg) {
        List[] rslt;
        List<List<Map<String, String>>> imgList = new ArrayList<>();
        List<Integer> chgFlgList = new ArrayList<>();
        List<String> orgChaNameList = new ArrayList<>();
        List<List<Map<String, Object>>> prodIdList = new ArrayList<>();
        List<String> freeTagsList = new ArrayList<>();

        if (hasImgFlg) {
            rslt = new List[3];
            rslt[0] = chgFlgList;
            rslt[1] = imgList;
            rslt[2] = prodIdList;
        } else {
            rslt = new List[3];
            rslt[0] = chgFlgList;
            rslt[1] = orgChaNameList;
            rslt[2] = freeTagsList;
        }
        if (groupsList == null || groupsList.isEmpty()) {
            $warn("CmsAdvSearchQueryService.getGroupExtraInfo groupsList为空");
            return rslt;
        }

        for (CmsBtProductBean groupObj : groupsList) {
            String prodCode = groupObj.getCommonNotNull().getFieldsNotNull().getCode();
            if (prodCode == null) {
                $warn("高级检索 getGroupExtraInfo 无产品code OBJ=:" + groupObj.toString());
                continue;
            }
            // 从group表合并platforms信息
            StringBuilder resultPlatforms = new StringBuilder();
            resultPlatforms.append(MongoUtils.splicingValue("cartId", cartId));
            resultPlatforms.append(",");
            resultPlatforms.append(MongoUtils.splicingValue("productCodes", new String[]{prodCode}, "$in"));

            // 在group表中过滤platforms相关信息
            JomgoQuery qrpQuy = new JomgoQuery();
            qrpQuy.setQuery("{" + resultPlatforms.toString() + "}");
            List<CmsBtProductGroupModel> grpList = productGroupService.getList(channelId, qrpQuy);
            CmsBtProductGroupModel groupModelMap = null;
            if (grpList == null || grpList.isEmpty()) {
                $warn("高级检索 getGroupExtraInfo group查询无结果 prodCode=" + prodCode);
                groupObj.setGroupBean(new CmsBtProductGroupModel());
            } else {
                groupModelMap = grpList.get(0);
                groupObj.setGroupBean(groupModelMap);
            }

            ChannelConfigEnums.Channel channel = ChannelConfigEnums.Channel.valueOfId(groupObj.getOrgChannelId());
            if (channel == null) {
                orgChaNameList.add("");
            } else {
                orgChaNameList.add(channel.getFullName());
            }

            boolean hasChg = false;
            List<CmsBtProductModel_Sku> skus = groupObj.getCommon().getSkus();
            if (skus != null) {
                for (CmsBtProductModel_Sku skuObj : skus) {
                    String chgFlg = StringUtils.trimToEmpty((String) (skuObj).get("priceChgFlg"));
                    if (chgFlg.startsWith("U") || chgFlg.startsWith("D") || chgFlg.startsWith("X")) {
                        hasChg = true;
                        break;
                    } else {
                        hasChg = false;
                    }
                }
            }
            if (hasChg) {
                chgFlgList.add(1);
            } else {
                chgFlgList.add(0);
            }

            if (!hasImgFlg) {
                // 获取商品free tag信息
                List<String> tagPathList = groupObj.getFreeTags();
                if (tagPathList != null && tagPathList.size() > 0) {
                    // 根据tag path查询tag path name
                    List<CmsBtTagBean> tagModelList = tagService.getTagPathNameByTagPath(channelId, tagPathList);
                    if (!tagModelList.isEmpty()) {
                        tagModelList = cmsChannelTagService.convertToTree(tagModelList);
                        List<CmsBtTagModel> tagList = cmsChannelTagService.convertToList(tagModelList);
                        List<String> tagPathStrList = new ArrayList<>();
                        tagList.forEach(tag -> tagPathStrList.add(tag.getTagPathName()));
                        freeTagsList.add(StringUtils.join(tagPathStrList, "<br>"));
                    }
                }

//                // 查询商品在各平台状态
//                List<CmsBtProductModel_Carts> carts = groupObj.getCarts();
//                if (carts != null && carts.size() > 0) {
//                    for (CmsBtProductModel_Carts cartsObj : carts) {
//                        StringBuilder resultStr = new StringBuilder();
//                        resultStr.append(MongoUtils.splicingValue("cartId", cartsObj.getCartId()));
//                        resultStr.append(",");
//                        resultStr.append(MongoUtils.splicingValue("productCodes", new String[]{prodCode}, "$in"));
//
//                        // 在group表中过滤platforms相关信息
//                        JomgoQuery qrpQuyObj = new JomgoQuery();
//                        qrpQuyObj.setQuery("{" + resultStr.toString() + "},{'_id':0,'numIId':1}");
//                        CmsBtProductGroupModel grpItem = productGroupService.getProductGroupByQuery(channelId, qrpQuyObj);
//                        if (grpItem != null) {
//                            cartsObj.setAttribute("numiid", grpItem.getNumIId());
//                        }
//                    }
//                }
            }

            List<Map<String, String>> images1Arr = new ArrayList<>();
            List<Map<String, Object>> groupProdIdList = new ArrayList<>();
            if (hasImgFlg && groupModelMap != null) {
                // 获取子商品的图片
                List pCdList = (List) groupModelMap.getProductCodes();
                if (pCdList != null && pCdList.size() > 1) {
                    for (int i = 1, leng = pCdList.size(); i < leng; i++) {
                        // 根据商品code找到其主图片
                        JomgoQuery queryObj = new JomgoQuery();
                        queryObj.setProjection("{'common.fields.images1':1,'prodId': 1, 'common.fields.code': 1,'_id':0}");
                        queryObj.setQuery("{'common.fields.code':'" + String.valueOf(pCdList.get(i)) + "'}");
                        CmsBtProductModel prod = productService.getProductByCondition(channelId, queryObj);
                        // 如果根据code获取不到数据就跳过
                        if (prod == null)
                            continue;
                        List<CmsBtProductModel_Field_Image> fldImgList = prod.getCommonNotNull().getFieldsNotNull().getImages1();
                        if (fldImgList.size() > 0) {
                            Map<String, String> map = new HashMap<>(1);
                            map.put("value", fldImgList.get(0).getName());
                            images1Arr.add(map);
                        }

                        // 设定该group对应的prodId
                        Map<String, Object> proMap = new HashMap<>();
                        proMap.put("prodId", prod.getProdId());
                        proMap.put("code", prod.getCommonNotNull().getFieldsNotNull().getCode());
                        groupProdIdList.add(proMap);
                    }
                }
            }
            imgList.add(images1Arr);
            prodIdList.add(groupProdIdList);
        }
        return rslt;
    }


    public List<Map<String, String>> getSalesTypeList(String channelId, String language, List<String> filterList) {
        List<Map<String, String>> salseSum7List = new ArrayList<>();
        List<Map<String, String>> salseSum30List = new ArrayList<>();
        List<Map<String, String>> salseSumAllList = new ArrayList<>();

        // 设置按销量排序的选择列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
        if (cartList == null) {
            return salseSumAllList;
        }
        for (TypeChannelBean cartObj : cartList) {
            int cartId = NumberUtils.toInt(cartObj.getValue(), -1);
            if (cartId == 1 || cartId == -1) {
                continue;
            }
            Map<String, String> keySum7Map = new HashMap<>();
            Map<String, String> keySum30Map = new HashMap<>();
            Map<String, String> keySumAllMap = new HashMap<>();
            if (cartId == 0) {
                keySum7Map.put("name", "7Days总销量");
                keySum7Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_7 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySum30Map.put("name", "30Days总销量");
                keySum30Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_30 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySumAllMap.put("name", "总销量");
                keySumAllMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_ALL + "." + CmsBtProductModel_Sales.CARTID + cartId);
            } else {
                keySum7Map.put("name", cartObj.getName() + "7Days销量");
                keySum7Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_7 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySum30Map.put("name", cartObj.getName() + "30Days销量");
                keySum30Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_30 + "." + CmsBtProductModel_Sales.CARTID + cartId);
                keySumAllMap.put("name", cartObj.getName() + "总销量");
                keySumAllMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_ALL + "." + CmsBtProductModel_Sales.CARTID + cartId);
            }
            salseSum7List.add(keySum7Map);
            salseSum30List.add(keySum30Map);
            salseSumAllList.add(keySumAllMap);
        }
        salseSumAllList.addAll(salseSum30List);
        salseSumAllList.addAll(salseSum7List);

        if (filterList != null) {
            List<Map<String, String>> sumAllList = new ArrayList<>();
            for (Map<String, String> sumObj : salseSumAllList) {
                if (filterList.contains(sumObj.get("value"))) {
                    sumAllList.add(sumObj);
                }
            }
            return sumAllList;
        }
        return salseSumAllList;
    }
}
