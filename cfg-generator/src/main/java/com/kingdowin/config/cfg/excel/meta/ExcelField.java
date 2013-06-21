package com.kingdowin.config.cfg.excel.meta;

public class ExcelField
{
  private String desc;
  private String type;
  private String name;
  private boolean visible;

  public ExcelField()
  {
  }

  public ExcelField(String desc, String type, String name, boolean visible)
  {
    if (desc != null) {
      desc = desc.replaceAll("\r\n", " ");
      desc = desc.replaceAll("\r", " ");
      desc = desc.replaceAll("\n", " ");
    }
    this.desc = desc;
    this.type = type;
    this.name = name;
    this.visible = visible;
  }

  public boolean isSkip() {
    return "skip".equalsIgnoreCase(this.type);
  }

  public boolean isString() {
    return "string".equalsIgnoreCase(this.type);
  }

  public boolean isNumber() {
    return (!isString()) && (!isSkip());
  }

  public String getGetName() {
    String upperName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1, this.name.length());

    return "get" + upperName;
  }

  public String getDesc()
  {
    return this.desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isVisible() {
    return this.visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }
}