package com.voyageone.batch.bi.spider.service.channel.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.configs.DriverConfigs;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.constants.data.DataApiConstants;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.service.base.CommonSettingService;
import com.voyageone.batch.bi.spider.service.base.ProductViewService;
import com.voyageone.batch.bi.spider.service.base.StoreViewService;
import com.voyageone.batch.bi.spider.service.channel.DealTBdataService;
import com.voyageone.batch.bi.spider.service.driver.PhantomjsDriverService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.bi.util.UtilResolveData;
import com.voyageone.common.components.tmall.TbSaleService;
import com.voyageone.common.util.DateTimeUtil;

/**
 * Created by Kylin on 2015/6/10.
 */
@Service
public class DealTBdataServiceImpl implements DealTBdataService {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CommonSettingService commonSettingService;
    @Autowired
    ProductViewService productViewService;
    @Autowired
    StoreViewService storeViewService;
    @Autowired
    TbSaleService tbSaleService;
    @Autowired
    PhantomjsDriverService phantomjsDriverService;

    @Override
    public void doTBstoreDataProcess(WebDriver driver, int icircle) throws Exception {
        String strJson;
        String strDataURL;
        //Web取得的项目List
        List<String> listColumns;
        //Web取得项目所对应的Value List
        List<String> listValues = null;
        try {

            //检查店铺抽取数据用的用户和密码
            FormUser formUser = UtilCheckData.getLocalUser();
            //进程休眠10秒
            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            //获取Web取得的项目List
            listColumns = getColumnsTB(driver, formUser, DataSearchConstants.TYPE_STORE_SHOW_TM);
            if (listColumns != null) {
                //初始化，依次前滚80周
                for (int i = 0; i < icircle; i++) {
                    //进程休眠10秒
                    Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
                    //获取天猫的Json数据（初始化状态下7天一取一前滚）——店铺级
                    strDataURL = UtilResolveData.getDataURL(Constants.FORMAT_IID, DataSearchConstants.TYPE_STORE_SHOW_TM, DateTimeUtil.addDays(DateTimeUtil.getDate(), -7 * i));
                    logger.debug("doTBstoreDataProcess url:=" + strDataURL);
                    strJson = getJson(driver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                    logger.debug("doTBstoreDataProcess strJson:=" + strJson);
                    if (!"".equals(strJson)) {
                        //解析Json，获取Web取得项目所对应的Value List——天猫
                        listValues = UtilResolveData.getInfoTM(strJson);
                        if (listValues != null) {
                            //导入更新Store数据
                        	 logger.info("doTBstoreDataProcess duplicateStoreInfo listValues.size:=" + listValues.size());
                            storeViewService.duplicateStoreInfo(listColumns, listValues);
                        }
                    } else {
                        logger.error("shop:" + formUser.getShop_id() + " Reason:strJson is null 无法更新数据。");
                    }
                }
            } else {
            	logger.error("shop:" + formUser.getShop_id() + " Reason: listColumns is null 无法更新数据。");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (driver != null) {
                driver.quit();
                //进程休眠10秒
                Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
            }
        }
    }

    @Override
    public void doTBproductDataProcess(WebDriver driver, int icircle) throws Exception {
        String strJson;
        String strDataURL;
        //Web取得的项目List
        List<String> listColumns;
        //Web取得项目所对应的Value List
        List<String> listValues;
        int iCountProductIid = 0;
        //检查店铺抽取数据用的用户和密码
        FormUser formUser = UtilCheckData.getLocalUser();

        if (icircle == Constants.DAILY_WEEK_SIZE) {
            //获得商品总数
            iCountProductIid = commonSettingService.getCountViewProductIid();
        } else {
            //获得商品总数
            iCountProductIid = commonSettingService.getCountProductIid();
        }
        //进程休眠0.2秒
        //Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
        //获取Web取得的项目List
        listColumns = getColumnsTB(driver, formUser, DataSearchConstants.TYPE_PRODUCT_SHOW_TM);
        if (driver != null) {
            driver.quit();
        }
        if (listColumns != null) {
            //初始化，依次前滚80周
            for (int i = 0; i < icircle; i++) {
                WebDriver webDriver = null;
                for (int pageIndex = 0; pageIndex <= iCountProductIid / Constants.PAGE_SIZE; pageIndex++) {
                	if (webDriver != null) {
                		webDriver.quit();
                		webDriver = null;
                	}
                    try {
                        webDriver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
                        //获得商品Iid一览（大于3000拆分，未满3000直接取）
                        List<String> listIid = null;
                        if (icircle == Constants.DAILY_WEEK_SIZE) {
                            listIid = commonSettingService.getViewProductIidList(pageIndex * Constants.PAGE_SIZE, Constants.PAGE_SIZE);
                        } else {
                            //获得商品Iid一览（大于40拆分，未满40直接取）
                            listIid = commonSettingService.getProductIidList(pageIndex * Constants.PAGE_SIZE, Constants.PAGE_SIZE);
                        }
                        //遍历所获取段的商品IID
                        for (String iid : listIid) {
                            //进程休眠1秒
                            Thread.sleep(200);
                            //获取天猫的Json数据（初始化状态下7天一取一前滚）——商品级
                            strDataURL = UtilResolveData.getDataURL(iid, DataSearchConstants.TYPE_PRODUCT_SHOW_TM, DateTimeUtil.addDays(DateTimeUtil.getDate(), -7 * i));
                            strJson = getJson(webDriver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                            if (!strJson.equals("")) {
                            	logger.info("doTBproductDataProcess shop:=" + formUser.getShop_id()+ "iid:=" + iid + " OK");
                                //解析Json，获取Web取得项目所对应的Value List——天猫
                                listValues = UtilResolveData.getInfoTM(strJson);
                                if (listValues != null) {
                                    //导入更新Product数据
                                    productViewService.duplicateProductInfo(listColumns, iid, listValues);
                                }
                            } else {
                            	logger.info("doTBproductDataProcess shop:=" + formUser.getShop_id()+ "iid:=" + iid + " NULL");
                            	Thread.sleep(DataSearchConstants.THREAD_SLEEP);
                            	if (webDriver != null) {
                            		webDriver.quit();
                            	}
                            	webDriver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
                            }
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    } finally {
                        if (webDriver != null) {
                            webDriver.quit();
                            //进程休眠10秒
                            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
                        }
                    }
                }
            }
        } else {
        	logger.error("shop:" + formUser.getShop_id() + " Reason:listColumns is null 无法更新数据。");
        }
    }

    private List<String> getColumnsTB(WebDriver driver, FormUser formUser, int iShowType) throws Exception {

        //Web取得的项目List
        List<String> listKey;
        String strJson = "";
        String strDataURL;

        switch (iShowType) {
            case DataSearchConstants.TYPE_STORE_SHOW_TM:
                //获取天猫的Json数据（初始化状态下7天一取一前滚）——店铺级
                strDataURL = UtilResolveData.getDataURL(Constants.FORMAT_IID, iShowType, DateTimeUtil.getDate());
                strJson = getJson(driver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                if (!strJson.equals("")) {
                    //解析Json，获取Web取得项目 List——天猫
                    listKey = UtilResolveData.getKeysTM(strJson);

                    if (listKey != null) {
                        //获取Web取得项目所对应的数据库Table项目List
                        return commonSettingService.getShopColumnList(DataSearchConstants.COLUMN_CONDITION_SHOP, listKey);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }

            case DataSearchConstants.TYPE_PRODUCT_SHOW_TM:
                //获取天猫的Json数据（初始化状态下7天一取一前滚）——店铺级
                strDataURL = UtilResolveData.getDataURL(commonSettingService.getProductIid(), iShowType, DateTimeUtil.getDate());
                strJson = getJson(driver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                if (!strJson.equals("")) {
                    //解析Json，获取Web取得项目 List——天猫
                    listKey = UtilResolveData.getKeysTM(strJson);
                    if (listKey != null) {
                        //获取Web取得项目所对应的数据库Table项目List
                        return commonSettingService.getProductColumnList(DataSearchConstants.COLUMN_CONDITION_PRODUCT, listKey);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }

            default:
                return null;
        }
    }

    @Override
    public void doTBproductOnsaleReview() throws Exception {

        try {
            //检查店铺抽取数据用的用户和密码
            FormUser formUser = UtilCheckData.getLocalUser();
            String strEcommCode = commonSettingService.getEcommCode(formUser.getEcomm_id());
            String strChannelCode = commonSettingService.getChannelCode(formUser.getChannel_id());

            for (int i = 0; i < Constants.PAGE_SIZE_ONSALE_API; i++) {
                //List<Item> listItem = tbSaleService.getOnsaleProduct("001", "23", (long) i + 1, DataApiConstants.ONSALE_FILE_LIST_TM);
                List<Item> listItem = tbSaleService.getOnsaleProduct(strChannelCode, strEcommCode, (long) i + 1, DataApiConstants.ONSALE_FILE_LIST_TM);
                if (listItem == null) {
                    break;
                } else if (listItem.size() < 1) {
                    break;
                } else if (listItem.size() < 200) {
                    productViewService.duplicateViewProductLifeInfoTM(listItem);
                    break;
                } else {
                    productViewService.duplicateViewProductLifeInfoTM(listItem);
                }
            }
            
            String[] bannerArr = new String[]{"for_shelved", "sold_out", "violation_off_shelf"};
            for (String banner:bannerArr) {
            	for (int i = 0; i < Constants.PAGE_SIZE_ONSALE_API; i++) {
                    //List<Item> listItem = tbSaleService.getOnsaleProduct("001", "23", (long) i + 1, DataApiConstants.ONSALE_FILE_LIST_TM);
                    List<Item> listItem = tbSaleService.getInventoryProduct(strChannelCode, strEcommCode, (long) i + 1, DataApiConstants.ONSALE_FILE_LIST_TM, banner);
                    if (listItem == null) {
                    	break;
                    } else if (listItem.size() < 1) {
                        break;
                    } else if (listItem.size() < 200) {
                        productViewService.duplicateViewProductLifeInfoTM(listItem);
                        break;
                    } else {
                        productViewService.duplicateViewProductLifeInfoTM(listItem);
                    }
                }
            }
        } catch (ApiException ae) {
            logger.error(ae.getMessage(), ae);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    private String getJson(WebDriver driver, DriverConfigBean driverConfigBean, String strDataURL) throws InterruptedException {
        String strJsonCheck = "";
        int index = 0;
        while (strJsonCheck.equals("")) {
            try {
                //运行查询URL
                driver.get(strDataURL);
                //获得返回数据
                strJsonCheck = driver.getPageSource();
                strJsonCheck = UtilResolveData.replaceJson(driverConfigBean.getShopBean().getEcomm_id(), strJsonCheck);

                if (UtilResolveData.checkJson(strJsonCheck)) {
                    return strJsonCheck;
                } else {
                    strJsonCheck = "";
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                strJsonCheck = "";
            }

            index++;
            if (index > 3) {
                break;
            }
            Thread.sleep(DataSearchConstants.THREAD_SLEEP);
        }
        Thread.sleep(DataSearchConstants.THREAD_SLEEP);
        return strJsonCheck;
    }
}
