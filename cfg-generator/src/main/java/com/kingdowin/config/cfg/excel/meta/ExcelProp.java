package com.kingdowin.config.cfg.excel.meta;

public enum ExcelProp
{
  excel(true, true), 
  excel_sheet(true, true), 
  java_package(false), 
  java_class(false), 
  java_proxy_class(false), 
  java_container_class(false), 
  skip_client_type(false);

  private boolean required;
  private boolean multiValue;

  private ExcelProp(boolean required) {
    this(required, false);
  }

  private ExcelProp(boolean required, boolean multiValue) {
    this.required = required;
    this.multiValue = multiValue;
  }

  public boolean isRequired() {
    return this.required;
  }

  public boolean isMultiValue() {
    return this.multiValue;
  }
}