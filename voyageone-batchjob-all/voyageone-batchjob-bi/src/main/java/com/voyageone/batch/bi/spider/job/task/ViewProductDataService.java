package com.voyageone.batch.bi.spider.job.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.mapper.UserMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.channel.DealJDdataService;
import com.voyageone.batch.bi.spider.service.channel.DealTBdataService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.core.modelbean.TaskControlBean;

@Service
public class ViewProductDataService extends BaseSpiderService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DealTBdataService dealTBdataService;
	@Autowired
	private DealJDdataService dealJDdataService;
	
	@Override
	public String getTaskName() {
		return "ViewProductDataService";
	}

	@Override
	protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
		List<FormUser> listUser = userMapper.select_list_vm_shop_user(Constants.ENABLE);

        for (FormUser user : listUser) {
//        	if (user.getShop_id() != 10) {
//        		continue;
//        	}
            //dealDataService.runProductDataProcess(user);
            //Thread.sleep(DataSearchConstants.THREAD_SLEEP_LOGIN);
			//获取该进程下用户
			UtilCheckData.setLocalUser(user);

			if(UtilCheckData.checkUser(user)) {
				switch (user.getEcomm_id()) {
					case DataSearchConstants.ECOMM_TM:
						dealTBdataService.doTBproductOnsaleReview();
						break;

					case DataSearchConstants.ECOMM_TB:
						dealTBdataService.doTBproductOnsaleReview();
						break;

					case DataSearchConstants.ECOMM_OF:
						break;

					case DataSearchConstants.ECOMM_TG:
						dealTBdataService.doTBproductOnsaleReview();
						break;

					case DataSearchConstants.ECOMM_JD:
						dealJDdataService.doJDproductOnsaleReview();
						break;

					case DataSearchConstants.ECOMM_CN:
						break;

					case DataSearchConstants.ECOMM_JG:
						dealJDdataService.doJDproductOnsaleReview();
						break;

				}
			}
        }
	}

}
