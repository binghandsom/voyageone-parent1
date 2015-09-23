package com.voyageone.common.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.voyageone.common.configs.beans.ImsCategoryBean;
import com.voyageone.common.configs.dao.ImsCategoryDao;

@Service
public class ImsCategoryConfigs {

	private static Log logger = LogFactory.getLog(ImsCategoryConfigs.class);

	private static Map<Integer, ImsCategoryBean> categoryBeamMap = new HashMap<>();

	public static void init(ImsCategoryDao mtCategoryDao) {
		logger.info("主类目取得开始...");
		List<ImsCategoryBean> categoryBeans = mtCategoryDao.getMtCategories();

		// 设置属性层次关系.
		List<ImsCategoryBean> copyCategoryBeans = new ArrayList<ImsCategoryBean>(categoryBeans);

		ImsCategoryBean topBean = new ImsCategoryBean();

		List<ImsCategoryBean> topBeanList = new ArrayList<>();

		for (ImsCategoryBean mtCategoryBean : categoryBeans) {
			List<ImsCategoryBean> subCategoryBeans = new ArrayList<ImsCategoryBean>();
			for (Iterator assIterator = copyCategoryBeans.iterator(); assIterator.hasNext();) {

				ImsCategoryBean subBean = (ImsCategoryBean) assIterator.next();
				if (subBean.getParentCid() == mtCategoryBean.getCategoryId()) {
					subCategoryBeans.add(subBean);
				}

			}
			mtCategoryBean.setSubCategories(subCategoryBeans);
			categoryBeamMap.put(mtCategoryBean.getCategoryId(), mtCategoryBean);
			if (mtCategoryBean.getParentCid() == 0) {
				topBeanList.add(mtCategoryBean);
			}

		}

		topBean.setSubCategories(topBeanList);

		categoryBeamMap.put(0, topBean);
		logger.info("主类目取得结束...");
	}

	public static ImsCategoryBean getMtCategoryBeanById(int categoryId) {
		return categoryBeamMap.get(categoryId);
	}

}
