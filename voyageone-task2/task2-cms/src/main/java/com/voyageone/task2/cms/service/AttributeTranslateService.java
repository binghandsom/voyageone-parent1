package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.AttributeDao;
import com.voyageone.common.util.baidu.translate.BaiduTranslateUtil;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by jacky on 2015/10/15.
 */
@Service
public class AttributeTranslateService extends BaseTaskService {
    // 百度翻译设置
    private static final String BAIDU_TRANSLATE_CONFIG = "BAIDU_TRANSLATE_CONFIG";

    @Autowired
    private AttributeDao attributeDao;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "AttributeTranslateJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        $info("AttributeTranslateService onStartup start...");
        $info("AttributeTranslateService onStartup end...");
//        // 获得要翻译的数据
//        List<Map<String, String>> attributeValueList = getAttributeValue();
//
//        ExecutorService es = null;
//        List<Future<String>> resultList = new ArrayList<>();
//
//        if (attributeValueList != null && attributeValueList.size() > 0) {
//
//            // 拆分多线程翻译处理
//            int count = 100;
//            int totalSize = attributeValueList.size();
//            int threadCount;
//            int subSize;
//            if (totalSize > count) {
//                if (totalSize % count != 0) {
//                    threadCount = totalSize / count + 1;
//                } else {
//                    threadCount = totalSize / count;
//                }
//                subSize = totalSize / threadCount;
//
//            } else {
//                subSize = totalSize;
//                threadCount = 1;
//            }
//
//            es = Executors.newFixedThreadPool(threadCount);
//            for (int i = 0; i < threadCount; i++) {
//                int startIndex = i * subSize;
//
//                int endIndex = startIndex + subSize;
//
//                if (i == threadCount - 1) {
//                    endIndex = totalSize;
//                }
//                List<Map<String, String>> subAttributeValueList = attributeValueList.subList(startIndex, endIndex);
//
//                Future<String> future = es.submit(new BaiduTranslateTask(subAttributeValueList, i + 1));
//                resultList.add(future);
//            }
//
//            for (Future<String> fs : resultList) {
//                try {
//                    //打印各个线程（任务）执行的结果
//                    $info(fs.get());
//                } finally {
//                    if (es != null) {
//                        es.shutdown();
//                    }
//                }
//            }
//
//        } else {
//            $info("没有要翻译的属性值记录");
//        }
    }


}
