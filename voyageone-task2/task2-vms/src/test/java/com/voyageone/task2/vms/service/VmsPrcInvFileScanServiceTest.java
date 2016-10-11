package com.voyageone.task2.vms.service;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.service.model.vms.VmsBtInventoryFileModel;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * 扫描文件测试
 * Created by vantis on 16-9-9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-vms-test.xml")
public class VmsPrcInvFileScanServiceTest extends VOAbsLoggable {

    @Autowired
    private VmsPrcInvFileScanService vmsPrcInvFileScanService;

    @Test
    public void getSubSystem() throws Exception {
        $debug(vmsPrcInvFileScanService.getSubSystem().toString());
    }

    @Test
    public void getTaskName() throws Exception {
        $debug(vmsPrcInvFileScanService.getTaskName());
    }

    @Test
    public void getImportingFile() throws Exception {
//        @Nullable VmsBtInventoryFileModel importingFile = vmsPrcInvFileScanService.getImportingFile("088");
//        $info(null == importingFile ? "null" : importingFile.toString());
    }

    @Test
    public void checkWhetherTheFTPUploadedFileExistsThenRenameItAndInsertIntoDB() throws Exception {
//        boolean result = vmsPrcInvFileScanService.checkWhetherTheFTPUploadedFileExistsThenRenameItAndInsertIntoDB("088");
//        $debug(String.valueOf(result));
    }

    @Test
    public void onStartUp() throws Exception {

    }
}