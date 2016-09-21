package com.voyageone.task2.cms.service;

import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.dao.cms.mongo.CmsBtSxCnInfoDao;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.sx.ConditionPropValueService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import com.voyageone.service.model.cms.mongo.CmsBtSxCnInfoModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 独立域名产品上新
 *
 * @author morse on 2016/9/20
 * @version 2.6.0
 */
@Service
public class CmsBuildPlatformProductUploadCnService extends BaseTaskService {

    @Autowired
    private ConditionPropValueService conditionPropValueService;
    @Autowired
    private CnSchemaService cnSchemaService;
    @Autowired
    private SxProductService sxProductService;
    @Autowired
    private BusinessLogService businessLogService;

    @Autowired
    private CmsBtSxCnInfoDao cmsBtSxCnInfoDao;

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

        // 初始化cms_mt_channel_condition_config表的条件表达式(避免多线程时2次初始化)
        conditionPropValueService.init();

        // 循环所有销售渠道
        if (channelIdList != null && channelIdList.size() > 0) {
            for (String channelId : channelIdList) {
                // 独立域名商品信息新增或更新
                doUpload(channelId, Integer.parseInt(CartEnums.Cart.CN.getId()));
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
     */
    public void doUpload(String channelId, int cartId) {
        // 等待上传 的数据
        List<CmsBtSxCnInfoModel> listSxModel = cmsBtSxCnInfoDao.selectWaitingPublishData(channelId, PUBLISH_PRODUCT_RECORD_COUNT_ONCE_HANDLE);

        // 把状态更新成 1:上传中，防止推过来更新的xml数据
        List<Long> listGroupId = listSxModel.stream().map(CmsBtSxCnInfoModel::getGroupId).collect(Collectors.toList());
        cmsBtSxCnInfoDao.updatePublishFlg(channelId, listGroupId, 1, getTaskName());

        // 上传
        List<List<Field>> listProductFields = new ArrayList<>();
        List<List<Field>> listSkuFields = new ArrayList<>();
        String startTime = "";
        String endTime = "";
        int index = 0;
        for (CmsBtSxCnInfoModel sxModel : listSxModel) {
            if (index == 0) {
                startTime = sxModel.getCreated();
            }
            if (index == listSxModel.size() - 1) {
                endTime = sxModel.getCreated();
            }

            listProductFields.addAll(cnSchemaService.readProductXmlString(sxModel.getProductXml()));
            listSkuFields.addAll(cnSchemaService.readSkuXmlString(sxModel.getSkuXml()));

            index++;
        }
        String productXml = cnSchemaService.writeProductXmlString(listProductFields);
        String skuXml = cnSchemaService.writeSkuXmlString(listSkuFields);
        $info("独立域名上传产品的xml:" + productXml);
        $info("独立域名上传Sku的xml:" + skuXml);
        // TODO: doPost
        boolean isSuccess = false;



//        if (!isSuccess) {
//            // 只有网络问题推送失败才会false，所以就打个log，不把status更新成error
//            // 把状态更新成 0:等待上传，下次再推
//            cmsBtSxCnInfoDao.updatePublishFlg(channelId, listGroupId, 0, getTaskName());
//            // 回写详细错误信息表(cms_bt_business_log)
//            insertBusinessLog(channelId, String.format("独立域名xml推送失败!请耐心等待下一次推送!本次推送数据的Approve时间为[%s]-[%s]", startTime, endTime));
//        } else {
//            // 上传产品和sku成功的场合,回写product group表中的numIId和platformStatus(Onsale/InStock)
//            String numIId = sxData.getMainProduct().getOrgChannelId() + "-" + Long.toString(sxData.getMainProduct().getProdId()); // 因为现在是一个group一个code
//            sxProductService.updateProductGroupNumIIdStatus(sxData, numIId, getTaskName());
//            // 回写ims_bt_product表(numIId)
//            sxProductService.updateImsBtProduct(sxData, getTaskNameForUpdate());
//            // 回写workload表   (1上新成功)
//            sxProductService.updateSxWorkload(cmsBtSxWorkloadModel, CmsConstants.SxWorkloadPublishStatusNum.okNum, getTaskName());
//        }

        // 类目保存


    }

    /**
     * 出错的时候将错误信息回写到cms_bt_business_log表
     *
     * @param errorMsg 错误信息
     */
    private void insertBusinessLog(String channelId, String errorMsg) {
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

        businessLogService.insertBusinessLog(businessLogModel);

    }
}
