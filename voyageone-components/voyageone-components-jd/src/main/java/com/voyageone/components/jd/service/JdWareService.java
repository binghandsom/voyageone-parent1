package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.request.imgzone.ImgzonePictureReplaceRequest;
import com.jd.open.api.sdk.request.imgzone.ImgzonePictureUploadRequest;
import com.jd.open.api.sdk.request.ware.WareAddRequest;
import com.jd.open.api.sdk.request.ware.WareSkuAddRequest;
import com.jd.open.api.sdk.request.ware.WareSkuUpdateRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateRequest;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureReplaceResponse;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureUploadResponse;
import com.jd.open.api.sdk.response.ware.WareAddResponse;
import com.jd.open.api.sdk.response.ware.WareSkuAddResponse;
import com.jd.open.api.sdk.response.ware.WareSkuUpdateResponse;
import com.jd.open.api.sdk.response.ware.WareUpdateResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import com.voyageone.components.jd.bean.JdProductBean;
import org.springframework.stereotype.Component;

/**
 * 京东商品类API调用服务
 * <p/>
 * Created by desmond on 2016/4/12.
 */
@Component
public class JdWareService extends JdBase {

    // 上新重试次数
    private static final int MAX_RETRY_TIMES = 3;

	/**
     * 京东新增商品
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return Long    京东商品ID
     */
    public Long addProduct(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
        WareAddRequest request = new WareAddRequest();

        // 流水号(非必须)
        request.setTradeNo(jdProduct.getTradeNo());
        // 产地(非必须)
        request.setWareLocation(jdProduct.getWareLocation());
        // 类目id(必须)
        request.setCid(jdProduct.getCid());
        // 商品标题(必须)
        request.setTitle(jdProduct.getTitle());
        // UPC编码(非必须)
        request.setUpcCode(jdProduct.getUpcCode());
        // 操作类型 现只支持：offsale 或onsale,默认为下架状态 (非必须)
        request.setOptionType(jdProduct.getOptionType());
        // 外部商品编号，对应商家后台货号(非必须)
        request.setItemNum(jdProduct.getItemNum());
        // 库存(必须)
        request.setStockNum(jdProduct.getStockNum());
        // 生产厂商(非必须)
        request.setProducter(jdProduct.getProducter());
        // 包装规格 (非必须)
        request.setWrap(jdProduct.getWrap());
        // 长(单位:mm)(必须)
        request.setLength(jdProduct.getLength());
        // 宽(单位:mm)(必须)
        request.setWide(jdProduct.getWide());
        // 高(单位:mm)(必须)
        request.setHigh(jdProduct.getHigh());
        // 重量(单位:kg)(必须)
        request.setWeight(jdProduct.getWeight());
        // 进货价,精确到2位小数，单位:元(非必须)
        request.setCostPrice(jdProduct.getCostPrice());
        // 市场价, 精确到2位小数，单位:元(必须)
        request.setJdPrice(jdProduct.getJdPrice());
        // 京东价,精确到2位小数，单位:元(必须)
        request.setMarketPrice(jdProduct.getMarketPrice());
        // 描述（最多支持3万个英文字符(必须)
        request.setNotes(jdProduct.getNotes());
        // 图片信息（图片尺寸为800*800，单张大小不超过 1024K）(必须)
        request.setWareImage(jdProduct.getWareImage());
        // 包装清单(非必须)
        request.setPackListing(jdProduct.getPackListing());
        // 售后服务(非必须)
        request.setService(jdProduct.getService());
        // sku属性,一组sku 属性之间用^分隔，多组用|分隔格式(非必须)
        request.setSkuProperties(jdProduct.getSkuProperties());
        // 商品属性列表,多组之间用|分隔，格式:aid:vid 或 aid:vid|aid1:vid1 或 aid1:vid1(必须)
        request.setAttributes(jdProduct.getAttributes());
        // sku价格,多组之间用‘|’分隔，格式:p1|p2 (非必须)
        request.setSkuPrices(jdProduct.getSkuPrices());
        // sku 库存,多组之间用‘|’分隔， 格式:s1|s2(非必须)
        request.setSkuStocks(jdProduct.getSkuStocks());
        // 自定义属性值别名：属性ID:属性值ID:别名(非必须)
        request.setPropertyAlias(jdProduct.getPropertyAlias());
        // SKU外部ID，对个之间用‘|’分隔格(非必须)
        request.setOuterId(jdProduct.getOuterId());
        // 是否先款后货,false为否，true为是 (非必须)
        request.setPayFirst(jdProduct.getPayFirst());
        // 发票限制：非必须输入，true为限制，false为不限制开增值税发票，FBP、LBP、SOPL、SOP类型商品均可输入(非必须)
        request.setCanVAT(jdProduct.getCanVAT());
        // 是否进口商品：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
        request.setImported(jdProduct.getImported());
        // 是否保健品：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
        request.setHealthProduct(jdProduct.getHealthProduct());
        // 是否保质期管理商品, false为否，true为是(非必须)
        request.setShelfLife(jdProduct.getShelfLife());
        // 保质期：非必须输入，0-99999范围区间，FBP类型商品可输入(非必须)
        request.setShelfLifeDays(jdProduct.getShelfLifeDays());
        // 是否序列号管理：非必须输入，false为否，true为是，FBP类型商品可输入(非必须)
        request.setSerialNo(jdProduct.getSerialNo());
        // 大家电购物卡：非必须输入，false为否，true为是，FBP类型商品可输入  (非必须)
        request.setAppliancesCard(jdProduct.getAppliancesCard());
        // 是否特殊液体：非必须输入，false为否，true为是，FBP、LBP、SOPL类型商品可输入(非必须)
        request.setSpecialWet(jdProduct.getSpecialWet());
        // 商品件型：FBP类型商品必须输入，非FBP类型商品可输入非必填，0免费、1超大件、2超大件半件、3大件、4大件半件、5中件、6中件半件、7小件、8超小件(必须)
        request.setWareBigSmallModel(jdProduct.getWareBigSmallModel());
        // 商品包装：FBP类型商品必须输入，非FBP类型商品可输入非必填，1普通商品、2易碎品、3裸瓶液体、4带包装液体、5按原包装出库(必须)
        request.setWarePackType(jdProduct.getWarePackType());
        // 用户自行输入的类目属性ID串结构：‘pid1|pid2|pid3’,属性的pid调用360buy.ware.get.attribute取得, 输入类型input_type=3即输入(非必须)
        request.setInputPids(jdProduct.getInputPids());
        // 用户自行输入的属性值,结构:‘输入值|输入值2|输入值3’图书品类输入值规则： ISBN：数字、字母格式 出版时间：日期格式“yyyy-mm-dd”
        // 版次：数字格式 印刷时间：日期格式“yyyy-mm-dd” 印次：数字格式 页数：数字格式 字数：数字格式 套装数量：数字格式 附件数量：数字格式(非必须)
        request.setInputStrs(jdProduct.getInputStrs());
        // 是否输入验证码 true:是;false:否  (非必须)
        request.setHasCheckCode(jdProduct.getHasCheckCode());
        // 广告词内容最大支持45个字符(非必须)
        request.setAdContent(jdProduct.getAdContent());
        // 定时上架时间 时间格式：yyyy-MM-dd HH:mm:ss;规则是大于当前时间，10天内。(非必须)
        request.setListTime(jdProduct.getListTime());

        long wareId = 0;

        try {
            // 调用京东新增商品API(360buy.ware.add)
            WareAddResponse response = reqApi(shop, request, this.MAX_RETRY_TIMES);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回商品ID
                    wareId = response.getWareId();
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API新增京东商品信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

            throw new BusinessException(shop.getShop_name() + "新增京东商品信息失败 " + ex.getMessage());
        }


        return wareId;
    }

