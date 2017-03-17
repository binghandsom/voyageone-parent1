package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.domain.Prop;
import com.jd.open.api.sdk.domain.Sku;
import com.jd.open.api.sdk.domain.Ware;
import com.jd.open.api.sdk.domain.ware.ImageReadService.Image;
import com.jd.open.api.sdk.request.ware.*;
import com.jd.open.api.sdk.response.ware.*;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import com.voyageone.components.jd.bean.JdProductBean;
import com.voyageone.components.jd.bean.JdProductNewBean;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 京东商品类API调用服务
 * <p/>
 * update by charis on 2017/2/23.
 */
@SuppressWarnings("ALL")
@Component
public class JdWareNewService extends JdBase {

    // 重试次数
    private static final int MAX_RETRY_TIMES = 3;


    /**
     * 京东新增商品
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return Long    京东商品ID
     */
    public Long addProduct(ShopBean shop, JdProductNewBean jdProduct) throws BusinessException {
        WareWriteAddRequest request = new WareWriteAddRequest();
        Ware ware = new Ware();
        // 商品id(必须)
        if (jdProduct.getWareId() != null)             ware.setWareId(jdProduct.getWareId());
        // 京东三级类目id(必须)
        if (jdProduct.getCategoryId() != null)         ware.setCategoryId(jdProduct.getCategoryId());
        // 商品名称(必须)
        if (jdProduct.getTitle() != null)              ware.setTitle(jdProduct.getTitle());
        // 品牌ID(非必须)
        if (jdProduct.getBrandId() != null)            ware.setBrandId(jdProduct.getBrandId());
        // 关联版式ID(必须)
        if (jdProduct.getTemplateId() != null)         ware.setTemplateId(jdProduct.getTemplateId());
        // 运费模板ID(必须)
        if (jdProduct.getTransportId() != null)        ware.setTransportId(jdProduct.getTransportId());
        // 商品状态(非必须)
        if (jdProduct.getWareStatus() != null)         ware.setWareStatus(jdProduct.getWareStatus());
        // 商品外部ID(非必须)
        if (jdProduct.getOuterId() != null)            ware.setOuterId(jdProduct.getOuterId());
        // 商品货号(非必须)
        if (jdProduct.getItemNum() != null)            ware.setItemNum(jdProduct.getItemNum());
        // 商品条形码(非必须,不使用)
//        ware.setBarCode();
        // 商品产出地区(非必须,不使用)
//        ware.setWareLocation();
        // 商品发货地(非必须,不使用)
//        ware.setDelivery();
        // 广告词链接地址 带链接的广告词 广告词仅文字内容
        if (jdProduct.getAdWords() != null)            ware.setAdWords(jdProduct.getAdWords());
        // 包装规格
        if (jdProduct.getWrap() != null)               ware.setWrap(jdProduct.getWrap());
        // 商品包装清单
        if (jdProduct.getPackListing() != null)        ware.setPackListing(jdProduct.getPackListing());
        // 长(单位:mm)(必须)
        if (jdProduct.getLength() != null)             ware.setLength(jdProduct.getLength());
        // 宽(单位:mm)(必须)
        if (jdProduct.getWidth() != null)              ware.setWidth(jdProduct.getWidth());
        // 高(单位:mm)(必须)
        if (jdProduct.getHeight() != null)             ware.setHeight(jdProduct.getHeight());
        // 重量(单位:kg)(必须)
        if (jdProduct.getWeight() != null)             ware.setWeight(jdProduct.getWeight());
        // 商品基础属性
        if (jdProduct.getProps() != null)              ware.setProps(jdProduct.getProps());
        // 商品特殊属性
        if (jdProduct.getFeatures() != null)           ware.setFeatures(jdProduct.getFeatures());
        // 店内分类ID
        if (jdProduct.getShopCategorys() != null)      ware.setShopCategorys(jdProduct.getShopCategorys());
        // 移动版商品介绍(目前先不设置)
//        ware.setMobileDesc();
        // PC版商品介绍
        if (jdProduct.getIntroduction() != null)       ware.setIntroduction(jdProduct.getIntroduction());
        // 售后服务
        if (jdProduct.getAfterSales() != null)         ware.setAfterSales(jdProduct.getAfterSales());
        // 京东价
        if (jdProduct.getJdPrice() != null)            ware.setJdPrice(jdProduct.getJdPrice());
        // 市场价
        if (jdProduct.getMarketPrice() != null)        ware.setMarketPrice(jdProduct.getMarketPrice());
        // SKU
        if (jdProduct.getSkus() != null)               ware.setSkus(jdProduct.getSkus());
        // 商品图片
        if (jdProduct.getImages() != null)             ware.setImages(jdProduct.getImages());

//        WareAddRequest request = new WareAddRequest();
//        // 流水号(非必须)
//        if (jdProduct.getTradeNo() != null)           request.setTradeNo(jdProduct.getTradeNo());
//        // 产地(非必须)
//        if (jdProduct.getWareLocation() != null)      request.setWareLocation(jdProduct.getWareLocation());
//        // 类目id(必须)
//        request.setCid(jdProduct.getCid());
//        // 自定义店内分类(非必须)
//        if (jdProduct.getShopCategory() != null)      request.setShopCategory(jdProduct.getShopCategory());
//        // 商品标题(必须)
//        if (jdProduct.getTitle() != null)             request.setTitle(jdProduct.getTitle());
//        // UPC编码(非必须)
//        if (jdProduct.getUpcCode() != null)           request.setUpcCode(jdProduct.getUpcCode());
//        // 操作类型 现只支持：offsale 或onsale,默认为下架状态 (非必须)
//        if (jdProduct.getOptionType() != null)        request.setOptionType(jdProduct.getOptionType());
//        // 外部商品编号，对应商家后台货号(非必须)
//        if (jdProduct.getItemNum() != null)           request.setItemNum(jdProduct.getItemNum());
//        // 库存(必须)
//        if (jdProduct.getStockNum() != null)          request.setStockNum(jdProduct.getStockNum());
//        // 生产厂商(非必须)
//        if (jdProduct.getProducter() != null)         request.setProducter(jdProduct.getProducter());
//        // 包装规格 (非必须)
//        if (jdProduct.getWrap() != null)              request.setWrap(jdProduct.getWrap());
//        // 长(单位:mm)(必须)
//        if (jdProduct.getLength() != null)            request.setLength(jdProduct.getLength());
//        // 宽(单位:mm)(必须)
//        if (jdProduct.getWide() != null)              request.setWide(jdProduct.getWide());
//        // 高(单位:mm)(必须)
//        if (jdProduct.getHigh() != null)              request.setHigh(jdProduct.getHigh());
//        // 重量(单位:kg)(必须)
//        if (jdProduct.getWeight() != null)            request.setWeight(jdProduct.getWeight());
//        // 进货价,精确到2位小数，单位:元(非必须)
//        if (jdProduct.getCostPrice() != null)         request.setCostPrice(jdProduct.getCostPrice());
//        // 市场价, 精确到2位小数，单位:元(必须)
//        if (jdProduct.getMarketPrice() != null)       request.setMarketPrice(jdProduct.getMarketPrice());
//        // 京东价,精确到2位小数，单位:元(必须)
//        if (jdProduct.getJdPrice() != null)           request.setJdPrice(jdProduct.getJdPrice());
//        // 描述（最多支持3万个英文字符(必须)
//        if (jdProduct.getNotes() != null)             request.setNotes(jdProduct.getNotes());
//        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
//        if (jdProduct.getWareImage() != null)         request.setWareImage(jdProduct.getWareImage());
//        // 包装清单(非必须)
//        if (jdProduct.getPackListing() != null)       request.setPackListing(jdProduct.getPackListing());
//        // 售后服务(非必须)
//        if (jdProduct.getService() != null)           request.setService(jdProduct.getService());
////        // sku属性,一组sku 属性之间用^分隔，多组用|分隔格式(非必须)
////      if (jdProduct.getXXX() != null)               request.setSkuProperties(jdProduct.getSkuProperties());
//        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1(必须)
//        if (jdProduct.getAttributes() != null)        request.setAttributes(jdProduct.getAttributes());
////        // sku价格,多组之间用‘|’分隔，格式:p1|p2 (非必须)
////      if (jdProduct.getSkuPrices() != null)         request.setSkuPrices(jdProduct.getSkuPrices());
////        // sku 库存,多组之间用‘|’分隔， 格式:s1|s2(非必须)
////      if (jdProduct.getSkuStocks() != null)         request.setSkuStocks(jdProduct.getSkuStocks());
////        // 自定义属性值别名：属性ID:属性值ID:别名(非必须)
////      if (jdProduct.getPropertyAlias() != null)     request.setPropertyAlias(jdProduct.getPropertyAlias());
////        // SKU外部ID，对个之间用‘|’分隔格(非必须)
////      if (jdProduct.getOuterId() != null)           request.setOuterId(jdProduct.getOuterId());
//        // 是否先款后货,false为否，true为是 (非必须)
//        if (jdProduct.getPayFirst() != null)          request.setPayFirst(jdProduct.getPayFirst());
//        // 发票限制：非必须输入，true为限制，false为不限制开增值税发票，FBP、LBP、SOPL、SOP类型商品均可输入(非必须)
//        if (jdProduct.getCanVAT() != null)            request.setCanVAT(jdProduct.getCanVAT());
//        // 是否进口商品：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        if (jdProduct.getImported() != null)          request.setImported(jdProduct.getImported());
//        // 是否保健品：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        if (jdProduct.getHealthProduct() != null)     request.setHealthProduct(jdProduct.getHealthProduct());
//        // 是否保质期管理商品, false为否，true为是(非必须)
//        if (jdProduct.getShelfLife() != null)         request.setShelfLife(jdProduct.getShelfLife());
//        // 保质期：非必须输入，0-99999范围区间，FBP类型商品可输入(非必须)
//        if (jdProduct.getShelfLifeDays() != null)     request.setShelfLifeDays(jdProduct.getShelfLifeDays());
//        // 是否序列号管理：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
//        if (jdProduct.getSerialNo() != null)          request.setSerialNo(jdProduct.getSerialNo());
//        // 大家电购物卡：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
//        if (jdProduct.getAppliancesCard() != null)    request.setAppliancesCard(jdProduct.getAppliancesCard());
//        // 是否特殊液体：非必须输入，false为否，true为是，FBP、LBP、SOPL类型商品可输入(非必须)
//        if (jdProduct.getSpecialWet() != null)        request.setSpecialWet(jdProduct.getSpecialWet());
//        // 商品件型：FBP类型商品必须输入，非FBP类型商品可输入非必填，0免费、1超大件、2超大件半件、3大件、4大件半件、5中件、6中件半件、7小件、8超小件(必须)
//        if (jdProduct.getWareBigSmallModel() != null) request.setWareBigSmallModel(jdProduct.getWareBigSmallModel());
//        // 商品包装：FBP类型商品必须输入，非FBP类型商品可输入非必填，1普通商品、2易碎品、3裸瓶液体、4带包装液体、5按原包装出库(必须)
//        if (jdProduct.getWarePackType() != null)      request.setWarePackType(jdProduct.getWarePackType());
//        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入(非必须)
//        if (jdProduct.getInputPids() != null)         request.setInputPids(jdProduct.getInputPids());
//        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’图书品类输入值规则： ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd”
//        // 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式(非必须)
//        if (jdProduct.getInputStrs() != null)         request.setInputStrs(jdProduct.getInputStrs());
//        // 是否输入验证码 true:是;false:否  (非必须)
//        if (jdProduct.getHasCheckCode() != null)      request.setHasCheckCode(jdProduct.getHasCheckCode());
//        // 广告词内容最大支持45个字符(非必须)
//        if (jdProduct.getAdContent() != null)         request.setAdContent(jdProduct.getAdContent());
//        // 定时上架时间 时间格式：yyyy-MM-dd HH:mm:ss;规则是大于当前时间，10天内。(非必须)
//        if (jdProduct.getListTime() != null)          request.setListTime(jdProduct.getListTime());
//        // 品牌id
//        if (jdProduct.getBrandId() != null)           request.setBrandId(jdProduct.getBrandId());

        // 如果新增商品失败，返回0 (如果不设初期值后面有可能会报没有初期化的错误(wareId may not have been initialized))
        long wareId = 0L;

        request.setWare(ware);
        request.setSkus(ware.getSkus());
        try {
            // 调用京东新增商品API(360buy.ware.add)
            WareWriteAddResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回商品ID
                    wareId = response.getWare().getWareId();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getMsg());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("[京东返回应答为空(response = null)]");
            }
        } catch (Exception ex) {
            logger.error("调用京东API新增京东商品信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                    + shop.getCart_id() + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("新增京东商品信息失败 " + ex.getMessage());
        }


        return wareId;
    }

    /**
     * 京东修改商品
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return String    商品更改时间
     */
    public boolean updateProduct(ShopBean shop, JdProductNewBean jdProduct) throws BusinessException {
        // if null 更新的时候，如果属性为null，就不设置，如果是空，就让它改为空
        // 京东更新商品的接口换成 jingdong.ware.write.updateWare
        WareWriteUpdateWareRequest request = new WareWriteUpdateWareRequest();
        Ware ware = new Ware();
        // 商品id(必须)
        if (jdProduct.getWareId() != null)             ware.setWareId(jdProduct.getWareId());
        // 商品名称(必须)
        if (jdProduct.getTitle() != null)              ware.setTitle(jdProduct.getTitle());
        // 品牌ID(非必须)
        if (jdProduct.getBrandId() != null)            ware.setBrandId(jdProduct.getBrandId());
        // 关联版式ID(必须)
        if (jdProduct.getTemplateId() != null)         ware.setTemplateId(jdProduct.getTemplateId());
        // 运费模板ID(非必须)
        if (jdProduct.getTransportId() != null)        ware.setTransportId(jdProduct.getTransportId());
        // 商品状态(非必须)
        if (jdProduct.getWareStatus() != null)         ware.setWareStatus(jdProduct.getWareStatus());
        // 商品外部ID(非必须)
        if (jdProduct.getOuterId() != null)            ware.setOuterId(jdProduct.getOuterId());
        // 商品货号(非必须)
        if (jdProduct.getItemNum() != null)            ware.setItemNum(jdProduct.getItemNum());
        // 商品条形码(非必须,不使用)
//        ware.setBarCode();
        // 商品产出地区(非必须,不使用)
//        ware.setWareLocation();
        // 商品发货地(非必须,不使用)
//        ware.setDelivery();
        // 广告词链接地址 带链接的广告词 广告词仅文字内容
        if (jdProduct.getAdWords() != null)            ware.setAdWords(jdProduct.getAdWords());
        // 包装规格
        if (jdProduct.getWrap() != null)               ware.setWrap(jdProduct.getWrap());
        // 商品包装清单
        if (jdProduct.getPackListing() != null)        ware.setPackListing(jdProduct.getPackListing());
        // 长(单位:mm)(必须)
        if (jdProduct.getLength() != null)             ware.setLength(jdProduct.getLength());
        // 宽(单位:mm)(必须)
        if (jdProduct.getWidth() != null)              ware.setWidth(jdProduct.getWidth());
        // 高(单位:mm)(必须)
        if (jdProduct.getHeight() != null)             ware.setHeight(jdProduct.getHeight());
        // 重量(单位:kg)(必须)
        if (jdProduct.getWeight() != null)             ware.setWeight(jdProduct.getWeight());
        // 商品基础属性
        if (jdProduct.getProps() != null)              ware.setProps(jdProduct.getProps());
        // 商品特殊属性
        if (jdProduct.getFeatures() != null)           ware.setFeatures(jdProduct.getFeatures());
        // 商品图片(目前新API不支持该字段)
//        ware.setImages();
        // 店内分类ID
        if (jdProduct.getShopCategorys() != null)      ware.setShopCategorys(jdProduct.getShopCategorys());
        // 移动版商品介绍(目前先不设置)
//        ware.setMobileDesc();
        // PC版商品介绍
        if (jdProduct.getIntroduction() != null)       ware.setIntroduction(jdProduct.getIntroduction());
        // 售后服务
        if (jdProduct.getAfterSales() != null)         ware.setAfterSales(jdProduct.getAfterSales());
        // 京东价
        if (jdProduct.getJdPrice() != null)            ware.setJdPrice(jdProduct.getJdPrice());
        // 市场价
        if (jdProduct.getMarketPrice() != null)        ware.setMarketPrice(jdProduct.getMarketPrice());
        // SKU (目前新API不支持该字段)
//        ware.setSkus();

        request.setWare(ware);

        // 商品更改时间
        boolean result = false;

        try {
            // 调用京东修改商品API(jingdong.ware.write.updateWare)
            WareWriteUpdateWareResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回商品更改时间
                    result = response.getSuccess();
                } else {
                    // 京东返回失败的场合
                    String errMsg = response.getMsg();
                    // switch字符串不能为null
                    if (!StringUtils.isEmpty(response.getCode())) {
                        switch (response.getCode()) {
                            case "11000003":
                                // 11000003:参数太长
                                errMsg += " 可能是因为该Group下某些产品Sku属性中size的最大长度超过25位了，请设置小于25位的容量/尺码值";
                            case "11000012":
                                // 11000012:参数包含非法字符
                                errMsg += " 可能是因为该Group下某些产品尺寸或颜色中包含特殊字符，比如逗号等";
                            case "11000019":
                                // 11000019:非法的参数，不允许或者不能识别
                                errMsg += " 可能是因为该Group下某些产品的Sku属性中的容量/尺码没有设置";
                            default:
                                errMsg += "";
                        }
                    }
                    throw new BusinessException(errMsg);
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("[京东返回应答为空(response = null)]");
            }
        } catch (Exception ex) {
            logger.error("调用京东API修改商品信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                    + shop.getCart_id() + ",ware_id:" + request.getWare().getWareId() + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("修改京东商品信息失败[商品ID:" + request.getWare().getWareId() + "] " + ex.getMessage());
        }

        return result;
    }

    /**
     * 京东删除商品
     *
     * @param shop ShopBean      店铺信息
     * @param strWareId String   商品id
     * @param tradeNo String     流水号
     * @return boolean           删除商品是否成功
     */
    public boolean deleteWare(ShopBean shop, String strWareId, String tradeNo) throws BusinessException {
        WareDeleteRequest request = new WareDeleteRequest();

        // 商品id(必须)
        request.setWareId(strWareId);
        // 流水号(必须)
        request.setTradeNo(tradeNo);

        try {
            // 调用京东根据商品Id，流水号删除商品API(360buy.ware.delete)
            WareDeleteResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                String strReturnCode = response.getCode();
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(strReturnCode)) {
                    // 返回删除商品成功
                    return true;
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API删除商品失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                    + shop.getCart_id() + ",ware_id:" + request.getWareId() + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("删除京东商品信息失败[商品ID:" + request.getWareId() + "] " + ex.getMessage());
        }

        logger.error("调用京东API删除商品失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id()
                + ",ware_id:" + request.getWareId() + ",response=null");
        return false;
    }

    /**
     * 京东根据商品Id，销售属性值增加图片
     * 新增商品主图时，属性值Id(颜色值Id)请输入0000000000
     * 新增SKU图片时，属性值Id(颜色值Id)请输入颜色值Id
     *
     * @param shop ShopBean      店铺信息
     * @param strWareId String   商品id
     * @param attrValueId String 属性值Id(颜色值Id)
     * @param picUrl String      图片url
     * @param picName string     图片名
     * @param isMainPic Boolean  是否主图
     * @return boolean           新增主图是否成功
     */
    public boolean addWarePropimg(ShopBean shop, String strWareId, String attrValueId, String picUrl, String picName, Boolean isMainPic) throws BusinessException {
        // 如果图片Url为空，返回false
        if (StringUtils.isEmpty(picUrl)) {
            logger.error("调用京东API根据商品Id，销售属性值Id增加图片时传入的图片URL为空 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",ware_id:" + strWareId + ",attrValueId:" + attrValueId + ",pic_url:" + picUrl);
            return false;
        }
        WarePropimgAddRequest request = new WarePropimgAddRequest();

        // 商品id(必须)
        request.setWareId(strWareId);
        // 属性值Id(颜色值Id)（如果没有此ID，请输入0000000000）(必须)
        // 商品主图用0000000000，SKU图用颜色值id
        request.setAttributeValueId(attrValueId);
        // 否把当前图片设置为主图。若当前sku无主图，则此项必填true(非必须)
        request.setMainPic(isMainPic);
        // 图片数据（注意：签名时不需要添加此参数，image参数通过post 输出流方式发送）图片类型只支持：png和jpg格式，图片须800x800；不能大于1M
        InputStream is = null;
        String picFileName = strWareId + "-" + attrValueId + "-" + picName;

        try {
            is = getImgInputStream(picUrl, MAX_RETRY_TIMES);
            Assert.notNull(is, "inputStream为null，图片流获取失败！picUrl=" + picUrl);
            // 图片设置
            byte[] picBytes = IOUtils.toByteArray(is);
            com.jd.open.api.sdk.FileItem fileItem = new com.jd.open.api.sdk.FileItem(picFileName, picBytes);
            request.setImage(fileItem);

            boolean result = false;
            // 如果新增图片返回为null时重试
//            for (int retryTimes = 1; retryTimes <= MAX_RETRY_TIMES; retryTimes++) {
                // 调用京东根据商品Id，销售属性值Id增加图片API(360buy.ware.propimg.add)
                WarePropimgAddResponse response = reqApi(shop, request);

                // response=null时重试
                if (response == null) {
//                    logger.error("调用京东API根据商品Id，销售属性值Id增加图片第(" + retryTimes + "/" + MAX_RETRY_TIMES
//                            + ")次失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
//                            + shop.getCart_id() + ",ware_id:" + request.getWareId() + ",attrValueId:" + attrValueId
//                            + ",pic_url:" + picUrl + ",response=null");
//                    continue;
                    logger.error("调用京东API根据商品Id，销售属性值Id增加图片失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                            + shop.getCart_id() + ",ware_id:" + request.getWareId() + ",attrValueId:" + attrValueId
                            + ",pic_url:" + picUrl + ",response=null");
                    throw new BusinessException("[京东返回应答为空(response = null)]");
                } else {
                    result = true;
                    // 京东返回正常的场合
                    if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                        // 返回上传图片成功,跳出循环，返回true
                        logger.info("调用京东API根据商品Id，销售属性值Id增加图片成功 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                                + shop.getCart_id() + ",ware_id:" + request.getWareId() + ",attrValueId:" + attrValueId
                                + ",pic_url:" + picUrl);
//                        break;
                    } else {
                        // 京东返回失败的场合
                        throw new BusinessException(response.getZhDesc());
                    }
                }
//            }

            if (result) {
                // 返回上传图片成功
                return true;
            }

        } catch (Exception ex) {
            logger.error("调用京东API根据商品Id，销售属性值Id增加图片失败 " + "channel_id:" + shop.getOrder_channel_id()
                    + ",cart_id:" + shop.getCart_id() + ",ware_id:" + request.getWareId() + ",attrValueId:" + attrValueId
                    + ",pic_url:" + picUrl + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("京东根据商品Id，销售属性值Id增加图片失败 " + ex.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }

        logger.error("调用京东API根据商品Id，销售属性值Id增加图片失败 " + "channel_id:" + shop.getOrder_channel_id()
                + ",cart_id:" + shop.getCart_id() + ",ware_id:" + request.getWareId() + ",attrValueId:" + attrValueId
                + ",pic_url:" + picUrl + ",response=null");
        return false;
    }

    /**
     * 获取指定商品上的所有图片列表
     *
     * @param shop ShopBean  店铺信息
     * @param wareId String  京东商品id
     * @return String  指定商品上的所有图片列表
     */
    public List<Image> getImagesByWareId(ShopBean shop, long wareId) throws BusinessException {
        List<Image> imageList = new ArrayList<>();

        ImageReadFindImagesByWareIdRequest request = new ImageReadFindImagesByWareIdRequest();
        // 商品id(必须)
        request.setWareId(wareId);

        try {
            // 调用京东获取商品上的所有图片列表API(jingdong.image.read.findImagesByWareId)
            ImageReadFindImagesByWareIdResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回图片列表
                    imageList = response.getImages();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API获取商品上的所有图片列表异常 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:"
                    + shop.getCart_id() + ",ware_id:" + wareId + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("获取京东获取商品上的所有图片列表失败 " + ex.getMessage());
        }

        return imageList;
    }

    /**
     * 删除指定商品上的所有图片列表
     *
     * @param shop ShopBean  店铺信息
     * @param wareId long  京东商品id
     * @param colorIds String 颜色id数组(例："jingdong,yanfa,pop")
     * @param indexes String 图片位置数组，index值：1-N。如果删除靠前的index，系统会自动把后面的图片自动挪动(例："123,234,345")
     * @return boolean  删除指定商品上的所有图片是否成功
     */
    public boolean deleteImagesByWareId(ShopBean shop, long wareId, String colorIds, String indexes) throws BusinessException {

        ImageWriteDeleteRequest request = new ImageWriteDeleteRequest();
        // 商品id(必须)
        request.setWareId(wareId);
        // 颜色id数组("jingdong,yanfa,pop")(必须)
        request.setColorIds(colorIds);
        // 图片位置数组("123,234,345")(必须)
        request.setImgIndexes(indexes);

        try {
            // 调用京东删除商品图片API(jingdong.image.write.delete)
            ImageWriteDeleteResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回图片删除状态
                    return response.getSuccess();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API删除指定商品上的图片失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",ware_id:" + wareId +
                    ",colorIds:" + colorIds + ",imgIndexes:" + indexes + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("调用京东API删除指定商品上的图片失败 " + ex.getMessage());
        }

        logger.error("调用京东API删除指定商品上的图片失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",ware_id:" + wareId +
                ",colorIds:" + colorIds + ",imgIndexes:" + indexes + ",response=null");
        return false;
    }

    /**
     * 根据商品Id，销售属性值Id删除图片
     *
     * @param shop ShopBean  店铺信息
     * @param wareId long    京东商品id
     * @param attrValueId String 属性值Id(颜色值Id)
     * @param imageId String     图片Id
     * @return boolean  删除指定商品上的所有图片是否成功
     */
    public boolean deleteImagesByImageId(ShopBean shop, long wareId, String attrValueId, String imageId) throws BusinessException {

        WarePropimgDeleteRequest request = new WarePropimgDeleteRequest();
        // 商品id(必须)
        request.setWareId(String.valueOf(wareId));
        // 属性值Id(颜色值Id)(必须)
        request.setAttributeValueId(attrValueId);
        // 图片Id(必须)
        request.setImageId(imageId);

        try {
            // 调用京东删除商品图片API(jingdong.image.write.delete)
            WarePropimgDeleteResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回图片删除成功
                    return true;
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API根据商品Id，销售属性值Id删除图片失败 " + "channel_id:" + shop.getOrder_channel_id() +
                    ",cart_id:" + shop.getCart_id() + ",ware_id:" + wareId + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("根据商品Id，销售属性值Id删除图片失败 " + ex.getMessage());
        }

        logger.error("调用京东API根据商品Id，销售属性值Id删除图片失败 " + "channel_id:" + shop.getOrder_channel_id()
                + ",cart_id:" + shop.getCart_id() + ",ware_id:" + wareId + ",response=null");
        return false;
    }

    /**
     * 保存商品运费模板
     *
     * @param shop ShopBean  店铺信息
     * @param wareId long    京东商品id
     * @param transportId long 运费模板id
     * @return boolean  保存商品运费模板是否成功
     */
    public boolean updateWareTransportId(ShopBean shop, long wareId, long transportId) throws BusinessException {

        TransportWriteUpdateWareTransportIdRequest request = new TransportWriteUpdateWareTransportIdRequest();
        // 商品id(必须)
        request.setWareId(wareId);
        // 运费模板id(必须)
        request.setTransportId(transportId);

        try {
            // 调用京东保存商品运费模板API( jingdong.transport.write.updateWareTransportId)
            TransportWriteUpdateWareTransportIdResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (response.getSuccess()) {
                    return true;
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API设置商品运费模板失败! " + "wareId:" + wareId + ",transportId:" + transportId
                    + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("设置商品运费模板失败 " + ex.getMessage());
        }

        logger.error("调用京东API设置商品运费模板失败! " + "wareId:" + wareId + ",transportId:" + transportId + ",response=null");
        return false;
    }

    /**
     * 设置京东商品关联版式
     *
     * @param shop ShopBean   店铺信息
     * @param wareIds String  京东商品编号集合
     * @param layoutId String 版式id
     * @return boolean  设置关联版式到商品是否成功
     */
    public boolean updateWareLayoutId(ShopBean shop, String wareIds, String layoutId) throws BusinessException {

        WareTemplateToWaresUpdateRequest request = new WareTemplateToWaresUpdateRequest();
        // 版式id（取消商品关联版式时，请将此字段值设置为空）(必须)(例：112312)
        request.setId(layoutId);
        // 商品编号集合，最大不超过20个(必须)(例：123313,12312123)
        request.setWareIds(wareIds);

        try {
            // 调用京东设置关联版式到商品API(360buy.ware.template.to.wares.update)
            WareTemplateToWaresUpdateResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回设置关联版式到商品成功
                    return true;
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API设置商品关联版式失败! " + "版式id:" + layoutId + ",商品编号集合:" + wareIds
                    + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("设置商品关联版式失败 " + ex.getMessage());
        }

        logger.error("调用京东API设置商品关联版式失败! " + "版式id:" + layoutId + ",商品编号集合:" + wareIds + ",response=null");
        return false;
    }

    /**
     * 获取网络图片流，遇错重试
     *
     * @param url   imgUrl
     * @param retry retrycount
     * @return inputStream / throw Exception
     */
    public static InputStream getImgInputStream(String url, int retry) throws BusinessException {
        if (--retry > 0) {
            try {
                return HttpUtils.getInputStream(url, null);
            } catch (Exception e) {
                getImgInputStream(url, retry);
            }
        }
        throw new BusinessException("通过URL取得图片失败. url:" + url);
    }

//    /**
//     * 将图片流转为图片文件
//     *
//     * @param ins InputStream  图片流
//     * @param file File  图片文件（返回值）
//     * @return 无
//     */
//    public static void inputstreamToFile(InputStream ins, File file) {
//        try {
//            OutputStream os = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[8192];
//            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
//                os.write(buffer, 0, bytesRead);
//            }
//            os.close();
//            ins.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 京东增加SKU信息
//     *
//     * @param shop ShopBean      店铺信息
//     * @param jdProduct JdProductBean   京东商品对象
//     * @return String    新增的SKUID
//     */
//    public String addProductSku(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
//        WareSkuAddRequest request = new WareSkuAddRequest();
//
//        // 商品id(必须)
//        request.setWareId("wareid");
//        // Sku属性(100041:150041^1000046:15844)(必须)
//        request.setAttributes(jdProduct.getAttributes());
//        // 京东价格(必须)
//        request.setAttributes(jdProduct.getJdPrice());
//        // 库存(必须)
//        request.setStockNum(jdProduct.getStockNum());
//        // 流水号(非必须)
//        request.setTradeNo(jdProduct.getTradeNo());
//        // sku外部id(非必须)
//        request.setTradeNo(jdProduct.getTradeNo());
//
//        String retSkuId = "";
//
//        try {
//            // 调用京东增加SKU信息API(360buy.ware.sku.add)
//            WareSkuAddResponse response = reqApi(shop, request);
//
//            if (response != null) {
//                // 京东返回正常的场合
//                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
//                    // 返回SKUID
//                    retSkuId = response.getSkuId();
//                }
//            }
//        } catch (Exception ex) {
//            logger.error("调用京东API增加SKU信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",ware_id:" + request.getWareId());
//
//            throw new BusinessException("增加京东SKU信息失败 " + ex.getMessage());
//        }
//
//
//        return retSkuId;
//    }
//
//    /**
//     * 京东修改SKU信息
//     *
//     * @param shop ShopBean      店铺信息
//     * @param jdProduct JdProductBean   京东商品对象
//     * @return String    修改对象
//     */
//    public String updateProductSku(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
//        WareSkuUpdateRequest request = new WareSkuUpdateRequest();
//        // if null 更新的时候，如果属性为null，就不设置，如果是空，就让它改为空
//        // sku的id(必须)
//        request.setSkuId("skuid");
//        // 商品id(必须)
//        request.setWareId("wareid");
//        // 外部id(非必须)
//        request.setOuterId(jdProduct.getOuterId());
//        // 京东价格(必须)
//        request.setAttributes(jdProduct.getJdPrice());
//        // 库存(必须)
//        request.setStockNum(jdProduct.getStockNum());
//        // 流水号(非必须)
//        request.setTradeNo(jdProduct.getTradeNo());
//
//        String retModified = "";
//
//        try {
//            // 调用京东修改SKU信息API( 360buy.ware.sku.update)
//            WareSkuUpdateResponse response = reqApi(shop, request);
//
//            if (response != null) {
//                // 京东返回正常的场合
//                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
//                    // 返回SKU修改状态
//                    retModified = response.getModified();
//                }
//            }
//        } catch (Exception ex) {
//            logger.error("调用京东API修改SKU信息失败 " + "SKU id:" + request.getSkuId() + ",商品id:" + request.getWareId());
//
//            throw new BusinessException("修改京东SKU信息失败 " + ex.getMessage());
//        }
//
//
//        return retModified;
//    }
//
//    /**
//     * 京东上传单张图片
//     *
//     * @param shop ShopBean      店铺信息
//     * @param jdProduct JdProductBean   京东商品对象
//     * @return String    新增的图片ID
//     */
//    public String uploadPicture(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
//        ImgzonePictureUploadRequest request = new ImgzonePictureUploadRequest();
//
//        // 图片二进制文件流，允许png、jpg、gif、jpeg、bmp图片格式，1M以内(必须)
////        byte[] bytes = IOUtils.toByteArray(inputStream);("byte[] imagedata")\
////        if (charset != null) {
////            request.setImageData(jdProduct.getAttributes().getBytes(charset));
////        } else {
////            request.setImageData(jdProduct.getAttributes().getBytes());
////        }
//        request.setImageData(jdProduct.getAttributes().getBytes());
//        // 上传到的图片分类ID，为空上传至 默认分类(非必须)
//        request.setPictureCateId((long)0);
//        // 图片名称，不超过64字节，为空默认 未命名(非必须)
//        request.setPictureName("图片名称");
//
//        String retPictureId = "";
//
//        try {
//            // 调用京东图片空间API上传单张图片(jingdong.imgzone.picture.upload)
//            ImgzonePictureUploadResponse response = reqApi(shop, request);
//
//            if (response != null) {
//                // 京东返回正常的场合(返回码：1，操作成功)
//                if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_OK.equals(response.getReturnCode())) {
//                    // 返回SKUID
//                    retPictureId = response.getPictureId();
//                } else if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_NG.equals(response.getReturnCode())) {
//                    // 操作失败原因
//                    logger.error("调用京东图片空间API上传单张图片信息失败 图片名称：" + request.getPictureName() + "失败原因:" + response.getDesc());
//                }
//            }
//        } catch (Exception ex) {
//            logger.error("调用京东图片空间API上传单张图片信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());
//
//            throw new BusinessException("京东上传单张图片信息失败 " + ex.getMessage());
//        }
//
//        return retPictureId;
//    }
//
//    /**
//     * 京东替换单张图片
//     *
//     * @param shop ShopBean      店铺信息
//     * @param jdProduct JdProductBean   京东商品对象
//     * @return String    替换图片操作返回码(1，操作成功；0，操作失败)
//     */
//    public String replacePicture(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
//        ImgzonePictureReplaceRequest request = new ImgzonePictureReplaceRequest();
//
//        // 图片ID(必须)
//        request.setPictureId("图片id");
//        // 图片二进制文件流，允许png、jpg、gif、jpeg、bmp图片格式，1M以内(必须)
//        request.setImageData(jdProduct.getAttributes().getBytes());
//
//        // 返回码
//        String retReturnCode = "";
//
//        try {
//            // 调用京东图片空间API上传单张图片(jingdong.imgzone.picture.upload)
//            ImgzonePictureReplaceResponse response = reqApi(shop, request);
//
//            if (response != null) {
//                // 京东返回正常的场合(返回码：1，操作成功)
//                if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_OK.equals(response.getReturnCode())) {
//                    // 返回码
//                    retReturnCode = String.valueOf(response.getReturnCode());
//                } else if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_NG.equals(response.getReturnCode())) {
//                    // 返回码
//                    retReturnCode = String.valueOf(response.getReturnCode());
//                    // 操作失败原因
//                    logger.error("调用京东图片空间API替换单张图片信息失败 图片ID：" + request.getPictureId() + "失败原因:" + response.getDesc());
//                }
//            }
//        } catch (Exception ex) {
//            logger.error("调用京东图片空间API替换单张图片信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",picture_id:" + request.getPictureId());
//
//            throw new BusinessException("京东替换单张图片信息失败 " + ex.getMessage());
//        }
//
//        return retReturnCode;
//    }

    /**
     * 取得商品的类目id
     * 虽然API参数wareIds支持一次传多个商品，但本方法只传一个
     * 暂时做成，如果API调用失败等错误发生，不throw，只返回null
     *
     * @param shop
     * @param wareId 商品id(单个)
     * @return 类目id
     */
    public String getJdProductCatId(ShopBean shop, String wareId) {
        String catId = null;
        String field = "cid";
        WareListRequest request = new WareListRequest ();
        request.setWareIds(wareId);
        request.setFields(field);

        try {
            WareListResponse response = reqApi(shop, request);
            if ("0".equals(response.getCode())) {
                // API调用成功
                if (response.getWareList() != null && response.getWareList().size() > 0) {
                    catId = String.valueOf(response.getWareList().get(0).getCategoryId());
                }
            } else {
                logger.error("调用京东API取得商品信息失败[wareId:" + wareId + "]! " + response.getZhDesc());
            }

        } catch (Exception e) {
            logger.error("调用京东API取得商品信息失败[wareId:" + wareId + "]! ");
        }

        return catId;
    }
    public WareListResponse getJdProduct(ShopBean shop, String wareId, String field) {
        String catId = null;
        WareListRequest request = new WareListRequest ();
        request.setWareIds(wareId);
        request.setFields(field);

        try {
            WareListResponse response = reqApi(shop, request);
            if ("0".equals(response.getCode())) {
                return response;
            } else {
                logger.error("调用京东API取得商品信息失败[wareId:" + wareId + "]! " + response.getZhDesc());
            }

        } catch (Exception e) {
            logger.error("调用京东API取得商品信息失败[wareId:" + wareId + "]! ");
        }
        return null;
    }

    /**
     * 商品打标(设置商品特殊属性feature，例如7天无理由退货等)
     * featureKey和featureValue具体的值，可以在京东后台设置好了该属性之后，取得商品属性中的feature看到
     * 一次最多能同时打标的个数为20
     *
     * @param shop ShopBean   店铺信息
     * @param wareId Long  京东商品编号
     * @param featureKeys String 特殊标属性key(最大不超过20个,多个用逗号分隔)
     * @param featureValues String 特殊标属性值(最大不超过20个,多个用逗号分隔)
     * @return boolean  设置关联版式到商品是否成功
     */
    public boolean mergeWareFeatures(ShopBean shop, Long wareId, String featureKeys, String featureValues) {

        WareWriteMergeWareFeaturesRequest request = new WareWriteMergeWareFeaturesRequest();
        // 商品编号
        request.setWareId(wareId);
        // 特殊标属性key，最大不超过20个(非必须)(例：is7ToReturn,12312123)
        request.setFeatureKey(featureKeys);
        // 特殊标属性值，最大不超过20个(非必须)(例：1,12312124)
        request.setFeatureValue(featureValues);

        try {
            // 调用京东商品打标API(jingdong.ware.write.mergeWareFeatures)
            WareWriteMergeWareFeaturesResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回京东商品打标成功
                    return true;
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API设置商品打标失败! 商品编号:" + wareId + ",errorMsg:" + ex.getMessage());

            throw new BusinessException("设置商品打标失败 " + ex.getMessage());
        }

        logger.error("调用京东API设置商品打标失败! 商品编号:" + wareId + ",response=null");
        return false;
    }

    /**
     * 获取商品详细信息
     *
     * @param shop 店铺信息
     * @param wareId 京东商品编号
     * @param fields 需返回的字段列表。可选值：ware结构体中的所有字段；字段之间用,分隔
     * @return Ware  商品详细信息
     */
    public com.jd.open.api.sdk.domain.ware.Ware getWareInfo(ShopBean shop, String wareId, String fields) {

        WareGetRequest request = new WareGetRequest();
        // 商品编号
        request.setWareId(wareId);
        // 需返回的字段列表
        request.setFields(fields);

        try {
            // 调用"根据商品ID查询单个商品的详细信息"API(360buy.ware.get)
            WareGetResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    return response.getWare();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                throw new BusinessException("获取商品详细信息失败(response = null)");
            }
        } catch (Exception ex) {
            logger.error("调用京东API获取商品详细信息失败! 商品编号:" + wareId + ",errorMsg:" + ex.getMessage());
            throw new BusinessException("获取商品详细信息失败! " + ex.getMessage());
        }
    }

    /**
     * 全量保存SKU信息
     * 包含每个SKU对应的颜色别名，尺码别名，库存，京东价格，商家编码skuCode等信息
     *
     * @param shop    店铺信息
     * @param wareId  京东商品id
     * @param skuList 商品全量SKU信息列表
     * @param failCause 返回错误用
     */
    public List<com.jd.open.api.sdk.domain.ware.Sku> saveWareSkus(ShopBean shop, Long wareId, List<Sku> skuList, StringBuilder failCause) {

        SkuWriteSaveWareSkusRequest request = new SkuWriteSaveWareSkusRequest();
        // 京东商品Id(必须)
        request.setWareId(wareId);
        // 商品全量SKU信息列表(必须)
        request.setSkus(skuList);

        try {
            // 调用京东全量保存SKU信息API(jingdong.sku.write.saveWareSkus)
            SkuWriteSaveWareSkusResponse response = reqApi(shop, request);
            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    return response.getSkuList();
                } else {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getMsg());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东全量保存SKU信息API返回应答为空(response = null)");
            }
        } catch (Exception e) {
            String errMsg = String.format(shop.getShop_name() + "京东全量保存SKU信息API失败! [channelId:%s] [cartId:%s] [jdSkuId:%s] " +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), StringUtils.toString(wareId), e.getMessage());
            logger.error(errMsg);
            failCause.append(errMsg);
        }

        return null;
    }

    /**
     * 更新商品维度的销售属性值别名
     * 主要用于更新每个SKU对应的颜色和尺码别名信息
     *
     * @param shop    店铺信息
     * @param wareId  京东商品id
     * @param saleProps 商品维度的销售属性值别名列表(颜色和尺码别名)
     * @param failCause 返回错误用
     */
    public boolean updateWareSaleAttrvalueAlias(ShopBean shop, Long wareId, Set<Prop> saleProps, StringBuilder failCause) {

        WareWriteUpdateWareSaleAttrvalueAliasRequest request = new WareWriteUpdateWareSaleAttrvalueAliasRequest();
        // 京东商品Id(必须)
        request.setWareId(wareId);
        // 商品维度的销售属性值别名列表(必须)
        request.setProps(saleProps);

        try {
            // 调用京东更新商品维度的销售属性值别名API(jingdong.ware.write.updateWareSaleAttrvalueAlias)
            WareWriteUpdateWareSaleAttrvalueAliasResponse response = reqApi(shop, request);
            if (response != null) {
                // 京东返回正常的场合
                if (!JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 京东返回失败的场合
                    throw new BusinessException(response.getMsg());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东更新商品维度的销售属性值别名API返回应答为空(response = null)");
            }
        } catch (Exception e) {
            String errMsg = String.format(shop.getShop_name() + "调用京东更新商品维度的销售属性值别名API失败! [channelId:%s] [cartId:%s] [jdSkuId:%s] " +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), StringUtils.toString(wareId), e.getMessage());
            logger.error(errMsg);
            failCause.append(errMsg);
            return false;
        }

        return true;
    }

    /**
     * 京东商品图片更新时用jingdong.image.write.update进行全量设置
     * @param shop  店铺信息
     * @param jdWareId  京东wareId
     * @param colorList 商品销售属性颜色集
     * @param urlList  商品图片集
     * @param indexList  图片位置
     * @return ImageWriteUpdateResponse
     */
    public ImageWriteUpdateResponse imageWriteUpdate(ShopBean shop, Long jdWareId, List<String> colorList,
                                                     List<String> urlList, List<String> indexList) {
        ImageWriteUpdateRequest imageWriteUpdateRequest = new ImageWriteUpdateRequest();

        imageWriteUpdateRequest.setWareId(jdWareId);
        imageWriteUpdateRequest.setColorId(StringUtils.join(colorList, ","));
        imageWriteUpdateRequest.setImgUrl(StringUtils.join(urlList, ","));
        imageWriteUpdateRequest.setImgIndex(StringUtils.join(indexList, ","));

        ImageWriteUpdateResponse response = reqApi(shop, imageWriteUpdateRequest);

        return response;
    }
}
