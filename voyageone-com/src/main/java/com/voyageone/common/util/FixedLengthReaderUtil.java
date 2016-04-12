package com.voyageone.common.util;

import com.voyageone.common.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 定长格式的文件处理帮助类
 * <p>
 * Created by Jonas on 10/12/15.
 */
public class FixedLengthReaderUtil {

    // 读取值的时候是否消除空格
    private boolean trim;

    // 读取的文件是否有表头
    private boolean readFirst;

    public boolean isTrim() {
        return trim;
    }

    public boolean isReadFirst() {
        return readFirst;
    }

    /**
     * 定长格式的文件处理帮助类
     *
     * @param trim      读取值的时候是否消除空格
     * @param readFirst 读取的文件是否有表头
     */
    public FixedLengthReaderUtil(boolean trim, boolean readFirst) {
        this.trim = trim;
        this.readFirst = readFirst;
    }

    /**
     * 读取一行
     *
     * @param line    定长内容
     * @param colLens 列长的定义
     * @return 一行的拆分内容
     */
    public List<String> readLine(String line, int... colLens) {

        // 如果为空值,则跳过
        if (StringUtils.isEmpty(line)) return null;

        int start = 0;

        List<String> cols = new ArrayList<>();

        // 便利列定义
        for (int colLen : colLens) {
            // 每次计算列的结束未知
            int end = start + colLen;
            String col = line.substring(start, end);
            // 如果需要去除空格,则消除
            if (isTrim()) col = StringUtils.trim(col);

            cols.add(col);

            // 移动起始未知
            start = end;
        }

        return cols;
    }

    /**
     * 读取一行
     *
     * @param line    定长内容
     * @param colLens 列长的定义
     * @return 一行的拆分内容
     */
    public List<String> readLine(String line, String... colLens) {

        int array[] = new int[colLens.length];
        for(int i=0;i<colLens.length;i++) {
            array[i] = Integer.parseInt(colLens[i]);
        }
        return readLine(line,array);

    }

    /**
     * 从文件中读取
     *
     * @param fileName 文件全名
     * @param colLens  列长的定义
     * @return 文件内容
     * @throws IOException
     */
    public List<List<String>> readFile(String fileName, int... colLens) throws IOException {

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            return readFile(fileInputStream, colLens);
        }
    }

    /**
     * 从文件中读取
     *
     * @param inputStream 文件流
     * @param colLens     列长的定义
     * @return 文件内容
     * @throws IOException
     */
    public List<List<String>> readFile(InputStream inputStream, int... colLens) throws IOException {

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String line = reader.readLine();

            // 如果不读取第一行(有可能为表头),则标记为跳过
            // 空字符串,ReadLine 时会返回 null
            if (!isReadFirst()) line = Constants.EmptyString;

            List<List<String>> rows = new ArrayList<>();

            // 从流中逐行读取
            while (line != null) {

                List<String> row = readLine(line, colLens);

                // 此处如果返回 null,则认为时前面标记的第一行
                if (row != null) rows.add(row);

                // 读取下一行
                line = reader.readLine();
            }

            return rows;
        }
    }
}
