package org.hanyq.config.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class Config {
	private static final String META_EXCEL_PATH = "meta_excel_path";
	private static final String EXCEL_PATH = "src_excel_path";
	private static final String DEST_XML_PATH = "dest_xml_path";
	private static final String DEST_JAVA_PATH = "desc_java_path";
	private static final String BASE_PACKAGE = "base_package";
	private static final String DEFAULT_PAHT = "config.properties";
	private static Properties prop;

	public static void initConfig(String configPath) {
		prop = new Properties();
		try {

			File file = new File(configPath);
			System.out.println(file.getAbsolutePath());
			prop.load(new InputStreamReader(Config.class.getClassLoader()
					.getResourceAsStream(configPath), "utf8"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		if (prop == null) {
			initConfig(DEFAULT_PAHT);
		}
		return prop.getProperty(key);
	}

	public static String getMetaExcelPath() {
		return getProperty(META_EXCEL_PATH);
	}

	public static String getExcelPath() {
		return getProperty(EXCEL_PATH);
	}

	public static String getDestXmlPath() {
		return getProperty(DEST_XML_PATH);
	}

	public static String getDestJavaPath() {
		return getProperty(DEST_JAVA_PATH);
	}

	public static String getBasePackage() {
		return getProperty(BASE_PACKAGE);
	}
}
