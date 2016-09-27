package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.components.cn.service.CnSchemaService;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.cms.BusinessLogService;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.sx.CnImageService;
import com.voyageone.service.impl.cms.sx.ConditionPropValueService;
import com.voyageone.service.model.cms.CmsBtSxCnImagesModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 独立域名图片上传
 *
 * @author morse on 2016/9/27
 * @version 2.6.0
 */
@Service
public class CmsBuildPlatformImageUploadCnService extends BaseTaskService {

    @Autowired
    private ConditionPropValueService conditionPropValueService;
    @Autowired
    private CnSchemaService cnSchemaService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private BusinessLogService businessLogService;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;
    @Autowired
    private CnImageService cnImageService;

    private static final int PUBLISH_IMAGE_COUNT_ONCE_HANDLE = 10000;

    private static final String[][] CN_URLS = new String[][]{
            {"201", "http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x1200_1200x?$1200x1200$&$product="},
            {"202", "http://s7d5.scene7.com/is/image/sneakerhead/Target_20160906_x1200_1200x?$1200x1200_exportpng$&$product=="},
            {"203", "http://s7d5.scene7.com/image/sneakerhead/Target_20160527_x1200_1200x?$1200x1200$&$product="},
            {"204", "http://s7d5.scene7.com/is/image/sneakerhead/peijian_788_620?$bcbg_ringdetail_788_620$&$images="},
            {"205", "http://s7d5.scene7.com/is/image/sneakerhead/Target_20160527_x1200_1200x?$1200x1200$&$product="}
    }; // String[A][B] A:图片模板X B: 0:上传OSS后存放的文件夹名 1:图片模板

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsBuildPlatformProductUploadCnJob";
    }

    public String getTaskNameForUpdate() {
        return "CmsBuildPlatformImageUploadCnJob";
    }

    /**
     * 独立域名上传图片处理
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
     * 平台图片上传主处理
     *
     * @param channelId String 渠道ID
     * @param cartId    String 平台ID
     */
    public void doUpload(String channelId, int cartId) {
        // 等待上传 的数据
        List<CmsBtSxCnImagesModel> listImagesModel = cnImageService.selectListWaitingUpload(channelId, cartId, PUBLISH_IMAGE_COUNT_ONCE_HANDLE);

        // 默认线程池最大线程数
        int threadPoolCnt = 5;
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据上新任务列表中的groupid循环上新处理
        for (CmsBtSxCnImagesModel cmsBtSxCnImagesModel : listImagesModel) {
            // 启动多线程
            executor.execute(() -> uploadExecute(cmsBtSxCnImagesModel));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * 平台图片上传处理
     */
    public void uploadExecute(CmsBtSxCnImagesModel cmsBtSxCnImagesModel) {
        String strUrlKey = cmsBtSxCnImagesModel.getUrlKey();
        int index = cmsBtSxCnImagesModel.getIndex();
        String ossImageName = strUrlKey + "-" + Integer.toString(index);

        for (String[] cnUrl : CN_URLS) {
            try {
                cnImageService.doUploadImage(cnUrl[1] + cmsBtSxCnImagesModel.getImageName(), "010/" + cnUrl[0] + "/" + ossImageName);
            } catch (Exception ex) {
                $warn("独立域名上传图片失败!code[%s],url[%s],错误信息[%s]", cmsBtSxCnImagesModel.getCode(), cnUrl[1] + cmsBtSxCnImagesModel.getImageName(), ex.getMessage());
            }
        }
    }

}
