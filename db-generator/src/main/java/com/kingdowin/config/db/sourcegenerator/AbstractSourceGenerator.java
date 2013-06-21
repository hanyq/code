package com.kingdowin.config.db.sourcegenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;

public abstract class AbstractSourceGenerator<T> implements ISourceGenerator<T> {
	private String baseDirectory;
	
	
	public AbstractSourceGenerator(String baseDirectory){
		this.baseDirectory = baseDirectory;
	}
	
	public void generateSource(T mapperDefinition) {
		String fileName = baseDirectory + this.getSouceName(mapperDefinition);
		FileUtils.mkdir(baseDirectory);
		
		String content = this.doGeneratorSource(mapperDefinition);
		
		File file = new File(fileName);
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			bw.write(content);
			bw.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					
				}
			}
		}
	}

	protected abstract String getSouceName(T mapperDefinition);
	
	
	protected abstract String doGeneratorSource(T mapperDefinition);
}
