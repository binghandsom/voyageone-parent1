package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.request.promotion.SellerPromotionCommitRequest;
import com.jd.open.api.sdk.request.promotion.SellerPromotionGetRequest;
import com.jd.open.api.sdk.request.promotion.SellerPromotionSkuAddRequest;
import com.jd.open.api.sdk.request.promotion.SellerPromotionSkuListRequest;
import com.jd.open.api.sdk.response.promotion.*;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.JdBase;
import com.voyageone.components.jd.JdConstants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 京东促销相关API调用服务
 * <p/>
 * @author desmond on 2016/11/16.
 * @version 2.8.0
 * @since 2.8.0
 */
@Component
public class JdPromotionService extends JdBase {

    /**
     * 添加sku到京东促销（必须是价格促销）(jingdong.seller.promotion.sku.add)
     *
     * 添加参加促销的sku，单次最多添加100个SKU，一个促销最多支持1000个SKU，当基于套装促销添加SKU时，
     * 最多可设置3个商品的SKU，并且相同商品的次序要一致；
     * 当基于赠品促销添加SKU时，赠品SKU只能是1-5个，每个赠品只能赠送1-3个，赠品的总价应低于主商品中的最小京东价。
     *
     * @param shop        ShopBean  店铺信息
     * @param jdSkuIds    String    京东skuId(多个用","逗号分隔，单次最多添加100个SKU)
     * @param jdPrices    String    京东价(以元为单位，最高可精确到小数点后两位,多个用","逗号分隔，次序必须与jdSkuIds保持一致)
     * @param promoPrices String    促销价(以元为单位，精确到小数点后一位,多个用","逗号分隔，次序必须与jdSkuIds保持一致)
     * @return String               修改时间
     */
    public List<Long> addSkuIdToPromotion(ShopBean shop, Long promoId, String jdSkuIds, String jdPrices, String promoPrices) {

        return addSkuIdToPromotion(shop, promoId, jdSkuIds, jdPrices, promoPrices, null, null, null);
    }

    /**
     * 添加sku到京东促销（必须是价格促销）(jingdong.seller.promotion.sku.add)
     *
     * 添加参加促销的sku，单次最多添加100个SKU，一个促销最多支持1000个SKU，当基于套装促销添加SKU时，
     * 最多可设置3个商品的SKU，并且相同商品的次序要一致；
     * 当基于赠品促销添加SKU时，赠品SKU只能是1-5个，每个赠品只能赠送1-3个，赠品的总价应低于主商品中的最小京东价。
     *
     * @param shop        ShopBean  店铺信息
     * @param jdSkuIds    String    京东skuId(多个用","逗号分隔，单次最多添加100个SKU)
     * @param jdPrices    String    京东价(以元为单位，最高可精确到小数点后两位,多个用","逗号分隔，次序必须与jdSkuIds保持一致)
     * @param promoPrices String    促销价(以元为单位，精确到小数点后一位,多个用","逗号分隔，次序必须与jdSkuIds保持一致)
     * @return List<Long>           促销SKU编号(jdSkuId)列表，返回的SKU列表是都参加促销的SKU，无效的SKU会被剔除
     */
    public List<Long> addSkuIdToPromotion(ShopBean shop, Long promoId, String jdSkuIds, String jdPrices, String promoPrices,
                                          String seq, String num, String bindType) {

        SellerPromotionSkuAddRequest request = new SellerPromotionSkuAddRequest();
        // 促销编号(一个促销编号最多支持1000个SKU)
        if (promoId != null)                    request.setPromoId(promoId);
        // 京东skuId集合(多个用","逗号分隔，单次最多添加100个SKU,超过100个请在外面分批传进来)
        if (!StringUtils.isEmpty(jdSkuIds))     request.setSkuIds(jdSkuIds);
        // 京东价，以元为单位，最高可精确到小数点后两位(多个用","逗号分隔，京东价的次序必须跟jdSkuIds保持一致)
        if (!StringUtils.isEmpty(jdPrices))     request.setJdPrices(jdPrices);
        // 促销价，以元为单位，精确到小数点后一位，且必须小于京东价(多个用","逗号分隔，促销价的次序必须跟jdSkuIds保持一致)
        if (!StringUtils.isEmpty(promoPrices))  request.setPromoPrices(promoPrices);
        // 套装商品展示次序，相同商品的SKU上次序必须一致，次序必须是1到7之间的自然数。（只对套装促销有效）
        if (!StringUtils.isEmpty(seq))          request.setSeq(seq);
        // 赠品赠送数量，只能送1-3个。(只对赠品促销有效)
        if (!StringUtils.isEmpty(num))          request.setNum(num);
        // 绑定类型, 可选值：主商品（1），赠品（2）。(赠品促销、满减送促销中的赠品需要设置为2，其余均设置为1)
        if (!StringUtils.isEmpty(bindType))     request.setBindType(bindType);

        try {
            // 调用京东添加参加促销的skuAPI(jingdong.seller.promotion.sku.add)
            SellerPromotionSkuAddResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回更新时间
                    return response.getIds();
                } else {
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东添加sku到京东促销API返回应答为空(response = null)");
            }
        } catch (Exception ex) {
            String errMsg = String.format(shop.getShop_name() + "调用京东API添加sku到京东促销失败! [channelId:%s] [cartId:%s] [promotionId:%s] [jdSkuIds:%s] " +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), promoId, jdSkuIds, ex.getMessage());
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

