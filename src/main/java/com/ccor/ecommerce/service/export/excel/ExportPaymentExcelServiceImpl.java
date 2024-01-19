package com.ccor.ecommerce.service.export.excel;

import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.StatusPayment;
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

@Qualifier("Payment")
@Service
public class ExportPaymentExcelServiceImpl implements IExportExcelService {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Override
    public void writeHeader() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Payments");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Status", style);
        createCell(row, 2, "Date of creation", style);
        createCell(row, 3, "Is deleted", style);
        createCell(row, 4, "Id card", style);

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
        } else if (valueOfCell instanceof Date) {
            cell.setCellValue((Date) valueOfCell);
        } else if (valueOfCell instanceof StatusPayment) {
            cell.setCellValue(((StatusPayment) valueOfCell).ordinal());
        } else{
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
        List<Payment> payments = (List<Payment>) list;
        for (Payment payment: payments) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++,payment.getId(), style);
            createCell(row, columnCount++,payment.getStatusPayment() , style);
            createCell(row, columnCount++,payment.getCreateAt().toString() , style);
            if(payment.isDeleted()){
                createCell(row, columnCount++,"true", style);
            }else{
                createCell(row, columnCount++,"false", style);
            }
            if(payment.getCard()!=null){
                createCell(row, columnCount++,payment.getCard().getId() , style);
            }else{
                createCell(row, columnCount++,"Not present" , style);
            }
        }
    }

    @Override
    public void generateExcelFile(HttpServletResponse response, List<?> payments) throws IOException {
        writeHeader();
        write(payments);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write( outputStream);
        workbook.close();
        outputStream.close();
        sheet.getWorkbook().close();
    }
}
