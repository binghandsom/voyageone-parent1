package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.components.transaction.SimpleTransaction;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.ImageGroupService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.impl.cms.StockSeparateService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.views.promotion.task.CmsTaskStockIncrementDetailService;
import com.voyageone.web2.cms.views.promotion.task.CmsTaskStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by jeff.duan on 2016/5/5.
 */
@Service
public class CmsImageGroupService extends BaseAppService {

    @Autowired
    private ImageGroupService imageGroupService;

    @Autowired
    MongoSequenceService commSequenceMongoService; // DAO: Sequence

    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init (Map<String, Object> param) {

        Map<String, Object> result = new HashMap<>();

        // 取得当前channel, 有多少个platform(Approve平台)
//        List<TypeChannelBean> typeChannelBeanList = TypeChannels.getTypeListSkuCarts((String)param.get("channelId"), "A", (String)param.get("lang"));
//        // 生成只有taskName和cartName的平台信息列表
//        List<Map<String, Object>> platformList = new ArrayList<>();
//        for (TypeChannelBean platformChannel : typeChannelBeanList) {
//            Map<String, Object> platform = new HashMap<>();
//            platform.put("name", platformChannel.getName());
//            platform.put("name", platformChannel.getName());
//            platform.put("show", false);
//            platformList.add(platform);
//        }
        result.put("platformList", TypeChannels.getTypeListSkuCarts((String)param.get("channelId"), "A", (String)param.get("lang")));

        // 品牌下拉列表
        result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String)param.get("channelId"), (String)param.get("lang")));
        // 产品类型下拉列表
        result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String)param.get("channelId"), (String)param.get("lang")));
        // 尺寸类型下拉列表
        result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String)param.get("channelId"), (String)param.get("lang")));

        return result;
    }

    /**
     * 检索
     *
     * @param param 客户端参数
     * @return 检索结果
     */
//    public List<CmsBtImageGroupModel> search (Map<String, Object> param) {
//
//    }

    /**
     * 新加/编辑ImageGroup信息
     *
     * @param param 客户端参数
     * @return 检索结果
     */
    public void save(Map<String, Object> param) {
        CmsBtImageGroupModel model = new CmsBtImageGroupModel();
        model.setChannelId((String)param.get("channelId"));
        model.setCartId(Integer.parseInt((String)param.get("platform")));
        model.setImageGroupId(commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_GROUP_ID));
        model.setImageGroupName((String)param.get("imageGroupName"));
        model.setImageType(Integer.parseInt((String)param.get("imageType")));
        model.setViewType(Integer.parseInt((String)param.get("viewType")));
        if ((param.get("brandName")) instanceof String) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setBrandName(lst);
        } else {
            model.setBrandName((List) param.get("brandName"));
        }
        if ((param.get("productType")) instanceof String) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setProductType(lst);
        } else {
            model.setProductType((List) param.get("productType"));
        }
        if ((param.get("sizeType")) instanceof String) {
            List lst = new ArrayList<String>();
            lst.add("All");
            model.setSizeType(lst);
        } else {
            model.setSizeType((List) param.get("sizeType"));
        }
        model.setActive(0);
        imageGroupService.save(model);
    }

}