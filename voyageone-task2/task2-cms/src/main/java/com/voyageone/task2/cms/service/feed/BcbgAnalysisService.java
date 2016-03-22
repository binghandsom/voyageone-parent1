package com.voyageone.task2.cms.service.feed;

import com.google.gson.Gson;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.BcbgStyleBean;
import com.voyageone.task2.cms.bean.SuperFeedBcbgBean;
import com.voyageone.task2.cms.dao.SuperFeedDao;
import com.voyageone.task2.cms.dao.feed.BcbgSuperFeedDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.FeedEnums.Name;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.voyageone.task2.cms.service.feed.BcbgWsdlConstants.channel;
import static com.voyageone.task2.cms.service.feed.BcbgWsdlConstants.table_feed_full;
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

    @Autowired
    private BcbgWsdlAttribute attributeService;

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

        // 每次启动尝试初始化
        BcbgWsdlConstants.init();

        $info("开始处理 BCBG 数据");

        appendDataFromFile();

        // 处理下拉类属性
        attributeListInsert();

        // 使用接口提交
        boolean inserted = insertService.postNewProduct();
        boolean updated = updateService.postUpdatedProduct();

        if (inserted || updated) {
            attributeService.postAttributes();
        }
    }

    void appendDataFromFile() throws FileNotFoundException {
        File[] files = getDataFiles();

        File feedFile = files[0];
        File styleFile = files[1];

        // 同时木有...
        if (feedFile == null && styleFile == null)
            return;

        List<SuperFeedBcbgBean> bcbgBeans = null;
        BcbgStyleBean[] styleBeans = null;

        // 读取数据文件
        if (feedFile != null) {
            BcbgFeedFile bcbgFeedFile = BcbgFeedFile.read(feedFile);
            bcbgBeans = bcbgFeedFile.getMATERIALS();

            $info("已读取文件.获得 Feed %s 个", bcbgBeans.size());
        }

        if (styleFile != null) {
            FileReader styleFileReader = new FileReader(styleFile);
            styleBeans = new Gson().fromJson(styleFileReader, BcbgStyleBean[].class);

            $info("已读取文件.获得 Style %s 个", styleBeans.length);
        }

        // 插入数据库
        clearLastData();
        insertNewData(bcbgBeans, styleBeans);

        // 开始数据分析处理阶段
        transformer.new Context(channel, this).transform();
        $info("数据处理阶段结束");

        // 备份文件
        new Backup().fromData(feedFile, styleFile);
    }

    private File[] getDataFiles() {

        // 读取各种配置
        // 精简配置,减少独立配置,所以两个文件都配置在一个项目里
        String fileNames = Feed.getVal1(channel, Name.feed_ftp_filename); // 文件路径

        // 拆分成 feed 和 style
        String[] fileNameArr = fileNames.split(";");

        if (fileNameArr.length != 2) {
            $info("读取的文件路径错误,至少需要两个文件.退出任务");
            throw new BusinessException("BCBG 的配置 feed_ftp_filename 错误,至少两个文件.");
        }

        String sFeedXmlDir = fileNameArr[0];
        String sStyleJsonDir = fileNameArr[1];

        // 检查配置
        if (StringUtils.isAnyEmpty(sFeedXmlDir, sStyleJsonDir)) {
            $info("没读取到文件路径,退出任务");
            throw new BusinessException("BCBG 的配置 feed_ftp_filename 错误,路径为空.");
        }

        File feedFile = getDataFile(sFeedXmlDir, ".xml");

        File styleFile = getDataFile(sStyleJsonDir, ".json");

        return new File[] { feedFile, styleFile };
    }

    /**
     * 去 dir 下,按照 filter 过滤文件,返回第一个文件,并备份其他文件
     */
    private File getDataFile(String dir, String filter) {

        // 打开目录
        File feedFileDir = new File(dir);
        // 过滤文件
        File[] feedFiles = feedFileDir.listFiles(i -> i.getName().contains(filter));

        if (feedFiles == null || feedFiles.length < 1) {
            $info("没有找到 '%s' 文件", filter);
            return null;
        }

        // 排序文件
        List<File> feedFileList =  Arrays.asList(feedFiles).stream().sorted((f1, f2) -> f1.getName().compareTo(f2.getName())).collect(toList());

        // 取第一个作为目标文件,并从其中移除
        // 其他的等待后续

        return feedFileList.remove(0);
    }

    private void clearLastData() {
        // 删除所有
        bcbgSuperFeedDao.delete();
        bcbgSuperFeedDao.deleteStyles();
    }

    private void insertNewData(List<SuperFeedBcbgBean> bcbgBeans, BcbgStyleBean[] styleBeanArr) {

        int start, end, total, limit = 500;

        if (bcbgBeans != null) {
            start = 0;
            total = bcbgBeans.size();

            while (start < total) {

                end = start + limit;

                if (end > total) end = total;

                List<SuperFeedBcbgBean> subList = bcbgBeans.subList(start, end);

                int count = bcbgSuperFeedDao.insertWorkTables(subList);

                $info("分段插入 Feed %s 个", count);

                start = end;
            }
        }

        if (styleBeanArr == null) return;

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

        // logIssue("发现部分 BCBG Style 文件的无效数据", styleBeans.size() + "个");

        $info("发现 BCBG Style <无效>数据 %s 个", styleBeans.size());
    }

    private void attributeListInsert(){

        String channel_id = channel.getId();

        // 取出所有预定义的可选项属性
        List<String> attributeList = superFeedDao.selectSuperfeedAttributeList(channel_id, "1", "1");
        
        for (String attribute : attributeList) {
            // 从数据中取该属性的数据,并消除重复
            List<String> distinctValues = superFeedDao.selectAllAttribute(attribute, table_feed_full);

            for (String value : distinctValues) {
                // 针对每个值进行检查.有则跳过,没有则插入
                String countByValue = superFeedDao.selectFeedAttribute(channel_id, attribute, value);

                if (!countByValue.equals("0")) continue;

                superFeedDao.insertFeedAttributeNew(channel_id, attribute, countByValue);
            }
        }
    }

    private class Backup {

        private File backupDir;

        Backup() {

            String sBackupDir = Feed.getVal1(channel, Name.feed_backup_dir); // 备份的文件路径

            // 如果是模板,就尝试格式化
            if (sBackupDir.contains("%s"))
                sBackupDir = String.format(sBackupDir, DateTimeUtil.getNow("yyyyMMdd"), DateTimeUtil.getNow("HHmmss"));

            backupDir = new File(sBackupDir);

            if (!backupDir.exists() && !backupDir.mkdirs()) {
                $info("产品文件备份失败,目录创建失败");
                throw new BusinessException("产品文件备份失败,目录创建失败");
            }
        }

        protected void from(File file) {
            if (file == null) return;
            if (!file.renameTo(new File(backupDir, file.getName())))
                $info("文件备份失败 %s %s", file.getPath(), file.getName());
        }

        void fromData(File file, File styleFile) {
            from(file);
            from(styleFile);
        }
    }
}
