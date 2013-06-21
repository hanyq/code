package com.kingdowin.config.cfg.excel.convertor.classgenerator;

import com.kingdowin.config.cfg.excel.meta.ExcelMeta;

public class JavaContainerClassGenerator extends AbstractJavaClassGenerator {
	protected String makeJavaContent(ExcelMeta meta) {
		StringBuilder sb = new StringBuilder();
		String javaPackage = meta.getJavaPackage();
		String javaClass = getClassName(meta);
		if ((javaPackage != null) && (!javaPackage.isEmpty())) {
			sb.append("package ").append(javaPackage).append(";\r\n\r\n");
		}

		sb.append("import java.util.List;\r\n");

		sb.append("\r\n");
		sb.append("import org.simpleframework.xml.ElementList;\r\n");
		sb.append("import org.simpleframework.xml.Root;\r\n");

		sb.append("\r\n");
		sb.append("import com.kingdowin.framework.cfg.AbstractCfgContainer;\r\n");
		sb.append("\r\n");

		sb.append("@Root(name=\"" + meta.getJavaConfigClass() + "\")\r\n");
		sb.append("public class ").append(javaClass)
				.append(" extends AbstractCfgContainer").append("{\r\n");
		sb.append("\r\n");

		sb.append("\t@ElementList(inline=true, entry=\""
				+ meta.getJavaConfigClass() + "\")\r\n");
		sb.append("\t").append("private List<")
				.append(meta.getJavaMapperClass()).append("> ")
				.append("mappers;\r\n\r\n");

		sb.append("\r\n\r\n");

		sb.append("\t").append("@Override").append("\r\n");
		sb.append("\t").append("protected void reload() throws Exception {")
				.append("\r\n");
		sb.append("\t\t\r\n");
		sb.append("\t\t").append("for(").append(meta.getJavaMapperClass())
				.append(" ").append("mapper : mappers){").append("\r\n");
		sb.append("\t\t\t").append(meta.getJavaConfigClass())
				.append(" config = new ").append(meta.getJavaConfigClass())
				.append("(mapper);\r\n");
		sb.append("\t\t\t\r\n");
		sb.append("\t\t\t//TODO\r\n");

		sb.append("\t\t}").append("\r\n");
		sb.append("\t}\r\n");

		sb.append("\r\n");

		sb.append("\t@Override\r\n");
		sb.append("\t").append("public String toString(){").append("\r\n");

		sb.append("\t\t").append("return mappers.toString();").append("\r\n");

		sb.append("\t}");
		sb.append("\r\n");

		sb.append("}");

		return sb.toString();
	}

	protected String getClassName(ExcelMeta meta) {
		return meta.getJavaContainerClass();
	}
}