package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqSenderService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.SaveChannelCategoryMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_SellerCat;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gjl 2016/5/23.
 * @version 2.0.0
 */
@Service
public class CmsAddChannelCategoryService extends BaseViewService {

    @Autowired
    private ProductService productService;
    @Autowired
    private SellerCatService sellerCatService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private CmsMqSenderService cmsMqSenderService;

    private static final String DEFAULT_SELLER_CAT_CNT = "10";

    /**
     * 数据页面初始化(无产品信息)
     */
    public Map getChannelCategory(Map<String, Object> params, CmsSessionBean cmsSession) {
        String quyFlg = (String) params.get("isQuery");
        if (!"1".equals(quyFlg)) {
            // 添加店铺内分类
            return getChannelCategory4Product(params, cmsSession);
        }
        //channelId
        String channelId = (String) params.get("channelId");
        Map<String, Object> data = new HashMap<>();

        //cartId
        int cartId = StringUtils.toIntValue(params.get("cartId"));
        if (cartId == 0 || Carts.getCart(cartId) == null) {
            $warn("getChannelCategory cartI==0 " + params.toString());
            throw new BusinessException("未选择平台");
        }

        //Map<String, Boolean> emptyMap = new HashMap<>(0);
        data.put("orgChkStsMap", new HashMap<>(0));
        data.put("orgDispMap", new HashMap<>(0));

        //取得店铺渠道
        data.put("cartName", Carts.getCart(cartId).getName());

        //根据channelId在cms_bt_seller_cat取得对应的店铺内分类数据(树型结构)
        List<CmsBtSellerCatModel> channelCategoryList = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        data.put("channelCategoryList", channelCategoryList);
        return data;
    }

    /**
     * 数据页面初始化(有产品信息)
     */
    public Map getChannelCategory4Product(Map<String, Object> params, CmsSessionBean cmsSession) {
        //channelId
        String channelId = (String) params.get("channelId");
        Map<String, Object> data = new HashMap<>();

        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        //产品code
        List<String> codeList;
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            codeList = advanceSearchService.getProductCodeList(channelId, cmsSession);
        } else {
            codeList = (List) params.get("code");
        }
        if (codeList == null || codeList.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            throw new BusinessException("未选择商品");
        }

        //cartId
        int cartId = StringUtils.toIntValue(params.get("cartId"));
        if (cartId == 0) {
            $warn("getChannelCategory cartI==0 " + params.toString());
            throw new BusinessException("未选择平台");
        }
        //取得类目达标下面的个数
        data.put("cnt", getSellerCatCnt(cartId));

        //取得商品code
        JongoQuery query = new JongoQuery();
        query.setQuery("{'common.fields.code':{$in:#},'platforms.P" + cartId + "':{$exists:true}}");
        query.setParameters(codeList);
        query.setProjection("{'platforms.P" + cartId + ".sellerCats.cId':1}");
        //根据商品code取得对应的类目达标属性
        List<CmsBtProductModel> prodList = productService.getList(channelId, query);
        if (prodList == null || prodList.isEmpty()) {
            $warn("getChannelCategory cmsBtProductModel == null " + params.toString());
            throw new BusinessException("所选商品无指定平台的数据");
        }

        // 设置各个分类的勾选情况
        List<CmsBtSellerCatModel> orgSellCatList = sellerCatService.getSellerCatsByChannelCart(channelId, cartId, false);
        for (CmsBtSellerCatModel selCatObj : orgSellCatList) {
            selCatObj.setCartId(0);
        }
        Map<String, Boolean> orgChkStsMap = new HashMap<>(orgSellCatList.size());
        Map<String, Boolean> orgDispMap = new HashMap<>(orgSellCatList.size());

