package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.ecerp.interfaces.third.koala.beans.ItemEdit;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.ecerp.interfaces.third.koala.beans.PagedItemEdit;
import com.voyageone.ecerp.interfaces.third.koala.beans.request.ItemBatchStatusGetRequest;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.dao.cms.CmsBtPlatformNumiidDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.CmsProductCodeChangeGroupService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductStatusHistoryService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsBtPlatformNumiidModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 根据考拉现在商品分组情况来进行cms的产品code拆分合并
 * 考拉获取时的key是商品key，不是numIId，且拿不到
 *
 * @author morse on 2017/06/22
 * @version 2.6.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_KLGroupImportCms2Job)
public class CmsPlatformProductImportKlGroupService extends BaseMQCmsService {

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsProductCodeChangeGroupService cmsProductCodeChangeGroupService;
    @Autowired
    private ProductStatusHistoryService productStatusHistoryService;
    @Autowired
    private CmsPlatformProductImportKlFieldsService cmsPlatformProductImportKlFieldsService;

    @Autowired
    private KoalaItemService koalaItemService;

    @Autowired
    private CmsBtPlatformNumiidDao cmsBtPlatformNumiidDao;

    /**
     * runType=1的话，继续做取得考拉上商品属性并回写的事情  runType=2 从cms_bt_platform_numiid表里抽出numIId(商品key)去做
     */
    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {

        String channelId = null;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        }
        String cartId = CartEnums.Cart.KL.getId();
        String pid = null; // 产品id，对应考拉的商品key
        if (messageMap.containsKey("pid")) {
            pid = String.valueOf(messageMap.get("pid"));
        }

        String runType = null; // runType=1的话，继续做取得考拉上商品属性并回写的事情
        if (messageMap.containsKey("runType")) {
            runType = String.valueOf(messageMap.get("runType"));
        }

        CmsPlatformProductImportKlFieldsService.PlatformStatus status = null;
        if (!"2".equals(runType)) {
            String platformStatus = null;
            if (messageMap.containsKey("platformStatus")) {
                platformStatus = String.valueOf(messageMap.get("platformStatus"));
                status = CmsPlatformProductImportKlFieldsService.PlatformStatus.parse(platformStatus);
                if (status == null) {
                    $error("入参平台状态platformStatus输入错误!");
                    return;
                }
            }
            if (StringUtils.isEmpty(pid) && StringUtils.isEmpty(platformStatus)) {
                $error("入参未指定pid,必须指定平台状态platformStatus!");
                return;
            }
        }

        KoalaConfig shopBean = Shops.getShopKoala(channelId, cartId);

