package com.karan.experimentwithkafka.service;

import com.karan.experimentwithkafka.dto.ReceiptResponseDTO;

import com.karan.experimentwithkafka.model.ProductItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
@Service
public class ReceiptPdfGenerator {

    public  byte[] generatePdf(ReceiptResponseDTO receipt, String receiptId) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph("Receipt", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Info
        document.add(new Paragraph("Order ID: " + receipt.getOrderId()));
        document.add(new Paragraph("User ID: " + receipt.getUserId()));
        document.add(new Paragraph("Billing Address: " + receipt.getBillingAddress()));
        document.add(new Paragraph("Payment Method: " + receipt.getPaymentMethod()));
        document.add(new Paragraph("Issued At: " + receipt.getIssuedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        document.add(new Paragraph(" "));

        // Table
        PdfPTable table = new PdfPTable(3);
        table.addCell("Product");
        table.addCell("Quantity");
        table.addCell("Price");

        for (ProductItem item : receipt.getProducts()) {
            table.addCell(item.getName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell("₹" + item.getPrice());
        }

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total Amount: ₹" + receipt.getTotalAmount()));


        document.close();
        return out.toByteArray();
    }
}
