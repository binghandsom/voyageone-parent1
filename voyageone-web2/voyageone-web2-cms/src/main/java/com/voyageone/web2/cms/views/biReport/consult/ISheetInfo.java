package com.voyageone.web2.cms.views.biReport.consult;


/**
 * Created by dell on 2017/1/22.
 */
public interface ISheetInfo {
    interface SHEET
    {
        interface BASICINFO
        {
//            String PATH="E://bi_report//xlsxFile//";           /* E://bi_report//xlsxFile//*/
            Integer SUBMITNOTSTART=0;
            Integer FAILED=1;
            Integer CREATING=2;
            Integer CREATED=3;
            Integer OUTTIME=4;
         }
        interface SHEETNAME
        {
            String [] SheetTitles={"店铺月报","店铺周报","店铺日报","商品月报","商品周报","商品日报","产品月报",
                    "产品周报","产品日报","SKU月报","SKU周报","SKU日报"};
            Integer precise=2;
            Integer precise4=2;
        }
    }
    interface SHOP
    {
        interface SHOPDAILY
        {
             String[] headers1={"店铺日报"};
             String[] headers2={"销售指标"," 运营指标 ","商品指标","物流指标","服务指标"};
             String[] headers3={"店铺名称","日期","销售额","销售量","销售额环比","环比增幅%","销售额同比","同比增幅%","YTD金额","YTD完成率",
                    "MTD金额","WTD金额","UV","PV","买家数","订单数","转化率%","客单价","粉丝数","老客户数","在架商品数","下架商品数",
                    "动销率","发货率","直邮订单占比","72小时未发货率","服务态度动态评分","描述相符动态评分","物流服务动态评分","48小时响应率排名","退款完结时长排名",
                    "品质退款率排名","退货订单数","退款纠纷率","询单转化率"};
        }
        // shop weekly title information
        interface SHOPWEEKLY
        {
            String[] headers1={"店铺周报"};
            String[] headers2={"销售指标"," 运营指标 ","商品指标","物流指标","服务指标"};
            String[] headers3={"店铺名称","周","日期","销售额","销售量","销售额环比","环比增幅%","销售额同比","同比增幅%",
                    "UV","PV","买家数","订单数","转化率%","客单价","粉丝数","老客户数","在架商品数","下架商品数",
                    "动销率","发货率","直邮订单占比","72小时未发货率","服务态度动态评分","描述相符动态评分","物流服务动态评分","48小时响应率排名","退款完结时长排名",
                    "品质退款率排名","退货订单数","退款纠纷率","询单转化率"};
        }
        interface SHOPMONTHLY
        {
            String[] headers1={"店铺月报"};
            String[] headers2={"销售指标"," 运营指标 ","商品指标","物流指标","服务指标"};
            String[] headers3={"店铺名称","月份","业绩目标","完成率","销售额","销售量","销售额环比","环比增幅%","销售额同比","同比增幅%",
                    "UV","PV","买家数","订单数","转化率%","客单价","粉丝数","老客户数","在架商品数","下架商品数",
                    "动销率","发货率","直邮订单占比","72小时未发货率","服务态度动态评分","描述相符动态评分","物流服务动态评分","48小时响应率排名","退款完结时长排名",
                    "品质退款率排名","退货订单数","退款纠纷率","询单转化率"};
        }
    }
    interface MODEL
    {
        interface MODELDAILY
        {
            String[] headers1={"商品日报"};
            String[] headers2={"销售指标"," 运营指标 ","属性"};
            String[] headers3={"店铺名称","日期","商品ID","商品名称","销售额","销售量","买家数","人均成交件数","客单价",
                    "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值",
                    "类目","Brand","Color","Origin","Material"};
        }
        interface MODELMONTHLY
        {
            String[] headers1={"商品月报"};
            String[] headers2={"销售指标"," 运营指标 ","属性"};
            String[] headers3={"店铺名称","月份","商品ID","商品名称","销售额","销售量","买家数","人均成交件数","客单价",
                                "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值", "类目","Brand",
                              "Color","Origin","Material"};
        }
        interface MODELWEEKLY
        {
            String[] headers1={"商品周报"};
            String[] headers2={"销售指标"," 运营指标 ","属性"};
            String[] headers3={"店铺名称","周","日期","商品ID","商品名称","销售额","销售量","买家数","人均成交件数","客单价",
                    "           PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值", "类目","Brand",
                                "Color","Origin","Material"};
        }
    }
    interface PRODUCT
    {
        interface PRODUCTDAILY
        {
            String[] headers1={"产品日报"};
            String[] headers2={"销售指标"," 运营指标 ","属性"};
            String[] headers3={"店铺名称","日期","商品ID","商品名称","产品Code","销售额","销售量","买家数","人均成交件数","客单价",
                    "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值",
                    "类目","Brand","Color","Origin","Material"};
        }
        interface PRODUCTMONTHLY
        {
            String[] headers1={"产品月报"};
            String[] headers2={"销售指标","运营指标 ","属性"};
            String[] headers3={"店铺名称","月份","商品ID","商品名称","产品Code","销售额","销售量","买家数","人均成交件数","客单价",
                    "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值",
                    "类目","Brand","Color","Origin","Material"};
        }
        interface PRODUCTWEEKLY
        {
            String[] headers1={"产品周报"};
            String[] headers2={"销售指标","运营指标 ","属性"};
            String[] headers3={"店铺名称","周","日期","商品ID","商品名称","产品Code","销售额","销售量","买家数","人均成交件数","客单价",
                                "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值", "类目","Brand",
                                "Color","Origin","Material"};
        }
    }
    interface SKU
    {
        interface SKUDAILY
        {
            String[] headers1={"SKU日报"};
            String[] headers2={"销售指标"," 运营指标 ","属性"};
            String[] headers3={"店铺名称","日期","商品ID","商品名称","产品Code","SKU","销售额","销售量","买家数","人均成交件数",
                                "客单价", "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值", "类目",
                                "Brand","Color","Origin","Material","Weight","Size"};
        }
        interface SKUMONTHLY
        {
            String[] headers1={"SKU月报"};
            String[] headers2={"销售指标","运营指标 ","属性"};
            String[] headers3={"店铺名称","月份","商品ID","商品名称","产品Code","SKU","销售额","销售量","买家数","人均成交件数",
                    "客单价", "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值", "类目",
                    "Brand","Color","Origin","Material","Weight","Size"};
        }
        interface SKUWEEKLY
        {
            String[] headers1={"SKU周报"};
            String[] headers2={"销售指标","运营指标 ","属性"};
            String[] headers3={"店铺名称","周","日期","商品ID","商品名称","产品Code","SKU","销售额","销售量","买家数","人均成交件数",
                    "客单价", "PV","UV","转化率%","加购件数","粉丝数","页面停留时间","跳失率","访客价值", "类目",
                    "Brand","Color","Origin","Material","Weight","Size"};
        }
    }
}
