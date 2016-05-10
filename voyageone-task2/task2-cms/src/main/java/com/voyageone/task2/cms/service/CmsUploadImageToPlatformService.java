package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtImageGroupDao;
import com.voyageone.service.daoext.cms.CmsBtImagesDaoExt;
import com.voyageone.service.model.cms.CmsBtImagesModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageGroupModel_Image;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 图片上传到平台（暂时只支持天猫和聚美）
 * @author jeff.duan on 2016/5/10.
 * @version 2.0.0
 */
@Service
public class CmsUploadImageToPlatformService extends BaseTaskService {

    @Autowired
    private CmsBtImageGroupDao cmsBtImageGroupDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsUploadImageToPlatformJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        ExecutorService es = Executors.newFixedThreadPool(10);
        try {
            // 获得该渠道要上传到平台的图片url列表
            List<CmsBtImageGroupModel_Image> imageUrlList = new ArrayList<>();
            JomgoQuery queryObject = new JomgoQuery();
            queryObject.setQuery("{\"image.status\":1},{\"active\":1}");
            List<CmsBtImageGroupModel> imageGroupList = cmsBtImageGroupDao.select(queryObject);
            for (CmsBtImageGroupModel imageGroup : imageGroupList) {

            }


//            $info(channelId + String.format("渠道本次有%d要推送scene7的图片", imageUrlList.size()));
//            if (!imageUrlList.isEmpty()) {
//                List<List<CmsBtImagesModel>> imageSplitList = CommonUtil.splitList(imageUrlList,10);
//                for (List<CmsBtImagesModel> subImageUrlList :imageSplitList ){
//                    es.execute(() -> ImageGetAndSendTask(channelId, subImageUrlList));
//                }
//                es.shutdown();
//                es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
//
//            } else {
//                $debug(channelId + "渠道本次没有要推送scene7的图片");
//            }

        } catch (Exception ex) {
            $error(ex.getMessage(), ex);
            issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);
        }
    }
}

