package com.voyageone.task2.cms.service.feed;

import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.dao.feed.TransformSqlDao;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.Feeds;
import com.voyageone.common.configs.beans.FeedBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.voyageone.common.configs.Enums.FeedEnums.Name.transform;

/**
 * 针对 feed_config 中配置的操作步骤进行数据处理
 * <p>
 * Created by Jonas on 10/16/15.
 */
@Component
public class Transformer {

    @Autowired
    private TransformSqlDao sqlDao;

    /**
     * 保存上下文参数的内部类
     */
    public class Context {

        private List<FeedBean> configs;

        private Map<String, Object> simpleStack = new HashMap<>();

        private BaseTaskService taskService;

        /**
         * 使用渠道创建上下文
         *
         * @param channel 渠道
         */
        public Context(Channel channel, BaseTaskService taskService) {

            this.taskService = taskService;

            configs = Feeds.getConfigs(channel.getId(), transform);
            // 按配置从新排序
            if (configs != null && configs.size() > 0)
                configs.sort((a, b) -> a.getDisplay_sort() - b.getDisplay_sort());
        }

        /**
         * 根据配置处理数据
         */
        public void transform() {
            // 遍历配置,进行每一步的数据操作
            for (FeedBean feed : configs) {
                // 通过 val1 的配置选择具体数据操作
                // 操作中的日志,全部借 service 发出.所以通过 service 调用
                switch (feed.getCfg_val1()) {
                    case SELECT_STR:
                        List<String> strings = sqlDao.selectStrings(feed.getCfg_val2());
                        taskService.$info("执行 SELECT_STR 操作: [ %s ] -> %s", feed.getComment(), strings.size());
                        after(strings, feed);
                        break;
                    case SELECT_MAP:
                        List<Map<String, Object>> maps = sqlDao.selectMaps(feed.getCfg_val2());
                        taskService.$info("执行 SELECT_MAP 操作: [ %s ] -> %s", feed.getComment(), maps.size());
                        after(maps, feed);
                        break;
                    case INSERT:
                        int insertCount = sqlDao.insert(feed.getCfg_val2());
                        taskService.$info("执行 INSERT 操作: [ %s ] -> %s", feed.getComment(), insertCount);
                        break;
                    case DELETE:
                        int deleteCount = sqlDao.delete(feed.getCfg_val2());
                        taskService.$info("执行 DELETE 操作: [ %s ] -> %s", feed.getComment(), deleteCount);
                        after(deleteCount, feed);
                        break;
                    case UPDATE:
                        int updateCount = sqlDao.update(feed.getCfg_val2());
                        taskService.$info("执行 UPDATE 操作: [ %s ] -> %s", feed.getComment(), updateCount);
                        after(updateCount, feed);
                        break;
                    case MAIL:
                        Object val = before(feed);
                        taskService.logIssue(feed.getCfg_val2(), val);
                        break;
                }
            }
        }

        private Object before(FeedBean feed) {
            // 拆分变量操作部分的定义
            String[] varDeclare = getVarDeclare(feed);
            // 如果定义非法,则直接无视
            if (varDeclare == null || varDeclare.length < 1) return null;
            switch (varDeclare[0]) {
                case GET:
                    if (varDeclare.length < 2) return null;
                    return simpleStack.get(varDeclare[1]);
            }
            return null;
        }

        private void after(int val, FeedBean feed) {
            // 拆分变量操作部分的定义
            String[] varDeclare = getVarDeclare(feed);
            // 如果定义非法,则直接无视
            if (varDeclare == null || varDeclare.length < 1) return;
            // 这里只执行适用于 Int 类型的预定义操作
            switch (varDeclare[0]) {
                case SET:
                    if (varDeclare.length < 2) return;
                    simpleStack.put(varDeclare[1], val);
                    break;
            }
        }

        private void after(List list, FeedBean feed) {
            // 拆分变量操作部分的定义
            String[] varDeclare = getVarDeclare(feed);
            // 如果定义非法,则直接无视
            if (varDeclare == null || varDeclare.length < 1) return;
            // 这里只执行适用于 List<String> 类型的预定义操作
            switch (varDeclare[0]) {
                case SET:
                    if (varDeclare.length < 2) return;
                    simpleStack.put(varDeclare[1], list);
                    break;
                case APPEND:
                    if (varDeclare.length < 2) return;
                    Object obj = simpleStack.get(varDeclare[1]);
                    if (obj == null) obj = new ArrayList<String>();
                    List oldStrings = (List) obj;
                    oldStrings.addAll(list);
                    simpleStack.put(varDeclare[1], oldStrings);
                    break;
            }
        }

        private String[] getVarDeclare(FeedBean feed) {
            // 拆分变量操作部分的定义
            String declare = feed.getCfg_val3();

            if (StringUtils.isEmpty(declare)) return null;

            return declare.split(":");
        }
    }

    /**
     * 数据级操作
     */
    private static final String SELECT_STR = "select_str";
    private static final String SELECT_MAP = "select_map";
    private static final String INSERT = "insert";
    private static final String DELETE = "delete";
    private static final String UPDATE = "update";
    private static final String MAIL = "mail";

    /**
     * 变量级操作
     */
    private static final String GET = "get";
    private static final String SET = "set";
    private static final String APPEND = "append";
}
