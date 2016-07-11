package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
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

    /**
     * 返回页面端的检索条件拼装成mongo使用的条件
     */
    public JomgoQuery getSearchQuery(CmsSearchInfoBean2 searchValue, CmsSessionBean cmsSessionBean, boolean isMain) {
        JomgoQuery queryObject = new JomgoQuery();

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

            // 获取price start
            if (StringUtils.isNotEmpty(searchValue.getPriceType()) && searchValue.getPriceStart() != null) {
                queryObject.addQuery("{'platforms.P#.skus.#':{$gte:#}}");
                queryObject.addParameters(cartId, searchValue.getPriceType(), searchValue.getPriceStart());
            }
            // 获取price end
            if (StringUtils.isNotEmpty(searchValue.getPriceType()) && searchValue.getPriceEnd() != null) {
                queryObject.addQuery("{'platforms.P#.skus.#':{$lte:#}}");
                queryObject.addParameters(cartId, searchValue.getPriceType(), searchValue.getPriceEnd());
            }

            // 获取platform category
            if (StringUtils.isNotEmpty(searchValue.getpCatId())) {
                queryObject.addQuery("{'platforms.P#.pCatId':#}");
                queryObject.addParameters(cartId, searchValue.getpCatId());
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
            if (searchValue.getHasErrorFlg() == 1) {
                queryObject.addQuery("{'platforms.P#.pPublishError':'Error'}");
                queryObject.addParameters(cartId);
            }

            // 查询价格变动
            if (StringUtils.isNotEmpty(searchValue.getPriceChgFlg())) {
                queryObject.addQuery("{'platforms.P#.skus.priceChgFlg':{'$regex':'^"+ searchValue.getPriceChgFlg()+"'}}");
                queryObject.addParameters(cartId);
            }

            // 查询价格比较（建议销售价和实际销售价）
            if (StringUtils.isNotEmpty(searchValue.getPriceDiffFlg())) {
                // 建议销售价等于实际销售价
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
                        queryObject.addQuery("{'sales.code_sum_#.cartId_#':{$gte:#,$lte:#}}");
                        queryObject.addParameters(searchValue.getSalesType(), cartId, searchValue.getSalesStart(), searchValue.getSalesEnd());
                    } else {
                        queryObject.addQuery("{'sales.code_sum_#.cartId_#':{$gte:#}}");
                        queryObject.addParameters(searchValue.getSalesType(), cartId, searchValue.getSalesStart());
                    }
                } else {
                    if (searchValue.getSalesEnd() != null) {
                        queryObject.addQuery("{'sales.code_sum_#.cartId_#':{$lte:#}}");
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
    private void getSearchValueForMongo(CmsSearchInfoBean2 searchValue, JomgoQuery queryObject) {
        // 获取 feed category
        if (StringUtils.isNotEmpty(searchValue.getfCatId())) {
            queryObject.addQuery("{'feed.catId':#}");
            queryObject.addParameters(searchValue.getfCatId());
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
        if (StringUtils.isNotEmpty(searchValue.getBrand())) {
            queryObject.addQuery("{'common.fields.brand':#}");
            queryObject.addParameters(searchValue.getBrand());
        }

        // 获取free tag查询条件
        if (searchValue.getFreeTags() != null && searchValue.getFreeTags().size() > 0 && searchValue.getFreeTagType() > 0) {
            Object para = searchValue.getFreeTags();
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
            if ("1".equals(searchValue.getTransStsFlg())) {
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
        List<List<CmsBtProductGroupModel>> grpPriceList = new ArrayList<>();

        if (hasImgFlg) {
            rslt = new List[4];
            rslt[0] = chgFlgList;
            rslt[1] = imgList;
            rslt[2] = prodIdList;
            rslt[3] = grpPriceList;
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

            // 查看价格变化
            if (cartId > 0) {
                boolean hasChg = false;
                CmsBtProductModel_Platform_Cart platformObj = groupObj.getPlatform(cartId);
                if (platformObj != null) {
                    List<BaseMongoMap<String, Object>> skus = platformObj.getSkus();
                    if (skus != null && skus.size() > 0) {
                        for (BaseMongoMap skuObj : skus) {
                            String chgFlg = StringUtils.trimToEmpty((String) (skuObj).get("priceChgFlg"));
                            if (chgFlg.startsWith("U") || chgFlg.startsWith("D") || chgFlg.startsWith("X")) {
                                hasChg = true;
                                break;
                            } else {
                                hasChg = false;
                            }
                        }
                    }
                }

                if (hasChg) {
                    chgFlgList.add(1);
                } else {
                    chgFlgList.add(0);
                }
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

            // 获取Group的价格区间
            if (hasImgFlg) {
                qrpQuy = new JomgoQuery();
                qrpQuy.setQuery("{'mainProductCode':#,'cartId':{$nin:[null,'',0,1]}}");
                qrpQuy.setParameters(prodCode);
                grpList = productGroupService.getList(channelId, qrpQuy);
                grpPriceList.add(grpList);
            }
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
