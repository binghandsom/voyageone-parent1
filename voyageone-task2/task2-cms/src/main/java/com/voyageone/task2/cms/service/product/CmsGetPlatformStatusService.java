package com.voyageone.task2.cms.service.product;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.mongodb.WriteResult;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.voyageone.base.dao.mongodb.JomgoUpdate;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jd.service.JdSaleService;
import com.voyageone.components.tmall.service.TbSaleService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.task2.base.BaseTaskService;
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
public class CmsGetPlatformStatusService extends BaseTaskService {

    @Autowired
    private ChannelService channelService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private TbSaleService tbSaleService;
    @Autowired
    private JdSaleService jdSaleService;

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
        String channelIdStr = null;
        String cartIdStr = null;
        if (taskControlList != null && taskControlList.size() > 0) {
            for (TaskControlBean ctrlBean : taskControlList) {
                if ("channel_id".equals(ctrlBean.getCfg_name())) {
                    channelIdStr = StringUtils.trimToNull(ctrlBean.getCfg_val1());
                } else if ("cart_id".equals(ctrlBean.getCfg_name())) {
                    cartIdStr = StringUtils.trimToNull(ctrlBean.getCfg_val1());
                }
            }
        }
        // 取得所有店铺
        List<OrderChannelBean> list = channelService.getChannelListBy(channelIdStr, null, -1, "1");
        if (list == null || list.isEmpty()) {
            $error("CmsGetPlatformStatusService 无店铺(channel)数据！");
            return;
        }
        List<TypeChannelBean> cartList = null;
        long pageNo = 0;

        for (OrderChannelBean chnObj : list) {
            // 对每个店铺进行处理
            String channelId = chnObj.getOrder_channel_id();
            // 先判断该店铺的cms_bt_product_cxxx表是否存在
            boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(channelId));
            if (!exists) {
                $warn("CmsGetPlatformStatusService 本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
                continue;
            }

            // 取得该店铺的所有平台
            cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            if (cartList == null || cartList.isEmpty()) {
                $error("CmsGetPlatformStatusService 本店铺无平台数据！ channelId=" + channelId);
                continue;
            }
            if (cartIdStr != null) {
                // 指定平台时，过滤其他平台
                List<TypeChannelBean> newcartList = new ArrayList<>();
                for (TypeChannelBean cartObj : cartList) {
                    if (cartIdStr.equals(cartObj.getValue())) {
                        newcartList.add(cartObj);
                        break;
                    }
                }
                if (newcartList.isEmpty()) {
                    $error("CmsGetPlatformStatusService 本店铺无平台数据！ channelId=%s, cartId=%s", channelId, cartIdStr);
                    continue;
                }
                cartList = newcartList;
            }

            for (TypeChannelBean cartObj : cartList) {
                // 对指定店铺的每个平台进行处理
                int cartId = NumberUtils.toInt(cartObj.getValue());
                ShopBean shopProp = Shops.getShop(channelId, cartId);
                if (shopProp == null) {
                    $error("CmsGetPlatformStatusService 获取到店铺信息失败(shopProp == null)! [ChannelId:%s] [CartId:%s]", channelId, cartId);
                    continue;
                }

                if (PlatFormEnums.PlatForm.TM.getId().equals(shopProp.getPlatform_id())) {
                    // 先从淘宝获取商品上下架状态
                    List<Item> rsList = null;
                    pageNo = 1;
                    do {
                        rsList = null;
                        try {
                            // 查询上架
                            rsList = tbSaleService.getOnsaleProduct(channelId, cartObj.getValue(), pageNo++, 200L);
                        } catch (ApiException apiExp) {
                            $error("调用淘宝API获取上架商品时API出错", apiExp);
                            break;
                        } catch (Exception exp) {
                            $error("调用淘宝API获取上架商品时出错", exp);
                            break;
                        }
                        if (rsList != null && rsList.size() > 0) {
                            List<String> numIIdList = rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList());
                            savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.OnSale.name());
                        }
                    } while (rsList != null && rsList.size() == 200);

                    pageNo = 1;
                    do {
                        rsList = null;
                        try {
                            // 查询下架
                            rsList = tbSaleService.getInventoryProduct(channelId, cartObj.getValue(), pageNo++, 200L);
                        } catch (ApiException apiExp) {
                            $error("调用淘宝API获取下架商品时API出错", apiExp);
                            break;
                        } catch (Exception exp) {
                            $error("调用淘宝API获取下架商品时出错", exp);
                            break;
                        }
                        if (rsList != null && rsList.size() > 0) {
                            List<String> numIIdList = rsList.stream().map(tmItem -> tmItem.getNumIid().toString()).collect(Collectors.toList());
                            savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.InStock.name());
                        }
                    } while (rsList != null && rsList.size() == 200);

                } else if (PlatFormEnums.PlatForm.JD.getId().equals(shopProp.getPlatform_id())) {
                    // 再从京东获取商品上下架状态
                    List<Ware> jdList = null;
                    pageNo = 1;
                    do {
                        jdList = null;
                        try {
                            // 查询上架
                            jdList = jdSaleService.getOnListProduct(channelId, cartObj.getValue(), Long.toString(pageNo), "100");
                            pageNo++;
                        } catch (JdException apiExp) {
                            $error("调用京东API获取上架商品时API出错", apiExp);
                            break;
                        } catch (Exception exp) {
                            $error("调用京东API获取上架商品时出错", exp);
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
                            jdList = jdSaleService.getDeListProduct(channelId, cartObj.getValue(), Long.toString(pageNo), "100");
                            pageNo++;
                        } catch (JdException apiExp) {
                            $error("调用京东API获取下架商品时API出错", apiExp);
                            break;
                        } catch (Exception exp) {
                            $error("调用京东API获取下架商品时出错", exp);
                            break;
                        }
                        if (jdList != null && jdList.size() > 0) {
                            List<String> numIIdList = jdList.stream().map(jdWare -> jdWare.getWareId().toString()).collect(Collectors.toList());
                            savePlatfromSts(channelId, cartId, numIIdList, CmsConstants.PlatformStatus.InStock.name());
                        }
                    } while (jdList != null && jdList.size() == 100);

                } else {
                    $warn("CmsGetPlatformStatusService 缺少店铺信息 未知平台 [ChannelId:%s] [CartId:%s]", channelId, cartId);
                    continue;
                }
            }
        }
    }

    private void savePlatfromSts(String channelId, int cartId, List<String> numIIdList, String stsValue) {
        JomgoUpdate updObj = new JomgoUpdate();
        updObj.setQuery("{'platforms.P#.pNumIId':{$in:#},'platforms.P#.status':'Approved'}");
        updObj.setQueryParameters(cartId, numIIdList, cartId);
        updObj.setUpdate("{$set:{'platforms.P#.pReallyStatus':#,'modified':#,'modifier':#}}");
        updObj.setUpdateParameters(cartId, stsValue, DateTimeUtil.getNowTimeStamp(), getTaskName());
        WriteResult rs = cmsBtProductDao.updateMulti(updObj, channelId);
        $debug("CmsGetPlatformStatusService.savePlatfromSts channelId=%s, cartId=%d, 结果=%s", channelId, cartId, rs.toString());
    }
}
