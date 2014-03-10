package org.hanyq.generator.db.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hanyq.generator.db.meta.DbField;
import org.hanyq.generator.db.meta.DbTable;


public class Java2table {
	
	public static DbTable java2table(Class<?> clazz, String... pkeys){
		List<String> pkeyList = Arrays.asList(pkeys);
		DbTable dbTable = new DbTable(clazz);
		
		String table = NameConvertor.bean2db(clazz.getSimpleName());
		dbTable.setName(table);
		
		Method[] methods = clazz.getDeclaredMethods();
		
		Map<String, List<Method>> allGetfields = new LinkedHashMap<String, List<Method>>();
		Map<String, List<Method>> allSetfields = new LinkedHashMap<String, List<Method>>();
		for(Method method : methods){
			if(!Modifier.isPublic(method.getModifiers()) || Modifier.isStatic(method.getModifiers())){
				continue;
			}
			
			String methodName = method.getName();
			if(methodName.startsWith("get") && methodName.length() > "get".length()){
				if(method.getParameterTypes().length != 0){
					continue;
				}
				String fieldName = methodName.substring("get".length());
				
				List<Method> methodList = allGetfields.get(fieldName);
				if(methodList == null){
					methodList = new ArrayList<Method>();
					methodList.add(method);
				}
				allGetfields.put(fieldName, methodList);
			}/*else if(methodName.startsWith("is") && methodName.length() > "is".length()){
				String fieldName = methodName.substring("is".length());
				
				allGetfields.add(fieldName);
			}*/
			else if(methodName.startsWith("set") && methodName.length() > "set".length()){
				if(method.getParameterTypes().length != 1){
					continue;
				}
				
				String fieldName = methodName.substring("set".length());
				List<Method> methodList = allSetfields.get(fieldName);
				if(methodList == null){
					methodList = new ArrayList<Method>();
					methodList.add(method);
				}
				allSetfields.put(fieldName, methodList);
			}
			
		}
		
		for(Map.Entry<String, List<Method>> getfield : allGetfields.entrySet()){
			String fieldName = getfield.getKey();
			List<Method> getMethodList = getfield.getValue();
			List<Method> setMethodList = allSetfields.get(fieldName);
			if(setMethodList == null){
				continue;
			}
			Method getMethod = null;
			for(Method tempGetMethod : getMethodList){
				for(Method tempSetMethod : setMethodList){
					if(!tempGetMethod.getReturnType().equals(tempSetMethod.getParameterTypes()[0])){
						continue;
					}else{
						getMethod = tempGetMethod;
					}
				}
			}
			
			fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
			
			DbField dbField = new DbField(pkeyList.contains(fieldName), fieldName, getMethod.getReturnType());
			dbTable.addField(dbField);
		}
		
		
		
		/*Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			String fieldName = field.getName();
			
			Class<?> type = field.getType();
			
			DbField dbField = new DbField(pkeyList.contains(fieldName), fieldName, type);
			
			dbTable.addField(dbField);
		}*/
		
		return dbTable;
	}
	
}
