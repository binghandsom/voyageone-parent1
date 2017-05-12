package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jumei.JumeiHtMallService;
import com.voyageone.components.jumei.bean.HtMallSkuPriceUpdateInfo;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsJmMallPromotionPriceSyncMQMessageBody;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by james on 2017/1/23.
 */
@Service
@RabbitListener()
public class CmsJmMallPromotionPriceSyncMQJob extends TBaseMQCmsService<CmsJmMallPromotionPriceSyncMQMessageBody> {

    final
    CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt;
    final
    CmsBtJmPromotionProductDao cmsBtJmPromotionProductDao;
    private final CmsBtJmPromotionService cmsBtJmPromotionService;
    private final JumeiHtMallService jumeiHtMallService;
    private final CmsBtJmSkuDao cmsBtJmSkuDao;

    @Autowired
    public CmsJmMallPromotionPriceSyncMQJob(CmsBtJmPromotionService cmsBtJmPromotionService, JumeiHtMallService jumeiHtMallService, CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt, CmsBtJmPromotionProductDao cmsBtJmPromotionProductDao, CmsBtJmSkuDao cmsBtJmSkuDao) {
        this.cmsBtJmPromotionService = cmsBtJmPromotionService;
        this.jumeiHtMallService = jumeiHtMallService;
        this.cmsBtJmPromotionProductDaoExt = cmsBtJmPromotionProductDaoExt;
        this.cmsBtJmPromotionProductDao = cmsBtJmPromotionProductDao;
        this.cmsBtJmSkuDao = cmsBtJmSkuDao;
    }

    @Override
    public void onStartup(CmsJmMallPromotionPriceSyncMQMessageBody messageBody) throws Exception {
        Integer jmPid = messageBody.getJmPromotionId();
        List<String> productCodes = messageBody.getProductCodes();

        String channelId = messageBody.getChannelId();
        ShopBean shopBean = Shops.getShop(channelId, CartEnums.Cart.JM.getId());
        if (shopBean == null) {
            $error("JmMallPromotionPriceSyncService 店铺及平台数据不存在！ channelId=%s", channelId);

            throw new BusinessException(String.format("JmMallPromotionPriceSyncService 店铺及平台数据不存在！ channelId=%s", channelId));
        }

        // 找到该活动下所有sku
        List<Map<String, Object>> skus = cmsBtJmPromotionService.selectCloseJmPromotionSku(jmPid);
        Map<String, Double> codePrice = new HashMap<>();
        if(ListUtils.isNull(productCodes)) {
            productCodes = skus.stream().map(sku -> sku.get("product_code").toString()).distinct().collect(Collectors.toList());
        }
        // 获取code的deal_price
        for (Map<String, Object> skuPriceBean : skus) {
            if (!codePrice.containsKey(skuPriceBean.get("product_code").toString())) {
                codePrice.put(skuPriceBean.get("product_code").toString(), Double.parseDouble(skuPriceBean.get("deal_price").toString()));
            }
        }
        super.count = productCodes == null ? 0 : productCodes.size();
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        for(String productCode: productCodes){
            String errmsg = "";
            List<HtMallSkuPriceUpdateInfo> list = new ArrayList<>();
            try {
                List<CmsBtJmSkuModel> skuModelList = getCmsBtJmSkuModels(channelId, productCode);
                for (CmsBtJmSkuModel skuModel : skuModelList) {

                    if (productCode.equals(skuModel.getProductCode())
                            && !StringUtils.isEmpty(skuModel.getJmSkuNo())) {
                        HtMallSkuPriceUpdateInfo updateData = new HtMallSkuPriceUpdateInfo();
                        list.add(updateData);
                        updateData.setJumei_sku_no(skuModel.getJmSkuNo());
                        updateData.setMall_price(codePrice.get(productCode));
                    }
                }
                if(list.size()>0) {
                    updateJmMallPrice(productCode, list, shopBean, failList);
                }
            }catch (Exception e){
                errmsg = e.toString();
            }
            CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel = cmsBtJmPromotionProductDaoExt.selectByProductCodeChannelIdCmsBtJmPromotionId(productCode, channelId,jmPid );
            cmsBtJmPromotionProductModel.setErrorMsg(errmsg);
            cmsBtJmPromotionProductModel.setModifier(getTaskName());
            cmsBtJmPromotionProductModel.setModified(new Date());
            cmsBtJmPromotionProductDao.update(cmsBtJmPromotionProductModel);
        }

        if (failList.size() > 0) {
            //写业务错误日志
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", productCodes.size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }

    /**
     * 查询CmsBtJmSkuModel
     *
     * @param channelId
     * @param productCode
     * @return
     */
    private List<CmsBtJmSkuModel> getCmsBtJmSkuModels(String channelId, String productCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("productCode", productCode);
        map.put("channelId", channelId);

        List<CmsBtJmSkuModel> skuList = cmsBtJmSkuDao.selectList(map);
        if (skuList == null) {
            skuList = new ArrayList<>();
        }
        return skuList;
    }

    private void updateJmMallPrice(String code, List<HtMallSkuPriceUpdateInfo> updateData, ShopBean shopBean, List<CmsBtOperationLogModel_Msg> failList) throws Exception {

        List<List<HtMallSkuPriceUpdateInfo>> pageList = CommonUtil.splitList(updateData, 20);
        for (List<HtMallSkuPriceUpdateInfo> page : pageList) {
            StringBuffer sb = new StringBuffer();
            try {
                if (!jumeiHtMallService.updateMallSkuPrice(shopBean, page, sb)) {
                    $error(sb.toString());
                    throw new BusinessException(sb.toString());
                }
            } catch (Exception e) {
                $error(sb.toString());
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                StringBuffer sbSku = new StringBuffer();
                page.stream().forEach(skuInfo -> sbSku.append(skuInfo.getJumei_sku_no()).append(","));
                errorInfo.setSkuCode(sbSku.toString());
                errorInfo.setMsg(String.format("更新价格时异常 code=%s, errmsg=%s", code, e.getMessage()));
                failList.add(errorInfo);
                throw e;
            }
        }
    }
}
