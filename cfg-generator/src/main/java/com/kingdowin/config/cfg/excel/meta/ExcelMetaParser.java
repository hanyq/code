package com.kingdowin.config.cfg.excel.meta;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.kingdowin.config.cfg.excel.ExcelReaderUtils;

public class ExcelMetaParser
{
  private static final ExcelMetaParser INSTANCE = new ExcelMetaParser();

  public static ExcelMetaParser getInstance()
  {
    return INSTANCE;
  }

  public List<ExcelMeta> parser(File file) throws IOException {
    if (file.isDirectory()) {
      File[] subFiles = file.listFiles(new FileFilter()
      {
        public boolean accept(File file)
        {
          return (file.getName().endsWith(".xlsx")) || (file.getName().endsWith(".xls"));
        }
      });
      List<ExcelMeta> metaList = new ArrayList<ExcelMeta>();
      for (File subFile : subFiles) {
        metaList.addAll(parser(subFile));
      }

      return metaList;
    }
    return parser(new FileInputStream(file));
  }

  public List<ExcelMeta> parser(FileInputStream fis) throws IOException
  {
    Workbook book = ExcelReaderUtils.read(fis);

    return parseWorkbook(book);
  }

  private List<ExcelMeta> parseWorkbook(Workbook book) {
    List<ExcelMeta>  metaList = new ArrayList<ExcelMeta> ();

    int sheetNum = book.getNumberOfSheets();
    for (int i = 0; i < sheetNum; i++) {
      Sheet sheet = book.getSheetAt(i);
      if (sheet.getLastRowNum() >= 1)
      {
        Row firstRow = sheet.getRow(0);
        if (firstRow != null) {
          Cell cell = firstRow.getCell(0);
          String firstWord = cell.getStringCellValue();
          if ("skip".equalsIgnoreCase(firstWord)) {
            ExcelMeta meta = new ExcelMeta();
            meta.setSkip(true);
            meta.setMetaSheet(sheet.getSheetName());

            metaList.add(meta);
          }
          else{
        	  ExcelMeta meta = parseSheet(sheet);

              metaList.add(meta);
          }
        }

       
      }
    }
    return metaList;
  }

  private ExcelMeta parseSheet(Sheet sheet)
  {
    ExcelMeta meta = new ExcelMeta();
    meta.setMetaSheet(sheet.getSheetName());
    int TITLE_COUNT = 4;
    int lastRowNum = sheet.getLastRowNum();
    if (lastRowNum <= TITLE_COUNT) {
      throw new ExcelFormatException("sheet = " + sheet.getSheetName() + ",一个sheet至少要有（desc, type, name）三行！！！");
    }

    Row descRow = sheet.getRow(0);
    Row typeRow = sheet.getRow(1);
    Row nameRow = sheet.getRow(2);
    Row visibleRow = sheet.getRow(3);

    equals(new int[] { descRow.getLastCellNum(), typeRow.getLastCellNum(), nameRow.getLastCellNum() });

    int lastCellNum = descRow.getLastCellNum();
    List<ExcelField> fields = new ArrayList<ExcelField>();
    for (int i = 0; i <= lastCellNum; i++) {
      Cell typeCell = typeRow.getCell(i);
      if (typeCell == null) {
        break;
      }
      String type = ExcelReaderUtils.getValue(typeRow.getCell(i));
      String desc = ExcelReaderUtils.getValue(descRow.getCell(i));
      if ((StringUtils.isEmpty(type)) && (StringUtils.isEmpty(desc))) {
        break;
      }
      String name = "";
      if (nameRow.getCell(i) != null) {
        name = ExcelReaderUtils.getValue(nameRow.getCell(i));
      }
      boolean visible = true;
      if (visibleRow != null) {
        Cell visibleCell = visibleRow.getCell(i);

        if (visibleCell != null) {
          visible = !"unvisible".equalsIgnoreCase(visibleCell.getStringCellValue());
        }
      }

      ExcelField field = new ExcelField(desc, type, name, visible);
      fields.add(field);
      /*if (!field.isSkip()) {
        fields.add(field);
      }*/
    }

    meta.setFields((ExcelField[])fields.toArray(new ExcelField[0]));

    for (int j = TITLE_COUNT; j <= lastRowNum; j++) {
      Row row = sheet.getRow(j);
      if ((row != null) && (row.getLastCellNum() > 0)) {
        int cellCount = row.getLastCellNum();
        String propName = null;
        List<String> propValues = new ArrayList<String>();
        for (int k = 0; k <= cellCount; k++) {
          String content = ExcelReaderUtils.getValue(row.getCell(k));

          if (StringUtils.isEmpty(propName)) {
            if (!StringUtils.isEmpty(content)) {
              propName = content;
            }

          }
          else
          {
            propValues.add(content);
          }
        }
        if (!StringUtils.isEmpty(propName))
        {
          propValues = trimList(propValues);

          if (meta.containProp(propName)) {
            throw new ExcelFormatException("sheet = " + sheet.getSheetName() + ",Excel属性重复 ， 属性 = " + propName);
          }
          if (propValues.isEmpty()) {
            System.err.println("sheet = " + sheet.getSheetName() + ",Excel属性 " + propName + " 没有配置值！！！  行数 = " + j);
          }
          else
          {
            ExcelProp prop = ExcelProp.valueOf(propName);
            if ((prop != null) && (prop.isMultiValue())) {
              meta.putMultiValueProps(prop, propValues);
            } else {
              if (propValues.size() != 1) {
                throw new ExcelFormatException("sheet = " + sheet.getSheetName() + ", 属性：" + prop.name() + " 对应了多个值");
              }
              meta.putProp(propName, (String)propValues.get(0));
            }
          }
        }
      }
    }
    meta.postProcessAfterInit();

    return meta;
  }

  private List<String> trimList(List<String> lst) {
    List<String> tmpList = new ArrayList<String>();
    for (int i = lst.size() - 1; i >= 0; i--) {
      String str = (String)lst.get(i);
      if (!StringUtils.isEmpty(str)) {
        tmpList.add(0, str);
      }
    }

    return tmpList;
  }

  private boolean equals(int[] nums)
  {
    if (nums.length <= 1) {
      return true;
    }
    for (int num : nums) {
      if (num != nums[0]) {
        return false;
      }
    }

    return true;
  }

  public static void main(String[] args) throws IOException {
    File file = new File("C:\\Users\\Administrator\\Desktop\\beans.xlsx");
    List<ExcelMeta> ls = getInstance().parser(file);

    System.out.println(ls);
  }
}