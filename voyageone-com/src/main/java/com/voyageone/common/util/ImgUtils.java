package com.voyageone.common.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public final class ImgUtils {

	/**
	 * 图片流取得
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param imgPathPara 图片路径
	 */
	public static boolean getPicStream(HttpServletRequest request,
									   HttpServletResponse response,
									   String imgPathPara) throws Exception {
		ServletOutputStream out = null;
		InputStream ips = null;
		try {

			if (isLocalPath(imgPathPara)) {
				// 本地文件的场合
				ips = new FileInputStream(new File(imgPathPara));
			} else {
				// 网络文件的场合
				URL url = new URL(imgPathPara);
				HttpURLConnection  httpUrl = (HttpURLConnection) url.openConnection();
				httpUrl.connect();

				ips = new BufferedInputStream(httpUrl.getInputStream());
			}

			response.setContentType("multipart/form-data");
			out = response.getOutputStream();

			//	读取文件流
			int i;
			byte[] buffer = new	byte[4096];

			while((i = ips.read(buffer)) != -1) {
				out.write(buffer, 0, i);
			}

			out.flush();
			ips.close();

			return true;
		} finally {
			if (out != null) {
				out.close();
			}

			if (ips != null) {
				ips.close();
			}
		}
	}

	/**
	 * 图片路径判定 
	 *
	 * @param path path
	 */
	public static boolean isLocalPath(String path) {
		boolean ret = true;

		if (path.length() > 5) {
			if ("http".equals(path.substring(0, 4))) {
				ret = false;
			}
		}

		return ret;
	}


	/**
	 * Decode string to image
	 * @param imageString The string to decode
	 * @return decoded image
	 */
	public static BufferedImage decodeToImage(String imageString) throws IOException {
		byte[] imageByte = Base64.decodeBase64(imageString);
		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
		BufferedImage image = ImageIO.read(bis);
		bis.close();
		return image;
	}

	/**
	 * Decode string to image
	 */
	public static void decodeToImageFile(String imageString, File newFile) throws IOException {
		if (newFile.exists()) {
			newFile.delete();
		}

		byte[] imageByte = Base64.decodeBase64(imageString);

		ByteArrayInputStream bis1 = new ByteArrayInputStream(imageByte);
		FileOutputStream fos = new FileOutputStream(newFile);
		int data;
		while((data=bis1.read())!=-1) {
			char ch = (char) data;
			fos.write(ch);
		}
		fos.flush();
		fos.close();
	}

	/**
	 * Encode image to string
	 * @param file file
	 * @return encoded string
	 */
	public static String encodeToString(File file) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		IOUtils.copy(inputStream, bos);
		String result = encodeToString(bos.toByteArray());
		bos.close();

		return result;
	}

	/**
	 * Encode image to string
	 * @param inputStream inputStream
	 * @param fileName fileName
	 * @return encoded string
	 * @throws IOException
	 */
	public static String encodeToString(InputStream inputStream, String fileName) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		IOUtils.copy(inputStream, bos);
		String result = encodeToString(bos.toByteArray());
		bos.close();
		return result;
	}

	/**
	 * Encode image to string
	 * @param bufferedImage bufferedImage
	 * @param type type
	 * @return encoded string
	 * @throws IOException
	 */
	public static String encodeToString(BufferedImage bufferedImage, String type) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		ImageIO.write(bufferedImage, type, bos);
		byte[] imageBytes = bos.toByteArray();

		String imageString = encodeToString(imageBytes);

		bos.close();
		return imageString;
	}

	/**
	 * Encode image to string
	 * @param imageBytes imageBytes
	 * @return encoded string
	 * @throws IOException
	 */
	public static String encodeToString(byte[] imageBytes) throws IOException {
		return Base64.encodeBase64String(imageBytes);
	}

//	/**
//	 * 获取文件的真实媒体类型。目前只支持JPG, GIF, PNG, BMP四种图片文件。
//	 *
//	 * @param bytes 文件字节流
//	 * @return 媒体类型(MEME TYPE)
//	 */
//	public static String getMimeType(byte[] bytes) {
//		String suffix = getFileSuffix(bytes);
//		String mimeType;
//
//		if ("JPG".equals(suffix)) {
//			mimeType = "image/jpeg";
//		} else if ("GIF".equals(suffix)) {
//			mimeType = "image/gif";
//		} else if ("PNG".equals(suffix)) {
//			mimeType = "image/png";
//		} else if ("BMP".equals(suffix)) {
//			mimeType = "image/bmp";
//		}else {
//			mimeType = "application/octet-stream";
//		}
//
//		return mimeType;
//	}
//
//	/**
//	 * 获取文件的真实后缀名。目前只支持JPG, GIF, PNG, BMP四种图片文件。
//	 *
//	 * @param bytes 文件字节流
//	 * @return JPG, GIF, PNG or null
//	 */
//	public static String getFileSuffix(byte[] bytes) {
//		if (bytes == null || bytes.length < 10) {
//			return null;
//		}
//
//		if (bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F') {
//			return "GIF";
//		} else if (bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G') {
//			return "PNG";
//		} else if (bytes[6] == 'J' && bytes[7] == 'F' && bytes[8] == 'I' && bytes[9] == 'F') {
//			return "JPG";
//		} else if (bytes[0] == 'B' && bytes[1] == 'M') {
//			return "BMP";
//		} else {
//			return null;
//		}
//	}

	/**
	 * 获取图片的后缀名
	 * @param imageUrl
	 * @return
	 */
	public static String getImageExtend (String imageUrl) {
		String tempExtend = imageUrl.substring(imageUrl.lastIndexOf(".") > 0 ? imageUrl.lastIndexOf(".") : 0);
		return tempExtend.substring(0, tempExtend.indexOf("?") > 0 ? tempExtend.indexOf("?") : tempExtend.length()).toLowerCase();
	}

	/**
	 * 获取图片的名字和后缀
	 * @param imageUrl
	 * @return
	 */
	public static String getImageName (String imageUrl) {
		String tempExtend = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		return tempExtend.substring(0, tempExtend.indexOf(".") > 0 ? tempExtend.indexOf(".") : tempExtend.length());
	}
}
