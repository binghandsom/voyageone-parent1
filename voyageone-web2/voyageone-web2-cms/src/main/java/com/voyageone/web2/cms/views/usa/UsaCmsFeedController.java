package com.voyageone.web2.cms.views.usa;

import com.mongodb.WriteResult;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.usa.UsaFeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.bean.usa.FeedRequest;

import com.voyageone.web2.core.bean.UserSessionBean;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 美国CMS Feed相关Controller
 *
 * @Author rex.wu
 * @Create 2017-07-05 17:09
 */
@RestController
@RequestMapping(value = UsaCmsUrlConstants.FEED.ROOT)
public class UsaCmsFeedController extends BaseController {

    @Autowired
    private FeedInfoService feedInfoService;
    @Autowired
    private UsaFeedInfoService usaFeedInfoService;

    /**
     * 统一的当前语言环境提供
     */
    @Override
    public String getLang() {
        return "en";
    }

    @RequestMapping(value = UsaCmsUrlConstants.FEED.DETAIL)
    @ResponseBody
    public AjaxResponse getFeedDetail(@RequestBody FeedRequest reqParams) {

        String channelId = getUser().getSelChannelId();
        // 返回数据
        Map<String, Object> resultMap = new HashMap<>();
        CmsBtFeedInfoModel feed = usaFeedInfoService.getFeedById(channelId, reqParams.getId());
        resultMap.put("feed", feed);
        if (feed != null) {
            resultMap.put("brandList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, channelId, getLang()));
            resultMap.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, channelId, getLang()));
            resultMap.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, channelId, getLang()));
            resultMap.put("materialList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.material_TYPE_103, channelId, getLang()));
            resultMap.put("originList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.origin_TYPE_104, channelId, getLang()));
            resultMap.put("colorMap", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.colorMap_TYPE_105, channelId, getLang()));
        }
        return success(resultMap);
    }

    @RequestMapping(value = UsaCmsUrlConstants.FEED.GET_TOP_MODEL)
    public AjaxResponse getTopModel(@RequestBody FeedRequest reqParams) {
        return success(usaFeedInfoService.getTopFeedByModel(getUser().getSelChannelId(), reqParams.getCode(), reqParams.getModel(), reqParams.getTop()));
    }

    @RequestMapping(value = UsaCmsUrlConstants.FEED.SET_PRICE)
    public AjaxResponse setPrice(@RequestBody FeedRequest reqParams) {
        return success(usaFeedInfoService.setPrice(reqParams.getFeed()));
    }

    @RequestMapping(value = UsaCmsUrlConstants.FEED.APPROVE)
    public AjaxResponse approve(@RequestBody FeedRequest reqParams) {
        List<String> codeList = reqParams.getCodeList();
        Integer selAll = reqParams.getSelAll();
        if (Objects.equals(selAll, Integer.valueOf(1))) {
            // TODO: 2017/7/10 根据检索条件全量检索Feed
        }
        if (CollectionUtils.isEmpty(codeList)) {
            throw new BusinessException("No Code needs to be Approved.");
        }

        UserSessionBean user = getUser();
        usaFeedInfoService.approve(user.getSelChannelId(), codeList, reqParams.getApproveItems(), user.getUserName());

        return success(null);
    }

    /**
     * 更新Feed信息: Save 或 Submit至下一步 或 Approve
     *
     * @param reqParams 请求参数
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.UPDATE)
    public AjaxResponse updateFeed(@RequestBody FeedRequest reqParams) {
        UserSessionBean user = getUser();
        return success(usaFeedInfoService.saveOrSubmitFeed(user.getSelChannelId(), reqParams.getFeed(), Objects.equals(Integer.valueOf(1), reqParams.getFlag()), user.getUserName()));
    }

    /**
     * 条件查询feed列表信息
     * @param params
     * @return
     */
    @RequestMapping(value = UsaCmsUrlConstants.FEED.LIST)
    public AjaxResponse getFeedList(@RequestBody Map params) {
        Map<String, Object> resultBean = new HashMap<String, Object>();
        String selChannelId = getUser().getSelChannelId();
        //获取数据列表
        List<CmsBtFeedInfoModel> feedList = usaFeedInfoService.getFeedList(params, selChannelId);

        resultBean.put("feedList", feedList);
        //获取数据总量
        Long feedListTotal = usaFeedInfoService.getFeedCount(params, selChannelId);
        resultBean.put("feedListTotal", feedListTotal);

        // 返回feed信息
        return success(resultBean);
    }

    @RequestMapping(value = UsaCmsUrlConstants.FEED.UPDATEONE)
    public AjaxResponse upDateOne(@RequestBody Map params) {
        WriteResult writeResult = null;
        Double msrpPrice = null;
        Double price = null;
        if (params != null){
            HashMap<String, Object> queryMap = new HashMap<>();
            String code = (String) params.get("code");
            if (code != null){
                queryMap.put("code",code);
            }
            //这里有类型转换异常
            String priceClientMsrp = (String) params.get("priceClientMsrp");
            if (priceClientMsrp != null){
                msrpPrice =Double.parseDouble(priceClientMsrp);
            }

            String price1 = (String) params.get("price");
            if (price1 != null){
                 price =Double.parseDouble(price1);
            }


            CmsBtFeedInfoModel cmsBtFeedInfoModel = feedInfoService.getProductByCode(getUser().getSelChannelId(), code);
            if(cmsBtFeedInfoModel != null){
                cmsBtFeedInfoModel.setModifier(getUser().getUserName());
                final Double finalMsrpPrice = msrpPrice;
                final Double finalPrice = price;
                cmsBtFeedInfoModel.getSkus().forEach(sku->{
                    if(finalMsrpPrice != null){
                        sku.setPriceClientMsrp(finalMsrpPrice);
                    }
                    if(finalPrice != null){
                        sku.setPriceClientRetail(finalPrice);
                        sku.setPriceNet(finalPrice);
                    }
                });
            }
            String approvePricing = (String) params.get("approvePricing");

            if (approvePricing != null){
                cmsBtFeedInfoModel.setApprovePricing(approvePricing);
            }
            cmsBtFeedInfoModel = usaFeedInfoService.setPrice(cmsBtFeedInfoModel);
            writeResult = feedInfoService.updateFeedInfo(cmsBtFeedInfoModel);
        }


        return success(writeResult);
    }


}
