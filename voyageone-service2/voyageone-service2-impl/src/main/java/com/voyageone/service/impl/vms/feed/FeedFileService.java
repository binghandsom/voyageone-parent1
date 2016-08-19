package com.voyageone.service.impl.vms.feed;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.daoext.vms.VmsBtFeedFileDaoExt;
import com.voyageone.service.model.vms.VmsBtFeedFileModel;
import org.apache.commons.io.FileUtils;
import com.voyageone.service.dao.vms.VmsBtFeedFileDao;
import com.voyageone.service.impl.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FeedFileService
 *
 * @author jeff.duan 16/7/7
 * @version 1.0.0
 */
@Service
public class FeedFileService extends BaseService {

    @Autowired
    private VmsBtFeedFileDao vmsBtFeedFileDao;

    @Autowired
    private VmsBtFeedFileDaoExt vmsBtFeedFileDaoExt;

    /**
     * 新建一条文件信息到vms_bt_feed_file表
     *
     * @param channelId   渠道
     * @param fileName    文件名
     * @param newFileName 新文件名
     * @param uploadType 上传状态
     * @param status      状态
     * @param modifier    创建者
     * @return id
     */
    public Integer insertFeedFileInfo(String channelId, String fileName, String newFileName, String uploadType, String status, String modifier) {
        VmsBtFeedFileModel model = new VmsBtFeedFileModel();
        model.setChannelId(channelId);
        model.setClientFileName(fileName);
        model.setFileName(newFileName);
        model.setUploadType(uploadType);
        model.setStatus(status);
        model.setCreater(modifier);
        model.setModifier(modifier);
        vmsBtFeedFileDao.insert(model);
        return model.getId();

    }

    /**
     * 根据状态从vms_bt_feed_file表取得FeedFile信息
     *
     * @param channelId   渠道
     * @param status      状态
     * @return FeedFile列表
     */
    public List<VmsBtFeedFileModel> getFeedFileInfoByStatus(String channelId, String status) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("status", status);
        return vmsBtFeedFileDao.selectList(param);
    }

    /**
     * 保存online上传的文件到服务器指定的路径
     *
     * @param channelId   渠道id
     * @param fileName 文件名
     * @param inputStream 文件流
     * @return 新命名的文件名
     */
    public void saveOnlineFile(String channelId, String fileName, InputStream inputStream) {

        // 取得Feed文件上传路径
        String feedFilePath = com.voyageone.common.configs.Properties.readValue("vms.feed.online.upload");
        feedFilePath +=  "/" + channelId + "/";
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(feedFilePath  + fileName));
        } catch (IOException e) {
            $error(e);
            // Failed to upload file.
            throw new BusinessException("8000016");
        }
    }

    /**
     * 条件搜索FeedFile
     * @param param 搜索条件
     * @return FeedFile列表
     */
    public List<Map<String, Object>> getFeedFileList(Map<String, Object> param) {

        return vmsBtFeedFileDaoExt.selectList(param);

    }

    /**
     * 条件搜索FeedFile
     * @param param 搜索条件
     * @return FeedFile列表
     */
    public long getFeedFileListCount(Map<String, Object> param) {

        return vmsBtFeedFileDaoExt.selectListCount(param);

    }
}
