package com.voyageone.task2.cms.service.feed.status;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import com.voyageone.task2.cms.bean.SuperFeedTargetBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/9/22.
 * @version 2.0.0
 */
@Service
public class TargetSkuStatusCheckService extends FeedStatusCheckBaseService {
    @Override
    protected List<CmsFeedLiveSkuModel> getSkuList() throws Exception {
        CsvReader reader;
        List<CmsFeedLiveSkuModel> skus = new ArrayList<>();
        String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
        String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
        String fileFullName = String.format("%s/%s", filePath, fileName);

        String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);

        reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));

        // Head读入
        reader.readHeaders();
        reader.getHeaders();

        // Body读入
        while (reader.readRecord()) {
            int i = 0;
            CmsFeedLiveSkuModel cmsFeedLiveSkuModel = new CmsFeedLiveSkuModel();
            cmsFeedLiveSkuModel.setChannelId(getChannel().getId());
            String sku = reader.get(i++);
            cmsFeedLiveSkuModel.setSku(sku);
            cmsFeedLiveSkuModel.setQty(0);
            cmsFeedLiveSkuModel.setCreater(getTaskName());
            cmsFeedLiveSkuModel.setModifier(getTaskName());
            cmsFeedLiveSkuModel.setCreated(DateTimeUtil.getDate());
            cmsFeedLiveSkuModel.setModified(DateTimeUtil.getDate());
            skus.add(cmsFeedLiveSkuModel);
        }
        return skus;
    }

    @Override
    protected ChannelConfigEnums.Channel getChannel() {
        return ChannelConfigEnums.Channel.TARGET;
    }
}
