package com.kingdowin.config.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.dom4j.Document;

import com.kingdowin.config.cfg.excel.convertor.ExcelToJavaConvertor;
import com.kingdowin.config.cfg.excel.convertor.ExcelToXmlConvertor;
import com.kingdowin.config.cfg.excel.convertor.classgenerator.JavaFactoryGenerator;
import com.kingdowin.config.cfg.excel.meta.ExcelMeta;
import com.kingdowin.config.cfg.excel.meta.ExcelMetaParser;

public class App {
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		String metaExcelPath = Config.getMetaExcelPath();
		String srcExcelPath = Config.getExcelPath();
		String destJavaPath = Config.getDestJavaPath();
		String destXmlPath = Config.getDestXmlPath();

		File file = new File(App.class.getClassLoader()
				.getResource(metaExcelPath).getFile());
		List<ExcelMeta> ls = ExcelMetaParser.getInstance().parser(file);

		ExcelToXmlConvertor convertor = new ExcelToXmlConvertor(srcExcelPath);
		ExcelToJavaConvertor javaConvertor = new ExcelToJavaConvertor(
				destJavaPath);
		List<String> skippedExcelMetaList = new ArrayList<String>();

		Set<String> justCareXmlSet = getCareXmls();

		for (ExcelMeta meta : ls) {
			if ((justCareXmlSet.isEmpty())
					|| (justCareXmlSet.contains(meta.getJavaConfigClass()))) {
				if (meta.isSkip()) {
					skippedExcelMetaList.add(meta.getMetaSheet());
				} else {
					System.out.println(meta.getJavaConfigClass());

					Document doc = convertor.toXml(meta);
					convertor.saveDocument(destXmlPath, doc);

					if ("true".equalsIgnoreCase(Config
							.getProperty("create_java_class")))
						javaConvertor.makeJavaClass(meta);
				}
			}
		}
		JavaFactoryGenerator.makeJavaClass(destJavaPath, ls);

		System.err.println("\r\nNote : skip config " + skippedExcelMetaList);
		System.err.println("\r\nSuccess!!!");
	}

	private static Set<String> getCareXmls() {
		String justCareXml = Config.getProperty("just_care_xml");
		Set<String> justCareXmlSet = new HashSet<String>();
		if ((justCareXml != null) && (!justCareXml.isEmpty())) {
			String[] careXmls = justCareXml.split(",");
			for (String careXml : careXmls) {
				justCareXmlSet.add(careXml + "Config");
			}
		}

		return justCareXmlSet;
	}
}