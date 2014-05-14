package org.hanyq.config.cfg.excel.meta;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelMeta {
	private boolean skip;
	private String metaSheet;
	private ExcelField[] fields;
	private Map<String, String> props = new HashMap<String, String>();

	private Map<String, List<String>> multiValueProps = new HashMap<String, List<String>>();

	public void postProcessAfterInit() {
		Map<String, Integer> fieldCountMap = new HashMap<String, Integer>();

		for (ExcelField field : this.fields) {
			String name = field.getName();
			if ((name != null) && (!name.isEmpty())) {
				Integer count = fieldCountMap.get(name);
				count = Integer.valueOf(count == null ? 1
						: count.intValue() + 1);

				fieldCountMap.put(name, count);
			}

		}

		Map<String, Integer> fieldIndexMap = new HashMap<String, Integer>();
		for (ExcelField field : this.fields) {
			String name = field.getName();
			if ((name != null) && (!name.isEmpty())) {
				Integer countI = fieldCountMap.get(name);

				if (countI != null) {
					int count = countI.intValue();
					if (count > 1) {
						Integer index = (Integer) fieldIndexMap.get(name);
						index = Integer.valueOf(index == null ? 1 : index
								.intValue() + 1);

						fieldIndexMap.put(name, index);

						field.setName(name + index);
					}
				}
			}
		}

		List<String> excels = getExcel();
		List<String> excelSheets = getExcelSheet();
		if (excels.size() != excelSheets.size()) {
			throw new ExcelFormatException(
					"excels.size() != excelSheets.size()");
		}
		Set<String> excel_excelSheetSet = new HashSet<String>();
		for (int i = 0; i < excels.size(); i++)
			if (!(excel_excelSheetSet).add((String) excels.get(i) + "_"
					+ (String) excelSheets.get(i)))
				throw new ExcelFormatException("引用了重复的配置 excel = "
						+ (String) excels.get(i) + ", excelSheet = "
						+ (String) excelSheets.get(i));
	}

	public String getFieldName(int rowNum) {
		return this.fields[rowNum].getName();
	}

	public ExcelField getField(int rowNum) {
		return this.fields[rowNum];
	}

	public int getFiledLength() {
		return this.fields.length;
	}

	public List<String> getExcel() {
		return this.multiValueProps.get(ExcelProp.excel.name());
	}

	public List<String> getExcelSheet() {
		return this.multiValueProps.get(ExcelProp.excel_sheet.name());
	}

	public String getJavaPackage() {
		String javaPackage = getProp(ExcelProp.java_package);
		return javaPackage;
	}

	public String getJavaMapperClass() {
		return this.metaSheet + "Mapper";
	}

	public String getJavaConfigClass() {
		return this.metaSheet + "Cfg";
	}

	public String getJavaContainerClass() {
		return this.metaSheet + "CfgContainer";
	}

	public boolean skipClientType() {
		return "Yes".equalsIgnoreCase(getProp(ExcelProp.skip_client_type));
	}

	public String getProp(ExcelProp prop) {
		if (prop.isMultiValue()) {
			throw new IllegalArgumentException("属性：" + prop.name()
					+ " 对应多个值，应该调用getProps方法");
		}
		return getProp(prop.name());
	}

	public Collection<String> getProps(ExcelProp prop) {
		if (!prop.isMultiValue()) {
			throw new IllegalArgumentException("属性：" + prop.name()
					+ " 只有一个值，应该调用getProp方法");
		}
		return this.multiValueProps.get(prop.name());
	}

	private String getProp(String propName) {
		return (String) this.props.get(propName);
	}

	public void putProp(String name, String value) {
		ExcelProp prop = ExcelProp.valueOf(name);
		if ((prop != null) && (prop.isMultiValue())) {
			throw new IllegalArgumentException("属性：" + prop.name()
					+ " 对应多个值，应该调用putMultiValueProps方法");
		}
		this.props.put(name, value);
	}

	public void putMultiValueProps(ExcelProp prop, List<String> values) {
		if (!prop.isMultiValue()) {
			throw new IllegalArgumentException("属性：" + prop.name()
					+ " 对应单个值，应该调用putProp方法");
		}
		this.multiValueProps.put(prop.name(), values);
	}

	public boolean containProp(String name) {
		return (this.props.containsKey(name))
				|| (this.multiValueProps.containsKey(name));
	}

	public boolean isSkip() {
		return this.skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String getMetaSheet() {
		return this.metaSheet;
	}

	public void setMetaSheet(String metaSheet) {
		this.metaSheet = metaSheet;
	}

	public ExcelField[] getFields() {
		return this.fields;
	}

	public void setFields(ExcelField[] fields) {
		this.fields = fields;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("------------------").append(this.metaSheet)
				.append("-----------------------\r\n");
		if (!isSkip()) {
			for (int i = 0; i < this.fields.length; i++) {
				sb.append(this.fields[i].getDesc()).append("\t");
			}
			sb.append("\r\n");
			for (int i = 0; i < this.fields.length; i++) {
				sb.append(this.fields[i].getType()).append("\t");
			}
			sb.append("\r\n");
			for (int i = 0; i < this.fields.length; i++) {
				sb.append(this.fields[i].getName()).append("\t");
			}
			sb.append("\r\n");

			for (Map.Entry<String, String> entry : this.props.entrySet())
				sb.append((String) entry.getKey()).append("\t")
						.append((String) entry.getValue()).append("\r\n");
		} else {
			sb.append("********************************************************\r\n");
			sb.append("sheetName = " + this.metaSheet + " is skipped!!!\r\n");
			sb.append("********************************************************\r\n");
		}
		return sb.toString();
	}
}