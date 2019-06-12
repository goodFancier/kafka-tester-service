package com.kafka_tester.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka_tester.Model.DateAudit;
import com.kafka_tester.Model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KafkaServiceImpl implements KafkaService {

    private final KafkaTemplate<Long, User> kafkaUserTemplate;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Autowired
    public KafkaServiceImpl(KafkaTemplate<Long, User> kafkaUserTemplate,
                               ObjectMapper objectMapper) {
        this.kafkaUserTemplate = kafkaUserTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    @Override
    public void produce() {
        User dto = new User();
        logger.info("<= sending {}", writeValueAsString(dto));
        kafkaUserTemplate.send("server.user", dto);
    }

    private String writeValueAsString(User dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Writing value to JSON failed: " + dto.toString());
        }
    }
}
