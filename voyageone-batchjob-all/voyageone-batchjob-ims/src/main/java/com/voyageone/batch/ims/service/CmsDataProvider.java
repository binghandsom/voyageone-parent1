package com.voyageone.batch.ims.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voyageone.batch.ims.bean.UpJobParamBean;
import com.voyageone.batch.ims.bean.tcb.AbortTaskSignalInfo;
import com.voyageone.batch.ims.bean.tcb.TaskSignal;
import com.voyageone.batch.ims.bean.tcb.TaskSignalType;
import com.voyageone.batch.ims.dao.SkuInfoDao;
import com.voyageone.batch.ims.modelbean.*;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.MD5;
import com.voyageone.ims.enums.CmsFieldEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 2015/5/28.
 */
@Repository
public class CmsDataProvider {
    @Autowired
    private SkuInfoDao skuInfoDao;
    private Log logger = LogFactory.getLog(CmsDataProvider.class);

    /**
     * 本地测试环境： http://10.0.0.24:8080/VoyageOne_WebService/cms/products/getGroupModelProducts
     * 阿里云测试环境： http://121.41.58.229:8889/cms/products/getGroupModelProducts
     * 正式环境： http://api.voyageone.com.cn/cms/products/getGroupModelProducts
    */
    //private String cmsWebServiceUrl = null;
    //private String cmsWebServiceUrl = "http://10.0.0.24:8080/VoyageOne_WebService/cms/products/getGroupModelProducts";
    private String cmsWebServiceUrl = "http://api.voyageone.com.cn/cms/products/getGroupModelProducts";

    private int maxRetryTimes = 3;
    private int retryTimes = 0;

