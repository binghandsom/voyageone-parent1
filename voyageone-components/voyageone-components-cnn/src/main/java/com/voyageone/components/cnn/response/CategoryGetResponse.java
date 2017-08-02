package com.voyageone.components.cnn.response;

import com.voyageone.components.cnn.response.data.CategoryGetResDataBean;

/**
 * 查询店铺内分类信息
 * 只返回指定的分类信息及其下级分类信息，（不包括再下一级的分类，若需要请重新指定父节点查询）
 * Created by morse on 2017/7/31.
 */
public class CategoryGetResponse extends CnnDataResponse<CategoryGetResDataBean> {
}
