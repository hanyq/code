package org.hanyq.config.cfg.excel.convertor.classgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.hanyq.config.cfg.excel.meta.ExcelMeta;

public abstract class AbstractJavaClassGenerator
  implements IJavaClassGenerator
{
  private boolean overwriteIfExists;

  public boolean isOverwriteIfExists()
  {
    return this.overwriteIfExists;
  }

  public void setOverwriteIfExists(boolean overwriteIfExists) {
    this.overwriteIfExists = overwriteIfExists;
  }

  public void makeJavaClass(String configPath, ExcelMeta meta) throws IOException {
    String javaPackage = meta.getJavaPackage();
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

    File file = new File(javaClassPath);
    if (!file.exists()) {
      file.mkdirs();
    }

    File classFile = new File(javaClassPath, getClassName(meta) + ".java");

    if ((classFile.exists()) && (!isOverwriteIfExists())) {
      return;
    }

    FileWriter writer = new FileWriter(classFile);

    String classContent = makeJavaContent(meta);
    writer.append(classContent);
    writer.flush();
    writer.close();
  }

  protected abstract String makeJavaContent(ExcelMeta paramExcelMeta);

  protected abstract String getClassName(ExcelMeta paramExcelMeta);
}