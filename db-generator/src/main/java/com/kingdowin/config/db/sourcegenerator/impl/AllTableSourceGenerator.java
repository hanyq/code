package com.kingdowin.config.db.sourcegenerator.impl;

import java.util.List;

import com.kingdowin.config.db.meta.MapperDefinition;
import com.kingdowin.config.db.sourcegenerator.AbstractSourceGenerator;

public class AllTableSourceGenerator extends AbstractSourceGenerator<List<MapperDefinition>> {
	private TableSourceGenerator tableGenerator;
	
	public AllTableSourceGenerator(String baseDirectory){
		super(baseDirectory);
		tableGenerator = new TableSourceGenerator(baseDirectory);
	}
	
	@Override
	protected String getSouceName(List<MapperDefinition> mapperDefinitions) {
		return "newlol_tables.sql";
	}

	@Override
	protected String doGeneratorSource(List<MapperDefinition> mapperDefinitions) {
		StringBuilder sb = new StringBuilder();
		
		for(MapperDefinition def : mapperDefinitions){
			sb.append(tableGenerator.doGeneratorSource(def)).append("\r\n");
		}
		
		
		return sb.toString();
	}

}
