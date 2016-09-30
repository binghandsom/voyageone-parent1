package com.voyageone.components.jumei.reponse;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.UnicodeUtil;

import java.io.IOException;
import java.util.Map;

/**
 * HtProductUpdateResponse 修改商品属性
 * @author peitao.sun, 2016/3/29
 * @version 2.0.0
 * @since 2.0.0
 */
public class HtProductUpdateResponse extends BaseJMResponse {

    private boolean is_Success;
    private String errorMsg;
    private String body;

    public boolean getIs_Success() {
        return is_Success;
    }

    public void setIs_Success(boolean is_Success) {
        this.is_Success = is_Success;
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
        try {
        Map<String, Object> map = JacksonUtil.jsonToMap(body);
        if (map.containsKey("error_code") && "0".equals(map.get("error_code"))) {
            this.setIs_Success(true);
        } else {
            this.setIs_Success(false);
            if (map.get("error_code") != null) {
                String error_code = StringUtils.toString(map.get("error_code"));
                StringBuffer sbMsg = new StringBuffer(" 修改商品属性(/v1/htProduct/update)时,发生错误[" + error_code + ":");
                switch (error_code) {
                    case "10002":
                        sbMsg.append("client_id,client_key,sign 认证失败");
                        break;
                    case "100001":
                        sbMsg.append("jumei_product_id 参数格式错误(大于0的整数)");
                        break;
                    case "100002":
                        sbMsg.append("update_data必须不为空的JSON,且有效字段至少1个");
                        break;
                    case "100003":
                        sbMsg.append("不存在的产品");
                        break;
                    case "100004":
                        sbMsg.append("主站产品名已存在");
                        break;
                    case "100005":
                        sbMsg.append("草稿产品名已存在");
                        break;
                    case "100006":
                        sbMsg.append("当前商品不允许编辑(状态错误或正在更新)");
                        break;
                    // 下列错误，传了字段才会检测
                    case "505":
                        sbMsg.append("未捕获到的错误（返回此code时，会在\"response\"字段，标注具体原因）");
                        break;
                    case "100010":
                        sbMsg.append("category_v3_4_id，分类id错误（请确认字段类型及分类在聚美是否存在）");
                        break;
                    case "100011":
                        sbMsg.append("brand_id，品牌id（请确认字段类型及品牌id在聚美是否存在，以及是否有品牌授权）");
                        break;
                    case "100012":
                        sbMsg.append("产品名(name)格式错误，不能出现容量、规格、颜色等信息，不能填写除了\"（）\" \"/\" \"+\" \"*\"这" +
                                "4种符号以外的特殊符号(如-，<>，·等) ，空格等符号必须是英文半角符号，套装产品名以“+”号连接");
                        break;
                    case "100013":
                        sbMsg.append("foreign_language_name，产品外文名格式错误");
                        break;
                    case "100014":
                        sbMsg.append("function_ids，产品功效ID错误");
                        break;
                    default:
                        sbMsg.append(map.get("reason").toString());
                }
                sbMsg.append("] ");
                this.setErrorMsg(sbMsg.toString() + this.body);
            }
        }
        this.body = body;
        } catch (Exception ex) {
            logger.error("setBody ",ex);
            this.setIs_Success(false);
            this.setErrorMsg("HtProductUpdateResponse 返回参数解析错误" + UnicodeUtil.decodeUnicode(this.body));
        }
    }

}
