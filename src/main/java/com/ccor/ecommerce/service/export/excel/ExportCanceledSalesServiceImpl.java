package com.ccor.ecommerce.service.export.excel;

import com.ccor.ecommerce.model.CanceledSale;
import com.ccor.ecommerce.model.ProductStock;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
@Service
@Qualifier("canceledSale")
public class ExportCanceledSalesServiceImpl implements IExportExcelService{
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Override
    public void writeHeader() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Cancelled sales");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Sale id", style);
        createCell(row, 2, "Payment id", style);
        createCell(row, 3, "Date", style);
    }

    @Override
    public void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else if (valueOfCell instanceof Date) {
        cell.setCellValue((Date) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    @Override
    public void write(List<?> list) {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        List<CanceledSale> canceledSales = (List<CanceledSale>) list;
        for (CanceledSale canceledSale: canceledSales) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++,canceledSale.getCanceledSaleId(), style);
            createCell(row, columnCount++,canceledSale.getSaleId() , style);
            createCell(row, columnCount++,canceledSale.getPaymentId() , style);
            createCell(row, columnCount++,canceledSale.getCreateAt().toString() , style);
        }
    }

    @Override
    public void generateExcelFile(HttpServletResponse response, List<?> canceledSales) throws IOException {
        writeHeader();
        write(canceledSales);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write( outputStream);
        workbook.close();
        outputStream.close();
        sheet.getWorkbook().close();
    }
}
