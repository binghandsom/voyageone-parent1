package com.voyageone.common.flume.sink.file;

import org.apache.commons.lang.time.DateFormatUtils;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class VoPathManagerForFile {
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private File baseDirectory;
    private AtomicInteger fileIndex = new AtomicInteger();
    private File currentFile;

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return DateFormatUtils.format(cal, DATE_FORMAT);
    }

    public File nextFile(String prefix) {
        String fileName = prefix;
        String extendName = "log";
        if (prefix.contains(".")) {
            fileName = prefix.substring(0, prefix.lastIndexOf("."));
            extendName = prefix.substring(prefix.lastIndexOf(".")+1);
        }
        this.currentFile = new File(this.baseDirectory + File.separator + fileName + "-" + getCurrentDate() + "." + extendName);
        return this.currentFile;
    }

    public File getCurrentFile(String prefix) {
        return this.currentFile == null?this.nextFile(prefix):this.currentFile;
    }

    public void rotate() {
        this.currentFile = null;
    }

    public File getBaseDirectory() {
        return this.baseDirectory;
    }

    public void setBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
}
