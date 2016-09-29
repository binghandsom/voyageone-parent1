package com.voyageone.task2.cms.service.product;

import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.ChannelService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取group表与product不一致的数据
 * Created by jason.jiang on 2016/08/30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSearchIsolatedProductTest {

    private static final Log log = LogFactory.getLog(CmsSearchIsolatedProductTest.class);

    @Autowired
    private ChannelService channelService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    @Test
    public void testIsolatedProduct() {
        // 取得所有店铺
        List<OrderChannelBean> list = channelService.getChannelListBy(null, null, -1, "1");
        if (list == null || list.isEmpty()) {
            log.error("无店铺(channel)数据！");
            return;
        }

        String grpFilterStr = "{'mainProductCode':1,'productCodes':1, 'platformStatus':1, 'numIId':1,'groupId':1,'_id':0}";

        Map<String, List<CmsBtProductGroupModel>> groupMap = new HashMap<>();

        for (OrderChannelBean chnObj : list) {
            // 对每个店铺进行处理
            String channelId = chnObj.getOrder_channel_id();
            // 先判断该店铺的cms_bt_product_cxxx表是否存在
            boolean exists = cmsBtProductDao.collectionExists(cmsBtProductDao.getCollectionName(channelId));
            if (!exists) {
                log.warn("本店铺对应的cms_bt_product_cxxx表不存在！ channelId=" + channelId);
                continue;
            }
            exists = cmsBtProductGroupDao.collectionExists(cmsBtProductGroupDao.getCollectionName(channelId));
            if (!exists) {
                log.warn("本店铺对应的cms_bt_product_group_cxxx表不存在！ channelId=" + channelId);
                continue;
            }

            // 取得该店铺的所有平台
            List<TypeChannelBean> cartList = TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, "en");
            if (cartList == null || cartList.isEmpty()) {
                log.error("本店铺无平台数据！ channelId=" + channelId);
                continue;
            }

            log.info("开始处理 channelId=" + channelId + ", " + chnObj.getFull_name());
            log.info("先处理product表 cms_bt_product_c" + channelId);

            // 先判断product表中的主商品code是否在group表中存在，不存在则输出来
            StringBuilder filterStr = new StringBuilder();
            filterStr.append("{");
            for (TypeChannelBean cartObj : cartList) {
                // 对指定店铺的每个平台进行处理
                String cartId = cartObj.getValue();
                filterStr.append("'platforms.P" + cartId + ".mainProductCode':1,");
                filterStr.append("'platforms.P" + cartId + ".status':1,");
                filterStr.append("'platforms.P" + cartId + ".pStatus':1,");
                filterStr.append("'platforms.P" + cartId + ".pReallyStatus':1,");
                filterStr.append("'platforms.P" + cartId + ".pNumIId':1,");

                List<CmsBtProductGroupModel> groupList = cmsBtProductGroupDao.selectWithProjection("{'cartId':" + cartId + "}", grpFilterStr, channelId);
                if (groupList == null || groupList.isEmpty()) {
                    log.error("该group表无数据 channelId=" + channelId + " cartId=" + cartId);
                } else {
                    groupMap.put(cartId, groupList);
                }
            }
            filterStr.append("'common.fields.code':1}");

            List<CmsBtProductModel> prodList = cmsBtProductDao.selectWithProjection(null, filterStr.toString(), channelId);
            if (prodList == null || prodList.isEmpty()) {
                log.warn("no product data channelId=" + channelId);
                continue;
            }
            for (CmsBtProductModel prodModel : prodList) {
                String prodCode = StringUtils.trimToNull(prodModel.getCommonNotNull().getFieldsNotNull().getCode());
                if (prodCode == null) {
                    log.warn("产品缺少关键数据(code) channelId=" + channelId + " objid=" + prodModel.get_id());
                    continue;
                }
                for (TypeChannelBean cartObj : cartList) {
                    // 对指定店铺的每个平台进行处理
                    int cartId = NumberUtils.toInt(cartObj.getValue());

                    CmsBtProductModel_Platform_Cart ptfObj = prodModel.getPlatformNotNull(cartId);
                    String mpCode = StringUtils.trimToNull(ptfObj.getMainProductCode());
                    if (mpCode == null) {
                        log.warn("产品缺少关键数据(maincode) channelId=" + channelId + " objid=" + prodModel.get_id());
                        continue;
                    }

                    List<CmsBtProductGroupModel> groupList = groupMap.get(cartObj.getValue());
                    if (groupList == null) {
                        log.error("该平台商品对应的group不存在 channelId=" + channelId + toProdString(prodModel, cartId));
                        continue;
                    }
                    // 找到其group对象
                    CmsBtProductGroupModel groupModel = null;
                    for (CmsBtProductGroupModel model : groupList) {
                        List<String> subCodeList = model.getProductCodes();
                        if (mpCode.equals(model.getMainProductCode())) {
                            groupModel = model;
                            // 下面是可能错误的数据
                            if (subCodeList == null || subCodeList.isEmpty()) {
                                log.error("该group未配置子商品 channelId=" + channelId + ", cartId=" + cartId + toGroupString(model));
                                continue;
                            }
                            if (!subCodeList.contains(prodCode)) {
                                log.error("该group不包含此子商品 channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(model));
                                continue;
                            }
                        }

                    }
                    if (groupModel == null) {
                        log.error("该商品对应的group不存在 channelId=" + channelId + toProdString(prodModel, cartId));
                        continue;
                    }

//                    // 然后判断numiid以及状态是否一致
//                    String spNumIId = StringUtils.trimToNull(ptfObj.getpNumIId());
//                    String spStatus = StringUtils.trimToNull(ptfObj.getStringAttribute("pStatus"));
//                    String sprStatus = StringUtils.trimToNull(ptfObj.getStringAttribute("pReallyStatus"));
//
//                    String gNumIId = StringUtils.trimToNull(groupModel.getNumIId());
//                    String gStatus = groupModel.getPlatformStatus() == null ? null : groupModel.getPlatformStatus().name();
//
//                    if (spNumIId == null) {
//                        if (gNumIId != null) {
//                            log.error("\tgroup与product的numIId不一致(product的为空) channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                        }
//                    } else {
//                        if (gNumIId == null) {
//                            log.error("\tgroup与product的numIId不一致(group的为空) channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                        } else {
//                            if (!spNumIId.equals(gNumIId)) {
//                                log.error("\tgroup与product的numIId不一致 channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                            }
//                        }
//                    }
//
//                    if (spStatus == null) {
//                        if (gStatus != null) {
//                            log.error("\t\tgroup与product的平台状态不一致(product的为空) channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                        }
//                    } else {
//                        if (gStatus == null) {
//                            log.error("\t\tgroup与product的平台状态不一致(group的为空) channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                        } else {
//                            if (!spStatus.equals(gStatus)) {
//                                log.error("\t\tgroup与product的平台状态不一致 channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                            }
//                        }
//                    }
//
//                    if (spStatus == null) {
//                        if (sprStatus != null) {
//                            log.error("\t\t\tCMS与平台的实际状态不一致(CMS的为空) channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                        }
//                    } else {
//                        if (sprStatus == null) {
//                            log.error("\t\t\tCMS与平台的实际状态不一致(平台的为空) channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                        } else {
//                            if (!spStatus.equals(sprStatus)) {
//                                log.error("\t\t\tCMS与平台的实际状态不一致 channelId=" + channelId + toProdString(prodModel, cartId) + toGroupString(groupModel));
//                            }
//                        }
//                    }

                }
            }

            log.info("再处理group表 cms_bt_product_group_c" + channelId);
            for (TypeChannelBean cartObj : cartList) {
                // 对指定店铺的每个平台进行处理
                int cartId = NumberUtils.toInt(cartObj.getValue());

                // 然后判断group表中的商品code是否在product表中存在，不存在则输出来
                List<CmsBtProductGroupModel> groupList = groupMap.get(cartObj.getValue());
                if (groupList == null) {
                    log.warn("no group data channelId=" + channelId + ", cartId=" + cartId);
                    continue;
                }
                for (CmsBtProductGroupModel groupModel : groupList) {
                    String mpCode = StringUtils.trimToNull(groupModel.getMainProductCode());
                    if (mpCode == null) {
                        log.warn("产品缺少关键数据 channelId=" + channelId + ", cartId=" + cartId + toGroupString(groupModel));
                        continue;
                    }
                    List<String> subCodeList = groupModel.getProductCodes();
                    if (subCodeList == null || subCodeList.isEmpty()) {
                        log.error("该group未配置子商品 channelId=" + channelId + ", cartId=" + cartId + toGroupString(groupModel));
                        continue;
                    }

                    boolean hasData = false;
                    for (CmsBtProductModel prodModel : prodList) {
                        String prodCode = StringUtils.trimToNull(prodModel.getCommonNotNull().getFieldsNotNull().getCode()); // 这里不再检查商品code是否为空，上面已经作过了
                        if (prodCode == null) {
                            continue;
                        }

                        CmsBtProductModel_Platform_Cart ptfObj = prodModel.getPlatformNotNull(cartId);
                        String smpCode = StringUtils.trimToNull(ptfObj.getMainProductCode()); // 这里不再检查主商品是否为空，上面已经作过了
                        if (mpCode.equals(smpCode)) {
                            hasData = true;
                            break;
                        }
                    }
                    if (!hasData) {
                        log.error("该group对应的商品不存在 channelId=" + channelId + ", cartId=" + cartId + toGroupString(groupModel));
                        continue;
                    }
                }
            }
        }
    }

    private static String toProdString(CmsBtProductModel model, int cartId) {
        StringBuilder rs = new StringBuilder();
        rs.append(", cartId=");
        rs.append(cartId);
        rs.append(", prodModel={'code':");
        rs.append(model.getCommonNotNull().getFieldsNotNull().getCode());

        CmsBtProductModel_Platform_Cart ptfObj = model.getPlatformNotNull(cartId);
        rs.append(", 'mCode':");
        rs.append(ptfObj.getMainProductCode());
        rs.append(", 'status':");
        rs.append(ptfObj.getStatus());
        rs.append(", 'numIId':");
        rs.append(ptfObj.getpNumIId());
        rs.append(", 'pStatus':");
        rs.append(ptfObj.getStringAttribute("pStatus"));
        rs.append(", 'pryStatus':");
        rs.append(ptfObj.getStringAttribute("pReallyStatus"));

        rs.append("}");
        return rs.toString();
    }

    private static String toGroupString(CmsBtProductGroupModel model) {
        StringBuilder rs = new StringBuilder();
        rs.append(", groupModel={'mCode':");
        rs.append(model.getMainProductCode());
        rs.append(", 'subList':");
        if (model.getProductCodes() != null) {
            rs.append(model.getProductCodes().toString());
        } else {
            rs.append("[]");
        }
        rs.append(", 'numIId':");
        rs.append(model.getNumIId());
        rs.append(", 'pStatus':");
        if (model.getPlatformStatus() != null) {
            rs.append(model.getPlatformStatus().name());
        }

        rs.append("}");
        return rs.toString();
    }
}