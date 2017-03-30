package com.voyageone.task2.cms.service.feed.status;

import com.csvreader.CsvReader;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import com.voyageone.task2.cms.bean.SuperFeedEdcSkincareBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by james on 2017/3/29.
 */
@Service
public class EdcSkincareStatusCheckService extends FeedStatusCheckBaseService {
    @Override
    protected List<CmsFeedLiveSkuModel> getSkuList() throws Exception {
        $info("EdcSkincare产品文件读入开始");
        List<SuperFeedEdcSkincareBean> superfeed = new ArrayList<>();
        int cnt = 0;
        CsvReader reader;
        List<CmsFeedLiveSkuModel> cmsFeedLiveSkuModels = new ArrayList<>();
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/status_%s", filePath, fileName);
            String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);
            reader = new CsvReader(new FileInputStream(fileFullName), ',', Charset.forName(encode));
            // Head读入
            reader.readHeaders();
            // Body读入
            while (reader.readRecord()) {
                CmsFeedLiveSkuModel cmsFeedLiveSkuModel = new CmsFeedLiveSkuModel();
                int i = 0;
                cmsFeedLiveSkuModel.setChannelId(getChannel().getId());
                cmsFeedLiveSkuModel.setSku(reader.get(i++));
                cmsFeedLiveSkuModels.add(cmsFeedLiveSkuModel);
            }
            reader.close();
            $info("EdcSkincare产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("EdcSkincare产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("EdcSkincare产品文件读入失败");
            logIssue("cms 数据导入处理", "EdcSkincare产品文件读入失败 " + ex.getMessage());
        }
        return cmsFeedLiveSkuModels;
    }

    @Override
    protected ChannelConfigEnums.Channel getChannel() {
        return ChannelConfigEnums.Channel.EDCSKINCARE;
    }

    private boolean backupFeedFile(String channel_id) {
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/status_" + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        String filename_backup = Feeds.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1(channel_id, FeedEnums.Name.file_id));
        File file = new File(filename);
        File file_backup = new File(filename_backup);

        if (!file.renameTo(file_backup)) {
//            logger.error("产品文件备份失败");
            $info("产品文件备份失败");
        }

        $info("备份处理文件结束");
        return true;
    }
}
