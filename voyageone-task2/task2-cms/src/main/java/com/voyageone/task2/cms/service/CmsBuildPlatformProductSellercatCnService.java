package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.sx.CnCategoryService;
import com.voyageone.service.impl.cms.sx.ConditionPropValueService;
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
 * 独立域名类目-产品关系(排序)推送
 *
 * @author morse on 2016/9/22
 * @version 2.6.0
 */
@Service
public class CmsBuildPlatformProductSellercatCnService extends BaseTaskService {

    @Autowired
    private ConditionPropValueService conditionPropValueService;
    @Autowired
    private CnSchemaService cnSchemaService;
    @Autowired
    private CnCategoryService cnCategoryService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadCnJob";
    }

    public String getTaskNameForUpdate() {
        return "CmsBuildPlatformProductSellercatCnJob";
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
                // 主处理
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
     */
    public void doUpload(String channelId, int cartId) {
        List<List<Field>> result = new ArrayList<>();

        // 取得要更新的类目
        List<String> listCatIds = cnCategoryService.selectListWaitingUpload(channelId);

        for (String catId : listCatIds) {
            List<String> codes = cmsBtProductDao.selectListCodeBySellerCat(channelId, cartId, catId);
            if (ListUtils.isNull(codes)) {
                $warn("类目[%s]不存在一个上新过的code!", catId);
                continue;
            }

            List<Field> fields = new ArrayList<>();
            fields.add(createInputField("Id", catId)); // Id
            fields.add(createInputField("ProductCodes", codes.stream().collect(Collectors.joining(",")))); // ProductCodes

            result.add(fields);
        }

        String xml = cnSchemaService.writeCategoryProductXmlString(result);
        $debug("类目-产品xml:" + xml);

        // TODO:doPost

        boolean isSuccess = false;

        if (isSuccess) {
            // 状态更新成 1:已处理
            cnCategoryService.updateProductSellercatUpdFlg(channelId, listCatIds, "1", getTaskNameForUpdate());
        } else {
            // 只有网络问题推送失败才会false
            // 暂时什么都不做，下次重新推
        }

    }

    private Field createInputField(String id, String value) {
        InputField field = (InputField) FieldTypeEnum.createField(FieldTypeEnum.INPUT);
        field.setId(id);
        field.setValue(value);
        return field;
    }

}
