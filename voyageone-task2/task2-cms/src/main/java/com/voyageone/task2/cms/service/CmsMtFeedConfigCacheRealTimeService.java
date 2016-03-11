package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.beans.FeedBean;
import com.voyageone.common.configs.dao.FeedDao;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/3/8.
 * @version 2.0.0
 * @since 2.0.0
 */
//@Service
public class CmsMtFeedConfigCacheRealTimeService extends BaseTaskService {

    private static final String REDIS_KEY_NAME = "cms_mt_feed_config";

    private static final String MODIFYBEAN="$modify_bean$";

    /* redis模板 */
//    @Autowired
    private RedisTemplate<String,Map<String,FeedBean>> redisTemplate;

    /* hash操作对象 */
    private HashOperations<String,String,FeedBean> hashOperations;
//    @Autowired
    private FeedDao feedDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.COM;
    }

    @Override
    public String getTaskName() {
        return "cmsMtFeedConfigCacheRealTimeJob";
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
        if(hashOperations==null)
            hashOperations = redisTemplate.opsForHash();
        /**
         * feedBeanMaps数组三个map，
         * 按照小标对应
         * 0 为增加或更新缓存的FeedBeanMap
         * 1 删除缓存的FeedBeanMap
         * 2 modifyBean最后更新的ModifyBeanMap
         */
        HashMap<String,FeedBean>[] feedBeanMaps= new HashMap[]{new HashMap<>(), new HashMap<>(), new HashMap<>()};
        if(hashOperations.hasKey(REDIS_KEY_NAME,MODIFYBEAN)) {
            //拿到缓存服务器的ModifyBean
            feedBeanMaps[2].put(MODIFYBEAN, hashOperations.get(REDIS_KEY_NAME, MODIFYBEAN));
            List<FeedBean> feedBeens;
            if (!CollectionUtils.isEmpty(feedBeens = feedDao.getAllUpdate(feedBeanMaps[2].get(MODIFYBEAN).getModified())))
                loopAddFeedBeanMap(feedBeens, feedBeanMaps);
        }else
            loopAddFeedBeanMap(feedDao.getAll(),feedBeanMaps);
    }

    /**
     * 循环FeedBeanList，添加不同状态的FeedBean到更新和删除的Map，同时对比更新产生modifyBean
     * @param feedBeans Mysql 数据集
     * @param feedBeanMaps feedBeanMaps数组
     */
    private void loopAddFeedBeanMap(List<FeedBean> feedBeans,Map<String,FeedBean>[] feedBeanMaps){
        feedBeans.forEach(a->{
            if(a.getStatus()==0) feedBeanMapPutRule(feedBeanMaps[1],a); else feedBeanMapPutRule(feedBeanMaps[0],a);
            //0 != a.getStatus() ? feedBeanMapPutRule(feedBeanDateMap, a) : feedBeanMapPutRule(feedBeanDelMap, a);
            modifyBeanCompare(feedBeanMaps[2],a);
        });
        callRedisCache(feedBeanMaps);
    }

    /**
     * 调用redis Server缓存Map
     * @param feedBeanMaps feedBeanMaps数组
     */
    private void callRedisCache(Map<String,FeedBean>[] feedBeanMaps){
        //缓存删除所有数据
        if(MapUtils.isNotEmpty(feedBeanMaps[1]))
            hashOperations.delete(REDIS_KEY_NAME,feedBeanMaps[1].keySet());
        //缓存更新所有数据
        if(MapUtils.isNotEmpty(feedBeanMaps[0]))
            hashOperations.putAll(REDIS_KEY_NAME,feedBeanMaps[0]);
        //缓存增加一条 name_modify_feebean
        if(MapUtils.isNotEmpty(feedBeanMaps[2]))
            hashOperations.put(REDIS_KEY_NAME,MODIFYBEAN,feedBeanMaps[2].get(MODIFYBEAN));
    }

    /**
     * feedBean 加入缓存Map规则
     * @param feedBeanMap map容器
     * @param a 待放入的feedBean
     */
    private void feedBeanMapPutRule(Map<String,FeedBean> feedBeanMap,FeedBean a){
        feedBeanMap.put(a.getOrder_channel_id() + a.getCfg_name(), a);
    }

    /**
     * 最后更新Bean对比修改
     * @param modifyBeanMap 存放的容器
     * @param a 待对比的feedBean
     */
    private void modifyBeanCompare(Map<String,FeedBean> modifyBeanMap,FeedBean a){
        if(a.getModified()==null) return;
        if (MapUtils.isEmpty(modifyBeanMap)||modifyBeanMap.get(MODIFYBEAN).getModified().getTime() < a.getModified().getTime())
            modifyBeanMap.put(MODIFYBEAN, a);
    }
}
