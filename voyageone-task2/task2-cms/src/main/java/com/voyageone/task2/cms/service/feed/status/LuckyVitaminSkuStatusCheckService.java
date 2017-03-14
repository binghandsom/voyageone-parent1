package com.voyageone.task2.cms.service.feed.status;

import com.csvreader.CsvReader;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjl on 2017/3/14.
 */
@Service
public class LuckyVitaminSkuStatusCheckService extends FeedStatusCheckBaseService {
    @Override
    protected List<CmsFeedLiveSkuModel> getSkuList() throws Exception {
        CsvReader reader;
        List<CmsFeedLiveSkuModel> skuList = new ArrayList<>();
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
            String Discontinued = reader.get(95);
            if (Discontinued.equalsIgnoreCase("yes")) continue;
            CmsFeedLiveSkuModel cmsFeedLiveSkuModel = new CmsFeedLiveSkuModel();
            cmsFeedLiveSkuModel.setChannelId(getChannel().getId());
            String sku = reader.get(0);
            cmsFeedLiveSkuModel.setSku(sku);
            cmsFeedLiveSkuModel.setQty(0);
            cmsFeedLiveSkuModel.setCreater(getTaskName());
            cmsFeedLiveSkuModel.setModifier(getTaskName());
            cmsFeedLiveSkuModel.setCreated(DateTimeUtil.getDate());
            cmsFeedLiveSkuModel.setModified(DateTimeUtil.getDate());
            skuList.add(cmsFeedLiveSkuModel);
        }
        return skuList;
    }

    @Override
    protected ChannelConfigEnums.Channel getChannel() {
        return ChannelConfigEnums.Channel.LUCKY_VITAMIN;
    }
}
