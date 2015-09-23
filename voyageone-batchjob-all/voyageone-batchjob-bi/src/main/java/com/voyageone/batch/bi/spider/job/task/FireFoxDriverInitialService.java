package com.voyageone.batch.bi.spider.job.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.configs.DriverConfigs;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.core.modelbean.TaskControlBean;

@Service
public class FireFoxDriverInitialService extends BaseSpiderService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FireFoxDriverService fireFoxDriverService;

    @Override
    public String getTaskName() {
        return "FireFoxDriverInitialService";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        try {
            List<FormUser> listUser = userMapper.select_list_vm_shop_user(Constants.ENABLE);
            ExecutorService executorService = Executors.newFixedThreadPool(Constants.DRIVER_THREAD_COUNT);
            for (FormUser user : listUser) {
                if (UtilCheckData.checkUser(user)) {
                	DriverConfigBean driverConfigBean = DriverConfigs.getDriver(user.getShop_id());
                    fireFoxDriverService.initialRemoteLoginFireFoxDriver(driverConfigBean);
                    executorService.execute(new DriverThread(driverConfigBean));
                }
            }
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.error("Driver Initial execute error", e);
        }
    }

    /**
     * Created by Kylin on 2015/7/23.
     */
    class DriverThread implements Runnable {

        private DriverConfigBean driverBean = null;

        /**
         * MemFileSalesExtractThread
         *
         * @param driver
         */
        public DriverThread(DriverConfigBean driverBean) {
            this.driverBean = driverBean;
        }

        /**
         * run
         */
        public void run() {
            try {
                logger.info("ShopID:=" + driverBean.getShopBean().getShop_id()+"; Shop Code:=" + driverBean.getShopBean().getShop_code());
                fireFoxDriverService.reflashFireFoxDriver(driverBean);
            } catch (Exception e) {
                logger.error("DriverThread executeThread Error", e);
            }
        }

    }
}
