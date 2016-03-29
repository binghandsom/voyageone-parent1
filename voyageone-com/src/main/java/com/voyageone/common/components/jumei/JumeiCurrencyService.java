package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Bean.JmCurrencyBean;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.JacksonUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanyu.laing on 2016/1/25.
 */
@Service
public class JumeiCurrencyService extends JmBase {

    private List<JmCurrencyBean> currencys = null;

    private static String CURRENCY_URL = "v1/currency/query";

    /**
     * 初始化货币信息
     */
    public void initCurrencys(ShopBean shopBean) throws Exception {
        String result = reqJmApi(shopBean, CURRENCY_URL);
        List<JmCurrencyBean> currencyList = JacksonUtil.jsonToBeanList(result, JmCurrencyBean.class);
        if (currencyList != null) {
            currencys = currencyList;
        } else {
            currencys = new ArrayList<>();
        }
    }

    /**
     * 获取全部货币信息
     */
    public List<JmCurrencyBean> getCurrencys(ShopBean shopBean) throws Exception {
        if (currencys == null) {
            initCurrencys(shopBean);
        }
        return currencys;
    }

    /**
     * 根据名称查找货币信息
     */
    public JmCurrencyBean getCurrencyByName(ShopBean shopBean, String name) throws Exception {
        if (name == null) {
            return null;
        }

        if (currencys == null) {
            initCurrencys(shopBean);
        }

        for (JmCurrencyBean currency : currencys) {
            if (currency != null && currency.getArea_currency_name() != null
                    && currency.getArea_currency_name().startsWith(name)) {
                return currency;
            }
        }

        return null;
    }

}
