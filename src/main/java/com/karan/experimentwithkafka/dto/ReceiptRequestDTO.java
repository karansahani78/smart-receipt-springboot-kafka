package com.karan.experimentwithkafka.dto;

import com.karan.experimentwithkafka.model.ProductItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptRequestDTO {
        private String userId;
        private String orderId;
        private List<ProductItem> products;

        private double totalAmount;
        private String paymentMethod;
        private LocalDateTime issuedAt;
        private String billingAddress;



    }

