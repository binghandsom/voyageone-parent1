package com.voyageone.common.components;

import com.voyageone.common.util.FixedLengthReaderUtil;
import org.junit.Test;

import java.util.List;

/**
 * Created by Jonas on 10/12/15.
 */
public class FixedLengthReaderTest {

    @Test
    public void testReadFile() throws Exception {

        FixedLengthReaderUtil utils = new FixedLengthReaderUtil(true, false);

        int[] cols = {10, 5, 3, 10, 5, 3, 3};

        List<List<String>> list = utils.readFile("/Users/Jonas/Desktop/BCBG_20151012.txt", cols);

        for (List<String> row : list) {
            for (String col: row) {
                System.out.print(col);
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }
}