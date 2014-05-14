package org.hanyq.config.cfg.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtils {
	public static Workbook read(String file) throws IOException {
		return read(new File(file));
	}

	public static Workbook read(File file) throws IOException {
		return read(new FileInputStream(file));
	}

	public static Workbook read(FileInputStream fis) throws IOException {
		Workbook book = null;
		try {
			book = new XSSFWorkbook(fis);
		} catch (Exception e) {
			book = new HSSFWorkbook(fis);
		}
		fis.close();
		return book;
	}

	public static Sheet readSheet(String file, String sheet) throws IOException {
		Workbook book = read(file);

		return book.getSheet(sheet);
	}

	public static String getValue(Cell cell) {
		String content = null;
		if (cell != null) {
			switch (cell.getCellType()) {
			case 0:
				double d = cell.getNumericCellValue();
				if (d == (long) d)
					content = String.valueOf((long) d);
				else {
					content = String.valueOf(d);
				}

				break;
			case 1:
				content = cell.getStringCellValue();
				break;
			case 2:
				double dd = cell.getNumericCellValue();
				if (dd == (long) dd)
					content = String.valueOf((long) dd);
				else {
					content = String.valueOf(dd);
				}

				break;
			case 3:
				content = "";
				break;
			case 4:
				content = String.valueOf(cell.getBooleanCellValue());
			}

		}

		return content;
	}
}