package com.voyageone.core.controller;

import java.io.IOException;

import com.voyageone.base.BaseController;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/core/demo")
public class DownloadDemoController extends BaseController {

	@RequestMapping("/download")
	public ResponseEntity<byte[]> download() throws IOException {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
	      HSSFSheet sheet = workbook.createSheet("hello");
	      HSSFRow row= sheet.createRow(0);
	      HSSFCell cell = row.createCell(0);
	      cell.setCellValue("xxx");
	     byte[] bytes =  workbook.getBytes();
	     workbook.close();
	      
	    return genResponseEntityFromBytes("hello.xls", bytes);
	}
}
