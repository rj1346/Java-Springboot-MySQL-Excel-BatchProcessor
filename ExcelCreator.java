package batch.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import lombok.extern.slf4j.Slf4j;
import batch.model.User;
import batch.tasklet.ListOfFilmsTasklet;

@Slf4j
public class ExcelCreator {
	
	private static final Log log = LogFactory.getLog(ListOfFilmsTasklet.class);
	
	public SXSSFWorkbook workbook;
	public SXSSFSheet sheet;
	public Font font;
	public CellStyle style;

	public void initExcelExporter() throws Exception {
		log.info("initExcelExporter");
		try {
			workbook = new SXSSFWorkbook(10);
			style = workbook.createCellStyle();
			font = workbook.createFont();
			style.setFont(font);
		}
		catch (Exception e) {
			log.error("In initExcelExporter error=" + e.getMessage());			
		}
	}
	
	public void writeHeaderLine(String tabName, List<String> names) {
		sheet = workbook.createSheet(tabName);		
		sheet.trackAllColumnsForAutoSizing();
		Row row = sheet.createRow(0);
		Integer col = 0;
		for (String name : names) {
				createCell(row, col++, name, style);
		}
	}
	

	public void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}
	
	public void writeDataLines1(List<User> users) {
		//log.info("In writeDataLines1");
		int rowCount = 1;
		int count = 0;
		for (User user : users) {
			Row row = sheet.createRow(rowCount++);
			//log.info("Title= "+ user.getTitle());
			//log.info("Release_year= "+ user.getRelease_year());
			//log.info("Actor_name= "+ user.getActor_name());
			//log.info("Genre= "+ user.getGenre());
			//log.info("Rental_rate= "+ user.getRental_rate());
			//if (count++ > 100) break;
			int columnCount = 0;
			try {
				createCell(row, columnCount++, user.getTitle(), style);
				
				if (user.getRelease_year() != null)
					createCell(row, columnCount++, user.getRelease_year(), style);
				else
					createCell(row, columnCount++, " ", style);
				
				if(user.getActor_name() != null)			
					createCell(row, columnCount++, user.getActor_name(), style);
				else
					createCell(row, columnCount++, " ", style);
				
				if (user.getGenre() != null)				
					createCell(row, columnCount++, user.getGenre(), style);
				else
					createCell(row, columnCount++, " ", style);
				
				DecimalFormat twoPlaces = new DecimalFormat("0.00");
				if (user.getRental_rate() != null)
					createCell(row, columnCount++, twoPlaces.format(user.getRental_rate()), style);
				else
					createCell(row, columnCount++, " ", style);
				
				
			} catch (Exception e) 
			{
				log.error("ExcelCreator.writeDataLines1 - " + e.getMessage());
			}

		}
	}
	
	public void export(HttpServletResponse response) throws IOException {
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	public String exportToFile() throws IOException {
	    log.info("In exportToFile");
	    String fileName = "";
	    try {
	        LocalDateTime current = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmm");
	        String timestamp = current.format(formatter);
	        fileName = "ListOfFilms_" + timestamp + ".xlsx";

	        FileOutputStream out = new FileOutputStream(fileName);
	        workbook.write(out);
	        workbook.close();
	        String absolutePath = new File(fileName).getAbsolutePath();
	        log.info("Excel file exported and saved successfully: " + absolutePath);
	        log.info("Export process completed");
	        log.info("Excel file generation completed");
	    } catch (Exception e) {
	        log.error("ExcelCreator.exportToFile - " + e.getMessage());
	    }
	    return fileName;
	}

	public void test(User user) throws IOException {
		log.info("In test");
		try {
			workbook = new SXSSFWorkbook(500);
			
			sheet = workbook.createSheet("Users");
			
			Row row = sheet.createRow(0);
			
			CellStyle style = workbook.createCellStyle();
			HSSFFont font = (HSSFFont)workbook.createFont();
			style.setFont(font);
			
			createCell(row, 0, "Title", style);
			createCell(row, 1, "Release_year", style);
			
			int rowCount = 1;
			row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, user.getTitle(), style);
			createCell(row, columnCount++, user.getRelease_year(), style);
			//createCell(row, columnCount++, user.getActor_name(), style);
			//createCell(row, columnCount++, user.getGenre(), style);
			//createCell(row, columnCount++, user.getRental_rate(), style);

			FileOutputStream out = new FileOutputStream(".\\testExport.xlsx");

			workbook.write(out);
			workbook.close();
			
		} catch (Exception e) {			
			log.error("ExcelCreator.test - " + e.getMessage());
		}
	}

}
