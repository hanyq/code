package com.kingdowin.config.db.meta;

import java.util.ArrayList;
import java.util.List;

import com.kingdowin.config.db.config.Configs;
import com.kingdowin.config.db.utils.NameConvertor;

public class DbTable {
	private Class<?> clazz;
	private String name;
	private List<DbField> fields;
	
	private List<String> pkeys;
	private List<String> nonPkeys;
	
	public DbTable(Class<?> clazz){
		this.clazz = clazz;
		fields = new ArrayList<DbField>();
	}
	
	private void initPkeys(){
		List<String> pkeys = new ArrayList<String>();
		List<String> nonPkeys = new ArrayList<String>();
		
		for(DbField field : fields){
			if(field.isPkey()){
				pkeys.add(field.getBeanName());
			}else{
				nonPkeys.add(field.getBeanName());
			}
		}
		
		this.pkeys = pkeys;
		this.nonPkeys = nonPkeys;
	}
	
	public List<String> getPkeys(){
		if(this.pkeys == null){
			this.initPkeys();
		}
		
		return this.pkeys;
	}
	
	public List<String> getNonPkeys(){
		if(this.nonPkeys == null){
			this.initPkeys();
		}
		
		return this.nonPkeys;
	}
	
	public String getFieldType(String filedName){
		for(DbField field : fields){
			if(field.getBeanName().equals(filedName)){
				return field.getType().getSimpleName();
			}
		}
		
		throw new RuntimeException("field not found. fieldName = " + filedName + ", class = " + clazz.getName());
	}
	
	public String getBeanFullName(){
		return clazz.getName();
	}
	
	public String getMapperFullName(){
		return Configs.JAVA_MAPPER_PACKAGE + "." + this.getBeanName() + "Mapper";
	}
	
	public void addField(DbField field){
		fields.add(field);
	}
	
	public String getDbName(){
		return NameConvertor.bean2db(name);
	}
	
	public String getBeanName(){
		return NameConvertor.db2Class(name);
	}
	
	public String getBeanObjName(){
		String beanName = NameConvertor.db2Class(name);
		
		return Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);
	}
	
	public String getDaoName(){
		String beanName = NameConvertor.db2Class(name);
		
		return beanName + "Dao";
	}
	
	public String getDaoObjName(){
		return getBeanObjName() + "Dao";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DbField> getFields() {
		return fields;
	}
	public void setFields(List<DbField> fields) {
		this.fields = fields;
	}
	
	
	
}
