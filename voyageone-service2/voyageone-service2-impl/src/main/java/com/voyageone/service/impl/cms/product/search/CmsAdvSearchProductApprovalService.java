package com.voyageone.service.impl.cms.product.search;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.prices.PriceService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 高级检索-商品审批Job业务类
 *
 * @Author rex
 * @Create 2017-01-13 13:10
 */
@Service
public class CmsAdvSearchProductApprovalService extends BaseService {

    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    protected CmsBtProductDao cmsBtProductDao;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private PriceService priceService;

    public List<CmsBtOperationLogModel_Msg> approval(AdvSearchProductApprovalMQMessageBody mqMessageBody) {
        long threadNo = Thread.currentThread().getId();
        $info(String.format("threadNo=%d 参数%s", threadNo, JacksonUtil.bean2Json(mqMessageBody)));
        Integer cartIdValue = mqMessageBody.getCartList().get(0);
        String channelId = mqMessageBody.getChannelId();
        String userName = mqMessageBody.getSender();
        List<String> productCodes = mqMessageBody.getProductCodes();
        Map<String, Object> params = mqMessageBody.getParams();
        List<CmsBtOperationLogModel_Msg> errorCodeList = new ArrayList<>();

        // 先判断是否是ready状态（miNiMall店铺不验证）
        TypeChannelBean cartType = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, cartIdValue.toString(), "en");
        if (!"3".equals(cartType.getCartType())) {
            JongoQuery queryObject = new JongoQuery();
            StringBuilder qryStr = new StringBuilder();
            qryStr.append("{'common.fields.code':{$in:#},$or:[");
            if (!CartEnums.Cart.TT.getId().equals(String.valueOf(cartIdValue))
                    && !CartEnums.Cart.LTT.getId().equals(String.valueOf(cartIdValue))
                    && !CartEnums.Cart.CN.getId().equals(String.valueOf(cartIdValue))
                    && !CartEnums.Cart.LCN.getId().equals(String.valueOf(cartIdValue))
                    && !CartEnums.Cart.DT.getId().equals(String.valueOf(cartIdValue)))
                qryStr.append("{'platforms.P" + cartIdValue + ".status':{$nin:['Ready','Approved']}},");
            else if (!CartEnums.Cart.LTT.getId().equals(String.valueOf(cartIdValue)))
                qryStr.append("{'common.fields.hsCodeStatus': '0'},");
            else
                qryStr.append("{'prodId':{$exists:false}},");
            qryStr.deleteCharAt(qryStr.length() - 1);
            qryStr.append("]}");
            queryObject.setQuery(qryStr.toString());
            queryObject.setParameters(productCodes);
            queryObject.setProjection("{'common.fields.code':1,'common.fields.hsCodeStatus':1,'_id':0}");

            List<CmsBtProductModel> prodList = productService.getList(channelId, queryObject);
            if (prodList != null && prodList.size() > 0) {
                // 存在未ready状态
                for (CmsBtProductModel prodObj : prodList) {
                    if (prodObj.getCommon() == null) {
                        CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                        errorInfo.setSkuCode(prodObj.getProdId().toString());
                        errorInfo.setMsg(String.format("有商品的common为空, 无法审批 cartIdValue:%s", cartIdValue));
                        errorCodeList.add(errorInfo);
                        continue;
                    }
                    CmsBtProductModel_Field field = prodObj.getCommon().getFields();

                    // 判断该商品税号是否设置
                    if (field != null &&
                            ("0".equals(field.getHsCodeStatus())
                                    && !CartEnums.Cart.LTT.getId().equals(String.valueOf(cartIdValue)))) {
                        CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                        errorInfo.setSkuCode(field.getCode());
                        errorInfo.setMsg(String.format("有商品商品没有设置税号, 无法审批 cartIdValue:%s", cartIdValue));
                        errorCodeList.add(errorInfo);
                        productCodes.remove(field.getCode());
                        continue;
                    }

                    // 判断该商品的状态为Pending
                    if (field != null && field.getCode() != null) {
                        CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                        errorInfo.setSkuCode(field.getCode());
                        errorInfo.setMsg(String.format("有商品pending状态, 无法审批 cartIdValue:%s", cartIdValue));
                        errorCodeList.add(errorInfo);
                        productCodes.remove(field.getCode());
                        continue;
                    }
                }
            }
        }

