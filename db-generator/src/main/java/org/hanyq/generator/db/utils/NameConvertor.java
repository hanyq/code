package org.hanyq.generator.db.utils;

public class NameConvertor {
	
	public static String db2Class(String dbName){
		String bean = db2bean(dbName);
		
		return Character.toUpperCase(bean.charAt(0)) + bean.substring(1);
	}
	
	/**
	 * 数据库名称转换成java变量名称
	 * @param dbName
	 * @return
	 */
	public static String db2bean(String dbName){
		String[] words = dbName.split("_");
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < words.length; i++){
			if(i == 0){
				sb.append(words[i]);
			}else{
				String word = words[i];
				if(!word.isEmpty()){
					sb.append(Character.toUpperCase(word.charAt(0)));
					sb.append(word.substring(1));
				}
				
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Java变量名称转换成数据库名称
	 * @param beanName
	 * @return
	 */
	public static String bean2db(String beanName){
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(char ch : beanName.toCharArray()){
			if(i == 0){
				sb.append(ch);
			}else{
				if(Character.isUpperCase(ch)){
					sb.append("_");
				}
				
				sb.append(Character.toLowerCase(ch));
			}
			i++;
		}
		
		return sb.toString().toLowerCase();
	}
	
	
	
	public static void main(String[] args){
		String beanName = "howAreYou";
		String dbName = NameConvertor.bean2db(beanName);
		
		System.out.println(dbName);
		
		beanName = NameConvertor.db2bean(dbName);
		
		System.out.println(beanName);
	}
}
