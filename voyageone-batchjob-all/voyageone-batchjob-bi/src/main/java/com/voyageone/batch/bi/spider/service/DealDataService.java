package com.voyageone.batch.bi.spider.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.configs.DriverConfigs;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.service.channel.DealJDdataService;
import com.voyageone.batch.bi.spider.service.channel.DealTBdataService;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.bi.spider.service.driver.PhantomjsDriverService;
import com.voyageone.batch.bi.util.UtilCheckData;

/**
 * Created by Kylin on 2015/6/10.
 */
@Service
public class DealDataService {
	private static final Log logger = LogFactory.getLog(DealDataService.class);

    @Autowired
    DealTBdataService dealTBdataService;

    @Autowired
    DealJDdataService dealJDdataService;

    @Autowired
    FireFoxDriverService fireFoxDriverService;
    
    @Autowired
    PhantomjsDriverService phantomjsDriverService;


    /**
     * 开始数据导入
     *
     * @param formUser
     * @throws Exception
     */
	public void runShopDataProcess(FormUser formUser) throws Exception {
		int icircle = Constants.DAILY_WEEK_SIZE;
		int ecommID = formUser.getEcomm_id();
		if (ecommID == DataSearchConstants.ECOMM_TM
				|| ecommID == DataSearchConstants.ECOMM_TB
				|| ecommID == DataSearchConstants.ECOMM_TG) {
			icircle = Constants.DAILY_WEEK_SIZE;
		} else if (ecommID == DataSearchConstants.ECOMM_JD
				|| ecommID == DataSearchConstants.ECOMM_JG) {
			
			icircle = Constants.DAILY_MONTH_SIZE;
		}
		runShopDataProcess(formUser, icircle);
	}
	
	 /**
     * 开始数据导入
     *
     * @param formUser
     * @throws Exception
     */
    public void initialShopDataProcess(FormUser formUser) throws Exception {
		int icircle = Constants.INITIAL_WEEK_SIZE;
		int ecommID = formUser.getEcomm_id();
		if (ecommID == DataSearchConstants.ECOMM_TM
				|| ecommID == DataSearchConstants.ECOMM_TB
				|| ecommID == DataSearchConstants.ECOMM_TG) {
			
			icircle = Constants.INITIAL_WEEK_SIZE;
		} else if (ecommID == DataSearchConstants.ECOMM_JD
				|| ecommID == DataSearchConstants.ECOMM_JG) {
			
			icircle = Constants.INITIAL_MONTH_SIZE;
		}
		runShopDataProcess(formUser, icircle);
    }
    
    /**
     * 开始数据导入
     *
     * @param formUser
     * @throws Exception
     */
    private void runShopDataProcess(FormUser formUser, int icircle) throws Exception {

        WebDriver driver = null;

        try {
            //获取该进程下用户
            UtilCheckData.setLocalUser(formUser);
            driver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
            switch (formUser.getEcomm_id()) {
                case DataSearchConstants.ECOMM_TM:
                    if (driver != null) {
                        dealTBdataService.doTBstoreDataProcess(driver, icircle);
                    } else {
                        break;
                    }
                    break;

                case DataSearchConstants.ECOMM_TB:
                    if (driver != null) {
                        dealTBdataService.doTBstoreDataProcess(driver, icircle);
                    } else {
                        break;
                    }
                    break;

                case DataSearchConstants.ECOMM_OF:
                    break;

                case DataSearchConstants.ECOMM_TG:
                    if(driver != null){
                        dealTBdataService.doTBstoreDataProcess(driver, icircle);
                    }else {
                        break;
                    }
                    break;

                case DataSearchConstants.ECOMM_JD:
                    if (driver != null) {
                        dealJDdataService.doJDstoreDataProcess(driver, icircle);
                    } else {
                        break;
                    }
                    break;

                case DataSearchConstants.ECOMM_CN:
                    break;

                case DataSearchConstants.ECOMM_JG:
                    if (driver != null) {
                        dealJDdataService.doJDstoreDataProcess(driver, icircle);
                    } else {
                        break;
                    }
                    break;
            }
        } catch (Exception e) {
        	logger.error("runShopDataProcess error", e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    /**
     * 日数据导入
     *
     * @param formUser
     * @throws Exception
     */
    public void runProductDataProcess(FormUser formUser) throws Exception {
    	int icircle = Constants.DAILY_WEEK_SIZE;
		int ecommID = formUser.getEcomm_id();
		if (ecommID == DataSearchConstants.ECOMM_TM
				|| ecommID == DataSearchConstants.ECOMM_TB
				|| ecommID == DataSearchConstants.ECOMM_TG) {
			
			icircle = Constants.DAILY_WEEK_SIZE;
		} else if (ecommID == DataSearchConstants.ECOMM_JD
				|| ecommID == DataSearchConstants.ECOMM_JG) {
			
			icircle = Constants.DAILY_DAY_SIZE;
		}
		runProductDataProcess(formUser, icircle);
    }
    
    /**
     * init数据导入
     *
     * @param formUser
     * @throws Exception
     */
    public void initialProductDataProcess(FormUser formUser) throws Exception {
    	int icircle = Constants.INITIAL_WEEK_SIZE;
		int ecommID = formUser.getEcomm_id();
		if (ecommID == DataSearchConstants.ECOMM_TM
				|| ecommID == DataSearchConstants.ECOMM_TB
				|| ecommID == DataSearchConstants.ECOMM_TG) {
			
			icircle = Constants.INITIAL_WEEK_SIZE;
		} else if (ecommID == DataSearchConstants.ECOMM_JD
				|| ecommID == DataSearchConstants.ECOMM_JG) {
			
			icircle = Constants.INITIAL_DAY_SIZE;
		}
		runProductDataProcess(formUser, icircle);
    }

    /**
     * 开始数据导入
     *
     * @param formUser
     * @throws Exception
     */
    private void runProductDataProcess(FormUser formUser, int icircle) throws Exception {

        WebDriver driver = null;

        try {
            //获取该进程下用户
            UtilCheckData.setLocalUser(formUser);
            switch (formUser.getEcomm_id()) {
                case DataSearchConstants.ECOMM_TM:
                    driver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
                    if (driver != null) {
                        dealTBdataService.doTBproductDataProcess(driver, icircle);
                    } else {
                        break;
                    }
                    break;

                case DataSearchConstants.ECOMM_TB:
                    driver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
                    if (driver != null) {
                        dealTBdataService.doTBproductDataProcess(driver, icircle);
                    } else {
                        break;
                    }
                    break;

                case DataSearchConstants.ECOMM_OF:
                    break;

                case DataSearchConstants.ECOMM_TG:
                    driver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
                    if (driver != null) {
                        dealTBdataService.doTBproductDataProcess(driver, icircle);
                    } else {
                        break;
                    }
                    break;

                case DataSearchConstants.ECOMM_JD:
                    FormUser user_jd = UtilCheckData.getLocalUser();
                    if (UtilCheckData.checkUser(user_jd)) {
                        dealJDdataService.doJDproductDataProcess(icircle);
                    }
                    break;

                case DataSearchConstants.ECOMM_CN:
                    break;

                case DataSearchConstants.ECOMM_JG:
                    FormUser user_jg = UtilCheckData.getLocalUser();
                    if (UtilCheckData.checkUser(user_jg)) {
                        dealJDdataService.doJDproductDataProcess(icircle);
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error("runProductDataProcess error", e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

}
