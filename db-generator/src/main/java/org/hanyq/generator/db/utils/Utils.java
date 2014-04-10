package org.hanyq.generator.db.utils;

public class Utils {
	public static String getWrapperType(Class<?> clazz){
		if(clazz.isPrimitive()){
			if(clazz.equals(int.class)){
				return Integer.class.getSimpleName();
			}else if(clazz.equals(long.class)){
				return Long.class.getSimpleName();
			}else{
				throw new RuntimeException("自己去实现");
			}
		}
		
		return clazz.getSimpleName();
	}
}
