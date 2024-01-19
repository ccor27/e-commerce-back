package com.ccor.ecommerce.service.export.excel;

import com.ccor.ecommerce.model.ProductStock;
import com.ccor.ecommerce.repository.ProductStockRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Qualifier("ProductStock")
@Service
public class ExportProductStockExcelServiceImpl implements IExportExcelService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    @Autowired
    private ProductStockRepository productStockRepository;
    @Override
    public void writeHeader() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Products stock");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "Amount", style);
        createCell(row, 3, "Price Per Unit", style);
        createCell(row, 4, "Barcode", style);
        createCell(row, 5, "Enable product", style);
        //without picture
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
        List<ProductStock> products = (List<ProductStock>) list;
        for (ProductStock product: products) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++,product.getId(), style);
            createCell(row, columnCount++,product.getName() , style);
            createCell(row, columnCount++,product.getAmount() , style);
            createCell(row, columnCount++,product.getPricePerUnit() , style);
            createCell(row, columnCount++,product.getBarCode() , style);
            if(product.isEnableProduct()){
                createCell(row, columnCount++, "true", style);
            }else{
                createCell(row, columnCount++, "false", style);
            }
        }
    }

    @Override
    public void generateExcelFile(HttpServletResponse response, List<?> products) throws IOException {
        writeHeader();
        write(products);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write( outputStream);
        workbook.close();
        outputStream.close();
        sheet.getWorkbook().close();
    }
}
