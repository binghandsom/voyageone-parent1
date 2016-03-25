package com.voyageone.web2.cms.views.home.menu;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.impl.cms.ChannelCategoryService;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author Edward
 * @version 2.0.0, 15/12/2
 */
@Service
public class CmsMenuService extends BaseAppService{

    @Autowired
    private ChannelCategoryService cmsBtChannelCategoryService;

    @Autowired
    private CmsFeedCategoriesService cmsFeedCategoriesService;


    //TODO 临时使用
    private static final String CATEGORY_TYPE_FEED = "TH";

    /**
     * 获取该channel的category类型.
     * @param channelId
     * @return
     */
    public List<TypeChannelBean> getPlatformTypeList (String channelId, String language) {
        return TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_D, language);
    }

    /**
     * 根据userId和ChannelId获取Menu列表.
     */
    public List<CmsMtCategoryTreeModel> getCategoryTreeList (String cTypeId, String channelId){

        try {

            List<CmsMtCategoryTreeModel> categoryTreeList;

            switch (cTypeId) {
                case CATEGORY_TYPE_FEED:
                    categoryTreeList = cmsFeedCategoriesService.getFeedCategoryMap(channelId);
                    break;
                default:
                    // TODO 取得主数据的类目树,暂时使用feed的类目树
                    categoryTreeList = cmsBtChannelCategoryService.getCategoriesByChannelId(channelId);
                    break;
            }

            return categoryTreeList;
        } catch (IOException ex) {
          throw new BusinessException("获取类目失败");
        }
    }
}
