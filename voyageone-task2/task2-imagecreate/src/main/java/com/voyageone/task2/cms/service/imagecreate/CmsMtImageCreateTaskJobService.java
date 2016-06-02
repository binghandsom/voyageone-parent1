package com.voyageone.task2.cms.service.imagecreate;

import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateTaskDetailService;
import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateTaskService;
import com.voyageone.service.impl.cms.imagecreate.ImageCreateFileService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskDetailModel;
import com.voyageone.service.model.cms.CmsMtImageCreateTaskModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob)
public class CmsMtImageCreateTaskJobService extends BaseMQCmsService {

    @Autowired
    private ImageCreateFileService serviceImageCreateFile;
    @Autowired
    private CmsMtImageCreateTaskDetailService serviceCmsMtImageCreateTaskDetail;
    @Autowired
    private CmsMtImageCreateTaskService serviceCmsMtImageCreateTask;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        int cmsMtImageCreateTaskId = (int) messageMap.get("id");
        $info(String.format("CmsMtImageCreateTaskJobService start. [%s]", cmsMtImageCreateTaskId));
        CmsMtImageCreateTaskModel taskModel = serviceCmsMtImageCreateTask.get(cmsMtImageCreateTaskId);
        if (taskModel == null) {
            $info(String.format("CmsMtImageCreateTaskJobService id not found. [%s]", cmsMtImageCreateTaskId));
            return;
        }
        List<CmsMtImageCreateTaskDetailModel> list = serviceCmsMtImageCreateTaskDetail.getListByCmsMtImageCreateTaskId(cmsMtImageCreateTaskId);
        //1.执行开始时间
        taskModel.setBeginTime(new Date());
        List<Runnable> threads = new ArrayList<>();
        for (CmsMtImageCreateTaskDetailModel modelTaskDetail : list) {
            threads.add(() -> serviceImageCreateFile.createAndUploadImage(modelTaskDetail));
        }

        if (!threads.isEmpty()) {
            /**
             * runWithThreadPool
             */
            runWithThreadPool(threads, taskControlList);

            //2.执行结束时间
            taskModel.setEndTime(new Date());
            serviceCmsMtImageCreateTask.save(taskModel);
        }

        $info(String.format("CmsMtImageCreateTaskJobService end. [%s]", cmsMtImageCreateTaskId));
    }
}
