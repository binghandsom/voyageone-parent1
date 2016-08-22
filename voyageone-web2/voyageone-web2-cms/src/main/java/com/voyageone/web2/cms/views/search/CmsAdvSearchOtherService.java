package com.voyageone.web2.cms.views.search;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.bean.cms.product.CmsBtProductBean;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.cms.mongo.product.*;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.views.channel.CmsChannelTagService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 15/12/14
 */
@Service
public class CmsAdvSearchOtherService extends BaseAppService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsChannelTagService cmsChannelTagService;
    @Autowired
    private TagService tagService;

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
                qrpQuy = new JongoQuery();
                qrpQuy.setQuery("{'mainProductCode':#,'cartId':{$nin:[null,'',0,1]}}");
                qrpQuy.setParameters(prodCode);
                grpList = productGroupService.getList(channelId, qrpQuy);
                grpPriceList.add(grpList);
            }
        }
        return rslt;
    }

    /**
     * 取得销量数据显示列
     */
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
            Map<String, String> keySumMap = new HashMap<>();

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
            keySumMap.put("name", cartObj.getName() + "1天浏览量");
            keySumMap.put("value", "bi.sum1.pv.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "1天访客数");
            keySumMap.put("value", "bi.sum1.uv.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "1天加购件数");
            keySumMap.put("value", "bi.sum1.gwc.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "1天收藏人数");
            keySumMap.put("value", "bi.sum1.scs.cartId" + cartId);
            dataSumList.add(keySumMap);

            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "7天浏览量");
            keySumMap.put("value", "bi.sum7.pv.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "7天访客数");
            keySumMap.put("value", "bi.sum7.uv.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "7天加购件数");
            keySumMap.put("value", "bi.sum7.gwc.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "7天收藏人数");
            keySumMap.put("value", "bi.sum7.scs.cartId" + cartId);
            dataSumList.add(keySumMap);

            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "30天浏览量");
            keySumMap.put("value", "bi.sum30.pv.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "30天访客数");
            keySumMap.put("value", "bi.sum30.uv.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "30天加购件数");
            keySumMap.put("value", "bi.sum30.gwc.cartId" + cartId);
            dataSumList.add(keySumMap);
            keySumMap = new HashMap<>();
            keySumMap.put("name", cartObj.getName() + "30天收藏人数");
            keySumMap.put("value", "bi.sum30.scs.cartId" + cartId);
            dataSumList.add(keySumMap);
        }

        if (filterList != null) {
            List<Map<String, String>> sumAllList = new ArrayList<>();
            for (Map<String, String> sumObj : dataSumList) {
                if (filterList.contains(sumObj.get("value"))) {
                    sumAllList.add(sumObj);
                }
            }
            return sumAllList;
        }
        return dataSumList;
    }
}
