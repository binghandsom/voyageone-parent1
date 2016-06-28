package com.voyageone.web2.cms.views.tools.product;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gjl on 2016/6/7.
 */
@Service
public class CmsHsCodeService extends BaseAppService {

    @Autowired
    private ProductService productService;
    @Autowired
    ProductGroupService productGroupService;
    /**
     * 设定返回值.
     */
    private static String[] RET_FIELDS = {
            "common.fields.code",
            "common.fields.productNameCn",
            "common.fields.materialCn",
            "common.fields.hsCodePrivate",
            "common.fields.hsCodeCrop",
            "common.fields.images1",
            "common.fields.hsCodeStatus",
            "common.fields.hsCodeSetter",
            "common.fields.hsCodeSetTime",
    };
    /**
     * HsCode信息检索
     * @param lang
     * @param channelId
     * @param param
     * @return data
     */
    public Map<String, Object> searchHsCodeInfo(String lang, String channelId,String userName,Map param) {
        //返回数据类型
        Map<String, Object> data = new HashMap<>();
        /**
         * 商品税号设置状态,传入的参数
         * channelId(当前用户信息)
         * hsCodePrivate(userName)
         * hsCodeStatus(0未设置 1已设置)
         * translateStatus(0未设置 1已设置)
         * hsCodeSetter(null未设置 notNull已设置)
        */
        //当前日期及时间
        String hsCodeSetTime = DateTimeUtil.getNowTimeStamp();
        //未设置总数:等待设置税号的商品总数（包含 未分配+已分配但是已过期）
        data.put("notAssignedTotalHsCodeCnt", productService.getTotalHsCodeCnt(channelId, "", "0","1",""));
        //已分配但未完成总数:已经被分配，但是未过期（无法释放）的商品总数
        data.put("alreadyAssignedTotalHsCodeCnt", productService.getTotalHsCodeCnt(channelId, "notNull", "0","1","notNull"));
        //设置税号商品总数:当前Channel已经被设置税号的商品总数
        data.put("setChannelTotalHsCodeCnt", productService.getTotalHsCodeCnt(channelId, "notNull", "1","1","notNull"));
        //个人设置税号商品译数:当前用户税号设置总数
        data.put("setPersonalTotalHsCodeCnt", productService.getTotalHsCodeCnt(channelId, userName, "1","1","notNull"));

        /**
         *个人设置税号成果搜索
         * hsCodeStatus(0未设置 1已设置)
         * searchCondition(模糊查询(ProductCode，商品名称))
         */
        //税号设置状态
        String hsCodeStatus=(String) param.get("hsCodeStatus");
        //模糊查询(ProductCode，商品名称)
        String condition = (String) param.get("searchCondition");

        //显示第几页
        int curr = (Integer) param.get("curr");
        //每页显示的数目
        int size = (Integer) param.get("size");
        //设置税一览的信息
        data.put("hsCodeList", productService.getTotalHsCodeList(channelId, userName, hsCodeStatus, condition, curr, size, RET_FIELDS));
        //税号个人
        data.put("hsCodeValue", TypeChannels.getTypeWithLang("hsCodePrivate", channelId, lang));
        //返回数据类型
        return data;
    }
    /**
     * 获取任务
     * @param lang
     * @param channelId
     * @param userName
     * @param param
     * @return data
     */
    public Map<String, Object> getHsCodeInfo(String lang, String channelId, String userName, Map param) {
        //返回数据类型
        Map<String, Object> data = new HashMap<>();

        /**
         * 获取任务,传入的参数
         * qty(库存)
         * order(升序 降序)
         * code(当前商品code)
         * hsCodeTaskCnt(获取任务数)
         */
        //优先规则
        String qty = (String) param.get("qty");
        //循序(升序 降序)
        String order =(String) param.get("order");
        //商品Code
        String code =(String) param.get("code");
        //获取任务数
        int hsCodeTaskCnt=(Integer) param.get("hsCodeTaskCnt");
        //HsCodeCheck
        checkHsCode(hsCodeTaskCnt, lang);
        //显示第几页
        int curr = (Integer) param.get("curr");
        //每页显示的数目
        int size = (Integer) param.get("size");
        //主数据
        int cartId = 1;
        //根据获取任务数去取得对应的code
        List<CmsBtProductModel> hsCodeList =productService.getHsCodeInfo(channelId, "", userName,hsCodeTaskCnt,RET_FIELDS);
        //取得codeList结果集
        List<String> codeList = new ArrayList<>();
        //取得获取任务的信息
        if(hsCodeList.size()>0){
            for(CmsBtProductModel model:hsCodeList){
                codeList.add(model.getFields().getCode());
            }
        }
        //根据获取任务的主code同步到master同一个Group下所有code
        List<String> allCodeList=getAllCodeList(codeList, channelId, cartId);
        //当前日期及时间
        String hsCodeSetTime = DateTimeUtil.getNowTimeStamp();
        //更新cms_bt_product表的hsCodeInfo
        productService.updateHsCodeInfo(channelId, allCodeList, userName,"",hsCodeSetTime);
        //等待设置税一览
        data.put("hsCodeList", productService.getTotalHsCodeList(channelId, "", userName, "", curr, size, RET_FIELDS));
        //返回数据类型
        return data;
    }

    /**
     * 获取任务最好是加上最大值check，默认为10，最大不能超过50
     * @param hsCodeTaskCnt
     * @param lang
     */
    public void  checkHsCode(int hsCodeTaskCnt, String lang){
        //选择个数判断
        if(hsCodeTaskCnt>50){
            // 类目选择check
            throw new BusinessException("获取任务最好是加上最大值check，默认为10，最大不能超过50",50);
        }
    }
    /**
     * 根据codeList取得cms_bt_product_group相关的code
     */
    public List<String> getAllCodeList(List<String> codeList,String channelId,int cartId){
        List<String> allCodeList = new ArrayList<>();
        // 获取产品对应的group信息
        for(String allCode:codeList){
            //循环取得allCode
            CmsBtProductGroupModel groupModel = productGroupService.selectProductGroupByCode(channelId, allCode, cartId);
            if(groupModel.getProductCodes().size()>0){
                //循环取单条code
                for(String code:groupModel.getProductCodes()){
                    allCodeList.add(code);
                }
            }

        }
        return allCodeList;
    }
    /**
     * 保存任务
     * @param channelId
     * @param userName
     * @param param
     */
    public Map<String, Object> saveHsCodeInfo(String channelId, String userName, Map param) {
        String code=(String) param.get("code");
        String hsCodeStatus = "1";
        //当前日期及时间
        String hsCodeSetTime = DateTimeUtil.getNowTimeStamp();
        //主数据
        int cartId = 1;
        List<String> codeList = new ArrayList<>();
        codeList.add(code);
        //根据获取任务的主code同步到master同一个Group下所有code
        List<String> allCodeList=getAllCodeList(codeList, channelId, cartId);
        //更新cms_bt_product表的hsCodeInfo
        productService.updateHsCodeInfo(channelId, allCodeList, userName, hsCodeStatus, hsCodeSetTime);
        return null;
    }
}
