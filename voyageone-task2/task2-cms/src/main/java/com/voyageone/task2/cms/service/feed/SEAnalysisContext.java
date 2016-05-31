package com.voyageone.task2.cms.service.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Codes;
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

    private List<CmsBtFeedInfoModel> codeList = new ArrayList<>();

    private Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

    private final ExpressionParser parser = new SpelExpressionParser();

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
            imageUrls.add(getImageFullUrl(ChannelConfigEnums.Channel.SHOE_CITY.getId(), feedBean.getImg_id()) + ".jpg");
        product.setImage(imageUrls);
        product.setBrand(feedBean.getBrand());
        product.setWeight("4");
        product.setShortDescription("");
        product.setLongDescription("");
        product.setSkus(new ArrayList<>());
        product.setAttribute(new HashMap<>());
        product.setUpdFlg(0);
        product.setChannelId(ChannelConfigEnums.Channel.SHOE_CITY.getId());
        product.setProductType(feedBean.getProduct_type());

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

    /**
     * 取得显示用图片的url,其中图片名字的%s保留(http://shenzhen-vo.oss-cn-shenzhen.aliyuncs.com/products/010/50/%s.jpg)
     * @param channelId
     * @return
     */
    private String getImageFullUrl (String channelId, String imageName) {

        // 取得CMS中默认的显示用模板ID
        String commonTemplateId = Codes.getCodeName("IMAGE_TEMPLATE", "DEFAULT_ID");
        if (commonTemplateId == null)
            throw new BusinessException("tm_code表中IMAGE_TEMPLATE的DEFAULT_ID定义不存在");

        // 取得显示图片用URL
        String templateImageUrl = Codes.getCodeName("IMAGE_TEMPLATE", "URL");
        if (StringUtils.isEmpty(templateImageUrl))
            throw new BusinessException("tm_codes表中对应的IMAGE_TEMPLATE,URL找不到数据");

        // 返回图片URl(其中图片名字%s未替换)
        return String.format(templateImageUrl, channelId, commonTemplateId, imageName);
    }
}
