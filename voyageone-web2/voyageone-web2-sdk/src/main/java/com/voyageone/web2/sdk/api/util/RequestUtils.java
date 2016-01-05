package com.voyageone.web2.sdk.api.util;

import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;

import java.util.Collection;

public class RequestUtils {

	public static final String ERROR_CODE_ARGUMENTS_MISSING = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70005.getErrorCode();
	public static final String ERROR_CODE_ARGUMENTS_INVALID = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70006.getErrorCode();

//	public static void checkNotEmpty(Object value, String fieldName) throws ApiRuleException {
//		if (value == null) {
//			throw new ApiRuleException(ERROR_CODE_ARGUMENTS_MISSING, "client-error:Missing required arguments:" + fieldName + "");
//		}
//		if (value instanceof String) {
//			if (((String) value).trim().length() == 0) {
//				throw new ApiRuleException(ERROR_CODE_ARGUMENTS_MISSING, "client-error:Missing required arguments:" + fieldName + "");
//			}
//		}
//	}

	public static void checkNotEmpty(String fieldName, Object... args) throws ApiRuleException {
		if (args.length == 0)
			return;

		boolean isNotEmpty = false;
		for (Object value : args) {
			if (value != null) {
				if (value instanceof String) {
					if (((String) value).trim().length() > 0) {
						isNotEmpty = true;
						break;
					}
				} else if (value instanceof Integer || value instanceof Long || value instanceof Double) {
					if (value.toString().trim().length() > 0) {
						isNotEmpty = true;
						break;
					}
				} else if (value instanceof Collection) {
					Collection listValue = (Collection)value;
					if (listValue.size() > 0) {
						boolean isNotEmptyList = false;
						for (Object cellValue : listValue) {
							if (cellValue instanceof String) {
								if (((String) cellValue).trim().length() > 0) {
									isNotEmptyList = true;
									break;
								}
							} else if (cellValue instanceof Integer || cellValue instanceof Long || cellValue instanceof Double) {
								if (cellValue.toString().trim().length() > 0) {
									isNotEmptyList = true;
									break;
								}
							} else {
								if (cellValue != null) {
									isNotEmptyList = true;
									break;
								}
							}
						}
						if (isNotEmptyList) {
							isNotEmpty = true;
							break;
						}
					}
				} else {
					if (value != null) {
						isNotEmpty = true;
						break;
					}
				}
			}
		}
		if (!isNotEmpty) {
			throw new ApiRuleException(ERROR_CODE_ARGUMENTS_MISSING, "client-error:Missing required arguments:" + fieldName + "");
		}

	}

	public static void checkMaxLength(String value, int maxLength, String fieldName) throws ApiRuleException {
		if (value != null) {
			if (value.length() > maxLength) {
				throw new ApiRuleException(ERROR_CODE_ARGUMENTS_INVALID, "client-error:Invalid arguments:the string length of " + fieldName + " can not be larger than " + maxLength + ".");
			}
		}
	}

//	public static void checkMaxLength(FileItem fileItem, int maxLength, String fieldName) throws ApiRuleException {
//		try {
//			if (fileItem != null && fileItem.getContent() != null) {
//				if (fileItem.getContent().length > maxLength) {
//					throw new ApiRuleException(ERROR_CODE_ARGUMENTS_INVALID, "client-error:Invalid arguments:the file size of " + fieldName + " can not be larger than " + maxLength + ".");
//				}
//			}
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
//	}

	public static void checkMaxListSize(String value, int maxSize, String fieldName) throws ApiRuleException {
		if (value != null) {
			String[] list = value.split(",");
			if (list.length > maxSize) {
				throw new ApiRuleException(ERROR_CODE_ARGUMENTS_INVALID, "client-error:Invalid arguments:the array size of " + fieldName + " must be less than " + maxSize + ".");
			}
		}
	}

//	public static void checkObjectMaxListSize(String value, int maxSize, String fieldName) throws ApiRuleException {
//		if (value != null) {
//			JSONReader reader = new JSONValidatingReader();
//			Object obj = reader.read(value);
//			if (obj instanceof List<?> && ((List<?>) obj).size() > maxSize) {
//				throw new ApiRuleException(ERROR_CODE_ARGUMENTS_INVALID, "client-error:Invalid arguments:the array size of " + fieldName + " must be less than " + maxSize + ".");
//			}
//		}
//	}

	public static void checkMaxValue(Long value, long maxValue, String fieldName) throws ApiRuleException {
		if (value != null) {
			if (value > maxValue) {
				throw new ApiRuleException(ERROR_CODE_ARGUMENTS_INVALID, "client-error:Invalid arguments:the value of " + fieldName + " can not be larger than " + maxValue + ".");
			}
		}
	}

	public static void checkMinValue(Long value, long minValue, String fieldName) throws ApiRuleException {
		if (value != null) {
			if (value < minValue) {
				throw new ApiRuleException(ERROR_CODE_ARGUMENTS_INVALID, "client-error:Invalid arguments:the value of " + fieldName + " can not be less than " + minValue + ".");
			}
		}
	}

	public static String addProp(String props, String key, Object value) {
		String temp = null;
		if (value instanceof String) {
			temp = "\"%s\" : \"%s\"";
		} else if (value instanceof Integer
				|| value instanceof Long
				|| value instanceof Double) {
			temp = "\"%s\" : %s";
		} else {
			VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70004;
			throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
		}

		String propValue = String.format(temp, key, value.toString());

		String result = null;
		if (!StringUtils.isEmpty(props)) {
			result = props + " ; " + propValue;
		} else {
			result = propValue;
		}
		return result;
	}
}
