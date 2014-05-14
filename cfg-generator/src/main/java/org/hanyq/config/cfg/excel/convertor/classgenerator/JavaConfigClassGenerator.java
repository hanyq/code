package org.hanyq.config.cfg.excel.convertor.classgenerator;

import org.hanyq.config.cfg.excel.meta.ExcelField;
import org.hanyq.config.cfg.excel.meta.ExcelMeta;

public class JavaConfigClassGenerator extends AbstractJavaClassGenerator {
	protected String makeJavaContent(ExcelMeta meta) {
		StringBuilder sb = new StringBuilder();
		String javaPackage = meta.getJavaPackage();
		String javaClass = getClassName(meta);
		if ((javaPackage != null) && (!javaPackage.isEmpty())) {
			sb.append("package ").append(javaPackage).append(";\r\n\r\n");
		}

		sb.append("public class ").append(javaClass).append("{\r\n");

		ExcelField[] fields = meta.getFields();

		sb.append("\t").append("private ").append(meta.getJavaMapperClass())
				.append(" ").append("proxied;\r\n\r\n");

		sb.append("\t").append("public ").append(javaClass).append("(")
				.append(meta.getJavaMapperClass()).append(" proxied) throws Exception {\r\n");
		sb.append("\t\t").append("this.proxied = proxied;\r\n");
		sb.append("\t\t").append("init();\r\n");
		sb.append("\t}");

		sb.append("\r\n\r\n");

		for (ExcelField field : fields) {
			if (!field.isSkip() && field.isVisible()) {
				String type = field.isString() ? "String" : field.getType();
				String methodName = field.getGetName();

				sb.append("\t/**\r\n").append("\t * ").append(field.getDesc())
						.append("\r\n \t */\r\n");
				sb.append("\t").append("public ").append(type).append(" ")
						.append(methodName).append("(){\r\n");
				sb.append("\t\t").append("return proxied.").append(methodName)
						.append("();\r\n");
				sb.append("\t}\r\n\r\n");
			}
		}

		sb.append("\t@Override\r\n");
		sb.append("\t").append("public String toString(){").append("\r\n");

		sb.append("\t\t").append("return proxied.toString();").append("\r\n");

		sb.append("\t}");
		sb.append("\r\n");

		sb.append("\r\n");

		sb.append("/***********************以下为手写代码区域******************************/");
		sb.append("\r\n");
		sb.append("\r\n");

		sb.append("\t").append("public void init() throws Exception {").append("\r\n");
		sb.append("\t\t").append("//写初始化代码").append("\r\n");
		sb.append("\t").append("}").append("\r\n");

		sb.append("\r\n}");

		return sb.toString();
	}

	protected String getClassName(ExcelMeta meta) {
		return meta.getJavaConfigClass();
	}
}