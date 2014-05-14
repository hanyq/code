package org.hanyq.config.cfg.excel.convertor.classgenerator;

import java.io.IOException;

import org.hanyq.config.cfg.excel.meta.ExcelMeta;

public abstract interface IJavaClassGenerator
{
  public abstract void makeJavaClass(String paramString, ExcelMeta paramExcelMeta)
    throws IOException;

  public abstract void setOverwriteIfExists(boolean paramBoolean);
}
