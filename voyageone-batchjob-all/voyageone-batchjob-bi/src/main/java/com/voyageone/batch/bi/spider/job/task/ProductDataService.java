package com.voyageone.batch.bi.spider.job.task;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.DealDataService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ProductDataService extends BaseSpiderService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DealDataService dealDataService;

    @Override
    public String getTaskName() {
        return "ProductDataService";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        try {
            List<FormUser> listUser = userMapper.select_list_vm_shop_user(Constants.ENABLE);

            ExecutorService executorService = Executors.newFixedThreadPool(Constants.PRODUCT_THREAD_COUNT);
            int i = 0;
            for (FormUser user : listUser) {
                i++;
                executorService.execute(new ProductDataThread(user, i));
            }

            executorService.shutdown();
            while (!executorService.isTerminated()) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.error("Product Initial execute error", e);
        }

    }

    /**
     * Created by Kylin on 2015/7/23.
     */
    class ProductDataThread implements Runnable {

        // ExtractModelVO
        private FormUser user = null;
        //private int index = 0;

        /**
         * MemFileSalesExtractThread
         *
         * @param user
         */
        public ProductDataThread(FormUser user, int index) {
            this.user = user;
            //this.index = index;
        }

        /**
         * run
         */
        public void run() {
            try {
                //Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);
                logger.info("User_name:=" + this.user.getUser_name());
                dealDataService.runProductDataProcess(this.user);
                //dealDataService.initialProductDataProcess(user);
            } catch (Exception e) {
                logger.error("ProductDataThread executeThread Error", e);
            }
        }

    }

}
