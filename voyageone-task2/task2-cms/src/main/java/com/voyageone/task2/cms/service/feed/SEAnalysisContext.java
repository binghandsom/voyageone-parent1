package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.StringUtils;
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
import java.util.regex.Pattern;

/**
 * @author jonasvlag. 16/3/30.
 * @version 2.0.0
 * @since 2.0.0
 */
class SEAnalysisContext {

    private final static Pattern NUM_REGEX = Pattern.compile("^\\d+$");
    private final ExpressionParser parser = new SpelExpressionParser();
    private List<CmsBtFeedInfoModel> codeList = new ArrayList<>();
    private Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

    void put(ShoeCityFeedBean feedBean) {

        CmsBtFeedInfoModel_Sku sku = new CmsBtFeedInfoModel_Sku();

        sku.setSku(feedBean.getSku());
        if (NUM_REGEX.matcher(feedBean.getSize()).matches()) {
            String size = String.valueOf(Integer.valueOf(feedBean.getSize()) / 10);
            if(Integer.valueOf(feedBean.getSize()) % 10 != 0){
                size +="."+Integer.valueOf(feedBean.getSize()) % 10;
            }
            sku.setSize(size);
        }else {
            sku.setSize(feedBean.getSize());
        }
        sku.setBarcode(feedBean.getUpc());
        sku.setClientSku(feedBean.getClientSku());
        sku.setPriceNet(feedBean.getCost().doubleValue());
        BigDecimal price = new BigDecimal(getPriceCurrent(feedBean));
        sku.setPriceCurrent(price.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue());

        sku.setPriceMsrp(CommonUtil.getRoundUp2Digits(sku.getPriceCurrent() / 0.7));

        sku.setPriceClientMsrp(CommonUtil.getRoundUp2Digits(sku.getPriceMsrp() / 6.5));

        sku.setPriceClientRetail(CommonUtil.getRoundUp2Digits(sku.getPriceCurrent() / 6.5));

        sku.setQty(feedBean.getQty());

        sku.setWeightCalc("4");

        sku.setWeightOrgUnit("lb");

        sku.setWeightOrg("4");

        CmsBtFeedInfoModel code = getProduct(feedBean);

        code.getSkus().add(sku);
    }

    List<CmsBtFeedInfoModel> getCodeList() {
        return codeList;
    }

    private CmsBtFeedInfoModel getProduct(ShoeCityFeedBean feedBean) {

        String code = StringUtils.replaceBlankToDash(feedBean.getCode() + "-"+feedBean.getImg_id());

        if (codeMap.containsKey(code))
            return codeMap.get(code);

        CmsBtFeedInfoModel product = new CmsBtFeedInfoModel();

        product.setCategory(feedBean.getProduct_type());
        product.setCode(code);
        product.setName(feedBean.getCategory());
        product.setModel(StringUtils.replaceBlankToDash(feedBean.getCode()));
        product.setColor(feedBean.getColor());
        product.setOrigin("None");
        product.setSizeType(feedBean.getSize_type());
        List<String> imageUrls = new ArrayList<>();
        for (int i = 1; i <= 5; i++)
            imageUrls.add(String.format(Constants.productForOtherSystemInfo.IMG_URL_WITH_PARAMENTER, feedBean.getImg_id(), i));
        product.setImage(imageUrls);
        product.setBrand(feedBean.getBrand());
        product.setWeight("4");
        product.setShortDescription(feedBean.getCategory());
        product.setLongDescription(feedBean.getCategory());
        product.setSkus(new ArrayList<>());
        //增加属性
        Map<String, List<String>> attribute = new HashMap<>();
        List<String> productType = new ArrayList<>();
        productType.add(feedBean.getProduct_type());
        List<String> sizeType = new ArrayList<>();
        sizeType.add(feedBean.getSize_type());
        attribute.put("productType",productType);
        attribute.put("sizeType",sizeType);
        //增加属性结束
        product.setAttribute(attribute);
        product.setUpdFlg(0);
        product.setChannelId(ChannelConfigEnums.Channel.SHOE_CITY.getId());
        product.setProductType(feedBean.getProduct_type());
        product.setUsageEn(feedBean.getCategory());
        product.setMaterial(feedBean.getCategory());
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
        String exp = getExp(FeedEnums.Name.useDuty);
        ExpParams params = getParam(feedBean);
        return getValue(exp, params, Boolean.class);
    }

    private Double getPriceWithDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(FeedEnums.Name.priceWithDuty);
        ExpParams params = getParam(feedBean);
        return getValue(exp, params, Double.class);
    }

    private Double getPriceWithoutDuty(ShoeCityFeedBean feedBean) {
        String exp = getExp(FeedEnums.Name.priceWithoutDuty);
        ExpParams params = getParam(feedBean);
        return getValue(exp, params, Double.class);
    }

    private ExpParams getParam(ShoeCityFeedBean feedBean) {
        return new ExpParams(feedBean);
    }

    private <T> T getValue(String exp, ExpParams context, Class<T> resultClass) {
        return parser.parseExpression(exp).getValue(context, resultClass);
    }

    private String getExp(FeedEnums.Name name) {
        return Feeds.getVal1(ChannelConfigEnums.Channel.SHOE_CITY.getId(), name);
    }

    /**
     * 表达式常量
     */
    private class ExpParams {

        // 梁兄请不要删除一下常量 parseExpression会用
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
