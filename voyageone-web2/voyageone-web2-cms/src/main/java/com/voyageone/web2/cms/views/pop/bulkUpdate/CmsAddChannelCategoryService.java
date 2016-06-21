package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.CmsSessionBean;
import com.voyageone.web2.cms.views.search.CmsAdvanceSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gjl 2016/5/23.
 * @version 2.0.0
 */
@Service
public class CmsAddChannelCategoryService extends BaseAppService {

    @Autowired
    private ProductService productService;
    @Autowired
    private SellerCatService sellerCatService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsAdvanceSearchService advanceSearchService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    private static final String DEFAULT_SELLER_CAT_CNT = "10";
    private static final String DEFAULT_SELLER_CATS_FULL_CIDS = "0";

    /**
     * 数据页面初始化
     */
    public Map getChannelCategory(Map<String, Object> params,String lang) {
        Map<String, Object> data = new HashMap<>();
        //产品code
        List<String> codeList = (List) params.get("code");
        //channelId
        String channelId = (String) params.get("channelId");
        //cartId
        int cartId = StringUtils.toIntValue(params.get("cartId"));
        //取得类目达标下面的个数
        data.put("cnt", getSellerCatCnt(cartId));
        if(codeList.size()==1){
            //选择一条记录．根据code在cms_bt_product取得对应的属性记录
            for (String code : codeList) {
                //取得商品code
                //根据商品code取得对应的类目达标属性
                CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
                //取得叶子类目的catId
                List isSelectCidList = (List) cmsBtProductModel.getSellerCats().get("cIds");
                Map<String, Object> isSelectCidMap = new HashMap<>();
                if (isSelectCidList != null) {
                    for (Object anIsSelectCidList : isSelectCidList) {
                        //取得选择的cid
                        isSelectCidMap.put(anIsSelectCidList.toString(), true);
                    }
                }
                data.put("isSelectCid", isSelectCidMap);
            }
        }
        //取得店铺渠道
        List<TypeChannelBean> cartList= TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, lang);
        data.put("cartList",cartList);
        //根据channelId在cms_bt_seller_cat取得对应的达标数据
        List<CmsBtSellerCatModel> channelCategoryList = sellerCatService.getSellerCatsByChannelCart(channelId, cartId);
        data.put("channelCategoryList", channelCategoryList);
        return data;
    }

    /**
     * 保存数据到cms_bt_product
     */
    public Map<String, Object> saveChannelCategory(Map<String, Object> params, CmsSessionBean cmsSession) {
        //channelId
        String channelId = (String) params.get("channelId");
        Integer isSelAll = (Integer) params.get("isSelAll");
        if (isSelAll == null) {
            isSelAll = 0;
        }

        List<String> codeList = (List) params.get("productIds");
        if (isSelAll == 1 && (codeList == null || codeList.isEmpty())) {
            // 从高级检索重新取得查询结果（根据session中保存的查询条件）
            codeList = advanceSearchService.getProductCodeList(channelId, cmsSession);
        }
        if (codeList == null || codeList.isEmpty()) {
            $error("没有code条件 params=" + params.toString());
            return null;
        }

        //cartId
        int cartId = StringUtils.toIntValue(params.get("cartId"));
        saveChannelCategory(params, codeList, cartId);
        return null;
    }

    private void saveChannelCategory(Map<String, Object> params, List<String> codeList, int cartId) {
        List<Map> sellerCats = (List) params.get("sellerCats");

        //channelId
        String channelId = (String) params.get("channelId");
        //modifier
        String userName = (String) params.get("userName");

        //数据check
        checkChannelCategory(sellerCats, cartId);
        //更新cms_bt_product表的SellerCat字段

        HashMap<String, Object> updateMap3 = new HashMap<>();
        updateMap3.put("platforms.P" + cartId + ".sellerCats", sellerCats);
        updateMap3.put("modified", DateTimeUtil.getNowTimeStamp());
        updateMap3.put("modifier", userName);
        HashMap<String, Object> updateMap2 = new HashMap<>();
        updateMap2.put("$set", updateMap3);

        HashMap<String, Object> queryMap1 = new HashMap<>();
        queryMap1.put("$in", codeList);
        HashMap<String, Object> queryMap2 = new HashMap<>();
        queryMap2.put("$exists", true);
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("common.fields.code", queryMap1);
        queryMap.put("platforms.P" + cartId, queryMap2); // 要过滤掉platforms.Pxx未设置的情况

        WriteResult rslt = cmsBtProductDao.update(channelId, queryMap, updateMap2);
        $debug("更新店铺内分类结果：" + rslt.toString());

        //取得approved的code插入
        List<Integer> cartIdList = new ArrayList<>(1);
        cartIdList.add(cartId);
        productService.insertSxWorkLoad(channelId, codeList, cartIdList, userName);
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
    };

    /**
     * 取得最大达标个数
     * @param cartId
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

    /**
     * 同一店铺不同渠道的叶子类目插入形式不同
     * @param cartId
     * @return cnt
     */
    private String getSellerCatCategoryCid(int cartId) {
        String cartIdStr = String.valueOf(cartId);
        String cnt = Codes.getCodeName("SELLER_CATS_FULL_CIDS", cartIdStr);
        if (StringUtils.isNullOrBlank2(cnt)) {
            cnt = DEFAULT_SELLER_CATS_FULL_CIDS;
        }
        return cnt;
    }
}
