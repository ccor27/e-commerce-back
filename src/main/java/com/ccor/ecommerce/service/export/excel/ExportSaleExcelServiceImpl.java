package com.ccor.ecommerce.service.export.excel;

import com.ccor.ecommerce.model.Sale;
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

@Qualifier("Sale")
@Service
public class ExportSaleExcelServiceImpl implements IExportExcelService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Override
    public void writeHeader() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Sales");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Concept", style);
        createCell(row, 2, "Is deleted", style);
        createCell(row, 3, "Has products", style);
        createCell(row, 4, "Date of creation", style);
        createCell(row, 5, "Payment id", style);
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
        } if (valueOfCell instanceof Date) {
            cell.setCellValue((Date) valueOfCell);
        }else {
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
        List<Sale> sales = (List<Sale>) list;
        for (Sale sale: sales) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++,sale.getId(), style);
            createCell(row, columnCount++,sale.getConcept() , style);
            if(sale.isDeleted()){
                createCell(row, columnCount++,"true" , style);
            }else{
                createCell(row, columnCount++,"false", style);
            }
            if(sale.getProductsSold().isEmpty()){
                createCell(row, columnCount++,"Not present" , style);
            }else{
                createCell(row, columnCount++,"Present" , style);
            }
            createCell(row, columnCount++,sale.getCreateAt() .toString(), style);
            if(sale.getPayment()!=null){
                createCell(row, columnCount++,sale.getPayment().getId() , style);
            }else{
                createCell(row, columnCount++,"Not present" , style);
            }
        }
    }

    @Override
    public void generateExcelFile(HttpServletResponse response, List<?> sales) throws IOException {
        writeHeader();
        write(sales);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write( outputStream);
        workbook.close();
        outputStream.close();
        sheet.getWorkbook().close();
    }
}