        boolean isSuccess = true;
        try {
            if ("2".equals(runType)) {
                // 从cms_bt_platform_numiid表里抽出numIId(商品key)去做
                isSuccess = executeFromTable(shopBean, channelId, Integer.parseInt(cartId));
            } else if (StringUtils.isEmpty(pid)) {
                // 未指定某个商品，全店处理
                isSuccess = executeAll(shopBean, channelId, cartId, status);
            } else {
                // 只处理指定的商品
                executeSingle(shopBean, channelId, cartId, pid);
            }
        } catch (Exception e) {
            isSuccess = false;
            if (e instanceof BusinessException) {
                $error(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }

        $info("finish MqTask[CmsPlatformProductImportKlGroupService根据考拉现在商品分组情况来进行cms的产品code拆分合并]");

        if ("1".equals(runType) && StringUtils.isEmpty(pid) && isSuccess) {
            // 继续做
            messageMap.remove("runType");
            messageMap.remove("pid");
            cmsPlatformProductImportKlFieldsService.onStartup(messageMap);
        }
    }

    private boolean executeFromTable(KoalaConfig shopBean, String channelId, int cartId) {
        Map<String, Object> seachParam = new HashMap<>();
        seachParam.put("channelId", channelId);
        seachParam.put("cartId", cartId);
        seachParam.put("status", "0");
        List<CmsBtPlatformNumiidModel> listModel = cmsBtPlatformNumiidDao.selectList(seachParam);
        if (ListUtils.isNull(listModel)) {
            $warn("cms_bt_platform_numiid表未找到符合的数据!");
            return false;
        }
        List<String> listSuccessPid = new ArrayList<>();
        List<String> listErrorPid = new ArrayList<>();
        boolean hasErrorData = false;
        int index = 1;
        int pageSize = 300;
        List<List<String>> listAllPid = CommonUtil.splitList(listModel.stream().map(CmsBtPlatformNumiidModel::getNumIid).collect(Collectors.toList()), pageSize);
        for (List<String> listPid : listAllPid) {
            ItemEdit[] itemEdits;
            try {
                itemEdits = koalaItemService.batchGet(shopBean, listPid.toArray(new String[listPid.size()]));
            } catch (Exception e) {
                $error(String.format("调用获取商品信息API失败!channelId:%s, cartId:%s, pid:%s!" + e.getMessage(), channelId, cartId, listPid));
                index = index + pageSize;
                continue;
            }

            for (ItemEdit itemEdit : itemEdits) {
                String platformPid = itemEdit.getKey();
                $info(String.format("%s-%s-%s考拉分组 %d/%d", channelId, cartId, platformPid, index, listModel.size()));
                try {
                    executeMove(shopBean, channelId, cartId, itemEdit);
                    listSuccessPid.add(platformPid);
                } catch (Exception e) {
                    hasErrorData = true;
                    listErrorPid.add(platformPid);
                    if (e instanceof BusinessException) {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 分组失败!" + e.getMessage(), channelId, cartId, platformPid));
                    } else {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 分组失败!", channelId, cartId, platformPid));
                        e.printStackTrace();
                    }
                }
                index++;
            }
            // 300一更新
            cmsPlatformProductImportKlFieldsService.updateCmsBtPlatformNumiid(channelId, cartId, listSuccessPid, listErrorPid);
            listSuccessPid.clear();
            listErrorPid.clear();
        }

        return hasErrorData;
    }

    private void executeSingle(KoalaConfig shopBean, String channelId, String cartId, String platformPid) throws Exception {
        ItemEdit itemEdit = koalaItemService.itemGet(shopBean, platformPid);
        if (itemEdit == null) {
            throw new BusinessException(String.format("考拉平台未找到此商品key[%s]", platformPid));
        }
        executeMove(shopBean, channelId, Integer.parseInt(cartId), itemEdit);
    }

    private boolean executeAll(KoalaConfig shopBean, String channelId, String cartId, CmsPlatformProductImportKlFieldsService.PlatformStatus status) throws Exception {
        List<String> errorList = new ArrayList<>();
        ItemBatchStatusGetRequest req = new ItemBatchStatusGetRequest();
        req.setItemEditStatus(Integer.parseInt(status.value()));
        int pageNo = 1;
        int pageSize = 100;
        int index = 1;
        while (true) {
            req.setPageNo(pageNo++);
            req.setPageSize(pageSize);
            PagedItemEdit edit = koalaItemService.batchStatusGet(shopBean, req);
            ItemEdit[] itemEdits = edit.getItemEditList();
            if (itemEdits == null || itemEdits.length == 0) {
                break;
            }

            for (ItemEdit itemEdit : itemEdits) {
                $info(String.format("%s-%s-%s考拉[%s]分组 %d/%d", channelId, cartId, itemEdit.getKey(), status.name(), index, edit.getTotalCount()));
                try {
                    executeMove(shopBean, channelId, Integer.valueOf(cartId), itemEdit);
                } catch (Exception e) {
                    errorList.add(itemEdit.getKey());
                    if (e instanceof BusinessException) {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 分组失败!" + e.getMessage(), channelId, cartId, itemEdit.getKey()));
                    } else {
                        $error(String.format("channelId:%s, cartId:%s, pid:%s 分组失败!", channelId, cartId, itemEdit.getKey()));
                        e.printStackTrace();
                    }
                }
                index++;
            }

            if (itemEdits.length < pageSize) {
                break;
            }
        }

        return errorList.size() == 0;
    }

    /**
     * 根据商品key，取得平台sku，看看是不是要合并拆分group
     */
    private void executeMove(KoalaConfig shopBean, String channelId, int cartId, ItemEdit itemEdit) throws Exception {
        String platformPid = itemEdit.getKey();
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByPlatformPid(channelId, cartId, platformPid);
        CmsPlatformProductImportKlFieldsService.PlatformStatus klPlatformStatus = CmsPlatformProductImportKlFieldsService.PlatformStatus.parse(itemEdit.getRawItemEdit().getItemOnlineStatus());
        CmsConstants.PlatformStatus status;
        if (klPlatformStatus == CmsPlatformProductImportKlFieldsService.PlatformStatus.ON_SALE) {
            status = CmsConstants.PlatformStatus.OnSale;
        } else {
            status = CmsConstants.PlatformStatus.InStock;
        }

        List<String> klSkuList = Arrays.stream(itemEdit.getSkuList()).map(model -> model.getRawSku().getBarCode().toLowerCase()).collect(Collectors.toList()); // 考拉barCode处设置了skuCode
        if (klSkuList.size() == 0) {
            // 一般不会，出现的话要调查
            throw new BusinessException(String.format("pid:%s 考拉skuCode取得失败!", platformPid));
        }

        Map<String, Object> fieldMap = new HashMap<>();
        if (cmsBtProductGroup == null) {
            // 拆分出一个新的group
            doMoveCodeToNewGroup(klSkuList, platformPid, channelId, cartId, status);
        } else {
            // 拆分合并code
            doMoveCodeToAnotherGroup(klSkuList, cmsBtProductGroup, channelId, cartId, status);
        }
    }

    /**
     * 拆分合并code(创建一个新的group)
     */
    private void doMoveCodeToNewGroup(List<String> klSkuList, String platformPid, String channelId, int cartId, CmsConstants.PlatformStatus status) {
        // 根据要移动的skuList取得code列表
        Map<String, CmsBtProductModel> moveCods = getMoveCodesBySkuList(platformPid, channelId, cartId, klSkuList);
        if (moveCods.size() == 0) {
            $error(String.format("需要新建group,但是没有一个sku在cms里存在! channelId:%s, cartId:%s, platformPid:%s", channelId, String.valueOf(cartId), platformPid));
            return;
        }

        boolean isFirst = true;
        CmsBtProductGroupModel newGroupModel = null;
        for (Map.Entry<String, CmsBtProductModel> entry : moveCods.entrySet()) {
            String moveCode = entry.getKey();
            CmsBtProductGroupModel sourceGroupModel = productGroupService.selectProductGroupByCode(channelId, moveCode, cartId);
            if (isFirst) {
                // 第一个code，创建一个group
                isFirst = false;
                newGroupModel = cmsProductCodeChangeGroupService.moveToNewGroup(channelId, cartId, moveCode, sourceGroupModel, getTaskName());
                // 回写下PlatformPid,状态
                newGroupModel.setCreater(sourceGroupModel.getCreater());
                newGroupModel.setCreated(sourceGroupModel.getCreated());
                newGroupModel.setPlatformStatus(status);
                newGroupModel.setPlatformPid(platformPid);
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

        $info(String.format("新group做成! channelId:%s, cartId:%s, platformPid:%s, productCodes:%s", channelId, String.valueOf(cartId), platformPid, moveCods.keySet()));
    }

    /**
     * 拆分合并code(合并在现有的group)
     */
    private void doMoveCodeToAnotherGroup(List<String> klSkuList, CmsBtProductGroupModel cmsBtProductGroup, String channelId, int cartId, CmsConstants.PlatformStatus status) {

        cmsBtProductGroup.getProductCodes().forEach(productCode -> {
            CmsBtProductModel productModel = cmsBtProductDao.selectByCode(productCode, channelId);
            productModel.getCommon().getSkus().forEach(sku -> {
                // 全小写比较skuCode
                String skucode = sku.getSkuCode().toLowerCase();
                if (klSkuList.contains(skucode)) {
                    klSkuList.remove(skucode);
                }
            });
        });

        // 根据要移动的skuList取得code列表
        Map<String, CmsBtProductModel> moveCods = getMoveCodesBySkuList(cmsBtProductGroup.getPlatformPid(), channelId, cartId, klSkuList);

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
            $info(String.format("group追加了code! channelId:%s, cartId:%s, platformPid:%s, 追加的productCodes:%s", channelId, String.valueOf(cartId), cmsBtProductGroup.getPlatformPid(), moveCods.keySet()));
        } else {
            if (klSkuList.size() != 0) {
                $error(String.format("group需要追加code,但是没有一个要合并的sku在cms里存在! channelId:%s, cartId:%s, platformPid:%s", channelId, String.valueOf(cartId), cmsBtProductGroup.getPlatformPid()));
            } else {
                $info(String.format("group不需要追加code! channelId:%s, cartId:%s, platformPid:%s", channelId, String.valueOf(cartId), cmsBtProductGroup.getPlatformPid()));
            }
        }

    }

    /**
     * 根据要移动的skuList取得code列表
     *
     * @return Map<code, CmsBtProductModel>
     */
    private Map<String, CmsBtProductModel> getMoveCodesBySkuList(String platformPid, String channelId, int cartId, List<String> klSkuList) {
        Map<String, CmsBtProductModel> moveCods = new HashMap<>();
        for (String skuCode : klSkuList) {
            // 剩下的是考拉上有，但是group表里没有的sku
            // 那么把这些sku对应的code移到这个group下
            CmsBtProductModel productModel = cmsBtProductDao.selectBySkuIgnoreCase(skuCode, channelId);
            if (productModel == null) {
                $warn(String.format("考拉上存在一个cms里没有的sku! channelId:%s, cartId:%s, platformPid:%s, Sku:%s", channelId, String.valueOf(cartId), platformPid, skuCode));
                continue;
            }

            String code = productModel.getCommon().getFields().getCode();
            if (!moveCods.containsKey(code)) {
                moveCods.put(code, productModel);
            }
        }
        return moveCods;
    }
}
