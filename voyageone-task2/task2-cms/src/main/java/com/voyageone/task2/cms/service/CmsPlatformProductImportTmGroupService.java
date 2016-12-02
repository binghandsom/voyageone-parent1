package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.service.TbSaleService;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.CmsProductCodeChangeGroupService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 根据天猫现在商品分组情况来进行cms的产品code拆分合并
 *
 * @author morse on 2016/11/08
 * @version 2.6.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_TMGroupImportCms2Job)
public class CmsPlatformProductImportTmGroupService extends BaseMQCmsService {

    @Autowired
    private TbSaleService tbSaleService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsProductCodeChangeGroupService cmsProductCodeChangeGroupService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private CmsPlatformProductImportTmFieldsService cmsPlatformProductImportTmFieldsService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }
        String cartId = null;
        if (messageMap.containsKey("cartId")) {
            cartId = String.valueOf(messageMap.get("cartId"));
            if (!CartEnums.Cart.isTmSeries(CartEnums.Cart.getValueByID(cartId))) {
                $error("入参的平台id不是天猫系!");
                return;
            }
        }
        String numIId = null;
        if (messageMap.containsKey("numIId")) {
            numIId = String.valueOf(messageMap.get("numIId"));
        }
        String platformStatus = null;
        CmsConstants.PlatformStatus status = null;
        if (messageMap.containsKey("platformStatus")) {
            platformStatus = String.valueOf(messageMap.get("platformStatus"));
            if (platformStatus.equals(CmsConstants.PlatformStatus.InStock.name())) {
                status = CmsConstants.PlatformStatus.InStock;
            } else if (platformStatus.equals(CmsConstants.PlatformStatus.OnSale.name())) {
                status = CmsConstants.PlatformStatus.OnSale;
            } else {
                $error("入参平台状态platformStatus输入错误!");
                return;
            }
        }
        if (!StringUtils.isEmpty(numIId) && StringUtils.isEmpty(platformStatus)) {
            $error("入参指定了numIId,但未指定平台状态platformStatus!");
            return;
        }

        String runType = null; // runType=1的话，继续做取得天猫上商品属性并回写的事情
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        ShopBean shopBean = Shops.getShop(channelId, cartId);

        boolean isSuccess = true;
        try {
            if (StringUtils.isEmpty(numIId)) {
                // 未指定某个商品，全店处理
                executeAll(shopBean, channelId, cartId);
            } else {
                // 只处理指定的商品
                executeMove(shopBean, channelId, Integer.valueOf(cartId), numIId, status);
            }
        } catch (Exception e) {
            isSuccess = false;
            if (e instanceof BusinessException) {
                $error(e.getMessage());
            }
            e.printStackTrace();
        }

        if ("1".equals(runType) && StringUtils.isEmpty(numIId) && isSuccess) {
            // 继续做
            messageMap.remove("runType");
            messageMap.remove("numIId");
            messageMap.remove("platformStatus");
            cmsPlatformProductImportTmFieldsService.onStartup(messageMap);
        }
    }

    public void executeAll(ShopBean shopBean, String channelId, String cartId) throws Exception {
        Map<CmsConstants.PlatformStatus, List<String>> numIIdMap = tbSaleService.getTmNumIIdList(channelId, cartId);

        numIIdMap.forEach((status, numIIdList) -> {
            int index = 1;
            for (String numIId : numIIdList) {
                $info(String.format("%s-%s天猫[%s]分组 %d/%d", channelId, numIId, status.name(), index, numIIdList.size()));
                try {
                    executeMove(shopBean, channelId, Integer.valueOf(cartId), numIId, status);
                } catch (Exception e) {
                    if (e instanceof BusinessException) {
                        $error(e.getMessage());
                    }
                    e.printStackTrace();
                }
                index++;
            }
        });
    }

    /**
     * 根据numIId，取得平台sku，看看是不是要合并拆分group
     */
    private void executeMove(ShopBean shopBean, String channelId, int cartId, String numIId, CmsConstants.PlatformStatus status) throws Exception {
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByNumIId(channelId, cartId, numIId);

        Map<String, Object> fieldMap = new HashMap<>();
        if (cmsBtProductGroup == null) {
//            // TODO: 暂时没想到怎么解决，Group先拆出来，然后报个错出来吧
//            throw new BusinessException(String.format("天猫上的商品在cms里不存在! [numIId:%s] [Sku:%s]", numIId, tmSkuList));
            fieldMap.putAll(cmsPlatformProductImportTmFieldsService.getPlatformWareInfoItem(numIId, shopBean));
            List<String> tmSkuList = geTmSkuList(fieldMap); // 获取天猫上的sku列表
            // 拆分出一个新的group
            doMoveCodeToNewGroup(tmSkuList, numIId, channelId, cartId, status);
        } else {
            // deleted by morse.lu 2016/11/09 start
            // 貌似不需要取得产品信息
//            fieldMap.putAll(cmsPlatformProductImportTmFieldsService.getPlatformProduct(cmsBtProductGroup.getPlatformPid(), shopBean));
            // deleted by morse.lu 2016/11/09 end
            fieldMap.putAll(cmsPlatformProductImportTmFieldsService.getPlatformWareInfoItem(cmsBtProductGroup.getNumIId(), shopBean));

            // 拆分合并code
            doMoveCodeToAnotherGroup(fieldMap, cmsBtProductGroup, channelId, cartId, status);
        }
    }

    /**
     * 拆分合并code(创建一个新的group)
     */
    private void doMoveCodeToNewGroup(List<String> tmSkuList, String numIId, String channelId, int cartId, CmsConstants.PlatformStatus status) {
        // 根据要移动的skuList取得code列表
        Map<String, CmsBtProductModel> moveCods = getMoveCodesBySkuList(numIId, channelId, tmSkuList);

        boolean isFirst = true;
        CmsBtProductGroupModel newGroupModel = null;
        for (Map.Entry<String, CmsBtProductModel> entry : moveCods.entrySet()) {
            String moveCode = entry.getKey();
            CmsBtProductGroupModel sourceGroupModel = productGroupService.selectProductGroupByCode(channelId, moveCode, cartId);
            if (isFirst) {
                // 第一个code，创建一个group
                isFirst = false;
                newGroupModel = cmsProductCodeChangeGroupService.moveToNewGroup(channelId, cartId, moveCode, sourceGroupModel, getTaskName());
                // 回写下numIId,状态
                newGroupModel.setCreater(sourceGroupModel.getCreater());
                newGroupModel.setCreated(sourceGroupModel.getCreated());
                newGroupModel.setPlatformStatus(status);
                newGroupModel.setNumIId(numIId);
                productGroupService.update(newGroupModel);
            } else {
                // 第二个code开始，追加进这个
                cmsProductCodeChangeGroupService.moveToAnotherGroup(channelId, cartId, moveCode, sourceGroupModel, newGroupModel, getTaskName());
            }
            // 插入商品操作履历
            productStatusHistoryService.insert(channelId, moveCode,
                    status.name(), cartId, EnumProductOperationType.MoveCode,
                    "从Group:" + sourceGroupModel.getGroupId() + "移动到Group:" + newGroupModel.getGroupId(), getTaskName());
        }

        $info(String.format("新group做成! numIId:%s, productCodes:%s", numIId, moveCods.keySet()));
    }

    /**
     * 拆分合并code(合并在现有的group)
     */
    private void doMoveCodeToAnotherGroup(Map<String, Object> fieldMap, CmsBtProductGroupModel cmsBtProductGroup, String channelId, int cartId, CmsConstants.PlatformStatus status) {
        List<String> tmSkuList = geTmSkuList(fieldMap); // 获取天猫上的sku列表

        cmsBtProductGroup.getProductCodes().forEach(productCode -> {
            CmsBtProductModel productModel = cmsBtProductDao.selectByCode(productCode, channelId);
            productModel.getCommon().getSkus().forEach(sku -> {
                // modified by morse.lu 2016/11/18 start
                // 全小写比较skuCode
//                String skucode = sku.getSkuCode();
                String skucode = sku.getSkuCode().toLowerCase();
                // modified by morse.lu 2016/11/18 end
                if (tmSkuList.contains(skucode)) {
                    tmSkuList.remove(skucode);
                }
            });
        });

        // 根据要移动的skuList取得code列表
        Map<String, CmsBtProductModel> moveCods = getMoveCodesBySkuList(cmsBtProductGroup.getNumIId(), channelId, tmSkuList);

        cmsBtProductGroup.setPlatformStatus(status);

        moveCods.forEach((moveCode, productModel) -> {
            // 把这些code移到这个group下
            CmsBtProductGroupModel sourceGroupModel = cmsProductCodeChangeGroupService.moveToAnotherGroup(channelId, cartId, moveCode, cmsBtProductGroup, getTaskName());

            // 插入商品操作履历
            productStatusHistoryService.insert(channelId, moveCode,
                    productModel.getPlatform(cartId).getStatus(), cartId, EnumProductOperationType.MoveCode,
                    "从Group:" + sourceGroupModel.getGroupId() + "移动到Group:" + cmsBtProductGroup.getGroupId(), getTaskName());
        });

        if (moveCods.size() > 0) {
            $info(String.format("group追加了code! numIId:%s, 追加的productCodes:%s", cmsBtProductGroup.getNumIId(), moveCods.keySet()));
        } else {
            $info(String.format("group不需要追加code! numIId:%s", cmsBtProductGroup.getNumIId()));
        }

    }

    /**
     * 获取天猫上的sku列表
     */
    private List<String> geTmSkuList(Map<String, Object> fieldMap) {
        List<String> tmSkuList = new ArrayList<>(); // 天猫上的sku列表
        if (fieldMap.containsKey("sku")) {
            // sku级
            List<Map<String, Object>> listSkuVal = (List) fieldMap.get("sku");
            // modified by morse.lu 2016/11/18 start
            // 全小写比较skuCode
//            tmSkuList.addAll(listSkuVal.stream().map(skuInfo -> (String) skuInfo.get("sku_outerId")).collect(Collectors.toList()));
            tmSkuList.addAll(listSkuVal.stream().map(skuInfo -> skuInfo.get("sku_outerId").toString().toLowerCase()).collect(Collectors.toList()));
            // modified by morse.lu 2016/11/18 end
        } else if (fieldMap.containsKey("darwin_sku")) {
            // sku级
            List<Map<String, Object>> listSkuVal = (List) fieldMap.get("darwin_sku");
            // modified by morse.lu 2016/11/18 start
            // 全小写比较skuCode
//            tmSkuList.addAll(listSkuVal.stream().map(skuInfo -> (String) skuInfo.get("sku_outerId")).collect(Collectors.toList()));
            tmSkuList.addAll(listSkuVal.stream().map(skuInfo -> skuInfo.get("sku_outerId").toString().toLowerCase()).collect(Collectors.toList()));
            // modified by morse.lu 2016/11/18 end
        } else {
            // modified by morse.lu 2016/11/18 start
            // 全小写比较skuCode
//            tmSkuList.add(fieldMap.get("outer_id").toString());
            tmSkuList.add(fieldMap.get("outer_id").toString().toLowerCase());
            // modified by morse.lu 2016/11/18 end
        }

        return tmSkuList;
    }

    /**
     * 根据要移动的skuList取得code列表
     * @return Map<code, CmsBtProductModel>
     */
    private Map<String, CmsBtProductModel> getMoveCodesBySkuList(String numIId, String channelId, List<String> tmSkuList) {
        Map<String, CmsBtProductModel> moveCods = new HashMap<>();
        for (String skuCode : tmSkuList) {
            // 剩下的是天猫上有，但是group表里没有的sku
            // 那么把这些sku对应的code移到这个group下
            CmsBtProductModel productModel = cmsBtProductDao.selectBySkuIgnoreCase(skuCode, channelId);
            if (productModel == null) {
                throw new BusinessException(String.format("天猫上存在一个cms里没有的sku! [numIId:%s] [Sku:%s]", numIId, skuCode));
            }

            String code = productModel.getCommon().getFields().getCode();
            if (!moveCods.containsKey(code)) {
                moveCods.put(code, productModel);
            }
        }
        return moveCods;
    }
}
