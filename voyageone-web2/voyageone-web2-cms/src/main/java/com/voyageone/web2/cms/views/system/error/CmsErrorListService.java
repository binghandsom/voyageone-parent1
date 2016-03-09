package com.voyageone.web2.cms.views.system.error;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.service.impl.cms.CmsBtChannelCategoryService;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import com.voyageone.web2.core.dao.ChannelShopDao;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.BusinessLogGetRequest;
import com.voyageone.web2.sdk.api.request.BusinessLogUpdateRequest;
import com.voyageone.web2.sdk.api.response.BusinessLogGetResponse;
import com.voyageone.web2.sdk.api.response.BusinessLogUpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 16/01/26
 */
@Service
public class CmsErrorListService extends BaseAppService{

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Autowired
    protected CmsBtChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    protected ChannelShopDao channelShopDao;

    /**
     * 获取检索页面初始化的master data数据
     * @param userInfo
     * @return
     */
    public Map<String, Object> getMasterData(UserSessionBean userInfo, String language) throws IOException{

        Map<String, Object> masterData = new HashMap<>();

        // 获取platform信息
        masterData.put("platformTypeList", TypeChannel.getTypeListSkuCarts(userInfo.getSelChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, language));

        // 获取category list
        masterData.put("categoryList", cmsBtChannelCategoryService.getFinallyCategoriesByChannelId(userInfo.getSelChannelId()));

        // 获取错误信息列表
        masterData.put("errorTypeList", TypeConfigEnums.MastType.errorType.getList(language));

        return masterData;
    }

    /**
     * 检索错误信息
     * @param params
     * @return
     */
    public BusinessLogGetResponse search(Map params, UserSessionBean userInfo) {
        BusinessLogGetRequest request=new BusinessLogGetRequest();
        request.setChannelId(userInfo.getSelChannelId());
        if (params.get("codes") != null)
            request.setCodes((ArrayList<String>)params.get("codes"));
        if (params.get("errType") != null)
            request.setErrType(params.get("errType").toString());
        if (params.get("productName") != null)
        request.setProductName(params.get("productName").toString());
        if (params.get("cartId") != null)
        request.setCartId(params.get("cartId").toString());
        if (params.get("catId") != null)
        request.setCatId(params.get("catId").toString());
        if (params.get("offset") != null)
        request.setOffset(Integer.valueOf(params.get("offset").toString()));
        if (params.get("rows") != null)
        request.setRows(Integer.valueOf(params.get("rows").toString()));

        return voApiClient.execute(request);
    }

    /**
     * 更新error错误状态
     * @param params
     * @return
     */
    public BusinessLogUpdateResponse updateStatus(Map params, UserSessionBean userInfo) {
        BusinessLogUpdateRequest request = new BusinessLogUpdateRequest();
        request.setSeq(Integer.parseInt(params.get("seq").toString()));
        request.setModifier(userInfo.getUserName());
        return voApiClient.execute(request);
    }
}
