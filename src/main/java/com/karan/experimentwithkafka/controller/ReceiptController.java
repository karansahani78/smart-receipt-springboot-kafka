package com.karan.experimentwithkafka.controller;

import com.karan.experimentwithkafka.dto.ReceiptRequestDTO;
import com.karan.experimentwithkafka.dto.ReceiptResponseDTO;
import com.karan.experimentwithkafka.model.Receipt;
import com.karan.experimentwithkafka.repository.ReceiptRepository;
import com.karan.experimentwithkafka.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;


    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;

    }

    @PostMapping
    public ResponseEntity<ReceiptResponseDTO> createReceipt(@RequestBody ReceiptRequestDTO receiptRequestDTO) {
        try {
            ReceiptResponseDTO receiptResponseDTO = receiptService.createReceipt(receiptRequestDTO);
            return ResponseEntity.ok(receiptResponseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Handle exceptions appropriately
        }
    }



}
