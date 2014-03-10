package org.hanyq.generator.db.sourcegenerator.impl;

import java.util.List;

import org.hanyq.generator.db.meta.MapperDefinition;
import org.hanyq.generator.db.sourcegenerator.AbstractSourceGenerator;


public class AllTableSourceGenerator extends AbstractSourceGenerator<List<MapperDefinition>> {
	private TableSourceGenerator tableGenerator;
	
	public AllTableSourceGenerator(String baseDirectory){
		super(baseDirectory);
		tableGenerator = new TableSourceGenerator(baseDirectory);
	}
	
	@Override
	protected String getSouceName(List<MapperDefinition> mapperDefinitions) {
		return "all_tables.sql";
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
