package com.voyageone.service.impl.cms.tools.product.common;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
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
import java.util.stream.Collectors;

/**
 * Created by gjl on 2016/10/9.
 */
@Service
public class CmsMasterBrandMappingService extends BaseService {

    @Autowired
    private CmsMtMasterBrandDaoExt cmsMtMasterBrandDaoExt;


    public List<CmsMtMasterBrandModel> getMasterBrandListByChannelId(String channelId){
        Map<String, Object> data = new HashMap<>();
        //店铺渠道取得
        data.put("channelId", channelId);
        List<CmsMtMasterBrandModel> result = cmsMtMasterBrandDaoExt.searchBrandsByPage(data);
        if(result != null && result.size()>0){
            result = result.stream().filter(cmsMtMasterBrandModel -> !StringUtil.isEmpty(cmsMtMasterBrandModel.getMasterBrandEn())).collect(Collectors.toList());
        }
        //返回数据类型
        return result;
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
        data.put("selectedBrand", selectedBrand);
        if (statusList.contains(3)) {
            data.put("statusListFlag", "3");
        }
        data.put("statusList", statusList);
        //店铺渠道取得
        data.put("channelId", channelId);
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsByPage(data);
    }

    /**
     * @param channelId
     * @param param
     * @return
     */
    public int searchMasterBrandCount(String channelId, Map param) {
        Map<String, Object> data = new HashMap<>();
        //主品牌匹配状态
        List<Integer> statusList = (List<Integer>) param.get("statusList");
        data.put("statusList", statusList);
        //Master品牌
        String selectedBrand = (String) param.get("selectedBrand");
        data.put("selectedBrand", selectedBrand);
        if (statusList.contains(3)) {
            data.put("statusListFlag", "3");
        }
        //店铺渠道取得
        data.put("channelId", channelId);
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsCount(data);
    }
}
