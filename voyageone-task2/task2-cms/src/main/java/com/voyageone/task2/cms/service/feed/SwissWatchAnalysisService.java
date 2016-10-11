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
                SuperFeedSwissWatchBean swissWatchan = new SuperFeedSwissWatchBean();
                int i = 0;
                swissWatchan.setInventorynumber(reader.get(i++));
                swissWatchan.setAuctiontitle(reader.get(i++));
                swissWatchan.setUpc(reader.get(i++));
                swissWatchan.setMpn(reader.get(i++));
                swissWatchan.setDescription(reader.get(i++));
                swissWatchan.setManufacturer(reader.get(i++));
                swissWatchan.setBrand(reader.get(i++));
                swissWatchan.setSellercost(reader.get(i++));
                swissWatchan.setProductmargin(reader.get(i++));
                swissWatchan.setBuyitnowprice(reader.get(i++));
                swissWatchan.setPictureurls(reader.get(i++));
                swissWatchan.setClassification(reader.get(i++));
                swissWatchan.setAttribute1name(reader.get(i++));
                swissWatchan.setAttribute1value(reader.get(i++));
                swissWatchan.setAttribute2name(reader.get(i++));
                swissWatchan.setAttribute2value(reader.get(i++));
                swissWatchan.setAttribute3name(reader.get(i++));
                swissWatchan.setAttribute3value(reader.get(i++));
                swissWatchan.setAttribute4name(reader.get(i++));
                swissWatchan.setAttribute4value(reader.get(i++));
                swissWatchan.setAttribute5name(reader.get(i++));
                swissWatchan.setAttribute5value(reader.get(i++));
                swissWatchan.setAttribute6name(reader.get(i++));
                swissWatchan.setAttribute6value(reader.get(i++));
                swissWatchan.setAttribute7name(reader.get(i++));
                swissWatchan.setAttribute7value(reader.get(i++));
                swissWatchan.setAttribute8name(reader.get(i++));
                swissWatchan.setAttribute8value(reader.get(i++));
                swissWatchan.setAttribute9name(reader.get(i++));
                swissWatchan.setAttribute9value(reader.get(i++));
                swissWatchan.setAttribute10name(reader.get(i++));
                swissWatchan.setAttribute10value(reader.get(i++));
                swissWatchan.setAttribute11name(reader.get(i++));
                swissWatchan.setAttribute11value(reader.get(i++));
                swissWatchan.setAttribute12name(reader.get(i++));
                swissWatchan.setAttribute12value(reader.get(i++));
                swissWatchan.setAttribute13name(reader.get(i++));
                swissWatchan.setAttribute13value(reader.get(i++));
                swissWatchan.setAttribute14name(reader.get(i++));
                swissWatchan.setAttribute14value(reader.get(i++));
                swissWatchan.setAttribute15name(reader.get(i++));
                swissWatchan.setAttribute15value(reader.get(i++));
                swissWatchan.setAttribute16name(reader.get(i++));
                swissWatchan.setAttribute16value(reader.get(i++));
                swissWatchan.setAttribute17name(reader.get(i++));
                swissWatchan.setAttribute17value(reader.get(i++));
                swissWatchan.setAttribute18name(reader.get(i++));
                swissWatchan.setAttribute18value(reader.get(i++));
                swissWatchan.setAttribute19name(reader.get(i++));
                swissWatchan.setAttribute19value(reader.get(i++));
                swissWatchan.setAttribute20name(reader.get(i++));
                swissWatchan.setAttribute20value(reader.get(i++));
                swissWatchan.setAttribute21name(reader.get(i++));
                swissWatchan.setAttribute21value(reader.get(i++));
                swissWatchan.setAttribute22name(reader.get(i++));
                swissWatchan.setAttribute22value(reader.get(i++));
                swissWatchan.setAttribute23name(reader.get(i++));
                swissWatchan.setAttribute23value(reader.get(i++));
                swissWatchan.setAttribute24name(reader.get(i++));
                swissWatchan.setAttribute24value(reader.get(i++));
                swissWatchan.setAttribute25name(reader.get(i++));
                swissWatchan.setAttribute25value(reader.get(i++));
                swissWatchan.setAttribute26name(reader.get(i++));
                swissWatchan.setAttribute26value(reader.get(i++));
                swissWatchan.setAttribute27name(reader.get(i++));
                swissWatchan.setAttribute27value(reader.get(i++));
                swissWatchan.setAttribute28name(reader.get(i++));
                swissWatchan.setAttribute28value(reader.get(i++));
                swissWatchan.setAttribute29name(reader.get(i++));
                swissWatchan.setAttribute29value(reader.get(i++));
                swissWatchan.setAttribute30name(reader.get(i++));
                swissWatchan.setAttribute30value(reader.get(i++));
                swissWatchan.setAttribute31name(reader.get(i++));
                swissWatchan.setAttribute31value(reader.get(i++));
                swissWatchan.setAttribute32name(reader.get(i++));
                swissWatchan.setAttribute32value(reader.get(i++));
                swissWatchan.setAttribute33name(reader.get(i++));
                swissWatchan.setAttribute33value(reader.get(i++));
                swissWatchan.setAttribute34name(reader.get(i++));
                swissWatchan.setAttribute34value(reader.get(i++));
                swissWatchan.setAttribute35name(reader.get(i++));
                swissWatchan.setAttribute35value(reader.get(i++));
                swissWatchan.setAttribute36name(reader.get(i++));
                swissWatchan.setAttribute36value(reader.get(i++));
                swissWatchan.setAttribute37name(reader.get(i++));
                swissWatchan.setAttribute37value(reader.get(i++));
                swissWatchan.setAttribute38name(reader.get(i++));
                swissWatchan.setAttribute38value(reader.get(i++));
                swissWatchan.setAttribute39name(reader.get(i++));
                swissWatchan.setAttribute39value(reader.get(i++));
                swissWatchan.setAttribute40name(reader.get(i++));
                swissWatchan.setAttribute40value(reader.get(i++));
                swissWatchan.setAttribute41name(reader.get(i++));
                swissWatchan.setAttribute41value(reader.get(i++));
                swissWatchan.setAttribute42name(reader.get(i++));
                swissWatchan.setAttribute42value(reader.get(i++));
                swissWatchan.setAttribute43name(reader.get(i++));
                swissWatchan.setAttribute43value(reader.get(i++));
                swissWatchan.setAttribute44name(reader.get(i++));
                swissWatchan.setAttribute44value(reader.get(i++));
                swissWatchan.setAttribute45name(reader.get(i++));
                swissWatchan.setAttribute45value(reader.get(i++));
                swissWatchan.setAttribute46name(reader.get(i++));
                swissWatchan.setAttribute46value(reader.get(i++));
                swissWatchan.setAttribute47name(reader.get(i++));
                swissWatchan.setAttribute47value(reader.get(i++));
                swissWatchan.setAttribute48name(reader.get(i++));
                swissWatchan.setAttribute48value(reader.get(i++));
                swissWatchan.setAttribute49name(reader.get(i++));
                swissWatchan.setAttribute49value(reader.get(i++));
                swissWatchan.setAttribute50name(reader.get(i++));
                swissWatchan.setAttribute50value(reader.get(i++));
                swissWatchan.setAttribute51name(reader.get(i++));
                swissWatchan.setAttribute51value(reader.get(i++));
                swissWatchan.setAttribute52name(reader.get(i++));
                swissWatchan.setAttribute52value(reader.get(i++));
                swissWatchan.setAttribute53name(reader.get(i++));
                swissWatchan.setAttribute53value(reader.get(i++));
                swissWatchan.setAttribute54name(reader.get(i++));
                swissWatchan.setAttribute54value(reader.get(i++));
                swissWatchan.setAttribute55name(reader.get(i++));
                swissWatchan.setAttribute55value(reader.get(i++));
                swissWatchan.setAttribute56name(reader.get(i++));
                swissWatchan.setAttribute56value(reader.get(i++));
                swissWatchan.setAttribute57name(reader.get(i++));
                swissWatchan.setAttribute57value(reader.get(i++));
                swissWatchan.setAttribute58name(reader.get(i++));
                swissWatchan.setAttribute58value(reader.get(i++));
                swissWatchan.setAttribute59name(reader.get(i++));
                swissWatchan.setAttribute59value(reader.get(i++));
                swissWatchan.setAttribute60name(reader.get(i++));
                swissWatchan.setAttribute60value(reader.get(i++));
                swissWatchan.setAttribute61name(reader.get(i++));
                swissWatchan.setAttribute61value(reader.get(i++));
                swissWatchan.setAttribute62name(reader.get(i++));
                swissWatchan.setAttribute62value(reader.get(i++));
                swissWatchan.setAttribute63name(reader.get(i++));
                swissWatchan.setAttribute63value(reader.get(i++));
                swissWatchan.setAttribute64name(reader.get(i++));
                swissWatchan.setAttribute64value(reader.get(i++));
                swissWatchan.setAttribute65name(reader.get(i++));
                swissWatchan.setAttribute65value(reader.get(i++));
                swissWatchan.setAttribute66name(reader.get(i++));
                swissWatchan.setAttribute66value(reader.get(i++));
                swissWatchan.setAttribute67name(reader.get(i++));
                swissWatchan.setAttribute67value(reader.get(i++));
                swissWatchan.setAttribute68name(reader.get(i++));
                swissWatchan.setAttribute68value(reader.get(i++));
                swissWatchan.setAttribute69name(reader.get(i++));
                swissWatchan.setAttribute69value(reader.get(i++));
                swissWatchan.setAttribute70name(reader.get(i++));
                swissWatchan.setAttribute70value(reader.get(i++));
                swissWatchan.setAttribute71name(reader.get(i++));
                swissWatchan.setAttribute71value(reader.get(i++));
                swissWatchan.setAttribute72name(reader.get(i++));
                swissWatchan.setAttribute72value(reader.get(i++));
                swissWatchan.setAttribute73name(reader.get(i++));
                swissWatchan.setAttribute73value(reader.get(i++));
                swissWatchan.setAttribute74name(reader.get(i++));
                swissWatchan.setAttribute74value(reader.get(i++));
                swissWatchan.setAttribute75name(reader.get(i++));
                swissWatchan.setAttribute75value(reader.get(i++));
                swissWatchan.setAttribute76name(reader.get(i++));
                swissWatchan.setAttribute76value(reader.get(i++));
                swissWatchan.setAttribute77name(reader.get(i++));
                swissWatchan.setAttribute77value(reader.get(i++));
                swissWatchan.setAttribute78name(reader.get(i++));
                swissWatchan.setAttribute78value(reader.get(i++));
                swissWatchan.setAttribute79name(reader.get(i++));
                swissWatchan.setAttribute79value(reader.get(i++));
                swissWatchan.setAttribute80name(reader.get(i++));
                swissWatchan.setAttribute80value(reader.get(i++));
                swissWatchan.setAttribute81name(reader.get(i++));
                swissWatchan.setAttribute81value(reader.get(i++));
                swissWatchan.setAttribute82name(reader.get(i++));
                swissWatchan.setAttribute82value(reader.get(i++));
                swissWatchan.setAttribute83name(reader.get(i++));
                swissWatchan.setAttribute83value(reader.get(i++));
                swissWatchan.setAttribute84name(reader.get(i++));
                swissWatchan.setAttribute84value(reader.get(i++));
                swissWatchan.setAttribute85name(reader.get(i++));
                swissWatchan.setAttribute85value(reader.get(i++));
                swissWatchan.setAttribute86name(reader.get(i++));
                swissWatchan.setAttribute86value(reader.get(i++));
                swissWatchan.setAttribute87name(reader.get(i++));
                swissWatchan.setAttribute87value(reader.get(i++));
                swissWatchan.setAttribute88name(reader.get(i++));
                swissWatchan.setAttribute88value(reader.get(i++));
                swissWatchan.setAttribute89name(reader.get(i++));
                swissWatchan.setAttribute89value(reader.get(i++));
                swissWatchan.setAttribute90name(reader.get(i++));
                swissWatchan.setAttribute90value(reader.get(i++));
                swissWatchan.setAttribute91name(reader.get(i++));
                swissWatchan.setAttribute91value(reader.get(i++));
                swissWatchan.setAttribute92name(reader.get(i++));
                swissWatchan.setAttribute92value(reader.get(i++));
                swissWatchan.setAttribute93name(reader.get(i++));
                swissWatchan.setAttribute93value(reader.get(i++));
                swissWatchan.setAttribute94name(reader.get(i++));
                swissWatchan.setAttribute94value(reader.get(i++));
                swissWatchan.setAttribute95name(reader.get(i++));
                swissWatchan.setAttribute95value(reader.get(i++));
                swissWatchan.setAttribute96name(reader.get(i++));
                swissWatchan.setAttribute96value(reader.get(i++));
                swissWatchan.setAttribute97name(reader.get(i++));
                swissWatchan.setAttribute97value(reader.get(i++));
                swissWatchan.setAttribute98name(reader.get(i++));
                swissWatchan.setAttribute98value(reader.get(i++));
                swissWatchan.setAttribute99name(reader.get(i++));
                swissWatchan.setAttribute99value(reader.get(i++));
                swissWatchan.setAttribute100name(reader.get(i++));
                swissWatchan.setAttribute100value(reader.get(i++));
                swissWatchan.setAttribute101name(reader.get(i++));
                swissWatchan.setAttribute101value(reader.get(i++));
                swissWatchan.setAttribute102name(reader.get(i++));
                swissWatchan.setAttribute102value(reader.get(i++));
                swissWatchan.setAttribute103name(reader.get(i++));
                swissWatchan.setAttribute103value(reader.get(i++));
                swissWatchan.setAttribute104name(reader.get(i++));
                swissWatchan.setAttribute104value(reader.get(i++));
                swissWatchan.setAttribute105name(reader.get(i++));
                swissWatchan.setAttribute105value(reader.get(i++));
                swissWatchan.setAttribute106name(reader.get(i++));
                swissWatchan.setAttribute106value(reader.get(i++));
                swissWatchan.setAttribute107name(reader.get(i++));
                swissWatchan.setAttribute107value(reader.get(i++));
                swissWatchan.setAttribute108name(reader.get(i++));
                swissWatchan.setAttribute108value(reader.get(i++));
                swissWatchan.setAttribute109name(reader.get(i++));
                swissWatchan.setAttribute109value(reader.get(i++));
                swissWatchan.setAttribute110name(reader.get(i++));
                swissWatchan.setAttribute110value(reader.get(i++));
                swissWatchan.setAttribute111name(reader.get(i++));
                swissWatchan.setAttribute111value(reader.get(i++));
                swissWatchan.setAttribute112name(reader.get(i++));
                swissWatchan.setAttribute112value(reader.get(i++));
                swissWatchan.setAttribute113name(reader.get(i++));
                swissWatchan.setAttribute113value(reader.get(i++));
                swissWatchan.setAttribute114name(reader.get(i++));
                swissWatchan.setAttribute114value(reader.get(i++));
                swissWatchan.setAttribute115name(reader.get(i++));
                swissWatchan.setAttribute115value(reader.get(i++));
                swissWatchan.setAttribute116name(reader.get(i++));
                swissWatchan.setAttribute116value(reader.get(i++));
                swissWatchan.setAttribute117name(reader.get(i++));
                swissWatchan.setAttribute117value(reader.get(i++));
                swissWatchan.setAttribute118name(reader.get(i++));
                swissWatchan.setAttribute118value(reader.get(i++));
                swissWatchan.setMsrp(reader.get(i++));
                swissWatchan.setAttribute120name(reader.get(i++));
                swissWatchan.setAttribute120value(reader.get(i++));
                swissWatchan.setAttribute121name(reader.get(i++));
                swissWatchan.setAttribute121value(reader.get(i++));
                swissWatchan.setAttribute122name(reader.get(i++));
                swissWatchan.setAttribute122value(reader.get(i++));
                swissWatchan.setAttribute123name(reader.get(i++));
                swissWatchan.setAttribute123value(reader.get(i++));
                swissWatchan.setAttribute124name(reader.get(i++));
                swissWatchan.setAttribute124value(reader.get(i++));
                swissWatchan.setAttribute125name(reader.get(i++));
                swissWatchan.setAttribute125value(reader.get(i++));
                swissWatchan.setAttribute126name(reader.get(i++));
                swissWatchan.setAttribute126value(reader.get(i++));
                swissWatchan.setAttribute127name(reader.get(i++));
                swissWatchan.setAttribute127value(reader.get(i++));
                swissWatchan.setAttribute128name(reader.get(i++));
                swissWatchan.setAttribute128value(reader.get(i++));
                swissWatchan.setAttribute129name(reader.get(i++));
                swissWatchan.setAttribute129value(reader.get(i++));
                swissWatchan.setAttribute130name(reader.get(i++));
                swissWatchan.setAttribute130value(reader.get(i++));
                swissWatchan.setAttribute131name(reader.get(i++));
                swissWatchan.setAttribute131value(reader.get(i++));
                swissWatchan.setAttribute132name(reader.get(i++));
                swissWatchan.setAttribute132value(reader.get(i++));
                swissWatchan.setAttribute133name(reader.get(i++));
                swissWatchan.setAttribute133value(reader.get(i++));
                swissWatchan.setAttribute134name(reader.get(i++));
                swissWatchan.setAttribute134value(reader.get(i++));
                swissWatchan.setAttribute135name(reader.get(i++));
                swissWatchan.setAttribute135value(reader.get(i++));
                swissWatchan.setAttribute136name(reader.get(i++));
                swissWatchan.setAttribute136value(reader.get(i++));
                swissWatchan.setAttribute137name(reader.get(i++));
                swissWatchan.setAttribute137value(reader.get(i++));
                swissWatchan.setAttribute138name(reader.get(i++));
                swissWatchan.setAttribute138value(reader.get(i++));
                swissWatchan.setAttribute139name(reader.get(i++));
                swissWatchan.setAttribute139value(reader.get(i++));
                swissWatchan.setAttribute140name(reader.get(i++));
                swissWatchan.setAttribute140value(reader.get(i++));
                swissWatchan.setAttribute141name(reader.get(i++));
                swissWatchan.setAttribute141value(reader.get(i++));
                swissWatchan.setAttribute142name(reader.get(i++));
                swissWatchan.setAttribute142value(reader.get(i++));
                swissWatchan.setAttribute143name(reader.get(i++));
                swissWatchan.setAttribute143value(reader.get(i++));
                swissWatchan.setAttribute144name(reader.get(i++));
                swissWatchan.setAttribute144value(reader.get(i++));
                swissWatchan.setAttribute145name(reader.get(i++));
                swissWatchan.setAttribute145value(reader.get(i++));
                swissWatchan.setAttribute146name(reader.get(i++));
                swissWatchan.setAttribute146value(reader.get(i++));
                swissWatchan.setAttribute147name(reader.get(i++));
                swissWatchan.setAttribute147value(reader.get(i++));
                swissWatchan.setAttribute148name(reader.get(i++));
                swissWatchan.setAttribute148value(reader.get(i++));
                swissWatchan.setAttribute150name(reader.get(i++));
                swissWatchan.setAttribute150value(reader.get(i++));
                swissWatchan.setAttribute151name(reader.get(i++));
                swissWatchan.setAttribute151value(reader.get(i++));
                swissWatchan.setAttribute152name(reader.get(i++));
                swissWatchan.setAttribute152value(reader.get(i++));
                swissWatchan.setAttribute153name(reader.get(i++));
                swissWatchan.setAttribute153value(reader.get(i++));
                swissWatchan.setAttribute154name(reader.get(i++));
                swissWatchan.setAttribute154value(reader.get(i++));
                swissWatchan.setAttribute155name(reader.get(i++));
                swissWatchan.setAttribute155value(reader.get(i++));
                swissWatchan.setAttribute156name(reader.get(i++));
                swissWatchan.setAttribute156value(reader.get(i++));
                swissWatchan.setAttribute157name(reader.get(i++));
                swissWatchan.setAttribute157value(reader.get(i++));
                swissWatchan.setAttribute158name(reader.get(i++));
                swissWatchan.setAttribute158value(reader.get(i++));
                swissWatchan.setAttribute159name(reader.get(i++));
                swissWatchan.setAttribute159value(reader.get(i++));
                swissWatchan.setAttribute160name(reader.get(i++));
                swissWatchan.setAttribute160value(reader.get(i++));
                swissWatchan.setAttribute161name(reader.get(i++));
                swissWatchan.setAttribute161value(reader.get(i++));
                swissWatchan.setAttribute162name(reader.get(i++));
                swissWatchan.setAttribute162value(reader.get(i++));
                swissWatchan.setAttribute163name(reader.get(i++));
                swissWatchan.setAttribute163value(reader.get(i++));
                swissWatchan.setAttribute164name(reader.get(i++));
                swissWatchan.setAttribute164value(reader.get(i++));
                swissWatchan.setAttribute165name(reader.get(i++));
                swissWatchan.setAttribute165value(reader.get(i++));
                swissWatchan.setAttribute166name(reader.get(i++));
                swissWatchan.setAttribute166value(reader.get(i++));
                swissWatchan.setAttribute167name(reader.get(i++));
                swissWatchan.setAttribute167value(reader.get(i++));
                swissWatchan.setAttribute168name(reader.get(i++));
                swissWatchan.setAttribute168value(reader.get(i++));
                swissWatchan.setAttribute169name(reader.get(i++));
                swissWatchan.setAttribute169value(reader.get(i++));
                swissWatchan.setVoyageOnePrice(reader.get(i++));
                superfeed.add(swissWatchan);
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
