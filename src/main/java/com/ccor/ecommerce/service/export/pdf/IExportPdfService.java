package com.ccor.ecommerce.service.export.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPTable;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface IExportPdfService {
     void writeTableHeader(PdfPTable table, String type);
    void writeTableData(PdfPTable table, Document document);
    void export(HttpServletResponse response, Long id) throws Exception;
}
