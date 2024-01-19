package com.ccor.ecommerce.service.export.pdf;

import com.ccor.ecommerce.model.History;
import com.ccor.ecommerce.model.Payment;
import com.ccor.ecommerce.model.ProductSold;
import com.ccor.ecommerce.model.Sale;
import com.ccor.ecommerce.repository.HistoryRepository;
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

@Qualifier("History")
@Service
public class ExportHistoryPdfServiceImpl implements IExportPdfService{

    @Autowired
    private HistoryRepository historyRepository;
    private History history;
    private Payment payment;
    @Override
    public void writeTableHeader(PdfPTable table, String type) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        switch (type){
            case "History":
                cell.setPhrase(new Phrase("ID", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Customer full name", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Date modification", font));
                table.addCell(cell);
                break;
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
    public void writeTableData(PdfPTable historyTable, Document document) {
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
        //writeTableHeader(historyTable,"History");
        historyTable.addCell(String.valueOf(history.getId()));
        historyTable.addCell(String.valueOf(history.getCustomerFullName()));
        historyTable.addCell(String.valueOf(history.getModificationDate().toString()));
        document.add(historyTable);
        //sales
        if(history.getSales()!=null && !history.getSales().isEmpty()){
            List<Sale> sales = history.getSales();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);
            for (Sale sale:sales) {
                Paragraph saleParagraph = new Paragraph("Sale "+sale.getId(), font);
                saleParagraph.setAlignment(Paragraph.ALIGN_LEFT);
                document.add(saleParagraph);
                PdfPTable saleTable = new PdfPTable(3);
                saleTable.setWidthPercentage(100f);
                saleTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
                saleTable.setSpacingBefore(10);
                writeTableHeader(saleTable,"Sale");
                saleTable.addCell(String.valueOf(sale.getId()));
                saleTable.addCell(String.valueOf(sale.getConcept()));
                saleTable.addCell(String.valueOf(sale.getCreateAt().toString()));
                document.add(saleTable);
                if(sale.getProductsSold()!=null && !sale.getProductsSold().isEmpty()){
                    //products
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
                    System.out.println("PDF export: the sale has not products");
                }
                if(sale.getPayment()!=null){
                    //payment
                    payment=sale.getPayment();
                    Paragraph paymentParagraph = new Paragraph("Payment", font);
                    paymentParagraph.setAlignment(Paragraph.ALIGN_LEFT);
                    document.add(paymentParagraph);
                    PdfPTable paymentTable = new PdfPTable(4);
                    paymentTable.setWidthPercentage(100f);
                    paymentTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,3.5f});
                    paymentTable.setSpacingBefore(10);
                    writeTableHeader(paymentTable,"Payment");
                    paymentTable.addCell(String.valueOf(payment.getId()));
                    paymentTable.addCell(String.valueOf(payment.getStatusPayment().toString()));
                    paymentTable.addCell(String.valueOf(payment.getCreateAt().toString()));
                    paymentTable.addCell(String.valueOf(payment.getCard().getId()));
                    document.add(paymentTable);
                }else{
                    System.out.println("PDF export: the sale has not payment");
                }
            }
        }
    }

    @Override
    public void export(HttpServletResponse response, Long id) throws Exception {
        history = historyRepository.findById(id).orElse(null);
        if(history==null) throw new Exception("Error in the export to pdf (history)");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph historyParagraph = new Paragraph("History", font);
        historyParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(historyParagraph);
        PdfPTable historyTable = new PdfPTable(3);
        historyTable.setWidthPercentage(100f);
        historyTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
        historyTable.setSpacingBefore(10);
        writeTableHeader(historyTable,"History");
        writeTableData(historyTable,document);
        document.close();
    }
}
