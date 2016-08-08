package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealPriceBatch_UpdateData;
import com.voyageone.components.jumei.bean.HtDeal_UpdateDealStockBatch_UpdateData;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.jumei.SkuPriceBean;
import com.voyageone.service.bean.cms.jumei.UpdateJmParameter;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.BaseService;
//import com.voyageone.service.impl.cms.jumei.platform.JMShopBeanService;
//import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductAddPlatformService;
import com.voyageone.service.impl.cms.jumei.JMShopBeanService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dell on 2016/4/19.
 */
@Service
public class JuMeiProductPlatform3Service extends BaseService {
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;
    @Autowired
    JMShopBeanService serviceJMShopBean;
   // @Autowired
   // JuMeiProductUpdateService service;
    @Autowired
    JumeiHtDealService serviceJumeiHtDeal;

    @Autowired
    CmsBtJmProductDaoExt daoExtCmsBtJmProduct;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    ProductService productService;
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductPlatform3Service.class);

    public void updateJmByPromotionId(int promotionId) throws Exception {
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(promotionId);
        ShopBean shopBean = serviceJMShopBean.getShopBean(modelCmsBtJmPromotion.getChannelId());
        LOG.info(promotionId + " 聚美上新开始");
        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = daoExtCmsBtJmPromotionProduct.selectJMCopyList(promotionId);
        try {
            for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {
                LOG.info(promotionId + " code:" + model.getProductCode() + "上新begin");
                updateJm(modelCmsBtJmPromotion, model, shopBean);
                LOG.info(promotionId + " code:" + model.getProductCode() + "上新end");
            }
        } catch (Exception ex) {
            LOG.error("addProductAndDealByPromotionId上新失败", ex);
            ex.printStackTrace();
        }
        LOG.info(promotionId + " 聚美上新end");
    }
   protected  UpdateJmParameter getUpdateJmParameter(CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel model, ShopBean shopBean) {
       UpdateJmParameter parameter = new UpdateJmParameter();
       parameter.cmsBtJmPromotionProductModel = model;
       parameter.cmsBtJmPromotionModel = modelCmsBtJmPromotion;
       parameter.shopBean = shopBean;
       parameter.cmsBtProductModel = productService.getProductByCode(model.getChannelId(), model.getProductCode());//todo cmsBtProductModel null处理11
       parameter.platform=parameter.cmsBtProductModel.getPlatform(CartEnums.Cart.JM);//todo  platform null处理11
       return parameter;
   }
    public void updateJm(CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel, ShopBean shopBean) throws Exception {
        try {
            UpdateJmParameter parameter = getUpdateJmParameter(modelCmsBtJmPromotion, cmsBtJmPromotionProductModel, shopBean);
            api_beforeCheck(parameter);//api调用前check
            if (parameter.cmsBtJmPromotionProductModel.getSynchStatus() != 2) {
                // 再售
                if (StringUtil.isEmpty(parameter.cmsBtJmPromotionProductModel.getJmHashId())) {
                    //6.1.1再售接口前check   copyDeal_beforeCheck(4.1)
                    copyDeal_beforeCheck(parameter);
                    //6.1.2调用再售接口  copyDeal
                    copyDeal(parameter);
                    //6.1.3再售接口后check   在方法copyDeal内部调用copyDeal_afterCheck(4.2)
                } else {
                    parameter.cmsBtJmPromotionProductModel.setStockStatus(1);//库存设置待更新
                    parameter.cmsBtJmPromotionProductModel.setSynchStatus(2);//有jmHashId 已上传
                }
                setOriginJmHashId(parameter);
                parameter.cmsBtJmPromotionProductModel.setPriceStatus(1);
                // 更新jm价格 限购limit 库存 延期
                updateJMDeal(parameter);
            } else {
                // 更新jm价格 限购limit 库存 延期
                updateJMDeal(parameter);
            }
        } catch (Exception ex) {
            cmsBtJmPromotionProductModel.setErrorMsg(ex.getMessage());
            // model.setUpdateState(EnumJuMeiUpdateState.Error.getId());//同步更新失败
            LOG.error("JuMeiProductPlatform3Service.addProductAndDealByPromotionId", ex);
            try {
                if (cmsBtJmPromotionProductModel.getErrorMsg().length() > 600) {
                    cmsBtJmPromotionProductModel.setErrorMsg(cmsBtJmPromotionProductModel.getErrorMsg().substring(0, 600));
                }
                daoCmsBtJmPromotionProduct.update(cmsBtJmPromotionProductModel);
            } catch (Exception cex) {
                LOG.error("JuMeiProductPlatform3Service.addProductAndDealByPromotionId", cex);
            }
        }
    }
    public  void  setOriginJmHashId( UpdateJmParameter parameter)
    {
        //todo  逻辑待定最新jmHashId(取该商品的最新jmHashId,这样最靠谱)
        parameter.platform.setpNumIId(parameter.cmsBtJmPromotionProductModel.getJmHashId());
        productService.updateProductPlatform(parameter.cmsBtProductModel.getChannelId(), parameter.cmsBtProductModel.getProdId(), parameter.platform, parameter.cmsBtJmPromotionProductModel.getModifier());
    }
    //所有api调用前check
    public void api_beforeCheck(UpdateJmParameter parameter) {

        String errorMsg = "";
        // 6.0.1
        if ("1".equalsIgnoreCase(parameter.cmsBtProductModel.getLock()))//1:lock, 0:unLock
        {
            errorMsg = "商品被Lock，如确实需要上传商品，请先解锁";
        }
        //6.0.2
        else if (parameter.cmsBtJmPromotionProductModel.getDealPrice().doubleValue() >= parameter.cmsBtJmPromotionProductModel.getMarketPrice().doubleValue()) {
            errorMsg = "市场价必须大于团购价";
        }
        if(!StringUtils.isEmpty(errorMsg)) {
            throw new BusinessException(errorMsg);
        }
    }
    void updateJMDeal( UpdateJmParameter parameter) throws Exception {
        if (parameter.cmsBtJmPromotionProductModel.getPriceStatus() == 1) //更新价格
        {
            updateDeal(parameter);//更新deal   商品属性 价格
        }
        if (parameter.cmsBtJmPromotionProductModel.getDealEndTimeStatus() == 1) {
            updateDealEndTime(parameter);//deal延期
        }
        if (parameter.cmsBtJmPromotionProductModel.getStockStatus() != 2) {
            updateDealStock(parameter);//更新库存
        }

    }
    //再售前check
    public  void copyDeal_beforeCheck(UpdateJmParameter parameter) {
        CmsBtJmPromotionProductModel model = parameter.cmsBtJmPromotionProductModel;
        String errorMsg = "";
        if (parameter.cmsBtJmPromotionModel.getIsPromotionFullMinus())//满减专场
        {
            CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectDateRepeatByCode(model.getId(), model.getChannelId(), model.getProductCode(), model.getActivityStart(), model.getActivityEnd());
            if (modelPromotionProduct != null) { //活动日期重叠
                errorMsg = "该商品已于相关时间段内，在其它专场中完成上传，为避免财务结算问题，请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品";//取一个活动id
            }
        } else { //非满减专场 非大促
            CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectFullMinusDateRepeat(model.getId(), model.getChannelId(), model.getProductCode(), model.getActivityStart(), model.getActivityEnd());
            if (modelPromotionProduct != null) { //活动日期重叠
                errorMsg = "该商品已在该大促时间范围内的其它未过期聚美专场中，完成上传，且开始时间与大促开始时间不一致。无法加入当前大促专场。聚美会监控大促专场的营销数据，禁止商品在活动启动前偷跑，大促商品必须有预热。请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品";//取一个活动id

            }
        }
        if (StringUtils.isEmpty(errorMsg) && parameter.cmsBtJmPromotionModel.getPromotionType() == 2)//大促专场
        {
            CmsBtJmPromotionProductModel modelPromotionProduct = daoExtCmsBtJmPromotionProduct.selectDateRepeatByCode(model.getId(), model.getChannelId(), model.getProductCode(), model.getActivityStart(), model.getActivityEnd());
            if (modelPromotionProduct != null && modelPromotionProduct.getActivityStart() != model.getActivityStart()) { //活动日期重叠 开始时间不相等
                errorMsg = "该商品已于相关时间段内，在其它专场中完成上传，为避免财务结算问题，请放弃导入,JmPromotionId:" + modelPromotionProduct.getCmsBtJmPromotionId() + "存在该商品";//取一个活动id

            }
        }
        if(!StringUtils.isEmpty(errorMsg))
        {
          throw  new BusinessException(errorMsg);
        }
    }

    public CmsBtJmPromotionModel getCmsBtJmPromotionModelBySellHashId(String SellHashId) {
        Map<String, Object> map = new HashMap<>();
        map.put("jmHashId", SellHashId);
        CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel = daoCmsBtJmPromotionProduct.selectOne(map);
        if(cmsBtJmPromotionProductModel==null) return  null;
        return daoCmsBtJmPromotion.select(cmsBtJmPromotionProductModel.getCmsBtJmPromotionId());
    }
    //
    private void updateDealEndTime(UpdateJmParameter parameter) throws Exception {
        CmsBtJmPromotionModel modelCmsBtJmPromotion=parameter.cmsBtJmPromotionModel;
        CmsBtJmPromotionProductModel model=parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean=parameter.shopBean;
        if (modelCmsBtJmPromotion.getActivityEnd().getTime() > model.getActivityEnd().getTime()) {
            jmHtDealUpdateDealEndTime(modelCmsBtJmPromotion, model, shopBean);
            model.setDealEndTimeStatus(2);
            model.setErrorMsg("");
            model.setActivityEnd(modelCmsBtJmPromotion.getActivityEnd());
            daoCmsBtJmPromotionProduct.update(model);
        }
    }

    private void updateDeal(UpdateJmParameter parameter) throws Exception {
        CmsBtJmPromotionProductModel model=parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean=parameter.shopBean;
        List<SkuPriceBean> listSkuPrice = daoExtCmsBtJmPromotionSku.selectJmSkuPriceInfoListByPromotionProductId(model.getId());
        jmHtDealUpdateDealPriceBatch(model, shopBean, listSkuPrice);//更新deal价格
        String jmSkuNoList = getjmSkuNo(listSkuPrice);
        jmHtDealUpdate(model, shopBean, jmSkuNoList);//更新deal信息   limit   jmSkuNo
        model.setPriceStatus(2);
        if (model.getUpdateStatus() == 1) {
            model.setUpdateStatus(2);//价格和商品属性都更新成功后 设置为已变更
        }
        model.setErrorMsg("");
        daoCmsBtJmPromotionProduct.update(model);
    }
    private void updateDealStock(UpdateJmParameter parameter) throws Exception {
        CmsBtJmPromotionProductModel model=parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean=parameter.shopBean;
        List<SkuPriceBean> listSkuPrice = daoExtCmsBtJmPromotionSku.selectJmSkuPriceInfoListByPromotionProductId(model.getId());
        jmHtDealUpdateDealStockBatch(model, shopBean, listSkuPrice);//更新deal信息   limit   jmSkuNo
        model.setStockStatus(2);
        model.setErrorMsg("");
        daoCmsBtJmPromotionProduct.update(model);
    }
    //再售
    private void copyDeal( UpdateJmParameter parameter) throws Exception {
        CmsBtJmPromotionModel modelCmsBtJmPromotion = parameter.cmsBtJmPromotionModel;
        CmsBtJmPromotionProductModel model = parameter.cmsBtJmPromotionProductModel;
        ShopBean shopBean = parameter.shopBean;
       // CmsBtJmProductModel modelJmProduct = daoExtCmsBtJmProduct.selectByProductCodeChannelId(model.getProductCode(), model.getChannelId());
        jmHtDealCopy(parameter,parameter.platform.getpNumIId());//再售
        model.setActivityStart(modelCmsBtJmPromotion.getActivityStart());
        model.setActivityEnd(modelCmsBtJmPromotion.getActivityEnd());
        model.setSynchStatus(2);
        model.setStockStatus(1);//库存设置待更新
        daoCmsBtJmPromotionProduct.update(model);//在售之后先保存  避免下面调用接口失败 jmHashId丢失

        if (modelCmsBtJmPromotion.getStatus() == null || modelCmsBtJmPromotion.getStatus() == 0) {//更新互动
            modelCmsBtJmPromotion.setStatus(1);//已经有商品上新
            daoCmsBtJmPromotion.update(modelCmsBtJmPromotion);
        }
    }
    private String getjmSkuNo(List<SkuPriceBean> listSkuPrice) {
        String jmSkuNoList = "";
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            jmSkuNoList += skuPriceBean.getJmSkuNo() + ",";
        }
        if (jmSkuNoList.length() > 0) {
            jmSkuNoList = jmSkuNoList.substring(0, jmSkuNoList.length() - 1);
        }
        return jmSkuNoList;
    }

    //再售
    private void jmHtDealCopy(UpdateJmParameter parameter, String originJmHashId) throws Exception {
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        request.setStart_time(parameter.cmsBtJmPromotionModel.getActivityStart());
        request.setEnd_time(parameter.cmsBtJmPromotionModel.getActivityEnd());
        request.setJumei_hash_id(originJmHashId);//原始jmHashId
        try {
            HtDealCopyDealResponse response = serviceJumeiHtDeal.copyDeal(parameter.shopBean, request);
            if (response.is_Success()) {
                parameter.cmsBtJmPromotionProductModel.setJmHashId(response.getJumei_hash_id());
            } else {
                //再售后check
                String errorMsg = copyDeal_afteErrorCheck(parameter, response);
                if(!StringUtils.isEmpty(parameter.cmsBtJmPromotionProductModel.getJmHashId()))
                {
                    setOriginJmHashId(parameter);
                }
                throw new BusinessException(errorMsg + response.getBody());
            }
        } catch (Exception ex) {
            parameter.cmsBtJmPromotionProductModel.setSynchStatus(3);
            parameter.cmsBtJmPromotionProductModel.setPriceStatus(0);
            parameter.cmsBtJmPromotionProductModel.setDealEndTimeStatus(0);
            throw ex;
        }
    }
    //再售后check
    public  String copyDeal_afteErrorCheck(UpdateJmParameter parameter,HtDealCopyDealResponse response) throws Exception {

        CmsBtJmPromotionModel jmPromotion = parameter.cmsBtJmPromotionModel;
        CmsBtJmPromotionProductModel jmPromotionProduct = parameter.cmsBtJmPromotionProductModel;
        String errorMsg = "";
        if (!StringUtils.isEmpty(response.getJumei_hash_id())) {
            parameter.cmsBtJmPromotionProductModel.setJmHashId(response.getJumei_hash_id());
        } else if (StringUtils.isEmpty(response.getSell_hash_id())) {//sell_hash_id 不存在
            errorMsg = "请登录聚美后台，检查商品相关【产品库】与【Deal】信息，如发现“待审核”、“审核通过”、“取消送审”字样，请将相关 聚美产品ID，聚美产品品牌，聚美HashID申报给聚美运营，进行人工审核并发布.";
            return errorMsg;
        } else {
            HtDealGetDealByHashIDRequest getDealByHashIDRequest = new HtDealGetDealByHashIDRequest();
            getDealByHashIDRequest.setJumei_hash_id(response.getSell_hash_id());
            HtDealGetDealByHashIDResponse getDealByHashIDResponse = serviceJumeiHtDeal.getDealByHashID(parameter.shopBean, getDealByHashIDRequest);
            String sell_hash_id = response.getSell_hash_id();
            long jmActivityStartTime = DateTimeUtilBeijing.toLocalTime(parameter.cmsBtJmPromotionModel.getActivityStart());
            long jmActivityEndTime = DateTimeUtilBeijing.toLocalTime(parameter.cmsBtJmPromotionModel.getActivityEnd());
            long sellJmEndTime = DateTimeUtilBeijing.toLocalTime(getDealByHashIDResponse.getEnd_time());
            long sellJmStartTime = DateTimeUtilBeijing.toLocalTime(getDealByHashIDResponse.getStart_time());
            CmsBtJmPromotionModel sellJmPromotion = getCmsBtJmPromotionModelBySellHashId(response.getSell_hash_id());
            String sellJmPromotionName = sellJmPromotion.getName();
//            if (jmEndTime >= activityStart) {//if true then 和本次活动重叠 Sell_hash_id作为本次活动的jumeiHashId
//                parameter.cmsBtJmPromotionProductModel.setJmHashId(response.getSell_hash_id());
//                long activeEnd = DateTimeUtilBeijing.toLocalTime(parameter.cmsBtJmPromotionModel.getActivityEnd());
//                if (activeEnd > jmEndTime) {//if true then 本次活动时间大于deal的结束时间 延期
//                    parameter.cmsBtJmPromotionProductModel.setDealEndTimeStatus(1);
//                }
//            }
            if (jmPromotion.getIsPromotionFullMinus())//当前专场为 满减专场
            { //4.2.3
                errorMsg = String.format("该商品已加入专场【%s】，并上传成功。满减专场不可与其它专场共用商品，请于专场【%s】结束后，再进行上传", sellJmPromotionName, sellJmPromotionName);
                return errorMsg;
            }

            if (!jmPromotion.getIsPromotionFullMinus())//当前专场为 非满减专场
            { //4.2.4
                if (sellJmPromotion.getIsPromotionFullMinus())//在售专场为 满减专场
                {
                    errorMsg = String.format("该商品已加入满减专场【%s】，并上传成功。满减专场不可与其它专场共用商品，请于专场【%s】结束后，再进行上传.", sellJmPromotionName, sellJmPromotionName);
                    return errorMsg;
                }
            }
            // 4.2.5
            if (jmPromotion.getPromotionType() == 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为 大促非满减专场
            {
                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为 非满减专场
                {
                    if (sellJmStartTime == jmActivityStartTime)//开始时间相等
                    {
                        jmPromotionProduct.setJmHashId(sell_hash_id);//设置当前专场jmHashId
                        if (sellJmEndTime < jmActivityEndTime)//【sell_hash_id】的结束时间小于当前专场结束时间的场合
                        {//调用延迟Deal结束时间API
                            jmPromotionProduct.setDealEndTimeStatus(1);//设置为待延期
                        }
                        errorMsg = String.format("该商品已加入专场【%s】，并上传成功。介于开始时间相同，当前大促专场引用了同一HashID，并进行了延期。如需变更价格，请重新点击【重刷】/【批量同步价格】。操作将影响关联专场，请慎重", sellJmPromotionName);
                        return errorMsg;
                    }
                }
            }
            //4.2.6
            if (jmPromotion.getPromotionType() == 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为  大促非满减专场
            {
                if (sellJmStartTime != jmActivityStartTime)//开始时间不相等
                {
                    errorMsg = String.format("该商品已加入专场【%s】，并上传成功。聚美平台监控大促开场，严禁大促专场商品出现时间异常。介于开始时间不相同，该商品已无法在当前大促专场进行售卖，请替换商品.", sellJmPromotionName);
                    return errorMsg;
                }
            }
            //4.2.7
            if (jmPromotion.getPromotionType() != 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为  非大促 非满减专场
            {
                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为 非满减专场
                {
                    if (sellJmStartTime > jmActivityEndTime)//在售专场开始时间>当前专场结束时间
                    {
                        errorMsg = String.format("该商品已加入专场【%s】，并上传成功。介于其有效期为%s】（年月日 时分秒）至【%s】（年月日 时分秒），晚于当前专场，该商品已无法在当前专场进行售卖，请替换商品", sellJmPromotionName, getDealByHashIDResponse.getStart_time(), getDealByHashIDResponse.getEnd_time());
                        return errorMsg;
                    }
                }
            }
            //4.2.8
            if (jmPromotion.getPromotionType() != 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为  非大促 非满减专场
            {
                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为非满减专场
                {
                    if (DateTimeUtil.addDays(jmPromotion.getActivityEnd(), -4).getTime() >= getDealByHashIDResponse.getEnd_time().getTime())//【sell_hash_id】的结束时间早于当前专场结束时间4天或以上 时
                    {
                        errorMsg = String.format("该商品已加入专场【sellJmPromotion.getName()】，并上传成功。请于【sellJmPromotion.getActivityEnd()】（年月日 时分秒）之后，再进行上传", sellJmPromotionName, DateTimeUtil.format(getDealByHashIDResponse.getEnd_time(), "yyyy-MM-dd HH:mm:ss"));
                        return errorMsg;
                    }
                }
            }
            //4.2.9
            if (jmPromotion.getPromotionType() != 2 && !jmPromotion.getIsPromotionFullMinus())//当前专场为  非大促 非满减专场
            {
                if (!sellJmPromotion.getIsPromotionFullMinus())//在售专场为非满减专场
                {
                    if (DateTimeUtil.addDays(jmPromotion.getActivityEnd(), -4).getTime() < getDealByHashIDResponse.getEnd_time().getTime())//【sell_hash_id】的结束时间早于当前专场结束时间3天或以下 时，
                    {
                        //4.2.9.1
                        jmPromotionProduct.setJmHashId(sell_hash_id);//设置当前专场jmHashId
                        //4.2.9.2 新HashID替换MongoDB中，该商品的Origin HashID 参考步骤6.0.3
                        //4.2.9.2 //在售专场的结束时间小于当前专场结束时间
                        if (sellJmEndTime < jmActivityEndTime) {
                            //调用延迟Deal结束时间API
                            jmPromotionProduct.setDealEndTimeStatus(1);
                        }
                        errorMsg = String.format("该商品已加入专场【%s】，并上传成功。当前专场引用了同一HashID，并进行了延期。该商品无预热。如需变更价格，请重新点击【重刷】/【批量同步价格】。操作将影响关联专场，请慎重。", sellJmPromotionName);
                        return errorMsg;
                    }
                }
            }
        }
        // throw new BusinessException("productCode:" +  parameter.cmsBtJmPromotionProductModel.getProductCode() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
        return errorMsg;
    }

    //更新特卖信息
    private void jmHtDealUpdate(CmsBtJmPromotionProductModel model, ShopBean shopBean, String jumei_sku_no) throws Exception {
        HtDealUpdateRequest request = new HtDealUpdateRequest();
        request.setJumei_hash_id(model.getJmHashId());
        HtDealUpdate_DealInfo update_dealInfo = new HtDealUpdate_DealInfo();
        update_dealInfo.setUser_purchase_limit(model.getLimit()); //限购数量
        update_dealInfo.setJumei_sku_no(jumei_sku_no);           //jmskuno
        request.setUpdate_data(update_dealInfo);
        try {
            HtDealUpdateResponse response= serviceJumeiHtDeal.update(shopBean, request);
            if(!response.is_Success())
            {
                model.setPriceStatus(3);
                throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
                // $error(response.getBody());
            }
        }
        catch (Exception ex)
        {
            model.setPriceStatus(3);
            throw  ex;
        }

    }
    private void jmHtDealUpdateDealEndTime(CmsBtJmPromotionModel modelCmsBtJmPromotion,CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {

        //设置请求参数
        HtDealUpdateDealEndTimeRequest request = new HtDealUpdateDealEndTimeRequest();
        request.setJumei_hash_id(model.getJmHashId());
        request.setEnd_time(modelCmsBtJmPromotion.getActivityEnd());

        try {
            HtDealUpdateDealEndTimeResponse response = serviceJumeiHtDeal.updateDealEndTime(shopBean, request);
            if (!response.is_Success()) {
                model.setDealEndTimeStatus(3);
                throw new BusinessException("productId:" + model.getId() + "updateDealEndTime:" + response.getErrorMsg());
            }
        }catch (Exception ex)
        {
            model.setDealEndTimeStatus(3);
            throw  ex;
        }
    }
    //更新价格
    private void   jmHtDealUpdateDealPriceBatch(CmsBtJmPromotionProductModel model, ShopBean shopBean, List<SkuPriceBean> listSkuPrice) throws Exception {

        //设置请求参数
        HtDealUpdateDealPriceBatchRequest request = new HtDealUpdateDealPriceBatchRequest();
        HtDeal_UpdateDealPriceBatch_UpdateData updateData = null;
        List<HtDeal_UpdateDealPriceBatch_UpdateData> list = new ArrayList<>();
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            updateData = new HtDeal_UpdateDealPriceBatch_UpdateData();
            list.add(updateData);
            updateData.setJumei_hash_id(model.getJmHashId());
            updateData.setJumei_sku_no(skuPriceBean.getJmSkuNo());
            updateData.setDeal_price(skuPriceBean.getDealPrice());
            updateData.setMarket_price(skuPriceBean.getMarketPrice());
        }

        try {
            List<List<HtDeal_UpdateDealPriceBatch_UpdateData>> pageList = CommonUtil.splitList(list,10);
            for(List<HtDeal_UpdateDealPriceBatch_UpdateData> page:pageList) {
                request.setUpdate_data(page);
                $info("jmHtDealUpdateDealPriceBatch:"+ model.getProductCode()+JacksonUtil.bean2Json(request.getUpdate_data()));
                HtDealUpdateDealPriceBatchResponse response = serviceJumeiHtDeal.updateDealPriceBatch(shopBean, request);
                if (!response.is_Success()) {
                    model.setPriceStatus(3);
                    throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
                }
            }
        }
        catch (Exception ex)
        {
            model.setPriceStatus(3);
            throw  ex;
        }
    }
    //批量同步deal库存
    private void   jmHtDealUpdateDealStockBatch(CmsBtJmPromotionProductModel model, ShopBean shopBean, List<SkuPriceBean> listSkuPrice) throws Exception {
        //取orgChanelId
        CmsBtProductModel productInfo = productService.getProductByCode(model.getChannelId(), model.getProductCode());
        String orgChanelId=StringUtils.isEmpty(productInfo.getOrgChannelId())?model.getChannelId():productInfo.getOrgChannelId();

        ///取skuCode
        List<String> skuList=listSkuPrice.stream().map(m->m.getSkuCode()).collect(Collectors.toList());

        //取skuCode库存
         Map<String, Integer> skuLogicQtyMap = productService.getLogicQty(orgChanelId,skuList);

        //设置请求参数
        HtDealUpdateDealStockBatchRequest request = new HtDealUpdateDealStockBatchRequest();
        HtDeal_UpdateDealStockBatch_UpdateData updateData = null;
        List<HtDeal_UpdateDealStockBatch_UpdateData> list = new ArrayList<>();
        for (SkuPriceBean skuPriceBean : listSkuPrice) {
            updateData = new HtDeal_UpdateDealStockBatch_UpdateData();
            list.add(updateData);
            updateData.setJumei_sku_no(skuPriceBean.getJmSkuNo());
            Integer stock = skuLogicQtyMap.get(skuPriceBean.getSkuCode());//获取库存
            if(stock!=null) {
                updateData.setStock(stock);
            }
        }

        try {
            List<List<HtDeal_UpdateDealStockBatch_UpdateData>> pageList = CommonUtil.splitList(list,10);
            for(List<HtDeal_UpdateDealStockBatch_UpdateData> page:pageList) {
                request.setUpdate_data(page);
                $info("jmHtDealUpdateDealStockBatch:"+ model.getProductCode()+JacksonUtil.bean2Json(request.getUpdate_data()));
                HtDealUpdateDealStockBatchResponse response = serviceJumeiHtDeal.updateDealStockBatch(shopBean, request);
                if (!response.is_Success()) {
                    model.setStockStatus(3);
                    throw new BusinessException("productId:" + model.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
                }
            }
        }
        catch (Exception ex)
        {
            model.setStockStatus(3);
            throw  ex;
        }
    }
}
