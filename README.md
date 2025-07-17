# Smart Receipt SpringBoot Kafka

A comprehensive Java Spring Boot application for intelligent receipt generation with Kafka-based event streaming, automated PDF invoice creation, and QR code integration. This monolithic application provides complete billing functionality with file storage capabilities and RESTful APIs for seamless integration and receipt verification.

## 🚀 Features

- **Receipt Generation**: Automated receipt creation with PDF generation
- **Event-Driven Architecture**: Kafka-based event publishing for scalable processing
- **QR Code Integration**: Automatic QR code generation for receipt verification
- **PDF Invoice Creation**: Professional PDF receipts with detailed billing information
- **MongoDB Integration**: Persistent storage for receipt data
- **RESTful API**: Clean endpoints for easy integration
- **File Storage**: Local file system storage for PDFs and QR codes
- **Real-time Processing**: Asynchronous event handling with Kafka consumers

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.5.3
- **Database**: MongoDB
- **Message Broker**: Apache Kafka
- **PDF Generation**: OpenPDF (LibrePDF)
- **QR Code Generation**: ZXing (Zebra Crossing)
- **Build Tool**: Maven 3.9.10
- **Documentation**: Lombok for boilerplate reduction

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6+ (or use included Maven wrapper)
- MongoDB 4.4+
- Apache Kafka 2.8+
- ZooKeeper (for Kafka)

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/smart-receipt-springboot-kafka.git
cd smart-receipt-springboot-kafka
```

### 2. Start Required Services

#### MongoDB
```bash
# Using Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest

# Or install locally and start
mongod --dbpath /path/to/your/db
```

#### Kafka & ZooKeeper
```bash
# Start ZooKeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start Kafka
bin/kafka-server-start.sh config/server.properties

# Create required topic
bin/kafka-topics.sh --create --topic receipt-topic --bootstrap-server localhost:9092
```

### 3. Configure Application

Update `src/main/resources/application.properties` if needed:

```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/ReceiptDb

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=receipt-group
```

### 4. Build and Run

```bash
# Using Maven wrapper (recommended)
./mvnw spring-boot:run

# Or using installed Maven
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Create Receipt

**POST** `/api/receipts`

Creates a new receipt with PDF generation and QR code.

#### Request Body
```json
{
  "userId": "user123",
  "orderId": "order456",
  "products": [
    {
      "name": "Product A",
      "quantity": 2,
      "price": 25.99
    },
    {
      "name": "Product B",
      "quantity": 1,
      "price": 15.50
    }
  ],
  "totalAmount": 67.48,
  "paymentMethod": "Credit Card",
  "billingAddress": "123 Main St, City, State 12345"
}
```
![Request](https://github.com/user-attachments/assets/e9eba0da-9d8f-472e-96f7-097b6db5d530)


#### Response
```json
{
  "id": "generated-id",
  "receiptId": "uuid-receipt-id",
  "userId": "user123",
  "orderId": "order456",
  "products": [...],
  "pdfUrl": "/path/to/receipt.pdf",
  "qrCodeUrl": "/path/to/qrcode.png",
  "totalAmount": 67.48,
  "paymentMethod": "Credit Card",
  "issuedAt": "2024-01-15T10:30:00",
  "billingAddress": "123 Main St, City, State 12345"
}
```
![Response](https://github.com/user-attachments/assets/0247ccfb-62af-44d5-bb14-5c1b5c412263)

# Uploaded Pdf Sample
[5f97a9c5-0936-40db-8abb-49f92c0f1f3b.pdf](https://github.com/user-attachments/files/21303213/5f97a9c5-0936-40db-8abb-49f92c0f1f3b.pdf)

# Uploaded Qr Sample
<img width="250" height="250" alt="5f97a9c5-0936-40db-8abb-49f92c0f1f3b" src="https://github.com/user-attachments/assets/750f08a6-17fd-43ad-bff5-26514d203476" />

## 🏗️ Architecture Overview

### Components

1. **ReceiptController**: RESTful endpoints for receipt operations
2. **ReceiptService**: Core business logic for receipt processing
3. **PdfStorageService**: File storage management
4. **ReceiptPdfGenerator**: PDF creation using OpenPDF
5. **QrCodeGenerator**: QR code generation using ZXing
6. **KafkaProducer/Consumer**: Event streaming configuration

### Data Flow

1. **Receipt Request** → Controller receives request
2. **Processing** → Service calculates totals and generates receipt ID
3. **PDF Generation** → Creates professional PDF invoice
4. **QR Code Creation** → Generates QR code linking to receipt
5. **File Storage** → Saves PDF and QR code to local storage
6. **Event Publishing** → Publishes receipt event to Kafka
7. **Response** → Returns receipt details with file URLs

### Event-Driven Architecture

The application uses Kafka for asynchronous event processing:

- **Topic**: `receipt-topic`
- **Producer**: Publishes receipt events after creation
- **Consumer**: Configured for future processing extensions

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/karan/experimentwithkafka/
│   │   ├── config/          # Kafka configuration
│   │   ├── controller/      # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── event/          # Kafka event models
│   │   ├── model/          # Domain models
│   │   ├── repository/     # Data access layer
│   │   └── service/        # Business logic
│   └── resources/
│       └── application.properties
└── test/
    └── java/              # Test classes
```

## 🔒 File Storage

Files are stored locally in the user's Documents directory:
- **Location**: `~/Documents/uploads/`
- **PDF Files**: `{receiptId}.pdf`
- **QR Codes**: `{receiptId}.png`

## 🐛 Troubleshooting

### Common Issues

1. **Kafka Connection Issues**
   - Ensure Kafka and ZooKeeper are running
   - Check if the topic exists: `kafka-topics.sh --list --bootstrap-server localhost:9092`

2. **MongoDB Connection**
   - Verify MongoDB is running on port 27017
   - Check database permissions

3. **File Storage Permissions**
   - Ensure write permissions for `~/Documents/uploads/`
   - Check disk space availability

### Logs

Enable debug logging in `application.properties`:
```properties
logging.level.com.karan=DEBUG
```

## 🧪 Testing

Run the test suite:
```bash
./mvnw test
```

Test the API using curl:
```bash
curl -X POST http://localhost:8080/api/receipts \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "test-user",
    "orderId": "test-order",
    "products": [{"name": "Test Product", "quantity": 1, "price": 10.00}],
    "totalAmount": 10.00,
    "paymentMethod": "Cash",
    "billingAddress": "Test Address"
  }'
```

## 🚀 Future Enhancements

- [ ] Cloud storage integration (AWS S3, Google Cloud Storage)
- [ ] Email notification service
- [ ] Receipt template customization
- [ ] Analytics dashboard
- [ ] Multi-tenant support
- [ ] Receipt search and filtering
- [ ] Digital signature integration
- [ ] Mobile app integration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue on GitHub
- Email: karansahani723@gamil.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Apache Kafka for reliable event streaming
- OpenPDF for PDF generation capabilities
- ZXing for QR code generation
- MongoDB for flexible data storage

---

**Built with ❤️ using Spring Boot and Kafka**
