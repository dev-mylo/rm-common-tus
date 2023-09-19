package com.rm.util.excel.style.header;


import com.rm.util.excel.style.CustomExcelCellStyle;
import com.rm.util.excel.style.align.DefaultExcelAlign;
import com.rm.util.excel.style.border.DefaultExcelBorders;
import com.rm.util.excel.style.border.ExcelBorderStyle;
import com.rm.util.excel.style.configurer.ExcelCellStyleConfigurer;

public class BlackHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {

        configurer.foregroundColor(0, 0, 0)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}