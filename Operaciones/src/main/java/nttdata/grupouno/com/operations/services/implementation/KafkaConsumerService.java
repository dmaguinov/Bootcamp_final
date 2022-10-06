package nttdata.grupouno.com.operations.services.implementation;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import nttdata.grupouno.com.operations.models.ClientWalletModel;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "Kafka_target", groupId = "group_json", containerFactory = "userKafkaListenerFactory")
    public ClientWalletModel consumeJsonTarjet(ClientWalletModel user) {
        System.out.println("Consumed JSON Message: " + user);
        return user;
    }
}
