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
import com.voyageone.common.configs.CmsChannelConfigs;
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
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    public void approval(AdvSearchProductApprovalMQMessageBody mqMessageBody) {
        long threadNo =  Thread.currentThread().getId();
        $info(String.format("threadNo=%d 参数%s",threadNo, JacksonUtil.bean2Json(mqMessageBody)));
        List<Integer> cartList = mqMessageBody.getCartList();
        String channelId = mqMessageBody.getChannelId();
        String userName = mqMessageBody.getUserName();
        List<String> productCodes = mqMessageBody.getProductCodes();
        Map<String, Object> params = mqMessageBody.getParams();

        // 先判断是否是ready状态（minimall店铺不验证）
        List<Integer> newcartList = new ArrayList<>();
        for (Integer cartIdVal : cartList) {
            TypeChannelBean cartType = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, cartIdVal.toString(), "en");
            if (!"3".equals(cartType.getCartType())) {
                newcartList.add(cartIdVal);
            }
        }
        if (newcartList.size() > 0) {
            JongoQuery queryObject = new JongoQuery();
            StringBuilder qryStr = new StringBuilder();
            qryStr.append("{'common.fields.code':{$in:#},$or:[");
            for (Integer cartIdVal : newcartList) {
                if (!CartEnums.Cart.TT.getId().equals(String.valueOf(cartIdVal))
                        && !CartEnums.Cart.USTT.getId().equals(String.valueOf(cartIdVal))
                        && !CartEnums.Cart.DT.getId().equals(String.valueOf(cartIdVal)))
                    qryStr.append("{'platforms.P" + cartIdVal + ".status':{$nin:['Ready','Approved']}},");
                else
                    qryStr.append("{'common.fields.hsCodeStatus': '0'},");
            }
            qryStr.deleteCharAt(qryStr.length() - 1);
            qryStr.append("]}");
            queryObject.setQuery(qryStr.toString());
            queryObject.setParameters(productCodes);
            queryObject.setProjection("{'common.fields.code':1,'common.fields.hsCodeStatus':1,'_id':0}");

            List<CmsBtProductModel> prodList = productService.getList(channelId, queryObject);
            if (prodList != null && prodList.size() > 0) {
                // 存在未ready状态
                List<String> codeList = new ArrayList<>(prodList.size());
                List<String> hsCodeList = new ArrayList<>();

                for (CmsBtProductModel prodObj : prodList) {
                    if (prodObj.getCommon() == null) {
                        continue;
                    }
                    CmsBtProductModel_Field field = prodObj.getCommon().getFields();
                    if (field != null && field.getCode() != null) {
                        codeList.add(field.getCode());
                    }

                    if(field != null && "0".equals(field.getHsCodeStatus())){
                        hsCodeList.add(field.getCode());
                    }
                }

                if (hsCodeList.size() > 0 && (newcartList.contains(Integer.parseInt(CartEnums.Cart.TT.getId()))
                        || newcartList.contains(Integer.parseInt(CartEnums.Cart.USTT.getId()))
                        || newcartList.contains(Integer.parseInt(CartEnums.Cart.DT.getId())))) {
                    throw new BusinessException("有商品商品没有设置税号, 无法审批, 请修改. codes=" + JacksonUtil.bean2Json(hsCodeList));
                }else{
                    throw new BusinessException("有商品pending状态, 无法审批, 请修改. codes=" + JacksonUtil.bean2Json(codeList));
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
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.MANDATORY_BREAK_THRESHOLD);
            Double breakThreshold = null;
            if (cmsChannelConfigBean != null) {
                breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue1()) / 100D + 1.0;
            }

            Map<String, Object> rsMap = new HashMap<>();
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

                for (Integer cartIdVal : cartList) {
                    CmsBtProductModel_Platform_Cart ptmObj = productModel.getPlatform(cartIdVal);
                    if (ptmObj == null) {
                        continue;
                    }
                    String cartName = Carts.getCart(cartIdVal).getName();
                    List<BaseMongoMap<String, Object>> skuObjList = ptmObj.getSkus();
                    if (skuObjList == null) {
                        continue;
                    }
                    for (BaseMongoMap<String, Object> skuObj : skuObjList) {
                        double priceSale = skuObj.getDoubleAttribute("priceSale");
                        double priceRetail = skuObj.getDoubleAttribute("priceRetail");
                        Map<String, Object> priceInfo = new HashMap<>();
                        if (priceSale < priceRetail) {
                            priceInfo.put("priceRetail", priceRetail);
                        }
                        if (breakThreshold != null) {
                            priceLimit = priceRetail * breakThreshold;
                        }
                        if (breakThreshold != null && priceSale > priceLimit) {
                            priceInfo.put("priceLimit", priceLimit);
                        }
                        if (priceSale < priceRetail || (breakThreshold != null && priceSale > priceLimit)) {
                            priceInfo.put("priceSale", priceSale);
                            priceInfo.put("skuCode", skuObj.get("skuCode"));
                            priceInfo.put("cartName", cartName);
                            prodInfoList.add(priceInfo);
                        }
                    }
                }
                if (prodInfoList.size() > 0) {
                    rsMap.put("startIdx", idx);
                    rsMap.put("code", code);
                    rsMap.put("infoList", prodInfoList);
                    break;
                }
            }
            if (rsMap.size() > 0) {
               throw new BusinessException("商品价格有问题, msg=" + JacksonUtil.bean2Json(rsMap));
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
                continue;
            }
            CmsBtProductModel_Field field = productModel.getCommon().getFields();
            if (field == null) {
                continue;
            }

            List<String> strList = new ArrayList<>();
            for (Integer cartIdVal : cartList) {
                // 如果该产品以前就是approved,则不更新pStatus
                String prodStatus = productModel.getPlatformNotNull(cartIdVal).getStatus();
                TypeChannelBean cartType = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, cartIdVal.toString(), "en");
                if ("3".equals(cartType.getCartType())) {
                    strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");
                } else {
                    if (CmsConstants.ProductStatus.Ready.name().equals(prodStatus)) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved','platforms.P" + cartIdVal + ".pStatus':'WaitingPublish'");
                    } else if (CmsConstants.ProductStatus.Approved.name().equals(prodStatus)) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved'");
                    } else if (newcartList.contains(Integer.parseInt(CartEnums.Cart.TT.getId()))
                            || newcartList.contains(Integer.parseInt(CartEnums.Cart.USTT.getId()))
                            || newcartList.contains(Integer.parseInt(CartEnums.Cart.DT.getId()))) {
                        strList.add("'platforms.P" + cartIdVal + ".status':'Approved'");
                    }
                }
            }

            if (strList.isEmpty()) {
                $debug("产品未更新 code=" + code);
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
            grpUpdObj.setQuery("{'productCodes':#,'channelId':#,'cartId':{$in:#},'platformStatus':{$in:[null,'']}}");
            grpUpdObj.setQueryParameters(code, channelId, cartList);
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
        Integer cartId = (Integer) params.get("cartId");
        if (cartId == null || cartId == 0) {
            msg = "高级检索 商品审批(全平台)";
        } else {
            msg = "高级检索 商品审批";
        }
        for (Integer cartIdVal : cartList) {
            // 插入上新程序
            $info("批量修改属性 (商品审批) 开始记入SxWorkLoad表");
            long sta = System.currentTimeMillis();
            sxProductService.insertSxWorkLoad(channelId, newProdCodeList, cartIdVal, userName, false);
            $info("批量修改属性 (商品审批) 记入SxWorkLoad表结束 耗时" + (System.currentTimeMillis() - sta));

            sta = System.currentTimeMillis();
            // 记录商品修改历史
            productStatusHistoryService.insertList(channelId, newProdCodeList, cartIdVal, EnumProductOperationType.ProductApproved, msg, userName);
            $info("批量修改属性 (商品审批) 记入状态历史表结束 耗时" + (System.currentTimeMillis() - sta));
        }

    }

}
