package com.kingdowin.config.cfg.excel.convertor.classgenerator;

import com.kingdowin.config.cfg.excel.meta.ExcelField;
import com.kingdowin.config.cfg.excel.meta.ExcelMeta;


public class JavaMapperClassGenerator extends AbstractJavaClassGenerator
{
  public JavaMapperClassGenerator()
  {
    super.setOverwriteIfExists(true);
  }

  protected String makeJavaContent(ExcelMeta meta)
  {
    StringBuilder sb = new StringBuilder();
    String javaPackage = meta.getJavaPackage();
    String javaClass = getClassName(meta);
    if ((javaPackage != null) && (!javaPackage.isEmpty())) {
      sb.append("package ").append(javaPackage).append(";\r\n\r\n");
    }
    if (hasString(meta)) {
      sb.append("import org.simpleframework.xml.Element;\r\n");
    }
    if (hasNumber(meta)) {
      sb.append("import org.simpleframework.xml.Attribute;\r\n\r\n");
    }
    sb.append("/**\r\n").append(" * 自动生成的代码\r\n *\r\n */\r\n");
    sb.append("public class ").append(javaClass).append("{\r\n");

    ExcelField[] fields = meta.getFields();

    for (ExcelField field : fields) {
    	if(field.isSkip()){
    		continue;
    	}
    	String type = field.isString() ? "String" : field.getType();
    	if (field.isString())
    		sb.append("\t@Element(data=true, required=false)\r\n");
    	else {
    		sb.append("\t@Attribute\r\n");
    	}
    	sb.append("\t").append("private ").append(type).append(" ").append(field.getName()).append(";//").append(field.getDesc()).append("\r\n");
    }

    sb.append("\r\n");
    String type;
    for (ExcelField field : fields) {
    	if(field.isSkip()){
    		continue;
    	}
    	type = field.isString() ? "String" : field.getType();
    	sb.append("\t").append("public ").append(type).append(" ").append(field.getGetName()).append("(){\r\n");
    	if (field.isString())
    		sb.append("\t\t").append("return ").append(field.getName()).append(" == null ? \"\" : ").append(field.getName()).append(" ;\r\n");
    	else {
    		sb.append("\t\t").append("return ").append(field.getName()).append(";\r\n");
    	}
    	sb.append("\t}\r\n\r\n");
    }

    sb.append("\t@Override\r\n");
    sb.append("\t").append("public String toString(){").append("\r\n");
    sb.append("\t\t").append("StringBuilder sb = new StringBuilder();").append("\r\n");
    sb.append("\t\t").append("sb.append(\"{\");").append("\r\n");
    int index = 0;
    for (ExcelField field : fields) {
    	if(field.isSkip()){
    		continue;
    	}
    	sb.append("\t\t").append("sb.append(\"").append(field.getName()).append(" = \").append(").append(field.getGetName()).append("())");
    	if (index != fields.length - 1) {
    		sb.append(".append(\", \")");
    	}
    	sb.append(";\r\n");
    	index++;
    }

    sb.append("\t\t").append("sb.append(\"}\");").append("\r\n");
    sb.append("\r\n");
    sb.append("\t\t").append("return sb.toString();").append("\r\n");
    sb.append("\t}");

    sb.append("\r\n}");

    return sb.toString();
  }

  private boolean hasString(ExcelMeta meta) {
    for (ExcelField field : meta.getFields()) {
      if (field.isString()) {
        return true;
      }
    }

    return false;
  }

  private boolean hasNumber(ExcelMeta meta) {
    for (ExcelField field : meta.getFields()) {
      if (field.isNumber()) {
        return true;
      }
    }

    return false;
  }

  protected String getClassName(ExcelMeta meta)
  {
    return meta.getJavaMapperClass();
  }
}