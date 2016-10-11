package com.voyageone.common.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Zip File Tools
 *
 * @author chuanyu.liang, 2016/07/12
 * @version 1.3.0
 * @since 1.0.0
 */
public class FileZipTools {
    private static Logger logger = Logger.getLogger(FileZipTools.class);

    /**
     * 把zip文件解压到指定的文件夹
     *
     * @param zipFilePath zip文件
     * @param saveFileDir 解压后的文件存放路径
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void unzip(File zipFilePath, String saveFileDir, String encoding) {
        if (!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/")) {
            saveFileDir += File.separator;
        }

        File dir = new File(saveFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (zipFilePath.exists()) {
            InputStream is = null;
            ZipArchiveInputStream zais = null;
            try {
                is = new FileInputStream(zipFilePath);
                if (encoding == null || "".equals(encoding.trim())) {
                    zais = new ZipArchiveInputStream(is);
                } else {
                    zais = new ZipArchiveInputStream(is, encoding);
                }
                ArchiveEntry archiveEntry;
                while ((archiveEntry = zais.getNextEntry()) != null) {
                    // 获取文件名
                    String entryFileName = archiveEntry.getName();
                    // 构造解压出来的文件存放路径
                    String entryFilePath = saveFileDir + entryFileName;
                    OutputStream os = null;
                    try {
                        // 把解压出来的文件写到指定路径
                        File entryFile = new File(entryFilePath);
                        if (entryFileName.endsWith("/")) {
                            entryFile.mkdirs();
                        } else {
                            os = new BufferedOutputStream(new FileOutputStream(entryFile));
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = zais.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        throw new IOException(e);
                    } finally {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    }

                }
            } catch (Exception e) {
                logger.error("upZipFile error:", e);
                throw new RuntimeException(e);
            } finally {
                if (zais != null) {
                    try {
                        zais.close();
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
        }
    }
}
