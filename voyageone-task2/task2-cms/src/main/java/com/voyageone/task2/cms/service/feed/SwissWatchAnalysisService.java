package com.voyageone.task2.cms.service.feed;

import com.csvreader.CsvReader;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.CamelUtil;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.task2.cms.bean.SuperFeedSwissWatchBean;
import com.voyageone.task2.cms.dao.feed.SwissWatchFeedDao;
import com.voyageone.task2.cms.model.CmsBtFeedInfoSwissWatchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.SWISSWATCH;

/**
 * Created by gjl on 2016/08/16.
 */
@Service
public class SwissWatchAnalysisService extends BaseAnalysisService {
    @Autowired
    SwissWatchFeedDao swissWatchFeedDao;

    @Override
    protected void updateFull(List<String> itemIds) {
        if (itemIds.size() > 0) {
            List<List<String>> listItems = CommonUtil.splitList(itemIds, 1000);
            listItems.forEach(strings -> {
                swissWatchFeedDao.delFullBySku(strings);
                swissWatchFeedDao.insertFullBySku(strings);
                swissWatchFeedDao.updateFlagBySku(strings);
            });

        }
    }

    @Override
    protected void zzWorkClear() {
        swissWatchFeedDao.delete();
    }

    @Override
    protected int superFeedImport() {
        $info("SwissWatch产品文件读入开始");
        List<SuperFeedSwissWatchBean> superfeed = new ArrayList<>();
        int cnt = 0;
        CsvReader reader;
        try {
            String fileName = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.file_id);
            String filePath = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_localpath);
            String fileFullName = String.format("%s/%s", filePath, fileName);
            String encode = Feeds.getVal1(getChannel().getId(), FeedEnums.Name.feed_ftp_file_coding);
            reader = new CsvReader(new FileInputStream(fileFullName), '\t', Charset.forName(encode));
            // Head读入
            reader.readHeaders();
            // Body读入
            while (reader.readRecord()) {
                SuperFeedSwissWatchBean edcSkincareBean = new SuperFeedSwissWatchBean();
                int i = 0;
                edcSkincareBean.setInventorynumber(reader.get(i++));
                edcSkincareBean.setAuctiontitle(reader.get(i++));
                edcSkincareBean.setUpc(reader.get(i++));
                edcSkincareBean.setMpn(reader.get(i++));
                edcSkincareBean.setDescription(reader.get(i++));
                edcSkincareBean.setManufacturer(reader.get(i++));
                edcSkincareBean.setBrand(reader.get(i++));
                edcSkincareBean.setSellercost(reader.get(i++));
                edcSkincareBean.setProductmargin(reader.get(i++));
                edcSkincareBean.setBuyitnowprice(reader.get(i++));
                edcSkincareBean.setRetailprice(reader.get(i++));
                edcSkincareBean.setPictureurls(reader.get(i++));
                edcSkincareBean.setClassification(reader.get(i++));
                edcSkincareBean.setAttribute1name(reader.get(i++));
                edcSkincareBean.setAttribute1value(reader.get(i++));
                edcSkincareBean.setAttribute2name(reader.get(i++));
                edcSkincareBean.setAttribute2value(reader.get(i++));
                edcSkincareBean.setAttribute3name(reader.get(i++));
                edcSkincareBean.setAttribute3value(reader.get(i++));
                edcSkincareBean.setAttribute4name(reader.get(i++));
                edcSkincareBean.setAttribute4value(reader.get(i++));
                edcSkincareBean.setAttribute5name(reader.get(i++));
                edcSkincareBean.setAttribute5value(reader.get(i++));
                edcSkincareBean.setAttribute6name(reader.get(i++));
                edcSkincareBean.setAttribute6value(reader.get(i++));
                edcSkincareBean.setAttribute7name(reader.get(i++));
                edcSkincareBean.setAttribute7value(reader.get(i++));
                edcSkincareBean.setAttribute8name(reader.get(i++));
                edcSkincareBean.setAttribute8value(reader.get(i++));
                edcSkincareBean.setAttribute9name(reader.get(i++));
                edcSkincareBean.setAttribute9value(reader.get(i++));
                edcSkincareBean.setAttribute10name(reader.get(i++));
                edcSkincareBean.setAttribute10value(reader.get(i++));
                edcSkincareBean.setAttribute11name(reader.get(i++));
                edcSkincareBean.setAttribute11value(reader.get(i++));
                edcSkincareBean.setAttribute12name(reader.get(i++));
                edcSkincareBean.setAttribute12value(reader.get(i++));
                edcSkincareBean.setAttribute13name(reader.get(i++));
                edcSkincareBean.setAttribute13value(reader.get(i++));
                edcSkincareBean.setAttribute14name(reader.get(i++));
                edcSkincareBean.setAttribute14value(reader.get(i++));
                edcSkincareBean.setAttribute15name(reader.get(i++));
                edcSkincareBean.setAttribute15value(reader.get(i++));
                edcSkincareBean.setAttribute16name(reader.get(i++));
                edcSkincareBean.setAttribute16value(reader.get(i++));
                edcSkincareBean.setAttribute17name(reader.get(i++));
                edcSkincareBean.setAttribute17value(reader.get(i++));
                edcSkincareBean.setAttribute18name(reader.get(i++));
                edcSkincareBean.setAttribute18value(reader.get(i++));
                edcSkincareBean.setAttribute19name(reader.get(i++));
                edcSkincareBean.setAttribute19value(reader.get(i++));
                edcSkincareBean.setAttribute20name(reader.get(i++));
                edcSkincareBean.setAttribute20value(reader.get(i++));
                edcSkincareBean.setAttribute21name(reader.get(i++));
                edcSkincareBean.setAttribute21value(reader.get(i++));
                edcSkincareBean.setAttribute22name(reader.get(i++));
                edcSkincareBean.setAttribute22value(reader.get(i++));
                edcSkincareBean.setAttribute23name(reader.get(i++));
                edcSkincareBean.setAttribute23value(reader.get(i++));
                edcSkincareBean.setAttribute24name(reader.get(i++));
                edcSkincareBean.setAttribute24value(reader.get(i++));
                edcSkincareBean.setAttribute25name(reader.get(i++));
                edcSkincareBean.setAttribute25value(reader.get(i++));
                edcSkincareBean.setAttribute26name(reader.get(i++));
                edcSkincareBean.setAttribute26value(reader.get(i++));
                edcSkincareBean.setAttribute27name(reader.get(i++));
                edcSkincareBean.setAttribute27value(reader.get(i++));
                edcSkincareBean.setAttribute28name(reader.get(i++));
                edcSkincareBean.setAttribute28value(reader.get(i++));
                edcSkincareBean.setAttribute29name(reader.get(i++));
                edcSkincareBean.setAttribute29value(reader.get(i++));
                edcSkincareBean.setAttribute30name(reader.get(i++));
                edcSkincareBean.setAttribute30value(reader.get(i++));
                edcSkincareBean.setAttribute31name(reader.get(i++));
                edcSkincareBean.setAttribute31value(reader.get(i++));
                edcSkincareBean.setAttribute32name(reader.get(i++));
                edcSkincareBean.setAttribute32value(reader.get(i++));
                edcSkincareBean.setAttribute33name(reader.get(i++));
                edcSkincareBean.setAttribute33value(reader.get(i++));
                edcSkincareBean.setAttribute34name(reader.get(i++));
                edcSkincareBean.setAttribute34value(reader.get(i++));
                edcSkincareBean.setAttribute35name(reader.get(i++));
                edcSkincareBean.setAttribute35value(reader.get(i++));
                edcSkincareBean.setAttribute36name(reader.get(i++));
                edcSkincareBean.setAttribute36value(reader.get(i++));
                edcSkincareBean.setAttribute37name(reader.get(i++));
                edcSkincareBean.setAttribute37value(reader.get(i++));
                edcSkincareBean.setAttribute38name(reader.get(i++));
                edcSkincareBean.setAttribute38value(reader.get(i++));
                edcSkincareBean.setAttribute39name(reader.get(i++));
                edcSkincareBean.setAttribute39value(reader.get(i++));
                edcSkincareBean.setAttribute40name(reader.get(i++));
                edcSkincareBean.setAttribute40value(reader.get(i++));
                edcSkincareBean.setAttribute41name(reader.get(i++));
                edcSkincareBean.setAttribute41value(reader.get(i++));
                edcSkincareBean.setAttribute42name(reader.get(i++));
                edcSkincareBean.setAttribute42value(reader.get(i++));
                edcSkincareBean.setAttribute43name(reader.get(i++));
                edcSkincareBean.setAttribute43value(reader.get(i++));
                edcSkincareBean.setAttribute44name(reader.get(i++));
                edcSkincareBean.setAttribute44value(reader.get(i++));
                edcSkincareBean.setAttribute45name(reader.get(i++));
                edcSkincareBean.setAttribute45value(reader.get(i++));
                edcSkincareBean.setAttribute46name(reader.get(i++));
                edcSkincareBean.setAttribute46value(reader.get(i++));
                edcSkincareBean.setAttribute47name(reader.get(i++));
                edcSkincareBean.setAttribute47value(reader.get(i++));
                edcSkincareBean.setAttribute48name(reader.get(i++));
                edcSkincareBean.setAttribute48value(reader.get(i++));
                edcSkincareBean.setAttribute49name(reader.get(i++));
                edcSkincareBean.setAttribute49value(reader.get(i++));
                edcSkincareBean.setAttribute50name(reader.get(i++));
                edcSkincareBean.setAttribute50value(reader.get(i++));
                edcSkincareBean.setAttribute51name(reader.get(i++));
                edcSkincareBean.setAttribute51value(reader.get(i++));
                edcSkincareBean.setAttribute52name(reader.get(i++));
                edcSkincareBean.setAttribute52value(reader.get(i++));
                edcSkincareBean.setAttribute53name(reader.get(i++));
                edcSkincareBean.setAttribute53value(reader.get(i++));
                edcSkincareBean.setAttribute54name(reader.get(i++));
                edcSkincareBean.setAttribute54value(reader.get(i++));
                edcSkincareBean.setAttribute55name(reader.get(i++));
                edcSkincareBean.setAttribute55value(reader.get(i++));
                edcSkincareBean.setAttribute56name(reader.get(i++));
                edcSkincareBean.setAttribute56value(reader.get(i++));
                edcSkincareBean.setAttribute57name(reader.get(i++));
                edcSkincareBean.setAttribute57value(reader.get(i++));
                edcSkincareBean.setAttribute58name(reader.get(i++));
                edcSkincareBean.setAttribute58value(reader.get(i++));
                edcSkincareBean.setAttribute59name(reader.get(i++));
                edcSkincareBean.setAttribute59value(reader.get(i++));
                edcSkincareBean.setAttribute60name(reader.get(i++));
                edcSkincareBean.setAttribute60value(reader.get(i++));
                edcSkincareBean.setAttribute61name(reader.get(i++));
                edcSkincareBean.setAttribute61value(reader.get(i++));
                edcSkincareBean.setAttribute62name(reader.get(i++));
                edcSkincareBean.setAttribute62value(reader.get(i++));
                edcSkincareBean.setAttribute63name(reader.get(i++));
                edcSkincareBean.setAttribute63value(reader.get(i++));
                edcSkincareBean.setAttribute64name(reader.get(i++));
                edcSkincareBean.setAttribute64value(reader.get(i++));
                edcSkincareBean.setAttribute65name(reader.get(i++));
                edcSkincareBean.setAttribute65value(reader.get(i++));
                edcSkincareBean.setAttribute66name(reader.get(i++));
                edcSkincareBean.setAttribute66value(reader.get(i++));
                edcSkincareBean.setAttribute67name(reader.get(i++));
                edcSkincareBean.setAttribute67value(reader.get(i++));
                edcSkincareBean.setAttribute68name(reader.get(i++));
                edcSkincareBean.setAttribute68value(reader.get(i++));
                edcSkincareBean.setAttribute69name(reader.get(i++));
                edcSkincareBean.setAttribute69value(reader.get(i++));
                edcSkincareBean.setAttribute70name(reader.get(i++));
                edcSkincareBean.setAttribute70value(reader.get(i++));
                edcSkincareBean.setAttribute71name(reader.get(i++));
                edcSkincareBean.setAttribute71value(reader.get(i++));
                edcSkincareBean.setAttribute72name(reader.get(i++));
                edcSkincareBean.setAttribute72value(reader.get(i++));
                edcSkincareBean.setAttribute73name(reader.get(i++));
                edcSkincareBean.setAttribute73value(reader.get(i++));
                edcSkincareBean.setAttribute74name(reader.get(i++));
                edcSkincareBean.setAttribute74value(reader.get(i++));
                edcSkincareBean.setAttribute75name(reader.get(i++));
                edcSkincareBean.setAttribute75value(reader.get(i++));
                edcSkincareBean.setAttribute76name(reader.get(i++));
                edcSkincareBean.setAttribute76value(reader.get(i++));
                edcSkincareBean.setAttribute77name(reader.get(i++));
                edcSkincareBean.setAttribute77value(reader.get(i++));
                edcSkincareBean.setAttribute78name(reader.get(i++));
                edcSkincareBean.setAttribute78value(reader.get(i++));
                edcSkincareBean.setAttribute79name(reader.get(i++));
                edcSkincareBean.setAttribute79value(reader.get(i++));
                edcSkincareBean.setAttribute80name(reader.get(i++));
                edcSkincareBean.setAttribute80value(reader.get(i++));
                edcSkincareBean.setAttribute81name(reader.get(i++));
                edcSkincareBean.setAttribute81value(reader.get(i++));
                edcSkincareBean.setAttribute82name(reader.get(i++));
                edcSkincareBean.setAttribute82value(reader.get(i++));
                edcSkincareBean.setAttribute83name(reader.get(i++));
                edcSkincareBean.setAttribute83value(reader.get(i++));
                edcSkincareBean.setAttribute84name(reader.get(i++));
                edcSkincareBean.setAttribute84value(reader.get(i++));
                edcSkincareBean.setAttribute85name(reader.get(i++));
                edcSkincareBean.setAttribute85value(reader.get(i++));
                edcSkincareBean.setAttribute86name(reader.get(i++));
                edcSkincareBean.setAttribute86value(reader.get(i++));
                edcSkincareBean.setAttribute87name(reader.get(i++));
                edcSkincareBean.setAttribute87value(reader.get(i++));
                edcSkincareBean.setAttribute88name(reader.get(i++));
                edcSkincareBean.setAttribute88value(reader.get(i++));
                edcSkincareBean.setAttribute89name(reader.get(i++));
                edcSkincareBean.setAttribute89value(reader.get(i++));
                edcSkincareBean.setAttribute90name(reader.get(i++));
                edcSkincareBean.setAttribute90value(reader.get(i++));
                edcSkincareBean.setAttribute91name(reader.get(i++));
                edcSkincareBean.setAttribute91value(reader.get(i++));
                edcSkincareBean.setAttribute92name(reader.get(i++));
                edcSkincareBean.setAttribute92value(reader.get(i++));
                edcSkincareBean.setAttribute93name(reader.get(i++));
                edcSkincareBean.setAttribute93value(reader.get(i++));
                edcSkincareBean.setAttribute94name(reader.get(i++));
                edcSkincareBean.setAttribute94value(reader.get(i++));
                edcSkincareBean.setAttribute95name(reader.get(i++));
                edcSkincareBean.setAttribute95value(reader.get(i++));
                edcSkincareBean.setAttribute96name(reader.get(i++));
                edcSkincareBean.setAttribute96value(reader.get(i++));
                edcSkincareBean.setAttribute97name(reader.get(i++));
                edcSkincareBean.setAttribute97value(reader.get(i++));
                edcSkincareBean.setAttribute98name(reader.get(i++));
                edcSkincareBean.setAttribute98value(reader.get(i++));
                edcSkincareBean.setAttribute99name(reader.get(i++));
                edcSkincareBean.setAttribute99value(reader.get(i++));
                edcSkincareBean.setAttribute100name(reader.get(i++));
                edcSkincareBean.setAttribute100value(reader.get(i++));
                edcSkincareBean.setAttribute101name(reader.get(i++));
                edcSkincareBean.setAttribute101value(reader.get(i++));
                edcSkincareBean.setAttribute102name(reader.get(i++));
                edcSkincareBean.setAttribute102value(reader.get(i++));
                edcSkincareBean.setAttribute103name(reader.get(i++));
                edcSkincareBean.setAttribute103value(reader.get(i++));
                edcSkincareBean.setAttribute104name(reader.get(i++));
                edcSkincareBean.setAttribute104value(reader.get(i++));
                edcSkincareBean.setAttribute105name(reader.get(i++));
                edcSkincareBean.setAttribute105value(reader.get(i++));
                edcSkincareBean.setAttribute106name(reader.get(i++));
                edcSkincareBean.setAttribute106value(reader.get(i++));
                edcSkincareBean.setAttribute107name(reader.get(i++));
                edcSkincareBean.setAttribute107value(reader.get(i++));
                edcSkincareBean.setAttribute108name(reader.get(i++));
                edcSkincareBean.setAttribute108value(reader.get(i++));
                edcSkincareBean.setAttribute109name(reader.get(i++));
                edcSkincareBean.setAttribute109value(reader.get(i++));
                edcSkincareBean.setAttribute110name(reader.get(i++));
                edcSkincareBean.setAttribute110value(reader.get(i++));
                edcSkincareBean.setAttribute111name(reader.get(i++));
                edcSkincareBean.setAttribute111value(reader.get(i++));
                edcSkincareBean.setAttribute112name(reader.get(i++));
                edcSkincareBean.setAttribute112value(reader.get(i++));
                edcSkincareBean.setAttribute113name(reader.get(i++));
                edcSkincareBean.setAttribute113value(reader.get(i++));
                edcSkincareBean.setAttribute114name(reader.get(i++));
                edcSkincareBean.setAttribute114value(reader.get(i++));
                edcSkincareBean.setAttribute115name(reader.get(i++));
                edcSkincareBean.setAttribute115value(reader.get(i++));
                edcSkincareBean.setAttribute116name(reader.get(i++));
                edcSkincareBean.setAttribute116value(reader.get(i++));
                edcSkincareBean.setAttribute117name(reader.get(i++));
                edcSkincareBean.setAttribute117value(reader.get(i++));
                edcSkincareBean.setAttribute118name(reader.get(i++));
                edcSkincareBean.setAttribute118value(reader.get(i++));
                edcSkincareBean.setAttribute119name(reader.get(i++));
                edcSkincareBean.setAttribute119value(reader.get(i++));
                edcSkincareBean.setAttribute120name(reader.get(i++));
                edcSkincareBean.setAttribute120value(reader.get(i++));
                edcSkincareBean.setAttribute121name(reader.get(i++));
                edcSkincareBean.setAttribute121value(reader.get(i++));
                edcSkincareBean.setAttribute122name(reader.get(i++));
                edcSkincareBean.setAttribute122value(reader.get(i++));
                edcSkincareBean.setAttribute123name(reader.get(i++));
                edcSkincareBean.setAttribute123value(reader.get(i++));
                edcSkincareBean.setAttribute124name(reader.get(i++));
                edcSkincareBean.setAttribute124value(reader.get(i++));
                edcSkincareBean.setAttribute125name(reader.get(i++));
                edcSkincareBean.setAttribute125value(reader.get(i++));
                edcSkincareBean.setAttribute126name(reader.get(i++));
                edcSkincareBean.setAttribute126value(reader.get(i++));
                edcSkincareBean.setAttribute127name(reader.get(i++));
                edcSkincareBean.setAttribute127value(reader.get(i++));
                edcSkincareBean.setAttribute128name(reader.get(i++));
                edcSkincareBean.setAttribute128value(reader.get(i++));
                edcSkincareBean.setAttribute129name(reader.get(i++));
                edcSkincareBean.setAttribute129value(reader.get(i++));
                edcSkincareBean.setAttribute130name(reader.get(i++));
                edcSkincareBean.setAttribute130value(reader.get(i++));
                edcSkincareBean.setAttribute131name(reader.get(i++));
                edcSkincareBean.setAttribute131value(reader.get(i++));
                edcSkincareBean.setAttribute132name(reader.get(i++));
                edcSkincareBean.setAttribute132value(reader.get(i++));
                edcSkincareBean.setAttribute133name(reader.get(i++));
                edcSkincareBean.setAttribute133value(reader.get(i++));
                edcSkincareBean.setAttribute134name(reader.get(i++));
                edcSkincareBean.setAttribute134value(reader.get(i++));
                edcSkincareBean.setAttribute135name(reader.get(i++));
                edcSkincareBean.setAttribute135value(reader.get(i++));
                edcSkincareBean.setAttribute136name(reader.get(i++));
                edcSkincareBean.setAttribute136value(reader.get(i++));
                edcSkincareBean.setAttribute137name(reader.get(i++));
                edcSkincareBean.setAttribute137value(reader.get(i++));
                edcSkincareBean.setAttribute138name(reader.get(i++));
                edcSkincareBean.setAttribute138value(reader.get(i++));
                edcSkincareBean.setAttribute139name(reader.get(i++));
                edcSkincareBean.setAttribute139value(reader.get(i++));
                edcSkincareBean.setAttribute140name(reader.get(i++));
                edcSkincareBean.setAttribute140value(reader.get(i++));
                edcSkincareBean.setAttribute141name(reader.get(i++));
                edcSkincareBean.setAttribute141value(reader.get(i++));
                edcSkincareBean.setAttribute142name(reader.get(i++));
                edcSkincareBean.setAttribute142value(reader.get(i++));
                edcSkincareBean.setAttribute143name(reader.get(i++));
                edcSkincareBean.setAttribute143value(reader.get(i++));
                edcSkincareBean.setAttribute144name(reader.get(i++));
                edcSkincareBean.setAttribute144value(reader.get(i++));
                edcSkincareBean.setAttribute145name(reader.get(i++));
                edcSkincareBean.setAttribute145value(reader.get(i++));
                edcSkincareBean.setAttribute146name(reader.get(i++));
                edcSkincareBean.setAttribute146value(reader.get(i++));
                edcSkincareBean.setAttribute147name(reader.get(i++));
                edcSkincareBean.setAttribute147value(reader.get(i++));
                edcSkincareBean.setAttribute148name(reader.get(i++));
                edcSkincareBean.setAttribute148value(reader.get(i++));
                edcSkincareBean.setAttribute150name(reader.get(i++));
                edcSkincareBean.setAttribute150value(reader.get(i++));
                edcSkincareBean.setAttribute151name(reader.get(i++));
                edcSkincareBean.setAttribute151value(reader.get(i++));
                edcSkincareBean.setAttribute152name(reader.get(i++));
                edcSkincareBean.setAttribute152value(reader.get(i++));
                edcSkincareBean.setAttribute153name(reader.get(i++));
                edcSkincareBean.setAttribute153value(reader.get(i++));
                edcSkincareBean.setAttribute154name(reader.get(i++));
                edcSkincareBean.setAttribute154value(reader.get(i++));
                edcSkincareBean.setAttribute155name(reader.get(i++));
                edcSkincareBean.setAttribute155value(reader.get(i++));
                edcSkincareBean.setAttribute156name(reader.get(i++));
                edcSkincareBean.setAttribute156value(reader.get(i++));
                edcSkincareBean.setAttribute157name(reader.get(i++));
                edcSkincareBean.setAttribute157value(reader.get(i++));
                edcSkincareBean.setAttribute158name(reader.get(i++));
                edcSkincareBean.setAttribute158value(reader.get(i++));
                edcSkincareBean.setAttribute159name(reader.get(i++));
                edcSkincareBean.setAttribute159value(reader.get(i++));
                edcSkincareBean.setAttribute160name(reader.get(i++));
                edcSkincareBean.setAttribute160value(reader.get(i++));
                edcSkincareBean.setAttribute161name(reader.get(i++));
                edcSkincareBean.setAttribute161value(reader.get(i++));
                edcSkincareBean.setAttribute162name(reader.get(i++));
                edcSkincareBean.setAttribute162value(reader.get(i++));
                edcSkincareBean.setAttribute163name(reader.get(i++));
                edcSkincareBean.setAttribute163value(reader.get(i++));
                edcSkincareBean.setAttribute164name(reader.get(i++));
                edcSkincareBean.setAttribute164value(reader.get(i++));
                edcSkincareBean.setAttribute165name(reader.get(i++));
                edcSkincareBean.setAttribute165value(reader.get(i++));
                edcSkincareBean.setAttribute166name(reader.get(i++));
                edcSkincareBean.setAttribute166value(reader.get(i++));
                edcSkincareBean.setAttribute167name(reader.get(i++));
                edcSkincareBean.setAttribute167value(reader.get(i++));
                edcSkincareBean.setAttribute168name(reader.get(i++));
                edcSkincareBean.setAttribute168value(reader.get(i++));
                edcSkincareBean.setAttribute169name(reader.get(i++));
                edcSkincareBean.setAttribute169value(reader.get(i++));
                edcSkincareBean.setAttribute170name(reader.get(i++));
                edcSkincareBean.setAttribute170value(reader.get(i++));
                superfeed.add(edcSkincareBean);
                cnt++;
                if (superfeed.size() > 1000) {
                    transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                    superfeed.clear();
                }
            }
            if (superfeed.size() > 0) {
                transactionRunner.runWithTran(() -> insertSuperFeed(superfeed));
                superfeed.clear();
            }
            reader.close();
            $info("SwissWatch产品文件读入完成");
        } catch (FileNotFoundException e) {
            $info("SwissWatch产品文件读入不存在");
        } catch (Exception ex) {
            ex.printStackTrace();
            $info("SwissWatch产品文件读入失败");
            logIssue("cms 数据导入处理", "SwissWatch产品文件读入失败 " + ex.getMessage());
        }
        return cnt;
    }

    /**
     * JE产品信息插入
     *
     * @return isSuccess
     */
    public boolean insertSuperFeed(List<SuperFeedSwissWatchBean> superfeedlist) {

        for (SuperFeedSwissWatchBean superfeed : superfeedlist) {

            if (swissWatchFeedDao.insertSelective(superfeed) <= 0) {
                $info("SwissWatch产品信息插入失败 Sku = " + superfeed.getInventorynumber());
            }
        }
        return true;
    }

    @Override
    public ChannelConfigEnums.Channel getChannel() {
        return SWISSWATCH;
    }

    @Override
    protected List<CmsBtFeedInfoModel> getFeedInfoByCategory(String categorPath) {
        Map colums = getColumns();
        Map<String, CmsBtFeedInfoModel> codeMap = new HashMap<>();

        List<FeedBean> feedBeans = Feeds.getConfigs(channel.getId(), FeedEnums.Name.valueOf("attribute"));
        List<String> attList = new ArrayList<>();
        for (FeedBean feedConfig : feedBeans) {
            if (!StringUtil.isEmpty(feedConfig.getCfg_val1())) {
                attList.add(feedConfig.getCfg_val1());
            }
        }

        // 条件则根据类目筛选
        String where = String.format("WHERE %s AND %s = '%s' ", INSERT_FLG, colums.get("category").toString(),
                categorPath.replace("'", "\\\'"));

        colums.put("keyword", where);
        colums.put("tableName", table);
        if (attList.size() > 0) {
            colums.put("attr", attList.stream().map(s -> "`" + s + "`").collect(Collectors.joining(",")));
        }

        List<CmsBtFeedInfoSwissWatchModel> vtmModelBeans = swissWatchFeedDao.selectSuperfeedModel(colums);
        List<CmsBtFeedInfoModel> modelBeans = new ArrayList<>();
        for (CmsBtFeedInfoSwissWatchModel vtmModelBean : vtmModelBeans) {

            Map temp = JacksonUtil.json2Bean(JacksonUtil.bean2Json(vtmModelBean), HashMap.class);
            Map<String, List<String>> attribute = new HashMap<>();

            for (String attr : attList) {
                String key = CamelUtil.underlineToCamel(attr.toLowerCase());
                if (temp.get(key) == null || StringUtil.isEmpty(temp.get(key).toString())) continue;
                List<String> values = new ArrayList<>();
                if (key.contains("attribute")) continue;
                values.add((String) temp.get(key));
                attribute.put(key, values);
            }
            for(int i = 2;i<170;i++){
                if(temp.get("attribute"+i+"value") != null){
                    String value= (String)temp.get("attribute"+i+"value");
                    if(!StringUtil.isEmpty(value)){
                        List<String> values= new ArrayList<>();
                        values.add(value);
                        attribute.put(temp.get("attribute" + i + "name").toString(), values);
                    }
                }
            }

            CmsBtFeedInfoModel cmsBtFeedInfoModel = vtmModelBean.getCmsBtFeedInfoModel(getChannel());
            cmsBtFeedInfoModel.setAttribute(attribute);
            List<CmsBtFeedInfoModel_Sku> skus = vtmModelBean.getSkus();
            for (CmsBtFeedInfoModel_Sku sku : skus) {
                String Weight = sku.getWeightOrg().trim();
                Pattern pattern = Pattern.compile("[^0-9.]");
                Matcher matcher = pattern.matcher(Weight);
                if (matcher.find()) {
                    int index = Weight.indexOf(matcher.group());
                    if (index != -1) {
                        String weightOrg = Weight.substring(0, index);
                        sku.setWeightOrg(weightOrg);
                    }
                }
                sku.setWeightOrgUnit("lb");
            }
            if (codeMap.containsKey(cmsBtFeedInfoModel.getCode())) {
                CmsBtFeedInfoModel beforeFeed = codeMap.get(cmsBtFeedInfoModel.getCode());
                beforeFeed.getSkus().addAll(cmsBtFeedInfoModel.getSkus());
                beforeFeed.getImage().addAll(cmsBtFeedInfoModel.getImage());
                beforeFeed.setImage(beforeFeed.getImage().stream().distinct().collect(Collectors.toList()));
                beforeFeed.setAttribute(attributeMerge(beforeFeed.getAttribute(), cmsBtFeedInfoModel.getAttribute()));
            } else {
                modelBeans.add(cmsBtFeedInfoModel);
                codeMap.put(cmsBtFeedInfoModel.getCode(), cmsBtFeedInfoModel);
            }

        }
        $info("取得 [ %s ] 的 Product 数 %s", categorPath, modelBeans.size());

        return modelBeans;
    }

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsSwissWatchAnalysisJob";
    }
}
