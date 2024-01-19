package com.ccor.ecommerce.service.export.excel;

import com.ccor.ecommerce.model.Address;
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
import java.util.List;
@Qualifier("address")
@Service
public class ExportAddressExcelServiceImpl implements IExportExcelService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Override
    public void writeHeader() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Address");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Street", style);
        createCell(row, 2, "Country", style);
        createCell(row, 3, "Postal code", style);
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
        List<Address> addresses = (List<Address>) list;
        for (Address address: addresses) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++,address.getId(), style);
            createCell(row, columnCount++,address.getStreet() , style);
            createCell(row, columnCount++,address.getCountry() , style);
            createCell(row, columnCount++,address.getPostalCode() , style);
        }
    }

    @Override
    public void generateExcelFile(HttpServletResponse response, List<?> addresses) throws IOException {
        writeHeader();
        write(addresses);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write( outputStream);
        workbook.close();
        outputStream.close();
        sheet.getWorkbook().close();
    }
}
