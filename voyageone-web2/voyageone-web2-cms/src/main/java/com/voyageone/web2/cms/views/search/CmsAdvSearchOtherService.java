package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.views.channel.CmsChannelTagService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsAdvSearchOtherService extends BaseViewService {

    private static final String[] biSortItems = {"1", "7", "30"};
    private static final String[][] biSortItemKeys = {
            {"bi.sum%s.pv.cartId", "%s浏览量"},
            {"bi.sum%s.uv.cartId", "%s访客数"},
            {"bi.sum%s.gwc.cartId", "%s加购件数"},
            {"bi.sum%s.scs.cartId", "%s收藏人数"}
    };
    private static final String[][] commonSortItems = {
            {"platforms.P%s.pPublishTime", "商品发布时间"},
            {"platforms.P%s.pPriceMsrpEd", "中国建议售价"},
            {"platforms.P%s.pPriceRetailSt", "中国指导售价"},
            {"platforms.P%s.pPriceSaleEd", "中国最终售价"}
    };
    private static final String[][] platformItems = {
            {"platforms.P%s.URL", "URL"},
            {"platforms.P%s.pNumIId", "Numiid"},
            {"platforms.P%s.fields.title", "商品名称"},
            {"platforms.P%s.fields.fields.pCatPath", "类目"},
            {"platforms.P%s.fields.pPriceMsrpEd", "官方建议售价"},
            {"platforms.P%s.fields.pPriceRetailEd", "指导售价"},
            {"platforms.P%s.fields.pPriceSaleEd", "最终售价"},
            {"platforms.P%s.mainProductCode", "主商品编码"},
            {"platforms.P%s.qty", "可售库存"},
            {"platforms.P%s.lock", "是否锁定"},
            {"platforms.P%s.skus.isSale", "是否销售"},
            {"platforms.P%s.pPublishTime", "上新时间"},
    };
    private static final String[][] platformItemsJd = {
            {"platforms.P%s.jdSkuId", "jdSkuId"},
    };
    private static final String[][] platformItemsJM = {
            {"platforms.P%s.MallURL", "MallURL"},
            {"platforms.P%s.pMallId", "MallId"},
            {"platforms.P%s.URL", "URL"},
            {"platforms.P%s.pNumIId", "HashID"},
            {"platforms.P%s.fields.title", "商品名称"},
            {"platforms.P%s.fields.fields.pCatPath", "类目"},
            {"platforms.P%s.fields.pPriceMsrpEd", "官方建议售价"},
            {"platforms.P%s.fields.pPriceRetailEd", "指导售价"},
            {"platforms.P%s.fields.pPriceSaleEd", "最终售价"},
            {"platforms.P%s.mainProductCode", "主商品编码"},
            {"platforms.P%s.qty", "可售库存"},
            {"platforms.P%s.lock", "是否锁定"},
            {"platforms.P%s.skus.isSale", "是否销售"},
            {"platforms.P%s.pPublishTime", "上新时间"},
            {"platforms.P%s.skus.jmSkuNo", "SkuNo"},
            {"platforms.P%s.skus.jmSpuNo", "SpuNo"},
    };
    private static final String[][] platformItemsTM = {
            {"platforms.P%s.skus.scCode", "sku货品编码"},
    };
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsChannelTagService cmsChannelTagService;
    @Autowired
    private TagService tagService;
    @Autowired
    CmsBtProductDao cmsBtProductDao;
    @Autowired
    PriceService priceService;


    /**
     * 取得当前主商品所在组的其他信息：所有商品的价格变动信息，子商品图片
     */
    public List[] getGroupExtraInfo(List<CmsBtProductBean> groupsList, String channelId, int cartId, boolean hasImgFlg) {
        List[] rslt;
        List<List<Map<String, String>>> imgList = new ArrayList<>();
        List<String> orgChaNameList = new ArrayList<>();
        List<List<Map<String, Object>>> prodIdList = new ArrayList<>();
        List<String> freeTagsList = new ArrayList<>();
        List<List<CmsBtProductGroupModel>> grpPriceList = new ArrayList<>();

        if (hasImgFlg) {
            rslt = new List[3];
            rslt[0] = imgList;
            rslt[1] = prodIdList;
            rslt[2] = grpPriceList;
        } else {
            rslt = new List[2];
            rslt[0] = orgChaNameList;
            rslt[1] = freeTagsList;
        }
        if (groupsList == null || groupsList.isEmpty()) {
            $warn("CmsAdvSearchQueryService.getGroupExtraInfo groupsList为空");
            return rslt;
        }

        Map<String, CmsBtTagBean> cachTag = new HashMap<>();
        for (CmsBtProductBean groupObj : groupsList) {
            String prodCode = groupObj.getCommonNotNull().getFieldsNotNull().getCode();
            if (prodCode == null) {
                $warn("高级检索 getGroupExtraInfo 无产品code ObjId=:" + groupObj.get_id());
                continue;
            }
            // 从group表合并platforms信息
            StringBuilder resultPlatforms = new StringBuilder();
            resultPlatforms.append(MongoUtils.splicingValue("cartId", cartId));
            resultPlatforms.append(",");
            resultPlatforms.append(MongoUtils.splicingValue("productCodes", new String[]{prodCode}, "$in"));

            // 在group表中过滤platforms相关信息
            JongoQuery qrpQuy = new JongoQuery();
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

//            ChannelConfigEnums.Channel channel = ChannelConfigEnums.Channel.valueOfId(groupObj.getOrgChannelId());
//            if (channel == null) {
//                orgChaNameList.add("");
//            } else {
                orgChaNameList.add(Channels.getChannel(groupObj.getOrgChannelId()).getFull_name());
//            }

            if (!hasImgFlg) {
                // 获取商品free tag信息
                List<String> tagPathList = groupObj.getFreeTags();
                // 追加美国CMS free tag
                if (tagPathList == null) {
                    tagPathList = Collections.emptyList();
                }
                if (CollectionUtils.isNotEmpty(groupObj.getUsFreeTags())) {
                    tagPathList.addAll(groupObj.getUsFreeTags());
                }
                if (tagPathList != null && tagPathList.size() > 0) {
                    List<CmsBtTagBean> tagModelList = new ArrayList<>();
                    List<String> temp = new ArrayList<>();
                    for (String tag : tagPathList) {
                        if (cachTag.containsKey(tag)) {
                            tagModelList.add(cachTag.get(tag));
                        } else {
                            temp.add(tag);
                        }
                    }
                    if (temp.size() > 0) {
                        List<CmsBtTagBean> ts = tagService.getTagPathNameByTagPath(channelId, temp);
                        if (!ListUtils.isNull(ts)) {
                            for (CmsBtTagBean cmsBtTagBean : ts) {
                                cachTag.put(cmsBtTagBean.getTagPath(), cmsBtTagBean);
                                tagModelList.add(cmsBtTagBean);
                            }
                        }
                    }
                    // 根据tag path查询tag path name
//                    List<CmsBtTagBean> tagModelList = tagService.getTagPathNameByTagPath(channelId, tagPathList);
                    if (!tagModelList.isEmpty()) {
                        tagModelList = cmsChannelTagService.convertToTree(tagModelList);
                        List<CmsBtTagModel> tagList = cmsChannelTagService.convertToList(tagModelList);
                        List<String> tagPathStrList = new ArrayList<>();
                        tagList.forEach(tag -> tagPathStrList.add(tag.getTagPathName()));
                        freeTagsList.add(StringUtils.join(tagPathStrList, "<br>"));
                    } else {
                        freeTagsList.add("");
                    }
                } else {
                    freeTagsList.add("");
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
//                        JongoQuery qrpQuyObj = new JongoQuery();
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
                        JongoQuery queryObj = new JongoQuery();
                        queryObj.setProjection("{'common.fields.images1':1,'common.fields.images6':1,'prodId': 1, 'common.fields.code': 1,'_id':0}");
                        queryObj.setQuery("{'common.fields.code':'" + String.valueOf(pCdList.get(i)) + "'}");
                        CmsBtProductModel prod = productService.getProductByCondition(channelId, queryObj);
                        // 如果根据code获取不到数据就跳过
                        if (prod == null)
                            continue;
                        List<CmsBtProductModel_Field_Image> fldImgList = prod.getCommonNotNull().getFieldsNotNull().getImages6();
                        if (fldImgList.size() > 0) {
                            Map<String, String> map = new HashMap<>(1);
                            map.put("value", fldImgList.get(0).getName());
                            images1Arr.add(map);
                        } else {
                            fldImgList = prod.getCommonNotNull().getFieldsNotNull().getImages1();
                            if (fldImgList.size() > 0) {
                                Map<String, String> map = new HashMap<>(1);
                                map.put("value", fldImgList.get(0).getName());
                                images1Arr.add(map);
                            }
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
                qrpQuy = new JongoQuery();
                qrpQuy.setQuery("{'mainProductCode':#,'cartId':{$nin:[null,'',0,1]}}");
                qrpQuy.setParameters(prodCode);
                grpList = productGroupService.getList(channelId, qrpQuy);
                grpPriceList.add(grpList);
            }
        }
        return rslt;
    }

    public List[] getProductExtraInfo(List<CmsBtProductBean> groupsList, String channelId, int cartId) {
        List[] rslt;
        List<String> orgChaNameList = new ArrayList<>();
        List<String> freeTagsList = new ArrayList<>();

        rslt = new List[2];
        rslt[0] = orgChaNameList;
        rslt[1] = freeTagsList;
        if (groupsList == null || groupsList.isEmpty()) {
            $warn("CmsAdvSearchQueryService.getGroupExtraInfo groupsList为空");
            return rslt;
        }

        Map<String, CmsBtTagBean> cachTag = new HashMap<>();
        for (CmsBtProductBean groupObj : groupsList) {
            String prodCode = groupObj.getCommonNotNull().getFieldsNotNull().getCode();
            if (prodCode == null) {
                $warn("高级检索 getGroupExtraInfo 无产品code ObjId=:" + groupObj.get_id());
                continue;
            }

            ChannelConfigEnums.Channel channel = ChannelConfigEnums.Channel.valueOfId(groupObj.getOrgChannelId());
            if (channel == null) {
                orgChaNameList.add("");
            } else {
                orgChaNameList.add(channel.getFullName());
            }

            // 获取商品free tag信息
            List<String> tagPathList = groupObj.getFreeTags();
            // 追加美国CMS free tag
            if (tagPathList == null) {
                tagPathList = Collections.emptyList();
            }
            if (CollectionUtils.isNotEmpty(groupObj.getUsFreeTags())) {
                tagPathList.addAll(groupObj.getUsFreeTags());
            }
            if (tagPathList != null && tagPathList.size() > 0) {
                List<CmsBtTagBean> tagModelList = new ArrayList<>();
                List<String> temp = new ArrayList<>();
                for (String tag : tagPathList) {
                    if (cachTag.containsKey(tag)) {
                        tagModelList.add(cachTag.get(tag));
                    } else {
                        temp.add(tag);
                    }
                }
                if (temp.size() > 0) {
                    List<CmsBtTagBean> ts = tagService.getTagPathNameByTagPath(channelId, temp);
                    if (!ListUtils.isNull(ts)) {
                        for (CmsBtTagBean cmsBtTagBean : ts) {
                            cachTag.put(cmsBtTagBean.getTagPath(), cmsBtTagBean);
                            tagModelList.add(cmsBtTagBean);
                        }
                    }
                }
                // 根据tag path查询tag path name
//                    List<CmsBtTagBean> tagModelList = tagService.getTagPathNameByTagPath(channelId, tagPathList);
                if (!tagModelList.isEmpty()) {
                    tagModelList = cmsChannelTagService.convertToTree(tagModelList);
                    List<CmsBtTagModel> tagList = cmsChannelTagService.convertToList(tagModelList);
                    List<String> tagPathStrList = new ArrayList<>();
                    tagList.forEach(tag -> tagPathStrList.add(tag.getTagPathName()));
                    freeTagsList.add(StringUtils.join(tagPathStrList, "<br>"));
                } else {
                    freeTagsList.add("");
                }
            } else {
                freeTagsList.add("");
            }
        }
        return rslt;
    }

    /**
     * 取得销量数据显示列
     */
    public List<Map<String, String>> getSalesTypeList(String channelId, String language, List<String> filterList) {
        // 设置按销量排序的选择列表
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
        if (cartList == null) {
            return Collections.emptyList();
        }

        List<Map<String, String>> salseSumList = new ArrayList<>(0);

        for (TypeChannelBean cartObj : cartList) {
            int cartId = NumberUtils.toInt(cartObj.getValue(), -1);
            if ((cartId == 1 && CartType.FEED.getShortName().equals(cartObj.getAdd_name2())) || cartId == -1) {
                continue;
            }
            String cartName = cartObj.getName();
            if (cartId == 0) {
                cartName = "";
            }

            Map<String, String> keySum7Map = new HashMap<>(2);
            keySum7Map.put("name", cartName + "7天销量");
            keySum7Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_7 + "." + CmsBtProductModel_Sales.CARTID + cartId);
            salseSumList.add(keySum7Map);

            Map<String, String> keySum30Map = new HashMap<>(2);
            keySum30Map.put("name", cartName + "30天销量");
            keySum30Map.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_30 + "." + CmsBtProductModel_Sales.CARTID + cartId);
            salseSumList.add(keySum30Map);

            Map<String, String> keySumYearMap = new HashMap<>(2);
            keySumYearMap.put("name", cartName + "YTD销量");
            keySumYearMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_YEAR + "." + CmsBtProductModel_Sales.CARTID + cartId);
            salseSumList.add(keySumYearMap);

            Map<String, String> keySumAllMap = new HashMap<>(2);
            keySumAllMap.put("name", cartName + "总销量");
            keySumAllMap.put("value", "sales." + CmsBtProductModel_Sales.CODE_SUM_ALL + "." + CmsBtProductModel_Sales.CARTID + cartId);
            salseSumList.add(keySumAllMap);
        }

        if (filterList != null) {
            List<Map<String, String>> sumAllList = new ArrayList<>();
            for (Map<String, String> sumObj : salseSumList) {
                if (filterList.contains(sumObj.get("value"))) {
                    sumAllList.add(sumObj);
                }
            }
            return sumAllList;
        }
        return salseSumList;
    }

    /**
     * 取得销量数据显示列
     */
    public List<Map<String, String>> getPlatformList(String channelId, String language, List<String> filterList) {
        List<Map<String, String>> dataSumList = new ArrayList<>();

        // 设置显示列排序
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
        if (cartList == null) {
            return dataSumList;
        }
        for (TypeChannelBean cartObj : cartList) {
            int cartId = NumberUtils.toInt(cartObj.getValue(), -1);
            if (cartId == 0 || cartId == 1 || cartId == -1) {
                continue;
            }

            // 目前只有淘宝和京东有bi数据，其他平台都忽略
            // 目前暂不计算平台相加总数
            ShopBean shopProp = Shops.getShop(channelId, cartId);
            if (shopProp == null) {
                $error("CmsAdvSearchOtherService 获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                continue;
            }

            if (PlatFormEnums.PlatForm.JM.getId().equals(shopProp.getPlatform_id())) {
                // 添加各平台的排序字段
                for (String[] platformItem : platformItemsJM) {
                    Map<String, String> keySumMap = new HashMap<>();
                    keySumMap.put("name", cartObj.getName() + platformItem[1]);
                    keySumMap.put("value", String.format(platformItem[0], cartId));
                    dataSumList.add(keySumMap);
                }
            } else {
                // 添加各平台的排序字段
                for (String[] platformItem : platformItems) {
                    Map<String, String> keySumMap = new HashMap<>();
                    keySumMap.put("name", cartObj.getName() + platformItem[1]);
                    keySumMap.put("value", String.format(platformItem[0], cartId));
                    dataSumList.add(keySumMap);
                }
                if (PlatFormEnums.PlatForm.JD.getId().equals(shopProp.getPlatform_id())) {
                    Map<String, String> keySumMap = new HashMap<>();
                    keySumMap.put("name", cartObj.getName() + platformItemsJd[0][1]);
                    keySumMap.put("value", String.format(platformItemsJd[0][0], cartId));
                    dataSumList.add(keySumMap);
                }
                if (PlatFormEnums.PlatForm.TM.getId().equals(shopProp.getPlatform_id())) {
                    for (String[] platformItem : platformItemsTM) {
                        Map<String, String> keySumMap = new HashMap<>();
                        keySumMap.put("name", cartObj.getName() + platformItem[1]);
                        keySumMap.put("value", String.format(platformItem[0], cartId));
                        dataSumList.add(keySumMap);
                    }
                }
            }
        }

        if (filterList != null) {
            return filterSelList(dataSumList, filterList);
        }

        return dataSumList;
    }

    private List<Map<String, String>> filterSelList(List<Map<String, String>> orgList, List<String> filterList) {
        List<Map<String, String>> resultList = new ArrayList<>();
        for (Map<String, String> sumObj : orgList) {
            if (filterList.contains(sumObj.get("value"))) {
                resultList.add(sumObj);
            }
        }

        return resultList;
    }

    /**
     * 取得BI数据显示列
     */
    public List<Map<String, String>> getBiDataList(String channelId, String language, List<String> filterList) {
        List<Map<String, String>> dataSumList = new ArrayList<>();

        // 设置显示列排序
        List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
        if (cartList == null) {
            return dataSumList;
        }
        for (TypeChannelBean cartObj : cartList) {
            int cartId = NumberUtils.toInt(cartObj.getValue(), -1);
            if (cartId == 0 || cartId == 1 || cartId == -1) {
                continue;
            }

            // 目前只有淘宝和京东有bi数据，其他平台都忽略
            // 目前暂不计算平台相加总数
            ShopBean shopProp = Shops.getShop(channelId, cartId);
            if (shopProp == null) {
                $error("CmsAdvSearchOtherService 获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                continue;
            }
            if (!PlatFormEnums.PlatForm.TM.getId().equals(shopProp.getPlatform_id()) && !PlatFormEnums.PlatForm.JD.getId().equals(shopProp.getPlatform_id())) {
                $info("CmsAdvSearchOtherService 目前只有淘宝和京东有bi数据，其他平台都忽略 [ChannelId:%s] [CartId:%s]", channelId, cartId);
                continue;
            }

            // 添加各平台的排序字段
            for (String biSortItem : biSortItems) {
                for (String[] biSortItemKey : biSortItemKeys) {
                    Map<String, String> keySumMap = new HashMap<>();
                    keySumMap.put("name", cartObj.getName() + String.format(biSortItemKey[1], biSortItem));
                    keySumMap.put("value", String.format(biSortItemKey[0], biSortItem) + cartId);
                    dataSumList.add(keySumMap);
                }
            }

            // 添加通用的排序字段
            for (String[] commonSortItem : commonSortItems) {
                Map<String, String> keySumMap = new HashMap<>();
                keySumMap.put("name", cartObj.getName() + commonSortItem[1]);
                keySumMap.put("value", String.format(commonSortItem[0], cartId));
                dataSumList.add(keySumMap);
            }
        }

        if (filterList != null) {
            return filterSelList(dataSumList, filterList);
        }

        return dataSumList;
    }
}
