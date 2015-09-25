package com.voyageone.batch.cms.service;

import com.csvreader.CsvReader;
import javax.ws.rs.core.MediaType;

import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.bean.*;
import com.voyageone.batch.cms.utils.WebServiceUtil;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.cms.CmsConstants;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;

import java.io.*;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CmsGetSuperFeedService extends BaseTaskService {

    @Autowired
    SuperFeedDao superfeeddao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return "CmsGetSuperFeedJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

		logger.info( "channel_id="+ TaskControlEnums.Name.order_channel_id  );
		logger.info( "orderChannelIdList="+ orderChannelIdList.size()  );
		// 线程
        List<Runnable> threads = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

			logger.info( "channel_id=" +  orderChannelID );
            threads.add(new Runnable() {
                @Override
                public void run() {
                    new getSuperFeed(orderChannelID).doRun();
                }
            });
        }

        runWithThreadPool(threads, taskControlList);
    }
    
    /**
     * 按渠道进行产品导入
     */
    public class getSuperFeed  {
        private OrderChannelBean channel; 

        public getSuperFeed(String orderChannelId) {
            this.channel = ChannelConfigs.getChannel(orderChannelId); 
        }

        public void doRun() {
            logger.info(channel.getFull_name() + "产品导入开始");
			ProductsFeedInsert productsFeed = new ProductsFeedInsert();

			boolean isSuccess = true;
//			// 下载文件
//			try {
//				isSuccess = downloadFileForFtp(channel.getOrder_channel_id());
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.error("文件下载失败");
//				issueLog.log("cms 数据导入处理", "文件下载失败" +  e.getMessage(), ErrorType.BatchJob, SubSystem.CMS);
//			}

			// JEWELRY数据导入
			if(channel.getOrder_channel_id().equals(ChannelConfigEnums.Channel.JEWELRY.getId())){
				// JE产品文件读入
				List<SuperFeedJEBean> superfeedjebean = jeSuperFeedImport();

				if (superfeedjebean.size() > 0) {
					// JE产品信息插入
					isSuccess = insertSuperFeedJE(superfeedjebean);
				}else{
					isSuccess = false;
				}
			}

			// LOCONDO数据导入
			if(channel.getOrder_channel_id().equals(ChannelConfigEnums.Channel.LOCONDO.getId())){
				// LC产品文件读入
				List<SuperFeedLCBean> superfeedlcbean = lcSuperFeedImport();

				if (superfeedlcbean.size() > 0) {
					// JE产品信息插入
					isSuccess = insertSuperFeedLC(superfeedlcbean);
				}else{
					isSuccess = false;
				}
			}

			// 所有Attribute
			AttributeListInsert(channel.getOrder_channel_id());

			// 新旧数据判定
			if (isSuccess == true) {
				isSuccess = CheckSuperFeed(channel.getOrder_channel_id());
			}

			// 新旧数据处理
			if (isSuccess == true) {
				// 插入数据
				isSuccess =  NewProductFeed(channel.getOrder_channel_id());
				// 更新数据
				isSuccess =  OldProductFeed(channel.getOrder_channel_id());
			}

			if (isSuccess == true) {
				// 清除本地文件
				isSuccess = backupFeedFile(channel.getOrder_channel_id());
			}

            logger.info(channel.getFull_name() + "产品导入结束");
        }
	}

	/**
	 * JE产品文件读入
	 */
	public List<SuperFeedJEBean> jeSuperFeedImport(){
		logger.info("JE产品文件读入开始");

		List<SuperFeedJEBean> superfeed =  new ArrayList<SuperFeedJEBean>();

		CsvReader reader;
		try {
			reader = new CsvReader(new FileInputStream(Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(),FeedEnums.Name.feed_ftp_localpath) + "/"
					+ Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(),FeedEnums.Name.file_id)),'\t', Charset.forName(Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(),FeedEnums.Name.feed_ftp_file_coding)));

			// Head读入
			reader.readHeaders();
			String[] headers = reader.getHeaders();

			// Body读入
			while (reader.readRecord()) {
				SuperFeedJEBean superfeedjebean = new SuperFeedJEBean();

				int i = 0;
				superfeedjebean.setAuctionTitle(reader.get(i++));
				superfeedjebean.setInventoryNumber(reader.get(i++));
				superfeedjebean.setWeight(reader.get(i++));
				superfeedjebean.setISBN(reader.get(i++));
				superfeedjebean.setUPC(reader.get(i++));
				superfeedjebean.setEAN(reader.get(i++));
				superfeedjebean.setASIN(reader.get(i++));
				superfeedjebean.setMPN(reader.get(i++));
				superfeedjebean.setShortDescription(reader.get(i++));
				superfeedjebean.setDescription(reader.get(i++));
				superfeedjebean.setFlag(reader.get(i++));
				superfeedjebean.setFlagDescription(reader.get(i++));
				superfeedjebean.setBlocked(reader.get(i++));
				superfeedjebean.setBlockedComment(reader.get(i++));
				superfeedjebean.setManufacturer(reader.get(i++));
				superfeedjebean.setBrand(reader.get(i++));
				superfeedjebean.setCondition(reader.get(i++));
				superfeedjebean.setWarranty(reader.get(i++));
				superfeedjebean.setSellerCost(reader.get(i++));
				superfeedjebean.setProductMargin(reader.get(i++));
				superfeedjebean.setBuyItNowPrice(reader.get(i++));
				superfeedjebean.setRetailPrice(reader.get(i++));
				superfeedjebean.setPictureURLs(reader.get(i++));
				superfeedjebean.setTaxProductCode(reader.get(i++));
				superfeedjebean.setSupplierCode(reader.get(i++));
				superfeedjebean.setSupplierPO(reader.get(i++));
				superfeedjebean.setWarehouseLocation(reader.get(i++));
				superfeedjebean.setInventorySubtitle(reader.get(i++));
				superfeedjebean.setRelationshipName(reader.get(i++));
				superfeedjebean.setVariationParentSKU(reader.get(i++));
				superfeedjebean.setLabels(reader.get(i++));
				superfeedjebean.setAttribute1Name(reader.get(i++));
				superfeedjebean.setAttribute1Value(reader.get(i++));
				superfeedjebean.setAttribute2Name(reader.get(i++));
				superfeedjebean.setAttribute2Value(reader.get(i++));
				superfeedjebean.setAttribute3Name(reader.get(i++));
				superfeedjebean.setAttribute3Value(reader.get(i++));
				superfeedjebean.setAttribute4Name(reader.get(i++));
				superfeedjebean.setAttribute4Value(reader.get(i++));
				superfeedjebean.setAttribute5Name(reader.get(i++));
				superfeedjebean.setAttribute5Value(reader.get(i++));
				superfeedjebean.setAttribute6Name(reader.get(i++));
				superfeedjebean.setAttribute6Value(reader.get(i++));
				superfeedjebean.setAttribute7Name(reader.get(i++));
				superfeedjebean.setAttribute7Value(reader.get(i++));
				superfeedjebean.setAttribute8Name(reader.get(i++));
				superfeedjebean.setAttribute8Value(reader.get(i++));
				superfeedjebean.setAttribute9Name(reader.get(i++));
				superfeedjebean.setAttribute9Value(reader.get(i++));
				superfeedjebean.setAttribute10Name(reader.get(i++));
				superfeedjebean.setAttribute10Value(reader.get(i++));
				superfeedjebean.setAttribute11Name(reader.get(i++));
				superfeedjebean.setAttribute11Value(reader.get(i++));
				superfeedjebean.setAttribute12Name(reader.get(i++));
				superfeedjebean.setAttribute12Value(reader.get(i++));
				superfeedjebean.setAttribute13Name(reader.get(i++));
				superfeedjebean.setAttribute13Value(reader.get(i++));
				superfeedjebean.setAttribute14Name(reader.get(i++));
				superfeedjebean.setAttribute14Value(reader.get(i++));
				superfeedjebean.setAttribute15Name(reader.get(i++));
				superfeedjebean.setAttribute15Value(reader.get(i++));
				superfeedjebean.setAttribute16Name(reader.get(i++));
				superfeedjebean.setAttribute16Value(reader.get(i++));
				superfeedjebean.setAttribute17Name(reader.get(i++));
				superfeedjebean.setAttribute17Value(reader.get(i++));
				superfeedjebean.setAttribute18Name(reader.get(i++));
				superfeedjebean.setAttribute18Value(reader.get(i++));
				superfeedjebean.setAttribute19Name(reader.get(i++));
				superfeedjebean.setAttribute19Value(reader.get(i++));
				superfeedjebean.setAttribute20Name(reader.get(i++));
				superfeedjebean.setAttribute20Value(reader.get(i++));
				superfeedjebean.setAttribute21Name(reader.get(i++));
				superfeedjebean.setAttribute21Value(reader.get(i++));
				superfeedjebean.setAttribute22Name(reader.get(i++));
				superfeedjebean.setAttribute22Value(reader.get(i++));
				superfeedjebean.setAttribute23Name(reader.get(i++));
				superfeedjebean.setAttribute23Value(reader.get(i++));
				superfeedjebean.setAttribute24Name(reader.get(i++));
				superfeedjebean.setAttribute24Value(reader.get(i++));
				superfeedjebean.setAttribute25Name(reader.get(i++));
				superfeedjebean.setAttribute25Value(reader.get(i++));
				superfeedjebean.setAttribute26Name(reader.get(i++));
				superfeedjebean.setAttribute26Value(reader.get(i++));
				superfeedjebean.setAttribute27Name(reader.get(i++));
				superfeedjebean.setAttribute27Value(reader.get(i++));
				superfeedjebean.setAttribute28Name(reader.get(i++));
				superfeedjebean.setAttribute28Value(reader.get(i++));
				superfeedjebean.setAttribute29Name(reader.get(i++));
				superfeedjebean.setAttribute29Value(reader.get(i++));
				superfeedjebean.setAttribute30Name(reader.get(i++));
				superfeedjebean.setAttribute30Value(reader.get(i++));
				superfeedjebean.setAttribute31Name(reader.get(i++));
				superfeedjebean.setAttribute31Value(reader.get(i++));
				superfeedjebean.setAttribute32Name(reader.get(i++));
				superfeedjebean.setAttribute32Value(reader.get(i++));
				superfeedjebean.setAttribute33Name(reader.get(i++));
				superfeedjebean.setAttribute33Value(reader.get(i++));
				superfeedjebean.setAttribute34Name(reader.get(i++));
				superfeedjebean.setAttribute34Value(reader.get(i++));
				superfeedjebean.setAttribute35Name(reader.get(i++));
				superfeedjebean.setAttribute35Value(reader.get(i++));
				superfeedjebean.setAttribute36Name(reader.get(i++));
				superfeedjebean.setAttribute36Value(reader.get(i++));
				superfeedjebean.setAttribute37Name(reader.get(i++));
				superfeedjebean.setAttribute37Value(reader.get(i++));
				superfeedjebean.setAttribute38Name(reader.get(i++));
				superfeedjebean.setAttribute38Value(reader.get(i++));
				superfeedjebean.setAttribute39Name(reader.get(i++));
				superfeedjebean.setAttribute39Value(reader.get(i++));
				superfeedjebean.setAttribute40Name(reader.get(i++));
				superfeedjebean.setAttribute40Value(reader.get(i++));
				superfeedjebean.setAttribute41Name(reader.get(i++));
				superfeedjebean.setAttribute41Value(reader.get(i++));
				superfeedjebean.setAttribute42Name(reader.get(i++));
				superfeedjebean.setAttribute42Value(reader.get(i++));
				superfeedjebean.setAttribute43Name(reader.get(i++));
				superfeedjebean.setAttribute43Value(reader.get(i++));
				superfeedjebean.setAttribute44Name(reader.get(i++));
				superfeedjebean.setAttribute44Value(reader.get(i++));
				superfeedjebean.setAttribute47Name(reader.get(i++));
				superfeedjebean.setAttribute47Value(reader.get(i++));
				superfeedjebean.setAttribute48Name(reader.get(i++));
				superfeedjebean.setAttribute48Value(reader.get(i++));
				superfeedjebean.setAttribute49Name(reader.get(i++));
				superfeedjebean.setAttribute49Value(reader.get(i++));
				superfeedjebean.setAttribute50Name(reader.get(i++));
				superfeedjebean.setAttribute50Value(reader.get(i++));
				superfeedjebean.setAttribute51Name(reader.get(i++));
				superfeedjebean.setAttribute51Value(reader.get(i++));
				superfeedjebean.setAttribute52Name(reader.get(i++));
				superfeedjebean.setAttribute52Value(reader.get(i++));
				superfeedjebean.setAttribute53Name(reader.get(i++));
				superfeedjebean.setAttribute53Value(reader.get(i++));
				superfeedjebean.setAttribute54Name(reader.get(i++));
				superfeedjebean.setAttribute54Value(reader.get(i++));
				superfeedjebean.setAttribute55Name(reader.get(i++));
				superfeedjebean.setAttribute55Value(reader.get(i++));
				superfeedjebean.setAttribute56Name(reader.get(i++));
				superfeedjebean.setAttribute56Value(reader.get(i++));
				superfeedjebean.setAttribute57Name(reader.get(i++));
				superfeedjebean.setAttribute57Value(reader.get(i++));
				superfeedjebean.setAttribute58Name(reader.get(i++));
				superfeedjebean.setAttribute58Value(reader.get(i++));
				superfeedjebean.setAttribute59Name(reader.get(i++));
				superfeedjebean.setAttribute59Value(reader.get(i++));
				superfeedjebean.setAttribute60Name(reader.get(i++));
				superfeedjebean.setAttribute60Value(reader.get(i++));
				superfeedjebean.setAttribute61Name(reader.get(i++));
				superfeedjebean.setAttribute61Value(reader.get(i++));
				superfeedjebean.setAttribute62Name(reader.get(i++));
				superfeedjebean.setAttribute62Value(reader.get(i++));
				superfeedjebean.setAttribute63Name(reader.get(i++));
				superfeedjebean.setAttribute63Value(reader.get(i++));
				superfeedjebean.setAttribute64Name(reader.get(i++));
				superfeedjebean.setAttribute64Value(reader.get(i++));
				superfeedjebean.setAttribute65Name(reader.get(i++));
				superfeedjebean.setAttribute65Value(reader.get(i++));
				superfeedjebean.setAttribute66Name(reader.get(i++));
				superfeedjebean.setAttribute66Value(reader.get(i++));
				superfeedjebean.setAttribute67Name(reader.get(i++));
				superfeedjebean.setAttribute67Value(reader.get(i++));
				superfeedjebean.setAttribute68Name(reader.get(i++));
				superfeedjebean.setAttribute68Value(reader.get(i++));
				superfeedjebean.setAttribute69Name(reader.get(i++));
				superfeedjebean.setAttribute69Value(reader.get(i++));
				superfeedjebean.setAttribute70Name(reader.get(i++));
				superfeedjebean.setAttribute70Value(reader.get(i++));
				superfeedjebean.setAttribute71Name(reader.get(i++));
				superfeedjebean.setAttribute71Value(reader.get(i++));
				superfeedjebean.setAttribute72Name(reader.get(i++));
				superfeedjebean.setAttribute72Value(reader.get(i++));
				superfeedjebean.setAttribute73Name(reader.get(i++));
				superfeedjebean.setAttribute73Value(reader.get(i++));
				superfeedjebean.setAttribute74Name(reader.get(i++));
				superfeedjebean.setAttribute74Value(reader.get(i++));
				superfeedjebean.setAttribute75Name(reader.get(i++));
				superfeedjebean.setAttribute75Value(reader.get(i++));
				superfeedjebean.setAttribute76Name(reader.get(i++));
				superfeedjebean.setAttribute76Value(reader.get(i++));
				superfeedjebean.setAttribute77Name(reader.get(i++));
				superfeedjebean.setAttribute77Value(reader.get(i++));
				superfeedjebean.setAttribute78Name(reader.get(i++));
				superfeedjebean.setAttribute78Value(reader.get(i++));
				superfeedjebean.setAttribute79Name(reader.get(i++));
				superfeedjebean.setAttribute79Value(reader.get(i++));
				superfeedjebean.setAttribute80Name(reader.get(i++));
				superfeedjebean.setAttribute80Value(reader.get(i++));
				superfeedjebean.setAttribute81Name(reader.get(i++));
				superfeedjebean.setAttribute81Value(reader.get(i++));
				superfeedjebean.setAttribute82Name(reader.get(i++));
				superfeedjebean.setAttribute82Value(reader.get(i++));
				superfeedjebean.setAttribute83Name(reader.get(i++));
				superfeedjebean.setAttribute83Value(reader.get(i++));
				superfeedjebean.setAttribute84Name(reader.get(i++));
				superfeedjebean.setAttribute84Value(reader.get(i++));
				superfeedjebean.setAttribute85Name(reader.get(i++));
				superfeedjebean.setAttribute85Value(reader.get(i++));
				superfeedjebean.setAttribute86Name(reader.get(i++));
				superfeedjebean.setAttribute86Value(reader.get(i++));
				superfeedjebean.setAttribute87Name(reader.get(i++));
				superfeedjebean.setAttribute87Value(reader.get(i++));
				superfeedjebean.setAttribute88Name(reader.get(i++));
				superfeedjebean.setAttribute88Value(reader.get(i++));
				superfeedjebean.setAttribute89Name(reader.get(i++));
				superfeedjebean.setAttribute89Value(reader.get(i++));
				superfeedjebean.setAttribute90Name(reader.get(i++));
				superfeedjebean.setAttribute90Value(reader.get(i++));
				superfeedjebean.setAttribute91Name(reader.get(i++));
				superfeedjebean.setAttribute91Value(reader.get(i++));
				superfeedjebean.setAttribute92Name(reader.get(i++));
				superfeedjebean.setAttribute92Value(reader.get(i++));
				superfeedjebean.setAttribute93Name(reader.get(i++));
				superfeedjebean.setAttribute93Value(reader.get(i++));
				superfeedjebean.setAttribute94Name(reader.get(i++));
				superfeedjebean.setAttribute94Value(reader.get(i++));
				superfeedjebean.setHarmonizedCode(reader.get(i++));
				superfeedjebean.setHeight(reader.get(i++));
				superfeedjebean.setLength(reader.get(i++));
				superfeedjebean.setWidth(reader.get(i++));
				superfeedjebean.setDCCode(reader.get(i++));
				superfeedjebean.setClassification(reader.get(i++));
				superfeedjebean.setAttribute95Name(reader.get(i++));
				superfeedjebean.setAttribute95Value(reader.get(i++));
				superfeedjebean.setAttribute96Name(reader.get(i++));
				superfeedjebean.setAttribute96Value(reader.get(i++));

				superfeed.add(superfeedjebean);
			}

			reader.close();
			logger.info("JE产品文件读入完成");
		} catch (Exception ex) {
			logger.info("JE产品文件读入失败");
			issueLog.log("cms 数据导入处理", "JE产品文件读入失败 " +  ex.getMessage(), ErrorType.BatchJob, SubSystem.CMS);
		}
		return superfeed;
	}

	/**
	 * LC产品文件读入
	 */
	public List<SuperFeedLCBean> lcSuperFeedImport(){
		logger.info("LC产品文件读入开始");

		List<SuperFeedLCBean> superfeed =  new ArrayList<SuperFeedLCBean>();

		CsvReader reader;
		try {
			reader = new CsvReader(new FileInputStream(Feed.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(),FeedEnums.Name.feed_ftp_localpath) + "/"
					+ Feed.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(),FeedEnums.Name.file_id)),',', Charset.forName(Feed.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(),FeedEnums.Name.feed_ftp_file_coding)));
			// Head读入
			reader.readHeaders();
			String[] headers = reader.getHeaders();

			// Body读入
			while (reader.readRecord()) {
				SuperFeedLCBean superfeedlcbean = new SuperFeedLCBean();

				int i = 0;
				superfeedlcbean.setPath(reader.get(i++));
				superfeedlcbean.setName(reader.get(i++));
				superfeedlcbean.setCode(reader.get(i++));
				superfeedlcbean.setSub_code(reader.get(i++));
				superfeedlcbean.setOriginal_price(reader.get(i++));
				superfeedlcbean.setPrice(reader.get(i++));
				superfeedlcbean.setSale_price(reader.get(i++));
				superfeedlcbean.setOptions(reader.get(i++));
				superfeedlcbean.setHeadline(reader.get(i++));
				superfeedlcbean.setCaption(reader.get(i++));
				superfeedlcbean.setAbstract(reader.get(i++));
				superfeedlcbean.setExplanation(reader.get(i++));
				superfeedlcbean.setAdditional1(reader.get(i++));
				superfeedlcbean.setAdditional2(reader.get(i++));
				superfeedlcbean.setAdditional3(reader.get(i++));
				superfeedlcbean.setRelevant_links(reader.get(i++));
				superfeedlcbean.setShip_weight(reader.get(i++));
				superfeedlcbean.setTaxable(reader.get(i++));
				superfeedlcbean.setRelease_date(reader.get(i++));
				superfeedlcbean.setTemporary_point_term(reader.get(i++));
				superfeedlcbean.setPoint_code(reader.get(i++));
				superfeedlcbean.setMeta_key(reader.get(i++));
				superfeedlcbean.setMeta_desc(reader.get(i++));
				superfeedlcbean.setDisplay(reader.get(i++));
				superfeedlcbean.setTemplate(reader.get(i++));
				superfeedlcbean.setSale_period_start(reader.get(i++));
				superfeedlcbean.setSale_period_end(reader.get(i++));
				superfeedlcbean.setSale_limit(reader.get(i++));
				superfeedlcbean.setSp_code(reader.get(i++));
				superfeedlcbean.setBrand_code(reader.get(i++));
				superfeedlcbean.setYahoo_product_code(reader.get(i++));
				superfeedlcbean.setProduct_code(reader.get(i++));
				superfeedlcbean.setJan(reader.get(i++));
				superfeedlcbean.setDelivery(reader.get(i++));
				superfeedlcbean.setAstk_code(reader.get(i++));
				superfeedlcbean.setCondition(reader.get(i++));
				superfeedlcbean.setProduct_category(reader.get(i++));
				superfeedlcbean.setSpec1(reader.get(i++));
				superfeedlcbean.setSpec2(reader.get(i++));
				superfeedlcbean.setSpec3(reader.get(i++));
				superfeedlcbean.setSpec4(reader.get(i++));
				superfeedlcbean.setSpec5(reader.get(i++));
				superfeedlcbean.setSort(reader.get(i++));
				superfeedlcbean.setSp_additional(reader.get(i++));

				superfeed.add(superfeedlcbean);
			}

			reader.close();
			logger.info("LC产品文件读入完成");
		} catch (Exception ex) {
			logger.info("LC产品文件读入失败");
			issueLog.log("cms 数据导入处理", "LC产品文件读入失败 " +  ex.getMessage(), ErrorType.BatchJob, SubSystem.CMS);
		}
		return superfeed;
	}

	/**
	 * JE产品信息插入
	 *
	 * @param  superfeedlist
	 * @return isSuccess
	 */
	public boolean insertSuperFeedJE(List<SuperFeedJEBean> superfeedlist ){
		boolean isSuccess = true;
		logger.info("JE产品信息插入开始");

		// 清表
		if (superfeeddao.deleteTableInfo(Feed.getVal1(ChannelConfigEnums.Channel.JEWELRY.getId(),FeedEnums.Name.table_id)) >= 0) {
			for (int i = 0; i < superfeedlist.size(); i++) {

				SuperFeedJEBean superfeed = superfeedlist.get(i);

				if (superfeeddao.insertSuperfeedJEInfo(superfeed)<=0 ){
					logger.info("JE产品信息插入失败 InventoryNumber = " + superfeed.getInventoryNumber());
				};
			}
		}else{
			isSuccess = false;
			logger.info("JE产品信息插入失败 清TMP表失败");
		}
		logger.info("JE产品信息插入完成");
		return isSuccess;
	}

	/**
	 * LC产品信息插入
	 *
	 * @param  superfeedlist
	 * @return isSuccess
	 */
	public boolean insertSuperFeedLC(List<SuperFeedLCBean> superfeedlist ){
		boolean isSuccess = true;
		logger.info("LC产品信息插入开始");

		// 清表
		if (superfeeddao.deleteTableInfo(Feed.getVal1(ChannelConfigEnums.Channel.LOCONDO.getId(),FeedEnums.Name.table_id)) >= 0) {
			for (int i = 0; i < superfeedlist.size(); i++) {

				SuperFeedLCBean superfeed = superfeedlist.get(i);

				if (superfeeddao.insertSuperfeedLCInfo(superfeed)<=0 ){
					logger.info("LC产品信息插入失败 code = " + superfeed.getCode());
				};
			}
		}else{
			isSuccess = false;
			logger.info("LC产品信息插入失败 清TMP表失败");
		}
		logger.info("LC产品信息插入完成");
		return isSuccess;
	}

	/**
	 * CheckSuperFeed 产品信息
	 * 异常数据清除 新数据，更新数据划分。
	 *
	 * @param channel_id
	 * @return isSuccessS
	 */
	public boolean CheckSuperFeed(String channel_id){
		boolean isSuccess = true;
		logger.info("异常数据清除开始");

		if (superfeeddao.deleteData(Feed.getVal1(channel_id,FeedEnums.Name.feed_delete_category_err_sql)) < 0){
			logger.error("异常数据清除失败");
			issueLog.log("cms 数据导入处理", "异常数据清除失败" , ErrorType.BatchJob, SubSystem.CMS);
			isSuccess = false;
		}

		// 取得model异常数据
		List<String> err_modellist = superfeeddao.selectErrData(Feed.getVal1(channel_id,FeedEnums.Name.feed_delete_model_sql));
		// 取得product异常数据
		List<String> err_productlist = superfeeddao.selectErrData(Feed.getVal1(channel_id,FeedEnums.Name.feed_delete_product_sql));
		// 取得有model无product异常数据
		List<String> err_model_noproductlist = superfeeddao.selectErrData(Feed.getVal1(channel_id,FeedEnums.Name.feed_delete_model_no_product_sql));
		// 取得有prouduct无model异常数据
		List<String> err_product_nomodellist = superfeeddao.selectErrData(Feed.getVal1(channel_id,FeedEnums.Name.feed_delete_product_no_model_sql));

		// 删除异常数据
		String err_data="";
		for (int i = 0; i < err_modellist.size(); i++) {
			err_data = err_data + " '" +err_modellist.get(i) + "',";
		}
		for (int i = 0; i < err_productlist.size(); i++) {
			err_data = err_data + " '" +err_productlist.get(i) + "',";
		}
		for (int i = 0; i < err_model_noproductlist.size(); i++) {
			err_data = err_data + " '" +err_model_noproductlist.get(i) + "',";
		}
		for (int i = 0; i < err_product_nomodellist.size(); i++) {
			err_data = err_data + " '" +err_product_nomodellist.get(i) + "',";
		}

		// 存在异常数据
		if (err_data.length() > 0){
			// 去掉最后一个“，”
			err_data = Feed.getVal1(channel_id,FeedEnums.Name.feed_item_key) + " in (" + err_data.substring(0,err_data.lastIndexOf(",")) +")";

			if (superfeeddao.deleteErrData(Feed.getVal1(channel_id,FeedEnums.Name.table_id), err_data) <= 0 ){
				//异常数据清除失败
				logger.info("异常数据清除失败");
				issueLog.log("cms 数据导入处理", "异常数据清除失败", ErrorType.BatchJob, SubSystem.CMS);
				isSuccess = false;
			}
		}
		logger.info("异常数据清除结束");

		// UpdateFlag : 1:插入 2:更新 3:两者都有 0:不处理
		logger.info("数据判定开始");
		if (isSuccess == true){
			// insert data
			String str_insert_data;
			List<String> insert_data = superfeeddao.inertSuperfeedInsertData(Feed.getVal1(channel_id, FeedEnums.Name.category_column),
					Feed.getVal1(channel_id, FeedEnums.Name.feed_model_key),
					Feed.getVal1(channel_id, FeedEnums.Name.feed_code_key),
					Feed.getVal1(channel_id, FeedEnums.Name.table_id),
					Feed.getVal1(channel_id, FeedEnums.Name.table_id)+"_full");

			// update data
			String str_update_data;
			List<String> update_data = superfeeddao.inertSuperfeedUpdateData(Feed.getVal1(channel_id, FeedEnums.Name.feed_code_key),
					Feed.getVal1(channel_id, FeedEnums.Name.table_id),
					Feed.getVal1(channel_id, FeedEnums.Name.table_id) + "_full");

			// 新数据_code
			String str_code_insert = "";
			// 更新数据_code
			String str_code_update = "";
			// 插入数据_code 存在
			if (insert_data.size() > 0){
				for (int i = 0; i < insert_data.size(); i++) {
					str_code_insert = str_code_insert + "'"  + insert_data.get(i) + "',";
				}
				// 去掉最后一个“，”
				str_code_insert = str_code_insert.substring(0, str_code_insert.lastIndexOf(",")) ;

				// 插入数据 更新UpdateFlag :1
				int reslut_insert = superfeeddao.updateInsertData(Feed.getVal1(channel_id,FeedEnums.Name.table_id), Feed.getVal1(channel_id,FeedEnums.Name.feed_code_key), str_code_insert);
				if (reslut_insert<=0){
					issueLog.log("cms 数据导入处理", "更新UpdateFlag :1", ErrorType.BatchJob, SubSystem.CMS);
				}

				// 插入数据 补足没有model的数据
				int reslut_insertmodel = superfeeddao.updateInsertModelData(Feed.getVal1(channel_id,FeedEnums.Name.table_id), Feed.getVal1(channel_id,FeedEnums.Name.feed_model_key),
						Feed.getVal1(channel_id,FeedEnums.Name.feed_code_key), str_code_insert, Feed.getVal1(channel_id,FeedEnums.Name.feed_model_keyword));
				if (reslut_insertmodel<0){
					issueLog.log("cms 数据导入处理", "更新UpdateFlag :1 model", ErrorType.BatchJob, SubSystem.CMS);
				}
			}

			// 更新数据_code 存在
			if (update_data.size() > 0){
				for (int i = 0; i < update_data.size(); i++) {
					str_code_update = str_code_update + "'"  + update_data.get(i) + "',";
				}
				// 去掉最后一个“，”
				str_code_update = str_code_update.substring(0, str_code_update.lastIndexOf(",")) ;

				// 更新数据 更新UpdateFlag :2，3
				int reslut_update =superfeeddao.updateUpdateData(Feed.getVal1(channel_id,FeedEnums.Name.table_id), Feed.getVal1(channel_id,FeedEnums.Name.feed_code_key), str_code_update);
				if (reslut_update <= 0){
					issueLog.log("cms 数据导入处理", "更新UpdateFlag :2，3", ErrorType.BatchJob, SubSystem.CMS);
				}
			}

//
//			// --------------------------------------------------------
//
//			// 预处理数据取得
//			List <ProductBean> productbean_update = createProduct(channel_id,"","","","",Feed.getVal1(channel_id,FeedEnums.Name.table_id));
//
//			// 新数据_code
//			String str_code_insert = "";
//			// 更新数据_code
//			String str_code_update = "";
//
//			// 取得预更新数据code
//			for (int i = 0; i < productbean_update.size(); i++) {
//
//				// 预更新数据_full 取得 单条取得
//				List <ProductBean>  productbean_update_full = createProduct(channel_id,"","",productbean_update.get(i).getP_code(),"",Feed.getVal1(channel_id,FeedEnums.Name.table_id) + "_full");
//
//				if (productbean_update_full.size() > 0) {
//
//					// Url_key 不一样 那就新数据看看能不能处理
//					if (!productbean_update.get(i).getUrl_key().equals(productbean_update_full.get(0).getUrl_key())){
//						// 插入数据_code
//						str_code_insert = str_code_insert + "'"  + productbean_update.get(i).getP_code() + "',";
//
//						// 更新数据_code
//						str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
//					}
//					else{
//						// image
//						String image_url= "";
//						for (int j = 0; j < productbean_update.get(i).getImages().size(); j++) {
//							image_url = image_url +  productbean_update.get(i).getImages().get(j).getImage_url() + ";";
//						}
//
//						// image_full
//						String image_url_full= "";
//						for (int j = 0; j < productbean_update_full.get(0).getImages().size(); j++) {
//							image_url_full = image_url_full + productbean_update_full.get(0).getImages().get(j).getImage_url() + ";";
//						}
//
//						// msrp、price、cn_price、long_description、image_url 不一样
//						if ((!productbean_update.get(i).getP_msrp().equals(productbean_update_full.get(0).getP_msrp())
//								|| !productbean_update.get(i).getPs_price().equals(productbean_update_full.get(0).getPs_price())
//								|| !productbean_update.get(i).getCps_cn_price_rmb().equals(productbean_update_full.get(0).getCps_cn_price_rmb())
//								|| !productbean_update.get(i).getPe_long_description().equals(productbean_update_full.get(0).getPe_long_description())
//								|| !image_url.equals(image_url_full))){
//
//							// 更新数据_code
//							str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
//						}
//
////					if (!productbean_update.get(i).getP_msrp().equals(productbean_update_full.get(0).getP_msrp()) ){
////
////						// 更新数据_code
////						str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
////					}
////
////					if (!productbean_update.get(i).getPs_price().equals(productbean_update_full.get(0).getPs_price().toString())){
////
////						// 更新数据_code
////						str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
////					}
////
////					if (!productbean_update.get(i).getCps_cn_price_rmb().equals(productbean_update_full.get(0).getCps_cn_price_rmb())){
////
////						// 更新数据_code
////						str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
////					}
////
////					if (!productbean_update.get(i).getPe_long_description().equals(productbean_update_full.get(0).getPe_long_description())){
////
////						// 更新数据_code
////						str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
////					}
////					if (!image_url.equals(image_url_full)){
////
////						// 更新数据_code
////						str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
////					}
////					if (!productbean_update.get(i).getImages().equals(productbean_update_full.get(0).getImages())){
////
////						// 更新数据_code
////						str_code_update = str_code_update + "'"  + productbean_update.get(i).getP_code() + "',";
////					}
//
//						// item_sku
//						String item_sku= "";
//						for (int j = 0; j < productbean_update.get(i).getItembeans().size(); j++) {
//							item_sku = item_sku +  productbean_update.get(i).getItembeans().get(j).getI_sku() + ";";
//						}
//
//						// item_sku_full
//						String item_sku_full= "";
//						for (int j = 0; j < productbean_update_full.get(0).getItembeans().size(); j++) {
//							item_sku_full = item_sku_full +  productbean_update_full.get(0).getItembeans().get(j).getI_sku() + ";";
//						}
//
//						// itmebean 不一样
//						if (!item_sku.equals(item_sku_full)){
//							// 插入数据_code
//							str_code_insert = str_code_insert + "'"  + productbean_update.get(i).getP_code() + "',";
//						}
//					}
//				}else{
//					// 插入数据_code
//					str_code_insert = str_code_insert + "'"  + productbean_update.get(i).getP_code() + "',";
//				}
//			}
//
//			// 插入数据_code 存在
//			if (str_code_insert.length() > 0){
//				// 去掉最后一个“，”
////				int a = str_code_insert.lastIndexOf(",");
//				str_code_insert = str_code_insert.substring(0, str_code_insert.lastIndexOf(",")) ;
//
//				// 插入数据 更新UpdateFlag :1
//				int reslut_insert = superfeeddao.updateInsertData(Feed.getVal1(channel_id,FeedEnums.Name.table_id), Feed.getVal1(channel_id,FeedEnums.Name.feed_code_key), str_code_insert);
//				if (reslut_insert<=0){
//					issueLog.log("cms 数据导入处理", "更新UpdateFlag :1", ErrorType.BatchJob, SubSystem.CMS);
//				}
//
//				// 插入数据 补足没有model的数据
//				int reslut_insertmodel = superfeeddao.updateInsertModelData(Feed.getVal1(channel_id,FeedEnums.Name.table_id), Feed.getVal1(channel_id,FeedEnums.Name.feed_model_key),
//						Feed.getVal1(channel_id,FeedEnums.Name.feed_code_key), str_code_insert, Feed.getVal1(channel_id,FeedEnums.Name.feed_model_keyword));
//				if (reslut_insertmodel<0){
//					issueLog.log("cms 数据导入处理", "更新UpdateFlag :1 model", ErrorType.BatchJob, SubSystem.CMS);
//				}
//			}
//
//			// 更新数据_code 存在
//			if (str_code_update.length() > 0){
//				// 去掉最后一个“，”
//				str_code_update = str_code_update.substring(0, str_code_update.lastIndexOf(",")) ;
//
//				// 更新数据 更新UpdateFlag :2，3
//				int reslut_update =superfeeddao.updateUpdateData(Feed.getVal1(channel_id,FeedEnums.Name.table_id), Feed.getVal1(channel_id,FeedEnums.Name.feed_code_key), str_code_update);
//				if (reslut_update <= 0){
//					issueLog.log("cms 数据导入处理", "更新UpdateFlag :2，3", ErrorType.BatchJob, SubSystem.CMS);
//				}
//			}
		}
		logger.info("数据判定结束");

		return isSuccess;
	}

	/**
	 * NewProductFeed 新产品处理
	 *
	 * @param channel_id
	 * @return isSuccess
	 */
	public boolean  NewProductFeed(String channel_id){
		boolean isSuccess = true;
		logger.info("新产品处理开始");

		ProductsFeedInsert productsFeed = new ProductsFeedInsert();
		// 只处理新数据
		String keyword = " and UpdateFlag in ('1','3')";

		// 取得所有Superfeed category划分
		List<String> superfeedjebeanlist = superfeeddao.selectSuperfeedCategory(Feed.getVal1(channel_id,FeedEnums.Name.category_column) ,Feed.getVal1(channel_id,FeedEnums.Name.table_id),keyword);

 		for (int i = 0; i < superfeedjebeanlist.size(); i++) {
			// CategoryBean ModelBean ProductBean 以Category单位
			List<CategoryBean> categoryBeans = new ArrayList();

			// category
			String[] strarray=superfeedjebeanlist.get(i).split(Feed.getVal1(channel_id,FeedEnums.Name.category_split));
			for (int j = 0; j < strarray.length; j++) {
				CategoryBean  categoryBean = new CategoryBean();

				// 父层
				if (j==0){
					categoryBean.setC_name(formaturl(strarray[j],"0" ,Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
					categoryBean.setC_header_title(formaturl(strarray[j],"0",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
					// 全部小写,且空格用-替代
					categoryBean.setUrl_key(formaturl(strarray[j],"1",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));

				}else{
					// 子层
					categoryBean.setC_name(formaturl(strarray[j],"0",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
					categoryBean.setC_header_title(formaturl(strarray[j],"0",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
					// 全部小写,且空格用-替代
					categoryBean.setUrl_key(categoryBeans.get(categoryBeans.size() - 1).getUrl_key() + '-' + formaturl(strarray[j],"1",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
					categoryBean.setParent_url_key(categoryBeans.get(categoryBeans.size() - 1).getUrl_key());
				}

				categoryBean.setC_is_enable_filter("1");
				categoryBean.setC_is_visible_on_menu("0");
				categoryBean.setC_is_published("0");
				categoryBean.setC_is_effective("1");

				List<ModelBean> modelBeans = new ArrayList();
				// 最终category
				if (j == strarray.length - 1){
					// model 取得
					modelBeans = createModel(channel_id,superfeedjebeanlist.get(i), keyword);
					for (int k  = 0; k < modelBeans.size(); k++) {
						ModelBean modelbean = modelBeans.get(k);

						List<ProductBean> productBeans = new ArrayList();
						// product 取得
						productBeans = createProduct(channel_id,superfeedjebeanlist.get(i),modelbean.getM_model(),"",keyword, Feed.getVal1(channel_id,FeedEnums.Name.table_id));
						// product 设定
						modelbean.setProductbeans(productBeans);
						modelBeans.set(k, modelbean);

					}
					if (modelBeans.size() > 0){
						// model 设定
						categoryBean.setModelbeans(modelBeans);
					}
				}
				categoryBeans.add(categoryBean);
			}

			productsFeed.setChannel_id(channel_id);
			productsFeed.setCategorybeans(categoryBeans);

			//新产品Attribute处理
			if (!AttributeInsert(channel_id ,keyword, superfeedjebeanlist.get(i) ,"")){
				logger.info("新产品Attribute处理异常");
				continue;
			};

			logger.info("新产品category= " + superfeedjebeanlist.get(i).toString());

			// post web servies
			WsdlResponseBean  wsdlResponseBean= jsonBeanOutInsert(productsFeed);
			ProductFeedResponseBean productFeedResponseBean = (ProductFeedResponseBean)  JsonUtil.jsonToBean(JsonUtil.getJsonString(wsdlResponseBean.getResultInfo()),ProductFeedResponseBean.class);

			if (wsdlResponseBean.getResult().equals("OK") && productFeedResponseBean.getSuccess().size() > 0){
				List <String> modelFailList = new  ArrayList();
				List <String> productFailList = new  ArrayList();

				// 出错统计
				List<ProductFeedDetailBean> productFeedDetailBeans = productFeedResponseBean.getFailure();
				for (int b  = 0; b < productFeedDetailBeans.size(); b ++) {
					//  处理类型 1:category 无; 2:model
					if (productFeedDetailBeans.get(b).getBeanType() == 2){
						modelFailList.add(productFeedDetailBeans.get(b).getDealObject().getModel());
					}
					//  处理类型 3:product; 4:item
					if (productFeedDetailBeans.get(b).getBeanType() == 3 || productFeedDetailBeans.get(b).getBeanType() == 4){
						productFailList.add(productFeedDetailBeans.get(b).getDealObject().getCode());
					}
				}

				// 插入ZZ_Work_Superfeed_Full
				if (!SuperfeedFullInsert(superfeedjebeanlist.get(i).toString() ,channel_id, modelFailList, productFailList)){
					logger.error("插入ZZ_Work_Superfeed_Full 失败");
				}
			}

			if (wsdlResponseBean.getResult().equals("NG")){
				issueLog.log("cms 数据导入处理", "新产品推送失败：Message=" + wsdlResponseBean.getMessage(),
						ErrorType.BatchJob, SubSystem.CMS);
			}
		}

		logger.info("新产品处理完成");
		return isSuccess;
	}

	/**
	 * Attribute 属性插入、更新处理
	 *
	 * @param channel_id keyword
	 * @return isSuccess
	 */
	public boolean AttributeInsert(String channel_id, String keyword, String category, String product){
		boolean isSuccess = true;
		logger.info("产品Attribute处理开始");
		AttributeBean attributebean_param = new AttributeBean();

		attributebean_param.setCategory_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_category_url_key));
		attributebean_param.setModel_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_model_url_key));
		attributebean_param.setProduct_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_url_key));
		attributebean_param.setAttribute1(Feed.getVal1(channel_id, FeedEnums.Name.attribute1));
		attributebean_param.setAttribute2(Feed.getVal1(channel_id, FeedEnums.Name.attribute2));
		attributebean_param.setAttribute3(Feed.getVal1(channel_id, FeedEnums.Name.attribute3));
		attributebean_param.setAttribute4(Feed.getVal1(channel_id, FeedEnums.Name.attribute4));
		attributebean_param.setAttribute5(Feed.getVal1(channel_id, FeedEnums.Name.attribute5));
		attributebean_param.setAttribute6(Feed.getVal1(channel_id, FeedEnums.Name.attribute6));
		attributebean_param.setAttribute7(Feed.getVal1(channel_id, FeedEnums.Name.attribute7));
		attributebean_param.setAttribute8(Feed.getVal1(channel_id, FeedEnums.Name.attribute8));
		attributebean_param.setAttribute9(Feed.getVal1(channel_id, FeedEnums.Name.attribute9));
		attributebean_param.setAttribute10(Feed.getVal1(channel_id, FeedEnums.Name.attribute10));
		attributebean_param.setAttribute11(Feed.getVal1(channel_id, FeedEnums.Name.attribute11));
		attributebean_param.setAttribute12(Feed.getVal1(channel_id, FeedEnums.Name.attribute12));
		attributebean_param.setAttribute13(Feed.getVal1(channel_id, FeedEnums.Name.attribute13));
		attributebean_param.setAttribute14(Feed.getVal1(channel_id, FeedEnums.Name.attribute14));
		attributebean_param.setAttribute15(Feed.getVal1(channel_id, FeedEnums.Name.attribute15));
		attributebean_param.setAttribute16(Feed.getVal1(channel_id, FeedEnums.Name.attribute16));
		attributebean_param.setAttribute17(Feed.getVal1(channel_id, FeedEnums.Name.attribute17));
		attributebean_param.setAttribute18(Feed.getVal1(channel_id, FeedEnums.Name.attribute18));
		attributebean_param.setAttribute19(Feed.getVal1(channel_id, FeedEnums.Name.attribute19));
		attributebean_param.setAttribute20(Feed.getVal1(channel_id, FeedEnums.Name.attribute20));
		attributebean_param.setAttribute21(Feed.getVal1(channel_id, FeedEnums.Name.attribute21));
		attributebean_param.setAttribute22(Feed.getVal1(channel_id, FeedEnums.Name.attribute22));
		attributebean_param.setAttribute23(Feed.getVal1(channel_id, FeedEnums.Name.attribute23));
		attributebean_param.setAttribute24(Feed.getVal1(channel_id, FeedEnums.Name.attribute24));
		attributebean_param.setAttribute25(Feed.getVal1(channel_id, FeedEnums.Name.attribute25));
		attributebean_param.setAttribute26(Feed.getVal1(channel_id, FeedEnums.Name.attribute26));
		attributebean_param.setAttribute27(Feed.getVal1(channel_id, FeedEnums.Name.attribute27));
		attributebean_param.setAttribute28(Feed.getVal1(channel_id, FeedEnums.Name.attribute28));
		attributebean_param.setAttribute29(Feed.getVal1(channel_id, FeedEnums.Name.attribute29));
		attributebean_param.setAttribute30(Feed.getVal1(channel_id, FeedEnums.Name.attribute30));
		attributebean_param.setAttribute31(Feed.getVal1(channel_id, FeedEnums.Name.attribute31));
		attributebean_param.setAttribute32(Feed.getVal1(channel_id, FeedEnums.Name.attribute32));
		attributebean_param.setAttribute33(Feed.getVal1(channel_id, FeedEnums.Name.attribute33));
		attributebean_param.setAttribute34(Feed.getVal1(channel_id, FeedEnums.Name.attribute34));
		attributebean_param.setAttribute35(Feed.getVal1(channel_id, FeedEnums.Name.attribute35));
		attributebean_param.setAttribute36(Feed.getVal1(channel_id, FeedEnums.Name.attribute36));
		attributebean_param.setAttribute37(Feed.getVal1(channel_id, FeedEnums.Name.attribute37));

		String keyword_attribute = Feed.getVal1(channel_id,FeedEnums.Name.product_keyword) + keyword;

		if (category!=""){
			keyword_attribute = keyword_attribute +  " and " + Feed.getVal1(channel_id,FeedEnums.Name.product_category_url_key) + " ='" + transferStr(category)  + "'";
		}

		if (product!=""){
			keyword_attribute = keyword_attribute +  " and " + Feed.getVal1(channel_id,FeedEnums.Name.product_p_code) + " ='" +transferStr(product)  + "'";
		}

		// 新产品Attribute处理
		ProductsFeedAttribute productsFeedAttribute = new ProductsFeedAttribute();
		List<AttributeBean> attributebeans = superfeeddao.selectAttribute(attributebean_param,Feed.getVal1(channel_id,FeedEnums.Name.table_id),keyword_attribute);

		for (int k  = 0; k < attributebeans.size(); k++) {
			AttributeBean attributebean = attributebeans.get(k);
			attributebean.setCategory_url_key(formaturl(attributebean.getCategory_url_key(), "1", Feed.getVal1(channel_id, FeedEnums.Name.category_split)));
			attributebean.setModel_url_key(formaturl(attributebean.getModel_url_key(), "1", Feed.getVal1(channel_id, FeedEnums.Name.category_split)));
			attributebean.setProduct_url_key(formaturl(attributebean.getProduct_url_key(), "1", Feed.getVal1(channel_id, FeedEnums.Name.category_split)));

			attributebeans.set(k, attributebean);
		}

		// 产品属性存在
		if (attributebeans.size()>  0) {
			productsFeedAttribute.setChannel_id(channel_id);
			productsFeedAttribute.setAttributebeans(attributebeans);

			// post web servies
			WsdlResponseBean  wsdlResponseBean= jsonBeanOutAttribute(productsFeedAttribute);

			if (wsdlResponseBean.getResult().equals("NG")){
				isSuccess = false;
				logger.error("产品Attribute处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
				issueLog.log("cms 数据导入处理", "产品Attribute处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage(),
						ErrorType.BatchJob, SubSystem.CMS);
 			}
		}else{
			isSuccess = false;
		}

		logger.info("产品Attribute处理结束");
		return  isSuccess;
	}

	/**
	 * OldProductFeed 更新产品处理
	 *
	 * @param channel_id
	 * @return isSuccess
	 */
	public boolean  OldProductFeed(String channel_id){
		boolean isSuccess = true;
		logger.info("更新产品处理开始");

		// 只处理更新数据
		String keyword = " and UpdateFlag in ('2','3')";

		List<ProductBean> productBeans = new ArrayList();

		// product 取得
		productBeans = createProduct(channel_id,"","","",keyword, Feed.getVal1(channel_id,FeedEnums.Name.table_id));

		// 自动生成 Name Urlkey Parentcategory Parenturlkey
		for (int i = 0; i < productBeans.size(); i++) {

			ProductsFeedUpdate productsFeed = new ProductsFeedUpdate();

			List<ProductBean> productBeans_full = createProduct(channel_id,"","",transferStr(productBeans.get(i).getP_code()), "",  Feed.getVal1(channel_id,FeedEnums.Name.table_id) + "_full");

			if(productBeans_full.size() !=1 ){
				logger.error("更新产品处理异常 code=" + productBeans.get(i).getP_code() + ",抽出件数=" + productBeans_full.size());
				issueLog.log("cms 数据导入处理", "更新产品处理异常 code=" + productBeans.get(i).getP_code() + ",抽出件数=" + productBeans_full.size(),
						ErrorType.BatchJob, SubSystem.CMS);
				continue;
			}

			Map<String,String> updatefields = new HashMap<String,String>();

			// msrp、price、cn_price、long_description、image_url 变化
			if (!productBeans.get(i).getP_msrp().equals(productBeans_full.get(0).getP_msrp())){
				updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_MSRP,productBeans.get(i).getP_msrp() );
			}

			if (!productBeans.get(i).getPs_price().equals(productBeans_full.get(0).getPs_price())){
				updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_PRICE,productBeans.get(i).getPs_price() );
			}

			if (!productBeans.get(i).getCps_cn_price_rmb().equals(productBeans_full.get(0).getCps_cn_price_rmb())){
				updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_CN_PRICE,productBeans.get(i).getCps_cn_price_rmb() );
			}

			if (!productBeans.get(i).getPe_long_description().equals(productBeans_full.get(0).getPe_long_description())){
				updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_LONG_DESCRIPTION,productBeans.get(i).getPe_long_description() );
			}

			// image
			String image_url= "";
			for (int j = 0; j < productBeans.get(i).getImages().size(); j++) {
				image_url = image_url +  productBeans.get(i).getImages().get(j).getImage_url() + CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;
			}

			// image_full
			String image_url_full= "";
			for (int j = 0; j < productBeans_full.get(0).getImages().size(); j++) {
				image_url_full = image_url_full + productBeans_full.get(0).getImages().get(j).getImage_url() + CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_SPLIT;
			}

			// image update
			if (!image_url.equals(image_url_full)) {
				updatefields.put(CmsConstants.FEED_IO_UPDATEFIELDS_IMAGE_URL,image_url);
			}

			// 没有变化跳过。
			if(updatefields.size() <= 0 ){
				// 更新ZZ_Work_Superfeed_Full
				if (!SuperfeedFullUpdate(channel_id, productBeans.get(i).getP_code())){
					logger.error("更新ZZ_Work_Superfeed_Full 失败");
					issueLog.log("cms 数据导入处理", "更新ZZ_Work_Superfeed_Full 失败，code=" + productBeans.get(i).getP_code(),
							ErrorType.BatchJob, SubSystem.CMS);
				}
				// 如果失败下面跳过
				continue;
			}

			// key :channel_id,code,product_url_key,barcode
			productsFeed.setChannel_id(channel_id) ;
			productsFeed.setCode(productBeans.get(i).getP_code()) ;
			productsFeed.setProduct_url_key(productBeans.get(i).getUrl_key()) ;
//			productsFeed.setBarcode();

			// update fields: msrp,price,cn_price,long_description,image_url
			productsFeed.setUpdatefields(updatefields);

			//新产品Attribute处理
			if (!AttributeInsert(channel_id ,keyword, "" ,productBeans.get(i).getP_code())){
				logger.info("更新产品Attribute处理异常");
				// 如果失败下面跳过
				continue;
			};

			// post web servies
			WsdlResponseBean  wsdlResponseBean= jsonBeanOutUpdate(productsFeed);

			ProductUpdateResponseBean productUpdateResponseBean = (ProductUpdateResponseBean)  JsonUtil.jsonToBean(JsonUtil.getJsonString(wsdlResponseBean.getResultInfo()),ProductUpdateResponseBean.class);

			// web servies 返回失败
			if (wsdlResponseBean.getResult().equals("NG") || productUpdateResponseBean.getFailure().size() > 0){

				// web servies 返回系统失败
				if (wsdlResponseBean.getResult().equals("NG")){
					logger.error("更新产品处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
					issueLog.log("cms 数据导入处理", "更新产品处理异常 code=" + productBeans.get(i).getP_code() + ",抽出件数=" + productBeans_full.size(),
							ErrorType.BatchJob, SubSystem.CMS);
				}
				// web servies 返回数据失败
				else{
					String failureMessage = "";
					// 出错统计
					List<ProductUpdateDetailBean> productUpdateDetailBeans = productUpdateResponseBean.getFailure();
					for (int b  = 0; b < productUpdateDetailBeans.size(); b ++) {
						failureMessage  = failureMessage + "Messag(" + b +")=" + productUpdateDetailBeans.get(b).getResultMessage() + ";";
					}
					logger.error("更新产品处理失败，" + failureMessage);
					issueLog.log("cms 数据导入处理", "更新产品处理失败，" + failureMessage,
							ErrorType.BatchJob, SubSystem.CMS);
				}
			}else{
				// 更新ZZ_Work_Superfeed_Full
				if (!SuperfeedFullUpdate(channel_id, productBeans.get(i).getP_code())){
					logger.error("更新ZZ_Work_Superfeed_Full 失败");
					issueLog.log("cms 数据导入处理", "更新ZZ_Work_Superfeed_Full 失败，code=" +productBeans.get(i).getP_code(),
							ErrorType.BatchJob, SubSystem.CMS);
				}
			}
		}
		logger.info("更新产品处理完成");
		return isSuccess;
	}

	/**
	 * 创建 Model
	 *
	 * @param channel_id category keyword
	 * @return modelBeans
	 */
	public  List <ModelBean> createModel(String channel_id ,String category, String keyword ){
		logger.info("新产品Model处理开始");

		List <ModelBean> modelBeans = new ArrayList() ;

		ModelBean modelbean_params = new ModelBean();
		String model_keyword = Feed.getVal1(channel_id,FeedEnums.Name.model_keyword)  + " and "
				+ Feed.getVal1(channel_id,FeedEnums.Name.model_category_url_key) + "='" + transferStr(category) + "' " + keyword;

		// Model 字段 以category为单位
		modelbean_params.setUrl_key(Feed.getVal1(channel_id,FeedEnums.Name.model_url_key));
		modelbean_params.setCategory_url_key(Feed.getVal1(channel_id, FeedEnums.Name.model_category_url_key));
		modelbean_params.setM_product_type(Feed.getVal1(channel_id, FeedEnums.Name.model_m_product_type));
		modelbean_params.setM_brand(Feed.getVal1(channel_id, FeedEnums.Name.model_m_brand));
		modelbean_params.setM_model(Feed.getVal1(channel_id, FeedEnums.Name.model_m_model));
		modelbean_params.setM_name(Feed.getVal1(channel_id, FeedEnums.Name.model_m_name));
		modelbean_params.setM_short_description(Feed.getVal1(channel_id, FeedEnums.Name.model_m_short_description));
		modelbean_params.setM_long_description(Feed.getVal1(channel_id, FeedEnums.Name.model_m_long_description));
		modelbean_params.setM_size_type(Feed.getVal1(channel_id, FeedEnums.Name.model_m_size_type));
		modelbean_params.setM_is_unisex(Feed.getVal1(channel_id, FeedEnums.Name.model_m_is_unisex));
		modelbean_params.setM_weight(Feed.getVal1(channel_id, FeedEnums.Name.model_m_weight));
		modelbean_params.setM_is_taxable(Feed.getVal1(channel_id, FeedEnums.Name.model_m_is_taxable));
		modelbean_params.setM_is_effective(Feed.getVal1(channel_id, FeedEnums.Name.model_m_is_effective));

		// 取得所有Superfeed Model
		modelBeans = superfeeddao.selectSuperfeedModel(model_keyword, modelbean_params, Feed.getVal1(channel_id, FeedEnums.Name.table_id));

		// urlkey  category_url_key 转换
		for (int i = 0; i < modelBeans.size(); i++) {
			ModelBean modelbean = modelBeans.get(i);

			// urlkey 转换
			modelbean.setCategory_url_key(formaturl(modelbean.getCategory_url_key(),"1",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
			modelbean.setUrl_key(formaturl(modelbean.getUrl_key(),"1",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));

			modelBeans.set(i,modelbean);
		}

		logger.info("新产品Model处理完成");
		return modelBeans;
	}

	/**
	 * 创建 Product
	 *
	 * @param category keyword tablename
	 * @return productBeans
	 */
	public  List <ProductBean> createProduct(String channel_id, String category,  String models, String codes, String keyword, String tablename){
		logger.info("产品Product处理开始");

		List <ProductBean> productBeans = new ArrayList() ;
		String category_keyword ="";
		String model_keyword ="";
		String code_keyword ="";
		String updateflag_keyword ="";

		// category
		if (category != "") {
			category_keyword =  " and " + Feed.getVal1(channel_id,FeedEnums.Name.product_category_url_key) + "='" + transferStr(category) + "'";
		}
		// product models
		if (models != "") {
			model_keyword =  " and (" +   Feed.getVal1(channel_id,FeedEnums.Name.model_m_model) + " in ('" + transferStr(models) + "')"
			+ " or " +   Feed.getVal1(channel_id,FeedEnums.Name.model_m_model_other_feild) + " in ('" + transferStr(models) + "'))";
		}
		// product codes
		if (codes != "") {
			code_keyword =  " and " +   Feed.getVal1(channel_id,FeedEnums.Name.product_p_code) + " in ('" + transferStr(codes) + "')";
		}
		// updateflag
		if (keyword != "") {
			updateflag_keyword =  keyword;
		}

		ProductBean productbean_params = new ProductBean();

		String product_keyword = Feed.getVal1(channel_id,FeedEnums.Name.product_keyword) + category_keyword + model_keyword  + code_keyword + updateflag_keyword;
		productbean_params.setUrl_key(Feed.getVal1(channel_id, FeedEnums.Name.product_url_key));
		productbean_params.setModel_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_model_url_key));
		productbean_params.setCategory_url_key(Feed.getVal1(channel_id, FeedEnums.Name.product_category_url_key));
		productbean_params.setP_code(Feed.getVal1(channel_id, FeedEnums.Name.product_p_code));
		productbean_params.setP_name(Feed.getVal1(channel_id, FeedEnums.Name.product_p_name));
		productbean_params.setP_color(Feed.getVal1(channel_id, FeedEnums.Name.product_p_color));
		productbean_params.setP_msrp(Feed.getVal1(channel_id, FeedEnums.Name.product_p_msrp));
		productbean_params.setP_made_in_country(Feed.getVal1(channel_id, FeedEnums.Name.product_p_made_in_country));
		productbean_params.setPe_short_description(Feed.getVal1(channel_id, FeedEnums.Name.product_pe_short_description));
		productbean_params.setPe_long_description(Feed.getVal1(channel_id, FeedEnums.Name.product_pe_long_description));
		productbean_params.setPs_price(Feed.getVal1(channel_id, FeedEnums.Name.product_ps_price));
		productbean_params.setCps_cn_price_rmb(Feed.getVal1(channel_id, FeedEnums.Name.product_cps_cn_price_rmb));

		// 取得所有Superfeed  product
		productBeans = superfeeddao.selectSuperfeedProduct(product_keyword,
				productbean_params, tablename);

		// urlkey  category_url_key 转换
		for (int i = 0; i < productBeans.size(); i++) {
			ProductBean productbean = productBeans.get(i);

			// urlkey 转换
			productbean.setCategory_url_key(formaturl(productbean.getCategory_url_key(), "1",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
			productbean.setModel_url_key(formaturl(productbean.getModel_url_key(), "1",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
			productbean.setUrl_key(formaturl(productbean.getUrl_key(), "1",Feed.getVal1(channel_id,FeedEnums.Name.category_split)));
			// 初期值 0
			productbean.setP_image_item_count("0");

			// 抽出条件 code
			String item_keyword = product_keyword + " and" + Feed.getVal1(channel_id,FeedEnums.Name.product_p_code)  +" = '" + transferStr(productbean.getP_code()) + "'";

			// 图片取得
			List <String> image = superfeeddao.selectSuperfeedImage(item_keyword, Feed.getVal1(channel_id,FeedEnums.Name.images),tablename);
			List <ImageBean> imagebeans = new ArrayList() ;

			// 多条只取第一条;
			for (int j = 0;j < image.size(); j++) {
				String[] images=image.get(j).split(Feed.getVal1(channel_id,FeedEnums.Name.image_split));

				for (int  k= 0; k < images.length; k++) {

					ImageBean imagebean = new ImageBean();
					// 有图
					if (images[k].lastIndexOf("/")> 0 && images[k].lastIndexOf(".") > 0 ){
						// Image
						imagebean.setImage_type("1");
						imagebean.setImage(String.valueOf(imagebeans.size() + 1));
						imagebean.setImage_url(images[k]);
						imagebean.setImage_name(images[k].substring(images[k].lastIndexOf("/") + 1, images[k].lastIndexOf(".")));
						imagebean.setDisplay_order("0");

						imagebeans.add(imagebean);
					}
				}

				// 图片更新
				if (imagebeans.size() > 0 ){
					productbean.setImages(imagebeans);
					productbean.setP_image_item_count(String.valueOf(imagebeans.size()));
					break;
				}
			}

			ItemBean itembean_params = new ItemBean();
			itembean_params.setCode(Feed.getVal1(channel_id,FeedEnums.Name.item_code) );
			itembean_params.setI_sku(Feed.getVal1(channel_id,FeedEnums.Name.item_i_sku) );
			itembean_params.setI_itemcode(Feed.getVal1(channel_id,FeedEnums.Name.item_i_itemcode) );
			itembean_params.setI_size(Feed.getVal1(channel_id,FeedEnums.Name.item_i_size) );
			itembean_params.setI_barcode(Feed.getVal1(channel_id,FeedEnums.Name.item_i_barcode));

			// item size取得
			List<ItemBean> items = superfeeddao.selectSuperfeedItem(item_keyword, itembean_params,tablename);

			// item更新
			if (items.size() > 0 ){
				productbean.setItembeans(items);
			}

			productBeans.set(i,productbean);
		}

		logger.info("产品Product处理完成");
		return productBeans;
	}

	/**
	 * formaturl 转换分隔符为-，转换空格为-，去除特殊字符，转小写
	 *
	 * @param url tolower split
	 * @return formaturl
	 */
	public  String formaturl(String url, String tolower ,String split ){

		String strformaturl = url.replace(split, "-");

		for (int i = 0; i < CmsConstants.URL_FORMAT.length(); i++) {
			strformaturl =  strformaturl.replace(CmsConstants.URL_FORMAT.substring(i, i + 1), "");
		}

		if ("1".equals(tolower)){
			// 除去空格 去除双连续空格
			strformaturl = strformaturl.replace("  ", "-");
			// 除去单空格
			strformaturl = strformaturl.replace(" ", "-");
			strformaturl =  strformaturl.toLowerCase();
		}

//		logger.info("url = " +  url + ", strformaturl = " +  strformaturl);
		return strformaturl;
	}
//
//	public void backupfile(ProductsFeedInsert productsFeed)   {
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		String date_ymd  = sdf.format(date);
//		File file = new File(CmsConstants.BACKUP_FEED_FILE + date_ymd +".txt");
//		try {
//			FileOutputStream outFile = new FileOutputStream(file);
//			// 准备要输出的内容
//			StringBuffer sb = new StringBuffer();
//			sb.append( JsonUtil.getJsonString(productsFeed));
//			sb.append("\n");
//
//			// 关闭文件
//			try {
//				outFile.write(sb.toString().getBytes());
//				outFile.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//				logger.error(e.getMessage());
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			logger.error(e.getMessage());
//		}
//	}

	/**
	 * json bean 新数据  post
	 *
	 */
	public WsdlResponseBean jsonBeanOutInsert(ProductsFeedInsert productsFeed){

		Map param = new HashMap();
		Map authMap = new HashMap();
		authMap.put("appKey", "21006636");
		authMap.put("appSecret", "ca16bd08019790b2a9332e000e52e19f");
		authMap.put("sessionKey", "7200a23ce180124c6Z248fa2bd5b420Zdf0df34db94bd5a90702966b");

		param.put("authentication", authMap);
		param.put("dataBody", productsFeed);

		String jsonParam =  JsonUtil.getJsonString(param);

		String response = null;
		WsdlResponseBean wsdlresponsebean = null;
		try {
			response = WebServiceUtil.postByJsonStr(CmsConstants.WEB_SERVIES_URI_INSERT, jsonParam);
			wsdlresponsebean = JsonUtil.jsonToBean(response, WsdlResponseBean.class);
		} catch (Exception e) {
			logger.error("json bean 新数据 post 失败: web servies =" + CmsConstants.WEB_SERVIES_URI_INSERT);
			issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
		}

		return wsdlresponsebean;
	}

	/**
	 * json bean 更新数据  post
	 *
	 */
	private WsdlResponseBean jsonBeanOutUpdate(ProductsFeedUpdate productsFeed){

		Map param = new HashMap();
		Map authMap = new HashMap();
		authMap.put("appKey", "21006636");
		authMap.put("appSecret", "ca16bd08019790b2a9332e000e52e19f");
		authMap.put("sessionKey", "7200a23ce180124c6Z248fa2bd5b420Zdf0df34db94bd5a90702966b");

		param.put("authentication", authMap);
		param.put("dataBody", productsFeed);
		String jsonParam =  JsonUtil.getJsonString(param);
		String response = null;
		WsdlResponseBean wsdlresponsebean = null;
		try {
			response = WebServiceUtil.postByJsonStr(CmsConstants.WEB_SERVIES_URI_UPDATE, jsonParam);
			wsdlresponsebean = JsonUtil.jsonToBean(response, WsdlResponseBean.class);
		} catch (Exception e) {
			logger.error("json bean 更新数据 post 失败: web servies =" + CmsConstants.WEB_SERVIES_URI_UPDATE);
			issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
		}
		return wsdlresponsebean;
 	}

	/**
	 * json bean 属性数据  post
	 *
	 */
	private WsdlResponseBean jsonBeanOutAttribute(ProductsFeedAttribute attributebeans){

		Map param = new HashMap();
		Map authMap = new HashMap();
		authMap.put("appKey", "21006636");
		authMap.put("appSecret", "ca16bd08019790b2a9332e000e52e19f");
		authMap.put("sessionKey", "7200a23ce180124c6Z248fa2bd5b420Zdf0df34db94bd5a90702966b");

		param.put("authentication", authMap);
		param.put("dataBody", attributebeans);
		String jsonParam =  JsonUtil.getJsonString(param);
		String response = null;
		WsdlResponseBean wsdlresponsebean = null;
		try {
			response = WebServiceUtil.postByJsonStr(CmsConstants.WEB_SERVIES_URI_ATTRIBUTE, jsonParam);
			wsdlresponsebean = JsonUtil.jsonToBean(response, WsdlResponseBean.class);
		} catch (Exception e) {
			logger.error("json bean 属性数据 post 失败: web servies =" + CmsConstants.WEB_SERVIES_URI_ATTRIBUTE);
			issueLog.log(e, ErrorType.BatchJob, SubSystem.CMS);
		}
		return wsdlresponsebean;
	}

	/**
	 * 下载ftp文件
	 * @param channel_id
	 */
	private boolean downloadFileForFtp(String channel_id ) throws Exception{
		logger.info("产品文件下载开始 ");
		boolean isSuccess = true;

		// FtpBean初期化
		FtpBean ftpBean = new FtpBean();

		ftpBean.setPort(Feed.getVal1(channel_id,FeedEnums.Name.feed_ftp_port));
		ftpBean.setUrl(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_url));
		ftpBean.setUsername(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_username));
		ftpBean.setPassword(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_password));
		ftpBean.setFile_coding(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_file_coding));

		FtpUtil ftpUtil = new FtpUtil();
		FTPClient ftpClient = new FTPClient();
		List<String> fileNames;
		try {
			//建立连接
			ftpClient = ftpUtil.linkFtp(ftpBean);
			if (ftpClient != null) {
				//本地文件路径设定
				ftpBean.setDown_localpath(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath));
				//Ftp源文件路径设定
				ftpBean.setDown_remotepath(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_remotepath));
				//Ftp源文件名设定
				ftpBean.setDown_filename(StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_filename)));

				String filePathName = ftpBean.getDown_localpath() + "/" + ftpBean.getDown_filename();
				int result = ftpUtil.downFile(ftpBean,ftpClient);

				//下载文件 失败
				if (result != 2 ){
					if (result == 0){
						File file = new File(filePathName);
						file.delete();
						isSuccess = false;
						logger.error(filePathName + "下载异常！");
						issueLog.log("cms 数据导入处理", filePathName + "下载异常！", ErrorType.BatchJob, SubSystem.CMS);
					}else {
						logger.info(filePathName + "文件不存在.");
					}
				}else{
					//下载文件 成功
					ftpUtil.delOneFile(ftpBean,ftpClient, StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_filename)));
				}
			}
		}finally{
			//断开连接
			ftpUtil.disconnectFtp(ftpClient);
		}
		logger.info("产品文件下载结束");
		return  isSuccess;
	}

	/**
	 * 备份处理文件
	 * @param channel_id
	 */
	private boolean backupFeedFile(String channel_id ){
		logger.info("备份处理文件开始");
		boolean isSuccess = true;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String date_ymd  = sdf.format(date);

		String filename  = Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.file_id));
		String filename_backup = Feed.getVal1(channel_id, FeedEnums.Name.feed_ftp_localpath) + "/" + date_ymd + "_"
				+ StringUtils.null2Space(Feed.getVal1(channel_id, FeedEnums.Name.file_id));
		File file = new File(filename);
		File file_backup = new File(filename_backup);

		if (!file.renameTo(file_backup)){
			logger.error("产品文件备份失败");
		};

		logger.info("备份处理文件结束");
		return  isSuccess;
	}

	/**
	 * 备份处理文件
	 * @param channel_id
	 */
	private boolean AttributeListInsert(String channel_id ){
		logger.info("AttributeList处理开始");
		boolean isSuccess = true;

		List<String> attributelist = superfeeddao.selectSuperfeedAttributeList(channel_id, "1", "1");
		for (int i = 0; i < attributelist.size(); i++) {
			String attribute = attributelist.get(i);

			// 单事务处理
			transactionRunner.runWithTran(new Runnable() {
				@Override
				public void run() {
//					String sql = "delete from voyageone_cms.cms_mt_feed_attribute where attribute_name = '" + attribute + "'";
//					if (superfeeddao.deleteData(sql) >= 0) {
// 					}

					List<String> allattributes = superfeeddao.selectAllAttribute(attribute, Feed.getVal1(channel_id, FeedEnums.Name.table_id));
					for (int j = 0; j < allattributes.size(); j++) {
						String result =  superfeeddao.selectFeedAttribute(channel_id,attribute,allattributes.get(j));
						if (Integer.parseInt(result)==0){
							if (superfeeddao.insertFeedAttributeNew(channel_id,attribute ,allattributes.get(j))<0){
								logger.error("AttributeList 插入失败 attribute = " + attribute + ","+ allattributes.get(j));
								issueLog.log("cms 数据导入处理", "AttributeList 插入失败 attribute= " + attribute, ErrorType.BatchJob, SubSystem.CMS);
							};
						}
					}

//					if (superfeeddao.deleteData(sql) >= 0) {
//						if (superfeeddao.insertFeedAttribute(channel_id, attribute, Feed.getVal1(channel_id, FeedEnums.Name.table_id)) < 0) {
//							logger.error("AttributeList 插入失败 attribute = " + attribute);
//							issueLog.log("cms 数据导入处理", "AttributeList 插入失败 attribute= " + attribute, ErrorType.BatchJob, SubSystem.CMS);
//						}
//					}
				}
			});
		}

		logger.info("AttributeList处理结束");
		return  isSuccess;
	}

	/**
	 * 转换数据中的特殊字符
	 *
	 * @param data
	 * @return
	 */
	public static String transferStr(String data) {
		return data.replace("'", "''").replace("\\", "\\\\").replace("\r\n", " ").replace("\n", " ").replace("\r", " ");
	}

	/**
	 * 插入ZZ_Work_Superfeed_Full产品信息
	 * @param channel_id,modelList,productList
	 */
	private boolean SuperfeedFullInsert(String category, String channel_id , List <String> modelList, List <String> productList ){
		boolean isSuccess = true;
		logger.info("插入ZZ_Work_Superfeed_Full产品处理开始");

		// 成功的数据保存至full表
		String keyWrod= "";
		// model date
		String keyWrod_model = "";
		for (int f  = 0; f < modelList.size(); f++) {
			if ( f == 0 ){
				keyWrod_model = Feed.getVal1(channel_id,FeedEnums.Name.model_m_model) + " not in ('" + modelList.get(f);
			}else{
				keyWrod_model = keyWrod_model + "', " + modelList.get(f);
			}
		}
		if (keyWrod_model !=""){
			keyWrod_model = keyWrod_model + "')";
		}

		// product date
		String keyWrod_product = "";
		for (int f  = 0; f < productList.size(); f++) {
			if ( f == 0 ){
				keyWrod_product = Feed.getVal1(channel_id,FeedEnums.Name.product_p_code) + "not in ('" + productList.get(f);
			}else{
				keyWrod_product = keyWrod_product + "', " + productList.get(f);
			}
		}
		if (keyWrod_product !=""){
			keyWrod_product = keyWrod_product + "')";
		}

		keyWrod = " where UpdateFlag in ('1','3') and "
				+ Feed.getVal1(channel_id,FeedEnums.Name.category_column) +" ='"+ transferStr(category) + "'";

		// model + product date isexist'
		if (keyWrod_model != "" && keyWrod_product != ""){
			keyWrod = keyWrod + " and (" + keyWrod_model  + " and " +  keyWrod_product + ")";;
		}else{
			// model or product date isexist
			if (keyWrod_model != "" || keyWrod_product != ""){
				keyWrod = keyWrod + " and (" + keyWrod_model  +  keyWrod_product + ")";
			}
		}

		if (keyWrod !="" && keyWrod != null){
			int count = superfeeddao.inertSuperfeedFull(keyWrod, Feed.getVal1(channel_id, FeedEnums.Name.table_id), Feed.getVal1(channel_id, FeedEnums.Name.table_id) + "_full");
			if (count< 0){
				isSuccess = false;
				logger.error("插入ZZ_Work_Superfeed_Full产品处理失败");
			}
		}

		logger.info("插入ZZ_Work_Superfeed_Full产品处理结束");
		return  isSuccess;
	}

	/**
	 * 更新ZZ_Work_Superfeed_Full产品信息
	 * @param channel_id,product_code
	 */
	private boolean SuperfeedFullUpdate(String channel_id ,String product_code){
		boolean isSuccess = true;
		logger.info("更新ZZ_Work_Superfeed_Full产品处理开始");

		// 单事务处理
		transactionRunner.runWithTran(new Runnable() {
			@Override
			public void run() {

				String deletefeed = "delete from " + Feed.getVal1(channel_id,FeedEnums.Name.table_id) + "_full" + " where "
						+ Feed.getVal1(channel_id,FeedEnums.Name.product_p_code) + "='" + product_code + "'";

				// 删除full表即存数据
				if  (superfeeddao.deleteData(deletefeed) > 0 ){
					String keyword = " where " + Feed.getVal1(channel_id,FeedEnums.Name.product_p_code) + "='" + product_code+ "'";

					// 新的增加
					int result = superfeeddao.inertSuperfeedFull(keyword, Feed.getVal1(channel_id,FeedEnums.Name.table_id),Feed.getVal1(channel_id,FeedEnums.Name.table_id) + "_full");

					if (result <= 0){
						logger.error("更新 ZZ_Work_Superfeed_Full表 delete失败，code= " + product_code);
					}
				}else{
					logger.error("更新 ZZ_Work_Superfeed_Full表 insert失败，code= " + product_code);
				}
			}
		});

		logger.info("更新ZZ_Work_Superfeed_Full产品处理结束");
		return  isSuccess;
	}
}