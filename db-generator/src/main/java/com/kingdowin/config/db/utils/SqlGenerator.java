package com.kingdowin.config.db.utils;

import java.util.Arrays;
import java.util.List;

import com.kingdowin.config.db.meta.DbField;
import com.kingdowin.config.db.meta.DbTable;
import com.kingdowin.newlol.domain.user.User;

public class SqlGenerator {
	
	public static String generateLoadMaxSql(DbTable table, String maxColumn){
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT \r\n\t");
		sb.append("max(").append(NameConvertor.bean2db(maxColumn)).append(")");
		sb.append("\r\n").append("FROM ").append("\r\n\t");
		sb.append(table.getName());
		
		return sb.toString();
	}
	
	public static String generateLoadSql(DbTable table){
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT \r\n\t");
		for(DbField field : table.getFields()){
			sb.append(field.getDbName()).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\r\n").append("FROM ").append("\r\n\t");
		sb.append(table.getName());
		
		sb.append("\r\nWHERE ");
		int i = 0;
		for(DbField field : table.getFields()){
			if(!field.isPkey()){
				continue;
			}
			if(i++ != 0){
				sb.append(" AND ");
			}
			sb.append("\r\n\t");
			
			sb.append(field.getDbName()).append(" = ").append(field.getDbPlaceHolderName());
		}
		
		return sb.toString();
	}
	
	public static String generateLoadAllSql(DbTable table){
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT \r\n\t");
		for(DbField field : table.getFields()){
			sb.append(field.getDbName()).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\r\n").append("FROM ").append("\r\n\t");
		sb.append(table.getName());
		
		return sb.toString();
	}
	

	public static String generateLoadByWhereSql(DbTable table, String... whereConditions){
		List<String> whereConditionList = Arrays.asList(whereConditions);
		if(whereConditionList.isEmpty()){
			return generateLoadAllSql(table);
		}
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT \r\n\t");
		for(DbField field : table.getFields()){
			sb.append(field.getDbName()).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append("\r\n").append("FROM ").append("\r\n\t");
		sb.append(table.getName());
		
		sb.append("\r\nWHERE ");
		int i = 0;
		for(DbField field : table.getFields()){
			if(!whereConditionList.contains(field.getBeanName())){
				continue;
			}
			if(i++ != 0){
				sb.append(" AND ");
			}
			sb.append("\r\n\t");
			
			sb.append(field.getDbName()).append(" = ").append(field.getDbPlaceHolderName());
		}
		return sb.toString();
	}
	
	
	public static String generatSaveSql(DbTable table){
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT INTO \r\n\t").append(table.getName()).append("(");
		for(DbField field : table.getFields()){
			sb.append(field.getDbName()).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") ");
		
		sb.append("\r\nVALUES\r\n\t(");
		for(DbField field : table.getFields()){
			sb.append("#{").append(field.getBeanName()).append("}, ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") ");
		
		return sb.toString();
	}
	
	public static String generatUpdateSql(DbTable table){
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE \r\n\t").append(table.getName());
		sb.append("\r\nSET ");
		for(DbField field : table.getFields()){
			if(field.isPkey()){
				continue;
			}
			sb.append("\r\n\t").append(field.getDbName()).append(" = ").append(field.getDbPlaceHolderName()).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") ");
		
		sb.append("\r\nWHERE ");
		int i = 0;
		for(DbField field : table.getFields()){
			if(!field.isPkey()){
				continue;
			}
			if(i++ != 0){
				sb.append(" AND ");
			}
			sb.append("\r\n\t");
			
			sb.append(field.getDbName()).append(" = ").append(field.getDbPlaceHolderName());
		}
		
		return sb.toString();
	}
	
	public static String generatUpdateSql(DbTable table, String... updateFields){
		List<String> updateFieldList = Arrays.asList(updateFields);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE \r\n\t").append(table.getName());
		sb.append("\r\nSET ");
		for(DbField field : table.getFields()){
			if(field.isPkey()){
				continue;
			}
			if(!updateFieldList.contains(field.getBeanName())){
				continue;
			}
			sb.append("\r\n\t").append(field.getDbName()).append(" = ").append(field.getDbPlaceHolderName()).append(", ");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" ");
		
		sb.append("\r\nWHERE ");
		int i = 0;
		for(DbField field : table.getFields()){
			if(!field.isPkey()){
				continue;
			}
			if(i++ != 0){
				sb.append(" AND ");
			}
			sb.append("\r\n\t");
			
			sb.append(field.getDbName()).append(" = ").append(field.getDbPlaceHolderName());
		}
		
		return sb.toString();
	}
	
	public static String generatDeleteSql(DbTable table){
		StringBuilder sb = new StringBuilder();
		
		sb.append("DELETE FROM \r\n\t").append(table.getName());
		
		sb.append("\r\nWHERE ");
		int i = 0;
		for(DbField field : table.getFields()){
			if(!field.isPkey()){
				continue;
			}
			if(i++ != 0){
				sb.append(" AND ");
			}
			sb.append("\r\n\t");
			
			sb.append(field.getDbName()).append(" = ").append(field.getDbPlaceHolderName());
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args){
		DbTable table = Java2table.java2table(User.class, "userId");
		
		
		String loadSql = SqlGenerator.generateLoadSql(table);
		System.out.println(loadSql);
		
		String loadAllSql = SqlGenerator.generateLoadAllSql(table);
		System.out.println(loadAllSql);
		
		String loadByWhereSql = SqlGenerator.generateLoadByWhereSql(table, "userName");
		System.out.println(loadByWhereSql);
		
		String saveSql = SqlGenerator.generatSaveSql(table);
		
		System.out.println(saveSql);
		
		String updateSql = SqlGenerator.generatUpdateSql(table);
		
		System.out.println(updateSql);
		
		String updateTSql = SqlGenerator.generatUpdateSql(table, "userName");
		
		System.out.println(updateTSql);
		

		String deleteSql = SqlGenerator.generatDeleteSql(table);
		
		System.out.println(deleteSql);
	}
}
