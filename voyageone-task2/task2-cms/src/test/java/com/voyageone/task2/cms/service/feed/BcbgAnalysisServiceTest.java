package com.voyageone.task2.cms.service.feed;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.FeedsHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * 解析测试..
 * Created by Jonas on 10/14/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class BcbgAnalysisServiceTest {

    @Autowired
    private BcbgAnalysisService bcbgAnalysisService;

    @Before
    public void init() throws Exception {
        BcbgConstants.init();
    }

    @Test
    public void testBcbgAnalysisService() throws Exception {

        String filepaths = "/Users/Jonas/Desktop/bcbg_temp;/Users/Jonas/Desktop/bcbg_temp";
        String backupDir = "/Users/Jonas/Desktop/bcbg_temp/back/%s/%s/";

        FeedsHelper.put(ChannelConfigEnums.Channel.BCBG, FeedEnums.Name.feed_ftp_filename, filepaths);
        FeedsHelper.put(ChannelConfigEnums.Channel.BCBG, FeedEnums.Name.feed_backup_dir, backupDir);

        try {
            bcbgAnalysisService.onStartup(new ArrayList<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}