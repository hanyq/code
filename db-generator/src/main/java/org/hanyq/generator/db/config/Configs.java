package org.hanyq.generator.db.config;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Configs {
	public static String JAVA_MAPPER_PACKAGE = "com.kingdowin.newlol.db.mapper";
	
	public static String JAVA_DAO_PACKAGE = "com.kingdowin.newlol.db.dao";
	
	public static String JAVA_DAO_CONTAINER_PACKAGE = "com.kingdowin.newlol.db";
	
	public static String javaMapperDir;
	public static String xmlMapperDir;
	public static String sqlMapperDir;
	public static String daoDir;
	public static String daoContainerDir;
	
	public static void reload(String file){
		try{
			SAXReader reader = new SAXReader();
			InputStream in = Configs.class.getResourceAsStream(file);
			Document doc = reader.read(in);
			Element root = doc.getRootElement();
			
			JAVA_MAPPER_PACKAGE = root.elementText("JAVA_MAPPER_PACKAGE");
			JAVA_DAO_PACKAGE = root.elementText("JAVA_DAO_PACKAGE");
			JAVA_DAO_CONTAINER_PACKAGE = root.elementText("JAVA_DAO_CONTAINER_PACKAGE");
			javaMapperDir = root.elementText("javaMapperDir");
			xmlMapperDir = root.elementText("xmlMapperDir");
			sqlMapperDir = root.elementText("sqlMapperDir");
			daoDir = root.elementText("daoDir");
			daoContainerDir = root.elementText("daoContainerDir");
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
