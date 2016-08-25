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
                SuperFeedSwissWatchBean swissWatchBean = new SuperFeedSwissWatchBean();
                int i = 0;
                swissWatchBean.setInventorynumber(reader.get(i++));
                swissWatchBean.setAuctiontitle(reader.get(i++));
                swissWatchBean.setUpc(reader.get(i++));
                swissWatchBean.setMpn(reader.get(i++));
                swissWatchBean.setDescription(reader.get(i++));
                swissWatchBean.setManufacturer(reader.get(i++));
                swissWatchBean.setBrand(reader.get(i++));
                swissWatchBean.setSellercost(reader.get(i++));
                swissWatchBean.setProductmargin(reader.get(i++));
                swissWatchBean.setBuyitnowprice(reader.get(i++));
                swissWatchBean.setPictureurls(reader.get(i++));
                swissWatchBean.setClassification(reader.get(i++));
                swissWatchBean.setAttribute1name(reader.get(i++));
                swissWatchBean.setAttribute1value(reader.get(i++));
                swissWatchBean.setAttribute2name(reader.get(i++));
                swissWatchBean.setAttribute2value(reader.get(i++));
                swissWatchBean.setAttribute3name(reader.get(i++));
                swissWatchBean.setAttribute3value(reader.get(i++));
                swissWatchBean.setAttribute4name(reader.get(i++));
                swissWatchBean.setAttribute4value(reader.get(i++));
                swissWatchBean.setAttribute5name(reader.get(i++));
                swissWatchBean.setAttribute5value(reader.get(i++));
                swissWatchBean.setAttribute6name(reader.get(i++));
                swissWatchBean.setAttribute6value(reader.get(i++));
                swissWatchBean.setAttribute7name(reader.get(i++));
                swissWatchBean.setAttribute7value(reader.get(i++));
                swissWatchBean.setAttribute8name(reader.get(i++));
                swissWatchBean.setAttribute8value(reader.get(i++));
                swissWatchBean.setAttribute9name(reader.get(i++));
                swissWatchBean.setAttribute9value(reader.get(i++));
                swissWatchBean.setAttribute10name(reader.get(i++));
                swissWatchBean.setAttribute10value(reader.get(i++));
                swissWatchBean.setAttribute11name(reader.get(i++));
                swissWatchBean.setAttribute11value(reader.get(i++));
                swissWatchBean.setAttribute12name(reader.get(i++));
                swissWatchBean.setAttribute12value(reader.get(i++));
                swissWatchBean.setAttribute13name(reader.get(i++));
                swissWatchBean.setAttribute13value(reader.get(i++));
                swissWatchBean.setAttribute14name(reader.get(i++));
                swissWatchBean.setAttribute14value(reader.get(i++));
                swissWatchBean.setAttribute15name(reader.get(i++));
                swissWatchBean.setAttribute15value(reader.get(i++));
                swissWatchBean.setAttribute16name(reader.get(i++));
                swissWatchBean.setAttribute16value(reader.get(i++));
                swissWatchBean.setAttribute17name(reader.get(i++));
                swissWatchBean.setAttribute17value(reader.get(i++));
                swissWatchBean.setAttribute18name(reader.get(i++));
                swissWatchBean.setAttribute18value(reader.get(i++));
                swissWatchBean.setAttribute19name(reader.get(i++));
                swissWatchBean.setAttribute19value(reader.get(i++));
                swissWatchBean.setAttribute20name(reader.get(i++));
                swissWatchBean.setAttribute20value(reader.get(i++));
                swissWatchBean.setAttribute21name(reader.get(i++));
                swissWatchBean.setAttribute21value(reader.get(i++));
                swissWatchBean.setAttribute22name(reader.get(i++));
                swissWatchBean.setAttribute22value(reader.get(i++));
                swissWatchBean.setAttribute23name(reader.get(i++));
                swissWatchBean.setAttribute23value(reader.get(i++));
                swissWatchBean.setAttribute24name(reader.get(i++));
                swissWatchBean.setAttribute24value(reader.get(i++));
                swissWatchBean.setAttribute25name(reader.get(i++));
                swissWatchBean.setAttribute25value(reader.get(i++));
                swissWatchBean.setAttribute26name(reader.get(i++));
                swissWatchBean.setAttribute26value(reader.get(i++));
                swissWatchBean.setAttribute27name(reader.get(i++));
                swissWatchBean.setAttribute27value(reader.get(i++));
                swissWatchBean.setAttribute28name(reader.get(i++));
                swissWatchBean.setAttribute28value(reader.get(i++));
                swissWatchBean.setAttribute29name(reader.get(i++));
                swissWatchBean.setAttribute29value(reader.get(i++));
                swissWatchBean.setAttribute30name(reader.get(i++));
                swissWatchBean.setAttribute30value(reader.get(i++));
                swissWatchBean.setAttribute31name(reader.get(i++));
                swissWatchBean.setAttribute31value(reader.get(i++));
                swissWatchBean.setAttribute32name(reader.get(i++));
                swissWatchBean.setAttribute32value(reader.get(i++));
                swissWatchBean.setAttribute33name(reader.get(i++));
                swissWatchBean.setAttribute33value(reader.get(i++));
                swissWatchBean.setAttribute34name(reader.get(i++));
                swissWatchBean.setAttribute34value(reader.get(i++));
                swissWatchBean.setAttribute35name(reader.get(i++));
                swissWatchBean.setAttribute35value(reader.get(i++));
                swissWatchBean.setAttribute36name(reader.get(i++));
                swissWatchBean.setAttribute36value(reader.get(i++));
                swissWatchBean.setAttribute37name(reader.get(i++));
                swissWatchBean.setAttribute37value(reader.get(i++));
                swissWatchBean.setAttribute38name(reader.get(i++));
                swissWatchBean.setAttribute38value(reader.get(i++));
                swissWatchBean.setAttribute39name(reader.get(i++));
                swissWatchBean.setAttribute39value(reader.get(i++));
                swissWatchBean.setAttribute40name(reader.get(i++));
                swissWatchBean.setAttribute40value(reader.get(i++));
                swissWatchBean.setAttribute41name(reader.get(i++));
                swissWatchBean.setAttribute41value(reader.get(i++));
                swissWatchBean.setAttribute42name(reader.get(i++));
                swissWatchBean.setAttribute42value(reader.get(i++));
                swissWatchBean.setAttribute43name(reader.get(i++));
                swissWatchBean.setAttribute43value(reader.get(i++));
                swissWatchBean.setAttribute44name(reader.get(i++));
                swissWatchBean.setAttribute44value(reader.get(i++));
                swissWatchBean.setAttribute45name(reader.get(i++));
                swissWatchBean.setAttribute45value(reader.get(i++));
                swissWatchBean.setAttribute46name(reader.get(i++));
                swissWatchBean.setAttribute46value(reader.get(i++));
                swissWatchBean.setAttribute47name(reader.get(i++));
                swissWatchBean.setAttribute47value(reader.get(i++));
                swissWatchBean.setAttribute48name(reader.get(i++));
                swissWatchBean.setAttribute48value(reader.get(i++));
                swissWatchBean.setAttribute49name(reader.get(i++));
                swissWatchBean.setAttribute49value(reader.get(i++));
                swissWatchBean.setAttribute50name(reader.get(i++));
                swissWatchBean.setAttribute50value(reader.get(i++));
                swissWatchBean.setAttribute51name(reader.get(i++));
                swissWatchBean.setAttribute51value(reader.get(i++));
                swissWatchBean.setAttribute52name(reader.get(i++));
                swissWatchBean.setAttribute52value(reader.get(i++));
                swissWatchBean.setAttribute53name(reader.get(i++));
                swissWatchBean.setAttribute53value(reader.get(i++));
                swissWatchBean.setAttribute54name(reader.get(i++));
                swissWatchBean.setAttribute54value(reader.get(i++));
                swissWatchBean.setAttribute55name(reader.get(i++));
                swissWatchBean.setAttribute55value(reader.get(i++));
                swissWatchBean.setAttribute56name(reader.get(i++));
                swissWatchBean.setAttribute56value(reader.get(i++));
                swissWatchBean.setAttribute57name(reader.get(i++));
                swissWatchBean.setAttribute57value(reader.get(i++));
                swissWatchBean.setAttribute58name(reader.get(i++));
                swissWatchBean.setAttribute58value(reader.get(i++));
                swissWatchBean.setAttribute59name(reader.get(i++));
                swissWatchBean.setAttribute59value(reader.get(i++));
                swissWatchBean.setAttribute60name(reader.get(i++));
                swissWatchBean.setAttribute60value(reader.get(i++));
                swissWatchBean.setAttribute61name(reader.get(i++));
                swissWatchBean.setAttribute61value(reader.get(i++));
                swissWatchBean.setAttribute62name(reader.get(i++));
                swissWatchBean.setAttribute62value(reader.get(i++));
                swissWatchBean.setAttribute63name(reader.get(i++));
                swissWatchBean.setAttribute63value(reader.get(i++));
                swissWatchBean.setAttribute64name(reader.get(i++));
                swissWatchBean.setAttribute64value(reader.get(i++));
                swissWatchBean.setAttribute65name(reader.get(i++));
                swissWatchBean.setAttribute65value(reader.get(i++));
                swissWatchBean.setAttribute66name(reader.get(i++));
                swissWatchBean.setAttribute66value(reader.get(i++));
                swissWatchBean.setAttribute67name(reader.get(i++));
                swissWatchBean.setAttribute67value(reader.get(i++));
                swissWatchBean.setAttribute68name(reader.get(i++));
                swissWatchBean.setAttribute68value(reader.get(i++));
                swissWatchBean.setAttribute69name(reader.get(i++));
                swissWatchBean.setAttribute69value(reader.get(i++));
                swissWatchBean.setAttribute70name(reader.get(i++));
                swissWatchBean.setAttribute70value(reader.get(i++));
                swissWatchBean.setAttribute71name(reader.get(i++));
                swissWatchBean.setAttribute71value(reader.get(i++));
                swissWatchBean.setAttribute72name(reader.get(i++));
                swissWatchBean.setAttribute72value(reader.get(i++));
                swissWatchBean.setAttribute73name(reader.get(i++));
                swissWatchBean.setAttribute73value(reader.get(i++));
                swissWatchBean.setAttribute74name(reader.get(i++));
                swissWatchBean.setAttribute74value(reader.get(i++));
                swissWatchBean.setAttribute75name(reader.get(i++));
                swissWatchBean.setAttribute75value(reader.get(i++));
                swissWatchBean.setAttribute76name(reader.get(i++));
                swissWatchBean.setAttribute76value(reader.get(i++));
                swissWatchBean.setAttribute77name(reader.get(i++));
                swissWatchBean.setAttribute77value(reader.get(i++));
                swissWatchBean.setAttribute78name(reader.get(i++));
                swissWatchBean.setAttribute78value(reader.get(i++));
                swissWatchBean.setAttribute79name(reader.get(i++));
                swissWatchBean.setAttribute79value(reader.get(i++));
                swissWatchBean.setAttribute80name(reader.get(i++));
                swissWatchBean.setAttribute80value(reader.get(i++));
                swissWatchBean.setAttribute81name(reader.get(i++));
                swissWatchBean.setAttribute81value(reader.get(i++));
                swissWatchBean.setAttribute82name(reader.get(i++));
                swissWatchBean.setAttribute82value(reader.get(i++));
                swissWatchBean.setAttribute83name(reader.get(i++));
                swissWatchBean.setAttribute83value(reader.get(i++));
                swissWatchBean.setAttribute84name(reader.get(i++));
                swissWatchBean.setAttribute84value(reader.get(i++));
                swissWatchBean.setAttribute85name(reader.get(i++));
                swissWatchBean.setAttribute85value(reader.get(i++));
                swissWatchBean.setAttribute86name(reader.get(i++));
                swissWatchBean.setAttribute86value(reader.get(i++));
                swissWatchBean.setAttribute87name(reader.get(i++));
                swissWatchBean.setAttribute87value(reader.get(i++));
                swissWatchBean.setAttribute88name(reader.get(i++));
                swissWatchBean.setAttribute88value(reader.get(i++));
                swissWatchBean.setAttribute89name(reader.get(i++));
                swissWatchBean.setAttribute89value(reader.get(i++));
                swissWatchBean.setAttribute90name(reader.get(i++));
                swissWatchBean.setAttribute90value(reader.get(i++));
                swissWatchBean.setAttribute91name(reader.get(i++));
                swissWatchBean.setAttribute91value(reader.get(i++));
                swissWatchBean.setAttribute92name(reader.get(i++));
                swissWatchBean.setAttribute92value(reader.get(i++));
                swissWatchBean.setAttribute93name(reader.get(i++));
                swissWatchBean.setAttribute93value(reader.get(i++));
                swissWatchBean.setAttribute94name(reader.get(i++));
                swissWatchBean.setAttribute94value(reader.get(i++));
                swissWatchBean.setAttribute95name(reader.get(i++));
                swissWatchBean.setAttribute95value(reader.get(i++));
                swissWatchBean.setAttribute96name(reader.get(i++));
                swissWatchBean.setAttribute96value(reader.get(i++));
                swissWatchBean.setAttribute97name(reader.get(i++));
                swissWatchBean.setAttribute97value(reader.get(i++));
                swissWatchBean.setAttribute98name(reader.get(i++));
                swissWatchBean.setAttribute98value(reader.get(i++));
                swissWatchBean.setAttribute99name(reader.get(i++));
                swissWatchBean.setAttribute99value(reader.get(i++));
                swissWatchBean.setAttribute100name(reader.get(i++));
                swissWatchBean.setAttribute100value(reader.get(i++));
                swissWatchBean.setAttribute101name(reader.get(i++));
                swissWatchBean.setAttribute101value(reader.get(i++));
                swissWatchBean.setAttribute102name(reader.get(i++));
                swissWatchBean.setAttribute102value(reader.get(i++));
                swissWatchBean.setAttribute103name(reader.get(i++));
                swissWatchBean.setAttribute103value(reader.get(i++));
                swissWatchBean.setAttribute104name(reader.get(i++));
                swissWatchBean.setAttribute104value(reader.get(i++));
                swissWatchBean.setAttribute105name(reader.get(i++));
                swissWatchBean.setAttribute105value(reader.get(i++));
                swissWatchBean.setAttribute106name(reader.get(i++));
                swissWatchBean.setAttribute106value(reader.get(i++));
                swissWatchBean.setAttribute107name(reader.get(i++));
                swissWatchBean.setAttribute107value(reader.get(i++));
                swissWatchBean.setAttribute108name(reader.get(i++));
                swissWatchBean.setAttribute108value(reader.get(i++));
                swissWatchBean.setAttribute109name(reader.get(i++));
                swissWatchBean.setAttribute109value(reader.get(i++));
                swissWatchBean.setAttribute110name(reader.get(i++));
                swissWatchBean.setAttribute110value(reader.get(i++));
                swissWatchBean.setAttribute111name(reader.get(i++));
                swissWatchBean.setAttribute111value(reader.get(i++));
                swissWatchBean.setAttribute112name(reader.get(i++));
                swissWatchBean.setAttribute112value(reader.get(i++));
                swissWatchBean.setAttribute113name(reader.get(i++));
                swissWatchBean.setAttribute113value(reader.get(i++));
                swissWatchBean.setAttribute114name(reader.get(i++));
                swissWatchBean.setAttribute114value(reader.get(i++));
                swissWatchBean.setAttribute115name(reader.get(i++));
                swissWatchBean.setAttribute115value(reader.get(i++));
                swissWatchBean.setAttribute116name(reader.get(i++));
                swissWatchBean.setAttribute116value(reader.get(i++));
                swissWatchBean.setAttribute117name(reader.get(i++));
                swissWatchBean.setAttribute117value(reader.get(i++));
                swissWatchBean.setAttribute118name(reader.get(i++));
                swissWatchBean.setAttribute118value(reader.get(i++));
                swissWatchBean.setAttribute119name(reader.get(i++));
                swissWatchBean.setAttribute119value(reader.get(i++));
                swissWatchBean.setAttribute120name(reader.get(i++));
                swissWatchBean.setAttribute120value(reader.get(i++));
                swissWatchBean.setAttribute121name(reader.get(i++));
                swissWatchBean.setAttribute121value(reader.get(i++));
                swissWatchBean.setAttribute122name(reader.get(i++));
                swissWatchBean.setAttribute122value(reader.get(i++));
                swissWatchBean.setAttribute123name(reader.get(i++));
                swissWatchBean.setAttribute123value(reader.get(i++));
                swissWatchBean.setAttribute124name(reader.get(i++));
                swissWatchBean.setAttribute124value(reader.get(i++));
                swissWatchBean.setAttribute125name(reader.get(i++));
                swissWatchBean.setAttribute125value(reader.get(i++));
                swissWatchBean.setAttribute126name(reader.get(i++));
                swissWatchBean.setAttribute126value(reader.get(i++));
                swissWatchBean.setAttribute127name(reader.get(i++));
                swissWatchBean.setAttribute127value(reader.get(i++));
                swissWatchBean.setAttribute128name(reader.get(i++));
                swissWatchBean.setAttribute128value(reader.get(i++));
                swissWatchBean.setAttribute129name(reader.get(i++));
                swissWatchBean.setAttribute129value(reader.get(i++));
                swissWatchBean.setAttribute130name(reader.get(i++));
                swissWatchBean.setAttribute130value(reader.get(i++));
                swissWatchBean.setAttribute131name(reader.get(i++));
                swissWatchBean.setAttribute131value(reader.get(i++));
                swissWatchBean.setAttribute132name(reader.get(i++));
                swissWatchBean.setAttribute132value(reader.get(i++));
                swissWatchBean.setAttribute133name(reader.get(i++));
                swissWatchBean.setAttribute133value(reader.get(i++));
                swissWatchBean.setAttribute134name(reader.get(i++));
                swissWatchBean.setAttribute134value(reader.get(i++));
                swissWatchBean.setAttribute135name(reader.get(i++));
                swissWatchBean.setAttribute135value(reader.get(i++));
                swissWatchBean.setAttribute136name(reader.get(i++));
                swissWatchBean.setAttribute136value(reader.get(i++));
                swissWatchBean.setAttribute137name(reader.get(i++));
                swissWatchBean.setAttribute137value(reader.get(i++));
                swissWatchBean.setAttribute138name(reader.get(i++));
                swissWatchBean.setAttribute138value(reader.get(i++));
                swissWatchBean.setAttribute139name(reader.get(i++));
                swissWatchBean.setAttribute139value(reader.get(i++));
                swissWatchBean.setAttribute140name(reader.get(i++));
                swissWatchBean.setAttribute140value(reader.get(i++));
                swissWatchBean.setAttribute141name(reader.get(i++));
                swissWatchBean.setAttribute141value(reader.get(i++));
                swissWatchBean.setAttribute142name(reader.get(i++));
                swissWatchBean.setAttribute142value(reader.get(i++));
                swissWatchBean.setAttribute143name(reader.get(i++));
                swissWatchBean.setAttribute143value(reader.get(i++));
                swissWatchBean.setAttribute144name(reader.get(i++));
                swissWatchBean.setAttribute144value(reader.get(i++));
                swissWatchBean.setAttribute145name(reader.get(i++));
                swissWatchBean.setAttribute145value(reader.get(i++));
                swissWatchBean.setAttribute146name(reader.get(i++));
                swissWatchBean.setAttribute146value(reader.get(i++));
                swissWatchBean.setAttribute147name(reader.get(i++));
                swissWatchBean.setAttribute147value(reader.get(i++));
                swissWatchBean.setAttribute148name(reader.get(i++));
                swissWatchBean.setAttribute148value(reader.get(i++));
                swissWatchBean.setAttribute150name(reader.get(i++));
                swissWatchBean.setAttribute150value(reader.get(i++));
                swissWatchBean.setAttribute151name(reader.get(i++));
                swissWatchBean.setAttribute151value(reader.get(i++));
                swissWatchBean.setAttribute152name(reader.get(i++));
                swissWatchBean.setAttribute152value(reader.get(i++));
                swissWatchBean.setAttribute153name(reader.get(i++));
                swissWatchBean.setAttribute153value(reader.get(i++));
                swissWatchBean.setAttribute154name(reader.get(i++));
                swissWatchBean.setAttribute154value(reader.get(i++));
                swissWatchBean.setAttribute155name(reader.get(i++));
                swissWatchBean.setAttribute155value(reader.get(i++));
                swissWatchBean.setAttribute156name(reader.get(i++));
                swissWatchBean.setAttribute156value(reader.get(i++));
                swissWatchBean.setAttribute157name(reader.get(i++));
                swissWatchBean.setAttribute157value(reader.get(i++));
                swissWatchBean.setAttribute158name(reader.get(i++));
                swissWatchBean.setAttribute158value(reader.get(i++));
                swissWatchBean.setAttribute159name(reader.get(i++));
                swissWatchBean.setAttribute159value(reader.get(i++));
                swissWatchBean.setAttribute160name(reader.get(i++));
                swissWatchBean.setAttribute160value(reader.get(i++));
                swissWatchBean.setAttribute161name(reader.get(i++));
                swissWatchBean.setAttribute161value(reader.get(i++));
                swissWatchBean.setAttribute162name(reader.get(i++));
                swissWatchBean.setAttribute162value(reader.get(i++));
                swissWatchBean.setAttribute163name(reader.get(i++));
                swissWatchBean.setAttribute163value(reader.get(i++));
                swissWatchBean.setAttribute164name(reader.get(i++));
                swissWatchBean.setAttribute164value(reader.get(i++));
                swissWatchBean.setAttribute165name(reader.get(i++));
                swissWatchBean.setAttribute165value(reader.get(i++));
                swissWatchBean.setAttribute166name(reader.get(i++));
                swissWatchBean.setAttribute166value(reader.get(i++));
                swissWatchBean.setAttribute167name(reader.get(i++));
                swissWatchBean.setAttribute167value(reader.get(i++));
                swissWatchBean.setAttribute168name(reader.get(i++));
                swissWatchBean.setAttribute168value(reader.get(i++));
                swissWatchBean.setAttribute169name(reader.get(i++));
                swissWatchBean.setAttribute169value(reader.get(i++));
                swissWatchBean.setAttribute170name(reader.get(i++));
                swissWatchBean.setAttribute170value(reader.get(i++));
                superfeed.add(swissWatchBean);

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
