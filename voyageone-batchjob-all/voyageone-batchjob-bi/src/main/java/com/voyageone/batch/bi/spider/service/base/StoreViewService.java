package com.voyageone.batch.bi.spider.service.base;

import java.util.List;

/**
 * Created by Kylin on 2015/6/5.
 */
public interface StoreViewService {

    public void selectStoreInfo();

    public void updateStoreInfo(List<String> listColumns, List<String> listValues);

    public void insertStoreInfo(List<String> listColumns, List<String> listValues);

    public void replaceStoreInfo(List<String> listColumns, List<String> listValues);

    public int duplicateStoreInfo(List<String> listColumns, List<String> listValues);

}
