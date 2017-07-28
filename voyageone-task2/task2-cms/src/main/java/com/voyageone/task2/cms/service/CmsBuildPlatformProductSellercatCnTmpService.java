package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductTopDao;
import com.voyageone.service.impl.cms.sx.CnCategoryService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductTopModel;
import com.voyageone.task2.base.BaseCronTaskService;
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
 * Liking店临时处理用
 *
 * @author morse on 2016/9/22
 * @version 2.6.0
 */
@Service
public class CmsBuildPlatformProductSellercatCnTmpService extends BaseCronTaskService {

    @Autowired
    private CnSchemaService cnSchemaService;
    @Autowired
    private CnCategoryService cnCategoryService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductTopDao cmsBtProductTopDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadCnTmpJob";
    }

    public String getTaskNameForUpdate() {
        return "CmsBuildPlatformProductSellercatCnTmpJob";
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
                    ShopBean shopBean = Shops.getShop(channelId, CartEnums.Cart.LCN.getId());
                    // 主处理
                    doUpload(channelId, Integer.parseInt(CartEnums.Cart.LCN.getId()), shopBean);
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
     */
    public void doUpload(String channelId, int cartId, ShopBean shopBean) {
        // 取得要更新的类目
        List<String> listCatIds = cnCategoryService.selectListWaitingUpload(channelId, cartId);
        if (ListUtils.isNull(listCatIds)) {
            return;
        }

        List<List<Field>> result = new ArrayList<>();
        for (String catId : listCatIds) {
            // modified by morse.lu 2016/11/30 start
//            List<String> codes = cmsBtProductDao.selectListCodeBySellerCat(channelId, cartId, catId);
            List<String> codes = new ArrayList<>();
            CmsBtProductTopModel topModel = cmsBtProductTopDao.selectBySellerCatId(catId, channelId, cartId);
            if (topModel == null) {
                codes.addAll(cmsBtProductDao.selectListCodeBySellerCat(channelId, cartId, catId, null, null, null)); // 普通code排序
            } else {
                if (ListUtils.notNull(topModel.getProductCodeList())) {
                    codes.addAll(topModel.getProductCodeList()); // 置顶列表
                }
                codes.addAll(cmsBtProductDao.selectListCodeBySellerCat(channelId, cartId, catId, topModel.getSortColumnName(), topModel.getSortType(), topModel.getProductCodeList())); // 普通code排序
            }
            // modified by morse.lu 2016/11/30 end
            if (ListUtils.isNull(codes)) {
                $warn("类目[%s]不存在一个上新过的code!", catId);
                // 理论上不会有这类垃圾数据，以防万一一下
                cnCategoryService.updateProductSellercatUpdFlg(channelId, cartId, new ArrayList<String>(){{add(catId);}}, "2", getTaskNameForUpdate());
                continue;
            }

            List<Field> fields = new ArrayList<>();
            fields.add(createInputField("Id", catId)); // Id
            if (cartId == CartEnums.Cart.CN.getValue()) {
                fields.add(createInputField("ProductCodes", codes.stream().collect(Collectors.joining(",")))); // ProductCodes
            } else {
                fields.add(createInputField("ProductCodes", codes.stream().map(code -> "C" + code).collect(Collectors.joining(",")))); // ProductCodes
            }

            result.add(fields);
        }

        String xml = cnSchemaService.writeCategoryProductXmlString(result);
        $debug("类目-产品xml:" + xml);

        // doPost
        boolean isSuccess = false;
        try {
            String resultPost = cnSchemaService.postXml(xml, shopBean);
            if (resultPost != null && resultPost.indexOf("Success") >= 0) {
                isSuccess = true;
            }
        } catch (Exception e) {
            $error("推送类目-产品xml时发生异常!");
        }

        if (isSuccess) {
            // 状态更新成 1:已处理
            cnCategoryService.updateProductSellercatUpdFlg(channelId, cartId, listCatIds, "1", getTaskNameForUpdate());
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
