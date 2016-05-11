package com.voyageone.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

/**
 * @author Jerry
 * @version 0.0.1
 */

public class FileWriteUtils {

    private final static Logger logger = LoggerFactory.getLogger(FileWriteUtils.class);

    private Writer outputStream;
    private String systemRecordDelimiter;
    private boolean initialized;
    private boolean useCustomRecordDelimiter;
    private boolean closed;
    private char recordDelimiter;

    //  字符标识
    private String characterIdentify;
    //  数值标识
    private String numericIdentify;

    public FileWriteUtils(Writer var1, String var2, String var3) {
        this.outputStream = null;
        this.initialized = false;
        this.closed = false;
        this.useCustomRecordDelimiter = false;
        this.systemRecordDelimiter = System.getProperty("line.separator");
        if(var1 == null) {
            throw new IllegalArgumentException("Parameter outputStream can not be null.");
        } else {
            this.outputStream = var1;
            this.characterIdentify = var2;
            this.numericIdentify = var3;
            this.initialized = true;
        }
    }

    /**
     * 初期化
     *
     * @param var1 输出流
     * @param var2 输出流
     * @param var3 字符类型标识
     * @param var4 数值类型标识
     *
     */
    public FileWriteUtils(OutputStream var1, Charset var2, String var3, String var4) {
        this(new OutputStreamWriter(var1, var2), var3, var4);
    }

    public void write(String var1) throws IOException {
        outputStream.write(var1);
    }

    public void write(String var, String outputFormat) throws IOException {
        outputStream.write(getFormattedString(var, outputFormat));
    }

    private String getFormattedString(String inputString, String outputFormat) {
        String outputString = "";

        String[] formatList = outputFormat.split(",");
        String type = formatList[0];
        String formatContent = formatList[1];

        //  文字的场合
        if (characterIdentify.equals(type)) {
            if (inputString.length() > Integer.valueOf(formatContent)) {
                outputString = inputString.substring(0, Integer.valueOf(formatContent));
            } else {
                outputString = StringUtils.rPad(inputString, " ", Integer.valueOf(formatContent));
            }
        //  数值的场合
        } else {
            // 负数判定
            boolean isNegative = isInputStringNegative(inputString);

            // 负号去除
            if (isNegative) {
                inputString = inputString.substring(1, inputString.length());
            }

            if (formatContent.contains(".")) {
                String[] formatContentArr = formatContent.split("\\.");
                String numericLength = formatContentArr[0];

                if (inputString.contains(".")) {
                    double numericDouble = Double.parseDouble(inputString);
                    DecimalFormat df = new DecimalFormat("#.00");

                    if (isNegative) {
                        outputString = "-" + StringUtils.lPad(df.format(numericDouble), "0", Integer.valueOf(numericLength) - 1);
                    } else {
                        outputString = StringUtils.lPad(df.format(numericDouble), "0", Integer.valueOf(numericLength));
                    }
                } else {
                    if (isNegative) {
                        outputString = "-" + StringUtils.lPad(inputString, "0", Integer.valueOf(numericLength) - 1);
                    } else {
                        outputString = StringUtils.lPad(inputString, "0", Integer.valueOf(numericLength));
                    }
                }
            } else {
                if (isNegative) {
                    outputString = "-" + StringUtils.lPad(inputString, "0", Integer.valueOf(formatContent) - 1);
                } else {
                    outputString = StringUtils.lPad(inputString, "0", Integer.valueOf(formatContent));
                }
            }
        }

        return outputString;
    }

    private boolean isInputStringNegative(String inputString) {
        boolean ret = false;

        if (!StringUtils.isEmpty(inputString)) {
            float inputContent = Float.parseFloat(inputString);

            if (inputContent < 0) {
                ret = true;
            }
        }

        return ret;
    }

    public void endRecord() throws IOException {
        if(this.useCustomRecordDelimiter) {
            this.outputStream.write(this.recordDelimiter);
        } else {
            this.outputStream.write(this.systemRecordDelimiter);
        }
    }

    public void setRecordDelimiter(char var1) {
        this.useCustomRecordDelimiter = true;
        this.recordDelimiter = var1;
    }

    public void flush() throws IOException {
        this.outputStream.flush();
    }

    public void close() {
        if(!this.closed) {
            this.close(true);
            this.closed = true;
        }

    }

    private void close(boolean var1) {
        if(!this.closed) {
            try {
                if(this.initialized) {
                    this.outputStream.close();
                }
            } catch (Exception ignored) {
            }

            this.outputStream = null;
            this.closed = true;
        }

    }

    private void checkClosed() throws IOException {
        if(this.closed) {
            throw new IOException("This instance of the CsvWriter class has already been closed.");
        }
    }
}
