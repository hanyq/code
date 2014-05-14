package org.hanyq.config.cfg.excel.convertor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hanyq.config.cfg.excel.convertor.classgenerator.IJavaClassGenerator;
import org.hanyq.config.cfg.excel.convertor.classgenerator.JavaConfigClassGenerator;
import org.hanyq.config.cfg.excel.convertor.classgenerator.JavaContainerClassGenerator;
import org.hanyq.config.cfg.excel.convertor.classgenerator.JavaMapperClassGenerator;
import org.hanyq.config.cfg.excel.meta.ExcelMeta;

public class ExcelToJavaConvertor {
	private String javaPath;
	private List<IJavaClassGenerator> classGenerators;

	public ExcelToJavaConvertor(String javaPath) {
		this.javaPath = javaPath;

		this.classGenerators = new ArrayList<IJavaClassGenerator>();
		this.classGenerators.add(new JavaMapperClassGenerator());
		this.classGenerators.add(new JavaConfigClassGenerator());
		this.classGenerators.add(new JavaContainerClassGenerator());
	}

	public void makeJavaClass(ExcelMeta meta) throws IOException {
		for (IJavaClassGenerator classGenerator : this.classGenerators) {
			classGenerator.makeJavaClass(this.javaPath, meta);
		}
	}
}