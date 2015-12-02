package com.voyageone.cms.utils;

import java.util.List;

import com.voyageone.cms.modelbean.PropertyRule;

public class RuleDecodeUtil {
	
	public static void decodeRule(List<PropertyRule> rules) {
		
		for (PropertyRule rule : rules) {
			StringBuilder decodeName = new StringBuilder();
			switch (rule.getProp_rule_name()) {
			case "devTipRule":
				rule.setDecodeRule(rule.getProp_rule_value());
				
				break;
			case "disableRule":
			
				rule.setDecodeRule("禁用规则: "+rule.getProp_rule_relationship_operator());
				break;
			case "maxImageSizeRule":
				decodeName.append("最大图片尺寸规则（宽度*高度）");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				rule.setDecodeRule(decodeName.toString());
				break;
			case "minImageSizeRule":
				decodeName.append("最小图片尺寸规则（宽度*高度）");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				rule.setDecodeRule(decodeName.toString());
				break;
			case "maxInputNumRule":
				decodeName.append("最多允许选择（或设置）的项目数");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				rule.setDecodeRule(decodeName.toString());
				break;
			case "minInputNumRule":
				decodeName.append("最少需要选择（或设置）的项目数");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				rule.setDecodeRule(decodeName.toString());
				break;
			case "maxLengthRule":
				decodeName.append("最大长度");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				
				rule.setDecodeRule(decodeName.toString());
				break;
			case "minLengthRule":
				decodeName.append("最小长度");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				
				rule.setDecodeRule(decodeName.toString());
				break;
			case "maxTargetSizeRule":
				decodeName.append("目标（文件）最大允许大小");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				
				rule.setDecodeRule(decodeName.toString());
				break;
			case "maxValueRule":
				decodeName.append("最大值（数字、小数、日期）");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				
				rule.setDecodeRule(decodeName.toString());
				break;
			
			case "minValueRule":
				decodeName.append("最小值（数字、小数、日期）");
				if ("include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（包括）");
				}else if ("not include".equals(rule.getProp_rule_exProperty())) {
					decodeName.append("（不包括）");
				}
				decodeName.append(rule.getProp_rule_value());
				rule.setDecodeRule(decodeName.toString());
				break;
			case "readOnlyRule":
				if ("true".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("禁止填写");
				}
				
				break;
			case "regexRule":
				if ("^\\d+(\\.\\d{1,2})?$".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("必须是数字，允许保留小数点之后最多两位");
				}else if ("yyyy-MM-dd".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("必须符合 [yyyy-MM-dd] 格式");
				}else if ("^[0-9]*$".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("必须是数字");
				}else if ("^.*\\.mp3$".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("必须以 [.mp3] 结尾");
				}else if ("^\\d+(\\.\\d+)?$".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("必须是数字，允许保留小数点之后任意位数");
				}else if ("^([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(\\.[0-9]{0,2})?$".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("必须符合 [999,999.99] 格式");
				}
				break;
			case "requiredRule":
				if ("true".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("必须填写");
				}
				break;
			case "tipRule":
				decodeName.append(rule.getProp_rule_value());
				if (rule.getProp_rule_url()!=null) {
					decodeName.append("("+rule.getProp_rule_url()+")");
				}
				rule.setDecodeRule(decodeName.toString());
				break;
			case "valueTypeRule":
				if ("long".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：很大的整数");
				}else if ("html".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：超文本（html）");
				}else if ("text".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：文本");
				}else if ("decimal".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：任意数字");
				}else if ("integer".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：整数");
				}else if ("textarea".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：多行文本");
				}else if ("url".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：超链接（url）");
				}else if ("time".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：时间类型");
				}else if ("date".equals(rule.getProp_rule_value())) {
					rule.setDecodeRule("允许输入类型：日期类型");
				}
				break;

			default:
				rule.setDecodeRule(rule.getProp_rule_value());
				break;
			}
			
		}
	}

}
