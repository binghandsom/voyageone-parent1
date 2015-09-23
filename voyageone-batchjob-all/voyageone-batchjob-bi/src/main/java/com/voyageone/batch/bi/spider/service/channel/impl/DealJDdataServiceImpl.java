package com.voyageone.batch.bi.spider.service.channel.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.voyageone.batch.bi.bean.configsbean.DriverConfigBean;
import com.voyageone.batch.bi.bean.formbean.FormUser;
import com.voyageone.batch.bi.configs.DriverConfigs;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.bi.spider.constants.data.DataApiConstants;
import com.voyageone.batch.bi.spider.constants.data.DataSearchConstants;
import com.voyageone.batch.bi.spider.service.base.CommonSettingService;
import com.voyageone.batch.bi.spider.service.base.ProductViewService;
import com.voyageone.batch.bi.spider.service.base.StoreViewService;
import com.voyageone.batch.bi.spider.service.channel.DealJDdataService;
import com.voyageone.batch.bi.spider.service.driver.PhantomjsDriverService;
import com.voyageone.batch.bi.util.UtilCheckData;
import com.voyageone.batch.bi.util.UtilResolveData;
import com.voyageone.common.components.jd.JdSaleService;
import com.voyageone.common.util.DateTimeUtil;

/**
 * Created by Kylin on 2015/6/10.
 */
@Service
public class DealJDdataServiceImpl implements DealJDdataService {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    CommonSettingService commonSettingService;
    @Autowired
    ProductViewService productViewService;
    @Autowired
    StoreViewService storeViewService;
    @Autowired
    JdSaleService jdSaleService;
    @Autowired
    PhantomjsDriverService phantomjsDriverService;


