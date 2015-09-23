package com.voyageone.batch.bi.spider.service.base;

import com.jd.open.api.sdk.domain.ware.Ware;
import com.taobao.api.domain.Item;

import java.util.List;

/**
 * Created by Kylin on 2015/6/5.
 */
public interface ProductViewService {

    public void selectProductInfo();

    public void updateProductInfo(List<String> listColumns, List<String> listValues);

    public void insertProductInfo(List<String> listColumns, List<String> listValues);

    public void replaceProductInfo(List<String> listColumns, List<String> listValues);

    public int duplicateProductInfo(List<String> listColumns, String strProductIid, List<String> listValues);

    public int duplicateProductInfoJDTotal(List<String> listColumns, String strProductIid, List<String> listValues);

    public int duplicateProductInfoJDpc(List<String> listColumns, String strProductIid, List<String> listValues);

    public int duplicateProductInfoJDmobile(List<String> listColumns, String strProductIid, List<String> listValues);

    public int duplicateViewProductLifeInfoTM(List<Item> listItems);

    public int duplicateViewProductLifeOnListInfoJD(List<Ware> listWares);

    public int duplicateViewProductLifeDeListInfoJD(List<Ware> listWares);

}
