package com.voyageone.service.impl.cms.tools.product.common;

import com.voyageone.service.bean.cms.CmsBtBrandMappingBean;
import com.voyageone.service.daoext.cms.CmsBtBrandMappingDaoExt;
import com.voyageone.service.daoext.cms.CmsMtMasterBrandDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtMasterBrandModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/10/9.
 */
@Service
public class CmsMasterBrandMappingService extends BaseService {

    @Autowired
    private CmsMtMasterBrandDaoExt cmsMtMasterBrandDaoExt;

    private static int noMatchBrand = 3;

    public List<CmsMtMasterBrandModel> getMasterBrandListByChannelId(String channelId){
        Map<String, Object> data = new HashMap<>();
        //店铺渠道取得
        data.put("channelId", channelId);
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsByPage(data);
    }


    /**
     * Master品牌匹配初始化
     *
     * @param channelId
     * @param param
     * @return List<CmsMtMasterBrandModel>
     */
    public List<CmsMtMasterBrandModel> searchMasterBrandList(String channelId, Map param) {
        Map<String, Object> data = new HashMap<>();
        int curr = (Integer) param.get("curr");
        int size = (Integer) param.get("size");
        //显示第几页
        data.put("offset", (curr - 1) * size);
        //每页显示的数目
        data.put("size", size);
        //主品牌匹配状态
        List<Integer> statusList = (List<Integer>) param.get("statusList");
        //Master品牌
        String selectedBrand = (String) param.get("selectedBrand");
        //检索的品牌
        data.put("selectedBrand", selectedBrand);
        //未匹配的品牌
        if (statusList.contains(noMatchBrand)) {
            data.put("statusListFlag", noMatchBrand);
        }
        //匹配状态
        data.put("statusList", statusList);
        //店铺渠道取得
        data.put("channelId", channelId);
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsByPage(data);
    }

    /**
     * @param channelId
     * @param param
     * @return searchMasterBrandCount
     */
    public int searchMasterBrandCount(String channelId, Map param) {
        Map<String, Object> data = new HashMap<>();
        //主品牌匹配状态
        List<Integer> statusList = (List<Integer>) param.get("statusList");
        //检索的品牌
        String selectedBrand = (String) param.get("selectedBrand");
        //未匹配的品牌
        data.put("selectedBrand", selectedBrand);
        //未匹配的品牌
        if (statusList.contains(noMatchBrand)) {
            data.put("statusListFlag", noMatchBrand);
        }
        //匹配状态
        data.put("statusList", statusList);
        //店铺渠道取得
        data.put("channelId", channelId);
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsCount(data);
    }
}
