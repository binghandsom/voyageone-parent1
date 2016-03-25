package com.voyageone.common.configs.Enums;

import com.voyageone.common.configs.beans.TypeBean;

import com.voyageone.common.configs.Types;

import java.util.List;

/**
 * Created by Jack on 4/14/2017.
 */

public class TypeConfigEnums {
    /**
     * 对应 com_mt_type 表中存在的所有配置名称
     */
    public enum MastType {
        /**
         * 快速检索
         */
    	quickFilter(1),
        /**
         * 快递方式
         */
    	shippingMethod(2),
        /**
         * 付款方式
         */
    	paymentMethod(3),
        /**
         * 订单状态
         */
    	orderStatus(5),
    	/**
    	 * 发票
    	 */
    	invoice(6),
    	/**
    	 * 锁单
    	 */
    	localShipOnHold(7),
    	/**
         * 客户付费
         */
    	freightByCustomer(8),
        /**
         * Cart
         */
    	shopCart(9),
    	/**
    	 * 国家
    	 */
    	country(10),
    	/**
    	 * 订单类型
    	 */
    	orderType(11),
    	/**
    	 * 地区类型
    	 */
    	areaType(12),
    	/**
    	 * 发货方式
    	 */
    	sendMethod(13),
    	/**
         * 物品状态
         */
    	reservationStatus(14),
        /**
         * 出入库种类
         */
    	transferType(15),
    	/**
    	 * 身份证绑定有无
    	 */
    	IdCardBinded(16),
    	/**
    	 * return状态
    	 */
    	returnStatus(17),
    	/**
    	 * return原因
    	 */
    	returnReason(18),
        /**
         * return快递公司
         */
        returnExpress(19),
        /**
         * return产品的好坏
         */
        returnCondition(20),
        /**
         * 语言种类
         */
        languageType(21),

        /**
         * 付款信息
         */
        payInfo(22),

        /**
         * returnSession状态
         */
        returnSessionStatus(23),

		/**
		 * returnSession状态
		 */
		stocktakeSessionStatus(24),

		/**
		 * 盘点类型
		 */
		stocktakeType(25),

		/**
		 * 盘点section状态
		 */
		stocktakeSectionStatus(26),

		/**
		 * 发票种类
		 */
		invoiceKind(27),

		/**
		 * 用户评价关键字
		 */
		ratingKeyword(28),

		/**
		 * 天猫退款状态
		 */
		tmallRefundStatus(29),

		/**
		 * 天猫退款类型
		 */
		tmallRefundKind(30),

		/**
		 * 快递公司编号
		 */
		expressCode(31),

		/**
		 * transfer状态
		 */
		transferStatus(32),

		/**
		 * transfer来源
		 */
		transferOrigin(33),

		/**
		 * 退货类型
		 */
		returnType(34),
    	
    	/**
		 * 赠品规则类型
		 */
		giftRuleType(35),

		/**
		 * 产品状态:cms2
		 */
		productStatus(44),

		/**
		 * 发布状态:cms2
		 */
		platFormStatus(45),

		/**
		 * 标签:cms2
		 */
		label(46),

		/**
		 * 价格种类:cms2
		 */
		priceType(47),

		/**
		 * 数量比较种类:cms2
		 */
		compareType(48),

		/**
		 * 错误类别:cms2
		 */
		errorType(55),

		/**
		 * 活动状态:cms2
		 */
		promotionStatus(56),

		/**
		 * 获取任务的优先顺序:cms2
		 */
		translateTask(66);

    	private int id;

		MastType(int id) {
			this.id = id;
		} 

		public int getId() {
			return id;
		}

		public TypeBean getBean() {
			return Types.getTypeBean(getId());
		}

		public TypeBean getBean(String lang) {
			return Types.getTypeBean(getId(), lang);
		}

		public List<TypeBean> getList() {
			return Types.getTypeList(getId());
		}

		public List<TypeBean> getList(String lang) {
			return Types.getTypeList(getId(), lang);
		}
	}
}
