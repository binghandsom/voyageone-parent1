package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.Constants;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedBcbgBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voyageone.task2.cms.service.feed.BcbgConstants.*;

/**
 * 将 Feed 数据, 转换, 构造为 Cms 数据
 * Created by jonasvlag on 16/3/22.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
class BcbgAnalysisContext {

    private final static String APPAREL = "Apparel";

    private final static String ACCESSORIES = "Accessories";

    private List<CmsBtFeedInfoModel> codeList = new ArrayList<>();

    private Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

    void put(SuperFeedBcbgBean bcbgBean) {

        CmsBtFeedInfoModel_Sku sku = new CmsBtFeedInfoModel_Sku();

        sku.setSku(String.format("%s-%s-%s", bcbgBean.getSATNR(), bcbgBean.getCOLOR(), bcbgBean.getSIZE1()));
        sku.setSize(bcbgBean.getSIZE1());
        sku.setBarcode(bcbgBean.getEAN11());
        sku.setClientSku(bcbgBean.getMATNR());
        sku.setImage(bcbgBean.getStyleBean().getProductImgURLs());
        setPrices(bcbgBean, sku);

        CmsBtFeedInfoModel code = getProduct(bcbgBean);

        code.getSkus().add(sku);

        setAttributes(code.getAttribute(), bcbgBean);
    }

    List<CmsBtFeedInfoModel> getCodeList() {
        return codeList;
    }

    private void setAttributes(Map<String, List<String>> attributes, SuperFeedBcbgBean bcbgBean) {
        setAttribute(attributes, MATNR, bcbgBean.getMATNR());
        setAttribute(attributes, EAN11, bcbgBean.getEAN11());
        setAttribute(attributes, BRAND_ID, bcbgBean.getBRAND_ID());
        setAttribute(attributes, MATKL, bcbgBean.getMATKL());
        setAttribute(attributes, MATKL_ATT1, bcbgBean.getMATKL_ATT1());
        setAttribute(attributes, ZZCODE1, bcbgBean.getZZCODE1());
        setAttribute(attributes, ZZCODE2, bcbgBean.getZZCODE2());
        setAttribute(attributes, ZZCODE3, bcbgBean.getZZCODE3());
        setAttribute(attributes, MEINS, bcbgBean.getMEINS());
        setAttribute(attributes, BSTME, bcbgBean.getBSTME());
        setAttribute(attributes, COLOR, bcbgBean.getCOLOR());
        setAttribute(attributes, COLOR_ATWTB, bcbgBean.getCOLOR_ATWTB());
        setAttribute(attributes, SIZE1, bcbgBean.getSIZE1());
        setAttribute(attributes, SIZE1_ATWTB, bcbgBean.getSIZE1_ATWTB());
        setAttribute(attributes, SIZE1_ATINN, bcbgBean.getSIZE1_ATINN());
        setAttribute(attributes, ATBEZ, bcbgBean.getATBEZ());
        setAttribute(attributes, SAISO, bcbgBean.getSAISO());
        setAttribute(attributes, SAISJ, bcbgBean.getSAISJ());
        setAttribute(attributes, SAITY, bcbgBean.getSAITY());
        setAttribute(attributes, SATNR, bcbgBean.getSATNR());
        setAttribute(attributes, MAKTX, bcbgBean.getMAKTX());
        setAttribute(attributes, WLADG, bcbgBean.getWLADG());
        setAttribute(attributes, WHERL, bcbgBean.getWHERL());
        setAttribute(attributes, MEAN_EAN11, bcbgBean.getMEAN_EAN11());
        setAttribute(attributes, SIZE_FIT, bcbgBean.getStyleBean().getSizeFit());
    }

    private void setAttribute(Map<String, List<String>> attributes, String attribute, String value) {

        List<String> values;

        if (attributes.containsKey(attribute))
            values = attributes.get(attribute);
        else
            attributes.put(attribute, values = new ArrayList<>());

        if (!values.contains(value))
            values.add(value);
    }

    private CmsBtFeedInfoModel getProduct(SuperFeedBcbgBean bcbgBean) {

        CmsBtFeedInfoModel feedInfoModel;
        String code = bcbgBean.getStyleBean().getStyleID();
        String name = bcbgBean.getMAKTX();
        int indexOfComma = name.indexOf(",");
        if (indexOfComma > 0)
            name = name.substring(0, indexOfComma);

        if (codeMap.containsKey(code)) {
            feedInfoModel = codeMap.get(code);
            if (feedInfoModel.getName().length() < name.length())
                feedInfoModel.setShortDescription(name);
            return feedInfoModel;
        }
        
        String productDesc = bcbgBean.getStyleBean().getProductDesc();

        feedInfoModel = new CmsBtFeedInfoModel();

        feedInfoModel.setCategory(clearSpecialSymbol(bcbgBean.getMATKL()));
        feedInfoModel.setCode(code);
        feedInfoModel.setName(name);
        feedInfoModel.setModel(bcbgBean.getSATNR());
        feedInfoModel.setColor(bcbgBean.getCOLOR_ATWTB());
        feedInfoModel.setOrigin(bcbgBean.getWHERL());
        feedInfoModel.setSizeType("Women");
        feedInfoModel.setImage(bcbgBean.getStyleBean().getProductImgURLs());
        feedInfoModel.setBrand(bcbgBean.getBRAND_ID());
        feedInfoModel.setWeight(Constants.EmptyString);
        feedInfoModel.setShortDescription(name);
        feedInfoModel.setLongDescription(productDesc);
        feedInfoModel.setSkus(new ArrayList<>());
        feedInfoModel.setAttribute(new HashMap<>());
        feedInfoModel.setUpdFlg(0);
        feedInfoModel.setChannelId(channel.getId());
        feedInfoModel.setMaterial(productDesc);
        feedInfoModel.setUsageEn(productDesc);
        feedInfoModel.setProductType(bcbgBean.getMATKL_ATT1());

        codeList.add(feedInfoModel);
        codeMap.put(code, feedInfoModel);

        return feedInfoModel;
    }

    private String clearSpecialSymbol(String name) {
        return special_symbol.matcher(name.toLowerCase()).replaceAll(Constants.EmptyString).replace(" ", "-");
    }

    private void setPrices(SuperFeedBcbgBean bcbgBean, CmsBtFeedInfoModel_Sku sku) {
        BigDecimal msrp = new BigDecimal(bcbgBean.getA304_KBETR());
        BigDecimal price = new BigDecimal(bcbgBean.getA073_KBETR());
        BigDecimal retail = price;
               
        sku.setPriceCurrent(0d); // 人民币当前售价
        sku.setPriceClientRetail(price.doubleValue()); // 美金售价
        sku.setPriceMsrp(0d); // 人民币专柜价
        sku.setPriceClientMsrp(msrp.doubleValue()); // 美金专柜价
        sku.setPriceNet(retail.doubleValue()); // 美金成本价
    }
}
