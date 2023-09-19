package com.rm.util.excel.style.header;


import com.rm.util.excel.style.CustomExcelCellStyle;
import com.rm.util.excel.style.align.DefaultExcelAlign;
import com.rm.util.excel.style.border.DefaultExcelBorders;
import com.rm.util.excel.style.border.ExcelBorderStyle;
import com.rm.util.excel.style.configurer.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}