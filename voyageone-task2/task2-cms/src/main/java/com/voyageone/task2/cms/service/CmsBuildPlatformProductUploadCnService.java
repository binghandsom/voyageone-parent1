package com.voyageone.task2.cms.service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.service.dao.cms.CmsBtSxCnSkuDao;
import com.voyageone.service.dao.cms.CmsBtSxWorkloadDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.dao.cms.mongo.CmsBtSxCnInfoDao;
import com.voyageone.service.daoext.cms.CmsBtSxCnSkuDaoExt;
import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.CnCategoryService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.CmsBtSxCnSkuModel;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.service.model.cms.mongo.CmsBtSxCnInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 独立域名产品上新
 *
 * @author morse on 2016/9/20
 * @version 2.6.0
 */
@Service
public class CmsBuildPlatformProductUploadCnService extends BaseCronTaskService {

    @Autowired
    private CnSchemaService cnSchemaService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private BusinessLogService businessLogService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CnCategoryService cnCategoryService;

    @Autowired
    private CmsBtSxCnInfoDao cmsBtSxCnInfoDao;
    @Autowired
    private CmsBtSxCnSkuDao cmsBtSxCnSkuDao;
    @Autowired
    private CmsBtSxCnSkuDaoExt cmsBtSxCnSkuDaoExt;
    @Autowired
    private CmsBtSxWorkloadDao sxWorkloadDao;
    @Autowired
    private CmsBtSxWorkloadDaoExt sxWorkloadDaoExt;