        //###############################################################################################################
        // 检查商品价格 notChkPrice=1时表示忽略价格问题
        Integer notChkPriceFlg = (Integer) params.get("notChkPrice");
        if (notChkPriceFlg == null) {
            notChkPriceFlg = 0;
        }
        if (notChkPriceFlg == 0) {
            Integer startIdx = (Integer) params.get("startIdx");
            if (startIdx == null) {
                startIdx = 0;
            }

            // 阀值
            CmsChannelConfigBean cmsChannelConfigBean = priceService.getMandatoryBreakThresholdOption(channelId, cartIdValue);
            Double breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D + 1.0;

            int idx = 0;
            for (String code : productCodes) {
                if (idx < startIdx) {
                    idx++;
                    continue;
                }
                idx++;
                // 获取产品的信息
                CmsBtProductModel productModel = productService.getProductByCode(channelId, code);
                List<Map<String, Object>> prodInfoList = new ArrayList<>();
                double priceLimit = 0;

                CmsBtProductModel_Platform_Cart ptmObj = productModel.getPlatform(cartIdValue);
                if (ptmObj == null) {
                    continue;
                }
                String cartName = Carts.getCart(cartIdValue).getName();
                List<BaseMongoMap<String, Object>> skuObjList = ptmObj.getSkus();
                if (skuObjList == null) {
                    continue;
                }

                try {
                    priceService.priceChk(channelId, productModel, cartIdValue);
                } catch (BusinessException ex) {
                    CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                    errorInfo.setSkuCode(code);
                    errorInfo.setMsg(ex.getMessage());
                    errorCodeList.add(errorInfo);
                    productCodes.remove(code);
                }
            }
        }

        // ############################################################################################################

        BulkJongoUpdateList prodBulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
        BulkJongoUpdateList grpBulkList = new BulkJongoUpdateList(1000, cmsBtProductGroupDao, channelId);
        List<String> newProdCodeList = new ArrayList<>();
        for (String code : productCodes) {
            // 获取产品的信息
            CmsBtProductModel productModel = productService.getProductByCode(channelId, code);
            if (productModel.getCommon() == null) {
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(code);
                errorInfo.setMsg(String.format("有商品的common为空, 无法审批 cartIdValue:%s", cartIdValue));
                errorCodeList.add(errorInfo);
                continue;
            }
            CmsBtProductModel_Field field = productModel.getCommon().getFields();
            if (field == null) {
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(code);
                errorInfo.setMsg(String.format("有商品的common.fields为空, 无法审批 cartIdValue:%s", cartIdValue));
                errorCodeList.add(errorInfo);
                continue;
            }

            List<String> strList = new ArrayList<>();

            // 如果该产品以前就是approved,则不更新pStatus
            String prodStatus = productModel.getPlatformNotNull(cartIdValue).getStatus();
            if ("3".equals(cartType.getCartType())) {
                strList.add("'platforms.P" + cartIdValue + ".status':'Approved','platforms.P" + cartIdValue + ".pStatus':'WaitingPublish'");
            } else {
                if (CmsConstants.ProductStatus.Ready.name().equals(prodStatus)) {
                    strList.add("'platforms.P" + cartIdValue + ".status':'Approved','platforms.P" + cartIdValue + ".pStatus':'WaitingPublish'");
                } else if (CmsConstants.ProductStatus.Approved.name().equals(prodStatus)) {
                    strList.add("'platforms.P" + cartIdValue + ".status':'Approved'");
                } else if (CartEnums.Cart.TT.getId().equals(String.valueOf(cartIdValue))
                        || CartEnums.Cart.LTT.getId().equals(String.valueOf(cartIdValue))
                        || CartEnums.Cart.CN.getId().equals(String.valueOf(cartIdValue))
                        || CartEnums.Cart.LCN.getId().equals(String.valueOf(cartIdValue))
                        || CartEnums.Cart.DT.getId().equals(String.valueOf(cartIdValue))) {
                    strList.add("'platforms.P" + cartIdValue + ".status':'Approved','platforms.P" + cartIdValue + ".pStatus':'WaitingPublish'");
                }
            }

            if (strList.isEmpty()) {
                $debug("产品未更新 code=" + code);
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(code);
                errorInfo.setMsg(String.format("产品未更新, 无法审批 cartIdValue:%s", cartIdValue));
                errorCodeList.add(errorInfo);
                continue;
            }
            newProdCodeList.add(code);
            String updStr = "{$set:{";
            updStr += StringUtils.join(strList, ',');
            updStr += ",'modified':#,'modifier':#}}";
            // 更新product表的status及pStatus
            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setQueryParameters(code);
            updObj.setUpdate(updStr);
            updObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userName);

