package com.kingdowin.config.db.meta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Mapper的定义
 * @author hanyongqiang
 *
 * @date 2013-6-17
 */
public class MapperDefinition {
	private DbTable table;
	private Map<String, String[]> loadSqlMap = new LinkedHashMap<String, String[]>();
	private Map<String, String[]> updateSqlMap = new LinkedHashMap<String, String[]>();
	private String loadMaxSqlName;
	private String loadMaxColumn;
	
	
	public MapperDefinition(DbTable table){
		this.table = table;
		
		List<String> pkeys = new ArrayList<String>();
		List<String> nonPkeys = new ArrayList<String>();
		for(DbField field : table.getFields()){
			if(field.isPkey()){
				pkeys.add(field.getBeanName());
			}else{
				nonPkeys.add(field.getBeanName());
			}
		}
		
		//loadSqlMap.put("load" + table.getBeanName(), pkeys.toArray(new String[0]));
		loadSqlMap.put("loadAll" + table.getBeanName(), new String[0]);
		
		updateSqlMap.put("update" + table.getBeanName(), nonPkeys.toArray(new String[0]));
	}
	
	public void addLoadSql(String sqlName, String... whereConditions){
		loadSqlMap.put(sqlName, whereConditions);
	}
	
	public void addUpdateSql(String sqlName, String... updateFields){
		updateSqlMap.put(sqlName, updateFields);
	}

	public DbTable getTable() {
		return table;
	}

	public void setTable(DbTable table) {
		this.table = table;
	}

	public Map<String, String[]> getLoadSqlMap() {
		return loadSqlMap;
	}

	public void setLoadSqlMap(Map<String, String[]> loadSqlMap) {
		this.loadSqlMap = loadSqlMap;
	}

	public Map<String, String[]> getUpdateSqlMap() {
		return updateSqlMap;
	}

	public void setUpdateSqlMap(Map<String, String[]> updateSqlMap) {
		this.updateSqlMap = updateSqlMap;
	}

	public String getLoadMaxColumn() {
		return loadMaxColumn;
	}

	public void setLoadMaxColumn(String loadMaxColumn) {
		this.loadMaxColumn = loadMaxColumn;
	}

	public String getLoadMaxSqlName() {
		return loadMaxSqlName;
	}

	public void setLoadMaxSqlName(String loadSqlName) {
		this.loadMaxSqlName = loadSqlName;
	}
	
	
}
