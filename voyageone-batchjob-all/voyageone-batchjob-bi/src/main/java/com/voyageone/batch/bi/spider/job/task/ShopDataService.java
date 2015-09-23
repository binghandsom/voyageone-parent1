package com.voyageone.batch.bi.spider.job.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.DealDataService;
import com.voyageone.batch.core.modelbean.TaskControlBean;

@Service
public class ShopDataService extends BaseSpiderService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DealDataService dealDataService;
	
	@Override
	public String getTaskName() {
		return "ShopDataService";
	}

	@Override
	protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
		List<FormUser> listUser = userMapper.select_list_vm_shop_user(Constants.ENABLE);

        for (FormUser user : listUser) {
        	logger.info("ShopDataService shop:="+user.getShop_id() + " start");
            dealDataService.runShopDataProcess(user);
            logger.info("ShopDataService shop:="+user.getShop_id() + " end");
//        	if (user.getShop_id() == 10 ) {
//                Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);
//    			dealDataService.initialShopDataProcess(user);
//        	}
        }
	}

}
