package com.ccor.ecommerce.service.export.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.util.List;

public interface IExportExcelService {

    public void writeHeader();
    public void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style);
    public void write(List<?> list);
    public void generateExcelFile(HttpServletResponse response,List<?> customers) throws IOException;
}