    /**
     * 填充model，code和sku属性，并设置主商品属性
     * @param workLoadBean
     * @param failCause
     * @return
     * @throws TaskSignal
     */
    public boolean fillCmsProp(WorkLoadBean workLoadBean, StringBuffer failCause) throws TaskSignal {
        ObjectMapper om = new ObjectMapper();
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();

        paramMap.put("cart_id", workLoadBean.getCart_id());
        paramMap.put("channel_id", workLoadBean.getOrder_channel_id());
        paramMap.put("model_id", String.valueOf(workLoadBean.getModelId()));
        paramMap.put("group_id", workLoadBean.getGroupId());

        String currentDate = new Date().toString();
        String prefixStr = "VoyageOne";
        dataMap.put("timeStamp", currentDate);
        dataMap.put("signature", MD5.getMD5(prefixStr + currentDate));
        dataMap.put("dataBody", paramMap);

        String param;
        try {
            param = om.writeValueAsString(dataMap);
            logger.debug("param:" + param);
            /*
            String result;
            {
                FileInputStream fis = new FileInputStream(new File("/home/leo/json-pa"));
                byte bytes[] = new byte[1024*10];
                fis.read(bytes);
                result = new String(bytes);

                CmsWorkLoad cmsWorkLoad = om.readValue(result, CmsWorkLoad.class);
                workLoadBean.setCmsModelProp(cmsWorkLoad.getModelProp());
                workLoadBean.setLevel(cmsWorkLoad.getLevel());
                workLoadBean.setLevelValue(cmsWorkLoad.getLevelValue());
            }
            */

            String result = null;
            if (cmsWebServiceUrl == null) {
                failCause.append("cmsWebServiceUrl不能为空");
                //这是整个任务的错误，而不是workload的，发生此错误时，应该停止整个任务，而不是单个的workload
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(failCause.toString()));
            }

            do {
                try {
                    result = HttpUtils.postWithException(cmsWebServiceUrl, param);
                    //如果上一步没有发生异常，那么重置尝试次数
                    retryTimes = 0;
                    break;
                } catch (IOException e) {
                    failCause.append(e.getMessage());
                    retryTimes++;
                }
            } while (retryTimes < maxRetryTimes);

            if (retryTimes >= maxRetryTimes) {
                String abortCause = String.format("访问cmsWebServiceUrl(%s)出现问题: %s", cmsWebServiceUrl, failCause);
                throw new TaskSignal(TaskSignalType.ABORT, new AbortTaskSignalInfo(abortCause.toString()));
            }

            logger.debug("wsResult: " + result);

            CmsPropWSGetResponseBean cmsPropWsGetResponse= om.readValue(result, CmsPropWSGetResponseBean.class);
            if (cmsPropWsGetResponse == null)
            {
                failCause.append("fail to parse response:" + result);
                return false;
            }
            else if (!"OK".equals(cmsPropWsGetResponse.getResult()))
            {
                failCause.append("NG, cause:" + cmsPropWsGetResponse.getMessageCode());
                return false;
            }
            else
            {
                CmsWorkLoad cmsWorkLoad = cmsPropWsGetResponse.getResultInfo();

                if (cmsWorkLoad.getModelProp().getPropMap() == null
                        || cmsWorkLoad.getModelProp().getCmsCodePropBeanList() == null
                        || cmsWorkLoad.getModelProp().getCmsCodePropBeanList().isEmpty())
                {
                    failCause.append("Cms model和code属性不能为空!");
                    return false;
                }

                workLoadBean.setCmsModelProp(cmsWorkLoad.getModelProp());
                workLoadBean.setLevel(cmsWorkLoad.getLevel());
                workLoadBean.setLevelValue(cmsWorkLoad.getLevelValue());
            }

            //sku
            if (!getAndFillLatestSkuInfo(workLoadBean, workLoadBean.getCmsModelProp()))
            {
                failCause.append("Cms sku 属性不能为空!");
                return false;
            }

            //设置主产品code
            CmsModelPropBean cmsModelPropBean = workLoadBean.getCmsModelProp();
            if (workLoadBean.getMainCode() == null || "".equals(workLoadBean.getMainCode())) {
                if (workLoadBean.getUpJobParam().getMethod().equals(UpJobParamBean.METHOD_UPDATE)) {
                    failCause.append("更新商品时，必须存在主商品");
                    return false;
                }
                //上传商品时，使用第一个code作为主商品
                CmsCodePropBean mainCmsCodeProp = cmsModelPropBean.getCmsCodePropBeanList().get(0);
                workLoadBean.setMainCode(mainCmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.code));
                workLoadBean.setMainProductProp(mainCmsCodeProp);
            } else {
                for (CmsCodePropBean cmsCodePropBean : workLoadBean.getCmsModelProp().getCmsCodePropBeanList()) {
                    if (workLoadBean.getMainCode().equals(cmsCodePropBean.getProp(CmsFieldEnum.CmsCodeEnum.code))) {
                        workLoadBean.setMainProductProp(cmsCodePropBean);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean getAndFillLatestSkuInfo(WorkLoadBean workload, CmsModelPropBean cmsModelProp) {
        String channelId = workload.getOrder_channel_id();
        for (CmsCodePropBean cmsCodeProp : cmsModelProp.getCmsCodePropBeanList())
        {
            String code = cmsCodeProp.getProp(CmsFieldEnum.CmsCodeEnum.code);
            List<SkuInfoBean> skuInfos = skuInfoDao.getSkuInfo(channelId, code);
            if (skuInfos == null || skuInfos.isEmpty()) {
                return false;
            }

            cmsCodeProp.getCmsSkuPropBeanList().clear();
            for (SkuInfoBean skuInfo : skuInfos)
            {
                CmsSkuPropBean cmsSkuProp = new CmsSkuPropBean();
                String sku = skuInfo.getSku();
                cmsSkuProp.setProp(CmsFieldEnum.CmsSkuEnum.sku, sku);

                String skuInventory = skuInfoDao.getSkuInventory(workload.getOrder_channel_id(), skuInfo.getCode(), skuInfo.getSku());
                cmsSkuProp.setProp(CmsFieldEnum.CmsSkuEnum.sku_quantity, skuInventory);

                String size = skuInfoDao.getSkuSize(channelId, code, sku);
                cmsSkuProp.setProp(CmsFieldEnum.CmsSkuEnum.size, size);

                //目前sku的价格都是code的价格
                String skuPrice = MasterDataMappingService.getCodePrice(workload, cmsCodeProp);
                if (skuPrice == null) {
                    skuPrice = "0";
                }
                cmsSkuProp.setProp(CmsFieldEnum.CmsSkuEnum.sku_price, skuPrice);

                cmsSkuProp.setCmsCodePropBean(cmsCodeProp);
                cmsCodeProp.getCmsSkuPropBeanList().add(cmsSkuProp);
            }
        }
        return true;
    }

    public void setCmsWebServiceUrl(String cmsWebServiceUrl) {
        this.cmsWebServiceUrl = cmsWebServiceUrl;
    }
}
