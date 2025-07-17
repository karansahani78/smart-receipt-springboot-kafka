package com.karan.experimentwithkafka.event;

import com.karan.experimentwithkafka.model.ProductItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class ReceiptEvent {
    private String receiptId;
    private String userId;
    private String orderId;
    private List<ProductItem> products;
    private double totalAmount;
    private String paymentMethod;
    private LocalDateTime issuedAt;

}
