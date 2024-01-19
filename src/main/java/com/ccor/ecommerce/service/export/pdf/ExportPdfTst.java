package com.ccor.ecommerce.service.export.pdf;

import org.springframework.stereotype.Service;

@Service
public class ExportPdfTst {
  /*  @Override
    public void writeTableHeader(PdfPTable table,String instance) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

      switch (instance){
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
              cell.setPhrase(new Phrase("Type", font));
              table.addCell(cell);
              break;


      }
    }

    @Override
    public void writeTableData(PdfPTable table,Object o) {
      if(o instanceof Customer){
          Customer c = (Customer) o;
          table.addCell(String.valueOf(c.getId()));
          table.addCell(String.valueOf(c.getName()));
          table.addCell(String.valueOf(c.getLastName()));
          table.addCell(String.valueOf(c.getCellphone()));
          table.addCell(String.valueOf(c.getEmail()));
          table.addCell(String.valueOf(Boolean.valueOf(c.isEnabled())));
          table.addCell(String.valueOf(Boolean.valueOf(c.isReceiveNotifications())));
      } else if (o instanceof History) {
          History h = (History) o;
          table.addCell(String.valueOf(h.getId()));
          table.addCell(String.valueOf(h.getCustomerFullName()));
          table.addCell(String.valueOf(h.getModificationDate().toString()));
      } else if (o instanceof Sale) {
          Sale s =  (Sale) o;
          table.addCell(String.valueOf(s.getId()));
          table.addCell(String.valueOf(s.getConcept()));
          table.addCell(String.valueOf(s.getCreateAt().toString()));
      } else if (o instanceof ProductSold) {
          ProductSold p =(ProductSold) o;
          table.addCell(String.valueOf(p.getId()));
          table.addCell(String.valueOf(p.getBarCode()));
          table.addCell(String.valueOf(p.getName()));
          table.addCell(String.valueOf(p.getAmount()));
          table.addCell(String.valueOf(Double.valueOf(p.getPrice())));
      } else if (o instanceof Payment) {
          Payment pay = (Payment) o;
          table.addCell(String.valueOf(pay.getId()));
          table.addCell(String.valueOf(pay.getStatusPayment().toString()));
          table.addCell(String.valueOf(pay.getCreateAt().toString()));
          table.addCell(String.valueOf(pay.getCard().getId()));
      } else if (o instanceof CreditCard) {
        CreditCard card = (CreditCard) o;
          table.addCell(String.valueOf(card.getId()));
          table.addCell(String.valueOf(card.getNumber()));
          table.addCell(String.valueOf(card.getTypeCard().toString()));
      } else if (o instanceof Address) {
        Address a = (Address) o;
          table.addCell(String.valueOf(a.getId()));
          table.addCell(String.valueOf(a.getStreet()));
          table.addCell(String.valueOf(a.getCountry()));
          table.addCell(String.valueOf(a.getPostalCode()));
      }
    }

    @Override
    public void export(HttpServletResponse response,Object o) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        if(o instanceof Customer){
            customerOperation((Customer) o, document, font);
        } else if (o instanceof History){
            historyOperation((History) o, document, font);
        } else if (o instanceof Sale) {
            saleOperation((Sale) o, document, font);
        } else{
            throw new Exception("Error with the object in the pdf export");
        }
        document.close();
    }

    private void saleOperation(Sale o, Document document, Font font) {
        Sale s = o;
        List<ProductSold> productSolds=new ArrayList<>();//hoping that all sales have a productSold
        Paragraph saleParagraph = new Paragraph("Sale", font);
        saleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(saleParagraph);
        PdfPTable saleTable = new PdfPTable(3);
        saleTable.setWidthPercentage(100f);
        saleTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
        saleTable.setSpacingBefore(10);
        writeTableHeader(saleTable,"Sale");
        writeTableData(saleTable,s);
        document.add(saleTable);
        productSolds=s.getProductsSold();
        for (ProductSold p:productSolds) {
            Paragraph productParagraph = new Paragraph("Product", font);
            productParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(productParagraph);
            PdfPTable productTable = new PdfPTable(5);
            productTable.setWidthPercentage(100f);
            productTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,1.5f,3.5f});
            productTable.setSpacingBefore(10);
            writeTableHeader(productTable,"Product");
            writeTableData(productTable,p);
            document.add(productTable);
        }
        productSolds.clear();
        if(s.getPayment()!=null){
            Payment p = s.getPayment();
            Paragraph paymentParagraph = new Paragraph("Payment", font);
            paymentParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(paymentParagraph);
            PdfPTable paymentTable = new PdfPTable(4);
            paymentTable.setWidthPercentage(100f);
            paymentTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,3.5f});
            paymentTable.setSpacingBefore(10);
            writeTableHeader(paymentTable,"Payment");
            writeTableData(paymentTable,p);
            document.add(paymentTable);
        }
    }

    private void historyOperation(History o, Document document, Font font) {
        History h = o;
        Paragraph historyParagraph = new Paragraph("History", font);
        historyParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(historyParagraph);
        PdfPTable historyTable = new PdfPTable(3);
        historyTable.setWidthPercentage(100f);
        historyTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
        historyTable.setSpacingBefore(10);
        writeTableHeader(historyTable,"History");
        writeTableData(historyTable,h);
        document.add(historyTable);
        if(h.getSales()!=null && !h.getSales().isEmpty()){
            List<Sale> sales = h.getSales();
            List<ProductSold> productSolds=new ArrayList<>();//hoping that all sales have a productSold
            for (Sale s:sales) {
                Paragraph saleParagraph = new Paragraph("Sale", font);
                saleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(saleParagraph);
                PdfPTable saleTable = new PdfPTable(3);
                saleTable.setWidthPercentage(100f);
                saleTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
                saleTable.setSpacingBefore(10);
                writeTableHeader(saleTable,"Sale");
                writeTableData(saleTable,s);
                document.add(saleTable);
                productSolds=s.getProductsSold();
                for (ProductSold p:productSolds) {
                    Paragraph productParagraph = new Paragraph("Sale", font);
                    productParagraph.setAlignment(Paragraph.ALIGN_LEFT);
                    document.add(productParagraph);
                    PdfPTable productTable = new PdfPTable(5);
                    productTable.setWidthPercentage(100f);
                    productTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,1.5f,3.5f});
                    productTable.setSpacingBefore(10);
                    writeTableHeader(productTable,"Product");
                    writeTableData(productTable,p);
                    document.add(productTable);
                }
                productSolds.clear();
            }
        }
    }

    private void customerOperation(Customer o, Document document, Font font) {
        Customer c  = o;
        Paragraph customerParagraph = new Paragraph("Customer: "+c.getName(), font);
        customerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(customerParagraph);
        PdfPTable customerTable = new PdfPTable(7);
        customerTable.setWidthPercentage(100f);
        customerTable.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f,3.5f,3.5f});
        customerTable.setSpacingBefore(10);
        writeTableHeader(customerTable,"Customer");
        writeTableData(customerTable,c);
        document.add(customerTable);

        if(c.getAddress()!=null && !c.getAddress().isEmpty()){

            List<Address> addresses = o.getAddress();
            Paragraph AddressParagraph = new Paragraph("Addresses", font);
            AddressParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(AddressParagraph);
            PdfPTable AddressTable = new PdfPTable(4);
            AddressTable.setWidthPercentage(100f);
            AddressTable.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f});
            AddressTable.setSpacingBefore(10);
            writeTableHeader(AddressTable,"Address");
            for (Address a:addresses) {
                writeTableData(AddressTable,a);
            }
            document.add(AddressTable);
        }
        if(c.getCards()!=null && !c.getCards().isEmpty()){
            List<CreditCard> cards = c.getCards();
            Paragraph cardsParagraph = new Paragraph("Cards", font);
            cardsParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(cardsParagraph);
            PdfPTable CardTable = new PdfPTable(3);
            CardTable.setWidthPercentage(100f);
            CardTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
            CardTable.setSpacingBefore(10);
            writeTableHeader(CardTable,"Card");
            for (CreditCard card:cards) {
                writeTableData(CardTable,card);
            }
            document.add(CardTable);
        }
        if(c.getHistory()!=null){
            History h =  c.getHistory();
            Paragraph historyParagraph = new Paragraph("History", font);
            historyParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(historyParagraph);
            PdfPTable historyTable = new PdfPTable(3);
            historyTable.setWidthPercentage(100f);
            historyTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
            historyTable.setSpacingBefore(10);
            writeTableHeader(historyTable,"History");
            writeTableData(historyTable,h);
            document.add(historyTable);
            if(h.getSales()!=null && !h.getSales().isEmpty()){
                List<Sale> sales = h.getSales();
                List<ProductSold> productSolds=new ArrayList<>();//hoping that all sales have a productSold
                for (Sale s:sales) {
                    Paragraph saleParagraph = new Paragraph("Sale", font);
                    saleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
                    document.add(saleParagraph);
                    PdfPTable saleTable = new PdfPTable(3);
                    saleTable.setWidthPercentage(100f);
                    saleTable.setWidths(new float[] {1.5f, 3.5f, 3.0f});
                    saleTable.setSpacingBefore(10);
                    writeTableHeader(saleTable,"Sale");
                    writeTableData(saleTable,s);
                    document.add(saleTable);
                    productSolds=s.getProductsSold();
                    for (ProductSold p:productSolds) {
                        Paragraph productParagraph = new Paragraph("Product", font);
                        productParagraph.setAlignment(Paragraph.ALIGN_LEFT);
                        document.add(productParagraph);
                        PdfPTable productTable = new PdfPTable(5);
                        productTable.setWidthPercentage(100f);
                        productTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,1.5f,3.5f});
                        productTable.setSpacingBefore(10);
                        writeTableHeader(productTable,"Product");
                        writeTableData(productTable,p);
                        document.add(productTable);
                    }
                    productSolds.clear();
                    if(s.getPayment()!=null){
                        Payment p = s.getPayment();
                        Paragraph paymentParagraph = new Paragraph("Payment", font);
                        paymentParagraph.setAlignment(Paragraph.ALIGN_LEFT);
                        document.add(paymentParagraph);
                        PdfPTable paymentTable = new PdfPTable(4);
                        paymentTable.setWidthPercentage(100f);
                        paymentTable.setWidths(new float[] {1.5f, 3.5f, 3.0f,3.5f});
                        paymentTable.setSpacingBefore(10);
                        writeTableHeader(paymentTable,"Payment");
                        writeTableData(paymentTable,p);
                        document.add(paymentTable);
                    }
                }
            }
        }
    }*/
}
