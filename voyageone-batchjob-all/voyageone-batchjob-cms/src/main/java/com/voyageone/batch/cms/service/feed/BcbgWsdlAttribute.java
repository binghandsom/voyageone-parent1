package com.voyageone.batch.cms.service.feed;

import com.voyageone.batch.cms.bean.AttributeBean;
import com.voyageone.batch.cms.bean.ProductsFeedAttribute;
import com.voyageone.batch.cms.bean.WsdlResponseBean;
import com.voyageone.batch.cms.dao.SuperFeedDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Enums.ChannelConfigEnums.Channel;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Created by Jonas on 10/27/15.
 */
@Service
public class BcbgWsdlAttribute extends BcbgWsdlBase {

    @Autowired
    private SuperFeedDao superFeedDao;

    @Override
    protected String getWhereUpdateFlg() {
        return Constants.EmptyString;
    }

    /**
     * 获取任务名称
     */
    @Override
    public String getTaskName() {
        return "BcbgAnalysis.Attribute";
    }

    protected class Context extends ContextBase {

        private AttributeBean attributeColumns;

        protected Context(Channel channel) {
            super(channel);
        }
        
        public void postAttributes() throws Exception {

            $info("产品Attribute处理开始");

            List<AttributeBean> attributeBeans = getAttributes();

            $info("获取未处理重复的 AttributeBean %s 个", attributeBeans.size());

            ProductsFeedAttribute feedAttribute = new ProductsFeedAttribute();
            feedAttribute.setChannel_id(channel.getId());
            feedAttribute.setAttributebeans(attributeBeans);

            WsdlProductService service = new WsdlProductService(channel);
            WsdlResponseBean wsdlResponseBean = service.attribute(feedAttribute);

            // 处理失败
            if (wsdlResponseBean == null) {
                $info("产品Attribute处理失败，处理失败！");
            } else {
                $info("调用结果 Attribute 接口");
                $info("\tMessage:\t%s", wsdlResponseBean.getMessage());
                $info("\tResult:\t%s", wsdlResponseBean.getResult());
                $info("\tResultInfo:\t%s", wsdlResponseBean.getResultInfo());

                if (wsdlResponseBean.getResult().equals("NG")) {
                    $info("产品Attribute处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
                    logIssue("cms 数据导入处理", "产品Attribute处理失败，MessageCode = " + wsdlResponseBean.getMessageCode() + ",Message = " + wsdlResponseBean.getMessage());
                }
            }

            $info("产品Attribute处理结束");
        }

        private List<AttributeBean> getAttributes() {
            /*
             * 先查询所有商品的属性
             * 之后根据 ProductUrlKey 分组, 取第一个. 以此进行去重复
             * 同时格式化 UrlKey
             */
            String groupBy = Feed.getVal1(channel, FeedEnums.Name.product_sql_ending);
            return superFeedDao.selectAttribute(getAttributeColumns(), String.format("%s %s", productTable, productJoin), groupBy)
                    .stream()
                    .collect(groupingBy(AttributeBean::getProduct_url_key, toList()))
                    .values()
                    .stream()
                    .map(list -> {
                        AttributeBean attributeBean = list.get(0);
                        attributeBean.setCategory_url_key(clearSpecialSymbol(attributeBean.getCategory_url_key()));
                        attributeBean.setModel_url_key(clearSpecialSymbol(attributeBean.getModel_url_key()));
                        attributeBean.setProduct_url_key(clearSpecialSymbol(attributeBean.getProduct_url_key()));
                        return attributeBean;
                    })
                    .collect(toList());
        }

        private AttributeBean getAttributeColumns() {

            if (attributeColumns != null) return attributeColumns;

            attributeColumns = new AttributeBean();

            attributeColumns.setCategory_url_key(Feed.getVal1(channel, FeedEnums.Name.product_category_url_key));
            attributeColumns.setModel_url_key(Feed.getVal1(channel, FeedEnums.Name.product_model_url_key));
            attributeColumns.setProduct_url_key(Feed.getVal1(channel, FeedEnums.Name.product_url_key));
            attributeColumns.setAttribute1(Feed.getVal1(channel, FeedEnums.Name.attribute1));
            attributeColumns.setAttribute2(Feed.getVal1(channel, FeedEnums.Name.attribute2));
            attributeColumns.setAttribute3(Feed.getVal1(channel, FeedEnums.Name.attribute3));
            attributeColumns.setAttribute4(Feed.getVal1(channel, FeedEnums.Name.attribute4));
            attributeColumns.setAttribute5(Feed.getVal1(channel, FeedEnums.Name.attribute5));
            attributeColumns.setAttribute6(Feed.getVal1(channel, FeedEnums.Name.attribute6));
            attributeColumns.setAttribute7(Feed.getVal1(channel, FeedEnums.Name.attribute7));
            attributeColumns.setAttribute8(Feed.getVal1(channel, FeedEnums.Name.attribute8));
            attributeColumns.setAttribute9(Feed.getVal1(channel, FeedEnums.Name.attribute9));
            attributeColumns.setAttribute10(Feed.getVal1(channel, FeedEnums.Name.attribute10));
            attributeColumns.setAttribute11(Feed.getVal1(channel, FeedEnums.Name.attribute11));
            attributeColumns.setAttribute12(Feed.getVal1(channel, FeedEnums.Name.attribute12));
            attributeColumns.setAttribute13(Feed.getVal1(channel, FeedEnums.Name.attribute13));
            attributeColumns.setAttribute14(Feed.getVal1(channel, FeedEnums.Name.attribute14));
            attributeColumns.setAttribute15(Feed.getVal1(channel, FeedEnums.Name.attribute15));
            attributeColumns.setAttribute16(Feed.getVal1(channel, FeedEnums.Name.attribute16));
            attributeColumns.setAttribute17(Feed.getVal1(channel, FeedEnums.Name.attribute17));
            attributeColumns.setAttribute18(Feed.getVal1(channel, FeedEnums.Name.attribute18));
            attributeColumns.setAttribute19(Feed.getVal1(channel, FeedEnums.Name.attribute19));
            attributeColumns.setAttribute20(Feed.getVal1(channel, FeedEnums.Name.attribute20));
            attributeColumns.setAttribute21(Feed.getVal1(channel, FeedEnums.Name.attribute21));
            attributeColumns.setAttribute22(Feed.getVal1(channel, FeedEnums.Name.attribute22));
            attributeColumns.setAttribute23(Feed.getVal1(channel, FeedEnums.Name.attribute23));
            attributeColumns.setAttribute24(Feed.getVal1(channel, FeedEnums.Name.attribute24));
            attributeColumns.setAttribute25(Feed.getVal1(channel, FeedEnums.Name.attribute25));
            attributeColumns.setAttribute26(Feed.getVal1(channel, FeedEnums.Name.attribute26));
            attributeColumns.setAttribute27(Feed.getVal1(channel, FeedEnums.Name.attribute27));
            attributeColumns.setAttribute28(Feed.getVal1(channel, FeedEnums.Name.attribute28));
            attributeColumns.setAttribute29(Feed.getVal1(channel, FeedEnums.Name.attribute29));
            attributeColumns.setAttribute30(Feed.getVal1(channel, FeedEnums.Name.attribute30));
            attributeColumns.setAttribute31(Feed.getVal1(channel, FeedEnums.Name.attribute31));
            attributeColumns.setAttribute32(Feed.getVal1(channel, FeedEnums.Name.attribute32));
            attributeColumns.setAttribute33(Feed.getVal1(channel, FeedEnums.Name.attribute33));
            attributeColumns.setAttribute34(Feed.getVal1(channel, FeedEnums.Name.attribute34));
            attributeColumns.setAttribute35(Feed.getVal1(channel, FeedEnums.Name.attribute35));
            attributeColumns.setAttribute36(Feed.getVal1(channel, FeedEnums.Name.attribute36));
            attributeColumns.setAttribute37(Feed.getVal1(channel, FeedEnums.Name.attribute37));
            attributeColumns.setAttribute37(Feed.getVal1(channel, FeedEnums.Name.attribute37));
            attributeColumns.setAttribute38(Feed.getVal1(channel, FeedEnums.Name.attribute38));
            attributeColumns.setAttribute39(Feed.getVal1(channel, FeedEnums.Name.attribute39));
            attributeColumns.setAttribute40(Feed.getVal1(channel, FeedEnums.Name.attribute40));
            attributeColumns.setAttribute41(Feed.getVal1(channel, FeedEnums.Name.attribute41));
            attributeColumns.setAttribute42(Feed.getVal1(channel, FeedEnums.Name.attribute42));
            attributeColumns.setAttribute43(Feed.getVal1(channel, FeedEnums.Name.attribute43));
            attributeColumns.setAttribute44(Feed.getVal1(channel, FeedEnums.Name.attribute44));
            attributeColumns.setAttribute45(Feed.getVal1(channel, FeedEnums.Name.attribute45));
            attributeColumns.setAttribute46(Feed.getVal1(channel, FeedEnums.Name.attribute46));
            attributeColumns.setAttribute47(Feed.getVal1(channel, FeedEnums.Name.attribute47));
            attributeColumns.setAttribute48(Feed.getVal1(channel, FeedEnums.Name.attribute48));
            attributeColumns.setAttribute49(Feed.getVal1(channel, FeedEnums.Name.attribute49));
            attributeColumns.setAttribute50(Feed.getVal1(channel, FeedEnums.Name.attribute50));

            return attributeColumns;
        }
    }
}
