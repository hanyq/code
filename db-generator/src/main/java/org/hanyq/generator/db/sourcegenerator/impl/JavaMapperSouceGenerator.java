package org.hanyq.generator.db.sourcegenerator.impl;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hanyq.generator.db.config.Configs;
import org.hanyq.generator.db.meta.DbTable;
import org.hanyq.generator.db.meta.MapperDefinition;
import org.hanyq.generator.db.sourcegenerator.AbstractSourceGenerator;


public class JavaMapperSouceGenerator extends AbstractSourceGenerator<MapperDefinition> {
	
	public JavaMapperSouceGenerator(String baseDirectory){
		super(baseDirectory);
	}
	
	@Override
	protected String getSouceName(MapperDefinition mapperDefinition) {
		String beanName = mapperDefinition.getTable().getBeanName();
		
		return beanName + "Mapper.java";
	}

	@Override
	protected String doGeneratorSource(MapperDefinition mapperDefinition) {
		DbTable table = mapperDefinition.getTable();
		String beanName = table.getBeanName();
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(Configs.JAVA_MAPPER_PACKAGE).append(";\r\n\r\n");
		
		Set<String> importSet = new TreeSet<String>();
		importSet.add("java.util.List");
		importSet.add("org.apache.ibatis.annotations.Param");
		importSet.add(table.getBeanFullName());
		
		for(String importItem : importSet){
			sb.append("import ").append(importItem).append(";\r\n");
		}
		sb.append("\r\n");
		
		sb.append("public interface ").append(beanName).append("Mapper { \r\n");
		sb.append("\r\n");
		
		
		if(mapperDefinition.getLoadMaxColumn() != null){
			if("long".equals(table.getFieldType(mapperDefinition.getLoadMaxColumn()))){
				sb.append("\t").append("Long");
			}
			else{
				sb.append("\t").append("Integer");
			}
			sb.append(" ").append(mapperDefinition.getLoadMaxSqlName()).append("(@Param(\"minId\") long minId, @Param(\"maxId\") long maxId);").append("\r\n");
			sb.append("\r\n");
		}
		
		//loadXXX   根据主键load
		sb.append("\t").append(beanName).append(" ").append("load").append(beanName).append("(");
		for(String pkey : table.getPkeys()){
			sb.append("@Param(\"").append(pkey).append("\") ").append(table.getFieldType(pkey)).append(" ").append(pkey).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		
		sb.append(");\r\n");
		
		//load--XX 根据自定义条件load
		for(Map.Entry<String, String[]> loadEntry : mapperDefinition.getLoadSqlMap().entrySet()){
			sb.append("\r\n");
			sb.append("\t").append("List<").append(beanName).append("> ").append(loadEntry.getKey()).append("(");
			for(String whereField : loadEntry.getValue()){
				sb.append("@Param(\"").append(whereField).append("\") ").append(table.getFieldType(whereField)).append(" ").append(whereField).append(", ");
			}
			if(loadEntry.getValue().length > 0){
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
			}
			
			sb.append(");\r\n");
		}
		
		
		String loadByIdsSql = mapperDefinition.getLoadByIdsSqlName();
		if(loadByIdsSql != null && !loadByIdsSql.isEmpty()){
			sb.append("\r\n");
			sb.append("\t").append("List<").append(beanName).append("> ").append(loadByIdsSql).append("(");
			sb.append("List<").append(table.getFieldClassType(mapperDefinition.getLoadByCollectionColumn())).append("> ");
			sb.append(mapperDefinition.getLoadByCollectionColumn()).append("s);\r\n");
		}
		
		//saveXXX
		sb.append("\r\n");
		sb.append("\t").append("void save").append(beanName).append("(");
		sb.append(beanName).append(" ").append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(");\r\n");
		
		
		//updateXXX
		for(Map.Entry<String, String[]> updatgeEntry : mapperDefinition.getUpdateSqlMap().entrySet()){
			sb.append("\r\n");
			sb.append("\t").append("void ").append(updatgeEntry.getKey()).append("(");
			sb.append(beanName).append(" ").append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(");\r\n");
		}
		
		//deleteXXX
		/*sb.append("\r\n");
		sb.append("\t").append("void delete").append(beanName).append("(");
		sb.append(beanName).append(" ").append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(");\r\n");
	*/
		sb.append("\r\n");
		sb.append("\t").append("void delete").append(beanName).append("(");
		for(String pkey : table.getPkeys()){
			sb.append("@Param(\"").append(pkey).append("\") ").append(table.getFieldType(pkey)).append(" ").append(pkey).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		
		sb.append(");\r\n");
	
		
		for(Map.Entry<String, String> entry : mapperDefinition.getDeleteMap().entrySet()){
			String deleteBySqlName = entry.getKey();
			if(deleteBySqlName != null && !deleteBySqlName.isEmpty()){
				String deleteByColumn = entry.getValue();
				sb.append("\r\n");
				sb.append("\t").append("void ").append(deleteBySqlName).append("(");
				sb.append("@Param(\"").append(deleteByColumn).append("\") ").append(table.getFieldType(deleteByColumn)).append(" ").append(deleteByColumn).append(");\r\n");
			}
		}
		
		if(mapperDefinition.isDeleteAll()){
			sb.append("\r\n");
			sb.append("\t").append("void deleteAll").append(beanName).append("();\r\n");
		}
		
		sb.append("\r\n").append("}");
		
		
		return sb.toString();
	}

}
