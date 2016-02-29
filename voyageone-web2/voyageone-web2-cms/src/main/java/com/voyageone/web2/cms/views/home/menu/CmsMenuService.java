package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.cms.service.CmsBtChannelCategoryService;
import com.voyageone.cms.service.FeedToCmsService;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannel;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.dao.ChannelShopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 15/12/2
 */
@Service
public class CmsMenuService extends BaseAppService{

    @Autowired
    private ChannelShopDao channelShopDao;

    @Autowired
    private FeedToCmsService feedToCmsService;

    @Autowired
    private CmsBtChannelCategoryService cmsBtChannelCategoryService;

    //TODO 临时使用
    private static final String CATEGORY_TYPE_FEED = "Feed";

    /**
     * 获取该channel的category类型.
     * @param channelId
     * @return
     */
    public List<TypeChannelBean> getPlatformTypeList (String channelId, String language) {
        return TypeChannel.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
    }

    /**
     * 根据userId和ChannelId获取Menu列表.
     */
    public List<CmsMtCategoryTreeModel> getCategoryTreeList (String cTypeId, String channelId) {

        List<CmsMtCategoryTreeModel> categoryTreeList;

//        switch (cTypeId) {
//            case CATEGORY_TYPE_FEED:
//                categoryTreeList = feedToCmsService.getFeedCategory(channelId).getCategoryTree();
//                break;
//            default:
                // TODO 取得主数据的类目树,暂时使用feed的类目树
                categoryTreeList = cmsBtChannelCategoryService.getCategoriesByChannelId(channelId);
//                        feedToCmsService.getFeedCategory(channelId).getCategoryTree();
//                break;
//        }

        return categoryTreeList;
    }

}
