package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.ShoeCityFeedBean;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author jonasvlag. 16/3/30.
 * @version 2.0.0
 * @since 2.0.0
 */
class SEAnalysisContext {

    private final static String USE_DUTY = "useDuty";

    private final static String PRICE_WITH_DUTY = "priceWithDuty";

    private final static String PRICE_WITHOUT_DUTY = "priceWithoutDuty";

    private final static Pattern NUM_REGEX = Pattern.compile("^\\d+$");

    private List<CmsBtFeedInfoModel> codeList = new ArrayList<>();

    private Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

    private final ExpressionParser parser = new SpelExpressionParser();

    void put(ShoeCityFeedBean feedBean) {

        CmsBtFeedInfoModel_Sku sku = new CmsBtFeedInfoModel_Sku();

        sku.setSku(feedBean.getSku());
        if (NUM_REGEX.matcher(feedBean.getSize()).matches())
            sku.setSize(String.valueOf(Integer.valueOf(feedBean.getSize()) / 10));
        else
            sku.setSize(feedBean.getSize());
        sku.setBarcode(feedBean.getUpc());
        sku.setClientSku(feedBean.getClientSku());
        sku.setPrice_net(feedBean.getCost().doubleValue());
        sku.setPrice_current(getPriceCurrent(feedBean));

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
        List<String> imageUrls = new ArrayList<>();
        for (int i = 1; i <= 5; i++)
            imageUrls.add(String.format("http://image.sneakerhead.com/is/image/sneakerhead/%s-%s", feedBean.getImg_id(), i));
        product.setImage(imageUrls);
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

    private Double getPriceCurrent(ShoeCityFeedBean feedBean) {
        if (isUseDuty(feedBean)) {
            return getPriceWithDuty(feedBean);
        }
        return getPriceWithoutDuty(feedBean);
    }

    private boolean isUseDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(USE_DUTY);
        ExpParams params = getParam(feedBean);
        return getValue(exp, params, Boolean.class);
    }

    private Double getPriceWithDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(PRICE_WITH_DUTY);
        ExpParams params = getParam(feedBean);
        return getValue(exp, params, Double.class);
    }

    private Double getPriceWithoutDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(PRICE_WITHOUT_DUTY);
        ExpParams params = getParam(feedBean);
        return getValue(exp, params, Double.class);
    }

    private ExpParams getParam(ShoeCityFeedBean feedBean) {
        return new ExpParams(feedBean);
    }

    private <T> T getValue(String exp, ExpParams context, Class<T> resultClass) {
        return parser.parseExpression(exp).getValue(context, resultClass);
    }

    private String getExp(String name) {
        return Feeds.getVal1(ChannelConfigEnums.Channel.SHOE_CITY, name);
    }

    /**
     * 表达式常量
     */
    private class ExpParams {
        public final double ITEM_WEIGHT = 4D;
        public final double ALIPAY_COMMISSION = 0D;
        public final double TMALL_COMMISSION = 0.05D;
        public final double RETURN_MANAGEMENT = 0.05D;
        public final double ITEM_DUTY = 0.1D;
        public final double EXCHANGE_RATE = 6.5D;
        public double cost;
        ExpParams(ShoeCityFeedBean feedBean) {
            this.cost = feedBean.getCost().doubleValue();
        }
    }
}
