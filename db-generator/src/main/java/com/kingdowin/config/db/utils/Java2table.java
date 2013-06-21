package com.kingdowin.config.db.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.kingdowin.config.db.meta.DbField;
import com.kingdowin.config.db.meta.DbTable;

public class Java2table {
	
	public static DbTable java2table(Class<?> clazz, String... pkeys){
		List<String> pkeyList = Arrays.asList(pkeys);
		DbTable dbTable = new DbTable(clazz);
		
		String table = NameConvertor.bean2db(clazz.getSimpleName());
		dbTable.setName(table);
		
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			String fieldName = field.getName();
			
			Class<?> type = field.getType();
			
			DbField dbField = new DbField(pkeyList.contains(fieldName), fieldName, type);
			
			dbTable.addField(dbField);
		}
		
		return dbTable;
	}
	
}
