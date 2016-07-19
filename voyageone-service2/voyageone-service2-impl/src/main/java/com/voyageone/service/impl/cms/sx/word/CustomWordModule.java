package com.voyageone.service.impl.cms.sx.word;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.ims.rule_expression.CustomWord;
import com.voyageone.service.bean.cms.product.SxData;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.sx.rule_parser.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by morse.lu on 16-4-26.(copy from task2 and then modified)
 */
public abstract class CustomWordModule extends VOAbsLoggable {

    private String moduleName;

    public CustomWordModule(String moduleName) {
        this.moduleName = moduleName;
    }

    abstract public String parse(CustomWord customWord, ExpressionParser expressionParser, SxProductService sxProductService, SxData sxData, ShopBean shopBean, String user, String[] extParameter) throws Exception;
//    abstract public String parse(CustomWord customWord, ExpressionParser expressionParser, SxData sxData, ShopBean shopBean, String user, Set<String> imageSet);

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CustomWordModule))
            return false;

        CustomWordModule customWordModule = (CustomWordModule) obj;

        return customWordModule.getModuleName().equals(moduleName);

    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    // Add by desmond 2016/07/15 start
    /**
     * 取得scene7上图片的原图URL
     * 先根据图片名取得图片大小，然后根据图片大小取得原始图片URL
     *
     * @param imageName   String   图片名
     * @param moduleName  String   调用者名
     * @return String 原图URL
     */
    public String getPicOriUrl(String imageName, String moduleName) {
        String completeImageOriUrl = "";

        try {
            // 20160717 tom 换一种方法 START
//            String url = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?req=imageprops", imageName);
//            $info("[" + moduleName + "]取得图片大小url:" + url);
//            String result = HttpUtils.get(url, null);
//            result = result.substring(result.indexOf("image"));
//            String[] args = result.split("image\\.");
//            Map<String, String> picSizeMap = new HashMap<>();
//            for (String param : args) {
//                if (param.indexOf("=") > 0) {
//                    String[] keyVal = param.split("=");
//                    if (keyVal.length > 1) {
//                        picSizeMap.put(keyVal[0], keyVal[1]);
//                    } else {
//                        picSizeMap.put(keyVal[0], "");
//                    }
//                }
//            }
//            completeImageOriUrl = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&rgn=0,0,%s,%s", imageName, picSizeMap.get("width"), picSizeMap.get("height"));
            completeImageOriUrl = String.format("http://s7d5.scene7.com/is/image/sneakerhead/%s?fmt=jpg&scl=1&qlt=100", imageName);
            // 20160717 tom 换一种方法 END
            $info("[" + moduleName + "]取得原始图片url:" + completeImageOriUrl);
        } catch (Exception e) {
            throw new BusinessException("[\" + moduleName + \"]取得原始图片url失败!(imageName:" + imageName + ")");
        }

        return completeImageOriUrl;
    }
    // Add by desmond 2016/07/15 end
}
