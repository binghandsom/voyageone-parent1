package com.voyageone.task2.cms.service.feed.status;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.feed.FeedSaleService;
import com.voyageone.service.model.cms.CmsFeedLiveSkuModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class LuckyVitaminSkuStatusCheckService  extends BaseCronTaskService {

    @Autowired
    private FeedInfoService feedInfoService;

    @Autowired
    private FeedSaleService feedSaleService;

    protected void checkSale() throws Exception {
        CsvReader reader;
        List<CmsFeedLiveSkuModel> skuList = new ArrayList<>();
        String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
        String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
        String fileFullName = String.format("%s/%s", filePath, "discontinue_"+fileName);

        String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);

        reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));

        // Head读入
        reader.readHeaders();
        reader.getHeaders();
        int sale = 0;
        int noSale = 0 ;
        // Body读入
        while (reader.readRecord()) {
            String discontinued = reader.get(95);
            //upc,MerchantPrimaryCategory,cnMsrp,cNPrice,ImageList
            if (StringUtils.isEmpty(reader.get(1)) || StringUtils.isEmpty(reader.get(49))
                    || StringUtils.isEmpty(reader.get(19))
                    || StringUtils.isEmpty(reader.get(20))
                    || StringUtils.isEmpty(reader.get(37))
                    ) continue;
            String sku = reader.get(0);
            if(!StringUtil.isEmpty(sku)) {
                if (discontinued.equalsIgnoreCase("yes")) {
                    feedSaleService.notSale(getChannel().getId(), sku);
                    $info(getChannel().getId() + " " + sku + " sale -> notSale");
                    sale++;
                } else {
                    feedSaleService.sale(getChannel().getId(), sku, 0);
                    $info(getChannel().getId() + " " + sku + " notSale -> sale");
                    noSale++;
                }
            }
        }
        $info(String.format("notSale -> sale 共%d件  sale -> notSale 共%d件", sale, noSale));

    }

    protected ChannelConfigEnums.Channel getChannel() {
        return ChannelConfigEnums.Channel.LUCKY_VITAMIN;
    }

    public void backupFeedFile(){
        $info("备份处理文件开始");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date_ymd = sdf.format(date);

        String filename = Feeds.getVal1("017", FeedEnums.Name.feed_ftp_localpath) + "/" +"discontinue_"+ StringUtils.null2Space(Feeds.getVal1("017", FeedEnums.Name.file_id));
        String filename_backup = Feeds.getVal1("017", FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
                + StringUtils.null2Space(Feeds.getVal1("017", FeedEnums.Name.file_id));
        File file = new File(filename);
        File file_backup = new File(filename_backup);
        if (!file.renameTo(file_backup)) {
            $info("备份处理失败");
        }

        $info("备份处理文件结束");
    }

    @Override
    protected String getTaskName() {
        return "LuckyVitaminSkuStatusCheckService";
    }

    @Override
    protected SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        checkSale();
    }
}
