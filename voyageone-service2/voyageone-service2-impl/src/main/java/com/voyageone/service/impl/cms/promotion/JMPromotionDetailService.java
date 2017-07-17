package com.voyageone.service.impl.cms.promotion;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.common.util.ConvertUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.InitParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.TagTreeNode;
import com.voyageone.service.bean.cms.businessmodel.CmsBtTag.TagCodeCountInfo;
import com.voyageone.service.bean.cms.jumei.BatchUpdateSkuPriceParameterBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionSkuDao;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.TagService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionSku3Service;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionTagProductService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.service.model.util.MapModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/11/7.
 */
@Service
public class JMPromotionDetailService extends BaseService {

    @Autowired
    CmsBtJmPromotionDaoExt cmsBtJmPromotionDaoExt;
    @Autowired
    CmsBtJmPromotionProductDao dao;

    @Autowired
    CmsBtJmPromotionProductDaoExt daoext;
    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;

    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;

    @Autowired
    CmsBtJmPromotionSku3Service cmsBtJmPromotionSku3Service;

    @Autowired
    CmsBtJmPromotionTagProductService cmsBtJmPromotionTagProductService;
    @Autowired
    PromotionService promotionService;
    @Autowired
    CmsBtPromotionCodesDaoExtCamel cmsBtPromotionCodesDaoExtCamel;
    @Autowired
    CmsBtPromotionSkusDaoExtCamel cmsBtPromotionSkusDaoExtCamel;
    @Autowired
    TagService tagService;
    @Autowired
    CmsBtJmPromotionTagProductDaoExt cmsBtJmPromotionTagProductDaoExt;
    @Autowired
    private ProductService productService;

    public Map init(InitParameter params, String channelId,List<String> codeList) {
        Map<String, Object> data = new HashedMap();
        int cartId = params.getCartId();
        List<TagTreeNode> listTagTreeNode = new ArrayList<>();

        List<MapModel> list = cmsBtJmPromotionDaoExt.selectAddPromotionList(channelId, cartId, codeList, params.getActivityStart(), params.getActivityEnd());
        list.forEach(m -> listTagTreeNode.add(getPromotionTagTreeNode(m, codeList)));
        data.put("listTreeNode", listTagTreeNode);
        return data;
    }
    //获取活动的节点数据
    TagTreeNode getPromotionTagTreeNode(MapModel map, List<String> codeList) {
        int codeCount = codeList.size();

        TagTreeNode tagTreeNode = new TagTreeNode();
        int id = ConvertUtil.toInt(map.get("id"));
        int productCount = ConvertUtil.toInt(map.get("productCount"));
        tagTreeNode.setId(id);
        tagTreeNode.setName(ConvertUtil.toString(map.get("name")));
        if (productCount > 0) {
            tagTreeNode.setChecked(productCount == codeCount ? 2 : 1);
        }
        tagTreeNode.setChildren(new ArrayList<>());
        List<TagCodeCountInfo> list = cmsBtJmPromotionTagProductDaoExt.selectListTagCodeCount(id, ConvertUtil.toInt(map.get("refTagId")), codeList);
        if (list.size() == 0) return tagTreeNode;

        list.forEach(f -> {
            TagTreeNode node = new TagTreeNode();
            node.setId(f.getId());
            node.setName(f.getTagName());
            if (f.getProductCount() > 0) {
                node.setChecked(f.getProductCount() == codeCount ? 2 : 1);//0:未选 1：半选 2全选
            }
            node.setOldChecked(node.getChecked());
            tagTreeNode.getChildren().add(node);
        });
        tagTreeNode.setOldChecked(tagTreeNode.getChecked());
        return tagTreeNode;
    }

