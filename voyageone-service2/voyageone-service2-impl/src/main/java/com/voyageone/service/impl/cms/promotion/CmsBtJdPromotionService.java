/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.service.impl.cms.promotion;

import com.google.common.base.Joiner;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.response.promotion.PromotionVO;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.bean.JdPromotionSkuBean;
import com.voyageone.components.jd.service.JdPromotionService;
import com.voyageone.components.jd.service.JdSkuService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 京东平台促销活动服务
 *
 * @author desmond 16/11/17
 * @version 2.9.1
 * @since 2.9.1
 */
@Service
public class CmsBtJdPromotionService extends BaseService {

    @Autowired
    private JdSkuService jdSkuService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private JdPromotionService jdPromotionService;

    /**
     * 批量添加sku到京东促销（必须是价格促销）(jingdong.seller.promotion.sku.add)
     *
     * 添加参加促销的sku，单次最多添加100个SKU，一个促销最多支持1000个SKU，当基于套装促销添加SKU时，
     * 最多可设置3个商品的SKU，并且相同商品的次序要一致；
     * 当基于赠品促销添加SKU时，赠品SKU只能是1-5个，每个赠品只能赠送1-3个，赠品的总价应低于主商品中的最小京东价。
     *
     * @param shop        ShopBean  店铺信息
     * @param promoId     Long      京东促销id
     * @param jdPromoSkuList List   京东促销用SKU对象列表
     * @param modifier    String    更新者(用于回写jdSkuId)
     * @return List<Long>           添加sku到京东促销返回结果
     */
    public void addSkuIdToPromotionBatch(ShopBean shop, Long promoId, List<JdPromotionSkuBean> jdPromoSkuList, String modifier) {

        if (shop == null || promoId == null || ListUtils.isNull(jdPromoSkuList)) return;

        // 追加SKU到促销活动前先查询促销，如果促销存在且促销状态是未提交状态（status=-1）才可以添加；如果促销状态<>-1报出异常说不能添加了
        StringBuffer sbFault = new StringBuffer("");
        if (!canAddSkuToPromotion(shop, promoId, sbFault)) {
            String errMsg = String.format("不能添加jdSkuId到促销;%s", sbFault.toString());
            $error(errMsg);
            throw new BusinessException(errMsg);
        }

        // 用于保存真正需要加入到promotion中去的sku对象列表
        List<JdPromotionSkuBean> successSkuList = new ArrayList<>();
        // 用于保存参数传进来的sku没有jdSkuId,在这里从平台上重新取得jdSkuId之后，需要回写到product表中的sku对象列表
        List<JdPromotionSkuBean> updateSkuIdList = new ArrayList<>();
        // 用于保存根据skuCode未能取到jdSkuId的sku对象列表
        List<JdPromotionSkuBean> getSkuIdErrList = new ArrayList<>();
        StringBuffer failCause = new StringBuffer();
        // 看看有没有SKU没有jdSkuId的，有的话就从京东平台上取得一下(后面会回写到product表中)
        for (JdPromotionSkuBean jdPromSku : jdPromoSkuList) {
            // 如果skuCode和jdSkuId都为空时，跳过
            if (StringUtils.isEmpty(jdPromSku.getSkuCode()) && StringUtils.isEmpty(jdPromSku.getJdSkuId()))
                continue;

            // 如果jdSkuId为空的话，需要用skuCode到京东平台上重新取得一下
            if (StringUtils.isEmpty(jdPromSku.getJdSkuId())) {
                failCause.setLength(0);
                // 根据skuCode到京东平台上重新取得京东skuId
                Sku currentSku = jdSkuService.getSkuByOuterId(shop, jdPromSku.getSkuCode(), failCause);
                if (currentSku != null) {
                    jdPromSku.setJdSkuId(StringUtils.toString(currentSku.getSkuId()));
                    successSkuList.add(jdPromSku);
                    updateSkuIdList.add(jdPromSku);
                } else {
                    // 如果从平台也没有取到skuId的话，加入到出错列表中(不加到promotion中)
                    getSkuIdErrList.add(jdPromSku);
                }
            } else {
                successSkuList.add(jdPromSku);
            }
        }

        // 先回写jdSkuId
        if (ListUtils.notNull(updateSkuIdList)) {
            try {
                // 批量更新这次取得的jdSkuId到mongoDb的product表中(即使回写失败也往后做，大不了下次再重新取一下jdSkuId)
                updateJdSkuIds(shop.getOrder_channel_id(), shop.getCart_id(), updateSkuIdList, modifier);
            } catch (Exception e) {
                $warn(String.format("批量添加sku到京东促销时，批量回写jdSkuId失败！[ChannelId:%s] [CartId:%s] [ErrMsg:%s]",
                        shop.getOrder_channel_id(), shop.getCart_id(), e.getMessage()));
            }
        }

        // 调用京东API添加sku到京东促销
        StringBuilder addFailCause = new StringBuilder();
        // 保存调用京东批量添加sku到京东促销API之后，返回的促销SKU编号(jdSkuId)列表(返回的SKU列表是都参加促销的SKU，无效的SKU会被剔除 )
        List<Long> addSuccessSkuIds = null;
        if (ListUtils.notNull(successSkuList)) {
            StringBuilder sbSkuIds = new StringBuilder();
            StringBuilder sbJdPrices = new StringBuilder();
            StringBuilder sbPromoPrices = new StringBuilder();

            // 单次最多添加100个SKU，超过100个SKU的时候，分批处理
            List<List<JdPromotionSkuBean>> pageList = CommonUtil.splitList(successSkuList, 100);
            for(List<JdPromotionSkuBean> page : pageList) {
                sbSkuIds.setLength(0);
                sbJdPrices.setLength(0);
                sbPromoPrices.setLength(0);
                for (JdPromotionSkuBean jdPromSku : page) {
                    sbSkuIds.append(jdPromSku.getJdSkuId() + ",");
                    sbJdPrices.append(jdPromSku.getJdPrice() + ",");
                    sbPromoPrices.append(jdPromSku.getJdPromoPrice() + ",");
                }

                // 移除StringBuffer最后的","
                if (sbSkuIds.length() > 0) {
                    sbSkuIds.deleteCharAt(sbSkuIds.length() - 1);
                }
                if (sbJdPrices.length() > 0) {
                    sbJdPrices.deleteCharAt(sbJdPrices.length() - 1);
                }
                if (sbPromoPrices.length() > 0) {
                    sbPromoPrices.deleteCharAt(sbPromoPrices.length() - 1);
                }

                try {
                    // 调用京东添加sku到京东促销API(如果出错，记录错误信息，继续追加下一批sku)
                    addSuccessSkuIds = jdPromotionService.addSkuIdToPromotion(shop, promoId, sbSkuIds.toString(),
                            sbJdPrices.toString(), sbPromoPrices.toString());
                } catch (Exception e) {
                    addFailCause.append(e.getMessage());
                }

            }
        }

        // 调用京东API添加sku到京东促销出现错误时，报出错误消息
        if (!StringUtils.isEmpty(addFailCause.toString())) {
            $error(addFailCause.toString());
            throw new BusinessException(addFailCause.toString());
        }

        // 如果有未追加成功的skuIds时，报出异常
        if (ListUtils.notNull(addSuccessSkuIds)) {
            List<String> addSkuIds = addSuccessSkuIds.stream().map(p -> StringUtils.toString(p)).collect(Collectors.toList());
            List<String> notAddSkuIds = new ArrayList<>();
            successSkuList.forEach(p -> {
                if (!addSkuIds.contains(p.getJdSkuId())) notAddSkuIds.add(p.getJdSkuId());
            });

            // 如果有未追加成功的skuIds时，报出异常
            if (ListUtils.notNull(notAddSkuIds)) {
                String errMsg = String.format(shop.getShop_name() + "调用京东API添加sku到京东促销部分成功，部分未被追加进去! [channelId:%s] [cartId:%s] [promotionId:%s] [追加成功jdSkuId:%s] " +
                        "[未被追加jdSkuId:%s]", shop.getOrder_channel_id(), shop.getCart_id(), promoId, Joiner.on(",").join(addSkuIds), Joiner.on(",").join(notAddSkuIds));
                $error(errMsg);
                throw new BusinessException(errMsg);
            }
        }

        //  sku全部追加成功之后，就调用commit提交保存促销活动，并要再促销活动创建3小时内commit，commit之后就不能再添加sku了
        jdPromotionService.doPromotionCommit(shop, promoId);

    }

