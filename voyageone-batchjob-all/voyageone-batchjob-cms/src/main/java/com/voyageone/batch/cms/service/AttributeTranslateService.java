package com.voyageone.batch.cms.service;

import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.dao.AttributeDao;
import com.voyageone.batch.cms.utils.WebServiceUtil;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by jacky on 2015/10/15.
 */
@Service
public class AttributeTranslateService extends BaseTaskService {
    // 百度翻译设置
    private static final String BAIDU_TRANSLATE_CONFIG = "BAIDU_TRANSLATE_CONFIG";

    private List<List<Map<String, String>>> TRANSLATE_RESULT_TMP_LIST = null;

    private List<Map<String, String>> TRANSLATE_RESULT_LIST = null;

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
        // 获得要翻译的数据
        List<Map<String, String>> attributeValueList = getAttributeValue();

        ExecutorService es = null;
        List<Future<String>> resultList = new ArrayList<>();

        if (attributeValueList != null && attributeValueList.size() > 0) {
            TRANSLATE_RESULT_TMP_LIST = new ArrayList<List<Map<String, String>>>();
            TRANSLATE_RESULT_LIST = new ArrayList<Map<String, String>>();

            // 拆分多线程翻译处理
            int count = 50;
            int totalSize = attributeValueList.size();
            int threadCount;
            int subSize;
            if (totalSize > count) {
                if (totalSize % count != 0) {
                    threadCount = totalSize / count + 1;
                } else {
                    threadCount = totalSize / count;
                }
                subSize = totalSize / threadCount;

            } else {
                subSize = totalSize;
                threadCount = 1;
            }

            es = Executors.newFixedThreadPool(threadCount);
            for (int i = 0; i < threadCount; i++) {
                int startIndex = i * subSize;

                int endIndex = startIndex + subSize;

                if (i == threadCount - 1) {
                    endIndex = totalSize;
                }
                List<Map<String, String>> subAttributeValueList = attributeValueList.subList(startIndex, endIndex);

                Future<String> future = es.submit(new BaiduTranslateTask(subAttributeValueList, i + 1));
                resultList.add(future);
            }

            for (Future<String> fs : resultList) {
                try {
                    //打印各个线程（任务）执行的结果
                    logger.info(fs.get());
                } finally {
                    if (es != null) {
                        es.shutdown();
                    }
                }
            }

            for (List<Map<String, String>> threadResult : TRANSLATE_RESULT_TMP_LIST) {
                TRANSLATE_RESULT_LIST.addAll(threadResult);
            }

            // 更新需要翻译的字段
            if (TRANSLATE_RESULT_LIST.size() == totalSize) {
                updateAttributeValueCn(TRANSLATE_RESULT_LIST);
            } else {
                $info("翻译之后的记录数和带翻译数不一致，异常");
            }

        } else {
            $info("没有要翻译的属性值记录");
        }
    }

    /**
     * 获得要翻译的数据
     *
     * @return
     */
    private List<Map<String, String>> getAttributeValue() {
        List<Map<String, String>> attributeValueList = attributeDao.getAttributeValue();

        return attributeValueList;
    }

    /**
     * 更新需要翻译的字段
     *
     * @return
     */
    private boolean updateAttributeValueCn(List<Map<String, String>> resultList) {
        boolean isSuccess = false;
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            for (Map<String, String> attributeMap : resultList) {
                attributeMap.put("taskName", "AttributeTranslateJob");
                isSuccess = attributeDao.updateAttributeValueCn(attributeMap);
                if (!isSuccess) {
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

            logIssue(ex);

            isSuccess = false;

        } finally {
            if (isSuccess) {
                transactionManager.commit(status);
                $info("更新翻译后属性值updateAttributeValueCn is commit");

            } else {
                transactionManager.rollback(status);
                $info("更新翻译后属性值updateAttributeValueCn is rollback");
            }
        }

        return isSuccess;
    }

    /**
     * 翻译字符串列表
     */
    private List<String> translate(List<String> beforeStringList, int threadNo) throws Exception {
        List<String> resultStrList = new ArrayList<String>();

        StringBuilder url = new StringBuilder(Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "Url"));
        String apiKey = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "ApiKey" + threadNo);
        String from = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "From");
        String to = Codes.getCodeName(BAIDU_TRANSLATE_CONFIG, "To");

        StringBuilder q = new StringBuilder("q=");
        if (beforeStringList != null && beforeStringList.size() > 0) {
            String qSplit = URLEncoder.encode("\n", "utf-8");
            int qSize = beforeStringList.size();
            for (int i = 0; i < qSize; i++) {
                if (i == 0) {
                    q.append(URLEncoder.encode(beforeStringList.get(i), "utf-8"));
                } else {
                    q.append(qSplit);
                    q.append(URLEncoder.encode(beforeStringList.get(i), "utf-8"));
                }
            }
        } else {
            return resultStrList;
        }

        url.append("?");
        url.append("client_id=");
        url.append(apiKey);
        url.append("&");
        url.append(q.toString());
        url.append("&");
        url.append("from=");
        url.append(from);
        url.append("&");
        url.append("to=");
        url.append(to);

        $info(url.toString());

        String transResult = WebServiceUtil.getByUrl(url.toString());

        Map<String, Object> jsonToMap = JsonUtil.jsonToMap(transResult);
        // 百度翻译API服务发生错误
        if (jsonToMap.containsKey("error_code")) {
            Object error_code = jsonToMap.get("error_code");
            Object error_msg = jsonToMap.get("error_msg");
            logIssue("百度翻译API服务发生错误。error_code：" + error_code + " error_msg：" + error_msg);

        } else {
            Object trans_result = jsonToMap.get("trans_result");
            List<Map> mapList = (List<Map>) trans_result;
            if (mapList != null && mapList.size() > 0) {
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, String> map = mapList.get(i);
                    String src = map.get("src");
                    String dst = map.get("dst");
                    $info("src:" + src + " -> dst:" + dst);
                    resultStrList.add(dst);
                }
            }
        }

        return resultStrList;
    }

    class BaiduTranslateTask implements Callable<String> {

        /**
         * 待翻译的文言列表
         */
        private List<Map<String, String>> subTransStrList;

        /**
         * 线程号
         */
        private int threadNo;

        /**
         * 构造函数
         */
        public BaiduTranslateTask(List<Map<String, String>> subTransStrList, int threadNo) {
            this.subTransStrList = subTransStrList;
            this.threadNo = threadNo;
        }

        @Override
        public String call() throws Exception {
            logger.info("百度翻译 product attribute value 任务 thread-" + threadNo + " start");

            if (subTransStrList == null || subTransStrList.size() <= 0) {
                return "thread-" + threadNo + " 要翻译的文言列表为空，不需要翻译";
            }

            List<String> transStrList = new ArrayList<String>();
            for (Map<String, String> transMap : subTransStrList) {
                String transStr = transMap.get("attribute_value");
                transStrList.add(transStr);
            }

            List<String>  translateList = translate(transStrList, threadNo);

            if (translateList.size() > 0) {

                if (translateList.size() != subTransStrList.size()) {
                    $info("百度翻译任务thread-" + threadNo + "翻译之后的文言数和翻译前不一致，有错误发生");
                } else {
                    for (int i = 0; i < translateList.size(); i++) {
                        String finishStr = translateList.get(i);
                        subTransStrList.get(i).put("attribute_value_cn", finishStr);
                    }
                    TRANSLATE_RESULT_TMP_LIST.add(subTransStrList);
                }
            } else {
                $info("百度翻译任务thread-" + threadNo + "有错误发生");
            }

            // 已翻译成功的文言返回
            $info("百度翻译 product attribute value 任务 thread-" + threadNo + "结束");

            return "百度翻译 product attribute value 任务 thread-" + threadNo + "结束";
        }
    }
}
