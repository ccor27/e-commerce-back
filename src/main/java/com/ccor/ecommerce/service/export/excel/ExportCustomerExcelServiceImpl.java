package com.ccor.ecommerce.service.export.excel;

import com.ccor.ecommerce.model.Customer;
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
@Qualifier("Customer")
@Service
public class ExportCustomerExcelServiceImpl implements IExportExcelService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Override
    public void writeHeader() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Customers");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "Last Name", style);
        createCell(row, 3, "Cellphone", style);
        createCell(row, 4, "Email", style);
        createCell(row, 5, "History ID", style);
        createCell(row, 6, "Is Enable", style);
        createCell(row, 7, "Receive Notifications", style);
        createCell(row, 8, "Username", style);
        createCell(row, 9, "Is Deleted", style);
        //missing pwd, addresses and cards
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
        List<Customer> customers = (List<Customer>) list;
        for (Customer customer: customers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++,customer.getId(), style);
            createCell(row, columnCount++,customer.getName() , style);
            createCell(row, columnCount++,customer.getLastName() , style);
            createCell(row, columnCount++,customer.getCellphone() , style);
            createCell(row, columnCount++,customer.getEmail() , style);
            createCell(row, columnCount++,customer.getHistory().getId() , style);
            if(customer.isReceiveNotifications()){
                createCell(row, columnCount++,"true", style);
            }else{
                createCell(row, columnCount++,"false", style);
            }
            createCell(row, columnCount++,customer.getUsername() ,style);
            if(customer.isDeleted()){
                createCell(row, columnCount++,"true" ,style);
            }else{
                createCell(row, columnCount++,"false" ,style);
            }
        }
    }

    @Override
    public void generateExcelFile(HttpServletResponse response,List<?> customers) throws IOException {
        writeHeader();
        write(customers);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write( outputStream);
        workbook.close();
        outputStream.close();
        sheet.getWorkbook().close();
    }
}
