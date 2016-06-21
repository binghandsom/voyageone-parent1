package com.voyageone.common.util;


import com.voyageone.common.configs.Properties;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author sky
 * @version 0.0.1
 */

public final class FileUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * @param preFileName 源文件名
     * @param filePath    文件所在目录
     * @return 以【XXXX_】开头的文件组
     * 文件名必须以【XXXX_】开头，在指定目录下获取文件名以【XXXX_】开头的文件组，
     */
    public static List getFileGroup(String preFileName, String filePath) {
        ArrayList<String> fileNameList = new ArrayList<>();
        int sIndex = preFileName.indexOf("*");
        if (sIndex != -1) {
            String fileNameLike = preFileName.substring(0, sIndex).toUpperCase();
            File file = new File(filePath);
            for (String fileName : file.list()) {
                if (fileName.toUpperCase().contains(fileNameLike)) {
                    fileNameList.add(fileName);
                }
            }
        } else {
            fileNameList.add(preFileName);
        }
        return fileNameList;
    }

    /**
     * @param filePath 文件所在目录
     * @return 该目录下的文件组
     * filePath下的文件组返回
     */
    public static List getFileGroup2(String filePath, String postfix) {
        ArrayList<String> fileNameList = new ArrayList<>();

        File file = new File(filePath);
        for (String fileName : file.list()) {
            if (fileName.contains(postfix)) {
                fileNameList.add(fileName);
            }
        }
        return fileNameList;
    }

    /**
     * 复制文件
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     */
    public static void copyFile(String srcFile, String targetFile) {
        try {
            int byteread;
            File oldfile = new File(srcFile);
            if (oldfile.exists()) {
                FileInputStream inStream = new FileInputStream(srcFile);
                FileOutputStream fs = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            String logMsg = "复制单个文件操作出错! 源文件：" + srcFile + "; 目标文件:" + targetFile;
            logger.error(logMsg);
            throw new RuntimeException(logMsg);
        }
    }

    /**
     * 删除文件
     *
     * @param filePathAndName 要删除的文件的路径
     */
    public static void delFile(String filePathAndName) {
        File file = new File(filePathAndName);
        if (file.isFile() && file.exists()) {
            file.delete();
        } else {
            String logMsg = "需要移除的文件错误，filePath:" + filePathAndName;
            logger.error(logMsg);
            throw new RuntimeException(logMsg);
        }
    }

    /**
     * 文件移动
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     */
    public static void moveFile(String srcFile, String targetFile) {
        copyFile(srcFile, targetFile);
        delFile(srcFile);
    }

    /**
     * 通过修改文件名来判断文件是否已经被其他程序在使用
     *
     * @param file 需要判断的文件
     * @return boolean true:文件有其他程序在操作; false:文件无其他程序在操作
     */
    public static boolean fileIsInUse(File file) {
        boolean useFlg = file.renameTo(file);
        if (useFlg) {
            logger.info("===============文件未被其他程序使用!===============");
        } else {
            logger.info("===============文件正在被其他程序使用!===============");
        }
        return !useFlg;
    }

    /**
     * @param filePath 文件所在目录
     *                 description 在指定目录下获取全部文件组
     */
    public static List<String[]> getFileGroup(String filePath) {
        List<String[]> fileNameList = new ArrayList<>();
        File file = new File(filePath);
        for (String fileName : file.list()) {
            File file2 = new File(filePath + "/" + fileName);
            if (file2.isFile() && file2.exists()) {
                String[] fileUpload = new String[2];
                fileUpload[0] = fileName;
                //是否上传标志位，默认上传
                fileUpload[1] = "1";
                fileNameList.add(fileUpload);
            }
        }
        return fileNameList;
    }

    /**
     * 文件是否存在
     *
     * @param chkFileName  被检查文件
     * @param chkFilePath  被检查路径
     * @param suffixLength 后缀长度
     * @return boolean
     */
    public static boolean isFileExist(String chkFileName, String chkFilePath, int suffixLength) {
        boolean ret = false;

        String fileNameLike = chkFileName.substring(0, chkFileName.length() - suffixLength);

        File file = new File(chkFilePath);
        for (String fileName : file.list()) {
            if (fileName.contains(fileNameLike)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    /**
     * 复制文件
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     */
    public static void copyFileByBcbg(String srcFile, String targetFile) throws IOException {

        try (FileInputStream inStream = new FileInputStream(srcFile);
             FileOutputStream fs = new FileOutputStream(targetFile)) {
            int byteRead;
            File oldFile = new File(srcFile);
            if (oldFile.exists()) {
                byte[] buffer = new byte[1444];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                //inStream.close();
            }
            fs.close();
        } catch (Exception e) {
            //inStream.close();
            //fs.close();
            String logMsg = "复制单个文件操作出错! 源文件：" + srcFile + "; 目标文件:" + targetFile;
            logger.error(logMsg);
            throw new RuntimeException(logMsg);
        }
    }

    /**
     * 文件移动
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     */
    public static void moveFileByBcbg(String srcFile, String targetFile) throws IOException {
        copyFileByBcbg(srcFile, targetFile);
        delFile(srcFile);
    }

    /**
     * url download file
     */
    public static boolean downloadImage(String urlString, String filename, String savePath) {
        return downloadImage(urlString, filename, savePath, null, 0, null, null);
    }

    /**
     * url download file with proxy
     */
    public static boolean downloadImageWithProxy(String urlString, String filename, String savePath) {
        String proxyHost = Properties.readValue("download.proxyHost");
        int proxyPort = Integer.parseInt(Properties.readValue("download.proxyPort"));
        String proxyUser = Properties.readValue("download.proxyUser");
        String proxyPassword = Properties.readValue("download.proxyPassword");

        return downloadImage(urlString, filename, savePath, proxyHost, proxyPort, proxyUser, proxyPassword);
    }

    /**
     * url download file with proxy
     */
    public static boolean downloadImage(String urlString, String filename, String savePath, String proxyIP, int proxyPort, String proxyUserName, String proxyPwd) {
        boolean result = false;

        OutputStream os = null;
        InputStream is = null;

        try {
            // 构造URL
            URL url = new URL(urlString);
            URLConnection con;
            //proxyIP = null;
            if (proxyIP != null) {
                // 设置代理 地址和端口
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIP, proxyPort));
                // 设置代理的密码验证
                if (proxyUserName != null) {
                    Authenticator auth = new Authenticator() {
                        private PasswordAuthentication pa = new PasswordAuthentication(proxyUserName, proxyPwd.toCharArray());

                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return pa;
                        }
                    };
                    Authenticator.setDefault(auth);
                }
                // 打开连接
                con = url.openConnection(proxy);
            } else {
                // 打开连接
                con = url.openConnection();
            }
            con.setRequestProperty("Accept-Charset", "UTF-8");
            //设置请求超时为5s
            con.setConnectTimeout(10 * 1000);
            //设置请求超时为5s
            con.setReadTimeout(20 * 1000);
            // 输入流
            is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            File file = new File(sf.getPath() + "\\" + filename);
            if (file.exists()) {
                file.delete();
            }
            os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            result = true;

        } catch (Exception e) {
            logger.error("downloadImage", e);
            logger.error("downloadImage url:=" + urlString);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignored) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
        return result;
    }

    /**
     * Excel Cell样式取得
     *
     * @param book Excel Book对象
     * @return CellStyle Excel Cell样式
     */
    public static CellStyle createUnLockStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();

        cellStyle.setLocked(false);

        return cellStyle;
    }

    /**
     * Excel 行对象取得
     *
     * @param sheet    Excel Sheet对象
     * @param rowIndex Excel RowIndex
     * @return Row Excel Row对象
     */
    public static Row row(Sheet sheet, int rowIndex) {

        Row row = sheet.getRow(rowIndex);

        if (row == null) row = sheet.createRow(rowIndex);

        return row;
    }

    /**
     * Excel Cell对象取得
     *
     * @param row       Excel Row对象
     * @param index     Excel CellIndex
     * @param cellStyle Excel Cell样式
     * @return Row Excel Cell对象
     */
    public static Cell cell(Row row, int index, CellStyle cellStyle) {

        Cell cell = row.getCell(index);

        if (cell == null) cell = row.createCell(index);

        if (cellStyle != null) cell.setCellStyle(cellStyle);

        return cell;
    }

    public static void downloadFile(HttpServletResponse response, String fileName, String filePath) throws IOException {
        OutputStream os = null;
        InputStream inputStream = null;
        try {
            File excelFile = new File(filePath);
            os = response.getOutputStream();
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentType("application/octet-stream; charset=utf-8");
            //os.write(FileUtils.readFileToByteArray(excelFile));
            // os.flush();
            inputStream = new FileInputStream(excelFile);
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    public static void mkdirPath(String path) {
        String[] paths = path.split("/");
        StringBuilder fullPath = new StringBuilder();
        for (String path1 : paths) {
            fullPath.append(path1).append("/");
            File file = new File(fullPath.toString());
            if (!file.exists()) {
                file.mkdir();
                logger.info("创建目录为：" + fullPath.toString());
            }
        }
    }

    public static List<String> uploadFile(HttpServletRequest request, String path) throws IOException {
        List<String> listFileName = new ArrayList<>();
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (!"".equals(myFileName.trim())) {
                        //重命名上传后的文件名
                        String fileName = file.getOriginalFilename();
                        String timerstr = DateTimeUtil.format(new Date(), "yyyyMMddHHmmssSSS");
                        //定义上传路径
                        String filepath = path + "/" + timerstr + fileName;
                        File localFile = new File(filepath);
                        file.transferTo(localFile);
                        listFileName.add(timerstr + fileName);
                    }
                }
            }
        }
        return listFileName;
    }
}