    /**
     * 根据促销编号获取促销详细信息(jingdong.seller.promotion.get)
     *
     * 返回值"京东促销详细信息(PromotionVO)"的项目说明：
     * name :促销名称，字符串长度小于等于10
     * type :促销类型，可选值：单品促销（1），赠品促销（4），套装促销（6），总价促销（10）
     * bound:促销范围，总价促销为必填项，其它促销类型无效，可选值：部分商品参加（1）、全场参加（2）、部分商品不参加（3），注：M元任选N件只支持部分商品参加
     * begin_time:促销开始时间，格式为yyyy-MM-dd HH:mm:ss，精确到分钟，最长可设置为距当前时间180天之内的时间点
     * end_time:促销结束时间，格式为yyyy-MM-dd HH:mm:ss，精确到分钟，必须大于开始时间至少一分钟，且晚于当前时间，建议至少晚10分钟，且和开始时间最大间隔不能超过180天
     * member:会员限制，默认值：注册会员（50），可选值：注册会员（50）、铜牌（56）、银牌（61）、金牌（62）、钻石（105）、VIP（110）
     * slogan:广告语，字符串长度小于等于50
     * comment:活动备注，不超过200字节
     * status:促销状态，可选值：驳回（1），未审核（2），人工审核（3），审核通过（4），已生效（5），已暂停（6），强制暂停（7）
     *        未commit(-1):"3小时之内可以追加skuId，超过3小时追加skuId或者追加了skuId但未commit都会被京东后台视为无效促销而删除"
     * favor_mode:促销类型，满赠（0），满减（1），每满减（2），满赠加价购（5），满M件减N件（6），阶梯买M件减N件（7），M元任选N件（13），M件N折（15），满减送（元）（16），满减送（件）（17）
     *
     * @param shop        ShopBean  店铺信息
     * @param promoId     Long      促销编号
     * @return PromotionVO          京东促销详细信息，包括创建时所有输入的促销信息
     */
    public PromotionVO getPromotionInfo(ShopBean shop, Long promoId) {

        if (promoId == null || promoId == 0L) {
            String errMsg = String.format(shop.getShop_name() + "调用京东API取得促销编号获取促销详细信息失败(促销编号为空)! " +
                    "[channelId:%s] [cartId:%s] [promotionId:%s]", shop.getOrder_channel_id(), shop.getCart_id(), promoId);
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }

        SellerPromotionGetRequest request = new SellerPromotionGetRequest();
        // 促销编号(必须)
        request.setPromoId(promoId);

        try {
            // 调用京东根据促销编号获取促销详细信息API(jingdong.seller.promotion.get)
            SellerPromotionGetResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回更新时间
                    return response.getPromotionVO();
                } else {
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东取得促销编号获取促销详细信息API返回应答为空(response = null)");
            }
        } catch (Exception ex) {
            String errMsg = String.format(shop.getShop_name() + "调用京东API取得促销编号获取促销详细信息失败! [channelId:%s] [cartId:%s] [promotionId:%s]" +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), promoId, ex.getMessage());
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

    /**
     * 促销sku列表全量查询(jingdong.seller.promotion.sku.list)
     *
     * @param shop        ShopBean  店铺信息
     * @param promoId     Long      促销编号
     * @return PromotionVO          京东促销详细信息，包括创建时所有输入的促销信息
     */
    public List<PromoSkuVO> getPromotionSkuListAllById(ShopBean shop, Long promoId) {
        return getPromotionSkuListAll(shop, null, null, promoId, null, null);
    }

