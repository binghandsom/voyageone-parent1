package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.vomessage.body.FeedExportMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Edward
 * @version 2.0.0, 2017/2/13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtOperationLogServiceTest {

    @Autowired
    CmsBtOperationLogService cmsBtOperationLogService;

    FeedExportMQMessageBody messageBody = new FeedExportMQMessageBody();
    private String jobName = "jobName";
    private String jobTitle = "jobTitle";
    private String userName = "edward";
    private String channelId = "928";
    private BusinessException exception = new BusinessException("测试异常");
    private List<CmsBtOperationLogModel_Msg> msgList = new ArrayList<>();

    public CmsBtOperationLogServiceTest() {
        messageBody.setCmsBtExportTaskId(123);
        messageBody.setSender(userName);

        CmsBtOperationLogModel_Msg msg = new CmsBtOperationLogModel_Msg();
        msg.setSkuCode("123456-001");
        msg.setMsg("上新错误");
        msgList.add(msg);
    }

    @Test
    public void testLog() throws Exception {
        cmsBtOperationLogService.log(jobName, jobTitle, messageBody, exception);
    }

    @Test
    public void testLog1() throws Exception {
        cmsBtOperationLogService.log(jobName, jobTitle, messageBody, OperationLog_Type.unknownException, exception);
    }

    @Test
    public void testLog2() throws Exception {
        cmsBtOperationLogService.log(jobName, jobTitle, OperationLog_Type.unknownException, msgList, userName, channelId);
    }

    @Test
    public void testLog3() throws Exception {
        cmsBtOperationLogService.log(jobName, jobTitle, messageBody, OperationLog_Type.unknownException,"comment", msgList);
    }

    @Test
    public void testSearchMqCmsBtOperationLogData_noPrams() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("curr", 1);
        params.put("size", 10);
        List<CmsBtOperationLogModel> result = cmsBtOperationLogService.searchMqCmsBtOperationLogData(params);
        System.out.print(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchMqCmsBtOperationLogData_byName() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", jobName);
        params.put("userName", userName);
        params.put("curr", 1);
        params.put("size", 10);
        List<CmsBtOperationLogModel> result = cmsBtOperationLogService.searchMqCmsBtOperationLogData(params);
        System.out.print(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchMqCmsBtOperationLogData_byTitle() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("title", jobTitle);
        params.put("userName", userName);
        params.put("curr", 1);
        params.put("size", 10);
        List<CmsBtOperationLogModel> result = cmsBtOperationLogService.searchMqCmsBtOperationLogData(params);
        System.out.print(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchMqCmsBtOperationLogData_byType() throws Exception {
        Map<String, Object> params = new HashMap<>();
        List<String> listType = new ArrayList<>();
        listType.add("1");
        listType.add("6");
        params.put("typeValue", listType);
        params.put("userName", userName);
        params.put("curr", 1);
        params.put("size", 10);
        List<CmsBtOperationLogModel> result = cmsBtOperationLogService.searchMqCmsBtOperationLogData(params);
        System.out.print(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchMqCmsBtOperationLogData_byUser0() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "0");
        params.put("curr", 1);
        params.put("size", 10);
        List<CmsBtOperationLogModel> result = cmsBtOperationLogService.searchMqCmsBtOperationLogData(params);
        System.out.print(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchMqCmsBtOperationLogData_byUserName() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("curr", 1);
        params.put("size", 10);
        List<CmsBtOperationLogModel> result = cmsBtOperationLogService.searchMqCmsBtOperationLogData(params);
        System.out.print(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchMqCmsBtOperationLogData_byAll() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", jobName);
        params.put("title", jobTitle);
        List<Integer> listType = new ArrayList<>();
        listType.add(6);
        params.put("typeValue", listType);
        params.put("userName", userName);
        params.put("curr", 1);
        params.put("size", 10);
        List<CmsBtOperationLogModel> result = cmsBtOperationLogService.searchMqCmsBtOperationLogData(params);
        System.out.print(JacksonUtil.bean2Json(result));
    }

    @Test
    public void testSearchMqCmsBtOperationLogDataCnt() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", jobName);
        params.put("title", jobTitle);
        List<Integer> listType = new ArrayList<>();
        listType.add(6);
        params.put("typeValue", listType);
        params.put("userName", userName);
        long result = cmsBtOperationLogService.searchMqCmsBtOperationLogDataCnt(params);
        System.out.print(result);
    }
}