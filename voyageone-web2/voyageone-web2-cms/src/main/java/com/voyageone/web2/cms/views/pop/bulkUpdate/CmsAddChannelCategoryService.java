package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
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
    ProductGroupService productGroupService;

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
         String cartId= (String) params.get("cartId");
        //取得类目达标下面的个数
        data.put("cnt", getSellerCatCnt(Integer.parseInt(cartId)));
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
        List<CmsBtSellerCatModel> channelCategoryList = sellerCatService.getSellerCatsByChannelCart(channelId, Integer.parseInt(cartId));
        data.put("channelCategoryList", channelCategoryList);
        return data;
    }

    /**
     * 保存数据到cms_bt_product
     */
    public Map<String, Object> saveChannelCategory(Map<String, Object> params){
        //
        List<String> cIdsList = (List) params.get("cIds");
        List<String> cNamesList = (List) params.get("cNames");
        List<String> fullCNamesList = (List) params.get("fullCNames");
        List<String> fullCatIdList = (List) params.get("fullCatId");
        List<String> codeList = (List) params.get("code");
        //cartId
        int cartId= Integer.parseInt(String.valueOf(params.get("cartId")));
        //channelId
        String channelId = (String) params.get("channelId");
        //modifier
        String userName = (String) params.get("userName");
        //根据codeList取得相关联的code
        List<String> allCodeList = getAllCodeList(codeList, channelId, cartId);
        $warn(String.format("该产品在group表中没有记录 codeList=%s, channelId=%s, cartId=%d", codeList.toString(), channelId, cartId));

        //同一店铺不同渠道的叶子类目插入形式不同
        String cnt = getSellerCatCategoryCid(cartId);
        List<String> editFullCNamesList = new ArrayList<>();
        if(cnt.equals(DEFAULT_SELLER_CATS_FULL_CIDS)){
            editFullCNamesList=fullCatIdList;
        }else{
            for(String fullCatId:fullCatIdList){
                String fullCatIds[]=fullCatId.split("-");
                editFullCNamesList.add(fullCatIds[fullCatIds.length-1]);
            }
        }
        //数据check
        checkChannelCategory(fullCatIdList, cartId);
        //更新cms_bt_product表的SellerCat字段
        Map<String, Object> resultMap = productService.updateSellerCat(cIdsList, cNamesList, fullCNamesList, editFullCNamesList, allCodeList, cartId, userName, channelId);
        //取得approved的code插入
        insertCmsBtSxWorkload(allCodeList, channelId, userName, cartId);
        return resultMap;
    }

    /**
     * 取得approved的code插入
     */
    private void insertCmsBtSxWorkload(List<String> allCodeList, String channelId, String userName, int cartId) {
        //根据allCodeList在cms_bt_product取得已经approved的code
        String[] codeArr = new String[allCodeList.size()];
        codeArr = allCodeList.toArray(codeArr);

        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setQuery("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") + "," + MongoUtils.splicingValue("fields.status", "Approved") + "}");
        List<CmsBtProductModel> rst = productService.getList(channelId, queryObject);

        //根据rst的code在cms_bt_product_group获取所有的可上新的平台group信息
        List<CmsBtSxWorkloadModel> models = new ArrayList<>();
        for(CmsBtProductModel cmsBtProductModel:rst){
            //循环取得allCode
            String code = cmsBtProductModel.getFields().getCode();
            CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, code, cartId);
            CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
            model.setChannelId(groupModel.getChannelId());
            model.setGroupId(groupModel.getGroupId());
            model.setCartId(cartId);
            model.setPublishStatus(0);
            model.setModifier(userName);
            model.setCreater(userName);
            models.add(model);
        }

        if (models.size() > 0) {
            productService.addtSxWorkloadModels(models);
        }
    }

    /**
     * 根据codeList取得cms_bt_product_group相关的code
     */
    public List<String> getAllCodeList(List<String> codeList, String channelId, int cartId) {
        List<String> allCodeList = new ArrayList<>();
        // 获取产品对应的group信息
        for (String allCode : codeList) {
            //循环取得allCode
            CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, allCode, cartId);
            if (groupModel == null || groupModel.getProductCodes() == null) {
                $warn(String.format("该产品在group表中没有记录 code=%s, channelId=%s, cartId=%d", allCode, channelId, cartId));
                continue;
            }
            //循环取单条code
            for (String code : groupModel.getProductCodes()) {
                allCodeList.add(code);
            }
        }
        return allCodeList;
    }

    /**
     * 数据check
     */
    public void checkChannelCategory(List<String> fullCatIdList, int cartId){
        //取得类目达标下面的个数
        String cnt = getSellerCatCnt(cartId);
        //选择个数判断
        if(fullCatIdList.size()>Integer.parseInt(cnt)){
            // 类目选择check
            throw new BusinessException("7000090",cnt);
        }
    };

    /**
     * 取得最大达标个数
     * @param cartId
     * @return cnt
     */
    public String getSellerCatCnt(int cartId){
        String cartIdStr = String.valueOf(cartId);
        String cnt = Codes.getCodeName("SELLER_CAT_MAX_CNT", cartIdStr);
        if(StringUtils.isNullOrBlank2(cnt)){
            cnt=DEFAULT_SELLER_CAT_CNT;
        }
        return cnt;
    }

    /**
     * 同一店铺不同渠道的叶子类目插入形式不同
     * @param cartId
     * @return cnt
     */
    public String getSellerCatCategoryCid(int cartId){
        String cartIdStr = String.valueOf(cartId);
        String cnt = Codes.getCodeName("SELLER_CATS_FULL_CIDS", cartIdStr);
        if(StringUtils.isNullOrBlank2(cnt)){
            cnt=DEFAULT_SELLER_CATS_FULL_CIDS;
        }
        return cnt;
    }
}
