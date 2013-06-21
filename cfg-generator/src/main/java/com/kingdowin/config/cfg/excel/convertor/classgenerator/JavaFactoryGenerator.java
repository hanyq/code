package com.kingdowin.config.cfg.excel.convertor.classgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.kingdowin.config.cfg.Config;
import com.kingdowin.config.cfg.excel.meta.ExcelMeta;

public class JavaFactoryGenerator {
	public static void makeJavaClass(String configPath, List<ExcelMeta> metas)
			throws IOException {
		Set<String> oldImportSet = readOldJavaImport(configPath);
		Set<String> oldClassSet = getOldClass(oldImportSet);
		StringBuilder sb = new StringBuilder();

		String javaPackage = getJavaPackage();

		sb.append("package ").append(javaPackage).append(";\r\n\r\n");

		
		sb.append("\r\n");
		sb.append("import com.kingdowin.framework.cfg.CfgManager;").append("\r\n");
		Set<String> newImportSet = new HashSet<String>();
		String newImport;
		for (ExcelMeta meta : metas) {
			if (!meta.isSkip()) {
				newImport = meta.getJavaPackage() + "."
						+ meta.getJavaContainerClass();
				sb.append("import ").append(newImport).append(";\r\n");
				newImportSet.add(newImport);
			}
		}
		for (String oldImport : oldImportSet) {
			if (!newImportSet.contains(oldImport)) {
				sb.append("import ").append(oldImport).append(";\r\n");
			}
		}
		sb.append("\r\n");

		String className = getClassName();
		sb.append("public class ").append(className).append("{\r\n");
		sb.append("\r\n");

		for (ExcelMeta meta : metas) {
			if ((!meta.isSkip())
					|| (oldClassSet.contains(meta.getJavaContainerClass()))) {
				sb.append("\t").append("public static ")
						.append(meta.getJavaContainerClass()).append(" get")
						.append(meta.getJavaContainerClass()).append("(){\r\n");
				sb.append("\t\t").append("return CfgManager.get(")
						.append(meta.getJavaContainerClass())
						.append(".class);\r\n");
				sb.append("\t}").append("\r\n");
				sb.append("\r\n");
			}
		}

		sb.append("\r\n");

		sb.append("\t").append("public static void loadAndCheck(){")
				.append("\r\n");
		int i = 0;
		for (ExcelMeta meta : metas) {
			if ((!meta.isSkip())
					|| (oldClassSet.contains(meta.getJavaContainerClass()))) {
				sb.append("\t\t").append(meta.getJavaContainerClass())
						.append(" container").append(i).append(" = get")
						.append(meta.getJavaContainerClass()).append("();\r\n");
				sb.append("\t\t").append("if(container").append(i)
						.append(" == null){").append("\r\n");
				sb.append("\t\t\t")
						.append("throw new RuntimeException(\"配置错误!!! Config = ")
						.append(meta.getJavaConfigClass()).append("\");")
						.append("\r\n");
				sb.append("\t\t}");
				sb.append("\r\n");
				sb.append("\r\n");
				i++;
			}
		}
		sb.append("\t}");
		sb.append("\r\n}");

		String javaContent = sb.toString();
		String javaClassPath = getJavaClassPath(configPath);

		File file = new File(javaClassPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		File classFile = new File(javaClassPath, className + ".java");

		FileWriter writer = new FileWriter(classFile);

		writer.append(javaContent);
		writer.flush();
		writer.close();
	}

	private static String getClassName() {
		return "CfgContainers";
	}

	private static String getJavaPackage() {
		return Config.getBasePackage();
	}

	private static String getJavaClassPath(String configPath) {
		String javaPackage = getJavaPackage();
		String[] folders = new String[0];
		if ((javaPackage != null) && (!javaPackage.isEmpty())) {
			folders = javaPackage.split("\\.");
		}
		String javaClassPath = configPath;
		if (!javaClassPath.endsWith("\\")) {
			javaClassPath = javaClassPath + "\\";
		}
		for (String folder : folders) {
			javaClassPath = javaClassPath + folder + "\\";
		}

		return javaClassPath;
	}

	private static Set<String> readOldJavaImport(String configPath)
			throws IOException {
		Set<String> oldJavaImport = new HashSet<String>();
		String javaClassPath = getJavaClassPath(configPath);
		String className = getClassName();
		File classFile = new File(javaClassPath, className + ".java");
		if (classFile.exists()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(classFile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("import ")) {
					String packageAndClass = line.substring("import ".length());
					packageAndClass = packageAndClass.trim();
					packageAndClass = packageAndClass.substring(0,
							packageAndClass.length() - 1);

					if (!packageAndClass.endsWith("CfgManager")) {
						oldJavaImport.add(packageAndClass);
					}
				}
			}

			br.close();
		}

		return oldJavaImport;
	}

	private static Set<String> getOldClass(Set<String> oldImportSet) {
		Set<String> oldClassSet = new HashSet<String>();
		for (String oldImport : oldImportSet) {
			String oldClass = oldImport;
			if (oldClass.contains(".")) {
				oldClass = oldClass.substring(oldClass.lastIndexOf(".") + 1,
						oldClass.length());
			}
			
			oldClassSet.add(oldClass);
		}

		return oldClassSet;
	}
}