package com.kingdowin.config.db.sourcegenerator.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.kingdowin.config.db.config.Configs;
import com.kingdowin.config.db.meta.DbTable;
import com.kingdowin.config.db.meta.MapperDefinition;
import com.kingdowin.config.db.sourcegenerator.AbstractSourceGenerator;

public class DaoContainerSourceGenerator extends AbstractSourceGenerator<List<MapperDefinition>> {
	
	public DaoContainerSourceGenerator(String baseDirectory){
		super(baseDirectory);
	}

	@Override
	protected String getSouceName(List<MapperDefinition> mapperDefinitions) {
		return "DaoContainers.java";
	}

	@Override
	protected String doGeneratorSource(List<MapperDefinition> mapperDefinitions) {
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(Configs.JAVA_DAO_CONTAINER_PACKAGE).append(";\r\n\r\n");
		
		Set<String> importSet = new TreeSet<String>();
		importSet.add("com.kingdowin.newlol.utils.BeanContainers");
		for(MapperDefinition def : mapperDefinitions){
			importSet.add(Configs.JAVA_DAO_PACKAGE + "." + def.getTable().getDaoName());
		}
		
		for(String importItem : importSet){
			sb.append("import ").append(importItem).append(";\r\n");
		}
		sb.append("\r\n");
		
		
		sb.append("public class DaoContainers { ").append("\r\n");
		
		//定义变量
		for(MapperDefinition def : mapperDefinitions){
			sb.append("\r\n");
			DbTable table = def.getTable();
			sb.append("\t").append("private static ").append(table.getDaoName()).append(" ").append(table.getDaoObjName()).append(";\r\n");
		}
		
		//init
		sb.append("\r\n").append("\t").append("public static void init() { ").append("\r\n");
		for(MapperDefinition def : mapperDefinitions){
			DbTable table = def.getTable();
			sb.append("\t\t").append(table.getDaoObjName()).append(" = BeanContainers.getBean(").append(table.getDaoName()).append(".class").append(");\r\n");
		}
		sb.append("\t").append("}\r\n");
		
		
		//get
		for(MapperDefinition def : mapperDefinitions){
			DbTable table = def.getTable();
			sb.append("\r\n");
			sb.append("\t").append("public static ").append(table.getDaoName()).append(" get").append(table.getDaoName()).append("() {\r\n");
			sb.append("\t\t").append("return ").append(table.getDaoObjName()).append(";\r\n");
			sb.append("\t").append("}\r\n");
		}
		
		sb.append("").append("}\r\n");
		
		return sb.toString();
	}
	
	
	
	
}
