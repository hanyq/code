package org.hanyq.generator.db.sourcegenerator;

public interface ISourceGenerator<T> {
	
	void generateSource(T t);
	
}
