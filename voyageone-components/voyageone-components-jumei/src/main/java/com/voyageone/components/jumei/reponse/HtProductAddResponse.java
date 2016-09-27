package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HtProductAddResponse(国际POP - 创建商品并同时创建Deal)
 *
 * @author desmond, 2016/5/19
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductAddResponse extends BaseJMResponse {
    private boolean is_Success;
    private String jumei_product_id;  // 聚美生成的产品Id
    private String jm_hash_id;  // 聚美生成的hash_id
    private String error_code;


    private String codes;
    private String errorMsg;
    private String body;
    private List<HtProductAddResponse_Spu> spus;


    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public boolean getIs_Success() {
        return is_Success;
    }

    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
    }

    public String getJumei_Product_Id() {
        return jumei_product_id;
    }

    public void setJumei_Product_Id(String jumei_product_id) {
        this.jumei_product_id = jumei_product_id;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getBody() {
        return body;
    }



    public void setBody(String body) throws IOException {
        error_code = "";
        codes = "";

        this.body = body;
        try {
            Map<String, Object> map = JacksonUtil.jsonToMap(body);
            // 取得error code
            if (map.containsKey("error")) {
                Map<String, Object> errorMap = (Map<String, Object>) map.get("error");
                if (errorMap.containsKey("code")) {
                    this.setError_code(String.valueOf(errorMap.get("code")));
                }
                if (errorMap.containsKey("codes")) {
                    Map<String, Object> codesMap = (Map<String, Object>)errorMap.get("codes");
                    codes = (codesMap.keySet()).toString();
                }

                if(!StringUtils.isNullOrBlank2(codes)) {
                    if (StringUtils.isNullOrBlank2(error_code)) {
                        error_code = codes;
                    } else {
                        error_code = error_code + "," + codes;
                    }
                }
            }


            // 取得聚美生成的产品Id jumei_product_id
            if (map.containsKey("product")) {
                Map<String, Object> productMap = (Map<String, Object>) map.get("product");
                if (productMap.containsKey("jumei_product_id")) {
                    this.setJumei_Product_Id(String.valueOf(productMap.get("jumei_product_id")));
                }
            }

            if(map.containsKey("spus"))
            {
                List<Map<String, Object>> spuList = (List<Map<String, Object>>)map.get("spus");
                spus = new ArrayList<>();
                for (Map<String, Object> spuMap: spuList) {
                    HtProductAddResponse_Spu spu = new HtProductAddResponse_Spu();
                    spu.setJumei_spu_no(spuMap.get("jumei_spu_no").toString());
                    spu.setPartner_spu_no(spuMap.get("partner_spu_no").toString());
                    Map<String, Object> skuInfo = (Map<String, Object>) spuMap.get("skuinfo");
                    spu.setPartner_sku_no(skuInfo.get("partner_sku_no").toString());
                    spu.setJumei_sku_no(skuInfo.get("jumei_sku_no").toString());
                    spus.add(spu);
                }



            }

            if(map.containsKey("dealInfo"))
            {
                List<Map<String, Object>> dealInfoList = (List<Map<String, Object>>)map.get("dealInfo");
                Map<String, Object> dealInfoMap = dealInfoList.get(0);
                if (dealInfoMap.containsKey("jumei_hash_id")) {
                    jm_hash_id = String.valueOf(dealInfoMap.get("jumei_hash_id"));
                }
            }
            // error_code为空 并且 聚美生成的产品Id不为空的时候，设为成功
            if (StringUtils.isEmpty(this.error_code) && !StringUtils.isEmpty(this.jumei_product_id)) {
                this.setIs_Success(true);
            } else {
                this.setIs_Success(false);
                StringBuffer sbMsg = new StringBuffer(" 创建商品并同时创建Deal(/v1/htProduct/addProductAndDeal)时,发生错误[" + this.error_code + ":");
                switch (this.error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100000":
                        sbMsg.append("product参数不是正确的JSON格式，并且不能为空");
                        break;
                    case "100001":
                        sbMsg.append("spus参数不是正确的JSON格式，并且不能为空");
                        break;
                    case "100002":
                        sbMsg.append("dealinfo参数不是正确的JSON格式，并且不能为空");
                        break;
                    case "100010":
                        sbMsg.append("开始时间不正确");
                        break;
                    case "100011":
                        sbMsg.append("结束时间不正确");
                        break;
                    case "100012":
                        sbMsg.append("结束时间不能小于开始时间");
                        break;
                    case "100013":
                        sbMsg.append("新建 deal 中start_time 和 end_time 有重叠");
                        break;
                    case "100014":
                        sbMsg.append("Deal 的 start_time,end_time 和数据库中的 start_time,end_time 有重叠");
                        break;
                    case "100015":
                        sbMsg.append("入参Shipping_system_id不是正确的格式");
                        break;
                    case "100016":
                        sbMsg.append("在聚美不存在的Shipping_system_id");
                        break;
                    case "100018":
                        sbMsg.append("产品短标题(product_short_name)长度为：0<长度<=15字");
                        break;
                    case "100020":
                        sbMsg.append("产品中标题(product_medium_name)长度为：0<长度<=35字");
                        break;
                    case "100022":
                        sbMsg.append("产品长标题(product_long_name)长度为：0<长度<=130字");
                        break;
                    case "100024":
                        sbMsg.append("1,本单详情(description_properties)不能为空;2,CSS语法错误,出现}},不要用Id定义CSS,CSS不能" +
                                "直接使用标签定位,自定义的CSS没有包含XX,CSS中不能导入其他CSS,样式中不能使用聚美外部连接;3,HTML,标签href属性" +
                                "禁止使用javascript代码,不能使用外链 ");
                        break;
                    case "100027":
                        sbMsg.append("1,使用方法(description_usage)不能为空;2,使用方法(description_usage)CSS语法错误,出现}},不要" +
                                "用Id定义CSS,CSS不能直接使用标签定位,自定义的CSS没有包含XX,CSS中不能导入其他CSS,样式中不能使用聚美外部" +
                                "连接;3,使用方法(description_usage)HTML,标签href属性禁止使用javascript代码,不能使用外链");
                        break;
                    case "100028":
                        sbMsg.append("1,商品实拍(description_images)不能为空;2,商品实拍(description_images)CSS语法错误,出现}},不要" +
                                "用Id定义CSS,CSS不能直接使用标签定位,自定义的CSS没有包含XX,CSS中不能导入其他CSS,样式中不能使用聚美外部" +
                                "连接;3,商品实拍(description_images)HTML,标签href属性禁止使用javascript代码,不能使用外链");
                    case "100029":
                        sbMsg.append("生产地区(address_of_produce)不能为空,并且不能超过150个字");
                        break;
                    case "100030":
                        sbMsg.append("保质期限(before_date)不能为空,并且不能超过150个字");
                        break;
                    case "100032":
                        sbMsg.append("适用人群(suit_people)不能为空,并且不能超过150个字");
                        break;
                    case "100034":
                        sbMsg.append("特殊说明(special_explain)不能为空");
                    case "100035":
                        sbMsg.append("特殊说明长度不能超过150个字符");
                        break;
                    case "100043":
                        sbMsg.append("商品实拍(description_images)不存在");
                        break;
                    case "100045":
                        sbMsg.append("保质期限(before_date)不存在");
                        break;
                    case "100047":
                        sbMsg.append("特殊说明(special_explain)不存在");
                    case "102056":
                        sbMsg.append("partner_deal_id不能为空");
                        break;
                    case "102058":
                        sbMsg.append("partner_sku_no不能为空");
                        break;
                    case "102062":
                        sbMsg.append("商家编码(businessman_num)不能为空，并且必须唯一");
                        break;
                    case "102063":
                        sbMsg.append("在聚美已存在的商家编码(businessman_num)");
                    case "102064":
                        sbMsg.append("商家商品编码(upc_code)不能为空");
                        break;
                    case "102065":
                        sbMsg.append("Sku库存(stocks)不能为空,并且必须为数字");
                        break;
                    case "102066":
                        sbMsg.append("Deal sku（deal_price） 价格不能为空，并且不能小于15，并且不能大于市场价（market_price）");
                        break;
                    case "102071":
                        sbMsg.append("市场价(market_price)不能为空，并且不能小于15，并且不能小于团购价（deal_price）");
                    case "103077":
                        sbMsg.append("品牌 ID(brand_id)不能为空");
                        break;
                    case "103079":
                        sbMsg.append("不是聚美的品牌 ID(brand_id)");
                        break;
                    case "103080":
                        sbMsg.append("产品英文名称(foreign_language_name)不能为空,长度不能超过100字");
                        break;
                    case "103083":
                        sbMsg.append("产品名称(name)不能为空,长度不能大于100字,包含不被允许的字符");
                    case "103087":
                        sbMsg.append("产品名称(name)在聚美已存在");
                        break;
                    case "103089":
                        sbMsg.append("聚美在处理上级分类时出错(上级分类可能被删除、禁用；提示此错误建议重新获取分类信息)");
                        break;
                    case "103095":
                        sbMsg.append("分类四级 ID 只能是大于0的整数");
                        break;
                    case "103096":
                        sbMsg.append("分类四级 ID 在聚美不存在,或者不是四级分类ID（提示此错误建议重新获取分类信息）");
                    case "103097":
                        sbMsg.append("功效(function_ids)格式错误");
                        break;
                    case "103098":
                        sbMsg.append("不是聚美的功效(function_ids)");
                        break;
                    case "105104":
                        sbMsg.append("SPU_ID不能为空");
                        break;
                    case "105106":
                        sbMsg.append("商品自带条码（UPC_CODE）存在相同 值或UPC_CODE在聚美已存在");
                    case "105107":
                        sbMsg.append("子型号规格不在设置范围内('FORMAL','MS','OTHER')");
                        break;
                    case "105109":
                        sbMsg.append("尺寸(子型号容量)不能为空");
                        break;
                    case "105111":
                        sbMsg.append("海外官网价格不能为空,正确格式如：23.45，并且不能小于0");
                        break;
                    case "105115":
                        sbMsg.append("海外地址(abroad_url)必须是正确的url格式");
                    case "105117":
                        sbMsg.append("地区(area_code)不存在");
                        break;
                    case "105118":
                        sbMsg.append("不是聚美的地区(area_code)货币类型");
                        break;
                    default:
                        sbMsg.append(map.get("reason").toString());
                }
                sbMsg.append("] ");
                this.setErrorMsg(sbMsg.toString() + this.body);
            }
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("HtProductAddResponse 返回参数解析错误" + this.body);
        }
    }

    public String getJm_hash_id() {
        return jm_hash_id;
    }

    public void setJm_hash_id(String jm_hash_id) {
        this.jm_hash_id = jm_hash_id;
    }

    public List<HtProductAddResponse_Spu> getSpus() {
        return spus;
    }

    public void setSpus(List<HtProductAddResponse_Spu> spus) {
        this.spus = spus;
    }
}