    /**
     * 促销sku列表全量查询(jingdong.seller.promotion.sku.list)
     *
     * @param shop        ShopBean  店铺信息
     * @param promoId     Long      促销编号
     * @return PromotionVO          京东促销详细信息，包括创建时所有输入的促销信息
     */
    public List<PromoSkuVO> getPromotionSkuListAll(ShopBean shop, Long wareId, Long jdSkuId, Long promoId, Integer bindType, Integer size) {

        if (size == null || size == 0) size = 10; // 默认为每次查询10个促销SKU

        int totalSkuCnt = 0;
        List<PromoSkuVO> promoSkuVOList = new ArrayList<>();

        try {
            // 第一次先查询一次，取得促销中SKU总数(最多返回10个)
            SellerPromotionSkuListResponse response = getPromotionSkuListPage(shop, wareId, jdSkuId, promoId, bindType, 1, size);
            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 取得促销中SKU总件数
                    totalSkuCnt = response.getTotalCount();
                    // 取得第一页的所有SKU信息
                    if (totalSkuCnt > 0 && ListUtils.notNull(response.getPromoSkuVOS())) {
                        promoSkuVOList.addAll(response.getPromoSkuVOS());
                    }
                } else {
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东取得促销sku列表查询(最多返回10个)API返回应答为空(response = null)");
            }
        } catch (Exception ex) {
            String errMsg = String.format(shop.getShop_name() + "调用京东API取得促销sku列表查询(最多返回10个)失败! [channelId:%s] [cartId:%s] [promotionId:%s]" +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), promoId, ex.getMessage());
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }

        // 如果促销中的SKU总件数>每页记录个数，则还要再循环去取得后面页的SKU列表
        if (totalSkuCnt > size) {
            int retryPage = (totalSkuCnt%size == 0) ? (totalSkuCnt/size) : (totalSkuCnt/size+1);
            for (int page = 2; page <= retryPage; page++) {
                try {
                    SellerPromotionSkuListResponse pageResponse = getPromotionSkuListPage(shop, wareId, jdSkuId, promoId, bindType, page, size);
                    if (pageResponse != null) {
                        // 京东返回正常的场合
                        if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(pageResponse.getCode())) {
                            // 取得当前页的所有SKU信息
                            if (ListUtils.notNull(pageResponse.getPromoSkuVOS())) {
                                promoSkuVOList.addAll(pageResponse.getPromoSkuVOS());
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        return promoSkuVOList;
    }

    /**
     * 促销sku列表查询(jingdong.seller.promotion.sku.list)
     * 分页查询指定促销下符合条件的促销SKU，最多返回10个
     *
     * @param shop        ShopBean  店铺信息
     * @param wareId      Long      商品ID
     * @param jdSkuId     Long      京东skuId
     * @param promoId     Long      促销编号
     * @param bindType    Integer   绑定类型
     * @param page        Integer   页码
     * @param size        Integer   每页记录个数
     * @return SellerPromotionSkuListResponse 促销sku列表查询结果
     */
    public SellerPromotionSkuListResponse getPromotionSkuListPage(ShopBean shop, Long wareId, Long jdSkuId, Long promoId, Integer bindType, Integer page, Integer size) {

        SellerPromotionSkuListRequest request = new SellerPromotionSkuListRequest();
        // 商品ID(可选)
        if (wareId != null)      request.setWareId(wareId);
        // 京东skuId(可选)
        if (jdSkuId != null)     request.setSkuId(jdSkuId);
        // 促销编号(必须)
        if (promoId != null)     request.setPromoId(promoId);
        // 绑定类型(可选), 可选值：主商品（1），赠品（2）。(赠品促销、满减送促销中的赠品需要设置为2，其余均设置为1)
        if (bindType != null)    request.setBindType(bindType);
        // 页码（必须为正整数）(必须)
        if (page != null)        request.setPage(page);
        // 每页记录个数（每页最少1个，最多10个）(必须)
        if (size != null)        request.setSize(size);

        try {
            // 调用京东促销sku列表查询API(最多返回10个)(jingdong.seller.promotion.get)
            SellerPromotionSkuListResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    // 返回更新时间
                    return response;
                } else {
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东取得促销sku列表查询(最多返回10个)API返回应答为空(response = null)");
            }
        } catch (Exception ex) {
            String errMsg = String.format(shop.getShop_name() + "调用京东API取得促销sku列表查询(最多返回10个)失败! [channelId:%s] [cartId:%s] [promotionId:%s]" +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), promoId, ex.getMessage());
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

    /**
     * 促销创建完毕，提交保存促销命令(jingdong.seller.promotion.commit)
     * commit之后就不能再追加jdSkuId了
     *
     * @param shop        ShopBean  店铺信息
     * @param promoId     Long      促销编号
     */
    public void doPromotionCommit(ShopBean shop, Long promoId) {

        SellerPromotionCommitRequest request = new SellerPromotionCommitRequest();
        // 促销编号(必须)
        if (promoId != null)     request.setPromoId(promoId);

        try {
            // 调用京东促销创建完毕，提交保存促销命令API(jingdong.seller.promotion.commit)
            SellerPromotionCommitResponse response = reqApi(shop, request);

            if (response != null) {
                // 京东返回正常的场合
                if (JdConstants.C_JD_RETURN_SUCCESS_OK.equals(response.getCode())) {
                    return;
                } else {
                    throw new BusinessException(response.getZhDesc());
                }
            } else {
                // response = null（https://api.jd.com/routerjson）不能访问的可能原因是服务器禁掉了https端口
                // 或app_url,app_key等不正确
                throw new BusinessException("京东促销创建完毕提交保存促销命令API返回应答为空(response = null)");
            }
        } catch (Exception ex) {
            String errMsg = String.format(shop.getShop_name() + "调用京东促销创建完毕提交保存促销命令失败! [channelId:%s] [cartId:%s] [promotionId:%s]" +
                    "[errMsg:%s]", shop.getOrder_channel_id(), shop.getCart_id(), promoId, ex.getMessage());
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }
    }

}
