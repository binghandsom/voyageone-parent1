package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.SellerCatService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsBtSellerCatModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.views.home.menu.CmsMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/5/23.
 */
@Service
public class CmsAddChannelCategoryService extends BaseAppService {
    /**
     *
     * @param params
     * @return
     */
    @Autowired
    private ProductService productService;
    @Autowired
    private SellerCatService sellerCatService;
    @Autowired
    private CmsMenuService menuService;
    @Autowired
    ProductGroupService productGroupService;
    @Autowired
    private CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * 数据页面初始化
     * @param params
     * @param lang
     * @return data
     */
    public Map getChannelCategory(Map<String, Object> params,String lang) {
        Map data = new HashMap<>();
        //产品code
        List<String> codeList = (List) params.get("code");
        //channelId
        String channelId = (String) params.get("channelId");
        //cartId
        String cartId= (String) params.get("cartId");
        //取得类目达标下面的个数
        String cnt = Codes.getCodeName("MAX_SELLER_CAT_CNT", cartId);
        data.put("cnt", cnt);
        if(codeList.size()==1){
            //选择一条记录．根据code在cms_bt_product取得对应的属性记录
            for(int i=0;i<codeList.size();i++){
                //取得商品code
                String code = codeList.get(i).toString();
                //根据商品code取得对应的类目达标属性
                CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
                //取得叶子类目的catId
                List isSelectCidList = (List) cmsBtProductModel.getSellerCats().get("cIds");
                Map isSelectCidMap = new HashMap<>();
                if(isSelectCidList!=null){
                    for(int j=0;j<isSelectCidList.size();j++){
                        //取得选择的cid
                        isSelectCidMap.put(isSelectCidList.get(j).toString(),true);
                    }
                }
                data.put("isSelectCid",isSelectCidMap);
            }
        }
        //取得店铺渠道
        List<TypeChannelBean> cartList= menuService.getPlatformTypeList(channelId, lang);
        data.put("cartList",cartList);
        //根据channelId在cms_bt_seller_cat取得对应的达标数据
        List<CmsBtSellerCatModel> channelCategoryList = sellerCatService.getSellerCatsByChannelCart(channelId, Integer.parseInt(cartId));
        data.put("channelCategoryList", channelCategoryList);
        return data;
    }

    /**
     * 保存数据到cms_bt_product
     * @param params
     */
    public Map<String, Object> saveChannelCategory(Map<String, Object> params){
        //
        List<String> cIdsList = (List) params.get("cIds");
        List<String> cNamesList = (List) params.get("cNames");
        List<String> fullCNamesList = (List) params.get("fullCNames");
        List<String> fullCatCIdList = (List) params.get("fullCatCId");
        List<String> codeList = (List) params.get("code");
        //cartId
        int cartId= (int) params.get("cartId");
        //channelId
        String channelId = (String) params.get("channelId");
        //modifier
        String userName = (String) params.get("userName");
        //根据codeList取得相关联的code
        List<String> allCodeList=getAllCodeList(codeList, channelId, cartId);
        //数据check
        checkChannelCategory(fullCatCIdList);
        //更新cms_bt_product表的SellerCat字段
        Map<String, Object> resultMap = productService.updateSellerCat(cIdsList, cNamesList, fullCNamesList, fullCatCIdList, allCodeList, cartId, userName, channelId);
        //取得approved的code插入
        insertCmsBtSxWorkload(allCodeList, channelId, userName);
        return resultMap;
    }

    /**
     * 取得approved的code插入
     * @param allCodeList
     * @param channelId
     * @param userName
     */
    private void insertCmsBtSxWorkload(List<String> allCodeList, String channelId, String userName) {
        JomgoQuery queryObject = new JomgoQuery();
        //根据allCodeList在cms_bt_product取得已经approved的code
        String[] codeArr = new String[allCodeList.size()];
        codeArr = allCodeList.toArray(codeArr);
        queryObject.setQuery("{" + MongoUtils.splicingValue("fields.code", codeArr, "$in") +"fields.status"+"Approved"+"}");
        List<CmsBtProductModel> rst = cmsBtProductDao.select(queryObject, channelId);
        //根据rst的code在cms_bt_product_group获取所有的可上新的平台group信息
        List<CmsBtSxWorkloadModel> models = new ArrayList<>();
        for(CmsBtProductModel cmsBtProductModel:rst){
            //循环取得allCode
            String code = cmsBtProductModel.getFields().getCode();
            int cartId = Integer.parseInt(cmsBtProductModel.getCatId());
            CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, code, cartId);
            CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
            Long groupId= groupModel.getGroupId();
            model.setChannelId(groupModel.getChannelId());
            model.setGroupId(new Integer(String.valueOf(groupId)));
            model.setCartId(Integer.parseInt(cmsBtProductModel.getCatId()));
            model.setPublishStatus(0);
            model.setModifier(userName);
        }
        cmsBtSxWorkloadDaoExt.insertSxWorkloadModels(models);
    }

    /**
     * 根据codeList取得cms_bt_product_group相关的code
     * @param codeList
     * @return
     */
    public List<String> getAllCodeList(List<String> codeList,String channelId,int cartId){
        List<String> allCodeList = new ArrayList<>();
        // 获取产品对应的group信息
        for(String allCode:codeList){
            //循环取得allCode
            CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId,allCode, cartId);
            //循环取单条code
            for(String code:groupModel.getProductCodes()){
                allCodeList.add(code);
            }
        }
        return allCodeList;
    }
    /**
     * 数据check
     * @param fullCatCIdList
     */
    public void checkChannelCategory(List<String> fullCatCIdList){
        if(fullCatCIdList.size()>10){
            // 类目选择check
            throw new BusinessException("只能10個");
        }
    };
}
