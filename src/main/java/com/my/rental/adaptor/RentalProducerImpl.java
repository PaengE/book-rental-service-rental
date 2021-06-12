package com.my.rental.adaptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.rental.config.KafkaProperties;
import com.my.rental.domain.event.CatalogChanged;
import com.my.rental.domain.event.PointChanged;
import com.my.rental.domain.event.StockChanged;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RentalProducerImpl implements RentalProducer {

    private final Logger log = LoggerFactory.getLogger(RentalProducerImpl.class);

    // 토픽명
    private static final String TOPIC_BOOK = "topic_book";
    private static final String TOPIC_CATALOG = "topic_catalog";
    private static final String TOPIC_POINT = "topic_point";

    private final KafkaProperties kafkaProperties;
    private KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RentalProducerImpl(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        log.info("Kafka producer initializing...");
        this.producer = new KafkaProducer<String, String>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    // kafka 메시지 발행 - 책 상태 변경 - book service
    @Override
    public void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException {
        StockChanged stockChanged = new StockChanged(bookId, bookStatus);
        String message = objectMapper.writeValueAsString(stockChanged);
        producer.send(new ProducerRecord<>(TOPIC_BOOK, message)).get();
    }

    // kafka 메시지 발행 - 포인트 적립  - user service(gateway)
    @Override
    public void savePoints(Long userId, int pointPerBooks) throws ExecutionException, InterruptedException, JsonProcessingException {
        PointChanged pointChanged = new PointChanged(userId, pointPerBooks);
        String message = objectMapper.writeValueAsString(pointChanged);
        producer.send(new ProducerRecord<>(TOPIC_POINT, message)).get();
    }

    // kafka 메시지 발행 - 책 상태 변경  - bookCatalog service
    @Override
    public void updateBookCatalogStatus(Long bookId, String eventType)
        throws InterruptedException, ExecutionException, JsonProcessingException {
        CatalogChanged catalogChanged = new CatalogChanged();
        catalogChanged.setBookId(bookId);
        catalogChanged.setEventType(eventType);
        String message = objectMapper.writeValueAsString(catalogChanged);
        producer.send(new ProducerRecord<>(TOPIC_CATALOG, message)).get();
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown Kafka producer");
        producer.close();
    }
}
