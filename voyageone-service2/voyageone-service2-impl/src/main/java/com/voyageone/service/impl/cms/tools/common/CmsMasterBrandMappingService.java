package com.voyageone.service.impl.cms.tools.common;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.CmsBtBrandMappingBean;
import com.voyageone.service.bean.cms.CmsMtMasterBrandBean;
import com.voyageone.service.daoext.cms.CmsBtBrandMappingDaoExt;
import com.voyageone.service.daoext.cms.CmsMtMasterBrandDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtMasterBrandModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gjl on 2016/10/9.
 */
@Service
public class CmsMasterBrandMappingService extends BaseService {

    @Autowired
    private CmsMtMasterBrandDaoExt cmsMtMasterBrandDaoExt;

    private static int noMatchBrand = 3;

    public List<CmsMtMasterBrandModel> getMasterBrandListByChannelId(String channelId) {
        Map<String, Object> data = new HashMap<>();
        //店铺渠道取得
        data.put("channelId", channelId);
        data.put("statusList", Arrays.asList(1));
        List<CmsMtMasterBrandModel> result = cmsMtMasterBrandDaoExt.searchBrandsByPage(data);
        if (result != null && result.size() > 0) {
            result = result.stream().filter(cmsMtMasterBrandModel -> !StringUtil.isEmpty(cmsMtMasterBrandModel.getMasterBrandEn()) && cmsMtMasterBrandModel.getMasterFlag() == 1).collect(Collectors.toList());
        }
        //返回数据类型
        return result;
    }

    public List<String> getFeedBrandByMasterBrand(String channelId, List<String> masterBrand) {
        List<CmsMtMasterBrandModel> cmsMtMasterBrandModels = getMasterBrandListByChannelId(channelId);
        List<String> feedBrand = cmsMtMasterBrandModels.stream()
                .filter(cmsMtMasterBrandModel -> masterBrand.contains(cmsMtMasterBrandModel.getMasterBrandEn()))
                .map(cmsMtMasterBrandModel -> ((CmsMtMasterBrandBean) cmsMtMasterBrandModel).getValue())
                .collect(Collectors.toList());
        return feedBrand;
    }

    public String getMasterBrandByFeedBrand(String channelId, String feedBrand){
        List<CmsMtMasterBrandModel> cmsMtMasterBrandModels = getMasterBrandListByChannelId(channelId);
        String masterBrand = cmsMtMasterBrandModels.stream()
                .filter(cmsMtMasterBrandModel -> feedBrand.equalsIgnoreCase(((CmsMtMasterBrandBean)cmsMtMasterBrandModel).getValue()))
                .map(cmsMtMasterBrandModel -> cmsMtMasterBrandModel.getMasterBrandEn())
                .findFirst().orElse("");
        return masterBrand;
    }


    /**
     * 检索Master品牌匹配的数据
     *
     * @param channelId
     * @param param
     * @return List<CmsMtMasterBrandModel>
     */
    public List<CmsMtMasterBrandModel> searchMasterBrandInfo(String channelId, Map param) {
        //Master品牌匹配共通属性
        Map<String, Object> data = masterBrandCommonInfo(channelId, param);
        //Master品牌匹配共通属性分頁用
        data.putAll(masterBrandCommonCnt(param));
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsByPage(data);
    }

    /***
     * 检索Master品牌匹配的数量
     *
     * @param channelId
     * @param param
     * @return searchMasterBrandCount
     */
    public int searchMasterBrandCount(String channelId, Map param) {
        //Master品牌匹配共通属性
        Map<String, Object> data = masterBrandCommonInfo(channelId, param);
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsCount(data);
    }

    /**
     * Master品牌管理的数据
     *
     * @param channelId
     * @param param
     * @return List<CmsMtMasterBrandModel>
     */
    public List<CmsMtMasterBrandModel> searchMasterBrandApplicationInfo(String channelId, Map param) {
        //Master品牌管理共通属性
        Map<String, Object> data = masterBrandCommonInfo(channelId, param);
        //Master品牌管理
        data.putAll(masterBrandApplicationCommonInfo(param));
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsByPage(data);
    }

    /**
     * Master品牌管理的数量
     *
     * @param channelId
     * @param param
     * @return searchMasterBrandCount
     */
    public int searchMasterBrandApplicationCount(String channelId, Map param) {
        //Master品牌管理共通属性
        Map<String, Object> data = masterBrandCommonInfo(channelId, param);
        //Master品牌管理分頁用
        data.putAll(masterBrandCommonCnt(param));
        //Master品牌管理
        data.putAll(masterBrandApplicationCommonInfo(param));
        //返回数据类型
        return cmsMtMasterBrandDaoExt.searchBrandsCount(data);
    }

    /**
     * Master品牌匹配共通属性
     *
     * @param channelId
     * @param param
     * @return data
     */
    private Map<String, Object> masterBrandCommonInfo(String channelId, Map param) {
        Map<String, Object> data = new HashMap<>();
        //主品牌匹配状态
        List<Integer> statusList = (List<Integer>) param.get("statusList");
        //Master品牌
        String feedBrand = (String) param.get("feedBrand");
        //检索的品牌
        data.put("feedBrand", feedBrand);
        //未匹配的品牌
        if (statusList.contains(noMatchBrand)) {
            data.put("statusListFlag", noMatchBrand);
        }
        //匹配状态
        data.put("statusList", statusList);
        //店铺渠道取得
        data.put("channelId", channelId);
        //返回数据类型
        return data;
    }

    /**
     * Master品牌匹配共通属性分頁用
     *
     * @param param
     * @return data
     */
    private Map<String, Object> masterBrandCommonCnt(Map param) {
        Map<String, Object> data = new HashMap<>();
        int curr = (Integer) param.get("curr");
        int size = (Integer) param.get("size");
        //显示第几页
        data.put("offset", (curr - 1) * size);
        //每页显示的数目
        data.put("size", size);
        //返回数据类型
        return data;
    }

    /**
     * Master品牌管理
     *
     * @param param
     * @return data
     */
    private Map<String, Object> masterBrandApplicationCommonInfo(Map param) {

        Map<String, Object> data = new HashMap<>();
        String masterBrandEn = (String) param.get("masterBrandEn");
        data.put("masterBrandEn", masterBrandEn);
        String masterChannelId = (String) param.get("channelId");
        data.put("masterChannelId", masterChannelId);
        String feedBrand = (String) param.get("feedBrand");
        data.put("feedBrand", feedBrand);
        String cartBrandName = (String) param.get("cartBrandName");
        data.put("cartBrandName", cartBrandName);
        //返回数据类型
        return data;
    }
}
