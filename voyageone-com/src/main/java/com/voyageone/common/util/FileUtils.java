package com.voyageone.common.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sky
 * @version 0.0.1
 */

public final class FileUtils {

    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * @param preFileName 源文件名
     * @param filePath 文件所在目录
     * @return 以【XXXX_】开头的文件组
     * @description 文件名必须以【XXXX_】开头，在指定目录下获取文件名以【XXXX_】开头的文件组，
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
     * 复制文件
     * @param srcFile 源文件
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
     * @param filePathAndName 要删除的文件的路径
     * @return boolean
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
     * @param srcFile 源文件
     * @param targetFile 目标文件
     * @return boolean
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
        if(useFlg) {
            logger.info("===============文件未被其他程序使用!===============");
        }else{
            logger.info("===============文件正在被其他程序使用!===============");
        }
        return !useFlg;
    }

    /**
     * @param filePath 文件所在目录
     * @description 在指定目录下获取全部文件组
     */
    public static List getFileGroup(String filePath) {
        ArrayList<String> fileNameList = new ArrayList<>();
        File file = new File(filePath);
        for (String fileName : file.list()) {
            File file2 = new File(filePath + "/" + fileName);
            if (file2.isFile() && file2.exists()){
                fileNameList.add(fileName);
            }
        }
        return fileNameList;
    }

    /**
     * 文件是否存在
     * @param chkFileName 被检查文件
     * @param chkFilePath 被检查路径
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
     * @param srcFile 源文件
     * @param targetFile 目标文件
     */
    public static void copyFileByBcbg(String srcFile, String targetFile) throws IOException {

        try (FileInputStream inStream = new FileInputStream(srcFile);
             FileOutputStream fs = new FileOutputStream(targetFile);){
            int byteread;
            File oldfile = new File(srcFile);
            if (oldfile.exists()) {
//                FileInputStream inStream = new FileInputStream(srcFile);
//                FileOutputStream fs = new FileOutputStream(targetFile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                //inStream.close();
                //fs.close();
            }
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
     * @param srcFile 源文件
     * @param targetFile 目标文件
     * @return boolean
     */
    public static void moveFileByBcbg(String srcFile, String targetFile)throws IOException {
        copyFileByBcbg(srcFile, targetFile);
        delFile(srcFile);
    }

}