            BulkWriteResult rs = prodBulkList.addBulkJongo(updObj);
            if (rs != null) {
                $debug(String.format("商品审批(product表) channelId=%s 执行结果=%s", channelId, rs.toString()));
            }
            // 更新group表的platformStatus
            JongoUpdate grpUpdObj = new JongoUpdate();
            grpUpdObj.setQuery("{'productCodes':#,'channelId':#,'cartId':#,'platformStatus':{$in:[null,'']}}");
            grpUpdObj.setQueryParameters(code, channelId, cartIdValue);
            grpUpdObj.setUpdate("{$set:{'platformStatus':'WaitingPublish','modified':#,'modifier':#}}");
            grpUpdObj.setUpdateParameters(DateTimeUtil.getNowTimeStamp(), userName);

            rs = grpBulkList.addBulkJongo(grpUpdObj);
            if (rs != null) {
                $debug(String.format("商品审批(group表) channelId=%s 执行结果=%s", channelId, rs.toString()));
            }
        }

        BulkWriteResult rs = prodBulkList.execute();
        if (rs != null) {
            $debug(String.format("商品审批(product表) channelId=%s 结果=%s", channelId, rs.toString()));
        }
        rs = grpBulkList.execute();
        if (rs != null) {
            $debug(String.format("商品审批(group表) channelId=%s 结果=%s", channelId, rs.toString()));
        }

        String msg;
        if (cartIdValue == null || cartIdValue == 0) {
            msg = "高级检索 商品审批(全平台)";
        } else {
            msg = "高级检索 商品审批";
        }

        // 插入上新程序
        $debug("批量修改属性 (商品审批) 开始记入SxWorkLoad表");
        long sta = System.currentTimeMillis();

        /**京东系和聚美的上新程序，blnSmartSx【上新标识】设置 : true added by Piao*/
        CartEnums.Cart _cartEnum = CartEnums.Cart.getValueByID(String.valueOf(cartIdValue));

        if (CartEnums.Cart.isJdSeries(_cartEnum) || CartEnums.Cart.JM.equals(_cartEnum)) {
            sxProductService.insertSxWorkLoad(channelId, newProdCodeList, cartIdValue, userName, true);
        } else {
            sxProductService.insertSxWorkLoad(channelId, newProdCodeList, cartIdValue, userName, false);
        }


        $debug("批量修改属性 (商品审批) 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));

        // 记录商品修改历史
        productStatusHistoryService.insertList(channelId, newProdCodeList, cartIdValue, EnumProductOperationType.ProductApproved, msg, userName);
        sta = System.currentTimeMillis();
        // 记录商品修改历史
        productStatusHistoryService.insertList(channelId, newProdCodeList, cartIdValue, EnumProductOperationType.ProductApproved, msg, userName);
        $info("批量修改属性 (商品审批) 记入状态历史表结束 耗时" + (System.currentTimeMillis() - sta));

        return errorCodeList;
    }

}
