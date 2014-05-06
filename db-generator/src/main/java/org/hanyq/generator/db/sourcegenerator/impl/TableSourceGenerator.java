package org.hanyq.generator.db.sourcegenerator.impl;

import java.util.List;

import org.hanyq.generator.db.meta.DbField;
import org.hanyq.generator.db.meta.DbTable;
import org.hanyq.generator.db.meta.MapperDefinition;
import org.hanyq.generator.db.sourcegenerator.AbstractSourceGenerator;
import org.hanyq.generator.db.utils.NameConvertor;


public class TableSourceGenerator extends AbstractSourceGenerator<MapperDefinition> {
	
	public TableSourceGenerator(String baseDirectory){
		super(baseDirectory);
	}
	
	@Override
	protected String getSouceName(MapperDefinition mapperDefinition) {
		return mapperDefinition.getTable().getDbName() + ".sql";
	}

	@Override
	protected String doGeneratorSource(MapperDefinition mapperDefinition) {
		DbTable table = mapperDefinition.getTable();
		StringBuilder sb = new StringBuilder();
		
		sb.append("DROP TABLE ").append(this.toDbName(table.getDbName())).append(";").append("\r\n");
		sb.append("CREATE TABLE ").append(this.toDbName(table.getDbName())).append(" (").append("\r\n");
		for(DbField field : table.getFields()){
			sb.append("\t").append(toDbName(field.getDbName())).append(" ").append(field.getDbType()).append(" NOT NULL  DEFAULT ").append(field.getDbDefaultValue());
			
			sb.append(",\r\n");
		}
		
		sb.append("\t").append("PRIMARY KEY (");
		
		List<String> pkeys = table.getPkeys();
		for(String peky : pkeys){
			String dbName = NameConvertor.bean2db(peky);
			sb.append(toDbName(dbName)).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		
		sb.append(")").append("\r\n");
		
		sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8; ").append("\r\n");
		
		return sb.toString();
	}
	
	private String toDbName(String name){
		if(!name.startsWith("`")){
			return "`" + name + "`";
		}
		
		return name;
	}
	
}
