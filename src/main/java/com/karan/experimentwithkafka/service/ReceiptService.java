package com.karan.experimentwithkafka.service;

import com.karan.experimentwithkafka.dto.ReceiptRequestDTO;
import com.karan.experimentwithkafka.dto.ReceiptResponseDTO;
import com.karan.experimentwithkafka.event.ReceiptEvent;
import com.karan.experimentwithkafka.repository.ReceiptRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReceiptService {

    private final PdfStorageService pdfStorageService;
    private final ReceiptRepository receiptRepository;
    private final QrCodeGenerator qrCodeGenerator;
    private final ReceiptPdfGenerator receiptPdfGenerator;
    private final KafkaTemplate<String, ReceiptEvent> kafkaTemplate;

    public ReceiptService(PdfStorageService pdfStorageService,
                          ReceiptRepository receiptRepository,
                          QrCodeGenerator qrCodeGenerator,
                          ReceiptPdfGenerator receiptPdfGenerator,
                          KafkaTemplate<String, ReceiptEvent> kafkaTemplate) {
        this.pdfStorageService = pdfStorageService;
        this.receiptRepository = receiptRepository;
        this.qrCodeGenerator = qrCodeGenerator;
        this.receiptPdfGenerator = receiptPdfGenerator;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public ReceiptResponseDTO createReceipt(ReceiptRequestDTO receiptRequestDTO) throws Exception {
        // Step 1: Generate Receipt ID
        String receiptId = UUID.randomUUID().toString();

        // 🔹 Calculate total amount
        double totalAmount = receiptRequestDTO.getProducts().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Step 2: Prepare the response DTO
        ReceiptResponseDTO receiptResponseDTO = new ReceiptResponseDTO();
        receiptResponseDTO.setReceiptId(receiptId);
        receiptResponseDTO.setUserId(receiptRequestDTO.getUserId());
        receiptResponseDTO.setOrderId(receiptRequestDTO.getOrderId());
        receiptResponseDTO.setProducts(receiptRequestDTO.getProducts());
        receiptResponseDTO.setTotalAmount(totalAmount); // 🔸 Set calculated total
        receiptResponseDTO.setPaymentMethod(receiptRequestDTO.getPaymentMethod());
        receiptResponseDTO.setBillingAddress(receiptRequestDTO.getBillingAddress());
        receiptResponseDTO.setIssuedAt(LocalDateTime.now());

        // Step 3: Generate PDF and save to disk
        byte[] pdfBytes = receiptPdfGenerator.generatePdf(receiptResponseDTO, receiptId);
        String pdfFilePath = pdfStorageService.savePdf(pdfBytes, receiptId + ".pdf");
        receiptResponseDTO.setPdfUrl(pdfFilePath);

        // Step 4: Generate QR code and save to disk
        byte[] qrCodeBytes = qrCodeGenerator.generateQRCode(pdfFilePath);
        String qrCodeFilePath = pdfStorageService.savePdf(qrCodeBytes, receiptId + ".png");
        receiptResponseDTO.setQrCodeUrl(qrCodeFilePath);

        // Step 5: Publish ReceiptEvent to Kafka
        ReceiptEvent event = new ReceiptEvent();
        event.setReceiptId(receiptId);
        event.setUserId(receiptRequestDTO.getUserId());
        event.setOrderId(receiptRequestDTO.getOrderId());
        event.setProducts(receiptRequestDTO.getProducts());
        event.setTotalAmount(totalAmount); // 🔸 Use calculated total
        event.setIssuedAt(receiptResponseDTO.getIssuedAt());

        kafkaTemplate.send("receipt-topic", event);

        return receiptResponseDTO;
    }
}