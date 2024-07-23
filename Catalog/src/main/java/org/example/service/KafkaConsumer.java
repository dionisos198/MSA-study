package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.CatalogEntity;
import org.example.repository.CatalogRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    @KafkaListener(topics = "example-order-topic")
    public void processMessage(String kafkaMessage){
        log.info("Kafka Message: ===>" + kafkaMessage);

        Map<Object,Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try{
            map = mapper.readValue(kafkaMessage,new TypeReference<Map<Object,Object>>(){});
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        CatalogEntity catalogEntity = catalogRepository.findByProductId((String) map.get("productId")).orElseThrow(()->new NotFoundException("없음"));

        catalogEntity.setStock(catalogEntity.getStock()-(Integer) map.get("qty"));

        catalogRepository.save(catalogEntity);

    }

}
