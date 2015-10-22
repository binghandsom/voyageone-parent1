package com.voyageone.batch.cms.service.feed;

import com.google.gson.Gson;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.batch.cms.dao.feed.BcbgSuperFeedDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Enums.FeedEnums.Name;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.*;

import static com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel.BCBG;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Bcbg 的 Feed 数据分析服务
 *
 * Created by Jonas on 10/10/15.
 */
@Service
public class BcbgAnalysisService extends BaseTaskService {

    @Autowired
    private BcbgSuperFeedDao bcbgSuperFeedDao;
    
    @Autowired
    private SuperFeedDao superFeedDao;

    @Autowired
    private Transformer transformer;

    @Autowired
    private BcbgWsdlInsert insertService;

    @Autowired
    private BcbgWsdlUpdate updateService;

    /**
     * 获取子系统
     */
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "BcbgAnalysis";
    }

    /**
     * 必须实现的，具体的任务内容
     *
     * @param taskControlList job 配置
     */
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        $info("开始处理 BCBG 数据");

        // 读取各种配置
        // 精简配置,减少独立配置,所以两个文件都配置在一个项目里
        String fileNames = Feed.getVal1(BCBG, Name.feed_ftp_filename); // 文件路径

        // 拆分成 feed 和 style
        String[] fileNameArr = fileNames.split(";");

        if (fileNameArr.length != 2) {
            $info("读取的文件路径错误,至少需要两个文件.退出任务");
            throw new BusinessException("BCBG 的配置 feed_ftp_filename 错误,至少两个文件.");
        }

        String feedFileName = fileNameArr[0];
        String styleFileName = fileNameArr[1];

        // 检查配置
        if (StringUtils.isAnyEmpty(feedFileName, styleFileName)) {
            $info("没读取到文件路径,退出任务");
            throw new BusinessException("BCBG 的配置 feed_ftp_filename 错误,路径为空.");
        }

        // 检查文件是否存在
        // 不存在就直接结束处理
        File feedFile = new File(feedFileName);
        File styleFile = new File(styleFileName);
        if (!feedFile.exists() || !styleFile.exists()) {
            $info("数据文件不存在,退出任务 [ %s ] [ %s ]", feedFile.exists(), styleFile.exists());
            return;
        }

        // 读取数据文件
        BcbgFeedFile bcbgFeedFile = BcbgFeedFile.read(feedFile);
        List<SuperFeedBcbgBean> bcbgBeans = bcbgFeedFile.getMATERIALS();

        $info("已读取文件.获得 Feed %s 个", bcbgBeans.size());

        FileReader styleFileReader = new FileReader(styleFile);
        BcbgStyleBean[] styleBeans = new Gson().fromJson(styleFileReader, BcbgStyleBean[].class);

        $info("已读取文件.获得 Style %s 个", styleBeans.length);

        // 插入数据库
        clearLastData();
        insertNewData(bcbgBeans, styleBeans);

        // 处理下拉类属性
        attributeListInsert(BCBG);

        // 开始数据分析处理阶段
        transformer.new Context(BCBG, this).transform();
        $info("数据处理阶段结束");

        // 使用接口提交
        insertService.new Context(BCBG).postNewProduct();
        updateService.new Context(BCBG).postUpdatedProduct();
//        postAttribute();

        // 备份文件
