package com.voyageone.service.impl.vms.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import org.apache.commons.io.FileUtils;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.impl.BaseService;
import org.apache.poi.util.IntegerField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * FeedFileUploadService
 *
 * @author jeff.duan 16/7/7
 * @version 1.0.0
 */
@Service
public class FeedFileUploadService extends BaseService {

    @Autowired
    private VmsBtFeedFileDao vmsBtFeedFileDao;

    /**
     * 新建一条文件信息到vms_bt_feed_file表
     *
     * @param channelId     渠道
     * @param fileName 文件名
     * @param newFileName 新文件名
     * @param status  状态
     * @param modifier 创建者
     * @return id
     */
    public Integer insertFeedFileInfo(String channelId, String fileName, String newFileName, String status, String modifier) {
        VmsBtFeedFileModel model = new VmsBtFeedFileModel();
        model.setChannelId(channelId);
        model.setClientFileName(fileName);
        model.setFileName(newFileName);
        model.setStatus(status);
        model.setCreater(modifier);
        model.setModifier(modifier);
        vmsBtFeedFileDao.insert(model);
        return model.getId();
    }

    /**
     * 从vms_bt_feed_file表删除一条文件信息
     *
     * @param id  Key
     */
    public void deleteFeedFileInfo(Integer id) {
        vmsBtFeedFileDao.delete(id);
    }


    /**
     * 文件保存到服务器指定的路径
     *
     * @param channelId   渠道id
     * @param fileName 文件名
     * @param inputStream 文件流
     * @return 新命名的文件名
     */
    public void saveFile(String channelId, String fileName, InputStream inputStream) {

        // 取得Feed文件上传路径
        String feedFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.upload");
        feedFilePath +=  "/" + channelId + "/feed/";
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(feedFilePath  + fileName));
        } catch (IOException e) {
            // Failed to upload file.
            throw new BusinessException("8000016");
        }
    }
}
