package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.service.bean.cms.jumei.BatchUpdateSkuPriceParameterBean;
import com.voyageone.service.bean.cms.jumei.UpdateSkuDealPriceParameter;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionSkuDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesDaoExtCamel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.formula.functions.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionSku3Service {
    @Autowired
    CmsBtJmPromotionSkuDao dao;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    CmsBtPromotionCodesDaoExtCamel daoExtCamelCmsBtPromotionCodes;
    public CmsBtJmPromotionSkuModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionSkuModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionSkuModel entity) {
        return dao.insert(entity);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    @VOTransactional
    public int updateDealPrice(UpdateSkuDealPriceParameter parameter, String modifier) {
        CmsBtJmPromotionSkuModel model = dao.select(parameter.getPromotionSkuId());
        model.setDealPrice(BigDecimal.valueOf(parameter.getDealPrice()));
        model.setMarketPrice(BigDecimal.valueOf(parameter.getMarketPrice()));
        model.setDiscount(BigDecimalUtil.divide(model.getDealPrice(), model.getMarketPrice(), 2));
        model.setDiscount2(BigDecimalUtil.divide(model.getDealPrice(), model.getSalePrice(), 2));
        model.setModifier(modifier);
        int result = update(model);
        CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct = daoCmsBtJmPromotionProduct.select(model.getCmsBtJmPromotionProductId());
        if (modelCmsBtJmPromotionProduct.getUpdateStatus() != 1) {
            modelCmsBtJmPromotionProduct.setUpdateStatus(1);
            daoCmsBtJmPromotionProduct.update(modelCmsBtJmPromotionProduct);
        }
        daoExtCmsBtJmPromotionProduct.updateAvgPriceByPromotionProductId(model.getCmsBtJmPromotionProductId());//更新平均价格
        List<Integer> listJmPromotionProductId = new ArrayList<>();

        listJmPromotionProductId.add(model.getCmsBtJmPromotionProductId());
        daoExtCamelCmsBtPromotionCodes.updateJmPromotionPrice(model.getCmsBtJmPromotionId(), listJmPromotionProductId);//promotionPrice

        return result;
    }

    @VOTransactional
    public  void UpdateSkuDealPrice(BatchUpdateSkuPriceParameterBean parameter, int promotionProductId,String userName) {
        //1.商品内，SKU统一最高价    2.商品内，SKU统一最低价    3.商品内，SKU价格不统一
        List<Double> listPrice = new ArrayList<>();

        List<CmsBtJmPromotionSkuModel> listSku = getListByJmPromotionProductId(promotionProductId);
        UpdateSkuDealPrice(parameter,listSku,userName);
    }
    @VOTransactional
    public  void UpdateSkuDealPrice(BatchUpdateSkuPriceParameterBean parameter, List<CmsBtJmPromotionSkuModel> listSku,String userName) {
        //1.商品内，SKU统一最高价    2.商品内，SKU统一最低价    3.商品内，SKU价格不统一
        List<Double> listPrice = new ArrayList<>();

        listSku.forEach(sku -> {
            double price = getSkuPrice(parameter, sku);
            listPrice.add(price);
            sku.setDealPrice(new BigDecimal(price));
        });
        switch (parameter.getSkuUpdType()) {
            case 1:
                double maxPrice = listPrice.stream().max((m1, m2) -> m1 > m2 ? 1 : -1).get();
                listSku.forEach(f -> f.setDealPrice(new BigDecimal(maxPrice)));
                break;
            case 2:
                double minPrice = listPrice.stream().min((m1, m2) -> m1 > m2 ? 1 : -1).get();
                listSku.forEach(f -> f.setDealPrice(new BigDecimal(minPrice)));
                break;
            case 3:
                break;
        }
        listSku.forEach(f -> {
                    f.setModifier(userName);
                    f.setModified(new Date());
                   f.setDiscount(BigDecimalUtil.divide(f.getDealPrice(), f.getMarketPrice(), 2));//折扣
                    f.setDiscount2(BigDecimalUtil.divide(f.getDealPrice(), f.getSalePrice(), 2));//折扣

            dao.update(f);
                }
        );//更新deal价格
    }

      double getSkuPrice(BatchUpdateSkuPriceParameterBean parameter,CmsBtJmPromotionSkuModel sku) {
        //价格类型    1 建议售价   2指导售价  3最终售价  4固定售价
        double price = 0;
        if (parameter.getPriceTypeId() == 4) {// 固定价格

            price = parameter.getPriceValue();
        } else if (parameter.getPriceTypeId() == 1)//团购价 deal_price
        {
            price = sku.getMsrpRmb().doubleValue();
            // price = "a.msrp_rmb*" + Double.toString(parameter.getDiscount());//中国官网价格
        } else if (parameter.getPriceTypeId() == 2) //市场价 market_price
        {
            price = sku.getRetailPrice().doubleValue();
            // price = "a.retail_price*" + Double.toString(parameter.getDiscount());//中国指导价格
        } else if (parameter.getPriceTypeId() == 3) {
            price = sku.getSalePrice().doubleValue();
            //price = "a.sale_price*" + Double.toString(parameter.getDiscount());//中国最终售价
        }


        switch (parameter.getOptType()) {
            case "+":
                price += parameter.getPriceValue();
                break;
            case "-":
                price -= parameter.getPriceValue();
                break;
            case "*":
                price *= parameter.getPriceValue();
                break;
            case "=":
                //price = parameter.getPriceValue();
                break;
            default:
                break;
        }

        // 1小数点向上取整 Math.ceil(double a)   2个位向下取整    3个位向上取整    4无特殊处理   向上取整用   向下取整用Math.floor(double a)
        switch (parameter.getRoundType()) {

            case 1:
                price = Math.ceil(price);//1小数点向上取整
                break;
            case 2:
                price = Math.floor((price / 10)) * 10;//2个位向下取整
                break;
            case 3:
                price = Math.ceil((price / 10)) * 10;//3个位向上取整
                break;
            default:
                break;
        }
        return price;
    }
     List<CmsBtJmPromotionSkuModel> getListByJmPromotionProductId(int JmPromotionProductId) {
        Map<String, Object> map = new HashedMap();
        map.put("cmsBtJmPromotionProductId", JmPromotionProductId);
        return dao.selectList(map);
    }
}

