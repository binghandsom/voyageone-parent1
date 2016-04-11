package com.voyageone.components.gilt.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.gilt.GiltBase;
import com.voyageone.components.gilt.bean.GiltPageGetSkusRequest;
import com.voyageone.components.gilt.bean.GiltSale;
import com.voyageone.components.gilt.bean.GiltSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author aooer 2016/2/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class GiltSkuService extends GiltBase {

    private static final String URI = "skus";

    @Autowired
    private GiltSalesService giltSalesService;

    /**
     * @deprecated
     *  内存，网络原因不推荐使用此方法(耗时)，请使用getSalesSkuIds()方法
     *  获取Sales的所有Skus

     * @return List<GiltSku>
     * @throws Exception
     */
    public List<GiltSku> getAllSalesSkus() throws Exception {
        //所有的skuId
        Set<Long> skuIds=getSalesSkuIds();
        //返回Sku集合
        List<GiltSku> result=new ArrayList<GiltSku>();
        //每100个skuIds调用方法查询skus信息
        StringBuilder tempSkuIds=new StringBuilder();
        int i=0;
        for (Long skuId : skuIds){
            tempSkuIds.append(skuId);
            if(++i%100==0){
                result.addAll(getSkus(tempSkuIds.toString()));
                //重置变量
                tempSkuIds=new StringBuilder();
            }else
                tempSkuIds.append(",");
        }
        // 捡漏
        if(!StringUtils.isNullOrBlank2(tempSkuIds.toString())){
            GiltPageGetSkusRequest request=new GiltPageGetSkusRequest();
            //去除最后一个‘，’符合，如果有的话
            if(tempSkuIds.toString().endsWith(","))
                request.setSku_ids(tempSkuIds.substring(0,tempSkuIds.length()-2));
            else
                request.setSku_ids(tempSkuIds.toString());
            result.addAll(pageGetSkus(request));
        }
        return result;
    }

    /**
     *  获取Sales下所有SkuIds

     * @return Set<SkuId>
     */
    public Set<Long> getSalesSkuIds() throws Exception {
        //sales
        List<GiltSale> sales=giltSalesService.getAllSales();
        //所有的skuId
        Set<Long> skuIds=new HashSet<Long>();
        for (GiltSale sale:sales){
            skuIds.addAll(sale.getSku_ids());
        }
        return skuIds;
    }

    /**
     * 根据SkuIds获取Skus

     * @param skuIds skuIds
     * @return List<GiltSku>
     * @throws Exception
     */
    public List<GiltSku> getSkus(String skuIds) throws Exception {
        if(StringUtils.isNullOrBlank2(skuIds))
            throw new IllegalArgumentException("skuIds不能为空");
        if(skuIds.split(",").length>100)
            throw new IllegalArgumentException("skuIds最多只能设置100个");
        GiltPageGetSkusRequest request=new GiltPageGetSkusRequest();
        request.setSku_ids(skuIds);
        return pageGetSkus(request);
    }

    /**
     *  分页获取Skus

     * @param request request
     * @return List
     * @throws Exception
     */
    public List<GiltSku> pageGetSkus(GiltPageGetSkusRequest request) throws Exception {
        request.check();
        String result=reqGiltApi(URI,request.getBeanMap());
        return JacksonUtil.jsonToBeanList(result,GiltSku.class);
    }

    /**
     *  根据Id 获取Sku

     * @param skuId skuId
     * @return GiltSku
     * @throws Exception
     */
    public GiltSku getSkuById(String skuId) throws Exception {
        if(StringUtils.isNullOrBlank2(skuId))
            throw new IllegalArgumentException("skuId不能为空");
        String result=reqGiltApi( URI +"/"+skuId,new HashMap<String,String>());
        return JacksonUtil.json2Bean(result,GiltSku.class);
    }

}