    private static final int PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE = 1000;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadCnJob";
    }

    /**
     * 独立域名上新处理
     *
     * @param taskControlList taskcontrol信息
     */
    @Override
    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        // 获取该任务可以运行的销售渠道
        List<String> channelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                {
                    ShopBean shopBean = Shops.getShop(channelId, CartEnums.Cart.CN.getId());
                    // 独立域名商品信息新增或更新
                    doUpload(channelId, Integer.parseInt(CartEnums.Cart.CN.getId()), shopBean);
                }
                {
                    ShopBean shopBean = Shops.getShop(channelId, CartEnums.Cart.LIKING.getId());
                    // 独立域名商品信息新增或更新
                    doUpload(channelId, Integer.parseInt(CartEnums.Cart.LIKING.getId()), shopBean);
                }
            }
        }

        // 正常结束
        $info("正常结束");
    }

    /**
     * 平台产品上新主处理
     *
     * @param channelId String 渠道ID
     * @param cartId    String 平台ID
     * @param shopBean 作为传进来,可以做测试程序
     */
    public void doUpload(String channelId, int cartId, ShopBean shopBean) {
        // 等待上传 的数据
        List<CmsBtSxCnInfoModel> listSxModel = cmsBtSxCnInfoDao.selectWaitingPublishData(channelId, PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE);
        if (ListUtils.isNull(listSxModel)) {
            return;
        }

        // 把状态更新成 1:上传中，防止推过来更新的xml数据
        List<Long> listGroupId = listSxModel.stream().map(CmsBtSxCnInfoModel::getGroupId).collect(Collectors.toList());
        cmsBtSxCnInfoDao.updatePublishFlg(channelId, listGroupId, 1, getTaskName(), 0);

        boolean needRetry = false;
        String startTime = "";
        String endTime = "";
        try {
            // 上传
            List<List<Field>> listProductFields = new ArrayList<>();
            List<List<Field>> listSkuFields = new ArrayList<>();
            Set<String> catIds = new HashSet<>();
            Map<String, List<String>> mapProductCats = new HashMap<>(); // Map<code, List<catId>>
            Map<String, String> mapProductOrgChannel = new HashMap<>(); // Map<code, orgChannelId>
            List<String> listDelCodes = new ArrayList<>(); // 删除的code列表
            int index = 0;
            for (CmsBtSxCnInfoModel sxModel : listSxModel) {
                if (index == 0) {
                    startTime = sxModel.getCreated();
                }
                if (index == listSxModel.size() - 1) {
                    endTime = sxModel.getCreated();
                }

                listProductFields.addAll(cnSchemaService.readProductXmlString(sxModel.getProductXml()));
                if (sxModel.getPlatformActive() == CmsConstants.PlatformActive.ToInStock) {
                    // 删除
                    listDelCodes.add(sxModel.getCode());
                }
                if (!StringUtils.isEmpty(sxModel.getSkuXml())) {
                    // 存在sku没变化，不推的情况
                    listSkuFields.addAll(cnSchemaService.readSkuXmlString(sxModel.getSkuXml()));
                }
                catIds.addAll(sxModel.getCatIds());
                mapProductCats.put(sxModel.getCode(), new ArrayList<>(sxModel.getCatIds()));
                mapProductOrgChannel.put(sxModel.getCode(), sxModel.getOrgChannelId());

                index++;
            }
            String productXml = cnSchemaService.writeProductXmlString(listProductFields);
            $debug("独立域名上传产品的xml:" + productXml);
            // doPost
            boolean isSuccess = false;

            String result = cnSchemaService.postXml(productXml, shopBean);
            if (result != null && result.indexOf("Success") >= 0) {
                // product成功了，再传sku
                if (ListUtils.notNull(listSkuFields)) {
                    String skuXml = cnSchemaService.writeSkuXmlString(listSkuFields);
                    $debug("独立域名上传Sku的xml:" + skuXml);
                    result = cnSchemaService.postXml(skuXml, shopBean);
                    if (result != null && result.indexOf("Success") >= 0) {
                        isSuccess = true;
                    }
                } else {
                    // 存在sku没变化，不推的情况
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
                // 只有网络问题推送失败才会false，所以就打个log，不把status更新成error
                // 把状态更新成 0:等待上传，下次再推
                cmsBtSxCnInfoDao.updatePublishFlg(channelId, listGroupId, 0, getTaskName(), 1);
                needRetry = true;
                // 回写详细错误信息表(cms_bt_business_log)
                insertBusinessLog(channelId, cartId, String.format("独立域名xml推送失败!请耐心等待下一次推送!本次推送数据的Approve时间为[%s]-[%s]", startTime, endTime), null);
            } else {
                for (CmsBtSxCnInfoModel sxModel : listSxModel) {
                    // 上传产品和sku成功的场合,回写product group表中的numIId和platformStatus(Onsale/InStock)
                    String numIId = sxModel.getUrlKey(); // 因为现在是一个group一个code
                    try {
                        updateProductGroupNumIIdStatus(sxModel, numIId);
                        // 回写workload表   (1上新成功)
                        updateSxWorkload(sxModel.getSxWorkloadId(), CmsConstants.SxWorkloadPublishStatusNum.okNum);
                    } catch (Exception ex) {
                        $error(ex.getMessage());
                        insertBusinessLog(channelId, cartId, "回写numIId发生异常!" + ex.getMessage(), sxModel);
                    }
                }

                // 类目保存，用于之后上传类目下code以及排序
                cnCategoryService.updateProductSellercatForUpload(channelId, cartId, catIds, getTaskName());

                // 更新cms_bt_sx_cn_sku表，用于刷全量库存
                updateSxCnSku(channelId, cartId, listSkuFields, mapProductCats, mapProductOrgChannel);
                if (ListUtils.notNull(listDelCodes)) {
                    deleteSxCnSku(channelId, listDelCodes);
                }

                // 把状态更新成 2:上传结束
                cmsBtSxCnInfoDao.updatePublishFlg(channelId, listGroupId, 2, getTaskName(), 1);
            }
        } catch (Exception ex) {
            $error(ex.getMessage());
            if (!needRetry) {
                // 不需要重新传
                // 回写详细错误信息表(cms_bt_business_log)
                insertBusinessLog(channelId, cartId, String.format("发生预想外的异常,需要重新Approve!本次推送数据的Approve时间为[%s]-[%s]!错误内容是[%s]", startTime, endTime, ex.getMessage()), null);
                // 把状态更新成 2:上传结束
                cmsBtSxCnInfoDao.updatePublishFlg(channelId, listGroupId, 2, getTaskName(), 1);
            } else {
                // 回写详细错误信息表(cms_bt_business_log)
                insertBusinessLog(channelId, cartId, String.format("发生预想外的异常,请等待系统自动重新上传!本次推送数据的Approve时间为[%s]-[%s]!错误内容是[%s]", startTime, endTime, ex.getMessage()), null);
            }
        }
    }

    /**
     * 出错的时候将错误信息回写到cms_bt_business_log表
     *
     * @param errorMsg 错误信息
     */
    private void insertBusinessLog(String channelId, int cartId, String errorMsg, CmsBtSxCnInfoModel sxModel) {
        CmsBtBusinessLogModel businessLogModel = new CmsBtBusinessLogModel();
        // 渠道id
        businessLogModel.setChannelId(channelId);
        // 错误类型(1:上新错误)
        businessLogModel.setErrorTypeId(1);
        // 详细错误信息
        businessLogModel.setErrorMsg(errorMsg);
        // 状态(0:未处理 1:已处理)
        businessLogModel.setStatus(0);
        // 创建者
        businessLogModel.setCreater(getTaskName());
        // 更新者
        businessLogModel.setModifier(getTaskName());
		// 平台id
		businessLogModel.setCartId(cartId);

        if (sxModel != null) {
            // 单个产品错误
            // deleted by morse.lu 2016/09/22 start
            // 拼接后可能太长了,就干脆不塞值了吧
            // 类目id
//           businessLogModel.setCatId(sxModel.getCatIds().stream().collect(Collectors.joining(",")));
            // deleted by morse.lu 2016/09/22 end
            // Group id
            businessLogModel.setGroupId(String.valueOf(sxModel.getGroupId()));
            // 主商品的product_id
            businessLogModel.setProductId(String.valueOf(sxModel.getProdId()));
            // code
            businessLogModel.setCode(sxModel.getCode());
        }

        businessLogService.insertBusinessLog(businessLogModel);
    }

    /**
     * 回写product group表中的numIId和platformStatus(Onsale/InStock)
     */
    private void updateProductGroupNumIIdStatus(CmsBtSxCnInfoModel sxModel, String numIId) {
        CmsBtProductGroupModel grpModel = cmsBtProductGroupDao.selectOneWithQuery("{'groupId':" + sxModel.getGroupId() + "}", sxModel.getChannelId());
        if (grpModel == null) {
            throw new BusinessException("group表数据已删除!");
        }
        // 上新成功后回写product group表中的numIId和platformStatus
        // 回写商品id(wareId->numIId)
        grpModel.setNumIId(numIId);
        // 设置PublishTime
        grpModel.setPublishTime(DateTimeUtil.getNowTimeStamp());
        // platformActive平台上新状态类型(ToOnSale/ToInStock)
        CmsConstants.PlatformActive platformActive = sxModel.getPlatformActive();
        if (platformActive == CmsConstants.PlatformActive.ToOnSale) {
            // platformActive是(ToOnSale)时，把platformStatus更新成"OnSale"
            grpModel.setPlatformStatus(CmsConstants.PlatformStatus.OnSale);
        } else if (platformActive == CmsConstants.PlatformActive.ToInStock) {
            // platformActive是(ToInStock)时，把platformStatus更新成"InStock"
            grpModel.setPlatformStatus(CmsConstants.PlatformStatus.InStock);
        }
        // 更新者
        sxModel.setModifier(getTaskName());

        // 更新ProductGroup表(更新该model对应的所有(包括product表)和上新有关的状态信息)
        productGroupService.updateGroupsPlatformStatus(grpModel, new ArrayList<String>(){{this.add(sxModel.getCode());}});
    }

    /**
     * 回写cms_bt_sx_workload表
     */
    private int updateSxWorkload(int sxWorkloadId, int publishStatus) {
        CmsBtSxWorkloadModel upModel = sxWorkloadDao.select(sxWorkloadId);
        upModel.setPublishStatus(publishStatus);
        upModel.setModifier(getTaskName());
        return sxWorkloadDaoExt.updateSxWorkloadModelWithModifier(upModel);
    }

    /**
     * 更新cms_bt_sx_cn_sku表，用于刷全量库存
     * 相同sku保存一条,找到更新，找不到insert
     * 暂时不做批量insert了
     */
    private int updateSxCnSku(String channelId, int cartId, List<List<Field>> listSkuFields, Map<String, List<String>> mapProductCats, Map<String, String> mapProductOrgChannel) {
        int cnt = 0;

        CmsBtSxCnSkuModel model;
        for(List<Field> fieldList : listSkuFields) {
            model = new CmsBtSxCnSkuModel();
            model.setChannelId(channelId);
            model.setCreater(getTaskName());
            for (Field field : fieldList) {
                if ("ProductCode".equals(field.getId())) {
                    String code = getFieldValue(field);
                    if (cartId != CartEnums.Cart.CN.getValue()) {
                        code = code.substring(1); // 推送的是 固定"C" + code
                    }
                    model.setCode(code);
                    model.setOrgChannelId(mapProductOrgChannel.get(code));
                    model.setCategoryIds(mapProductCats.get(code).stream().collect(Collectors.joining(",")));
                } else if ("Sku".equals(field.getId())) {
                    String sku = getFieldValue(field);
                    if (cartId != CartEnums.Cart.CN.getValue()) {
                        sku = sku.substring(1); // 推送的是 固定"S" + sku
                    }
                    model.setSku(sku);
                } else if ("Size".equals(field.getId())) {
                    model.setSize(getFieldValue(field));
                } else if ("ShowSize".equals(field.getId())) {
                    model.setShowSize(getFieldValue(field));
                } else if ("Eursize".equals(field.getId())) {
                    model.setSize(getFieldValue(field));
                } else if ("Msrp".equals(field.getId())) {
                    model.setMsrp(Double.valueOf(getFieldValue(field)));
                } else if ("Price".equals(field.getId())) {
                    model.setPrice(Double.valueOf(getFieldValue(field)));
                }
            }

            Map<String, Object> searchParam = new HashMap<>();
            searchParam.put("channelId", channelId);
            searchParam.put("code", model.getCode());
            searchParam.put("sku", model.getSku());
            CmsBtSxCnSkuModel searchModel = cmsBtSxCnSkuDao.selectOne(searchParam);
            if (searchModel == null) {
                // insert
                cnt += cmsBtSxCnSkuDao.insert(model);
            } else {
                // update
                searchModel.setCategoryIds(model.getCategoryIds());
                searchModel.setSize(model.getSize());
                searchModel.setShowSize(model.getShowSize());
                searchModel.setMsrp(model.getMsrp());
                searchModel.setPrice(model.getPrice());
                searchModel.setModifier(getTaskName());
                searchModel.setModified(DateTimeUtil.getDate());
                cnt += cmsBtSxCnSkuDao.update(searchModel);
            }
        }

        $info("cms_bt_sx_cn_sku更新了%d件!", cnt);
        return cnt;
    }

    private String getFieldValue(Field field) {
        FieldTypeEnum fieldType = field.getType();
        if (fieldType == FieldTypeEnum.INPUT) {
            return  ((InputField) field).getValue();
        } else if (fieldType == FieldTypeEnum.SINGLECHECK) {
            return ((SingleCheckField) field).getValue().getValue();
        } else {
            // 不支持
            return null;
        }
    }

    /**
     * 删除cms_bt_sx_cn_sku表
     */
    private int deleteSxCnSku(String channelId,  List<String> listDelCodes) {
        int delCnt = cmsBtSxCnSkuDaoExt.deleteByListCodes(channelId, listDelCodes);
        $info("cms_bt_sx_cn_sku删除了%d件,code列表:" + listDelCodes, delCnt);
        return delCnt;
    }
}
