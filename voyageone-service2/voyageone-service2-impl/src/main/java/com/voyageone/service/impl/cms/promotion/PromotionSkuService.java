package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CmsBtPromotionSkuBean;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsPromotionDetail.SaveSkuPromotionPricesParameter;
import com.voyageone.service.dao.cms.CmsBtPromotionSkusDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesDaoExtCamel;
import com.voyageone.service.daoext.cms.CmsBtPromotionSkusDaoExt;
import com.voyageone.service.daoext.cms.CmsBtPromotionSkusDaoExtCamel;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionSkusModel;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionSkuService extends BaseService {
    @Autowired
    CmsBtPromotionSkusDao cmsBtPromotionSkusDao;
    @Autowired
    CmsBtPromotionSkusDaoExtCamel cmsBtPromotionSkusDaoExtCamel;
    @Autowired
    CmsBtPromotionCodesDaoExtCamel cmsBtPromotionCodesDaoExtCamel;
    @Autowired
    private CmsBtPromotionSkusDaoExt cmsPromotionSkuDao;

    public List<Map<String, Object>> getPromotionSkuList(Map<String, Object> params) {
        return cmsPromotionSkuDao.selectPromotionSkuList(params);
    }

    public int getPromotionSkuListCnt(Map<String, Object> params) {
        return cmsPromotionSkuDao.selectPromotionSkuListCnt(params);
    }

    @VOTransactional
    public int remove(int promotionId, long productId) {
        return cmsPromotionSkuDao.deletePromotionSkuByProductId(promotionId, productId);
    }

    public List<Map<String, Object>> getCmsBtPromotionSkuByPromotionIds(List<String> promotionIdList) {
        return cmsPromotionSkuDao.selectCmsBtPromotionSkuByPromotionIds(promotionIdList);
    }

    public List<CmsBtPromotionSkusModel> getListByWhere(Map<String, Object> map) {
//        Map<String, Object> map = new HashedMap();
//        map.put("promotionId", promotion_id);
//        map.put("productId", product_id);
        return cmsBtPromotionSkusDao.selectList(map);
    }

    public int update(CmsBtPromotionSkusModel cmsBtPromotionSkusModel) {
        return cmsBtPromotionSkusDao.update(cmsBtPromotionSkusModel);
    }

    public CmsBtPromotionSkusModel get(int promotionId, String productCode, String productSku) {
        Map<String, Object> map = new HashedMap();
        map.put("promotionId", promotionId);
        map.put("productCode", productCode);
        map.put("productSku", productSku);
        return cmsBtPromotionSkusDao.selectOne(map);
    }

    @VOTransactional
    public void saveSkuPromotionPrices(SaveSkuPromotionPricesParameter parameter) {
        parameter.getListSkuPromotionPriceInfo().forEach((p) -> {
            cmsBtPromotionSkusDaoExtCamel.updatePromotionPrice(p);
        });
        //CmsBtPromotionCodesDaoExtCamel
        cmsBtPromotionCodesDaoExtCamel.updatePromotionPrice(parameter.getPromotionId(), parameter.getProductCode());

    }

    public void loadSkuPrice(List<CmsBtPromotionSkuBean> listSku, AddProductSaveParameter parameter) {

        if(parameter==null) return;

        //1.商品内，SKU统一最高价    2.商品内，SKU统一最低价    3.商品内，SKU价格不统一
        List<Double> listPrice = new ArrayList<>();


        listSku.forEach(sku -> {
            double price = getSkuPrice(parameter, sku);
            listPrice.add(price);
            sku.setPromotionPrice(new BigDecimal(price));
        });
        switch (parameter.getSkuUpdType()) {
            case 1:
                double maxPrice = listPrice.stream().max((m1, m2) -> m1 > m2 ? 1 : -1).get();
                listSku.forEach(f -> f.setPromotionPrice(new BigDecimal(maxPrice)));
                break;
            case 2:
                double minPrice = listPrice.stream().min((m1, m2) -> m1 > m2 ? 1 : -1).get();
                listSku.forEach(f -> f.setPromotionPrice(new BigDecimal(minPrice)));
                break;
            case 3:
                break;
        }
    }

    double getSkuPrice(AddProductSaveParameter parameter, CmsBtPromotionSkuBean skuBean) {
        //价格类型    1 建议售价   2指导售价  3最终售价  4固定售价
        double price = 0;
        if (parameter.getPriceTypeId() == 4) {// 固定价格

            price = parameter.getPriceValue();

        } else if (parameter.getPriceTypeId() == 1)//团购价 deal_price
        {
            price = skuBean.getMsrpRmb().doubleValue();
            // price = "a.msrp_rmb*" + Double.toString(parameter.getDiscount());//中国官网价格
        } else if (parameter.getPriceTypeId() == 2) //市场价 market_price
        {
            price = skuBean.getRetailPrice().doubleValue();
            // price = "a.retail_price*" + Double.toString(parameter.getDiscount());//中国指导价格
        } else if (parameter.getPriceTypeId() == 3) {
            price = skuBean.getSalePrice().doubleValue();
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
}
