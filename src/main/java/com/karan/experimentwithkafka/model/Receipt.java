package com.karan.experimentwithkafka.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "receipts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt {
    @Id
    private String id;
    private String userId;
    private String orderId;
    private List<ProductItem> products;

    private double totalAmount;
    private String paymentMethod;
    private LocalDateTime issuedAt;
    private String billingAddress;
;

}
