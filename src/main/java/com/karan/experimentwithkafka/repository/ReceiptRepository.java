package com.karan.experimentwithkafka.repository;

import com.karan.experimentwithkafka.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends MongoRepository<Receipt, String> {
}