        for (CmsBtProductModel prodObj : prodList) {
            // 取得分类信息
            List<CmsBtProductModel_SellerCat> sellCatsList = prodObj.getPlatform(cartId).getSellerCats();
            if (sellCatsList == null || sellCatsList.isEmpty()) {
                $debug("getChannelCategory 指定商品无sellerCats数据 " + params.toString());
                continue;
            }

            for (CmsBtProductModel_SellerCat selectObj : sellCatsList) {
                //取得选择的cid
                for (CmsBtSellerCatModel selCatObj : orgSellCatList) {
                    if (org.apache.commons.lang3.StringUtils.trimToEmpty(selCatObj.getCatId()).equals(selectObj.getcId())) {
                        selCatObj.setCartId(selCatObj.getCartId() + 1);
                    }
                }
            }
        }

        for (CmsBtSellerCatModel selectObj : orgSellCatList) {
            if (selectObj.getIsParent() != null && selectObj.getIsParent() == 1) {
                // TODO -- 如果是父节点，则不处理，目前店铺内分类只有两级
                continue;
            }
            orgChkStsMap.put(selectObj.getCatId(), false);
            orgDispMap.put(selectObj.getCatId(), false);

            if (selectObj.getCartId() < codeList.size() && selectObj.getCartId() > 0) {
                orgDispMap.put(selectObj.getCatId(), true);
            }
            if (selectObj.getCartId() == codeList.size()) {
                orgChkStsMap.put(selectObj.getCatId(), true);
            }
        }
        data.put("orgChkStsMap", orgChkStsMap);
        data.put("orgDispMap", orgDispMap);

        //取得店铺渠道
        data.put("cartName", Carts.getCart(cartId).getName());

        //根据channelId在cms_bt_seller_cat取得对应的店铺内分类数据(树型结构)
        List<CmsBtSellerCatModel> channelCategoryList = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        data.put("channelCategoryList", channelCategoryList);
        return data;
    }

    /**
     * 保存数据到cms_bt_product
     */
    public void saveChannelCategory(Map<String, Object> params, CmsSessionBean cmsSession) {
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        List<String> codeList = (List) params.get("productIds");
        if (isSelAll == 1) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            //channelId
            String channelId = (String) params.get("channelId");
            codeList = advanceSearchService.getProductCodeList(channelId, cmsSession);
//            params.put("productIds", codeList);
        }
        if (codeList == null || codeList.isEmpty()) {
            $warn("没有code条件 params=" + params.toString());
            return;
        }

        //cartId
        int cartId = StringUtils.toIntValue(params.get("cartId"));
        if (cartId <= 0) {
            $warn("未选择平台/店铺 params=" + params.toString());
            return;
        }

        List<Map> sellerCats = (List) params.get("sellerCats");
        //数据check
        checkChannelCategory(sellerCats, cartId);

        // 开始批处理
        // params.put("_taskName", "saveChannelCategory");
        // sender.sendMessage(CmsMqRoutingKey.CMS_TASK_AdvSearch_AsynProcessJob, params);

        SaveChannelCategoryMQMessageBody mqMessageBody = new SaveChannelCategoryMQMessageBody();

        List<List<String>>productCodesList = CommonUtil.splitList(codeList,100);
        for (List<String> codes:productCodesList) {
            params.put("productIds",codes);
            mqMessageBody.setParams(params);
            mqMessageBody.setSender((String) params.get("userName"));
            cmsMqSenderService.sendMessage(mqMessageBody);
        }
        $info(JacksonUtil.bean2Json(params));

    }

    /**
     * 数据check
     */
    private void checkChannelCategory(List fullCatIdList, int cartId) {
        //取得类目达标下面的个数
        String cnt = getSellerCatCnt(cartId);
        //选择个数判断
        if (fullCatIdList.size() > Integer.parseInt(cnt)) {
            // 类目选择check
            throw new BusinessException("7000090", cnt);
        }
    }

    /**
     * 取得最大分类个数
     *
     * @param cartId int
     * @return cnt
     */
    private String getSellerCatCnt(int cartId) {
        String cartIdStr = String.valueOf(cartId);
        String cnt = Codes.getCodeName("SELLER_CAT_MAX_CNT", cartIdStr);
        if (StringUtils.isNullOrBlank2(cnt)) {
            cnt = DEFAULT_SELLER_CAT_CNT;
        }
        return cnt;
    }
}
