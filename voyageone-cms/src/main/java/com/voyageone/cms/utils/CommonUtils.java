package com.voyageone.cms.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;

import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import com.voyageone.cms.annotation.Extends;
import com.voyageone.cms.annotation.Table;
import com.voyageone.common.util.StringUtils;

public class CommonUtils {

	/**
	 * 两个对象进行合并 把parameter1里面属性是null或者是Empty 从parameter2中去取得并赋予parameter1
	 * 
	 * @param parameter1
	 * @param parameter2
	 * @return
	 * @throws Exception
	 */
	public static <T, K> T merger(T parameter1, K parameter2) {
		if (parameter1 == null || parameter2 == null) {
			return parameter1;
		}
		Class c1 = parameter1.getClass();
		Class c2 = parameter2.getClass();
		Field[] fields1 = c1.getDeclaredFields();
		for (Field f : fields1) {
			if (null == f.getAnnotation(Extends.class)) {
				continue;
			}
			Object fValue1;
			Object fValue2;
			try {
				fValue1 = getMethod(parameter1, f);
				if (fValue1 == null || StringUtils.isEmpty(fValue1.toString())) {
					fValue2 = getMethod(parameter2, f);
					setMethod(parameter1, f, fValue2);
				}
			} catch (Exception e) {

			}

		}
		return parameter1;
	}

	public static Object getMethod(Object object, Field field) throws Exception, NoSuchMethodException {

		Method m;
		if (field.getGenericType().toString().equals("boolean")) {
			if (field.getName().substring(0, 2).equals("is")) {
				m = object.getClass().getMethod(field.getName());
			} else {
				m = object.getClass().getMethod("is" + StringUtils.uppercaseFirst(field.getName()));
			}
		} else {
			m = object.getClass().getMethod("get" + StringUtils.uppercaseFirst(field.getName()));
		}
		Object value = m.invoke(object); // 调用getter方法获取属性值
		if (value != null) {
			return value;
		}
		return null;
	}

	public static void setMethod(Object object, Field field, Object value) throws Exception {
		Method m = object.getClass().getMethod("set" + StringUtils.uppercaseFirst(field.getName()), field.getType());
		m.invoke(object, value);
	}

	/**
	 * 比较两个bean中属性 把属性值不一样的抽出返回这些属性所存放的数据库表名
	 * 
	 * @param object1
	 * @param object2
	 * @return
	 * @throws Exception
	 */
	public static <T, K, V> List<String> compareBean(T object1, K object2, V object3) throws Exception {
		Class c1 = object1.getClass();
		Class c2 = object2.getClass();
		Class c3 = object3.getClass();
		List<String> dif = new ArrayList<String>();
		Field[] fields1 = c1.getDeclaredFields();
		for (Field f : fields1) {
			Table dbtable = f.getAnnotation(Table.class);
			if (dbtable == null) {
				continue;
			}
			Object fValue1;
			Object fValue2;
			try {
				fValue1 = getMethod(object1, f);
				fValue2 = getMethod(object2, f);
				if (fValue1 == null && fValue2 == null) {
					continue;
				} else if ((fValue1 == null && fValue2 != null) || (fValue1 != null && fValue2 == null) || !fValue1.toString().equals(fValue2.toString())) {
					if (!dif.contains(dbtable.name())) {
						dif.add(dbtable.name());
					}
					setMethod(object3, f, fValue1);
				}
			} catch (NoSuchMethodException e) {

			}
		}
		return dif;
	}

	/**
	 * 找出至少有一个字段不是null的表
	 * 
	 * @param object1
	 * @return 返回结果中不存在的表说明是要删除的
	 * @throws Exception
	 */
	public static <T> List<String> searchNotNullTable(T object1) throws Exception {
		List<String> notNullTables = new ArrayList<String>();
		Field[] fields1 = object1.getClass().getDeclaredFields();
		for (Field f : fields1) {
			Table dbtable = f.getAnnotation(Table.class);
			if (dbtable == null) {
				continue;
			}
			if (!notNullTables.contains(dbtable.name())) {
				if (getMethod(object1, f) != null && !"".equals(getMethod(object1, f).toString())) {
					notNullTables.add(dbtable.name());
				}
			}
		}
		return notNullTables;
	}

