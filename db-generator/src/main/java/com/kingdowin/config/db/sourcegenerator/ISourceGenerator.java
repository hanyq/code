package com.kingdowin.config.db.sourcegenerator;

public interface ISourceGenerator<T> {
	
	void generateSource(T t);
	
}