//        backupDataFile(feedFile, styleFile);
    }

    private void clearLastData() {
        // 删除所有
        bcbgSuperFeedDao.delete();
        bcbgSuperFeedDao.deleteStyles();
    }

    /**
     * 推送所有商品的非 CMS 属性,插入和更改都通过这里推送.
     * @throws Exception
     */
    private void postAttribute() throws Exception {

        String channel_id = BCBG.getId();

        WsdlProductService service = new WsdlProductService(BCBG);

        ProductsFeedAttribute feedAttribute = new ProductsFeedAttribute();

        List<AttributeBean> attributes = new ArrayList<>();

        AttributeBean attribute = new AttributeBean();

        attribute.setCategory_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_category_url_key));
        attribute.setModel_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_model_url_key));
        attribute.setProduct_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_url_key));
        attribute.setAttribute1(Feed.getVal1(channel_id, FeedEnums.Name.attribute1));
        attribute.setAttribute2(Feed.getVal1(channel_id, FeedEnums.Name.attribute2));
        attribute.setAttribute3(Feed.getVal1(channel_id, FeedEnums.Name.attribute3));
        attribute.setAttribute4(Feed.getVal1(channel_id, FeedEnums.Name.attribute4));
        attribute.setAttribute5(Feed.getVal1(channel_id, FeedEnums.Name.attribute5));
        attribute.setAttribute6(Feed.getVal1(channel_id, FeedEnums.Name.attribute6));
        attribute.setAttribute7(Feed.getVal1(channel_id, FeedEnums.Name.attribute7));
        attribute.setAttribute8(Feed.getVal1(channel_id, FeedEnums.Name.attribute8));
        attribute.setAttribute9(Feed.getVal1(channel_id, FeedEnums.Name.attribute9));
        attribute.setAttribute10(Feed.getVal1(channel_id, FeedEnums.Name.attribute10));
        attribute.setAttribute11(Feed.getVal1(channel_id, FeedEnums.Name.attribute11));
        attribute.setAttribute12(Feed.getVal1(channel_id, FeedEnums.Name.attribute12));
        attribute.setAttribute13(Feed.getVal1(channel_id, FeedEnums.Name.attribute13));
        attribute.setAttribute14(Feed.getVal1(channel_id, FeedEnums.Name.attribute14));
        attribute.setAttribute15(Feed.getVal1(channel_id, FeedEnums.Name.attribute15));
        attribute.setAttribute16(Feed.getVal1(channel_id, FeedEnums.Name.attribute16));
        attribute.setAttribute17(Feed.getVal1(channel_id, FeedEnums.Name.attribute17));
        attribute.setAttribute18(Feed.getVal1(channel_id, FeedEnums.Name.attribute18));
        attribute.setAttribute19(Feed.getVal1(channel_id, FeedEnums.Name.attribute19));
        attribute.setAttribute20(Feed.getVal1(channel_id, FeedEnums.Name.attribute20));
        attribute.setAttribute21(Feed.getVal1(channel_id, FeedEnums.Name.attribute21));
        attribute.setAttribute22(Feed.getVal1(channel_id, FeedEnums.Name.attribute22));
        attribute.setAttribute23(Feed.getVal1(channel_id, FeedEnums.Name.attribute23));
        attribute.setAttribute24(Feed.getVal1(channel_id, FeedEnums.Name.attribute24));
        attribute.setAttribute25(Feed.getVal1(channel_id, FeedEnums.Name.attribute25));
        attribute.setAttribute26(Feed.getVal1(channel_id, FeedEnums.Name.attribute26));
        attribute.setAttribute27(Feed.getVal1(channel_id, FeedEnums.Name.attribute27));
        attribute.setAttribute28(Feed.getVal1(channel_id, FeedEnums.Name.attribute28));
        attribute.setAttribute29(Feed.getVal1(channel_id, FeedEnums.Name.attribute29));
        attribute.setAttribute30(Feed.getVal1(channel_id, FeedEnums.Name.attribute30));
        attribute.setAttribute31(Feed.getVal1(channel_id, FeedEnums.Name.attribute31));
        attribute.setAttribute32(Feed.getVal1(channel_id, FeedEnums.Name.attribute32));
        attribute.setAttribute33(Feed.getVal1(channel_id, FeedEnums.Name.attribute33));
        attribute.setAttribute34(Feed.getVal1(channel_id, FeedEnums.Name.attribute34));
        attribute.setAttribute35(Feed.getVal1(channel_id, FeedEnums.Name.attribute35));
        attribute.setAttribute36(Feed.getVal1(channel_id, FeedEnums.Name.attribute36));
        attribute.setAttribute37(Feed.getVal1(channel_id, FeedEnums.Name.attribute37));
        attribute.setAttribute37(Feed.getVal1(channel_id, FeedEnums.Name.attribute37));
        attribute.setAttribute38(Feed.getVal1(channel_id, FeedEnums.Name.attribute38));
        attribute.setAttribute39(Feed.getVal1(channel_id, FeedEnums.Name.attribute39));
        attribute.setAttribute40(Feed.getVal1(channel_id, FeedEnums.Name.attribute40));
        attribute.setAttribute41(Feed.getVal1(channel_id, FeedEnums.Name.attribute41));
        attribute.setAttribute42(Feed.getVal1(channel_id, FeedEnums.Name.attribute42));
        attribute.setAttribute43(Feed.getVal1(channel_id, FeedEnums.Name.attribute43));
        attribute.setAttribute44(Feed.getVal1(channel_id, FeedEnums.Name.attribute44));
        attribute.setAttribute45(Feed.getVal1(channel_id, FeedEnums.Name.attribute45));
        attribute.setAttribute46(Feed.getVal1(channel_id, FeedEnums.Name.attribute46));
        attribute.setAttribute47(Feed.getVal1(channel_id, FeedEnums.Name.attribute47));
        attribute.setAttribute48(Feed.getVal1(channel_id, FeedEnums.Name.attribute48));
        attribute.setAttribute49(Feed.getVal1(channel_id, FeedEnums.Name.attribute49));
        attribute.setAttribute50(Feed.getVal1(channel_id, FeedEnums.Name.attribute50));

        attributes.add(attribute);

        feedAttribute.setChannel_id(channel_id);
        feedAttribute.setAttributebeans(attributes);

        WsdlResponseBean result = service.attribute(feedAttribute);

        // TODO 处理返回结果
    }

    private void insertNewData(List<SuperFeedBcbgBean> bcbgBeans, BcbgStyleBean[] styleBeanArr) {

        int start = 0, end, total = bcbgBeans.size(), limit = 500;

        while (start < total) {

            end = start + limit;

            if (end > total) end = total;

            List<SuperFeedBcbgBean> subList = bcbgBeans.subList(start, end);

            int count = bcbgSuperFeedDao.insertWorkTables(subList);

            $info("分段插入 Feed %s 个", count);

            start = end;
        }

        List<BcbgStyleBean> styleBeans = Arrays.asList(styleBeanArr);

        // 对数据进行有效性过滤
        Map<Boolean, List<BcbgStyleBean>> styleBeansMap = styleBeans.stream()
                .collect(groupingBy(BcbgStyleBean::isValid, toList()));

        $info("完成 Style 的有效性过滤");

        styleBeans = styleBeansMap.get(true);

        $info("预计处理<有效>数据 %s 个", styleBeans.size());

        start = 0;
        total = styleBeans.size();

        while (start < total) {

            end = start + limit;

            if (end > total) end = total;

            List<BcbgStyleBean> subList = styleBeans.subList(start, end);

            try {
                int count = bcbgSuperFeedDao.insertStyles(subList);
                $info("分段插入 Style %s 个", count);
            } catch (Exception e) {
                $info(e.getMessage());
            }

            start = end;
        }

        // 对无效数据进行警告处理

        styleBeans = styleBeansMap.get(false);

        if (styleBeans == null || styleBeans.size() < 1) return;

        logIssue("发现部分 BCBG Style 文件的无效数据", styleBeans.size() + "个");
        $info("已警告<无效>数据 %s 个", styleBeans.size());
    }

    private void attributeListInsert(Channel channel){

        String channel_id = channel.getId();

        // 取出所有预定义的可选项属性
        List<String> attributeList = superFeedDao.selectSuperfeedAttributeList(channel_id, "1", "1");
        
        for (String attribute : attributeList) {
            // 从数据中取该属性的数据,并消除重复
            List<String> distinctValues = superFeedDao.selectAllAttribute(attribute, Feed.getVal1(channel_id, FeedEnums.Name.table_id));

            for (String value : distinctValues) {
                // 针对每个值进行检查.有则跳过,没有则插入
                String countByValue = superFeedDao.selectFeedAttribute(channel_id, attribute, value);

                if (!countByValue.equals("0")) continue;

                superFeedDao.insertFeedAttributeNew(channel_id, attribute, countByValue);
            }
        }
    }

    private void backupDataFile(File file, File styleFile) {

        String sBackupDir = Feed.getVal1(BCBG, Name.feed_backup_dir); // 备份的文件路径

        // 格式化处理路径,指向到独特的位置
        sBackupDir = String.format(sBackupDir, DateTimeUtil.getNow("yyyyMMdd"), DateTimeUtil.getNow("HHmmss"));

        File backupDir = new File(sBackupDir);

        if (!backupDir.exists() && !backupDir.mkdirs()) {
            logger.error("产品文件备份失败,目录创建失败");
            return;
        }

        // 移动文件
        if (!file.renameTo(new File(backupDir, file.getName()))) logger.error("产品文件备份失败");
        if (!styleFile.renameTo(new File(backupDir, styleFile.getName()))) logger.error("样式文件备份失败");
    }
}