    @Override
    public void doJDstoreDataProcess(WebDriver driver, int icircle) throws Exception {
        String strJson;
        String strDataURL;
        //Web取得项目所对应的数据库Table项目情况整理Map（KEY = cor_column_table_name， VALUE = column_web_type + column_web_name）
        Map<String, String> mapConditions;

        try {
            //检查店铺抽取数据用的用户和密码
            FormUser formUser = UtilCheckData.getLocalUser();
            //获得Web取得项目所对应的数据库Table项目情况一览（cor_column_table_name，column_web_type，column_web_name）
            mapConditions = getConditionsJD(DataSearchConstants.TYPE_STORE_SHOW_JD);
            //初始化，依次前滚560天
            for (int i = 0; i < icircle; i++) {

                int iYear = DateTimeUtil.getDateYear(DateTimeUtil.addMonths(DateTimeUtil.addDays(DateTimeUtil.getDate(), -1), -1 * i));
                if (iYear < DateTimeUtil.getCurrentYear()) {
                    break;
                } else {
                    //进程休眠10秒
                    Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
                    //获取Json数据(京东，京东国际)。
                    strDataURL = UtilResolveData.getDataURL(Constants.FORMAT_IID, DataSearchConstants.TYPE_STORE_SHOW_JD, DateTimeUtil.addMonths(DateTimeUtil.addDays(DateTimeUtil.getDate(), -1), -1 * i));
                    logger.debug("doJDstoreDataProcess url:=" + strDataURL);
                    strJson = getJson(driver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                    logger.debug("doJDstoreDataProcess strJson:=" + strJson);
                    Map<String, List<String>> mapDuplaceResult = getColumnsValues(mapConditions, DataSearchConstants.TYPE_STORE_SHOW_JD, strJson);
                    logger.info("duplicateStoreInfo mapDuplaceResult.size:=" + mapDuplaceResult.size());
                    //更新数据
                    storeViewService.duplicateStoreInfo(mapDuplaceResult.get(DataSearchConstants.JD_COLUMN), mapDuplaceResult.get(DataSearchConstants.JD_VALUE));
                }
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
    public void doJDproductDataProcess(int icircle) throws Exception {
        String strJson = "";
        String strDataURL = "";
        //Web取得项目所对应的数据库Table项目情况整理Map（KEY = cor_column_table_name， VALUE = column_web_type + column_web_name）
        Map<String, String> mapConditions;
        //Web取得项目所对应的数据库Table项目情况整理Map（KEY = cor_column_table_name， VALUE = column_web_type + column_web_name）
        Map<String, String> mapConditionsPC = new HashMap<String, String>();
        //Web取得项目所对应的数据库Table项目情况整理Map（KEY = cor_column_table_name， VALUE = column_web_type + column_web_name）
        Map<String, String> mapConditionsMobile = new HashMap<String, String>();
        //获得商品总数
        int iCountProductIid = 0;

        mapConditions = getConditionsJD(DataSearchConstants.TYPE_PRODUCT_SHOW_JD);
        mapConditionsPC = getConditionsJD(DataSearchConstants.TYPE_PRODUCT_SHOW_JD_PC);
        mapConditionsMobile = getConditionsJD(DataSearchConstants.TYPE_PRODUCT_SHOW_JD_MOBILE);

        if (icircle == Constants.DAILY_DAY_SIZE) {
            //获得商品总数
            iCountProductIid = commonSettingService.getCountViewProductIid();
        } else {
            //获得商品总数
            iCountProductIid = commonSettingService.getCountProductIid();
        }
        //检查店铺抽取数据用的用户和密码
        FormUser formUser = UtilCheckData.getLocalUser();

        //初始化，依次前滚560天
        for (int i = 0; i < icircle; i++) {
            for (int pageIndex = 0; pageIndex <= iCountProductIid / Constants.PAGE_SIZE; pageIndex++) {
                WebDriver driver = null;
                try {
                    driver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
                    //获得商品Iid一览（大于3000拆分，未满3000直接取）
                    List<String> listIid = null;
                    if (icircle == Constants.DAILY_DAY_SIZE) {
                        listIid = commonSettingService.getViewProductIidList(pageIndex * Constants.PAGE_SIZE, Constants.PAGE_SIZE);
                    } else {
                        //获得商品Iid一览（大于40拆分，未满40直接取）
                        listIid = commonSettingService.getProductIidList(pageIndex * Constants.PAGE_SIZE, Constants.PAGE_SIZE);
                    }
                    //遍历所获取段的商品IID
                    for (String iid : listIid) {
                        //进程休眠1秒
                        Thread.sleep(DataSearchConstants.THREAD_SLEEP);
                        //获取Json数据(京东，京东国际)。
                        strDataURL = UtilResolveData.getDataURL(iid, DataSearchConstants.TYPE_PRODUCT_SHOW_JD, DateTimeUtil.addDays(DateTimeUtil.getDate(), -1 + -1 * i));
                        strJson = getJson(driver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                        if (!"".equals(strJson)) {
                        	logger.info("doJDproductDataProcess shop:=" + formUser.getShop_id()+ "iid:=" + iid + " OK");
                            //解析Json，获得Column和Value
                            Map<String, List<String>> mapDuplaceResult = getColumnsValues(mapConditions, DataSearchConstants.TYPE_PRODUCT_SHOW_JD, strJson);
                            if (mapDuplaceResult != null) {
                                //更新数据
                                productViewService.duplicateProductInfoJDTotal(mapDuplaceResult.get(DataSearchConstants.JD_COLUMN), iid, mapDuplaceResult.get(DataSearchConstants.JD_VALUE));
                            }
                            
                          //进程休眠0.2秒
                            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
                            //获取Json数据(京东，京东国际)。
                            strDataURL = UtilResolveData.getDataURL(iid, DataSearchConstants.TYPE_PRODUCT_SHOW_JD_PC, DateTimeUtil.addDays(DateTimeUtil.getDate(), -1 + -1 * i));
                            strJson = getJson(driver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                            if (!"".equals(strJson)) {
                                //解析Json，获得Column和Value
                                Map<String, List<String>> mapDuplaceResultPC = getColumnsValues(mapConditionsPC, DataSearchConstants.TYPE_PRODUCT_SHOW_JD_PC, strJson);
                                if (mapDuplaceResultPC != null) {
                                    //更新数据
                                    productViewService.duplicateProductInfoJDpc(mapDuplaceResultPC.get(DataSearchConstants.JD_COLUMN), iid, mapDuplaceResultPC.get(DataSearchConstants.JD_VALUE));
                                }
                            }
                            //进程休眠0.2秒
                            Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
                            //获取Json数据(京东，京东国际)。
                            strDataURL = UtilResolveData.getDataURL(iid, DataSearchConstants.TYPE_PRODUCT_SHOW_JD_MOBILE, DateTimeUtil.addDays(DateTimeUtil.getDate(), -1 + -1 * i));
                            strJson = getJson(driver, DriverConfigs.getDriver(formUser.getShop_id()), strDataURL);
                            if (!"".equals(strJson)) {
                                //解析Json，获得Column和Value
                                Map<String, List<String>> mapDuplaceResultMobile = getColumnsValues(mapConditionsMobile, DataSearchConstants.TYPE_PRODUCT_SHOW_JD_MOBILE, strJson);
                                if (mapDuplaceResultMobile != null) {
                                    //更新数据
                                    productViewService.duplicateProductInfoJDmobile(mapDuplaceResultMobile.get(DataSearchConstants.JD_COLUMN), iid, mapDuplaceResultMobile.get(DataSearchConstants.JD_VALUE));
                                }
                            }
                        } else {
                        	logger.info("doJDproductDataProcess shop:=" + formUser.getShop_id()+ "iid:=" + iid + " NULL");
                        	Thread.sleep(DataSearchConstants.THREAD_SLEEP);
                        	if (driver != null) {
                        		driver.quit();
                        	}
                        	driver = phantomjsDriverService.openProcessDriver(DriverConfigs.getDriver(formUser.getShop_id()));
                        }
                        
                    }
                } catch (Exception e) {
                    logger.info(strDataURL);
                    logger.info(strJson);
                    logger.error(e.getMessage(), e);
                } finally {
                    if (driver != null) {
                        driver.quit();
                        //进程休眠0.2秒
                        Thread.sleep(DataSearchConstants.THREAD_SLEEP_INPUT);
                    }
                }
            }
        }
    }


    /**
     * 获得Web取得项目所对应的数据库Table项目情况一览（cor_column_table_name，column_web_type，column_web_name）
     *
     * @param iShowType
     * @return
     */
    private Map<String, String> getConditionsJD(int iShowType) {
        //Web取得项目所对应的数据库Table项目情况一览（cor_column_table_name，column_web_type，column_web_name）
        List<Map<String, String>> listConditions = null;
        //Web取得项目所对应的数据库Table项目情况整理Map（KEY = cor_column_table_name， VALUE = column_web_type + column_web_name）
        Map<String, String> mapConditions = new HashMap<String, String>();

        //获取Web取得项目所对应的数据库Table项目List——（京东）单项成列
        switch (iShowType) {
            case DataSearchConstants.TYPE_STORE_SHOW_JD:
                listConditions = commonSettingService.getColumnList(DataSearchConstants.COLUMN_CONDITION_SHOP, "");
                break;

            case DataSearchConstants.TYPE_PRODUCT_SHOW_JD:
                listConditions = commonSettingService.getColumnList(DataSearchConstants.COLUMN_CONDITION_PRODUCT, DataSearchConstants.COLUMN_TYPE_PRODUCT);
                break;

            case DataSearchConstants.TYPE_PRODUCT_SHOW_JD_PC:
                listConditions = commonSettingService.getColumnList(DataSearchConstants.COLUMN_CONDITION_PRODUCT, DataSearchConstants.COLUMN_TYPE_PRODUCT_PC);
                break;

            case DataSearchConstants.TYPE_PRODUCT_SHOW_JD_MOBILE:
                listConditions = commonSettingService.getColumnList(DataSearchConstants.COLUMN_CONDITION_PRODUCT, DataSearchConstants.COLUMN_TYPE_PRODUCT_MOBILE);
                break;
        }

        if (listConditions != null) {
            for (Map<String, String> conditions : listConditions) {
                mapConditions.put(conditions.get(DataSearchConstants.KEY_JD_TABLE_COLUMN).toString(),
                        conditions.get(DataSearchConstants.KEY_JD_WEB_TYPE).toString() + "_" + conditions.get(DataSearchConstants.KEY_JD_WEB_COLUMN).toString());
            }
            return mapConditions;
        } else {
            return null;
        }

    }

    /**
     * 解析Json，获得Column和Value
     *
     * @param mapConditions
     * @param strJson
     * @return
     */
    private Map<String, List<String>> getColumnsValues(Map<String, String> mapConditions, int iShowType, String
            strJson) {

        //Web取得的项目List
        List<String> listColumns = new ArrayList<String>();
        //Web取得项目所对应的Value List
        List<String> listValues;

        Map<String, List<String>> mapDuplaceResult = new HashMap<String, List<String>>();

        try {

            switch (iShowType) {
                case DataSearchConstants.TYPE_STORE_SHOW_JD:
                    listColumns.add(0, "shop_id");
                    listColumns.add(1, "process_date");
                    break;

                case DataSearchConstants.TYPE_PRODUCT_SHOW_JD:
                    listColumns.add(0, "shop_id");
                    listColumns.add(1, "num_iid");
                    listColumns.add(2, "process_date");
                    break;

                case DataSearchConstants.TYPE_PRODUCT_SHOW_JD_PC:
                    listColumns.add(0, "shop_id");
                    listColumns.add(1, "num_iid");
                    listColumns.add(2, "process_date");
                    break;

                case DataSearchConstants.TYPE_PRODUCT_SHOW_JD_MOBILE:
                    listColumns.add(0, "shop_id");
                    listColumns.add(1, "num_iid");
                    listColumns.add(2, "process_date");
                    break;

            }

            //初始化Web取得项目所对应的Value List
            listValues = UtilResolveData.initialListValues(strJson, iShowType);
            //遍历Web取得项目所对应的数据库Table项目情况整理Map（KEY = cor_column_table_name， VALUE = column_web_type + column_web_name）
            for (String strColumn : mapConditions.keySet()) {
                //检查设定项目是否为共同项目
                if (UtilCheckData.checkColumn(strColumn)) {

                    List<String> listValuesCheck = UtilResolveData.getInfoJD(mapConditions.get(strColumn), strJson, listValues);

                    if (listValuesCheck != null) {
                        //解析Json，获取Web取得项目所对应的Value List——京东
                        listValues = listValuesCheck;
                        //Web取得的项目List
                        listColumns.add(strColumn);
                    }
                }
            }
            //收尾Web取得项目所对应的Value List
            listValues = UtilResolveData.endListValues(listValues);
            mapDuplaceResult.put(DataSearchConstants.JD_COLUMN, listColumns);
            mapDuplaceResult.put(DataSearchConstants.JD_VALUE, listValues);
        } catch (Exception e) {
            logger.info(strJson);
            logger.error(e.getMessage(), e);
            mapDuplaceResult = null;
        } finally {
            
        }
        return mapDuplaceResult;
    }

    @Override
    public void doJDproductOnsaleReview() throws Exception {

        try {

            //检查店铺抽取数据用的用户和密码
            FormUser formUser = UtilCheckData.getLocalUser();
            String strEcommCode = commonSettingService.getEcommCode(formUser.getEcomm_id());
            String strChannelCode = commonSettingService.getChannelCode(formUser.getChannel_id());

            for (int i = 0; i < Constants.PAGE_SIZE_ONSALE_API; i++) {
                //List<Ware> onListWare = jdSaleService.getOnListProduct("001", "26", Integer.toString(i + 1), DataApiConstants.ONSALE_FILE_LIST_JD);
                List<Ware> onListWare = jdSaleService.getOnListProduct(strChannelCode, strEcommCode, Integer.toString(i + 1), DataApiConstants.ONSALE_FILE_LIST_JD);
                if (onListWare == null) {
                    break;
                } else if (onListWare.size() < 1) {
                    break;
                } else if (onListWare.size() < 100) {
                    productViewService.duplicateViewProductLifeOnListInfoJD(onListWare);
                    break;
                } else {
                    productViewService.duplicateViewProductLifeOnListInfoJD(onListWare);
                }
                productViewService.duplicateViewProductLifeOnListInfoJD(onListWare);
            }
            for (int i = 0; i < Constants.PAGE_SIZE_ONSALE_API; i++) {
                //List<Ware> deListWare = jdSaleService.getDeListProduct("001", "26", Integer.toString(i + 1), DataApiConstants.ONSALE_FILE_LIST_JD);
                List<Ware> deListWare = jdSaleService.getDeListProduct(strChannelCode, strEcommCode, Integer.toString(i + 1), DataApiConstants.ONSALE_FILE_LIST_JD);
                if (deListWare == null) {
                    break;
                } else if (deListWare.size() < 1) {
                    break;
                } else if (deListWare.size() < 100) {
                    productViewService.duplicateViewProductLifeDeListInfoJD(deListWare);
                    break;
                } else {
                    productViewService.duplicateViewProductLifeDeListInfoJD(deListWare);
                }
            }
        } catch (JdException ae) {
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
                logger.info(driver.getPageSource());
                logger.error(e.getMessage(), e);
                strJsonCheck = "";
            }

            index++;
            if (index > 3) {
                break;
            }
            Thread.sleep(DataSearchConstants.THREAD_SLEEP);
        }
        return strJsonCheck;
    }
}