    /**
     * 京东修改商品
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return String    修改对象
     */
    public String updateProduct(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
        WareUpdateRequest request = new WareUpdateRequest();

        // if null 更新的时候，如果属性为null，就不设置，如果是空，就让它改为空
        request.setWeight(jdProduct.getWeight());
        request.setCostPrice(jdProduct.getCostPrice());
        // ..............
        // ..............
        // ..............
        // ..............
        // ..............
        // ..............
        request.setCanVAT(jdProduct.getCanVAT());

        String retModified = "";

        try {
            // 调用京东修改商品API(360buy.ware.update)
            WareUpdateResponse response = reqApi(shop, request, this.MAX_RETRY_TIMES);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回商品修改状态
                    retModified = response.getModified();
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API修改商品信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",ware_id:" + request.getWareId());

            throw new BusinessException(shop.getShop_name() + "修改京东商品信息失败 " + ex.getMessage());
        }

        return retModified;
    }

    /**
     * 京东增加SKU信息
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return String    新增的SKUID
     */
    public String addProductSku(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
        WareSkuAddRequest request = new WareSkuAddRequest();

        // 商品id(必须)
        request.setWareId("wareid");
        // Sku属性(100041:150041^1000046:15844)(必须)
        request.setAttributes(jdProduct.getAttributes());
        // 京东价格(必须)
        request.setAttributes(jdProduct.getJdPrice());
        // 库存(必须)
        request.setStockNum(jdProduct.getStockNum());
        // 流水号(非必须)
        request.setTradeNo(jdProduct.getTradeNo());
        // sku外部id(非必须)
        request.setTradeNo(jdProduct.getTradeNo());

        String retSkuId = "";

        try {
            // 调用京东增加SKU信息API(360buy.ware.sku.add)
            WareSkuAddResponse response = reqApi(shop, request, this.MAX_RETRY_TIMES);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回SKUID
                    retSkuId = response.getSkuId();
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API增加SKU信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",ware_id:" + request.getWareId());

            throw new BusinessException(shop.getShop_name() + "增加京东SKU信息失败 " + ex.getMessage());
        }


        return retSkuId;
    }

    /**
     * 京东修改SKU信息
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return String    修改对象
     */
    public String updateProductSku(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
        WareSkuUpdateRequest request = new WareSkuUpdateRequest();
        // if null 更新的时候，如果属性为null，就不设置，如果是空，就让它改为空
        // sku的id(必须)
        request.setSkuId("skuid");
        // 商品id(必须)
        request.setWareId("wareid");
        // 外部id(非必须)
        request.setOuterId(jdProduct.getOuterId());
        // 京东价格(必须)
        request.setAttributes(jdProduct.getJdPrice());
        // 库存(必须)
        request.setStockNum(jdProduct.getStockNum());
        // 流水号(非必须)
        request.setTradeNo(jdProduct.getTradeNo());

        String retModified = "";

        try {
            // 调用京东修改SKU信息API( 360buy.ware.sku.update)
            WareSkuUpdateResponse response = reqApi(shop, request, this.MAX_RETRY_TIMES);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回SKU修改状态
                    retModified = response.getModified();
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东API修改SKU信息失败 " + "SKU id:" + request.getSkuId() + ",商品id:" + request.getWareId());

            throw new BusinessException(shop.getShop_name() + "修改京东SKU信息失败 " + ex.getMessage());
        }


        return retModified;
    }

    /**
     * 京东上传单张图片
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return String    新增的图片ID
     */
    public String uploadPicture(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
        ImgzonePictureUploadRequest request = new ImgzonePictureUploadRequest();

        // 图片二进制文件流，允许png、jpg、gif、jpeg、bmp图片格式，1M以内(必须)
//        byte[] bytes = IOUtils.toByteArray(inputStream);("byte[] imagedata")\
//        if (charset != null) {
//            request.setImageData(jdProduct.getAttributes().getBytes(charset));
//        } else {
//            request.setImageData(jdProduct.getAttributes().getBytes());
//        }
        request.setImageData(jdProduct.getAttributes().getBytes());
        // 上传到的图片分类ID，为空上传至 默认分类(非必须)
        request.setPictureCateId((long)0);
        // 图片名称，不超过64字节，为空默认 未命名(非必须)
        request.setPictureName("图片名称");

        String retPictureId = "";

        try {
            // 调用京东图片空间API上传单张图片(jingdong.imgzone.picture.upload)
            ImgzonePictureUploadResponse response = reqApi(shop, request, this.MAX_RETRY_TIMES);

            if (response != null) {
                // 京东返回正常的场合(返回码：1，操作成功)
                if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_OK.equals(response.getReturnCode())) {
                    // 返回SKUID
                    retPictureId = response.getPictureId();
                } else if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_NG.equals(response.getReturnCode())) {
                    // 操作失败原因
                    logger.error("调用京东图片空间API上传单张图片信息失败 图片名称：" + request.getPictureName() + "失败原因:" + response.getDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东图片空间API上传单张图片信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id());

            throw new BusinessException(shop.getShop_name() + "京东上传单张图片信息失败 " + ex.getMessage());
        }

        return retPictureId;
    }

    /**
     * 京东替换单张图片
     *
     * @param shop ShopBean      店铺信息
     * @param jdProduct JdProductBean   京东商品对象
     * @return String    替换图片操作返回码(1，操作成功；0，操作失败)
     */
    public String replacePicture(ShopBean shop, JdProductBean jdProduct) throws BusinessException {
        ImgzonePictureReplaceRequest request = new ImgzonePictureReplaceRequest();

        // 图片ID(必须)
        request.setPictureId("图片id");
        // 图片二进制文件流，允许png、jpg、gif、jpeg、bmp图片格式，1M以内(必须)
        request.setImageData(jdProduct.getAttributes().getBytes());

        // 返回码
        String retReturnCode = "";

        try {
            // 调用京东图片空间API上传单张图片(jingdong.imgzone.picture.upload)
            ImgzonePictureReplaceResponse response = reqApi(shop, request, this.MAX_RETRY_TIMES);

            if (response != null) {
                // 京东返回正常的场合(返回码：1，操作成功)
                if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_OK.equals(response.getReturnCode())) {
                    // 返回码
                    retReturnCode = String.valueOf(response.getReturnCode());
                } else if (JdConstants.C_JD_IMGZONE_RETURN_SUCCESS_NG.equals(response.getReturnCode())) {
                    // 返回码
                    retReturnCode = String.valueOf(response.getReturnCode());
                    // 操作失败原因
                    logger.error("调用京东图片空间API替换单张图片信息失败 图片ID：" + request.getPictureId() + "失败原因:" + response.getDesc());
                }
            }
        } catch (Exception ex) {
            logger.error("调用京东图片空间API替换单张图片信息失败 " + "channel_id:" + shop.getOrder_channel_id() + ",cart_id:" + shop.getCart_id() + ",picture_id:" + request.getPictureId());

            throw new BusinessException(shop.getShop_name() + "京东替换单张图片信息失败 " + ex.getMessage());
        }

        return retReturnCode;
    }
}