    /**
     * 批量回写jdSkuId到product表中
     *
     * @param channelId        渠道id
     * @param cartId           平台id
     * @param jdPromSkuBeanList 要回写jdSkuId的对象列表
     * @param modifier    String    更新者(用于回写jdSkuId)
     */
    public void updateJdSkuIds(String channelId, String cartId, List<JdPromotionSkuBean> jdPromSkuBeanList, String modifier) {
        if (StringUtils.isEmpty(channelId) || ListUtils.isNull(jdPromSkuBeanList)) return;

        // 循环取得的sku信息列表，把jdSkuId批量更新到product中去
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
        BulkWriteResult rs;
        for (JdPromotionSkuBean sku : jdPromSkuBeanList) {
            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'platforms.P"+ cartId +".skus.skuCode':#}");
            updObj.setQueryParameters(sku.getSkuCode());
            updObj.setUpdate("{$set:{'platforms.P"+ cartId +".skus.$.jdSkuId':#,'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(sku.getJdSkuId(), DateTimeUtil.getNowTimeStamp(), modifier);
            rs = bulkList.addBulkJongo(updObj);
            if (rs != null) {
                $debug("京东添加SKUID到promotion时发现jdSkuId为空，从平台上取得jdSkuId之后回写jdSkuId处理 channelId=%s, cartId=%s, skuCode=%s, skuId=%s, jdSkuId更新结果=%s",
                        channelId, cartId, sku.getSkuCode(), sku.getJdSkuId(), rs.toString());
            }
        }

        rs = bulkList.execute();
        if (rs != null) {
            $debug("京东添加SKUID到promotion时发现jdSkuId为空，从平台上取得jdSkuId之后回写jdSkuId处理 channelId=%s, cartId=%s, jdSkuId更新结果=%s", channelId, cartId, rs.toString());
        }
    }

    /**
     * 判断当前促销id是否可以添加jdSkuId
     *
     * @param shop        店铺信息
     * @param promoId     促销id
     * @return boolean    该促销id是否可以添加jdSkuId
     */
    public boolean canAddSkuToPromotion(ShopBean shop, Long promoId) {
        return canAddSkuToPromotion(shop, promoId, null);
    }

    /**
     * 判断当前促销id是否可以添加jdSkuId
     *
     * @param shop        店铺信息
     * @param promoId     促销id
     * @param sbFault     返回用错误信息
     * @return boolean    该促销id是否可以添加jdSkuId
     */
    public boolean canAddSkuToPromotion(ShopBean shop, Long promoId, StringBuffer sbFault) {
        // 取得京东促销详细信息
        PromotionVO promoInfo = jdPromotionService.getPromotionInfo(shop, promoId);
        String promoStatus = null;
        if (promoInfo != null) {
            switch (promoInfo.getStatus()) {
                case -1: promoStatus = "-1:新建促销且在3小时内，可以添加jdSkuId";break;
                case 1: promoStatus = "1:驳回";break;
                case 2: promoStatus = "2:未审核";break;
                case 3: promoStatus = "3:人工审核";break;
                case 4: promoStatus = "4:审核通过";break;
                case 5: promoStatus = "5:已生效";break;
                case 6: promoStatus = "6:已暂停";break;
                case 7: promoStatus = "7:强制暂停";break;
                default: promoStatus = "未知的status(" + promoInfo.getStatus() + ")";
            }

            if (promoInfo.getStatus() == -1) {
                return true;
            }
        }

        if (sbFault != null) {
            sbFault.append(String.format("该促销(%s)未被创建或者创建时间已经超过3个小时不能添加jdSkuId了! [status:%s]", promoId, promoStatus));
        }

        return false;
    }

}
