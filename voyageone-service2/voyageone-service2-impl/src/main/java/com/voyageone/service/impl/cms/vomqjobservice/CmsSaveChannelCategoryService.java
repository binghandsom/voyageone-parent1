package com.voyageone.service.impl.cms.vomqjobservice;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BulkJongoUpdateList;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.sx.PlatformWorkloadAttribute;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtSxCnProductSellercatModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 高级检索业务的批量设置店铺内分类
 *
 * @author jiangjusheng on 2016/09/07
 * @version 2.0.0
 */
@Service
public class CmsSaveChannelCategoryService extends VOAbsLoggable {

    private final CmsBtProductDao cmsBtProductDao;
    private final ProductService productService;
    private final SxProductService sxProductService;
    private final ProductStatusHistoryService productStatusHistoryService;
    private final ProductGroupService productGroupService;

    @Autowired
    public CmsSaveChannelCategoryService(CmsBtProductDao cmsBtProductDao, ProductService productService, SxProductService sxProductService, ProductStatusHistoryService productStatusHistoryService, ProductGroupService productGroupService) {
        this.cmsBtProductDao = cmsBtProductDao;
        this.productService = productService;
        this.sxProductService = sxProductService;
        this.productStatusHistoryService = productStatusHistoryService;
        this.productGroupService = productGroupService;
    }

    /**
     * 返回值由void改为Map，存放因“产品数据不完整”跳过的产品code和对于的错误提示信息
     * @param messageMap
     * @return
     */
    public List<CmsBtOperationLogModel_Msg> onStartup(Map<String, Object> messageMap) {
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        List<String> codeList = (List) messageMap.get("productIds");
        List<String> approvedCodeList = new ArrayList<>();
        if (codeList == null || codeList.isEmpty()) {
            /*$warn("没有code条件 params=" + messageMap.toString());
            return;*/
            throw new BusinessException(String.format("参数中没有code条件(productIds),params=%s"), JacksonUtil.bean2Json(messageMap));
        }

        //cartId
        int cartId = StringUtils.toIntValue(messageMap.get("cartId"));
        if (cartId <= 0) {
            /*$warn("未选择平台/店铺 params=" + messageMap.toString());
            return;*/
            throw new BusinessException(String.format("未选择平台/店铺(cartId),params=%s"), JacksonUtil.bean2Json(messageMap));
        }

        // 已勾选的分类
        List<Map<String, Object>> sellerCats = (List) messageMap.get("sellerCats");
        if (sellerCats == null || sellerCats.isEmpty()) {
            $info("sellerCats为空，可能是删除所有分类 params=" + messageMap.toString());
        }


        //channelId
        String channelId = (String) messageMap.get("channelId");
        //modifier
        String userName = (String) messageMap.get("userName");
        // 各分类的勾选状态(半选),对应的分类保留不变，其他的则按输入值sellerCats
        List<String> orgDispList = (List) messageMap.get("_orgDispList");
        if (orgDispList == null) {
            orgDispList = new ArrayList<>(0);
        }

        codeList = findOtheCodes(channelId, codeList, cartId);

        JongoQuery queryObject = new JongoQuery();
        queryObject.setQuery("{'common.fields.code':{$in:#}}");
        queryObject.setParameters(codeList);
        queryObject.setProjection("{'common.fields.code':1,'platforms.P" + cartId + "':1,'_id':0}");

        List<CmsBtProductModel> prodList = productService.getList(channelId, queryObject);
        if (ListUtils.isNull(prodList)){
            throw new BusinessException("查询不到产品,params=%s", JacksonUtil.bean2Json(messageMap));
        }

        // 被变更的分类类目ID
        Map<String, Map<String, Object>> updCatObjMap = null;
        if (CartEnums.Cart.CN.getValue() == cartId) {
            updCatObjMap = new HashMap<>();
        }

        // 更新cms_bt_product表的Platform->SellerCat字段
        BulkJongoUpdateList bulkList = new BulkJongoUpdateList(1000, cmsBtProductDao, channelId);
        for (CmsBtProductModel prodObj : prodList) {
            String prodCode = prodObj.getCommon().getFields().getCode();
            CmsBtProductModel_Platform_Cart platformObj = prodObj.getPlatform(cartId);
            if (platformObj == null) {
                // 产品数据不完整的记录到日志中
                $error("产品数据不完整，缺少Platform， prodCode=" + prodCode);

                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(prodCode);
                errorInfo.setMsg(String.format("产品数据不完整，缺少Platform，cartId=%s", cartId));
                failList.add(errorInfo);
                continue;
            }
            // 变更前的分类选择
            List<Map<String, Object>> catsList = platformObj.getSellerCatsByMap();
            if (catsList == null || catsList.isEmpty()) {
                // 如果该商品未设置店铺内分类，则直接保存输入值
                catsList = sellerCats;
                if (CartEnums.Cart.CN.getValue() == cartId) {
                    for (Map<String, Object> catObj : sellerCats) {
                        updCatObjMap.put((String) catObj.get("cId"), catObj);
                    }
                }

            } else {
                // 若已有设置，则要对比输入值与原始值，结果合并后保存
                Iterator<Map<String, Object>> catsEntries = null;
                if (CartEnums.Cart.CN.getValue() == cartId) {
                    // 先记录删除的分类
                    catsEntries = catsList.iterator();
                    while (catsEntries.hasNext()) {
                        Map<String, Object> catsObj = catsEntries.next();
                        String cId = (String) catsObj.get("cId");
                        if (!orgDispList.contains(cId) && !isInChecked(sellerCats, cId)) {
                            // 该分类已被删除
                            updCatObjMap.put(cId, catsObj);
                        }
                    }

                    // 然后记录新增的分类
                    for (Map<String, Object> vatObj : sellerCats) {
                        String cId = (String) vatObj.get("cId");
                        if (!isInChecked(catsList, cId)) {
                            updCatObjMap.put(cId, vatObj);
                        }
                    }
                }

                // 删除非保留的分类
                catsEntries = catsList.iterator();
                while (catsEntries.hasNext()) {
                    Map<String, Object> catsObj = catsEntries.next();
                    String cId = (String) catsObj.get("cId");
                    if (!orgDispList.contains(cId)) {
                        catsEntries.remove();
                    }
                }
                // 合并输入值
                if (sellerCats != null && sellerCats.size() > 0) {
                    catsList.addAll(sellerCats);
                }
            }

            // 更新产品的信息
            JongoUpdate updObj = new JongoUpdate();
            updObj.setQuery("{'common.fields.code':#}");
            updObj.setQueryParameters(prodCode);
            updObj.setUpdate("{$set:{'platforms.P" + cartId + ".sellerCats':#,'modified':#,'modifier':#}}");
            updObj.setUpdateParameters(catsList, DateTimeUtil.getNowTimeStamp(), userName);
            BulkWriteResult rs = bulkList.addBulkJongo(updObj);
            if (rs != null) {
                $debug(String.format("批量设置店铺内分类 channelId=%s 执行结果=%s", channelId, rs.toString()));
            }
            if(prodObj.getPlatform(cartId) != null && CmsConstants.ProductStatus.Approved.toString().equalsIgnoreCase(prodObj.getPlatform(cartId).getStatus())){
                approvedCodeList.add(prodObj.getCommon().getFields().getCode());
            }
        }
        BulkWriteResult rs = bulkList.execute();
        if (rs != null) {
            $debug(String.format("批量设置店铺内分类 channelId=%s 结果=%s", channelId, rs.toString()));
        }

        //取得approved的code插入
        $debug("批量设置店铺内分类 开始记入SxWorkLoad表");
        sxProductService.insertPlatformWorkload(channelId, cartId, PlatformWorkloadAttribute.SELLER_CIDS, approvedCodeList, userName);

        // 记录商品修改历史
        TypeChannelBean cartObj = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, Integer.toString(cartId), "cn");
        String msg = "";
        StringBuilder catNameStr = new StringBuilder();
        if (sellerCats != null && sellerCats.size() > 0) {
            catNameStr.append("：");
            int idx = 0;
            for (Map<String, Object> catItem : sellerCats) {
                if (idx > 0) {
                    catNameStr.append("，");
                }
                idx ++;
                catNameStr.append(catItem.get("cName"));
            }
        }

