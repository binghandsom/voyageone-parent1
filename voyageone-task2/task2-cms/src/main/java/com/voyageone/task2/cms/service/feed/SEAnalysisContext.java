package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.ShoeCityFeedBean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jonasvlag. 16/3/30.
 * @version 2.0.0
 * @since 2.0.0
 */
public class SEAnalysisContext {

    private final static String USE_DUTY = "useDuty";

    private final static String PRICE_WITH_DUTY = "priceWithDuty";

    private final static String PRICE_WITHOUT_DUTY = "priceWithoutDuty";

    private List<CmsBtFeedInfoModel> codeList = new ArrayList<>();

    private Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

    private final ExpressionParser parser = new SpelExpressionParser();

    private <T> T calc(String exp, ShoeCityFeedBean feedBean, Class<T> resultType) {
        return parser.parseExpression(exp).getValue(feedBean, resultType);
    }

    void put(ShoeCityFeedBean feedBean) {

        CmsBtFeedInfoModel_Sku sku = new CmsBtFeedInfoModel_Sku();

        sku.setSku(feedBean.getSku());
        sku.setSize(String.valueOf(Integer.valueOf(feedBean.getSize()) / 10));
        sku.setBarcode(feedBean.getUpc());
        sku.setClientSku(feedBean.getClientSku());
        sku.setPrice_net(feedBean.getCost().doubleValue());
        sku.setPrice_current(getPriceCurrent(feedBean).doubleValue());

        CmsBtFeedInfoModel code = getProduct(feedBean);

        code.getSkus().add(sku);
    }

    List<CmsBtFeedInfoModel> getCodeList() {
        return codeList;
    }

    private CmsBtFeedInfoModel getProduct(ShoeCityFeedBean feedBean) {

        String code = feedBean.getCode();

        if (codeMap.containsKey(code))
            return codeMap.get(code);

        CmsBtFeedInfoModel product = new CmsBtFeedInfoModel();

        product.setCategory(feedBean.getProduct_type());
        product.setCode(code);
        product.setName(feedBean.getCategory());
        product.setModel(code);
        product.setColor(feedBean.getColor());
        product.setOrigin("None");
        product.setSizeType(feedBean.getSize_type());
        product.setImage(null); // TODO ??
        product.setBrand(feedBean.getBrand());
        product.setWeight("4");
        product.setShort_description("");
        product.setLong_description("");
        product.setSkus(new ArrayList<>());
        product.setAttribute(new HashMap<>());
        product.setUpdFlg(0);
        product.setChannelId(ChannelConfigEnums.Channel.SHOE_CITY.getId());

        codeList.add(product);
        codeMap.put(code, product);

        return product;
    }

    private BigDecimal getPriceCurrent(ShoeCityFeedBean feedBean) {
        if (isUseDuty(feedBean)) {
            return getPriceWithDuty(feedBean);
        }
        return getPriceWithoutDuty(feedBean);
    }

    private boolean isUseDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(USE_DUTY);
        Map<String, Object> params = getParam(feedBean);
        return getValue(exp, params, Boolean.class);
    }

    private BigDecimal getPriceWithDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(PRICE_WITH_DUTY);
        Map<String, Object> params = getParam(feedBean);
        return getValue(exp, params, BigDecimal.class);
    }

    private BigDecimal getPriceWithoutDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(PRICE_WITHOUT_DUTY);
        Map<String, Object> params = getParam(feedBean);
        return getValue(exp, params, BigDecimal.class);
    }

    private Map<String, Object> getParam(ShoeCityFeedBean feedBean) {
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("const", new ExpConstants());
        paramMap.put("feed", feedBean);
        return paramMap;
    }

    private <T> T getValue(String exp, Map<String, Object> context, Class<T> resultClass) {
        return parser.parseExpression(exp).getValue(context, resultClass);
    }

    private String getExp(String name) {
        return Feeds.getVal1(ChannelConfigEnums.Channel.SHOE_CITY, name);
    }

    /**
     * 表达式常量
     */
    private class ExpConstants {
        public final BigDecimal ITEM_WEIGHT = new BigDecimal(4);
        public final BigDecimal ALIPAY_COMMISSION = new BigDecimal(0);
        public final BigDecimal TMALL_COMMISSION = new BigDecimal(0.05);
        public final BigDecimal RETURN_MANAGEMENT = new BigDecimal(0.05);
        public final BigDecimal ITEM_DUTY = new BigDecimal(0.1);
        public final BigDecimal EXCHANGE_RATE = new BigDecimal(6.5);
    }
}
