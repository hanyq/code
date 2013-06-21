package org.hanyq.generator.db.meta;

import java.util.HashMap;
import java.util.Map;

import org.hanyq.generator.db.utils.NameConvertor;


public class DbField {
	private static Map<Class<?>, DbFieldMeta> java2DbTypeMap;
	
	private boolean isPkey;
	private String name;
	private Class<?> type;
	
	public static class DbFieldMeta{
		String dbType;
		String defaultValue;
		
		public DbFieldMeta(String dbType, String defaultValue){
			this.dbType = dbType;
			this.defaultValue = defaultValue;
		}
	}
	
	
	static{
		java2DbTypeMap = new HashMap<Class<?>, DbFieldMeta>();
		
		java2DbTypeMap.put(byte.class, new DbFieldMeta("tinyint(4)", "'0'"));
		java2DbTypeMap.put(short.class, new DbFieldMeta("smallint(6)", "'0'"));
		java2DbTypeMap.put(int.class, new DbFieldMeta("int(11)", "'0'"));
		java2DbTypeMap.put(long.class, new DbFieldMeta("bigint(20)", "'0'"));
		java2DbTypeMap.put(String.class, new DbFieldMeta("varchar(50)", "''"));
		
	}
	
	public DbField(boolean isPkey, String name, Class<?> type) {
		super();
		this.isPkey = isPkey;
		this.name = name;
		this.type = type;
	}
	
	private DbFieldMeta getTypeMeta(){
		DbFieldMeta meta = java2DbTypeMap.get(type);
		
		if(meta == null){
			throw new RuntimeException("type not regist. type  = "  + type);
		}
		
		return meta;
	}
	
	public String getDbType(){
		return getTypeMeta().dbType; 
	}
	
	public String getDbDefaultValue(){
		return this.getTypeMeta().defaultValue;
	}
	
	public String getDbName(){
		return NameConvertor.bean2db(name);
	}
	
	public String getDbPlaceHolderName(){
		return "#{" + getBeanName() + "}";
	}
	
	public String getBeanName(){
		return NameConvertor.db2bean(name);
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}

	public boolean isPkey() {
		return isPkey;
	}

	public void setPkey(boolean isPkey) {
		this.isPkey = isPkey;
	}
	
	
	
	
}
