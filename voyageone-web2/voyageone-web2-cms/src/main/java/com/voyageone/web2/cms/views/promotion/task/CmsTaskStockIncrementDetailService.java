package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtStockSeparateIncrementItemDao;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by morse.lu on 2016/3/23.
 */
@Service
public class CmsTaskStockIncrementDetailService extends BaseAppService {

    @Autowired
    private CmsBtStockSeparateIncrementItemDao cmsBtStockSeparateIncrementItemDao;

    /**
     * 增量库存隔离数据是否移到history表
     *
     * @param taskId 任务id
     * @return 增量库存隔离数据是否移到history表
     */
    public boolean isHistoryExist(String taskId){
        return (cmsBtStockSeparateIncrementItemDao.selectStockSeparateIncrementItemHistoryCnt(new HashMap<String,Object>(){{this.put("taskId", taskId);}}) !=  0) ? true : false;
    }

    /**
     * 库存隔离Excel文档做成，数据流返回
     *
     * @param param 客户端参数
     * @return byte[] 数据流
     * @throws IOException
     * @throws InvalidFormatException
     */
    public byte[] getExcelFileStockIncrementInfo(Map param) throws IOException, InvalidFormatException {

        return new byte[]{};
    }
}
