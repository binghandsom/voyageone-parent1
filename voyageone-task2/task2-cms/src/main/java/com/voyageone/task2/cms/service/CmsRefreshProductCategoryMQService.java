package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.category.match.MatchResult;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.*;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.cms.service.platform.uj.UploadToUSJoiService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 批量重刷主类目MQ服务
 *
 * @author desmond
 * Created by james on 2016/12/30.
 * @version 2.10.0
 * @version 2.10.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_CmsBatchRefreshMainCategoryJob)
public class CmsRefreshProductCategoryMQService extends BaseMQCmsService  {

    @Autowired
    private ProductService productService;
    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private UploadToUSJoiService uploadToUSJoiService;

    /**
     * 入口
     * 输入参数:
     *     channelId：渠道ID（必须）
     *     codeList： 产品code列表（必须）
     *     userName:  更新者(任意，默认为当前Job名字）
     *
     * @param messageMap Mq消息Map
     * @throws Exception
     */
    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        $info("CmsRefreshProductCategoryMQService start 参数 " + JacksonUtil.bean2Json(messageMap));

        // 检查输入参数
        // 参数: 渠道id
        String channelId;
        if (messageMap.containsKey("channelId")) {
            channelId = String.valueOf(messageMap.get("channelId"));
        } else {
            $error("批量重刷主类目(MQ): 输入参数不存在: channelId");
            return;
        }

        // 参数: 产品code列表
        List<String> codeList = null;
        if (messageMap.containsKey("codeList")) {
            codeList = (List<String>) messageMap.get("codeList");
        }
        if (ListUtils.isNull(codeList)) {
            $error("批量重刷主类目(MQ): 输入参数不存在: codeList");
            return;
        }

        // 参数: 更新者
        String userNameTemp = null;
        if (messageMap.containsKey("userName")) {
            userNameTemp = String.valueOf(messageMap.get("userName"));
        }
        if (StringUtils.isEmpty(userNameTemp)) {
            userNameTemp = "CmsRefreshProductCategoryMQService";
        }
        String userName = userNameTemp;

