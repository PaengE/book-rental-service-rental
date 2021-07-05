package com.my.rental.adaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.rental.config.KafkaProperties;
import com.my.rental.domain.Rental;
import com.my.rental.domain.event.UserIdCreated;
import com.my.rental.service.RentalService;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RentalConsumer {

    private final Logger log = LoggerFactory.getLogger(RentalConsumer.class);

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public static final String TOPIC = "topic_rental";

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    private final RentalService rentalService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public RentalConsumer(KafkaProperties kafkaProperties, RentalService rentalService) {
        this.kafkaProperties = kafkaProperties;
        this.rentalService = rentalService;
    }

    @PostConstruct
    public void start() {
        log.info("Kafka consumer starting ...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        kafkaConsumer.subscribe(Collections.singleton(TOPIC));
        log.info("Kafka consumer started");

        executorService.execute(
            () -> {
                try {
                    while (!closed.get()) {
                        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                        for (ConsumerRecord<String, String> record : records) {
                            log.info("Consumed message in {} : {}", TOPIC, record.value());
                            ObjectMapper objectMapper = new ObjectMapper();
                            UserIdCreated userIdCreated = objectMapper.readValue(record.value(), UserIdCreated.class);
                            Rental rental = rentalService.createRental(userIdCreated);
                        }
                    }
                    kafkaConsumer.commitSync();
                } catch (WakeupException e) {
                    if (!closed.get()) {
                        throw e;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    log.info("kafka consumer close");
                    kafkaConsumer.close();
                }
            }
        );
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
