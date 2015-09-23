package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.ProductBean;
import com.voyageone.batch.ims.modelbean.WorkLoadBean;
import com.voyageone.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ProductDao")
public class ProductDao extends BaseDao {

	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 根据条件{channel_id, code, cart_id}查找product，对应表ims_bt_product
	 * @param productBean
	 * 读取记录条数
	 * @return
	 * List<ProductBean>
	 */
	public List<ProductBean> selectProductsByCriteria(ProductBean productBean) {
		return (List<ProductBean>)(List) selectList(Constants.DAO_NAME_SPACE_IMS + "ims_bt_product_select_by_criteria", productBean);
	}
 }
