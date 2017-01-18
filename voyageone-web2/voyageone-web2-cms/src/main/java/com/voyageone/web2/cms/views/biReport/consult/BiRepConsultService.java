package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.dao.report.BiReportSalesProduct010Dao;
import com.voyageone.service.dao.report.BiReportSalesShop010Dao;
import com.voyageone.service.daoext.report.BiReportSalesShop010DaoExt;
import com.voyageone.service.model.report.ShopSalesOfChannel010Model;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dell on 2017/1/9.
 */
@Service
public class BiRepConsultService {

    @Autowired
    private DateUtilHelper dateUtilHelper;
    @Autowired
    private BiReportSalesProduct010Dao biReportSalesProduct010Dao;
    @Autowired
    private BiRepSupport BiRepSupport;
    @Autowired
    private BiReportSalesShop010Dao biReportSalesShop010Dao;
    @Autowired
    private BiRepExcelFileCreator biRepExcelFileCreator;
    @Autowired
    private BiReportSalesShop010DaoExt biReportSalesShop010DaoExt;
    public Map<String, Object> biRepDownload(PageQueryParameters parameters) {

      /*  result.put("platformTypeList", TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));*/

        return null;
    }
    /**
     * 返回workbook 字节流
     * @param para
     * @return
     */
    public byte[] createXLSFile(Map para)
    {
          final String[] headers1={"店铺日报"};
          final String[] headers2={"销售指标"," 运营指标 ","商品指标","物流指标","服务指标"};
          final String[] headers3={"店铺名称","日期","销售额","销售量","销售额环比","环比增幅%","销售额同比","同比增幅%","YTD金额","YTD完成率",
                "MTD金额","WTD金额","UV","PV","买家数","订单数","转化率%","客单价","粉丝数","老客户数","在架商品数","下架商品数",
                "动销率","发货率","直邮订单占比","72小时未发货率","服务态度动态评分","描述相符动态评分","物流服务动态评分","48小时响应率排名","退款完结时长排名",
                "品质退款率排名","退款率%(退货订单数)","退款纠纷率","询单转化率"};
        String sheetName=headers1[0];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Workbook workbook =biRepExcelFileCreator.createExcelFile(dealSaleShopModel(getData(para)),getMergeInfo(),sheetName,headers1,headers2,headers3);
        try {
            workbook.write(baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            byte[] aa = baos.toByteArray();
            return aa;
        }
//        try {
//            OutputStream out = new FileOutputStream("E://shopSample.xls");
////            OutputStream out2 = new FileOutputStream("E://test1.xls");
////            biRepSupport.exportExcel(headers, dataset, out);
//              workbook.write(out);
////            biRepSupport.exportExcel(headers2, dataset2, out2);
//            out.close();
//            JOptionPane.showMessageDialog(null, "导出成功!");
//            System.out.println("excel导出成功！");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("process end!");
//        }
//        return null;

    }

    /**
     * 从数据库中读出一次能读出的数据
     * @param para
     * @return
     */
    public List<ShopSalesOfChannel010Model> getData(Map para)
    {
        //BiReportSalesProduct010Key key=new BiReportSalesProduct010Key();
        Map map1=new HashMap<String,Object>();
        Date staDate= null;
        Date endDate= null;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            staDate = sdf.parse((String)para.get("staDate"));
            endDate = sdf.parse((String)para.get("endDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        map1.put("staDate",staDate);
        map1.put("endDate",endDate);
        map1.put("nameCn",(String)para.get("nameCn"));
        List<ShopSalesOfChannel010Model> shopSalesList=biReportSalesShop010DaoExt.selectListByDate(map1);
        System.out.println(shopSalesList.size());
       /* for(ShopSalesOfChannel010Model s:shopSalesList)
        {
            System.out.println(s.getId());
        }*/
       return shopSalesList;
    }

    /**
     * 处理数据，第一次从数据中读出来的数据是不完整的
     * @param shopDaySalelist
     * @return
     */
    public List<ShopSalesOfChannel010Model> dealSaleShopModel(List<ShopSalesOfChannel010Model> shopDaySalelist)
    {
        List<ShopSalesOfChannel010Model> result=shopDaySalelist;
        Iterator<ShopSalesOfChannel010Model> iterator=result.iterator();
        while(iterator.hasNext())
        {
            Map mapForYTD= new HashMap<String,Object>();
            Map mapForMTD= new HashMap<String,Object>();
            Map mapForWTD= new HashMap<String,Object>();

            Map mapForSameDayLastYear= new HashMap<String,Object>();
            Map mapForYesterday= new HashMap<String,Object>();

            ShopSalesOfChannel010Model sscm=iterator.next();
            Integer id=sscm.getId();
            Date date=sscm.getDate();
            dateUtilHelper.setDate(date);
            Date theSameDayLastYear=dateUtilHelper.getTheSameDayLastYear();
            Date thisWeekFirstDay=dateUtilHelper.getNowWeekMonday();
            Date thisMonthFirstDay=dateUtilHelper.getTimesMonthmorning();
            Date yesterday=dateUtilHelper.getYesterday();
            //环比值
            mapForYesterday.put("shopId",id);
            mapForYesterday.put("staDate",yesterday);
            mapForYesterday.put("endDate",yesterday);
            BigDecimal yamt=biReportSalesShop010DaoExt.selectAmtDateToDate(mapForYesterday);
            sscm.setYamt(yamt);
            //同比值
            mapForSameDayLastYear.put("shopId",id);
            mapForSameDayLastYear.put("staDate",theSameDayLastYear);
            mapForSameDayLastYear.put("endDate",theSameDayLastYear);
            BigDecimal lastYearAmt=biReportSalesShop010DaoExt.selectAmtDateToDate(mapForSameDayLastYear);
            sscm.setLyamt(lastYearAmt);
            //WTD
            mapForWTD.put("shopId",id);
            mapForWTD.put("staDate",thisWeekFirstDay);
            mapForWTD.put("endDate",date);
            BigDecimal WTD=biReportSalesShop010DaoExt.selectAmtDateToDate(mapForWTD);
            sscm.setWTD(WTD);
            //MTD
            mapForMTD.put("shopId",id);
            mapForMTD.put("staDate",thisMonthFirstDay);
            mapForMTD.put("endDate",date);
            BigDecimal MTD=biReportSalesShop010DaoExt.selectAmtDateToDate(mapForMTD);
            sscm.setMTD(MTD);
            //YTD
            mapForYTD.put("shopId",id);
            mapForYTD.put("staDate",theSameDayLastYear);
            mapForYTD.put("endDate",date);
            BigDecimal YTD=biReportSalesShop010DaoExt.selectAmtDateToDate(mapForYTD);
            sscm.setYTD(YTD);

            //注解：c 为amt，e为yamt 环比，g 为lyamt 同比，
            //(c4-E4)/E4* 100 销售额-销售额同比 * 100 amt-yamt
            BigDecimal amt=sscm.getAmt();
            Integer uv=sscm.getUv();
            Integer oreders=sscm.getOrders();
            Integer buyers=sscm.getBuyers();

            //(c4 -e4)/e4
            sscm.setYamtRate((selfDivide(selfSub(amt,yamt),yamt))==null?null:(selfDivide(selfSub(amt,yamt),yamt).multiply(new BigDecimal(100))));
            sscm.setLyamtRate((selfDivide(selfSub(amt,lastYearAmt),lastYearAmt))==null?null:(selfDivide(selfSub(amt,lastYearAmt),lastYearAmt)).multiply(new BigDecimal(100)));
            sscm.setTranRate((selfDivide(oreders,uv)==null)?null:((selfDivide(oreders,uv))).multiply(new BigDecimal(100)));
            sscm.setCustomerAverageAmt(selfDivide(amt,buyers));
            //(C4-g4)/g4*100
            //(p4/m4* 100 订单数/uv
            //客单价 c4/O4 客单价
            //
        }
        return result;
    }
    public BigDecimal selfDivide(BigDecimal dividend,BigDecimal divisor)
    {
        if(divisor.compareTo(BigDecimal.ZERO)==0||divisor==null||dividend==null)
        {
            return null;
        }
        else {
            return dividend.divide(divisor,2,BigDecimal.ROUND_HALF_UP);
        }
    }

    public BigDecimal selfDivide(BigDecimal dividend,Integer divisor)
    {
        if(divisor==0||divisor==null||dividend==null)
        {
            return null;
        }
        else {
            BigDecimal bgdivisor=new BigDecimal(divisor);
            return dividend.divide(bgdivisor,2,BigDecimal.ROUND_HALF_UP);
        }
    }
    public BigDecimal selfDivide(Integer dividend,Integer divisor)
    {
        if(divisor==0||divisor==null||dividend==null)
        {
            return null;
        }
        else {
            return new BigDecimal((double)dividend/divisor).setScale(4,BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 获取合并区域信息
     * @return
     */
    public List<HeaderInfo> getMergeInfo()
    {
        List<HeaderInfo> mergeRangeList=new ArrayList();
        mergeRangeList.add(new HeaderInfo("店铺名称", new CellRangeAddress(0, 0, 0, 34)));
        mergeRangeList.add(new HeaderInfo("运营指标", new CellRangeAddress(1, 1, 2, 11)));
        mergeRangeList.add(new HeaderInfo("商品指标", new CellRangeAddress(1, 1, 12, 19)));
        mergeRangeList.add(new HeaderInfo("物流指标", new CellRangeAddress(1, 1, 20, 22)));
        mergeRangeList.add(new HeaderInfo("服务指标", new CellRangeAddress(1, 1, 23, 34)));
        return mergeRangeList;
    }
    /**
     * bigdemical自定义减法运算
     */
    public BigDecimal selfSub(BigDecimal subtrahend,BigDecimal subtractor)
    {
        if(subtractor==null &&subtrahend==null)
        {
            return null;
        }
        if(subtrahend!=null&&subtractor!=null)
        {
            return subtrahend;
        }
        if(subtrahend==null&&subtractor!=null)
        {
            return BigDecimal.ZERO.subtract(subtractor);
        }
        else
        {
            return subtrahend.subtract(subtractor);
        }
    }
}
