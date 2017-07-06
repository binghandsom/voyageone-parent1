package com.voyageone.service.impl.cms.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedInfoDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import org.apache.commons.collections4.CollectionUtils;

import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    private CmsBtProductDao cmsBtProductDao;

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
    public List<CmsBtFeedInfoModel> getTopModelsByModel(String channelId, String model) {
        // TODO: 2017/7/6 rex.wu cartId=23待定.......
        List<CmsConstants.ProductStatus> queryProductStatus = new ArrayList<>();
        queryProductStatus.add(CmsConstants.ProductStatus.Approved);
        queryProductStatus.add(CmsConstants.ProductStatus.Ready);
        queryProductStatus.add(CmsConstants.ProductStatus.Pending);
        int top = 5;
        // 先从Product查询
        List<CmsBtProductModel> resultProductList = new ArrayList<>(top);
        String projection = "{_id:1,code:1,model:1}";
        String query = String.format("{\"channelId\":#,\"common.fields.model\":#,\"platforms.P#\":{$exists:true},\"platforms.P#.status\":#}");
        int count = 0;
        for (CmsConstants.ProductStatus productStatus : queryProductStatus) {
            JongoQuery jongoQuery = new JongoQuery(null, query, null, top - resultProductList.size(), 0);
            jongoQuery.setParameters(channelId, model, 23, 23, productStatus.name());
            List<CmsBtProductModel> tempResultProductList = cmsBtProductDao.select(jongoQuery, channelId);
            count = tempResultProductList.size();
            if (count > 0) {
                resultProductList.addAll(tempResultProductList);
            }
            if (resultProductList.size() >= top) {
                break;
            }
        }
        // 如果从Product查询不到数据，则再从Feed中查出数据
        if (resultProductList.isEmpty()) {
            count = 0;
            List<CmsConstants.UsaFeedStatus> queryFeedStatus = new ArrayList<>();
            queryFeedStatus.add(CmsConstants.UsaFeedStatus.Approved);
            queryFeedStatus.add(CmsConstants.UsaFeedStatus.Ready);
            queryFeedStatus.add(CmsConstants.UsaFeedStatus.Pending);
            // 再从Feed中查询
            List<CmsBtFeedInfoModel> resultFeedList = new ArrayList<>(top);
            projection = "{_id:1,code:1,model:1}";
            query = String.format("{\"channelId\":#,\"model\":#,\"status\":{$exists:true},\"status\":#}");
            for (CmsConstants.UsaFeedStatus feedStatus : queryFeedStatus) {
                JongoQuery jongoQuery = new JongoQuery(projection, query, null, top - resultFeedList.size(), 0);
                jongoQuery.setParameters(channelId, model, 23, 23, feedStatus.name());
                List<CmsBtFeedInfoModel> tempResultFeedList = cmsBtFeedInfoDao.select(jongoQuery, channelId);
                count = tempResultFeedList.size();
                if (count > 0) {
                    resultFeedList.addAll(tempResultFeedList);
                }
                if (resultFeedList.size() >= top) {
                    break;
                }
            }
            return resultFeedList;
        }

        // 选中同Model的某个Code,则执行覆盖操作,覆盖内容: 除了code, name, color, colorMap, 图片，urlKey以外的code级别属性
        // 覆盖内容: Brand,ProductType,SizeType,Material,Made In,Amazon Category,Usage,Short Description,Long Description,Order Limit Count,Accessory
        if (!resultProductList.isEmpty()) {
            return this.tempConvertToFeedInfo(resultProductList);
        }
        return Collections.emptyList();
    }

    /**
     * 抽取Product部分属性,将其临时封装为FeedInfo
     *
     * @param productModels 待处理Product数据
     */
    private List<CmsBtFeedInfoModel> tempConvertToFeedInfo(List<CmsBtProductModel> productModels) {
        List<CmsBtFeedInfoModel> feedInfoModels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(productModels)) {
            CmsBtFeedInfoModel feed = null;
            for (CmsBtProductModel product : productModels) {
                CmsBtProductModel_Field fields = product.getCommon().getFields();
                feed = new CmsBtFeedInfoModel();
                feed.setChannelId(product.getChannelId());
                feed.setModel(fields.getModel());
                feed.setCode(fields.getCode());
                // Brand
                feed.setBrand(fields.getBrand());
                // ProductType
                feed.setProductType(fields.getProductType());
                // SizeType
                feed.setSizeType(fields.getSizeType());
                // Material
                feed.setMaterial(fields.getMaterialEn());
                // Made In
                feed.setOrigin(fields.getOrigin());
                // Amazon Category
                // TODO: 2017/7/6 rex.wu
                // Usage
                feed.setUsageEn(fields.getUsageEn());
                // Short Description
                feed.setShortDescription(fields.getShortDesEn());
                // Long Description
                feed.setLongDescription(fields.getLongDesEn());
                // Order Limit Count
                // TODO: 2017/7/6 rex.wu
                // Accessory
                // TODO: 2017/7/6 rex.wu
                feedInfoModels.add(feed);
            }
        }
        return feedInfoModels;
    }

    /**
     * 保存或者提交Feed
     *
     * @param feedInfoModel Feed信息
     * @param feedStatus    Submit后Feed状态，如果传入值为null或者和feedInfoModel值一直则视为Save操作
     */
    public void saveOrSubmitFeed(CmsBtFeedInfoModel feedInfoModel, CmsConstants.UsaFeedStatus feedStatus) {
        if (feedInfoModel == null) return;
        boolean isSave = feedStatus == null || Objects.equals(feedInfoModel.getStatus(), feedStatus.name());
        if (isSave) {
            // TODO: 2017/7/6 rex.wu Save FeedInfo
        } else {
            // FeedInfoModel->status状态流[New->Pending->Ready->Approved]
            String nextFeedStatus = null;
            if (CmsConstants.UsaFeedStatus.New.name().equals(feedInfoModel.getStatus())) {
                nextFeedStatus = CmsConstants.UsaFeedStatus.Pending.name();
            } else if (CmsConstants.UsaFeedStatus.Pending.name().equals(feedInfoModel.getStatus())) {
                nextFeedStatus = CmsConstants.UsaFeedStatus.Ready.name();
            } else if (CmsConstants.UsaFeedStatus.Ready.name().equals(feedInfoModel.getStatus())) {
                nextFeedStatus = CmsConstants.UsaFeedStatus.Approved.name();
            }
            if (nextFeedStatus == null) {
                throw new BusinessException(String.format("Invalid Status, Current Status(%s) -> Next Status(%s)", feedInfoModel.getStatus(), nextFeedStatus));
            }
            // TODO: 2017/7/6 rex.wu Submit FeedInfo


            if (CmsConstants.UsaFeedStatus.Approved.name().equals(nextFeedStatus)) {

            }
        }

    }

}
