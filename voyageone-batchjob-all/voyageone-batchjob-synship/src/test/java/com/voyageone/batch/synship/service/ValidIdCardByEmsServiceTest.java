package com.voyageone.batch.synship.service;

import com.voyageone.batch.synship.dao.IdCardDao;
import com.voyageone.batch.synship.modelbean.IdCardBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.voyageone.batch.SynshipConstants.IdCardStatus.*;

/**
 * 身份证验证
 *
 * Created by Jonas on 9/24/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:syn-ship-test-spring-context.xml")
public class ValidIdCardByEmsServiceTest {

    private Log log = LogFactory.getLog(getClass());

    @Autowired
    private ValidIdCardByEmsService validIdCardByEmsService;

    @Autowired
    private IdCardDao idCardDao;

    @Test
    public void testValidOnThread() throws Exception {

        List<IdCardBean> idCardBeans = idCardDao.selectNewestByApproved(WAITING_AUTO, 1);

        validIdCardByEmsService.validOnThread(idCardBeans);
    }
}