    @VOTransactional
    public CmsBtJmPromotionProductModel addPromotionDetail(PromotionDetailAddBean bean, CmsBtJmPromotionModel jmPromotionModel, String modifier) {

        CmsBtProductModel productInfo=bean.getProductInfo();//check 初始化
        bean.setProductCode(productInfo.getCommon().getFields().getCode());
        //1.初始化 JmPromotionProduct
        CmsBtJmPromotionProductModel jmProductModel = loadJmPromotionProduct(bean, jmPromotionModel, modifier, productInfo);

        //2.初始化 JmPromotionSku
        List<CmsBtJmPromotionSkuModel> listPromotionSku = loadJmPromotionSkus(bean, jmProductModel, productInfo, modifier);

        //3.计算活动价格
        BatchUpdateSkuPriceParameterBean parameter = new BatchUpdateSkuPriceParameterBean();
        parameter.setJmPromotionId(jmPromotionModel.getId());
        parameter.setOptType(bean.getAddProductSaveParameter().getOptType());
        parameter.setPriceTypeId(bean.getAddProductSaveParameter().getPriceTypeId());
        parameter.setPriceValue(bean.getAddProductSaveParameter().getPriceValue());
        parameter.setRoundType(bean.getAddProductSaveParameter().getRoundType());
        parameter.setSkuUpdType(bean.getAddProductSaveParameter().getSkuUpdType());
        cmsBtJmPromotionSku3Service.UpdateSkuDealPrice(parameter, listPromotionSku, modifier);

        //设置 JmPromotionProduct价格
        CmsBtProductModel_Platform_Cart p_Platform_Cart= productInfo.getPlatform(CartEnums.Cart.JM);
        jmProductModel.setMaxMsrpUsd(new BigDecimal(productInfo.getCommon().getFields().getPriceMsrpEd()));
        jmProductModel.setMinMsrpUsd(new BigDecimal(productInfo.getCommon().getFields().getPriceMsrpSt()));
        if(p_Platform_Cart!=null) {
            jmProductModel.setMaxMsrpRmb(new BigDecimal(p_Platform_Cart.getpPriceMsrpEd()));
            jmProductModel.setMinMsrpRmb(new BigDecimal(p_Platform_Cart.getpPriceMsrpSt()));
            jmProductModel.setMaxRetailPrice(new BigDecimal(p_Platform_Cart.getpPriceRetailEd()));
            jmProductModel.setMinRetailPrice(new BigDecimal(p_Platform_Cart.getpPriceRetailSt()));
            jmProductModel.setMaxSalePrice(new BigDecimal(p_Platform_Cart.getpPriceSaleEd()));
            jmProductModel.setMinSalePrice(new BigDecimal(p_Platform_Cart.getpPriceSaleSt()));
        }
        if (listPromotionSku.size() > 0) {
            jmProductModel.setMaxMarketPrice(getMaxMarketPrice(listPromotionSku));
            jmProductModel.setMinMarketPrice(getMinMarketPrice(listPromotionSku));
            jmProductModel.setMaxDealPrice(getMaxDealPrice(listPromotionSku));
            jmProductModel.setMinDealPrice(getMinDealPrice(listPromotionSku));
            jmProductModel.setDiscount(listPromotionSku.get(0).getDiscount());//折扣
            jmProductModel.setDiscount2(listPromotionSku.get(0).getDiscount2());//折扣
            jmProductModel.setSkuCount(listPromotionSku.size());

            // 统计code级别的库存
            List<String> skuList = productInfo.getPlatform(27).getSkus()
                    .stream()
                    .filter(sku -> Boolean.valueOf(sku.getStringAttribute("isSale")))
                    .map(sku -> sku.getStringAttribute("skuCode")).collect(Collectors.toList());
            Integer qty = 0;
            for (CmsBtProductModel_Sku sku : productInfo.getCommon().getSkus()) {
                if(skuList.contains(sku.getSkuCode()))
                    qty += sku.getQty();
            }
            jmProductModel.setQuantity(qty);
        }

        // 保存 JmPromotionProduct
        if (jmProductModel.getId() != null && jmProductModel.getId() > 0) {
            dao.update(jmProductModel);
        } else {
            dao.insert(jmProductModel);
        }

        // 保存 JmPromotionSku
        listPromotionSku.forEach(f -> {
            f.setCmsBtJmPromotionProductId(jmProductModel.getId());
            if (f.getId() != null && f.getId() > 0) {
                daoCmsBtJmPromotionSku.update(f);
            } else {
                daoCmsBtJmPromotionSku.insert(f);
            }
        });

        //更新 tag
        cmsBtJmPromotionTagProductService.updateJmPromotionTagProduct(bean.getTagList(), jmPromotionModel.getChannelId(), jmProductModel.getId(), modifier);

        //更新mongo product tag
        productService.updateCmsBtProductTags(bean.getChannelId(), productInfo, jmPromotionModel.getRefTagId(), bean.getTagList(), modifier);

        return jmProductModel;

    }
    public BigDecimal getMaxMarketPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().max((m1,m2)->{return m1.getMarketPrice().doubleValue()>m2.getMarketPrice().doubleValue()?1:-1;}).get().getMarketPrice();
    }
    public BigDecimal getMinMarketPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().min((m1,m2)->{return m1.getMarketPrice().doubleValue()>m2.getMarketPrice().doubleValue()?1:-1;}).get().getMarketPrice();
    }
    public BigDecimal getMaxDealPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().max((m1,m2)->{return m1.getDealPrice().doubleValue()>m2.getDealPrice().doubleValue()?1:-1;}).get().getDealPrice();
    }
    public BigDecimal getMinDealPrice(List<CmsBtJmPromotionSkuModel> skuList)
    {
        return   skuList.stream().min((m1,m2)->{return m1.getDealPrice().doubleValue()>m2.getDealPrice().doubleValue()?1:-1;}).get().getDealPrice();
    }
    CmsBtJmPromotionProductModel loadJmPromotionProduct(PromotionDetailAddBean bean, CmsBtJmPromotionModel jmPromotionModel, String userName, CmsBtProductModel productInfo) {
        CmsBtJmPromotionProductModel jmProductModel = daoext.selectByProductCode(bean.getProductCode(), jmPromotionModel.getChannelId(), jmPromotionModel.getId());
        if (jmProductModel == null) {
            jmProductModel = new CmsBtJmPromotionProductModel();
            jmProductModel.setId(0);
            jmProductModel.setCreater(userName);
            jmProductModel.setCreated(new Date());
            jmProductModel.setJmHashId("");
//            if (!com.voyageone.common.util.StringUtils.isEmpty(product.getErrorMsg())) {
//                jmProductModel.setErrorMsg(product.getErrorMsg());
//            } else {
//                jmProductModel.setErrorMsg("");
//            }
            jmProductModel.setPriceStatus(0);
            jmProductModel.setDiscount(new BigDecimal(0));
            jmProductModel.setDiscount2(new BigDecimal(0));
            jmProductModel.setSkuCount(0);
            jmProductModel.setQuantity(0);
            jmProductModel.setDealEndTimeStatus(0);
            jmProductModel.setActivityStart(jmPromotionModel.getActivityStart());
            jmProductModel.setActivityEnd(jmPromotionModel.getActivityEnd());
            jmProductModel.setProductCode(bean.getProductCode());
            jmProductModel.setCmsBtJmPromotionId(jmPromotionModel.getId());
            jmProductModel.setChannelId(jmPromotionModel.getChannelId());
            jmProductModel.setSynchStatus(0);
            jmProductModel.setLimit(0);
            jmProductModel.setUpdateStatus(0);
            jmProductModel.setPromotionTag("");
            jmProductModel.setErrorMsg("");
            jmProductModel.setProductNameEn(productInfo.getCommon().getFields().getProductNameEn());
            if (productInfo.getCommon().getFields().getImages1() != null && productInfo.getCommon().getFields().getImages1().size() > 0) {
                if (productInfo.getCommon().getFields().getImages1().get(0).get("image1") != null) {
                    jmProductModel.setImage1(productInfo.getCommon().getFields().getImages1().get(0).get("image1").toString());
                }
            }
        }
        jmProductModel.setAppId(0L);
        jmProductModel.setPcId(0L);
        jmProductModel.setModifier(userName);
        jmProductModel.setModified(new Date());


        //获取 旧的tag数组
        String[] tagArray = jmProductModel.getPromotionTag().split("\\|");
        //tag数组转List
        ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tagArray));
        //tag 处理
        bean.getTagList().forEach(f -> {
            if (f.getChecked() == 0) {
                //删除
                tagList.remove(f.getName());
            } else if (f.getChecked() == 2) {
                //新增
                tagList.add(f.getName());
            }
        });

        //tag数组转为位以竖线(|)分隔的字符串
        StringBuilder sbPromotionTag = new StringBuilder();
        tagList.stream().distinct().forEach(f ->
                {
                    if (!StringUtils.isEmpty(f)) {
                        sbPromotionTag.append("|").append(f);
                    }
                }
        );
        if (sbPromotionTag.length() > 0) {
            jmProductModel.setPromotionTag(sbPromotionTag.substring(1));
        } else {
            jmProductModel.setPromotionTag("");
        }

        return jmProductModel;
    }

    List<CmsBtJmPromotionSkuModel> loadJmPromotionSkus(PromotionDetailAddBean bean, CmsBtJmPromotionProductModel jmProductModel, CmsBtProductModel productInfo, String modifier) {
        List<CmsBtProductModel_Sku> skusList = productInfo.getCommonNotNull().getSkus();
        List<BaseMongoMap<String, Object>> listSkuMongo = productInfo.getPlatform(bean.getCartId()).getSkus();
        List<CmsBtJmPromotionSkuModel> listPromotionSku = new ArrayList<>();
        skusList.forEach(sku -> {
            Double priceMsrp = 0d;
            Double priceRetail = 0d;
            Double priceSale = 0d;
            Double msrpUsd = 9d;
            BaseMongoMap<String, Object> mapSkuPlatform = getJMPlatformSkuMongo(listSkuMongo, sku.getSkuCode());
            if (mapSkuPlatform != null && Boolean.valueOf(mapSkuPlatform.getStringAttribute("isSale"))) {
                priceMsrp = mapSkuPlatform.getDoubleAttribute("priceMsrp");
                priceRetail = mapSkuPlatform.getDoubleAttribute("priceRetail");
                priceSale = mapSkuPlatform.getDoubleAttribute("priceSale");
                CmsBtProductModel_Sku cmsBtProductModel_sku = productInfo.getCommon().getSku(sku.getSkuCode());
                if (cmsBtProductModel_sku != null) {
                    msrpUsd = cmsBtProductModel_sku.getClientMsrpPrice();
                }
                CmsBtJmPromotionSkuModel skuModel = null;
                if (jmProductModel.getId() != null && jmProductModel.getId() > 0) {
                    skuModel = daoExtCmsBtJmPromotionSku.selectBySkuCode(sku.getSkuCode(), jmProductModel.getId(), jmProductModel.getCmsBtJmPromotionId());
                }
                if (skuModel == null) {
                    skuModel = new CmsBtJmPromotionSkuModel();
                    skuModel.setSynchStatus(0);
                    skuModel.setUpdateState(0);
                    skuModel.setCmsBtJmPromotionId(jmProductModel.getCmsBtJmPromotionId());
                    skuModel.setChannelId(jmProductModel.getChannelId());
                    skuModel.setSkuCode(sku.getSkuCode());
                    skuModel.setCreated(new Date());
                    skuModel.setCreater(modifier);
                    skuModel.setProductCode(jmProductModel.getProductCode());
                    skuModel.setErrorMsg("");
                    if (jmProductModel.getSynchStatus() == 2) {
                        skuModel.setUpdateState(1);//已变更
                        jmProductModel.setUpdateStatus(1);//已变更     新增了一个sku
                    }
                }
//            if (jmProductModel.getSynchStatus() == 2) {
//                if (skuModel.getDealPrice().doubleValue() != skuImportBean.getDealPrice()) {
//                    skuModel.setUpdateState(1);//已变更
//                    jmProductModel.setUpdateStatus(1);//已变更
//                }
//                if (skuModel.getMarketPrice().doubleValue() != skuImportBean.getMarketPrice()) {
//                    skuModel.setUpdateState(1);//已变更
//                    jmProductModel.setUpdateStatus(1);//已变更
//                }
//            }

                skuModel.setMsrpRmb(new BigDecimal(priceMsrp));
                skuModel.setRetailPrice(new BigDecimal(priceRetail));
                skuModel.setSalePrice(new BigDecimal(priceSale));
                skuModel.setMsrpUsd(new BigDecimal(msrpUsd));

                skuModel.setDealPrice(new BigDecimal(0));
                skuModel.setMarketPrice(new BigDecimal(priceMsrp));
                skuModel.setDiscount(BigDecimalUtil.divide(skuModel.getDealPrice(), skuModel.getMarketPrice(), 2));//折扣
                skuModel.setDiscount2(BigDecimalUtil.divide(skuModel.getDealPrice(), skuModel.getSalePrice(), 2));//折扣
                skuModel.setModified(new Date());
                skuModel.setModifier(modifier);
                listPromotionSku.add(skuModel);
            }
        });
        return listPromotionSku;
    }

    private BaseMongoMap<String, Object> getJMPlatformSkuMongo(List<BaseMongoMap<String, Object>> list, String skuCode) {
        for (BaseMongoMap<String, Object> map : list) {
            if (skuCode.equalsIgnoreCase(map.getStringAttribute("skuCode"))) {
                return map;
            }
        }
        return null;
    }

    @VOTransactional
    public void deleteFromPromotion(CmsBtJmPromotionModel promotion, AddProductSaveParameter parameter) {

        // CmsBtJmPromotionModel model = daoCmsBtJmPromotion.select(promotion());
        //Map<String,Object> map=new HashedMap();

        // 2.7.1
        if (promotion.getPrePeriodStart().getTime() < DateTimeUtilBeijing.getCurrentBeiJingDate().getTime()) {
            throw new BusinessException("预热已经开始,不能批量删除!");
        }
        //获取未上传的jmproduct
        List<CmsBtJmPromotionProductModel> listNotSych = daoext.selectNotSynchListByProductCodes(promotion.getId(), parameter.getCodeList());
        List<String> listNotSychCode = getListNotSychCode(listNotSych);//获取未上传的code

        if (listNotSychCode.size() > 0) {
            productService.removeTagByCodes(promotion.getChannelId(), listNotSychCode, promotion.getRefTagId());
        }
       List<Integer> jmPromotionProductIdList= getListNotSychJmPromotionProductId(listNotSych);
        //2.7.2.1 只删除未上传的商品  先删除sku  tag  再删除product
        if(jmPromotionProductIdList.size()>0) {
            daoExtCmsBtJmPromotionSku.batchDeleteSku(jmPromotionProductIdList);
            cmsBtJmPromotionTagProductDaoExt.batchDeleteTag(jmPromotionProductIdList);
            daoext.batchDeleteProduct(jmPromotionProductIdList);
        }
//        //2.7.3 删除 CmsBtPromotionCodes  CmsBtPromotionSkus
        CmsBtPromotionModel modelCmsBtPromotion = promotionService.getCmsBtPromotionModelByJmPromotionId(promotion.getId());
        if (modelCmsBtPromotion != null && listNotSych.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("listProductCode", listNotSychCode);
            map.put("promotionId", modelCmsBtPromotion.getId());
            cmsBtPromotionCodesDaoExtCamel.deleteByPromotionCodeList(map);
            cmsBtPromotionSkusDaoExtCamel.deleteByPromotionCodeList(map);
        }


    }

    public List<String> getListNotSychCode(List<CmsBtJmPromotionProductModel> listNotSych) {
        List<String> codeList = new ArrayList<>();
        listNotSych.stream().forEach((o) -> {
            codeList.add(o.getProductCode());
        });
        return codeList;
    }

    public List<Integer> getListNotSychJmPromotionProductId(List<CmsBtJmPromotionProductModel> listNotSych) {
        List<Integer> idList = new ArrayList<>();
        listNotSych.stream().forEach((o) -> {
            idList.add(o.getId());
        });
        return idList;
    }
}