        if (cartObj == null) {
            msg = "高级检索 批量设置店铺内分类" + catNameStr.toString();
        } else {
            msg = "高级检索 批量设置[" + cartObj.getName() + "]店铺内分类" + catNameStr.toString();
        }
        productStatusHistoryService.insertList(channelId, codeList, cartId, EnumProductOperationType.BatchSetCats, msg, userName);
        return failList;
    }

    /**
     * 判断指定的分类类目是否在输入的选择项中
     */
    private boolean isInChecked(List<Map<String, Object>> sellerCats, String catId) {
        for (Map<String, Object> vatObj : sellerCats) {
            String cId = (String) vatObj.get("cId");
            if (catId.equals(cId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断指定的分类类目是否在输入的选择项中
     */
    private boolean isInList(List<CmsBtSxCnProductSellercatModel> modelList, String catId) {
        for (CmsBtSxCnProductSellercatModel vatObj : modelList) {
            if (catId.equals(vatObj.getCatId())) {
                return true;
            }
        }
        return false;
    }

    private List<String> findOtheCodes(String channelId, List<String> codes, Integer cartId){
        List<CmsBtProductGroupModel> productGroups = productGroupService.selectGroupByCodesAndCart(channelId, codes, cartId);
        if(ListUtils.notNull(productGroups)){
            productGroups.forEach(group->{
                if(ListUtils.notNull(group.getProductCodes())){
                    group.getProductCodes().forEach(code->{
                        if(!codes.contains(code)){
                            codes.add(code);
                        }
                    });
                }
            });
        }
        return codes;
    }
}
