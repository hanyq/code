package com.kingdowin.config.db.sourcegenerator.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.kingdowin.config.db.meta.DbField;
import com.kingdowin.config.db.meta.DbTable;
import com.kingdowin.config.db.meta.MapperDefinition;
import com.kingdowin.config.db.sourcegenerator.AbstractSourceGenerator;
import com.kingdowin.config.db.utils.SqlGenerator;

public class XmlMapperSouceGenerator extends AbstractSourceGenerator<MapperDefinition> {
	
	public XmlMapperSouceGenerator(String baseDirectory){
		super(baseDirectory);
	}
	
	@Override
	protected String getSouceName(MapperDefinition mapperDefinition) {
		String beanName = mapperDefinition.getTable().getBeanName();
		
		return beanName + "Mapper.xml";
	}

	@Override
	protected String doGeneratorSource(MapperDefinition mapperDefinition) {
		DbTable table = mapperDefinition.getTable();
		
		//Document doc = DocumentHelper.createDocument();
		Element mapperEle = DocumentHelper.createElement("mapper");
		mapperEle.addAttribute("namespace", table.getMapperFullName());
		
		String resultMap = table.getName() + "Map";
		Element resultMapEle = mapperEle.addElement("resultMap");
		resultMapEle.addAttribute("type", table.getBeanFullName());
		resultMapEle.addAttribute("id", resultMap);
		for(DbField field : table.getFields()){
			Element resultEle = resultMapEle.addElement("result");
			resultEle.addAttribute("property", field.getBeanName());
			resultEle.addAttribute("column", field.getDbName());
		}
		
		String beanName = table.getBeanName();
		
		if(mapperDefinition.getLoadMaxColumn() != null){
			Element loadMaxEle = mapperEle.addElement("select");
			
			loadMaxEle.addAttribute("id", mapperDefinition.getLoadMaxSqlName());
			//resultMapEle.addAttribute("parameterType", table.getName() + "Map");
			loadMaxEle.addAttribute("resultType", table.getFieldType(mapperDefinition.getLoadMaxColumn()));
			addCDATA(loadMaxEle, SqlGenerator.generateLoadMaxSql(table, mapperDefinition.getLoadMaxColumn()));
		}
		
		Element loadEle = mapperEle.addElement("select");
		loadEle.addAttribute("id", "load" + beanName);
		//resultMapEle.addAttribute("parameterType", table.getName() + "Map");
		loadEle.addAttribute("resultMap", resultMap);
		addCDATA(loadEle, SqlGenerator.generateLoadSql(table));
		
		//load--XX 根据自定义条件load
		for(Map.Entry<String, String[]> loadEntry : mapperDefinition.getLoadSqlMap().entrySet()){
			Element theLoadEle = mapperEle.addElement("select");
			theLoadEle.addAttribute("id", loadEntry.getKey());
			//resultMapEle.addAttribute("parameterType", table.getName() + "Map");
			theLoadEle.addAttribute("resultMap", resultMap);
			addCDATA(theLoadEle, SqlGenerator.generateLoadByWhereSql(table, loadEntry.getValue()));
		}
		
		
		//save sql
		Element saveEle = mapperEle.addElement("insert");
		saveEle.addAttribute("id", "save" + beanName);
		//resultMapEle.addAttribute("parameterType", table.getName() + "Map");
		//saveEle.addAttribute("parameterMap", resultMap);
		addCDATA(saveEle, SqlGenerator.generatSaveSql(table));
		
		
		
		//update sql
		for(Map.Entry<String, String[]> updatgeEntry : mapperDefinition.getUpdateSqlMap().entrySet()){
			Element updateEle = mapperEle.addElement("update");
			updateEle.addAttribute("id", updatgeEntry.getKey());
			//resultMapEle.addAttribute("parameterType", table.getName() + "Map");
			//updateEle.addAttribute("parameterMap", resultMap);
			addCDATA(updateEle, SqlGenerator.generatUpdateSql(table, updatgeEntry.getValue()));
		}
		
		//deleteXXX
		Element deleteEle = mapperEle.addElement("delete");
		deleteEle.addAttribute("id", "delete" + beanName);
		//resultMapEle.addAttribute("parameterType", table.getName() + "Map");
		//deleteEle.addAttribute("parameterMap", resultMap);
		addCDATA(deleteEle, SqlGenerator.generatDeleteSql(table));
	
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		StringWriter sw = new StringWriter();
		XMLWriter writer = new XMLWriter(sw, format);
		try {
			writer.write(mapperEle);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sb.append("\r\n<!DOCTYPE mapper        PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"     ");   
		sb.append("\r\n\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\"> ");
		
		sb.append("\r\n\r\n").append(sw.toString());
		
		System.out.println(sb.toString());
		
		return sb.toString();
	}

	
	private void addCDATA(Element ele, String sql){
		sql = "\r\n" + sql + "\r\n";
		sql = sql.replaceAll("\r\n", "\r\n\t\t");
		ele.addCDATA(sql);
	}
}
