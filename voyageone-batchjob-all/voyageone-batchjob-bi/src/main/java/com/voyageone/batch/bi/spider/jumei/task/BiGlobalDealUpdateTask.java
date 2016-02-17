package com.voyageone.batch.bi.spider.jumei.task;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiDealBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiRecordBean;
import com.voyageone.batch.bi.bean.modelbean.jumei.JumeiSkuBean;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.job.BaseSpiderService;
import com.voyageone.batch.bi.spider.service.base.JumeiUploadDataService;
import com.voyageone.batch.bi.spider.service.driver.FireFoxDriverService;
import com.voyageone.batch.bi.spider.service.jumei.JumeiUpdateService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.core.modelbean.TaskControlBean;

@Service
public class BiGlobalDealUpdateTask extends BaseSpiderService {

    @Autowired
    private FireFoxDriverService fireFoxDriverService;
    @Autowired
    private JumeiUploadDataService jumeiUploadDataService;
    @Autowired
    private JumeiUpdateService jumeiUpdateService;


    @Override
    public String getTaskName() {
        return "FireFoxDriverInitialService";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        try {
            FormUser user = new FormUser();
            user.setUser_name("Voyageone");
            user.setUser_ps("voyage1@sh&LA");
            //需要修改：data_base
            user.setDb_name("test");
            //需要修改：channel_id
            user.setChannel_code("JW");
            if (UtilCheckData.checkUser(user)) {
                UtilCheckData.setLocalUser(user);

                DriverConfigBean driverConfigBean = new DriverConfigBean();//DriverConfigs.getDriver(user.getShop_id());
                ShopChannelEcommBean shopBean = new ShopChannelEcommBean();
                driverConfigBean.setShopBean(shopBean);
                shopBean.setEcomm_id(DataSearchConstants.ECOMM_JM);
                shopBean.setLogin_url("https://v.jumei.com/v1/api/login?app_id=e27e3ecab118&request_uri=http://a.jumeiglobal.com/&tag=9");
                shopBean.setUser_name(user.getUser_name());
                shopBean.setUser_ps(user.getUser_ps());
                shopBean.setReflash_url("http://a.jumeiglobal.com/");

                fireFoxDriverService.initialLocalLoginFireFoxDriver(driverConfigBean);
                WebDriver driver = driverConfigBean.getInitial_driver();
                //需要修改：task_id
                List<JumeiDealBean> productDealBeanLst = jumeiUploadDataService.getUploadJumeiDealList("", "JW003");
                for (JumeiDealBean deal : productDealBeanLst) {
                    //运行查询URL
                	String searchUrl = "http://a.jumeiglobal.com/GlobalDeal/List?product_name=" + deal.getProduct_code();
                    driver.get(searchUrl);
                    String strOrderLog = driver.findElement(By.className("tb-top")).getText();
                    boolean isFindProduct = true;
                    if (strOrderLog.contains("共0个商品")) {
                    	driver.findElement(By.name("Search[product_name]")).clear();
//                    	driver.findElement(By.name("Search[product_name]")).sendKeys(deal.getProduct_code());
                        driver.findElement(By.name("Search[sku_no]")).sendKeys(deal.getTitle_short());
                        List<WebElement> elementsButton = driver.findElements(By.xpath("//div/button"));
                        elementsButton.get(0).click();
                        strOrderLog = driver.findElement(By.className("tb-top")).getText();
                        if (strOrderLog.contains("共0个商品")) {
                        	logger.info("商品编号" + deal.getProduct_code() + "未上传");
                            String strTitle =deal.getTitle_long() + " " + deal.getProduct_code();
                            insertRecordLog(deal, 8, strTitle, "Deal失败：未找到对应新品");
                            isFindProduct = false;
                    	}
                    }
                        
                    if (isFindProduct) {

                        List<WebElement> tds = driver.findElements(By.xpath("//tbody/tr/td"));
                        //获得PID
                        WebElement elementPID = tds.get(20);

                        List<WebElement> a_list = elementPID.findElements(By.xpath("div/div/a"));
                        String dID = null;
                        for (WebElement aTag : a_list) {
                        	if (aTag.getAttribute("innerHTML").indexOf("编辑")>=0) {
                        		String url = aTag.getAttribute("href");
                        		Pattern p=Pattern.compile("\\d+"); 
                    			Matcher m=p.matcher(url); 
                    			while(m.find()) { 
                    				dID = m.group();
                    				break;
                    			} 
                        	}
                        	if (dID != null) {
                        		break;
                        	}
                        }
                        
                        if (dID != null) {
                            String strProductCode = deal.getProduct_code();
                            //需要修改：task_id
                            List<JumeiSkuBean> skuLst = jumeiUploadDataService.getUploadJumeiSkuList(strProductCode, "JW003");
                        	//更新DEAL
                        	jumeiUpdateService.updateDeal(driver, dID, deal, skuLst);
                        }else {
                            String strTitle =deal.getTitle_long() + " " + deal.getProduct_code();
                            insertRecordLog(deal, 9, strTitle, "Deal失败：对应新品未完成审核(pid not found)");
                        }
                    }

                }

            }
        } catch (Exception e) {
            logger.error("Driver Initial execute error", e);
        }finally {
            jumeiUploadDataService.synchronizeJumeiProductRecord();
            jumeiUploadDataService.synchronizeJumeiDealRecord();
            jumeiUploadDataService.updateJumeiRecord();
        }
    }

    private void insertRecordLog(JumeiDealBean deal, int error_type_id, String name, String message) {
    	JumeiRecordBean jumeiRecord  = new JumeiRecordBean();
        jumeiRecord.setChannel_id(deal.getChannel_id());
        jumeiRecord.setTask_id(deal.getTask_id());
        jumeiRecord.setProduct_code(deal.getProduct_code());
        jumeiRecord.setError_type_id(error_type_id);
        jumeiRecord.setJumei_product_name_cn_real(name.replace("'", "\'"));
        jumeiRecord.setError_message(message);
        jumeiUploadDataService.insertJumeiRecord(jumeiRecord);
    }
}
