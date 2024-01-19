package com.ccor.ecommerce.service.export.pdf;

import com.ccor.ecommerce.model.*;
import com.ccor.ecommerce.repository.CustomerRepository;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
@Qualifier("Customer")
@Service
public class ExportCustomerPdfServiceImpl implements IExportPdfService {

    @Autowired
    private CustomerRepository customerRepository;
    private Customer customer;
    private History history;
    private List<Address> addresses;
    private List<CreditCard> cards;
    private List<Sale> sales;
    private Payment payment;

    @Override
    public void writeTableHeader(PdfPTable table,String type) {

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        switch (type){
            case "Customer":
                cell.setPhrase(new Phrase("ID", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Name", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Last name", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Cellphone", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Email", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Is enable", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Receive Notifications", font));
                table.addCell(cell);
                break;
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
            case "Address":
                cell.setPhrase(new Phrase("ID", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Street", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Country", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Postal code", font));
                table.addCell(cell);
                break;
            case "Card":
                cell.setPhrase(new Phrase("ID", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Number", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Holder name", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Cvv", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Type", font));
                table.addCell(cell);
                cell.setPhrase(new Phrase("Expiration", font));
                table.addCell(cell);
                break;


        }
    }

    @Override
    public void writeTableData(PdfPTable customerTable,Document document) {

        //customer info
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        infoFont.setSize(12);
        infoFont.setColor(Color.BLACK);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(calendar.getTime());
        String info = " \n " +"Date: "+formattedDate +
                "\n This is the information of "+customer.getName()+" that has the role or roles: \n" +
                " "+customer.getRoles().toString()+". \n"
                +"also the customer has the following vias to be notified: "+customer.getChannelNotifications().toString()+"\n";
        Paragraph paragraphInfo = new Paragraph(info,infoFont);
        paragraphInfo.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraphInfo);
        writeTableHeader(customerTable,"Customer");
        customerTable.addCell(String.valueOf(customer.getId()));
        customerTable.addCell(String.valueOf(customer.getName()));
        customerTable.addCell(String.valueOf(customer.getLastName()));
        customerTable.addCell(String.valueOf(customer.getCellphone()));
        customerTable.addCell(String.valueOf(customer.getEmail()));
        if(customer.isEnable()){
            customerTable.addCell("true");
        }else{
            customerTable.addCell("false");
        }
        if(customer.isReceiveNotifications()){
            customerTable.addCell("true");
        }else{
            customerTable.addCell("false");
        }
        document.add(customerTable);
        //insertar info sobre las notificaciones
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        //Addresses
        if(addresses!=null && !addresses.isEmpty()){
            Paragraph addressParagraph = new Paragraph("Addresses", font);
            addressParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(addressParagraph);
            PdfPTable addressTable = new PdfPTable(4);
            addressTable.setWidthPercentage(100f);
            addressTable.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f});
            addressTable.setSpacingBefore(10);
            writeTableHeader(addressTable,"Address");
            for (Address a:addresses ) {
                addressTable.addCell(String.valueOf(a.getId()));
                addressTable.addCell(String.valueOf(a.getStreet()));
                addressTable.addCell(String.valueOf(a.getCountry()));
                addressTable.addCell(String.valueOf(a.getPostalCode()));
            }
            document.add(addressTable);
        }
        //Cards
        if(cards!=null && !cards.isEmpty()){
            Paragraph cardsParagraph = new Paragraph("Cards", font);
            cardsParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(cardsParagraph);
            PdfPTable cardTable = new PdfPTable(3);
            cardTable.setWidthPercentage(100f);
            cardTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
            cardTable.setSpacingBefore(10);
            writeTableHeader(cardTable,"Card");
            for (CreditCard card:cards ) {
                cardTable.addCell(String.valueOf(card.getId()));
                cardTable.addCell(String.valueOf(card.getNumber()));
                cardTable.addCell(String.valueOf(card.getHolderName()));
                cardTable.addCell(String.valueOf(card.getCvv()));
                cardTable.addCell(String.valueOf(card.getTypeCard().toString()));
                cardTable.addCell(card.getMonthExp()+"-"+card.getYearExp());
            }
            document.add(cardTable);
        }
        //History
        if(history!=null){
            Paragraph historyParagraph = new Paragraph("History", font);
            historyParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(historyParagraph);
            PdfPTable historyTable = new PdfPTable(3);
            historyTable.setWidthPercentage(100f);
            historyTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
            historyTable.setSpacingBefore(10);
            writeTableHeader(historyTable,"History");
            historyTable.addCell(String.valueOf(history.getId()));
            historyTable.addCell(String.valueOf(history.getCustomerFullName()));
            historyTable.addCell(String.valueOf(history.getModificationDate().toString()));
            document.add(historyTable);
            //sales
            if(history.getSales()!=null && !history.getSales().isEmpty()){
                sales=history.getSales();
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
    }

    @Override
    public void export(HttpServletResponse response, Long id) throws Exception {
       customer = customerRepository.findById(id).orElse(null);
       if(customer==null) throw new Exception("Error in the export to pdf");

       history=customer.getHistory();
       addresses=customer.getAddress();
       cards=customer.getCards();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph customerParagraph = new Paragraph("Customer: ", font);
        customerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(customerParagraph);
        PdfPTable customerTable = new PdfPTable(7);
        customerTable.setWidthPercentage(100f);
        customerTable.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f,3.5f,3.5f});
        customerTable.setSpacingBefore(10);
         writeTableData(customerTable,document);
         document.close();
    }
}
