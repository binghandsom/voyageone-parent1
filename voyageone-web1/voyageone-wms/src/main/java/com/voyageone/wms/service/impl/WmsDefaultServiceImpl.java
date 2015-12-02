package com.voyageone.wms.service.impl;

import com.voyageone.wms.dao.TransferDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.wms.service.WmsDefaultService;

@Service
public class WmsDefaultServiceImpl implements WmsDefaultService {

    @Autowired
    private TransferDao transferDao;

    @Override
    public int getTransferCount() {

        int transferCount = transferDao.getTransferCounts();

        return transferCount;

    }

}
