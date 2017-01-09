package com.voyageone.service.dao.vms;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author vantis
 * @version 1.0.0
 * @date 2017/1/4
 * @description 闲舟江流夕照晚 =。=
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class VmsBtShipmentDaoTest extends VOAbsLoggable {

    @Autowired
    VmsBtShipmentDao vmsBtShipmentDao;
    @Test
    public void selectList() throws Exception {
        HashMap<String, Object> param = new HashMap<>();
        param.put("channelId", "031");
        param.put("shipmentId", 15305);
        List<VmsBtShipmentModel> vmsBtShipmentModels = vmsBtShipmentDao.selectList(param);
        $info(vmsBtShipmentModels.toString());
    }

}