        // 默认线程池最大线程数(因为调用接口取得主类目信息很慢，所以这里用多线程应该比批量回写mongoDB要快)
        int threadPoolCnt = 5;

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolCnt);
        // 根据MQ消息传过来的对象code列表循环
        for(String code : codeList) {
            // 启动多线程
            executor.execute(() -> doMain(channelId, code, userName));
        }
        // ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
        // 当所有已经提交的任务执行完毕后将会关闭ExecutorService。
        executor.shutdown(); // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
        try {
            // 阻塞，直到线程池里所有任务结束
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        $info("CmsRefreshProductCategoryMQService success end");
    }

    /**
     * 主类目设置处理
     *
     * @param channelId 渠道id
     * @param code 对象产品code
     * @param userName 更新者
     */
    protected void doMain(String channelId, String code, String userName) {

        try {
            // 取得产品信息
            CmsBtProductModel prodObj = productService.getProductByCode(channelId, code);
            if (prodObj == null) {
                String warnMsg = String.format("没有取得该产品信息！[channelId:%s] [code:%s]", channelId, code);
                $warn(warnMsg);
                return;
            }

            // TODO 2016/12/30暂时这样更新，以后要改
            // 如果common.catConf="0"(非人工匹配主类目)时，不用自动匹配主类目
//            if (!"0".equals(prodObj.getCommonNotNull().getCatConf())) return;

            // 如果common.catConf="0"(非人工匹配主类目)时，自动匹配商品主类目
            // 共通Field
            CmsBtProductModel_Field prodCommonField = prodObj.getCommonNotNull().getFieldsNotNull();
            // 调用Feed到主数据的匹配接口取得匹配度最高的主类目
            MatchResult searchResult = uploadToUSJoiService.getMainCatInfo(prodObj.getFeed().getCatPath(),
                    !StringUtils.isEmpty(prodCommonField.getOrigProductType()) ? prodCommonField.getOrigProductType() : "",
                    !StringUtils.isEmpty(prodCommonField.getOrigSizeType()) ? prodCommonField.getOrigSizeType() : "",
                    prodCommonField.getProductNameEn(),
                    prodCommonField.getBrand());
            if (searchResult == null) {
                String warnMsg = String.format("调用Feed到主数据的匹配接口未能取得匹配度最高的主类目！[channelId:%s] [code:%s] [catConf:%s]" +
                                "[feedCategoryPath:%s] [productType:%s] [sizeType:%s] [productNameEn:%s] [brand:%s]",
                        channelId, code, prodObj.getCommonNotNull().getCatConf(), prodObj.getFeed().getCatPath(),
                        prodCommonField.getProductType(), prodCommonField.getSizeType(), prodCommonField.getProductNameEn(), prodCommonField.getBrand());
                $warn(warnMsg);
                return;
            }

            // 构造更新SQL
            List<BulkUpdateModel> bulkList = new ArrayList<>();
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", code);
            // 更新字段
            HashMap<String, Object> updateMap = new HashMap<>();
            // 主类目path(中文)
            if (!StringUtils.isEmpty(searchResult.getCnName()))   updateMap.put("common.catPath", searchResult.getCnName());
            // 主类目path(英文)
            if (!StringUtils.isEmpty(searchResult.getEnName()))   updateMap.put("common.catPathEn", searchResult.getEnName());
            // 主类目id(就是主类目path中文的MD5码)
            if (!StringUtils.isEmpty(searchResult.getCnName())) {
                updateMap.put("common.catId", MD5.getMD5(searchResult.getCnName()));
                updateMap.put("common.fields.categorySetTime", DateTimeUtil.getNow());
                updateMap.put("common.fields.categorySetter", userName);
            }
            // 更新主类目设置状态(匹配到主类目中文名字 或者 以前catId就有值时)
            if (!StringUtil.isEmpty(searchResult.getCnName())
                    || !StringUtils.isEmpty(prodObj.getCommonNotNull().getCatId())) {
                updateMap.put("common.fields.categoryStatus", "1");
            } else {
                updateMap.put("common.fields.categoryStatus", "0");
            }
            // 产品分类(英文)
            if (!StringUtils.isEmpty(searchResult.getProductTypeEn()))   updateMap.put("common.fields.productType", searchResult.getProductTypeEn().toLowerCase());
            // 产品分类(中文)
            if (!StringUtils.isEmpty(searchResult.getProductTypeCn()))   updateMap.put("common.fields.productTypeCn", searchResult.getProductTypeCn());
            // 适合人群(英文)
            if (!StringUtils.isEmpty(searchResult.getSizeTypeEn()))      updateMap.put("common.fields.sizeType", searchResult.getSizeTypeEn().toLowerCase());
            // 适合人群(中文)
            if (!StringUtils.isEmpty(searchResult.getSizeTypeCn()))      updateMap.put("common.fields.sizeTypeCn", searchResult.getSizeTypeCn());
            // TODO 2016/12/30暂时这样更新，以后要改
            if ("CmsUploadProductToUSJoiJob".equalsIgnoreCase(prodCommonField.getHsCodeSetter())
                    || (StringUtils.isEmpty(prodCommonField.getHsCodePrivate()))
                    || (!StringUtils.isEmpty(prodCommonField.getHsCodePrivate())
                        && prodCommonField.getHsCodePrivate().split(",").length != 3)) {
                // 税号个人
                if (!StringUtils.isEmpty(searchResult.getTaxPersonal())) {
                    updateMap.put("common.fields.hsCodePrivate", searchResult.getTaxPersonal());
                    updateMap.put("common.fields.hsCodeSetTime", DateTimeUtil.getNow());
                    updateMap.put("common.fields.hsCodeSetter", userName);
                }
                // 更新税号设置状态(匹配到税号个人 或者 以前税号个人就有值时)
                if (!StringUtil.isEmpty(searchResult.getTaxPersonal())
                        || !StringUtils.isEmpty(prodCommonField.getHsCodePrivate())) {
                    updateMap.put("common.fields.hsCodeStatus", "1");
                } else {
                    updateMap.put("common.fields.hsCodeStatus", "0");
                }
            }
            // 税号跨境申报（10位）
            if (!StringUtils.isEmpty(searchResult.getTaxDeclare()))      updateMap.put("common.fields.hsCodeCross", searchResult.getTaxDeclare());

            // 商品中文名称(如果已翻译，则不设置)
            if ("0".equals(prodCommonField.getTranslateStatus())) {
                if (!StringUtils.isEmpty(searchResult.getCnName())) {
                    // 主类目叶子级中文名称（"服饰>服饰配件>钱包卡包钥匙包>护照夹" -> "护照夹"）
                    String leafCategoryCnName = searchResult.getCnName().substring(searchResult.getCnName().lastIndexOf(">") + 1,
                            searchResult.getCnName().length());

                    String titleCn = "";
                    // 设置商品中文名称（品牌 + 空格 + Size Type中文 + 空格 + 主类目叶子级中文名称）
                    if(channelId.equals("928")) {
                        TypeChannelBean typeChannelBean = null;
                        if (!StringUtil.isEmpty(prodCommonField.getBrand())) {
                            typeChannelBean = TypeChannels.getTypeChannelByCode(Constants.comMtTypeChannel.BRAND_41, "928", prodCommonField.getBrand(), "cn");
                        }
                        if (typeChannelBean != null && !StringUtil.isEmpty(typeChannelBean.getName())) {
                            titleCn = uploadToUSJoiService.getOriginalTitleCnByCategory(typeChannelBean.getName(), prodCommonField.getSizeTypeCn(), leafCategoryCnName);
                        } else {
                            titleCn = uploadToUSJoiService.getOriginalTitleCnByCategory(prodCommonField.getBrand(), prodCommonField.getSizeTypeCn(), leafCategoryCnName);
                        }
                    }else{
                        titleCn = uploadToUSJoiService.getOriginalTitleCnByCategory(prodCommonField.getBrand(), prodCommonField.getSizeTypeCn(), leafCategoryCnName);
                    }
                    if (!StringUtils.isEmpty(titleCn)) {
                        if (!"017".equals(prodObj.getOrgChannelId())
                                || ("017".equals(prodObj.getOrgChannelId()) && StringUtils.isEmpty(prodCommonField.getOriginalTitleCn())))
                        updateMap.put("common.fields.originalTitleCn", titleCn);
                    }
                }
            }
            BulkUpdateModel model = new BulkUpdateModel();
            model.setQueryMap(queryMap);
            model.setUpdateMap(updateMap);
            bulkList.add(model);
            // 更新主类目信息
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");
            $info("产品(%s)主类目设置成功!", code);
        } catch (Exception exception) {
            String warnMsg = String.format("主类目设置处理异常！[channleId:%s] [code:%s] [userName:%s] [errMsg:%s]",
                    channelId, code, userName, Arrays.toString(exception.getStackTrace()));
            $warn(warnMsg);
        }
    }
}
