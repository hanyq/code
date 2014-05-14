package org.hanyq.config.cfg.excel.meta;

public class ExcelFormatException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public ExcelFormatException()
  {
  }

  public ExcelFormatException(String msg)
  {
    super(msg);
  }

  public ExcelFormatException(Exception e) {
    super(e);
  }
}