	/**
	 * 判断Bean的值是否为空
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static <T> boolean isNullBean(T object) throws Exception {
		boolean flag = false;
		Field[] field = object.getClass().getDeclaredFields();
		for (Field f : field) {
			if (null == f.getAnnotation(Extends.class)) {
				continue;
			}
			Object value = getMethod(object, f);
			if (String.valueOf(value).equals("")) {
				flag = true;
			} else {
				flag = false;
				break;
			}
		}
		return flag;

	}

	/**
	 * 比较两个Bean，结果给另一个bean赋值
	 * 
	 * @param obj1
	 * @param obj2
	 * @param obj3
	 * @return
	 * @throws Exception
	 */
	public static <T, K, P> T packingBean(T obj1, K obj2, P obj3) throws Exception {
		Field[] field1 = obj1.getClass().getDeclaredFields();
		Field[] field2 = obj2.getClass().getDeclaredFields();
		Field[] field3 = obj3.getClass().getDeclaredFields();

		for (Field f : field2) {
			for (Field f1 : field1) {
				if (f1.getName().equals(f.getName())) {
					Object value1 = getMethod(obj2, f);
					Object value2 = getMethod(obj3, f);
					if (isChanged(String.valueOf(value1), String.valueOf(value2))) {
						setMethod(obj1, f, value1);
					}
				}
			}
		}
		return obj1;

	}

	/**
	 * 拷贝obj1到obj2
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 * @throws Exception
	 */
	public static <T, K> K copyBean(T obj1, K obj2) throws Exception {
		Field[] field1 = obj1.getClass().getDeclaredFields();
		Field[] field2 = obj2.getClass().getDeclaredFields();
		for (Field f : field1) {
			for (Field f1 : field2) {
				if (f1.getName().equals(f.getName())) {
					Object value1 = getMethod(obj1, f);

					setMethod(obj2, f, value1);
				}
			}
		}
		return obj2;

	}

	/**
	 * 判断属性内容是否改变
	 * 
	 * @param changeValue
	 * @param orgValue
	 * @return
	 */
	private static boolean isChanged(String changeValue, String orgValue) {
		boolean flag = true;
		changeValue = StringUtils.null2Space(changeValue);
		orgValue = StringUtils.null2Space(orgValue);

		if (changeValue.equals(orgValue)) {
			flag = false;
		}
		return flag;

	}

	public static double round(double value) {
		BigDecimal bg = new BigDecimal(value);
		return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 图片的上传
	 * 
	 * @param dirname
	 *            保存目录
	 * @param rawFilename
	 *            保存文件名
	 * @param rawFile64
	 *            文件内容
	 * 
	 * @return int
	 */
	public static int uploadImage(String dirname, String rawFilename, String rawFile64) {
		int uploadResult = 0;
		try {
			// 目标文件夹
			File needDir = new File(dirname);
			// 目标文件夹不存在的场合，新建文件夹
			if (!needDir.exists()) {
				needDir.mkdirs();
			}

			// 前端未压缩过的上传方法
			String saveFileName = dirname + rawFilename;

			try (OutputStream out = new FileOutputStream(saveFileName)) {
				BASE64Decoder decoder = new BASE64Decoder();
				// Base64解码(开始一段图片信息说明要去除)
				byte[] b = decoder.decodeBuffer(rawFile64.substring(rawFile64.indexOf(",") + 1));
				for (int i = 0; i < b.length; ++i) {
					if (b[i] < 0) {// 调整异常数据
						b[i] += 256;
					}
				}
				// 生成jpeg图片
				out.write(b);
				out.flush();
			} catch (Exception e) {
				uploadResult = 1;
			}
		} catch (Exception e) {
			uploadResult = 2;
		}

		return uploadResult;
	}

	/**
	 * 删文件
	 * 
	 * @param sPath
	 * @return
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	public static boolean createCSVFile(List<String> head, List<List<Object>> dataList, String outPutPath, String filename) {
		boolean isSuccess = false;
		File csvFile = null;
		try {
			// 1. 创建文件
			csvFile = new File(outPutPath + File.separator + filename + ".csv");
			File parent = csvFile.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			csvFile.createNewFile();
			// =========== 2. 写出CSV文件 ============//
			CsvWriterSettings writerSettings = new CsvWriterSettings();
			String strings[] = new String[head.size()];
			for (int i = 0, j = head.size(); i < j; i++) {
				strings[i] = head.get(i);
			}
			writerSettings.setHeaders(strings);
			CsvWriter writer = new CsvWriter(new FileWriter(csvFile), writerSettings);
			writer.writeRows(dataList);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return false;
	}

}
