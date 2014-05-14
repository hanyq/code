package org.hanyq.config.cfg.excel.convertor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hanyq.config.cfg.excel.ExcelReaderUtils;
import org.hanyq.config.cfg.excel.meta.ExcelField;
import org.hanyq.config.cfg.excel.meta.ExcelFormatException;
import org.hanyq.config.cfg.excel.meta.ExcelMeta;

public class ExcelToXmlConvertor {
	private String excelPath;

	public ExcelToXmlConvertor(String excelPath) {
		this.excelPath = excelPath;
		if (!this.excelPath.endsWith("/"))
			this.excelPath += "/";
	}

	public Document toXml(ExcelMeta meta) throws IOException {
		Document beansDoc = DocumentHelper.createDocument();
		beansDoc.setName(meta.getJavaConfigClass());
		Element rootEle = beansDoc.addElement("Root");

		List<String> excels = meta.getExcel();
		List<String> excelSheets = meta.getExcelSheet();
		for (int i = 0; i < excels.size(); i++) {
			toXml(meta, (String) excels.get(i), (String) excelSheets.get(i),
					rootEle);
		}

		return beansDoc;
	}

	public void toXml(ExcelMeta meta, String excel, String excelSheet,
			Element rootEle) throws IOException {
		String excelFile = this.excelPath + excel;
		Sheet sheet = ExcelReaderUtils.readSheet(excelFile, excelSheet);

		int TITLE_COUNT = 1;
		int rowCount = sheet.getLastRowNum();
		if (rowCount < TITLE_COUNT) {
			throw new ExcelFormatException("excel = " + excel + ",sheet = "
					+ sheet.getSheetName() + ",一个sheet至少要有（desc, type）两行！！！");
		}
		Row descRow = sheet.getRow(0);
		//Row typeRow = sheet.getRow(1);

		if (meta.skipClientType()) {
			//typeRow = sheet.getRow(2);
			TITLE_COUNT++;
		}

		int lastCellNum = descRow.getLastCellNum();
		for (int n = lastCellNum; n >= 0; n--) {
			//String type = ExcelReaderUtils.getValue(typeRow.getCell(n));
			String desc = ExcelReaderUtils.getValue(descRow.getCell(n));

			//if ((!StringUtils.isEmpty(type)) || (!StringUtils.isEmpty(desc)))
			if (!StringUtils.isEmpty(desc)){
				break;
			}
			lastCellNum--;
		}

		Set<Integer> skipCellNums = new HashSet<Integer>();
		for (int i = 0; i <= lastCellNum; i++) {
			/*String desc = ExcelReaderUtils.getValue(descRow.getCell(i));
			
			if ("skip".equalsIgnoreCase(type)) {
				skipCellNums.add(Integer.valueOf(i));
			}*/
			
			if(meta.getFields()[i].isSkip()){
				skipCellNums.add(Integer.valueOf(i));
			}
		}

		if (lastCellNum + 1 != meta.getFiledLength()) {
			throw new ExcelFormatException("excel = " + excel + ",sheet = "
					+ sheet.getSheetName() + ",元数据和Excel中字段长度不相等！！！");
		}

		for (int j = TITLE_COUNT; j <= rowCount; j++) {
			Row contentRow = sheet.getRow(j);
			if (contentRow != null) {
				Element beanEle = DocumentHelper.createElement(meta
						.getJavaConfigClass());
				boolean notEmpty = false;
				for (int k = 0; k <= lastCellNum; k++) {
					ExcelField field = meta.getField(k);
					if(field.isSkip()){
						continue;
					}
					
					Cell cell = contentRow.getCell(k);
					String content = ExcelReaderUtils.getValue(cell);

					content = getStringValue(content, field.isNumber());

					if (field.isString()) {
						if ((content != null) && (!content.isEmpty())) {
							Element propEle = DocumentHelper
									.createElement(field.getName());
							propEle.addCDATA(content);

							beanEle.add(propEle);

							notEmpty = true;
						}
					} else {
						try {
							if(!field.isFloat()){
								content = String.valueOf((long) Double
										.parseDouble(content));
							}
						} catch (Exception e) {
							throw new ExcelFormatException("excel = "
									+ excel + ",sheet = "
									+ sheet.getSheetName()
									+ ",无法转换成数字！！！ row = " + j
									+ ", desc = " + field.getDesc());
						}
						if (!"0".endsWith(content)) {
							notEmpty = true;
						}
						beanEle.addAttribute(field.getName(), content);
					}
				}
				if (notEmpty)
					rootEle.add(beanEle);
			}
		}
	}

	private String getStringValue(String value, boolean isNumber) {
		if (value != null) {
			value = value.trim();
		}
		if ((value != null) && (!value.isEmpty())) {
			return value;
		}

		return isNumber ? "0" : "";
	}

	public void saveDocument(String path, Document doc) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		if (!path.endsWith("//")) {
			path = path + "//";
		}

		OutputFormat output = OutputFormat.createPrettyPrint();
		Writer writer = new FileWriter(path + doc.getName() + ".xml");
		XMLWriter xmlWriter = new XMLWriter(writer, output);
		xmlWriter.write(doc);
		xmlWriter.close();
	}
}