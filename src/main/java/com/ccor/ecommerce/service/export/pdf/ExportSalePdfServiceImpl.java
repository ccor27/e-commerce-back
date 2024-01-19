package com.ccor.ecommerce.service.export.pdf;

import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.repository.SaleRepository;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Qualifier("Sale")
@Service
public class ExportSalePdfServiceImpl implements IExportPdfService{

    @Autowired
    private SaleRepository saleRepository;
    private Sale sale;

    @Override
    public void writeTableHeader(PdfPTable table, String type) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        switch (type){
        case "Sale":
            cell.setPhrase(new Phrase("ID", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("concept", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Date of creation", font));
            table.addCell(cell);
            break;
        case "Product":
            cell.setPhrase(new Phrase("ID", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Bar code", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Name", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Amount", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Price", font));
            table.addCell(cell);
            break;
        case "Payment":
            cell.setPhrase(new Phrase("ID", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Status", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Date of creation", font));
            table.addCell(cell);
            cell.setPhrase(new Phrase("Card id", font));
            table.addCell(cell);
            break;
    }
    }

    @Override
    public void writeTableData(PdfPTable saleTable, Document document) {

        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        infoFont.setSize(12);
        infoFont.setColor(Color.BLACK);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(calendar.getTime());
        String info = "Date: "+formattedDate;
        Paragraph paragraphInfo = new Paragraph(info,infoFont);
        paragraphInfo.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraphInfo);
        saleTable.addCell(String.valueOf(sale.getId()));
        saleTable.addCell(String.valueOf(sale.getConcept()));
        saleTable.addCell(String.valueOf(sale.getCreateAt().toString()));
        document.add(saleTable);
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        if(sale.getProductsSold()!=null && !sale.getProductsSold().isEmpty()){
            List<ProductSold> productSolds = sale.getProductsSold();
            Paragraph productParagraph = new Paragraph("Product", font);
            productParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(productParagraph);
            PdfPTable productTable = new PdfPTable(5);
            productTable.setWidthPercentage(100f);
            productTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,1.5f,3.5f});
            productTable.setSpacingBefore(10);
            writeTableHeader(productTable,"Product");
            for (ProductSold p: productSolds) {
                productTable.addCell(String.valueOf(p.getId()));
                productTable.addCell(String.valueOf(p.getBarCode()));
                productTable.addCell(String.valueOf(p.getName()));
                productTable.addCell(String.valueOf(p.getAmount()));
                productTable.addCell(String.valueOf(Double.valueOf(p.getPrice())));
            }
            document.add(productTable);
        }else{
            System.out.println("The sale has not products (export pdf)");
        }
        if(sale.getPayment()!=null){
            Payment p = sale.getPayment();
            Paragraph paymentParagraph = new Paragraph("Payment", font);
            paymentParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(paymentParagraph);
            PdfPTable paymentTable = new PdfPTable(4);
            paymentTable.setWidthPercentage(100f);
            paymentTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,3.5f});
            paymentTable.setSpacingBefore(10);
            writeTableHeader(paymentTable,"Payment");
            paymentTable.addCell(String.valueOf(p.getId()));
            paymentTable.addCell(String.valueOf(p.getStatusPayment().toString()));
            paymentTable.addCell(String.valueOf(p.getCreateAt().toString()));
            paymentTable.addCell(String.valueOf(p.getCard().getId()));
            document.add(paymentTable);
        }else{
            System.out.println("The sale has not payment (export pdf)");
        }
    }

    @Override
    public void export(HttpServletResponse response, Long id) throws Exception {
        sale = saleRepository.findById(id).orElse(null);
        if(sale==null) throw new Exception("Error in the export to pdf (sale)");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph saleParagraph = new Paragraph("Sale "+sale.getId(), font);
        saleParagraph.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(saleParagraph);
        PdfPTable saleTable = new PdfPTable(3);
        saleTable.setWidthPercentage(100f);
        saleTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
        saleTable.setSpacingBefore(10);
        writeTableHeader(saleTable,"Sale");
        writeTableData(saleTable,document);
        document.close();
    }
}
