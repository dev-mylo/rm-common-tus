package com.rm.util.excel.annotation;


import com.rm.util.excel.style.ExcelCellStyle;

public @interface ExcelColumnStyle {

	/**
	 * Enum implements {@link ExcelCellStyle}
	 * Also, can use just class.
	 * If not use Enum, enumName will be ignored
	 */
	Class<? extends ExcelCellStyle> excelCellStyleClass();

	/**
	 * name of Enum implements {@link ExcelCellStyle}
	 * if not use Enum, enumName will be ignored
	 */
	String enumName() default "";

}
