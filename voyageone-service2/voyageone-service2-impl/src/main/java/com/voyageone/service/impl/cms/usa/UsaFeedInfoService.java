package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/7/5.
 * 美国用 feed info数据service
 */
@Service
public class UsaFeedInfoService extends BaseService {

    @Autowired
    FeedInfoService feedInfoService;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    /**
     * 条件查询feed商品列表
     * @param searchValue
     * @param userInfo
     */
    public List<CmsBtFeedInfoModel> getFeedList(Map<String, Object> searchValue, UserSessionBean userInfo) {
        String channel = userInfo.getSelChannel().toString();
        //封装查询条件
        JongoQuery queryObject = getQuery(searchValue, userInfo);
        //设置排序条件,排序字段后面拼接排序方式,下划线分割(1,正序,-1倒叙),默认不排序,点击那个按照哪个排序
        String  sortFild = (String) searchValue.get("sort");
        if (sortFild != null){
            String[] split = sortFild.split("_");
            StringBuffer buffer = new StringBuffer();
            buffer.append("{'");
            buffer.append(split[0]);
            buffer.append("':");
            buffer.append(split[1]);
            buffer.append("}");
            queryObject.setSort(buffer.toString());
        }
        //封装分页条件
        int pageNum = (Integer) searchValue.get("pageNum");
        int pageSize = (Integer) searchValue.get("pageSize");
        queryObject.setSkip((pageNum - 1) * pageSize);
        queryObject.setLimit(pageSize);

        return  feedInfoService.getList(channel,queryObject);
    }

    /**
     * 查询feed商品总数
     * @param searchValue
     * @param userInfo
     * @return
     */
    public Long getFeedCount(Map<String, Object> searchValue, UserSessionBean userInfo) {
        String channel = userInfo.getSelChannel().toString();
        //封装查询条件
        JongoQuery query = getQuery(searchValue, userInfo);
        return cmsBtFeedInfoDao.countByQuery(query.getQuery(), channel);
    }

    //组装查询条件
    public JongoQuery getQuery(Map<String, Object> searchValue, UserSessionBean userInfo){
        //封装查询条件
        Criteria criteria = new Criteria();
        //状态
        if (searchValue.get("status") != null){
            criteria = criteria.and("status").is((String)searchValue.get("status"));
        }
        //设置开始和截止的时间
        if (searchValue.get("lastReceivedOnStart") != null && searchValue.get("lastReceivedOnEnd") == null){
            criteria = criteria.and("lastReceivedOn").gte((String)searchValue.get("lastReceivedOnStart"));
        }
        if (searchValue.get("lastReceivedOnEnd") != null && searchValue.get("lastReceivedOnStart") == null){
            criteria = criteria.and("lastReceivedOn").lte((String)searchValue.get("lastReceivedOnEnd"));
        }
        if (searchValue.get("lastReceivedOnEnd") != null && searchValue.get("lastReceivedOnStart") != null){
            criteria = criteria.and("lastReceivedOn").gte((String)searchValue.get("lastReceivedOnStart")).lte((String)searchValue.get("lastReceivedOnEnd"));
        }
        //name模糊查询
        if (searchValue.get("name") != null){
            String name = (String) searchValue.get("name");
            StringBuffer buffer = new StringBuffer("/");
            buffer.append(name);
            buffer.append("/");
            criteria = criteria.and("name").is(buffer.toString());
        }
        //多条件精确查询,SKU/ Barcode/ Code / Model
        if (searchValue.get("searchContent") != null){
            String searchContent = (String) searchValue.get("searchContent");
            String[] split = searchContent.split("/n");
            List<String> searchContents = Arrays.asList(split);
            Criteria criteria2 = new Criteria("code").in(searchContents).orOperator(new Criteria("model").in(searchContents)).
                    orOperator(new Criteria("skus.sku").in(searchContent)).orOperator(new Criteria("skus.barcode").in(searchContent));
            criteria.orOperator(criteria2);
        }
        if (searchValue.get("isApprove") != null){
            criteria = criteria.and("isApprove").is((String)searchValue.get("isApprove"));
        }
        return new JongoQuery(criteria);
    }


/*xu*/
    //---------------------^^^^-------------------------------
/*wu*/
    /**
     * 根据model查询符合特定条件的特定个数(暂定5)的code
     * <p>先查询product.platforms.Pxx.status in[Approved->Ready->Pending]</p>
     * <p>  其中xx为U.S.Official对应的cartId, 平台状态有优先级</p>
     * <p>---------------------------分割线------------------------------</p>
     * <p>如果product查不到满足条件的model信息，则从feed中查询model且status in[Approved->Ready->Pending->New]</p>
     * <p>  feed状态有优先级</p>
     *
     * @param channelId 渠道ID
     * @param model     feed->model
     */
    public void getTopModelsByModel(String channelId, String model) {
        // TODO: 2017/7/5 rex.wu 
    }


}
