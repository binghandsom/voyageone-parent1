package com.voyageone.task2.cms.service.platform;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.mongodb.WriteResult;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jd.service.JdSaleService;
import com.voyageone.components.jumei.service.JumeiSaleService;
import com.voyageone.components.tmall.service.TbSaleService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取商品在平台的实际上下架状态
 *
 * @author jason.jiang on 2016/08/03
 * @version 2.0.0
 */
@Service
public class CmsGetPlatformStatusService extends BaseCronTaskService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private TbSaleService tbSaleService;
    @Autowired
    private JdSaleService jdSaleService;
    @Autowired
    private JumeiSaleService jmSaleService;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsGetPlatformStatusJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 取得参数值(指定平台和渠道的情况下),目前只用于测试
        String channelId = null;
        String cartIdStr = null;
        if (taskControlList != null && taskControlList.size() > 0) {
            for (TaskControlBean ctrlBean : taskControlList) {
                if ("channel_id".equals(ctrlBean.getCfg_name())) {
                    channelId = StringUtils.trimToNull(ctrlBean.getCfg_val1());
                } else if ("cart_id".equals(ctrlBean.getCfg_name())) {
                    cartIdStr = StringUtils.trimToNull(ctrlBean.getCfg_val1());
                }
            }
        }
        // 取得所有店铺
        List<ShopBean> list = null;
        if (channelId != null && cartIdStr != null) {
            ShopBean shopObj = Shops.getShop(channelId, cartIdStr);
            if (shopObj == null) {
                $warn("CmsGetPlatformStatusService 指定店铺及平台不存在！ channelId=%s, cartId=%s", channelId, cartIdStr);
                return;
            }
            list = new ArrayList<>(1);
            list.add(shopObj);
        } else {
            list = Shops.getShopList();
            if (list == null || list.isEmpty()) {
                $error("CmsGetPlatformStatusService 店铺及平台数据不存在！");
                return;
            }
        }

        long pageNo = 0;

        for (ShopBean shopObj : list) {
            // 对每个店铺进行处理
            if (StringUtils.trimToNull(shopObj.getApp_url()) == null) {
                $warn("CmsGetPlatformStatusService 店铺数据不完整！ channelId=%s, cartId=%s", channelId, cartIdStr);
                continue;
            }
            channelId = shopObj.getOrder_channel_id();
            cartIdStr = shopObj.getCart_id();
            // 先判断该店铺的cms_bt_product_cxxx表是否存在
            boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(channelId));
            if (!exists) {
                $warn("CmsGetPlatformStatusService 本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
                continue;
            }

            // 验证该店铺的平台配置
            TypeChannelBean cartBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.SKU_CARTS_53, channelId, cartIdStr);
            if (cartBean == null) {
                $error("CmsGetPlatformStatusService 本店铺无平台数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                continue;
            }

            // 对指定店铺的每个平台进行处理
            int cartId = NumberUtils.toInt(cartIdStr);

            if (PlatFormEnums.PlatForm.TM.getId().equals(shopObj.getPlatform_id())) {
                // 先从淘宝获取商品上下架状态
                List<Item> rsList = null;
                pageNo = 1;
                do {
                    rsList = null;
                    try {
                        // 查询下架
//                        rsList = tbSaleService.getInventoryProduct(channelId, cartIdStr, pageNo++, 200L);
                        rsList = tbSaleService.getInventoryProductForShelved(channelId, cartIdStr, pageNo++, 200L);
                    } catch (ApiException apiExp) {
                        $error(String.format("调用淘宝API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartIdStr), apiExp);
                        break;
                    } catch (Exception exp) {
                        $error(String.format("调用淘宝API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartIdStr), exp);
                        break;
                    }
                    if (rsList != null && rsList.size() > 0) {
                        List<String> numIIdList = rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList());
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.InStock.name());
                    }
                } while (rsList != null && rsList.size() == 200);

                pageNo = 1;
                do {
                    rsList = null;
                    try {
                        // 查询卖完
                        rsList = tbSaleService.getInventoryProductSoldOut(channelId, cartIdStr, pageNo++, 200L);
                    } catch (ApiException apiExp) {
                        $error(String.format("调用淘宝API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartIdStr), apiExp);
                        break;
                    } catch (Exception exp) {
                        $error(String.format("调用淘宝API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartIdStr), exp);
                        break;
                    }
                    if (rsList != null && rsList.size() > 0) {
                        List<String> numIIdList = rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList());
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.InStock.name());
                    }
                } while (rsList != null && rsList.size() == 200);

                pageNo = 1;
                do {
                    rsList = null;
                    try {
                        // 查询下架
                        rsList = tbSaleService.getInventoryProduct(channelId, cartIdStr, "violation_off_shelf", pageNo++, 200L);
                    } catch (ApiException apiExp) {
                        $error(String.format("调用淘宝API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartIdStr), apiExp);
                        break;
                    } catch (Exception exp) {
                        $error(String.format("调用淘宝API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartIdStr), exp);
                        break;
                    }
                    if (rsList != null && rsList.size() > 0) {
                        List<String> numIIdList = rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList());
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.Violation.name());
                    }
                } while (rsList != null && rsList.size() == 200);

                pageNo = 1;
                do {
                    rsList = null;
                    try {
                        // 查询上架
                        rsList = tbSaleService.getOnsaleProduct(channelId, cartIdStr, pageNo++, 200L);
                    } catch (ApiException apiExp) {
                        $error(String.format("调用淘宝API获取上架商品时API出错 channelId=%s, cartId=%s", channelId, cartIdStr), apiExp);
                        break;
                    } catch (Exception exp) {
                        $error(String.format("调用淘宝API获取上架商品时出错 channelId=%s, cartId=%s", channelId, cartIdStr), exp);
                        break;
                    }
                    if (rsList != null && rsList.size() > 0) {
                        List<String> numIIdList = rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList());
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.OnSale.name());
                    }
                } while (rsList != null && rsList.size() == 200);

            } else if (PlatFormEnums.PlatForm.JD.getId().equals(shopObj.getPlatform_id())) {
                // 再从京东获取商品上下架状态
                List<Ware> jdList = null;
                pageNo = 1;
                do {
                    jdList = null;
                    try {
                        // 查询上架
                        jdList = jdSaleService.getOnListProduct(channelId, cartIdStr, Long.toString(pageNo), "100");
                        pageNo++;
                    } catch (JdException apiExp) {
                        $error(String.format("调用京东API获取上架商品时API出错 channelId=%s, cartId=%s", channelId, cartIdStr), apiExp);
                        break;
                    } catch (Exception exp) {
                        $error(String.format("调用京东API获取上架商品时出错 channelId=%s, cartId=%s", channelId, cartIdStr), exp);
                        break;
                    }
                    if (jdList != null && jdList.size() > 0) {
                        List<String> numIIdList = jdList.stream().map(jdWare -> jdWare.getWareId().toString()).collect(Collectors.toList());
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.OnSale.name());
                    }
                } while (jdList != null && jdList.size() == 100);

                pageNo = 1;
                do {
                    jdList = null;
                    try {
                        // 查询下架
                        jdList = jdSaleService.getDeListProduct(channelId, cartIdStr, Long.toString(pageNo), "100");
                        pageNo++;
                    } catch (JdException apiExp) {
                        $error(String.format("调用京东API获取下架商品时API出错 channelId=%s, cartId=%s", channelId, cartIdStr), apiExp);
                        break;
                    } catch (Exception exp) {
                        $error(String.format("调用京东API获取下架商品时出错 channelId=%s, cartId=%s", channelId, cartIdStr), exp);
                        break;
                    }
                    if (jdList != null && jdList.size() > 0) {
                        List<String> numIIdList = jdList.stream().map(jdWare -> jdWare.getWareId().toString()).collect(Collectors.toList());
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.InStock.name());
                    }
                } while (jdList != null && jdList.size() == 100);

            } else if (PlatFormEnums.PlatForm.JM.getId().equals(shopObj.getPlatform_id())) {
                // 从聚美获取商品上下架状态
                List<String> numIIdList = null;
                int pageIdx = 1;
                do {
                    // 查询上架
                    numIIdList = jmSaleService.getOnListProduct(channelId, cartIdStr, pageIdx, 50);
                    pageIdx ++;
                    if (numIIdList != null && numIIdList.size() > 0) {
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.OnSale.name());
                    }
                } while (numIIdList != null && numIIdList.size() == 50);

                pageIdx = 1;
                do {
                    // 查询下架
                    numIIdList = jmSaleService.getDeListProduct(channelId, cartIdStr, pageIdx, 50);
                    pageIdx ++;

                    if (numIIdList != null && numIIdList.size() > 0) {
                        savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.InStock.name());
                    }
                } while (numIIdList != null && numIIdList.size() == 50);

            } else {
                $warn("CmsGetPlatformStatusService 缺少店铺信息 未知平台 [ChannelId:%s] [CartId:%s]", channelId, cartId);
                continue;
            }
        }
    }

    /**
     * 保存平台的实际商品状态
     */
    private void savePlatfromSts(String channelId, int cartId, List<String> numIIdList, String stsValue) {
        JongoUpdate updObj = new JongoUpdate();
        updObj.setQuery("{'platforms.P#.pNumIId':{$in:#},'platforms.P#.status':'Approved'}");
        updObj.setQueryParameters(cartId, numIIdList, cartId);
        updObj.setUpdate("{$set:{'platforms.P#.pReallyStatus':#,'modified':#,'modifier':#}}");
        updObj.setUpdateParameters(cartId, stsValue, DateTimeUtil.getNowTimeStamp(), getTaskName());
        WriteResult rs = cmsBtProductDao.updateMulti(updObj, channelId);
        numIIdList.forEach(item->$info(item + "  stsValue"));
        $debug("CmsGetPlatformStatusService.savePlatfromSts channelId=%s, cartId=%d, 结果=%s", channelId, cartId, rs.toString());
    }
}
