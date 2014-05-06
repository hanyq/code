package org.hanyq.generator.db.sourcegenerator.impl;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.hanyq.generator.db.config.Configs;
import org.hanyq.generator.db.meta.DbTable;
import org.hanyq.generator.db.meta.MapperDefinition;
import org.hanyq.generator.db.sourcegenerator.AbstractSourceGenerator;


public class DaoSourceGenerator extends AbstractSourceGenerator<MapperDefinition> {
	
	public DaoSourceGenerator(String baseDirectory){
		super(baseDirectory);
	}
	
	@Override
	protected String getSouceName(MapperDefinition mapperDefinition) {
		String beanName = mapperDefinition.getTable().getBeanName();
		
		return beanName + "Dao.java";
	}

	@Override
	protected String doGeneratorSource(MapperDefinition mapperDefinition) {
		DbTable table = mapperDefinition.getTable();
		String beanName = table.getBeanName();
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(Configs.JAVA_DAO_PACKAGE).append(";\r\n\r\n");
		
		Set<String> importSet = new TreeSet<String>();
		importSet.add("java.util.List");
		importSet.add("org.springframework.beans.factory.annotation.Autowired");
		importSet.add("org.springframework.stereotype.Component");
		importSet.add(Configs.JAVA_DAO_CONTAINER_PACKAGE + ".AbstractMybatisDao");
		importSet.add(table.getMapperFullName());
		importSet.add(table.getBeanFullName());
		
		for(String importItem : importSet){
			sb.append("import ").append(importItem).append(";\r\n");
		}
		sb.append("\r\n");
		
		sb.append("@Component").append("\r\n");
		sb.append("public class ").append(beanName).append("Dao extends AbstractMybatisDao { \r\n");
		//sb.append("\r\n");
		
		sb.append("\t").append("@Autowired").append("\r\n");
		sb.append("\tprivate ").append(beanName).append("Mapper ").append("mapper;").append("\r\n");
		sb.append("\r\n");
		
		
		if(mapperDefinition.getLoadMaxColumn() != null){
			String fieldType  = table.getFieldType(mapperDefinition.getLoadMaxColumn());
			sb.append("\tpublic ").append(table.getFieldType(mapperDefinition.getLoadMaxColumn())).append(" ");
			sb.append(mapperDefinition.getLoadMaxSqlName()).append("(long minId, long maxId){").append("\r\n");
			
			if("long".equals(fieldType)){
				sb.append("\t\t").append("Long currMaxId = mapper.").append(mapperDefinition.getLoadMaxSqlName()).append("(minId, maxId);").append("\r\n");
				sb.append("\t\t").append("return currMaxId == null ? 0 : currMaxId.longValue();").append("\r\n");
			}else{
				sb.append("\t\t").append("Integer currMaxId = mapper.").append(mapperDefinition.getLoadMaxSqlName()).append("(minId, maxId);").append("\r\n");
				sb.append("\t\t").append("return currMaxId == null ? 0 : currMaxId.intValue();").append("\r\n");
			}
			sb.append("\t").append("}").append("\r\n");
			sb.append("\r\n");
		}
		
		//loadXXX   根据主键load
		sb.append("\t").append("public ").append(beanName).append(" ").append("load").append(beanName).append("(");
		for(String pkey : table.getPkeys()){
			sb.append(table.getFieldType(pkey)).append(" ").append(pkey).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		
		sb.append(") { \r\n");
		sb.append("\t\t").append("return mapper.load").append(beanName).append("(");
		for(String pkey : table.getPkeys()){
			sb.append(pkey).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		
		sb.append(");\r\n");
		sb.append("\t").append("} \r\n");
		
		//load--XX 根据自定义条件load
		for(Map.Entry<String, String[]> loadEntry : mapperDefinition.getLoadSqlMap().entrySet()){
			sb.append("\r\n");
			sb.append("\t").append("public List<").append(beanName).append("> ").append(loadEntry.getKey()).append("(");
			for(String whereField : loadEntry.getValue()){
				sb.append(table.getFieldType(whereField)).append(" ").append(whereField).append(", ");
			}
			if(loadEntry.getValue().length > 0){
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
			}
			
			sb.append(") {\r\n");
			
			sb.append("\t\t").append("return mapper.").append(loadEntry.getKey()).append("(");
			for(String whereField : loadEntry.getValue()){
				sb.append(whereField).append(", ");
			}
			if(loadEntry.getValue().length > 0){
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
			}
			
			sb.append(");\r\n");
			sb.append("\t").append("} \r\n");
			
		}
		
		String loadByIdsSql = mapperDefinition.getLoadByIdsSqlName();
		if(loadByIdsSql != null && !loadByIdsSql.isEmpty()){
			String paramName = mapperDefinition.getLoadByCollectionColumn() + "s";
			sb.append("\r\n");
			sb.append("\t").append("public List<").append(beanName).append("> ").append(loadByIdsSql).append("(");
			sb.append("List<").append(table.getFieldClassType(mapperDefinition.getLoadByCollectionColumn())).append("> ");
			sb.append(paramName).append("){\r\n");
			sb.append("\t\t").append("if(").append(paramName).append(" == null || ").append(paramName).append(".isEmpty()){").append("\r\n");
			sb.append("\t\t\t").append("return emptyList();\r\n");
			sb.append("\t\t}\r\n");
			sb.append("\t\t").append("return mapper.").append(loadByIdsSql).append("(").append(mapperDefinition.getLoadByCollectionColumn()).append("s);\r\n");
			sb.append("\t").append("}\r\n");
		}
		
		
		//saveXXX
		sb.append("\r\n");
		sb.append("\t").append("public void save").append(beanName).append("(");
		sb.append(beanName).append(" ").append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(") {\r\n");
		
		sb.append("\t\t").append("mapper.save").append(beanName).append("(").append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(");\r\n");
		
		sb.append("\t").append("} \r\n");
		
		
		//updateXXX
		for(Map.Entry<String, String[]> updatgeEntry : mapperDefinition.getUpdateSqlMap().entrySet()){
			sb.append("\r\n");
			sb.append("\t").append("public void ").append(updatgeEntry.getKey()).append("(");
			sb.append(beanName).append(" ").append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(") {\r\n");
			
			
			sb.append("\t\t").append("mapper.").append(updatgeEntry.getKey()).append("(");
			sb.append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(");\r\n");
			
			sb.append("\t").append("} \r\n");
		}
		
		//deleteXXX
		sb.append("\r\n");
		sb.append("\t").append("public void delete").append(beanName).append("(");
		sb.append(beanName).append(" ").append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(") {\r\n");
		
		sb.append("\t\t").append("delete").append(beanName).append("(");
		for(String pkey : table.getPkeys()){
			sb.append(Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1)).append(".get").append(Character.toUpperCase(pkey.charAt(0)) + pkey.substring(1)).append("(), ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(");\r\n");
		
		sb.append("\t").append("} \r\n");
		
		
		sb.append("\r\n");
		sb.append("\t").append("public void delete").append(beanName).append("(");
		for(String pkey : table.getPkeys()){
			sb.append(table.getFieldType(pkey)).append(" ").append(pkey).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		
		sb.append("){\r\n");
		sb.append("\t\t").append("mapper.delete").append(beanName).append("(");
		for(String pkey : table.getPkeys()){
			sb.append(pkey).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(");\r\n");
		sb.append("\t").append("} \r\n");
		
		
		
		String deleteBySqlName = mapperDefinition.getDeleteBySqlName();
		if(deleteBySqlName != null && !deleteBySqlName.isEmpty()){
			String deleteByColumn = mapperDefinition.getDeleteByColumn();
			sb.append("\r\n");
			sb.append("\t").append("public void ").append(mapperDefinition.getDeleteBySqlName()).append("(");
			sb.append(table.getFieldType(deleteByColumn)).append(" ").append(deleteByColumn).append("){\r\n");
			sb.append("\t\t").append("mapper.").append(mapperDefinition.getDeleteBySqlName()).append("(").append(deleteByColumn).append(");\r\n");
			sb.append("\t}\r\n");
		}
		
		if(mapperDefinition.isDeleteAll()){
			sb.append("\r\n");
			sb.append("\t").append("public void deleteAll").append("(){\r\n");
			sb.append("\t\t").append("mapper.deleteAll").append(beanName).append("();\r\n");
			sb.append("\t}\r\n");
		}
		
		
		sb.append("\r\n").append("}");
		
		
		return sb.toString();
	}
	
}
